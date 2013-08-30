package com.novelbio.nbcgui.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import com.novelbio.analysis.annotation.genAnno.AnnoAbs;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.gui.GUIFileOpen;
import com.novelbio.base.gui.JComboBoxData;
import com.novelbio.base.gui.JScrollPaneData;
import com.novelbio.database.domain.geneanno.GOtype;
import com.novelbio.database.model.species.Species;
import com.novelbio.nbcgui.controlquery.CtrlBatchAnnoGene;
/**
 * 批量注释，各种注释
 * @author zong0jie
 *
 */
public class GuiAnnoGene extends JPanel implements GuiNeedOpenFile {
	private static final long serialVersionUID = -4438216387830519443L;
	
	private JTextField txtColAccID;
	JProgressBar progressBar;
	JScrollPaneData scrollPaneData;
	JButton btnDel;
	
	GUIFileOpen guiFileOpen = new GUIFileOpen();
	
	CtrlBatchAnnoGene ctrlBatchAnno;
	private JButton btnRun;
	private JCheckBox chckbxBlastto;
	JComboBoxData<Species> cmbBlastSpecies;
	JComboBoxData<Species> cmbSpecies;
	
	JComboBoxData<Integer> cmbAnnoType;
	JComboBoxData<GOtype> cmbGOtype;
	
	/**
	 * Create the panel.
	 */
	public GuiAnnoGene() {
		setLayout(null);
		
		scrollPaneData = new JScrollPaneData();
		scrollPaneData.setBounds(12, 30, 693, 516);
		add(scrollPaneData);
		
		JButton btnOpenfile = new JButton("OpenFile");
		btnOpenfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<String> lsFileName = guiFileOpen.openLsFileName("", "");
				List<String[]> lsName2Out = new ArrayList<String[]>();
				for (String string : lsFileName) {
					String[] tmp = new String[2];
					tmp[0] = string;
					tmp[1] = FileOperate.changeFileSuffix(string, "_anno", null);
					lsName2Out.add(tmp);
				}
				scrollPaneData.addItemLs(lsName2Out);
			}
		});
		btnOpenfile.setBounds(717, 30, 118, 24);
		add(btnOpenfile);
		
		txtColAccID = new JTextField();
		txtColAccID.setBounds(717, 121, 114, 18);
		add(txtColAccID);
		txtColAccID.setColumns(10);
		
		JLabel lblAccidcolumn = new JLabel("AccIDColumn");
		lblAccidcolumn.setBounds(717, 102, 118, 14);
		add(lblAccidcolumn);
		
		btnDel = new JButton("DelFile");
		btnDel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scrollPaneData.deleteSelRows();
			}
		});
		btnDel.setBounds(717, 66, 118, 24);
		add(btnDel);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(12, 571, 693, 14);
		add(progressBar);
		
		chckbxBlastto = new JCheckBox("BlastTo");
		chckbxBlastto.setBounds(717, 445, 131, 22);
		add(chckbxBlastto);
		
		cmbBlastSpecies = new JComboBoxData<Species>();
		cmbBlastSpecies.setBounds(717, 490, 118, 23);
		add(cmbBlastSpecies);
		
		cmbSpecies = new JComboBoxData<Species>();
		cmbSpecies.setBounds(717, 394, 118, 23);
		add(cmbSpecies);
		

		cmbAnnoType = new JComboBoxData<>();
		cmbAnnoType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cmbAnnoType.getSelectedValue() == AnnoAbs.GO) {
					cmbGOtype.setVisible(true);
				} else {
					cmbGOtype.setVisible(false);
				}
			}
		});
		cmbAnnoType.setBounds(717, 164, 118, 27);
		add(cmbAnnoType);
		
		cmbGOtype = new JComboBoxData<>();
		cmbGOtype.setBounds(717, 245, 118, 27);
		add(cmbGOtype);
		
		btnRun = new JButton("Run");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnRun.setEnabled(false);
				ctrlBatchAnno.setAnnotationType(cmbAnnoType.getSelectedValue());
				ctrlBatchAnno.setGOtype(cmbGOtype.getSelectedValue());
				ctrlBatchAnno.setListQuery(scrollPaneData.getLsDataInfo());
				try {
					ctrlBatchAnno.setColumnAccIDFrom1(Integer.parseInt(txtColAccID.getText()));
				} catch (Exception e2) { }
				
				try {
					ctrlBatchAnno.setColumnAccIDFrom1(Integer.parseInt(txtColAccID.getText()));
				} catch (Exception e2) { }
				try {
					ctrlBatchAnno.setBlastTo(chckbxBlastto.isSelected(), cmbBlastSpecies.getSelectedValue().getTaxID());
				} catch (Exception e2) { }
				ctrlBatchAnno.setSpecies(cmbSpecies.getSelectedValue().getTaxID());
				
				ctrlBatchAnno.execute();
				JOptionPane.showMessageDialog(null, "finish", "finish", JOptionPane.CLOSED_OPTION);
				btnRun.setEnabled(true);
			}
		});
		btnRun.setBounds(717, 561, 118, 24);
		add(btnRun);
		

		initial();
	}
	
	private void initial() {
		selectRadAnno();
		cmbBlastSpecies.setMapItem(Species.getSpeciesName2Species(Species.KEGGNAME_SPECIES));
		cmbSpecies.setMapItem(Species.getSpeciesName2Species(Species.KEGGNAME_SPECIES));
		btnDel.setEnabled(true);
		scrollPaneData.setTitle(new String[]{"InFile", "OutFile"});
		cmbAnnoType.setMapItem(AnnoAbs.getMapAnnoType());
		
		cmbGOtype.setMapItem(GOtype.getMapStr2Gotype());
		cmbGOtype.setVisible(false);
	}
	
	private void selectRadAnno() {
		ctrlBatchAnno = new CtrlBatchAnnoGene(this);
		txtColAccID.setEditable(true);
		chckbxBlastto.setEnabled(true);
		cmbBlastSpecies.setEnabled(true);
	}
	
	public void setGuiFileOpen(GUIFileOpen guiFileOpen) {
		this.guiFileOpen = guiFileOpen;
	}
	
	public JProgressBar getProcessBar() {
		return progressBar;
	}
	public JButton getBtnSave() {
		return btnDel;
	}
	public JButton getBtnRun() {
		return btnRun;
	}
}
