package com.novelbio.report.generateReport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.novelbio.analysis.seq.mapping.MappingReadsType;
import com.novelbio.analysis.seq.sam.SamFileStatistics;
import com.novelbio.base.dataStructure.MathComput;
import com.novelbio.nbcgui.controlseq.CtrlSamRPKMLocate;
import com.novelbio.report.ReportTable;
import com.novelbio.report.Params.ReportSamAndRPKM;

public class SamAndRPKMReport {
	/** 报告中表格行数 */
	private static final int TABLEROW = 10;
	private ReportSamAndRPKM reportSamAndRPKM = new ReportSamAndRPKM();
	
	public SamAndRPKMReport(CtrlSamRPKMLocate ctrlSamRPKMLocate) {
		generateReport(ctrlSamRPKMLocate);
	}
	
	public ReportSamAndRPKM getReportSamAndRPKM() {
		return reportSamAndRPKM;
	}
	
	public void generateReport(CtrlSamRPKMLocate ctrlSamRPKMLocate) {
		
		Map<String, SamFileStatistics> mapPrefix2Statistics = ctrlSamRPKMLocate.getMapPrefix2Statistics();
		
		//用于获取上25分位点
		List<Double> lsMappingRate = new ArrayList<Double>();
		List<Double> lsUniqueMappingRate = new ArrayList<Double>();
		List<Double> lsJunctionReadsRate = new ArrayList<Double>();
		
		List<String[]> lsLsData = getLsLsData(mapPrefix2Statistics.size() + 1);
		//标识当前的是第几个样本
		int currentStatistics = 0;
		for (String prefix : mapPrefix2Statistics.keySet()) {
			SamFileStatistics samFileStatistics = mapPrefix2Statistics.get(prefix);
			
			long totalReads = samFileStatistics.getReadsNum(MappingReadsType.All);
			long totalMappedReads = samFileStatistics.getReadsNum(MappingReadsType.Mapped);
			double mappingRate = (double)totalMappedReads/totalReads;
			long uniqueMapping = samFileStatistics.getReadsNum(MappingReadsType.UniqueMapped);
			double uniqueMappingRate = (double)uniqueMapping/totalReads;
			long junctionAllMappedReads = samFileStatistics.getReadsNum(MappingReadsType.JunctionAllMapped);
			long junctionUniqueMapping = samFileStatistics.getReadsNum(MappingReadsType.JunctionUniqueMapped);
			long repeatMapping = samFileStatistics.getReadsNum(MappingReadsType.RepeatMapped);
			long unMapped = samFileStatistics.getReadsNum(MappingReadsType.UnMapped);
			double junctionReadsRate = (double)junctionAllMappedReads/totalReads;
			
			lsMappingRate.add(mappingRate);
			lsUniqueMappingRate.add(uniqueMappingRate);
			lsJunctionReadsRate.add(junctionReadsRate);
			
			lsLsData.get(0)[currentStatistics + 1] = prefix;
			lsLsData.get(1)[currentStatistics + 1] = totalReads + "";
			lsLsData.get(2)[currentStatistics + 1] = totalMappedReads + "";
			lsLsData.get(3)[currentStatistics + 1] = mappingRate + "";
			lsLsData.get(4)[currentStatistics + 1] = uniqueMapping + "";
			lsLsData.get(5)[currentStatistics + 1] = uniqueMappingRate + "";
			lsLsData.get(6)[currentStatistics + 1] = junctionAllMappedReads + "";
			lsLsData.get(7)[currentStatistics + 1] = junctionUniqueMapping + "";
			lsLsData.get(8)[currentStatistics + 1] = repeatMapping + "";
			lsLsData.get(9)[currentStatistics + 1] = unMapped + "";
			
			currentStatistics++;
		}
		reportSamAndRPKM.setMappingRate(MathComput.median(lsMappingRate, 25));
		reportSamAndRPKM.setUniqueMappingRate(MathComput.median(lsUniqueMappingRate, 25));
		reportSamAndRPKM.setJunctionReadsRate(MathComput.median(lsJunctionReadsRate, 25));
		ReportTable reportTable = new ReportTable();
		reportTable.getMapKey2Param("Mapping Statistics", lsLsData, 6);
		reportSamAndRPKM.addTable(reportTable.getMapKey2Param("Mapping Statistics", lsLsData, 6));
	}
	
	private List<String[]> getLsLsData(int length) {
		List<String[]> lsLsData = new ArrayList<String[]>();
		for (int i = 0; i < TABLEROW; i++) {
			String[] lsData = new String[length];
			lsLsData.add(lsData);
		}
		lsLsData.get(0)[0] = "sampleName";
		lsLsData.get(1)[0] = "TotalReads";
		lsLsData.get(2)[0] = "TotalMappedReads";
		lsLsData.get(3)[0] = "MappingRate";
		lsLsData.get(4)[0] = "UniqueMapping";
		lsLsData.get(5)[0] = "UniqueMappingRate";
		lsLsData.get(6)[0] = "junctionAllMappedReads";
		lsLsData.get(7)[0] = "junctionUniqueMapping";
		lsLsData.get(8)[0] = "repeatMapping";
		lsLsData.get(9)[0] = "UnMapped";
		return null;
	}

}
