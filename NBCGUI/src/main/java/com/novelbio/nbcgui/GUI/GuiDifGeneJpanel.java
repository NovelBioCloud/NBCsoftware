package com.novelbio.nbcgui.GUI;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.novelbio.analysis.diffexpress.DiffExpAbs;
import com.novelbio.analysis.diffexpress.EnumDifGene;
import com.novelbio.base.dataOperate.ExcelTxtRead;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.gui.GUIFileOpen;
import com.novelbio.base.gui.JComboBoxData;
import com.novelbio.base.gui.JScrollPaneData;
import com.novelbio.nbcgui.controltest.CtrlDifGene;
import javax.swing.JCheckBox;

public class GuiDifGeneJpanel extends JPanel {
	JScrollPaneData scrollPaneNormData;
	JScrollPaneData scrollPaneSample;
	JScrollPaneData scrollPaneDesign;
	GUIFileOpen guiFileOpen = new GUIFileOpen();
	ArrayList<String[]> lsGeneInfo;
	CtrlDifGene diffExpAbs;
	JComboBoxData<EnumDifGene> cmbMethod;
	JCheckBox chckSensitive;
	
	JComboBoxData<String> cmbGroup = new JComboBoxData<String>();
	
	private JTextField txtSave;
	private JTextField txtColAccID;
	/**
	 * Create the panel.
	 */
	public GuiDifGeneJpanel() {
		setLayout(null);
		
		cmbGroup.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				changeSclCompareGroup();
			}
		});
		
		scrollPaneNormData = new JScrollPaneData();
		scrollPaneNormData.setBounds(12, 24, 611, 528);
		add(scrollPaneNormData);
		
		scrollPaneSample = new JScrollPaneData();
		scrollPaneSample.setBounds(635, 24, 346, 195);
		add(scrollPaneSample);
		
		JButton btnSetSample = new JButton("SetSample");
		btnSetSample.setBounds(634, 231, 83, 20);
		btnSetSample.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scrollPaneSample.addItem(new String[]{"", ""});
			}
		});
		btnSetSample.setMargin(new Insets(0, 0, 0, 0));
		add(btnSetSample);
		
		JButton btnDelSample = new JButton("Delete");
		btnDelSample.setBounds(906, 231, 75, 20);
		btnDelSample.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scrollPaneSample.deleteSelRows();
			}
		});
		btnDelSample.setMargin(new Insets(0, 0, 0, 0));
		add(btnDelSample);
		
		scrollPaneDesign = new JScrollPaneData();
		scrollPaneDesign.setBounds(635, 268, 346, 150);
		add(scrollPaneDesign);
		
		JButton btnSetDesign = new JButton("addDesign");
		btnSetDesign.setBounds(635, 431, 82, 20);
		btnSetDesign.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scrollPaneDesign.addItem(new String[]{"", ""});
			}
		});
		btnSetDesign.setMargin(new Insets(0, 0, 0, 0));
		add(btnSetDesign);
		
		JButton btnDelDesign = new JButton("delete");
		btnDelDesign.setBounds(894, 430, 87, 20);
		btnDelDesign.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scrollPaneDesign.deleteSelRows();
			}
		});
		btnDelDesign.setMargin(new Insets(0, 0, 0, 0));
		btnSetDesign.setMargin(new Insets(0, 0, 0, 0));
		add(btnDelDesign);
		
		JButton btnSave = new JButton("Save");
		btnSave.setBounds(924, 532, 57, 20);
		btnSave.setMargin(new Insets(0, 0, 0, 0));
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String fileName = guiFileOpen.saveFileNameAndPath("SavePathAndPrefix", "");
				txtSave.setText(fileName);
			}
		});
		add(btnSave);
		
		cmbMethod = new JComboBoxData<>();
		cmbMethod.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectMethod();
			}
		});
		cmbMethod.setBounds(635, 463, 271, 23);
		add(cmbMethod);
		
		final JCheckBox chckbxIslogvalueOnlyforlimma = new JCheckBox("To Log The Value, OnlyForLimma");
		chckbxIslogvalueOnlyforlimma.setBounds(218, 552, 258, 26);
		add(chckbxIslogvalueOnlyforlimma);
		
		txtSave = new JTextField();
		txtSave.setBounds(635, 534, 277, 18);
		add(txtSave);
		txtSave.setColumns(10);
		
		JButton btnRun = new JButton("Run");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<String[]> lsCol2Sample = scrollPaneSample.getLsDataInfo();
				diffExpAbs = new CtrlDifGene(cmbMethod.getSelectedValue());
				if (!txtColAccID.getText().trim().equals("")) {
					diffExpAbs.setColID(Integer.parseInt(txtColAccID.getText()));
				}
				else {
					diffExpAbs.setColID(1);
				}
				diffExpAbs.setGeneInfo(lsGeneInfo);
				diffExpAbs.setCol2Sample(lsCol2Sample);
				diffExpAbs.setLogTheValue(chckbxIslogvalueOnlyforlimma.isSelected());
				diffExpAbs.setSensitive(chckSensitive.isSelected());
				String pathPrefix = getPathPrefix();
				if (pathPrefix == null) {
					return;
				}
				
				ArrayList<String[]> lsFileName = scrollPaneDesign.getLsDataInfo();
				for (String[] strings : lsFileName) {
					String fileName = strings[2];
					String[] pair = new String[]{strings[0], strings[1]};
					fileName = pathPrefix + fileName;
					if (!fileName.endsWith("txt") && !fileName.endsWith("xls") && !fileName.endsWith("xlsx")) {
						fileName = FileOperate.changeFileSuffix(fileName, "", "xls");
					}
					diffExpAbs.addFileName2Compare(fileName, pair);
				}
				diffExpAbs.calculateResult();
				diffExpAbs.getResultFileName();
				JOptionPane.showMessageDialog(null, "Your Diff Expressed Gene Is Already Finding", "Info",JOptionPane.INFORMATION_MESSAGE);
				diffExpAbs.clean();
			}
		});
		btnRun.setBounds(863, 563, 118, 24);
		add(btnRun);
		
		JButton btnOpenfile = new JButton("OpenFile");
		btnOpenfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String geneFile = guiFileOpen.openFileName("txt/excel", "");
				lsGeneInfo = ExcelTxtRead.readLsExcelTxt(geneFile, 1);
				scrollPaneNormData.setItemLsWithTitle(lsGeneInfo);
			}
		});
		btnOpenfile.setBounds(506, 563, 118, 24);
		add(btnOpenfile);
		
		txtColAccID = new JTextField();
		txtColAccID.setBounds(80, 556, 114, 18);
		add(txtColAccID);
		txtColAccID.setColumns(10);
		
		JLabel lblColaccid = new JLabel("ColAccID");
		lblColaccid.setBounds(12, 558, 69, 14);
		add(lblColaccid);
		
		chckSensitive = new JCheckBox("Sensitive");
		chckSensitive.setBounds(631, 494, 275, 26);
		add(chckSensitive);
		initial();
	}
	
	public void initial() {
		cmbMethod.setMapItem(DiffExpAbs.getMapMethod2ID());
		scrollPaneSample.setTitle(new String[]{"SampleColumn","GroupName"});
		scrollPaneDesign.setTitle(new String[]{"group1","group2","FileName"});
		scrollPaneDesign.setItem(0, cmbGroup);
		scrollPaneDesign.setItem(1, cmbGroup);
		selectMethod();
	}
	
	private String getPathPrefix() {
		String pathPrefix = txtSave.getText().trim();
		if (pathPrefix.equals("")) {
			JOptionPane.showMessageDialog(null, "no save path", "error",JOptionPane.ERROR_MESSAGE);
			return null;
		}
		if (FileOperate.isFileDirectory(pathPrefix)) {
			return FileOperate.addSep(pathPrefix);
		}
		return pathPrefix;
	}
	
	private void selectMethod() {
		if (cmbMethod.getSelectedValue() == EnumDifGene.DESeq) {
			chckSensitive.setVisible(true);
			chckSensitive.setText("Sensitive For Replicate");
		} else if (cmbMethod.getSelectedValue() == EnumDifGene.EBSeq) {
			chckSensitive.setVisible(true);
			chckSensitive.setText("Sensitive");
		} else if (cmbMethod.getSelectedValue() == EnumDifGene.EdgeR) {
			chckSensitive.setVisible(true);
			chckSensitive.setText("Sensitive For NonReplicate");
		} else {
			chckSensitive.setVisible(false);
		}
	}
	
	private void changeSclCompareGroup() {
		ArrayList<String[]> lsSnp2Prefix = scrollPaneSample.getLsDataInfo();
		Map<String, String> mapString2Value = new LinkedHashMap<String, String>();
		for (String[] snp2prefix : lsSnp2Prefix) {
			mapString2Value.put(snp2prefix[1], snp2prefix[1]);
		}
		cmbGroup.setMapItem(mapString2Value);
	}
}
