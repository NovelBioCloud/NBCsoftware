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
import com.novelbio.analysis.seq.genome.gffOperate.GffType;
import com.novelbio.analysis.seq.mapping.StrandSpecific;
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
	JComboBoxData<Species> cmbSpecies;
	JComboBoxData<String> cmbVersion;
	JButton btnSaveto;
	JButton btnOpenFastqLeft;
	JButton btnDelFastqLeft;
	JButton btnRun;
	JButton btnRefgtf;
	JCheckBox chckbxModifythisRefGtf;
	
	CtrlCufflinksTranscriptome cufflinksGTF = new CtrlCufflinksTranscriptome();
	private JComboBoxData<StrandSpecific> cmbStrandSpecific;
	private JLabel lblStrandtype;
	JCheckBox chckbxReconstructtrancsriptome;
	JSpinner spinThreadNum;
	private JCheckBox chkCalculateUQfpkm;
	private JTextField txtRefGTF;
	public GuiTranscriptomeCufflinks() {
		setLayout(null);
		
		JLabel lblFastqfile = new JLabel("BamFile");
		lblFastqfile.setBounds(10, 10, 68, 14);
		add(lblFastqfile);
		
		scrollPaneSamBamFile = new JScrollPaneData();
		scrollPaneSamBamFile.setBounds(10, 30, 783, 188);
		scrollPaneSamBamFile.setTitle(new String[]{"BamFileName", "prefix"});
		add(scrollPaneSamBamFile);
		
		btnOpenFastqLeft = new JButton("Open");
		btnOpenFastqLeft.setBounds(805, 26, 82, 24);
		add(btnOpenFastqLeft);
		
		cmbSpecies = new JComboBoxData<Species>();
		cmbSpecies.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Species species = cmbSpecies.getSelectedValue();
				cmbVersion.setMapItem(species.getMapVersion());
			}
		});
		
		cmbSpecies.setMapItem(Species.getSpeciesName2Species(Species.SEQINFO_SPECIES));
		cmbSpecies.setBounds(10, 252, 194, 23);
		add(cmbSpecies);
		
		JLabel lblSpecies = new JLabel("Species");
		lblSpecies.setBounds(10, 230, 168, 14);
		add(lblSpecies);
		
		JLabel lblAlgrethm = new JLabel("algrethm");
		lblAlgrethm.setBounds(12, 187, 66, 14);
		add(lblAlgrethm);
		//初始化cmbSpeciesVersion
		try {} catch (Exception e) { }
		
		JLabel lblExtendto = new JLabel("ExtendTo");
		lblExtendto.setBounds(17, 450, -137, -132);
		add(lblExtendto);
		
		txtSavePathAndPrefix = new JTextField();
		txtSavePathAndPrefix.setBounds(10, 484, 783, 24);
		add(txtSavePathAndPrefix);
		txtSavePathAndPrefix.setColumns(10);
		
		JLabel lblResultpath = new JLabel("ResultPath");
		lblResultpath.setBounds(10, 458, 80, 14);
		add(lblResultpath);
		
		txtRefGTF = new JTextField();
		txtRefGTF.setBounds(10, 397, 783, 22);
		add(txtRefGTF);
		txtRefGTF.setColumns(10);
		
		btnSaveto = new JButton("SaveTo");
		btnSaveto.setBounds(805, 484, 88, 24);
		btnSaveto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String filePathName = guiFileOpen.openFilePathName("", "");
				txtSavePathAndPrefix.setText(filePathName);
			}
		});
		add(btnSaveto);
		
		btnDelFastqLeft = new JButton("Delete");
		btnDelFastqLeft.setBounds(805, 194, 82, 24);
		
		btnDelFastqLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scrollPaneSamBamFile.deleteSelRows();
			}
		});
		add(btnDelFastqLeft);
		
		btnRun = new JButton("Run");
		btnRun.setBounds(775, 523, 118, 24);
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<String[]> lsSamFileName = scrollPaneSamBamFile.getLsDataInfo();
				cufflinksGTF.setLsBamFile2Prefix(lsSamFileName);
				Species species = cmbSpecies.getSelectedValue();
				species.setVersion(cmbVersion.getSelectedValue());
				GffChrAbs gffChrAbs = new GffChrAbs(species);
				cufflinksGTF.setGffChrAbs(gffChrAbs);
				if (chckbxModifythisRefGtf.isSelected() && FileOperate.isFileExist(txtRefGTF.getText())) {
					cufflinksGTF.setGTFfile(txtRefGTF.getText());
				}
			
				cufflinksGTF.setStrandSpecifictype(cmbStrandSpecific.getSelectedValue());
				String outPathPrefix = txtSavePathAndPrefix.getText();
				if (FileOperate.isFileDirectory(outPathPrefix)) {
					outPathPrefix = FileOperate.addSep(outPathPrefix);
				}
				cufflinksGTF.setOutPathPrefix(outPathPrefix);
				cufflinksGTF.setReconstructTranscriptome(chckbxReconstructtrancsriptome.isSelected());
				cufflinksGTF.setThreadNum((Integer) spinThreadNum.getValue());
				cufflinksGTF.run();
			}
		});
		add(btnRun);
		
		cmbVersion = new JComboBoxData<String>();
		cmbVersion.setBounds(240, 252, 184, 23);
		add(cmbVersion);
		
		JLabel lblVersion = new JLabel("Version");
		lblVersion.setBounds(238, 230, 69, 14);
		add(lblVersion);
		
		cmbStrandSpecific = new JComboBoxData<StrandSpecific>();
		cmbStrandSpecific.setBounds(10, 319, 194, 23);
		add(cmbStrandSpecific);
		
		lblStrandtype = new JLabel("StrandType");
		lblStrandtype.setBounds(10, 298, 118, 14);
		add(lblStrandtype);
		
		chckbxReconstructtrancsriptome = new JCheckBox("reconstructTrancsriptome");
		chckbxReconstructtrancsriptome.setBounds(293, 319, 195, 22);
		add(chckbxReconstructtrancsriptome);
		
		JLabel lblThreadNum = new JLabel("ThreadNum");
		lblThreadNum.setBounds(498, 256, 88, 14);
		add(lblThreadNum);
		
		spinThreadNum = new JSpinner();
		spinThreadNum.setBounds(591, 254, 53, 18);
		add(spinThreadNum);
		
		chkCalculateUQfpkm = new JCheckBox("Upper Quartile FPKM");
		chkCalculateUQfpkm.setBounds(293, 292, 177, 23);
		add(chkCalculateUQfpkm);

		
		btnRefgtf = new JButton("RefGTF");
		btnRefgtf.setBounds(805, 394, 102, 28);
		add(btnRefgtf);
		
		chckbxModifythisRefGtf = new JCheckBox("ModifyThis Ref Gtf instead of Database GFF");
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
		chckbxModifythisRefGtf.setBounds(10, 367, 389, 26);
		add(chckbxModifythisRefGtf);

		
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
		cmbSpecies.setSelectedIndex(0);
		cmbStrandSpecific.setMapItem(StrandSpecific.getMapStrandLibrary());
		spinThreadNum.setValue(4);
	}
}
