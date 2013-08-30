package com.novelbio.nbcgui.controlseq;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.novelbio.base.multithread.RunGetInfo;
import com.novelbio.base.multithread.RunProcess;
import com.novelbio.nbcgui.GUI.GuiSnpCalling;
import com.novelbio.analysis.seq.genome.GffChrAbs;
import com.novelbio.analysis.seq.resequencing.SnpDetailGet;
import com.novelbio.analysis.seq.resequencing.SnpFilterDetailInfo;
import com.novelbio.analysis.seq.resequencing.SnpGroupFilterInfo;

public class CtrlSnpGetInfo implements RunGetInfo<SnpFilterDetailInfo>{
	SnpDetailGet snpDetailGet = new SnpDetailGet();	
	SnpGroupFilterInfo snpGroupInfoFilter = new SnpGroupFilterInfo();
	
	GuiSnpCalling guiSnpCalling;

	public CtrlSnpGetInfo(GuiSnpCalling guiSnpCalling) {
		this.guiSnpCalling = guiSnpCalling;
		snpDetailGet.setRunGetInfo(this);
	}
	/** 读取文件
	 * @param lsReadFile
	 * @param colChrID 实际列数
	 * @param colSiteStart 实际列数
	 */
	public void setLsReadFile(ArrayList<String> lsReadFile, int colChrID, int colSiteStart) {
		for (String string : lsReadFile) {
			snpDetailGet.readSnpSiteInfo(string, colChrID, colSiteStart);
		}
	}
	/** 输出文件 */
	public void setOutfile(String outFile) {
		snpDetailGet.setOutFile(outFile);
	}
	public void addPileupFile(String sampleName, String pileupFileName) {
		snpDetailGet.addSample2PileupFile(sampleName, pileupFileName);
	}
	public void runSnpGetInfo() {
		guiSnpCalling.getBtnAddPileupFile().setEnabled(false);
		guiSnpCalling.getBtnDeletePileup().setEnabled(false);
		guiSnpCalling.getBtnAddSnpFile().setEnabled(false);
		guiSnpCalling.getBtnDeleteSnp().setEnabled(false);
		guiSnpCalling.getBtnRun().setEnabled(false);
		guiSnpCalling.getProgressBar().setMinimum(0);
		guiSnpCalling.getProgressBar().setMaximum((int) snpDetailGet.getFileSizeEvaluateK());
		
		Thread thread = new Thread(snpDetailGet);
		thread.start();
	}
	
	@Override
	public void setRunningInfo(SnpFilterDetailInfo info) {
		long kb = info.getAllByte()/1000;
		if (kb != 0) {
			guiSnpCalling.getProgressBar().setValue((int) kb);
		}
		if (info.getMessage() != null) {
			guiSnpCalling.getTxtInfo().setText(info.getMessage());
		}
	}

	@Override
	public void done(RunProcess<SnpFilterDetailInfo> runProcess) {
		guiSnpCalling.getTxtInfo().setText("Snp Calling Complete");
		JOptionPane.showMessageDialog(guiSnpCalling, "Snp Calling Complete", "finish", JOptionPane.INFORMATION_MESSAGE);
		
		guiSnpCalling.getBtnAddPileupFile().setEnabled(true);
		guiSnpCalling.getBtnDeletePileup().setEnabled(true);
		guiSnpCalling.getBtnAddSnpFile().setEnabled(true);
		guiSnpCalling.getBtnDeleteSnp().setEnabled(true);
		guiSnpCalling.getBtnRun().setEnabled(true);
		guiSnpCalling.getProgressBar().setValue(guiSnpCalling.getProgressBar().getMaximum());
	}

	@Override
	public void threadSuspended(RunProcess<SnpFilterDetailInfo> runProcess) {
		guiSnpCalling.getBtnRun().setEnabled(true);
	}

	@Override
	public void threadResumed(RunProcess<SnpFilterDetailInfo> runProcess) {
		guiSnpCalling.getBtnRun().setEnabled(false);
	}

	@Override
	public void threadStop(RunProcess<SnpFilterDetailInfo> runProcess) {
		guiSnpCalling.getBtnRun().setEnabled(true);
	}
	
}
