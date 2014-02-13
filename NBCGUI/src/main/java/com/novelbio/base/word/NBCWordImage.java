package com.novelbio.base.word;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 自动化报告图片参数类
 * @author novelbio
 *
 */
public class NBCWordImage implements Serializable{
	private static final long serialVersionUID = 1L;
	/** 图片的标题 */
	private String title = "";
	/** 图片的注： */
	private String note = ""; 
	/** 图片的上方文本 */
	private String upCompare = "";
	/** 图片的下方文本 */
	private String downCompare = "";
	/** 实验组名 */
	private String expTeamName = "";
	// 是否需要换行
	private boolean withEnter = false;
	// 存在时的文本内容
	private String existText;
	/** 图片对齐方式 null则默认居中 0-左对齐, 1-居中, 2-右对齐, 3-两端对齐, 4-分散对齐*/
	private Integer align = null;
	/** 图片宽度 ,null则使用原始宽度*/
	private Integer width = null;
	/** 图片高度 ,null则使用原始高度*/
	private Integer height = null;
	/** 图片所在路径 */
	List<String> lsPicPaths = new ArrayList<>();
	
	/**
	 * 添加到word中
	 * @param selection 选中的插入点
	 * @param pattern 填充的参数表达式
	 * @throws Exception 
	 */
	public void addToWord(Selection selection,String pattern) throws Exception {
		if(lsPicPaths.size() == 0)
			return;
		paresePattern(pattern);
		if (existText != null){
			selection.replaceSelected(existText);
			return;
		}
		addPictures(selection);
	}
	
	/**
	 * 转义表达式
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
			else if (methods[i].startsWith("n|"))
				this.withEnter = true;
		}
	}
	/**
	 * 添加图片及说明等到word中
	 * @throws Exception
	 */
	private void addPictures(Selection selection) throws Exception{
		selection.defaultFont();
		if(!upCompare.equals("")){
			selection.setParagraphsProperties(0,0,0,0,2);
			selection.writeText(upCompare);
			selection.nextRow();
		}
		selection.defaultParagraphStyle();
		selection.insertPicture(lsPicPaths,title,align,width,height);
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
	/** 图片所在路径 */
	public List<String> getLsPicPaths() {
		return lsPicPaths;
	}
	/** 图片所在路径 */
	public void setLsPicPaths(List<String> lsPicPaths) {
		this.lsPicPaths = lsPicPaths;
	}
	/**
	 * 添加图片路径
	 * @param picPath
	 * @return
	 */
	public List<String> addPicPath(String picPath){
		lsPicPaths.add(picPath);
		return lsPicPaths;
	}
	/** 图片对齐方式 null则默认居中 0-左对齐, 1-居中, 2-右对齐, 3-两端对齐, 4-分散对齐*/
	public Integer getAlign() {
		return align;
	}
	/** 图片对齐方式 null则默认居中 0-左对齐, 1-居中, 2-右对齐, 3-两端对齐, 4-分散对齐*/
	public void setAlign(Integer align) {
		this.align = align;
	}
	/** 图片宽度 ,null则使用原始宽度*/
	public Integer getWidth() {
		return width;
	}
	/** 图片宽度 ,null则使用原始宽度*/
	public void setWidth(Integer width) {
		this.width = width;
	}
	/** 图片高度 ,null则使用原始高度*/
	public Integer getHeight() {
		return height;
	}
	/** 图片高度 ,null则使用原始高度*/
	public void setHeight(Integer height) {
		this.height = height;
	}
	
}
