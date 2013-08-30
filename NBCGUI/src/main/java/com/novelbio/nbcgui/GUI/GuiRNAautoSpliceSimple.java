package com.novelbio.nbcgui.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import com.novelbio.analysis.seq.fasta.SeqHash;
import com.novelbio.analysis.seq.genome.gffOperate.GffHashGene;
import com.novelbio.analysis.seq.genome.gffOperate.GffType;
import com.novelbio.analysis.seq.rnaseq.CtrlSplicing;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.gui.GUIFileOpen;
import com.novelbio.base.gui.JComboBoxData;
import com.novelbio.base.gui.JScrollPaneData;
import com.novelbio.base.multithread.RunProcess;
import com.novelbio.nbcgui.GUIinfo;
import com.novelbio.nbcgui.GuiAnnoInfo;

public class GuiRNAautoSpliceSimple extends JPanel implements GUIinfo {
	static final int progressLength = 10000;
	private JTextField txtGff;
	JScrollPaneData scrlBam;
	JScrollPaneData scrlCompare;
	JButton btnOpeanbam;
	JButton btnDelbam;
	JButton btnRun;
	JButton btnOpengtf;
	JCheckBox chckbxDisplayAllSplicing;
	JCheckBox chckbxReconstructIso;
	GUIFileOpen guiFileOpen = new GUIFileOpen();
	private JTextField txtSaveTo;
	
	JProgressBar progressBar;
	JLabel lblInformation;
	JLabel lblDetailInfo;
	
	CtrlSplicing ctrlSplicing = new CtrlSplicing();
	
	/** 设定bar的分级<br>
	 * 现在是3级<br>
	 * 就是读取junction 1级<br>
	 * 读取表达1级<br>
	 * 计算差异1级<br>
	 */
	List<Double> lsProgressBarLevel = new ArrayList<Double>();
	long startBarNum;
	long endBarNum;
	int level;
	
	JComboBoxData<String> cmbGroup = new JComboBoxData<String>();
	private JTextField txtChromFaPath;
	private JCheckBox chckbxLowMemoryUse;
	private JLabel lblNewLabel;
	
