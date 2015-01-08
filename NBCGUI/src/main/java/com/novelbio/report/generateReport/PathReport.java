package com.novelbio.report.generateReport;

import java.util.Map;

import com.novelbio.analysis.annotation.functiontest.FunctionTest;
import com.novelbio.analysis.annotation.functiontest.StatisticTestResult;
import com.novelbio.database.model.species.Species;
import com.novelbio.nbcgui.controltest.CtrlGOPath;
import com.novelbio.nbcgui.controltest.CtrlGOall;
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
		
		Map<String, FunctionTest> mapResult_prefix2FunTest = ctrlPath.getMapResult_Prefix2FunTest();
//		
//		int sigTermNum = GOReport.getSigTermNum(functionTest)
		
//		CtrlGOall tr = null;
//		FunctionTest ff = tr.getMapResult_Prefix2FunTest().values().iterator().next().getMapResult_Prefix2FunTest().values().iterator().next();
//		ff.getAllDifGeneNum();//328 genes were annotated based on Vitis vinifera 
//		GOReport.getSigTermNum(ff);//16  pathways categories were detected to be significant enriched in as sho
		reportPathWay = new ReportPathWay();
		
		reportPathWay.setDb("KEGG");
		reportPathWay.setUpRegulation(ctrlPath.getUpAndDownRegulation()[0]);
		reportPathWay.setDownRegulation(ctrlPath.getUpAndDownRegulation()[1]);
		reportPathWay.setDifGeneNum(ctrlPath.getUpAndDownRegulation()[2]);
		reportPathWay.setFinderCondition(ctrlPath.getFinderCondition());
		
//		StatisticTestResult statisticTestResult = functionTestUp.ge
//		
//		for (int i = 0; i < GOReport.getSigTermNum(ff); i++) {
//			if (i >= 3) break;
//			StatisticTestResult statisticTestResult = ff.getTestResult().get(i);
//			GOReport.getSigTermNum(ff);
//			statisticTestResult.getItemTerm();
//			
//			reportGOResult.setGoTerm_Num( goType, i, statisticTestResult.getItemTerm(),
//					statisticTestResult.getDifGeneInItemNum(), statisticTestResult.getPvalue());
//		}
		
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
