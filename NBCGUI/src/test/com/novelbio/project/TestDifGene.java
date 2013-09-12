package com.novelbio.project;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

import com.novelbio.base.fileOperate.FileHadoop;
import com.novelbio.nbcReport.Params.ReportDifGeneAll;
import com.novelbio.nbcReport.Params.ReportProject;

public class TestDifGene {
	
	ReportDifGeneAll reportDifGeneAll;
	
	@Before
	public void init() {
		
		
		
		

	}
	
	@Test
	public void runTest() {
		
		String savePath = "/media/hdfs/nbCloud/staff/gaozhu/我的文档/Difference-Expression_result";
		List<String> lsList = new ArrayList<>();
		lsList.add(savePath);
		ReportProject reportProject = new ReportProject(lsList);
		reportProject.outputReport("/home/novelbio/桌面/Dif.docx");

		
	}

}
