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
public class ReportGOAll extends ReportBase {
	private String no = "${no}";
	
	@Override
	public EnumReport getEnumReport() {
		return EnumReport.GOAnalysis;
	}


	@Override
	public Map<String, Object> buildFinalParamMap() {
		Map<String, Object> mapKey2Params = new HashMap<String, Object>();
		if (mapTempName2setReportBase.get(EnumReport.GOAnalysis.getTempName()).size()>0) {
			ReportGO reportGO = (ReportGO) mapTempName2setReportBase.get(EnumReport.GOAnalysis.getTempName()).iterator().next();
			mapKey2Params.put("testMethod", reportGO.getTestMethod());
			mapKey2Params.put("finderCondition", getFinderCondition());
		}
		mapKey2Params.putAll(mapTempName2setReportBase);
		mapKey2Params.put("no", no);
		return mapKey2Params;
	}
	
	/**
	 * 统计结果，返回筛选条件
	 * 
	 * @param lsTestResults
	 * @param knownCondition
	 *            已知条件，用来比较返回更宽松的条件
	 * @return
	 */
	public String getFinderCondition() {
		String[] result = { "FDR&lt;0.01", "FDR&lt;0.05", "P-value&lt;0.01", "P-value&lt;0.05" };
		int conditionNum = 0;
		for (ReportBase reportGO : mapTempName2setReportBase.get(EnumReport.GOAnalysis.getTempName())) {
			String condition = ((ReportGO)reportGO).getFinderCondition();
			for (int i = 0; i < result.length; i++) {
				if (condition.equals(result[i]) && i > conditionNum)
					conditionNum = i;
			}
		}
		return result[conditionNum];
	}
	
	public String getNo() {
		return no;
	}


	@Override
	public boolean readReportFromFile(String savePath) {
		List<String> lsReportFiles = FileOperate.getFoldFileNameLs(FileOperate.addSep(savePath)+".report", "report_*", "*");
		for (String reportFile : lsReportFiles) {
			try {
				ReportGO reportGO = (ReportGO)FileOperate.readFileAsObject(reportFile);
				addChildReport(reportGO);
			} catch (Exception e) {
				continue;
			}
		}
		return true;
	}
	
	
}
