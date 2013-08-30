package com.novelbio.nbcgui.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import com.novelbio.analysis.seq.genome.GffChrAbs;
import com.novelbio.analysis.seq.resequencing.SnpGroupFilterInfo;
import com.novelbio.analysis.seq.resequencing.SnpLevel;
import com.novelbio.analysis.seq.resequencing.SnpSomaticFilter;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.gui.GUIFileOpen;
import com.novelbio.base.gui.JComboBoxData;
import com.novelbio.base.gui.JScrollPaneData;
import com.novelbio.base.multithread.RunProcess;
import com.novelbio.nbcgui.GuiAnnoInfo;

public class GuiSnpFilterSimple extends JPanel {
	static final int progressLength = 10000;
	
	JScrollPaneData sclSnpFile;
	JScrollPaneData sclPileupFile;
	JScrollPaneData scrlCompare;
	JButton btnOpeanbam;
	JButton btnDelbam;
	JButton btnRun;
	GUIFileOpen guiFileOpen = new GUIFileOpen();
	private JTextField txtSaveTo;
	
	JProgressBar progressBar;
	JLabel lblInformation;
	JLabel lblDetailInfo;
	
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
	JComboBoxData<SnpLevel> cmbSnpLevel = new JComboBoxData<SnpLevel>();
	
	GffChrAbs gffChrAbs = new GffChrAbs();
	private JTextField txtSnpHeto;
	private JTextField txtSnpHetoMore;
	
