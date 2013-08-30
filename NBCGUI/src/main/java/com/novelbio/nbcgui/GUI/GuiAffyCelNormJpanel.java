package com.novelbio.nbcgui.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.novelbio.analysis.microarray.AffyNormalization;
import com.novelbio.base.gui.GUIFileOpen;
import com.novelbio.base.gui.JComboBoxData;
import com.novelbio.base.gui.JScrollPaneData;

public class GuiAffyCelNormJpanel extends JPanel {
	private JTextField txtSavePathAndPrefix;
	GUIFileOpen guiFileOpen = new GUIFileOpen();
	private final ButtonGroup groupLibrary = new ButtonGroup();
	JScrollPaneData scrollPaneCelFile;
	JComboBoxData<Integer> cmbNormalizedType;
	JComboBoxData<String> cmbArrayType;
	JButton btnSaveto;
	JButton btnOpenFastqLeft;
	JButton btnDelFastqLeft;
	JButton btnRun;
	AffyNormalization affyNormalization = new AffyNormalization();
	
	public GuiAffyCelNormJpanel() {
		setLayout(null);
		
		JLabel lblFastqfile = new JLabel("FastQFile");
		lblFastqfile.setBounds(10, 10, 68, 14);
		add(lblFastqfile);
		
		scrollPaneCelFile = new JScrollPaneData();
		scrollPaneCelFile.setBounds(10, 30, 783, 188);
		scrollPaneCelFile.setTitle(new String[]{"CelFileName"});
		add(scrollPaneCelFile);
		
		btnOpenFastqLeft = new JButton("Open");
		btnOpenFastqLeft.setBounds(805, 26, 82, 24);
		add(btnOpenFastqLeft);
		
		cmbNormalizedType = new JComboBoxData<Integer>();
		
		cmbNormalizedType.setMapItem(AffyNormalization.getMapNormStr2ID());
		cmbNormalizedType.setBounds(175, 252, 194, 23);
		add(cmbNormalizedType);
		
		JLabel lblReadsQuality = new JLabel("NormalizedType");
		lblReadsQuality.setBounds(10, 256, 168, 14);
		add(lblReadsQuality);
		
		JLabel lblAlgrethm = new JLabel("algrethm");
		lblAlgrethm.setBounds(12, 187, 66, 14);
		add(lblAlgrethm);
		//初始化cmbSpeciesVersion
		try {} catch (Exception e) { }
		
		JLabel lblExtendto = new JLabel("ExtendTo");
		lblExtendto.setBounds(17, 450, -137, -132);
		add(lblExtendto);
		
		txtSavePathAndPrefix = new JTextField();
		txtSavePathAndPrefix.setBounds(10, 388, 783, 24);
		add(txtSavePathAndPrefix);
		txtSavePathAndPrefix.setColumns(10);
		
		JLabel lblResultpath = new JLabel("ResultPath");
		lblResultpath.setBounds(10, 369, 80, 14);
		add(lblResultpath);
		
		btnSaveto = new JButton("SaveTo");
		btnSaveto.setBounds(805, 388, 88, 24);
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
				scrollPaneCelFile.deleteSelRows();
			}
		});
		add(btnDelFastqLeft);
		
		btnRun = new JButton("Run");
		btnRun.setBounds(775, 469, 118, 24);
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<String[]> lsCelFileName = scrollPaneCelFile.getLsDataInfo();
				ArrayList<String> lsCelFile = new ArrayList<String>();
				for (String[] string : lsCelFileName) {
					lsCelFile.add(string[0]);
				}
				affyNormalization.setLsRawCelFile(lsCelFile);
				affyNormalization.setNormalizedType(cmbNormalizedType.getSelectedValue());
				affyNormalization.setOutFileName(txtSavePathAndPrefix.getText());
				affyNormalization.setArrayType(cmbArrayType.getSelectedValue());
				affyNormalization.run();
			}
		});
		add(btnRun);
		
		JLabel lblArraytype = new JLabel("ArrayType");
		lblArraytype.setBounds(10, 298, 118, 14);
		add(lblArraytype);
		
		cmbArrayType = new JComboBoxData<String>();
		cmbArrayType.setBounds(175, 294, 194, 23);
		cmbArrayType.setMapItem(AffyNormalization.getMapArrayTpye());
		add(cmbArrayType);

		
		btnOpenFastqLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> lsFileLeft = guiFileOpen.openLsFileName("fastqFile","");
				for (String string : lsFileLeft) {
					scrollPaneCelFile.addItem(new String[]{string});
				}
			}
		});
		initialize();
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		cmbNormalizedType.setSelectedIndex(0);
		cmbArrayType.setSelectedIndex(0);
	}
}
