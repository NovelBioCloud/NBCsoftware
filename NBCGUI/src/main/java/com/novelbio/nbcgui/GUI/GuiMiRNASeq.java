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
import com.novelbio.analysis.seq.mirna.CtrlMiRNApipeline;
import com.novelbio.analysis.seq.sam.SamFile;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.gui.GUIFileOpen;
import com.novelbio.base.gui.JComboBoxData;
import com.novelbio.base.gui.JScrollPaneData;
import com.novelbio.database.model.species.Species;
import com.novelbio.database.model.species.Species.EnumSpeciesType;

public class GuiMiRNASeq extends JPanel{
	private static final long serialVersionUID = -5940420720636777182L;
	private JFrame frame;
	private JTextField txtOutPathPrefix;
	JCheckBox chkMapAllBedFileToGenome;

	JButton btnRunning;
	JCheckBox chkMapping;
	JCheckBox chckbxMappingToSpecies;
	
	JButton btnOutpath;
	JScrollPaneData sclpanFastq;
	JScrollPaneData sclAnnoMiRNA;
	JCheckBox chkPredictMiRNA;
	JScrollPaneData sclNovelMiRNAbed;
	JButton btnDelFastQfilerow;
	
	ArrayList<Component> lsComponentsMapping = new ArrayList<Component>();
	ArrayList<Component> lsComponentsPredictMiRNA = new ArrayList<Component>();
	HashSet<Component> lsComponentsAll = new HashSet<Component>();
	
	GUIFileOpen guiFileOpen = new GUIFileOpen();
	
	GuiLayeredPaneSpeciesVersionGff guiSpeciesVersionGff;
	JCheckBox chckCoverExistResult;
	
	private JButton btnFastqfile;
	private JButton btnNovelmirnabed;
	private JButton btnDelNovelMiRNAbedFileRow;
	private JCheckBox chkMapAllToRfam;
	private JTextField txtThread;
	
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
		btnRunning.setBounds(709, 555, 95, 24);
		add(btnRunning);
		
