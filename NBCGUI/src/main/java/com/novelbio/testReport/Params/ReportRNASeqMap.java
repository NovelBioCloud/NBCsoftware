package com.novelbio.testReport.Params;

import java.util.HashMap;
import java.util.Map;

public class ReportRNASeqMap extends ReportBase{
	private String no = "${no}";
	
	public ReportRNASeqMap() {
		// TODO Auto-generated constructor stub
	}
	
	public EnumReport getEnumReport() {
		return EnumReport.RNASeqMap;
	}
	
	public Map<String, Object> getMapKey2Param() {
		mapKey2Param.put("no", no);
		return super.getMapKey2Param();
	}
	
	public String getNo() {
		return no;
	}
	
}
