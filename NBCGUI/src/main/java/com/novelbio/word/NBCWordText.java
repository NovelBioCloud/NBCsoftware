package com.novelbio.word;

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
	/** 选 中的文本*/
	private Selection selection;
	/** 表达式*/
	private String pattern;
	
	/**
	 * 插入文档
	 * @param selection
	 * @param object
	 */
	public void insertToDoc(Selection selection, Object object) {
		// TODO
	}
	
	/**
	 * 使用默认插入文档
	 * @param selection
	 */
	public void useDefaultText(Selection selection) {
		// TODO
	}
	
}
