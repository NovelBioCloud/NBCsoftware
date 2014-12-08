package com.novelbio.word;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class Selection {
	
	/**word应用程序*/
	private ActiveXComponent wordApp;
	/**所在文档*/
	private Document document;
	/**dispatch实例*/
	private Dispatch instance;
	
	public Selection(ActiveXComponent wordApp, Document document, Dispatch instance) {
		this.wordApp = wordApp;
		this.document = document;
		this.instance = instance;
	}
	
	public Dispatch getInstance() {
		return instance;
	}
	
	/**替换文本*/
	public void replace(String oldText, String newText) {
		while (find(oldText, false)) {
			Dispatch.put(instance, "Text", newText);
			if (newText != "")
				Dispatch.call(instance, "MoveRight");
		}
	}
	
	/**查找文本*/
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
	
	/**获取当前选中文本*/
	public String getText() {
		return Dispatch.get(instance, "Text").getString();
	}
	
	/**
	 * 替换已选中的文字
	 * 
	 * @param newText
	 * @throws Exception
	 */
	public void replaceSelected(String newText) {
		Dispatch.put(instance, "Text", newText);
		if (newText != "")
			Dispatch.call(instance, "MoveRight");
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
		Dispatch paragraphFormat = Dispatch.get(instance, "ParagraphFormat").toDispatch();
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
		this.setFont(false, false, false, "0, 0, 0", "10.5", "宋体");
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
		Dispatch font = Dispatch.get(instance, "Font").toDispatch();
		Dispatch.put(font, "Name", new Variant(name));
		Dispatch.put(font, "Bold", new Variant(isBold));
		Dispatch.put(font, "Italic", new Variant(isItalic));
		Dispatch.put(font, "Underline", new Variant(isUnderLine));
		Dispatch.put(font, "Color", color);
		Dispatch.put(font, "Size", size);
	}
	
	/**光标下移*/
	public void moveDown() {
		Dispatch.call(instance, "MoveDown");
	}
	
}
