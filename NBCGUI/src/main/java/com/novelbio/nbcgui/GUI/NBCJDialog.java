package com.novelbio.nbcgui.GUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.text.AttributedCharacterIterator;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle;
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
public class NBCJDialog extends javax.swing.JDialog {
	private JButton jBtnOK;
	private JTextArea jTextArea;

	/**
	* Auto-generated main method to display this JDialog
	*/
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame();
				NBCJDialog inst = new NBCJDialog(frame);
				inst.setVisible(true);
				
			}
		});
	}
	
	public NBCJDialog(JFrame frame) {
		super(frame);
		initGUI();
		
	}
	
	private void initGUI() {
		try {
			
			GroupLayout thisLayout = new GroupLayout((JComponent)getContentPane());
			getContentPane().setLayout(thisLayout);
			this.setTitle("Aout NovelBio QueryDB");
			this.setResizable(false);
			{
				jBtnOK = new JButton();
				jBtnOK.setText("OK");
				jBtnOK.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						setVisible(false);
						
					}
				});
			}
			{
				jTextArea = new JTextArea();
				jTextArea.setEditable(false);
				String info = "\n         NovelBio Databae Query System\n" +
						           "                            Vision 1.0    \n\n";
				info = info + "                            Copyright:\n  Novel Bioinformatics co.,ltd  2010 -- 2011 \n" +
						           "                      All rights reserved\n\n" +
						           "          WebSite: http:\\\\www.novelbio.com\n" +
						           "                      Tel:021-6051 5125\n" +
						           "                 Email: tech@novelbio.com";
				
				jTextArea.setText(info);
				jTextArea.setDragEnabled(true);
				jTextArea.setFont(new java.awt.Font("DejaVu Serif",0,16));
				jTextArea.setLineWrap(true);
			}
			thisLayout.setVerticalGroup(thisLayout.createSequentialGroup()
				.addComponent(jTextArea, 0, 235, Short.MAX_VALUE)
				.addGap(17)
				.addComponent(jBtnOK, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				.addContainerGap());
			thisLayout.setHorizontalGroup(thisLayout.createParallelGroup()
				.addComponent(jTextArea, GroupLayout.Alignment.LEADING, 0, 381, Short.MAX_VALUE)
				.addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
				    .addGap(160)
				    .addComponent(jBtnOK, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE)
				    .addContainerGap(157, Short.MAX_VALUE)));
			this.setSize(389, 312);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

