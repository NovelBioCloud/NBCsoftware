package com.novelbio.nbcgui.controlseq;

import java.util.ArrayList;
import java.util.List;

import com.novelbio.analysis.seq.genome.GffChrAbs;
import com.novelbio.analysis.seq.genome.gffOperate.GffHashGene;
import com.novelbio.analysis.seq.genome.gffOperate.GffType;
import com.novelbio.analysis.seq.mapping.StrandSpecific;
import com.novelbio.analysis.seq.rnaseq.CuffMerge;
import com.novelbio.analysis.seq.rnaseq.CufflinksGTF;
import com.novelbio.analysis.seq.rnaseq.GffHashMerge;
import com.novelbio.analysis.seq.rnaseq.TranscriptomStatistics;
import com.novelbio.base.dataOperate.TxtReadandWrite;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.database.domain.information.SoftWareInfo;
import com.novelbio.database.domain.information.SoftWareInfo.SoftWare;

public class CtrlCufflinksTranscriptome {
	boolean reconstructTranscriptome = false;
	SoftWareInfo softWareInfo = new SoftWareInfo(SoftWare.cufflinks);
	CufflinksGTF cufflinksGTF = new CufflinksGTF();

	GffChrAbs gffChrAbs;
	GffHashGene gffHashGene;
	String outPrefix;
	int thread = 4;
	String gtfRefFile;
	
	String outGtf;
	String outStatistics;
	
	
	
	/** 输入已有的物种信息<br>
	 * 和{@link #setGTFfile(String)} 两者选一
	 * @param gffChrAbs
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
	private void setExepath() {
		cufflinksGTF.setExePath(softWareInfo.getExePath(), gffChrAbs.getSpecies().getChromSeq());
	}
	
	public void setOutPathPrefix(String outPathPrefix) {
		cufflinksGTF.setOutPathPrefix(outPathPrefix);
		this.outPrefix = outPathPrefix;
	}
	public void run() {
		gffHashGene = null;
		setExepath();
		outGtf = outPrefix + "novelTranscriptom.gtf";
		outStatistics =  outPrefix + "novelTranscriptomStatistics.txt";
		cufflinksGTF.setGtfFile(getGtfFileName());
		cufflinksGTF.setIntronLen(getIntronSmall2Big());
		cufflinksGTF.runCufflinks();
		List<String> lsResultGTF = cufflinksGTF.getLsCufflinksResult();
		if (lsResultGTF.size() > 1) {
			CuffMerge cuffMerge = new CuffMerge();
			cuffMerge.setExePath(softWareInfo.getExePath());
			cuffMerge.setLsGtfTobeMerged(lsResultGTF);
			cuffMerge.setRefGtf(cufflinksGTF.getGtfReffile());
			cuffMerge.setOutputPrefix(outGtf);
			try { cuffMerge.setRefChrFa(gffChrAbs.getSpecies().getChromSeq()); } catch (Exception e) { }
			cuffMerge.setThreadNum(thread);
			outGtf = cuffMerge.runCuffmerge();
		} else if (lsResultGTF.size() == 1) {
			outGtf = lsResultGTF.get(0);
		}
		
		if (!reconstructTranscriptome) {
			return;
		}
		
		GffHashMerge gffHashMerge = new GffHashMerge();
		gffHashMerge.setSpecies(gffChrAbs.getSpecies());
		gffHashMerge.setGffHashGeneRef(getGffHashRef());
		gffHashMerge.addGffHashGene(new GffHashGene(GffType.GTF, outGtf));
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
		GffHashGene gffHashGene = getGffHashRef();
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
		} else {
			gtfFile = "";
		}
		return gtfFile;
	}
	
	private GffHashGene getGffHashRef() {
		if (gffHashGene != null) {
			return gffHashGene;
		}
		if (FileOperate.isFileExistAndBigThanSize(gtfRefFile, 10)) {
			gffHashGene = new GffHashGene(GffType.GTF, gtfRefFile);
		} else if (gffChrAbs != null && gffChrAbs.getGffHashGene() != null) {
			gffHashGene = gffChrAbs.getGffHashGene();
		}
		return gffHashGene;
	}
}
