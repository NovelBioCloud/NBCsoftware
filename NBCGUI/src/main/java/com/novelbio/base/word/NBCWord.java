package com.novelbio.base.word;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.novelbio.nbcReport.Params.ReportBase;


/**
 * NBCWord渲染工具
 * @author Administrator
 *
 */
public class NBCWord {
	public static Logger logger = Logger.getLogger(NBCWord.class);
	private WordApp wordApp;
	private Document nowDoc;
	
	/**
	 * @param tempPath 模板路径
	 */
	public NBCWord(String tempPath) {
		try {
			wordApp = new WordApp(true);
			if(tempPath == null)
				nowDoc = wordApp.addNewDocument();
			else
				nowDoc = wordApp.openExistDocument(tempPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 渲染报告
	 * @param reportBase
	 */
	public void renderReport(ReportBase reportBase){
		try {
			render(nowDoc,reportBase.buildFinalParamMap());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("报告渲染出错");
		}
	}
	
	/**
	 * 开始渲染参数
	 * @param param
	 * @return
	 * @throws Exception 
	 */
	private NBCWord render(Document doc,Map<String,Object> param) throws  Exception{
		for(String key : param.keySet()){
			if(param.get(key) == null)
				continue;
			if(param.get(key) instanceof Collection){
				if(((Collection<?>)param.get(key)).isEmpty())
					continue;
				List<Object> lsParam = new ArrayList<>();
				lsParam.addAll((Collection<?>) param.get(key));
				if(lsParam.get(0) instanceof NBCWordTable){
					writeTables(doc,key,lsParam);
				}else if(lsParam.get(0) instanceof NBCWordImage){
					writeImages(doc,key,lsParam);
				}else if(lsParam.get(0) instanceof ReportBase){
					writeOtherTemp(doc,key, lsParam);
				}else{
					writeText(doc,key,(Collection<?>)param.get(key));
				}
			}else{
				List<Object> lsParam = new ArrayList<>();
				if(param.get(key) instanceof NBCWordTable){
					lsParam.add(param.get(key));
					writeTables(doc,key,lsParam);
				}else if(param.get(key) instanceof NBCWordImage){
					lsParam.add(param.get(key));
					writeImages(doc,key,lsParam);
				}else if(param.get(key) instanceof ReportBase){
					lsParam.add(param.get(key));
					writeOtherTemp(doc,key, lsParam);
				}else{
					lsParam.add(param.get(key));
					writeText(doc,key,lsParam);
				}
				
				
			}
		}
		replaceOtherKeyToDefault(doc);
		return this;
	}
	
	/**
	 * 把其它需要替换的文本变成空或者默认值${aa##p|asf##a|aaaf##n|##d|bas}
	 * aa是变量名 ##p|asf是指 p方法(在变量文本之间添加文本)参数为asf  ##a|aaaf a方法参数为aaaf
	 * @throws Exception 
	 */
	private void replaceOtherKeyToDefault(Document doc) throws Exception{
		while(doc.getSelection().find("\\$\\{*\\}",true)){
			NBCWordText pattern = new NBCWordText(doc.getSelection());
			pattern.useDefaultText();
		}
	}
	
	/**
	 * 写入文本
	 * @param key 替换的关键字
	 * @param values 需要添加的值
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	private void writeText(Document doc,String key,Collection<?> values) throws Exception{
		while(doc.getSelection().find("\\$\\{"+key+"\\}",true)){
			NBCWordText wordText = new NBCWordText(doc.getSelection());
			wordText.replaceAs((Collection<String>)values);
		}
		while(doc.getSelection().find("\\$\\{"+key+"[#]*\\}",true)){
			NBCWordText wordText = new NBCWordText(doc.getSelection());
			wordText.replaceAs((Collection<String>)values);
		}
	}
	
	/**
	 * 写入表格
	 * @param key 替换的关键字
	 * @param tables 添加的表格集合
	 * @throws Exception 
	 */
	private void writeTables(Document doc,String key,List<Object> tables) throws Exception{
		if(!(tables.get(0) instanceof NBCWordTable))
			return;
		while(doc.getSelection().find("\\$\\{"+key+"\\}",true)){
			String pattern = doc.getSelection().getText();
			doc.getSelection().replaceSelected("");
			for (Object object : tables) {
				NBCWordTable table = (NBCWordTable)object;
				table.addToWord(doc.getSelection(),pattern);
			}
		}
		while(doc.getSelection().find("\\$\\{"+key+"[#]*\\}",true)){
			String pattern = doc.getSelection().getText();
			doc.getSelection().replaceSelected("");
			for (Object object : tables) {
				NBCWordTable table = (NBCWordTable)object;
				table.addToWord(doc.getSelection(),pattern);
			}
		}
	}
	
	/**
	 * 写入其它模板中内容
	 * @param key 替换的关键字
	 * @param reportParams 报告参数
	 * @throws Exception
	 */
	private void writeOtherTemp(Document doc,String key,List<Object> reportBases) throws Exception{
		if(!(reportBases.get(0) instanceof ReportBase))
			return;
		while(doc.getSelection().find("\\$\\{"+key+"\\}",true)){
			doc.getSelection().replaceSelected("");
			for (Object object : reportBases) {
				ReportBase reportBase = (ReportBase)object;
				Document otherDoc = doc.openDocumentForCopy(reportBase.getTempPathAndName());
				render(otherDoc, reportBase.buildFinalParamMap());
				doc.copyAllFromAnother();
			}
		}
		while(doc.getSelection().find("\\$\\{"+key+"[#]*\\}",true)){
			String pattern = doc.getSelection().getText();
			if(pattern.contains("##e|")){
				String patternLeft = pattern.substring(2, pattern.length() - 1);
				String[] methods = patternLeft.split("##");
				for (int i = 0; i < methods.length; i++) {
					if (methods[i].startsWith("e|")){
						String existText = methods[i].split("e\\|")[1];
						doc.getSelection().replaceSelected(existText);
					}
				}
				return;
			}
			for (Object object : reportBases) {
				ReportBase reportBase = (ReportBase)object;
				Document otherDoc = doc.openDocumentForCopy(reportBase.getTempPathAndName());
				render(otherDoc, reportBase.buildFinalParamMap());
				doc.copyAllFromAnother();
			}
		}
	}
	
	/**
	 * 写入图片
	 * @param key 替换的关键字
	 * @param images 添加的图片集合
	 * @throws Exception 
	 */
	private void writeImages(Document doc,String key,List<Object> images) throws Exception{
		if(!(images.get(0) instanceof NBCWordImage))
			return;
		while(doc.getSelection().find("\\$\\{"+key+"\\}",true)){
			String pattern = doc.getSelection().getText();
			doc.getSelection().replaceSelected("");
			for (Object object : images) {
				NBCWordImage image = (NBCWordImage)object;
				image.addToWord(doc.getSelection(),pattern);
			}
		}
		while(doc.getSelection().find("\\$\\{"+key+"[#]*\\}",true)){
			String pattern = doc.getSelection().getText();
			doc.getSelection().replaceSelected("");
			for (Object object : images) {
				NBCWordImage image = (NBCWordImage)object;
				image.addToWord(doc.getSelection(),pattern);
			}
		}
	}
	
	
	/**
	 * 不保存而关闭word
	 * @throws Exception 
	 */
	public void close() throws Exception {
		wordApp.quit();
	}

	public WordApp getWordApp() {
		return wordApp;
	}
	

	/**
	 * 结果另存为
	 * @param filePathName　新的全路径
	 * @throws Exception 
	 */
	public void saveDocAs(String filePathName) throws Exception {
		wordApp.getNowDocument().saveAs(filePathName);
	}
	
	/**
	 * 保存打开的报告,如果是打开的模板,请调用saveDocAs方法,不然会覆盖模板,这是我们不希望看到的
	 * @throws Exception 
	 */
	public void saveDoc() throws Exception {
		wordApp.getNowDocument().save();
	}
	
}
