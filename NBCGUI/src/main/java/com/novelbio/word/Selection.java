package com.novelbio.word;

import java.util.List;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.novelbio.base.PathDetail;
import com.novelbio.base.dataOperate.DateUtil;
import com.novelbio.base.fileOperate.FileOperate;

public class Selection {
	
	/**word应用程序*/
	private ActiveXComponent wordApp;
	/**所在文档*/
	private Dispatch currentDis;
	/**dispatch实例*/
	private Dispatch selectedDispatch;
	
	public Selection(ActiveXComponent wordApp, Dispatch currentDis, Dispatch selectedDis) {
		this.wordApp = wordApp;
		this.currentDis = currentDis;
		this.selectedDispatch = selectedDis;
	}
	
	public Dispatch getInstance() {
		return selectedDispatch;
	}
	
	/**替换文本*/
	public void replace(String oldText, String newText) {
		while (find(oldText, false)) {
			Dispatch.put(selectedDispatch, "Text", newText);
			if (newText != "")
				Dispatch.call(selectedDispatch, "MoveRight");
		}
	}
	
	/**查找文本*/
	public boolean find(String toFindText, boolean isRegex) {
		if (toFindText == null || toFindText.equals(""))
			return false;
		// 从selection所在位置开始查询
		Dispatch find = wordApp.call(selectedDispatch, "Find").toDispatch();
		// 设置要查找的内容
		Dispatch.put(find, "Text", toFindText);
		// 向前查找
		Dispatch.put(find, "Forward", "True");
		Dispatch.put(find, "Wrap", "1");
		// 设置格式
		Dispatch.put(find, "Format", "True");
		// 大小写匹配
		Dispatch.put(find, "MatchCase", "True");
		// 全字匹配
		Dispatch.put(find, "MatchWholeWord", "False");
		// 通配符
		Dispatch.put(find, "MatchWildcards", isRegex ? "True" : "False");
		// 查找并选中
		return Dispatch.call(find, "Execute").getBoolean();
	}
	
	/**获取当前选中文本*/
	public String getText() {
		return Dispatch.get(selectedDispatch, "Text").getString();
	}
	
	/**
	 * 写入文本
	 * @param text
	 */
	public void writeText(String text) {
		Dispatch.call(selectedDispatch, "TypeText", text);
	}
	
	/**
	 * 写入图片
	 * @param lsPicPaths
	 * @param title
	 * @param alignment
	 * @param width
	 * @param height
	 */
	public void writePicture(List<String> lsPicPaths, String title, Integer alignment, Integer width, Integer height) {
		if(alignment == null)
			alignment = 1;
		Dispatch inLineShapes = Dispatch.get(selectedDispatch, "InLineShapes").toDispatch();
		for (String imagePath : lsPicPaths) {
			//生成临时文件路径，从hdfs复制文件
			String tmpPath = FileOperate.getTempFilePath(imagePath);
			FileOperate.copyFile(imagePath, tmpPath, true);
			
			Dispatch shape = Dispatch.call(inLineShapes, "AddPicture", tmpPath).toDispatch();
			Dispatch.put(shape, "Width", width == null ? Dispatch.get(shape, "Width").getFloat() : width);
			Dispatch.put(shape, "Height", height == null ? Dispatch.get(shape, "Height").getFloat() : height);
			Dispatch.call(shape, "Select");
			Dispatch paragraphFormat = Dispatch.get(selectedDispatch, "ParagraphFormat").toDispatch();
			Dispatch.put(paragraphFormat, "Alignment", new Variant(alignment));
			Dispatch.call(selectedDispatch, "MoveRight");
			
			//删除临时文件
			FileOperate.delFile(tmpPath);
		}
		nextRow();
		Dispatch paragraphFormat = Dispatch.get(selectedDispatch, "ParagraphFormat").toDispatch();
		Dispatch.put(paragraphFormat, "Alignment", "1");
	}
	
	/**
	 * 写入表格数据
	 * @param data
	 */
	public void wirteTable(List<List<String>> data) {
		if (data.size() == 0) {
			return;
		}
		Dispatch table = createTable(data.get(0).size(),data.size());
		setTableStyleAsDefault(table);
		fillTable(table, data);
		moveDown();
	}
	
	/**
	 * 在文档中创建表格
	 * @param numCols
	 * @param numRows
	 * @return
	 */
	private Dispatch createTable(int numCols, int numRows) {
		Dispatch tables = Dispatch.get(currentDis, "Tables").toDispatch();
		Dispatch range = Dispatch.get(selectedDispatch, "Range").toDispatch();
		Dispatch newTable = Dispatch.call(tables, "Add", range, new Variant(numRows), new Variant(numCols)).toDispatch();
		return newTable;
	}
	
