package com.novelbio.nbcReport;

import java.util.HashMap;
import java.util.Map;

import com.novelbio.base.fileOperate.FileOperate;

import junit.framework.TestCase;

public class TestTemplateRender extends TestCase {
	
	public void testRender() {
		TemplateRender templateRender = new TemplateRender();
		Map<String, Object> mapStr2Str = new HashMap<String, Object>();
		mapStr2Str.put("name", "张三");
		mapStr2Str.put("sex", "男");
		templateRender.render("/com/novelbio/nbcReport/template", "test.ftl", "/home/novelbio/jpx/test.tex", mapStr2Str);
		assertEquals(true, FileOperate.isFileExist("/home/novelbio/jpx/test.tex"));
	}

}
