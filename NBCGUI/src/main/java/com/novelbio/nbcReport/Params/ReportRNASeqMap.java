package com.novelbio.nbcReport.Params;

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

	@Override
	protected Map<String, Object> addParamMap() {
		Map<String, Object> mapKey2Params = new HashMap<String, Object>();
		mapKey2Params.put("no", no);
		return mapKey2Params;
	}
	
	public String getNo() {
		return no;
	}

	@Override
	public boolean readReportFromFile(String savePath) {
		return true;
	}
	
}
