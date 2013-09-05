package com.novelbio.nbcReport.Params;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.novelbio.base.fileOperate.FileOperate;

public class ReportPathWayAll extends ReportBase{
	private String no = "${no}";
	private List<ReportPathWay> lsReportPathWays = new ArrayList<ReportPathWay>();
	
	
	@Override
	public EnumReport getEnumReport() {
		return EnumReport.PathWay;
	}

	@Override
	protected Map<String, Object> addParamMap() {
		Map<String, Object> mapKey2Params = new HashMap<String, Object>();
		if (lsReportPathWays.size()>0) {
			ReportPathWay reportPathWay = lsReportPathWays.get(0);
			mapKey2Params.put("db", reportPathWay.getDb());
			mapKey2Params.put("finderCondition", getFinderCondition());
		}
		mapKey2Params.put("no", no);
		mapKey2Params.put("lsReportPathWays", lsReportPathWays);
		return mapKey2Params;
	}
	
	public String getNo() {
		return no;
	}
	
	public void addReportPathWay(ReportPathWay reportPathWay) {
		lsReportPathWays.add(reportPathWay);
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
		for (ReportPathWay reportPathWay : lsReportPathWays) {
			String condition = reportPathWay.getFinderCondition();
			for (int i = 0; i < result.length; i++) {
				if (condition.equals(result[i]) && i > conditionNum)
					conditionNum = i;
			}
		}
		return result[conditionNum];
	}

	@Override
	public boolean readReportFromFile(String savePath) {
		List<String> lsReportFiles = FileOperate.getFoldFileNameLs(FileOperate.addSep(savePath)+".report", "report_*", "*");
		for (String reportFile : lsReportFiles) {
			try {
				ReportPathWay reportPathWay = (ReportPathWay)FileOperate.readFileAsObject(reportFile);
				addReportPathWay(reportPathWay);
			} catch (Exception e) {
				continue;
			}
		}
		return true;
	}
	
}
