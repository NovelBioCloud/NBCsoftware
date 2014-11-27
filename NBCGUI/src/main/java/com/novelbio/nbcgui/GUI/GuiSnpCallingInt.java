package com.novelbio.nbcgui.GUI;

import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.JTextPane;

/** snpCalling的界面 */
public interface GuiSnpCallingInt {
	
	public JProgressBar getProgressBar();
	/** 读取信息 */
	public JTextPane getTxtInfo();
	public JButton getBtnAddPileupFile();
	public JButton getBtnDeletePileup();
	public JButton getBtnAddSnpFile();
	
	public JButton getBtnDeleteSnp();
	public JButton getBtnRun();
}
