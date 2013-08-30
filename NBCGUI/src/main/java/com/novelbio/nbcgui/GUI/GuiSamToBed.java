package com.novelbio.nbcgui.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import com.google.common.collect.ArrayListMultimap;
import com.novelbio.analysis.seq.bed.BedSeq;
import com.novelbio.analysis.seq.sam.AlignSamReading;
import com.novelbio.analysis.seq.sam.SamFile;
import com.novelbio.analysis.seq.sam.SamToBed;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.gui.GUIFileOpen;
import com.novelbio.base.gui.JComboBoxData;
import com.novelbio.base.gui.JScrollPaneData;
import com.novelbio.database.model.species.Species;

public class GuiSamToBed extends JPanel {
	private static final long serialVersionUID = 2596513239050914728L;

	private JTextField txtExtend;

	JCheckBox chckbxExtend;
	JCheckBox chckbxFilterreads;
	JCheckBox chckbxCis;
	JCheckBox chckbxTrans;
	JCheckBox chckbxSortBed;
	JCheckBox chckbxSortBam;
	JCheckBox chckbxIndex;
	JCheckBox chckRecalibrate;
	JCheckBox chckRealign;
	JCheckBox chckRemoveduplicate;
	JCheckBox chckbxMergebyprefix;
	JCheckBox chckbxAddGroupInfo;
	
	JButton btnSamtobed;
	JButton btnAddvcf;
	JButton btnDelvcf;
	
	JSpinner spinMapNumSmall;
	JSpinner spinMapNumBig;
	
	JScrollPaneData scrlSamFile;
	JScrollPaneData scrlBedFile;
	
	ButtonGroup buttonGroupRad;
	JRadioButton radGenome;
	JRadioButton radRefRNA;
	JScrollPaneData sclVcfFile;
	
	JButton btnRefseqFile;
	
