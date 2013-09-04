package com.novelbio.nbcReport.Params;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.nbcReport.EnumTableType;
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
	private XdocTmpltExcel xdocTmpltExcel;

	@Override
	public EnumReport getEnumReport() {
		return EnumReport.FastQC;
	}

	@Override
	protected Map<String, Object> addParamMap() {
		Map<String, Object> mapKey2Params = new HashMap<String, Object>();
		mapKey2Params.put("no", no);
		// 只要一个做为示例
		List<ReportQC> lsReportQCs1 = new ArrayList<>();
		if (lsReportQCs.size() > 0) {
			lsReportQCs1.add(lsReportQCs.get(0));
		}
		mapKey2Params.put("lsReportQCs", lsReportQCs1);
		mapKey2Params.put("excels", getExcels());
		return mapKey2Params;
	}

	/**
	 * 添加表格模板
	 * 
	 * @param xdocTmpltExcel
	 */
	public void addXdocTempExcel(XdocTmpltExcel xdocTmpltExcel) {
		this.xdocTmpltExcel = xdocTmpltExcel;
	}

	/**
	 * 取得所有的表格集合
	 * 
	 * @return
	 */
	public List<String> getExcels() {
		List<String> lsExcels = new ArrayList<String>();
		lsExcels.add(xdocTmpltExcel.toString());
		return lsExcels;
	}

	public void addReportQC(ReportQC reportQC) {
		lsReportQCs.add(reportQC);
	}

	public String getNo() {
		return no;
	}

	public List<ReportQC> getLsReportQCs() {
		return lsReportQCs;
	}

	@Override
	public boolean readReportFromFile(String savePath) {
		List<String> lsReportFiles = FileOperate.getFoldFileNameLs(FileOperate.addSep(savePath)+".report", "report_*", "*");
		for (String reportFile : lsReportFiles) {
			try {
				ReportQC reportQC = (ReportQC)FileOperate.readFileAsObject(reportFile);
				addReportQC(reportQC);
				if (xdocTmpltExcel == null) {
					XdocTmpltExcel xdocTmpltExcel = new XdocTmpltExcel(EnumTableType.QC_BasicStatAll.getXdocTable());
					xdocTmpltExcel.addExcel(reportQC.getBasicStatExcelPath(), 1);
					addXdocTempExcel(xdocTmpltExcel);
				}
			} catch (Exception e) {
				continue;
			}
		}
		return true;
	}

}
