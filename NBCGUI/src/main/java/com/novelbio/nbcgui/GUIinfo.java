package com.novelbio.nbcgui;

import java.util.List;

import com.novelbio.GuiAnnoInfo;
import com.novelbio.base.multithread.RunProcess;

public interface GUIinfo {
	
	void setRunningInfo(GuiAnnoInfo string);
	
	void done(RunProcess runProcess);

	void setMessage(String string);

	void setProgressBarLevelLs(List<Double> lsLevels);

	void setProcessBarStartEndBarNum(int level, long startBarNum, long endBarNum);

}
