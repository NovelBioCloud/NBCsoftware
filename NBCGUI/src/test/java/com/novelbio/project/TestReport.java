package com.novelbio.project;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.novelbio.base.fileOperate.FileHadoop;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.nbcReport.Params.EnumReport;
import com.novelbio.nbcReport.Params.ReportProject;

public class TestReport {
	ReportProject reportProject;
	
	
	@Before
	public void init() throws Exception{
//		List<String> lsFolders = new ArrayList<>();
//		lsFolders.add(FileHadoop.getHdfsHeadSymbol("/nbCloud/staff/gaozhu/我的文档/"+EnumReport.GOAnalysis.getResultFolder()));
		reportProject = new ReportProject(FileHadoop.getHdfsHeadSymbol("/nbCloud/staff/gaozhu/我的文档"));
	}
	
	@Test
	public void runReport() throws Exception {
		String reportTest = "/home/novelbio/桌面/testReport.docx";
		reportProject.outputReport(reportTest);
		Assert.assertNotNull(FileOperate.isFileExist(reportTest));
	}
	
	
	@After
	public void destroy(){
		
	}
}
