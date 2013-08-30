package com.novelbio.nbcgui.GUI;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JLabel;

import org.apache.poi.hssf.record.FilePassRecord;
import org.apache.poi.hssf.record.SCLRecord;

import com.novelbio.analysis.seq.FormatSeq;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.gui.GUIFileOpen;
import com.novelbio.base.gui.JComboBoxData;
import com.novelbio.base.gui.JScrollPaneData;
import com.novelbio.database.model.species.Species;
import com.novelbio.nbcgui.controlseq.CtrlDGEgetvalue;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JComboBox;

public class GuiDGEgetvalue extends JPanel {
	private JTextField txtSavePathPrefix;
	JButton btnDelete;
	
	ButtonGroup buttonGroup = new ButtonGroup();
	JRadioButton rdSamBamFile;
	JRadioButton rdBedFile;
	
	JCheckBox chckbxFileIsAlready;
	JButton btnSaveto;
	
	JComboBoxData<Species> cmbSpecies;
	JComboBoxData<String> cmbVersion;
	
	GUIFileOpen guiFileOpen = new GUIFileOpen();
	
	JScrollPaneData scrlAlignSeqFile;
	
	CtrlDGEgetvalue ctrlDGEgetvalue = new CtrlDGEgetvalue();
	private JButton btnRun;
	
	/**
	 * Create the panel.
	 */
	public GuiDGEgetvalue() {
		setLayout(null);
		
		scrlAlignSeqFile = new JScrollPaneData();
		scrlAlignSeqFile.setBounds(23, 30, 556, 102);
		add(scrlAlignSeqFile);
		
		JButton btnAddAlignSeq = new JButton("AddFile");
		btnAddAlignSeq.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> lsFileName = guiFileOpen.openLsFileName("Sam/Bam/Bed", "");
				ArrayList<String[]> lsFile2Prefix = new ArrayList<String[]>();
				for (String fileName : lsFileName) {
					String[] tmp = new String[2];
					tmp[0] = fileName;
					tmp[1] = FileOperate.getFileNameSep(fileName)[0].split("_")[0];
					lsFile2Prefix.add(tmp);
				}
				scrlAlignSeqFile.addItemLs(lsFile2Prefix);
			}
		});
		btnAddAlignSeq.setBounds(591, 30, 118, 24);
		add(btnAddAlignSeq);
		
		btnDelete = new JButton("DelFile");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scrlAlignSeqFile.deleteSelRows();
			}
		});
		btnDelete.setBounds(591, 108, 118, 24);
		add(btnDelete);
		
		rdSamBamFile = new JRadioButton("SamBamFile");
		rdSamBamFile.setBounds(23, 140, 151, 22);
		add(rdSamBamFile);
		
		rdBedFile = new JRadioButton("BedFile");
		rdBedFile.setBounds(211, 140, 151, 22);
		add(rdBedFile);
		
		txtSavePathPrefix = new JTextField();
		txtSavePathPrefix.setBounds(23, 306, 439, 18);
		add(txtSavePathPrefix);
		txtSavePathPrefix.setColumns(10);
		
		btnSaveto = new JButton("SaveTo");
		btnSaveto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtSavePathPrefix.setText(guiFileOpen.openFileName("", ""));
			}
		});
		btnSaveto.setBounds(474, 303, 105, 24);
		add(btnSaveto);
		
		JLabel lblSaveto = new JLabel("SaveTo");
		lblSaveto.setBounds(23, 286, 69, 14);
		add(lblSaveto);
		
		JLabel lblBamallbed = new JLabel("BamAllBed");
		lblBamallbed.setBounds(23, 12, 118, 14);
		add(lblBamallbed);
		
		chckbxFileIsAlready = new JCheckBox("File Is Already Sorted");
		chckbxFileIsAlready.setBounds(23, 256, 198, 22);
		add(chckbxFileIsAlready);
		
		btnRun = new JButton("Run");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<String[]> lsFile2Prefix = scrlAlignSeqFile.getLsDataInfo();
				FormatSeq formatSeq = null;
				if (rdBedFile.isSelected()) {
					formatSeq = FormatSeq.BED;
				}
				else if (rdSamBamFile.isSelected()) {
					formatSeq = FormatSeq.BAM;
				}
				String outFile = txtSavePathPrefix.getText();
				ctrlDGEgetvalue.setLsAlignSeq(lsFile2Prefix, formatSeq, outFile);
				ctrlDGEgetvalue.setSpecies(getSpecies());
				if (!chckbxFileIsAlready.isSelected()) {
					ctrlDGEgetvalue.sort();
				}
				ctrlDGEgetvalue.dgeCal();
			}
		});
		btnRun.setBounds(612, 303, 118, 24);
		add(btnRun);
		
		cmbSpecies = new JComboBoxData<Species>();
		cmbSpecies.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectCmbSpecies();
			}
		});
		cmbSpecies.setBounds(24, 213, 198, 23);
		add(cmbSpecies);
		
		cmbVersion = new JComboBoxData<String>();
		cmbVersion.setBounds(256, 213, 206, 23);
		add(cmbVersion);
		
		JLabel lblSpecies = new JLabel("Species");
		lblSpecies.setBounds(23, 187, 69, 14);
		add(lblSpecies);
		
		JLabel lblVersion = new JLabel("Version");
		lblVersion.setBounds(256, 187, 69, 14);
		add(lblVersion);
		
		initial();
	}
	
	private void initial() {
		scrlAlignSeqFile.setTitle(new String[]{"FileName", "Prefix"});
		buttonGroup.add(rdBedFile);
		buttonGroup.add(rdSamBamFile);
		rdBedFile.setSelected(true);
		cmbSpecies.setMapItem(Species.getSpeciesName2Species(Species.SEQINFO_SPECIES));
		selectCmbSpecies();
	}
	
	private void selectCmbSpecies() {
		Species species = cmbSpecies.getSelectedValue();
		cmbVersion.setMapItem(species.getMapVersion());
	}
	
	private Species getSpecies() {
		Species species = cmbSpecies.getSelectedValue();
		if (species.getTaxID() != 0) {
			species.setVersion(cmbVersion.getSelectedValue());
		}
		return species;
	}
}
