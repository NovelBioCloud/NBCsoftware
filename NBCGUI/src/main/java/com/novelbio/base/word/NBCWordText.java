package com.novelbio.base.word;

import java.util.Collection;
import java.util.Iterator;

public class NBCWordText {
	// 默认文本
	private String defaultText = "";
	// 加在前面的文本
	private String preText = "";
	// 加在后面的文本
	private String appendText = "";
	// 是否需要换行
	private boolean withEnter = false;
	// 每个文本间都加上换行
	private boolean perEnter = false;
	// 选 中的文本
	private Selection selection;
	// 表达式
	private String pattern;

	// ${aa##p|asf##a|aaaf##n|##d|bas}
	public NBCWordText(Selection selection) {
		this.selection = selection;
		this.pattern = selection.getText();
		if (!(pattern.startsWith("${") && pattern.endsWith("}"))) {
			throw new RuntimeException("word表达式:" + pattern + "异常");
		}
		String patternLeft = pattern.substring(2, pattern.length() - 1);
		this.withEnter = patternLeft.contains("##n|");
		String[] methods = patternLeft.split("##");
		for (int i = 0; i < methods.length; i++) {
			if (i == 0)
				continue;
			if (methods[i].startsWith("p|"))
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

	/**
	 * 替换表达式
	 * 
	 * @param obj
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void replaceAs(Object obj) throws Exception {
		if (obj == null)
			return;
		if (obj instanceof Collection) {
			Collection values = (Collection) obj;
			if (values.size() == 0) {
				useDefaultText();
			}
			Iterator iterator = values.iterator();
			Object object = (Object) iterator.next();
			selection.replaceSelected(preText + object + appendText);
			while (iterator.hasNext()) {
				if (perEnter)
					selection.nextRow();
				Object object1 = (Object) iterator.next();
				if (withEnter)
					selection.nextRow();
				selection.writeText(preText + object1 + appendText);
			}
		} else {
			selection.replace(pattern, preText + obj + appendText);
			if (withEnter)
				selection.nextRow();
		}
	}

	/**
	 * 使用默认文本
	 * 
	 * @throws Exception
	 */
	public void useDefaultText() throws Exception {
		selection.replaceSelected(defaultText);
		if (withEnter)
			selection.nextRow();
	}
}
