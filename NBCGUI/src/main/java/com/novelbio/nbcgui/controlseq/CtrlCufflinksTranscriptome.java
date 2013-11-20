package com.novelbio.nbcgui.controlseq;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.HashMultimap;
import com.novelbio.analysis.seq.fasta.SeqHash;
import com.novelbio.analysis.seq.genome.GffChrAbs;
import com.novelbio.analysis.seq.genome.GffHashModifyORF;
import com.novelbio.analysis.seq.genome.gffOperate.GffHashGene;
import com.novelbio.analysis.seq.genome.gffOperate.GffType;
import com.novelbio.analysis.seq.mapping.StrandSpecific;
import com.novelbio.analysis.seq.rnaseq.CuffMerge;
import com.novelbio.analysis.seq.rnaseq.CufflinksGTF;
import com.novelbio.analysis.seq.rnaseq.GffHashMerge;
import com.novelbio.analysis.seq.rnaseq.TranscriptomStatistics;
import com.novelbio.base.FoldeCreate;
import com.novelbio.base.dataOperate.TxtReadandWrite;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.database.domain.information.SoftWareInfo;
import com.novelbio.database.domain.information.SoftWareInfo.SoftWare;
import com.novelbio.nbcReport.Params.EnumReport;

public class CtrlCufflinksTranscriptome {
	boolean reconstructTranscriptome = false;
	SoftWareInfo softWareInfo = new SoftWareInfo(SoftWare.cufflinks);
	CufflinksGTF cufflinksGTF = new CufflinksGTF();
	GffChrAbs gffChrAbs = new GffChrAbs();
	String outPrefix;
	int thread = 4;
	String gtfRefFile;
	String chrSeq;
	String outGtf;
	String outStatistics;
	
	/** 输入已有的物种信息<br>
	 * 和{@link #setGTFfile(String)} 两者选一
	 * @param gffChrAbs 注意结束后会关闭流
	 */
	public void setGffChrAbs(GffChrAbs gffChrAbs) {
		this.gffChrAbs = gffChrAbs;
	}
	/** 用额外的GTF辅助重建转录本<br>
	 * 和{@link #setGffChrAbs(gffChrAbs)} 两者选一
	 * @param gtfFile
	 */
	public void setGTFfile(String gtfFile) {
		this.gtfRefFile = gtfFile;
	}
	public void setLsBamFile2Prefix(ArrayList<String[]> lsBamFile2Prefix) {
		cufflinksGTF.setBam(lsBamFile2Prefix);
	}
	public void setThreadNum(int threadNum) {
		this.thread = threadNum;
		cufflinksGTF.setThreadNum(threadNum);
	}
	public void setStrandSpecifictype(StrandSpecific strandSpecific) {
		cufflinksGTF.setStrandSpecifictype(strandSpecific);
	}
	/** 是否重建转录本 */
	public void setReconstructTranscriptome(boolean reconstructTranscriptome) {
		this.reconstructTranscriptome = reconstructTranscriptome;
	}
	/** 是否根据prefix合并bam文件，然后再做cufflinks分析 默认false */
	public void setIsMergeBamByPrefix(boolean isMergeBamByPrefix) {
		cufflinksGTF.setIsMergeBamByPrefix(isMergeBamByPrefix);
	}
	/** 在junction 的一头搭上exon的最短比例 0-1之间，默认0.09 */
	public void setSmallAnchorFraction(double anchorLength) {
		cufflinksGTF.setSmallAnchorFraction(anchorLength);
	}
	/** 是否用上四分之一位点标准化 */
	public void setUpQuartileNormalized(boolean isUpQuartileNormalized) {
		cufflinksGTF.setUpQuartileNormalized(isUpQuartileNormalized);
	}

	public void setOutPathPrefix(String outPathPrefix) {
		this.outPrefix = FoldeCreate.createAndInFold(outPathPrefix, EnumReport.ReconstructTranscriptome.getResultFolder());
		String tmpCufflinksResult = FileOperate.getPathName(outPrefix) + "tmpCufflinks/";
		FileOperate.createFolders(tmpCufflinksResult);
		cufflinksGTF.setOutPathPrefix(tmpCufflinksResult);
	}
	
	public void run() {
		reconstruct();
		close();
	}
	
