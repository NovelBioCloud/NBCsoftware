package com.novelbio.nbcgui.controlseq;

import java.util.ArrayList;
import java.util.List;

import com.novelbio.base.cmd.CmdOperate;
import com.novelbio.base.dataStructure.ArrayOperate;
import com.novelbio.base.fileOperate.FileOperate;


/**
 * simple script :
 * 		Single-sample Calling:
 * 						 java -jar VarScan.jar pileup2snp [pileup file] OPTIONS 
 * 							pileup file - The SAMtools pileup file
 * 						 java -jar VarScan.jar pileup2indel [pileup file] OPTIONS  
 * 							pileup file - The SAMtools pileup file
 *						 java -jar VarScan.jar pileup2cns [pileup file] OPTIONS  
 *							pileup file - The SAMtools pileup file
 * 		Multi-sample Calling:
 *						 java -jar VarScan.jar mpileup2snp [mpileup file] OPTIONS 
 *							mpileup file - The SAMtools mpileup file
 * 					 	 java -jar VarScan.jar mpileup2indel [mpileup file] OPTIONS 
 * 							mpileup file - The SAMtools mpileup file
 * 						 java -jar VarScan.jar mpileup2cns [mpileup file] OPTIONS 
 * 							mpileup file - The SAMtools mpileup file
 * 		Tumor-normal Comparison:
 * 						java -jar VarScan.jar somatic [normal_pileup] [tumor_pileup] [output] OPTIONS 
 * 							normal_pileup - The SAMtools pileup file for Normal
 * 							tumor_pileup - The SAMtools pileup file for Tumor 
 * 							output - Output base name for SNP and indel output
 * 		copynumber CNV检测:
 * 						java -jar VarScan.jar copynumber [normal_pileup] [tumor_pileup] [output] OPTIONS
 * 							normal_pileup - The SAMtools pileup file for Normal
 * 							tumor_pileup - The SAMtools pileup file for Tumor
 * 							output - Output base name for SNP and indel output
 * 		filter:
 * 						java -jar VarScan.jar filter [variants file] OPTIONS
 * 							variants file - A file of SNP or indel calls from VarScan pileup2snp or pileup2indel
 * 		somaticFilter:
 * 					  java -jar VarScan.jar somaticFilter [mutations file] OPTIONS
 * 						   mutations file - A file of SNVs from VarScan somatic
 * 		compare:
 * 					 java -jar VarScan.jar compare [file1] [file2] [type] [output] OPTIONS
 * 							file1 - A file of chromosome-positions, tab-delimited
 * 							file2 - A file of chromosome-positions, tab-delimited
 *							type - Type of comparison [intersect|merge|unique1|unique2]
 * 							output - Output file for the comparison result
 * @author bll
 *
 */
public class CtrlVarScan {
//public class CtrlSnpVarscan  implements IntCmdSoft {
	String exePath = "";	
	/** 输入文件*/
	String inputFile;
	//Single-sample Calling: 使用 pileup2snp  pileup2indel  pileup2cns
	/** Minimum read depth at a position to make a call [8]*/
	int minCoverage;
	/** Minimum supporting reads at a position to call variants [2]*/
	int minReads2;
	/** Minimum base quality at a position to count a read [15]*/
	int minAvgQual;
	/** Minimum variant allele frequency threshold [0.01]*/
	double minVarFreq;
	/** Default p-value threshold for calling variants [0.99]*/
	double pValue;
		
	// Multi-sample Calling: 使用 mpileup2snp  mpileup2indel  mpileup2cns；此外，除了以上参数以外，添加了如下参数
	/** Minimum frequency to call homozygote [0.75]*/
	double minFreqForHom;
	/** Ignore variants with >90% support on one strand [1] */
	int strandFilter;
	/** If set to 1, outputs in VCF format */
	int outoutVcf;
	/** Report only variant (SNP/indel) positions (mpileup2cns only) [0] */
	int variants;

	// Tumor-normal Comparison: 使用 somatic 此外，除了以上参数以外，添加了如下参数
	/** Output file for SNP calls [default: output.snp] */
	String outputSnp;
	/** Output file for indel calls [default: output.indel] */
	String outputIndel;
	/** Minimum coverage in normal to call somatic [8] */
	int minCoverNormal;
	/** Minimum coverage in tumor to call somatic [6] */
	int minCoverTumor;
	/** P-value threshold to call a somatic site [0.05] */
	double somaPValue;
	/** If set to 1, outputs all compared positions even if non-variant */
	int validation;

