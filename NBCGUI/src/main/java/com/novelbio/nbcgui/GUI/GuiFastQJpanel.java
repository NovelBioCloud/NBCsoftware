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
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.gui.GUIFileOpen;
import com.novelbio.base.gui.JComboBoxData;
import com.novelbio.base.gui.JScrollPaneData;
import com.novelbio.database.domain.information.SoftWareInfo.SoftWare;
import com.novelbio.database.model.species.Species;
import com.novelbio.database.service.SpringFactory;
import com.novelbio.nbcgui.GUI.GuiLayeredPanSpeciesVersion.SpeciesSelect;
import com.novelbio.nbcgui.controlseq.CtrlDNAMapping;
import com.novelbio.nbcgui.controlseq.CtrlFastQ;

public class GuiFastQJpanel extends JPanel {
	
	private JTextField txtMinReadsLen;
	private JTextField txtMappingIndex;
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
	
	JComboBoxData<Integer> cmbReadsQuality;
	JComboBoxData<Integer> cmbMaptoIndex;
	JComboBoxData<MapLibrary> cmbLibrary;
	JButton btnSaveto;
	JButton btnOpenFastqLeft;
	JButton btnDelFastqLeft;
	JButton btnMappingindex;
	JButton btnRun;
	JButton btnOpenFastQRight;
	JButton btnDeleteFastQRight;
	GuiLayeredPanSpeciesVersion speciesLayOut;
	
	ButtonGroup buttonGroupMappingTo = new ButtonGroup();
	
	JCheckBox chckbxLowcaseAdaptor;
//	CtrlFastQMapping ctrlFastQMapping = new CtrlFastQMapping();
	CtrlFastQ ctrlFastQ;
	CtrlDNAMapping ctrlDNAMapping;
	
	
	JComboBoxData<SoftWare> cmbMappingSoftware;
	ArrayList<Component> lsComponentsMapping = new ArrayList<Component>();
	ArrayList<Component> lsComponentsFiltering = new ArrayList<Component>();
	
