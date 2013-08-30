package com.novelbio.nbcgui.GUI;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import com.novelbio.analysis.seq.genome.GffChrAbs;
import com.novelbio.analysis.seq.genome.GffChrMap;
import com.novelbio.analysis.seq.genome.gffOperate.GffDetailGene.GeneStructure;
import com.novelbio.analysis.seq.genome.mappingOperate.MapReadsAbs;
import com.novelbio.base.dataOperate.ExcelTxtRead;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.gui.GUIFileOpen;
import com.novelbio.base.gui.JComboBoxData;
import com.novelbio.base.gui.JScrollPaneData;
import com.novelbio.nbcgui.controlseq.CtrlMapReads;

/**
 * 批量注释，各种注释
 * @author zong0jie
 *
 */
public class GuiBedTssAndChrome extends JPanel implements GuiRunningBarAbs, GuiNeedOpenFile {
	private static final long serialVersionUID = -4438216387830519443L;
	private JTextField txtColGene;
	private JTextField txtColPeakStartMid;
	JLabel lblColValue;
	JProgressBar progressBar;
	JScrollPaneData scrollPaneData;
	JButton btnSave;
	JButton btnRunchrome;
	
	GUIFileOpen guiFileOpen = new GUIFileOpen();
	GffChrAbs gffChrAbs = new GffChrAbs();
	GffChrMap gffChrMap = new GffChrMap(gffChrAbs);
	private JButton btnRunTss;
	
	ArrayList<String[]> lsGeneInfo;
	private JTextField txtTssUp;
	private JTextField txtTssDown;
	
	String readFile = "";
	private JTextField txtBedFile;
	private JCheckBox chckSortBig2Small;
	private JTextField txtSaveTo;
	JButton btnOpenfile;
	JButton btnOpenBamBedFile;
	JSpinner spinInvNum;
	JSpinner spinLoadFirstBp;
	ButtonGroup buttonGroupPlotType = new ButtonGroup();
	
	CtrlMapReads ctrlMapReads = new CtrlMapReads(this);
	private JLabel lblInvnum;
	private JTextField txtResultBinNum;
	JCheckBox chckUniqueMapping;
	JCheckBox chckOneSiteOneReads;
	JButton btnLoading ;
	private JTextField txtColEnd;
	
	private String filePeakGene;
	private JTextField txtHeatMapSmall;
	private JLabel lblHeatmapsmall;
	private JLabel lblHeatmapbig;
	private JTextField txtHeatmapBig;
	
	JRadioButton rdbtnAllgene;
	JRadioButton rdbtnPeakcovered;
	JRadioButton rdbtnReadgene;
	private GuiLayeredPaneSpeciesVersionGff layeredPaneSpecies;

	JComboBoxData<Integer> cmbNormalizedType;
	JComboBoxData<Boolean> cmbReadsFilter;
	
