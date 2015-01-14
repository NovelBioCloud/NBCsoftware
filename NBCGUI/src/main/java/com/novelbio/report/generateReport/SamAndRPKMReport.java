package com.novelbio.report.generateReport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.novelbio.analysis.seq.mapping.MappingReadsType;
import com.novelbio.analysis.seq.sam.SamFileStatistics;
import com.novelbio.base.dataStructure.MathComput;
import com.novelbio.nbcgui.controlseq.CtrlSamRPKMLocate;
import com.novelbio.report.Params.ReportSamAndRPKM;

public class SamAndRPKMReport {
	private ReportSamAndRPKM reportSamAndRPKM = new ReportSamAndRPKM();
	
	public SamAndRPKMReport(CtrlSamRPKMLocate ctrlSamRPKMLocate) {
		generateReport(ctrlSamRPKMLocate);
	}
	
	public void generateReport(CtrlSamRPKMLocate ctrlSamRPKMLocate) {
		
		Map<String, SamFileStatistics> mapPrefix2Statistics = ctrlSamRPKMLocate.getMapPrefix2Statistics();
		
		//用于获取上25分位点
		List<Double> lsMappingRate = new ArrayList<Double>();
		List<Double> lsUniqueMappingRate = new ArrayList<Double>();
		List<Double> lsJunctionReadsRate = new ArrayList<Double>();
		
		for (String prefix : mapPrefix2Statistics.keySet()) {
			//key对应的为参数的名字，SampleInfo是参数名对应的值
			Map<String, Object> mapKey2SampleInfo = new HashMap<String, Object>();
			SamFileStatistics samFileStatistics = mapPrefix2Statistics.get(prefix);
			
			long totalReads = samFileStatistics.getReadsNum(MappingReadsType.allReads);
			long totalMappedReads = samFileStatistics.getReadsNum(MappingReadsType.allMappedReads);
			double mappingRate = (double)totalMappedReads/totalReads;
			long uniqueMapping = samFileStatistics.getReadsNum(MappingReadsType.uniqueMapping);
			double uniqueMappingRate = (double)uniqueMapping/totalReads;
			long junctionAllMappedReads = samFileStatistics.getReadsNum(MappingReadsType.junctionAllMappedReads);
			long junctionUniqueMapping = samFileStatistics.getReadsNum(MappingReadsType.junctionUniqueMapping);
			long repeatMapping = samFileStatistics.getReadsNum(MappingReadsType.repeatMapping);
			long unMapped = samFileStatistics.getReadsNum(MappingReadsType.unMapped);
			double junctionReadsRate = (double)junctionAllMappedReads/totalReads;
			
			lsMappingRate.add(mappingRate);
			lsUniqueMappingRate.add(uniqueMappingRate);
			lsJunctionReadsRate.add(junctionReadsRate);

			mapKey2SampleInfo.put("sampleName", prefix);
			mapKey2SampleInfo.put("TotalReads", totalReads);
			mapKey2SampleInfo.put("TotalMappedReads", totalMappedReads);
			mapKey2SampleInfo.put("MappingRate", mappingRate);
			mapKey2SampleInfo.put("UniqueMapping", uniqueMapping);
			mapKey2SampleInfo.put("UniqueMappingRate", uniqueMappingRate);
			mapKey2SampleInfo.put("junctionAllMappedReads", junctionAllMappedReads);
			mapKey2SampleInfo.put("junctionUniqueMapping", junctionUniqueMapping);
			mapKey2SampleInfo.put("repeatMapping", repeatMapping);
			mapKey2SampleInfo.put("UnMapped", unMapped);
			
			reportSamAndRPKM.addSampleInfo(mapKey2SampleInfo);
		}
		reportSamAndRPKM.setMappingRate(MathComput.median(lsMappingRate, 25));
		reportSamAndRPKM.setUniqueMappingRate(MathComput.median(lsUniqueMappingRate, 25));
		reportSamAndRPKM.setJunctionReadsRate(MathComput.median(lsJunctionReadsRate, 25));
		
	}

}