	GUIFileOpen guiFileOpen = new GUIFileOpen();
	private JLabel lblTo;
	private JCheckBox chckbxNonUniqueMapping;
	private JComboBoxData<Species> cmbSpecies;
	JComboBoxData<String> cmbVersion;
	private JTextField txtReferenceSequence;
	private JCheckBox chckbxGeneratepileupfile;
	/**
	 * Create the panel.
	 */
	public GuiSamToBed() {
		setLayout(null);
		
		JButton btnNewButton = new JButton("OpenSamFile");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> lsFileName = guiFileOpen.openLsFileName("SamBam", "");
				ArrayList<String[]> lsToScrlFile = new ArrayList<String[]>();
				for (String string : lsFileName) {
					String prefix = FileOperate.getFileNameSep(string)[0].split("_")[0];
					String[] strings = new String[]{string, prefix};
					lsToScrlFile.add(strings);
				}
				scrlSamFile.addItemLs(lsToScrlFile);
			}
		});
		btnNewButton.setBounds(12, 166, 157, 24);
		add(btnNewButton);
		
		JButton btnConvertSam = new JButton("ConvertSam");
		btnConvertSam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ((chckRecalibrate.isSelected() || chckbxGeneratepileupfile.isSelected()) && cmbSpecies.getSelectedValue().getTaxID() == 0) {
					JOptionPane.showConfirmDialog(null, "ReAlign And PileUp Need the Species Info", "Warning", JOptionPane.CLOSED_OPTION);
					return;
				}
				
				ArrayList<String[]> lsInfo = scrlSamFile.getLsDataInfo();
				ArrayListMultimap<String, String> mapPrefix2FileName = ArrayListMultimap.create();
				HashSet<String> setTmp = new HashSet<String>();
				for (String[] strings : lsInfo) {
					if (FileOperate.isFileExist(strings[0])) {
						String prefix = getPrefix(strings[1], setTmp);
						mapPrefix2FileName.put(prefix, strings[0]);
					}
				}
				
				String resultMergePath = null;
				for (String prefix : mapPrefix2FileName.keySet()) {
					List<String> lsSamFiles = mapPrefix2FileName.get(prefix);
					if (lsSamFiles.size() > 1) {
						resultMergePath = guiFileOpen.saveFileName("", "");
						if (FileOperate.isFileDirectory(resultMergePath)) {
							resultMergePath = FileOperate.addSep(resultMergePath);
						}
						break;
					}
				}
				
				for (String prefix : mapPrefix2FileName.keySet()) {
					List<String> lsSamFiles = mapPrefix2FileName.get(prefix);
					convertSamFile(resultMergePath, prefix, lsSamFiles);
				}
			}
			
			/** 如果prefix为null或""，则返回一个全新的prefix，意思不在任何分组中 */
			private String getPrefix(String prefixOld, HashSet<String> setTmp) {
				if (prefixOld != null && !prefixOld.equals("")) {
					return prefixOld;
				}
				int i = 0;
				while (setTmp.contains(i + "")) {
					i++;
				}
				return i + "";
			}
		});
		btnConvertSam.setBounds(407, 516, 157, 24);
		add(btnConvertSam);
		
		JLabel lblSamfile = new JLabel("SamToBam");
		lblSamfile.setBounds(12, 21, 121, 14);
		add(lblSamfile);
		
		JButton btnBedFile = new JButton("OpenBedFile");
		btnBedFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					ArrayList<String> lsFileName = guiFileOpen.openLsFileName("bed", "");
					ArrayList<String[]> lsToScrlFile = new ArrayList<String[]>();
					for (String string : lsFileName) {
						String[] strings = new String[]{string};
						lsToScrlFile.add(strings);
					}
					scrlBedFile.addItemLs(lsToScrlFile);
				}
		});
		btnBedFile.setBounds(623, 169, 128, 24);
		add(btnBedFile);
		
		JLabel lblBedfile = new JLabel("BedFile");
		lblBedfile.setBounds(618, 25, 69, 14);
		add(lblBedfile);
		
		txtExtend = new JTextField();
		txtExtend.setBounds(709, 208, 114, 18);
		add(txtExtend);
		txtExtend.setColumns(10);
		
		JButton btnConvertBed = new JButton("ConvertBed");
		btnConvertBed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<String[]> lsInfo = scrlBedFile.getLsDataInfo();
				for (String[] strings : lsInfo) {
					if (FileOperate.isFileExist(strings[0])) {
						convertBedFile(strings[0]);
					}
				}
			}
		});
		btnConvertBed.setBounds(885, 303, 147, 24);
		add(btnConvertBed);
		
		JLabel lblMappingnum = new JLabel("MappingNum");
		lblMappingnum.setBounds(627, 308, 99, 14);
		add(lblMappingnum);
		
		lblTo = new JLabel("To");
		lblTo.setBounds(776, 309, 30, 14);
		add(lblTo);
		
		chckbxNonUniqueMapping = new JCheckBox("Non Unique Mapping Get Random Reads");
		chckbxNonUniqueMapping.setBounds(13, 553, 320, 22);
		add(chckbxNonUniqueMapping);
		
		chckbxExtend = new JCheckBox("Extend");
		chckbxExtend.setBounds(621, 204, 82, 22);
		add(chckbxExtend);
		
		chckbxFilterreads = new JCheckBox("FilterReads");
		chckbxFilterreads.setBounds(620, 233, 131, 22);
		add(chckbxFilterreads);
		
		chckbxCis = new JCheckBox("Cis");
		chckbxCis.setBounds(755, 233, 53, 22);
		add(chckbxCis);
		
		chckbxTrans = new JCheckBox("Trans");
		chckbxTrans.setBounds(822, 233, 69, 22);
		add(chckbxTrans);
		
		chckbxSortBed = new JCheckBox("SortBed");
		chckbxSortBed.setBounds(620, 267, 131, 22);
		add(chckbxSortBed);
		
		chckbxSortBam = new JCheckBox("sortBam");
		chckbxSortBam.setBounds(295, 198, 91, 22);
		add(chckbxSortBam);
		
		chckbxIndex = new JCheckBox("index");
		chckbxIndex.setBounds(393, 198, 69, 22);
		add(chckbxIndex);
		
		
		chckRealign = new JCheckBox("Realign");
		chckRealign.setBounds(161, 224, 91, 22);
		add(chckRealign);
		
		chckRemoveduplicate = new JCheckBox("RemoveDuplicate");
		chckRemoveduplicate.setBounds(12, 224, 147, 22);
		add(chckRemoveduplicate);
		
		
		chckRecalibrate = new JCheckBox("Recalibrate Need DBsnpVcf");
		chckRecalibrate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (chckRecalibrate.isSelected()) {
					sclVcfFile.setEnabled(true);
					btnAddvcf.setEnabled(true);
					btnDelvcf.setEnabled(true);
				} else {
					sclVcfFile.setEnabled(false);
					btnAddvcf.setEnabled(false);
					btnDelvcf.setEnabled(false);
				}
			}
		});
		chckRecalibrate.setBounds(295, 224, 231, 22);
		add(chckRecalibrate);
		
		btnSamtobed = new JButton("SamtoBed");
		btnSamtobed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<String[]> lsInfo = scrlSamFile.getLsDataInfo();
				for (String[] strings : lsInfo) {
					if (FileOperate.isFileExist(strings[0])) {
						samToBed(strings[0]);
					}
				}
			}
		});
		btnSamtobed.setBounds(446, 552, 118, 24);
		add(btnSamtobed);
		
		spinMapNumSmall = new JSpinner();
		spinMapNumSmall.setBounds(724, 307, 49, 18);
		add(spinMapNumSmall);
		
		spinMapNumBig = new JSpinner();
		spinMapNumBig.setBounds(808, 307, 49, 18);
		add(spinMapNumBig);
		
		scrlBedFile = new JScrollPaneData();
		scrlBedFile.setBounds(618, 44, 416, 109);
		add(scrlBedFile);
		
		JButton btnDelBed = new JButton("DeleteBed");
		btnDelBed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scrlBedFile.deleteSelRows();
			}
		});
		btnDelBed.setBounds(881, 171, 153, 24);
		add(btnDelBed);
		
		scrlSamFile = new JScrollPaneData();
		scrlSamFile.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getID() == KeyEvent.VK_DELETE) {
					scrlSamFile.deleteSelRows();
				}
			}
		});
		scrlSamFile.setBounds(13, 38, 552, 116);
		add(scrlSamFile);
		
		JButton btnDelScrSam = new JButton("DeleteSam");
		btnDelScrSam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scrlSamFile.deleteSelRows();
			}
		});
		btnDelScrSam.setBounds(407, 168, 157, 24);
		add(btnDelScrSam);
		
		cmbSpecies = new JComboBoxData<Species>();
		cmbSpecies.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectCombSpecies();
			}
		});
		cmbSpecies.setBounds(14, 300, 171, 23);
		add(cmbSpecies);
		
		JLabel lblSpecies = new JLabel("Species");
		lblSpecies.setBounds(16, 282, 69, 14);
		add(lblSpecies);
		
		cmbVersion = new JComboBoxData<String>();
		cmbVersion.setBounds(14, 345, 171, 23);
		add(cmbVersion);
		
		JLabel lblVersion = new JLabel("Version");
		lblVersion.setBounds(13, 327, 69, 14);
		add(lblVersion);
		
		txtReferenceSequence = new JTextField();
		txtReferenceSequence.setBounds(322, 303, 231, 18);
		add(txtReferenceSequence);
		txtReferenceSequence.setColumns(10);
		
		btnRefseqFile = new JButton("RefseqFile");
		btnRefseqFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtReferenceSequence.setText(guiFileOpen.openFileName("refseq", ""));
			}
		});
		btnRefseqFile.setBounds(439, 331, 118, 24);
		add(btnRefseqFile);
		
		radGenome = new JRadioButton("Genome");
		radGenome.setBounds(207, 306, 91, 22);
		add(radGenome);
		
		radRefRNA = new JRadioButton("RNA");
		radRefRNA.setBounds(208, 337, 69, 22);
		add(radRefRNA);
		
		chckbxGeneratepileupfile = new JCheckBox("GeneratePileUpFile");
		chckbxGeneratepileupfile.setBounds(12, 250, 187, 22);
		add(chckbxGeneratepileupfile);

		sclVcfFile = new JScrollPaneData();
		sclVcfFile.setBounds(16, 407, 548, 97);
		add(sclVcfFile);
		
		JLabel lblVcffile = new JLabel("VCF To Help Recalibrate");
		lblVcffile.setBounds(16, 381, 248, 14);
		add(lblVcffile);
		
		btnAddvcf = new JButton("AddVcf");
		btnAddvcf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<String> lsVCFfile = guiFileOpen.openLsFileName("vcf", "vcf");
				sclVcfFile.addItemLsSingle(lsVCFfile);
			}
		});
		btnAddvcf.setBounds(15, 516, 118, 24);
		add(btnAddvcf);
		
		btnDelvcf = new JButton("DelVcf");
		btnDelvcf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sclVcfFile.deleteSelRows();
			}
		});
		btnDelvcf.setBounds(173, 516, 118, 24);
		add(btnDelvcf);
		
		chckbxMergebyprefix = new JCheckBox("MergeByPrefix");
		chckbxMergebyprefix.setSelected(true);
		chckbxMergebyprefix.setBounds(12, 198, 140, 23);
		add(chckbxMergebyprefix);
		
		chckbxAddGroupInfo = new JCheckBox("add Group Info");
		chckbxAddGroupInfo.setBounds(161, 198, 128, 26);
		add(chckbxAddGroupInfo);
		initial();
	}
	
	private void initial() {
		buttonGroupRad = new ButtonGroup();
		buttonGroupRad.add(radGenome);
		buttonGroupRad.add(radRefRNA);
		
		scrlSamFile.setTitle(new String[]{"SamBamFile", "prefix"});
		scrlBedFile.setTitle(new String[]{"BedFile"});
		
		radGenome.setSelected(true);
		
		cmbSpecies.setMapItem(Species.getSpeciesName2Species(Species.SEQINFO_SPECIES));
		
		selectCombSpecies();
	}
	
	private void selectRealign() {
		if (chckRecalibrate.isSelected()) {
			chckbxSortBam.setEnabled(false);
			chckbxIndex.setEnabled(false);
			cmbSpecies.setEnabled(true);
			cmbVersion.setEnabled(true);
			radGenome.setEnabled(true);
			radRefRNA.setEnabled(true);
			txtReferenceSequence.setEnabled(true);
			btnRefseqFile.setEnabled(true);
			selectCombSpecies();
		}
		else {
			chckbxSortBam.setEnabled(true);
			chckbxIndex.setEnabled(true);
			cmbSpecies.setEnabled(false);
			cmbVersion.setEnabled(false);
			radGenome.setEnabled(false);
			radRefRNA.setEnabled(false);
			txtReferenceSequence.setEnabled(false);
			btnRefseqFile.setEnabled(false);		
		}
	}
	
	private void selectCombSpecies() {
		Species species = cmbSpecies.getSelectedValue();
		if (species.getTaxID() == 0) {
			radGenome.setEnabled(false);
			radRefRNA.setEnabled(false);
			cmbVersion.setEnabled(false);
			txtReferenceSequence.setEnabled(true);
			btnRefseqFile.setEnabled(true);
		} 
		else {
			radGenome.setEnabled(true);
			radRefRNA.setEnabled(true);
			cmbVersion.setEnabled(true);
			txtReferenceSequence.setEnabled(false);
			btnRefseqFile.setEnabled(false);
			radGenome.setSelected(true);
			cmbVersion.setMapItem(species.getMapVersion());
		}
	}
	private void samToBed(String samFilestr) {
		SamFile samFile = new SamFile(samFilestr);
		AlignSamReading alignSamReading = new AlignSamReading(samFile);
		SamToBed samToBed = new SamToBed(samFile);
		samToBed.setUniqueRandomSelectOneRead(chckbxNonUniqueMapping.isSelected());
		alignSamReading.addAlignmentRecorder(samToBed);
		alignSamReading.run();
		BedSeq bedSeq = samToBed.getBedSeq();
		bedSeq.close();
	}

	private void convertSamFile(String resultMergePath, String prefix, List<String> lsSamFilestr) {
		String refFile = "";
		Species species = cmbSpecies.getSelectedValue();
		
		if (species.getTaxID() == 0) {
			refFile = txtReferenceSequence.getText();
		} else {
			species.setVersion(cmbVersion.getSelectedValue());
			if (radGenome.isSelected()) {
				refFile = species.getChromSeq();
			} else if (radRefRNA.isSelected()) {
				refFile = species.getRefseqFile();
			}
		}
		
		List<SamFile> lsSamFiles = new ArrayList<SamFile>(); 
		for (String string : lsSamFilestr) {
			SamFile samFile = new SamFile(string);
			samFile.setReferenceFileName(refFile);
			lsSamFiles.add(samFile);
		}
		List<SamFile> lsBamFile = new ArrayList<SamFile>();
		for (SamFile samFile : lsSamFiles) {
			lsBamFile.add(samFile.convertToBam());
		}
		
		if (chckbxMergebyprefix.isSelected()) {
			SamFile samFileMerge = mergeSamFile(resultMergePath, prefix, lsBamFile);
			lsBamFile.clear();
			lsBamFile.add(samFileMerge);
		}
		for (int i = 0; i < lsBamFile.size(); i++) {
			SamFile samFileMerge = lsBamFile.get(i);
			if (lsBamFile.size() == 1) {
				copeSamBamFile(prefix, samFileMerge);
			} else {
				copeSamBamFile(prefix + "_" + (i+1), samFileMerge);
			}
		}
	}
	
	/**
	 * 将输入的文件转化为bam文件，并合并
	 * @param prefix
	 * @param lsSamFile
	 * @return
	 */
	private SamFile mergeSamFile(String resultPath, String prefix, List<SamFile> lsSamFile) {
		if (lsSamFile.size() == 1) {
			return lsSamFile.get(0);
		}
		
		String resultName = resultPath + prefix;
		resultName = FileOperate.changeFileSuffix(resultName, "_merge", "bam");
		SamFile samFileMerge = SamFile.mergeBamFile(resultName , lsSamFile);
		return samFileMerge;
	}
	
	
	
	private void copeSamBamFile(String prefix, SamFile samFileMerge) {
		if (chckbxAddGroupInfo.isSelected()) {
			samFileMerge = samFileMerge.addGroup(prefix, prefix, prefix, null);
		}
		if (chckbxSortBam.isSelected()) {
			samFileMerge = samFileMerge.sort();
		}
		if (chckbxIndex.isSelected()) {
			samFileMerge.indexMake();
		}
		if (chckRemoveduplicate.isSelected()) {
			samFileMerge = samFileMerge.removeDuplicate();
			if (samFileMerge == null) {
				return;
			}
		}
		if (chckRealign.isSelected()) {
			samFileMerge = samFileMerge.addGroup(prefix, prefix, prefix, "ILLUMINA");
			samFileMerge = samFileMerge.realign();
			if (samFileMerge == null) {
				return;
			}
		}
		if (chckRecalibrate.isSelected()) {
			List<String[]> lsInfo = sclVcfFile.getLsDataInfo();
			List<String> lsVcfFile = new ArrayList<String>();
			for (String[] strings : lsInfo) {
				if (FileOperate.isFileExistAndBigThanSize(strings[0], 0)) {
					lsVcfFile.add(strings[0]);
				}
			}
			if (lsVcfFile.size() != 0) {
				samFileMerge = samFileMerge.recalibrate(lsVcfFile);
				if (samFileMerge == null) {
					return;
				};
			}
		}
		if (chckbxGeneratepileupfile.isSelected()) {
			samFileMerge.pileup();
		}
	}
	
	private void convertBedFile(String bedFile) {
		BedSeq bedSeq = new BedSeq(bedFile);
		if (chckbxExtend.isSelected()) {
			int extendLen = 250;
			try { extendLen = Integer.parseInt(txtExtend.getText()); } catch (Exception e2) { }
			bedSeq = bedSeq.extend(extendLen);
		}
		if (chckbxFilterreads.isSelected()) {
			Boolean strand = null;
			if (chckbxCis.isSelected()) {
				strand = true;
			}
			else if (chckbxTrans.isSelected()) {
				strand = false;
			}
			int small = 1;
			try { small = (Integer)spinMapNumSmall.getValue(); } catch (Exception e2) { e2.printStackTrace();}
			int big = 1;
			try { big =  (Integer)spinMapNumBig.getValue(); } catch (Exception e2) { }
			bedSeq = bedSeq.filterSeq(small, big, strand);
		}
		if (chckbxSortBed.isSelected()) {
			bedSeq.sort();
		}
	
	}
}
