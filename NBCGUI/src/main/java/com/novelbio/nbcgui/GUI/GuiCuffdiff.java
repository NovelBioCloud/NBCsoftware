package com.novelbio.nbcgui.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import com.novelbio.analysis.seq.genome.GffChrAbs;
import com.novelbio.analysis.seq.rnaseq.Cuffcompare;
import com.novelbio.analysis.seq.rnaseq.Cuffdiff;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.gui.GUIFileOpen;
import com.novelbio.base.gui.JScrollPaneData;
import com.novelbio.database.domain.information.SoftWareInfo;
import com.novelbio.database.domain.information.SoftWareInfo.SoftWare;
import com.novelbio.database.model.species.Species;

public class GuiCuffdiff extends JPanel {
	private static final long serialVersionUID = 54835174459034502L;
	
	private JTextField txtGtfCuffdiff;
	GuiLayeredPaneSpeciesVersionGff guiLayeredPaneSpeciesVersionGff;
	ButtonGroup buttonGroup = new ButtonGroup();
	JRadioButton rdbtnCuffcompare;
	JRadioButton rdbtnCuffdiff;
	JButton btnGtffile;
	
	JScrollPaneData sclFileName;
	
	GUIFileOpen guiFileOpen = new GUIFileOpen();
	
	Cuffdiff cuffdiff = new Cuffdiff();
	Cuffcompare cuffcompare = new Cuffcompare();
	private JTextField txtSaveTo;
	private JButton btnSaveto;
	private JScrollPaneData sclCompare;
	private JButton btnAddcompare;
	private JButton btnDelCompare;
	
