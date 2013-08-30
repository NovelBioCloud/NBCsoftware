package com.novelbio.nbcgui.controlseq;

import java.util.ArrayList;

import com.novelbio.analysis.seq.fasta.SeqFasta;
import com.novelbio.analysis.seq.genome.GffChrAbs;
import com.novelbio.analysis.seq.genome.GffChrSeq;
import com.novelbio.analysis.seq.genome.GffChrSeq.GffChrSeqProcessInfo;
import com.novelbio.analysis.seq.genome.gffOperate.GffDetailGene.GeneStructure;
import com.novelbio.analysis.seq.genome.mappingOperate.SiteSeqInfo;
import com.novelbio.base.multithread.RunGetInfo;
import com.novelbio.base.multithread.RunProcess;
import com.novelbio.database.model.species.Species;
import com.novelbio.nbcgui.GUI.GuiGetSeq;

public class CtrlGetSeq implements RunGetInfo<GffChrSeq.GffChrSeqProcessInfo>{
	int[] upAndDownStream = new int[2];
	GffChrAbs gffChrAbs = new GffChrAbs();
	GuiGetSeq guiGetSeq;
	GffChrSeq gffChrSeq = new GffChrSeq();
	
	public CtrlGetSeq(GuiGetSeq guiGetSeq) {
		this.guiGetSeq = guiGetSeq;
		gffChrSeq.setRunGetInfo(this);
	}
	
	public void setGffChrAbs(GffChrAbs gffChrAbs) {
		this.gffChrAbs = gffChrAbs;
		gffChrSeq.setGffChrAbs(this.gffChrAbs);
	}
	public void setSpecies(Species species) {
		gffChrAbs.setSpecies(species);
		gffChrSeq.setGffChrAbs(gffChrAbs);
	}
	public void setUpAndDownStream(int[] upAndDownStream) {
		this.upAndDownStream = upAndDownStream;
	}
	/** 默认是 true */
	public void setSaveToFile(boolean saveToFile) {
		gffChrSeq.setIsSaveToFile(saveToFile);
	}
	/** 是每个LOC提取一条序列还是提取全部 */
	public void setGetAllIso(boolean getAllIso) {
		gffChrSeq.setGetAllIso(getAllIso);
	}
	/**
	 * 提取基因的时候遇到内含子，是提取出来还是跳过去
	 * @param getIntron
	 */
	public void setGetIntron(boolean getIntron) {
		gffChrSeq.setGetIntron(getIntron);
	}
	public void setGetAAseq(boolean getAAseq) {
		gffChrSeq.setGetAAseq(getAAseq);
	}
 
	public void setOutPutFile(String outPutFile) {
		gffChrSeq.setOutPutFile(outPutFile);
	}
	/** 待提取基因的哪一个部分 */
	public void setGeneStructure(GeneStructure geneStructure) {
		gffChrSeq.setGeneStructure(geneStructure);
	}
	/**
	 * 输入名字提取序列，内部会去除重复基因
	 * @param lsIsoName
	 */
	public void setGetSeqIso(ArrayList<String> lsIsoName) {
		gffChrSeq.setGetSeqIso(lsIsoName);
	}
	/**
	 * 输入名字提取序列，内部会去除重复基因
	 * @param lsListGffName
	 */
	public void setGetSeqIsoGenomWide() {
		gffChrSeq.setGetSeqGenomWide();
	}
	/**
	 * 输入位点提取序列
	 * @param lsIsoName
	 */
	public void setGetSeqSite(ArrayList<SiteSeqInfo> lsIsoName) {
		gffChrSeq.setGetSeqSite(lsIsoName);
	}
	/** 如果不是保存在文件中，就可以通过这个来获得结果 */
	public ArrayList<SeqFasta> getLsResult() {
		return gffChrSeq.getLsResult();
	}

	public void execute() {
		gffChrSeq.setTssRange(upAndDownStream);
		gffChrSeq.setTesRange(upAndDownStream);
		guiGetSeq.getProgressBar().setMinimum(0);
		guiGetSeq.getProgressBar().setMaximum(gffChrSeq.getNumOfQuerySeq());
		guiGetSeq.getBtnOpen().setEnabled(false);
		guiGetSeq.getBtnSave().setEnabled(false);
		guiGetSeq.getBtnRun().setEnabled(false);
		Thread thread = new Thread(gffChrSeq);
		thread.start();
	}
	
	public void reset() {
		gffChrSeq.reset();
	}
	
	@Override
	public void setRunningInfo(GffChrSeqProcessInfo info) {
		guiGetSeq.getProgressBar().setValue(info.getNumber());
	}

	@Override
	public void done(RunProcess<GffChrSeqProcessInfo> runProcess) {
		guiGetSeq.getProgressBar().setValue(guiGetSeq.getProgressBar().getMaximum());
		guiGetSeq.getBtnOpen().setEnabled(true);
		guiGetSeq.getBtnSave().setEnabled(true);
		guiGetSeq.getBtnRun().setEnabled(true);
	}

	@Override
	public void threadSuspended(RunProcess<GffChrSeqProcessInfo> runProcess) {		
	}

	@Override
	public void threadResumed(RunProcess<GffChrSeqProcessInfo> runProcess) {
	}

	@Override
	public void threadStop(RunProcess<GffChrSeqProcessInfo> runProcess) {
	}
	
}
