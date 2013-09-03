package com.novelbio.nbcReport.Params;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * GOAnalysis参数对象类，记录结果报告所需要的参数
 * 
 * @author novelbio
 * 
 */
public class ReportQCAll extends ReportBase {
	private String no = "${no}";
	private List<ReportQC> lsReportQCs = new ArrayList<ReportQC>();
	
	@Override
	public EnumReport getEnumReport() {
		return EnumReport.FastQC;
	}


	@Override
	protected Map<String, Object> addParamMap() {
		Map<String, Object> mapKey2Params = new HashMap<String, Object>();
		mapKey2Params.put("no", no);
		mapKey2Params.put("lsReportQCs", lsReportQCs);
		return mapKey2Params;
	}
	
	public void addReportQC(ReportQC reportQC) {
		lsReportQCs.add(reportQC);
	}

	public String getNo() {
		return no;
	}
	
	
}
