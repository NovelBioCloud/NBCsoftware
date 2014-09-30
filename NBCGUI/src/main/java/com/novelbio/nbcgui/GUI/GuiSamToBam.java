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

import net.sf.samtools.SAMFileHeader.SortOrder;

import com.google.common.collect.ArrayListMultimap;
import com.novelbio.analysis.seq.bed.BedSeq;
import com.novelbio.analysis.seq.sam.AlignSamReading;
import com.novelbio.analysis.seq.sam.SamFile;
import com.novelbio.analysis.seq.sam.SamToBamSort;
import com.novelbio.analysis.seq.sam.SamToBed;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.gui.GUIFileOpen;
import com.novelbio.base.gui.JComboBoxData;
import com.novelbio.base.gui.JScrollPaneData;
import com.novelbio.database.model.species.Species;
import com.novelbio.database.model.species.Species.EnumSpeciesType;

public class GuiSamToBam extends JPanel {
	private static final long serialVersionUID = 2596513239050914728L;
	JCheckBox chckbxSortBam;
	JCheckBox chckbxIndex;
	JCheckBox chckRecalibrate;
	JCheckBox chckRealign;
	JCheckBox chckRemoveduplicate;
	JCheckBox chckbxMergebyprefix;
	JButton btnAddvcf;
	JButton btnDelvcf;
	
	JScrollPaneData scrlSamFile;
	
	ButtonGroup buttonGroupRad;
	JScrollPaneData sclVcfFile;
	
