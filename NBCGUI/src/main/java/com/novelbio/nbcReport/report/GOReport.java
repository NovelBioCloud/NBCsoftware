package com.novelbio.nbcReport.report;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.novelbio.analysis.annotation.functiontest.FunctionTest;
import com.novelbio.analysis.annotation.functiontest.StatisticTestResult;
import com.novelbio.base.word.NBCWordImage;
import com.novelbio.database.domain.geneanno.GOtype;
import com.novelbio.database.model.species.Species;
import com.novelbio.nbcReport.Params.ReportGO;
import com.novelbio.nbcReport.Params.ReportGOAll;
import com.novelbio.nbcReport.Params.ReportGOCluster;
import com.novelbio.nbcReport.Params.ReportGOClusterType;
import com.novelbio.nbcReport.Params.ReportGOUpDown;
import com.novelbio.nbcgui.controltest.CtrlGO;
import com.novelbio.nbcgui.controltest.CtrlGOPath;
import com.novelbio.nbcgui.controltest.CtrlGOall;


public class GOReport {
	CtrlGOall ctrlGOall;
	/** 结果报告 */
	ReportGO reportGO = new ReportGO();
	ReportGOAll reportGOAll;
	ReportGOUpDown reportGOUpDown;
	ReportGOCluster reportGOCluster;
	Map<String, ReportGOClusterType> mapPrefix2ReportCluster = new HashMap<>();
	
	public void setCtrlGOall(CtrlGOall ctrlGOall) {
		this.ctrlGOall = ctrlGOall;
	}
	
	public ReportGO getReportGO() {
		return reportGO;
	}

	public void generateReport() {
		for (CtrlGO ctrlGO : ctrlGOall.mapGOtype2CtrlGO.values()) {
			saveReportGoAll(ctrlGO);
			if (!ctrlGOall.isCluster() && !ctrlGOall.isJustAll()) {
				saveReportGoNorm(ctrlGO);
			} else if (!ctrlGOall.isCluster() && !ctrlGOall.isJustAll()) {
				saveReportGoCluster(ctrlGO);
			}
		}
		for (String prefix : ctrlGOall.mapPrefix2ResultPic.keySet()) {
			String picName = ctrlGOall.mapPrefix2ResultPic.get(prefix);
			NBCWordImage image = new NBCWordImage();
			image.addPicPath(picName);
			
			if (prefix.equals(CtrlGOPath.All) && picName != null) {
				reportGOAll.addNBCWordImage(image);
			} else if (!ctrlGOall.isCluster()) {
				reportGOUpDown.addNBCWordImage(prefix.equals(CtrlGOPath.Up), image);
			} else {
				ReportGOClusterType reportGOClusterType = mapPrefix2ReportCluster.get(prefix);
				if (reportGOClusterType != null) {
					reportGOClusterType.addNBCWordImage(image);
				}
			}
		}
		reportGO.setTestMethod(ctrlGOall.getGoAlgorithm().toString());
		reportGO.setTeamName(ctrlGOall.getSavePrefix());
		Species species = new Species(ctrlGOall.getTaxID());
		reportGO.setSpeciesName(species);
		List<Species> lsBlastTo = new ArrayList<>();
		ctrlGOall.getBlastTaxID();
		for (int taxid : ctrlGOall.getBlastTaxID()) {
			lsBlastTo.add(new Species(taxid));
		}
		if (reportGOAll != null) {
			reportGOAll.setSpecies(species);
			reportGOAll.setBlastToSpecies(lsBlastTo);
			reportGO.addChildReport(reportGOAll);
		}
		if (reportGOUpDown != null) {
			reportGOUpDown.setSpecies(species);
			reportGOUpDown.setBlastToSpecies(lsBlastTo);
			reportGO.addChildReport(reportGOUpDown);
		}
		//看是否放入gocluster
		if (!mapPrefix2ReportCluster.isEmpty()) {
			List<ReportGOClusterType> lsClusterTypes = new ArrayList<>(mapPrefix2ReportCluster.values());
			Collections.sort(lsClusterTypes, new Comparator<ReportGOClusterType>() {
				public int compare(ReportGOClusterType o1, ReportGOClusterType o2) {
					Integer o1num = o1.getGeneNum();
					Integer o2num = o2.getGeneNum();
					return -o1num.compareTo(o2num);
				}
			});
			int num = 0;
			for (ReportGOClusterType reportGOClusterType : lsClusterTypes) {
				if (num++ > 3) {
					break;
				}
				reportGOCluster.addChildReport(reportGOClusterType);
			}
			reportGO.addChildReport(reportGOCluster);
		}
	}
	
