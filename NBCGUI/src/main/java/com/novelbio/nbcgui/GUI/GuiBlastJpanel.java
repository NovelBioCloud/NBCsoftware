package com.novelbio.nbcgui.GUI;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import com.novelbio.base.dataOperate.ExcelOperate;
import com.novelbio.base.dataOperate.ExcelTxtRead;
import com.novelbio.base.gui.JComboBoxData;
import com.novelbio.base.gui.JScrollPaneData;
import com.novelbio.database.domain.geneanno.GOtype;
import com.novelbio.database.model.modgeneid.GeneID;
import com.novelbio.database.model.species.Species;
import com.novelbio.nbcgui.controlquery.CtrlBlastAnno;
import com.novelbio.nbcgui.controlquery.CtrlBlastGo;
import com.novelbio.nbcgui.controlquery.CtrlBlastPath;


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
public class GuiBlastJpanel extends JPanel {
	private static final long serialVersionUID = -610030039829839849L;
	private JTextArea jTxtGenID;
	private JLabel jLbGeneID;
	private JScrollPane jScrollPane1;
	private JRadioButton jRadioButtonPath;
	private JRadioButton jRadioButtonGO;
	private ButtonGroup buttonGroup1;
	private JScrollPaneData jScrlGOTable;

	private JLabel jLabelTax;

	private JLabel jLbGOandPath;
	private JLabel jLblCond;
	private JButton jBtnGoPath;
	private JButton jBtnAnno;
	private JProgressBar jProgressBar1;
	private JButton jBtnSaveGO;
	private JSeparator jSeparator1;
	private JButton jBtnSaveAno;
	private JButton jBtnGetFile;
	private JCheckBox jChBlast;
	private JTable jTabFAnno;
	private JScrollPane jScroxTxtGeneID;
	private DefaultTableModel jTabAnno;
	
	private JComboBoxData<GOtype> jComGOClassSelect;
	private JComboBoxData<Species> jCmbSpeciesBlast;
	private JComboBoxData<Species> jCobTaxSelect;
	/**
	 * 一次最多查询的个数
	 */
	static int numLimit = 100000;
	
	GuiBlastJpanel jBlastJpanel = null;
	public GuiBlastJpanel() {

			this.setPreferredSize(new Dimension(1046, 652));
			setAlignmentX(0.0f);
			setComponent();
			setLayout(null);
			add(getJLbGOandPath());
			add(jScroxTxtGeneID);
			add(jLbGeneID);
			add(jBtnGetFile);
			add(getJBtnGoPath());
			add(getJRadioButtonGO());
			add(getJCmbGOClassSelect());
			add(getJRadioButtonPath());
			add(getJBtnSaveGO());
			add(getJBtnSaveAno());
			add(getJSeparator1());
			add(jChBlast);
			add(getJCmbSpeciesBlast());
			add(getJBtnAnno());
			add(getJLblCond());
			add(getJLabelTax());
			add(getJCobTaxSelect());
			add(jScrollPane1);
			add(getJScrlGOTable());
			add(getJProgressBar1());
			jBlastJpanel = this;
		
	}
	
