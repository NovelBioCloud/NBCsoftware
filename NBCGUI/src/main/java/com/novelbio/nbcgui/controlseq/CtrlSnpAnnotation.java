package com.novelbio.nbcgui.controlseq;

import javax.swing.JOptionPane;

import com.novelbio.analysis.seq.genome.GffChrAbs;
import com.novelbio.analysis.seq.resequencing.SnpAnnotation;
import com.novelbio.analysis.seq.resequencing.SnpFilterDetailInfo;
import com.novelbio.base.multithread.RunGetInfo;
import com.novelbio.base.multithread.RunProcess;
import com.novelbio.nbcgui.GUI.GuiSnpCalling;

public class CtrlSnpAnnotation implements RunGetInfo<SnpFilterDetailInfo>{

	SnpAnnotation snpAnnotation = new SnpAnnotation();	
	GuiSnpCalling guiSnpCalling;
		
	public CtrlSnpAnnotation(GuiSnpCalling guiSnpCalling) {
		this.guiSnpCalling = guiSnpCalling;
		snpAnnotation.setRunGetInfo(this);
	}
	
	public void setProcessBar() {
		guiSnpCalling.getProgressBar().setMaximum((int) snpAnnotation.getFileSizeEvaluateK());
	}
	public void clean() {
		snpAnnotation.clearSnpFile();
	}
	
	public void setGffChrAbs(GffChrAbs gffChrAbs) {
		snpAnnotation.setGffChrAbs(gffChrAbs);
	}
	
	public void setCol(int colChrID, int colRefStartSite, int colRefNr, int colThisNr) {
		snpAnnotation.setCol(colChrID, colRefStartSite, colRefNr, colThisNr);
	}
	public void addSnpFile(String txtFile, String txtOut) {
		snpAnnotation.addTxtSnpFile(txtFile, txtOut);
	}
	
	public void runAnnotation() {
		guiSnpCalling.getBtnAddPileupFile().setEnabled(false);
		guiSnpCalling.getBtnDeletePileup().setEnabled(false);
		guiSnpCalling.getBtnRun().setEnabled(false);
		guiSnpCalling.getProgressBar().setMinimum(0);
		guiSnpCalling.getProgressBar().setMaximum((int) snpAnnotation.getFileSizeEvaluateK());
		
		Thread thread = new Thread(snpAnnotation);
		thread.start();
	}
	@Override
	public void setRunningInfo(SnpFilterDetailInfo info) {
		long kb = info.getAllByte()/1000;
		guiSnpCalling.getProgressBar().setValue((int) kb);
		if (info.getMessage() != null) {
			guiSnpCalling.getTxtInfo().setText(info.getMessage());
		}
	}
	
	public void stop() {
		snpAnnotation.threadStop();
	}
	public void suspend() {
		snpAnnotation.threadSuspend();
	}
	public void resume() {
		snpAnnotation.threadResume();
	}
	
	@Override
	public void done(RunProcess<SnpFilterDetailInfo> runProcess) {
		guiSnpCalling.getProgressBar().setValue(guiSnpCalling.getProgressBar().getMaximum());
		guiSnpCalling.getTxtInfo().setText("Snp Annotation Complete");
		
		JOptionPane.showMessageDialog(guiSnpCalling, "Snp Annotation Complete", "finish", JOptionPane.INFORMATION_MESSAGE);
		
		guiSnpCalling.getBtnAddPileupFile().setEnabled(true);
		guiSnpCalling.getBtnDeletePileup().setEnabled(true);
		guiSnpCalling.getBtnRun().setEnabled(true);
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
