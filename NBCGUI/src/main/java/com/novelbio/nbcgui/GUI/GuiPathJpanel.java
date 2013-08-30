package com.novelbio.nbcgui.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.novelbio.analysis.annotation.functiontest.FunctionTest;
import com.novelbio.base.dataOperate.ExcelTxtRead;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.gui.GUIFileOpen;
import com.novelbio.base.gui.JComboBoxData;
import com.novelbio.base.gui.JScrollPaneData;
import com.novelbio.base.gui.JTextFieldData;
import com.novelbio.database.model.species.Species;
import com.novelbio.database.service.SpringFactory;
import com.novelbio.nbcgui.controltest.CtrlTestPathInt;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class GuiPathJpanel extends JPanel{
	private static final long serialVersionUID = -1785031773572897544L;
	
	private JTabbedPane jTabbedPanePathResult;
	private JButton jBtbSavePath;
	private JButton jButRunPath;
	private JProgressBar jProgressBarPath;
	private JLabel jLabResultReviewPath;
	private JLabel jLabInputReviewPath;
	private JTextFieldData jTxtDownValuePath;
	private JLabel jLabDownValuePath;
	private JTextFieldData jTxtUpValuePath;
	private JLabel jLabUpValuePath;
	private JButton jBtnBGFilePath;
	private JTextFieldData jTxtBGPath;
	private JLabel jLabBGPath;
	private JLabel jLabPathPath;
	private JTextFieldData jTxtFilePathPath;
	private JButton jBtnFileOpenPath;
	private JTextFieldData jTxtValColPath;
	private JLabel jLabValueColPath;
	private JCheckBox jChkCluster;
	private JLabel jLabAccColPath;
	private JTextFieldData jTxtAccColPath;
	private JComboBoxData<Species> jCombSelSpePath;
	private JLabel jLabPathQtaxID;
	private JScrollPaneData jScrollPaneInputPath;
	
	CtrlTestPathInt ctrlPath;
	JScrollPaneData scrollPaneBlast;
	GUIFileOpen guiFileOpen = new GUIFileOpen();;
	
	public GuiPathJpanel() {
		setPreferredSize(new java.awt.Dimension(1046, 617));
		setAlignmentX(0.0f);
		setComponent();
		setLayout(null);
		add(jTxtFilePathPath);
		add(jLabValueColPath);
		add(jBtbSavePath);
		add(jLabDownValuePath);
		add(jLabUpValuePath);
		add(jLabAccColPath);
		add(jChkCluster);
		add(jLabPathPath);
		add(jTxtDownValuePath);
		add(jTxtUpValuePath);
		add(jLabPathQtaxID);
		add(jCombSelSpePath);
		add(jLabBGPath);
		add(jButRunPath);
		add(jTxtValColPath);
		add(jTxtAccColPath);
		add(jBtnFileOpenPath);
		add(jBtnBGFilePath);
		add(jTxtBGPath);
		add(jTabbedPanePathResult);
		add(jScrollPaneInputPath);
		add(jLabInputReviewPath);
		add(jLabResultReviewPath);
		add(jProgressBarPath);
		
		scrollPaneBlast = new JScrollPaneData();
		scrollPaneBlast.setBounds(12, 385, 174, 138);
		JComboBoxData<Species> cmbSpeciesBlast = new JComboBoxData<Species>();
		cmbSpeciesBlast.setMapItem(Species.getSpeciesName2Species(Species.KEGGNAME_SPECIES));
		scrollPaneBlast.setTitle(new String[]{"BlastSpecies"});
		scrollPaneBlast.setItem(0, cmbSpeciesBlast);
		add(scrollPaneBlast);
		
		JButton btnAddSpecies = new JButton("Add");
		btnAddSpecies.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scrollPaneBlast.addItem(new String[]{""});
			}
		});
		btnAddSpecies.setBounds(190, 387, 61, 24);
		add(btnAddSpecies);
		
		JButton btnDel = new JButton("Del");
		btnDel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scrollPaneBlast.deleteSelRows();
			}
		});
		btnDel.setBounds(190, 499, 61, 24);
		add(btnDel);
	}
	private void setComponent() {
		{
			jLabPathQtaxID = new JLabel();
			jLabPathQtaxID.setBounds(12, 88, 129, 18);
			jLabPathQtaxID.setText("Query Species");
		}
		{
			jTxtFilePathPath = new JTextFieldData();
			jTxtFilePathPath.setBounds(12, 36, 217, 18);
			jTxtFilePathPath.addKeyListener(new KeyAdapter() {
				public void keyTyped(KeyEvent evt) {
					if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
						try {
							setPathProview(jTxtFilePathPath.getText());
						} catch (Exception e) {
							System.out.println("mei you wen jian");
						}
						
					}
				}
			});
		}
		{
			jBtnFileOpenPath = new JButton();
			jBtnFileOpenPath.setBounds(122, 60, 105, 22);
			jBtnFileOpenPath.setText("LoadData");
			jBtnFileOpenPath.setMargin(new java.awt.Insets(1, 1, 1, 1));
			jBtnFileOpenPath.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					String filename = guiFileOpen.openFileName("txt/excel2003", "txt","xls");
					jTxtFilePathPath.setText(filename);
					try {
						setPathProview(jTxtFilePathPath.getText());
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("mei you wen jian");
					}
				}
			});
		}
		{
			jLabPathPath = new JLabel();
			jLabPathPath.setBounds(12, 12, 85, 18);
			jLabPathPath.setText("InputData");
		}
		{
			jTxtAccColPath = new JTextFieldData();
			jTxtAccColPath.setBounds(122, 231, 43, 20);
			jTxtAccColPath.setNumOnly();
		}
		{
			jLabAccColPath = new JLabel();
			jLabAccColPath.setBounds(12, 234, 92, 14);
			jLabAccColPath.setText("AccIDColNum");
			jLabAccColPath.setAlignmentY(0.0f);
		}
		{
			jLabValueColPath = new JLabel();
			jLabValueColPath.setBounds(11, 286, 93, 14);
			jLabValueColPath.setText("ValueColNum");
			jLabValueColPath.setAlignmentY(0.0f);
		}
		{
			jLabUpValuePath = new JLabel();
			jLabUpValuePath.setBounds(12, 312, 72, 17);
			jLabUpValuePath.setText("UpValue");
		}
		{
			jTxtUpValuePath = new JTextFieldData();
			jTxtUpValuePath.setBounds(102, 312, 69, 22);
			jTxtUpValuePath.setNumOnly(10,4);
		}
		{
			jLabDownValuePath = new JLabel();
			jLabDownValuePath.setBounds(12, 346, 87, 11);
			jLabDownValuePath.setText("DownValue");
		}
		{
			jTxtDownValuePath = new JTextFieldData();
			jTxtDownValuePath.setBounds(102, 340, 69, 22);
			jTxtDownValuePath.setNumOnly(10,4);

		}
		{
			jBtnBGFilePath = new JButton();
			jBtnBGFilePath.setBounds(130, 200, 97, 23);
			jBtnBGFilePath.setText("BackGround");
			jBtnBGFilePath.setMargin(new java.awt.Insets(1, 0, 1, 0));
			jBtnBGFilePath.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					String filename = guiFileOpen.openFileName("txt/excel2003", "txt","xls");
					jTxtBGPath.setText(filename);
				}
			});
		}
		{
			jTxtValColPath = new JTextFieldData();
			jTxtValColPath.setBounds(122, 283, 44, 20);
			jTxtValColPath.setNumOnly();
		}

		{
			jScrollPaneInputPath = new JScrollPaneData();
			jScrollPaneInputPath.setBounds(251, 36, 784, 192);
		}
		{
			jLabInputReviewPath = new JLabel();
			jLabInputReviewPath.setBounds(241, 14, 97, 14);
			jLabInputReviewPath.setText("InputReview");
		}
		{
			jLabResultReviewPath = new JLabel();
			jLabResultReviewPath.setBounds(248, 234, 127, 14);
			jLabResultReviewPath.setText("ResultReview");
		}
		{
			jProgressBarPath = new JProgressBar();
			jProgressBarPath.setBounds(12, 588, 1023, 14);
		}
		{
			jChkCluster = new JCheckBox();
			jChkCluster.setBounds(12, 256, 89, 22);
			jChkCluster.setText("ClusterPath");
			jChkCluster.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					if (jChkCluster.isSelected()) {
						jTxtDownValuePath.setEnabled(false);
						jTxtUpValuePath.setEnabled(false);
					}
					else {
						jTxtDownValuePath.setEnabled(true);
						jTxtUpValuePath.setEnabled(true);
					}
				}
			});
		}
		{
			jTabbedPanePathResult = new JTabbedPane();
			jTabbedPanePathResult.setBounds(258, 254, 777, 331);
		}
		{
			jBtbSavePath = new JButton();
			jBtbSavePath.setBounds(12, 550, 92, 23);
			jBtbSavePath.setText("Save As");
			jBtbSavePath.setMargin(new java.awt.Insets(1, 0, 1, 0));
			jBtbSavePath.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					String savefilename = guiFileOpen.saveFileNameAndPath("excel2003", "");
					if (ctrlPath != null) {
						ctrlPath.saveExcel(savefilename);
					}
				}
			});
		}
		{
			jButRunPath = new JButton();
			jButRunPath.setBounds(116, 549, 80, 24);
			jButRunPath.setText("Analysis");
			jButRunPath.setMargin(new java.awt.Insets(1, 1, 1, 1));
			jButRunPath.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					getResult();
				}
			});
		}
		{
			jLabBGPath = new JLabel();
			jLabBGPath.setBounds(12, 147, 110, 14);
			jLabBGPath.setText("BackGround");
			jLabBGPath.setAlignmentY(0.0f);
			jLabBGPath.setAutoscrolls(true);
		}
		{
			jTxtBGPath = new JTextFieldData();
			jTxtBGPath.setBounds(14, 170, 215, 18);
		}
		{
			jCombSelSpePath = new JComboBoxData<Species>();
			jCombSelSpePath.setBounds(12, 112, 174, 23);
			jCombSelSpePath.setEditable(true);
			jCombSelSpePath.setMapItem(Species.getSpeciesName2Species(Species.ALL_SPECIES));
		}
	}
	
	/**
	 * 查看文件的鼠标或键盘事件响应时调用
	 */
	private void setPathProview(String filePath) {
		ArrayList<String[]> lsInfo = ExcelTxtRead.readLsExcelTxt(filePath, 1);
		String[][] tableValue = null;
		DefaultTableModel jTabInputPath = new DefaultTableModel(tableValue,lsInfo.get(0));
		JTable jTabFInputPath = new JTable();
		jScrollPaneInputPath.setViewportView(jTabFInputPath);
		jTabFInputPath.setModel(jTabInputPath);
		for (int i = 1; i < lsInfo.size(); i++) {
			jTabInputPath.addRow(lsInfo.get(i));
		}
		
	}
	/**
	 * analysis按下去后得到结果
	 */
	private void getResult() {
		//jScrollPaneInputPath 最外层的方框
		//jTabbedPanePathTest 里面的标签框
		//jPanPathTest 具体的标签
		//jScrollPanePathtest 标签里面的方框
		//jTabFInputPath 方框里面的数据框
		//jTabInputPath 具体数据
		String geneFileXls = jTxtFilePathPath.getText();
		int colAccID = Integer.parseInt(jTxtAccColPath.getText());
		int colFC = Integer.parseInt(jTxtValColPath.getText());
		
		String backGroundFile = jTxtBGPath.getText();
		double evalue = 1e-10;
				
		ArrayList<String[]> lsAccID = null;
		if (colAccID != colFC) {
			 lsAccID = ExcelTxtRead.readLsExcelTxt(geneFileXls, new int[]{colAccID, colFC}, 1, 0);
		} else {
			lsAccID = ExcelTxtRead.readLsExcelTxt(geneFileXls, new int[]{colAccID}, 1, 0);
		}
		ctrlPath = (CtrlTestPathInt)SpringFactory.getFactory().getBean("ctrlPath");
		ctrlPath.clearParam();
		int taxID = -1;
		Species species = jCombSelSpePath.getSelectedValue();
		
		if (species == null) {
			try {
				taxID = Integer.parseInt(jCombSelSpePath.getEditor().getItem().toString());
			} catch (Exception e) { }
		} else {
			taxID = species.getTaxID();
		}
		ctrlPath.setTaxID(taxID);
		
		List<Integer> lsStaxID = new ArrayList<Integer>();
		Map<String, Species> mapComName2Species = Species.getSpeciesName2Species(Species.ALL_SPECIES);
		for (String[] strings : scrollPaneBlast.getLsDataInfo()) {
			Species speciesS = mapComName2Species.get(strings[0]);
			if (speciesS == null) {
				continue;
			} else {
				lsStaxID.add(speciesS.getTaxID());
			}
		}
//		ctrlPath = (CtrlTestInt)SpringFactory.getFactory().getBean("ctrlPath");
		ctrlPath.setBlastInfo(evalue, lsStaxID);

		ctrlPath.setLsBG(backGroundFile);
		if (!jChkCluster.isSelected() || colAccID == colFC) {
			double up = 0; double down = 0;
			if ( colAccID != colFC) {
				up = Double.parseDouble(jTxtUpValuePath.getText());
				down = Double.parseDouble(jTxtDownValuePath.getText());
			}
			ctrlPath.setUpDown(up, down);
			ctrlPath.setIsCluster(false);
		} else {
			ctrlPath.setIsCluster(jChkCluster.isSelected());
		}
		ctrlPath.setLsAccID2Value(lsAccID);
		ctrlPath.run();
		setNormalGo(ctrlPath);
	}
	
	private void setNormalGo(CtrlTestPathInt ctrlPath) {
		//jScrollPaneInputGo 最外层的方框
		//jTabbedPaneGOTest 里面的标签框
		//jPanGoTest 具体的标签
		// jScrollPaneGOtest 标签里面的方框
		// jTabFInputGo 方框里面的数据框
		// jTabInputGo 具体数据
		Map<String, FunctionTest> hashResult = ctrlPath.getMapResult_Prefix2FunTest();
		jTabbedPanePathResult.removeAll();
		int i = 0;
		for (Entry<String, FunctionTest> entry : hashResult.entrySet()) {
			if (i > 2) {
				break;
			}
			Map<String, List<String[]>> mapSheetName2LsInfo = entry.getValue().getMapWriteToExcel();
			for (String sheetName : mapSheetName2LsInfo.keySet()) {
				settab(jTabbedPanePathResult, entry.getKey() + sheetName, mapSheetName2LsInfo.get(sheetName));
			}
			i++;
		}
	}
	
	private void settab(JTabbedPane jTabbedPaneGoResult, String tabName , List<String[]> lsResult) {
		//里层
		String[][] tableValue = null;
		DefaultTableModel jTabResult = new DefaultTableModel(tableValue,lsResult.get(0));
		//中层
		JTable jTabFResult = new JTable();
		jTabFResult.setModel(jTabResult);
		//外层
		JScrollPane jScrollPanelResult = new JScrollPane();
		jScrollPanelResult.setViewportView(jTabFResult);
		//最外层
		jTabbedPaneGoResult.addTab(tabName, null, jScrollPanelResult, null);
		for (int i = 1; i < lsResult.size(); i++) {
			jTabResult.addRow(lsResult.get(i));
		}
	}
}
