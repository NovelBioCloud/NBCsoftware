package com.novelbio.nbcgui.GUI;
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import javax.swing.SwingUtilities;

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
public class GUIanalysisFormOther extends javax.swing.JFrame {
	private static final long serialVersionUID = 6809702573230604814L;
	private JTabbedPane jTabbedPane1;
	private GuiSeqCope guiSeqCope;

	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				GUIanalysisFormOther inst = new GUIanalysisFormOther();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
				inst.setTitle("NovelBio Data Analysis Platform");
				Image im = Toolkit.getDefaultToolkit().getImage("/media/winE/NBC/advertise/宣传/LOGO/favicon.png");
				inst.setIconImage(im);
				inst.setResizable(false); 
			}
		});
	}
	
	public GUIanalysisFormOther() {
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
				
				guiSeqCope = new GuiSeqCope();
				jTabbedPane1.addTab("SeqCope", null, guiSeqCope, null);
				
			}
			pack();
			this.setSize(1150, 750);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
