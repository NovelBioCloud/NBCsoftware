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

import com.novelbio.analysis.annotation.cog.COGanno;
import com.novelbio.analysis.annotation.cog.EnumCogType;
import com.novelbio.analysis.annotation.functiontest.TopGO.GoAlgorithm;
import com.novelbio.base.FoldeCreate;
import com.novelbio.base.dataOperate.ExcelTxtRead;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.gui.GUIFileOpen;
import com.novelbio.base.gui.JComboBoxData;
import com.novelbio.base.gui.JScrollPaneData;
import com.novelbio.base.gui.JTextFieldData;
import com.novelbio.database.model.species.Species;
import com.novelbio.database.model.species.Species.EnumSpeciesType;
import com.novelbio.database.service.SpringFactory;
import com.novelbio.nbcgui.controltest.CtrlCOG;
import com.novelbio.nbcgui.controltest.CtrlTestCOGInt;
import com.novelbio.nbcgui.controltest.CtrlTestGOInt;
import com.novelbio.nbcgui.controltest.CtrlTestPathInt;

import java.awt.Dimension;

import javax.swing.JComboBox;


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
	private JButton btnQuerySeq;
	
	JSpinner spnGOlevel;
	
	JScrollPaneData sclBlast;
	
	JComboBoxData<GoAlgorithm> cmbGoAlgorithm;
	JComboBoxData<Species> cmbSpecies;
	JComboBoxData<EnumCogType> cmbCogType;
	
	JCheckBox chkGOLevel;
	JCheckBox chckbxPathAnalysis;
	JCheckBox chckbxCog;
	
	String GoClass = "";
	
	GUIFileOpen guiFileOpen = new GUIFileOpen();
	
	JCheckBox chckbxGoAnalysis;
	private JTextField txtSaveToPathAndPrefix;
	private JTextField txtQuerySeq;
	private JTextField txtEvalueCutoff;
	
	
	public GuiGoMultiJPanel() {
	

		this.setPreferredSize(new Dimension(1078, 662));
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
		cmbSpeciesBlast.setMapItem(Species.getSpeciesName2Species(EnumSpeciesType.All));
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
		
		chckbxCog = new JCheckBox("COG");
		chckbxCog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectCog();
			}
		});
		
		chckbxCog.setSelected(false);
		chckbxCog.setBounds(8, 297, 111, 23);
		add(chckbxCog);
		
		txtSaveToPathAndPrefix = new JTextField();
		txtSaveToPathAndPrefix.setBounds(316, 71, 506, 19);
		add(txtSaveToPathAndPrefix);
		txtSaveToPathAndPrefix.setColumns(10);
		
		JButton btnSavepath = new JButton("SaveToPathAndPrefix");
		btnSavepath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String fileName = guiFileOpen.openFilePathName("", "");
				txtSaveToPathAndPrefix.setText(fileName);
			}
		});
		btnSavepath.setBounds(834, 68, 181, 25);
		add(btnSavepath);
		
		txtQuerySeq = new JTextField();
		txtQuerySeq.setBounds(408, 30, 414, 19);
		add(txtQuerySeq);
		txtQuerySeq.setColumns(10);
		
		btnQuerySeq = new JButton("QuerySequence");
		btnQuerySeq.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String fileName = guiFileOpen.openFileName("sequence", "");
				txtQuerySeq.setText(fileName);
			}
		});
		btnQuerySeq.setBounds(834, 27, 181, 25);
		add(btnQuerySeq);
		
		txtEvalueCutoff = new JTextField();
		txtEvalueCutoff.setBounds(316, 30, 80, 19);
		add(txtEvalueCutoff);
		txtEvalueCutoff.setColumns(10);
		
		JLabel lblEvalueCutoff = new JLabel("evalue cutoff");
		lblEvalueCutoff.setBounds(316, 12, 80, 15);
		add(lblEvalueCutoff);
		
		cmbCogType = new JComboBoxData<>();
		cmbCogType.setMapItem(EnumCogType.getMapCogType());
		cmbCogType.setBounds(152, 296, 152, 24);
		add(cmbCogType);
		
		initial();
	}
	
	private void selectCog() {
		if (chckbxCog.isSelected()) {
			Species species = cmbSpecies.getSelectedValue();
			if (species.getTaxID() == 0 || species.getGffFile() == null) {
				txtEvalueCutoff.setEnabled(true);
				txtQuerySeq.setEnabled(true);
				btnQuerySeq.setEnabled(true);
			} else {
				txtEvalueCutoff.setEnabled(false);
				txtQuerySeq.setEnabled(false);
				btnQuerySeq.setEnabled(false);
			}
		} else {
			txtEvalueCutoff.setEnabled(false);
			txtQuerySeq.setEnabled(false);
			btnQuerySeq.setEnabled(false);
		}
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
		jScrollPaneInput.setBounds(316, 126, 723, 393);
		jScrollPaneInput.setTitle(new String[]{"InputFile", "OutputPathAndPrefix"});
		
		jLabInputReviewGo = new JLabel();
		jLabInputReviewGo.setBounds(316, 102, 97, 14);
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
						saveTo = FileOperate.getParentPathNameWithSep(fileIn2Out[0]);
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
					if (chckbxCog.isSelected()) {
						try {
							runCOG(fileIn2Out[0], saveTo);
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
		cmbSpecies.setMapItem(Species.getSpeciesName2Species(EnumSpeciesType.All, true, null));
		cmbSpecies.setEditable(false);
		cmbSpecies.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				selectCog();
			}
		});
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
		
		if (chckbxCog.isSelected()) {
			txtQuerySeq.setEnabled(true);
			btnQuerySeq.setEnabled(true);
		} else {
			txtQuerySeq.setEnabled(false);
			btnQuerySeq.setEnabled(false);
		}
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
		Map<String, Species> mapComName2Species = Species.getSpeciesName2Species(EnumSpeciesType.All);
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
		ctrlGO.setTaxID(species);

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
		String resultPath = FoldeCreate.createAndInFold(outFile, "GOAnalysis_result");
		ctrlGO.saveExcel(resultPath);// TODO 加路径