	GUIFileOpen guiFileOpen = new GUIFileOpen();
	private JComboBoxData<Species> cmbSpecies;
	JComboBoxData<String> cmbVersion;
	private JCheckBox chckbxGeneratepileupfile;
	/**
	 * Create the panel.
	 */
	public GuiSamToBam() {
		setLayout(null);
		
		JButton btnNewButton = new JButton("Open Sam/Bam File");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> lsFileName = guiFileOpen.openLsFileName("Sam/Bam", "");
				ArrayList<String[]> lsToScrlFile = new ArrayList<String[]>();
				for (String string : lsFileName) {
					String prefix = FileOperate.getFileNameSep(string)[0].split("_")[0];
					String[] strings = new String[]{string, prefix};
					lsToScrlFile.add(strings);
				}
				scrlSamFile.addItemLs(lsToScrlFile);
			}
		});
		btnNewButton.setBounds(38, 166, 187, 24);
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
		btnConvertSam.setBounds(776, 552, 157, 24);
		add(btnConvertSam);
		
		JLabel lblSamfile = new JLabel("BamConvert");
		lblSamfile.setBounds(38, 12, 121, 14);
		add(lblSamfile);
		
		chckbxSortBam = new JCheckBox("SortBam");
		chckbxSortBam.setBounds(385, 200, 139, 22);
		add(chckbxSortBam);
		
		chckbxIndex = new JCheckBox("Index");
		chckbxIndex.setBounds(676, 200, 129, 22);
		add(chckbxIndex);
		
		
		chckRealign = new JCheckBox("Realign");
		chckRealign.setBounds(385, 226, 139, 22);
		add(chckRealign);
		
		chckRemoveduplicate = new JCheckBox("RemoveDuplicate");
		chckRemoveduplicate.setBounds(38, 225, 187, 22);
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
		chckRecalibrate.setBounds(676, 226, 305, 22);
		add(chckRecalibrate);
		
		scrlSamFile = new JScrollPaneData();
		scrlSamFile.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getID() == KeyEvent.VK_DELETE) {
					scrlSamFile.deleteSelRows();
				}
			}
		});
		scrlSamFile.setBounds(38, 38, 895, 116);
		add(scrlSamFile);
		
		JButton btnDelScrSam = new JButton("Delete");
		btnDelScrSam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scrlSamFile.deleteSelRows();
			}
		});
		btnDelScrSam.setBounds(757, 166, 176, 24);
		add(btnDelScrSam);
		
		cmbSpecies = new JComboBoxData<Species>();
		cmbSpecies.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectCombSpecies();
			}
		});
		cmbSpecies.setBounds(104, 295, 238, 23);
		add(cmbSpecies);
		
		JLabel lblSpecies = new JLabel("Species");
		lblSpecies.setBounds(38, 299, 69, 14);
		add(lblSpecies);
		
		cmbVersion = new JComboBoxData<String>();
		cmbVersion.setBounds(455, 295, 222, 23);
		add(cmbVersion);
		
		JLabel lblVersion = new JLabel("Version");
		lblVersion.setBounds(385, 299, 69, 14);
		add(lblVersion);
		
		chckbxGeneratepileupfile = new JCheckBox("GeneratePileUpFile");
		chckbxGeneratepileupfile.setBounds(38, 250, 187, 22);
		add(chckbxGeneratepileupfile);

		sclVcfFile = new JScrollPaneData();
		sclVcfFile.setBounds(38, 407, 895, 97);
		sclVcfFile.setTitle(new String[]{"Vcf file"});
		add(sclVcfFile);
		
		JLabel lblVcffile = new JLabel("VCF To Help Recalibrate");
		lblVcffile.setBounds(38, 381, 248, 14);
		add(lblVcffile);
		
		btnAddvcf = new JButton("AddVcf");
		btnAddvcf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<String> lsVCFfile = guiFileOpen.openLsFileName("vcf", "vcf");
				sclVcfFile.addItemLsSingle(lsVCFfile);
			}
		});
		btnAddvcf.setBounds(38, 516, 118, 24);
		add(btnAddvcf);
		
		btnDelvcf = new JButton("DeleteVcf");
		btnDelvcf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sclVcfFile.deleteSelRows();
			}
		});
		btnDelvcf.setBounds(815, 516, 118, 24);
		add(btnDelvcf);
		
		chckbxMergebyprefix = new JCheckBox("MergeByPrefix");
		chckbxMergebyprefix.setSelected(true);
		chckbxMergebyprefix.setBounds(38, 198, 187, 23);
		add(chckbxMergebyprefix);
		initial();
	}
	
	private void initial() {
		buttonGroupRad = new ButtonGroup();
		
		scrlSamFile.setTitle(new String[]{"Sam/BamFile", "prefix"});
		
		cmbSpecies.setMapItem(Species.getSpeciesName2Species(EnumSpeciesType.Genome));
		
		selectCombSpecies();
	}
	
	private void selectRealign() {
		if (chckRecalibrate.isSelected()) {
			chckbxSortBam.setEnabled(false);
			chckbxIndex.setEnabled(false);
			cmbSpecies.setEnabled(true);
			cmbVersion.setEnabled(true);
			selectCombSpecies();
		}
		else {
			chckbxSortBam.setEnabled(true);
			chckbxIndex.setEnabled(true);
			cmbSpecies.setEnabled(false);
			cmbVersion.setEnabled(false);
		}
	}
	
	private void selectCombSpecies() {
		Species species = cmbSpecies.getSelectedValue();
		if (species.getTaxID() == 0) {
			cmbVersion.setEnabled(false);
		} 
		else {
			cmbVersion.setEnabled(true);
			cmbVersion.setMapItem(species.getMapVersion());
		}
	}

	private void convertSamFile(String resultMergePath, String prefix, List<String> lsSamFilestr) {
		String refFile = "";
		Species species = cmbSpecies.getSelectedValue();
		
		if (species.getTaxID() == 0) {
			refFile = null;
		} else {
			refFile = species.getChromSeq();
		}
		
		List<SamFile> lsSamFiles = new ArrayList<SamFile>(); 
		for (String string : lsSamFilestr) {
			SamFile samFile = new SamFile(string);
			samFile.setReferenceFileName(refFile);
			lsSamFiles.add(samFile);
		}
		List<SamFile> lsBamFile = new ArrayList<SamFile>();
		for (SamFile samFile : lsSamFiles) {
			lsBamFile.add(samFile.convertToBam(false));
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
}
