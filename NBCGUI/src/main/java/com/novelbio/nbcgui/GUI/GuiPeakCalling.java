package com.novelbio.nbcgui.GUI;

import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.JLabel;

import com.novelbio.analysis.seq.chipseq.peakcalling.Macs14control;
import com.novelbio.analysis.seq.chipseq.peakcalling.PeakCallingSicer;
import com.novelbio.analysis.seq.chipseq.peakcalling.SicerControl;
import com.novelbio.analysis.seq.chipseq.peakcalling.PeakCallingSicer.PeakCallingSicerType;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.gui.GUIFileOpen;
import com.novelbio.base.gui.JComboBoxData;
import com.novelbio.database.model.species.Species;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.border.EmptyBorder;

import org.apache.commons.collections.map.LinkedMap;
import javax.swing.SpinnerNumberModel;

public class GuiPeakCalling extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private JTextField txtSavePath;
	private static ButtonGroup buttonGroup = new ButtonGroup();
	private JTextField txtControlFileMacs;
	private JTextField txtInFileMacs;
	private JTextField txtPvalue;
	private JTextField txtKobedFileSICER;
	private JTextField txtWtBedFileSICER;
	private JTextField txtKoColFileSICER;
	private JTextField txtWtcolFileSICER;
	JComboBoxData<Integer> cmbMethyTypeSicer;
	JComboBoxData<PeakCallingSicerType> cmbSICERtype;
	JPanel sicer_df_rbPanel;
	JPanel sicer_dfPanel;
	JRadioButton macs14RadioButton;
	JRadioButton sicerRadioButton;
	JPanel macs14Panel;
	JPanel Sicerpanel;
	JButton kobedFilebutton;
	JButton wtBedFilebutton;
	JButton KoColFilebutton;
	JButton wtcolFilebutton;
	GUIFileOpen guiFileOpen = new GUIFileOpen();
	GuiLayeredPanSpeciesVersion guiLayeredPanSpeciesVersion;

	Macs14control macs14control;
	JSpinner mfoldminspinner;
	int speciesID;
	int methylationType;

	/*
	 * sicer
	 */
	JSpinner mfoldMaxspinner;
	GuiWinAndGsize dialog = new GuiWinAndGsize();
	SicerControl sicerControl;
	
	private JTextField txtKoPrefixSICER;
	private JTextField txtWtPrefixSICER;
	JLabel wtPrefix;
	
	/**
	 * Create the panel.
	 */
	public GuiPeakCalling() {
		setLayout(null);
		
		macs14RadioButton = new JRadioButton("macs14");
		macs14RadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (macs14RadioButton.isSelected()) {
					macs14Panel.setVisible(true);
					Sicerpanel.setVisible(false);
				}
			}
		});
		macs14RadioButton.setFont(new Font("Dialog", Font.BOLD, 15));
		macs14RadioButton.setHorizontalAlignment(SwingConstants.CENTER);
		macs14RadioButton.setBounds(19, 116, 122, 31);
		add(macs14RadioButton);
		buttonGroup.add(macs14RadioButton);
		
		sicerRadioButton = new JRadioButton("Sicer");
		sicerRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (sicerRadioButton.isSelected()) {
					Sicerpanel.setVisible(true);
					macs14Panel.setVisible(false);
				}
			}
		});
		sicerRadioButton.setFont(new Font("Dialog", Font.BOLD, 15));
		sicerRadioButton.setBounds(27, 261, 103, 23);
		add(sicerRadioButton);
		buttonGroup.add(sicerRadioButton);
		
		guiLayeredPanSpeciesVersion = new GuiLayeredPanSpeciesVersion();
		guiLayeredPanSpeciesVersion.setBounds(27, 12, 237, 96);
		add(guiLayeredPanSpeciesVersion);
		JButton saveButton = new JButton("SAVE");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String outPath = guiFileOpen.saveFileNameAndPath("", "");
				txtSavePath.setText(outPath);
			}
		});
		saveButton.setBounds(276, 25, 118, 25);
		add(saveButton);
		
		txtSavePath = new JTextField();
		txtSavePath.setBounds(429, 25, 292, 25);
		add(txtSavePath);
		txtSavePath.setColumns(10);
		
		macs14Panel = new JPanel();
		macs14Panel.setLayout(null);
		macs14Panel.setBounds(37, 157, 684, 96);
		add(macs14Panel);
		
		txtControlFileMacs = new JTextField();
		txtControlFileMacs.setColumns(10);
		txtControlFileMacs.setBounds(149, 49, 114, 25);
		macs14Panel.add(txtControlFileMacs);
		
		txtInFileMacs = new JTextField();
		txtInFileMacs.setColumns(10);
		txtInFileMacs.setBounds(149, 12, 114, 25);
		macs14Panel.add(txtInFileMacs);
		
		JButton controlFilebutton = new JButton("ControlFile");
		controlFilebutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtControlFileMacs.setText(guiFileOpen.openFileName("", ""));
			}
		});
		controlFilebutton.setBounds(12, 49, 93, 25);
		macs14Panel.add(controlFilebutton);
		
		JButton inFilebutton = new JButton("InFile");
		inFilebutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtInFileMacs.setText(guiFileOpen.openFileName("", ""));
			}
		});
		inFilebutton.setBounds(12, 12, 93, 25);
		macs14Panel.add(inFilebutton);
		
		mfoldminspinner = new JSpinner();
		mfoldminspinner.setModel(new SpinnerNumberModel(new Integer(2), null, null, new Integer(1)));
		mfoldminspinner.setBounds(403, 13, 69, 23);
		macs14Panel.add(mfoldminspinner);
		
		mfoldMaxspinner = new JSpinner();
		mfoldMaxspinner.setModel(new SpinnerNumberModel(new Integer(300), null, null, new Integer(1)));
		mfoldMaxspinner.setBounds(584, 13, 69, 23);
		macs14Panel.add(mfoldMaxspinner);
		
		JLabel pvalueLabel = new JLabel("pvalue");
		pvalueLabel.setBounds(304, 49, 69, 25);
		macs14Panel.add(pvalueLabel);
		
		JLabel mfoldMaxLabel = new JLabel("mfoldMax");
		mfoldMaxLabel.setBounds(495, 12, 81, 25);
		macs14Panel.add(mfoldMaxLabel);
		
		JLabel mfoldminLabel = new JLabel("mfoldMin");
		mfoldminLabel.setBounds(304, 12, 81, 25);
		macs14Panel.add(mfoldminLabel);
		
		JButton mac14startbutton = new JButton("macs14Start");
		mac14startbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				runMacs();
			}
		});
		mac14startbutton.setBounds(521, 42, 132, 38);
		macs14Panel.add(mac14startbutton);
		
		txtPvalue = new JTextField();
		txtPvalue.setText("0.01");
		txtPvalue.setColumns(10);
		txtPvalue.setBounds(403, 49, 69, 25);
		macs14Panel.add(txtPvalue);
		
		Sicerpanel = new JPanel();
		Sicerpanel.setBounds(37, 294, 691, 296);
		Sicerpanel.setVisible(false);
		add(Sicerpanel);
		Sicerpanel.setLayout(null);
		
		
		cmbSICERtype = new JComboBoxData<PeakCallingSicerType>();
		cmbSICERtype.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setComponentVisibleSICER(cmbSICERtype.getSelectedValue());
			}
		});
		cmbSICERtype.setBounds(18, 10, 323, 32);
		Sicerpanel.add(cmbSICERtype);
		cmbSICERtype.setMapItem(PeakCallingSicerType.getMapType());
		
		sicer_df_rbPanel = new JPanel();
		sicer_df_rbPanel.setBounds(18, 80, 517, 81);
		Sicerpanel.add(sicer_df_rbPanel);
		sicer_df_rbPanel.setLayout(null);
		
		kobedFilebutton = new JButton("koBedFile");
		kobedFilebutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String koBedPathAndFile = guiFileOpen.openFileName("", "");
				txtKobedFileSICER.setText(koBedPathAndFile);
				txtKoPrefixSICER.setText( FileOperate.getFileName(koBedPathAndFile).split("_")[0]);
			}
		});
		kobedFilebutton.setBounds(16, 12, 106, 25);
		sicer_df_rbPanel.add(kobedFilebutton);
		
		txtKobedFileSICER = new JTextField();
		txtKobedFileSICER.setColumns(10);
		txtKobedFileSICER.setBounds(134, 11, 177, 27);
		sicer_df_rbPanel.add(txtKobedFileSICER);
		
		KoColFilebutton = new JButton("koColFile");
		KoColFilebutton.setBounds(16, 49, 106, 25);
		sicer_df_rbPanel.add(KoColFilebutton);
		KoColFilebutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String koColPathAndFile = guiFileOpen.openFileName("", "");
				txtKoColFileSICER.setText(koColPathAndFile);
			}
		});
		KoColFilebutton.setEnabled(false);
		
		txtKoColFileSICER = new JTextField();
		txtKoColFileSICER.setBounds(134, 50, 175, 25);
		sicer_df_rbPanel.add(txtKoColFileSICER);
		txtKoColFileSICER.setColumns(10);
		txtKoColFileSICER.setEnabled(false);
		
		sicer_dfPanel = new JPanel();
		sicer_dfPanel.setLayout(null);
		sicer_dfPanel.setBounds(18, 173, 517, 81);
		Sicerpanel.add(sicer_dfPanel);
		
		wtBedFilebutton = new JButton("wtBedFile");
		wtBedFilebutton.setBounds(12, 12, 106, 25);
		sicer_dfPanel.add(wtBedFilebutton);
		wtBedFilebutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String wtBedPathAndFile = guiFileOpen.openFileName("", "");
				txtWtBedFileSICER.setText(wtBedPathAndFile);
				txtWtPrefixSICER.setText(FileOperate.getFileName(wtBedPathAndFile).split("_")[0]);
			}
		});
		wtBedFilebutton.setEnabled(false);
		
		txtWtBedFileSICER = new JTextField();
		txtWtBedFileSICER.setBounds(136, 12, 175, 25);
		sicer_dfPanel.add(txtWtBedFileSICER);
		txtWtBedFileSICER.setColumns(10);
		txtWtBedFileSICER.setEditable(false);
		
		wtcolFilebutton = new JButton("wtColFile");
		wtcolFilebutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String wtColPathAndFile = guiFileOpen.openFileName("", "");
				txtWtcolFileSICER.setText(wtColPathAndFile);
			}
		});
		wtcolFilebutton.setBounds(12, 49, 106, 25);
		sicer_dfPanel.add(wtcolFilebutton);
		
		txtWtcolFileSICER = new JTextField();
		txtWtcolFileSICER.setColumns(10);
		txtWtcolFileSICER.setBounds(136, 49, 175, 25);
		sicer_dfPanel.add(txtWtcolFileSICER);
		
		JButton sicerStartbutton = new JButton("sicerSTART");
		sicerStartbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				runSicer();
			}
		});
		sicerStartbutton.setBounds(547, 80, 129, 174);
		Sicerpanel.add(sicerStartbutton);
		
		cmbMethyTypeSicer = new JComboBoxData<Integer>();
		cmbMethyTypeSicer.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {	
				methylationType = cmbMethyTypeSicer.getSelectedValue();
				if (methylationType == 40) {
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
					System.out.println(dialog.getWindowSize());
					System.out.println(dialog.getGapSize());
				}
			}
		});
		

		cmbMethyTypeSicer.setMapItem(SicerControl.getMapMethyStr2Int());
		cmbMethyTypeSicer.setBounds(18, 54, 323, 24);
		Sicerpanel.add(cmbMethyTypeSicer);
		kobedFilebutton.setEnabled(true);
		txtKobedFileSICER.setEnabled(true);
		
		txtKoPrefixSICER = new JTextField();
		txtKoPrefixSICER.setBounds(382, 15, 123, 19);
		sicer_df_rbPanel.add(txtKoPrefixSICER);
		txtKoPrefixSICER.setColumns(10);
		
		
		
		JLabel koPrelabel = new JLabel("koPrefix");
		koPrelabel.setBounds(315, 17, 68, 15);
		sicer_df_rbPanel.add(koPrelabel);
		wtcolFilebutton.setEnabled(false);
		txtWtcolFileSICER.setEnabled(false);
	
		
		txtWtPrefixSICER = new JTextField();
		txtWtPrefixSICER.setBounds(386, 15, 119, 19);
		sicer_dfPanel.add(txtWtPrefixSICER);
		txtWtPrefixSICER.setColumns(10);
		txtWtPrefixSICER.setEditable(false);
		
		
		wtPrefix = new JLabel("wtPrefix");
		wtPrefix.setBounds(317, 17, 68, 15);
		sicer_dfPanel.add(wtPrefix);
		wtPrefix.setEnabled(false);
	}
	
	/**
	 * 根据不同的sicer类型，显示不同的组件
	 */
	private void setComponentVisibleSICER(PeakCallingSicerType sicerType) {
		if (sicerType == PeakCallingSicerType.SICERrb) {
			kobedFilebutton.setEnabled(true);
			txtKobedFileSICER.setEnabled(true);
			wtBedFilebutton.setEnabled(false);
			txtWtBedFileSICER.setEditable(false);
			KoColFilebutton.setEnabled(false);
			txtKoColFileSICER.setEnabled(false);
			wtcolFilebutton.setEnabled(false);
			txtWtcolFileSICER.setEnabled(false);
		}
		if (sicerType == PeakCallingSicerType.SICER) {
			kobedFilebutton.setEnabled(true);
			txtKobedFileSICER.setEnabled(true);
			wtBedFilebutton.setEnabled(false);
			txtWtBedFileSICER.setEditable(false);
			KoColFilebutton.setEnabled(true);
			txtKoColFileSICER.setEnabled(true);
			wtcolFilebutton.setEnabled(false);
			txtWtcolFileSICER.setEnabled(false);
		}
		if (sicerType == PeakCallingSicerType.SICERdfrb) {
			kobedFilebutton.setEnabled(true);
			txtKobedFileSICER.setEnabled(true);
			wtBedFilebutton.setEnabled(true);
			txtWtBedFileSICER.setEditable(true);
			KoColFilebutton.setEnabled(false);
			txtKoColFileSICER.setEnabled(false);
			wtcolFilebutton.setEnabled(false);
			txtWtcolFileSICER.setEnabled(false);
			wtPrefix.setEnabled(true); 
			txtWtPrefixSICER.setEditable(true);
		}
		if (sicerType == PeakCallingSicerType.SICERdf) {
			kobedFilebutton.setEnabled(true);
			txtKobedFileSICER.setEnabled(true);
			wtBedFilebutton.setEnabled(true);
			txtWtBedFileSICER.setEditable(true);
			KoColFilebutton.setEnabled(true);
			txtKoColFileSICER.setEnabled(true);
			wtcolFilebutton.setEnabled(true);
			txtWtcolFileSICER.setEnabled(true);
		}
	}
	
	private void runMacs() {
		macs14control = new Macs14control();
		Species species = guiLayeredPanSpeciesVersion.getSelectSpecies();
		Double pvalue =Double.parseDouble(txtPvalue.getText()) ;
		int mfoldMax =  (Integer) mfoldMaxspinner.getValue();
		int mfoldMin = (Integer) mfoldminspinner.getValue();
		macs14control.setmfoldMax(mfoldMax);
		macs14control.setMfoldMin(mfoldMin);
		macs14control.setPathinput(txtInFileMacs.getText());
		macs14control.setpathinputColl(txtControlFileMacs.getText());
		macs14control.setPathoutput(txtSavePath.getText());
		macs14control.setPvalue(pvalue);
		macs14control.setSpecies(species);
		macs14control.peakCalling();
	}
	
	private void runSicer() {
		sicerControl = new SicerControl();
		sicerControl.setKoBedFile(txtKobedFileSICER.getText(), txtKoPrefixSICER.getText());
		sicerControl.setWtBedFile(txtWtBedFileSICER.getText(), txtWtPrefixSICER.getText());
		sicerControl.setKoControlFile(txtKoColFileSICER.getText());
		sicerControl.setWtControlFile(txtWtcolFileSICER.getText());
		
		sicerControl.setEvalue(100);
		sicerControl.setFDR(0.01);
		sicerControl.setSpecies(guiLayeredPanSpeciesVersion.getSelectSpecies());
		sicerControl.setSicerType(cmbSICERtype.getSelectedValue());
		
		sicerControl.setOutputDir(txtSavePath.getText());

		methylationType = cmbMethyTypeSicer.getSelectedValue();
		sicerControl.setMethylationType(methylationType);
		if (methylationType == SicerControl.METHY_UNKNOWN) {
			if (dialog.getWindowSize() == 0 && dialog.getGapSize() == 0) {
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setVisible(true);
				sicerControl.setMethylationType(dialog.getWindowSize(), dialog.getGapSize());
			} else {
				sicerControl.setMethylationType(dialog.getWindowSize(), dialog.getGapSize());
			}
		}
		sicerControl.peakCalling();
	}
	
	public static class GuiWinAndGsize extends JDialog {
		private static final long serialVersionUID = -7836621158081537978L;
		
		private final JPanel contentPanel = new JPanel();
		private JTextField windowSizetextField;
		private JTextField gapSizetextField;
		private int  windowSize;
		private int gapSize;
		
		/**
		 * Launch the application.
		 */
		public static void main(String[] args) {
			try {
				GuiWinAndGsize dialog= new GuiWinAndGsize();
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public void setWindowSize(int windowSize) {
			this.windowSize = windowSize;
		}
		
		public void setGapSize(int gapSize) {
			this.gapSize = gapSize;
		}
		
		public int getWindowSize() {
			return windowSize;
		}
		
		public int getGapSize() {
			return gapSize;
		}

		/**
		 * Create the dialog.
		 */
		public GuiWinAndGsize() {
			setBounds(100, 100, 450, 300);
			getContentPane().setLayout(new BorderLayout());
			contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
			getContentPane().add(contentPanel, BorderLayout.CENTER);
			contentPanel.setLayout(null);
			
			JLabel windowSizeLabel = new JLabel("windowSize");
			windowSizeLabel.setBounds(120, 72, 89, 31);
			contentPanel.add(windowSizeLabel);
			
			windowSizetextField = new JTextField();
			windowSizetextField.setBounds(212, 72, 127, 31);
			contentPanel.add(windowSizetextField);
			windowSizetextField.setColumns(10);
			
			JLabel lblGsize = new JLabel("gapSize");
			lblGsize.setBounds(120, 135, 89, 15);
			contentPanel.add(lblGsize);
			
			gapSizetextField = new JTextField();
			gapSizetextField.setColumns(10);
			gapSizetextField.setBounds(212, 127, 127, 31);
			contentPanel.add(gapSizetextField);
			{
				JPanel buttonPane = new JPanel();
				buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
				getContentPane().add(buttonPane, BorderLayout.SOUTH);
				{
					JButton okButton = new JButton("OK");
					okButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							windowSize = Integer.parseInt(windowSizetextField.getText());
							gapSize = Integer.parseInt(gapSizetextField.getText());
							System.out.println(windowSize);
							System.out.println(gapSize);
							setVisible(false);
						}
					});
					okButton.setActionCommand("OK");
					buttonPane.add(okButton);
					getRootPane().setDefaultButton(okButton);
				}
				{
					JButton cancelButton = new JButton("Cancel");
					cancelButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							setVisible(false);
							
//							System.exit(1);
						}
					});
					cancelButton.setActionCommand("Cancel");
					buttonPane.add(cancelButton);
				}
			}
		}
	}

}
