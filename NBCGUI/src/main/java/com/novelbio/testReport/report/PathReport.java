package com.novelbio.testReport.report;

import com.novelbio.nbcgui.controltest.CtrlPath;
import com.novelbio.testReport.Params.ReportBase;
import com.novelbio.testReport.Params.ReportPathWay;

public class PathReport {
	
	private CtrlPath ctrlPath;
	
	private ReportBase reportPathWay;
	
	public void generateReport() {
		reportPathWay = ctrlPath.getReportPathWay();
	}

}
