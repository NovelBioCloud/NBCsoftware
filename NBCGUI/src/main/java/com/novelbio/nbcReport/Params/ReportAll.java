package com.novelbio.nbcReport.Params;

import java.util.Map;

import org.apache.log4j.Logger;
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
		mapKey2Param.putAll(mapTempName2setReportBase);
		mapKey2Param.put("projectName", projectName);
		return mapKey2Param;
	}

	/** 为项目报告添加名称 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

}
