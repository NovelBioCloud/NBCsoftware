package com.novelbio.base.word;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.novelbio.base.dataOperate.ExcelTxtRead;

/**
 * 代表窗口或窗格中的当前所选内容。所选内容代表文档中选定（或突出显示）的区域， 如果文档中没有选定任何内容，则代表插入点。 每个文档窗格只能有一个
 * Selection 对象， 并且在整个应用程序中只能有一个活动的 Selection 对象
 */
public class Selection extends BaseWord {
	private ActiveXComponent wordApp;
	private Dispatch doc;

	public Selection(Dispatch instance, ActiveXComponent wordApp,Dispatch doc) {
		super(instance);
		this.doc = doc;
		this.wordApp = wordApp;
	}

	/**
	 * 设置选择区的文字
	 * 
	 */
	public void setText(String text) throws Exception {
		Dispatch.put(instance, "Text", text);
	}

	/**
	 * 写入文本
	 * 
	 * @param text
	 * @throws Exception
	 */
	public void writeText(String text) throws Exception {
		Dispatch.call(instance, "TypeText", text);
	}

	/**
	 * 用新文字替换旧文字
	 * 
	 * 说明：
	 * 
	 * @param oldText
	 * @param newText
	 * @throws Exception
	 *             创建时间：2011-6-4 下午06:32:43
	 */
	public void replace(String oldText, String newText) throws Exception {
		while (find(oldText, false)) {
			Dispatch.put(instance, "Text", newText);
			Dispatch.call(instance, "MoveRight");
		}
	}

	/**
	 * 替换已选中的文字
	 * 
	 * @param newText
	 * @throws Exception
	 */
	public void replaceSelected(String newText) throws Exception {
		Dispatch.put(instance, "Text", newText);
		Dispatch.call(instance, "MoveRight");
	}

