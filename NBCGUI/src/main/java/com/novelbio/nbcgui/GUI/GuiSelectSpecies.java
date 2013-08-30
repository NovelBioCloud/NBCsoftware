package com.novelbio.nbcgui.GUI;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class GuiSelectSpecies extends JPanel {
	/**
	 * Create the panel.
	 */
	public GuiSelectSpecies() {
		setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(203, 15, 425, 272);
		add(scrollPane);

	}
}
