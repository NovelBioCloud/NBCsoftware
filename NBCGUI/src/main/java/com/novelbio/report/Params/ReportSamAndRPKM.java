package com.novelbio.report.Params;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReportSamAndRPKM extends ReportBase{
	private static final long serialVersionUID = -1180209931503665138L;
	
	public void setMappingRate(double mappingRate) {
		mapKey2Param.put("mappingRate", mappingRate * 100);
	}
	
	public void setUniqueMappingRate(double uniqueMappingRate) {
		mapKey2Param.put("uniqueMappingRate", uniqueMappingRate * 100);
	}
	
	public void setJunctionReadsRate(double junctionReadsRate) {
		mapKey2Param.put("junctionReadsRate", junctionReadsRate * 100);
	}
	
	/**
	 * 添加样本信息
	 * @param mapKey2SampleInfo key对应的参数的名字，SampleInfo为参数名字所对应的值
	 */
	public void addSampleInfo(Map<String, Object> mapKey2SampleInfo) {
		List<Map<String, Object>> lsSampleInfo = null;
		if (mapKey2Param.containsKey("lsSampleInfo")) {
			lsSampleInfo = (List<Map<String, Object>>) mapKey2Param.get("lsSampleInfo");
		} else {
			lsSampleInfo = new ArrayList<Map<String,Object>>();
		}
		lsSampleInfo.add(mapKey2SampleInfo);
		mapKey2Param.put("lsSampleInfo", lsSampleInfo);
	}

	@Override
	public EnumReport getEnumReport() {
		return EnumReport.SamStatistics;
	}
	
}
