package com.novelbio.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.novelbio.base.fileOperate.FileHadoop;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.nbcReport.EnumTableType;
import com.novelbio.nbcReport.XdocTmpltExcel;
import com.novelbio.nbcReport.XdocTmpltPic;
import com.novelbio.nbcReport.Params.ReportProject;
import com.novelbio.nbcReport.Params.ReportSamAndRPKM;
import com.novelbio.nbcReport.Params.ReportSamAndRPKMAll;


public class TestSamAndRPKM {
	ReportSamAndRPKMAll reportSamAndRPKMAll;
	
	@Before
	public void init() {
		
		
	}
	
	
	@Test
	public void runTest() {
		String savePath = FileHadoop.getHdfsHeadSymbol("/nbCloud/staff/gaozhu/我的文档/SamAndRPKM_result");
		String savePath2 = FileHadoop.getHdfsHeadSymbol("/nbCloud/staff/gaozhu/我的文档/GeneExpression_result");
		List<String> lsList = new ArrayList<>();
		lsList.add(savePath);
		lsList.add(savePath2);
		ReportProject reportProject = new ReportProject(lsList);
		reportProject.outputReport("/home/novelbio/桌面/bbc.docx");
	}

}
