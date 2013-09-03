package com.novelbio.nbcReport.Params;

import java.util.ArrayList;
import java.util.List;

import com.novelbio.nbcReport.XdocTmpltExcel;
import com.novelbio.nbcReport.XdocTmpltPic;

/**
 * GOAnalysis参数对象类，记录结果报告所需要的参数
 * 
 * @author novelbio
 * 
 */
public class ReportQC{
	private String teamName;
	private List<String> lsResultFiles;
	private List<XdocTmpltPic> lsXdocTmpltPics;
	private List<XdocTmpltPic> lsXdocTmpltPics1;
	
	
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
	
	public List<String> getLsResultFiles() {
		List<String> lsResultRealFiles = new ArrayList<>();
		for (String file : lsResultFiles) {
			lsResultRealFiles.add(EnumReport.FastQC.getResultFolder() + file.split(EnumReport.FastQC.getResultFolder())[1]);
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