	public void reconstruct() {
		setGffHashRef();
		setSeqHash();

		cufflinksGTF.setExePath(softWareInfo.getExePath(), getChromFaFile());
		cufflinksGTF.setGtfFile(getGtfFileName(), reconstructTranscriptome);
		cufflinksGTF.setIntronLen(getIntronSmall2Big());
		cufflinksGTF.setSkipErrorMode(true);
		cufflinksGTF.runCufflinks();
		List<String> lsResultGTF = cufflinksGTF.getLsCufflinksResult();
		if (!reconstructTranscriptome) {
			return;
		}
		
		outGtf = outPrefix + "novelTranscriptom.gtf";
		outStatistics =  outPrefix + "novelTranscriptomStatistics.txt";
		String resultGtf = null;
		if (lsResultGTF.size() > 1) {
			CuffMerge cuffMerge = new CuffMerge();
			cuffMerge.setExePath(softWareInfo.getExePath());
			cuffMerge.setLsGtfTobeMerged(lsResultGTF);
			cuffMerge.setRefGtf(cufflinksGTF.getGtfReffile());
			cuffMerge.setOutputPrefix(outGtf);
			try { cuffMerge.setRefChrFa(gffChrAbs.getSpecies().getChromSeq()); } catch (Exception e) { }
			cuffMerge.setThreadNum(thread);
			resultGtf = cuffMerge.runCuffmerge();
			
		} else if (lsResultGTF.size() == 1) {
			resultGtf = lsResultGTF.get(0);
		}
		
		GffHashGene gffHashGeneThis = new GffHashGene(GffType.GTF, resultGtf);
		GffHashModifyORF gffHashModifyORF = new GffHashModifyORF();
		gffHashModifyORF.setGffHashGeneRaw(gffHashGeneThis);
		gffHashModifyORF.setGffHashGeneRef(gffChrAbs.getGffHashGene());
		gffHashModifyORF.setRenameGene(true);
		gffHashModifyORF.setRenameIso(true);//TODO 可以考虑不换iso的名字
		gffHashModifyORF.modifyGff();
		
		List<String> lsChrName = (gffChrAbs.getSeqHash() != null)? gffChrAbs.getSeqHash().getLsSeqName() : null;
		gffHashGeneThis.writeToGTF(lsChrName, outGtf);
		
		GffHashMerge gffHashMerge = new GffHashMerge();
		gffHashMerge.setSeqHash(gffChrAbs.getSeqHash());
		gffHashMerge.setGffHashGeneRef(gffChrAbs.getGffHashGene());
		gffHashMerge.addGffHashGene(gffHashGeneThis);
		TranscriptomStatistics transcriptomStatistics = gffHashMerge.getStatisticsCompareGff();
		TxtReadandWrite txtOut = new TxtReadandWrite(outStatistics, true);
		txtOut.ExcelWrite(transcriptomStatistics.getStatisticsResult());
		txtOut.close();
	}

	/** 根据gff文件，返回最小和最大intron的长度
	 * 如果gff不存在，则返回null
	 * @return
	 */
	private int[] getIntronSmall2Big() {
		GffHashGene gffHashGene = gffChrAbs.getGffHashGene();
		if (gffHashGene == null) {
			return null;
		}
		List<Integer> lsIntronSortedS2M = gffHashGene.getLsIntronSortedS2M();
		int intronLenMin = lsIntronSortedS2M.get(50);
		int intronLenMax = lsIntronSortedS2M.get(lsIntronSortedS2M.size() - 10);
		return new int[]{intronLenMin, intronLenMax};
	}
	
	private String getGtfFileName() {
		String gtfFile = null;
		if (FileOperate.isFileExistAndBigThanSize(gtfRefFile, 10)) {
			gtfFile = gtfRefFile;
		} else if (gffChrAbs != null && gffChrAbs.getGffHashGene() != null) {
			gtfFile = gffChrAbs.getGtfFile();
		}
		return gtfFile;
	}
	private String getChromFaFile() {
		String chromFa = null;
		if (FileOperate.isFileExistAndBigThanSize(chrSeq, 10)) {
			chromFa = chrSeq;
		} else if (gffChrAbs != null && gffChrAbs.getSeqHash() != null) {
			chromFa = gffChrAbs.getSeqHash().getChrFile();
		}
		return chromFa;		
	}
	
	private void setGffHashRef() {
		if (FileOperate.isFileExistAndBigThanSize(gtfRefFile, 10)) {
			GffHashGene gffHashGene = new GffHashGene(GffType.GTF, gtfRefFile);
			gffChrAbs.setGffHash(gffHashGene);
		}
	}
	
	private void setSeqHash() {
		if (FileOperate.isFileExistAndBigThanSize(chrSeq, 10)) {
			SeqHash seqHash = new SeqHash(chrSeq, null);
			if (gffChrAbs.getSeqHash() != null) {
				gffChrAbs.getSeqHash().close();
				gffChrAbs.setSeqHash(seqHash);
			}
		}
	}
	/** 关闭gffChrAbs中的seqhash信息 */
	private void close() {
		gffChrAbs.close();
	}
	
	/** 返回预测的文件名
	 * @param isFilter 是否过滤，如果不过滤就直接合并
	 * @return
	 */
	public static HashMultimap<String, String> getPredictMapPrefix2FilteredFQ(String outPrefix, boolean isReconstruct) {
		HashMultimap<String, String> mapPrefix2File = HashMultimap.create();
		if (!isReconstruct) return mapPrefix2File;
			
		String outFoldPrefix = FoldeCreate.getInFold(outPrefix, EnumReport.ReconstructTranscriptome.getResultFolder());
		String outGtf = outFoldPrefix + "novelTranscriptom.gtf";
		String outStatistics = outFoldPrefix + "novelTranscriptomStatistics.txt";
		mapPrefix2File.put("reconstruct_gtf", outGtf);
		mapPrefix2File.put("reconstruct_statistics", outStatistics);
		return mapPrefix2File;
	}

}