	public GuiFastQJpanel() {
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
		
		chckbxTrimEnd = new JCheckBox("TrimEnd");
		chckbxTrimEnd.setSelected(true);
		chckbxTrimEnd.setBounds(347, 226, 95, 22);
		add(chckbxTrimEnd);
		
		txtMinReadsLen = new JTextField();
		txtMinReadsLen.setText("50");
		txtMinReadsLen.setBounds(96, 282, 76, 18);
		add(txtMinReadsLen);
		txtMinReadsLen.setColumns(10);
		
		cmbReadsQuality = new JComboBoxData<Integer>();
		cmbReadsQuality.setMapItem(FastQ.getMapReadsQuality());
		cmbReadsQuality.setBounds(121, 252, 153, 23);
		add(cmbReadsQuality);
		
		JLabel lblReadsQuality = new JLabel("Reads Quality");
		lblReadsQuality.setBounds(10, 256, 114, 14);
		add(lblReadsQuality);
		
		JLabel lblRetainBp = new JLabel("Retain Bp");
		lblRetainBp.setBounds(10, 284, 69, 14);
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
		chckbxMapping.setBounds(8, 346, 86, 22);
		add(chckbxMapping);
		
		JLabel lblAlgrethm = new JLabel("algrethm");
		lblAlgrethm.setBounds(12, 187, 66, 14);
		add(lblAlgrethm);
		//初始化cmbSpeciesVersion
		try {} catch (Exception e) { }
		
		txtMappingIndex = new JTextField();
		txtMappingIndex.setBounds(10, 505, 337, 24);
		add(txtMappingIndex);
		txtMappingIndex.setColumns(10);
		
		JLabel lblExtendto = new JLabel("ExtendTo");
		lblExtendto.setBounds(17, 450, -137, -132);
		add(lblExtendto);
		
		txtSavePathAndPrefix = new JTextField();
		txtSavePathAndPrefix.setBounds(10, 556, 337, 24);
		add(txtSavePathAndPrefix);
		txtSavePathAndPrefix.setColumns(10);
		
		JLabel lblResultpath = new JLabel("ResultPath");
		lblResultpath.setBounds(10, 486, 80, 14);
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
		
		JLabel lblMappingToFile = new JLabel("Mapping To File");
		lblMappingToFile.setBounds(10, 541, 121, 14);
		add(lblMappingToFile);
		
		btnMappingindex = new JButton("MappingIndex");
		btnMappingindex.setBounds(387, 505, 134, 24);
		btnMappingindex.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtMappingIndex.setText(guiFileOpen.openFileName("fasta file", ""));
			}
		});
		add(btnMappingindex);
		
		btnRun = new JButton("Run");
		btnRun.setBounds(653, 556, 118, 24);
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ctrlFastQ = (CtrlFastQ)SpringFactory.getFactory().getBean("ctrlFastQ");
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
				if (chckbxFilterreads.isSelected()) {
					ctrlFastQ.setFilter(true);
					ctrlFastQ.setAdaptorLeft(txtLeftAdaptor.getText());
					ctrlFastQ.setAdaptorRight(txtRightAdaptor.getText());
					ctrlFastQ.setAdaptorLowercase(chckbxLowcaseAdaptor.isSelected());
					ctrlFastQ.setFastqQuality(cmbReadsQuality.getSelectedValue());
					ctrlFastQ.setTrimNNN(chckbxTrimEnd.isSelected());
					ctrlFastQ.setOutFilePrefix(txtSavePathAndPrefix.getText());
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
					ctrlDNAMapping.setChrIndexFile(txtMappingIndex.getText());
					//TODO
					ctrlDNAMapping.setLibraryType(cmbLibrary.getSelectedValue());
					ctrlDNAMapping.setSoftMapping(cmbMappingSoftware.getSelectedValue());
					Species species = speciesLayOut.getSelectSpecies();
					ctrlDNAMapping.setSpecies(species, cmbMaptoIndex.getSelectedValue());
					ctrlDNAMapping.setOutFilePrefix(txtSavePathAndPrefix.getText());
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
		lblLeftadaptor.setBounds(298, 260, 109, 14);
		add(lblLeftadaptor);
		
		txtRightAdaptor = new JTextField();
		txtRightAdaptor.setBounds(417, 284, 166, 18);
		add(txtRightAdaptor);
		txtRightAdaptor.setColumns(10);
		
		JLabel lblRightadaptor = new JLabel("RightAdaptor");
		lblRightadaptor.setBounds(298, 286, 109, 14);
		add(lblRightadaptor);
		
		txtLeftAdaptor = new JTextField();
		txtLeftAdaptor.setBounds(417, 254, 166, 18);
		add(txtLeftAdaptor);
		txtLeftAdaptor.setColumns(10);
		
		JLabel lblMismatch = new JLabel("mismatch");
		lblMismatch.setBounds(341, 350, 69, 14);
		add(lblMismatch);
		
		txtMisMatch = new JTextField();
		txtMisMatch.setText("5");
		txtMisMatch.setBounds(417, 348, 68, 18);
		add(txtMisMatch);
		txtMisMatch.setColumns(10);
		
		JLabel lblGaplength = new JLabel("gapLength");
		lblGaplength.setBounds(514, 350, 95, 14);
		add(lblGaplength);
		
		txtGapLength = new JTextField();
		txtGapLength.setText("30");
		txtGapLength.setBounds(604, 348, 114, 18);
		add(txtGapLength);
		txtGapLength.setColumns(10);
		
		JLabel lblThread = new JLabel("Thread");
		lblThread.setBounds(555, 510, 69, 14);
		add(lblThread);
		
		txtThreadNum = new JTextField();
		txtThreadNum.setBounds(614, 508, 114, 18);
		add(txtThreadNum);
		txtThreadNum.setColumns(10);
		
		chckbxLowcaseAdaptor = new JCheckBox("LowCase Adaptor");
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
		chckbxLowcaseAdaptor.setBounds(614, 252, 196, 22);
		add(chckbxLowcaseAdaptor);
		
		cmbLibrary = new JComboBoxData<MapLibrary>();
		cmbLibrary.setMapItem(MapLibrary.getMapLibrary());
		cmbLibrary.setBounds(773, 446, 134, 23);
		add(cmbLibrary);
		
		JLabel lblLibrary = new JLabel("Library");
		lblLibrary.setBounds(773, 420, 69, 14);
		add(lblLibrary);
		
		cmbMaptoIndex = new JComboBoxData<Integer>();
		cmbMaptoIndex.sortValue(true);
		cmbMaptoIndex.setMapItem(CtrlDNAMapping.getMapStr2Index());
		cmbMaptoIndex.setBounds(448, 446, 161, 23);
		add(cmbMaptoIndex);
		
		JLabel lblMappingTo = new JLabel("Mapping To");
		lblMappingTo.setBounds(448, 422, 95, 14);
		add(lblMappingTo);
		
		speciesLayOut = new GuiLayeredPanSpeciesVersion();
		speciesLayOut.setSelectSpecies(new Select());
		speciesLayOut.setBounds(175, 376, 232, 101);
		add(speciesLayOut);
		
		cmbMappingSoftware = new JComboBoxData<SoftWare>();
		cmbMappingSoftware.setBounds(10, 390, 153, 23);
		add(cmbMappingSoftware);
		
		chckbxQcbeforefilter = new JCheckBox("QC before Filter");
		chckbxQcbeforefilter.setSelected(true);
		chckbxQcbeforefilter.setBounds(10, 308, 162, 23);
		add(chckbxQcbeforefilter);
		
		chckbxQcafterFilter = new JCheckBox("QC after Filter");
		chckbxQcafterFilter.setBounds(175, 308, 140, 23);
		add(chckbxQcafterFilter);
		
		
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
	
	class Select implements SpeciesSelect{
		@Override
		public void selectSpecies() {
			if (speciesLayOut.getSelectSpecies().getTaxID() == 0) {
				txtMappingIndex.setEnabled(false);
				btnMappingindex.setEnabled(false);
			} else {
				txtMappingIndex.setEnabled(true);
				btnMappingindex.setEnabled(true);
			}
		}
		
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
		
		
		lsComponentsMapping.add(txtGapLength);
		lsComponentsMapping.add(txtMappingIndex);
		lsComponentsMapping.add(txtMisMatch);
		lsComponentsMapping.add(txtThreadNum);
		lsComponentsMapping.add(txtGapLength);
		lsComponentsMapping.add(btnMappingindex);
		lsComponentsMapping.add(cmbLibrary);
		
		chckbxMapping.setSelected(true);
		chckbxFilterreads.setSelected(true);
		txtMappingIndex.setEnabled(false);
		btnMappingindex.setEnabled(false);
		if (speciesLayOut.getSelectSpecies().getTaxID() == 0) {
			cmbMaptoIndex.setEnabled(false);
		}
		cmbMappingSoftware.setMapItem(SoftWare.getMapStr2MappingSoftware());
	}
}
