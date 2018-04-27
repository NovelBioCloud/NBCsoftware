package com.novelbio.nbcgui.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.gui.GUIFileOpen;
import com.novelbio.base.gui.JComboBoxData;
import com.novelbio.base.gui.JScrollPaneData;
import com.novelbio.database.domain.species.Species;
import com.novelbio.database.domain.species.Species.EnumSpeciesType;

public class GuiFilterRrna extends JPanel {
	private JTextField txtRrna;
	private JTextField txtSaveTo;
	GUIFileOpen guiFileOpen = new GUIFileOpen();
	JScrollPaneData sclLeft;
	JScrollPaneData sclRight;
	JComboBoxData<Species> comboBox;
	private JButton btnOpenfileLeft;
	private JButton btnOpenfileRight;
	/**
	 * Create the panel.
	 */
	public GuiFilterRrna() {
		setLayout(null);
		
		sclLeft = new JScrollPaneData();
		sclLeft.setTitle(new String[]{"FastqFile", "prefix"});
		sclLeft.setBounds(39, 12, 356, 221);
		add(sclLeft);
		
		sclRight = new JScrollPaneData();
		sclRight.setTitle(new String[]{"FastqFile"});
		sclRight.setBounds(454, 12, 386, 221);
		add(sclRight);
		
		txtRrna = new JTextField();
		txtRrna.setBounds(39, 427, 326, 22);
		add(txtRrna);
		txtRrna.setColumns(10);
		
		JButton btnOpenRrnaFile = new JButton("New button");
		btnOpenRrnaFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtRrna.setText(guiFileOpen.openFileName("fastqFile", ""));
			}
		});
		btnOpenRrnaFile.setBounds(377, 424, 102, 28);
		add(btnOpenRrnaFile);
		
		txtSaveTo = new JTextField();
		txtSaveTo.setBounds(505, 335, 335, 22);
		add(txtSaveTo);
		txtSaveTo.setColumns(10);
		
		JButton btnSaveTo = new JButton("New button");
		btnSaveTo.setBounds(738, 371, 102, 28);
		add(btnSaveTo);
		
		JButton btnRun = new JButton("New button");
		btnRun.setBounds(738, 424, 102, 28);
		add(btnRun);
		
		comboBox = new JComboBoxData<>();
		comboBox.setMapItem(Species.getSpeciesName2Species(EnumSpeciesType.Genome));
		comboBox.setBounds(39, 333, 198, 27);
		add(comboBox);
		
		btnOpenfileLeft = new JButton("OpenFile");
		btnOpenfileLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> lsFileLeft = guiFileOpen.openLsFileName("fastqFile", "");
				for (String string : lsFileLeft) {
					String filePrefix = FileOperate.getFileNameSep(string)[0].split("_")[0];
					sclLeft.addItem(new String[]{string, filePrefix});
				}
			}
		});
		btnOpenfileLeft.setBounds(290, 245, 102, 28);
		add(btnOpenfileLeft);
		
		btnOpenfileRight = new JButton("OpenFile");
		btnOpenfileRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> lsFileRigth = guiFileOpen.openLsFileName("fastqFile", "");
				for (String string : lsFileRigth) {
					sclLeft.addItem(new String[]{string});
				}
			}
		});
		btnOpenfileRight.setBounds(738, 245, 102, 28);
		add(btnOpenfileRight);

	}
}
