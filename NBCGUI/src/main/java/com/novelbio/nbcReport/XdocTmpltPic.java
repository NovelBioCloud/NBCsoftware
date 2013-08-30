package com.novelbio.nbcReport;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.nbcReport.Params.EnumReport;

import freemarker.template.Configuration;
import freemarker.template.Template;


/**
 *  图片模板对应的实体类
 * @author gaozhu
 *
 */
public class XdocTmpltPic{

	private int height = 300;
	private int width = 600;
	/** 图片的标题 */
	private String title = "";
	/** 图片的注： */
	private String note = ""; 
	/** 同类图片的对比说明在这类图片的上方 */
	private String upCompare = "";
	/** 同类图片的对比说明在这类图片的下方 */
	private String downCompare = "";
	/** 实验组名 */
	private String expTeamName = "";
	/** 用于并排显示的图片名 */
	List<String> lsPicPaths;
	
	/** 一张图片的构造方法 */
	public XdocTmpltPic(String picPath) {
		lsPicPaths = new ArrayList<String>();
		lsPicPaths.add(picPath);
		String titleDefault = "";
		for (String filePath : lsPicPaths) {
			if (!titleDefault.equals("")) {
				titleDefault += "、";
			}
			titleDefault += FileOperate.getFileNameSep(filePath)[0];
		}
		this.title = "上图为文件 "+titleDefault+" 所展示的图片";
	}
	
	/** 并列多张图片的构造方法 */
	public XdocTmpltPic(List<String> lsPicPaths){
		this.lsPicPaths = lsPicPaths;
		String titleDefault = "";
		for (String filePath : lsPicPaths) {
			if (!titleDefault.equals("")) {
				titleDefault += "、";
			}
			titleDefault += FileOperate.getFileNameSep(filePath)[0];
		}
		this.title = "上图为文件 "+titleDefault+" 所展示的图片";
	}
	
	/**
	 * 生成freemarker所需要的参数
	 */
	private Map<String, Object> addParams(){
		Map<String, Object> mapKey2Param = new HashMap<String, Object>();
		mapKey2Param.put("width",width);
		mapKey2Param.put("height",height);
		mapKey2Param.put("lsSrcs", lsPicPaths);
		mapKey2Param.put("title",title);
		mapKey2Param.put("note",note);
		mapKey2Param.put("upCompare",upCompare);
		mapKey2Param.put("downCompare",downCompare);
		mapKey2Param.put("expTeamName",expTeamName);
		return mapKey2Param;
	}
	
	/** 输出渲染好的xdoc的toString结果 */
	@Override
	public String toString(){
		Map<String, Object> mapKey2Params = addParams();
		/** 把子xdoc的toString方法封装成集合传递给本xdoc */
		try {
			Configuration cf = new Configuration();
			cf.setClassicCompatible(true);
			// 模板存放路径
			cf.setDirectoryForTemplateLoading(new File(EnumReport.Picture.getTempPath()));
			cf.setEncoding(Locale.getDefault(), "UTF-8");
			// 模板名称
			Template template = cf.getTemplate(EnumReport.Picture.getTempName());
			StringWriter sw = new StringWriter();
			// 处理并把结果输出到字符串中
			template.process(mapKey2Params, sw);
			return sw.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/** 设置图片的高度 */
	public void setHeight(int height) {
		this.height = height;
	}
	/** 设置图片的宽度 */
	public void setWidth(int width) {
		this.width = width;
	}

	/** 图片的标题 */
	public void setTitle(String title) {
		this.title = title;
	}
	/** 图片的注： */
	public void setNote(String note) {
		this.note = note;
	}
	/** 图片上方说明 */
	public void setUpCompare(String upCompare) {
		this.upCompare = upCompare;
	}
	/** 图片下方说明 */
	public void setDownCompare(String downCompare) {
		this.downCompare = downCompare;
	}
	/** 组名 */
	public void setExpTeamName(String expTeamName) {
		this.expTeamName = expTeamName;
	}

}
