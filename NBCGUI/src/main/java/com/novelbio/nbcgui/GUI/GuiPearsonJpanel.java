package com.novelbio.nbcgui.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import com.novelbio.analysis.coexp.simpCoExp.CoExp;
import com.novelbio.base.dataOperate.ExcelOperate;
import com.novelbio.base.dataOperate.ExcelTxtRead;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.gui.GUIFileOpen;
import com.novelbio.base.gui.JComboBoxData;
import com.novelbio.base.gui.JScrollPaneData;
import com.novelbio.base.gui.JTextFieldData;
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
public class GuiPearsonJpanel extends JPanel{

	private JTabbedPane jTabbedPanePathResult;
	private JButton jBtbSavePath;
	private JProgressBar jProgressBarPath;
	private JLabel jLabResultReviewPath;
	private JLabel jLabInputReviewPath;
	private JLabel jLabPathPath;
	private JTextField jTxtFilePathPath;
	private JButton jBtnFileOpenPath;
	private JTextFieldData jTxtPvalueCufoff;
	private JLabel jLabPathQtaxID;
	private JScrollPaneData jScrollPaneInputPath;
	private JLabel lblPvaluecutoff;
	GUIFileOpen guiFileOpen = new GUIFileOpen();
	////////////	
	
	public GuiPearsonJpanel() 
	{
		

		setPreferredSize(new java.awt.Dimension(1046, 617));
	
		setAlignmentX(0.0f);
		setComponent();
		setLayout(null);
		add(jTxtFilePathPath);
		add(jLabPathQtaxID);
		add(jLabPathPath);
		add(jBtbSavePath);
		add(jTxtPvalueCufoff);
		add(jBtnFileOpenPath);
		add(jLabInputReviewPath);
		add(jScrollPaneInputPath);
		add(jTabbedPanePathResult);
		add(jLabResultReviewPath);
		add(jProgressBarPath);
		{
			lblPvaluecutoff = new JLabel("PvalueCutOff");
			lblPvaluecutoff.setBounds(14, 248, 129, 18);
			add(lblPvaluecutoff);
		}
	}
	private void setComponent() {
		{
			jLabPathQtaxID = new JLabel();
			jLabPathQtaxID.setBounds(14, 101, 129, 18);
			jLabPathQtaxID.setText("Query Species");
		}
		{
			jTxtFilePathPath = new JTextField();
			jTxtFilePathPath.setBounds(14, 38, 217, 24);
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
			jBtnFileOpenPath.setBounds(124, 69, 105, 25);
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
			jLabPathPath.setBounds(14, 13, 85, 18);
			jLabPathPath.setText("InputData");
		}
		{
			jTxtPvalueCufoff = new JTextFieldData();
			jTxtPvalueCufoff.setBounds(14, 277, 85, 20);
			jTxtPvalueCufoff.setNumOnly(5);
		}


		{
			jScrollPaneInputPath = new JScrollPaneData();
			jScrollPaneInputPath.setBounds(245, 38, 794, 192);
		}
		{
			jLabInputReviewPath = new JLabel();
			jLabInputReviewPath.setBounds(245, 13, 97, 18);
			jLabInputReviewPath.setText("InputReview");
		}
		{
			jLabResultReviewPath = new JLabel();
			jLabResultReviewPath.setBounds(252, 248, 127, 18);
			jLabResultReviewPath.setText("ResultReview");
		}
		{
			jProgressBarPath = new JProgressBar();
			jProgressBarPath.setBounds(14, 617, 1025, 14);
		}
		{
			jTabbedPanePathResult = new JTabbedPane();
			jTabbedPanePathResult.setBounds(245, 273, 794, 331);
		}
		{
			jBtbSavePath = new JButton();
			jBtbSavePath.setBounds(14, 310, 92, 23);
			jBtbSavePath.setText("Save As");
			jBtbSavePath.setMargin(new java.awt.Insets(1, 0, 1, 0));
			jBtbSavePath.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					String savefilename = guiFileOpen.saveFileName("xls", "xls");
					if (!FileOperate.getFileNameSep(savefilename)[1].equals("xls")) {
						savefilename = savefilename+".xls";
					}
					try {
						String inputFile = jTxtFilePathPath.getText();
						CoExp coExp = new CoExp();
						coExp.setPvalueCutoff( Double.parseDouble(jTxtPvalueCufoff.getText()));
						coExp.readTxtExcel(inputFile, null);
						coExp.writeToExcel(savefilename);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		initial();
		
	}
	
	private void initial() {
		jTxtPvalueCufoff.setText("0.05");
	}
	/**
	 * 查看文件的鼠标或键盘事件响应时调用
	 */
	private void setPathProview(String filePath) {
		ArrayList<String[]> PathRawData = ExcelTxtRead.readLsExcelTxt(filePath, 1);
		jScrollPaneInputPath.setItemLsWithTitle(PathRawData);
	}
}
