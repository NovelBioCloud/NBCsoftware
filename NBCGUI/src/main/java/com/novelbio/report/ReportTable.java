package com.novelbio.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.novelbio.base.PathDetail;
import com.novelbio.base.dataOperate.ExcelTxtRead;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.word.ExcelDataFormat;

public class ReportTable {
	/**
	 *  获得表格的参数
	 * @param tableTitle 表格的标题
	 * @param lsLsData List<String[]>类型的数据
	 * @param columnNum 列数
	 * @return
	 */
	public Map<String, Object> getMapKey2Param(String tableTitle, String label, List<String[]> lsLsData, int columnNum) {
		Map<String, Object> mapKey2Param = new HashMap<String, Object>();
		mapKey2Param.put("tableTitle", tableTitle);
		mapKey2Param.put("label", label);
		mapKey2Param.put("lsFirstColumn", getFirstColumn(lsLsData));
		mapKey2Param.put("lsLsData", lsLsData);
		//表格的列数需要除掉第一列，渲染表格时需要
		mapKey2Param.put("columnNum", columnNum - 1);
		return mapKey2Param;
	}
	
	private List<String> getFirstColumn(List<String[]> lsLsData) {
		List<String> firstColumn = new ArrayList<String>();
		for (String[] lsData : lsLsData) {
			firstColumn.add(lsData[0]);
		}
		return firstColumn;
	}
	
	/**获得表格的参数*/
	public Map<String, Object> getMapKey2Param(String tableTitle,String label, String excelPath, int columnNum) {
		Map<String, Object> mapKey2Param = new HashMap<String, Object>();
		mapKey2Param.put("tableTitle", tableTitle);
		mapKey2Param.put("label", label);
		mapKey2Param.put("lsLsData", getLsLsData(excelPath));
		return mapKey2Param;
	}
	
	/** 获得excel中的表格数据 */
	private List<String[]> getLsLsData(String excelPath) {
		//生成临时文件路径，从hdfs复制文件
		PathDetail.getTmpHdfsPath();
		String tmpPath = PathDetail.getTmpPath();
		FileOperate.copyFile(excelPath, tmpPath, true);
		List<String[]>  data = ExcelTxtRead.readLsExcelTxt(excelPath, 1);
		 data = formatDataList(data);
		return data;
	}
	
	/**
	 * 获得list中的表格数据
	 * @param lsInfo 第一行是title
	 * @return
	 */
	private List<String[]> getLsLsData(List<String[]> lsInfo) {
		List<String[]> data = formatDataList(lsInfo);
		return data;
	}
	
	/**对从excel中获得的数据进行格式化*/
	private List<String[]> formatDataList(List<String[]> lsAllDatas) {
		List<String[]> lsNewDatas = new ArrayList<String[]>();
		if(lsAllDatas.size() == 0)
			return lsAllDatas;
		String[] lsTitles = lsAllDatas.get(0);
		lsNewDatas.add(lsTitles);
		for (int i = 1; i < lsAllDatas.size(); i++) {
			List<String> lsData = new ArrayList<String>();
			for (int j = 0; j < lsTitles.length; j++) {
				try {
					lsData.add(ExcelDataFormat.format(lsTitles[j], lsAllDatas.get(i)[j]));
				} catch (Exception e) {
					lsData.add(ExcelDataFormat.format(lsTitles[j], lsAllDatas.get(i)[j]));
					e.printStackTrace();
				}
			}
			lsNewDatas.add((String[])lsData.toArray());
		}
		return lsNewDatas;
	}

}
