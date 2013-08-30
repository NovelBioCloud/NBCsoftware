package com.novelbio.nbcgui.GUI;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.novelbio.analysis.seq.AlignSeq;
import com.novelbio.analysis.seq.FormatSeq;
import com.novelbio.analysis.seq.bed.BedSeq;
import com.novelbio.analysis.seq.genome.GffChrAbs;
import com.novelbio.analysis.seq.mirna.CtrlMiRNAfastq;
import com.novelbio.analysis.seq.mirna.CtrlMiRNApredict;
import com.novelbio.analysis.seq.sam.SamFile;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.gui.GUIFileOpen;
import com.novelbio.base.gui.JScrollPaneData;
import com.novelbio.database.model.species.Species;
import com.novelbio.generalConf.PathDetailNBC;

public class GuiMiRNASeq extends JPanel{
	private static final long serialVersionUID = -5940420720636777182L;
	private JFrame frame;
	private JTextField txtOutPathPrefix;
	JCheckBox chkMapAllBedFileToGenome;

	JButton btnRunning;
	JCheckBox chkMapping;
	JButton btnOutpath;
	JScrollPaneData sclpanFastq;
	JCheckBox chkPredictMiRNA;
	JScrollPaneData sclNovelMiRNAbed;
	JButton btnDelFastQfilerow;
	
	ArrayList<Component> lsComponentsMapping = new ArrayList<Component>();
	ArrayList<Component> lsComponentsPredictMiRNA = new ArrayList<Component>();
	HashSet<Component> lsComponentsAll = new HashSet<Component>();
	
	GUIFileOpen guiFileOpen = new GUIFileOpen();
	
	CtrlMiRNAfastq ctrlMiRNAfastq = new CtrlMiRNAfastq();
	CtrlMiRNApredict ctrlMiRNApredict = new CtrlMiRNApredict();
	GuiLayeredPaneSpeciesVersionGff guiSpeciesVersionGff;
	
