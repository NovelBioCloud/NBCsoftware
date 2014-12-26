package com.novelbio.nbcgui.GUI;
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.novelbio.base.PathDetail;
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
public class GUIanalysisFormLXJ extends javax.swing.JFrame {
	private static final long serialVersionUID = 6809702573230604814L;
	private JTabbedPane jTabbedPane1;
	private GuiGoJPanel guiGoJPanel;
	private GuiKegArrayDownload guiKegArrayDownload;
	private GuiPathJpanel guiPathJpanel;
	private GuiBlastJpanel guiBlastJpanel;
	private GuiSrcToTrgJpanel guiSrcToTrg;
	private GuiDegreeAddJpanel guiDegreeAdd;
	private GuiPearsonJpanel guiPearson;
	private GuiToolsJpanel guiTools;
	private GuiFastQJpanel guiFastQ;
	private GuiFastQFanwei guiFastQFanwei;
	private GuiDifGeneJpanel guiDifGene;
	private GuiBlast guiBlast;
	private GuiMiRNASeq guiMiRNASeq;
	private GuiMirnaTargetPredict guiMirnaTargetPredict;
	private GuiAnnoGene guiAnnoGene;
	private GuiAnnoPeak guiAnnoPeak;
	private GuiPeakStatistics guiPeakStatistics;
	private GuiGetSeq guiGetSeq;
	private GuiBedTssAndChrome guiBedTssAndChrome;
	private GuiRNASeqMapping guiRNASeqMapping;
	private GuiAffyCelNormJpanel guiCelNormJpanel;
	private GuiSamToBed guiSamToBed;
	private GuiSamToBam guiSamToBam;
	private GuiSnpCalling guiSnpCalling;
	private GuiTranscriptomeCufflinks guiTranscriptomeCufflinks;
	private GuiRNAautoSplice guiRNAautoSplice;
	private GuiDGEgetvalue guiDGEgetvalue;
	private GuiFilterDifGene guiFilterDifGene;
	private GuiCuffdiff guiCuffdiff;
	private GuiPeakCalling guiPeakCalling;
	private GuiSpeciesInfo guiSpeciesInfo;
	private GuiSamStatistics guiSamStatistics;
	private GuiSnpFiltering guiSnpFiltering;
	private GuiSnpFilterSimple guiSnpFilterSimple;
	private GuiRNAalterSpliceSimple guiRNAautoSpliceSimple;
	private GuiUpdateDB guiUpdateDB;
	private GuiVolcanoPlot guiVolcanoPlot;
	private GuiGeneNetWork guiGeneNetWork;
	private GuilncLocation guilncLocation;
	private GuiGoMultiJPanel guiGoMultiJPanel;
	private GuiMotif guiMotif;
	private GuiRNAAssembly guiRNAAssembly;

	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				GUIanalysisFormLXJ inst = new GUIanalysisFormLXJ();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
//				inst.setTitle("CASH v 1.0.12");
				inst.setTitle("RNA-Seq Analysis Platform");
				Image im = Toolkit.getDefaultToolkit().getImage("/home/zong0jie/desktop/logo.png");
				inst.setIconImage(im);
				inst.setResizable(false); 
			}
		});
	}
	
	public GUIanalysisFormLXJ() {
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
				
//				guiFastQFanwei = new GuiFastQFanwei();
//				jTabbedPane1.addTab("FilterAndMapping", null, guiFastQFanwei, null);
//				
//				guiRNASeqMapping = new GuiRNASeqMapping();
//				jTabbedPane1.addTab("RNAseqMap", guiRNASeqMapping);
//				
//				guiSamToBed = new GuiSamToBed();
//				jTabbedPane1.addTab("SamToBed", null, guiSamToBed, null);
//
//				guiTranscriptomeCufflinks = new GuiTranscriptomeCufflinks();
//				jTabbedPane1.addTab("cufflinks", guiTranscriptomeCufflinks);
				
				guiSamStatistics = new GuiSamStatistics();
				jTabbedPane1.addTab("SamStatisticsAndRPKM", guiSamStatistics);
				
//				guiCuffdiff = new GuiCuffdiff();
//				jTabbedPane1.addTab("CuffDiff", null, guiCuffdiff, null);
//				
//				guiDifGene = new GuiDifGeneJpanel();
//				jTabbedPane1.addTab("DifGene", null, guiDifGene, null);
//				
//				guiRNAautoSplice = new GuiRNAautoSplice();
//				jTabbedPane1.addTab("RNAalterSplice", guiRNAautoSplice);
//				
//				guiTools = new GuiToolsJpanel();
//				jTabbedPane1.addTab("Tools", null, guiTools, null);
				
			}
			pack();
			this.setSize(1150, 750);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
