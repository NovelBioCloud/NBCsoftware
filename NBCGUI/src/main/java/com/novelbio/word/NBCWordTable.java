package com.novelbio.word;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

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
	
	public void insertToDoc(Selection selection) {
		// TODO
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
	public void add(String excelPath,String sheetName){
		mapExcelPath2SheetName.put(excelPath, sheetName);
	}

}
