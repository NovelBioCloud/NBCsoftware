package com.novelbio.nbcReport.Params;

import java.io.File;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.novelbio.base.dataOperate.TxtReadandWrite;
import com.novelbio.base.fileOperate.FileOperate;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 参数对象基本抽象类
 * @author novelbio
 *
 */
public abstract class ReportBase {
	
	
	/**
	 * 添加报告参数
	 */
	protected abstract Map<String, Object> addParamMap();
	
	/**
	 * 得到报告类型
	 * @return
	 */
	public abstract EnumReport getEnumReport();
	
	
	/**
	 * 输出模板结果
	 * @param filePath 结果目录
	 * @return
	 */
	public boolean outputReportXdoc(String filePath){
		TxtReadandWrite txtReadandWrite = null;
		Map<String, Object> mapKey2Params = addParamMap();
		try {
			Configuration cf = new Configuration();
			cf.setClassicCompatible(true);
			// 模板存放路径
			cf.setDirectoryForTemplateLoading(new File(getEnumReport().getTempPath()));
			cf.setEncoding(Locale.getDefault(), "UTF-8");
			// 模板名称
			Template template = cf.getTemplate(getEnumReport().getTempName());
			StringWriter sw = new StringWriter();
			// 处理并把结果输出到字符串中
			template.process(mapKey2Params, sw);
			// 返回渲染好的xdoc字符串
			txtReadandWrite = new TxtReadandWrite(FileOperate.addSep(filePath)+getEnumReport().getReportXdocFileName(),true);
			txtReadandWrite.writefile(sw.toString(), true);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			txtReadandWrite.close();
		}
		return true;
	}
}
