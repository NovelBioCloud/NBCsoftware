package com.novelbio.nbcReport.Params;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;

import com.novelbio.base.word.NBCWordImage;

/**
 * GOAnalysis参数对象类，记录结果报告所需要的参数
 * 
 * @author novelbio
 * 
 */
public class ReportGO extends ReportBase{
	private static final long serialVersionUID = 5362772692510596256L;
	/**　测试方法　*/
	private String testMethod;
	/**　筛选条件　*/
	private String finderCondition;
	/**　组名　*/
	private String teamName;
	/**　上调数　*/
	private int upRegulation;
	/**　下调数　*/
	private int downRegulation;
	/**　结果文件路径集合　*/
	private HashSet<String> setResultFiles = new LinkedHashSet<>();
	/**　结果图片集合　*/
	private HashSet<NBCWordImage> setNBCWordImages = new LinkedHashSet<>();
	
	
	public ReportGO() {
	}
	
	/**
	 * 添加图片模板
	 * @param xdocTmpltPic
	 */
	public void addNBCWordImage(NBCWordImage nbcWordImage) {
		setNBCWordImages.add(nbcWordImage);
	}
	
	@Override
	public Map<String, Object> buildFinalParamMap() {
		mapKey2Param.put("testMethod", testMethod);
		mapKey2Param.put("setResultFiles", setResultFiles);
		mapKey2Param.put("finderCondition", finderCondition);
		mapKey2Param.put("teamName", teamName);
		mapKey2Param.put("upRegulation", upRegulation);
		mapKey2Param.put("downRegulation", downRegulation);
		mapKey2Param.putAll(mapTempName2setReportBase);
		return mapKey2Param;
	}
	/**　测试方法　*/
	public void setTestMethod(String testMethod) {
		this.testMethod = testMethod;
	}
	/**　筛选条件　*/
	public void setFinderCondition(String finderCondition) {
		this.finderCondition = finderCondition;
	}
	
	/** 实验组名 */
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	/**　上调数　*/
	public void setUpRegulation(int upRegulation) {
		this.upRegulation = upRegulation;
	}

	/**　下调数　*/
	public void setDownRegulation(int downRegulation) {
		this.downRegulation = downRegulation;
	}
	/**　结果文件路径集合　*/
	public void addResultFile(String resultFile) {
		setResultFiles.add(resultFile);
	}

	@Override
	public EnumReport getEnumReport() {
		return EnumReport.GOAnalysis;
	}
}
