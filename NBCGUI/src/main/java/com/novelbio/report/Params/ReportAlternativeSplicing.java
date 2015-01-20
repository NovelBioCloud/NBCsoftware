package com.novelbio.report.Params;

public class ReportAlternativeSplicing extends ReportBase {
	private static final long serialVersionUID = 6060775931829366004L;
	
	public void setGroupName(String groupName) {
		mapKey2Param.put("groupName", groupName);
	}

	@Override
	public EnumReport getEnumReport() {
		return EnumReport.RNAAlternativeSplicing;
	}

}
