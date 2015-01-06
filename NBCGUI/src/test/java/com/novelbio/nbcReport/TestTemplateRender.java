package com.novelbio.nbcReport;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.testReport.ReportImage;
import com.novelbio.testReport.TemplateRender;

import junit.framework.TestCase;

public class TestTemplateRender extends TestCase {
	
	public void testRender() {
		TemplateRender templateRender = new TemplateRender();
		Map<String, Object> mapStr2Str = new HashMap<String, Object>();
		
		double d = 0.0000000001;
		
		List<List<String>> lsLsList = new ArrayList<List<String>>();
		List<String> lsStr = new ArrayList<String>();
		lsStr.add("1");
		lsStr.add("2");
		lsStr.add("3");
		lsStr.add("4");
		lsLsList.add(lsStr);
		lsLsList.add(lsStr);
		
		ReportImage reportImage = new ReportImage();
		reportImage.setImgHeight(3);
		reportImage.setImgWidth(5);
		reportImage.addImgPath("imgName.jpg");
		reportImage.addImgPath("test.jpg");
		reportImage.setImgTitle("测试用");
		
		mapStr2Str.put("image", reportImage.getMapKey2Param());
		mapStr2Str.put("name", "张三");
		mapStr2Str.put("sex", "男");
		mapStr2Str.put("lsStr", lsLsList);
		mapStr2Str.put("d", d);
		Writer out = new BufferedWriter(new OutputStreamWriter(FileOperate.getOutputStream("/home/novelbio/jpx/test.txt", true)));
		templateRender.render("/templateLatex/test.ftl", mapStr2Str, out);
		assertEquals(true, FileOperate.isFileExist("/home/novelbio/jpx/test.txt"));
	}

}
