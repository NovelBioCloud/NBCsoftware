package com.novelbio.nbcReport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.report.generateReport.ProjectReport;

import junit.framework.TestCase;

public class TestProjectReport extends TestCase {

	public void testProjectReport() throws IOException {
		
		List<String> lsTaskId = new ArrayList<String>();
		lsTaskId.add("54acd83c8314bd93c667cd3a");
		lsTaskId.add("54b39165831454cb18c6a891");
//		lsTaskId.add("54ad1c3f83145d5a484ae12f");
//		lsTaskId.add("54bf3f59831478065502032d");
		ProjectReport projectReport = new ProjectReport("53a3ef98559aa1839ed9c4d2", lsTaskId);
//		ProjectReport projectReport = new ProjectReport("537d9085e4b0970e16175c57", lsTaskId);
		projectReport.saveReport("/home/novelbio/jpx/test/reportTest");
		assertEquals(true, FileOperate.isFileFoldExist("/home/novelbio/jpx/test/reportTest/image_54acd83c8314bd93c667cd3a"));
		assertEquals(true, FileOperate.isFileFoldExist("/home/novelbio/jpx/test/reportTest/image_54b39165831454cb18c6a891"));
//		assertEquals(true, FileOperate.isFileFoldExist("/home/novelbio/jpx/test/reportTest/image_54ad1c3f83145d5a484ae12f"));
//		assertEquals(true, FileOperate.isFileFoldExist("/home/novelbio/jpx/test/reportTest/image_54bf3f59831478065502032d"));
		assertEquals(true, FileOperate.isFileExist("/home/novelbio/jpx/test/reportTest/projectReport.tex"));
	}
	
	/** 测试ProjectReport中的一个目录下所有文件的路径的方法 */
	public void testGetAllFilePath() {
		ProjectReport projectReport = new ProjectReport();
		String folderPath = "/media/nbfs/nbCloud/testCode/testGetAllFilePath/reportTest";
		List<String> lsFilePath = projectReport.getAllFilePath(folderPath);
		Set<String> setFilePath = new HashSet<String>(lsFilePath);
		List<String> lsActualPath = new ArrayList<String>();
		lsActualPath.add("/media/nbfs/nbCloud/testCode/testGetAllFilePath/reportTest/projectReport.tex");
		lsActualPath.add("/media/nbfs/nbCloud/testCode/testGetAllFilePath/reportTest/image_54acd83c8314bd93c667cd3a/Path-Analysis-Enrichment_All_AvsB-Dif.png");
		lsActualPath.add("/media/nbfs/nbCloud/testCode/testGetAllFilePath/reportTest/image_54b39165831454cb18c6a891/GO-Analysis-Log2P_All_BvsA-Dif.png");
		lsActualPath.add("/media/nbfs/nbCloud/testCode/testGetAllFilePath/reportTest/image_54b39165831454cb18c6a891/9522LTS9vs9522HTS9_statistics.txt");
		assertEquals(4, setFilePath.size());
		assertEquals(true, setFilePath.containsAll(lsActualPath));
	}
	
}
