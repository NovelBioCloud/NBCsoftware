package com.novelbio.nbcgui.GUI;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.LayoutStyle;
import javax.swing.ListModel;

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
public class NewJFrame extends javax.swing.JFrame {
	private JTabbedPane jTabbedPane1;
	private JPanel jPanel1;
	private JButton jButton1;
	private JList jList1;
	private JComboBox jComboBox1;
	private JButton jButton2;
	private JPanel jPanel2;

	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				NewJFrame inst = new NewJFrame();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}
	
	public NewJFrame() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			GroupLayout thisLayout = new GroupLayout((JComponent)getContentPane());
			getContentPane().setLayout(thisLayout);
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			{
				jTabbedPane1 = new JTabbedPane();
				{
					jPanel1 = new JPanel();
					GroupLayout jPanel1Layout = new GroupLayout((JComponent)jPanel1);
					jPanel1.setLayout(jPanel1Layout);
					jTabbedPane1.addTab("jPanel1", null, jPanel1, null);
					{
						jButton1 = new JButton();
						jButton1.setText("jButton1");
					}
					{
						ComboBoxModel jComboBox1Model = 
							new DefaultComboBoxModel(
									new String[] { "Item One", "Item Two" });
						jComboBox1 = new JComboBox();
						jComboBox1.setModel(jComboBox1Model);
					}
					{
						ListModel jList1Model = 
							new DefaultComboBoxModel(
									new String[] { "Item One", "Item Two" });
						jList1 = new JList();
						jList1.setModel(jList1Model);
					}
					jPanel1Layout.setHorizontalGroup(jPanel1Layout.createSequentialGroup()
						.addContainerGap(39, 39)
						.addComponent(jButton1, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGap(165)
						.addComponent(jComboBox1, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGap(95)
						.addComponent(jList1, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(459, Short.MAX_VALUE));
					jPanel1Layout.setVerticalGroup(jPanel1Layout.createSequentialGroup()
						.addContainerGap(77, 77)
						.addComponent(jList1, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
						.addGap(0, 18, Short.MAX_VALUE)
						.addComponent(jComboBox1, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addComponent(jButton1, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(358, 358));
				}
				{
					jPanel2 = new JPanel();
					GroupLayout jPanel2Layout = new GroupLayout((JComponent)jPanel2);
					jPanel2.setLayout(jPanel2Layout);
					jTabbedPane1.addTab("jPanel2", null, jPanel2, null);
					{
						jButton2 = new JButton();
						jButton2.setText("jButton2");
					}
					jPanel2Layout.setHorizontalGroup(jPanel2Layout.createSequentialGroup()
						.addContainerGap(128, 128)
						.addComponent(jButton2, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(809, Short.MAX_VALUE));
					jPanel2Layout.setVerticalGroup(jPanel2Layout.createSequentialGroup()
						.addContainerGap(144, 144)
						.addComponent(jButton2, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(345, Short.MAX_VALUE));
				}
			}
			thisLayout.setVerticalGroup(thisLayout.createSequentialGroup()
				.addComponent(jTabbedPane1, 0, 536, Short.MAX_VALUE));
			thisLayout.setHorizontalGroup(thisLayout.createSequentialGroup()
				.addComponent(jTabbedPane1, 0, 1007, Short.MAX_VALUE));
			pack();
			this.setSize(1017, 566);
		} catch (Exception e) {
		    //add your error handling code here
			e.printStackTrace();
		}
	}

}
