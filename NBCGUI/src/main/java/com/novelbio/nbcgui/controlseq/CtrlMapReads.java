package com.novelbio.nbcgui.controlseq;

import java.util.List;

import javax.swing.JButton;

import com.novelbio.analysis.seq.AlignSeq;
import com.novelbio.analysis.seq.FormatSeq;
import com.novelbio.analysis.seq.bed.BedSeq;
import com.novelbio.analysis.seq.genome.mappingOperate.MapReads;
import com.novelbio.analysis.seq.genome.mappingOperate.MapReadsAbs;
import com.novelbio.analysis.seq.genome.mappingOperate.MapReadsAbs.MapReadsProcessInfo;
import com.novelbio.analysis.seq.sam.SamFile;
import com.novelbio.base.dataStructure.Equations;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.multithread.RunGetInfo;
import com.novelbio.base.multithread.RunProcess;
import com.novelbio.database.model.species.Species;
import com.novelbio.nbcgui.GUI.GuiRunningBarAbs;

public class CtrlMapReads implements RunGetInfo<MapReadsAbs.MapReadsProcessInfo>{
	String bedFileName;
	MapReads mapReads;
	GuiRunningBarAbs guiRunningBarAbs;
	

	public CtrlMapReads(GuiRunningBarAbs guiRunningBarAbs) {
		this.guiRunningBarAbs = guiRunningBarAbs;
		mapReads = new MapReads();
		mapReads.setRunGetInfo(this);
	}
	public void setBamBedFile(String bedFileName) {
		this.bedFileName = bedFileName;
		FormatSeq formatSeq = FormatSeq.getFileType(bedFileName);
		AlignSeq alignSeq = null;
		if (formatSeq == FormatSeq.BED) {
			alignSeq = new BedSeq(bedFileName);
		} else if (formatSeq == FormatSeq.SAM || formatSeq == FormatSeq.BAM) {
			alignSeq = new SamFile(bedFileName);
		}
		mapReads.setAlignSeqReader(alignSeq);
	}
	
	public void setInvNum(int invNum) {
		mapReads.setInvNum(invNum);
	}
	public MapReads getMapReads() {
		return mapReads;
	}
	public void setSpecies(Species species) {
		mapReads.setMapChrID2Len(species.getMapChromInfo());
	}
	/**
	 * @param uniqReads 当reads mapping至同一个位置时，是否仅保留一个reads
	 * @param startCod 从起点开始读取该reads的几个bp，韩燕用到 小于等于0表示全部读取 大于reads长度的则忽略该参数
	 * @param colUnique  Unique的reads在哪一列 novelbio的标记在第七列，从1开始计算
	 * @param booUniqueMapping 重复的reads是否只选择一条
	 * @param cis5to3 是否仅选取某一方向的reads，null不考虑
	 */
	public void setFilter(boolean uniqReads, int startCod, boolean booUniqueMapping, Boolean FilteredStrand) {
		mapReads.setFilter(uniqReads, startCod, booUniqueMapping, FilteredStrand);
	}
	/**
	 * 用于校正reads数的方程，默认设定基因组上reads的最小值为0，凡是校正小于0的都改为0
	 * @param FormulatToCorrectReads
	 */
	public void setFormulatToCorrectReads(Equations FormulatToCorrectReads) {
		mapReads.setFormulatToCorrectReads(FormulatToCorrectReads);
	}
	 /**
	  * 设定标准化方法，可以随时设定，不一定要在读取文件前
	  * 默认是NORMALIZATION_ALL_READS
	  * @param normalType
	  */
	public void setNormalType(int normalType) {
		if (mapReads != null) {
			mapReads.setNormalType(normalType);
		}
	}
	@Override
	public void setRunningInfo(MapReadsProcessInfo info) {
		guiRunningBarAbs.getProcessBar().setValue((int) (info.getReadsize()/1024));
	}

	@Override
	public void done(RunProcess<MapReadsProcessInfo> runProcess) {
		guiRunningBarAbs.getProcessBar().setValue(guiRunningBarAbs.getProcessBar().getMaximum());
		List<JButton> lsJButtons = guiRunningBarAbs.getLsBtn();
		for (JButton jButton : lsJButtons) {
			jButton.setEnabled(true);
		}
	}

	@Override
	public void threadSuspended(RunProcess<MapReadsProcessInfo> runProcess) {
		List<JButton> lsJButtons = guiRunningBarAbs.getLsBtn();
		for (JButton jButton : lsJButtons) {
			jButton.setEnabled(true);
		}
		
	}

	@Override
	public void threadResumed(RunProcess<MapReadsProcessInfo> runProcess) {
		List<JButton> lsJButtons = guiRunningBarAbs.getLsBtn();
		for (JButton jButton : lsJButtons) {
			jButton.setEnabled(false);
		}
	}

	@Override
	public void threadStop(RunProcess<MapReadsProcessInfo> runProcess) {
		List<JButton> lsJButtons = guiRunningBarAbs.getLsBtn();
		for (JButton jButton : lsJButtons) {
			jButton.setEnabled(true);
		}
	}

	public void execute() {
		List<JButton> lsJButtons = guiRunningBarAbs.getLsBtn();
		for (JButton jButton : lsJButtons) {
			jButton.setEnabled(false);
		}
		guiRunningBarAbs.getProcessBar().setMinimum(0);
		guiRunningBarAbs.getProcessBar().setMaximum((int) (FileOperate.getFileSizeLong(bedFileName)/1024));
		guiRunningBarAbs.getProcessBar().setValue(0);
		
		mapReads.setRunGetInfo(this);
		Thread thread = new Thread(mapReads);
		thread.start();
	}

}
