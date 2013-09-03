package com.novelbio.nbcReport.Params;

import java.util.ArrayList;
import java.util.List;

import com.novelbio.nbcReport.XdocTmpltPic;

/**
 * GOAnalysis参数对象类，记录结果报告所需要的参数
 * 
 * @author novelbio
 * 
 */
public class ReportGO{
	private String testMethod;
	private String finderCondition;
	private String teamName;
	private int upRegulation;
	private int downRegulation;
	private List<String> lsResultFiles;
	private List<XdocTmpltPic> lsXdocTmpltPics;
	
	/**
	 * 添加图片模板
	 * @param xdocTmpltPic
	 */
	public void addXdocTempPic(XdocTmpltPic xdocTmpltPic) {
		if (lsXdocTmpltPics == null) {
			lsXdocTmpltPics = new ArrayList<XdocTmpltPic>();
		}
		lsXdocTmpltPics.add(xdocTmpltPic);
	}
	
	public String getTestMethod() {
		return testMethod;
	}
	
	/**
	 * 取得所有的图片集合
	 * @return
	 */
	public List<String> getPictures(){
		List<String> lsPictures = new ArrayList<String>();
		for (XdocTmpltPic xdocTmpltPic : lsXdocTmpltPics) {
			lsPictures.add(xdocTmpltPic.toString());
		}
		return lsPictures;
	}
	
	public void setTestMethod(String testMethod) {
		this.testMethod = testMethod;
	}

	public String getFinderCondition() {
		return finderCondition;
	}

	public void setFinderCondition(String finderCondition) {
		this.finderCondition = finderCondition;
	}
	/** 实验组名 */
	public String getTeamName() {
		return teamName;
	}
	/** 实验组名 */
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public int getUpRegulation() {
		return upRegulation;
	}

	public void setUpRegulation(int upRegulation) {
		this.upRegulation = upRegulation;
	}

	public int getDownRegulation() {
		return downRegulation;
	}
	
	public void setDownRegulation(int downRegulation) {
		this.downRegulation = downRegulation;
	}

	public List<String> getLsResultFiles() {
		List<String> lsResultRealFiles = new ArrayList<>();
		for (String file : lsResultFiles) {
			lsResultRealFiles.add(EnumReport.GOAnalysis.getResultFolder() + file.split(EnumReport.GOAnalysis.getResultFolder())[1]);
		}
		return lsResultRealFiles;
	}
	
	public List<String> getLsResultRealFiles(){
		return lsResultFiles;
	}

	public void addResultFile(String resultFile) {
		if (lsResultFiles == null) {
			lsResultFiles = new ArrayList<String>();
		}
		lsResultFiles.add(resultFile);
	}
}
