package com.novelbio.nbcgui.controlseq;

import java.util.ArrayList;
import java.util.List;

import com.novelbio.analysis.IntCmdSoft;
import com.novelbio.base.cmd.CmdOperate;
import com.novelbio.base.dataStructure.ArrayOperate;
import com.novelbio.database.domain.information.SoftWareInfo;
import com.novelbio.database.domain.information.SoftWareInfo.SoftWare;

public class CtrlVarScanSomatic implements IntCmdSoft {
	
	String exePath = "";
	String varScanCom = "";
	
	/** 输入文件*/
	String inputFile;
	/** Minimum read depth at a position to make a call [8]*/
	int minCoverage;
	/** Minimum coverage in normal to call somatic [6] */
	int minCovNor;
	/** Minimum coverage in tumor to call somatic [6] */
	int minCovTum;
	/** P-value threshold to call a somatic site [0.05] */
	double somaPValue;
	/** Minimum variant allele frequency threshold [0.01]*/
	double minVarFreq;
	/** Default p-value threshold for calling variants [0.99]*/
	double pValue;
	/** Minimum frequency to call homozygote [0.75]*/
	double minFreqForHom;
	/** If set to 1, outputs in VCF format */
	int outputVcf;
	/** Output file for SNP calls [default: output.snp] */
	String outputSnp;
	/** Output file for indel calls [default: output.indel] */
	String outputIndel;
	/** Minimum coverage in normal to call somatic [8] */
	
	public void setVarScanCom(String varScanCom) {
		this.varScanCom = varScanCom;
	}
	
	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}

	public void setMinCoverage(int minCoverage) {
		this.minCoverage = minCoverage;
	}

	public void setMinVarFreq(double minVarFreq) {
		this.minVarFreq = minVarFreq;
	}

	public void setPValue(double pValue) {
		this.pValue = pValue;
	}

	public void setMinFreqForHom(double minFreqForHom) {
		this.minFreqForHom = minFreqForHom;
	}

	public void setOutoutVcf(int outputVcf) {
		this.outputVcf = outputVcf;
	}

	public void setMinCovNor(int minCovNor) {
		this.minCovNor = minCovNor;
	}

	public void setMinCovTum(int minCovTum) {
		this.minCovTum = minCovTum;
	}

	public void setOutputIndel(String outputIndel) {
		this.outputIndel = outputIndel;
	}

	public void setOutputSnp(String outputSnp) {
		this.outputSnp = outputSnp;
	}

	public void setSomaPValue(double somaPValue) {
		this.somaPValue = somaPValue;
	}

	public CtrlVarScanSomatic() {
		 SoftWareInfo softWareInfo = new SoftWareInfo(SoftWare.varscan);
		 this.exePath = softWareInfo.getExePathRun();
	}
	public void run() {
		CmdOperate cmdOperate = new CmdOperate(getCmdExeStr());
	}
	List<String> lsCmd = new ArrayList<>();
	
	public List<String> getCmdExeStr() {
		lsCmd.clear();
		lsCmd.add("java -jar " + exePath + "varscan.jar" + " somatic");
		ArrayOperate.addArrayToList(lsCmd, getInputFile(inputFile));
		ArrayOperate.addArrayToList(lsCmd, getMinCoverage());
		ArrayOperate.addArrayToList(lsCmd, getMinCovNor());
		ArrayOperate.addArrayToList(lsCmd, getMinCovTum());
		ArrayOperate.addArrayToList(lsCmd, getMinVarFreq());
		ArrayOperate.addArrayToList(lsCmd, getMinFreqForHom());
		ArrayOperate.addArrayToList(lsCmd, getPValue());
		ArrayOperate.addArrayToList(lsCmd, getSomaPValue());
		ArrayOperate.addArrayToList(lsCmd, getOutputVcf());
		ArrayOperate.addArrayToList(lsCmd, getOutputSnp());
		ArrayOperate.addArrayToList(lsCmd, getOutputIndel());
		return lsCmd;
	}
	private String[] getInputFile(String inputFile) {
		return new String[]{inputFile};
	}
	private String[] getMinCoverage() {
		return new String[] { "--min-coverage", minCoverage + "" };
	}
	
	private String[] getMinCovNor() {
		return new String[] { "--min-coverage-normal", minCovNor + ""};
	}
	
	private String[] getMinCovTum() {
		return new String[] { "--min-coverage-tumor", minCovTum + ""};
	}
	private String[] getMinVarFreq() {
		return new String[] { "--min-var-freq", minVarFreq + "" };
	}
	private String[] getMinFreqForHom() {
		return new String[] { "--min-freq-for-hom", minFreqForHom + "" };
	}
	private String[] getPValue() {
		return new String[] { "--p-value", pValue + "" };
	}
	
	private String[] getSomaPValue() {
		return new String[] { "--min-coverage-tumor", somaPValue + ""};
	}
	private String[] getOutputVcf() {
		if (outputVcf != 1) {
			return null;
		}
		return new String[] { "--output-vcf", outputVcf + "" };
	}
	
	private String[] getOutputSnp() {
		return new String[] { "--output-snp", outputSnp};
	}
	
	private String[] getOutputIndel() {
		return new String[] { "--output-indel", outputIndel};
	}

}
