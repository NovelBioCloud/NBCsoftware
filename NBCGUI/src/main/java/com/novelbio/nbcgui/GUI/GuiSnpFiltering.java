package com.novelbio.nbcgui.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import com.novelbio.analysis.seq.resequencing.SnpLevel;
import com.novelbio.analysis.seq.resequencing.SnpSomaticFinder;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.gui.GUIFileOpen;
import com.novelbio.base.gui.JComboBoxData;
import com.novelbio.base.gui.JScrollPaneData;

public class GuiSnpFiltering extends JPanel {
	JScrollPaneData sclSnpFile;
	JScrollPaneData sclSnpPileUp;
	JScrollPaneData sclGroup2Sample;
	JScrollPaneData sclGropuInfo;

	JComboBoxData<String> cmbSample = new JComboBoxData<String>();
	/** 在sclGroup2Prefix中使用，可以添加新的group */
	JComboBoxData<String> cmbGroupSet = new JComboBoxData<String>();
	/** 在sclGropuInfo中使用，不可以添加新的group */
	JComboBoxData<String> cmbGroupGet = new JComboBoxData<String>();
	JComboBoxData<SnpLevel> cmbSnpLevel = new JComboBoxData<SnpLevel>();
	JComboBoxData<SnpLevel> cmbSnpLevelShow = new JComboBoxData<SnpLevel>();
	private JTextField txtSaveTo;
	
	GuiLayeredPaneSpeciesVersionGff guiLayeredPaneSpeciesVersionGff;
	JSpinner snpGeneSnpNum;
	
	GUIFileOpen guiFileOpen = new GUIFileOpen();
	
	SnpSomaticFinder snpSomaticFinder = new SnpSomaticFinder();
	JCheckBox chckbxFiltergene;
	
