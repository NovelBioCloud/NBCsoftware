package com.novelbio.nbcgui.GUI;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import com.novelbio.analysis.seq.genome.gffOperate.GffDetailGene;
import com.novelbio.analysis.seq.genome.gffOperate.GffDetailGene.GeneStructure;
import com.novelbio.analysis.seq.genome.mappingOperate.SiteSeqInfo;
import com.novelbio.base.dataOperate.ExcelTxtRead;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.gui.GUIFileOpen;
import com.novelbio.base.gui.JComboBoxData;
import com.novelbio.base.gui.JScrollPaneData;
import com.novelbio.database.domain.geneanno.SpeciesFile.ExtractSmallRNASeq;
import com.novelbio.database.model.species.Species;
import com.novelbio.generalConf.PathDetailNBC;
import com.novelbio.nbcgui.controlquery.CtrlPeakStatistics;
import com.novelbio.nbcgui.controlseq.CtrlGetSeq;

/**
 * 批量注释，各种注释
 * @author zong0jie
 *
 */
public class GuiGetSeq extends JPanel {
	private static final long serialVersionUID = -4438216387830519443L;
	JLabel lblPeakstartcolumn;
	JScrollPaneData scrollPaneData;
	JButton btnSave;
	JCheckBox chckbxGenomwide;
	JCheckBox chckbxGetalliso;
	JComboBoxData<GffDetailGene.GeneStructure> cmbGeneStructure;
	JButton btnOpenfile;
	JLabel lblTssTes;
	JCheckBox chckbxGetaminoacid;
	JProgressBar progressBar;
	GUIFileOpen guiFileOpen = new GUIFileOpen();
	
	CtrlPeakStatistics ctrlPeakStatistics;
	private JButton btnRun;
	
	JComboBoxData<Species> cmbSpecies;
	JComboBoxData<String> cmbSpeciesVersion;
	
	ArrayList<String[]> lsGeneInfo;
	private JTextField txtTssUp;
	private JTextField txtTssDown;
	
	String readFile = "";
	JSpinner spinColChr;
	private JSpinner spinStart;
	private JSpinner spinEnd;
	private JRadioButton rdbtnSite;
	private JRadioButton rdbtnGene;
	private JRadioButton rdbtnRegion;

	ArrayList<Component> lsCompRegion = new ArrayList<Component>();
	ArrayList<Component> lsCompSite = new ArrayList<Component>();
	ArrayList<Component> lsCompGene = new ArrayList<Component>();
	
	ButtonGroup btnGroupRand;
	private JTextField txtSavePath;
	JCheckBox chckbxGetMirna;
	
	CtrlGetSeq ctrlGetSeq = new CtrlGetSeq(this);
	
