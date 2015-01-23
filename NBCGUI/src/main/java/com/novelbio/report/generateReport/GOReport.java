package com.novelbio.report.generateReport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.novelbio.analysis.annotation.functiontest.FunctionTest;
import com.novelbio.analysis.annotation.functiontest.StatisticTestResult;
import com.novelbio.base.dataOperate.DateUtil;
import com.novelbio.database.domain.geneanno.GOtype;
import com.novelbio.database.model.species.Species;
import com.novelbio.nbcgui.controltest.CtrlGO;
import com.novelbio.nbcgui.controltest.CtrlGOPath;
import com.novelbio.nbcgui.controltest.CtrlGOall;
import com.novelbio.report.ReportImage;
import com.novelbio.report.Params.ReportGO;
import com.novelbio.report.Params.ReportGOAll;
import com.novelbio.report.Params.ReportGOCluster;
import com.novelbio.report.Params.ReportGOResult;
import com.novelbio.report.Params.ReportGOUpDown;

public class GOReport {
	static Logger logger = Logger.getLogger(GOReport.class);
	private static final String GOIMGTITLE = "The Gene Ontology Analysis on Differentially Expressed gene";

	/** 要显示的goTerm的个数 */
	private static final int goTermNum = 3;
	/** 要显示的goCluster的个数 */
	private static final int goClusterNum = 3;
	
	/** GO报告 */
	private ReportGO reportGO = new ReportGO();
	/** GOAll报告 */
	private ReportGOAll reportGOAll;
	/** GOUpDown报告 */
	private ReportGOUpDown reportGOUpDown;
	/** GOCluster报告 */
	private ReportGOCluster reportGOCluster;
	/** GOCluster的子报告，可以有all, up，down，cluster */
	private Map<String, ReportGOResult> mapPrefix2Report = new HashMap<>();
	/** 标志是否含有ReportGOAll */
	private boolean isHaveGOAll;
	
	public GOReport(CtrlGOall ctrlGOall) {
		generateReport(ctrlGOall);
	}
	
	public ReportGO getReportGO() {
		return reportGO;
	}

	/**生成报告*/
	public void generateReport(CtrlGOall ctrlGOall) {
		//初始化
		init(ctrlGOall);

		for (CtrlGO ctrlGO : ctrlGOall.mapGOtype2CtrlGO.values()) {
			setReportGoAll(ctrlGO);
			if (!ctrlGOall.isCluster() && !ctrlGOall.isJustAll()) {
				setReportGoUpDown(ctrlGO);
			}
			for (String prefix : ctrlGO.getMapResult_Prefix2FunTest().keySet()) {
				saveReportDetail(prefix, ctrlGO.getGOType(), ctrlGO.getMapResult_Prefix2FunTest().get(prefix));
			}
		}
		
		//添加结果图片
		for (String prefix : ctrlGOall.mapPrefix2ResultPic.keySet()) {
			String picName = ctrlGOall.mapPrefix2ResultPic.get(prefix);
			if (picName == null) continue;
		
			ReportImage reportImage = new ReportImage();
			reportImage.setImgTitle(GOIMGTITLE);
			String imgLabel = ReportImage.IMAGELABEL + DateUtil.getDateMSAndRandom();
			logger.info(imgLabel);
			reportImage.setLabel(imgLabel);
			reportImage.addImgPath(picName);
			ReportGOResult reportGOResult = mapPrefix2Report.get(prefix);
			if (reportGOResult != null) {
				reportGOResult.addReportImage(reportImage);
			}
		}
		
		Species species = new Species(ctrlGOall.getTaxID());
		//设置GO总报告的参数
		reportGO.setSpeciesName(species);
		reportGO.setTestMethod(ctrlGOall.getGoAlgorithm().toString());
		reportGO.setTreatName(ctrlGOall.getSavePrefix());
		
		List<Species> lsBlastTo = new ArrayList<>();
		for (int taxid : ctrlGOall.getBlastTaxID()) {
			lsBlastTo.add(new Species(taxid));
		}
		//设置GOAll报告的参数和子报告
		if (isHaveGOAll) {
			reportGOAll.setSpecies(species);
			reportGOAll.setBlastToSpecies(lsBlastTo);
			reportGOAll.addSubReport(mapPrefix2Report.get(CtrlGOPath.All));
			mapPrefix2Report.remove(CtrlGOPath.All);
			reportGO.addSubReport(reportGOAll);
		}
		//设置GOUpDown报告的参数和子报告
		if (reportGOUpDown != null) {
			reportGOUpDown.setSpecies(species);
			reportGOUpDown.setBlastToSpecies(lsBlastTo);
			reportGOUpDown.addSubReport(mapPrefix2Report.get(CtrlGOPath.Up));
			reportGOUpDown.addSubReport(mapPrefix2Report.get(CtrlGOPath.Down));
			mapPrefix2Report.remove(CtrlGOPath.Up);
			mapPrefix2Report.remove(CtrlGOPath.Down);
			reportGO.addSubReport(reportGOUpDown);
		}
		
		//看是否放入gocluster
		if (ctrlGOall.isCluster() && !mapPrefix2Report.isEmpty()) {
			reportGOCluster.setSpecies(species);
			reportGOCluster.setBlastToSpecies(lsBlastTo);
			reportGOCluster.setClusterTypeNum(mapPrefix2Report.size());
			List<ReportGOResult> lsClusterTypes = new ArrayList<>(mapPrefix2Report.values());
			Collections.sort(lsClusterTypes, new Comparator<ReportGOResult>() {
				public int compare(ReportGOResult o1, ReportGOResult o2) {
					Integer o1num = o1.getGeneNum();
					Integer o2num = o2.getGeneNum();
					return -o1num.compareTo(o2num);
				}
			});
			//仅展示clustergo 的前三个结果
			int num = 0;
			for (ReportGOResult reportGOClusterType : lsClusterTypes) {
				if (num++ >= goClusterNum) {
					break;
				}
				reportGOCluster.addSubReport(reportGOClusterType);
			}
			reportGO.addSubReport(reportGOCluster);
		}
	}
	
