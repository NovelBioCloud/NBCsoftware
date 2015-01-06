package com.novelbio.nbcReport;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;

import com.novelbio.base.StringOperate;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.testReport.TemplateRender;
import com.novelbio.testReport.Params.ReportBase;

import junit.framework.TestCase;

public class TestReportBase extends TestCase {
	
	public static void main(String[] args) {
		String str = "/media/nbfs/nbCloud/public/dev/AllProject/project_53a3ef98559aa1839ed9c4d2/task_54abb1f683145149794b196d/GOAnalysis_result/.report/report_GOAnalysis_AvsB-Dif";
		System.out.println(str.substring(str.substring(0,  str.lastIndexOf("/")).lastIndexOf("/"), str.length()));
//		System.out.println(str.substring(0, i));
	}
	
	public void testReadReportFromFile() {
		ReportBase reportBase = ReportBase.readReportFromFile("/media/nbfs/nbCloud/public/dev/AllProject/project_53a3ef98559aa1839ed9c4d2/task_54abb1f683145149794b196d/GOAnalysis_result/.report/report_GOAnalysis_AvsB-Dif");
		TemplateRender templateRender = new TemplateRender();
		Writer out = new BufferedWriter(new OutputStreamWriter(FileOperate.getOutputStream("/home/novelbio/jpx/test.txt", true)));
		templateRender.render(reportBase, out);
	}

}