	/**
	 * 把数据填到表格中
	 * @param table
	 * @param data
	 */
	private void fillTable(Dispatch table, List<List<String>> data) {
		for (int i = 0; i < data.size(); i++) {
			for (int j = 0; j < data.get(i).size(); j++) {
				putTxtToCell(table, i+1, j+1, data.get(i).get(j));
			}
		}
	}
	
	/**
	 * 在指定的单元格里填写数据 
	 * @param tableIndex
	 * @param cellRowIdx
	 * @param cellColIdx
	 * @param txt
	 */
	private void putTxtToCell(Dispatch table, int cellRowIdx, int cellColIdx, String txt) {
		Dispatch cell = Dispatch.call(table, "Cell", new Variant(cellRowIdx), new Variant(cellColIdx)).toDispatch();
		Dispatch.call(cell, "Select");
		Dispatch.put(selectedDispatch, "Text", txt);
	}
	
	/**
	 * 设置默认的表格样式
	 * @param table
	 */
	private void setTableStyleAsDefault(Dispatch table) {
		Dispatch.put(table, "Style", "中等深浅底纹 1 - 强调文字颜色 5");
		Dispatch.put(table, "Spacing", "0");
		Dispatch.put(table, "PreferredWidthType", "3");
		Dispatch.put(table, "PreferredWidth", new Variant(450));
		Dispatch rows = Dispatch.get(table,"Rows").toDispatch();
		Dispatch.put(rows, "HeightRule", 2);
		Dispatch.put(rows, "Height", new Variant(15));
		Dispatch.call(table, "Select");
		Dispatch font = Dispatch.get(selectedDispatch, "Font").toDispatch();
		Dispatch.put(font, "Size", 9);
	}
	
	/**
	 * 替换已选中的文字
	 * @param newText
	 */
	public void replaceSelected(String newText) {
		Dispatch.put(selectedDispatch, "Text", newText);
		if (newText != "")
			Dispatch.call(selectedDispatch, "MoveRight");
	}
	
	/** 换一行*/
	public void nextRow() {
		Dispatch.call(selectedDispatch, "TypeParagraph");
		defaultFont();
		defaultParagraphStyle();
	}
	
	/**默认的段落样式*/
	public void defaultParagraphStyle(){
		setParagraphsProperties(0, 0, 0, 0, 0);
	}
	
	/**
	 * 设置段落格式
	 * @param alignment 0-左对齐, 1-居中, 2-右对齐, 3-两端对齐, 4-分散对齐
	 * @param lineSpaceingRule 0 行距
	 * @param lineUnitBefore 0 段前
	 * @param lineUnitAfter 0 段后
	 * @param characterUnitFirstLineIndent 2 首行缩进字符数
	 */

	public void setParagraphsProperties(int alignment, int lineSpaceingRule, int lineUnitBefore, int lineUnitAfter, int characterUnitFirstLineIndent) {
		Dispatch paragraphFormat = Dispatch.get(selectedDispatch, "ParagraphFormat").toDispatch();
		// 对齐方式
		Dispatch.put(paragraphFormat, "Alignment", new Variant(alignment));
		Dispatch.put(paragraphFormat, "FirstLineIndent", new Variant(0));
		// 行距
		Dispatch.put(paragraphFormat, "LineSpacingRule", new Variant(lineSpaceingRule));
		// 段前
		Dispatch.put(paragraphFormat, "LineUnitBefore", new Variant(lineUnitBefore));
		// 段后
		Dispatch.put(paragraphFormat, "LineUnitAfter", new Variant(lineUnitAfter));
		float lineIndent = Dispatch.get(paragraphFormat,"CharacterUnitFirstLineIndent").getFloat();
		if (lineIndent > 0) {
			characterUnitFirstLineIndent = 0;
		}
		// 首行缩进字符数
		Dispatch.put(paragraphFormat, "CharacterUnitFirstLineIndent", new Variant(characterUnitFirstLineIndent));
	}
	
	/**恢复默认字体 不加粗，不倾斜，没下划线，黑色，小四号字，宋体*/
	public void defaultFont() {
		this.setFont(false, false, false, "0,0,0", "10.5", "宋体");
	}

	/**
	 * 设置字体
	 * @param isBold
	 * @param isItalic
	 * @param isUnderLine
	 * @param color
	 * @param size
	 * @param name
	 */
	public void setFont(boolean isBold, boolean isItalic, boolean isUnderLine, String color, String size, String name) {
		Dispatch font = Dispatch.get(selectedDispatch, "Font").toDispatch();
		Dispatch.put(font, "Name", new Variant(name));
		Dispatch.put(font, "Bold", new Variant(isBold));
		Dispatch.put(font, "Italic", new Variant(isItalic));
		Dispatch.put(font, "Underline", new Variant(isUnderLine));
		Dispatch.put(font, "Color", color);
		Dispatch.put(font, "Size", size);
	}
	
	/**光标下移*/
	public void moveDown() {
		Dispatch.call(selectedDispatch, "MoveDown");
	}
	
}
