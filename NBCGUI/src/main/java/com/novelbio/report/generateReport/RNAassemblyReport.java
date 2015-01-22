package com.novelbio.report.generateReport;

import com.novelbio.nbcgui.controlseq.CtrlTrinity;
import com.novelbio.report.Params.ReportRNAassembly;

public class RNAassemblyReport {
	
	private ReportRNAassembly reportRNAassembly = new ReportRNAassembly();
	
	public RNAassemblyReport(CtrlTrinity ctrlTrinity) {
		generateReport(ctrlTrinity);
	}
	
	public ReportRNAassembly getReportRNAassembly() {
		return reportRNAassembly;
	}
	
	public void generateReport(CtrlTrinity ctrlTrinity) {

	}

}