	int maxSeqNum = 30000;
	/**
	 * Create the panel.
	 */
	public GuiGetSeq() {
		setLayout(null);
		
		scrollPaneData = new JScrollPaneData();
		scrollPaneData.setBounds(12, 30, 693, 460);
		add(scrollPaneData);
		
		btnOpenfile = new JButton("OpenFile");
		btnOpenfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				readFile = guiFileOpen.openFileName("excel/txt", "");
				lsGeneInfo = ExcelTxtRead.readLsExcelTxt(readFile, 1);
				scrollPaneData.setItemLs(lsGeneInfo);
			}
		});
		btnOpenfile.setBounds(717, 30, 120, 24);
		add(btnOpenfile);
		
		JLabel lblChridcolumn = new JLabel("ColChrID");
		lblChridcolumn.setBounds(723, 197, 69, 14);
		add(lblChridcolumn);
		
		lblPeakstartcolumn = new JLabel("Start");
		lblPeakstartcolumn.setBounds(723, 223, 52, 14);
		add(lblPeakstartcolumn);
		
		cmbSpeciesVersion = new JComboBoxData<String>();
		
		cmbSpeciesVersion.setBounds(717, 101, 118, 23);
		add(cmbSpeciesVersion);
		
		btnSave = new JButton("SavePath");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String fileName = guiFileOpen.saveFileName("txt", "");
				txtSavePath.setText(fileName);
			}
		});
		btnSave.setBounds(640, 496, 118, 24);
		add(btnSave);
		
		btnRun = new JButton("Run");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				running();
			}
		});
		btnRun.setBounds(749, 541, 118, 24);
		add(btnRun);
		
		cmbSpecies = new JComboBoxData<Species>();
		cmbSpecies.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Species species = cmbSpecies.getSelectedValue();
				cmbSpeciesVersion.setMapItem(species.getMapVersion());
			}
		});
		cmbSpecies.setBounds(717, 66, 118, 23);
		add(cmbSpecies);
		
		txtTssUp = new JTextField();
		txtTssUp.setBounds(717, 468, 52, 18);
		add(txtTssUp);
		txtTssUp.setColumns(10);
		
		txtTssDown = new JTextField();
		txtTssDown.setBounds(785, 468, 52, 18);
		add(txtTssDown);
		txtTssDown.setColumns(10);
		
		lblTssTes = new JLabel("Tss");
		lblTssTes.setBounds(717, 422, 69, 14);
		add(lblTssTes);
		
		JLabel lblUp = new JLabel("Up");
		lblUp.setBounds(726, 448, 43, 14);
		add(lblUp);
		
		JLabel lblDown = new JLabel("Down");
		lblDown.setBounds(791, 448, 52, 14);
		add(lblDown);
		
		spinColChr = new JSpinner();
		spinColChr.setBounds(797, 195, 55, 18);
		add(spinColChr);
		
		spinStart = new JSpinner();
		spinStart.setBounds(723, 240, 52, 18);
		add(spinStart);
		
		spinEnd = new JSpinner();
		spinEnd.setBounds(800, 240, 52, 18);
		add(spinEnd);
		
		JLabel lblEnd = new JLabel("End");
		lblEnd.setBounds(800, 223, 69, 14);
		add(lblEnd);
		
		rdbtnRegion = new JRadioButton("Region");
		rdbtnRegion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectRegion();
			}
		});
		rdbtnRegion.setBounds(713, 131, 81, 22);
		add(rdbtnRegion);
		
		rdbtnSite = new JRadioButton("Site");
		rdbtnSite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectSite();
			}
		});
		rdbtnSite.setBounds(794, 131, 81, 22);
		add(rdbtnSite);
		
		rdbtnGene = new JRadioButton("Gene");
		rdbtnGene.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectGene();
			}
		});
		rdbtnGene.setBounds(713, 157, 81, 22);
		add(rdbtnGene);
		
		cmbGeneStructure = new JComboBoxData<GffDetailGene.GeneStructure>();
		cmbGeneStructure.setBounds(721, 356, 131, 23);
		add(cmbGeneStructure);
		cmbGeneStructure.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeGeneStructure();
			}
		});
		
		JLabel lblGeneStructure = new JLabel("GeneStructure");
		lblGeneStructure.setBounds(717, 333, 131, 14);
		add(lblGeneStructure);
		
		chckbxGenomwide = new JCheckBox("GenomWide");
		chckbxGenomwide.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectGenomeWide(chckbxGenomwide.isSelected());
			}
		});
		chckbxGenomwide.setBounds(713, 292, 120, 22);
		add(chckbxGenomwide);
		
		txtSavePath = new JTextField();
		txtSavePath.setBounds(16, 501, 603, 18);
		add(txtSavePath);
		txtSavePath.setColumns(10);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(18, 548, 698, 14);
		add(progressBar);
		
		chckbxGetaminoacid = new JCheckBox("getAminoAcid");
		chckbxGetaminoacid.setBounds(720, 392, 131, 22);
		add(chckbxGetaminoacid);
		
		chckbxGetalliso = new JCheckBox("GetAllIso");
		chckbxGetalliso.setBounds(713, 266, 131, 22);
		add(chckbxGetalliso);
		
		chckbxGetMirna = new JCheckBox("get miRNA");
		chckbxGetMirna.setBounds(766, 497, 118, 23);
		add(chckbxGetMirna);
		
		initial();
	}
	
	private void initial() {
		cmbSpecies.setMapItem(Species.getSpeciesName2Species(Species.SEQINFO_SPECIES));
		Species species = cmbSpecies.getSelectedValue();
		cmbSpeciesVersion.setMapItem(species.getMapVersion());
		cmbGeneStructure.setMapItem(GeneStructure.getMapInfo2GeneStr());
		cmbGeneStructure.setSelectedIndex(0);
				
		lsCompGene.add(spinColChr);
		lsCompGene.add(chckbxGenomwide);
		lsCompGene.add(cmbGeneStructure);
		
		lsCompRegion.add(spinColChr);
		lsCompRegion.add(spinStart);
		lsCompRegion.add(spinEnd);
		
		lsCompSite.add(spinColChr);
		lsCompSite.add(spinStart);
		lsCompSite.add(txtTssUp);
		lsCompSite.add(txtTssDown);
		
		btnGroupRand = new ButtonGroup();
		btnGroupRand.add(rdbtnSite);
		btnGroupRand.add(rdbtnGene);
		btnGroupRand.add(rdbtnRegion);
		chckbxGetaminoacid.setEnabled(false);
		selectRegion();
		rdbtnRegion.setSelected(true);
	}
	
	private void selectRegion() {
		chckbxGenomwide.setSelected(false);
		selectGenomeWide(false);
		
		for (Component component : lsCompSite) {
			component.setEnabled(false);
		}
		for (Component component : lsCompGene) {
			component.setEnabled(false);
		}
		for (Component component : lsCompRegion) {
			component.setEnabled(true);
		}
		lblPeakstartcolumn.setText("Start");
	}
	private void selectSite() {
		chckbxGenomwide.setSelected(false);
		selectGenomeWide(false);
		
		for (Component component : lsCompRegion) {
			component.setEnabled(false);
		}
		for (Component component : lsCompGene) {
			component.setEnabled(false);
		}
		for (Component component : lsCompSite) {
			component.setEnabled(true);
		}
		lblPeakstartcolumn.setText("ColSite");
	}	
	private void selectGene() {
		for (Component component : lsCompRegion) {
			component.setEnabled(false);
		}
		for (Component component : lsCompSite) {
			component.setEnabled(false);
		}
		for (Component component : lsCompGene) {
			component.setEnabled(true);
		}
		spinColChr.setValue(1);
		chckbxGenomwide.setSelected(false);
		selectGenomeWide(false);
		changeGeneStructure();
	}
	
	private void selectGenomeWide(boolean selected) {
		if (selected) {
			btnOpenfile.setEnabled(false);
		} else {
			btnOpenfile.setEnabled(true);
		}
	}
	
	private void changeGeneStructure() {
		if (!cmbGeneStructure.isEnabled()) {
			txtTssUp.setEnabled(false);
			txtTssDown.setEnabled(false);
			return;
		}
		
		if (cmbGeneStructure.getSelectedValue().equals(GeneStructure.TSS) || cmbGeneStructure.getSelectedValue().equals(GeneStructure.TES)) {
			if (cmbGeneStructure.getSelectedValue().equals(GeneStructure.TSS)) {
				lblTssTes.setText("Tss");
			}
			else if (cmbGeneStructure.getSelectedValue().equals(GeneStructure.TES)) {
				lblTssTes.setText("Tes");
			}
			txtTssUp.setEnabled(true);
			txtTssDown.setEnabled(true);
		}
		else {
			if (cmbGeneStructure.getSelectedValue().equals(GeneStructure.CDS) ) {
				chckbxGetaminoacid.setEnabled(true);
			} else {
				chckbxGetaminoacid.setEnabled(false);
				chckbxGetaminoacid.setSelected(false);
			}
			txtTssUp.setEnabled(false);
			txtTssDown.setEnabled(false);
		}
	}
	
	private void running() {
		if (chckbxGetMirna.isSelected()) {
			if (!chckbxGenomwide.isSelected()) {
				int colChrID = (Integer) spinColChr.getValue() - 1;
				runGetMiRNA(lsGeneInfo, colChrID);
			} else {
				runGetMiRNA(null, 0);
			}
			return;
		}
		
		
		if (rdbtnSite.isSelected()) {
			int colChrID = (Integer) spinColChr.getValue() - 1;
			int colSite = (Integer) spinStart.getValue() - 1;
			int upStream = Integer.parseInt(txtTssUp.getText());
			int downStream = Integer.parseInt(txtTssDown.getText());
			runGetSeqSite(lsGeneInfo, colChrID, colSite, upStream, downStream);
		}
		else if (rdbtnRegion.isSelected()) {
			int colChrID = (Integer) spinColChr.getValue() - 1;
			int colStart = (Integer) spinStart.getValue() - 1;
			int colEnd = (Integer) spinEnd.getValue() - 1;
			runGetSeqRegion(lsGeneInfo, colChrID, colStart, colEnd);
		}
		else if (rdbtnGene.isSelected() ) {
			int upStream = 0, downStream = 0;
			try { upStream = Integer.parseInt(txtTssUp.getText()); } catch (Exception e) { }
			try { downStream = Integer.parseInt(txtTssDown.getText()); } catch (Exception e) { }
			if (!chckbxGenomwide.isSelected()) {
				int colGeneID = (Integer) spinColChr.getValue() - 1;
				runGetSeqGene(lsGeneInfo, colGeneID, upStream, downStream);
			}
			else {
				runGetSeqGeneGenomeWide(upStream, downStream);
			}
		}
	}
	
	private void runGetSeqRegion(ArrayList<String[]> lsInfo, int colChrID, int colStart, int colEnd) {
		ctrlGetSeq.reset();
		Species species = cmbSpecies.getSelectedValue();
		species.setVersion(cmbSpeciesVersion.getSelectedValue());
		ctrlGetSeq.setSpecies(species);
		ctrlGetSeq.setOutPutFile(txtSavePath.getText());
		ctrlGetSeq.setGetAAseq(chckbxGetaminoacid.isSelected());
		
		ArrayList<SiteSeqInfo> lsSiteInfo = new ArrayList<SiteSeqInfo>();
		for (String[] info : lsInfo) {
			String chrID = info[colChrID];
			int start, end;
			try {
				start = Integer.parseInt(info[colStart]);
				end = Integer.parseInt(info[colEnd]);
			} catch (Exception e) {
				continue;
			}
			SiteSeqInfo siteInfo = new SiteSeqInfo(chrID, start, end);
			lsSiteInfo.add(siteInfo);
		}
		
		if (lsSiteInfo.size() > maxSeqNum) {
			JOptionPane.showMessageDialog(null, "To Protect Your HardDisk, Only " + maxSeqNum + " Sequence Will Be Queried.", "Warning", JOptionPane.WARNING_MESSAGE);
		}
		ctrlGetSeq.setGetSeqSite(lsSiteInfo);
		ctrlGetSeq.execute();
	}
	
	private void runGetMiRNA(ArrayList<String[]> lsInfo, int colMiRNAname) {
		List<String> lsMiRNAname = new ArrayList<String>();
		if (lsInfo != null) {
			for (String[] string : lsInfo) {
				String miRNA = string[colMiRNAname];
				lsMiRNAname.add(miRNA);
			}
		}
		ExtractSmallRNASeq extractSmallRNASeq = new ExtractSmallRNASeq();
		extractSmallRNASeq.setLsMiRNAname(lsMiRNAname);
		extractSmallRNASeq.setOutMatureRNA(txtSavePath.getText());
		extractSmallRNASeq.setMiRNAdata(PathDetailNBC.getMiRNADat(), cmbSpecies.getSelectedValue().getAbbrName());
		if (chckbxGenomwide.isSelected()) {
			extractSmallRNASeq.setOutHairpinRNA(FileOperate.changeFileSuffix(txtSavePath.getText(), "_pre", null));
		}
		extractSmallRNASeq.getSeq();
	}
	
	/**
	 * @param lsInfo
	 * @param colChrID
	 * @param colSummit
	 * @param upstream 上游为负数
	 * @param dowstream 下游为正数
	 */
	private void runGetSeqSite(ArrayList<String[]> lsInfo, int colChrID, int colSummit, int upstream, int dowstream) {
		ctrlGetSeq.reset();
		Species species = cmbSpecies.getSelectedValue();
		species.setVersion(cmbSpeciesVersion.getSelectedValue());
		ctrlGetSeq.setSpecies(species);
		ctrlGetSeq.setOutPutFile(txtSavePath.getText());
		ctrlGetSeq.setGetAAseq(chckbxGetaminoacid.isSelected());
		
		ArrayList<SiteSeqInfo> lsSiteInfo = new ArrayList<SiteSeqInfo>();
		for (String[] info : lsInfo) {
			String chrID = info[colChrID];
			int Summit = 0;
			try {
				Summit = Integer.parseInt(info[colSummit]);
			} catch (Exception e) {
				continue;
			}
			int start = Summit + upstream;
			int end = Summit + dowstream;
			SiteSeqInfo siteInfo = new SiteSeqInfo(chrID, start, end);
			lsSiteInfo.add(siteInfo);
		}
		
		if (lsSiteInfo.size() > maxSeqNum) {
			JOptionPane.showMessageDialog(null, "To Protect Your HardDisk, Only " + maxSeqNum + " Sequence Will Be Queried.", "Warning", JOptionPane.WARNING_MESSAGE);
		}
		ctrlGetSeq.setGetSeqSite(lsSiteInfo);
		ctrlGetSeq.execute();
	}
	
	/**
	 * @param lsInfo
	 * @param colChrID
	 * @param colSummit
	 * @param upstream 上游为负数
	 * @param dowstream 下游为正数
	 */
	private void runGetSeqGene(ArrayList<String[]> lsInfo, int colGeneID, int upstream, int dowstream) {
		ctrlGetSeq.reset();
		Species species = cmbSpecies.getSelectedValue();
		species.setVersion(cmbSpeciesVersion.getSelectedValue());
		ctrlGetSeq.setSpecies(species);
		ctrlGetSeq.setOutPutFile(txtSavePath.getText());
		ctrlGetSeq.setGetAAseq(chckbxGetaminoacid.isSelected());
		ctrlGetSeq.setUpAndDownStream(new int[]{upstream, dowstream});
		ctrlGetSeq.setGetAllIso(true);
		ctrlGetSeq.setGeneStructure(cmbGeneStructure.getSelectedValue());
		ctrlGetSeq.setGetIntron(false);
		
		ArrayList<String> lsIsoName = new ArrayList<String>();
		for (String[] info : lsInfo) {
			lsIsoName.add(info[colGeneID]);
		}
		ctrlGetSeq.setGetSeqIso(lsIsoName);
		ctrlGetSeq.execute();
	}
	
	/**
	 * @param lsInfo
	 * @param colChrID
	 * @param colSummit
	 * @param upstream 上游为负数
	 * @param dowstream 下游为正数
	 */
	private void runGetSeqGeneGenomeWide(int upstream, int dowstream) {
		ctrlGetSeq.reset();
 		Species species = cmbSpecies.getSelectedValue();
		species.setVersion(cmbSpeciesVersion.getSelectedValue());
		ctrlGetSeq.setSpecies(species);
		ctrlGetSeq.setOutPutFile(txtSavePath.getText());
		ctrlGetSeq.setGetAAseq(chckbxGetaminoacid.isSelected());
		ctrlGetSeq.setUpAndDownStream(new int[]{upstream, dowstream});
		ctrlGetSeq.setGetAllIso(chckbxGetalliso.isSelected());
		ctrlGetSeq.setGeneStructure(cmbGeneStructure.getSelectedValue());
		ctrlGetSeq.setGetIntron(false);
		ctrlGetSeq.setGetSeqIsoGenomWide();
		ctrlGetSeq.execute();
	}
	
	public JProgressBar getProgressBar() {
		return progressBar;
	}
	public JButton getBtnOpen() {
		return btnOpenfile;
	}
	public JButton getBtnSave() {
		return btnSave;
	}
	public JButton getBtnRun() {
		return btnRun;
	}
}
