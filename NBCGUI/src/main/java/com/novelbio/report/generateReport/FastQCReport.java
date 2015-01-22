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
	private static final int TABLECOLUMN = 3;
	private static final int TABLEROW = 5;
	private static final int TABLEHASBAROW = 6;
	
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
		for (int i = 0; i < TABLEROW; i++) {
			String[] sampleInfo = new String[mapPrefix2BasicStatsBefore.size() + 1];
			lsSampleInfo.add(sampleInfo);
		}
		lsSampleInfo.get(0)[0] = "sampleName";
		lsSampleInfo.get(1)[0] = "readsNum";
		lsSampleInfo.get(2)[0] = "length";
		lsSampleInfo.get(3)[0] = "baseNum";
		lsSampleInfo.get(4)[0] = "CG\\%";
		// 当前的BasicStats
		int currentBasicStats = 0;
		for (String prefix : mapPrefix2BasicStatsBefore.keySet()) {
			BasicStats basicStatsBefore = mapPrefix2BasicStatsBefore.get(prefix);
			
			lsSampleInfo.get(0)[currentBasicStats + 1] = prefix;
			lsSampleInfo.get(1)[currentBasicStats + 1] = basicStatsBefore.getReadsNum() + "";
			lsSampleInfo.get(2)[currentBasicStats + 1] = basicStatsBefore.getSeqLen() + "";
			lsSampleInfo.get(3)[currentBasicStats + 1] = basicStatsBefore.getBaseNum() + "";
			lsSampleInfo.get(4)[currentBasicStats + 1] = TWO_DECIMAL.format(basicStatsBefore.getGCpersentage() * 100) + "\\%";
			
		    baseNumSum = baseNumSum + basicStatsBefore.getBaseNum();
		    
		}
		
		ReportTable reportTable = new ReportTable();
		reportQC.addTable("lsSampleInfo", reportTable.getMapKey2Param(TABLETITLE, lsSampleInfo, TABLECOLUMN));
		
		double avgSize = baseNumSum/mapPrefix2BasicStatsBefore.size();
		double sizeGB =  (avgSize/Math.pow(1024, 3));
		reportQC.setAvgSize(sizeGB);
	}
	
	private void createSampleInfoHasBATable(Map<String, BasicStats> mapPrefix2BasicStatsBefore, Map<String, BasicStats> mapPrefix2BasicStatsAfter) {
		
		//计算总的筛选比率
		double baseFitlerRateSum = 0;
		//计算总的筛选后的数量
		double baseNumSum = 0;
		
		List<String[]> lsSampleInfo = new ArrayList<String[]>();
		
		for (int i = 0; i < TABLEHASBAROW; i++) {
			String[] sampleInfo = new String[mapPrefix2BasicStatsBefore.size() + 1];
			lsSampleInfo.add(sampleInfo);
		}
		lsSampleInfo.get(0)[0] = "sampleName";
		lsSampleInfo.get(1)[0] = "beforeReadsNum";
		lsSampleInfo.get(2)[0] = "afterReadsNum";
		lsSampleInfo.get(3)[0] = "beforeBaseNum";
		lsSampleInfo.get(4)[0] = "afterBaseNum";
		lsSampleInfo.get(5)[0] = "baseFilterRate\\%";
		// 当前的BasicStats
		int currentBasicStats = 0;
		for (String prefix : mapPrefix2BasicStatsBefore.keySet()) {
			BasicStats basicStatsBefore = mapPrefix2BasicStatsBefore.get(prefix);
			BasicStats basicStatsAfter = mapPrefix2BasicStatsAfter.get(prefix);
			
			lsSampleInfo.get(0)[currentBasicStats + 1] = prefix;
			lsSampleInfo.get(1)[currentBasicStats + 1] = basicStatsBefore.getReadsNum() + "";
			lsSampleInfo.get(2)[currentBasicStats + 1] = basicStatsAfter.getReadsNum() + "";
			lsSampleInfo.get(3)[currentBasicStats + 1] = basicStatsBefore.getBaseNum() + "";
			lsSampleInfo.get(4)[currentBasicStats + 1] = basicStatsAfter.getBaseNum() + "";
			 double baseFitlerRate = (double)basicStatsAfter.getBaseNum()/basicStatsBefore.getBaseNum();
			lsSampleInfo.get(5)[currentBasicStats + 1] = TWO_DECIMAL.format(baseFitlerRate * 100) + "\\%";

		    baseNumSum = baseNumSum + basicStatsAfter.getBaseNum();
		    baseFitlerRateSum = baseFitlerRateSum + baseFitlerRate;
		}
		
		ReportTable reportTable = new ReportTable();
		reportQC.addTable("lsSampleInfoHasBA", reportTable.getMapKey2Param(TABLETITLE, lsSampleInfo, TABLECOLUMN));
		
		double avgSize = baseNumSum/mapPrefix2BasicStatsBefore.size();
		int sizeGB = (int) (avgSize/Math.pow(1024, 3));
		reportQC.setAvgSize(sizeGB);
		double avgFilterRate = baseFitlerRateSum/mapPrefix2BasicStatsBefore.size();
		reportQC.setAvgFilterRate(avgFilterRate);
	}
	
}
