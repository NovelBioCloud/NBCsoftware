package com.novelbio.nbcgui.controlseq;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.novelbio.analysis.annotation.genanno.AnnoAbs;
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
	//Single-sample Calling
	public static final int PILEUP2SNP = 1234;	
	public static final int PILEUP2INDEL= 1345;	
	public static final int PILEUP2CNS = 1456;	
	//Multi-sample Calling:
	public static final int MPILEUP2SNP = 2345;	
	public static final int MPILEUP2INDEL = 2456;
	public static final int MPILEUP2CNS = 2567;	
	//Tumor-normal Comparison:
	public static final int SOMATIC = 3456;	
	//copynumber CNV检测:
	public static final int COPYNUMBER = 4567;
	//filter:
	public static final int FILTER = 5678;
	//somaticFilter:
	public static final int SOMATICFILTER = 6789;
	//compare:
	public static final int COMPARE = 7890;

	String exePath = "";
	int varScanType;
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
	int outputVcf;
	/** For VCF output, a list of sample names in order, one per line */
	List<String> vcfSamList;
	/** Report only variant (SNP/indel) positions (mpileup2cns only) [0] */
	int variants;

	// Tumor-normal Comparison: 使用 somatic 此外，除了以上参数以外，添加了如下参数
	/** Output file for SNP calls [default: output.snp] */
	String outputSnp;
	/** Output file for indel calls [default: output.indel] */
	String outputIndel;
	/** Minimum coverage in normal to call somatic [8] */
	int minCovNor;
	/** Minimum coverage in tumor to call somatic [6] */
	int minCovTum;
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

	public void setVarScanType(int varScanType) {
		this.varScanType = varScanType;
	}
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

	public void setOutoutVcf(int outputVcf) {
		this.outputVcf = outputVcf;
	}

	public void setVariants(int variants) {
		this.variants = variants;
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
	//TODO
	protected static Map<String, String> decompressFile(Map<String, String> mapPrefix2InputFile, Map<String, String> mapPrefix2DecFile) {
		if (mapPrefix2InputFile.size() == 0) {
			return null;
		}

		return mapPrefix2DecFile;
	}
	List<String> lsCmd = new ArrayList<>();
	public List<String> getCmdExeStr() {
		// TODO Auto-generated method stub
		Map<String, Integer> mapVarType2Type = getVarScanType();
		for (String type : mapVarType2Type.keySet()) {
			if (varScanType == mapVarType2Type.get(type)) {
				lsCmd.add("java tar " + exePath + "VarScan.jar" + type);
				ArrayOperate.addArrayToList(lsCmd, getInputFile(inputFile));
				if (type.startsWith("pileup2") || (type.startsWith("mpileup2"))) {
					ArrayOperate.addArrayToList(lsCmd, getMinCoverage());
					ArrayOperate.addArrayToList(lsCmd, getMinReads2());
					ArrayOperate.addArrayToList(lsCmd, getMinAvgQual());
					ArrayOperate.addArrayToList(lsCmd, getMinFreqForHom());
					ArrayOperate.addArrayToList(lsCmd, getPValue());
					ArrayOperate.addArrayToList(lsCmd, getVariants());
					ArrayOperate.addArrayToList(lsCmd, getOutputFile());
					if (type.startsWith("mpileup2")) {
//						ArrayOperate.addArrayToList(lsCmd, getOutputVcf());
						ArrayOperate.addArrayToList(lsCmd, getStrandFilter());
						//TODO 添加vcf-sample-list 参数
//						ArrayOperate.addArrayToList(lsCmd, get());
					}
				} else if (type.equals("somatic")) {
					ArrayOperate.addArrayToList(lsCmd, getMinCoverage());
					ArrayOperate.addArrayToList(lsCmd, getMinCovNor());
					ArrayOperate.addArrayToList(lsCmd, getMinCovTum());
					ArrayOperate.addArrayToList(lsCmd, getMinVarFreq());
					ArrayOperate.addArrayToList(lsCmd, getMinFreqForHom());
					ArrayOperate.addArrayToList(lsCmd, getPValue());
					ArrayOperate.addArrayToList(lsCmd, getSomaPValue());
					ArrayOperate.addArrayToList(lsCmd, getStrandFilter());
					ArrayOperate.addArrayToList(lsCmd, getOutputSnp());
					ArrayOperate.addArrayToList(lsCmd, getOutputIndel());
				} else if (type.equals("copynumber")) {
					ArrayOperate.addArrayToList(lsCmd, getMinBaseQual());
					ArrayOperate.addArrayToList(lsCmd, getMinMapQual());
					ArrayOperate.addArrayToList(lsCmd, getMinCoverage());
					ArrayOperate.addArrayToList(lsCmd, getMinSegSize());
					ArrayOperate.addArrayToList(lsCmd, getMaxSegSize());
					ArrayOperate.addArrayToList(lsCmd, getPValue());
				} else if (type.equals("filter") || type.equals("somaticFilter")) {
					ArrayOperate.addArrayToList(lsCmd, getMinCoverage());
					ArrayOperate.addArrayToList(lsCmd, getMinReads2());
					ArrayOperate.addArrayToList(lsCmd, getMinStrands2());
					ArrayOperate.addArrayToList(lsCmd, getMinVarFreq());
					ArrayOperate.addArrayToList(lsCmd, getPValue());
					ArrayOperate.addArrayToList(lsCmd, getIndelFile());
					ArrayOperate.addArrayToList(lsCmd, getOutputFile());
					if (type.equals("filter")) {
						ArrayOperate.addArrayToList(lsCmd, getMinAvgQual());
					}
				} else if (type.equals("compare")) {
					ArrayOperate.addArrayToList(lsCmd, getComType());
					ArrayOperate.addArrayToList(lsCmd, getOutputFile());
				} 
			}
			
		}
		return lsCmd;
	}
	
	/**
	 * key是mapping的内容<br>
	 * value是mapping的int代号
	 * @return
	 */
	public static Map<String, Integer> getVarScanType() {
		Map<String, Integer> mapVarType2Type = new LinkedHashMap<>();
		mapVarType2Type.put("pileup2snp", CtrlVarScan.PILEUP2SNP);
		mapVarType2Type.put("pileup2indel", CtrlVarScan.PILEUP2INDEL);
		mapVarType2Type.put("pileup2cns", CtrlVarScan.PILEUP2CNS);
		mapVarType2Type.put("mpileup2snp", CtrlVarScan.MPILEUP2SNP);
		mapVarType2Type.put("mpileup2indel", CtrlVarScan.MPILEUP2INDEL);
		mapVarType2Type.put("mpileup2cns", CtrlVarScan.MPILEUP2CNS);
		mapVarType2Type.put("somatic", CtrlVarScan.SOMATIC);
		mapVarType2Type.put("copynumber", CtrlVarScan.COPYNUMBER);
		mapVarType2Type.put("filter", CtrlVarScan.FILTER);
		mapVarType2Type.put("somaticFilter", CtrlVarScan.SOMATICFILTER);
		mapVarType2Type.put("compare", CtrlVarScan.COMPARE);
		return mapVarType2Type;
	}

	private List<String> getVcfSamList() {
		return vcfSamList;
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
	private String[] getVariants() {
		return new String[] { "--variants", variants + "" };
	}
	private String[] getMinFreqForHom() {
		return new String[] { " ", minFreqForHom + "" };
	}
	private String[] getStrandFilter() {
		return new String[] { " ", strandFilter + "" };
	}	
	private String[] getOutputVcf() {
		return new String[] { " ", outputVcf + "" };
	}
	
	private String[] getOutputSnp() {
		return new String[] { " ", outputSnp};
	}
	
	private String[] getOutputIndel() {
		return new String[] { " ", outputIndel};
	}
	
	private String[] getMinCovNor() {
		return new String[] { " ", minCovNor + ""};
	}
	
	private String[] getMinCovTum() {
		return new String[] { " ", minCovTum + ""};
	}
	
	private String[] getSomaPValue() {
		return new String[] { " ", somaPValue + ""};
	}
	
	private String[] getValidation() {
		return new String[] { " ", validation + ""};
	}
	
	private String[] getMinBaseQual() {
		return new String[] { " ", minBaseQual + ""};
	}
	
	private String[] getMinMapQual() {
		return new String[] { " ", minMapQual + ""};
	}
	
	private String[] getMinSegSize() {
		return new String[] { " ", minSegSize + ""};
	}
	
	private String[] getMaxSegSize() {
		return new String[] { " ", maxSegSize + ""};
	}
	
	private String[] getDataRatio() {
		return new String[] { " ", dataRatio + ""};
	}
	private String[] getMinStrands2() {
		return new String[] { " ", minStrands2 + ""};
	}
	private String[] getIndelFile() {
		return new String[] { " ", indelFile + ""};
	}
	private String[] getOutputFile() {
		return new String[] { ">", outputFile + ""};
	}
	private String[] getComType() {
		return new String[] { " ", comType + ""};
	}
}
