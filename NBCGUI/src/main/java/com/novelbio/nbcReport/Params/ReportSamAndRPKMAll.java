package com.novelbio.nbcReport.Params;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.nbcReport.EnumTableType;
import com.novelbio.nbcReport.XdocTmpltExcel;

public class ReportSamAndRPKMAll   extends ReportBase {
	String no = "${no}";
	List<ReportSamAndRPKM> lsSamAndRPKMs = new ArrayList<>(); 
	
	

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public List<ReportSamAndRPKM> getLsSamAndRPKMs() {
		return lsSamAndRPKMs;
	}

	public void setLsSamAndRPKMs(List<ReportSamAndRPKM> lsSamAndRPKMs) {
		this.lsSamAndRPKMs = lsSamAndRPKMs;
	}

	@Override
	protected Map<String, Object> addParamMap() {
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("lsSamAndRPKMs", lsSamAndRPKMs);
		hashMap.put("no", no);
		return hashMap;
	}

	@Override
	public EnumReport getEnumReport() {
		return EnumReport.SamStatistics;
	}

	
	private void addSamAndRPKMs(ReportSamAndRPKM reportSamAndRPKM) {
		lsSamAndRPKMs.add(reportSamAndRPKM);
	}
	
	@Override
	public boolean readReportFromFile(String savePath) {
		List<String> lsReportFiles = FileOperate.getFoldFileNameLs(FileOperate.addSep(savePath)+".report", "report_*", "*");
		for (String reportFile : lsReportFiles) {
			try {
				ReportSamAndRPKM reportSamAndRPKM = (ReportSamAndRPKM)FileOperate.readFileAsObject(reportFile);
				addSamAndRPKMs(reportSamAndRPKM);
			} catch (Exception e) {
				continue;
			}
		}
		return true;
	}

}
