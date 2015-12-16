package com.novelbio.nbcgui.GUI;
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.novelbio.nbcgui.GUI.volcanoPlot.GuiVolcanoPlot;

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
public class GUIanalysisCASH extends javax.swing.JFrame {
	private static final long serialVersionUID = 6809702573230604814L;
	private JTabbedPane jTabbedPane1;
	
	private GuiRNAalterSpliceSimple guiRNAalterSpliceSimple;


	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				GUIanalysisCASH inst = new GUIanalysisCASH();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
				inst.setTitle("CASH v 2.01");
//				inst.setTitle("AgroMarker Finder");
				Image im = Toolkit.getDefaultToolkit().getImage("/home/zong0jie/desktop/logo.png");
				inst.setIconImage(im);
				inst.setResizable(false); 
			}
		});
	}
	
	public GUIanalysisCASH() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			{
				
				jTabbedPane1 = new JTabbedPane();
				getContentPane().add(jTabbedPane1, BorderLayout.CENTER);
				jTabbedPane1.setPreferredSize(new java.awt.Dimension(1035, 682));

				
				guiRNAalterSpliceSimple = new GuiRNAalterSpliceSimple();
				jTabbedPane1.addTab("cash", guiRNAalterSpliceSimple);
				
			}
			pack();
			this.setSize(1150, 750);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
