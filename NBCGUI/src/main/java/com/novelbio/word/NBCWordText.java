package com.novelbio.word;

import java.util.Collection;
import java.util.Iterator;

import com.novelbio.word.Selection;

public class NBCWordText {
	
	/** 默认文本*/
	private String defaultText = "";
	/** 加在前面的文本*/
	private String preText = "";
	/** 加在后面的文本*/
	private String appendText = "";
	/** 是否需要换行*/
	private boolean withEnter = false;
	/** 存在时的文本内容*/
	private String existText;
	/** 每个文本间都加上换行*/
	private boolean perEnter = false;
	
	/**
	 * 插入文档
	 * @param selection
	 * @param object
	 */
	public void insertToDoc(Selection selection, Object object) {
		String pattern = selection.getText();
		paresePattern(pattern);
		
		if (existText != null){
			selection.replaceSelected(existText);
			return;
		}
		
		if (object == null) {
			selection.replaceSelected(existText);
			return;
		}
		
		if (object instanceof Collection) {
			Collection<?> values = (Collection<?>) object;
			if (values.size() == 0) {
				selection.replaceSelected(defaultText);
			}
			Iterator<?> iterator = values.iterator();
			Object obj = (Object) iterator.next();
			selection.replaceSelected(preText + obj.toString() + appendText);
			while (iterator.hasNext()) {
				if (perEnter)
					selection.nextRow();
				Object obj1 = (Object) iterator.next();
				if (withEnter)
					selection.nextRow();
				selection.writeText(preText + obj1 + appendText);
			}
		} else {
			selection.replace(pattern, preText + object + appendText);
			if (withEnter)
				selection.nextRow();
		}
		
		System.out.println("插入文本"+pattern);
	}
	
	/**
	 * 使用默认插入文档
	 * @param selection
	 */
	public void useDefaultText(Selection selection) {
		String pattern = selection.getText();
		paresePattern(pattern);
		selection.replaceSelected(existText);
		System.out.println("插入默认文本"+pattern);
	}
	
	/**
	 * 解析选中的文本
	 * @param pattern
	 */
	private void paresePattern(String pattern) {
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
			else if (methods[i].startsWith("p|"))
				this.preText = methods[i].split("p\\|")[1];
			else if (methods[i].startsWith("a|"))
				this.appendText = methods[i].split("a\\|")[1];
			else if (methods[i].startsWith("d|"))
				this.defaultText = methods[i].split("d\\|")[1];
			else if (methods[i].startsWith("n|"))
				this.withEnter = true;
			else if (methods[i].startsWith("pn|"))
				this.perEnter = true;
		}
	}
	
}
