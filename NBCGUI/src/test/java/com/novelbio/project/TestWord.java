package com.novelbio.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.word.NBCWord;
import com.novelbio.base.word.NBCWordImage;
import com.novelbio.base.word.NBCWordTable;
import com.novelbio.nbcReport.Params.EnumReport;
import com.novelbio.nbcReport.Params.ReportBase;
import com.novelbio.nbcReport.Params.ReportDifGene;

public class TestWord {
	public static void main(String[] args) {
		NBCWord word  = new NBCWord("C:\\Documents and Settings\\Administrator\\桌面\\novelbio_yangke_20140103 Result Report.doc");
		Map<String,Object> map = new HashMap<>();
		List<String> lsTexts = new ArrayList<>();
		lsTexts.add("苹果");
		lsTexts.add("香蕉");
		lsTexts.add("桔子");
		lsTexts.add("牛奶");
		map.put("text", lsTexts);
		map.put("diffGene_result", map); 
		List<NBCWordTable> lsTables = new ArrayList<>();
		NBCWordTable table = new NBCWordTable();
		table.setTitle("这只是一个表格而已");
		table.setDownCompare("这是写在表格下方的说明，写多了也不好，就先写这么多吧");
		table.setNote("注:这是表格的注释说明");
		table.setUpCompare("这是写在表格上方的说明，写多了也不好，就先写这么多吧");
		table.add("C:\\Documents and Settings\\Administrator\\桌面\\test.xlsx", "sheet1");
		lsTables.add(table);
		List<NBCWordImage> pic = new ArrayList<>();
		NBCWordImage image = new NBCWordImage();
		image.setDownCompare("这是写在图片下方的说明，睁大你的眼睛看看");
		image.setUpCompare("这是写在图片上方的说明，睁大你的眼睛看看");
		image.setTitle("这是一张神奇的图片");
		image.setNote("注：这个图片只是用来测试有没有用的");
		image.addPicPath("C:\\Documents and Settings\\Administrator\\桌面\\QQ图片20140103164408.jpg");
		pic.add(image);
		NBCWordImage image3 = new NBCWordImage();
		image3.setDownCompare("这是写在图片下方的说明，睁大你的眼睛看看");
		image3.setUpCompare("这是写在图片上方的说明，睁大你的眼睛看看");
		image3.setTitle("这是一张神奇的图片");
		image3.setNote("注：这个图片只是用来测试有没有用的");
		image3.addPicPath("C:\\Documents and Settings\\Administrator\\桌面\\WebAppUML_1.png");
		pic.add(image3);
		map.put("pic", pic);
		List<NBCWordImage> pic1 = new ArrayList<>();
		NBCWordImage image2 = new NBCWordImage();
		image2.setDownCompare("这是写在图片下方的说明，睁大你的眼睛看看");
		image2.setUpCompare("这是写在图片上方的说明，睁大你的眼睛看看");
		image2.setTitle("这是一张神奇的图片");
		image2.setNote("注：这个图片只是用来测试有没有用的");
		image2.addPicPath("C:\\Documents and Settings\\Administrator\\桌面\\QQ截图20140103165141.png");
		image2.addPicPath("C:\\Documents and Settings\\Administrator\\桌面\\QQ截图20140103165141.png");
		pic1.add(image2);
		map.put("pic1", pic1);
		map.put("table", lsTables);
		List<ReportBase> lsReportBases = new ArrayList<>();
		ReportDifGene report = (ReportDifGene)FileOperate.readFileAsObject("Z:\\nbCloud\\public\\AllProject\\project_52a6c993e4b0aacc5c263007\\task_52aaae1de4b04b8c8e8eaab2\\DifferenceExpression_result\\.report\\report_DiffExp");
		lsReportBases.add(report);
		EnumReport.ReportAll.get
		word.renderReport(map);
//		word.saveDocAs("C:\\Documents and Settings\\Administrator\\桌面\\gooddd.docx");
//		word.close();
	}
}
