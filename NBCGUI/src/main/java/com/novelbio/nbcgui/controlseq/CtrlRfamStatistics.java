package com.novelbio.nbcgui.controlseq;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.novelbio.analysis.IntCmdSoft;
import com.novelbio.analysis.seq.GeneExpTable;
import com.novelbio.analysis.seq.fasta.SeqHash;
import com.novelbio.analysis.seq.fastq.FastQ;
import com.novelbio.analysis.seq.fastq.FastQRecord;
import com.novelbio.analysis.seq.mapping.MapBwaAln;
import com.novelbio.analysis.seq.mirna.RfamStatistic;
import com.novelbio.analysis.seq.rnaseq.RPKMcomput.EnumExpression;
import com.novelbio.analysis.seq.sam.SamFile;
import com.novelbio.analysis.seq.sam.SamFileStatistics;
import com.novelbio.analysis.seq.sam.SamMapRate;
import com.novelbio.base.dataOperate.TxtReadandWrite;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.database.domain.species.Species;
import com.novelbio.generalconf.PathDetailNBC;
import com.novelbio.generalconf.TitleFormatNBC;

/** 检测RNAseq样本的rrna污染情况以及ncRNA的情况 */
@Component
@Scope("prototype")
public class CtrlRfamStatistics implements IntCmdSoft {
	private static final Logger logger = Logger.getLogger(CtrlRfamStatistics.class);
	
	GeneExpTable expRfamID = new GeneExpTable(TitleFormatNBC.RfamID);
	GeneExpTable expRfamClass = new GeneExpTable(TitleFormatNBC.RfamClass);
	int threadNum = 5;
	RfamStatistic rfamStatistic = new RfamStatistic();
	SamMapRate samMapRate = new SamMapRate();
	String rfamFile;
	boolean isUseOldResult = true;
	
	List<String[]> lsPrefix2Fq;
	String outPath;
	
	List<String> lsFileTobeDelete = new ArrayList<>();
	
	List<String> lsCmd = new ArrayList<>();
	/** 只看前1百万条序列 */
	int testReadsNum = 1000000;
	