	/** 是否要运行读取方法，因为读取很花时间，如果只是调整参数，就不用再次读取了 */
	boolean isReadPileUp = true;
	boolean isFilterSnp = true;
	/**
	 * Create the panel.
	 */
	public GuiSnpFiltering() {
		setLayout(null);
		
		sclSnpFile = new JScrollPaneData();
		sclSnpFile.setBounds(32, 29, 619, 124);
		add(sclSnpFile);
		
		JButton btnAddSnpFile = new JButton("AddSnpFile");
		btnAddSnpFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<String> lsFileName = guiFileOpen.openLsFileName("", "");
				List<String[]> lsFileName2Prefix = new ArrayList<String[]>();
				for (String fileName : lsFileName) {
					String prefix = FileOperate.getFileNameSep(fileName)[0].split("_")[0];
					lsFileName2Prefix.add(new String[]{fileName, prefix});
				}
				sclSnpFile.addItemLs(lsFileName2Prefix);
				isReadPileUp = true;
				isFilterSnp = true;
			}
		});
		btnAddSnpFile.setBounds(32, 162, 118, 24);
		add(btnAddSnpFile);
		
		JButton btnDelSnpFile = new JButton("DelSnpFile");
		btnDelSnpFile.setBounds(534, 166, 118, 24);
		add(btnDelSnpFile);
		
		sclSnpPileUp = new JScrollPaneData();
		sclSnpPileUp.setBounds(32, 198, 619, 110);
		add(sclSnpPileUp);
		
		JButton btnAddPileUp = new JButton("AddPileUp");
		btnAddPileUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<String> lsFileName = guiFileOpen.openLsFileName("", "");
				List<String[]> lsFileName2Prefix = new ArrayList<String[]>();
				for (String fileName : lsFileName) {
					String prefix = FileOperate.getFileNameSep(fileName)[0].split("_")[0];
					lsFileName2Prefix.add(new String[]{fileName, prefix});
				}
				sclSnpPileUp.addItemLs(lsFileName2Prefix);
				isReadPileUp = true;
				isFilterSnp = true;
			}
		});
		btnAddPileUp.setBounds(32, 315, 118, 24);
		add(btnAddPileUp);
		
		JButton btnDelFileUp = new JButton("DelFileUp");
		btnDelFileUp.setBounds(533, 321, 118, 24);
		add(btnDelFileUp);
		
		sclGropuInfo = new JScrollPaneData();
		sclGropuInfo.setBounds(32, 362, 350, 104);
		add(sclGropuInfo);
		
		JButton btnAddcompare = new JButton("AddCmp");
		btnAddcompare.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sclGropuInfo.addItem(new String[]{"", "" ,"", "", ""});
				isFilterSnp = true;
			}
		});
		btnAddcompare.setBounds(32, 479, 91, 24);
		add(btnAddcompare);
		
		JButton btnDelcompare = new JButton("DelCmp");
		btnDelcompare.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sclGropuInfo.deleteSelRows();
			}
		});
		btnDelcompare.setBounds(291, 479, 91, 24);
		add(btnDelcompare);
		
		sclGroup2Sample = new JScrollPaneData();
		sclGroup2Sample.setBounds(661, 29, 194, 124);
		add(sclGroup2Sample);
		
		JButton btnAddgroup = new JButton("AddGroup");
		btnAddgroup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sclGroup2Sample.addItem(new String[]{"",""});
				isFilterSnp = true;
			}
		});
		btnAddgroup.setBounds(661, 161, 91, 27);
		add(btnAddgroup);
		
		JButton btnDelGroup = new JButton("DelGroup");
		btnDelGroup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sclGroup2Sample.deleteSelRows();
			}
		});
		btnDelGroup.setBounds(764, 161, 91, 27);
		add(btnDelGroup);
		
		guiLayeredPaneSpeciesVersionGff = new GuiLayeredPaneSpeciesVersionGff();
		guiLayeredPaneSpeciesVersionGff.setBounds(396, 358, 255, 145);
		add(guiLayeredPaneSpeciesVersionGff);
		
		JButton btnRun = new JButton("Run");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				run();
			}
		});
		btnRun.setBounds(698, 527, 113, 27);
		add(btnRun);
		
		txtSaveTo = new JTextField();
		txtSaveTo.setBounds(37, 528, 502, 24);
		add(txtSaveTo);
		txtSaveTo.setColumns(10);
		
		JButton btnSaveto = new JButton("SaveTo");
		btnSaveto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String fileName = guiFileOpen.saveFileNameAndPath("", "");
				txtSaveTo.setText(fileName);
			}
		});
		btnSaveto.setBounds(553, 527, 113, 27);
		add(btnSaveto);
		
		cmbSnpLevelShow.setBounds(661, 393, 177, 24);
		add(cmbSnpLevelShow);
		
		chckbxFiltergene = new JCheckBox("FilterGene");
		chckbxFiltergene.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (chckbxFiltergene.isSelected()) {
					cmbSnpLevelShow.setEnabled(true);
					snpGeneSnpNum.setEnabled(true);
				} else {
					cmbSnpLevelShow.setEnabled(false);
					snpGeneSnpNum.setEnabled(false);
				}
			}
		});
		chckbxFiltergene.setBounds(661, 357, 133, 27);
		add(chckbxFiltergene);
		
		snpGeneSnpNum = new JSpinner();
		snpGeneSnpNum.setBounds(661, 465, 91, 24);
		add(snpGeneSnpNum);
		
		JLabel lblSnpnumPerGene = new JLabel("SnpNum per Gene");
		lblSnpnumPerGene.setBounds(661, 434, 133, 18);
		add(lblSnpnumPerGene);

		initial();
	}
	private void initial() {
		cmbSample.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				changeSclComparePrefix();
			}
		});
		cmbGroupSet.setEditable(true);
		cmbGroupSet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeSclCompareGroup();
			}
		});
		cmbGroupGet.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				changeSclCompareGroup();
			}
		});
		
		sclSnpFile.setTitle(new String[]{"SnpFile","Sample"});
		
		sclSnpPileUp.setTitle(new String[]{"PileUpFile","Sample"});
		sclSnpPileUp.setItem(1, cmbSample);

		sclGroup2Sample.setTitle(new String[]{"Group","Sample"});
		cmbSnpLevel.setMapItem(SnpLevel.getMapStr2SnpLevel());
		sclGroup2Sample.setItem(0, cmbGroupSet);
		sclGroup2Sample.setItem(1, cmbSample);

		sclGropuInfo.setTitle(new String[]{"Group","SnpLevel","MinNum","MaxNum"});
		cmbSnpLevel.setMapItem(SnpLevel.getMapStr2SnpLevel());
		sclGropuInfo.setItem(1, cmbSnpLevel);
		sclGropuInfo.setItem(0, cmbGroupGet);
		
		cmbSnpLevelShow.setMapItem(SnpLevel.getMapStr2SnpLevel());
	}
	
	private void changeSclComparePrefix() {
		ArrayList<String[]> lsSnp2Prefix = sclSnpPileUp.getLsDataInfo();
		Map<String, String> mapString2Value = new HashMap<String, String>();
		for (String[] snp2prefix : lsSnp2Prefix) {
			mapString2Value.put(snp2prefix[1], snp2prefix[1]);
		}
		cmbSample.setMapItem(mapString2Value);
	}
	
	private void changeSclCompareGroup() {
		ArrayList<String[]> lsSnp2Prefix = sclGroup2Sample.getLsDataInfo();
		Map<String, String> mapString2Value = new HashMap<String, String>();
		for (String[] group2Prefix : lsSnp2Prefix) {
			if (group2Prefix[0].trim().equals("")) {
				continue;
			}
			mapString2Value.put(group2Prefix[0].trim(), group2Prefix[0].trim());
		}
		cmbGroupGet.setMapItem(mapString2Value);
		cmbGroupSet.setMapItem(mapString2Value);
	}
	
	private void run() {
		if (isReadPileUp) {
			snpSomaticFinder.setSnpFile(sclSnpFile.getLsDataInfo());
			snpSomaticFinder.setSnpPileUpFile(sclSnpPileUp.getLsDataInfo());
			snpSomaticFinder.readPileupFile();
			isReadPileUp = false;
		}
		
		if (isFilterSnp) {
			snpSomaticFinder.setGroup2Sample(sclGroup2Sample.getLsDataInfo());
			snpSomaticFinder.setGroupInfo(sclGropuInfo.getLsDataInfo());
			snpSomaticFinder.filterSnp();
			isFilterSnp = false;
		}
		
		if (chckbxFiltergene.isSelected()) {
			snpSomaticFinder.setSpecies(guiLayeredPaneSpeciesVersionGff.getSelectSpecies());
			snpSomaticFinder.setSnpLevel(cmbSnpLevelShow.getSelectedValue());
			snpSomaticFinder.setTreatFilteredNum((Integer)snpGeneSnpNum.getValue());
			snpSomaticFinder.filterByGene();
		}
		String fileName = txtSaveTo.getText();
		if (fileName.endsWith("/") || fileName.endsWith("\\")) {
			fileName = fileName + "filteredsnp.txt";
		}
		FileOperate.createFolders(FileOperate.getParentPathName(fileName));
		snpSomaticFinder.writeToFile(fileName);
	}
}
