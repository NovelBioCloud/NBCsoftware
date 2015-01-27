package com.novelbio.nbcgui.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import com.novelbio.analysis.seq.genome.GffChrAbs;
import com.novelbio.analysis.seq.mapping.StrandSpecific;
import com.novelbio.base.FoldeCreate;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.gui.GUIFileOpen;
import com.novelbio.base.gui.JComboBoxData;
import com.novelbio.base.gui.JScrollPaneData;
import com.novelbio.database.model.species.Species;
import com.novelbio.nbcgui.controlseq.CtrlCufflinksTranscriptome;

public class GuiTranscriptomeCufflinks extends JPanel {
	private static final long serialVersionUID = 1567894018870622166L;
	
	private JTextField txtSavePathAndPrefix;
	GUIFileOpen guiFileOpen = new GUIFileOpen();
	
	JScrollPaneData scrollPaneSamBamFile;
	JButton btnSaveto;
	JButton btnOpenFastqLeft;
	JButton btnDelFastqLeft;
	JButton btnRun;
	JButton btnRefgtf;
	JCheckBox chckbxModifythisRefGtf;
	GuiLayeredPaneSpeciesVersionGff guiSpeciesGff;
	CtrlCufflinksTranscriptome cufflinksGTF = new CtrlCufflinksTranscriptome();
	private JComboBoxData<StrandSpecific> cmbStrandSpecific;
	private JLabel lblStrandtype;
	JCheckBox chckbxReconstructtrancsriptome;
	JSpinner spinThreadNum;
	private JCheckBox chkCalculateUQfpkm;
	private JTextField txtRefGTF;
	private JTextField txtChrFile;
	public GuiTranscriptomeCufflinks() {
		setLayout(null);
		
		JLabel lblFastqfile = new JLabel("BamFile");
		lblFastqfile.setBounds(20, 3, 68, 14);
		add(lblFastqfile);
		
		scrollPaneSamBamFile = new JScrollPaneData();
		scrollPaneSamBamFile.setBounds(20, 30, 773, 199);
		scrollPaneSamBamFile.setTitle(new String[]{"BamFileName", "prefix"});
		add(scrollPaneSamBamFile);
		
		btnOpenFastqLeft = new JButton("Open");
		btnOpenFastqLeft.setBounds(807, 30, 82, 24);
		add(btnOpenFastqLeft);
		
		JLabel lblSpecies = new JLabel("Species");
		lblSpecies.setBounds(20, 242, 82, 14);
		add(lblSpecies);

		JLabel lblExtendto = new JLabel("ExtendTo");
		lblExtendto.setBounds(17, 450, -137, -132);
		add(lblExtendto);
		
		txtSavePathAndPrefix = new JTextField();
		txtSavePathAndPrefix.setBounds(14, 495, 783, 24);
		add(txtSavePathAndPrefix);
		txtSavePathAndPrefix.setColumns(10);
		
		JLabel lblResultpath = new JLabel("ResultPath");
		lblResultpath.setBounds(14, 469, 80, 14);
		add(lblResultpath);
		
		txtRefGTF = new JTextField();
		txtRefGTF.setBounds(14, 432, 783, 22);
		add(txtRefGTF);
		txtRefGTF.setColumns(10);
		
		btnSaveto = new JButton("SaveTo");
		btnSaveto.setBounds(809, 495, 88, 24);
		btnSaveto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String filePathName = guiFileOpen.openFilePathName("", "");
				txtSavePathAndPrefix.setText(filePathName);
			}
		});
		add(btnSaveto);
		
		btnDelFastqLeft = new JButton("Delete");
		btnDelFastqLeft.setBounds(805, 205, 82, 24);
		
		btnDelFastqLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scrollPaneSamBamFile.deleteSelRows();
			}
		});
		add(btnDelFastqLeft);
		
		btnRun = new JButton("Run");
		btnRun.setBounds(779, 534, 118, 24);
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<String[]> lsSamFileName = scrollPaneSamBamFile.getLsDataInfo();
				cufflinksGTF.setLsBamFile2Prefix(lsSamFileName);

				if (chckbxModifythisRefGtf.isSelected() && FileOperate.isFileExist(txtRefGTF.getText())) {
					cufflinksGTF.setGTFfile(txtRefGTF.getText());
				} else {
					Species species = guiSpeciesGff.getSelectSpecies();
					GffChrAbs gffChrAbs = new GffChrAbs(species);
					cufflinksGTF.setGffChrAbs(gffChrAbs);
				}
				cufflinksGTF.setIsMergeBamByPrefix(false);
				cufflinksGTF.setUpQuartileNormalized(chkCalculateUQfpkm.isSelected());
				cufflinksGTF.setChrSeq(txtChrFile.getText());
				cufflinksGTF.setStrandSpecifictype(cmbStrandSpecific.getSelectedValue());
				String outPathPrefix = txtSavePathAndPrefix.getText();
				if (FileOperate.isFileDirectory(outPathPrefix)) {
					outPathPrefix = FileOperate.addSep(outPathPrefix);
				}
				String resultPath = FoldeCreate.createAndInFold(outPathPrefix, "ReconstructTranscriptome_result");//TODO
				cufflinksGTF.setOutPathPrefix(resultPath);
				cufflinksGTF.setReconstructTranscriptome(chckbxReconstructtrancsriptome.isSelected());
				cufflinksGTF.setThreadNum((Integer) spinThreadNum.getValue());
				cufflinksGTF.run();
			}
		});
		add(btnRun);
		
		cmbStrandSpecific = new JComboBoxData<StrandSpecific>();
		cmbStrandSpecific.setBounds(629, 270, 194, 23);
		add(cmbStrandSpecific);
		
		lblStrandtype = new JLabel("StrandType");
		lblStrandtype.setBounds(640, 247, 118, 14);
		add(lblStrandtype);
		
		chckbxReconstructtrancsriptome = new JCheckBox("reconstructTrancsriptome");
		chckbxReconstructtrancsriptome.setBounds(248, 312, 252, 22);
		add(chckbxReconstructtrancsriptome);
		
		JLabel lblThreadNum = new JLabel("ThreadNum");
		lblThreadNum.setBounds(256, 263, 88, 14);
		add(lblThreadNum);
		
		spinThreadNum = new JSpinner();
		spinThreadNum.setBounds(349, 261, 53, 18);
		add(spinThreadNum);
		
		chkCalculateUQfpkm = new JCheckBox("Upper Quartile FPKM");
		chkCalculateUQfpkm.setBounds(248, 285, 239, 23);
		add(chkCalculateUQfpkm);

		
		btnRefgtf = new JButton("RefGTF");
		btnRefgtf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String gtfFile = guiFileOpen.openFileName("", "");
				txtRefGTF.setText(gtfFile);
			}
		});
		btnRefgtf.setBounds(809, 429, 102, 28);
		add(btnRefgtf);
		
		chckbxModifythisRefGtf = new JCheckBox("Use Third Part GTF");
		chckbxModifythisRefGtf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (chckbxModifythisRefGtf.isSelected()) {
					txtRefGTF.setEnabled(true);
					btnRefgtf.setEnabled(true);
				} else {
					txtRefGTF.setEnabled(false);
					btnRefgtf.setEnabled(false);
				}
			}
		});
		chckbxModifythisRefGtf.setBounds(14, 397, 324, 26);
		add(chckbxModifythisRefGtf);
		
		guiSpeciesGff = new GuiLayeredPaneSpeciesVersionGff();
		guiSpeciesGff.setBounds(20, 261, 218, 128);
		add(guiSpeciesGff);
		
		txtChrFile = new JTextField();
		txtChrFile.setBounds(248, 367, 538, 22);
		add(txtChrFile);
		txtChrFile.setColumns(10);
		
		JButton btnChrfile = new JButton("ChrFile");
		btnChrfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtChrFile.setText(guiFileOpen.openFileName("", ""));
			}
		});
		btnChrfile.setBounds(806, 366, 102, 28);
		add(btnChrfile);
		
		JLabel lblIfNoSpecies = new JLabel("If No Species In database, use this Chromosome file");
		lblIfNoSpecies.setBounds(248, 342, 349, 18);
		add(lblIfNoSpecies);

		
		btnOpenFastqLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> lsFileLeft = guiFileOpen.openLsFileName("samFile","");
				for (String string : lsFileLeft) {
					String prefix = FileOperate.getFileNameSep(string)[0].split("_")[0];
					scrollPaneSamBamFile.addItem(new String[]{string, prefix});
				}
			}
		});
		initialize();
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		cmbStrandSpecific.setMapItem(StrandSpecific.getMapStrandLibrary());
		spinThreadNum.setValue(4);
	}
}
