package com.novelbio.report.generateReport;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.report.TemplateRender;
import com.novelbio.report.Params.EnumReport;
import com.novelbio.report.Params.ReportBase;

public class ProjectReport {
	
	/** 生成项目报告，mapKey2ProjectInfo中是项目的信息，key代表的项目信息参数的名字，ProjectInfo为相映参数的信息；taskResultPath为任务结果的路径 
	 * @throws IOException */
	public void generateReport(Map<String, Object> mapKey2ProjectInfo, String projectPath, List<String> lsTaskResultPath) throws IOException {
		//放项目结果报告文件夹的路径
		String projectResultPath = FileOperate.addSep(projectPath) + "projectReport";
		FileOperate.createFolders(projectResultPath);
		//项目结果报告的全路径
		String projectReportPath = FileOperate.addSep(projectResultPath) + "projectReport.tex";
		
		Writer out = new BufferedWriter(new OutputStreamWriter(FileOperate.getOutputStream(projectReportPath, true)));
		TemplateRender templateRender = new TemplateRender();
		//渲染项目信息
		templateRender.render(EnumReport.ReportAll.getFtlTempName(), mapKey2ProjectInfo, out);
		//渲染各个task的报告
		for (String taskResultPath : lsTaskResultPath) {
			List<String> lsReportFilePath = FileOperate.getFoldFileNameLs(FileOperate.addSep(taskResultPath) + ".report");
			for (String reportFilePath : lsReportFilePath) {
				ReportBase reportBase = (ReportBase)ReportBase.readReportFromFile(reportFilePath);
				templateRender.render(reportBase, out);
			}
		}
		
		templateRender.render(EnumReport.NovelbioEnd.getFtlTempName(), mapKey2ProjectInfo, out);
		
		out.close();
		
	}
	
	private void copyImages(String ReportPath, String taskResultPath) {
		List<String> lsSubFilePath = FileOperate.getFoldFileNameLs(taskResultPath);
		for (String subFilePath : lsSubFilePath) {
			String fileName = FileOperate.getFileName(subFilePath);
		}
		String projectImagePath = FileOperate.addSep(ReportPath) + "images";
	}

}
