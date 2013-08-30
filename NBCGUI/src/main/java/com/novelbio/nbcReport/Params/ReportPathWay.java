package com.novelbio.nbcReport.Params;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.novelbio.nbcReport.XdocTmpltExcel;
import com.novelbio.nbcReport.XdocTmpltPic;

public class ReportPathWay extends ReportBase{
	private String no = "${no}";
	/** 所使用的数据库 */
	private String db;
	/** 筛选条件 */
	private String finderCondition;
	private int upRegulation;
	private int downRegulation;
	private List<String> lsResultFiles;
	private List<XdocTmpltPic> lsXdocTmpltPics;
	private List<XdocTmpltExcel> lsXdocTmpltExcels;
	
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
	
	/**
	 * 取得所有的图片集合
	 * @return
	 */
	public List<String> getExcels(){
		List<String> lsExcels = new ArrayList<String>();
		for (XdocTmpltExcel xdocTmpltExcel : lsXdocTmpltExcels) {
			lsExcels.add(xdocTmpltExcel.toString());
		}
		return lsExcels;
	}
	
	@Override
	public EnumReport getEnumReport() {
		return EnumReport.PathWay;
	}


	@Override
	protected Map<String, Object> addParamMap() {
		Map<String, Object> mapKey2Params = new HashMap<String, Object>();
		mapKey2Params.put("no", no);
		mapKey2Params.put("db", db);
		mapKey2Params.put("finderCondition", finderCondition);
		mapKey2Params.put("upRegulation", upRegulation);
		mapKey2Params.put("downRegulation", downRegulation);
		mapKey2Params.put("lsResultFiles", lsResultFiles);
		mapKey2Params.put("pictures", getPictures());
		mapKey2Params.put("excels", getExcels());
		return mapKey2Params;
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
	public String getNo() {
		return no;
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
		return lsResultFiles;
	}

	public void addResultFile(String resultFile) {
		if (lsResultFiles == null) {
			lsResultFiles = new ArrayList<String>();
		}
		lsResultFiles.add(resultFile);
	}
}
