package com.novelbio.nbcgui.controlseq;

import java.util.List;
import java.util.Map;

import com.novelbio.GuiAnnoInfo;
import com.novelbio.base.multithread.RunGetInfo;
import com.novelbio.bioinfo.base.FormatSeq;
import com.novelbio.bioinfo.gff.GffHashGene;
import com.novelbio.bioinfo.gff.GffHashGeneAbs;
import com.novelbio.bioinfo.sam.SamFileStatistics;
import com.novelbio.bioinfo.sam.StrandSpecific;
import com.novelbio.database.domain.species.Species;
import com.novelbio.nbcgui.GUI.GuiSamStatistics;

public interface CtrlSamPPKMint extends RunGetInfo<GuiAnnoInfo>, Runnable {
	/**
	 * 必须设定{@link #setFormatSeq(FormatSeq)} 方法
	 * 和{@link #setResultPrefix(String)} 方法
	 * @param guiPeakStatistics
	 */
	public void setGUI(GuiSamStatistics guiPeakStatistics);
	public void setSpecies(Species species);
	public void setGffHash(GffHashGene gffHashGene);
	public void setGffHash(GffHashGeneAbs gffHashGene);
	
	public void setQueryFile(List<String[]> lsReadFile);
	public void setIsCountRPKM(boolean isCountExpression, StrandSpecific strandSpecific, boolean isCountNCRNA, boolean isJustUseUniqueMappedReads);
	public void setTssRange(int[] tss);
	public void setChrReadsCorrect(boolean chrReadsCorrect);
	public void setTesRange(int[] tes);
		
	public Map<String, SamFileStatistics> getMapPrefix2Statistics();

	public void setResultSamPrefix(String resultSamPrefix);
	public void setResultExpPrefix(String resultExpPrefix);
	public void setResultGeneStructure(String resultGeneStructure);
	public void setResultPath(String resultPath);
	
	public void run();
	
	void clear();
	Map<String, Long> getMapChrID2Len();

}
