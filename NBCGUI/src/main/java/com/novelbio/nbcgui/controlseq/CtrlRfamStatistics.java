package com.novelbio.nbcgui.controlseq;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.novelbio.analysis.IntCmdSoft;
import com.novelbio.analysis.seq.GeneExpTable;
import com.novelbio.analysis.seq.fasta.SeqHash;
import com.novelbio.analysis.seq.fastq.FastQ;
import com.novelbio.analysis.seq.fastq.FastQRecord;
import com.novelbio.analysis.seq.mapping.MapBowtie;
import com.novelbio.analysis.seq.mirna.RfamStatistic;
import com.novelbio.analysis.seq.rnaseq.RPKMcomput.EnumExpression;
import com.novelbio.analysis.seq.sam.SamFile;
import com.novelbio.base.FoldeCreate;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.database.domain.information.SoftWareInfo;
import com.novelbio.database.domain.information.SoftWareInfo.SoftWare;
import com.novelbio.database.model.species.Species;
import com.novelbio.generalConf.PathDetailNBC;
import com.novelbio.generalConf.TitleFormatNBC;
import com.novelbio.nbcReport.Params.EnumReport;

/** 检测RNAseq样本的rrna污染情况以及ncRNA的情况 */
public class CtrlRfamStatistics implements IntCmdSoft {
	private static final Logger logger = Logger.getLogger(CtrlRfamStatistics.class);
	
	SoftWareInfo softWareInfo = new SoftWareInfo(SoftWare.bowtie2);
	GeneExpTable expRfamID = new GeneExpTable(TitleFormatNBC.RfamID);
	GeneExpTable expRfamClass = new GeneExpTable(TitleFormatNBC.RfamClass);
	int threadNum = 5;
	RfamStatistic rfamStatistic = new RfamStatistic();
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
		List<String> lsRfamNameRaw = SeqHash.getLsSeqName(rfamFile);
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
		this.outPath = FoldeCreate.createAndInFold(outPath, EnumReport.RfamStatistics.getResultFolder());
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
			String out = FileOperate.changeFileSuffix(fastq, "", "bam");
			expRfamClass.setCurrentCondition(prefix2Fq[0]);
			expRfamID.setCurrentCondition(prefix2Fq[0]);
			out = mapping(fastq, out);
			expRfamClass.addGeneExp(rfamStatistic.getMapRfamClass2Counts());
			expRfamID.addGeneExp(rfamStatistic.getMapRfamID2Counts());
			expRfamClass.addAllReads(rfamStatistic.getAllReadsNum());
			expRfamID.addAllReads(rfamStatistic.getAllReadsNum());
			
			expRfamID.writeFile(false, outPath + TitleFormatNBC.Samples.toString() + prefix2Fq[0] + "_RfamID.txt", EnumExpression.Ratio);
			expRfamClass.writeFile(false, outPath + TitleFormatNBC.Samples.toString() + prefix2Fq[0] + "_RfamClass.txt", EnumExpression.Ratio);
			lsFileTobeDelete.add(fastq);
			lsFileTobeDelete.add(out);
		}
		expRfamID.writeFile(true, outPath + "Rfam_ID_Statistics.txt", EnumExpression.Ratio);
		expRfamClass.writeFile(true, outPath + "Rfam_Class_Statistics.txt.txt", EnumExpression.Ratio);
	}
	
	private String getSampleFq(String fastqFile) {
		String samplingFq = outPath + TitleFormatNBC.TmpMapping.toString() + FileOperate.getSepPath() + FileOperate.getFileName(fastqFile);
		FileOperate.createFolders(FileOperate.getPathName(samplingFq));
		samplingFq = FileOperate.changeFileSuffix(samplingFq,  "_sampling", "fastq|fq", "fq.gz");
		if (isUseOldResult && FileOperate.isFileExistAndBigThanSize(samplingFq, 0)) {
			return samplingFq;
		}
		FastQ fastQSampling = new FastQ(samplingFq, true);
		FastQ fastQ = new FastQ(fastqFile);
		int i = 0;
		for (FastQRecord fastQRecord : fastQ.readlines()) {
			if (i++ > testReadsNum) {
				break;
			}
			fastQSampling.writeFastQRecord(fastQRecord);
		}
		fastQ.close();
		fastQSampling.close();
		return samplingFq;
	}
	
	private String mapping(String fastqFile, String outFile) {
		MapBowtie mapBowtie = new MapBowtie();
		mapBowtie.setFqFile(new FastQ(fastqFile), null);
		mapBowtie.setOutFileName(outFile);
		mapBowtie.setChrIndex(rfamFile);
		mapBowtie.setExePath(softWareInfo.getExePath());
		mapBowtie.setGapLength(3);
		mapBowtie.setThreadNum(threadNum);
		rfamStatistic.initial();
		mapBowtie.addAlignmentRecorder(rfamStatistic);
		if (!isUseOldResult || !FileOperate.isFileExistAndBigThanSize(mapBowtie.getOutNameCope(), 0)) {
			mapBowtie.mapReads();
		}
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
			FileOperate.DeleteFileFolder(tmpFile);
		}
	}
	
	@Override
	public List<String> getCmdExeStr() {
		return lsCmd;
	}
	
}
