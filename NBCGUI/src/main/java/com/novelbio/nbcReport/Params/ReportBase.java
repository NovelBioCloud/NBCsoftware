package com.novelbio.nbcReport.Params;

import java.io.File;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Locale;
import java.util.Map;

import com.novelbio.base.dataOperate.TxtReadandWrite;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.nbcReport.XdocTable;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 参数对象基本抽象类
 * @author novelbio
 *
 */
public abstract class ReportBase  implements Cloneable, Serializable {
	
	
	/**
	 * 添加报告参数
	 */
	protected Map<String, Object> addParamMap() {
		// TODO Auto-generated method stub
		return null;
	}
	
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
	public String outputReportXdoc(){
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
			return sw.toString();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 把对象本身写成二进制文件
	 * @param savePath 保存的路径，会添加.report目录
	 * @return
	 */
	public boolean writeAsFile(String savePath){
		String reportPath = FileOperate.addSep(savePath) + FileOperate.getSepPath() + ".report";
		FileOperate.createFolders(reportPath);
		String randomReportFile = FileOperate.addSep(reportPath) +  getEnumReport().getReportRandomFileName();
		FileOperate.writeObjectToFile(this, randomReportFile);
		return true;
	}
	
	/**
	 * 从文件中反序列化对象并添加到reportAll中,会自动到savePath下的.report文件夹找所有的序列化文件
	 * @return
	 */
	public abstract boolean readReportFromFile(String savePath);
	
	/**
	 * 取得克隆的对象
	 */
	public ReportBase getClone() {
		try {
			return (ReportBase) this.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
