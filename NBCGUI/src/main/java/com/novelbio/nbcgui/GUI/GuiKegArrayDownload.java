package com.novelbio.nbcgui.GUI;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;

import com.novelbio.analysis.tools.kegarray.DownKeggPng;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.gui.GUIFileOpen;
import com.novelbio.base.multithread.RunGetInfo;
import com.novelbio.base.multithread.RunProcess;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JProgressBar;

public class GuiKegArrayDownload extends JPanel implements RunGetInfo<Integer> {
	private static final long serialVersionUID = 6769461171854681820L;
	private JTextField txtKegArrayUrl;
	private JTextField txtOutPath;
	JProgressBar progressBar;
	GUIFileOpen guiFileOpen = new GUIFileOpen();
	JButton btnNewButton;
	private JTextField txtPngNum;
	/**
	 * Create the panel.
	 */
	public GuiKegArrayDownload() {
		setLayout(null);
		
		JLabel lblDownloadPng = new JLabel("DOWNLOAD  PNG");
		lblDownloadPng.setFont(new Font("Dialog", Font.BOLD, 17));
		lblDownloadPng.setBounds(120, 46, 214, 44);
		add(lblDownloadPng);
		
		txtKegArrayUrl = new JTextField();
		txtKegArrayUrl.setBounds(100, 115, 325, 35);
		add(txtKegArrayUrl);
		txtKegArrayUrl.setColumns(10);
		
		JButton outFileNewButton = new JButton("OUT FILE");
		outFileNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnNewButton.setEnabled(true);
				String outFile =  guiFileOpen.saveFileNameAndPath("", "");
				txtOutPath.setText(outFile);
			}
		});
		outFileNewButton.setBounds(65, 162, 118, 35);
		add(outFileNewButton);
		
		JLabel lblUrl = new JLabel("URL");
		lblUrl.setBounds(65, 115, 33, 34);
		add(lblUrl);
		
		txtOutPath = new JTextField();
		txtOutPath.setBounds(195, 162, 230, 35);
		add(txtOutPath);
		txtOutPath.setColumns(10);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(100, 209, 325, 25);
		add(progressBar);

		
		btnNewButton = new JButton("StartDownLoad");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				download();
			}
		});
		btnNewButton.setBounds(65, 247, 360, 41);
		add(btnNewButton);		
		
		txtPngNum = new JTextField();
		txtPngNum.setBounds(65, 209, 33, 26);
		add(txtPngNum);
		txtPngNum.setColumns(10);
	}
	
	private void download() {
		btnNewButton.setEnabled(false);
		DownKeggPng downKeggPng =new DownKeggPng();
		downKeggPng.setRunGetInfo(this);
		downKeggPng.setDownLoadPng(txtKegArrayUrl.getText().trim(), txtOutPath.getText());
		downKeggPng.querKegArrayUrl();
		
		progressBar.setMinimum(0);
		progressBar.setMaximum(downKeggPng.getDownloadPicNum());
		
		Thread thread = new Thread(downKeggPng);
		thread.start();
	}
	
	@Override
	public void setRunningInfo(Integer info) {
		progressBar.setValue(info);
		txtPngNum.setText(info + "");
	}
	@Override
	public void done(RunProcess<Integer> runProcess) {
		progressBar.setValue(progressBar.getMaximum());
		btnNewButton.setEnabled(true);
	}
	@Override
	public void threadSuspended(RunProcess<Integer> runProcess) {
		btnNewButton.setEnabled(true);
	}
	@Override
	public void threadResumed(RunProcess<Integer> runProcess) {
		btnNewButton.setEnabled(false);
	}
	@Override
	public void threadStop(RunProcess<Integer> runProcess) {
	}
}