		chkMapping = new JCheckBox("MappingAndAnalysis");
		chkMapping.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chkSelected(chkMapping.isSelected(), chkPredictMiRNA.isSelected());
			}
		});
		chkMapping.setBounds(70, 556, 206, 22);
		add(chkMapping);
		
		txtOutPathPrefix = new JTextField();
		txtOutPathPrefix.setBounds(441, 520, 233, 21);
		add(txtOutPathPrefix);
		txtOutPathPrefix.setColumns(10);
		
		JLabel lblOutpathprefix = new JLabel("OutPathPrefix");
		lblOutpathprefix.setBounds(441, 495, 105, 20);
		add(lblOutpathprefix);
		
		btnOutpath = new JButton("OutPath");
		btnOutpath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String fileName = guiFileOpen.saveFileNameAndPath("prefix", "");
				txtOutPathPrefix.setText(fileName);
			}
		});
		btnOutpath.setBounds(709, 519, 95, 24);
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
		chkPredictMiRNA.setBounds(320, 556, 131, 22);
		add(chkPredictMiRNA);
		
	 
		
		guiSpeciesVersionGff = new GuiLayeredPaneSpeciesVersionGff();
		guiSpeciesVersionGff.setBounds(23, 340, 243, 148);
		add(guiSpeciesVersionGff);
		
		chkMapAllToRfam = new JCheckBox("Mapping All To Rfam");
		chkMapAllToRfam.setSelected(true);
		chkMapAllToRfam.setBounds(16, 307, 178, 22);
		add(chkMapAllToRfam);
		
		chckbxMappingToSpecies = new JCheckBox("Mapping To Species Specific Rfam");
		chckbxMappingToSpecies.setBounds(454, 305, 312, 22);
		add(chckbxMappingToSpecies);
		
		sclAnnoMiRNA = new JScrollPaneData();
		JComboBoxData<Species> cmbSpeciesBlast = new JComboBoxData<Species>();
		cmbSpeciesBlast.setMapItem(Species.getSpeciesName2Species(EnumSpeciesType.Genome));
		sclAnnoMiRNA.setTitle(new String[]{"BlastSpecies"});
		sclAnnoMiRNA.setItem(0, cmbSpeciesBlast);
		sclAnnoMiRNA.setBounds(441, 366, 233, 95);
		add(sclAnnoMiRNA);
		
		JLabel lblNovelMirnaAnnotation = new JLabel("Novel MiRNA Annotation");
		lblNovelMirnaAnnotation.setBounds(441, 340, 178, 18);
		add(lblNovelMirnaAnnotation);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sclAnnoMiRNA.addItem(new String[]{""});
			}
		});
		btnAdd.setBounds(686, 360, 102, 28);
		add(btnAdd);
		
		JButton btnDel = new JButton("Del");
		btnDel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sclAnnoMiRNA.deleteSelRows();
			}
		});
		btnDel.setBounds(686, 432, 102, 28);
		add(btnDel);
		
		chckCoverExistResult = new JCheckBox("cover exist result");
		chckCoverExistResult.setBounds(831, 554, 184, 26);
		add(chckCoverExistResult);
		
		txtThread = new JTextField();
		txtThread.setText("7");
		txtThread.setBounds(121, 520, 70, 22);
		add(txtThread);
		txtThread.setColumns(10);
		
		JLabel lblThreadnum = new JLabel("ThreadNum");
		lblThreadnum.setBounds(38, 522, 81, 18);
		add(lblThreadnum);
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
		CtrlMiRNApipeline ctrlMiRNApipeline = new CtrlMiRNApipeline(species);
		try {
			ctrlMiRNApipeline.setThreadNum(Integer.parseInt(txtThread.getText()));
		} catch (Exception e) {
		}

		Map<String, AlignSeq> mapPrefix2AlignSeq = new LinkedHashMap<>();
		if (chkMapping.isSelected()) {
			ctrlMiRNApipeline.setMapMirna(true);
			List<String[]> lsFastq2Prefix = sclpanFastq.getLsDataInfo();
			Map<String, String> mapPrefix2Fastq = new LinkedHashMap<>();
			for (String[] strings : lsFastq2Prefix) {
				mapPrefix2Fastq.put(strings[1], strings[0]);
			}
			ctrlMiRNApipeline.setMapPrefix2Fastq(mapPrefix2Fastq);
		}
		if (chkPredictMiRNA.isSelected()) {
			ctrlMiRNApipeline.setPredictMirna(true);
			//如果没有mapping，则取输入的bed文件
			if (mapPrefix2AlignSeq.size() == 0) {
				List<String[]> lsInfo = sclNovelMiRNAbed.getLsDataInfo();
				for (String[] strings : lsInfo) {
					AlignSeq alignSeq = null;
					if (FormatSeq.getFileType(strings[0]) == FormatSeq.BED) {
						alignSeq = new BedSeq(strings[0]);
					} else if (FormatSeq.getFileType(strings[0]) == FormatSeq.BAM || FormatSeq.getFileType(strings[0]) == FormatSeq.SAM) {
						alignSeq = new SamFile(strings[0]);
					}
					
					if (alignSeq != null) {
						mapPrefix2AlignSeq.put(strings[1], alignSeq);
					}
				}
			}
			ctrlMiRNApipeline.setMapPrefix2AlignFile(mapPrefix2AlignSeq);
		}
		List<String[]> lsBlastTo = sclAnnoMiRNA.getLsDataInfo();
		List<Species> lsSpeciesBlastTo = new ArrayList<>();
		for (String[] strings : lsBlastTo) {
			Species speciesBlastTo = Species.getSpeciesName2Species(EnumSpeciesType.Genome).get(strings[0]);
			lsSpeciesBlastTo.add(speciesBlastTo);
		}
		ctrlMiRNApipeline.setLsSpeciesBlastTo(lsSpeciesBlastTo);
		ctrlMiRNApipeline.setOutPath(txtOutPathPrefix.getText());
		ctrlMiRNApipeline.setIsUseOldResult(!chckCoverExistResult.isSelected());
		ctrlMiRNApipeline.run();
	}
}
