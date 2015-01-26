package com.novelbio.report.Params;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReportRNAassembly extends ReportBase {
	private static final long serialVersionUID = 850010651127543477L;
	
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
	public EnumTaskReport getEnumReport() {
		return EnumTaskReport.RNAassembly;
	}

}