	// copynumber CNV检测: 使用 copynumber ，除了以上部分参数以外，添加了如下参数
	/** Minimum base quality to count for coverage [20] */
	int minBaseQual;
	/** Minimum read mapping quality to count for coverage [20] */
	int minMapQual;
	/** Minimum number of consecutive bases to report a segment [10] */
	int minSegSize;
	/** Max size before a new segment is made [100] */
	int maxSegSize;
	/** The normal/tumor input data ratio for copynumber adjustment [1.0] */
	int dataRatio;
	// filter:
	/** Minimum # of strands on which variant observed (1 or 2) [1] */
	int minStrands2;
	/** File of indels for filtering nearby SNPs, from pileup2indel command */
	String indelFile;
	/** File to contain variants passing filters */
	String outputFile;

	// compare:
	/** Type of comparison [intersect|merge|unique1|unique2] */
	String comType;

	public void setInputFile(String inputFile) {
		FileOperate.checkFileExistAndBigThanSize(inputFile, 0);
		this.inputFile = inputFile;
	}

	public void setMinCoverage(int minCoverage) {
		this.minCoverage = minCoverage;
	}

	public void setMinReads2(int minReads2) {
		this.minReads2 = minReads2;
	}

	public void setMinAvgQual(int minAvgQual) {
		this.minAvgQual = minAvgQual;
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

	public void setStrandFilter(int strandFilter) {
		this.strandFilter = strandFilter;
	}

	public void setOutoutVcf(int outoutVcf) {
		this.outoutVcf = outoutVcf;
	}

	public void setVariants(int variants) {
		this.variants = variants;
	}

	public void setMinCoverNormal(int minCoverNormal) {
		this.minCoverNormal = minCoverNormal;
	}

	public void setMinCoverTumor(int minCoverTumor) {
		this.minCoverTumor = minCoverTumor;
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

	public void setValidation(int validation) {
		this.validation = validation;
	}

	public void setMinBaseQual(int minBaseQual) {
		this.minBaseQual = minBaseQual;
	}

	public void setMinMapQual(int minMapQual) {
		this.minMapQual = minMapQual;
	}

	public void setMinSegSize(int minSegSize) {
		this.minSegSize = minSegSize;
	}

	public void setMaxSegSize(int maxSegSize) {
		this.maxSegSize = maxSegSize;
	}

	public void setDataRatio(int dataRatio) {
		this.dataRatio = dataRatio;
	}

	public void setMinStrands2(int minStrands2) {
		this.minStrands2 = minStrands2;
	}

	public void setIndelFile(String indelFile) {
		this.indelFile = indelFile;
	}

	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}

	public void setComType(String comType) {
		this.comType = comType;
	}

	public CtrlVarScan() {
		// SoftWareInfo softWareInfo = new SoftWareInfo(SoftWare.varscan);
		// this.exePath = softWareInfo.getExePathRun();
		this.exePath = exePath;
	}

	public void run() {
		CmdOperate cmdOperate = new CmdOperate(getCmdExeStr());
		cmdOperate.runWithExp("VarScan error:");
	}

	List<String> lsCmd = new ArrayList<>();

	public List<String> getCmdExeStr() {
		// TODO Auto-generated method stub
		lsCmd.add(exePath + "ESTScan");
		ArrayOperate.addArrayToList(lsCmd, getInputFile(inputFile));
		// ArrayOperate.addArrayToList(lsCmd, getMinMatrixValue());
		// ArrayOperate.addArrayToList(lsCmd, getScoreMatFile());
		// ArrayOperate.addArrayToList(lsCmd, getMinCDSLength());
		// ArrayOperate.addArrayToList(lsCmd, getPosStrand());
		// ArrayOperate.addArrayToList(lsCmd, getSkipMinLen());
		// ArrayOperate.addArrayToList(lsCmd, getPepFile());
		// ArrayOperate.addArrayToList(lsCmd, getCdsResultFile());
		return lsCmd;
	}

	private String[] getInputFile(String inputFile) {
		return new String[] { inputFile };
	}

	private String[] getMinCoverage() {
		return new String[] { "--min-coverage", minCoverage + "" };
	}

	private String[] getMinReads2() {
		return new String[] { "--min-reads2", minReads2 + "" };
	}

	private String[] getMinAvgQual() {
		return new String[] { "--min-avg-qua", minAvgQual + "" };
	}

	private String[] getMinVarFreq() {
		return new String[] { "--min-var-freq", minVarFreq + "" };
	}

	private String[] getPValue() {
		return new String[] { "--p-value", pValue + "" };
	}
}
