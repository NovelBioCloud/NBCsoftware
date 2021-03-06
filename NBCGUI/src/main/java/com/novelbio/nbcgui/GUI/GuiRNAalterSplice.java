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

import com.novelbio.GuiAnnoInfo;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.gui.GUIFileOpen;
import com.novelbio.base.gui.JComboBoxData;
import com.novelbio.base.gui.JScrollPaneData;
import com.novelbio.base.multithread.RunProcess;
import com.novelbio.bioinfo.gff.GffHashGene;
import com.novelbio.bioinfo.gff.GffType;
import com.novelbio.bioinfo.gffchr.GffChrAbs;
import com.novelbio.bioinfo.sam.StrandSpecific;
import com.novelbio.database.domain.species.Species;
import com.novelbio.nbcgui.GUIinfo;
import com.novelbio.nbcgui.controlseq.CtrlSplicing;

public class GuiRNAalterSplice extends JPanel implements GUIinfo {
	static final int progressLength = 10000;
	private JTextField txtGff;
	JScrollPaneData scrlBam;
	JScrollPaneData scrlCompare;
	JButton btnOpeanbam;
	JButton btnDelbam;
	JButton btnRun;
	JButton btnOpengtf;
	JCheckBox chckbxDisplayAllSplicing;
	JCheckBox chkUseExternalGTF;
	JComboBoxData<StrandSpecific> cmbStrand;
	
	GUIFileOpen guiFileOpen = new GUIFileOpen();
	private JTextField txtSaveTo;
	
	JProgressBar progressBar;
	JLabel lblInformation;
	JLabel lblDetailInfo;
	
	CtrlSplicing ctrlSplicing = new CtrlSplicing();
	GuiLayeredPaneSpeciesVersionGff guiLayeredPaneSpeciesVersionGff;
	
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
	private JCheckBox chckbxReconstructIso;
	private JCheckBox chckConsiderRepeat;
	
