package com.novelbio.nbcgui.controlseq;

import java.util.ArrayList;
import java.util.List;

import com.novelbio.GuiAnnoInfo;
import com.novelbio.analysis.seq.fasta.SeqHash;
import com.novelbio.analysis.seq.genome.gffOperate.GffHashGene;
import com.novelbio.analysis.seq.mapping.StrandSpecific;
import com.novelbio.analysis.seq.rnaseq.ExonJunction;
import com.novelbio.base.multithread.RunGetInfo;
import com.novelbio.base.multithread.RunProcess;
import com.novelbio.nbcgui.GUIinfo;

public class CtrlSplicing implements RunGetInfo<GuiAnnoInfo> , Runnable{
	GUIinfo guiRNAautoSplice;
	GffHashGene gffHashGene;
	SeqHash seqHash;
	
	boolean isDisplayAllEvent = true; 
	String outFile;
	List<String[]> lsBam2Prefix;
	List<String[]> lsCompareGroup;
	StrandSpecific strandSpecific = StrandSpecific.NONE;
	boolean memoryLow = false;
	boolean isReconstruceIso = false;
	
	public void setGuiRNAautoSplice(GUIinfo guiRNAautoSplice) {
		this.guiRNAautoSplice = guiRNAautoSplice;
	}
	public void setMemoryLow(boolean memoryLow) {
		this.memoryLow = memoryLow;
	}
	public void setLsBam2Prefix(List<String[]> lsBam2Prefix) {
		this.lsBam2Prefix = lsBam2Prefix;
	}
	public void setLsCompareGroup(List<String[]> lsCompareGroup) {
		this.lsCompareGroup = lsCompareGroup;
	}
	public void setReconstructIso(boolean isReconstructIso) {
		this.isReconstruceIso = isReconstructIso;
	}
	
	public void setStrandSpecific(StrandSpecific strandSpecific) {
		this.strandSpecific = strandSpecific;
	}
	@Override
	public void setRunningInfo(GuiAnnoInfo info) {
		guiRNAautoSplice.setRunningInfo(info);
	}

	@Override
	public void done(RunProcess<GuiAnnoInfo> runProcess) {
		guiRNAautoSplice.done(runProcess);
	}

	@Override
	public void threadSuspended(RunProcess<GuiAnnoInfo> runProcess) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void threadResumed(RunProcess<GuiAnnoInfo> runProcess) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void threadStop(RunProcess<GuiAnnoInfo> runProcess) {
		// TODO Auto-generated method stub
	}
	
	public void setGffHashGene(GffHashGene gffHashGene) {
		this.gffHashGene = gffHashGene;
	}
	
	/**
	 * 如果seqhash
	 * @param seqPath
	 */
	public void setSeqHash(SeqHash seqHash) {
		this.seqHash = seqHash;
	}
	
	public void setDisplayAllEvent(boolean isDisplayAllEvent) {
		this.isDisplayAllEvent = isDisplayAllEvent;
	}
	
	public void setOutFile(String outFile) {
		this.outFile = outFile;
	}
	
	@Override
	public void run() {
		ExonJunction exonJunction = new ExonJunction();
		exonJunction.setStrandSpecific(strandSpecific);
		exonJunction.setGffHashGene(gffHashGene);
		exonJunction.setOneGeneOneSpliceEvent(!isDisplayAllEvent);
		exonJunction.setRunGetInfo(this);
		exonJunction.setIsLessMemory(memoryLow);

		for (String[] strings : lsBam2Prefix) {
			//TODO 暂时没有多对多比较
			exonJunction.addBamSorted(strings[2], strings[0]);
		}
		//TODO
		exonJunction.setCompareGroupsLs(lsCompareGroup);
		exonJunction.setResultFile(outFile);
		exonJunction.setRunGetInfo(this);
		exonJunction.setSeqHash(seqHash);
		if (isReconstruceIso) {
			exonJunction.setgenerateNewIso();
		}
		Thread thread = new Thread(exonJunction);
		thread.start();
		
		try { Thread.sleep(2000); } catch (InterruptedException e) { }

		while (exonJunction.isRunning()) {
			try { Thread.sleep(300); } catch (InterruptedException e) { }
		}
		if (!exonJunction.isFinished()) {
			guiRNAautoSplice.setMessage("Running Error");
		} else {
			guiRNAautoSplice.setMessage("Congratulations! Enjoy your PASH.");
		}
		guiRNAautoSplice.done(null);
	}

	public void setProgressBarLevelLs(ArrayList<Double> lsLevels) {
		if (guiRNAautoSplice == null) {
			return;
		}
		guiRNAautoSplice.setProgressBarLevelLs(lsLevels);
	}
	
	/**
	 * 设定本次步骤里面将绘制progressBar的第几部分
	 * 并且本部分的最短点和最长点分别是什么
	 * @param information gui上显示的文本信息
	 * @param level 本次步骤里面将绘制progressBar的第几部分，也就是跑到第几步了。总共3步
	 * @param startBarNum 本步骤起点，一般为0
	 * @param endBarNum 本步骤终点
	 */
	public void setProcessBarStartEndBarNum(String string, int level, long startBarNum, long endBarNum) {
		if (guiRNAautoSplice == null) {
			return;
		}
		guiRNAautoSplice.setProcessBarStartEndBarNum(string, level, startBarNum, endBarNum);
	}
	public void setInfo(String string) {
		if (guiRNAautoSplice == null) {
			return;
		}
		guiRNAautoSplice.setInfo(string);
	}
	public void setDetailInfo(String string) {
		if (guiRNAautoSplice == null) {
			return;
		}
		guiRNAautoSplice.setDetailInfo(string);
	}

}