	private JTextField txtChromHight;
	/**
	 * Create the panel.
	 */
	public GuiBedTssAndChrome() {
		setLayout(null);
		
		scrollPaneData = new JScrollPaneData();
		scrollPaneData.setBounds(12, 101, 693, 190);
		add(scrollPaneData);
		
		btnOpenfile = new JButton("OpenGeneFile");
		btnOpenfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				filePeakGene = guiFileOpen.openFileName("excel/txt", "");
				lsGeneInfo = ExcelTxtRead.readLsExcelTxtFile(filePeakGene, 1, 1, 50, -1);
				scrollPaneData.setItemLs(lsGeneInfo);
			}
		});
		btnOpenfile.setBounds(717, 224, 157, 24);
		add(btnOpenfile);
		
		txtColGene = new JTextField();
		txtColGene.setBounds(717, 355, 114, 18);
		add(txtColGene);
		txtColGene.setColumns(10);
		
		JLabel lblColGene = new JLabel("colGene ColChrID");
		lblColGene.setBounds(717, 338, 136, 14);
		add(lblColGene);
		
		txtColPeakStartMid = new JTextField();
		txtColPeakStartMid.setBounds(717, 400, 114, 18);
		add(txtColPeakStartMid);
		txtColPeakStartMid.setColumns(10);
		
		lblColValue = new JLabel("ColValue ColStart");
		lblColValue.setBounds(717, 385, 157, 14);
		add(lblColValue);
		
		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String fileName = guiFileOpen.saveFileName("txt", "");
				txtSaveTo.setText(fileName);
			}
		});
		btnSave.setBounds(543, 564, 118, 24);
		add(btnSave);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(12, 600, 693, 14);
		add(progressBar);
		
		btnRunTss = new JButton("RunTss");
		btnRunTss.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gffChrAbs.setSpecies(layeredPaneSpecies.getSelectSpecies());
				gffChrMap.setPlotRange(new int[]{Integer.parseInt(txtTssUp.getText()), Integer.parseInt(txtTssDown.getText())});
				gffChrMap.setGffChrAbs(gffChrAbs);
				if (rdbtnAllgene.isSelected()) {
					plotAllTssTes();
				}
				else if (rdbtnPeakcovered.isSelected()) {
					plotPeakTssTes();
				}
				else if (rdbtnReadgene.isSelected()) {
					plotGeneTssTes();
					plotGeneTssTesHeatMap();
				}
			}
		});
		btnRunTss.setBounds(730, 542, 118, 24);
		add(btnRunTss);
		
		txtTssUp = new JTextField();
		txtTssUp.setBounds(720, 515, 52, 18);
		add(txtTssUp);
		txtTssUp.setColumns(10);
		
		txtTssDown = new JTextField();
		txtTssDown.setBounds(784, 515, 52, 18);
		add(txtTssDown);
		txtTssDown.setColumns(10);
		
		JLabel lblUp = new JLabel("Up");
		lblUp.setBounds(717, 498, 69, 14);
		add(lblUp);
		
		JLabel lblDown = new JLabel("Down");
		lblDown.setBounds(779, 498, 69, 14);
		add(lblDown);
		
		txtBedFile = new JTextField();
		txtBedFile.setBounds(12, 12, 425, 18);
		add(txtBedFile);
		txtBedFile.setColumns(10);
		
		btnOpenBamBedFile = new JButton("OpenBamBedFile");
		btnOpenBamBedFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String bedFile = guiFileOpen.openFileName("bed", "");
				txtBedFile.setText(bedFile);
			}
		});
		btnOpenBamBedFile.setBounds(449, 9, 164, 24);
		add(btnOpenBamBedFile);
		
		btnLoading = new JButton("Loading");
		btnLoading.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ctrlMapReads.setBamBedFile(txtBedFile.getText());
				Boolean FilteredStrand = cmbReadsFilter.getSelectedValue();
				ctrlMapReads.setFilter(chckOneSiteOneReads.isSelected(), (Integer)spinLoadFirstBp.getValue(), chckUniqueMapping.isSelected(), FilteredStrand);
				ctrlMapReads.setInvNum((Integer)spinInvNum.getValue());
				ctrlMapReads.setNormalType(MapReadsAbs.SUM_TYPE_MEAN);
				ctrlMapReads.setSpecies(layeredPaneSpecies.getSelectSpecies());
				gffChrMap.setMapReads(ctrlMapReads.getMapReads());
				ctrlMapReads.execute();
			}
		});
		btnLoading.setBounds(641, 9, 118, 24);
		add(btnLoading);
		
		chckSortBig2Small = new JCheckBox("SortBig2Small");
		chckSortBig2Small.setBounds(717, 468, 131, 22);
		add(chckSortBig2Small);
		
		txtSaveTo = new JTextField();
		txtSaveTo.setBounds(12, 570, 485, 18);
		add(txtSaveTo);
		txtSaveTo.setColumns(10);
		
		JLabel lblJustRead = new JLabel("ExtendBP");
		lblJustRead.setBounds(12, 45, 78, 14);
		add(lblJustRead);
		
		JLabel lblAdvancedSetup = new JLabel("Advanced Setup");
		lblAdvancedSetup.setBounds(12, 295, 164, 14);
		add(lblAdvancedSetup);
		
		chckUniqueMapping = new JCheckBox("UniqeMapping");
		chckUniqueMapping.setBounds(260, 71, 131, 22);
		add(chckUniqueMapping);
		
		chckOneSiteOneReads = new JCheckBox("OneSiteOneReads");
		chckOneSiteOneReads.setBounds(395, 71, 157, 22);
		add(chckOneSiteOneReads);
		
		lblInvnum = new JLabel("InvNum");
		lblInvnum.setBounds(573, 45, 69, 14);
		add(lblInvnum);
		
		spinInvNum = new JSpinner();
		spinInvNum.setToolTipText("");
		spinInvNum.setBounds(573, 73, 58, 18);
		add(spinInvNum);
		
		spinLoadFirstBp = new JSpinner();
		spinLoadFirstBp.setBounds(12, 73, 69, 18);
		add(spinLoadFirstBp);
		
		txtResultBinNum = new JTextField();
		txtResultBinNum.setText("5000");
		txtResultBinNum.setBounds(286, 415, 114, 18);
		add(txtResultBinNum);
		txtResultBinNum.setColumns(10);
		
		JLabel lblResultbinnum = new JLabel("ResultBinNum");
		lblResultbinnum.setBounds(286, 389, 114, 14);
		add(lblResultbinnum);
		
		rdbtnAllgene = new JRadioButton("AllGene");
		rdbtnAllgene.setBounds(713, 256, 151, 22);
		add(rdbtnAllgene);
		
		rdbtnPeakcovered = new JRadioButton("PeakCovered");
		rdbtnPeakcovered.setBounds(713, 282, 151, 22);
		add(rdbtnPeakcovered);
		
		rdbtnReadgene = new JRadioButton("ReadGene");
		rdbtnReadgene.setBounds(713, 308, 151, 22);
		add(rdbtnReadgene);
		
		txtColEnd = new JTextField();
		txtColEnd.setBounds(717, 442, 114, 18);
		add(txtColEnd);
		txtColEnd.setColumns(10);
		
		JLabel lblColend = new JLabel("ColEnd");
		lblColend.setBounds(717, 421, 69, 14);
		add(lblColend);
		
		txtHeatMapSmall = new JTextField();
		txtHeatMapSmall.setBounds(499, 358, 114, 18);
		add(txtHeatMapSmall);
		txtHeatMapSmall.setColumns(10);
		
		lblHeatmapsmall = new JLabel("heatmapSmall");
		lblHeatmapsmall.setBounds(499, 338, 102, 14);
		add(lblHeatmapsmall);
		
		lblHeatmapbig = new JLabel("heatmapBig");
		lblHeatmapbig.setBounds(499, 431, 87, 14);
		add(lblHeatmapbig);
		
		txtHeatmapBig = new JTextField();
		txtHeatmapBig.setBounds(499, 452, 114, 18);
		add(txtHeatmapBig);
		txtHeatmapBig.setColumns(10);
		
		btnRunchrome = new JButton("RunChrome");
		btnRunchrome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				plotChrome();
			}
		});
		btnRunchrome.setBounds(730, 590, 118, 24);
		add(btnRunchrome);
		
		layeredPaneSpecies = new GuiLayeredPaneSpeciesVersionGff();
		layeredPaneSpecies.setBounds(713, 54, 222, 158);
		add(layeredPaneSpecies);
		
		cmbNormalizedType = new JComboBoxData<Integer>();
		cmbNormalizedType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ctrlMapReads.setNormalType(cmbNormalizedType.getSelectedValue());
				System.out.println("Normalized Type Is" + cmbNormalizedType.getSelectedItem().toString());
			}
		});
		cmbNormalizedType.setBounds(333, 493, 238, 24);
		add(cmbNormalizedType);
		cmbNormalizedType.setMapItem(MapReadsAbs.getMapNormalizedType());
		
		JLabel lblNormalizedtype = new JLabel("NormalizedType");
		lblNormalizedtype.setBounds(333, 472, 136, 15);
		add(lblNormalizedtype);
		
		txtChromHight = new JTextField();
		txtChromHight.setBounds(150, 529, 69, 19);
		add(txtChromHight);
		txtChromHight.setColumns(10);
		
		JLabel lblChromPicHeight = new JLabel("ChromPicHeight");
		lblChromPicHeight.setBounds(12, 531, 127, 15);
		add(lblChromPicHeight);
		
		cmbReadsFilter = new JComboBoxData<Boolean>();
		cmbReadsFilter.setBounds(93, 70, 137, 24);
		add(cmbReadsFilter);
		
		JLabel lblReadsfilter = new JLabel("ReadsFilter");
		lblReadsfilter.setBounds(102, 45, 102, 15);
		add(lblReadsfilter);
		
		initial();
	}
	
	private void initial() {
		chckOneSiteOneReads.setSelected(false);
		chckUniqueMapping.setSelected(true);
		spinInvNum.setValue(10);
		
		buttonGroupPlotType.add(rdbtnAllgene);
		buttonGroupPlotType.add(rdbtnPeakcovered);
		buttonGroupPlotType.add(rdbtnReadgene);
		
		Map<String, Boolean> mapReadsFilter = new HashMap<String, Boolean>();
		mapReadsFilter.put("NotFilter", null);
		mapReadsFilter.put("CisReads", true);
		mapReadsFilter.put("TransReads", false);
		cmbReadsFilter.setMapItem(mapReadsFilter);
	}
	
	private void plotAllTssTes() {
		String save =  txtSaveTo.getText();
		String saveTss = FileOperate.changeFileSuffix(save, "_Tss", "xls");
		String saveTes = FileOperate.changeFileSuffix(save, "_Tes", "xls");
		int binNum = Integer.parseInt(txtResultBinNum.getText());
		gffChrMap.plotTssAllGene(binNum, saveTss, GeneStructure.TSS);
		gffChrMap.plotTssAllGene(binNum, saveTes, GeneStructure.TES);
	}
	
	private void plotPeakTssTes() {
		String save =  txtSaveTo.getText();
		String saveTss = FileOperate.changeFileSuffix(save, "_Tss", "xls");
		String saveTes = FileOperate.changeFileSuffix(save, "_Tes", "xls");
		int binNum = Integer.parseInt(txtResultBinNum.getText());

		gffChrMap.plotTssPeak(filePeakGene, 2, binNum, saveTss, GeneStructure.TSS);
		gffChrMap.plotTssPeak(filePeakGene, 2, binNum, saveTes, GeneStructure.TES);
	}
	
	private void plotGeneTssTes() {
		String save =  txtSaveTo.getText();
		String saveTss = FileOperate.changeFileSuffix(save, "_Tss", "xls");
		String saveTes = FileOperate.changeFileSuffix(save, "_Tes", "xls");
		int binNum = Integer.parseInt(txtResultBinNum.getText());

		gffChrMap.plotTssGene(filePeakGene, 2, binNum, saveTss, GeneStructure.TSS);
		gffChrMap.plotTssGene(filePeakGene, 2, binNum, saveTes, GeneStructure.TES);
	}
	
	private void plotGeneTssTesHeatMap() {
		String save =  txtSaveTo.getText();
		String saveTss = FileOperate.changeFileSuffix(save, "_Tss", "png");
		String saveTes = FileOperate.changeFileSuffix(save, "_Tes", "png");
		int binNum = Integer.parseInt(txtResultBinNum.getText());
		int colGeneID = Integer.parseInt(txtColGene.getText());
		int colScore = colGeneID;
		try {
			colScore = Integer.parseInt(txtColPeakStartMid.getText());
		} catch (Exception e) { }
	
		
		double heatmapSmall = Double.parseDouble(txtHeatMapSmall.getText());
		double heatmapBig = Double.parseDouble(txtHeatmapBig.getText());
		
		gffChrMap.plotTssHeatMap(Color.blue, chckSortBig2Small.isSelected(),filePeakGene, colGeneID, colScore, 2, heatmapSmall, heatmapBig, GeneStructure.TSS, binNum, saveTss);
		gffChrMap.plotTssHeatMap(Color.blue, chckSortBig2Small.isSelected(),filePeakGene, colGeneID, colScore, 2, heatmapSmall, heatmapBig, GeneStructure.TES, binNum, saveTes);
	}
	
	private void plotChrome() {
		gffChrAbs.setSpecies(layeredPaneSpecies.getSelectSpecies());
		String save =  txtSaveTo.getText();
		int chromHeight = -1;
		try {
			chromHeight = Integer.parseInt(txtChromHight.getText().trim());
		} catch (Exception e) {}
		gffChrMap.setGffChrAbs(gffChrAbs);
		gffChrMap.plotAllChrDist(save, chromHeight);
	}
	
	public void setGuiFileOpen(GUIFileOpen guiFileOpen) {
		this.guiFileOpen = guiFileOpen;
	}
	public JProgressBar getProcessBar() {
		return progressBar;
	}
	public List<JButton> getLsBtn() {
		List<JButton> lsButtons = new ArrayList<JButton>();
		lsButtons.add(btnSave);
		lsButtons.add(btnOpenBamBedFile);
		lsButtons.add(btnRunTss);
		lsButtons.add(btnLoading);
		lsButtons.add(btnRunchrome);
		return lsButtons;
	}

	@Override
	public JScrollPaneData getScrollPaneData() {
		return null;
	}
}
