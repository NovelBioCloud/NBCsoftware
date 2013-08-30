package com.novelbio.nbcgui.GUI;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;

import com.novelbio.analysis.seq.genome.GffChrAbs;
import com.novelbio.analysis.seq.mirna.MiRNAtargetRNAhybrid;
import com.novelbio.analysis.seq.mirna.MiRNAtargetRNAhybrid.RNAhybridClass;
import com.novelbio.base.gui.GUIFileOpen;
import com.novelbio.base.gui.JComboBoxData;
import com.novelbio.base.gui.JTextFieldData;
import com.novelbio.database.model.species.Species;
import com.novelbio.nbcgui.controlseq.CtrlMiRNAtargetPredict;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GuiMirnaTargetPredict  extends JPanel {

	private JTextField txtUTR3Seq;
	private JTextField txtMirSeq;
	private JTextFieldData txtScore;
	private JTextFieldData txtPvalue;
	private JTextFieldData txtEnergy;
	private JTextField txtOutput;
	
	JButton btnOpenUTR3Seq;
	JButton btnMiRNAseq;
	JButton btnOutput;
	
	JComboBoxData<Species> cmbSpecies;
	
	GUIFileOpen guiFileOpen = new GUIFileOpen();
	
	CtrlMiRNAtargetPredict ctrlMiRNAtargetPredict = new CtrlMiRNAtargetPredict();
	
	private JButton btnRun;
	private JComboBoxData<RNAhybridClass> cmbRNAhybridSpeciesType;
	private JLabel lblSpeciesClass;
			
	/**
	 * Create the application.
	 */
	public GuiMirnaTargetPredict() {
		setLayout(null);
		
		txtUTR3Seq = new JTextField();
		txtUTR3Seq.setBounds(12, 235, 312, 18);
		add(txtUTR3Seq);
		txtUTR3Seq.setColumns(10);
		
		JLabel lblutrseq = new JLabel("3UTRseq");
		lblutrseq.setBounds(12, 209, 86, 14);
		add(lblutrseq);
		
		btnOpenUTR3Seq = new JButton("3UTRseq");
		btnOpenUTR3Seq.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtUTR3Seq.setText(guiFileOpen.openFileName("fasta/fa", ""));
			}
		});
		btnOpenUTR3Seq.setBounds(336, 232, 118, 24);
		add(btnOpenUTR3Seq);
		
		cmbSpecies = new JComboBoxData<Species>();
		cmbSpecies.setBounds(12, 65, 312, 23);
		add(cmbSpecies);
		
		JLabel lblSpecies = new JLabel("Species");
		lblSpecies.setBounds(12, 41, 69, 14);
		add(lblSpecies);
		
		txtMirSeq = new JTextField();
		txtMirSeq.setBounds(12, 158, 312, 18);
		add(txtMirSeq);
		txtMirSeq.setColumns(10);
		
		JLabel lblMirnaseq = new JLabel("miRNAseq");
		lblMirnaseq.setBounds(12, 121, 97, 14);
		add(lblMirnaseq);
		
		btnMiRNAseq = new JButton("MiRNAseq");
		btnMiRNAseq.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtMirSeq.setText(guiFileOpen.openFileName("fastq/fasta", ""));
			}
		});
		btnMiRNAseq.setBounds(336, 155, 118, 24);
		add(btnMiRNAseq);
		
		txtScore = new JTextFieldData();
		txtScore.setBounds(12, 295, 114, 18);
		add(txtScore);
		txtScore.setColumns(10);
		
		txtPvalue = new JTextFieldData();
		txtPvalue.setBounds(164, 295, 114, 18);
		add(txtPvalue);
		txtPvalue.setColumns(10);
		
		txtEnergy = new JTextFieldData();
		txtEnergy.setBounds(324, 295, 114, 18);
		add(txtEnergy);
		txtEnergy.setColumns(10);
		
		JLabel lblScore = new JLabel("score");
		lblScore.setBounds(12, 275, 69, 14);
		add(lblScore);
		
		JLabel lblPvalue = new JLabel("pvalue");
		lblPvalue.setBounds(164, 275, 69, 14);
		add(lblPvalue);
		
		JLabel lblEnergy = new JLabel("energy");
		lblEnergy.setBounds(324, 275, 69, 14);
		add(lblEnergy);
		
		txtOutput = new JTextField();
		txtOutput.setBounds(12, 395, 312, 18);
		add(txtOutput);
		txtOutput.setColumns(10);
		
		btnOutput = new JButton("OutPut");
		btnOutput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtOutput.setText(guiFileOpen.saveFileName("txt", ""));
			}
		});
		btnOutput.setBounds(336, 392, 118, 24);
		add(btnOutput);
		
		btnRun = new JButton("Run");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GffChrAbs gffChrAbs = new GffChrAbs(cmbSpecies.getSelectedValue());
				ctrlMiRNAtargetPredict.setMirTargetOverlap(txtOutput.getText());
				
				ctrlMiRNAtargetPredict.setGffChrAbs(gffChrAbs);
				ctrlMiRNAtargetPredict.setTargetEnergy(Integer.parseInt(txtEnergy.getText()));
				ctrlMiRNAtargetPredict.setTargetPvalue(Double.parseDouble(txtPvalue.getText()));
				ctrlMiRNAtargetPredict.setTargetScore(Integer.parseInt(txtScore.getText()));
				
				ctrlMiRNAtargetPredict.setInputMiRNAseq(txtMirSeq.getText());
				ctrlMiRNAtargetPredict.setInputUTR3File(txtUTR3Seq.getText());
				ctrlMiRNAtargetPredict.setSpeciesType(cmbRNAhybridSpeciesType.getSelectedValue());
				
				
				ctrlMiRNAtargetPredict.predict();
			}
		});
		btnRun.setBounds(336, 441, 118, 24);
		add(btnRun);
		
		cmbRNAhybridSpeciesType = new JComboBoxData<RNAhybridClass>();
		cmbRNAhybridSpeciesType.setBounds(12, 360, 221, 23);
		add(cmbRNAhybridSpeciesType);
		
		lblSpeciesClass = new JLabel("Species Class");
		lblSpeciesClass.setBounds(12, 337, 114, 14);
		add(lblSpeciesClass);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		cmbSpecies.setMapItem(Species.getSpeciesName2Species(Species.SEQINFO_SPECIES));
		txtEnergy.setNumOnly();
		txtScore.setNumOnly();
		txtPvalue.setNumOnly(5);
		cmbRNAhybridSpeciesType.setMapItem(MiRNAtargetRNAhybrid.getMapSpeciesType2HybridClass());
		txtEnergy.setText(15 + "");
		txtScore.setText(140 + "");
		txtPvalue.setText(0.001 + "");
	}
}
