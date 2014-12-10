package com.novelbio.word;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.novelbio.base.dataOperate.ExcelTxtRead;
import com.novelbio.word.ExcelDataFormat;

public class NBCWordTable implements Serializable {
	
	private static final long serialVersionUID = 1L;
	/** 表格的标题 */
	private String title = "";
	/** 表格的注： */
	private String note = ""; 
	/** 表格的上方文本 */
	private String upCompare = "";
	/** 表格的下方文本 */
	private String downCompare = "";
	/** 实验组名 */
	private String expTeamName = "";
	/** 是否需要换行*/
	private boolean withEnter = false;
	/** 存在时的文本内容*/
	private String existText;
	/** 最大读取行数 默认15行*/
	private int maxRow = 15;
	/** 表格所在路径和sheet的集合 */
	private Map<String,String> mapExcelPath2SheetName = new LinkedHashMap<>();
	
	/**
	 * 插入文档
	 * @param selection
	 */
	public void insertToDoc(Selection selection) {
		String pattern = selection.getText();
		paresePattern(pattern);
		if (existText != null){
			selection.replaceSelected(existText);
			return;
		}
		insertAndSet(selection);
		System.out.println("插入表格"+pattern);
	}
	
	/**
	 * 插入表格并设置表格相关信息
	 * @param selection
	 */
	private void insertAndSet(Selection selection) {
		selection.setParagraphsProperties(0, 0, 0, 0, 0);
		selection.defaultFont();
		if (!upCompare.equals("")) {
			selection.setParagraphsProperties(0, 0, 0, 0, 2);
			selection.writeText(upCompare);
			selection.nextRow();
		}
		if(!title.equals("")){
			selection.setParagraphsProperties(1, 0, 0, 0, 2);
			selection.writeText(title);
			selection.nextRow();
		}
		selection.defaultParagraphStyle();
		formatTableDataAndInsert(selection);
		selection.nextRow();
		if (!note.equals("")) {
			selection.setParagraphsProperties(0, 0, 0, 0, 2);
			selection.writeText(note);
			selection.nextRow();
		}
		if(!downCompare.equals("")) {
			selection.writeText(downCompare);
			selection.nextRow();
		}
		if(withEnter)
			selection.nextRow();
	}
	
	/**
	 * 格式化表格数据并插入文档
	 * @param selection
	 */
	private void formatTableDataAndInsert(Selection selection) {
		for(String excelPath : mapExcelPath2SheetName.keySet()){
			String sheetName = mapExcelPath2SheetName.get(excelPath);
			List<List<String>>  data = ExcelTxtRead.readLsExcelTxtls(excelPath, sheetName, 1, maxRow);
			if(data.size() == 0)
				continue;
			data = formatDataList(data);
			selection.wirteTable(data);
		}
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
					lsData.add(ExcelDataFormat.format(lsTitles.get(j), lsAllDatas.get(i).get(j)));
					e.printStackTrace();
				}
			}
			lsNewDatas.add(lsData);
		}
		return lsNewDatas;
	}
	
	/**
	 * 解析选中文本
	 * @param pattern
	 */
	private void paresePattern(String pattern){
		if (!(pattern.startsWith("${") && pattern.endsWith("}"))) {
			throw new RuntimeException("word表达式:" + pattern + "异常");
		}
		String patternLeft = pattern.substring(2, pattern.length() - 1);
		this.withEnter = patternLeft.contains("##n|");
		String[] methods = patternLeft.split("##");
		for (int i = 0; i < methods.length; i++) {
			if (i == 0)
				continue;
			if (methods[i].startsWith("e|"))
				this.existText = methods[i].split("e\\|")[1];
			else if (methods[i].startsWith("r|")){
				try {
					this.maxRow = Integer.parseInt(methods[i].split("r\\|")[1]);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}else if (methods[i].startsWith("n|"))
				this.withEnter = true;
		}
	}
	
	/** 表格的标题 */
	public String getTitle() {
		return title;
	}
	/** 表格的标题 */
	public void setTitle(String title) {
		this.title = title;
	}
	/** 表格的注： */
	public String getNote() {
		return note;
	}
	/** 表格的注： */
	public void setNote(String note) {
		this.note = note;
	}
	/** 表格的上方文本 */
	public String getUpCompare() {
		return upCompare;
	}
	/** 表格的上方文本 */
	public void setUpCompare(String upCompare) {
		this.upCompare = upCompare;
	}
	/** 表格的下方文本 */
	public String getDownCompare() {
		return downCompare;
	}
	/** 表格的下方文本 */
	public void setDownCompare(String downCompare) {
		this.downCompare = downCompare;
	}
	/** 实验组名 */
	public String getExpTeamName() {
		return expTeamName;
	}
	/** 实验组名 */
	public void setExpTeamName(String expTeamName) {
		this.expTeamName = expTeamName;
	}
	/** 是否需要换行*/
	public boolean isWithEnter() {
		return withEnter;
	}
	/** 是否需要换行*/
	public void setWithEnter(boolean withEnter) {
		this.withEnter = withEnter;
	}
	/** 存在时的文本内容*/
	public String getExistText() {
		return existText;
	}
	/** 存在时的文本内容*/
	public void setExistText(String existText) {
		this.existText = existText;
	}
	/** 最大读取行数 默认15行*/
	public int getMaxRow() {
		return maxRow;
	}
	/** 最大读取行数 默认15行*/
	public void setMaxRow(int maxRow) {
		this.maxRow = maxRow;
	}
	/** 表格所在路径和sheet的集合 */
	public Map<String, String> getMapExcelPath2SheetName() {
		return mapExcelPath2SheetName;
	}
	/** 表格所在路径和sheet的集合 */
	public void setMapExcelPath2SheetName(Map<String, String> mapExcelPath2SheetName) {
		this.mapExcelPath2SheetName = mapExcelPath2SheetName;
	}
	/**
	 * 添加表格信息
	 * @param excelPath
	 * @param sheetName
	 */
	public void addMapExcelPath2SheetName(String excelPath,String sheetName){
		mapExcelPath2SheetName.put(excelPath, sheetName);
	}

}