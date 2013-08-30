package com.novelbio.nbcgui.GUI.volcanoPlot;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;

import com.novelbio.base.gui.GUIFileOpen;
import com.novelbio.base.plot.JpanelPlot;
import com.novelbio.base.plot.PlotScatter;
import com.novelbio.base.plot.Volcano;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GuiVolcanoPlotShow {

	private JFrame frame;
	private JTextField txtWeigth;
	private JTextField txtHeigth;
	GUIFileOpen guiFileOpen = new GUIFileOpen();

	/**
	 * Create the application.
	 */
	public GuiVolcanoPlotShow(PlotScatter plotScatter) {
		initialize(plotScatter);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(final PlotScatter plotScatter) {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JpanelPlot panel = new JpanelPlot();
		panel.setBounds(25, 12, 777, 631);
		frame.getContentPane().add(panel);
		panel.setPlotNBC(plotScatter);
		
		txtWeigth = new JTextField();
		txtWeigth.setBounds(104, 667, 114, 19);
		frame.getContentPane().add(txtWeigth);
		txtWeigth.setColumns(10);
		
		txtHeigth = new JTextField();
		txtHeigth.setBounds(320, 667, 114, 19);
		frame.getContentPane().add(txtHeigth);
		txtHeigth.setColumns(10);
		
		JLabel lblWeigth = new JLabel("weigth");
		lblWeigth.setBounds(25, 667, 61, 19);
		txtWeigth.setText("1024");
		frame.getContentPane().add(lblWeigth);
		
		JLabel lblNewLabel_1 = new JLabel("heigth");
		txtHeigth.setText("1024");
		lblNewLabel_1.setBounds(247, 668, 61, 17);
		frame.getContentPane().add(lblNewLabel_1);
		
		JButton btnSaveAs = new JButton("Save As");
		btnSaveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String outImageFile = guiFileOpen.openFileName("", "");
				plotScatter.saveToFile(outImageFile, Integer.parseInt(txtWeigth.getText()), Integer.parseInt(txtHeigth.getText()));
			}
		});
		btnSaveAs.setBounds(455, 664, 107, 25);
		frame.getContentPane().add(btnSaveAs);
		frame.setSize(1050, 955);
		frame.setVisible(true);
	}
}
