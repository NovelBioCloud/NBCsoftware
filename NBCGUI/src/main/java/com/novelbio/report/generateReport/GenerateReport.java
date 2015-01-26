package com.novelbio.report.generateReport;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.report.TemplateRender;
import com.novelbio.report.Params.ReportBase;

public class GenerateReport {
	
	/** 参数形式“/media/nbfs/nbCloud/public/dev/AllProject/project_53a3ef98559aa1839ed9c4d2/task_54aa3ed38314b5cdd982a205/GOAnalysis_result”，
	 * 获取路径下.report里的序列化文件并依次渲染成latex文件 */
	public void generateReport(String taskResultPath) throws IOException {
		//获取task路径下的序列化文件
		List<String> lsReportFilePath = FileOperate.getFoldFileNameLs(FileOperate.addSep(taskResultPath) + ".report");
		for (String reportFilePath : lsReportFilePath) {
			ReportBase reportBase = (ReportBase)ReportBase.readReportFromFile(reportFilePath);
			TemplateRender templateRender = new TemplateRender();
			//生成latex文件的全路径
//			String latexFilePath = taskResultPath + reportFilePath.substring(reportFilePath.lastIndexOf("/"), reportFilePath.length()) + ".tex";
			String latexFilePath = taskResultPath + FileOperate.getFileName(reportFilePath) + ".tex";
			//如果文件存在就删掉，因为如果不删会报错
			if (FileOperate.isFileExist(latexFilePath)) {
				FileOperate.delFile(latexFilePath);
			}
			Writer out = new BufferedWriter(new OutputStreamWriter(FileOperate.getOutputStream(latexFilePath, true)));
			templateRender.render(reportBase, out);
			out.close();
		}
	}
	
	public void generateReport(String taskResultPath, Writer out) {
		List<String> lsReportFilePath = FileOperate.getFoldFileNameLs(FileOperate.addSep(taskResultPath) + ".report");
		for (String reportFilePath : lsReportFilePath) {
			ReportBase reportBase = (ReportBase)ReportBase.readReportFromFile(reportFilePath);
			TemplateRender templateRender = new TemplateRender();
			//生成latex文件的全路径
//			String latexFilePath = taskResultPath + reportFilePath.substring(reportFilePath.lastIndexOf("/"), reportFilePath.length()) + ".tex";
			String latexFilePath = taskResultPath + FileOperate.getFileName(reportFilePath) + ".tex";
			//如果文件存在就删掉，因为如果不删会报错
			if (FileOperate.isFileExist(latexFilePath)) {
				FileOperate.delFile(latexFilePath);
			}
			templateRender.render(reportBase, out);
		}		
	}

}