	/**
	 * Create the panel.
	 */
	public GuiSnpFilterSimple() {
		setLayout(null);
		
//		cmbGroup.setEditable(true);
		cmbGroup.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				changeSclCompareGroup();
			}
		});
		
		sclPileupFile = new JScrollPaneData();
		sclPileupFile.setBounds(20, 231, 610, 155);
		add(sclPileupFile);
		
		btnOpeanbam = new JButton("OpeanBam");
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
				sclPileupFile.addItemLs(lsInfo);
			}
		});
		btnOpeanbam.setBounds(20, 398, 118, 24);
		add(btnOpeanbam);
		
		btnDelbam = new JButton("DelBam");
		btnDelbam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sclPileupFile.deleteSelRows();
			}
		});
		btnDelbam.setBounds(365, 398, 118, 24);
		add(btnDelbam);
		
		btnRun = new JButton("run");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				run();
			}
		});
		btnRun.setBounds(784, 481, 118, 24);
		add(btnRun);
		
		JLabel lblAddbamfile = new JLabel("AddPileUpFile");
		lblAddbamfile.setBounds(20, 214, 129, 14);
		add(lblAddbamfile);
		
		txtSaveTo = new JTextField();
		txtSaveTo.setBounds(20, 484, 532, 18);
		add(txtSaveTo);
		txtSaveTo.setColumns(10);
		
		JButton btnSaveto = new JButton("SaveTo");
		btnSaveto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtSaveTo.setText(guiFileOpen.saveFileNameAndPath("Out", ""));
			}
		});
		btnSaveto.setBounds(606, 481, 118, 24);
		add(btnSaveto);
		
		scrlCompare = new JScrollPaneData();
		scrlCompare.setBounds(642, 273, 260, 96);
		add(scrlCompare);
		
		JLabel lblCompare = new JLabel("Compare");
		lblCompare.setBounds(642, 248, 69, 14);
		add(lblCompare);
		
		JButton btnAddCompare = new JButton("AddCompare");
		btnAddCompare.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scrlCompare.addItem(new String[]{"",""});
			}
		});
		btnAddCompare.setBounds(642, 381, 115, 24);
		add(btnAddCompare);
		
		JButton btnDeleteCompare = new JButton("DelCompare");
		btnDeleteCompare.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scrlCompare.deleteSelRows();
			}
		});
		btnDeleteCompare.setBounds(784, 381, 118, 24);
		add(btnDeleteCompare);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(20, 525, 882, 14);
		add(progressBar);
		
		lblInformation = new JLabel("");
		lblInformation.setBounds(20, 460, 217, 14);
		add(lblInformation);
		
		lblDetailInfo = new JLabel("");
		lblDetailInfo.setBounds(255, 460, 260, 14);
		add(lblDetailInfo);
		
		guiLayeredPaneSpeciesVersionGff = new GuiLayeredPaneSpeciesVersionGff();
		guiLayeredPaneSpeciesVersionGff.setBounds(644, 34, 258, 153);
		add(guiLayeredPaneSpeciesVersionGff);
		
		sclSnpFile = new JScrollPaneData();
		sclSnpFile.setBounds(20, 41, 610, 130);
		add(sclSnpFile);
		
		JLabel lblAddSnpFile = new JLabel("AddSnpFile");
		lblAddSnpFile.setBounds(20, 18, 129, 14);
		add(lblAddSnpFile);
		
		JButton btnOpensnp = new JButton("OpenSnp");
		btnOpensnp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> lsFile = guiFileOpen.openLsFileName("TxtFile", "");
				ArrayList<String[]> lsInfo = new ArrayList<String[]>();
				for (String string : lsFile) {
					//0: fileName   1: sampleName 2:group
					String[] tmResult = new String[3];
					tmResult[0] = string; tmResult[1] = FileOperate.getFileNameSep(string)[0].split("_")[0];
					tmResult[2] = tmResult[1];
					lsInfo.add(tmResult);
				}
				sclSnpFile.addItemLs(lsInfo);
			}
		});
		btnOpensnp.setBounds(20, 178, 118, 24);
		add(btnOpensnp);
		
		JButton btnDelsnp = new JButton("DelSnp");
		btnDelsnp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sclSnpFile.deleteSelRows();
			}
		});
		btnDelsnp.setBounds(512, 178, 118, 24);
		add(btnDelsnp);
		
		JLabel lblHetosnppropfilter = new JLabel("HetoSnpPropFilter");
		lblHetosnppropfilter.setBounds(642, 199, 144, 14);
		add(lblHetosnppropfilter);
		
		JLabel label = new JLabel("HetoMoreSnpPropFilter");
		label.setBounds(642, 222, 179, 14);
		add(label);
		
		txtSnpHeto = new JTextField();
		txtSnpHeto.setBounds(833, 197, 69, 18);
		add(txtSnpHeto);
		txtSnpHeto.setColumns(10);
		
		txtSnpHetoMore = new JTextField();
		txtSnpHetoMore.setBounds(832, 220, 70, 18);
		add(txtSnpHetoMore);
		txtSnpHetoMore.setColumns(10);
		
		initial();
	}
	private void initial() {
		sclSnpFile.setTitle(new String[]{"SnpFile", "Prefix"});
		sclPileupFile.setTitle(new String[]{"PileUpFile", "Prefix"});

		cmbSnpLevel.setMapItem(SnpLevel.getMapStr2SnpLevel());
		scrlCompare.setTitle(new String[] {"Treat", "TreatSnp", "Control"});
		scrlCompare.setItem(0, cmbGroup);
		scrlCompare.setItem(1, cmbSnpLevel);
		scrlCompare.setItem(2, cmbGroup);

		progressBar.setMinimum(0);
		progressBar.setMaximum(progressLength);
	}

	private void run() {
		gffChrAbs.setSpecies(guiLayeredPaneSpeciesVersionGff.getSelectSpecies());
		SnpSomaticFilter snpSomaticFilter = new SnpSomaticFilter();
		try {
			snpSomaticFilter.setSnp_Heto_Contain_SnpProp_Min(Double.parseDouble(txtSnpHeto.getText()));
		} catch (Exception e) { }
		try {
			snpSomaticFilter.setSnp_HetoMore_Contain_SnpProp_Min(Double.parseDouble(txtSnpHetoMore.getText()));
		} catch (Exception e) { }

		List<String[]> lsSnpCompareInfo = scrlCompare.getLsDataInfo();
		List<String[]> lsSnpFile = sclSnpFile.getLsDataInfo();
		List<String[]> lsSnpPileup = sclPileupFile.getLsDataInfo();
		
		for (String[] strings : lsSnpFile) {
			snpSomaticFilter.addSnpFromNBCfile(strings[1], strings[0]);
		}
		if (lsSnpFile.size() == 0) {
			for (String[] strings : lsSnpPileup) {
				snpSomaticFilter.addSnpFromPileUpFile(strings[1], SnpLevel.HetoMid, strings[0]);
			}
		}
		for (String[] strings : lsSnpPileup) {
			snpSomaticFilter.addSampileupFile(strings[1], strings[0]);
		}
		snpSomaticFilter.readSnpDetailFromFile();
		
		for (String[] strings : lsSnpCompareInfo) {
			snpSomaticFilter.clearGroupFilterInfo();
			SnpGroupFilterInfo sampleDetailTreat = new SnpGroupFilterInfo();
			sampleDetailTreat.addSampleName(strings[2]);
			sampleDetailTreat.setSnpLevel(SnpLevel.RefHomo); 
			snpSomaticFilter.addFilterGroup(sampleDetailTreat);
			
			SnpGroupFilterInfo sampleDetailCol = new SnpGroupFilterInfo();
			sampleDetailCol.addSampleName(strings[0]);
			sampleDetailCol.setSnpLevel(SnpLevel.getSnpLevel(strings[1])); 
			snpSomaticFilter.addFilterGroup(sampleDetailCol);
			
			snpSomaticFilter.filterSnp();
			snpSomaticFilter.writeToFile(gffChrAbs, true, txtSaveTo.getText() + strings[0] + "vs" + strings[2] + "_" +strings[1] + ".xls");
		}
	}
	
	public void setProgressBarLevelLs(List<Double> lsProgressBarLevel) {
		this.lsProgressBarLevel = lsProgressBarLevel;
	}
	
	/**
	 * 设定本次步骤里面将绘制progressBar的第几部分
	 * 并且本部分的最短点和最长点分别是什么
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
		JOptionPane.showMessageDialog(null, "Info", string, JOptionPane.INFORMATION_MESSAGE);
	}
	
	private void changeSclCompareGroup() {
		ArrayList<String[]> lsSnp2Prefix = new ArrayList<String[]>();
		lsSnp2Prefix.addAll(sclSnpFile.getLsDataInfo());
		lsSnp2Prefix.addAll(sclPileupFile.getLsDataInfo());
		Map<String, String> mapString2Value = new LinkedHashMap<String, String>();
		for (String[] snp2prefix : lsSnp2Prefix) {
			if (snp2prefix[0] == null || snp2prefix[0].equals("") || snp2prefix[1] == null || snp2prefix[1].equals("")) {
				continue;
			}
			mapString2Value.put(snp2prefix[1], snp2prefix[1]);
		}
		cmbGroup.setMapItem(mapString2Value);
	}
}
