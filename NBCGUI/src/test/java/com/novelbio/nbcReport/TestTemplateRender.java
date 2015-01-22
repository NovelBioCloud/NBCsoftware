package com.novelbio.nbcReport;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.report.ReportImage;
import com.novelbio.report.TemplateRender;

import junit.framework.TestCase;

public class TestTemplateRender extends TestCase {
	
	public void testRender() {
		DecimalFormat decimalFormat = new DecimalFormat("#.###"); 
		TemplateRender templateRender = new TemplateRender();
		Map<String, Object> mapStr2Str = new HashMap<String, Object>();
		
		Map<String, Object> table = new HashMap<String, Object>();
		table.put("tableTitle", "测试表格");
		table.put("columnNum", 5);
		List<String[]> lsLsData = new ArrayList<String[]>();
		String[] lsData = new String[5];
		lsData[0] = "1";
		lsData[1] = "2";
		lsData[2] = "3";
		lsData[3] = "4";
		lsData[4] = "5";
//		lsData[5] = "6";
//		lsData[6] = "7";
		lsLsData.add(lsData);
		lsLsData.add(lsData);
		lsLsData.add(lsData);
		lsLsData.add(lsData);
		List<String> lsFirstColumn = new ArrayList<String>();
		lsFirstColumn.add("a");
		lsFirstColumn.add("a");
		lsFirstColumn.add("a");
		lsFirstColumn.add("a");
		table.put("lsFirstColumn", lsFirstColumn);
		table.put("lsLsData", lsLsData);
		double dou = 1.000;
		mapStr2Str.put("double", dou);
		mapStr2Str.put("table", table);
		
//		double d = 0.12345678;
//		
//		List<List<String>> lsLsList = new ArrayList<List<String>>();
//		List<String> lsStr = new ArrayList<String>();
//		lsStr.add("1");
//		lsStr.add("2");
//		lsStr.add("3");
//		lsStr.add("4");
//		lsLsList.add(lsStr);
//		lsLsList.add(lsStr);
//		
//		ReportImage reportImage = new ReportImage();
//		reportImage.setImgHeight(3);
//		reportImage.setImgWidth(5);
//		reportImage.addImgPath("imgName.jpg");
//		reportImage.addImgPath("test.jpg");
//		reportImage.setImgTitle("测试用");
//		
//		mapStr2Str.put("image", reportImage.getMapKey2Param());
//		mapStr2Str.put("name", "张三");
//		mapStr2Str.put("sex", "男");
//		mapStr2Str.put("lsStr", lsLsList);
//		mapStr2Str.put("d", decimalFormat.format(d));
//		
//		mapStr2Str.put("test", 3);
//		
//		List<String[]> lsLsData = new ArrayList<String[]>();
//		String[] lsData = new String[4];
//		lsData[0] = "0";
//		lsData[1] = "1";
//		lsData[2] = "2";
//		lsData[3] = "3";
//		lsLsData.add(lsData);
//		lsLsData.add(lsData);
//		mapStr2Str.put("lsLsData", lsLsData);
//		
//		List<String> lsFirstColumn = new ArrayList<String>();
//		lsFirstColumn.add("一");
//		lsFirstColumn.add("二");
		
		Writer out = new BufferedWriter(new OutputStreamWriter(FileOperate.getOutputStream("/home/novelbio/jpx/test.txt", true)));
		templateRender.render("/templateLatex/test.ftl", mapStr2Str, out);
		assertEquals(true, FileOperate.isFileExist("/home/novelbio/jpx/test.txt"));
	}

}
