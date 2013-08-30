package com.novelbio.nbcgui.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import com.novelbio.analysis.seq.fasta.SeqFasta;

public class GuiSeqCope extends JPanel {
	JTextPane txtIn;
	JTextPane txtOut;
	JCheckBox chckbxReservecomplement;
	JCheckBox chckbxTranslate;
	/**
	 * Create the panel.
	 */
	public GuiSeqCope() {
		setLayout(null);
		
		JScrollPane sclIn = new JScrollPane();
		sclIn.setBounds(58, 29, 558, 201);
		add(sclIn);
		
		txtIn = new JTextPane();
		sclIn.setViewportView(txtIn);
		
		chckbxReservecomplement = new JCheckBox("ReserveComplement");
		chckbxReservecomplement.setBounds(58, 238, 200, 22);
		add(chckbxReservecomplement);
		
		chckbxTranslate = new JCheckBox("Translate");
		chckbxTranslate.setBounds(277, 238, 131, 22);
		add(chckbxTranslate);

		
		JScrollPane sclOut = new JScrollPane();
		sclOut.setBounds(58, 294, 558, 185);
		add(sclOut);
		
		txtOut = new JTextPane();
		sclOut.setViewportView(txtOut);
		
		JButton btnRun = new JButton("Run");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String seq = txtIn.getText();
				seq = seq.replace(" ", "").replace("\r", "").replace("\n", "").replace("\t", "");
				SeqFasta seqFasta = new SeqFasta(seq);
				if (chckbxReservecomplement.isSelected()) {
					seqFasta = seqFasta.reservecom();
				}
				
				if (chckbxTranslate.isSelected()) {
					txtOut.setText(seqFasta.toStringAA(true));
				} else {
					txtOut.setText(seqFasta.toString());
				}
				
				
			}
		});
		btnRun.setBounds(502, 242, 118, 24);
		add(btnRun);
	}
}
