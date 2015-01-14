package com.novelbio.report.generateReport;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import uk.ac.babraham.FastQC.Modules.BasicStats;

import com.novelbio.nbcgui.controlseq.CtrlFastQ;
import com.novelbio.report.Params.ReportDifGene;
import com.novelbio.report.Params.ReportQC;

public class FastQCReport {
	
	ReportQC reportQC = new ReportQC();
	
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
		//计算总的筛选比率
		double baseFitlerRateSum = 0;
		//计算总的筛选后的数量
		double baseNumSum = 0;
		for (String prefix : mapPrefix2BasicStatsBefore.keySet()) {
			Map<String, Object> mapKey2SampleInfo = new HashMap<String, Object>();
			BasicStats basicStatsBefore = mapPrefix2BasicStatsBefore.get(prefix);
			BasicStats basicStatsAfter = mapPrefix2BasicStatsAfter.get(prefix);
			if (ctrlFastQ.isJustFastqc()) {
			    mapKey2SampleInfo.put("sampleName", basicStatsBefore.getName());
			    mapKey2SampleInfo.put("readsNum", basicStatsBefore.getReadsNum());
			    mapKey2SampleInfo.put("Length", basicStatsBefore.getSeqLen());
			    mapKey2SampleInfo.put("baseNum", basicStatsBefore.getBaseNum());
			    mapKey2SampleInfo.put("CG", basicStatsBefore.getGCpersentage() * 100);
			    baseNumSum = baseNumSum + basicStatsBefore.getBaseNum();
			} else {
			    mapKey2SampleInfo.put("sampleName", basicStatsBefore.getName());
			    mapKey2SampleInfo.put("beforeReadsNum", basicStatsBefore.getReadsNum());
			    mapKey2SampleInfo.put("afterReadsNum", basicStatsAfter.getReadsNum());
			    mapKey2SampleInfo.put("beforeBaseNum", basicStatsBefore.getBaseNum());
			    mapKey2SampleInfo.put("afterBaseNum", basicStatsAfter.getBaseNum());
			    double baseFitlerRate = (double)basicStatsAfter.getBaseNum()/basicStatsBefore.getBaseNum();
			    mapKey2SampleInfo.put("baseFilterRate", baseFitlerRate * 100);
			    baseNumSum = baseNumSum + basicStatsAfter.getBaseNum();
			    baseFitlerRateSum = baseFitlerRateSum + baseFitlerRate;
			}
			reportQC.addSampleInfo(mapKey2SampleInfo);
		}
		double avgSize = baseNumSum/mapPrefix2BasicStatsBefore.size();

		reportQC.setAvgSize(avgSize);

		if (baseFitlerRateSum != 0) {
			double avgFilterRate = baseFitlerRateSum/mapPrefix2BasicStatsBefore.size();
			reportQC.setAvgFilterRate(avgFilterRate);
		}
		
	}
}