	JSpinner spinThreadCuffDiff;
	JLabel lblThread;
	/**
	 * Create the panel.
	 */
	public GuiCuffdiff() {
		setLayout(null);
		
		sclFileName = new JScrollPaneData();
		sclFileName.setBounds(24, 22, 655, 231);
		add(sclFileName);
		
		rdbtnCuffcompare = new JRadioButton("CuffCompare");
		rdbtnCuffcompare.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnGtffile.setText("ReferenceGTF");

				btnAddcompare.setVisible(false);
				btnDelCompare.setVisible(false);
				sclCompare.setVisible(false);
				
				sclFileName.clean();
				sclFileName.setTitle(new String[]{"GtfFile"});
			}
		});
		rdbtnCuffcompare.setBounds(179, 280, 151, 22);
		add(rdbtnCuffcompare);
		
		rdbtnCuffdiff = new JRadioButton("CuffDiff");
		rdbtnCuffdiff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnGtffile.setText("CalculateGTF");
				
				btnAddcompare.setVisible(true);
				btnDelCompare.setVisible(true);
				sclCompare.setVisible(true);
				
				sclFileName.clean();
				sclFileName.setTitle(new String[]{"SamBamFile", "prefix"});
			}
		});
		rdbtnCuffdiff.setBounds(24, 280, 151, 22);
		add(rdbtnCuffdiff);
		
		JButton btnAddfile = new JButton("AddFile");
		btnAddfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> lsFileName = guiFileOpen.openLsFileName("", "");
				ArrayList<String[]> lsFileInput = new ArrayList<String[]>();
				for (String fileName : lsFileName) {
					lsFileInput.add(new String[]{fileName, FileOperate.getFileNameSep(fileName)[0].split("_")[0]});
				}
				sclFileName.addItemLs(lsFileInput);
			}
		});
		btnAddfile.setBounds(691, 22, 118, 24);
		add(btnAddfile);
		
		JButton btnDeleteFileRow = new JButton("DeleteRow");
		btnDeleteFileRow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sclFileName.deleteSelRows();
			}
		});
		btnDeleteFileRow.setBounds(691, 58, 118, 24);
		add(btnDeleteFileRow);
		
		guiLayeredPaneSpeciesVersionGff = new GuiLayeredPaneSpeciesVersionGff();
		guiLayeredPaneSpeciesVersionGff.setBounds(691, 110, 229, 143);
		add(guiLayeredPaneSpeciesVersionGff);
		
		txtGtfCuffdiff = new JTextField();
		txtGtfCuffdiff.setBounds(24, 341, 526, 18);
		add(txtGtfCuffdiff);
		txtGtfCuffdiff.setColumns(10);
		
		btnGtffile = new JButton("GTFfile");
		btnGtffile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String txtFile = guiFileOpen.openFileName("", "");
				txtGtfCuffdiff.setText(txtFile);
			}
		});
		btnGtffile.setBounds(559, 338, 118, 24);
		add(btnGtffile);
		
		JLabel lblInputGtfFile = new JLabel("Input Gtf File");
		lblInputGtfFile.setBounds(24, 315, 163, 14);
		add(lblInputGtfFile);
		
		txtSaveTo = new JTextField();
		txtSaveTo.setBounds(24, 383, 522, 18);
		add(txtSaveTo);
		txtSaveTo.setColumns(10);
		
		btnSaveto = new JButton("SaveTo");
		btnSaveto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String txtFile = guiFileOpen.saveFileNameAndPath("", "");
				txtSaveTo.setText(txtFile);
			}
		});
		btnSaveto.setBounds(561, 381, 118, 24);
		add(btnSaveto);
		
		JButton btnRun = new JButton("Run");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String outPathFile = txtSaveTo.getText();
				if (rdbtnCuffcompare.isSelected()) {
					runCuffcompare(outPathFile);
				} else if (rdbtnCuffdiff.isSelected()) {
					runCuffdiff(outPathFile);
				}
			}
		});
		btnRun.setBounds(561, 437, 118, 24);
		add(btnRun);
		
		sclCompare = new JScrollPaneData();
		sclCompare.setBounds(705, 293, 217, 139);
		add(sclCompare);
		
		btnAddcompare = new JButton("AddCompare");
		btnAddcompare.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sclCompare.addItem(new String[]{"",""});
			}
		});
		btnAddcompare.setBounds(708, 444, 131, 24);
		add(btnAddcompare);
		
		btnDelCompare = new JButton("Del");
		btnDelCompare.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sclCompare.deleteSelRows();
			}
		});
		btnDelCompare.setBounds(861, 444, 59, 24);
		add(btnDelCompare);
		
		spinThreadCuffDiff = new JSpinner();
		spinThreadCuffDiff.setBounds(98, 425, 59, 18);
		add(spinThreadCuffDiff);
		spinThreadCuffDiff.setValue(4);
		
		lblThread = new JLabel("Thread");
		lblThread.setBounds(22, 427, 69, 14);
		add(lblThread);
		
		initial();
	}
	
	private void initial() {
		buttonGroup.add(rdbtnCuffcompare);
		buttonGroup.add(rdbtnCuffdiff);
		rdbtnCuffdiff.setSelected(true);
		sclFileName.setTitle(new String[]{"SamBamFile", "prefix"});
		sclCompare.setTitle(new String[]{"Treat", "Control"});
	}
	
	private void runCuffdiff(String outFile) {
		ArrayList<String[]> lsFileName2Prefix = sclFileName.getLsDataInfo();
		SoftWareInfo softWareInfo = new SoftWareInfo(SoftWare.cufflinks);
		cuffdiff.setExePath(softWareInfo.getExePath());
		cuffdiff.setOutPath(outFile);
		String gtfFile = txtGtfCuffdiff.getText();
		Species species = guiLayeredPaneSpeciesVersionGff.getSelectSpecies();
		if (!FileOperate.isFileExistAndBigThanSize(gtfFile, 1)) {
			gtfFile = getSpeciesGtf(outFile);
		}
		
		if (!FileOperate.isFileExistAndBigThanSize(gtfFile, 1) || species.getTaxID() == 0) {
			JOptionPane.showConfirmDialog(null, "Error", "No useful Gtf Or No Species Selected", JOptionPane.CLOSED_OPTION);
			return;
		}
		
		//生成能给cuffdiff识别的gtf文件
		cuffcompare.setExePath(softWareInfo.getExePath());
		cuffcompare.setRefGtfFile(gtfFile);
		cuffcompare.setOutPath(outFile);
		cuffcompare.setSeqFasta(species.getChromFaPath());
		cuffcompare.setClearFile(true);
		gtfFile = cuffcompare.runCompareGtf();
		
		cuffdiff.setGtfFile(gtfFile);
		cuffdiff.setSeqFasta(species.getChromSeq());
		cuffdiff.setOutPath(outFile);
		cuffdiff.setLsSample2Prefix(lsFileName2Prefix);
		cuffdiff.setCompare(sclCompare.getLsDataInfo());
		cuffdiff.setThreadNum((Integer) spinThreadCuffDiff.getValue());
		cuffdiff.runCuffDiff();
	}
	
	private void runCuffcompare(String outFile) {
		Species species = guiLayeredPaneSpeciesVersionGff.getSelectSpecies();
		SoftWareInfo softWareInfo = new SoftWareInfo(SoftWare.cufflinks);
		cuffcompare.setExePath(softWareInfo.getExePath());
		cuffcompare.setOutPath(outFile);
		
		if (species.getTaxID() != 0) {
			String gtfSpecies = getSpeciesGtf(outFile);
			cuffcompare.setRefGtfFile(gtfSpecies);
			cuffcompare.setSeqFasta(species.getChromFaPath());
		}
		String refGTF = txtGtfCuffdiff.getText();
		if (FileOperate.isFileExist(refGTF)) {
			cuffcompare.setRefGtfFile(refGTF);
		}
		
		ArrayList<String[]> lsFileName = sclFileName.getLsDataInfo();
		ArrayList<String> lsGtfFile = new ArrayList<String>();
		for (String[] strings : lsFileName) {
			lsGtfFile.add(strings[0]);
		}
		cuffcompare.setLsInputGtfFile(lsGtfFile);
		
		cuffcompare.runCompareGtf();
	}
	/** 从物种生成的GTF文件 */
	private String getSpeciesGtf(String outFile) {
		Species species = guiLayeredPaneSpeciesVersionGff.getSelectSpecies();
		if (species.getTaxID() != 0) {
			GffChrAbs gffChrAbs = new GffChrAbs(species);
			String outGtf = FileOperate.changeFileSuffix(outFile, "_NovelGtf_Tmp", "gtf");
			gffChrAbs.getGffHashGene().writeToGTF(FileOperate.changeFileSuffix(outFile, "_NovelGtf_Tmp", "gtf"));
			return outGtf;
		}
		return "";
	}
}
