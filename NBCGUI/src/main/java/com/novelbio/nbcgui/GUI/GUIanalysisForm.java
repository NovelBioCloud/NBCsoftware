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
public class GUIanalysisForm extends javax.swing.JFrame {
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
	private GuiRNAautoSpliceSimple guiRNAautoSpliceSimple;
	private GuiUpdateDB guiUpdateDB;
	private GuiVolcanoPlot guiVolcanoPlot;
	private GuiGeneNetWork guiGeneNetWork;
	private GuilncLocation guilncLocation;
	private GuiGoMultiJPanel guiGoMultiJPanel;
	private GuiMotif guiMotif;
	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				GUIanalysisForm inst = new GUIanalysisForm();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
				inst.setTitle("NovelBio Analysis Platform");
				Image im = Toolkit.getDefaultToolkit().getImage("/home/zong0jie/desktop/logo.png");
				inst.setIconImage(im);
				inst.setResizable(false); 
			}
		});
	}
	
	public GUIanalysisForm() {
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
				
				guiBlastJpanel = new GuiBlastJpanel();
				jTabbedPane1.addTab("Query", null, guiBlastJpanel, null);
				
				guiAnnoGene = new GuiAnnoGene();
				jTabbedPane1.addTab("GeneAnno", null, guiAnnoGene, null);
				
				guiGoJPanel= new GuiGoJPanel();
				jTabbedPane1.addTab("GO", null, guiGoJPanel, null);
				
				guiPathJpanel = new GuiPathJpanel();
				jTabbedPane1.addTab("Path", null, guiPathJpanel, null);
				
				guiGoMultiJPanel = new GuiGoMultiJPanel();
				jTabbedPane1.addTab("GO_Path", guiGoMultiJPanel);
				
				guiKegArrayDownload = new GuiKegArrayDownload();
				jTabbedPane1.addTab("KegArrayDownload", guiKegArrayDownload);
				
				guiCelNormJpanel = new GuiAffyCelNormJpanel();
				jTabbedPane1.addTab("AffyCelNormalization", null, guiCelNormJpanel, null);
				
				guiDifGene = new GuiDifGeneJpanel();
				jTabbedPane1.addTab("DifGene", null, guiDifGene, null);
				
				guiVolcanoPlot = new GuiVolcanoPlot();
				jTabbedPane1.addTab("Volcano", guiVolcanoPlot);
				
				guiAnnoPeak = new GuiAnnoPeak();
				jTabbedPane1.addTab("PeakAnno", null, guiAnnoPeak, null);
				
				guilncLocation = new GuilncLocation();
				jTabbedPane1.addTab("LncLocation", guilncLocation);
				
				guiPeakStatistics = new GuiPeakStatistics();
				jTabbedPane1.addTab("PeakStatistics", guiPeakStatistics);
				
				guiSrcToTrg = new GuiSrcToTrgJpanel();
				jTabbedPane1.addTab("GeneAct", null, guiSrcToTrg, null);
			
				guiGeneNetWork = new GuiGeneNetWork();
				jTabbedPane1.addTab("NCBIinteract", null, guiGeneNetWork, null);

				guiPearson = new GuiPearsonJpanel();
				jTabbedPane1.addTab("CoExp", null, guiPearson, null);
				
				guiDegreeAdd = new GuiDegreeAddJpanel();
				jTabbedPane1.addTab("Degree", null, guiDegreeAdd, null);
				
				guiFastQ = new GuiFastQJpanel();
				jTabbedPane1.addTab("FastQ", null, guiFastQ, null);
				
				guiSamToBed = new GuiSamToBed();
				jTabbedPane1.addTab("SamToBed", null, guiSamToBed, null);
				
				guiDGEgetvalue = new GuiDGEgetvalue();
				jTabbedPane1.addTab("DGEgetValue", null, guiDGEgetvalue, null);
				
				guiRNASeqMapping = new GuiRNASeqMapping();
				jTabbedPane1.addTab("RNAseqMap", guiRNASeqMapping);
				
				guiTranscriptomeCufflinks = new GuiTranscriptomeCufflinks();
				jTabbedPane1.addTab("cufflinks", guiTranscriptomeCufflinks);
				
				guiCuffdiff = new GuiCuffdiff();
				jTabbedPane1.addTab("CuffDiff", null, guiCuffdiff, null);
				
				guiSamStatistics = new GuiSamStatistics();
				jTabbedPane1.addTab("SamStatisticsAndRPKM", guiSamStatistics);
				
				guiRNAautoSplice = new GuiRNAautoSplice();
				jTabbedPane1.addTab("RNAautoSplice", guiRNAautoSplice);
				
				guiBlast = new GuiBlast();
				jTabbedPane1.addTab("Blast", null, guiBlast, null);
				
				guiMiRNASeq = new GuiMiRNASeq();
				jTabbedPane1.addTab("miRNA", null, guiMiRNASeq, null);
				
				guiMirnaTargetPredict = new GuiMirnaTargetPredict();
				jTabbedPane1.addTab("miRNAtarget", null, guiMirnaTargetPredict, null);
				
				guiGetSeq = new GuiGetSeq();
				jTabbedPane1.add("GetSeq", guiGetSeq);
				
				guiBedTssAndChrome = new GuiBedTssAndChrome();
				jTabbedPane1.add("Tss", guiBedTssAndChrome);
				
				guiSnpCalling = new GuiSnpCalling();
				jTabbedPane1.add("SnpCalling", guiSnpCalling);
				
				guiTools = new GuiToolsJpanel();
				jTabbedPane1.addTab("Tools", null, guiTools, null);
				
				guiFilterDifGene = new GuiFilterDifGene();
				jTabbedPane1.addTab("filterGene", null, guiFilterDifGene, null);
				
				guiPeakCalling = new GuiPeakCalling();
				jTabbedPane1.addTab("PeakCalling", guiPeakCalling);
				
				guiMotif = new GuiMotif();
				jTabbedPane1.addTab("MotifScan", guiMotif);
				
				guiSpeciesInfo = new GuiSpeciesInfo();
				jTabbedPane1.addTab("SpeciesInfo", guiSpeciesInfo);
				
				guiSnpFiltering = new GuiSnpFiltering();
				jTabbedPane1.addTab("snpFilter", guiSnpFiltering);
				
				guiSnpFilterSimple = new GuiSnpFilterSimple();
				jTabbedPane1.addTab("snpFilterSimple", guiSnpFilterSimple);
				
				guiRNAautoSpliceSimple = new GuiRNAautoSpliceSimple();
				jTabbedPane1.addTab("ASD", guiRNAautoSpliceSimple);
				
				guiUpdateDB = new GuiUpdateDB();
				jTabbedPane1.addTab("UpdateDB", guiUpdateDB);
			}
			pack();
			this.setSize(1150, 750);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
