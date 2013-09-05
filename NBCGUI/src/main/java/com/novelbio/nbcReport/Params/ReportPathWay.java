package com.novelbio.nbcReport.Params;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.novelbio.nbcReport.XdocTmpltExcel;
import com.novelbio.nbcReport.XdocTmpltPic;

public class ReportPathWay extends ReportBase{
	/** 所使用的数据库 */
	private String db;
	/** 筛选条件 */
	private String finderCondition;
	private int upRegulation;
	private String teamName;
	private int downRegulation;
	private Set<String> setResultFiles;
	private List<XdocTmpltPic> lsXdocTmpltPics;
	private List<XdocTmpltExcel> lsXdocTmpltExcels;
	
	
	public ReportPathWay() {
		// TODO Auto-generated constructor stub
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
	 * 添加表格模板
	 * @param xdocTmpltExcel
	 */
	public void addXdocTempExcel(XdocTmpltExcel xdocTmpltExcel) {
		if (lsXdocTmpltExcels == null) {
			lsXdocTmpltExcels = new ArrayList<XdocTmpltExcel>();
		}
		lsXdocTmpltExcels.add(xdocTmpltExcel);
	}
	
	/** 实验组名 */
	public String getTeamName() {
		return teamName;
	}
	/** 实验组名 */
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	
	/**
	 * 取得所有的表格集合
	 * @return
	 */
	public List<String> getExcels(){
		List<String> lsExcels = new ArrayList<String>();
//		for (XdocTmpltExcel xdocTmpltExcel : lsXdocTmpltExcels) {
//			lsExcels.add(xdocTmpltExcel.toString());
//		}
		return lsExcels;
	}
	

	public String getDb() {
		return db;
	}
	/** 所使用的数据库 */
	public void setDb(String db) {
		this.db = db;
	}
	public String getFinderCondition() {
		return finderCondition;
	}
	/** 筛选条件 */
	public void setFinderCondition(String finderCondition) {
		this.finderCondition = finderCondition;
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
	
	public Set<String> getSetResultFiles() {
		Set<String> setResultFiles1 = new LinkedHashSet<>();
		for (String file : setResultFiles) {
			setResultFiles1.add(EnumReport.PathWay.getResultFolder() + file.split(EnumReport.PathWay.getResultFolder())[1]);
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

	@Override
	public EnumReport getEnumReport() {
		return EnumReport.PathWay;
	}

	@Override
	public boolean readReportFromFile(String savePath) {
		return false;
	}
}
