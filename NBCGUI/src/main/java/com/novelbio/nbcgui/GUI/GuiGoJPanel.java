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
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.novelbio.analysis.annotation.functiontest.FunctionTest;
import com.novelbio.analysis.annotation.functiontest.StatisticTestResult;
import com.novelbio.analysis.annotation.functiontest.TopGO.GoAlgorithm;
import com.novelbio.base.dataOperate.ExcelTxtRead;
import com.novelbio.base.gui.GUIFileOpen;
import com.novelbio.base.gui.JComboBoxData;
import com.novelbio.base.gui.JScrollPaneData;
import com.novelbio.base.gui.JTextFieldData;
import com.novelbio.database.domain.geneanno.GOtype;
import com.novelbio.database.model.species.Species;
import com.novelbio.database.service.SpringFactory;
import com.novelbio.nbcgui.controltest.CtrlGO;
import com.novelbio.nbcgui.controltest.CtrlTestGOInt;


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
public class GuiGoJPanel extends JPanel{
	private static final long serialVersionUID = 6495245480771910297L;
	
	private JTabbedPane jTabbedPaneGoResult;
	private JButton jBtbSaveGo;
	private JButton jButRunGo;
	private JProgressBar jProgressBarGo;
	private JLabel jLabResultReviewGo;
	private JLabel jLabInputReviewGo;
	private JLabel jLabGoType;
	private JLabel jLabAlgorithm;
	private JTextFieldData jTxtDownValueGo;
	private JLabel jLabDownValueGo;
	private JTextFieldData jTxtUpValueGo;
	private JLabel jLabUpValueGo;
	private JButton jBtnBGFileGo;
	private JTextFieldData jTxtBGGo;
	private JLabel jLabBGGo;
	private JLabel jLabPathGo;
	private JTextFieldData jTxtFilePathGo;
	private JButton jBtnFileOpenGo;
	private JTextFieldData jTxtValColGo;
	private JLabel jLabValueColGo;
	private JLabel jLabAccColGo;
	private JTextFieldData jTxtAccColGo;

	private JCheckBox jChkCluster;
	private JLabel jLabGoQtaxID;
	private JScrollPane jScrollPaneInputGo;
	
	JSpinner spnGOlevel;
	
	JScrollPaneData sclBlast;
	
	JComboBoxData<GoAlgorithm> cmbGoAlgorithm;
	JComboBoxData<GOtype> cmbGOType;
	JComboBoxData<Species> cmbSelSpeGo;
	
	JCheckBox chkGOLevel;
	
	String GoClass = "";
	