	/**
	 * Create the panel.
	 */
	public GuiRNAautoSpliceSimple() {
		setLayout(null);

		scrlBam = new JScrollPaneData();
		scrlBam.setBounds(20, 34, 610, 296);
		add(scrlBam);
		
		btnOpeanbam = new JButton("AddBam");
		btnOpeanbam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> lsFile = guiFileOpen.openLsFileName("BamFile", "");
				ArrayList<String[]> lsInfo = new ArrayList<String[]>();
				for (String string : lsFile) {
					//0: fileName   1: sampleName 2:group
					String[] tmResult = new String[3];
					tmResult[0] = string; tmResult[1] = FileOperate.getFileNameSep(string)[0].split("_")[0];
					tmResult[2] = tmResult[1];
					lsInfo.add(tmResult);
				}
				scrlBam.addItemLs(lsInfo);
			}
		});
		btnOpeanbam.setBounds(20, 342, 115, 24);
		add(btnOpeanbam);
		
		btnDelbam = new JButton("DelBam");
		btnDelbam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scrlBam.deleteSelRows();
			}
		});
		btnDelbam.setBounds(515, 342, 115, 24);
		add(btnDelbam);
		
		txtGff = new JTextField();
		txtGff.setBounds(642, 55, 258, 18);
		add(txtGff);
		txtGff.setColumns(10);
		
		btnRun = new JButton("runPASH");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				run();
			}
		});
		btnRun.setBounds(771, 409, 129, 63);
		add(btnRun);
		
		btnOpengtf = new JButton("OpenGTF");
		btnOpengtf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtGff.setText(guiFileOpen.openFileName("GTFfile", ""));
			}
		});
		btnOpengtf.setBounds(758, 81, 142, 24);
		add(btnOpengtf);
		
		JLabel lblAddbamfile = new JLabel("BamFiles");
		lblAddbamfile.setBounds(20, 12, 129, 14);
		add(lblAddbamfile);
		
		txtSaveTo = new JTextField();
		txtSaveTo.setBounds(20, 409, 532, 18);
		add(txtSaveTo);
		txtSaveTo.setColumns(10);
		
		JButton btnSaveto = new JButton("SaveTo");
		btnSaveto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtSaveTo.setText(guiFileOpen.saveFileNameAndPath("", ""));
			}
		});
		btnSaveto.setBounds(583, 406, 140, 24);
		add(btnSaveto);
		
		scrlCompare = new JScrollPaneData();
		scrlCompare.setBounds(642, 145, 260, 148);
		add(scrlCompare);
		
		JLabel lblCompare = new JLabel("Comparison");
		lblCompare.setBounds(642, 127, 92, 14);
		add(lblCompare);
		
		JButton btnAddCompare = new JButton("AddCmp");
		btnAddCompare.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scrlCompare.addItem(new String[]{"",""});
			}
		});
		btnAddCompare.setBounds(642, 306, 115, 24);
		add(btnAddCompare);
		
		JButton btnDeleteCompare = new JButton("DelCmp");
		btnDeleteCompare.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scrlCompare.deleteSelRows();
			}
		});
		btnDeleteCompare.setBounds(782, 305, 118, 24);
		add(btnDeleteCompare);
		
		chckbxDisplayAllSplicing = new JCheckBox("Display All Splicing Events");
		chckbxDisplayAllSplicing.setBounds(20, 379, 229, 22);
		add(chckbxDisplayAllSplicing);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(18, 511, 882, 14);
		add(progressBar);
		
		lblInformation = new JLabel("");
		lblInformation.setBounds(18, 484, 217, 14);
		add(lblInformation);
		
		lblDetailInfo = new JLabel("");
		lblDetailInfo.setBounds(253, 484, 496, 14);
		add(lblDetailInfo);
		
		txtChromFaPath = new JTextField();
		txtChromFaPath.setBounds(20, 454, 532, 18);
		add(txtChromFaPath);
		txtChromFaPath.setColumns(10);
		
		JButton btnOpenSeqPath = new JButton("OpenSeqPath");
		btnOpenSeqPath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtChromFaPath.setText(guiFileOpen.openFilePathName("", ""));
			}
		});
		btnOpenSeqPath.setBounds(583, 451, 140, 24);
		add(btnOpenSeqPath);
		
		chckbxLowMemoryUse = new JCheckBox("Low Memory (Note: spend more time)");
		chckbxLowMemoryUse.setBounds(253, 379, 429, 22);
		add(chckbxLowMemoryUse);
		
		JLabel lblOptiontoFetchSeq = new JLabel("Option:To Fetch Seq");
		lblOptiontoFetchSeq.setBounds(20, 439, 164, 14);
		add(lblOptiontoFetchSeq);
		
		lblNewLabel = new JLabel("Annotation");
		lblNewLabel.setBounds(642, 35, 115, 14);
		add(lblNewLabel);
		
		chckbxReconstructIso = new JCheckBox("reconstructISO--slow");
		chckbxReconstructIso.setBounds(180, 341, 185, 26);
		add(chckbxReconstructIso);
		
		initial();
	}
	private void initial() {
//		combSpecies.setMapItem(Species.getSpeciesName2Species(Species.SEQINFO_SPECIES));
		selectSpecies();
		scrlBam.setTitle(new String[]{"BamFile", "Prefix", "group"});

		cmbGroup.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				changeSclCompareGroup();
			}
		});
		
		scrlCompare.setTitle(new String[] {"group1", "group2"});
		scrlCompare.setItem(0, cmbGroup);
		scrlCompare.setItem(1, cmbGroup);
		
		progressBar.setMinimum(0);
		progressBar.setMaximum(progressLength);
	}
	private void selectSpecies() {
//		Species species = combSpecies.getSelectedValue();
//		combVersion.setMapItem(species.getMapVersion());
	}
	/** 如果txt存在，优先获得txt对应的gtf文件*/
	private GffHashGene getGffhashGene() {
		GffHashGene gffHashGeneResult = new GffHashGene();
		gffHashGeneResult.setGffInfo(GffType.GTF, txtGff.getText());
		gffHashGeneResult.run();
		return gffHashGeneResult;
	}

	private void run() {
		progressBar.setValue(progressBar.getMinimum());
		ctrlSplicing.setGuiRNAautoSplice(this);
		GffHashGene gffHashGene = getGffhashGene();
		if (gffHashGene.isFinished()) {
			setInfo("Finished Reading GTF File");
		} else {
			setInfo(" PASH Is Interrupted Because Reading GTF File Encounters Error");
			return;
		}
			
		ctrlSplicing.setGffHashGene(getGffhashGene());
		try {
			SeqHash seqHash = new SeqHash(txtChromFaPath.getText());
			ctrlSplicing.setSeqHash(seqHash);
		} catch (Exception e) { }

		ctrlSplicing.setDisplayAllEvent(chckbxDisplayAllSplicing.isSelected());
		String outFile = txtSaveTo.getText();
		ctrlSplicing.setOutFile(outFile);
		ctrlSplicing.setLsBam2Prefix(scrlBam.getLsDataInfo());
		ctrlSplicing.setLsCompareGroup(scrlCompare.getLsDataInfo());
		ctrlSplicing.setMemoryLow(chckbxLowMemoryUse.isSelected());
		ctrlSplicing.setReconstructIso(chckbxReconstructIso.isSelected());
		//TODO
		btnRun.setEnabled(false);
		Thread thread = new Thread(ctrlSplicing);
		thread.start();

	}
	public void setProgressBarLevelLs(List<Double> lsProgressBarLevel) {
		this.lsProgressBarLevel = lsProgressBarLevel;
	}
	/**
	 * 设定本次步骤里面将绘制progressBar的第几部分
	 * 并且本部分的最短点和最长点分别是什么
	 * @param information gui上显示的文本信息
	 * @param level 本次步骤里面将绘制progressBar的第几部分，也就是跑到第几步了。总共3步
	 * @param startBarNum 本步骤起点，一般为0
	 * @param endBarNum 本步骤终点
	 */
	public void setProcessBarStartEndBarNum(String information, int level, long startBarNum, long endBarNum) {
		this.level = level;
		this.startBarNum = startBarNum;
		this.endBarNum = endBarNum;
		this.lblInformation.setText(information);
	}
	private void setProcessBarValue(long number) {
		long progressNum = number - startBarNum;
		if (progressNum < 0) {
			progressNum = 0;
		} else if (progressNum > endBarNum - startBarNum) {
			progressNum = endBarNum - startBarNum;
		}
		double finalNum = (double)progressNum/(endBarNum - startBarNum);
		int progressBarNum = (int) finalNum;
		
		double startProgress = 0, endProgress = lsProgressBarLevel.get(level);
		if (level != 0) {
			startProgress = lsProgressBarLevel.get(level - 1);
		}
		int num = (int) ((endProgress - startProgress) * progressLength * finalNum + startProgress * progressLength);
		progressBar.setValue(num);
	}
	public void setInfo(String info) {
		this.lblInformation.setText(info);
	}
	
	public void setDetailInfo(String info) {
		this.lblDetailInfo.setText(info);
	}
	
	public void setRunningInfo(GuiAnnoInfo info) {
		setProcessBarValue((long) info.getNumDouble());
		setDetailInfo(info.getInfo());
	}
	public void done(RunProcess<GuiAnnoInfo> runProcess) {
		btnRun.setEnabled(true);
		progressBar.setValue(progressBar.getMaximum());
	}
	public void threadSuspended(RunProcess<GuiAnnoInfo> runProcess) {
		// TODO Auto-generated method stub
		
	}
	public void threadResumed(RunProcess<GuiAnnoInfo> runProcess) {
		// TODO Auto-generated method stub
		
	}
	public void threadStop(RunProcess<GuiAnnoInfo> runProcess) {
		btnRun.setEnabled(true);
	}
	public void setMessage(String string) {
		JOptionPane.showMessageDialog(null, string, "Thanks using ASD", JOptionPane.INFORMATION_MESSAGE);
	}
	
	private void changeSclCompareGroup() {
		ArrayList<String[]> lsSnp2Prefix = scrlBam.getLsDataInfo();
		Map<String, String> mapString2Value = new HashMap<String, String>();
		for (String[] snp2prefix : lsSnp2Prefix) {
			mapString2Value.put(snp2prefix[2], snp2prefix[2]);
		}
		cmbGroup.setMapItem(mapString2Value);
	}
}
