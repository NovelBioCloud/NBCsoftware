package com.novelbio.nbcgui.GUI.volcanoPlot;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JTextField;

import com.novelbio.base.dataOperate.ExcelTxtRead;
import com.novelbio.base.gui.GUIFileOpen;
import com.novelbio.base.gui.JComboBoxData;
import com.novelbio.base.gui.JScrollPaneData;
import com.novelbio.base.plot.PlotScatter;
import com.novelbio.base.plot.Volcano;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuiVolcanoPlot extends JPanel {
	private JTextField txtFCborder;
	private JTextField txtPValue;
	GUIFileOpen guiFileOpen = new GUIFileOpen();
	String inFileString;
	JScrollPaneData scrollPaneInFile;
	List<List<String>> lsInfo;
	JComboBoxData<Integer> comboBoxLogFC;
	JComboBoxData<Integer> comboBoxP_Value;
	/**
	 * Create the panel.
	 */
	public GuiVolcanoPlot() {
		setLayout(null);
		
		JLabel lblTitle = new JLabel("VolcanoPlot");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Dialog", Font.BOLD, 20));
		lblTitle.setBounds(356, 62, 360, 31);
		add(lblTitle);
		
		JButton btnInfile = new JButton("InFile");
		btnInfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String inFileString = guiFileOpen.openFileName("", "");
				lsInfo = ExcelTxtRead.readLsExcelTxtls(inFileString, 0);
				List<String> lsTitle = lsInfo.get(0);
				Map<String, Integer> mapColName2Value = new HashMap<String, Integer>();
				int i = 1;
				for (String string : lsTitle) {
					mapColName2Value.put(string, i);
					i++;
				}
				scrollPaneInFile.setItemLsLs(lsInfo);
				comboBoxLogFC.setMapItem(mapColName2Value);
				comboBoxP_Value.setMapItem(mapColName2Value);
			}
		});
		btnInfile.setBounds(65, 116, 107, 25);
		add(btnInfile);
		
		scrollPaneInFile = new JScrollPaneData();
		scrollPaneInFile.setBounds(184, 116, 825, 468);
		add(scrollPaneInFile);
		
		comboBoxLogFC = new JComboBoxData<Integer>();
		comboBoxLogFC.setBounds(65, 219, 107, 24);
		add(comboBoxLogFC);
		
		comboBoxP_Value = new JComboBoxData<Integer>();
		comboBoxP_Value.setBounds(65, 277, 107, 24);
		add(comboBoxP_Value);
		
		JLabel lblSelectLogfcLines = new JLabel("LogFc");
		lblSelectLogfcLines.setFont(new Font("Dialog", Font.BOLD, 15));
		lblSelectLogfcLines.setBounds(65, 193, 107, 15);
		add(lblSelectLogfcLines);
		
		JLabel lblPvalue = new JLabel("P-Value/FDR");
		lblPvalue.setFont(new Font("Dialog", Font.BOLD, 15));
		lblPvalue.setBounds(65, 255, 107, 15);
		add(lblPvalue);
		
		txtFCborder = new JTextField();
		txtFCborder.setText("1");
		txtFCborder.setBounds(65, 351, 107, 19);
		add(txtFCborder);
		txtFCborder.setColumns(10);
		
		JLabel lblLogfcborder = new JLabel("LogFcBorder");
		lblLogfcborder.setFont(new Font("Dialog", Font.BOLD, 15));
		lblLogfcborder.setBounds(65, 313, 107, 15);
		add(lblLogfcborder);
		
		JLabel lblPvalueborder = new JLabel("P-ValueBorder");
		lblPvalueborder.setFont(new Font("Dialog", Font.BOLD, 15));
		lblPvalueborder.setBounds(65, 382, 122, 15);
		add(lblPvalueborder);
		
		txtPValue = new JTextField();
		txtPValue.setColumns(10);
		txtPValue.setText("1.5");
		txtPValue.setBounds(65, 409, 107, 19);
		add(txtPValue);
		
		JButton btnDraw = new JButton("Draw");
		btnDraw.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				String outImage = FileOperate.changeFilePrefix(inFileString, "_out", "png");
				Volcano volcano = new Volcano();
				int LogFCColNum = comboBoxLogFC.getSelectedValue();
				
				int P_ValueColNum = comboBoxP_Value.getSelectedValue();
				volcano.setLogFC2Pvalue(lsInfo, comboBoxLogFC.getSelectedValue(), comboBoxP_Value.getSelectedValue());
				volcano.setLogFCBorder(Double.parseDouble(txtFCborder.getText()));
				volcano.setLogPvalueBorder(Double.parseDouble(txtPValue.getText()));
				PlotScatter plotScatter = volcano.drawVolimage(comboBoxP_Value.getSelectedItem().toString());
				GuiVolcanoPlotShow guiVolcanoPlotShow = new GuiVolcanoPlotShow(plotScatter); 
			}
		});
		btnDraw.setBounds(65, 440, 107, 25);
		add(btnDraw);

	}
}
