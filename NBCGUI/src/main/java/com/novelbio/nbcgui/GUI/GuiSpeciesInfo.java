package com.novelbio.nbcgui.GUI;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;

import com.novelbio.analysis.seq.genome.GffSpeciesInfo;
import com.novelbio.analysis.seq.genome.gffOperate.GffHashGene;
import com.novelbio.analysis.seq.genome.gffOperate.GffType;
import com.novelbio.base.dataOperate.TxtReadandWrite;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.gui.GUIFileOpen;
import com.novelbio.base.gui.JComboBoxData;
import com.novelbio.base.gui.JScrollPaneData;
import com.novelbio.database.model.species.Species;
import com.novelbio.nbcgui.GUI.GuiLayeredPaneSpeciesVersionGff;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JComboBox;

public class GuiSpeciesInfo extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7042827927643252396L;
	private JTextField txtOutPath;
	GUIFileOpen guiFileOpen = new GUIFileOpen();
	JScrollPaneData scrollPaneData;
	JComboBoxData<String> comboBoxData;
	JComboBoxData<GffType> cmbGTFtype;
	
	GuiLayeredPaneSpeciesVersionGff guiLayeredPanSpeciesVersion;
	GffSpeciesInfo specieInformation = new GffSpeciesInfo();
	private JTextField txtGTFfile;
	
	/**
	 * Create the panel.
	 */
	public GuiSpeciesInfo() {
		setForeground(Color.WHITE);
		setLayout(null);
		
		JLabel lblSpecieinformation = new JLabel("SpecieInformation");
		lblSpecieinformation.setFont(new Font("Dialog", Font.BOLD, 16));
		lblSpecieinformation.setBounds(95, 12, 177, 24);
		add(lblSpecieinformation);
				
		guiLayeredPanSpeciesVersion = new GuiLayeredPaneSpeciesVersionGff();
		guiLayeredPanSpeciesVersion.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				
			}
		});
		guiLayeredPanSpeciesVersion.setBounds(95, 48, 237, 153);
		add(guiLayeredPanSpeciesVersion);
		
		txtOutPath = new JTextField();
		txtOutPath.setBounds(546, 48, 237, 31);
		add(txtOutPath);
		txtOutPath.setColumns(10);
		
		JButton btnOutFile = new JButton("OUTFile");
		btnOutFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			 String outFile =  guiFileOpen.saveFileNameAndPath("", "");
			 txtOutPath.setText(outFile);
			}
		});
		btnOutFile.setBounds(546, 91, 111, 31);
		add(btnOutFile);
		
		JButton btnSaveInfo = new JButton("Save  Info");
		btnSaveInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Species species = guiLayeredPanSpeciesVersion.getSelectSpecies();
				specieInformation.setSpecies(species);
				String selectType =  comboBoxData.getSelectedValue();
				String outpath = txtOutPath.getText();
				if (selectType.equals("Exon")) {
					specieInformation.writeExonLength(outpath);
				} else if (selectType.equals("Intron")) {
					specieInformation.writeIntronLength(outpath);
				} else if (selectType.equals("GeneBG")) {
					specieInformation.writeGeneBG(outpath);
				} else if (selectType.equals("GeneDescription")) {
					specieInformation.writeGeneDescription(outpath);
				} else if (selectType.equals("GTFfile")) {
					specieInformation.getGffChrAbs().getGffHashGene().writeToGTF(outpath + species.getAbbrName() + "_GTFfile.gtf");
				}
			}
		});
		btnSaveInfo.setBounds(661, 91, 124, 31);
		add(btnSaveInfo);
		
		scrollPaneData = new JScrollPaneData();
		scrollPaneData.setBounds(95, 213, 697, 303);
		add(scrollPaneData);
		
		HashMap<String, String> mapselectType = new HashMap<String, String>();
		mapselectType.put("Exon", "Exon");
		mapselectType.put("Intron", "Intron");
		mapselectType.put("GeneBG", "GeneBG");
		mapselectType.put("GeneDescription", "GeneDescription");
		mapselectType.put("GTFfile", "GTFfile");
		comboBoxData = new JComboBoxData<String>();
		comboBoxData.setBounds(344, 48, 189, 31);
		comboBoxData.setMapItem(mapselectType);
		add(comboBoxData);
		
		JButton btnSelectChrLength = new JButton("SelectChrLength");
		btnSelectChrLength.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				scrollPaneData.clean();
				Species species = guiLayeredPanSpeciesVersion.getSelectSpecies();
				ArrayList<String[]> lsChrAndLength = new ArrayList<String[]>();
				lsChrAndLength = specieInformation.sortLsChrInfo( specieInformation.getChrLength(species));
				String[] title = {"Chromosome","Length"};
 				scrollPaneData.setTitle(title);
				scrollPaneData.addItemLs(lsChrAndLength);
			}
		});
		btnSelectChrLength.setBounds(344, 173, 189, 31);
		add(btnSelectChrLength);
		
		JButton btnSaveTo = new JButton("Save  ChrInfo");
		btnSaveTo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String saveToPathAndFile= guiFileOpen.saveFileName("", "");
				ArrayList<String[]> dataInfo = scrollPaneData.getLsDataInfo();
				TxtReadandWrite txtWrite = new TxtReadandWrite(saveToPathAndFile, true);
				for (String[] strings : dataInfo) {
					txtWrite.writefileln(strings[0]  + "\t" + strings[1]);
				}
				txtWrite.close();
			}
		});
		btnSaveTo.setBounds(640, 173, 146, 28);
		add(btnSaveTo);
		
		txtGTFfile = new JTextField();
		txtGTFfile.setBounds(267, 551, 401, 21);
		add(txtGTFfile);
		txtGTFfile.setColumns(10);
		
		JButton btnOpenGTF = new JButton("Open");
		btnOpenGTF.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtGTFfile.setText(guiFileOpen.openFileName("", ""));
			}
		});
		btnOpenGTF.setBounds(676, 549, 118, 24);
		add(btnOpenGTF);
		
		JLabel lblConverttogtf = new JLabel("ConvertToGTF");
		lblConverttogtf.setBounds(95, 527, 132, 18);
		add(lblConverttogtf);
		
		cmbGTFtype = new JComboBoxData<GffType>();
		cmbGTFtype.setMapItem(GffType.getMapGffTypeSimple());
		cmbGTFtype.setBounds(95, 549, 163, 23);
		add(cmbGTFtype);
		
		JButton btnSaveGTF = new JButton("Save");
		btnSaveGTF.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String out = guiFileOpen.openFileName("", "");
				GffHashGene gffHashGene = new GffHashGene(cmbGTFtype.getSelectedValue(), txtGTFfile.getText());
				gffHashGene.writeToGTF(FileOperate.changeFileSuffix(out, "", "gtf"));
			}
		});
		btnSaveGTF.setBounds(676, 589, 118, 24);
		add(btnSaveGTF);
		
		
	}
}