	/**
	 * 查找字符串
	 * 
	 * 说明：
	 * 
	 * @param toFindText
	 * @return 创建时间：2011-6-4 下午07:15:39
	 */
	public boolean find(String toFindText, boolean isRegex) {
		if (toFindText == null || toFindText.equals(""))
			return false;

		// 从selection所在位置开始查询
		Dispatch find = wordApp.call(instance, "Find").toDispatch();
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

	/**
	 * 插入图片
	 * 
	 * 说明：
	 * 
	 * @param imagePath
	 *            图片路径
	 * @param title
	 *            图片标题
	 * @param picNumInOneRow
	 *            几张图片并列 创建时间：2011-6-4 下午07:17:18
	 */
	public void insertPicture(List<String> lsPicPaths, String title,
			int picNumInOneRow) {
		double rate = 1;
		if (picNumInOneRow == 0) {
			picNumInOneRow = 1;
		} else if (lsPicPaths.size() < picNumInOneRow) {
			rate = Math.floor(0.92 / lsPicPaths.size() * 100) / 100;
		} else {
			rate = Math.floor(0.92 / picNumInOneRow * 100) / 100;
		}
		Dispatch inLineShapes = Dispatch.get(instance, "InLineShapes")
				.toDispatch();
		for (String imagePath : lsPicPaths) {
			Dispatch shape = Dispatch.call(inLineShapes, "AddPicture",
					imagePath).toDispatch();
			Dispatch.put(shape, "Width", Dispatch.get(shape, "Width")
					.getFloat() * rate);
			Dispatch.put(shape, "Height", Dispatch.get(shape, "Height")
					.getFloat() * rate);
		}
		if (lsPicPaths.size() % picNumInOneRow < picNumInOneRow)
			nextRow();
		Dispatch.call(instance, "InsertCaption", "Figure", " : " + title);
		// ,/**Label*/"-1",/**Title*/title
		// ,/**TitleAutoText"123",*//**Position*/"1",/**ExcludeLabel*/"False").toDispatch();
		Dispatch paragraphFormat = Dispatch.get(instance, "ParagraphFormat")
				.toDispatch();
		Dispatch.put(paragraphFormat, "Alignment", "1");
		// Dispatch.put(caption,"TitleAutoText","123");
		// Dispatch.put(caption,"Title",title);
		// Dispatch.put(caption,"Position","1");
		// Dispatch.put(caption,"ExcludeLabel","False");
		// Selection.InsertCaption Label:=图", TitleAutoText:="", Title:="", _
		// Position:=wdCaptionPositionBelow, ExcludeLabel:=0
		// Set tzShape = Selection.ShapeRange(1)
		// oShape.Select False
	}

	/**
	 * 取得选中的文本
	 * 
	 * @return
	 */
	public String getText() {
		return Dispatch.get(instance, "Text").getString();
	}
	
	/**
	 * 换一行
	 */
	public void nextRow() {
		Dispatch.call(instance, "TypeParagraph");
		defaultFont();
		defaultParagraphStyle();
	}

	/**
	 * 设置段落格式
	 * 
	 * @param alignment
	 *            0-左对齐, 1-居中, 2-右对齐, 3-两端对齐, 4-分散对齐
	 * @param lineSpaceingRule 0
	 * @param lineUnitBefore 0
	 * @param lineUnitAfter 0
	 * @param characterUnitFirstLineIndent 2
	 */

	public void setParagraphsProperties(int alignment, int lineSpaceingRule,

	int lineUnitBefore, int lineUnitAfter, int characterUnitFirstLineIndent) {

		Dispatch paragraphFormat = Dispatch.get(instance, "ParagraphFormat")
				.toDispatch();

		Dispatch.put(paragraphFormat, "Alignment", new Variant(alignment)); // 对齐方式
		Dispatch.put(paragraphFormat, "FirstLineIndent", new Variant(0));
		Dispatch.put(paragraphFormat, "LineSpacingRule", new Variant(
				lineSpaceingRule)); // 行距
		Dispatch.put(paragraphFormat, "LineUnitBefore", new Variant(lineUnitBefore)); // 段前

		Dispatch.put(paragraphFormat, "LineUnitAfter", new Variant(lineUnitAfter)); // 段后
		float lineIndent = Dispatch.get(paragraphFormat,"CharacterUnitFirstLineIndent").getFloat();
		if(lineIndent > 0){
			characterUnitFirstLineIndent = 0;
		}
		Dispatch.put(paragraphFormat, "CharacterUnitFirstLineIndent",
					new Variant(characterUnitFirstLineIndent)); // 首行缩进字符数
		

	}
	
	/**
	 * 默认的段落样式
	 */
	public void defaultParagraphStyle(){
		setParagraphsProperties(0,0,0,0,0);
	}
	
	/**
	 * 恢复默认字体 不加粗，不倾斜，没下划线，黑色，小四号字，宋体
	 */
	public void defaultFont() {
		this.setFont(false, false, false, "0,0,0", "10.5", "宋体");
	}

	/**
	 * 设置字体
	 * 
	 * @param isBold
	 * @param isItalic
	 * @param isUnderLine
	 * @param color
	 * @param size
	 * @param name
	 */
	public void setFont(boolean isBold, boolean isItalic, boolean isUnderLine,
			String color, String size, String name) {
		Dispatch font = Dispatch.get(instance, "Font").toDispatch();
		Dispatch.put(font, "Name", new Variant(name));
		Dispatch.put(font, "Bold", new Variant(isBold));
		Dispatch.put(font, "Italic", new Variant(isItalic));
		Dispatch.put(font, "Underline", new Variant(isUnderLine));
		Dispatch.put(font, "Color", color);
		Dispatch.put(font, "Size", size);
	}
	
	/**
	* 创建表格
	*

	* @param cols
	*            列数
	* @param rows
	*            行数
	*/
	public Dispatch createTable(int numCols, int numRows) {
		Dispatch tables = Dispatch.get(doc, "Tables").toDispatch();
		Dispatch range = Dispatch.get(instance, "Range").toDispatch();
		Dispatch newTable = Dispatch.call(tables, "Add", range,
		new Variant(numRows), new Variant(numCols)).toDispatch();
		return newTable;
	}
	

	/**
	 * 在指定的单元格里填写数据
	 *
	 * @param tableIndex
	 * @param cellRowIdx
	 * @param cellColIdx
	 * @param txt
	 */
	public void putTxtToCell(Dispatch table, int cellRowIdx, int cellColIdx,
			String txt) {
		Dispatch cell = Dispatch.call(table, "Cell", new Variant(cellRowIdx),
				new Variant(cellColIdx)).toDispatch();
		Dispatch.call(cell, "Select");
		Dispatch.put(instance, "Text", txt);
	}
	
	/**
	 * 从excel中读取数据并生成表格添加到word中
	 * @param mapExcelPath2SheetName excel和sheetName的集合
	 * @param title 标题
	 * @param maxRow 最大显示行数
	 */
	public void insertTablesFromExcel(Map<String, String> mapExcelPath2SheetName,
			String title, int maxRow) {
		Dispatch.call(instance, "InsertCaption", "Table", " : " + title);
		// ,/**Label*/"-1",/**Title*/title
		// ,/**TitleAutoText"123",*//**Position*/"1",/**ExcludeLabel*/"False").toDispatch();
		Dispatch paragraphFormat = Dispatch.get(instance, "ParagraphFormat")
				.toDispatch();
		Dispatch.put(paragraphFormat, "Alignment", "1");
		nextRow();
		setParagraphsProperties(0, 0, 0, 0, 0);
		for(String excelPath : mapExcelPath2SheetName.keySet()){
			String sheetName = mapExcelPath2SheetName.get(excelPath);
			List<List<String>>  data = ExcelTxtRead.readLsExcelTxtls(excelPath, sheetName, 1,maxRow);
			if(data.size() == 0)
				continue;
			Dispatch table = createTable(data.get(0).size(),data.size());
			setTableStyleAsDefault(table);
			fillTable(table,data);
		}
	}
	/**
	 * 把数据填到表格中
	 * @param table
	 * @param data
	 */
	private void fillTable(Dispatch table,List<List<String>> data){
		List<List<String>> lsNewDatas = formatDataList(data);
		for (int i = 0; i < lsNewDatas.size(); i++) {
			for (int j = 0; j < lsNewDatas.get(i).size(); j++) {
				putTxtToCell(table,i+1,j+1,lsNewDatas.get(i).get(j));
			}
		}
		moveDown();
	}
	
	/**
	 * 设置默认的表格样式
	 * @param table
	 */
	private void setTableStyleAsDefault(Dispatch table){
		Dispatch.put(table, "Style", "中等深浅底纹 1 - 强调文字颜色 5");
		Dispatch.put(table, "Spacing", "0");
		Dispatch.put(table, "PreferredWidthType", "3");
		Dispatch.put(table, "PreferredWidth", new Variant(450));
		Dispatch rows = Dispatch.get(table,"Rows").toDispatch();
		Dispatch.put(rows, "HeightRule", 2);
		Dispatch.put(rows, "Height", new Variant(15));
		Dispatch.call(table, "Select");
		Dispatch font = Dispatch.get(instance, "Font").toDispatch();
		Dispatch.put(font, "Size", 9);
//		 Selection.Tables(1).PreferredWidthType = wdPreferredWidthPoints
//				    Selection.Tables(1).PreferredWidth = CentimetersToPoints(15)
//				    Selection.Rows.HeightRule = wdRowHeightExactly
//				    Selection.Rows.Height = CentimetersToPoints(0.55)
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
	 * 游标往下一行
	 */
	public void moveDown(){
		Dispatch.call(instance, "MoveDown"); // 
	}
}