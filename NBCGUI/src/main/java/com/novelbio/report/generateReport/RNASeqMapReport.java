package com.novelbio.report.generateReport;

import com.novelbio.nbcgui.controlseq.CtrlRNAmap;
import com.novelbio.report.Params.ReportRNASeqMap;

public class RNASeqMapReport {
	
	private ReportRNASeqMap reportRNASeqMap = new ReportRNASeqMap();
	
	public RNASeqMapReport(CtrlRNAmap ctrlRNAmap) {
		generateReport(ctrlRNAmap);
	}
	
	public ReportRNASeqMap getReportRNASeqMap() {
		return reportRNASeqMap;
	}
	
	public void generateReport(CtrlRNAmap ctrlRNAmap) {
		reportRNASeqMap.setSoftware(ctrlRNAmap.getSoftWare());
		reportRNASeqMap.setSpeciesName(ctrlRNAmap.getSpecies());
	}

}
