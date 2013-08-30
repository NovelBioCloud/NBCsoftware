package com.novelbio.nbcgui.GUI;

import javax.swing.JPanel;
/**
 * 甲基化，ChIP-Seq，RNA-Seq等联合分析的东西
 * @author zong0jie
 *
 */
public class GuiReadsAndExpInfo extends JPanel {

	/**
	 * Create the panel.
	 */
	public GuiReadsAndExpInfo() {
		setLayout(null);
		
		GuiLayeredPaneSpeciesVersionGff layeredPane = new GuiLayeredPaneSpeciesVersionGff();
		layeredPane.setBounds(39, 25, 274, 176);
		add(layeredPane);
	}
}
