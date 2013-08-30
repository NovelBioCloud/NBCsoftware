package com.novelbio.nbcgui.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.table.DefaultTableModel;


import com.novelbio.analysis.annotation.pathway.kegg.kGpath.Scr2Target;
import com.novelbio.base.dataOperate.ExcelOperate;
import com.novelbio.base.dataOperate.ExcelTxtRead;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.gui.GUIFileOpen;
import com.novelbio.base.gui.JComboBoxData;
import com.novelbio.base.gui.JScrollPaneData;
import com.novelbio.base.gui.JTextFieldData;
import com.novelbio.database.model.modgeneid.GeneID;
import com.novelbio.database.model.species.Species;


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
public class GuiSrcToTrgJpanel extends JPanel{

	private JTabbedPane jTabbedPanePathResult;
	private JButton jBtbSavePath;
	private JProgressBar jProgressBarPath;
	private JLabel jLabResultReviewPath;
	private JLabel jLabInputReviewPath;
	private JLabel jLabPathPath;
	private JTextField jTxtFilePathPath;
	private JButton jBtnFileOpenPath;
	private JLabel jLabAccColPath;
	private JTextFieldData jTxtAccColPath;
	private JComboBoxData<Species> jCombBlastTaxPath;
	private JCheckBox jChkBlastPath;
	private ButtonGroup btnGroupPathMethod;
	private ButtonGroup btnGroupPathClass;
	private JComboBoxData<Species> jCombSelSpePath;
	private JLabel jLabPathQtaxID;
	private JScrollPaneData jScrollPaneInputPath;
	////////////	
	
