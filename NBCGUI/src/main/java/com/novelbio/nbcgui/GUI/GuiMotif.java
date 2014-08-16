package com.novelbio.nbcgui.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.novelbio.analysis.emboss.motif.MotifEmboss;
import com.novelbio.analysis.emboss.motif.MotifEmboss.MotifEmbossScanAlgorithm;
import com.novelbio.analysis.seq.fasta.SeqFasta;
import com.novelbio.analysis.seq.fasta.SeqFastaHash;
import com.novelbio.analysis.seq.fasta.SeqFastaMotifSearch;
import com.novelbio.analysis.seq.fasta.SeqHash;
import com.novelbio.base.dataOperate.TxtReadandWrite;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.gui.GUIFileOpen;
import com.novelbio.base.gui.JScrollPaneData;

public class GuiMotif extends JPanel {
	private JTextField txtMatrix;
	private JTextField txtRegex;
	JButton btnOpenmatrix;
	ButtonGroup buttonGroup = new ButtonGroup();
	JScrollPaneData scrollPane;
	GUIFileOpen guiFileOpen = new GUIFileOpen();
	JRadioButton rdbtnMatrix;
	JRadioButton rdbtnRegx;
	/**
	 * Create the panel.
	 */
	public GuiMotif() {
		setLayout(null);
		
		txtMatrix = new JTextField();
		txtMatrix.setBounds(119, 45, 334, 19);
		add(txtMatrix);
		txtMatrix.setColumns(10);
		
		btnOpenmatrix = new JButton("OpenMatrix");
		btnOpenmatrix.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtMatrix.setText(guiFileOpen.openFileName("", ""));
			}
		});
		btnOpenmatrix.setBounds(465, 42, 121, 25);
		add(btnOpenmatrix);
		
		rdbtnMatrix = new JRadioButton("Matrix");
		rdbtnMatrix.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (rdbtnMatrix.isSelected()) {
					txtMatrix.setEnabled(true);
					btnOpenmatrix.setEnabled(true);
					txtRegex.setEnabled(false);
				}
			}
		});
		rdbtnMatrix.setBounds(30, 43, 81, 23);
		add(rdbtnMatrix);
		buttonGroup.add(rdbtnMatrix);
		
		txtRegex = new JTextField();
		txtRegex.setBounds(119, 76, 334, 19);
		add(txtRegex);
		txtRegex.setColumns(10);
		
		rdbtnRegx = new JRadioButton("Regx");
		rdbtnRegx.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (rdbtnRegx.isSelected()) {
					txtMatrix.setEnabled(false);
					btnOpenmatrix.setEnabled(false);
					txtRegex.setEnabled(true);
				}
			}
		});
		rdbtnRegx.setBounds(30, 74, 81, 23);
		rdbtnRegx.setSelected(true);
		add(rdbtnRegx);
		buttonGroup.add(rdbtnRegx);
		
		scrollPane = new JScrollPaneData();
		scrollPane.setTitle(new String[]{"SeqFile", "Result"});
		scrollPane.setBounds(30, 124, 556, 361);
		add(scrollPane);
		
		JButton btnOpenseq = new JButton("OpenSeq");
		btnOpenseq.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<String> lsFileName = guiFileOpen.openLsFileName("", "");
				List<String[]> lsFile2Out = new ArrayList<String[]>();
				for (String string : lsFileName) {
					String[] ss = new String[]{string, FileOperate.changeFileSuffix(string, "_result", null)};
					lsFile2Out.add(ss);
				}
				scrollPane.addItemLs(lsFile2Out);
			}
		});
		btnOpenseq.setBounds(30, 497, 107, 25);
		add(btnOpenseq);
		
		JButton btnDelseq = new JButton("DelSeq");
		btnDelseq.setBounds(183, 497, 107, 25);
		add(btnDelseq);
		
		JButton btnRun = new JButton("Run");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				runScan();
			}
		});
		btnRun.setBounds(479, 497, 107, 25);
		add(btnRun);
	}
	
	private void runScan() {
		if (rdbtnMatrix.isSelected()) {
			runMotifEmobss();
		} else {
			runMotifRegx();
		}
	}
	
	/** Emboss找motif */
	private void runMotifEmobss() {
		SeqFastaHash seqFastaHash = new SeqFastaHash(txtMatrix.getText());
		List<String[]> lsFileInfo = scrollPane.getLsDataInfo();
		String inputFirst = lsFileInfo.get(0)[0];
		int seqType = SeqHash.getSeqType(inputFirst);
		boolean isNr = (seqType == SeqFasta.SEQ_DNA);
		MotifEmboss motifEmboss = new MotifEmboss();
		motifEmboss.setIsNR(isNr);
		motifEmboss.setAlignedMotifSeqHash(seqFastaHash);
		motifEmboss.setMotifEmbossScanAlgorithm(MotifEmbossScanAlgorithm.Frequency);
		motifEmboss.generateMatrix();
		for (String[] strings : lsFileInfo) {
			motifEmboss.setSeqFilePath(strings[0]);
			String[] result = motifEmboss.scanMotif();
			if (result.length == 1) {
				FileOperate.moveFile(true, result[0], strings[1]);
			} else {
				FileOperate.moveFile(true, result[0], strings[1]);
				FileOperate.moveFile(true, result[1], FileOperate.changeFileSuffix(strings[1], "_reverse", null));
			}
		}
	}
	
	/** 正则表达式找motif */
	private void runMotifRegx() {
		for (String[] strings : scrollPane.getLsDataInfo()) {
			TxtReadandWrite txtWrite = new TxtReadandWrite(strings[1], true);
			SeqFastaHash seqFastaHash = new SeqFastaHash(strings[0]);
			for (SeqFasta seqFasta : seqFastaHash.getSeqFastaAll()) {
				SeqFastaMotifSearch seqFastaMotifSearch = seqFasta.getMotifScan();
				List<String[]> lsTmpResultList = seqFastaMotifSearch.getMotifScanResult(txtRegex.getText());
				for (String[] strings2 : lsTmpResultList) {
					txtWrite.writefileln(strings2);
				}
			}
			txtWrite.close();
		}
	}
	
}
