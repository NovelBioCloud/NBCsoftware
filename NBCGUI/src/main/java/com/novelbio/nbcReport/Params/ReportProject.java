package com.novelbio.nbcReport.Params;

import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.hg.xdoc.XDoc;
import com.hg.xdoc.XDocIO;
import com.novelbio.base.PathDetail;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.fileOperate.ZipOperate;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class ReportProject extends ReportBase {
	public static final Logger logger = Logger.getLogger(ReportProject.class);
	private List<String> lsXdocChildren = new ArrayList<String>();
	private String noAssign = "<#assign no=1 />";
	private String noPlusOne = "<#assign no=no+1 />";
	private String catalogCode = "<#list lsCatalogs as catalog ><para lineSpacing=\"4\" ><text fontName=\"黑体\" fontSize=\"15\" >${catalog}</text></para></#list>";
	private List<String> lsCatalogs = new ArrayList<String>();
	private Set<String> lsMethod = new HashSet<String>();
	private String content = "";
	private String projectName = "";

	/**
	 * 根据一个目录下的所有子文件　初始化项目报告对象
	 * 
	 * @param folderPath
	 *            一个目录
	 */
	public ReportProject(String folderPath)  throws Exception{
		List<String> lsChildren = FileOperate.getFoldFileNameLs(folderPath, "*", "*");
		for (String fileName : lsChildren) {
			if (!FileOperate.isFileDirectory(fileName))
				continue;
			EnumReport enumReport = EnumReport.findByFolderName(FileOperate.getFileName(fileName));
			if (enumReport == null)
				continue;
			ReportBase reportBase =  enumReport.getReportAll();
			boolean readResult = reportBase.readReportFromFile(fileName);
			if (readResult) {
				String xdocString =  reportBase.outputReportXdoc();
				if (xdocString != null) {
					lsXdocChildren.add(xdocString);
				}
			}
			lsMethod.add(enumReport.getResultFolder());
		}
	}

	/**
	 * 指定若干个文件夹　初始化项目报告对象
	 * @param lsFolders　
	 * @param isChildFolder 结果文件夹是此文件夹，还是子文件夹
	 */
	public ReportProject(List<String> lsFolders,boolean isChildFolder)  throws Exception{
		if(isChildFolder){
			for (String fileName : lsFolders) {
				if (!FileOperate.isFileDirectory(fileName))
					continue;
				for (String fileName1 : FileOperate.getFile(fileName).list()) {
					String realFilePathAndName = FileOperate.addSep(fileName) + fileName1;
					if(!FileOperate.isFileDirectory(realFilePathAndName))
						continue;
					EnumReport enumReport = EnumReport.findByFolderName(fileName1);
					if (enumReport == null)
						continue;
					ReportBase reportBase =  enumReport.getReportAll();
					boolean readResult = reportBase.readReportFromFile(realFilePathAndName);
					if (readResult) {
						String xdocString =  reportBase.outputReportXdoc();
						if (xdocString != null) {
							lsXdocChildren.add(xdocString);
						}
					}
					lsMethod.add(enumReport.getResultFolder());
				}
			}
			return;
		}
		for (String fileName : lsFolders) {
			if (!FileOperate.isFileDirectory(fileName))
				continue;
			EnumReport enumReport = EnumReport.findByFolderName(FileOperate.getFileName(fileName));
			if (enumReport == null)
				continue;
			ReportBase reportBase =  enumReport.getReportAll();
			boolean readResult = reportBase.readReportFromFile(fileName);
			if (readResult) {
				String xdocString =  reportBase.outputReportXdoc();
				if (xdocString != null) {
					lsXdocChildren.add(xdocString);
				}
			}
			lsMethod.add(enumReport.getResultFolder());
		}
	}
	
	/**
	 * 指定若干个父文件夹　寻找文件夹中的子结果文件　初始化项目报告对象
	 * 
	 * @param folderChildren
	 */
	public ReportProject(List<String> parentfolders)  throws Exception{
		for (String fileName : parentfolders) {
			if (!FileOperate.isFileDirectory(fileName))
				continue;
			EnumReport enumReport = EnumReport.findByFolderName(FileOperate.getFileName(fileName));
			if (enumReport == null)
				continue;
			ReportBase reportBase =  enumReport.getReportAll();
			boolean readResult = reportBase.readReportFromFile(fileName);
			if (readResult) {
				String xdocString =  reportBase.outputReportXdoc();
				if (xdocString != null) {
					lsXdocChildren.add(xdocString);
				}
			}
			lsMethod.add(enumReport.getResultFolder());
		}
	}
	
	@Override
	public EnumReport getEnumReport() {
		return EnumReport.Project;
	}

	@Override
	protected Map<String, Object> addParamMap() {
		Map<String, Object> mapKey2Param = new HashMap<String, Object>();
		mapKey2Param.put("lsXdocChildren", lsXdocChildren);
		mapKey2Param.put("noAssign", noAssign);
		mapKey2Param.put("noPlusOne", noPlusOne);
		mapKey2Param.put("catalogCode", catalogCode);
		mapKey2Param.put("lsCatalogs", lsCatalogs);
		mapKey2Param.put("content", content);
		mapKey2Param.put("lsMethod", lsMethod);
		return mapKey2Param;
	}

	/**
	 * 根据渲染的String结果查找标题来创建目录
	 */
	private void createCatalog(String content)  throws Exception{
		Document doc = null;
		try {
			// 读取并解析XML文档
			// 下面的是通过解析xml字符串的
			String domContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><xdoc version=\"9.1.9\">" + content + "</xdoc>";
			doc = DocumentHelper.parseText(domContent); // 将字符串转为XML
			Element rootElt = doc.getRootElement(); // 获取根节点
			@SuppressWarnings("unchecked")
			// 找出name属性为title的所有para标签下的text标签，在设计模板的时候这类para标签下只允许设置一个text标签
			List<Element> elements = (List<Element>) rootElt.selectNodes("//para[@name='title']/text");
			for (Element element : elements) {
				// 根据<text> 标签中的name属性的值来确定标题级别，这个name的属性值必须为数字
				int titleLv = Integer.parseInt(element.attributeValue("name"));
				String title = element.getTextTrim();
				// 在不同级别标签前加不同数量的缩进符
				for (int i = 0; i < titleLv; i++) {
					title = "\t" + title;
				}
				// 最后放到目录集合里
				lsCatalogs.add(title);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("模板书写不规范");
			e.printStackTrace();

		}
	}

	/**
	 * 给定保存路径outPath、文件名fileName后缀名suffix，输出带有页面页脚的最终结果报告
	 */
	public boolean outputReport(String outPathFile) throws Exception{
		try {
			// 设置目录
			String tempPath = PathDetail.getTmpPathRandom();
			content = addTitleNo(outputReportXdoc());
			createCatalog(content);
			String tmpResult = addCatalogAndBG();
			List<String> lsImageSrcs = findAllImageSrc(tmpResult);
			// 用字符串构建XDoc
			XDoc xdoc = new XDoc(tmpResult);
			String fileName = FileOperate.getFileName(outPathFile);
			String resultTempFile = FileOperate.addSep(tempPath) + fileName;
			// 加上一个封面模板，因为封面模板是没有页眉页脚的
			// 生成的文件保存目录
			XDocIO.write(xdoc, new File(resultTempFile));
			if (FileOperate.getFileNameSep(outPathFile)[1].equalsIgnoreCase("docx")) {
				motifyReport(lsImageSrcs, resultTempFile);
			}
			boolean copyResult = FileOperate.copyFile(resultTempFile, outPathFile, true);
			if (copyResult) {
				FileOperate.delFile(resultTempFile);
			} else {
				logger.error("报告拷贝出错！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("保存目录无效或不能转换成此类文件！");
			return false;
		}
		return true;
	}

	/**
	 * 根据一个xml字符串读取里面的<img src="aaa">标签的src属性 返回一个图片全路径序列集合
	 */
	public List<String> findAllImageSrc(String result) throws Exception {
		List<String> lsImageSrcs = new ArrayList<String>();
		Document doc = null;
		String picTempPath = FileOperate.addSep(EnumReport.Picture.getTempPath()) + "picTemp.PNG";
		try {
			// 读取并解析XML文档
			// 下面的是通过解析xml字符串的
			doc = DocumentHelper.parseText(result); // 将字符串转为XML
			Element rootElt = doc.getRootElement(); // 获取根节点
			@SuppressWarnings("unchecked")
			// 找出所有img标签
			List<Element> elements = (ArrayList<Element>) rootElt.selectNodes("//img");
			// 循环标签，取得它的src属性值放到集合中
			for (Element element : elements) {
				lsImageSrcs.add(element.attribute("src").getValue());
				if (!element.attribute("src").getValue().startsWith("data:im")) {
					element.attribute("src").setValue(picTempPath);
				}
			}
			// 背景层里的一条线，虽然不是img标签，但是最终会保存为一张图片，所以要把它算到集合里
			lsImageSrcs.add(1, "data:im");
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();

		}
		return lsImageSrcs;
	}

	/**
	 * 将最后生成的word中的小图换成原始大图 同时将封面的页眉页脚去掉 同时还要把所有的表格边框线改成白色
	 * 
	 * @param lsImageSrcs
	 *            图片位置序列
	 * @param reportPath
	 *            生成的word报告文件，包含全路径
	 */
	public void motifyReport(List<String> lsImageSrcs, String reportPath) throws Exception {
		// word转成的压缩包名
		String zipFileName = FileOperate.changeFileSuffixReal(reportPath, null, "zip");
		// 解压后的文件夹全名
		String folderName = reportPath.substring(0, reportPath.lastIndexOf(FileOperate.getSepPath()) + 1) + FileOperate.getFileNameSep(reportPath)[0];
		// 图片所在路径
		String imagePath = folderName + FileOperate.getSepPath() + "word" + FileOperate.getSepPath() + "media" + FileOperate.getSepPath();
		// 页眉页脚需要修改的document.xml文件全路径
		String documtXMLPath = folderName + FileOperate.getSepPath() + "word" + FileOperate.getSepPath() + "document.xml";
		// final PatternOperate pat = new PatternOperate("rId(\\d+).png",
		// false);
		try {
			ZipOperate.unZipFiles(zipFileName, folderName);
			String result = null;
			for (int i = 0; i < lsImageSrcs.size(); i++) {
				result = lsImageSrcs.get(i);
				if (result.startsWith("data:im")) {
					continue;
				}
				FileOperate.delFile(imagePath + "rId" + (i + 11) + ".png");
				FileOperate.copyFile(result, imagePath + FileOperate.getFileName(result), true);
				FileOperate.changeFileName(imagePath + FileOperate.getFileName(result), "rId" + (i + 11) + ".png", true);
			}
			FileOperate.delFile(zipFileName);
			// 去掉首页的页眉页脚,和修改表格边框
			modifyWord(documtXMLPath);
			ZipOperate.zip(folderName, zipFileName);
			FileOperate.changeFileName(zipFileName, FileOperate.getFileName(reportPath), true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FileOperate.delFolder(folderName);
	}

	/** 去页眉页脚改表格边框线，同时设置行间距1.5倍 */
	@SuppressWarnings("unchecked")
	private void modifyWord(String documtXMLPath) throws Exception {
		// 去掉首页的页眉页脚
		SAXReader saxReader = new SAXReader();
		Document document = null;
		try {
			document = saxReader.read(new File(documtXMLPath));
		} catch (DocumentException e) {
			logger.error("修改报告失败");
			e.printStackTrace();
		}
		Element rootElement = document.getRootElement();

		// 在<w:sectPr>标签中加入<w:titlePg />这样一个标签，在word中意思为首页页眉页脚不同，要另外设定
		for (Element element : (ArrayList<Element>) rootElement.selectNodes("//w:sectPr")) {
			element.addElement("w:titlePg");
		}
		// 以下的两个循环用来移除<w:headerReference w:type="first" /> 首页 页眉内容标签 首页 页脚标签同理
		// 可用可不用，用了防止有意外，我们生成的没有以下这两种标签
		for (Element element : (ArrayList<Element>) rootElement.selectNodes("//w:headerReference")) {
			if (element.attributeValue("type").equals("first")) {
				element.getParent().remove(element);
			}
		}
		for (Element element : (ArrayList<Element>) rootElement.selectNodes("//w:footerReference")) {
			if (element.attributeValue("type").equals("first")) {
				element.getParent().remove(element);
			}
		}

		for (Element element : (ArrayList<Element>) rootElement.selectNodes("//w:trHeight")) {
			element.addAttribute("w:hRule", "exact");
		}

		// 在<w:tblPr >标签下添加<w:tblBorders >标签来设置table的边框属性
		for (Element element : (ArrayList<Element>) rootElement.selectNodes("//w:tblPr")) {
			Element tblBorders = element.addElement("w:tblBorders");
			// 创建一个需要插入的xml标签w:tblBorders
			Element top = tblBorders.addElement("w:top");
			top.addAttribute("w:val", "single");
			top.addAttribute("w:sz", "4");
			top.addAttribute("w:space", "0");
			top.addAttribute("w:color", "FFFFFF");
			top.addAttribute("w:themeColor", "background1");
			Element left = tblBorders.addElement("w:left");
			left.addAttribute("w:val", "single");
			left.addAttribute("w:sz", "4");
			left.addAttribute("w:space", "0");
			left.addAttribute("w:color", "FFFFFF");
			left.addAttribute("w:themeColor", "background1");
			Element bottom = tblBorders.addElement("w:bottom");
			bottom.addAttribute("w:val", "single");
			bottom.addAttribute("w:sz", "4");
			bottom.addAttribute("w:space", "0");
			bottom.addAttribute("w:color", "FFFFFF");
			bottom.addAttribute("w:themeColor", "background1");
			Element right = tblBorders.addElement("w:right");
			right.addAttribute("w:val", "single");
			right.addAttribute("w:sz", "4");
			right.addAttribute("w:space", "0");
			right.addAttribute("w:color", "FFFFFF");
			right.addAttribute("w:themeColor", "background1");
			Element insideH = tblBorders.addElement("w:insideH");
			insideH.addAttribute("w:val", "single");
			insideH.addAttribute("w:sz", "4");
			insideH.addAttribute("w:space", "0");
			insideH.addAttribute("w:color", "FFFFFF");
			insideH.addAttribute("w:themeColor", "background1");
			Element insideV = tblBorders.addElement("w:insideV");
			insideV.addAttribute("w:val", "single");
			insideV.addAttribute("w:sz", "4");
			insideV.addAttribute("w:space", "0");
			insideV.addAttribute("w:color", "FFFFFF");
			insideV.addAttribute("w:themeColor", "background1");
		}
		// 移除所有表格的每个cell的边框属性，让表格继承table的边框属性，上一个循环就是设置table的边框属性
		for (Element element : (ArrayList<Element>) rootElement.selectNodes("//w:tcBorders")) {
			element.getParent().remove(element);
		}

		// 设值1.5倍行间距<w:pPr><w:spacing w:line="360" w:lineRule="auto"/></w:pPr>
		// 不要添加在表格和目录上
		for (Element element : (ArrayList<Element>) rootElement.selectNodes("//w:pPr")) {
			if (!(element.getParent().getParent().getName().equals("tc"))) {
				Element spacing = element.addElement("w:spacing");
				spacing.addAttribute("w:line", "360");
				spacing.addAttribute("w:lineRule", "auto");
			}
		}
		// 移除目录的行间距
		for (Element element : (ArrayList<Element>) rootElement.selectNodes("//w:rPr/w:rFonts[@w:ascii='黑体']")) {
			if (element.getParent().element("sz").attributeValue("val").equals("22.5")) {
				Element p = element.getParent().getParent().getParent();
				p.element("pPr").clearContent();
			}
		}

		FileOperate.delFile(documtXMLPath);
		try {
			/** 将document中的内容写入文件中 */
			XMLWriter writer = new XMLWriter(new FileWriter(new File(documtXMLPath)));
			writer.write(document);
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 添加序号并渲染
	 * 
	 * @param path
	 *            生成好的模板位置
	 * @throws Exception
	 */
	private String addTitleNo(String tempString) throws Exception {
		Map<String, Object> mapKey2Params = addParamMap();
		// TODO 异常待处理
		// 加载模板
		Configuration cfg = new Configuration();
        StringTemplateLoader stringLoader = new StringTemplateLoader();
        stringLoader.putTemplate("myTemplate", tempString);
        cfg.setTemplateLoader(stringLoader);
        Template template = cfg.getTemplate("myTemplate","utf-8");
		StringWriter sw = new StringWriter();
		// 处理并把结果输出到字符串中
		template.process(mapKey2Params, sw);
		// 返回渲染好的xdoc字符串
		return sw.toString();
	}

	private String addCatalogAndBG() throws Exception {
		Map<String, Object> mapKey2Params = addParamMap();
		// TODO 异常待处理
		// 加载模板
		Configuration cf = new Configuration();
		cf.setClassicCompatible(true);
		// 模板存放路径
		cf.setDirectoryForTemplateLoading(new File(getEnumReport().getTempPath()));
		cf.setEncoding(Locale.getDefault(), "UTF-8");
		// 模板名称
		Template template = cf.getTemplate("background_catalog.xdoc");
		StringWriter sw = new StringWriter();
		// 处理并把结果输出到字符串中
		template.process(mapKey2Params, sw);
		// 返回渲染好的xdoc字符串
		return sw.toString();
	}

	/** 为项目报告添加名称 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Override
	public boolean readReportFromFile(String savePath) {
		return false;
	}
}
