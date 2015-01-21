package com.novelbio.report.generateReport;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import uk.ac.babraham.FastQC.Modules.BasicStats;

import com.novelbio.nbcgui.controlseq.CtrlFastQ;
import com.novelbio.report.ReportTable;
import com.novelbio.report.Params.ReportQC;

public class FastQCReport {
	private static final DecimalFormat TWO_DECIMAL = new DecimalFormat("#.##");
	private static final String TABLETITLE = "Raw Data \\& Clean Data Statistics";
	private static final int TABLECOLUMN = 5;
	private static final int TABLEHASBACOLUMN = 6;
	
	private ReportQC reportQC = new ReportQC();
	
	public FastQCReport(CtrlFastQ ctrlFastQ) {
		generateReport(ctrlFastQ);
	}
	
	public ReportQC getReportQC() {
		return reportQC;
	}
	
	public void generateReport(CtrlFastQ ctrlFastQ) {
		Map<String, BasicStats> mapPrefix2BasicStatsBefore = new LinkedHashMap<String, BasicStats>();
		Map<String, BasicStats> mapPrefix2BasicStatsAfter = new LinkedHashMap<String, BasicStats>();
		mapPrefix2BasicStatsBefore = ctrlFastQ.getMapPrefix2BasicStatsBefore();
		mapPrefix2BasicStatsAfter = ctrlFastQ.getMapPrefix2BasicStatsAfter();
		
		if (ctrlFastQ.isJustFastqc()) {
			createSampleInfoTable(mapPrefix2BasicStatsBefore);
		} else {
			createSampleInfoHasBATable(mapPrefix2BasicStatsBefore, mapPrefix2BasicStatsAfter);
		}
		
	}
	
	private void createSampleInfoTable(Map<String, BasicStats> mapPrefix2BasicStatsBefore) {
		
		//计算总的筛选后的数量
		double baseNumSum = 0;
		
		List<String[]> lsSampleInfo = new ArrayList<String[]>();
		String[] sampleInfo = new String[TABLECOLUMN];
		sampleInfo[0] = "sampleName";
		sampleInfo[1] = "readsNum";
		sampleInfo[2] = "length";
		sampleInfo[3] = "baseNum";
		sampleInfo[4] = "CG\\%";
		lsSampleInfo.add(sampleInfo);
		
		for (String prefix : mapPrefix2BasicStatsBefore.keySet()) {
			sampleInfo = new String[TABLECOLUMN];
			BasicStats basicStatsBefore = mapPrefix2BasicStatsBefore.get(prefix);
			
			sampleInfo[0] = basicStatsBefore.getName();
			sampleInfo[1] = basicStatsBefore.getReadsNum() + "";
			sampleInfo[2] = basicStatsBefore.getSeqLen() + "";
			sampleInfo[3] = basicStatsBefore.getBaseNum() + "";
			sampleInfo[4] = TWO_DECIMAL.format(basicStatsBefore.getGCpersentage() * 100) + "\\%";
			
		    baseNumSum = baseNumSum + basicStatsBefore.getBaseNum();
		    
		    lsSampleInfo.add(sampleInfo);
		}
		
		ReportTable reportTable = new ReportTable();
		reportQC.addTable("lsSampleInfo", reportTable.getMapKey2Param(TABLETITLE, lsSampleInfo, TABLECOLUMN));
		
		double avgSize = baseNumSum/mapPrefix2BasicStatsBefore.size();
		int sizeGB = (int) (avgSize/Math.pow(1024, 3));
		reportQC.setAvgSize(sizeGB);
		
	}
	
	private void createSampleInfoHasBATable(Map<String, BasicStats> mapPrefix2BasicStatsBefore, Map<String, BasicStats> mapPrefix2BasicStatsAfter) {
		
		//计算总的筛选比率
		double baseFitlerRateSum = 0;
		//计算总的筛选后的数量
		double baseNumSum = 0;
		
		List<String[]> lsSampleInfo = new ArrayList<String[]>();
		String[] sampleInfo = new String[TABLEHASBACOLUMN];
		sampleInfo[0] = "sampleName";
		sampleInfo[1] = "beforeReadsNum";
		sampleInfo[2] = "afterReadsNum";
		sampleInfo[3] = "beforeBaseNum";
		sampleInfo[4] = "afterBaseNum";
		sampleInfo[5] = "baseFilterRate\\%";
		lsSampleInfo.add(sampleInfo);
		
		for (String prefix : mapPrefix2BasicStatsBefore.keySet()) {
			sampleInfo = new String[TABLEHASBACOLUMN];
			BasicStats basicStatsBefore = mapPrefix2BasicStatsBefore.get(prefix);
			BasicStats basicStatsAfter = mapPrefix2BasicStatsAfter.get(prefix);
			
			sampleInfo[0] = basicStatsBefore.getName();
			sampleInfo[1] = basicStatsBefore.getReadsNum() + "";
			sampleInfo[2] = basicStatsAfter.getReadsNum() + "";
			sampleInfo[3] = basicStatsBefore.getBaseNum() + "";
			sampleInfo[4] = basicStatsAfter.getBaseNum() + "";
		    double baseFitlerRate = (double)basicStatsAfter.getBaseNum()/basicStatsBefore.getBaseNum();
			sampleInfo[5] = TWO_DECIMAL.format(baseFitlerRate * 100) + "\\%";

		    baseNumSum = baseNumSum + basicStatsAfter.getBaseNum();
		    baseFitlerRateSum = baseFitlerRateSum + baseFitlerRate;
		    
		    lsSampleInfo.add(sampleInfo);
		}
		
		ReportTable reportTable = new ReportTable();
		reportQC.addTable("lsSampleInfoHasBA", reportTable.getMapKey2Param(TABLETITLE, lsSampleInfo, TABLEHASBACOLUMN));
		
		double avgSize = baseNumSum/mapPrefix2BasicStatsBefore.size();
		int sizeGB = (int) (avgSize/Math.pow(1024, 3));
		reportQC.setAvgSize(sizeGB);
		double avgFilterRate = baseFitlerRateSum/mapPrefix2BasicStatsBefore.size();
		reportQC.setAvgFilterRate(avgFilterRate);
	}
	
}
