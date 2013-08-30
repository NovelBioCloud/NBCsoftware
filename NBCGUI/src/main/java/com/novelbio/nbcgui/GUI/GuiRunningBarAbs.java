package com.novelbio.nbcgui.GUI;

import java.util.List;

import javax.swing.JButton;
import javax.swing.JProgressBar;

import com.novelbio.base.gui.JScrollPaneData;

/** 有进度条，显示文本框的接口 */
public interface GuiRunningBarAbs {
	
	public JProgressBar getProcessBar();
	
	public JScrollPaneData getScrollPaneData();
	/** 导入文件 */
	public List<JButton> getLsBtn();

}
