package com.novelbio.nbcgui.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import com.novelbio.analysis.seq.rnaseq.lnc.LncInfo;
import com.novelbio.analysis.seq.rnaseq.lnc.LncSiteInfo;
import com.novelbio.base.dataOperate.ExcelTxtRead;
import com.novelbio.base.dataOperate.TxtReadandWrite;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.gui.GUIFileOpen;
import com.novelbio.base.gui.JScrollPaneData;

public class GuilncLocation extends JPanel {
	JScrollPaneData scrollPane;
	GuiLayeredPaneSpeciesVersionGff guiLayeredPaneSpeciesVersionGff;
	JSpinner spinner;
	
	GUIFileOpen guiFileOpen = new GUIFileOpen();
	
	LncSiteInfo lncSiteInfo = new LncSiteInfo();
	JRadioButton rdbtnByloc;
	JRadioButton rdbtnByname;
	ButtonGroup btnGroup = new ButtonGroup();
	JSpinner spinStart;
	JSpinner spinEnd;
	JLabel lblLncidcol;
	
	JLabel lblStartcol;
	JLabel lblEndcol;
	private JLabel lblDistancetoupdowngene;
	private JTextField txtUpDownDistance;
	
	/**
	 * Create the panel.
	 */
	public GuilncLocation() {
		setLayout(null);
		
		scrollPane = new JScrollPaneData();
		scrollPane.setBounds(12, 51, 572, 385);
		add(scrollPane);
		
		JButton btnAddfile = new JButton("addFile");
		btnAddfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<String> lsFile = guiFileOpen.openLsFileName("", "");
				List<String[]> lsFile2Result = new ArrayList<String[]>();
				for (String fileName : lsFile) {
					String[] tmpResult = new String[2];
					tmpResult[0] = fileName;
					tmpResult[1] = FileOperate.changeFileSuffix(fileName, "_lncLocation", null);
					lsFile2Result.add(tmpResult);
				}
				scrollPane.addItemLs(lsFile2Result);
			}
		});
		btnAddfile.setBounds(596, 51, 107, 25);
		add(btnAddfile);
		
		JButton btnDelfile = new JButton("delFile");
		btnDelfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scrollPane.deleteSelRows();
			}
		});
		btnDelfile.setBounds(596, 88, 107, 25);
		add(btnDelfile);
		
		guiLayeredPaneSpeciesVersionGff = new GuiLayeredPaneSpeciesVersionGff();
		guiLayeredPaneSpeciesVersionGff.setBounds(596, 125, 214, 137);
		add(guiLayeredPaneSpeciesVersionGff);
		
		JButton btnRun = new JButton("Run");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lncSiteInfo.setSpecies(guiLayeredPaneSpeciesVersionGff.getSelectSpecies());
				int colNum = (Integer)spinner.getValue() - 1;
				int colStart = 0, colEnd = 0;
				if (rdbtnByloc.isSelected()) {
					colStart = (Integer)spinStart.getValue() - 1;
					colEnd = (Integer)spinEnd.getValue() - 1;
				}
				try {
					lncSiteInfo.setUpDownExtend(Integer.parseInt(txtUpDownDistance.getText()));
				} catch (Exception e2) {
					// TODO: handle exception
				}
				
				for (String[] file2Result : scrollPane.getLsDataInfo()) {
					List<List<String>> lsInfo = ExcelTxtRead.readLsExcelTxtls(file2Result[0], 1);
					List<LncInfo> lsTmpResult = null;
					if (rdbtnByloc.isSelected()) {
						lncSiteInfo.setLsLncAligns(lsInfo, colNum, colStart, colEnd);
						lsTmpResult = lncSiteInfo.findLncInfoByLoc();
					} else {
						lncSiteInfo.setLsLncName(lsInfo, colNum);
						lsTmpResult = lncSiteInfo.findLncInfoByName();
					}

					TxtReadandWrite txtWrite = new TxtReadandWrite(file2Result[1], true);
					txtWrite.writefileln(LncInfo.getTitle());
					for (LncInfo lncInfo : lsTmpResult) {
						txtWrite.writefileln(lncInfo.toString());
					}
					txtWrite.close();
				}
			}
		});
		btnRun.setBounds(596, 411, 107, 25);
		add(btnRun);
		
		spinner = new JSpinner();
		spinner.setBounds(596, 379, 67, 20);
		spinner.setValue(1);
		add(spinner);
		
		lblLncidcol = new JLabel("LncIDCol");
		lblLncidcol.setBounds(596, 358, 92, 15);
		add(lblLncidcol);
		
		rdbtnByname = new JRadioButton("ByName");
		rdbtnByname.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setRdbtn(true);
			}
		});
		rdbtnByname.setSelected(true);
		rdbtnByname.setBounds(594, 271, 93, 26);
		add(rdbtnByname);
		
		rdbtnByloc = new JRadioButton("ByLoc");
		rdbtnByloc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setRdbtn(false);
			}
		});
		rdbtnByloc.setBounds(701, 271, 81, 26);
		add(rdbtnByloc);
		
		spinStart = new JSpinner();
		spinStart.setBounds(707, 377, 59, 22);
		add(spinStart);
		
		lblStartcol = new JLabel("StartCol");
		lblStartcol.setBounds(707, 356, 59, 18);
		add(lblStartcol);
		
		spinEnd = new JSpinner();
		spinEnd.setBounds(778, 377, 52, 22);
		add(spinEnd);
		
		lblEndcol = new JLabel("EndCol");
		lblEndcol.setBounds(778, 356, 59, 18);
		add(lblEndcol);
		
		lblDistancetoupdowngene = new JLabel("DistanceToUpDownGene");
		lblDistancetoupdowngene.setBounds(602, 305, 208, 18);
		add(lblDistancetoupdowngene);
		
		txtUpDownDistance = new JTextField();
		txtUpDownDistance.setText("10000");
		txtUpDownDistance.setBounds(599, 324, 67, 22);
		add(txtUpDownDistance);
		txtUpDownDistance.setColumns(10);
		
		initial();
	}
	
	private void initial() {
		scrollPane.setTitle(new String[]{"FileName", "ResultFileName"});
		btnGroup.add(rdbtnByloc); btnGroup.add(rdbtnByname);
		setRdbtn(true);
	}
	
	private void setRdbtn(boolean isSelectByName) {
		if (isSelectByName) {
			lblStartcol.setVisible(false);
			lblEndcol.setVisible(false);
			spinStart.setVisible(false);
			spinEnd.setVisible(false);
			lblLncidcol.setText("LncIDCol");
		} else {
			lblStartcol.setVisible(true);
			lblEndcol.setVisible(true);
			spinStart.setVisible(true);
			spinEnd.setVisible(true);
			lblLncidcol.setText("ChrIDCol");
		}
	}
}