	private void setComponent() {
		buttonGroup1 = new ButtonGroup();
		{
			jScroxTxtGeneID = new JScrollPane();
			jScroxTxtGeneID.setBounds(7, 32, 199, 274);
			{
				jTxtGenID = new JTextArea();
				jScroxTxtGeneID.setViewportView(jTxtGenID);
				//jTxtGenID.setPreferredSize(new java.awt.Dimension(173, 247));
			}
		}
		{
			jLbGeneID = new JLabel();
			jLbGeneID.setBounds(7, 0, 102, 26);
			jLbGeneID.setText("Enter GeneID");
		}
		{
			jBtnGetFile = new JButton();
			jBtnGetFile.setBounds(109, 3, 97, 20);
			jBtnGetFile.setText("OpenFile");
			jBtnGetFile.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					setJTxtFrame();
				}
			});
		}
		{
			jChBlast = new JCheckBox();
			jChBlast.setBounds(212, 270, 67, 20);
			jChBlast.setMargin(new java.awt.Insets(0, 0, 0, 0));
			jChBlast.setName("jChBlast");
		}
		{
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setBounds(212, 32, 822, 230);
			jScrlGOTable = new JScrollPaneData();
			jScrlGOTable.setBounds(7, 343, 1027, 266);
		}
	
	}
	
	public JButton getJBtnSaveAno() {
		if(jBtnSaveAno == null) {
			jBtnSaveAno = new JButton();
			jBtnSaveAno.setBounds(894, 268, 93, 24);
			jBtnSaveAno.setText("SaveAs");
			jBtnSaveAno.setRolloverEnabled(true);
			jBtnSaveAno.setEnabled(false);
			jBtnSaveAno.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					if (lsAnno != null && lsAnno.size()>0) {
						saveFile(lsAnno,titleAnno);
					}
				}
			});
		}
		return jBtnSaveAno;
	}
	
	private JSeparator getJSeparator1() {
		if(jSeparator1 == null) {
			jSeparator1 = new JSeparator();
			jSeparator1.setBounds(312, 298, 722, 6);
		}
		return jSeparator1;
	}
	
	private JScrollPane getJScrlGOTable() {
		if(jScrlGOTable == null) {
			jScrlGOTable = new JScrollPaneData();
			jScrlGOTable.setBounds(7, 343, 1027, 236);
		}
		return jScrlGOTable;
	}
	static int mm = 0;
	public JButton getJBtnSaveGO() {
		if(jBtnSaveGO == null) {
			jBtnSaveGO = new JButton();
			jBtnSaveGO.setBounds(894, 306, 93, 24);
			jBtnSaveGO.setText("SaveAs");
			jBtnSaveGO.setEnabled(false);
			jBtnSaveGO.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					if (lsGoandPath != null && lsGoandPath.size()>0) {
						saveFile(lsGoandPath, jScrlGOTable.getTitle());
					}
				}
			});
		}
		return jBtnSaveGO;
	}
	 public JProgressBar getJProgressBar1() {
		if(jProgressBar1 == null) {
			jProgressBar1 = new JProgressBar();
			jProgressBar1.setBounds(0, 614, 1027, 13);
		}
		return jProgressBar1;
	}
	
 
	
	private JRadioButton getJRadioButtonGO() {
		if(jRadioButtonGO == null) {
			jRadioButtonGO = new JRadioButton();
			jRadioButtonGO.setBounds(312, 307, 132, 22);
			jRadioButtonGO.setText("GeneOntology");
			jRadioButtonGO.setSelected(true);
			buttonGroup1.add(jRadioButtonGO);
			jRadioButtonGO.setMargin(new java.awt.Insets(2, 0, 2, 0));
			jRadioButtonGO.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					jComGOClassSelect.setEnabled(true);
				}
			});
		}
		return jRadioButtonGO;
	}
	
	private JRadioButton getJRadioButtonPath() {
		if(jRadioButtonPath == null) {
			jRadioButtonPath = new JRadioButton();
			jRadioButtonPath.setBounds(755, 307, 135, 22);
			jRadioButtonPath.setText("KEGG Pathway");
			buttonGroup1.add(jRadioButtonPath);
			jRadioButtonPath.setMargin(new java.awt.Insets(2, 0, 2, 0));
			jRadioButtonPath.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					jComGOClassSelect.setEnabled(false);
				}
			});
		}
		return jRadioButtonPath;
	}
	
	public DefaultTableModel getJTabAnnol() {
		return jTabAnno;
	}
	
	CtrlBlastAnno ctrlAnno;
	String[] titleAnno = null;
	public JButton getJBtnAnno() {
		if(jBtnAnno == null) {
			jBtnAnno = new JButton();
			jBtnAnno.setBounds(524, 268, 96, 24);
			jBtnAnno.setText("Query");
			jBtnAnno.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					
					String[] queryID = jTxtGenID.getText().split("\n");
					ArrayList<String> lsAccID = new ArrayList<String>();
					for (int i = 0; i < queryID.length; i++) {
						if (queryID[i].trim().equals("")) {
							continue;
						}
						lsAccID.add(queryID[i]);
					}
					List<String> lsGenID2 = null;
					//////////////////一次只能读取3000个
					if (lsAccID.size()>numLimit) {
						JOptionPane.showMessageDialog(null, "To ensure the stability of the database, the gene number of each query is limited in."+numLimit, "alert", JOptionPane.INFORMATION_MESSAGE); 
						lsGenID2 = lsAccID.subList(0, numLimit);
					}
					else
						lsGenID2 = lsAccID;
					///////////////各种设置
					//设置进度条
					getJBtnSaveAno().setEnabled(false);
					jProgressBar1.setMinimum(0); jProgressBar1.setMaximum(lsGenID2.size()-1);
					jProgressBar1.setValue(0);
					getJBtnAnno().setEnabled(false);
					getJLblCond().setText("Running, please be patient.");
					///////////////////
					
						boolean blast = jChBlast.isSelected();
						if (!blast) 
						{
							//设置anno结果框
							{
								titleAnno = new String[5];
								titleAnno[0] ="QueryID";titleAnno[1] = "AccID"; titleAnno[2] ="Symbol/AccID";titleAnno[3] ="Description";titleAnno[4] ="KeggID";
								String[][] tableValue = null;
								jTabAnno = new DefaultTableModel(tableValue,titleAnno);
								jTabFAnno = new JTable();
								jScrollPane1.setViewportView(jTabFAnno);
								jTabFAnno.setModel(jTabAnno);
							}
						}
						else {
							//设置anno结果框
							{
								titleAnno = new String[10];
								titleAnno[0] ="QueryID";titleAnno[1] ="AccID";titleAnno[2] ="Symbol/AccID";titleAnno[3] ="Description"; titleAnno[4] ="KeggID";
								titleAnno[5] ="evalue";titleAnno[6] ="BlastAccID"; titleAnno[7] ="BlastSymbol/AccID";titleAnno[8] ="BlastDescription";
								titleAnno[9] ="BlastKeggID";
								String[][] tableValue = null;
								jTabAnno = new DefaultTableModel(tableValue,titleAnno);
								jTabFAnno = new JTable();
								jScrollPane1.setViewportView(jTabFAnno);
								jTabFAnno.setModel(jTabAnno);
							}
						}
						ctrlAnno = new CtrlBlastAnno(blast, jCobTaxSelect.getSelectedValue().getTaxID(), jCmbSpeciesBlast.getSelectedValue().getTaxID(), 1e-10,jBlastJpanel );
						ctrlAnno.prepare(lsGenID2);
						ctrlAnno.execute();
						
						//ctrlAnno.done();
					
				}
			});
		}
		return jBtnAnno;
	}

	CtrlBlastGo ctrlGo;
	CtrlBlastPath ctrlPath;
	public JButton getJBtnGoPath() {
		if(jBtnGoPath == null) {
			jBtnGoPath = new JButton();
			jBtnGoPath.setBounds(212, 306, 96, 24);
			jBtnGoPath.setText("Query");
			jBtnGoPath.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					String[] queryID = jTxtGenID.getText().split("\n");
					ArrayList<String> lsGenID = new ArrayList<String>();
					for (int i = 0; i < queryID.length; i++) {
						if (queryID[i].trim().equals("")) {
							continue;
						}
						lsGenID.add(queryID[i]);
					}
					List<String> lsGenID2 = new ArrayList<String>();
					for (String string : lsGenID) {
						lsGenID2.add(string);
					}
					
					
					//////////////////一次只能读取3000个
					if (lsGenID.size() > numLimit) {
						JOptionPane.showMessageDialog(null, "To ensure the stability of the database, the gene number of each query is limited in 3000.", "alert", JOptionPane.INFORMATION_MESSAGE); 
						lsGenID2 = lsGenID2.subList(0, numLimit);
					}
					///////////////各种设置
					///////////////各种设置///////////////
					//设置进度条
					getJBtnSaveGO().setEnabled(false);
					jProgressBar1.setMinimum(0); jProgressBar1.setMaximum(lsGenID2.size()-1);
					jProgressBar1.setValue(0);
					getJBtnGoPath().setEnabled(false);
					getJLbGOandPath().setText("Running, please be patient.");
					///////////////////
					if (jRadioButtonGO.isSelected()) {
						boolean blast = jChBlast.isSelected();
						String[] titleGoandPath = null;
						if (!blast) {
							titleGoandPath = new String[4];
							titleGoandPath[0] = "QueryID"; titleGoandPath[1] = "Symbol/AccID"; titleGoandPath[2] ="GOID"; titleGoandPath[3] = "GOTerm";
						}
						else {
							titleGoandPath = new String[]{  "QueryID", "Symbol/AccID","GOID","GOTerm","evalue", "BlastSymbol/AccID","BlastGOID","BlastGOTerm"};
						}
						jScrlGOTable.setTitle(titleGoandPath);
						ctrlGo = new CtrlBlastGo(jBlastJpanel);
						ctrlGo.setTaxID(jCobTaxSelect.getSelectedValue().getTaxID());
						if (jChBlast.isSelected()) {
							ctrlGo.setBlastInfo(jCmbSpeciesBlast.getSelectedValue().getTaxID(), 1e-10);
						}
						ctrlGo.setGoClass(jComGOClassSelect.getSelectedValue());
						ctrlGo.prepare(lsGenID2);
						ctrlGo.execute();
					}
					else if (jRadioButtonPath.isSelected()) {
						boolean blast = jChBlast.isSelected();
						String[] titleGoandPath = null;
						if (!blast) {
							titleGoandPath = new String[4];
							titleGoandPath[0] = "QueryID"; titleGoandPath[1] = "Symbol/AccID"; titleGoandPath[2] ="PathID"; titleGoandPath[3] = "PathTitle";
						}
						else {
							titleGoandPath = new String[8];
							titleGoandPath[0] = "QueryID"; titleGoandPath[1] = "Symbol/AccID"; titleGoandPath[2] ="PathID"; titleGoandPath[3] = "PathTitle";
							titleGoandPath[4] = "evalue"; titleGoandPath[5] = "BlastSymbol/AccID"; titleGoandPath[6] ="BlastPathID"; titleGoandPath[7] = "BlastPathTitle";						
						}
						jScrlGOTable.setTitle(titleGoandPath);
						ctrlPath = new CtrlBlastPath(blast, jCobTaxSelect.getSelectedValue().getTaxID(), jCmbSpeciesBlast.getSelectedValue().getTaxID(), 1e-10, jBlastJpanel);
						ctrlPath.prepare(lsGenID2);
						ctrlPath.execute();
					}
				}
			});
		}
		return jBtnGoPath;
	}
	
	public JLabel getJLblCond() {
		if(jLblCond == null) {
			jLblCond = new JLabel();
			jLblCond.setBounds(236, 6, 318, 14);
			jLblCond.setText("Prepare");
		}
		return jLblCond;
	}
	
	private void thisWindowClosing(WindowEvent evt) {
		System.out.println("this.windowClosing, event="+evt);
		//TODO add your code for this.windowClosing
	}
	
	public JLabel getJLbGOandPath() {
		if(jLbGOandPath == null) {
			jLbGOandPath = new JLabel();
			jLbGOandPath.setBounds(7, 311, 205, 14);
			jLbGOandPath.setText("Prepare");
		}
		return jLbGOandPath;
	}