	public GuiSrcToTrgJpanel() 
	{
		GroupLayout jPanPathLayout = new GroupLayout((JComponent)this);
		

		setPreferredSize(new java.awt.Dimension(1046, 617));
		setAlignmentX(0.0f);
		setComponent();
		setGroup(jPanPathLayout);
	}
	private void setComponent() {
		btnGroupPathMethod = new ButtonGroup();
		btnGroupPathClass = new ButtonGroup();
		{
			jLabPathQtaxID = new JLabel();
			jLabPathQtaxID.setText("Query Species");
		}
		{
			jTxtFilePathPath = new JTextField();
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
			jBtnFileOpenPath.setText("LoadData");
			jBtnFileOpenPath.setMargin(new java.awt.Insets(1, 1, 1, 1));
			jBtnFileOpenPath.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					GUIFileOpen guiFileOpen = new GUIFileOpen();
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
			jLabPathPath.setText("InputData");
		}
		{
			jTxtAccColPath = new JTextFieldData();
			jTxtAccColPath.setNumOnly();
		}
		{
			jLabAccColPath = new JLabel();
			jLabAccColPath.setText("AccIDColNum");
			jLabAccColPath.setAlignmentY(0.0f);
		}

		{
			jScrollPaneInputPath = new JScrollPaneData();
		}
		{
			jLabInputReviewPath = new JLabel();
			jLabInputReviewPath.setText("InputReview");
		}
		{
			jLabResultReviewPath = new JLabel();
			jLabResultReviewPath.setText("ResultReview");
		}
		{
			jProgressBarPath = new JProgressBar();
		}
		{
			jTabbedPanePathResult = new JTabbedPane();
		}
		{
			jBtbSavePath = new JButton();
			jBtbSavePath.setText("Save As");
			jBtbSavePath.setMargin(new java.awt.Insets(1, 0, 1, 0));
			jBtbSavePath.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					GUIFileOpen guiFileOpen = new GUIFileOpen();
					String savefilename = guiFileOpen.saveFileName("excel", "xls");
					if (!FileOperate.getFileNameSep(savefilename)[1].equals("xls")) {
						savefilename = savefilename+".xls";
					}
					try {
						ArrayList<String[]> lsAccIDstr = ExcelTxtRead.readLsExcelTxt(jTxtFilePathPath.getText(), new int[]{Integer.parseInt(jTxtAccColPath.getText())}, 1, -1);
						ArrayList<String> lsAccID = new ArrayList<String>();
						for (String[] strings : lsAccIDstr) {
							if (strings[0] == null || strings[0].trim().equals("")) {
								continue;
							}
							lsAccID.add(strings[0]);
						}
						Scr2Target.getGene2RelateKo("",lsAccID,FileOperate.changeFileSuffix(savefilename, "_Gene_Act_Net", null),
								FileOperate.changeFileSuffix(savefilename, "_attr", null) , jCombSelSpePath.getSelectedValue().getTaxID(),
								jChkBlastPath.isSelected(), jCombBlastTaxPath.getSelectedValue().getTaxID(), 1e-5);
						
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			});
		}
		{
			jCombSelSpePath = new JComboBoxData<Species>();
			jCombSelSpePath.setMapItem(Species.getSpeciesName2Species(Species.KEGGNAME_SPECIES));
		}
		{
			jChkBlastPath = new JCheckBox();
			jChkBlastPath.setText("Blast");
		}
		{
			jCombBlastTaxPath = new JComboBoxData<Species>();
			jCombBlastTaxPath.setMapItem(Species.getSpeciesName2Species(Species.KEGGNAME_SPECIES));
		}
	}
	
	
	private void setGroup(GroupLayout jPanPathLayout)
	{
		this.setLayout(jPanPathLayout);
		jPanPathLayout.setHorizontalGroup(jPanPathLayout.createSequentialGroup()
		.addContainerGap()
		.addGroup(jPanPathLayout.createParallelGroup()
		    .addGroup(jPanPathLayout.createSequentialGroup()
		        .addGroup(jPanPathLayout.createParallelGroup()
		            .addComponent(jTxtFilePathPath, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 217, GroupLayout.PREFERRED_SIZE)
		            .addGroup(jPanPathLayout.createSequentialGroup()
		                .addGroup(jPanPathLayout.createParallelGroup()
		                    .addComponent(jBtbSavePath, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE)
		                    .addComponent(jLabAccColPath, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE)
		                    .addGroup(GroupLayout.Alignment.LEADING, jPanPathLayout.createSequentialGroup()
		                        .addComponent(jChkBlastPath, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
		                        .addGap(13))
		                    .addGroup(GroupLayout.Alignment.LEADING, jPanPathLayout.createSequentialGroup()
		                        .addComponent(jLabPathPath, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
		                        .addGap(8)))
		                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 54, GroupLayout.PREFERRED_SIZE))
		            .addGroup(GroupLayout.Alignment.LEADING, jPanPathLayout.createSequentialGroup()
		                .addComponent(jCombBlastTaxPath, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
		                .addGap(17))
		            .addGroup(GroupLayout.Alignment.LEADING, jPanPathLayout.createSequentialGroup()
		                .addComponent(jLabPathQtaxID, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE)
		                .addGap(88))
		            .addGroup(GroupLayout.Alignment.LEADING, jPanPathLayout.createSequentialGroup()
		                .addComponent(jCombSelSpePath, GroupLayout.PREFERRED_SIZE, 174, GroupLayout.PREFERRED_SIZE)
		                .addGap(43))
		            .addGroup(GroupLayout.Alignment.LEADING, jPanPathLayout.createSequentialGroup()
		                .addGap(110)
		                .addGroup(jPanPathLayout.createParallelGroup()
		                    .addGroup(GroupLayout.Alignment.LEADING, jPanPathLayout.createSequentialGroup()
		                        .addComponent(jTxtAccColPath, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
		                        .addGap(62))
		                    .addComponent(jBtnFileOpenPath, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE))))
		        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
		        .addGroup(jPanPathLayout.createParallelGroup()
		            .addGroup(jPanPathLayout.createSequentialGroup()
		                .addGap(0, 0, Short.MAX_VALUE)
		                .addComponent(jTabbedPanePathResult, GroupLayout.PREFERRED_SIZE, 794, GroupLayout.PREFERRED_SIZE))
		            .addGroup(jPanPathLayout.createSequentialGroup()
		                .addComponent(jScrollPaneInputPath, GroupLayout.PREFERRED_SIZE, 794, GroupLayout.PREFERRED_SIZE)
		                .addGap(0, 0, Short.MAX_VALUE))
		            .addGroup(GroupLayout.Alignment.LEADING, jPanPathLayout.createSequentialGroup()
		                .addComponent(jLabInputReviewPath, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE)
		                .addGap(0, 697, Short.MAX_VALUE))
		            .addGroup(GroupLayout.Alignment.LEADING, jPanPathLayout.createSequentialGroup()
		                .addGap(7)
		                .addComponent(jLabResultReviewPath, GroupLayout.PREFERRED_SIZE, 127, GroupLayout.PREFERRED_SIZE)
		                .addGap(0, 660, Short.MAX_VALUE))))
		    .addComponent(jProgressBarPath, GroupLayout.Alignment.LEADING, 0, 1023, Short.MAX_VALUE))
		.addContainerGap());
	jPanPathLayout.setVerticalGroup(jPanPathLayout.createSequentialGroup()
			.addContainerGap()
			.addGroup(jPanPathLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
			    .addComponent(jLabPathPath, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 18, GroupLayout.PREFERRED_SIZE)
			    .addComponent(jLabInputReviewPath, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
			.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
			.addGroup(jPanPathLayout.createParallelGroup()
			    .addGroup(GroupLayout.Alignment.LEADING, jPanPathLayout.createSequentialGroup()
			        .addComponent(jTxtFilePathPath, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
			        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
			        .addComponent(jBtnFileOpenPath, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
			        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
			        .addComponent(jLabPathQtaxID, GroupLayout.PREFERRED_SIZE, 18, GroupLayout.PREFERRED_SIZE)
			        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
			        .addComponent(jCombSelSpePath, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
			        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 41, GroupLayout.PREFERRED_SIZE))
			    .addComponent(jScrollPaneInputPath, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 192, GroupLayout.PREFERRED_SIZE))
			.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
			.addComponent(jLabResultReviewPath, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
			.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
			.addGroup(jPanPathLayout.createParallelGroup()
			    .addComponent(jTabbedPanePathResult, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 331, GroupLayout.PREFERRED_SIZE)
			    .addGroup(GroupLayout.Alignment.LEADING, jPanPathLayout.createSequentialGroup()
			        .addGap(10)
			        .addGroup(jPanPathLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
			            .addComponent(jTxtAccColPath, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
			            .addComponent(jLabAccColPath, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
			        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 41, GroupLayout.PREFERRED_SIZE)
			        .addComponent(jCombBlastTaxPath, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
			        .addGap(39)
			        .addComponent(jChkBlastPath, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
			        .addGap(24)
			        .addComponent(jBtbSavePath, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
			        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)))
			.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
			.addComponent(jProgressBarPath, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)
			.addGap(0, 6, Short.MAX_VALUE));
	}
	/**
	 * 查看文件的鼠标或键盘事件响应时调用
	 */
	private void setPathProview(String filePath) {
		ExcelOperate excelOperate = new ExcelOperate();
		excelOperate.openExcel(filePath);
		ArrayList<String[]> PathRawData = excelOperate.ReadLsExcel(1, 1, excelOperate.getRowCount(), excelOperate.getColCount());
		jScrollPaneInputPath.setItemLs(PathRawData);
	}
	
}
