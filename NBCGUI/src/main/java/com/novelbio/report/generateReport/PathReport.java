package com.novelbio.report.generateReport;

import java.util.List;
import java.util.Map;

import com.novelbio.analysis.annotation.functiontest.FunctionTest;
import com.novelbio.analysis.annotation.functiontest.StatisticTestResult;
import com.novelbio.base.dataOperate.DateUtil;
import com.novelbio.database.model.species.Species;
import com.novelbio.nbcgui.controltest.CtrlPath;
import com.novelbio.report.ReportImage;
import com.novelbio.report.Params.ReportPathResult;
import com.novelbio.report.Params.ReportPathUpDown;
import com.novelbio.report.Params.ReportPathWay;

public class PathReport {
	
	// TODO 问张博，确定pathway模板
	
	private static final String PATHIMGTITLE = "Pathway Analysis of All Differentially expressed Genes";
	
	/** Pathway报告 */
	private ReportPathWay reportPathWay = new ReportPathWay();
	private ReportPathUpDown reportPathUpDown;
	
	public PathReport(CtrlPath ctrlPath) {
		generateReport(ctrlPath);
	}
	
	public ReportPathWay getReportPathWay() {
		return reportPathWay;
	}
	
	public void generateReport(CtrlPath ctrlPath) {
		
		Map<String, FunctionTest> mapPrefix2Funtest = ctrlPath.getMapResult_Prefix2FunTest();
		for (String prefix : mapPrefix2Funtest.keySet()) {
			ReportPathResult reportPathResult = new ReportPathResult();
			FunctionTest functionTest = mapPrefix2Funtest.get(prefix);
			reportPathResult.setAllDifNum(functionTest.getAllDifGeneNum());
			reportPathResult.setDifNum(functionTest.getAllGeneNum());
			reportPathResult.setSigTermNum(GOReport.getSigTermNum(functionTest));
			Species species = new Species(functionTest.getTaxID());
			reportPathResult.setSpeciesName(species);
			ReportImage reportImage = new ReportImage();
			reportImage.addImgPath(ctrlPath.getSavePicEnrichmentName(prefix));
			reportImage.addImgPath(ctrlPath.getSavePicPvalueName(prefix));
			reportImage.setImgTitle(PATHIMGTITLE);
			reportImage.setLabel(ReportImage.IMAGELABEL + DateUtil.getDateAndRandom());
			reportPathResult.addReportImage(reportImage);
			List<StatisticTestResult> lsTestResult = functionTest.getTestResult();
			reportPathResult.setItemTerm(lsTestResult.get(0).getItemTerm(), lsTestResult.get(1).getItemTerm(), lsTestResult.get(2).getItemTerm());
			reportPathWay.addSubReport(reportPathResult);
		}
		
		reportPathUpDown = new ReportPathUpDown();
		reportPathUpDown.setUpDownRegulation(ctrlPath.getUpAndDownRegulation()[0], ctrlPath.getUpAndDownRegulation()[1]);
		reportPathUpDown.setFinderCondition(ctrlPath.getFinderCondition());
		reportPathWay.addSubReport(reportPathUpDown);
		
	}
	
}
