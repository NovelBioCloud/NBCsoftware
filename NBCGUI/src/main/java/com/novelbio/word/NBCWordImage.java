package com.novelbio.word;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
	/** 是否需要换行*/
	private boolean withEnter = false;
	/** 存在时的文本内容*/
	private String existText;
	/** 图片对齐方式 null则默认居中 0-左对齐, 1-居中, 2-右对齐, 3-两端对齐, 4-分散对齐*/
	private Integer align = null;
	/** 图片宽度 ,null则使用原始宽度*/
	private Integer width = null;
	/** 图片高度 ,null则使用原始高度*/
	private Integer height = null;
	/** 图片所在路径 */
	private List<String> lsPicPaths = new ArrayList<>();
	
	/**
	 * 插入文档中
	 * @param selection
	 */
	public void insertToDoc(Selection selection) {
		if(lsPicPaths.size() == 0)
			return;
		
		String pattern = selection.getText();
		paresePattern(pattern);
		
		if (existText != null){
			selection.replaceSelected(existText);
			return;
		}
		
		insertAndSet(selection);
		
		System.out.println("插入图片"+pattern);
	}
	
	/**
	 * 插入并设置图片和文本的参数
	 * @param selection
	 */
	private void insertAndSet(Selection selection) {
		selection.defaultFont();
		if(!upCompare.equals("")){
			selection.setParagraphsProperties(0,0,0,0,2);
			selection.writeText(upCompare);
			selection.nextRow();
		}
		selection.defaultParagraphStyle();
		selection.writePicture(lsPicPaths,title,align,width,height);
		selection.nextRow();
		if(!title.equals("")) {
			selection.setParagraphsProperties(1,0,0,0,2);
			selection.writeText(title);
			selection.nextRow();
		}
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
	
	/**
	 * 解析选中的文本
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
	/** 图片对齐方式*/
	public Integer getAlign() {
		return align;
	}
	/** 图片对齐方式*/
	public void setAlign(Integer align) {
		this.align = align;
	}
	/** 图片宽度*/
	public Integer getWidth() {
		return width;
	}
	/** 图片宽度*/
	public void setWidth(Integer width) {
		this.width = width;
	}
	/** 图片高度*/
	public Integer getHeight() {
		return height;
	}
	/** 图片高度*/
	public void setHeight(Integer height) {
		this.height = height;
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
	
}
