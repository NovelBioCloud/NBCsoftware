package com.novelbio.nbcgui.controlseq;

import java.util.ArrayList;
import java.util.List;

import org.apache.avalon.framework.logger.Loggable;

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
	String outPrefix;
	int thread = 4;
	String gtfFile;

	public void setGffChrAbs(GffChrAbs gffChrAbs) {
		this.gffChrAbs = gffChrAbs;
		cufflinksGTF.setGffChrAbs(gffChrAbs);
	}
	/** 用额外的GTF辅助重建转录本 */
	public void setGTFfile(String gtfFile) {
		this.gtfFile = gtfFile;
		cufflinksGTF.setGtfFile(gtfFile);
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
		setExepath();
		String outGtf = outPrefix + "novelTranscriptom.gtf";
		String outStatistics =  outPrefix + "novelTranscriptomStatistics.txt";
		String outCufflinksGTF = outPrefix + "cufflinksMerged.gtf";
		cufflinksGTF.runCufflinks();
		List<String> lsResultGTF = cufflinksGTF.getLsCufflinksResult();
		if (lsResultGTF.size() > 1) {
			CuffMerge cuffMerge = new CuffMerge();
			cuffMerge.setExePath(softWareInfo.getExePath());
			cuffMerge.setLsGtfTobeMerged(lsResultGTF);
			cuffMerge.setRefGtf(cufflinksGTF.getGtfReffile());
			cuffMerge.setOutputPrefix(outCufflinksGTF);
			try { cuffMerge.setRefChrFa(gffChrAbs.getSpecies().getRefseqFile()); } catch (Exception e) { }
			cuffMerge.setThreadNum(thread);
			outCufflinksGTF = cuffMerge.runCuffmerge();
		} else if (lsResultGTF.size() == 1) {
			outCufflinksGTF = lsResultGTF.get(0);
		}
		
		if (!reconstructTranscriptome) {
			return;
		}
		
		GffHashMerge gffHashMerge = new GffHashMerge();
		gffHashMerge.setSpecies(gffChrAbs.getSpecies());
		GffHashGene gffHashGeneRef = getGffHashRef();
		gffHashMerge.setGffHashGeneRef(gffHashGeneRef);
		gffHashMerge.addGffHashGene(new GffHashGene(GffType.GTF, outCufflinksGTF));
		GffHashGene gffHashGene = gffHashMerge.getGffHashGeneModifyResult();
		gffHashGene.removeDuplicateIso();
		gffHashGene.writeToGTF(outGtf, "novelbio");

		
		gffHashMerge = new GffHashMerge();
		gffHashMerge.setSpecies(gffChrAbs.getSpecies());
		gffHashMerge.setGffHashGeneRef(gffHashGeneRef);
		gffHashMerge.addGffHashGene(new GffHashGene(GffType.GTF, outGtf));
		TranscriptomStatistics transcriptomStatistics = gffHashMerge.getStatisticsCompareGff();
		TxtReadandWrite txtOut = new TxtReadandWrite(outStatistics, true);
		txtOut.ExcelWrite(transcriptomStatistics.getStatisticsResult());
		txtOut.close();
	}
	
	private GffHashGene getGffHashRef() {
		GffHashGene gffHashGene = null;
		if (FileOperate.isFileExistAndBigThanSize(gtfFile, 10)) {
			gffHashGene = new GffHashGene(GffType.GTF, gtfFile);
		} else if (gffChrAbs != null) {
			gffHashGene = gffChrAbs.getGffHashGene();
		}
		return gffHashGene;
	}
}
