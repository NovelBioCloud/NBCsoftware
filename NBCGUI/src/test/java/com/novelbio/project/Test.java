package com.novelbio.project;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.fs.FSDataInputStream;

import com.novelbio.base.PathDetail;
import com.novelbio.base.dataOperate.DateUtil;
import com.novelbio.base.fileOperate.FileHadoop;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.word.NBCWord;
import com.novelbio.nbcReport.Params.EnumReport;
import com.novelbio.nbcReport.Params.ReportAll;
import com.novelbio.nbcReport.Params.ReportBase;

public class Test {
	
	public static void main(String[] args) {

		try {
			System.setProperty("hadoop.home.dir", "C:/hadoop/hadoop-2.5.0_compile_win/hadoop-2.5.0");
			String path = FileHadoop.addHdfsHeadSymbol("/nbCloud/public/test/gopath/maizeBG.txt");
			InputStream is = FileOperate.getInputStream(path);
			File file = FileOperate.getFile(path);
//			InputStream is = FileOperate.getInputStream(file);
			System.out.println(file.getPath());
//			String tempPath = FileOperate.addSep(PathDetail.getTmpPath()) + DateUtil.getDateAndRandom();
//			OutputStream os = FileOperate.getOutputStream(tempPath, true);
//			OutputStreamWriter fw = new OutputStreamWriter(os);
//			System.out.println(tempPath);
			int b;
			while((b = is.read()) != -1){
				System.out.println(b);
//				os.write(in);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		List<String> lsFolders = new ArrayList<>();
//		lsFolders.add("");
//		ReportAll reportAll = EnumReport.ReportAll.getReportBase();
////		reportAll.addChildReport(reportAll.readReportFromFile("Z:\\hadoop\\GOAnalysis_result\\.report\\report_GOAnalysis"));
////		reportAll.setProjectName("projectName");
////		NBCWord word = reportAll.getWord();
//		NBCWord word  = new NBCWord("C:\\Documents and Settings\\Administrator\\桌面\\GOAnalysis_result.doc");
//		ReportBase reportBase = reportAll.readReportFromFile("Z:\\hadoop\\GOAnalysis_result\\.report\\report_GOAnalysis");
//		word.renderReport(reportBase);
////		String savePath = "";
////		String fileName =  "zzzz.doc";
////		String reportFilePath = FileOperate.addSep(savePath)+ "report_result" + FileOperate.getSepPath() + fileName;
////		File file = FileOperate.getFile(reportFilePath);
////		FileOperate.createFolders(file.getParent());
//		String realPath = "C:\\Documents and Settings\\Administrator\\桌面\\gooddd.doc";
//		try {
//			word.saveDocAs(realPath); 
//			word.close();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}

}