	private void saveReportGoAll(CtrlGO ctrlGO) {
		FunctionTest functionTest = ctrlGO.getMapResult_Prefix2FunTest().get(CtrlGOPath.All);
		if (functionTest == null || functionTest.getTestResult().isEmpty()) return;
		
		if (reportGOAll == null) reportGOAll = new ReportGOAll();
		
		reportGOAll.setDifGeneNum(ctrlGO.getUpAndDownRegulation()[2]);

		List<StatisticTestResult> lsTestResult = functionTest.getTestResult();
		int sigTermNum = getSigTermNum(lsTestResult);
		
		reportGOAll.setGoSigNum(ctrlGO.getGOType(), sigTermNum);
		
		for (int i = 0; i < sigTermNum; i++) {
			if (i >= 3) break;
			StatisticTestResult statisticTestResult = lsTestResult.get(i);
			reportGOAll.setGoTerm_Num(ctrlGO.getGOType(), i, statisticTestResult.getItemTerm(),
					statisticTestResult.getDifGeneInItemNum(), statisticTestResult.getPvalue());
		}
	}
	
	private void saveReportGoNorm(CtrlGO ctrlGO) {
		if (reportGOUpDown == null) reportGOUpDown = new ReportGOUpDown();

		reportGOUpDown.setUpDownNum(ctrlGO.getUpAndDownRegulation()[0], ctrlGO.getUpAndDownRegulation()[1]);
		FunctionTest functionTestUp = ctrlGO.getMapResult_Prefix2FunTest().get(CtrlGOPath.Up);
		saveReportGoNorm(true, ctrlGO.getGOType(), functionTestUp);
		
		FunctionTest functionTestDown = ctrlGO.getMapResult_Prefix2FunTest().get(CtrlGOPath.Down);
		saveReportGoNorm(true, ctrlGO.getGOType(), functionTestDown);
	}
	
	/** 是否为上调 */
	private void saveReportGoNorm(boolean up, GOtype goType, FunctionTest functionTest) {
		if (functionTest == null || functionTest.getTestResult().isEmpty()) return;
		
		List<StatisticTestResult> lsTestResult = functionTest.getTestResult();
		int sigTermNum = getSigTermNum(lsTestResult);
		reportGOUpDown.setGoSigNum(up, goType, getSigTermNum(lsTestResult));
		
		for (int i = 0; i < sigTermNum; i++) {
			if (i >= 3) break;
			StatisticTestResult statisticTestResult = lsTestResult.get(i);
			reportGOUpDown.setGoTerm_Num( goType, up, i, statisticTestResult.getItemTerm(),
					statisticTestResult.getDifGeneInItemNum(), statisticTestResult.getPvalue());
		}
	}
	
	private void saveReportGoCluster(CtrlGO ctrlGO) {
		for (String prefix : ctrlGO.getMapResult_Prefix2FunTest().keySet()) {
			saveReportGoCluster(prefix, ctrlGO.getGOType(), ctrlGO.getMapResult_Prefix2FunTest().get(prefix));
		}
	}
	
	/**
	 * @param prefix
	 * @param goType
	 * @param functionTest
	 */
	private void saveReportGoCluster(String prefix, GOtype goType, FunctionTest functionTest) {
		if (functionTest == null || functionTest.getTestResult().isEmpty()) return;
		List<StatisticTestResult> lsTestResult = functionTest.getTestResult();
		ReportGOClusterType reportGOClusterType = mapPrefix2ReportCluster.get(prefix);
		if (reportGOClusterType == null) {
			reportGOClusterType = new ReportGOClusterType();
			mapPrefix2ReportCluster.put(prefix, reportGOClusterType);
		}
		reportGOClusterType.setGeneNum(functionTest.getAllDifGeneNum());
		int sigTermNum = getSigTermNum(lsTestResult);
		for (int i = 0; i < sigTermNum; i++) {
			if (i >= 3) break;
			StatisticTestResult statisticTestResult = lsTestResult.get(i);
			reportGOClusterType.setGoTerm_Num( goType, i, statisticTestResult.getItemTerm(),
					statisticTestResult.getDifGeneInItemNum(), statisticTestResult.getPvalue());
		}
	}
	
	private int getSigTermNum(List<StatisticTestResult> lsTestResult) {
		int sigTermNum = 0;
		for (StatisticTestResult statisticTestResult : lsTestResult) {
			if (statisticTestResult.getPvalue() <= FunctionTest.PvalueFdr_Cutoff) {
				sigTermNum++;
			}
		}
		return sigTermNum;
	}
	
}
