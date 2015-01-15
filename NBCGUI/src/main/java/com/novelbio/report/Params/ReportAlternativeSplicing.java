package com.novelbio.report.Params;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReportAlternativeSplicing extends ReportBase {
	private static final long serialVersionUID = 6060775931829366004L;
	
	public void setGroupName(String groupName) {
		mapKey2Param.put("groupName", groupName);
	}
	
	public void addSplicingEvent(Map<String, Object> mapKey2SplicingEvent) {
		List<Map<String, Object>> lsSplicingEvent = null;
		if (mapKey2Param.containsKey("lsSplicingEvent")) {
			lsSplicingEvent = (List<Map<String, Object>>) mapKey2Param.get("lsSplicingEvent");
		} else {
			lsSplicingEvent = new ArrayList<Map<String,Object>>();
		}
		lsSplicingEvent.add(mapKey2SplicingEvent);
		mapKey2Param.put("lsExperimentInfo", lsSplicingEvent);
	}

	@Override
	public EnumReport getEnumReport() {
		return EnumReport.RNAAlternativeSplicing;
	}

}
