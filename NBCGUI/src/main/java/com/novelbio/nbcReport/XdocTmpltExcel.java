package com.novelbio.nbcReport;

import java.io.File;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.google.common.collect.HashMultimap;
import com.novelbio.base.dataOperate.ExcelTxtRead;
import com.novelbio.nbcReport.Params.EnumReport;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * excel读取结果实体类
 * @author gaozhu
 *
 */
public class XdocTmpltExcel implements Serializable{
	private static final long serialVersionUID = 1L;
	/** 表格的标题 */
	private String excelTitle = "";
	/** 表格的注： */
	private String note = ""; 
	/** 同类表格的对比说明在这类表格的上方 */
	private String upCompare = "";
	/** 同类表格的对比说明在这类表格的下方 */
	private String downCompare = "";
	private List<XdocTable> lsXdocTable = new ArrayList<XdocTable>();
	private Map<String, LinkedHashSet<String>> mapExcel2SheetNames = new LinkedHashMap<>();
	private Map<String, LinkedHashSet<Integer>> mapExcel2SheetNamesInt = new LinkedHashMap<>();
	private XdocTable xdocTable;
	
	public XdocTmpltExcel() {
		// TODO Auto-generated constructor stub
	}
	
	/** 根据excel路径完成本类的构造
	 * @param filePath
	 * @param excelName
	 */
	public XdocTmpltExcel(Map<String,LinkedHashSet<String>> mapExcel2SheetNames,XdocTable xdocTable) {
		this.mapExcel2SheetNames = mapExcel2SheetNames;
		this.xdocTable = xdocTable;
	}
	
	/**请使用addExcel添加数据*/
	public XdocTmpltExcel(XdocTable xdocTable){
		this.xdocTable = xdocTable;
	}
	
	/** 根据excel路径完成本类的构造
	 * @param excelName 全路径
	 * @param sheetName
	 */
	public XdocTmpltExcel(String excelName, String sheetName,XdocTable xdocTable) {
		LinkedHashSet<String> setSheetNames = mapExcel2SheetNames.get(excelName);
		if (setSheetNames == null) {
			setSheetNames = new LinkedHashSet<>();
			setSheetNames.add(sheetName);
			mapExcel2SheetNames.put(excelName, setSheetNames);
		}else {
			setSheetNames.add(sheetName);
		}
		this.xdocTable = xdocTable;
	}
	
	public List<String> getAllExcelFileName(){
		List<String> lsExcelNames = new ArrayList<>();
		lsExcelNames.addAll(mapExcel2SheetNames.keySet());
		return lsExcelNames;
	}
	
	/**
	 * 新增同类的表
	 * @param excelName
	 * @param sheetName
	 */
	public void addExcel(String excelName, String sheetName){
		LinkedHashSet<String> setSheetNames = mapExcel2SheetNames.get(excelName);
		if (setSheetNames == null) {
			setSheetNames = new LinkedHashSet<>();
			setSheetNames.add(sheetName);
			mapExcel2SheetNames.put(excelName, setSheetNames);
		}else {
			setSheetNames.add(sheetName);
		}
	}
	
	public void addExcel(String excelName, int sheetNum){
		LinkedHashSet<Integer> setSheetNames = mapExcel2SheetNamesInt.get(excelName);
		if (setSheetNames == null) {
			setSheetNames = new LinkedHashSet<>();
			setSheetNames.add(sheetNum);
			mapExcel2SheetNamesInt.put(excelName, setSheetNames);
		}else {
			setSheetNames.add(sheetNum);
		}
	}
	
	/** 读取excel的说明文件中的参数（允许不存在）*/
	private Map<String, Object> addParams(){
		Map<String, Object> mapKey2Param = new HashMap<String, Object>();
		mapKey2Param.put("excelTitle",excelTitle);
		mapKey2Param.put("note",note);
		mapKey2Param.put("upCompare",upCompare);
		mapKey2Param.put("downCompare",downCompare);
		for(String key : mapExcel2SheetNames.keySet()){
			for(String sheetName : mapExcel2SheetNames.get(key)){
				XdocTable xdocTableClone = xdocTable.getClone();
				xdocTableClone.setLsExcelTable(formatDataList(ExcelTxtRead.readLsExcelTxtls(key, sheetName, 1)));
				lsXdocTable.add(xdocTableClone);
			}
		}
		for(String key : mapExcel2SheetNamesInt.keySet()){
			for(int sheetNum : mapExcel2SheetNamesInt.get(key)){
				XdocTable xdocTableClone = xdocTable.getClone();
				List<List<String>> lsls = ExcelTxtRead.readLsExcelTxtls(key, sheetNum, 1);
				xdocTableClone.setLsExcelTable(formatDataList(lsls));
				lsXdocTable.add(xdocTableClone);
			}
		}
		//使用枚举格式化Excel中的数据
		mapKey2Param.put("lsXdocTable",lsXdocTable);
		return mapKey2Param;
	}
	
	/**把所有的表格数据格式化*/
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
					String aString  = lsTitles.get(j);
					String bString = lsAllDatas.get(i).get(j);
					lsData.add(ExcelDataFormat.format(lsTitles.get(j), lsAllDatas.get(i).get(j)));
					e.printStackTrace();
				}
			}
			lsNewDatas.add(lsData);
		}
		return lsNewDatas;
	}
	
	/** 输出渲染好的xdoc的toString结果 */
	@Override
	public String toString(){
		Map<String, Object> mapKey2Params = addParams();
		/** 把子xdoc的toString方法封装成集合传递给本xdoc */
		try {
			Configuration cf = new Configuration();
			cf.setClassicCompatible(true);
			// 模板存放路径
			cf.setClassForTemplateLoading(EnumReport.class,EnumReport.Excel.getTempPath());
			cf.setEncoding(Locale.getDefault(), "UTF-8");
			// 模板名称
			Template template = cf.getTemplate(EnumReport.Excel.getTempName());
			StringWriter sw = new StringWriter();
			// 处理并把结果输出到字符串中
			template.process(mapKey2Params, sw);
			return sw.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	/** 表格的标题 */
	public void setExcelTitle(String excelTitle) {
		this.excelTitle = excelTitle;
	}
	/** 表格的注： */
	public void setNote(String note) {
		this.note = note;
	}
	/** 同类表格的对比说明在这类表格的上方 */
	public void setUpCompare(String upCompare) {
		this.upCompare = upCompare;
	}
	/** 同类表格的对比说明在这类表格的下方 */
	public void setDownCompare(String downCompare) {
		this.downCompare = downCompare;
	}
	
}
