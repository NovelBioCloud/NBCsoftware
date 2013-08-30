package com.novelbio.nbcgui.GUI;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.novelbio.analysis.annotation.pathway.kegg.kGML2DB.KGML2DB;
import com.novelbio.analysis.annotation.pathway.kegg.kGML2DB.KeggIDcvt;
import com.novelbio.analysis.seq.genome.GffSpeciesInfo;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.gui.GUIFileOpen;
import com.novelbio.base.gui.JScrollPaneData;
import com.novelbio.database.domain.geneanno.DBInfo;
import com.novelbio.database.domain.information.SoftWareInfo;
import com.novelbio.database.model.species.Species;
import com.novelbio.database.updatedb.database.UpdateGene2GO;
import com.novelbio.database.updatedb.database.UpdateGeneInfoNorm;

public class GuiUpdateDB extends JPanel {
	private static final long serialVersionUID = -7042827927643252396L;
	
	GUIFileOpen guiFileOpen = new GUIFileOpen();
	GffSpeciesInfo specieInformation = new GffSpeciesInfo();
	JScrollPaneData sclUpdateGeneInfo;
	JScrollPaneData sclUpdateGene2GO;
	JScrollPaneData sclUpdateKegg;	
	
	/**
	 * Create the panel.
	 */
	public GuiUpdateDB() {
		setForeground(Color.WHITE);
		setLayout(null);
		
		JButton btnOpengeneidinfo = new JButton("OpenGeneIDInfo");
		btnOpengeneidinfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<String> lsFiles = guiFileOpen.openLsFileName("", "");
				List<String[]> lsFiles2isGeneID = new ArrayList<String[]>();
				for (String string : lsFiles) {
					lsFiles2isGeneID.add(new String[]{string, "false"});
				}
				sclUpdateGeneInfo.addItemLs(lsFiles2isGeneID);
			}
		});
		btnOpengeneidinfo.setBounds(30, 170, 160, 24);
		add(btnOpengeneidinfo);
		
		JButton btnOpenGOinfo = new JButton("OpenGOInfo");
		btnOpenGOinfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<String> lsFiles = guiFileOpen.openLsFileName("", "");
				List<String[]> lsFiles2isGeneID = new ArrayList<String[]>();
				for (String string : lsFiles) {
					lsFiles2isGeneID.add(new String[]{string, "false"});
				}
				sclUpdateGene2GO.addItemLs(lsFiles2isGeneID);
			}
		});
		btnOpenGOinfo.setBounds(30, 349, 160, 24);
		add(btnOpenGOinfo);
		
		JButton btnUpdate = new JButton("UpDate");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				update();
			}
		});
		btnUpdate.setBounds(721, 577, 118, 24);
		add(btnUpdate);
		
		JLabel lblNewLabel = new JLabel("taxID \\t accID \\t geneID(refAccID) \\t dbinfo \\t symbol \\t synoms \\t fullName \\t description \\t pubmedID(Number)");
		lblNewLabel.setBounds(25, 23, 870, 18);
		add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("taxID \\t geneID(refAccID) \\t  goID \\t evidence \\t pubmedID(Num) \\t qualifier \\t dbinfo");
		lblNewLabel_1.setBounds(30, 206, 830, 14);
		add(lblNewLabel_1);
		
		sclUpdateGeneInfo = new JScrollPaneData();
		sclUpdateGeneInfo.setBounds(30, 53, 809, 105);
		add(sclUpdateGeneInfo);
		
		JButton btnDel = new JButton("Del");
		btnDel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sclUpdateGeneInfo.deleteSelRows();
			}
		});
		btnDel.setBounds(721, 170, 118, 24);
		add(btnDel);
		
		sclUpdateGene2GO = new JScrollPaneData();
		sclUpdateGene2GO.setBounds(30, 232, 809, 105);
		add(sclUpdateGene2GO);
		
		JButton btnDel_1 = new JButton("Del");
		btnDel_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sclUpdateGene2GO.deleteSelRows();
			}
		});
		btnDel_1.setBounds(721, 349, 118, 24);
		add(btnDel_1);
		
		JButton btnImportspecies = new JButton("ImportSpeciesFile");
		btnImportspecies.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String speciesFile = guiFileOpen.openFileName("txt/xls", "");
				if (FileOperate.isFileExistAndBigThanSize(speciesFile, 0.1)) {
					Species species = new Species();
					species.setUpdateSpeciesFile(speciesFile);
					species.update();
				}
			}
		});
		
		btnImportspecies.setBounds(172, 577, 168, 24);
		add(btnImportspecies);
		
		JButton btnImportsoftware = new JButton("ImportSoftware");
		btnImportsoftware.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String softToolsFile = guiFileOpen.openFileName("txt/xls", "");
				if (FileOperate.isFileExistAndBigThanSize(softToolsFile, 0.01)) {
					SoftWareInfo.updateInfo(true, softToolsFile);
				}
			}
		});
		btnImportsoftware.setBounds(352, 577, 160, 24);
		add(btnImportsoftware);
		
		JButton btnImportDB = new JButton("ImportDataBase");
		btnImportDB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String dbFile = guiFileOpen.openFileName("txt/xls", "");
				if (FileOperate.isFileExistAndBigThanSize(dbFile, 0.01)) {
					DBInfo.updateDBinfo(dbFile);
				}
			}
		});
		btnImportDB.setBounds(524, 577, 185, 24);
		add(btnImportDB);
		
		sclUpdateKegg = new JScrollPaneData();
		sclUpdateKegg.setBounds(30, 424, 809, 105);
		add(sclUpdateKegg);
		
		JButton btnOpenkegg = new JButton("OpenKEGG");
		btnOpenkegg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<String> lsPath = guiFileOpen.openLsFileName("", "");
				List<String[]> lsInput = new ArrayList<String[]>();
				for (String string : lsPath) {
					lsInput.add(new String[]{string});
				}
				sclUpdateKegg.addItemLs(lsInput);
			}
		});
		btnOpenkegg.setBounds(30, 541, 160, 24);
		add(btnOpenkegg);
		
		JButton btnDel_2 = new JButton("Del");
		btnDel_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sclUpdateKegg.deleteSelRows();
			}
		});
		btnDel_2.setBounds(734, 541, 106, 24);
		add(btnDel_2);
		
		JLabel lblAddKgmlfileAnd = new JLabel("One Species With Several KGMLFile And One GeneKegList In One Fold");
		lblAddKgmlfileAnd.setBounds(30, 398, 573, 14);
		add(lblAddKgmlfileAnd);
		
		JButton btnImportTaxID = new JButton("ImportTaxID");
		btnImportTaxID.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String taxFile = guiFileOpen.openFileName("txt/xls", "");
				if (FileOperate.isFileExistAndBigThanSize(taxFile, 0.1)) {
					Species species = new Species();
					species.setUpdateTaxInfo(taxFile);
					species.update();
				}
			}
		});
		btnImportTaxID.setBounds(30, 577, 130, 24);
		add(btnImportTaxID);
		
		HashMap<String, String> mapselectType = new HashMap<String, String>();
		mapselectType.put("Exon", "Exon");
		mapselectType.put("Intron", "Intron");
		mapselectType.put("GeneBG", "GeneBG");
		mapselectType.put("GeneDescription", "GeneDescription");
		mapselectType.put("GTFfile", "GTFfile");
		
		initial();
	}
	
	private void initial() {
		sclUpdateGeneInfo.setTitle(new String[]{"file"});
		sclUpdateGene2GO.setTitle(new String[]{"file"});
		
		sclUpdateKegg.setTitle(new String[]{"KGML_GeneID2KEGID_Path"});
	}
	
	
	private void update() {
		List<String[]> lsUpdateGeneInfo = sclUpdateGeneInfo.getLsDataInfo();
		for (String[] strings : lsUpdateGeneInfo) {
			UpdateGeneInfoNorm updateGeneInfoNorm = new UpdateGeneInfoNorm();
			updateGeneInfoNorm.setOverlap(true);
			updateGeneInfoNorm.setTxtWriteExcep(FileOperate.changeFileSuffix(strings[0], "_failedUpdate", null));
			updateGeneInfoNorm.updateFile(strings[0]);
		}
		
		List<String[]> lsUpdateGO = sclUpdateGene2GO.getLsDataInfo();
		for (String[] strings : lsUpdateGO) {
			UpdateGene2GO updateGene2GO = new UpdateGene2GO();
			updateGene2GO.setTxtWriteExcep(FileOperate.changeFileSuffix(strings[0], "_failedUpdate", null));
			updateGene2GO.updateFile(strings[0]);
		}
		
		List<String[]> lsUpdateKegg = sclUpdateKegg.getLsDataInfo();
		for (String[] strings : lsUpdateKegg) {
			KGML2DB.readKGML(strings[0]);
			List<String> lsKGML=FileOperate.getFoldFileNameLs(strings[0], "*", "list");
			try {
				KeggIDcvt.upDateGen2Keg(lsKGML.get(0));
			} catch (Exception e1) { e1.printStackTrace(); }
		}
	}
}
