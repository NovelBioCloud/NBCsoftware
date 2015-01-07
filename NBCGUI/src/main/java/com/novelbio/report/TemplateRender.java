package com.novelbio.report;

import java.io.Writer;
import java.util.List;
import java.util.Map;

import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.report.Params.ReportBase;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class TemplateRender {
	
	/**
	 * 渲染报告，递归渲染其子报告
	 * @param reportBase 要渲染的报告
	 * @param writer
	 */
	public void render(ReportBase reportBase, Writer writer) {
		//获得报告的参数对
		Map<String, Object> mapKey2Param = reportBase.getMapKey2Param();
		render(reportBase.getFtlTempPathAndName(), mapKey2Param, writer);
		//获得子报告
		List<ReportBase> lsReportBase = reportBase.getSubReportBase();
		for (ReportBase reportBase2 : lsReportBase) {
			render(reportBase2, writer);
		}
	}
	
	/**
	 * 模板的渲染方法
	 * @param templatePath 模板的路径
	 * @param mapKey2Param 模板中需要的参数,key为string类型，对应的param为object类型
	 * @param writer 
	 */
	public void render(String templatePath, Map<String, Object> mapKey2Param, Writer writer) {
		//用来配置渲染模板时的默认编码和模板的路径
		Configuration configuration = new Configuration();
		configuration.setDefaultEncoding("utf-8");
		configuration.setClassForTemplateLoading(this.getClass(), FileOperate.getParentPathNameWithSep(templatePath));
		try {
			Template template = configuration.getTemplate(FileOperate.getFileName(templatePath));
			template.process(mapKey2Param, writer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
