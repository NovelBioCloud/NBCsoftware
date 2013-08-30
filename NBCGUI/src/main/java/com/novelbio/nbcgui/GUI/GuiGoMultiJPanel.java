package com.novelbio.nbcgui.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import com.novelbio.analysis.annotation.functiontest.TopGO.GoAlgorithm;
import com.novelbio.base.dataOperate.ExcelTxtRead;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.gui.GUIFileOpen;
import com.novelbio.base.gui.JComboBoxData;
import com.novelbio.base.gui.JScrollPaneData;
import com.novelbio.base.gui.JTextFieldData;
import com.novelbio.database.model.species.Species;
import com.novelbio.database.service.SpringFactory;
import com.novelbio.nbcgui.controltest.CtrlTestGOInt;
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
public class GuiGoMultiJPanel extends JPanel{
	private static final long serialVersionUID = 6495245480771910297L;
	private JButton jButRunGo;
	private JProgressBar jProgressBarGo;
	private JLabel jLabInputReviewGo;
	private JLabel jLabAlgorithm;
	private JTextFieldData jTxtDownValueGo;
	private JLabel jLabDownValueGo;
	private JTextFieldData jTxtUpValueGo;
	private JLabel jLabUpValueGo;
	private JButton jBtnBGFileGo;
	private JTextFieldData jTxtBGGo;
	private JLabel jLabBGGo;
	private JButton jBtnFileOpen;
	private JTextFieldData jTxtValColGo;
	private JLabel jLabValueColGo;
	private JLabel jLabAccColGo;
	private JTextFieldData jTxtAccColGo;

	private JCheckBox jChkCluster;
	private JLabel jLabGoQtaxID;
	private JScrollPaneData jScrollPaneInput;
	
	JSpinner spnGOlevel;
	
	JScrollPaneData sclBlast;
	
	JComboBoxData<GoAlgorithm> cmbGoAlgorithm;
	JComboBoxData<Species> cmbSpecies;
	
	JCheckBox chkGOLevel;
	JCheckBox chckbxPathAnalysis;
	
	String GoClass = "";
	
	GUIFileOpen guiFileOpen = new GUIFileOpen();
	
