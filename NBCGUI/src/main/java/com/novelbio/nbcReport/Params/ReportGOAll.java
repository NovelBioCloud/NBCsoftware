package com.novelbio.nbcReport.Params;

import java.util.Map;

public class ReportGOAll extends ReportBase  {

	private static final long serialVersionUID = 1L;

	@Override
	public EnumReport getEnumReport() {
		return EnumReport.GO_All;
	}
	
	@Override
	public Map<String, Object> buildFinalParamMap() {
		return super.buildFinalParamMap();
	}
}
