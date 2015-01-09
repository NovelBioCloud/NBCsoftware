package com.novelbio.report.generateReport;

import com.novelbio.database.model.species.Species;
import com.novelbio.nbcgui.controltest.CtrlCOG;
import com.novelbio.report.Params.ReportCOG;

public class COGReport {
	
	private ReportCOG reportCOG = new ReportCOG();
	
	public COGReport(CtrlCOG ctrlCOG) {
		generateReport(ctrlCOG);
	}
	
	public ReportCOG getReportCOG() {
		return reportCOG;
	}
	
	public void generateReport(CtrlCOG ctrlCOG) {
		
		Species species = new Species(ctrlCOG.getTaxID());
		reportCOG.setSpecies(species);
		
	}

}
