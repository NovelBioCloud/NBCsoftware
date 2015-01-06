package com.novelbio.testReport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.novelbio.base.PathDetail;
import com.novelbio.base.dataOperate.ExcelTxtRead;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.word.ExcelDataFormat;

public class ReportTable {
	
	/**表格的名称*/
	private String tableTitle;
	/**表格的行数*/
	private int rowNum = 10;
	/**excel表格的路径*/
	private String excelPath;
	/**表格的参数*/
	private Map<String, Object> mapKey2Param = new HashMap<String, Object>();
	
	/**获得表格的参数*/
	public Map<String, Object> getMapKey2Param() {
		mapKey2Param.put("tableTitle", tableTitle);
		mapKey2Param.put("lsLsData", getLsLsData());
		return mapKey2Param;
	}
	
	/**获得excel中的表格数据*/
	private List<List<String>> getLsLsData() {
		//生成临时文件路径，从hdfs复制文件
		PathDetail.getTmpHdfsPath();
		String tmpPath = PathDetail.getTmpPath();
		FileOperate.copyFile(excelPath, tmpPath, true);
		List<List<String>>  data = ExcelTxtRead.readLsExcelTxtls(tmpPath, tableTitle, 1, rowNum);
		data = formatDataList(data);
		return data;
	}
	
	/**对从excel中获得的数据进行格式化*/
	private List<List<String>> formatDataList(List<List<String>> lsAllDatas) {
		List<List<String>> lsNewDatas = new ArrayList<List<String>>();
		if(lsAllDatas.size() == 0)
			return lsAllDatas;
		List<String> lsTitles = lsAllDatas.get(0);
		lsNewDatas.add(lsTitles);
		for (int i = 1; i < lsAllDatas.size(); i++) {
			List<String> lsData = new ArrayList<String>();
			for (int j = 0; j < lsTitles.size(); j++) {
				try {
					lsData.add(ExcelDataFormat.format(lsTitles.get(j), lsAllDatas.get(i).get(j)));
				} catch (Exception e) {
					lsData.add(ExcelDataFormat.format(lsTitles.get(j), lsAllDatas.get(i).get(j)));
					e.printStackTrace();
				}
			}
			lsNewDatas.add(lsData);
		}
		return lsNewDatas;
	}
	
	/**表格的名称*/
	public String getTableTitle() {
		return tableTitle;
	}
	/**表格的名称*/
	public void setTableTitle(String tableTitle) {
		this.tableTitle = tableTitle;
	}
	/**表格的行数*/
	public int getRowNum() {
		return rowNum;
	}
	/**表格的行数*/
	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}
	/**excel表格的路径*/
	public String getExcelPath() {
		return excelPath;
	}
	/**excel表格的路径*/
	public void setExcelPath(String excelPath) {
		this.excelPath = excelPath;
	}

}
