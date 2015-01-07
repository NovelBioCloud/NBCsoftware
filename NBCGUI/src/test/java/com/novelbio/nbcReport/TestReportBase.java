package com.novelbio.nbcReport;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.report.TemplateRender;
import com.novelbio.report.Params.ReportBase;
import com.novelbio.report.generateReport.GenerateReport;

import junit.framework.TestCase;

public class TestReportBase extends TestCase {
	
	public void testReadReportFromFile() throws IOException {
		
		String reportPath = "/media/nbfs/nbCloud/public/dev/AllProject/project_53a3ef98559aa1839ed9c4d2/task_54aa497283144ad8830987fb/GOAnalysis_result";
		GenerateReport generateReport = new GenerateReport();
		generateReport.generateReport(reportPath);
		assertEquals(true, FileOperate.isFileExist("/media/nbfs/nbCloud/public/dev/AllProject/project_53a3ef98559aa1839ed9c4d2/task_54aa497283144ad8830987fb/GOAnalysis_result/report_GOAnalysis_AvsB-Dif.tex"));
		
	}

}
