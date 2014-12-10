package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.novelbio.nbcReport.Params.EnumReport;
import com.novelbio.nbcReport.Params.ReportAll;
import com.novelbio.nbcReport.Params.ReportBase;
import com.novelbio.nbcReport.Params.ReportGO;
import com.novelbio.word.NBCWordImage;
import com.novelbio.word.NBCWord;
import com.novelbio.word.NBCWordTable;

public class TestWord {
	
	public static void main(String[] args) {
		System.setProperty("hadoop.home.dir", "C:/hadoop/hadoop-2.5.0_compile_win/hadoop-2.5.0");
		
		List<String> lsFolders = new ArrayList<>();
		lsFolders.add("");
		ReportAll reportAll = EnumReport.ReportAll.getReportBase();
//		reportAll.addChildReport(reportAll.readReportFromFile("Z:\\hadoop\\GOAnalysis_result\\.report\\report_GOAnalysis"));
//		reportAll.setProjectName("projectName");
//		NBCWord word = reportAll.getWord();
		NBCWord word  = new NBCWord("C:\\Documents and Settings\\Administrator\\桌面\\GOAnalysis_result.doc");
		ReportBase reportBase = reportAll.readReportFromFile("Z:\\hadoop\\GOAnalysis_result\\.report\\report_GOAnalysis");
		word.renderReport(reportBase);
//		String savePath = "";
//		String fileName =  "zzzz.doc";
//		String reportFilePath = FileOperate.addSep(savePath)+ "report_result" + FileOperate.getSepPath() + fileName;
//		File file = FileOperate.getFile(reportFilePath);
//		FileOperate.createFolders(file.getParent());
		String realPath = "C:\\Documents and Settings\\Administrator\\桌面\\gooddd.doc";
		try {
			word.saveDocAs(realPath); 
			word.quit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		
//		NBCWord nbcWord = new NBCWord(null);
//		System.setProperty("hadoop.home.dir", "C:/hadoop/hadoop-2.5.0_compile_win/hadoop-2.5.0");
//		NBCWord nbcWord = new NBCWord("C:\\Documents and Settings\\Administrator\\桌面\\Read Me123.docx");
//		Map<String,Object> map = new HashMap<>();
//		List<String> lsTexts = new ArrayList<>();
//		lsTexts.add("苹果");
//		lsTexts.add("香蕉");
//		lsTexts.add("桔子");
//		lsTexts.add("牛奶");
//		map.put("text", lsTexts);
//		map.put("diffGene_result", map); 
//		List<NBCWordImage> pic = new ArrayList<>();
//		NBCWordImage image = new NBCWordImage();
//		image.setDownCompare("这是写在图片下方的说明，睁大你的眼睛看看");
//		image.setUpCompare("这是写在图片上方的说明，睁大你的眼睛看看");
//		image.setTitle("这是一张神奇的图片");
//		image.setNote("注：这个图片只是用来测试有没有用的");
//		image.setAlign(1);
//		image.addPicPath("C:\\Documents and Settings\\Administrator\\桌面\\viewphoto.jpg");
//		pic.add(image);
//		map.put("pic", pic);
//		List<NBCWordTable> lsTables = new ArrayList<>();
//		NBCWordTable nbcWordTable = new NBCWordTable();
//		nbcWordTable.setDownCompare("这是写在表格下方的说明，睁大你的眼睛看看");
//		nbcWordTable.setUpCompare("这是写在表格上方的说明，睁大你的眼睛看看");
//		nbcWordTable.setTitle("这是一张神奇的表格");
//		nbcWordTable.setNote("注：这个表格只是用来测试有没有用的");
//		nbcWordTable.addMapExcelPath2SheetName("C:\\Documents and Settings\\Administrator\\桌面\\A6VSA0-Dif_anno.xls", "A6VSA0-Dif_anno");
//		lsTables.add(nbcWordTable);
//		map.put("table", lsTables);
//		List<ReportBase> lsReportBases = new ArrayList<>();
//		ReportBase reportBase = new ReportGO();
//		lsReportBases.add(reportBase);
//		map.put("reportBase", lsReportBases);
//		nbcWord.getNowDoc().render(map);
//		nbcWord.saveDocAs("C:\\Documents and Settings\\Administrator\\桌面\\gooddd.doc");
//		nbcWord.quit();
	}

}
