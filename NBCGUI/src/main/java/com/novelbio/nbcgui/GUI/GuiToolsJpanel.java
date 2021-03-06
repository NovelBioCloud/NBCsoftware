package com.novelbio.nbcgui.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.novelbio.base.MD5generate;
import com.novelbio.base.SepSign;
import com.novelbio.base.dataOperate.DateUtil;
import com.novelbio.base.dataOperate.TxtReadandWrite;
import com.novelbio.base.dataStructure.PatternOperate;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.gui.GUIFileOpen;
import com.novelbio.base.gui.JScrollPaneData;
import com.novelbio.base.gui.JTextFieldData;
import com.novelbio.database.model.information.SoftWareInfo;
import com.novelbio.nbcgui.controltools.CtrlCombFile;
import com.novelbio.nbcgui.controltools.CtrlMedian;

public class GuiToolsJpanel extends JPanel {
	private static final long serialVersionUID = -6252286036589241467L;
	private JTextField jtxtFileNameMedian;
	private JTextFieldData jtxtAccID;
	private JTextField jtxtColNum;
	private JTextField jtxtAccIDComp;
	GUIFileOpen guiFileOpenComb = new GUIFileOpen();
	JScrollPaneData scrollPane;
	
	JScrollPaneData scrlFile2Md5;
	
	JCheckBox chckbxSaveToFile;
	JCheckBox chckbxChangeFileName;
	
