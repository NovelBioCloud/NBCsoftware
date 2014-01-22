package com.novelbio.nbcReport.Params;

import java.util.Map;

import org.apache.log4j.Logger;

import com.novelbio.base.word.NBCWord;
/**
 * 报告总类
 * @author Administrator
 *
 */
public class ReportAll extends ReportBase {
	
	private static final long serialVersionUID = 1L;
	public static final Logger logger = Logger.getLogger(ReportAll.class);
	/** 项目名称 */
	private String projectName;
	
	public ReportAll(){
	}
	
	@Override
	public EnumReport getEnumReport() {
		return EnumReport.ReportAll;
	}

	@Override
	public Map<String, Object> buildFinalParamMap() {
		return mapKey2Param;
	}

	/** 为项目报告添加名称 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Override
	public boolean readReportFromFile(String savePath) {
		return false;
	}
	
	
	public static void main(String[] args) {
		ReportBase reportAll = EnumReport.ReportAll.getReportBase();
		reportAll.addChildReport(reportAll);
		NBCWord word = reportAll.getWord();
		word.renderReport(reportAll);
	}
}
