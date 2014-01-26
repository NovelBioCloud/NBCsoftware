package com.novelbio.nbcReport.Params;

import java.util.Map;

public class ReportGOUpDown extends ReportBase {

	private static final long serialVersionUID = 1L;

	@Override
	public EnumReport getEnumReport() {
		return EnumReport.GO_UpDown;
	}
	
	@Override
	public Map<String, Object> buildFinalParamMap() {
		return super.buildFinalParamMap();
	}
}
