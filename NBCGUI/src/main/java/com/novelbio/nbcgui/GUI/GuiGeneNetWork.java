package com.novelbio.nbcgui.GUI;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.gui.GUIFileOpen;
import com.novelbio.base.gui.JComboBoxData;
import com.novelbio.base.gui.JScrollPaneData;
import com.novelbio.database.model.species.Species;
import com.novelbio.generalConf.PathDetailNBC;

public class GuiGeneNetWork extends JPanel {
	private static final long serialVersionUID = -5762081843879739918L;
	private JTextField txtinFile;
	JScrollPaneData scrollPaneInfile;
	JComboBoxData<Integer> comboBox;
	GeneNetWork geneNetWork = new GeneNetWork();
	private JScrollPaneData JSpaneOutFile;
	GUIFileOpen guiFileOpen = new GUIFileOpen();
	GuiLayeredPanSpeciesVersion guiLayeredPanSpeciesVersion = new GuiLayeredPanSpeciesVersion();
	//TODO loading路径可能有变
//	String loadingFile =  MyPath.getRealPath() + "/GeneNetwork/interactions.txt" ;
	String loadingFile = PathDetailNBC.getNCBIinteract();
	
	/**
	 * Create the panel.
	 */
	
	
	public GuiGeneNetWork() {
		setLayout(null);
		JLabel lblNewLabel = new JLabel("GeneNetWork");
		lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 20));
		lblNewLabel.setBounds(399, 36, 195, 44);
		add(lblNewLabel);
		
		JButton btnInFile = new JButton("InFile");
		btnInFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String inFileString = guiFileOpen.openFileName("", "");
				txtinFile.setText(inFileString);
				geneNetWork.readGeneExcel(inFileString);	
				comboBox.setMapItem(geneNetWork.getTitle2colNum());
				scrollPaneInfile.setItemLsLs(geneNetWork.getLslsGene());
			}
		});
		btnInFile.setFont(new Font("Dialog", Font.BOLD, 15));
		btnInFile.setBounds(151, 92, 107, 25);
		add(btnInFile);
		
		txtinFile = new JTextField();
		txtinFile.setBounds(270, 92, 438, 25);
		add(txtinFile);
		txtinFile.setColumns(10);
		
		scrollPaneInfile = new JScrollPaneData();
		scrollPaneInfile.setBounds(151, 127, 744, 179);
		add(scrollPaneInfile);
		
		comboBox = new JComboBoxData<Integer>();
		
		comboBox.setBounds(477, 318, 165, 24);
		add(comboBox);
		
		JSpaneOutFile = new JScrollPaneData();
		JSpaneOutFile.setBounds(151, 407, 744, 203);
		add(JSpaneOutFile);
		
		JLabel lblSelectcol = new JLabel("selectCol");
		lblSelectcol.setFont(new Font("Dialog", Font.BOLD, 15));
		lblSelectcol.setBounds(377, 317, 90, 24);
		add(lblSelectcol);
		
		JButton btnSave = new JButton("Save AS");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String saveAsFile = guiFileOpen.saveFileNameAndPath("", "");
				geneNetWork.writeResult(geneNetWork.getLslsResult(), saveAsFile);
			}
		});
		btnSave.setBounds(151, 610, 107, 25);
		add(btnSave);
		
		JButton btnNewButton = new JButton("Start");
		btnNewButton.setFont(new Font("Dialog", Font.BOLD, 15));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (txtinFile.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Please InFile");
				}
				Species species =  guiLayeredPanSpeciesVersion.getSelectSpecies();
				int colNum = comboBox.getSelectedValue();
				geneNetWork.findGeneNetWork(loadingFile, colNum, species);
				JSpaneOutFile.setItemLsLs(geneNetWork.getLslsResult());
				String OutFile = FileOperate.changeFileSuffix(txtinFile.getText(), "_out", null);
				geneNetWork.writeResult(geneNetWork.getLslsResult(), OutFile);
			}
		});
		btnNewButton.setBounds(533, 370, 107, 25);
		add(btnNewButton);
		guiLayeredPanSpeciesVersion.setBounds(137, 307, 231, 100);
		add(guiLayeredPanSpeciesVersion);
		
		
	}
}
