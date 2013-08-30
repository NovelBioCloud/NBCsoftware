package com.novelbio.nbcgui.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.novelbio.analysis.annotation.blast.BlastNBC;
import com.novelbio.analysis.annotation.blast.BlastType;
import com.novelbio.analysis.seq.fasta.SeqFasta;
import com.novelbio.analysis.seq.fasta.SeqHash;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.gui.GUIFileOpen;
import com.novelbio.base.gui.JComboBoxData;
import com.novelbio.base.gui.JScrollPaneData;
import com.novelbio.base.gui.JTextFieldData;
import com.novelbio.database.model.species.Species;
import com.novelbio.database.updatedb.database.BlastUp2DB;

public class GuiBlast extends JPanel implements GuiNeedOpenFile{
	private JTextField textQueryFasta;
	private JTextField textSubjectFasta;
	private JTextField textEvalue;
	private JTextFieldData textResultNum;
	private JTextField textResultFile;
	
	private BlastNBC blastNBC = new BlastNBC();
	
	GUIFileOpen fileOpen = new GUIFileOpen();
	JCheckBox chbRefStyle = null;

	JComboBoxData<BlastType> combBlastType = null;
	JComboBoxData<Integer> combResultType = null;
	
	JCheckBox chckbxSavetodb;
	JScrollPaneData sclPaneBlastFile;

	int queryType = SeqFasta.SEQ_UNKNOWN;
	int subjectType = SeqFasta.SEQ_UNKNOWN;
	private JTextField txtThreadNum;
		