///////////////////////////////////////////////////viewer方法实现///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//打开文本选择器
	private String getFileName() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("txt/excel 2003", "txt","xls");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(getParent());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile().getAbsolutePath();
		}
		return null;
	}
	//打开文本选择器
	private String saveFileName() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("txt/excel 2003", "txt","xls");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showSaveDialog(getParent());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile().getAbsolutePath();
		}
		return null;
	}
////////////////////////////////////////保存文件////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	ArrayList<String[]> lsAnno = null;
	ArrayList<String[]> lsGoandPath = null;
	public void setLsAnno(ArrayList<String[]> lsAnno) {
		this.lsAnno = lsAnno;
	}
	public void setLsGoandPath(ArrayList<String[]> lsGoandPath) {
		this.lsGoandPath = lsGoandPath;
	}
	///////////////读取文本的第一列，并保存为arraylist-string,错误则返回null并且弹出错误对话框/////////////////////////////////////////////////////////////
	private ArrayList<String> getFirstCol(String filePath) {
		ArrayList<String[]> lstmp = null;
		try {
			lstmp = ExcelTxtRead.readLsExcelTxt(filePath, 1);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "input file formate is not correct.\n Only accept excel2003 or txt file", "Error", JOptionPane.ERROR_MESSAGE); 
			e.printStackTrace();
			return null;
		}
		ArrayList<String> lsGeneID = new ArrayList<String>();
		for (String[] strings : lstmp) {
			lsGeneID.add(strings[0]);
		}
		return lsGeneID;
	}
	
	private void setJTxtFrame() {
		String filePathe = getFileName();
		ArrayList<String> lsGeneID = null;
		if (filePathe == null) 
			return;
		
		lsGeneID = getFirstCol(filePathe);

		if (lsGeneID == null)
			return;
		jTxtGenID.setText("");
		for (String string : lsGeneID) {
			jTxtGenID.append(string+"\n");
		}
		
	}
	private JComboBox getJCobTaxSelect() {
		if(jCobTaxSelect == null) {
			jCobTaxSelect = new JComboBoxData<Species>();
			jCobTaxSelect.setBounds(800, 2, 228, 23);
			jCobTaxSelect.setMapItem(Species.getSpeciesName2Species(Species.KEGGNAME_SPECIES));
			jCobTaxSelect.setEditable(true);
		}
		return jCobTaxSelect;
	}
	
	private JLabel getJLabelTax() {
		if(jLabelTax == null) {
			jLabelTax = new JLabel();
			jLabelTax.setBounds(661, 6, 139, 14);
			jLabelTax.setText("select species");
		}
		return jLabelTax;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void saveFile(ArrayList<String[]> lsResult,String[] title) {
		String filePath = saveFileName();
		System.out.println(filePath);
		if (filePath == null)
			return;
		File file = new File(filePath);
		int result = -100;
		if (file.exists()) {
			result = JOptionPane.showConfirmDialog(null, "Are you sure to overwrite the file?", "File Already exist", JOptionPane.YES_NO_OPTION);
			if(result != JOptionPane.OK_OPTION)
				return;
		}
		ExcelOperate excelSave = new ExcelOperate();
		if (!filePath.endsWith(".xls")) {
			filePath=filePath+".xls";
		}
		excelSave.newExcelOpen(filePath);
		lsResult.add(0, title);
		boolean save = excelSave.WriteExcel( 1, 1, lsResult);
		if (save) {
			JOptionPane.showMessageDialog(null, "Your Data Was Saved!", "Save Finished", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public JComboBoxData<GOtype> getJCmbGOClassSelect() {
		if (jComGOClassSelect == null) {
			jComGOClassSelect = new JComboBoxData<GOtype>();
			jComGOClassSelect.setBounds(453, 307, 229, 23);
			jComGOClassSelect.setMapItem(GOtype.getMapStrAllGotype(true));
		}
		return jComGOClassSelect;
	}
	
	private JComboBoxData<Species> getJCmbSpeciesBlast() {
		if(jCmbSpeciesBlast == null) {
			jCmbSpeciesBlast = new JComboBoxData<Species>();
			jCmbSpeciesBlast.setBounds(283, 269, 229, 23);
			jCmbSpeciesBlast.setMapItem(Species.getSpeciesName2Species(Species.KEGGNAME_SPECIES));
		}
		return jCmbSpeciesBlast;
	}

	NBCJDialog nbcjDialog;
	public JScrollPaneData getJTabGoandPath() {
		return jScrlGOTable;
	}

}