//		ctrlGO.saveExcel(outFile, "");// TODO 加路径
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
		Map<String, Species> mapComName2Species = Species.getSpeciesName2Species(EnumSpeciesType.All);
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
		ctrlPath.setTaxID(species);

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
		
		String resultPath = FoldeCreate.createAndInFold(outFile, "PathWayAnalysis_result");
		ctrlPath.saveExcel(resultPath);// TODO 加路径
//		ctrlPath.saveExcel(outFile, ""); // TODO 加路径
	}
	/**
	 * analysis按下去后得到结果
	 */
	private void runCOG(String excelFile, String outFile) {
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
		Map<String, Species> mapComName2Species = Species.getSpeciesName2Species(EnumSpeciesType.All);
		for (String[] strings : sclBlast.getLsDataInfo()) {
			Species speciesS = mapComName2Species.get(strings[0]);
			if (speciesS == null) {
				continue;
			} else {
				lsStaxID.add(speciesS.getTaxID());
			}
		}
	
		CtrlTestCOGInt ctrlCOG = (CtrlTestCOGInt)SpringFactory.getFactory().getBean("ctrlCOG");
		ctrlCOG.clearParam();
		ctrlCOG.setTaxID(species);
		
		COGanno cogAnno = new COGanno(cmbCogType.getSelectedValue());
		try { cogAnno.setEvalueCutoff(Double.parseDouble(txtEvalueCutoff.getText())); 
		} catch (Exception e) { }
		if (chckbxCog.isSelected()) {
			if (species.getTaxID() == 0 || species.getGffFile() == null) {
				cogAnno.setSeqFastaFile(txtQuerySeq.getText());
			} else {
				cogAnno.setSpecies(species);
			}
		}
		
		((CtrlCOG)ctrlCOG).setCogAnno(cogAnno);
		ctrlCOG.setBlastInfo(evalue, lsStaxID);

		ctrlCOG.setLsBG(backGroundFile);
		if (!jChkCluster.isSelected() || colAccID == colFC) {
			double up = 0; double down = 0;
			if ( colAccID != colFC) {
				up = Double.parseDouble(jTxtUpValueGo.getText());
				down = Double.parseDouble(jTxtDownValueGo.getText());
			}
			ctrlCOG.setUpDown(up, down);
			ctrlCOG.setIsCluster(false);
		} else {
			ctrlCOG.setIsCluster(jChkCluster.isSelected());
		}
		ctrlCOG.setLsAccID2Value(lsAccID);
		ctrlCOG.run();
		String resultPath = FoldeCreate.createAndInFold(outFile, "COGAnalysis_result");
		ctrlCOG.saveExcel(resultPath);// TODO 加路径
//		ctrlCOG.saveExcel(outFile, "");// TODO 加路径
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
