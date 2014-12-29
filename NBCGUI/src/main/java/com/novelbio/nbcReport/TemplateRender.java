package com.novelbio.nbcReport;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import com.novelbio.base.fileOperate.FileOperate;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class TemplateRender {
	
	/** 用来设置编码方式和模板的路径*/
	private Configuration configuration = null;

	public TemplateRender() {
		configuration = new Configuration();
		configuration.setDefaultEncoding("utf-8");
	}
	
	/**
	 * 模板的渲染方法
	 * @param templatePath 模板的路径
	 * @param templateName 模板的名称,为ftl格式的文件
	 * @param outputPath 文件输出的路径包含文件名
	 * @param mapKey2Param 模板中需要的参数,key为string类型，对应的param为object类型
	 */
	public void render(String templatePath, String templateName, String outputPath, Map<String, Object> mapKey2Param) {
		configuration.setClassForTemplateLoading(this.getClass(), templatePath);
		try {
			Template template = configuration.getTemplate(templateName);
			Writer out = new BufferedWriter(new OutputStreamWriter(FileOperate.getOutputStream(outputPath, true)));
			template.process(mapKey2Param, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
