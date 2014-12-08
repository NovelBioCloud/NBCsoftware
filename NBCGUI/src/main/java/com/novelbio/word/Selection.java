package com.novelbio.word;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;

public class Selection {
	
	/**word应用程序*/
	private ActiveXComponent wordApp;
	/**所在文档*/
	private Document document;
	/**dispatch实例*/
	private Dispatch instance;

	public Selection() {
		
	}
	
	public Selection(ActiveXComponent wordApp, Document document, Dispatch instance) {
		this.wordApp = wordApp;
		this.document = document;
		this.instance = instance;
	}
	
	public Dispatch getInstance() {
		return instance;
	}
	
	/**替换文本*/
	public void replace(String oldText, String newText) throws Exception {
		while (find(oldText, false)) {
			Dispatch.put(instance, "Text", newText);
			if(newText != "")
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
	 * 光标下移
	 */
	public void moveDown() {
		Dispatch.call(instance, "MoveDown");
	}
	
}
