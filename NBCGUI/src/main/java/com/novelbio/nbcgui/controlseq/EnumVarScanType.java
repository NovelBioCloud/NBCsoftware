package com.novelbio.nbcgui.controlseq;

public enum EnumVarScanType {

	//Single-sample Calling
	pileup2snp,
	pileup2indel,
	pileup2cns,
	//Multi-sample Calling:
	mpileup2snp,
	mpileup2indel,
	mpileup2cns,
	//Tumor-normal Comparison:
	somatic,
	//copynumber CNV检测:
	copynumber,
	//filter:
	filter,
	//somaticFilter:
	somaticFilter,
	//compare:
	compare,
}
