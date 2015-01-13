package com.novelbio.report.Params;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.novelbio.nbcReport.XdocTmpltPic;

/**
 * GOAnalysis参数对象类，记录结果报告所需要的参数
 * 
 * @author novelbio
 * 
 */
public class ReportQC extends ReportBase{
	private static final long serialVersionUID = 7757181385034786919L;
	/** 不包含before和after的样本的信息参数的个数 */
	private static final int sampleInfoSize = 5;
	
	public void setAvgSize(double avgSize) {
		mapKey2Param.put("avgSize", avgSize);
	}
	
	public void setAvgFilterRate(double avgFilterRate) {
		mapKey2Param.put("avgFilterRate", avgFilterRate * 100);
	}
	
	public void addSampleInfo(Map<String, Object> mapKey2SampleInfo) {
		List<Map<String, Object>> lsSampleInfo = null;
		String key;
		// 当mapKey2SampleInfo的长度为不包含before和after的样本信息参数的个数时，key为lsSampleInfo，否则为lsSampleInfoHasBA
		if (mapKey2SampleInfo.size() == ReportQC.sampleInfoSize) {
			key = "lsSampleInfo";
		} else {
			key = "lsSampleInfoHasBA";
		}
		
		if (mapKey2Param.containsKey(key)) {
			lsSampleInfo = (List<Map<String, Object>>) mapKey2Param.get(key);
		} else {
			lsSampleInfo = new ArrayList<Map<String,Object>>();
		}
		lsSampleInfo.add(mapKey2SampleInfo);
		mapKey2Param.put(key, lsSampleInfo);
	}

	@Override
	public EnumReport getEnumReport() {
		return EnumReport.FastQC;
	}

}