	/**初始化参数*/
	private void init(CtrlGOall ctrlGOall) {
		reportGOAll = new ReportGOAll();
		for (String prefix : ctrlGOall.mapPrefix2ResultPic.keySet()) {
			ReportGOResult reportGOResult = new ReportGOResult();
			mapPrefix2Report.put(prefix, reportGOResult);
		}
		if (ctrlGOall.isCluster()) {
			reportGOCluster = new ReportGOCluster();
		} else {
			reportGOUpDown = new ReportGOUpDown();
		}
	}
	
	/** 生成ReportGoAll */
	private void setReportGoAll(CtrlGO ctrlGO) {
		FunctionTest functionTest = ctrlGO.getMapResult_Prefix2FunTest().get(CtrlGOPath.All);
		if (functionTest == null || functionTest.getTestResult().isEmpty()) {
			isHaveGOAll = false;
			return;
		}
		isHaveGOAll = true;
		int sigTermNum = getSigTermNum(functionTest);
		reportGOAll.setAllDifGeneNum(functionTest.getAllDifGeneNum());
		reportGOAll.setDifGeneNum(ctrlGO.getUpAndDownRegulation()[2]);
		reportGOAll.setGoSigNum(ctrlGO.getGOType(), sigTermNum);
	}
	
	//TODO 测试极端情况，如go up或go down 没有的情况
	/** 生成ReportGoUpDown */
	private void setReportGoUpDown(CtrlGO ctrlGO) {
		reportGOUpDown.setUpDownNum(ctrlGO.getUpAndDownRegulation()[0], ctrlGO.getUpAndDownRegulation()[1]);
		FunctionTest functionTestUp = ctrlGO.getMapResult_Prefix2FunTest().get(CtrlGOPath.Up);
		FunctionTest functionTestDown = ctrlGO.getMapResult_Prefix2FunTest().get(CtrlGOPath.Down);
		
		reportGOUpDown.setGoSigNum(true, ctrlGO.getGOType(),  getSigTermNum(functionTestUp));
		reportGOUpDown.setGoSigNum(false, ctrlGO.getGOType(),  getSigTermNum(functionTestDown));
	}
	
	/**
	 * 具体子报告
	 * @param prefix
	 * @param goType
	 * @param functionTest
	 */
	private void saveReportDetail(String prefix, GOtype goType, FunctionTest functionTest) {
		if (functionTest == null || functionTest.getTestResult().isEmpty()) {
			return;
		}
		ReportGOResult reportGOResult = mapPrefix2Report.get(prefix);
		reportGOResult.setGeneNum(functionTest.getAllDifGeneNum());
		for (int i = 0; i < getSigTermNum(functionTest); i++) {
			if (i >= goTermNum) break;
			StatisticTestResult statisticTestResult = functionTest.getTestResult().get(i);
			reportGOResult.addGoTerm( goType, statisticTestResult.getItemTerm(),
					statisticTestResult.getDifGeneInItemNum(), statisticTestResult.getPvalue());
		}
	}
	
	protected static int getSigTermNum(FunctionTest functionTest) {
		if (functionTest == null) return 0;
		
		List<StatisticTestResult> lsTestResult = functionTest.getTestResult();
		int sigTermNum = 0;
		for (StatisticTestResult statisticTestResult : lsTestResult) {
			if (statisticTestResult.getPvalue() <= FunctionTest.PvalueFdr_Cutoff) {
				sigTermNum++;
			}
		}
		return sigTermNum;
	}
}
