package com.novelbio.nbcgui.GUI;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTextField;

import com.novelbio.base.dataOperate.ExcelTxtRead;
import com.novelbio.base.dataOperate.TxtReadandWrite;
import com.novelbio.base.gui.GUIFileOpen;
import com.novelbio.base.gui.JScrollPaneData;
import com.novelbio.database.model.species.Species;
import com.novelbio.nbcgui.controlquery.CtrlPeakStatistics;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JProgressBar;

/**
 * 批量注释，各种注释
 * @author zong0jie
 *
 */
public class GuiPeakStatistics extends JPanel {
	private static final long serialVersionUID = -4438216387830519443L;
	private JTextField txtColChrID;
	private JTextField txtColPeakStartMid;
	JCheckBox chckSetColSummit;
	JLabel lblPeakstartcolumn;
	JProgressBar progressBar;
	JScrollPaneData scrollPaneData;
	JButton btnSave;
	private JScrollPaneData scrollPaneDataResult;
	
	GUIFileOpen guiFileOpen = new GUIFileOpen();
	
	CtrlPeakStatistics ctrlPeakStatistics;
	private JButton btnRun;
	
	ArrayList<String[]> lsGeneInfo;
	private JTextField txtTssUp;
	private JTextField txtTssDown;
	private JTextField txtTesUp;
	private JTextField txtTesDown;
	
	String readFile = "";
	