	/**
	 * Create the panel.
	 */
	public GuiToolsJpanel() {
		setLayout(null);
		
		jtxtFileNameMedian = new JTextField();
		jtxtFileNameMedian.setBounds(9, 36, 364, 26);
		add(jtxtFileNameMedian);
		jtxtFileNameMedian.setColumns(10);
		
		JButton btnOpenfileMedian = new JButton("OpenFile");
		btnOpenfileMedian.setBounds(385, 37, 97, 24);
		//选择待取中位数的文件
		btnOpenfileMedian.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String filename = guiFileOpenComb.openFileName("txt/excel2003", "txt","xls");
				jtxtFileNameMedian.setText(filename);
			}
		});
		add(btnOpenfileMedian);
		
		jtxtAccID = new JTextFieldData();
		jtxtAccID.setBounds(9, 94, 96, 26);
		jtxtAccID.setNumOnly();
		add(jtxtAccID);
		jtxtAccID.setColumns(10);
		
		JLabel lblAccidcol = new JLabel("AccIDCol");
		lblAccidcol.setBounds(9, 73, 61, 14);
		add(lblAccidcol);
		
		JLabel lblFilename = new JLabel("FileName");
		lblFilename.setBounds(9, 15, 65, 14);
		add(lblFilename);
		
		jtxtColNum = new JTextField();
		jtxtColNum.setBounds(117, 94, 228, 26);
		add(jtxtColNum);
		jtxtColNum.setColumns(10);
		
		JLabel lblColnum = new JLabel("ColNum");
		lblColnum.setBounds(118, 74, 53, 13);
		add(lblColnum);
		
		JButton btnSaveasMedian = new JButton("SaveAs");
		btnSaveasMedian.setBounds(385, 95, 97, 24);
		btnSaveasMedian.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String filename = guiFileOpenComb.saveFileName("txt/excel2003", "txt","xls");
				String inFile = jtxtFileNameMedian.getText();
				String txtAccID = jtxtAccID.getText();
				String txtColID = jtxtColNum.getText();
				getMedian(inFile, txtAccID, txtColID, filename);
			}
		});
		add(btnSaveasMedian);
		
		scrollPane = new JScrollPaneData();
		scrollPane.setBounds(9, 208, 456, 252);
		add(scrollPane);
		
		JButton btnAddlineCompare = new JButton("AddFile");
		btnAddlineCompare.setBounds(10, 465, 86, 24);
		scrollPane.setTitle(new String[]{"FileName","FilePrix","CombCol"});
		btnAddlineCompare.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> lsfilename = guiFileOpenComb.openLsFileName("txt/excel2003", "txt","xls");
				for (String strings : lsfilename) {
					scrollPane.addItem(new String[]{strings,""});
				}
			}
		});
		add(btnAddlineCompare);
		
		JButton btnSaveasCompare = new JButton("SaveAs");
		btnSaveasCompare.setBounds(378, 465, 86, 24);
		btnSaveasCompare.setVerticalAlignment(SwingConstants.TOP);
		btnSaveasCompare.setHorizontalAlignment(SwingConstants.LEFT);
		btnSaveasCompare.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String filename = guiFileOpenComb.saveFileName("txt/excel2003", "txt","xls");
				CtrlCombFile ctrlCombFile = new CtrlCombFile();
				String colAccID = jtxtAccIDComp.getText();
				ctrlCombFile.setCompareCol(colAccID);
				ArrayList<String[]> lsInfo = scrollPane.getLsDataInfo();
				for (String[] strings : lsInfo) {
					String fileName = strings[0].trim();
					String filePrix = strings[1].trim();
					String colID = strings[2].trim();
					if (!FileOperate.isFileExistAndNotDir(fileName)) {
						continue;
					}
					if (filePrix.equals("")) {
						filePrix = FileOperate.getFileNameSep(fileName)[0];
					}
					ctrlCombFile.setColDetail(fileName, filePrix, colID);
				}
				ctrlCombFile.setOufFile(filename);
				ctrlCombFile.output();
			}
		});
		add(btnSaveasCompare);
		
		jtxtAccIDComp = new JTextField();
		jtxtAccIDComp.setBounds(281, 163, 68, 18);
		add(jtxtAccIDComp);
		jtxtAccIDComp.setColumns(10);
		
		JLabel lblCompareid = new JLabel("compareID");
		lblCompareid.setBounds(167, 165, 86, 14);
		add(lblCompareid);
		
		JLabel lblFile = new JLabel("File");
		lblFile.setBounds(10, 165, 46, 14);
		add(lblFile);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(917, 159, 0, 77);
		add(separator);
		
		JButton btnDelfile = new JButton("DelFile");
		btnDelfile.setBounds(148, 467, 82, 24);
		btnDelfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scrollPane.deleteSelRows();
			}
		});
		add(btnDelfile);
		
		JButton btnImportSoftwareInfo = new JButton("ImportSoftwareInfo");
		btnImportSoftwareInfo.setBounds(368, 532, 253, 24);
		btnImportSoftwareInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String softToolsFile = guiFileOpenComb.openFileName("txt/xls", "");
				if (FileOperate.isFileExistAndBigThanSize(softToolsFile, 0.1)) {
					SoftWareInfo.updateInfo(softToolsFile);
				}
			}
		});
		add(btnImportSoftwareInfo);
		
		scrlFile2Md5 = new JScrollPaneData();
		scrlFile2Md5.setBounds(509, 40, 469, 341);
		add(scrlFile2Md5);
		
		JLabel lblGetmd = new JLabel("GetMD5");
		lblGetmd.setBounds(507, 14, 69, 14);
		add(lblGetmd);
		
		JButton btnOpenfile = new JButton("Open");
		btnOpenfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> lsFileName = guiFileOpenComb.openLsFileName("", "");
				ArrayList<String[]> lsFile = new ArrayList<String[]>();
				for (String string : lsFileName) {
					String[] strings = new String[]{string};
					lsFile.add(strings);
				}
				scrlFile2Md5.addItemLs(lsFile);
			}
		});
		btnOpenfile.setBounds(509, 393, 65, 24);
		add(btnOpenfile);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scrlFile2Md5.deleteSelRows();
			}
		});
		btnDelete.setBounds(587, 394, 82, 24);
		add(btnDelete);
		
		JButton btnRungetmd = new JButton("RunGetMD5");
		btnRungetmd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<String[]> lsFileName = scrlFile2Md5.getLsDataInfo();
				ArrayList<String[]> lsResultMD5 = new ArrayList<String[]>();
				if (lsFileName.size() == 0) {
					return;
				}
				TxtReadandWrite txtWrite = new TxtReadandWrite(FileOperate.getPathName(lsFileName.get(0)[0]) + "MD5" + DateUtil.getDateAndRandom(), true);
				for (String[] strings : lsFileName) {
					String[] tmpResult = new String[2];
					tmpResult[0] = strings[0];
					tmpResult[1] = MD5generate.getFileMD5(strings[0]);
					lsResultMD5.add(tmpResult);
					if (chckbxSaveToFile.isSelected()) {
						if (chckbxChangeFileName.isSelected()) {
							FileOperate.changeFileSuffixReal(tmpResult[0], SepSign.SEP_INFO + tmpResult[1] + SepSign.SEP_INFO, null);
						} else {
							txtWrite.writefileln(FileOperate.getFileName(tmpResult[0]) + "\t" + tmpResult[1]);
						}
					}
				}
				txtWrite.close();
				scrlFile2Md5.clean();
				scrlFile2Md5.setItemLs(lsResultMD5);
			}
		});
		btnRungetmd.setBounds(860, 436, 118, 24);
		add(btnRungetmd);
		
		chckbxSaveToFile = new JCheckBox("save to file");
		chckbxSaveToFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (chckbxSaveToFile.isSelected()) {
					chckbxChangeFileName.setVisible(true);
				} else {
					chckbxChangeFileName.setVisible(false);
				}
			}
		});
		chckbxSaveToFile.setSelected(true);
		chckbxSaveToFile.setBounds(810, 388, 115, 26);
		add(chckbxSaveToFile);
		
		chckbxChangeFileName = new JCheckBox("change file name");
		chckbxChangeFileName.setSelected(true);
		chckbxChangeFileName.setBounds(810, 408, 171, 26);
		add(chckbxChangeFileName);

		
		initial();
	}
	
	private void initial() {
		scrlFile2Md5.setTitle(new String[]{"FileName", "MD5"});
	}
	
	private void getMedian(String inFile, String txtAccID, String txtColID, String outFile) {
		CtrlMedian ctrlMedian = new CtrlMedian();
		ctrlMedian.setAccID(Integer.parseInt(txtAccID));
		ArrayList<String[]> lsColID = PatternOperate.getPatLoc(txtColID, "\\d+", false);
		ArrayList<Integer> lsCol = new ArrayList<Integer>();
		for (String[] strings : lsColID) {
			lsCol.add(Integer.parseInt(strings[0]));
		}
		ctrlMedian.setFile(inFile);
		ctrlMedian.setMedianID(lsCol);
		ctrlMedian.readFile();
		ctrlMedian.getResult();
		ctrlMedian.saveFile(FileOperate.changeFileSuffix(outFile, "", "xls"));
	}
}