	private JButton btnFastqfile;
	private JButton btnNovelmirnabed;
	private JButton btnDelNovelMiRNAbedFileRow;
	private JCheckBox chkMapAllToRfam;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GuiMiRNASeq window = new GuiMiRNASeq();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GuiMiRNASeq() {
		setLayout(null);

		//是否将全部的bed文件mapping至基因组上，用于看基因组上的reads分布
		chkMapAllBedFileToGenome = new JCheckBox("Mapping All To Genome");
		chkMapAllBedFileToGenome.setBounds(227, 306, 197, 22);
		add(chkMapAllBedFileToGenome);

		
		btnRunning = new JButton("Running");
		btnRunning.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				running();
			}
		});
		btnRunning.setBounds(705, 526, 92, 24);
		add(btnRunning);
		
		chkMapping = new JCheckBox("MappingAndAnalysis");
		chkMapping.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chkSelected(chkMapping.isSelected(), chkPredictMiRNA.isSelected());
			}
		});
		chkMapping.setBounds(79, 527, 206, 22);
		add(chkMapping);
		
		txtOutPathPrefix = new JTextField();
		txtOutPathPrefix.setBounds(439, 470, 233, 18);
		add(txtOutPathPrefix);
		txtOutPathPrefix.setColumns(10);
		
		JLabel lblOutpathprefix = new JLabel("OutPathPrefix");
		lblOutpathprefix.setBounds(440, 447, 105, 20);
		add(lblOutpathprefix);
		
		btnOutpath = new JButton("OutPath");
		btnOutpath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String fileName = guiFileOpen.saveFileNameAndPath("prefix", "");
				txtOutPathPrefix.setText(fileName);
			}
		});
		btnOutpath.setBounds(709, 465, 95, 24);
		add(btnOutpath);
		
		btnFastqfile = new JButton("FastqFile");
		btnFastqfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> lsFileName = guiFileOpen.openLsFileName("fastq", "");
				for (String string : lsFileName) {
					String prefix = FileOperate.getFileNameSep(string)[0].split("_")[0];
					sclpanFastq.addItem(new String[]{string, prefix});
				}
			}
		});
		btnFastqfile.setBounds(427, 16, 118, 24);
		add(btnFastqfile);
		
		sclpanFastq = new JScrollPaneData();
		sclpanFastq.setBounds(22, 20, 393, 265);
		add(sclpanFastq);
		
		sclNovelMiRNAbed = new JScrollPaneData();
		sclNovelMiRNAbed.setBounds(582, 16, 252, 271);
		add(sclNovelMiRNAbed);
		
		btnDelFastQfilerow = new JButton("DelRow");
		btnDelFastQfilerow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sclpanFastq.deleteSelRows();
			}
		});
		btnDelFastQfilerow.setBounds(427, 61, 118, 24);
		add(btnDelFastQfilerow);
		
		btnNovelmirnabed = new JButton("NovelMiRNABed");
		btnNovelmirnabed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> lsFileName = guiFileOpen.openLsFileName("bed file", "");
				for (String string : lsFileName) {
					sclNovelMiRNAbed.addItem(new String[]{string, FileOperate.getFileNameSep(string)[0]});
				}
			}
		});
		btnNovelmirnabed.setBounds(846, 16, 145, 24);
		add(btnNovelmirnabed);
		
		btnDelNovelMiRNAbedFileRow = new JButton("DelRow");
		btnDelNovelMiRNAbedFileRow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sclNovelMiRNAbed.deleteSelRows();
			}
		});
		btnDelNovelMiRNAbedFileRow.setBounds(846, 52, 118, 24);
		add(btnDelNovelMiRNAbedFileRow);
		
		chkPredictMiRNA = new JCheckBox("PredictMiRNA");
		chkPredictMiRNA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chkSelected(chkMapping.isSelected(), chkPredictMiRNA.isSelected());
			}
		});
		chkPredictMiRNA.setBounds(315, 527, 131, 22);
		add(chkPredictMiRNA);
		
	 
		
		guiSpeciesVersionGff = new GuiLayeredPaneSpeciesVersionGff();
		guiSpeciesVersionGff.setBounds(23, 340, 295, 158);
		add(guiSpeciesVersionGff);
		
		chkMapAllToRfam = new JCheckBox("Mapping All To Rfam");
		chkMapAllToRfam.setSelected(true);
		chkMapAllToRfam.setBounds(16, 307, 178, 22);
		add(chkMapAllToRfam);
		
		JCheckBox chckbxMappingToSpecies = new JCheckBox("Mapping To Species Specific Rfam");
		chckbxMappingToSpecies.setBounds(454, 305, 312, 22);
		add(chckbxMappingToSpecies);
		initialize();
	}
	
	
	private void chkSelected(boolean booChkMap, boolean booChkPredictMiRNA) {
		if (booChkMap && !booChkPredictMiRNA) {
			for (Component component : lsComponentsAll)
				component.setEnabled(false);
			for (Component component : lsComponentsMapping)
				component.setEnabled(true);
			btnRunning.setEnabled(true);
		}
		else if (booChkMap && booChkPredictMiRNA) {
			for (Component component : lsComponentsAll)
				component.setEnabled(false);
			for (Component component : lsComponentsMapping)
				component.setEnabled(true);
			btnRunning.setEnabled(true);
		}
		else if (!booChkMap && !booChkPredictMiRNA) {
			for (Component component : lsComponentsAll)
				component.setEnabled(false);
			btnRunning.setEnabled(true);
		}
		else if (!booChkMap && booChkPredictMiRNA) {
			for (Component component : lsComponentsAll)
				component.setEnabled(false);
			for (Component component : lsComponentsPredictMiRNA)
				component.setEnabled(true);
			btnRunning.setEnabled(true);
		}
		
		else if (!booChkMap && !booChkPredictMiRNA) {
			for (Component component : lsComponentsAll) {
				component.setEnabled(false);
			}
			btnRunning.setEnabled(false);
		}
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		lsComponentsMapping.add(sclpanFastq);
		
		lsComponentsMapping.add(btnDelFastQfilerow);
		lsComponentsMapping.add(chkMapAllBedFileToGenome);
		lsComponentsMapping.add(btnFastqfile);
		
		lsComponentsPredictMiRNA.add(btnDelNovelMiRNAbedFileRow);
		lsComponentsPredictMiRNA.add(btnNovelmirnabed);
		lsComponentsPredictMiRNA.add(sclNovelMiRNAbed);
				
		lsComponentsAll.addAll(lsComponentsMapping);
		lsComponentsAll.addAll(lsComponentsPredictMiRNA);

		chkMapping.setSelected(true);

		for (Component component : lsComponentsMapping) {
			component.setEnabled(true);
		}
		sclpanFastq.setTitle(new String[]{"FastqFile", "prefix"});
		sclNovelMiRNAbed.setTitle(new String[]{"BedFile", "prefix"});
	}
	
	
	private void running() {
		Species species = guiSpeciesVersionGff.getSelectSpecies();
		GffChrAbs gffChrAbs = new GffChrAbs(species);
		ctrlMiRNAfastq.clear();
		Map<AlignSeq, String> mapBedFile2Prefix = new LinkedHashMap<AlignSeq, String>();
		if (chkMapping.isSelected()) {
			runMapping(gffChrAbs, species, sclpanFastq.getLsDataInfo());
			mapBedFile2Prefix = ctrlMiRNAfastq.getMapGenomeBed2Prefix();
		}
		if (chkPredictMiRNA.isSelected()) {
			//如果没有mapping，则取输入的bed文件
			if (mapBedFile2Prefix.size() == 0) {
				List<String[]> lsInfo = sclNovelMiRNAbed.getLsDataInfo();
				for (String[] strings : lsInfo) {
					AlignSeq alignSeq = null;
					if (FormatSeq.getFileType(strings[0]) == FormatSeq.BED) {
						alignSeq = new BedSeq(strings[0]);
					} else if (FormatSeq.getFileType(strings[0]) == FormatSeq.BAM || FormatSeq.getFileType(strings[0]) == FormatSeq.SAM) {
						alignSeq = new SamFile(strings[0]);
					}
					
					if (alignSeq != null) {
						mapBedFile2Prefix.put(alignSeq, strings[1]);
					}
				}
			}
			runPredict(mapBedFile2Prefix, gffChrAbs, species);
		}
	}
	
	private void runMapping(GffChrAbs gffChrAbs, Species species, ArrayList<String[]> lsfastqFile2Prefix) {
		ctrlMiRNAfastq.setMappingAll2Genome(chkMapAllBedFileToGenome.isSelected());
		ctrlMiRNAfastq.setSpecies(species);
		ctrlMiRNAfastq.setOutPath(txtOutPathPrefix.getText());
		ctrlMiRNAfastq.setGffChrAbs(gffChrAbs);
		ctrlMiRNAfastq.setLsFastqFile(lsfastqFile2Prefix);
		ctrlMiRNAfastq.setMiRNAinfo(PathDetailNBC.getMiRNADat());
		ctrlMiRNAfastq.setRfamFile(PathDetailNBC.getRfamTab());
		ctrlMiRNAfastq.setMapAll2Rfam(chkMapAllToRfam.isSelected());
		ctrlMiRNAfastq.mappingAndCounting();
		ctrlMiRNAfastq.writeToFile();
	}
	
	private void runPredict(Map<AlignSeq, String> mapBedFile2Prefix, GffChrAbs gffChrAbs, Species species) {
		ctrlMiRNApredict.setGffChrAbs(gffChrAbs);
		ctrlMiRNApredict.setSpecies(species);
		ctrlMiRNApredict.setLsSamFile2Prefix(mapBedFile2Prefix);
		ctrlMiRNApredict.setOutPath(txtOutPathPrefix.getText());
		
		ctrlMiRNApredict.runMiRNApredict();
		ctrlMiRNApredict.writeToFile();
	}

	/**
	 * 将输出的那个txtprefix分割为outpath和prefix
	 * @return
	 * 1: prefix<br>
	 * 0: path
	 */
	private String[] getTxtOutPathAndPrefix() {
		String[] result = new String[2];
		String out = txtOutPathPrefix.getText();
		if (FileOperate.isFileDirectory(out) || out.endsWith("\\|/")) {
			result[0] = "";
			result[1] = out;
			return result;
		}
		result[0] = FileOperate.getFileName(out);
		result[1] = FileOperate.getParentPathName(out);
		return result;
	}
}
