package com.novelbio.report.generateReport;

import java.util.Map;

import com.novelbio.analysis.annotation.functiontest.FunctionTest;
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
		
		Map<String, FunctionTest> mapPrefix2FunTest = ctrlCOG.getMapResult_Prefix2FunTest();
		Species species = new Species(ctrlCOG.getTaxID());
		reportCOG.setSpecies(species);
		
	}

}
