package com.novelbio.nbcReport.Params;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.novelbio.nbcReport.XdocTmpltExcel;


/**
 * GOAnalysis参数对象类，记录结果报告所需要的参数
 * 
 * @author novelbio
 * 
 */
public class ReportQCAll extends ReportBase {
	private String no = "${no}";
	private List<ReportQC> lsReportQCs = new ArrayList<ReportQC>();
	private List<XdocTmpltExcel> lsXdocTmpltExcels;
	
	@Override
	public EnumReport getEnumReport() {
		return EnumReport.FastQC;
	}


	@Override
	protected Map<String, Object> addParamMap() {
		Map<String, Object> mapKey2Params = new HashMap<String, Object>();
		mapKey2Params.put("no", no);
		mapKey2Params.put("lsReportQCs", lsReportQCs);
		mapKey2Params.put("excels", getExcels());
		return mapKey2Params;
	}
	
	/**
	 * 添加表格模板
	 * @param xdocTmpltExcel
	 */
	public void addXdocTempExcel(XdocTmpltExcel xdocTmpltExcel) {
		if (lsXdocTmpltExcels == null) {
			lsXdocTmpltExcels = new ArrayList<XdocTmpltExcel>();
		}
		lsXdocTmpltExcels.add(xdocTmpltExcel);
	}
	
	/**
	 * 取得所有的表格集合
	 * @return
	 */
	public List<String> getExcels(){
		List<String> lsExcels = new ArrayList<String>();
		for (XdocTmpltExcel xdocTmpltExcel : lsXdocTmpltExcels) {
			lsExcels.add(xdocTmpltExcel.toString());
		}
		return lsExcels;
	}

	public void addReportQC(ReportQC reportQC) {
		lsReportQCs.add(reportQC);
	}

	public String getNo() {
		return no;
	}
	
	
}
