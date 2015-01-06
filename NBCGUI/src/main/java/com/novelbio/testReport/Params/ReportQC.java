package com.novelbio.testReport.Params;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.novelbio.nbcReport.XdocTmpltPic;

/**
 * GOAnalysis参数对象类，记录结果报告所需要的参数
 * 
 * @author novelbio
 * 
 */
public class ReportQC extends ReportBase{
	private String teamName;
	private String basicStatExcelPath;
	private Set<String> setResultFiles;
	private List<XdocTmpltPic> lsXdocTmpltPics;
	private List<XdocTmpltPic> lsXdocTmpltPics1;
	
	public ReportQC() {
	}
	
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
	
	/**
	 * 添加图片模板
	 * @param xdocTmpltPic
	 */
	public void addXdocTempPic1(XdocTmpltPic xdocTmpltPic) {
		if (lsXdocTmpltPics1 == null) {
			lsXdocTmpltPics1 = new ArrayList<XdocTmpltPic>();
		}
		lsXdocTmpltPics1.add(xdocTmpltPic);
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
	
	/**
	 * 取得所有的图片集合
	 * @return
	 */
	public List<String> getPictures1(){
		List<String> lsPictures1 = new ArrayList<String>();
		for (XdocTmpltPic xdocTmpltPic : lsXdocTmpltPics1) {
			lsPictures1.add(xdocTmpltPic.toString());
		}
		return lsPictures1;
	}
	


	/** 实验组名 */
	public String getTeamName() {
		return teamName;
	}
	/** 实验组名 */
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	
	public Set<String> getLsResultFiles() {
		Set<String> setResultFiles1 = new LinkedHashSet<>();
		for (String file : setResultFiles) {
			setResultFiles1.add(EnumReport.FastQC.getResultFolder() + file.split(EnumReport.FastQC.getResultFolder())[1]);
		}
		return setResultFiles1;
	}
	
	public Set<String> getSetResultRealFiles(){
		return setResultFiles;
	}

	public void addResultFile(String resultFile) {
		if (setResultFiles == null) {
			setResultFiles = new LinkedHashSet<>();
		}
		setResultFiles.add(resultFile);
	}
	
	/**
	 * 总的统计文件路径
	 * @param basicStatExcelPath
	 */
	public void setBasicStatExcelPath(String basicStatExcelPath) {
		this.basicStatExcelPath = basicStatExcelPath;
	}
	
	/**
	 * 总的统计文件路径
	 */
	public String getBasicStatExcelPath() {
		return basicStatExcelPath;
	}
	
	@Override
	public EnumReport getEnumReport() {
		return EnumReport.FastQC;
	}

}