	/**
	 * Create the panel.
	 */
	public GuiRNAalterSplice() {
		setLayout(null);

		scrlBam = new JScrollPaneData();
		scrlBam.setBounds(20, 34, 610, 303);
		add(scrlBam);
		
		btnOpeanbam = new JButton("OpeanBam");
		btnOpeanbam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> lsFile = guiFileOpen.openLsFileName("BamFile", "");
				ArrayList<String[]> lsInfo = new ArrayList<String[]>();
				for (String string : lsFile) {
					//0: fileName   1: sampleName 2:group
					String[] tmResult = new String[3];
					tmResult[0] = string; tmResult[1] = FileOperate.getFileNameSep(string)[0].split("_")[0];
//					tmResult[2] = tmResult[1];
					lsInfo.add(tmResult);
				}
				scrlBam.addItemLs(lsInfo);
			}
		});
		btnOpeanbam.setBounds(31, 349, 118, 24);
		add(btnOpeanbam);
		
		btnDelbam = new JButton("DelBam");
		btnDelbam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scrlBam.deleteSelRows();
			}
		});
		btnDelbam.setBounds(369, 349, 118, 24);
		add(btnDelbam);
		
		txtGff = new JTextField();
		txtGff.setEnabled(false);
		txtGff.setBounds(644, 170, 258, 18);
		add(txtGff);
		txtGff.setColumns(10);
		
		btnRun = new JButton("run");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				run();
			}
		});
		btnRun.setBounds(784, 427, 118, 24);
		add(btnRun);
		
		btnOpengtf = new JButton("OpenGTF");
		btnOpengtf.setEnabled(false);
		btnOpengtf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtGff.setText(guiFileOpen.openFileName("GTFfile", ""));
			}
		});
		btnOpengtf.setBounds(784, 190, 118, 24);
		add(btnOpengtf);
		
		JLabel lblAddbamfile = new JLabel("AddBamFile");
		lblAddbamfile.setBounds(20, 12, 129, 14);
		add(lblAddbamfile);
		
		txtSaveTo = new JTextField();
		txtSaveTo.setBounds(20, 430, 532, 18);
		add(txtSaveTo);
		txtSaveTo.setColumns(10);
		
		JButton btnSaveto = new JButton("SaveTo");
		btnSaveto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtSaveTo.setText(guiFileOpen.saveFileNameAndPath("", ""));
			}
		});
		btnSaveto.setBounds(604, 427, 118, 24);
		add(btnSaveto);
		
		scrlCompare = new JScrollPaneData();
		scrlCompare.setBounds(642, 291, 260, 76);
		add(scrlCompare);
		
		JLabel lblCompare = new JLabel("Compare");
		lblCompare.setBounds(642, 266, 69, 14);
		add(lblCompare);
		
		JButton btnAddCompare = new JButton("AddCompare");
		btnAddCompare.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scrlCompare.addItem(new String[]{"",""});
			}
		});
		btnAddCompare.setBounds(642, 379, 115, 24);
		add(btnAddCompare);
		
		JButton btnDeleteCompare = new JButton("DelCompare");
		btnDeleteCompare.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scrlCompare.deleteSelRows();
			}
		});
		btnDeleteCompare.setBounds(784, 379, 118, 24);
		add(btnDeleteCompare);
		
		chckbxDisplayAllSplicing = new JCheckBox("Display All Splicing Events");
		chckbxDisplayAllSplicing.setSelected(true);
		chckbxDisplayAllSplicing.setBounds(20, 400, 243, 22);
		add(chckbxDisplayAllSplicing);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(20, 487, 882, 14);
		add(progressBar);
		
		lblInformation = new JLabel("");
		lblInformation.setBounds(20, 460, 223, 14);
		add(lblInformation);
		
		lblDetailInfo = new JLabel("");
		lblDetailInfo.setBounds(255, 461, 467, 14);
		add(lblDetailInfo);
		
		guiLayeredPaneSpeciesVersionGff = new GuiLayeredPaneSpeciesVersionGff();
		guiLayeredPaneSpeciesVersionGff.setBounds(644, 34, 258, 130);
		add(guiLayeredPaneSpeciesVersionGff);
		
		chkUseExternalGTF = new JCheckBox("Use External GTF");
		chkUseExternalGTF.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (chkUseExternalGTF.isSelected()) {
					txtGff.setEnabled(true);
					btnOpengtf.setEnabled(true);
				} else {
					txtGff.setEnabled(false);
					btnOpengtf.setEnabled(false);
				}
			}
		});
		chkUseExternalGTF.setBounds(644, 12, 223, 27);
		add(chkUseExternalGTF);
		
		cmbStrand = new JComboBoxData<>();
		cmbStrand.setBounds(642, 226, 198, 24);
		add(cmbStrand);
		
		chckbxReconstructIso = new JCheckBox("reconstruct iso");
		chckbxReconstructIso.setSelected(true);
		chckbxReconstructIso.setBounds(295, 398, 180, 26);
		add(chckbxReconstructIso);
		
		chckConsiderRepeat = new JCheckBox("Consider Repeat");
		chckConsiderRepeat.setBounds(735, 260, 167, 26);
		add(chckConsiderRepeat);
		
		initial();
	}
	private void initial() {
//		combSpecies.setMapItem(Species.getSpeciesName2Species(Species.SEQINFO_SPECIES));
		selectSpecies();
		scrlBam.setTitle(new String[]{"BamFile", "group"});

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
		cmbStrand.setMapItem(StrandSpecific.getMapStrandLibrary());
	}
	private void selectSpecies() {
//		Species species = combSpecies.getSelectedValue();
//		combVersion.setMapItem(species.getMapVersion());
	}
	/** 如果txt存在，优先获得txt对应的gtf文件*/
	private GffHashGene getGffhashGene() {
		GffHashGene gffHashGeneResult = null;

		String gtfFile = txtGff.getText();
		if (chkUseExternalGTF.isSelected() && FileOperate.isFileExistAndNotDir(gtfFile)) {
			gffHashGeneResult = new GffHashGene(GffType.GTF, txtGff.getText());
		} else {
			Species species = guiLayeredPaneSpeciesVersionGff.getSelectSpecies();
			GffChrAbs gffChrAbs = new GffChrAbs(species);
			gffHashGeneResult = gffChrAbs.getGffHashGene();
			gffChrAbs.close();
		}
		return gffHashGeneResult;
	}

	private void run() {
		progressBar.setValue(progressBar.getMinimum());
		ctrlSplicing.setGuiRNAautoSplice(this);
		ctrlSplicing.setGffHashGene(getGffhashGene());
		ctrlSplicing.setDisplayAllEvent(chckbxDisplayAllSplicing.isSelected());
		String outFile = txtSaveTo.getText();
		ctrlSplicing.setOutFile(outFile);
		ctrlSplicing.setLsBam2Prefix(scrlBam.getLsDataInfo());
		ctrlSplicing.setReconstructIso(chckbxReconstructIso.isSelected());
		ctrlSplicing.setLsCompareGroup(scrlCompare.getLsDataInfo());
		ctrlSplicing.setMemoryLow(false);
		ctrlSplicing.setCombine(!chckConsiderRepeat.isSelected());
		//TODO
		btnRun.setEnabled(false);
		Thread thread = new Thread(ctrlSplicing);
		thread.setDaemon(true);
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
	public void setProcessBarStartEndBarNum(int level, long startBarNum, long endBarNum) {
		this.level = level;
		this.startBarNum = startBarNum;
		this.endBarNum = endBarNum;
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
	
	public void setRunningInfo(GuiAnnoInfo info) {
		if (info.getNumDouble() > 0) {
			setProcessBarValue((long) info.getNumDouble());
		}
		if (info.getInfo2() != null) {
			setInfo(info.getInfo2());
		}
		if(info.getInfo() != null) {
			this.lblDetailInfo.setText(info.getInfo());
		}
	}
	
	public void done(RunProcess runProcess) {
		btnRun.setEnabled(true);
		progressBar.setValue(progressBar.getMaximum());
	}
	public void threadSuspended(RunProcess runProcess) {
		// TODO Auto-generated method stub
		
	}
	public void threadResumed(RunProcess runProcess) {
		// TODO Auto-generated method stub
		
	}
	public void threadStop(RunProcess runProcess) {
		btnRun.setEnabled(true);
	}
	public void setMessage(String string) {
		JOptionPane.showMessageDialog(null, "Info", string, JOptionPane.INFORMATION_MESSAGE);
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
