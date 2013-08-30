package com.novelbio.nbcgui.GUI;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JLabel;

import com.novelbio.analysis.tools.DifGeneFilter;
import com.novelbio.base.PathDetail;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.gui.GUIFileOpen;
import com.novelbio.base.gui.JScrollPaneData;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JSpinner;

public class GuiFilterDifGene extends JPanel {
	JScrollPaneData sclDifGene;
	JScrollPaneData sclFilter;
	
	JButton btnAddDifGeneFile;
	JButton btnDelDifGeneFile;
	
	JButton btnAddFilter;
	JButton btnDelFilter;
	JButton btnRun;
	JSpinner spnFirstLineBeRead;
	
	GUIFileOpen guiFileOpen = new GUIFileOpen();
	
	DifGeneFilter difGeneFilter = new DifGeneFilter();
	
	/**
	 * Create the panel.
	 */
	public GuiFilterDifGene() {
		setLayout(null);
		
		sclDifGene = new JScrollPaneData();
		sclDifGene.setBounds(27, 23, 613, 295);
		add(sclDifGene);
		
		btnAddDifGeneFile = new JButton("AddFile");
		btnAddDifGeneFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> lsFile = guiFileOpen.openLsFileName("", "");
				ArrayList<String[]> lsInputFile = new ArrayList<String[]>();
				for (String fileName : lsFile) {
					lsInputFile.add(new String[]{ fileName,FileOperate.changeFileSuffix(fileName, "_filtered", null)});
				}
				sclDifGene.addItemLs(lsInputFile);
			}
		});
		btnAddDifGeneFile.setBounds(652, 23, 118, 24);
		add(btnAddDifGeneFile);
		
		btnDelDifGeneFile = new JButton("DelFile");
		btnDelDifGeneFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sclDifGene.deleteSelRows();
			}
		});
		btnDelDifGeneFile.setBounds(652, 294, 118, 24);
		add(btnDelDifGeneFile);
		
		sclFilter = new JScrollPaneData();
		sclFilter.setBounds(27, 356, 450, 99);
		add(sclFilter);
		
		btnAddFilter = new JButton("addFilter");
		btnAddFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sclFilter.addItem(new String[4]);
			}
		});
		btnAddFilter.setBounds(502, 356, 118, 24);
		add(btnAddFilter);
		
		btnDelFilter = new JButton("delFilter");
		btnDelFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sclFilter.deleteSelRows();
			}
		});
		btnDelFilter.setBounds(502, 431, 118, 24);
		add(btnDelFilter);
		
		btnRun = new JButton("run");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				runFiltering();
			}
		});
		btnRun.setBounds(652, 431, 118, 24);
		add(btnRun);
		
		JLabel lblFilter = new JLabel("Filter");
		lblFilter.setBounds(27, 330, 69, 14);
		add(lblFilter);
		
		spnFirstLineBeRead = new JSpinner();
		spnFirstLineBeRead.setBounds(208, 478, 54, 18);
		add(spnFirstLineBeRead);
		
		JLabel label = new JLabel("First Line Be Filtered");
		label.setBounds(33, 479, 167, 14);
		add(label);
		initial();
	}
	
	private void initial() {
		sclDifGene.setTitle(new String[]{"DifGene", "FilteredDifGene"});
		//DefaultIsNotBetween
		//默认这个位点不是between
		sclFilter.setTitle(new String[]{"ColumnNum", "Small", "Big", "DefaultIsNotBetween"});
		//一般第一行是title
		spnFirstLineBeRead.setValue(2);
	}
	
	private void runFiltering() {
		ArrayList<String[]> lsFilters = sclFilter.getLsDataInfo();
		ArrayList<String[]> lsFileNames = sclDifGene.getLsDataInfo();
		//装载过滤器
		difGeneFilter.clearFilter();
		for (String[] filterInfo : lsFilters) {
			boolean isBetweenSmall2Big = false;
			if (!filterInfo[3].trim().equals("")) {
				isBetweenSmall2Big = true;
			}
			difGeneFilter.addFilterInfo(Integer.parseInt(filterInfo[0]), Double.parseDouble(filterInfo[1]), Double.parseDouble(filterInfo[2]), isBetweenSmall2Big);
		}
		
		//开始过滤
		for (String[] fileInAndOut : lsFileNames) {
			difGeneFilter.setReadFromLines((Integer)spnFirstLineBeRead.getValue());
			difGeneFilter.setInputFile(fileInAndOut[0]);
			difGeneFilter.setOutTxtFile(fileInAndOut[1]);
			difGeneFilter.filtering();
		}
		JOptionPane.showConfirmDialog(null, "finished", "infomation", JOptionPane.OK_OPTION);
	}
}