	GUIFileOpen guiFileOpen = new GUIFileOpen();
	CtrlTestGOInt ctrlGO;
	public GuiGoJPanel() {
	

		this.setPreferredSize(new java.awt.Dimension(1046, 644));
		setAlignmentX(0.0f);
		setComponent();
		setLayout(null);
		add(jBtbSaveGo);
		add(jLabDownValueGo);
		add(jLabAccColGo);
		add(jLabBGGo);
		add(jChkCluster);
		add(jTxtBGGo);
		add(jLabGoQtaxID);
		add(cmbSelSpeGo);
		add(jLabGoType);
		add(jLabValueColGo);
		add(jTxtDownValueGo);
		add(jTxtUpValueGo);
		add(jTxtValColGo);
		add(jTxtAccColGo);
		add(jBtnFileOpenGo);
		add(jBtnBGFileGo);
		add(jButRunGo);
		add(jLabPathGo);
		add(jLabUpValueGo);
		add(jLabAlgorithm);
		add(jTxtFilePathGo);
		add(jTabbedPaneGoResult);
		add(jScrollPaneInputGo);
		add(jLabInputReviewGo);
		add(jLabResultReviewGo);
		add(jProgressBarGo);
		
		cmbGoAlgorithm = new JComboBoxData<GoAlgorithm>();
		cmbGoAlgorithm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cmbGoAlgorithm.getSelectedValue() == GoAlgorithm.novelgo) {
					chkGOLevel.setEnabled(true);
					if (chkGOLevel.isSelected()) {
						spnGOlevel.setEnabled(true);
					} else {
						spnGOlevel.setEnabled(false);
					}
				} else {
					chkGOLevel.setEnabled(false);
					spnGOlevel.setEnabled(false);
				}
			}
		});
		cmbGoAlgorithm.setBounds(12, 312, 152, 23);
		cmbGoAlgorithm.setMapItem(GoAlgorithm.getMapStr2GoAlgrithm());
		add(cmbGoAlgorithm);
		
		cmbGOType = new JComboBoxData<GOtype>();
		cmbGOType.setBounds(12, 391, 206, 23);
		cmbGOType.setMapItem(GOtype.getMapStrAllGotype(false));
		add(cmbGOType);
		
		spnGOlevel = new JSpinner();
		spnGOlevel.setBounds(104, 347, 60, 18);
		add(spnGOlevel);
		
		chkGOLevel = new JCheckBox("GOLevel");
		chkGOLevel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (chkGOLevel.isSelected()) {
					spnGOlevel.setEnabled(true);
				} else {
					spnGOlevel.setEnabled(false);
				}
			}
		});
		chkGOLevel.setBounds(12, 343, 92, 22);
		add(chkGOLevel);
		
		sclBlast = new JScrollPaneData();
		JComboBoxData<Species> cmbSpeciesBlast = new JComboBoxData<Species>();
		cmbSpeciesBlast.setMapItem(Species.getSpeciesName2Species(Species.KEGGNAME_SPECIES));
		sclBlast.setTitle(new String[]{"BlastSpecies"});
		sclBlast.setItem(0, cmbSpeciesBlast);
		sclBlast.setBounds(12, 426, 215, 118);
		add(sclBlast);
		
		JButton btnAddBlast = new JButton("Add");
		btnAddBlast.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sclBlast.addItem(new String[]{""});
			}
		});
		btnAddBlast.setBounds(236, 426, 68, 24);
		add(btnAddBlast);
		
		JButton btnDelBlast = new JButton("Del");
		btnDelBlast.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sclBlast.deleteSelRows();
			}
		});
		btnDelBlast.setBounds(237, 519, 67, 24);
		add(btnDelBlast);
		
		initial();
	}
	private void setComponent() {
		{
			jLabGoQtaxID = new JLabel();
			jLabGoQtaxID.setBounds(12, 79, 111, 18);
			jLabGoQtaxID.setText("Query Species");
		}
		{
			jTxtFilePathGo = new JTextFieldData();
			jTxtFilePathGo.setBounds(97, 12, 206, 18);
			jTxtFilePathGo.addKeyListener(new KeyAdapter() {
				public void keyTyped(KeyEvent evt) {
					if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
						try {
							setGoProview(jTxtFilePathGo.getText());
						} catch (Exception e) {
							System.out.println("mei you wen jian");
						}
					}
				}
			});
		}
		{
			jBtnFileOpenGo = new JButton();
			jBtnFileOpenGo.setBounds(200, 36, 103, 22);
			jBtnFileOpenGo.setText("LoadData");
			jBtnFileOpenGo.setMargin(new java.awt.Insets(1, 1, 1, 1));
			jBtnFileOpenGo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					String filename = guiFileOpen.openFileName("txt/excel2003", "txt","xls");
					jTxtFilePathGo.setText(filename);
					try {
						setGoProview(jTxtFilePathGo.getText());
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("mei you wen jian");
					}
				}
			});
		}
		{
			jLabPathGo = new JLabel();
			jLabPathGo.setBounds(12, 12, 85, 18);
			jLabPathGo.setText("InputData");
		}
		{
			jTxtAccColGo = new JTextFieldData();
			jTxtAccColGo.setBounds(117, 172, 43, 20);
			jTxtAccColGo.setNumOnly();
		}
		{
			jLabAccColGo = new JLabel();
			jLabAccColGo.setBounds(12, 175, 92, 14);
			jLabAccColGo.setText("AccIDColNum");
			jLabAccColGo.setAlignmentY(0.0f);
		}
		{
			jLabValueColGo = new JLabel();
			jLabValueColGo.setBounds(12, 226, 93, 14);
			jLabValueColGo.setText("ValueColNum");
			jLabValueColGo.setAlignmentY(0.0f);
		}
		{
			jLabGoType = new JLabel();
			jLabGoType.setBounds(12, 377, 136, 13);
			jLabGoType.setText("GO Type");
		}
		{
			jLabUpValueGo = new JLabel();
			jLabUpValueGo.setBounds(12, 263, 72, 17);
			jLabUpValueGo.setText("UpValue");
		}
		{
			jTxtUpValueGo = new JTextFieldData();
			jTxtUpValueGo.setBounds(70, 264, 69, 22);
			jTxtUpValueGo.setNumOnly(10,4);
		}
		{
			jLabDownValueGo = new JLabel();
			jLabDownValueGo.setBounds(145, 264, 87, 11);
			jLabDownValueGo.setText("DownValue");
		}
		{
			jTxtDownValueGo = new JTextFieldData();
			jTxtDownValueGo.setBounds(237, 264, 67, 23);
			jTxtDownValueGo.setNumOnly(10,4);
		}
		{
			jBtnBGFileGo = new JButton();
			jBtnBGFileGo.setBounds(207, 143, 97, 23);
			jBtnBGFileGo.setText("BackGround");
			jBtnBGFileGo.setMargin(new java.awt.Insets(1, 0, 1, 0));
			jBtnBGFileGo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					String filename = guiFileOpen.openFileName("txt/excel2003", "txt","xls");
					jTxtBGGo.setText(filename);
				}
			});
		}
		{
			jTxtValColGo = new JTextFieldData();
			jTxtValColGo.setBounds(117, 224, 44, 19);
			jTxtValColGo.setNumOnly();
		}

		{
			jScrollPaneInputGo = new JScrollPane();
			jScrollPaneInputGo.setBounds(316, 36, 723, 205);
		}
		{
			jLabInputReviewGo = new JLabel();
			jLabInputReviewGo.setBounds(332, 14, 97, 14);
			jLabInputReviewGo.setText("InputReview");
		}
		{
			jLabResultReviewGo = new JLabel();
			jLabResultReviewGo.setBounds(332, 249, 127, 14);
			jLabResultReviewGo.setText("ResultReview");
		}
		{
			jProgressBarGo = new JProgressBar();
			jProgressBarGo.setBounds(12, 617, 1027, 14);
		}
		{
			jChkCluster = new JCheckBox();
			jChkCluster.setBounds(12, 200, 127, 22);
			jChkCluster.setText("ClusterGO");
			jChkCluster.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					if (jChkCluster.isSelected()) {
						jTxtDownValueGo.setEnabled(false);
						jTxtUpValueGo.setEnabled(false);
					}
					else {
						jTxtDownValueGo.setEnabled(true);
						jTxtUpValueGo.setEnabled(true);
					}
				}
			});
		}
		{
			jTabbedPaneGoResult = new JTabbedPane();
			jTabbedPaneGoResult.setBounds(316, 263, 723, 331);
		}
		{
			jBtbSaveGo = new JButton();
			jBtbSaveGo.setBounds(12, 556, 92, 23);
			jBtbSaveGo.setText("Save As");
			jBtbSaveGo.setMargin(new java.awt.Insets(1, 0, 1, 0));
			jBtbSaveGo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					String savefilename = guiFileOpen.saveFileNameAndPath("excel2007", "");
					if (ctrlGO != null) {
						ctrlGO.saveExcel(savefilename);
					}
				}
			});
		}
		{
			jButRunGo = new JButton();
			jButRunGo.setBounds(223, 555, 80, 24);
			jButRunGo.setText("Analysis");
			jButRunGo.setMargin(new java.awt.Insets(1, 1, 1, 1));
			jButRunGo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					run();
				}
			});
		}
		{
			jLabAlgorithm = new JLabel();
			jLabAlgorithm.setBounds(12, 298, 78, 15);
			jLabAlgorithm.setText("Algorithm");
		}
		{
			jLabBGGo = new JLabel();
			jLabBGGo.setBounds(12, 121, 92, 14);
			jLabBGGo.setText("BackGround");
			jLabBGGo.setAlignmentY(0.0f);
			jLabBGGo.setAutoscrolls(true);
		}
		{
			jTxtBGGo = new JTextFieldData();
			jTxtBGGo.setBounds(104, 119, 200, 18);
		}
		{
			cmbSelSpeGo = new JComboBoxData<Species>();
			cmbSelSpeGo.setBounds(131, 77, 173, 23);
			cmbSelSpeGo.setMapItem(Species.getSpeciesName2Species(Species.KEGGNAME_SPECIES));
			cmbSelSpeGo.setEditable(true);
		}
	}
	
	private void initial() {
		if (cmbGoAlgorithm.getSelectedValue() == GoAlgorithm.novelgo) {
			chkGOLevel.setEnabled(true);
			chkGOLevel.setSelected(false);
		} else {
			chkGOLevel.setEnabled(false);
			spnGOlevel.setEnabled(false);
		}
		spnGOlevel.setValue(2);
	}
	
	/**
	 * 查看文件的鼠标或键盘事件响应时调用
	 */
	private void setGoProview(String filePath) {
		ArrayList<String[]> lsInfo = ExcelTxtRead.readLsExcelTxt(filePath, 1);
		String[][] tableValue = null;
		DefaultTableModel jTabInputGo = new DefaultTableModel(tableValue, lsInfo.get(0));
		JTable jTabFInputGo = new JTable();
		jScrollPaneInputGo.setViewportView(jTabFInputGo);
		jTabFInputGo.setModel(jTabInputGo);
		for (int i = 1; i < lsInfo.size(); i++) {
			jTabInputGo.addRow(lsInfo.get(i));
		}
	}
	/**
	 * analysis按下去后得到结果
	 */
	private void run() {
		String geneFileXls = jTxtFilePathGo.getText();
		int colAccID = Integer.parseInt(jTxtAccColGo.getText());
		int colFC = Integer.parseInt(jTxtValColGo.getText());
		int taxID = -1;
		Species species = cmbSelSpeGo.getSelectedValue();
		if (species == null) {
			try {
				taxID = Integer.parseInt(cmbSelSpeGo.getEditor().getItem().toString());
			} catch (Exception e) { }
		} else {
			taxID = species.getTaxID();
		}
		
		ArrayList<String[]> lsAccID = null;
		if (colAccID != colFC) {
			lsAccID = ExcelTxtRead.readLsExcelTxt(geneFileXls, new int[]{colAccID, colFC}, 1, 0);
		} else {
			lsAccID = ExcelTxtRead.readLsExcelTxt(geneFileXls, new int[]{colAccID}, 1, 0);
		}
		
		String backGroundFile = jTxtBGGo.getText();
		double evalue = 1e-10;
		ctrlGO = (CtrlTestGOInt)SpringFactory.getFactory().getBean("ctrlGOall");
		ctrlGO.clearParam();
		ctrlGO.setGoAlgorithm(cmbGoAlgorithm.getSelectedValue());
//		ctrlGO.setGoAlgorithm(GoAlgorithm.novelgo);

		ctrlGO.setTaxID(taxID);
		List<Integer> lsStaxID = new ArrayList<Integer>();
		Map<String, Species> mapComName2Species = Species.getSpeciesName2Species(Species.ALL_SPECIES);
		for (String[] strings : sclBlast.getLsDataInfo()) {
			Species speciesS = mapComName2Species.get(strings[0]);
			if (speciesS == null) {
				continue;
			} else {
				lsStaxID.add(speciesS.getTaxID());
			}
		}
		ctrlGO.setBlastInfo(evalue, lsStaxID);
				
		if (chkGOLevel.isSelected()) {
			ctrlGO.setGOlevel((Integer) spnGOlevel.getValue());
		} else {
			ctrlGO.setGOlevel(-1);
		}
		
		if (!jChkCluster.isSelected() || colAccID == colFC) {
			double up = 0; double down = 0;
			if ( colAccID != colFC) {
				up = Double.parseDouble(jTxtUpValueGo.getText());
				down = Double.parseDouble(jTxtDownValueGo.getText());
			}
			ctrlGO.setUpDown(up, down);
			ctrlGO.setIsCluster(false);
		} else {
			ctrlGO.setIsCluster(jChkCluster.isSelected());
		}
		ctrlGO.setLsBG(backGroundFile);
		ctrlGO.setLsAccID2Value(lsAccID);
		ctrlGO.run();
		setNormalGo(ctrlGO);
	}
	
	private void setNormalGo(CtrlTestGOInt ctrlGO) {
		//jScrollPaneInputGo 最外层的方框
		//jTabbedPaneGOTest 里面的标签框
		//jPanGoTest 具体的标签
		// jScrollPaneGOtest 标签里面的方框
		// jTabFInputGo 方框里面的数据框
		// jTabInputGo 具体数据
		Map<GOtype, CtrlGO> hashResult = ctrlGO.getMapResult_Prefix2FunTest();
		jTabbedPaneGoResult.removeAll();
		for (CtrlGO ctrlGOResult : hashResult.values()) {
			for (Entry<String, FunctionTest> entry : ctrlGOResult.getMapResult_Prefix2FunTest().entrySet()) {
				settab(jTabbedPaneGoResult, entry.getKey() + ctrlGOResult.getGOClass().toString(), entry.getValue().getTestResult());
			}
		}
	}
	
	private void settab(JTabbedPane jTabbedPaneGoResult, String tabName , List<StatisticTestResult> lsResultTest) {
		//里层
		String[][] tableValue = null;
		List<String[]> lsStatisticTestResults = StatisticTestResult.getLsInfo(true, lsResultTest);
		
		DefaultTableModel jTabResult = new DefaultTableModel(tableValue,lsStatisticTestResults.get(0));
		//中层
		JTable jTabFResult = new JTable();
		jTabFResult.setModel(jTabResult);
		//外层
		JScrollPane jScrollPanelResult = new JScrollPane();
		jScrollPanelResult.setPreferredSize(new java.awt.Dimension(566, 305));
		jScrollPanelResult.setViewportView(jTabFResult);
		//最外层
		jTabbedPaneGoResult.addTab(tabName, null, jScrollPanelResult, null);
		for (int i = 1; i < lsStatisticTestResults.size(); i++) {
			jTabResult.addRow(lsStatisticTestResults.get(i));
		}
	}
}
