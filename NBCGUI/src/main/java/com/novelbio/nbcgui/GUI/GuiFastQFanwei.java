package com.novelbio.nbcgui.GUI;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.novelbio.analysis.seq.fastq.FastQ;
import com.novelbio.analysis.seq.mapping.MapLibrary;
import com.novelbio.base.FoldeCreate;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.gui.GUIFileOpen;
import com.novelbio.base.gui.JComboBoxData;
import com.novelbio.base.gui.JScrollPaneData;
import com.novelbio.database.domain.information.SoftWareInfo.SoftWare;
import com.novelbio.database.model.species.Species;
import com.novelbio.database.service.SpringFactoryBioinfo;
import com.novelbio.nbcgui.GUI.GuiLayeredPanSpeciesVersion.SpeciesSelect;
import com.novelbio.nbcgui.controlseq.CtrlDNAMapping;
import com.novelbio.nbcgui.controlseq.CtrlFastQ;

import javax.swing.JSpinner;

public class GuiFastQFanwei extends JPanel {
	
	private JTextField txtMinReadsLen;
	private JTextField txtSavePathAndPrefix;
	GUIFileOpen guiFileOpen = new GUIFileOpen();
	private final ButtonGroup groupLibrary = new ButtonGroup();
	JScrollPaneData scrollPaneFastqLeft;
	JScrollPaneData scrollPaneFastqRight;
	private JTextField txtRightAdaptor;
	private JTextField txtLeftAdaptor;
	private JTextField txtMisMatch;
	private JTextField txtGapLength;
	private JTextField txtThreadNum;
	JCheckBox chckbxFilterreads;
	JCheckBox chckbxTrimEnd;
	
	JCheckBox chckbxMapping;
	JCheckBox chckbxQcbeforefilter;
	JCheckBox chckbxQcafterFilter;
	
	JComboBoxData<String> cmbReadsQuality;
	JComboBoxData<MapLibrary> cmbLibrary;
	JButton btnSaveto;
	JButton btnOpenFastqLeft;
	JButton btnDelFastqLeft;
	JButton btnRun;
	JButton btnOpenFastQRight;
	JButton btnDeleteFastQRight;
	GuiLayeredPanSpeciesVersion speciesLayOut;
	
	ButtonGroup buttonGroupMappingTo = new ButtonGroup();
	
	JCheckBox chckbxLowcaseAdaptor;
	JCheckBox chkJustQC;
//	CtrlFastQMapping ctrlFastQMapping = new CtrlFastQMapping();
	CtrlFastQ ctrlFastQ;
	CtrlDNAMapping ctrlDNAMapping;
	
	
	JComboBoxData<SoftWare> cmbMappingSoftware;
	ArrayList<Component> lsComponentsMapping = new ArrayList<Component>();
	ArrayList<Component> lsComponentsFiltering = new ArrayList<Component>();
	JSpinner spnTrimNNNcutoff;
	
