package com.novelbio.nbcReport.Params;

import java.util.HashMap;
import java.util.Map;

public class ReportDNASeqMap extends ReportBase{
	private String no = "${no}";
	
	public ReportDNASeqMap() {
		// TODO Auto-generated constructor stub
	}
	
	public EnumReport getEnumReport() {
		return EnumReport.DNASeqMap;
	}

	@Override
	public Map<String, Object> buildFinalParamMap() {
		Map<String, Object> mapKey2Params = new HashMap<String, Object>();
		mapKey2Params.put("no", no);
		return mapKey2Params;
	}
	
	public String getNo() {
		return no;
	}
	
}
