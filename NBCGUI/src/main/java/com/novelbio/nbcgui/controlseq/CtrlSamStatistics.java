package com.novelbio.nbcgui.controlseq;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.novelbio.analysis.seq.sam.AlignSamReading;
import com.novelbio.analysis.seq.sam.SamFile;
import com.novelbio.analysis.seq.sam.SamFileStatistics;
import com.novelbio.base.dataOperate.TxtReadandWrite;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.multithread.RunGetInfo;
import com.novelbio.base.multithread.RunProcess;
import com.novelbio.nbcgui.GuiAnnoInfo;
import com.novelbio.nbcgui.GUI.GuiLinesStatistics;

/** 各种统计，主要是统计Sam文件的 */
@Component
@Scope("prototype")
public class CtrlSamStatistics implements RunGetInfo<GuiAnnoInfo> {
	GuiLinesStatistics guiLinesStatistics;
	
	SamFileStatistics samFileStatistics;
	SamFile samFile;
	
	String outFile;
	String prefix;

	String txtFile;
	
	public void setGuiLinesStatistics(GuiLinesStatistics guiLinesStatistics) {
		this.guiLinesStatistics = guiLinesStatistics;
	}
	
	public void setSamFile(String samFile) {
		this.samFile = new SamFile(samFile);
		if (prefix == null) {
			prefix = FileOperate.getFileNameSep(samFile)[0];
		}
	}
	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public void setOutFile(String outFile) {
		this.outFile = outFile;
	}
	
	public void writeSamStatistics() {
		AlignSamReading alignSamReading = new AlignSamReading(samFile);
		samFileStatistics = new SamFileStatistics("");
		alignSamReading.addAlignmentRecorder(samFileStatistics);
		Thread thread = new Thread(alignSamReading);
		thread.start();
		guiLinesStatistics.getBtnRun().setEnabled(false);
	}
	
	public void setTxtFile(String txtFile) {
		this.txtFile = txtFile;
		if (prefix == null) {
			prefix = FileOperate.getFileNameSep(txtFile)[0];
		}
	}
	
	public void writeTxtStatistics() {
		TxtReadandWrite txtWrite = new TxtReadandWrite(outFile, true);
		long lines = 0;
		for (String string : txtWrite.readlines()) {
			lines ++;
		}
		txtWrite.writefile(outFile + "\t" +lines);
		txtWrite.close();
	}
	
	@Override
	public void setRunningInfo(GuiAnnoInfo info) {
		guiLinesStatistics.getLblLinesDetail().setText(info.getInfo());
		guiLinesStatistics.getLblSampleDetail().setText(FileOperate.getFileNameSep(samFile.getFileName())[0]);
	}
	
	@Override
	public void done(RunProcess<GuiAnnoInfo> runProcess) {
		TxtReadandWrite txtWrite = new TxtReadandWrite(outFile, true);
		txtWrite.ExcelWrite(samFileStatistics.getMappingInfo());
		txtWrite.close();
		guiLinesStatistics.getBtnRun().setEnabled(true);
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
	
}
