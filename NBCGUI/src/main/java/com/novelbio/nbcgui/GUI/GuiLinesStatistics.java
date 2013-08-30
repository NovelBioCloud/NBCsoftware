package com.novelbio.nbcgui.GUI;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JButton;

import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.gui.GUIFileOpen;
import com.novelbio.base.gui.JScrollPaneData;
import com.novelbio.nbcgui.controlseq.CtrlSamStatistics;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.JProgressBar;
import javax.swing.JLabel;

public class GuiLinesStatistics extends JPanel {
	CtrlSamStatistics ctrlSamStatistics = new CtrlSamStatistics();
	GUIFileOpen guiFileOpen = new GUIFileOpen();
	JCheckBox chckbxSambamfile;
	JScrollPaneData sclBamSamFile;
	JLabel lblSampleDetail;
	JLabel lblLinesDetail;
	JButton btnRun;
	/**
	 * Create the panel.
	 */
	public GuiLinesStatistics() {
		setLayout(null);
		ctrlSamStatistics.setGuiLinesStatistics(this);
		
		chckbxSambamfile = new JCheckBox("SamBamFile");
		chckbxSambamfile.setBounds(22, 349, 131, 22);
		add(chckbxSambamfile);
		
		sclBamSamFile = new JScrollPaneData();
		sclBamSamFile.setBounds(22, 12, 588, 329);
		sclBamSamFile.setTitle(new String[]{"FileName", "OutFileName"});
		add(sclBamSamFile);
		
		JButton btnOpen = new JButton("OpenFile");
		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> lsFileName = guiFileOpen.openLsFileName("", "");
				sclBamSamFile.addItemLs(JScrollPaneData.getLsFileName2Out(lsFileName,"_report","txt"));
			}
		});
		btnOpen.setBounds(620, 12, 131, 24);
		add(btnOpen);
		
		JButton btnDelSelectRow = new JButton("DelSelectRow");
		btnDelSelectRow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sclBamSamFile.deleteSelRows();
			}
		});
		btnDelSelectRow.setBounds(620, 48, 131, 24);
		add(btnDelSelectRow);
		
		btnRun = new JButton("Run");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<String[]> lsFileName2Out = sclBamSamFile.getLsDataInfo();
				for (String[] fileName2Out : lsFileName2Out) {
					if (chckbxSambamfile.isSelected()) {
						ctrlSamStatistics.setSamFile(fileName2Out[0]);
					} else {
						ctrlSamStatistics.setTxtFile(fileName2Out[0]);
					}
					ctrlSamStatistics.setPrefix(FileOperate.getFileNameSep(fileName2Out[0])[0]);
					ctrlSamStatistics.setOutFile(fileName2Out[1]);
					if (chckbxSambamfile.isSelected()) {
						ctrlSamStatistics.writeSamStatistics();
					}
					else {
						ctrlSamStatistics.writeTxtStatistics();
					}
				}
			}
		});
		btnRun.setBounds(620, 348, 131, 24);
		add(btnRun);
		
		JLabel lblSample = new JLabel("Sample");
		lblSample.setBounds(22, 379, 69, 14);
		add(lblSample);
		
		JLabel lblLines = new JLabel("Lines");
		lblLines.setBounds(22, 405, 69, 14);
		add(lblLines);
		
		lblSampleDetail = new JLabel("");
		lblSampleDetail.setBounds(103, 379, 171, 14);
		add(lblSampleDetail);
		
		lblLinesDetail = new JLabel("");
		lblLinesDetail.setBounds(103, 405, 171, 14);
		add(lblLinesDetail);
	}
	
	public JLabel getLblLinesDetail() {
		return lblLinesDetail;
	}
	public JLabel getLblSampleDetail() {
		return lblSampleDetail;
	}
	public JButton getBtnRun() {
		return btnRun;
	}
}