	public GuiFastQFanwei() {
		setLayout(null);
		
		JLabel lblFastqfile = new JLabel("FastQFile");
		lblFastqfile.setBounds(10, 10, 68, 14);
		add(lblFastqfile);
		
		scrollPaneFastqLeft = new JScrollPaneData();
		scrollPaneFastqLeft.setBounds(10, 30, 371, 186);
		scrollPaneFastqLeft.setTitle(new String[]{"FileName","Prix"});
		add(scrollPaneFastqLeft);
		
		scrollPaneFastqRight = new JScrollPaneData();
		scrollPaneFastqRight.setBounds(487, 30, 322, 191);
		scrollPaneFastqRight.setTitle(new String[]{"FileName"});
		add(scrollPaneFastqRight);
		
		btnOpenFastqLeft = new JButton("Open");
		btnOpenFastqLeft.setBounds(393, 38, 82, 24);
		add(btnOpenFastqLeft);
		
		chckbxFilterreads = new JCheckBox("FilterReads");
		chckbxFilterreads.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!chckbxFilterreads.isSelected()) {
					for (Component component : lsComponentsFiltering) {
						component.setEnabled(false);
					}
				}
				else {
					for (Component component : lsComponentsFiltering) {
						component.setEnabled(true);
					}
				}
			}
		});
		chckbxFilterreads.setBounds(10, 226, 108, 22);
		add(chckbxFilterreads);
		
		chckbxTrimEnd = new JCheckBox("TrimEndQuality");
		chckbxTrimEnd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (chckbxTrimEnd.isSelected()) {
					spnTrimNNNcutoff.setVisible(true);
				} else {
					spnTrimNNNcutoff.setVisible(false);
				}
			}
		});
		chckbxTrimEnd.setSelected(true);
		chckbxTrimEnd.setBounds(347, 226, 128, 22);
		add(chckbxTrimEnd);
		
		txtMinReadsLen = new JTextField();
		txtMinReadsLen.setText("50");
		txtMinReadsLen.setBounds(140, 282, 76, 18);
		add(txtMinReadsLen);
		txtMinReadsLen.setColumns(10);
		
		cmbReadsQuality = new JComboBoxData<String>();
		cmbReadsQuality.setMapItem(FastQ.getMapReadsQuality());
		cmbReadsQuality.setBounds(140, 252, 153, 23);
		add(cmbReadsQuality);
		
		JLabel lblReadsQuality = new JLabel("Reads Quality");
		lblReadsQuality.setBounds(10, 256, 114, 14);
		add(lblReadsQuality);
		
		JLabel lblRetainBp = new JLabel("Retain Bp");
		lblRetainBp.setBounds(10, 284, 108, 14);
		add(lblRetainBp);
		
		chckbxMapping = new JCheckBox("Mapping");
		chckbxMapping.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!chckbxMapping.isSelected()) {
					for (Component component : lsComponentsMapping) {
						component.setEnabled(false);
					}
				} else {
					for (Component component : lsComponentsMapping) {
						component.setEnabled(true);
					}
				}
			}
		});
		chckbxMapping.setBounds(8, 346, 164, 22);
		add(chckbxMapping);
		
		JLabel lblAlgrethm = new JLabel("algrethm");
		lblAlgrethm.setBounds(12, 187, 66, 14);
		add(lblAlgrethm);
		//初始化cmbSpeciesVersion
		try {} catch (Exception e) { }
		
		JLabel lblExtendto = new JLabel("ExtendTo");
		lblExtendto.setBounds(17, 450, -137, -132);
		add(lblExtendto);
		
		txtSavePathAndPrefix = new JTextField();
		txtSavePathAndPrefix.setBounds(10, 556, 337, 24);
		add(txtSavePathAndPrefix);
		txtSavePathAndPrefix.setColumns(10);
		
		JLabel lblResultpath = new JLabel("ResultPath");
		lblResultpath.setBounds(10, 532, 80, 14);
		add(lblResultpath);
		
		btnSaveto = new JButton("SaveTo");
		btnSaveto.setBounds(387, 556, 88, 24);
		btnSaveto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String filePathName = guiFileOpen.openFilePathName("", "");
				txtSavePathAndPrefix.setText(filePathName);
			}
		});
		add(btnSaveto);
		
		btnDelFastqLeft = new JButton("Delete");
		btnDelFastqLeft.setBounds(393, 74, 82, 24);
		
		btnDelFastqLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scrollPaneFastqLeft.deleteSelRows();
			}
		});
		add(btnDelFastqLeft);
		
		btnRun = new JButton("Run");
		btnRun.setBounds(653, 556, 118, 24);
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ctrlFastQ = (CtrlFastQ)SpringFactoryBioinfo.getFactory().getBean("ctrlFastQ");
				ArrayList<String[]> lsInfoLeftAndPrefix = scrollPaneFastqLeft.getLsDataInfo();
				ArrayList<String[]> lsInfoRight = scrollPaneFastqRight.getLsDataInfo();
				ArrayList<String> lsLeftFq = new ArrayList<String>();
				ArrayList<String> lsPrefix = new ArrayList<String>();
				ArrayList<String> lsRightFq = new ArrayList<String>();
				for (String[] strings : lsInfoLeftAndPrefix) {
					lsLeftFq.add(strings[0]);
					lsPrefix.add(strings[1]);
				}
				for (String[] string : lsInfoRight) {
					lsRightFq.add(string[0]);	
				}
				ctrlFastQ.setLsFastQfileLeft(lsLeftFq);
				ctrlFastQ.setLsFastQfileRight(lsRightFq);
				ctrlFastQ.setLsPrefix(lsPrefix);
				ctrlFastQ.setFilter(false);
				ctrlFastQ.setCheckFormat(true);
				ctrlFastQ.setJustFastqc(chkJustQC.isSelected());
				String outFilePrefix = FoldeCreate.createAndInFold(txtSavePathAndPrefix.getText(), "QualityControl_result");//TODO
				
				if (chckbxFilterreads.isSelected()) {
					ctrlFastQ.setFilter(true);
					ctrlFastQ.setAdaptorLeft(txtLeftAdaptor.getText());
					ctrlFastQ.setAdaptorRight(txtRightAdaptor.getText());
					ctrlFastQ.setAdaptorLowercase(chckbxLowcaseAdaptor.isSelected());
					ctrlFastQ.setFastqQuality(cmbReadsQuality.getSelectedValue());
					ctrlFastQ.setTrimNNN(chckbxTrimEnd.isSelected(), (int)spnTrimNNNcutoff.getValue());
					ctrlFastQ.setOutFilePrefix(outFilePrefix);
//					ctrlFastQ.setOutFilePrefix(txtSavePathAndPrefix.getText());
					ctrlFastQ.setFastQC(chckbxQcbeforefilter.isSelected(), chckbxQcafterFilter.isSelected());
					try { ctrlFastQ.setReadsLenMin(Integer.parseInt(txtMinReadsLen.getText())); } catch (Exception e2) { }
					ctrlFastQ.running();
				}

				if (chckbxMapping.isSelected()) {
					ctrlDNAMapping = new CtrlDNAMapping();
					ctrlDNAMapping.setMapCondition2CombFastQLRFiltered(ctrlFastQ.getFilteredMap());
					try { ctrlDNAMapping.setGapLen(Integer.parseInt(txtGapLength.getText())); } catch (Exception e2) { 	}
					try { ctrlDNAMapping.setMismatch(Double.parseDouble(txtMisMatch.getText())); } catch (Exception e2) {}
					try { ctrlDNAMapping.setThread(Integer.parseInt(txtThreadNum.getText())); } catch (Exception e2) { 	}
					//TODO
					ctrlDNAMapping.setLibraryType(cmbLibrary.getSelectedValue());
					ctrlDNAMapping.setSoftMapping(cmbMappingSoftware.getSelectedValue());
					Species species = speciesLayOut.getSelectSpecies();
					ctrlDNAMapping.setSpecies(species, Species.CHROM);
					ctrlDNAMapping.setOutFilePrefix(outFilePrefix);
//					ctrlDNAMapping.setOutFilePrefix(txtSavePathAndPrefix.getText());
					ctrlDNAMapping.running();
				}
				JOptionPane.showConfirmDialog(null, "Finished", "ok", JOptionPane.CLOSED_OPTION);
			}
		});
		add(btnRun);
		
		btnOpenFastQRight = new JButton("Open");
		btnOpenFastQRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> lsFileRigth = guiFileOpen.openLsFileName("fastqFile", "");
				for (String string : lsFileRigth) {
					scrollPaneFastqRight.addItem(new String[]{string});
				}
				cmbLibrary.setSelectVaule(MapLibrary.PairEnd);
			}
		});
		btnOpenFastQRight.setBounds(821, 38, 86, 24);
		add(btnOpenFastQRight);

		btnDeleteFastQRight = new JButton("Delete");
		btnDeleteFastQRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scrollPaneFastqRight.deleteSelRows();
				if (scrollPaneFastqRight.getLsDataInfo().size() == 0) {
					cmbLibrary.setSelectVaule(MapLibrary.SingleEnd);
				}
			}
		});
		btnDeleteFastQRight.setBounds(821, 74, 86, 24);
		add(btnDeleteFastQRight);
		
		JLabel lblLeftadaptor = new JLabel("LeftAdaptor");
		lblLeftadaptor.setBounds(366, 256, 109, 14);
		add(lblLeftadaptor);
		
		txtRightAdaptor = new JTextField();
		txtRightAdaptor.setBounds(471, 282, 166, 18);
		add(txtRightAdaptor);
		txtRightAdaptor.setColumns(10);
		
		JLabel lblRightadaptor = new JLabel("RightAdaptor");
		lblRightadaptor.setBounds(366, 284, 109, 14);
		add(lblRightadaptor);
		
		txtLeftAdaptor = new JTextField();
		txtLeftAdaptor.setBounds(471, 256, 166, 18);
		add(txtLeftAdaptor);
		txtLeftAdaptor.setColumns(10);
		
		JLabel lblMismatch = new JLabel("Mismatch");
		lblMismatch.setBounds(327, 350, 80, 14);
		add(lblMismatch);
		
		txtMisMatch = new JTextField();
		txtMisMatch.setText("5");
		txtMisMatch.setBounds(417, 348, 68, 18);
		add(txtMisMatch);
		txtMisMatch.setColumns(10);
		
		JLabel lblGaplength = new JLabel("GapLength");
		lblGaplength.setBounds(514, 350, 95, 14);
		add(lblGaplength);
		
		txtGapLength = new JTextField();
		txtGapLength.setText("30");
		txtGapLength.setBounds(604, 348, 114, 18);
		add(txtGapLength);
		txtGapLength.setColumns(10);
		
		JLabel lblThread = new JLabel("Thread");
		lblThread.setBounds(9, 504, 85, 14);
		add(lblThread);
		
		txtThreadNum = new JTextField();
		txtThreadNum.setBounds(83, 502, 114, 18);
		add(txtThreadNum);
		txtThreadNum.setColumns(10);
		
		chckbxLowcaseAdaptor = new JCheckBox("LowerCase Adaptor");
		chckbxLowcaseAdaptor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (chckbxLowcaseAdaptor.isSelected()) {
					txtLeftAdaptor.setText("");
					txtLeftAdaptor.setEnabled(false);
					txtRightAdaptor.setText("");
					txtRightAdaptor.setEnabled(false);
				}
				else {
					txtLeftAdaptor.setText("");
					txtLeftAdaptor.setEnabled(true);
					txtRightAdaptor.setText("");
					txtRightAdaptor.setEnabled(true);
				}
			}
		});
		chckbxLowcaseAdaptor.setBounds(705, 252, 239, 22);
		add(chckbxLowcaseAdaptor);
		
		cmbLibrary = new JComboBoxData<MapLibrary>();
		cmbLibrary.setMapItem(MapLibrary.getMapLibrary());
		cmbLibrary.setBounds(471, 404, 134, 23);
		add(cmbLibrary);
		
		JLabel lblLibrary = new JLabel("Library");
		lblLibrary.setBounds(471, 378, 69, 14);
		add(lblLibrary);
		
		speciesLayOut = new GuiLayeredPanSpeciesVersion();
		speciesLayOut.setSelectSpecies(null);
		speciesLayOut.setBounds(175, 376, 232, 101);
		add(speciesLayOut);
		
		cmbMappingSoftware = new JComboBoxData<SoftWare>();
		cmbMappingSoftware.setBounds(10, 390, 153, 23);
		add(cmbMappingSoftware);
		
		chckbxQcbeforefilter = new JCheckBox("QC Before Filter");
		chckbxQcbeforefilter.setSelected(true);
		chckbxQcbeforefilter.setBounds(10, 308, 162, 23);
		add(chckbxQcbeforefilter);
		
		chckbxQcafterFilter = new JCheckBox("QC After Filter");
		chckbxQcafterFilter.setBounds(175, 308, 140, 23);
		add(chckbxQcafterFilter);
		
		spnTrimNNNcutoff = new JSpinner();
		spnTrimNNNcutoff.setBounds(487, 226, 66, 22);
		spnTrimNNNcutoff.setValue(15);
		add(spnTrimNNNcutoff);
		
		chkJustQC = new JCheckBox("JustQC");
		chkJustQC.setBounds(319, 306, 148, 26);
		add(chkJustQC);
		
		
		btnOpenFastqLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> lsFileLeft = guiFileOpen.openLsFileName("fastqFile", "");
				for (String string : lsFileLeft) {
					String filePrefix = FileOperate.getFileNameSep(string)[0].split("_")[0];
					scrollPaneFastqLeft.addItem(new String[]{string, filePrefix});
				}
			}
		});
		initialize();
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		lsComponentsFiltering.add(chckbxTrimEnd);
		lsComponentsFiltering.add(txtLeftAdaptor);
		lsComponentsFiltering.add(txtRightAdaptor);
		lsComponentsFiltering.add(txtMinReadsLen);
		lsComponentsFiltering.add(cmbReadsQuality);
		lsComponentsFiltering.add(chckbxLowcaseAdaptor);
		lsComponentsFiltering.add(chckbxQcbeforefilter);
		lsComponentsFiltering.add(chckbxQcafterFilter);
		lsComponentsFiltering.add(spnTrimNNNcutoff);
		
		lsComponentsMapping.add(txtGapLength);
		lsComponentsMapping.add(txtMisMatch);
		lsComponentsMapping.add(txtThreadNum);
		lsComponentsMapping.add(txtGapLength);
		lsComponentsMapping.add(cmbLibrary);
		
		chckbxMapping.setSelected(true);
		chckbxFilterreads.setSelected(true);
//		if (speciesLayOut.getSelectSpecies().getTaxID() == 0) {
//			cmbMaptoIndex.setEnabled(false);
//		}
		cmbMappingSoftware.setMapItem(SoftWare.getMapStr2MappingSoftware());
	}
}
