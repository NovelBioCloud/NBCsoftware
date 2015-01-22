package com.novelbio.report.Params;

import com.novelbio.report.generateReport.ProjectReport;

public enum EnumAnalysisType {
	
	GOAnalysis(ProjectReport.PRELIMINARY),
	GoPathway(ProjectReport.PRELIMINARY),
	DiffExp(ProjectReport.PRELIMINARY),
	FastQC(ProjectReport.RAWDTA),
	RNAAlternativeSplicing(ProjectReport.INDEPTH);
	
	String analysisType = null;
	EnumAnalysisType(String analysisType) {
		this.analysisType = analysisType;
	}
	
	@Override
	public String toString() {
		return analysisType;
	}


}
