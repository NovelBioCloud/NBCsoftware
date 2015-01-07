package com.novelbio.report.generateReport;

import com.novelbio.database.model.species.Species;
import com.novelbio.nbcgui.controltest.CtrlPath;
import com.novelbio.report.ReportImage;
import com.novelbio.report.Params.ReportPathWay;

public class PathReport {
	
	private ReportPathWay reportPathWay;

	public PathReport(CtrlPath ctrlPath) {
		generateReport(ctrlPath);
	}
	
	public ReportPathWay getReportPathWay() {
		return reportPathWay;
	}

	public void generateReport(CtrlPath ctrlPath) {
		
		reportPathWay = new ReportPathWay();
		
		reportPathWay.setDb("KEGG");
		reportPathWay.setUpRegulation(ctrlPath.getUpAndDownRegulation()[0]);
		reportPathWay.setDownRegulation(ctrlPath.getUpAndDownRegulation()[1]);
		reportPathWay.setFinderCondition(ctrlPath.getFinderCondition());
		
		//添加物种
		Species species = new Species(ctrlPath.getTaxID());
		reportPathWay.setSpecies(species);
		
		reportPathWay.setTeamName(ctrlPath.getSavePrefix());
		
		//添加图片
		for (String resultPic : ctrlPath.getLsResultPic()) {
			ReportImage reportImage = new ReportImage();
			reportImage.addImgPath(resultPic);
			reportPathWay.addReportImage(reportImage);
		}
		
	}

}