	private void setCombBlastType() {
		combBlastType.setMapItem(BlastType.getMapBlastType(queryType, subjectType));
	}
	
	
	/**
	 * Create the panel.
	 */
	public GuiBlast() {
		setLayout(null);
		
		textQueryFasta = new JTextField();
		textQueryFasta.setBounds(10, 50, 249, 21);
		add(textQueryFasta);
		textQueryFasta.setColumns(10);
		
		JButton btnQueryFasta = new JButton("QueryFasta");
		btnQueryFasta.setBounds(279, 48, 130, 24);
		btnQueryFasta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String path = fileOpen.openFileName("txt", "");
				textQueryFasta.setText(path);
				if (FileOperate.isFileExist(path)) {
					queryType = SeqHash.getSeqType(path);
				}
				setCombBlastType();
			}
		});
		add(btnQueryFasta);
		
		textSubjectFasta = new JTextField();
		textSubjectFasta.setBounds(10, 97, 249, 21);
		add(textSubjectFasta);
		textSubjectFasta.setColumns(10);
		
		JButton btnSubjectDB = new JButton("SubjectFasta");
		btnSubjectDB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String path = fileOpen.openFileName("txt", "");
				textSubjectFasta.setText(path);
				if (FileOperate.isFileExist(path)) {
					subjectType = SeqHash.getSeqType(path);
				}
				setCombBlastType();
			}
		});
		btnSubjectDB.setBounds(279, 95, 130, 24);
		add(btnSubjectDB);
		
		combBlastType = new JComboBoxData<BlastType>();
		combBlastType.setBounds(10, 175, 209, 23);
		add(combBlastType);
		
		combResultType = new JComboBoxData<Integer>();;
		combResultType.setMapItem(BlastNBC.getHashResultType());
		combResultType.setBounds(10, 440, 249, 23);
		add(combResultType);
		
		JLabel lblBlastType = new JLabel("BlastType");
		lblBlastType.setBounds(10, 152, 91, 14);
		add(lblBlastType);
		
		textEvalue = new JTextField();
		textEvalue.setText("0.01");
		textEvalue.setBounds(10, 240, 114, 18);
		add(textEvalue);
		textEvalue.setColumns(10);
		
		JLabel lblEvalue = new JLabel("E-Value");
		lblEvalue.setBounds(10, 216, 54, 14);
		add(lblEvalue);
		
		textResultNum = new JTextFieldData();
		textResultNum.setNumOnly();
		textResultNum.setText("2");
		textResultNum.setBounds(10, 299, 114, 18);
		add(textResultNum);
		textResultNum.setColumns(10);
		
		JLabel lblResultnum = new JLabel("ResultNum");
		lblResultnum.setBounds(10, 279, 77, 14);
		add(lblResultnum);
		
		textResultFile = new JTextField();
		textResultFile.setBounds(10, 367, 249, 21);
		add(textResultFile);
		textResultFile.setColumns(10);
		
		JButton btnResultfile = new JButton("ResultFile");
		btnResultfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textResultFile.setText(fileOpen.openFileName("OutFile", ""));
			}
		});
		btnResultfile.setBounds(279, 365, 105, 24);
		add(btnResultfile);
		
		JButton btnRunblast = new JButton("RunBlast");
		btnRunblast.setBounds(279, 439, 98, 24);
		btnRunblast.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				blastNBC.setBlastType(combBlastType.getSelectedValue());
				try {
					blastNBC.setCpuNum(Integer.parseInt(txtThreadNum.getText()));
				} catch (Exception e2) { }
				blastNBC.setDatabaseSeq(textSubjectFasta.getText());
				blastNBC.setQueryFastaFile(textQueryFasta.getText());
				blastNBC.setEvalue(Double.parseDouble(textEvalue.getText()));
				blastNBC.setResultAlignNum(Integer.parseInt(textResultNum.getText()));
				blastNBC.setResultSeqNum(Integer.parseInt(textResultNum.getText()));
				blastNBC.setResultFile(textResultFile.getText());
				blastNBC.setResultType((Integer)combResultType.getSelectedValue());
				blastNBC.blast();
			}
		});
		add(btnRunblast);
		
		JButton btnNewButton = new JButton("OpenBlastFile");
		btnNewButton.setBounds(446, 365, 146, 24);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<String> lsPath = fileOpen.openLsFileName("txt", "");
				List<String[]> lsInput = new ArrayList<String[]>();
				for (String path : lsPath) {
					lsInput.add(new String[]{path, "", ""});
				}
				sclPaneBlastFile.addItemLs(lsInput);
			}
		});
		add(btnNewButton);
		
		JButton btnUpdataBlast = new JButton("UpdataBlast");
		btnUpdataBlast.setBounds(783, 450, 123, 24);
		btnUpdataBlast.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addBlastInfo();
			}
		});
		add(btnUpdataBlast);
		
		JLabel lblResulttype = new JLabel("ResultType");
		lblResulttype.setBounds(10, 414, 91, 14);
		add(lblResulttype);
		
		chckbxSavetodb = new JCheckBox("SaveToDB");
		chckbxSavetodb.setBounds(775, 410, 131, 22);
		add(chckbxSavetodb);
		
		sclPaneBlastFile = new JScrollPaneData();
		sclPaneBlastFile.setBounds(446, 43, 460, 310);
		add(sclPaneBlastFile);
		
		JButton btnDelfile = new JButton("DelFile");
		btnDelfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sclPaneBlastFile.deleteSelRows();
			}
		});
		btnDelfile.setBounds(760, 365, 146, 24);
		add(btnDelfile);
		
		txtThreadNum = new JTextField();
		txtThreadNum.setText("4");
		txtThreadNum.setBounds(217, 299, 42, 18);
		add(txtThreadNum);
		txtThreadNum.setColumns(10);
		
		JLabel lblThreadnum = new JLabel("ThreadNum");
		lblThreadnum.setBounds(218, 279, 98, 14);
		add(lblThreadnum);
		
		initial();
	}
	
	private void initial() {
		JComboBoxData<Species> cmbSpeciesQ  = new JComboBoxData<Species>();
		cmbSpeciesQ.setMapItem(Species.getSpeciesName2Species(Species.ALL_SPECIES));
		cmbSpeciesQ.setEditable(true);
		JComboBoxData<Species> cmbSpeciesS  = new JComboBoxData<Species>();
		cmbSpeciesS.setMapItem(Species.getSpeciesName2Species(Species.ALL_SPECIES));
		cmbSpeciesS.setEditable(false);
		
		sclPaneBlastFile.setTitle(new String[]{"BlastFile","QueryTaxID", "SubTaxID"});
		sclPaneBlastFile.setItem(1, cmbSpeciesQ);
		sclPaneBlastFile.setItem(2, cmbSpeciesS);
	}
	
	private void addBlastInfo() {
		Map<String, Species> mapComName2Species = Species.getSpeciesName2Species(Species.ALL_SPECIES);
		for (String[] content : sclPaneBlastFile.getLsDataInfo()) {
			BlastUp2DB blast = new BlastUp2DB();
			blast.setUpdate(chckbxSavetodb.isSelected());
			
			//设定一个默认参数
			int taxIDQ = 1234;
			Species speciesQ = mapComName2Species.get(content[1]);
			if (speciesQ == null) {
				try {
					taxIDQ = Integer.parseInt(content[1].trim());
				} catch (Exception e) {
					taxIDQ = 1234;
				}
			} else {
				taxIDQ = speciesQ.getTaxID();
			}
			int taxIDS = mapComName2Species.get(content[2]).getTaxID();
						
			blast.setTaxID(taxIDQ);
			blast.setSubTaxID(taxIDS);
			if (chckbxSavetodb.isSelected()) {
				blast.setTxtWriteExcep(FileOperate.changeFileSuffix(content[0].trim(), "_cannotUpDate", null));
			}
			blast.updateFile(content[0]);
		}
	}
	
	public void setGuiFileOpen(GUIFileOpen guiFileOpen) {
		this.fileOpen = guiFileOpen;
	}
}