	private void initial() {
		lsCmd.clear();
		Species species = new Species(9606);
		rfamFile = species.getRfamFile(false);
		List<String> lsRfamNameRaw = SeqHash.getLsSeqNameFromFasta(rfamFile);
		rfamStatistic.readRfamTab(PathDetailNBC.getRfamTab());
		expRfamID.addLsGeneName(rfamStatistic.getLsRfamID(lsRfamNameRaw));
		expRfamID.addAnnotationArray(rfamStatistic.getMapRfamID2Info());
		expRfamID.addLsTitle(RfamStatistic.getLsTitleRfamIDAnno());
		expRfamClass.addLsGeneName(rfamStatistic.getLsRfamClass(lsRfamNameRaw));
	}
	
	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}
	public void setOutPath(String outPath) {
		this.outPath = outPath;
	}
	/** 获取每个样本的多少条reads，默认是仅检测前一百万条reads */
	public void setTestReadsNum(int testReadsNum) {
		this.testReadsNum = testReadsNum;
	}
	public void setLsFastqFiles(List<String[]> lsPrefix2Fq) {
		this.lsPrefix2Fq = lsPrefix2Fq;
	}
	
	public void calculate() {
		initial();
		lsFileTobeDelete.clear();
		for (String[] prefix2Fq : lsPrefix2Fq) {
			String fastq = getSampleFq(prefix2Fq[1]);
			String outTmp = outPath + TitleFormatNBC.TmpMapping.toString() + FileOperate.getSepPath();
			String out = outTmp + FileOperate.changeFileSuffix(FileOperate.getFileName(fastq), "", "bam");
			expRfamClass.setCurrentCondition(prefix2Fq[0]);
			expRfamID.setCurrentCondition(prefix2Fq[0]);
			out = mapping(fastq, out);
			expRfamClass.addGeneExp(rfamStatistic.getMapRfamClass2Counts());
			expRfamID.addGeneExp(rfamStatistic.getMapRfamID2Counts());
			expRfamClass.addAllReads(rfamStatistic.getAllReadsNum());
			expRfamID.addAllReads(rfamStatistic.getAllReadsNum());
			
			expRfamID.writeFile(false, outTmp + TitleFormatNBC.Samples.toString() + prefix2Fq[0] + ".RfamID.txt", EnumExpression.Ratio);
			expRfamClass.writeFile(false, outTmp + TitleFormatNBC.Samples.toString() + prefix2Fq[0] + ".RfamClass.txt", EnumExpression.Ratio);
			if (testReadsNum > 0) {
				lsFileTobeDelete.add(fastq);
			}
			lsFileTobeDelete.add(out);
		}
		List<String[]> lsResult = samMapRate.getLsResult();
		TxtReadandWrite txtWrite = new TxtReadandWrite(outPath + "Rfam.Map.Statistics.txt", true);
		txtWrite.ExcelWrite(lsResult);
		txtWrite.close();
		expRfamID.writeFile(true, outPath + "Rfam.ID.Statistics.txt", EnumExpression.Ratio);
		expRfamClass.writeFile(true, outPath + "Rfam.Class.Statistics.txt.txt", EnumExpression.Ratio);
	}
	
	private String getSampleFq(String fastqFile) {
		String samplingFq = outPath + TitleFormatNBC.TmpMapping.toString() + FileOperate.getSepPath() + FileOperate.getFileName(fastqFile);
		FileOperate.createFolders(FileOperate.getPathName(samplingFq));
		samplingFq = FileOperate.changeFileSuffix(samplingFq,  ".sampling", "fastq|fq", "fq.gz");
		if (isUseOldResult && FileOperate.isFileExistAndBigThanSize(samplingFq, 0)) {
			return samplingFq;
		}
		FastQ fastQSampling = null;
		FastQ fastQ = new FastQ(fastqFile);
		int i = 0;
		if (testReadsNum <= 0) {
			samplingFq = fastqFile;
		} else {
			fastQSampling = new FastQ(samplingFq, true);
			for (FastQRecord fastQRecord : fastQ.readlines()) {
				if (i++ > testReadsNum) {
					break;
				}
				fastQSampling.writeFastQRecord(fastQRecord);
			}
			fastQ.close();
			fastQSampling.close();
		}
		return samplingFq;
	}
	
	private String mapping(String fastqFile, String outFile) {
		SamFileStatistics samFileStatistics = new SamFileStatistics(FileOperate.getFileNameSep(fastqFile)[0]);
		MapBwaAln mapBowtie = new MapBwaAln();
		mapBowtie.setFqFile(new FastQ(fastqFile), null);
		mapBowtie.setOutFileName(outFile);
		mapBowtie.setChrIndex(rfamFile);
		mapBowtie.setGapLength(3);
		mapBowtie.setThreadNum(threadNum);
		rfamStatistic.initial();
		mapBowtie.addAlignmentRecorder(rfamStatistic);
		mapBowtie.addAlignmentRecorder(samFileStatistics);

		if (!isUseOldResult || !FileOperate.isFileExistAndBigThanSize(mapBowtie.getOutNameCope(), 0)) {
			mapBowtie.mapReads();
		}
		samMapRate.addMapInfo("Rfam", samFileStatistics);
		SamFile samFile = new SamFile(mapBowtie.getOutNameCope());
		rfamStatistic.setSamFile(samFile);
		rfamStatistic.countRfamBam();
		logger.info("finish mapping miRNA");
		lsCmd.addAll(mapBowtie.getCmdExeStr());
		return samFile.getFileName();
	}
	
	/** 删除临时文件 */
	public void clean() {
		for (String tmpFile : lsFileTobeDelete) {
			FileOperate.deleteFileFolder(tmpFile);
		}
	}
	
	@Override
	public List<String> getCmdExeStr() {
		return lsCmd;
	}
	
}