	GuiLayeredPaneSpeciesVersionGff layeredPane;
	/**
	 * Create the panel.
	 */
	public GuiPeakStatistics() {
		setLayout(null);
		
		scrollPaneData = new JScrollPaneData();
		scrollPaneData.setBounds(12, 30, 693, 207);
		add(scrollPaneData);
		
		JButton btnOpenfile = new JButton("BamSamBedFile");
		btnOpenfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				readFile = guiFileOpen.openFileName("excel/txt", "");
				lsGeneInfo = ExcelTxtRead.readLsExcelTxtFile(readFile, 1, 1, 50, -1);
				scrollPaneData.setItemLs(lsGeneInfo);
				btnRun.setEnabled(true);
				btnSave.setEnabled(true);
			}
		});
		btnOpenfile.setBounds(717, 30, 131, 24);
		add(btnOpenfile);
		
		chckSetColSummit = new JCheckBox("SetColSummit");
		chckSetColSummit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectChckPeakRangeAnno(chckSetColSummit.isSelected());
			}
		});
		chckSetColSummit.setBounds(723, 262, 131, 22);
		add(chckSetColSummit);
		
		txtColChrID = new JTextField();
		txtColChrID.setBounds(723, 319, 114, 18);
		add(txtColChrID);
		txtColChrID.setColumns(10);
		
		JLabel lblChridcolumn = new JLabel("ChrIDColumn");
		lblChridcolumn.setBounds(723, 293, 114, 14);
		add(lblChridcolumn);
		
		txtColPeakStartMid = new JTextField();
		txtColPeakStartMid.setBounds(723, 368, 114, 18);
		add(txtColPeakStartMid);
		txtColPeakStartMid.setColumns(10);
		
		lblPeakstartcolumn = new JLabel("PeakSummitColumn");
		lblPeakstartcolumn.setBounds(723, 351, 157, 14);
		add(lblPeakstartcolumn);
		
		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String fileName = guiFileOpen.saveFileName("txt", "");
				TxtReadandWrite txtOut = new TxtReadandWrite(fileName, true);
				txtOut.ExcelWrite(ctrlPeakStatistics.getResult());
			}
		});
		btnSave.setBounds(717, 561, 118, 24);
		add(btnSave);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(12, 571, 693, 14);
		add(progressBar);
		
		scrollPaneDataResult = new JScrollPaneData();
		scrollPaneDataResult.setBounds(12, 262, 693, 290);
		add(scrollPaneDataResult);
		
		btnRun = new JButton("Run");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				progressBar.setMinimum(0);
				progressBar.setValue(0);
				
				btnRun.setEnabled(false);
				ctrlPeakStatistics.setQueryFile(readFile);

				try { ctrlPeakStatistics.setColPeakSummit(Integer.parseInt(txtColPeakStartMid.getText()));
				} catch (Exception e2) { }
				try { ctrlPeakStatistics.setColChrID(Integer.parseInt(txtColChrID.getText()));
				} catch (Exception e2) { }
				
 				int[] tss = new int[]{0,0};
				int[] tes = new int[]{0,0};
				try { tss[0] = Integer.parseInt(txtTssUp.getText()); } catch (Exception e2) { }
				try { tss[1] = Integer.parseInt(txtTssDown.getText()); } catch (Exception e2) { }
				try { tes[0] = Integer.parseInt(txtTesUp.getText()); } catch (Exception e2) { }
				try { tes[1] = Integer.parseInt(txtTesDown.getText()); } catch (Exception e2) { }
				
				ctrlPeakStatistics.setTssRange(tss);
				ctrlPeakStatistics.setTesRange(tes);
				
				Species species = layeredPane.getSelectSpecies();
				
				ctrlPeakStatistics.setSpecies(species);
				ctrlPeakStatistics.execute();
				btnSave.setEnabled(false);
			}
		});
		btnRun.setBounds(717, 525, 118, 24);
		add(btnRun);
		
		txtTssUp = new JTextField();
		txtTssUp.setBounds(720, 420, 52, 18);
		add(txtTssUp);
		txtTssUp.setColumns(10);
		
		txtTssDown = new JTextField();
		txtTssDown.setBounds(784, 420, 52, 18);
		add(txtTssDown);
		txtTssDown.setColumns(10);
		
		JLabel lblTss = new JLabel("Tss");
		lblTss.setBounds(717, 394, 69, 14);
		add(lblTss);
		
		JLabel lblUp = new JLabel("Up");
		lblUp.setBounds(717, 403, 69, 14);
		add(lblUp);
		
		JLabel lblDown = new JLabel("Down");
		lblDown.setBounds(779, 403, 69, 14);
		add(lblDown);
		
		txtTesUp = new JTextField();
		txtTesUp.setBounds(721, 476, 52, 18);
		add(txtTesUp);
		txtTesUp.setColumns(10);
		
		JLabel lblUp_1 = new JLabel("Up");
		lblUp_1.setBounds(717, 461, 69, 14);
		add(lblUp_1);
		
		txtTesDown = new JTextField();
		txtTesDown.setBounds(784, 476, 52, 18);
		add(txtTesDown);
		txtTesDown.setColumns(10);
		
		JLabel lblDown_1 = new JLabel("Down");
		lblDown_1.setBounds(784, 461, 69, 14);
		add(lblDown_1);
		
		JLabel lblTes = new JLabel("Tes");
		lblTes.setBounds(717, 450, 69, 14);
		add(lblTes);
		
		layeredPane = new GuiLayeredPaneSpeciesVersionGff();
		layeredPane.setBounds(717, 66, 221, 154);
		add(layeredPane);
		
		initial();
	}
	
	private void initial() {
		ctrlPeakStatistics = new CtrlPeakStatistics(this);
		chckSetColSummit.setSelected(true);
		selectChckPeakRangeAnno(chckSetColSummit.isSelected());
		btnSave.setEnabled(false);
	}
	private void selectChckPeakRangeAnno(boolean isSelected) {
		if (isSelected) {
			lblPeakstartcolumn.setText("PeakStartColumn");
			txtColChrID.setEnabled(true);
			txtColPeakStartMid.setEnabled(true);
		}
		else {
			txtColChrID.setText("");
			txtColChrID.setEnabled(false);
			txtColPeakStartMid.setText("");
			txtColPeakStartMid.setEnabled(false);
		}
	}
	
	public JProgressBar getProcessBar() {
		return progressBar;
	}
	public JScrollPaneData getJScrollPaneDataResult() {
		return scrollPaneDataResult;
	}
	public JButton getBtnSave() {
		return btnSave;
	}
	public JButton getBtnRun() {
		return btnRun;
	}
}