	JCheckBox chckbxGoAnalysis;
	private JTextField txtSaveToPathAndPrefix;
	
	
	public GuiGoMultiJPanel() {
	

		this.setPreferredSize(new java.awt.Dimension(1046, 644));
		setAlignmentX(0.0f);
		setComponent();
		setLayout(null);
		add(jLabDownValueGo);
		add(jLabAccColGo);
		add(jLabBGGo);
		add(jChkCluster);
		add(jTxtBGGo);
		add(jLabGoQtaxID);
		add(cmbSpecies);
		add(jLabValueColGo);
		add(jTxtDownValueGo);
		add(jTxtUpValueGo);
		add(jTxtValColGo);
		add(jTxtAccColGo);
		add(jBtnFileOpen);
		add(jBtnBGFileGo);
		add(jButRunGo);
		add(jLabUpValueGo);
		add(jLabAlgorithm);
		add(jScrollPaneInput);
		add(jLabInputReviewGo);
		add(jProgressBarGo);
		
		cmbGoAlgorithm = new JComboBoxData<GoAlgorithm>();
		cmbGoAlgorithm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectCmbGoAlgorithm();
			}
		});
		cmbGoAlgorithm.setBounds(12, 356, 152, 23);
		cmbGoAlgorithm.setMapItem(GoAlgorithm.getMapStr2GoAlgrithm());
		add(cmbGoAlgorithm);
		
		spnGOlevel = new JSpinner();
		spnGOlevel.setBounds(112, 389, 60, 18);
		add(spnGOlevel);
		
		chkGOLevel = new JCheckBox("GOLevel");
		chkGOLevel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectGOlevel();
			}
		});
		chkGOLevel.setBounds(12, 387, 92, 22);
		add(chkGOLevel);
		
		sclBlast = new JScrollPaneData();
		JComboBoxData<Species> cmbSpeciesBlast = new JComboBoxData<Species>();
		cmbSpeciesBlast.setMapItem(Species.getSpeciesName2Species(Species.KEGGNAME_SPECIES));
		sclBlast.setTitle(new String[]{"BlastSpecies"});
		sclBlast.setItem(0, cmbSpeciesBlast);
		sclBlast.setBounds(12, 435, 215, 118);
		add(sclBlast);
		
		JButton btnAddBlast = new JButton("Add");
		btnAddBlast.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sclBlast.addItem(new String[]{""});
			}
		});
		btnAddBlast.setBounds(236, 431, 68, 24);
		add(btnAddBlast);
		
		JButton btnDelBlast = new JButton("Del");
		btnDelBlast.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sclBlast.deleteSelRows();
			}
		});
		btnDelBlast.setBounds(237, 530, 67, 24);
		add(btnDelBlast);
		
		JButton btnDeldata = new JButton("DelData");
		btnDeldata.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jScrollPaneInput.deleteSelRows();
			}
		});
		btnDeldata.setBounds(932, 530, 107, 25);
		add(btnDeldata);
		
		chckbxGoAnalysis = new JCheckBox("GO analysis");
		chckbxGoAnalysis.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (chckbxGoAnalysis.isSelected()) {
					jLabAlgorithm.setEnabled(true);
					cmbGoAlgorithm.setEnabled(true);
					selectCmbGoAlgorithm();
					selectGOlevel();
				} else {
					jLabAlgorithm.setEnabled(false);
					cmbGoAlgorithm.setEnabled(false);
					chkGOLevel.setEnabled(false);
					spnGOlevel.setEnabled(false);
				}
			}
		});
		chckbxGoAnalysis.setSelected(true);
		chckbxGoAnalysis.setBounds(8, 243, 118, 23);
		add(chckbxGoAnalysis);
		
		chckbxPathAnalysis = new JCheckBox("Pathway analysis");
		chckbxPathAnalysis.setSelected(true);
		chckbxPathAnalysis.setBounds(8, 270, 149, 23);
		add(chckbxPathAnalysis);
		
		txtSaveToPathAndPrefix = new JTextField();
		txtSaveToPathAndPrefix.setBounds(322, 30, 506, 19);
		add(txtSaveToPathAndPrefix);
		txtSaveToPathAndPrefix.setColumns(10);
		
		JButton btnSavepath = new JButton("SaveToPathAndPrefix");
		btnSavepath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String fileName = guiFileOpen.openFilePathName("", "");
				txtSaveToPathAndPrefix.setText(fileName);
			}
		});
		btnSavepath.setBounds(840, 27, 181, 25);
		add(btnSavepath);
		
		initial();
	}

	private void setComponent() {
		jLabGoQtaxID = new JLabel();
		jLabGoQtaxID.setBounds(12, 30, 111, 18);
		jLabGoQtaxID.setText("Query Species");
		jBtnFileOpen = new JButton();
		jBtnFileOpen.setBounds(316, 531, 103, 22);
		jBtnFileOpen.setText("LoadData");
		jBtnFileOpen.setMargin(new java.awt.Insets(1, 1, 1, 1));
		jBtnFileOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				List<String> lsFileNames = guiFileOpen.openLsFileName("", "");
				List<String[]> lsFileOut = new ArrayList<String[]>();
				for (String string : lsFileNames) {
					String[] tmp = new String[2];
					tmp[0] = string;
					tmp[1] = FileOperate.getFileNameSep(string)[0].split("_")[0];
					lsFileOut.add(tmp);
				}
				jScrollPaneInput.addItemLs(lsFileOut);
			}
		});

		jTxtAccColGo = new JTextFieldData();
		jTxtAccColGo.setBounds(104, 118, 43, 20);
		jTxtAccColGo.setNumOnly();

		jLabAccColGo = new JLabel();
		jLabAccColGo.setBounds(12, 120, 92, 14);
		jLabAccColGo.setText("AccIDColNum");
		jLabAccColGo.setAlignmentY(0.0f);

		jLabValueColGo = new JLabel();
		jLabValueColGo.setBounds(12, 172, 93, 14);
		jLabValueColGo.setText("ValueColNum");
		jLabValueColGo.setAlignmentY(0.0f);

		jLabUpValueGo = new JLabel();
		jLabUpValueGo.setBounds(12, 209, 72, 17);
		jLabUpValueGo.setText("UpValue");

		jTxtUpValueGo = new JTextFieldData();
		jTxtUpValueGo.setBounds(77, 207, 69, 22);
		jTxtUpValueGo.setNumOnly(10, 4);

		jLabDownValueGo = new JLabel();
		jLabDownValueGo.setBounds(152, 212, 87, 11);
		jLabDownValueGo.setText("DownValue");

		jTxtDownValueGo = new JTextFieldData();
		jTxtDownValueGo.setBounds(236, 206, 67, 23);

		jBtnBGFileGo = new JButton();
		jBtnBGFileGo.setBounds(207, 85, 97, 23);
		jBtnBGFileGo.setText("BackGround");
		jBtnBGFileGo.setMargin(new java.awt.Insets(1, 0, 1, 0));
		jBtnBGFileGo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				String filename = guiFileOpen.openFileName("txt/excel2003",
						"txt", "xls");
				jTxtBGGo.setText(filename);
			}
		});

		jTxtValColGo = new JTextFieldData();
		jTxtValColGo.setBounds(104, 172, 44, 19);
		jTxtValColGo.setNumOnly();

		jScrollPaneInput = new JScrollPaneData();
		jScrollPaneInput.setBounds(316, 82, 723, 437);
		jScrollPaneInput.setTitle(new String[]{"InputFile", "OutputPathAndPrefix"});
		
		jLabInputReviewGo = new JLabel();
		jLabInputReviewGo.setBounds(322, 60, 97, 14);
		jLabInputReviewGo.setText("InputReview");

		jProgressBarGo = new JProgressBar();
		jProgressBarGo.setBounds(12, 617, 1027, 14);

		jChkCluster = new JCheckBox();
		jChkCluster.setBounds(12, 142, 149, 22);
		jChkCluster.setText("ClusterGO_PATH");
		jChkCluster.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (jChkCluster.isSelected()) {
					jTxtDownValueGo.setEnabled(false);
					jTxtUpValueGo.setEnabled(false);
				} else {
					jTxtDownValueGo.setEnabled(true);
					jTxtUpValueGo.setEnabled(true);
				}
			}
		});

		jButRunGo = new JButton();
		jButRunGo.setBounds(959, 581, 80, 24);
		jButRunGo.setText("Analysis");
		jButRunGo.setMargin(new java.awt.Insets(1, 1, 1, 1));
		jButRunGo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				for (String[] fileIn2Out : jScrollPaneInput.getLsDataInfo()) {
					String saveTo = txtSaveToPathAndPrefix.getText();
					if (saveTo == null || saveTo.equals("")) {
						saveTo = FileOperate.getParentPathName(fileIn2Out[0]);
					}
					saveTo = saveTo + fileIn2Out[1];
					if (chckbxGoAnalysis.isSelected()) {
						try {
							runGO(fileIn2Out[0], saveTo);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if (chckbxPathAnalysis.isSelected()) {
						try {
							runPath(fileIn2Out[0], saveTo);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
		jLabAlgorithm = new JLabel();
		jLabAlgorithm.setBounds(12, 334, 111, 15);
		jLabAlgorithm.setText("GOAlgorithm");
		jLabBGGo = new JLabel();
		jLabBGGo.setBounds(12, 60, 92, 14);
		jLabBGGo.setText("BackGround");
		jLabBGGo.setAlignmentY(0.0f);
		jLabBGGo.setAutoscrolls(true);
		jTxtBGGo = new JTextFieldData();
		jTxtBGGo.setBounds(104, 58, 200, 18);
		cmbSpecies = new JComboBoxData<Species>();
		cmbSpecies.setBounds(131, 28, 173, 23);
		cmbSpecies.setMapItem(Species
				.getSpeciesName2Species(Species.KEGGNAME_SPECIES));
		cmbSpecies.setEditable(true);
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
	 * analysis按下去后得到结果
	 */
	private void runGO(String excelFile, String outFile) {
		int colAccID = Integer.parseInt(jTxtAccColGo.getText());
		int colFC = Integer.parseInt(jTxtValColGo.getText());
		int taxID = -1;
		Species species = cmbSpecies.getSelectedValue();
		String backGroundFile = jTxtBGGo.getText();
		if (species == null) {
			try {
				taxID = Integer.parseInt(cmbSpecies.getEditor().getItem().toString());
			} catch (Exception e) { }
		} else {
			taxID = species.getTaxID();
		}
		
		ArrayList<String[]> lsAccID = null;
		if (colAccID != colFC) {
			lsAccID = ExcelTxtRead.readLsExcelTxt(excelFile, new int[]{colAccID, colFC}, 1, 0);
		} else {
			lsAccID = ExcelTxtRead.readLsExcelTxt(excelFile, new int[]{colAccID}, 1, 0);
		}
		double evalue = 1e-10;
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
		
		CtrlTestGOInt ctrlGO = (CtrlTestGOInt)SpringFactory.getFactory().getBean("ctrlGOall");
		ctrlGO.clearParam();
		ctrlGO.setGoAlgorithm(cmbGoAlgorithm.getSelectedValue());
		ctrlGO.setTaxID(taxID);

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
		ctrlGO.saveExcel(outFile);
	}
	
	/**
	 * analysis按下去后得到结果
	 */
	private void runPath(String excelFile, String outFile) {
		int colAccID = Integer.parseInt(jTxtAccColGo.getText());
		int colFC = Integer.parseInt(jTxtValColGo.getText());
		int taxID = -1;
		Species species = cmbSpecies.getSelectedValue();
		String backGroundFile = jTxtBGGo.getText();
		if (species == null) {
			try {
				taxID = Integer.parseInt(cmbSpecies.getEditor().getItem().toString());
			} catch (Exception e) { }
		} else {
			taxID = species.getTaxID();
		}
		
		ArrayList<String[]> lsAccID = null;
		if (colAccID != colFC) {
			lsAccID = ExcelTxtRead.readLsExcelTxt(excelFile, new int[]{colAccID, colFC}, 1, 0);
		} else {
			lsAccID = ExcelTxtRead.readLsExcelTxt(excelFile, new int[]{colAccID}, 1, 0);
		}
		double evalue = 1e-10;
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
	
		CtrlTestPathInt ctrlPath = (CtrlTestPathInt)SpringFactory.getFactory().getBean("ctrlPath");
		ctrlPath.clearParam();
		ctrlPath.setTaxID(taxID);

		ctrlPath.setBlastInfo(evalue, lsStaxID);

		ctrlPath.setLsBG(backGroundFile);
		if (!jChkCluster.isSelected() || colAccID == colFC) {
			double up = 0; double down = 0;
			if ( colAccID != colFC) {
				up = Double.parseDouble(jTxtUpValueGo.getText());
				down = Double.parseDouble(jTxtDownValueGo.getText());
			}
			ctrlPath.setUpDown(up, down);
			ctrlPath.setIsCluster(false);
		} else {
			ctrlPath.setIsCluster(jChkCluster.isSelected());
		}
		ctrlPath.setLsAccID2Value(lsAccID);
		ctrlPath.run();
		ctrlPath.saveExcel(outFile);
	}
	
	private void selectCmbGoAlgorithm() {
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
	
	private void selectGOlevel() {
		if (chkGOLevel.isSelected()) {
			spnGOlevel.setEnabled(true);
		} else {
			spnGOlevel.setEnabled(false);
		}
	}
}
