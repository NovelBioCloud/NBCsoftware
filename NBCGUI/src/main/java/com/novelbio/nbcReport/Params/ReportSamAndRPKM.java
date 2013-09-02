package com.novelbio.nbcReport.Params;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.novelbio.nbcReport.XdocTmpltExcel;
import com.novelbio.nbcReport.XdocTmpltPic;

public class ReportSamAndRPKM  extends ReportBase{
	List<String> lsResultFile;
	List<XdocTmpltExcel> lsExcels;
	List<XdocTmpltExcel> lsExcels2;
	List<XdocTmpltPic> lsTmpltPics;
	String excelName1;
	String excelName2;
	String excelName3;
	String excelName4;
	

	public List<String> getLsResultFile() {
		return lsResultFile;
	}

	public void setLsResultFile(List<String> lsResultFile) {
		this.lsResultFile = lsResultFile;
	}

	public List<String> getLsExcels() {
		List<String> lsList = new ArrayList<>();
		for (XdocTmpltExcel xdocExcel : lsExcels) {
			lsList.add(xdocExcel.toString());
		}
		return lsList;
	}
	
	public List<String> getLsExcels2() {
		List<String> lsList = new ArrayList<>();
		for (XdocTmpltExcel xdocExcel : lsExcels2) {
			lsList.add(xdocExcel.toString());
		}
		return lsList;
	}

	public void setLsExcels(List<XdocTmpltExcel> lsExcels) {
		this.lsExcels = lsExcels;
	}

	public void setLsExcels2(List<XdocTmpltExcel> lsExcels2) {
		this.lsExcels2 = lsExcels2;
	}

	public List<String> getLsTmpltPics() {
		List<String> lsList = new ArrayList<>();
		for (XdocTmpltPic xdocPic : lsTmpltPics) {
			lsList.add(xdocPic.toString());
		}
		return lsList;
	}

	
	public String getExcelName1() {
		return excelName1;
	}

	public void setExcelName1(String excelName1) {
		this.excelName1 = excelName1;
	}

	public String getExcelName2() {
		return excelName2;
	}

	public void setExcelName2(String excelName2) {
		this.excelName2 = excelName2;
	}

	public String getExcelName3() {
		return excelName3;
	}

	public void setExcelName3(String excelName3) {
		this.excelName3 = excelName3;
	}

	public String getExcelName4() {
		return excelName4;
	}

	public void setExcelName4(String excelName4) {
		this.excelName4 = excelName4;
	}

	public void setLsTmpltPics(List<XdocTmpltPic> lsTmpltPics) {
		this.lsTmpltPics = lsTmpltPics;
	}

	@Override
	protected Map<String, Object> addParamMap() {
		HashMap<String, Object> mapParmerHashMap = new HashMap<>();
		mapParmerHashMap.put("lsResultFile", lsResultFile);
		mapParmerHashMap.put("lsExcels", lsExcels);
		mapParmerHashMap.put("lsExcels2", lsExcels2);
		mapParmerHashMap.put("lsPictures", lsTmpltPics);
		mapParmerHashMap.put("excelName1", excelName1);
		mapParmerHashMap.put("excelName2", excelName2);
		mapParmerHashMap.put("excelName3", excelName3);
		mapParmerHashMap.put("excelName4", excelName4);
		return mapParmerHashMap;
	}

	@Override
	public EnumReport getEnumReport() {
		return EnumReport.SamAndRPKM;
	}
	
}
