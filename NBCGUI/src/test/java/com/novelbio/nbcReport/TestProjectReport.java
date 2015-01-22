package com.novelbio.nbcReport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.report.generateReport.ProjectReport;

import junit.framework.TestCase;

public class TestProjectReport extends TestCase {

	public void testProjectReport() throws IOException {
		
		List<String> lsTaskId = new ArrayList<String>();
		lsTaskId.add("54bf3f59831478065502032d");
		ProjectReport projectReport = new ProjectReport("537d9085e4b0970e16175c57", lsTaskId);
//		projectReport.saveReport("/home/novelbio/jpx/test/reportTest");
//		assertEquals(true, FileOperate.isFileFoldExist("/home/novelbio/jpx/test/reportTest/image_54bf3f59831478065502032d"));
//		assertEquals(true, FileOperate.isFileExist("/home/novelbio/jpx/test/reportTest/projectReport.tex"));
		
		projectReport.copyImages("/home/novelbio/jpx/test/reportTest");
		
		
//		lsTaskId.add("54acd83c8314bd93c667cd3a");
//		lsTaskId.add("54ad1c3f83145d5a484ae12f");
//		lsTaskId.add("54b39165831454cb18c6a891");
//		Map<String, Object> mapKey2Param = reportAll.getMapKey2Param();
//		assertEquals("Ion Proton", mapKey2Param.get("sequence"));
//		assertEquals("rat", mapKey2Param.get("speciesName"));
//		List<String> lsTaskResultPath = projectReport.getLsTaskResultPath();
//		projectReport.copyImages("/home/novelbio/jpx/test/reportTest");
//		assertEquals(true, FileOperate.isFileFoldExist("/home/novelbio/jpx/test/reportTest/image_54acd83c8314bd93c667cd3a"));
//		assertEquals(true, FileOperate.isFileFoldExist("/home/novelbio/jpx/test/reportTest/image_54ad1c3f83145d5a484ae12f"));
//		assertEquals(true, FileOperate.isFileFoldExist("/home/novelbio/jpx/test/reportTest/image_54b39165831454cb18c6a891"));

	}
	
}
