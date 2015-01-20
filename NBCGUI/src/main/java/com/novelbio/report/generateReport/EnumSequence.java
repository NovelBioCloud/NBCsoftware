package com.novelbio.report.generateReport;

public enum EnumSequence {
	
	HiSeq("Illumina HiSeq"),
	Proton("Ion Proton");
	
	String sequence = null;
	EnumSequence(String sequence) {
		this.sequence = sequence;
	}
	
	@Override
	public String toString() {
		return sequence;
	}

}
