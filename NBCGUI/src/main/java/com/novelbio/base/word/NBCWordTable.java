package com.novelbio.base.word;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class NBCWordTable implements Serializable{
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
	// 是否需要换行
	private boolean withEnter = false;
	/** 最大读取行数 默认15行*/
	private int maxRow = 15;
	/** 表格所在路径和sheet的集合 */
	Map<String,String> mapExcelPath2SheetName = new LinkedHashMap<>();
	
	/**
	 * 添加到word中
	 * @param selection 选中的插入点
	 * @param pattern 填充的参数表达式
	 * @throws Exception 
	 */
	public void addToWord(Selection selection,String pattern) throws Exception {
		if(mapExcelPath2SheetName.size() == 0)
			return;
		paresePattern(pattern);
		addExcels(selection);
	}
	
	/**
	 * 转义表达式
	 * r|最大行数
	 * n|换行
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
	/**
	 * 添加表格及说明等到word中
	 * @throws Exception
	 */
	private void addExcels(Selection selection) throws Exception{
		selection.setParagraphsProperties(0,0,0,0,0);
		selection.defaultFont();
		if(!upCompare.equals("")){
			selection.setParagraphsProperties(0,0,0,0,2);
			selection.writeText(upCompare);
			selection.nextRow();
		}
		selection.defaultParagraphStyle();
		selection.insertTablesFromExcel(mapExcelPath2SheetName,title,maxRow);
		selection.nextRow();
		if(!note.equals("")){
			selection.setParagraphsProperties(0,0,0,0,2);
			selection.writeText(note);
			selection.nextRow();
		}
		if(!downCompare.equals("")){
			selection.writeText(downCompare);
			selection.nextRow();
		}
		if(withEnter)
			selection.nextRow();
	}
	/** 图片的标题 */
	public String getTitle() {
		return title;
	}
	/** 图片的标题 */
	public void setTitle(String title) {
		this.title = title;
	}
	/** 图片的注： */
	public String getNote() {
		return note;
	}
	/** 图片的注： */
	public void setNote(String note) {
		this.note = note;
	}
	/** 图片的上方文本 */
	public String getUpCompare() {
		return upCompare;
	}
	/** 图片的上方文本 */
	public void setUpCompare(String upCompare) {
		this.upCompare = upCompare;
	}
	/** 图片的下方文本 */
	public String getDownCompare() {
		return downCompare;
	}
	/** 图片的下方文本 */
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
