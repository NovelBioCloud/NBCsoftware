package com.novelbio.word;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.novelbio.word.NBCWordImage;
import com.novelbio.word.NBCWordTable;
import com.novelbio.word.NBCWordText;
import com.novelbio.nbcReport.Params.ReportBase;
import com.novelbio.word.Document;
import com.novelbio.word.Selection;

public class Document {	

	/**word应用程序*/
	private ActiveXComponent wordApp;
	/**光标的位置，选中的文本*/
	private Selection selection;
	
	private Dispatch currentDis;

	/**另外的文档*/
	private Document anotherDoc;

	public Document(ActiveXComponent wordApp, Dispatch currentDis) {
		this.wordApp = wordApp;
		this.currentDis = currentDis;
	}

	/**
	 * 按照参数的类别分别进行渲染
	 * @param mapKey2Param
	 * @return
	 */
	//TODO key是什么，value是什么
	public Document render(Map<String, Object> mapKey2Param) {
		for (String key : mapKey2Param.keySet()) {
			Object param = mapKey2Param.get(key);
			if (param == null)
				continue;
			
			List<Object> lsParam;

			if (param instanceof Collection) {
				if (((Collection<?>)param).isEmpty())
					continue;
				lsParam = new ArrayList<>((Collection<?>) param);
			} else {
				lsParam = new ArrayList<>();
				lsParam.add(param);
			}
			
			if (lsParam.get(0) instanceof NBCWordTable) {
				writeTables(lsParam, key);
			} else if (lsParam.get(0) instanceof NBCWordImage) {
				writeImages(lsParam, key);
			} else if (lsParam.get(0) instanceof ReportBase) {
				writeOtherTmp(lsParam, key);
			} else {
				writeText(lsParam, key);
			}
			
		}
		replaceOtherKeyToDefault();
		return this;
	}
	
	/**
	 * 写入表格
	 * @param lsNBCWordTable
	 * @param isKeyExist
	 */
	private void writeTables(List<Object> lsNBCWordTable, String key) {
		if (!(lsNBCWordTable.get(0) instanceof NBCWordTable)) {
			return;
		}
		while (getSelection().find("[\\$][\\{]"+key+"[\\}]",true)) {
			for (Object object : lsNBCWordTable) {
				NBCWordTable nbcWordTable = (NBCWordTable) object;
				nbcWordTable.insertToDoc(getSelection());
			}
		}
		while (getSelection().find("[\\$][\\{]"+key+"[#]*[\\}]",true)) {
			for (Object object : lsNBCWordTable) {
				NBCWordTable nbcWordTable = (NBCWordTable) object;
				nbcWordTable.insertToDoc(getSelection());
			}
		}
	}
	
	/**
	 * 写入图片
	 * @param lsNBCWordImage
	 * @param key
	 */
	private void writeImages(List<Object> lsNBCWordImage, String key) {
		if (!(lsNBCWordImage.get(0) instanceof NBCWordImage)) {
			return;
		}
		while (getSelection().find("[\\$][\\{]"+key+"[\\}]",true)) {
			for (Object object : lsNBCWordImage) {
				NBCWordImage nbcWordImage = (NBCWordImage) object;
				nbcWordImage.insertToDoc(getSelection());
			}
		}
		while (getSelection().find("[\\$][\\{]"+key+"[#]*[\\}]",true)) {
			for (Object object : lsNBCWordImage) {
				NBCWordImage nbcWordImage = (NBCWordImage) object;
				nbcWordImage.insertToDoc(getSelection());
			}
		}
	}
	
	/** 替换默认的 */
	private void replaceOtherKeyToDefault() {
		while(getSelection().find("[\\$][\\{]*[\\}]",true)){
			NBCWordText pattern = new NBCWordText();
			pattern.useDefaultText(getSelection());
		}
	}
	
	/**
	 * 写入文本
	 * @param values
	 * @param key
	 */
	private void writeText(Collection<?> values, String key) {
		while (getSelection().find("[\\$][\\{]"+key+"[\\}]",true)) {
			NBCWordText nbcWordText = new NBCWordText();
			nbcWordText.insertToDoc(getSelection(), values);
		}
		while (getSelection().find("[\\$][\\{]"+key+"[#]*[\\}]",true)) {
			NBCWordText nbcWordText = new NBCWordText();
			nbcWordText.insertToDoc(getSelection(), values);
		}
	}

	/**
	 * 写入子报告
	 * @param lsReportBases
	 */
	private void writeOtherTmp(List<Object> lsReportBases, String key) {
		if (!(lsReportBases.get(0) instanceof ReportBase)) {
			return;
		}
		while (getSelection().find("[\\$][\\{]"+key+"[\\}]",true)) {
			getSelection().replaceSelected("");
			insertReportBases(lsReportBases);
		}
		while (getSelection().find("[\\$][\\{]"+key+"[#]*[\\}]",true)) {
			if (!paresePattern(getSelection().getText())) {
				insertReportBases(lsReportBases);
			}
		}

	}
	
	/**
	 * 插入子报告
	 * @param lsReportBases
	 * @return
	 */
	private void insertReportBases(List<Object> lsReportBases) {
		for (Object object : lsReportBases) {
			ReportBase reportBase = (ReportBase)object;
//			anotherDoc = openDocumentForCopy(reportBase.getTempPathAndName());
//			anotherDoc.render(reportBase.buildFinalParamMap());
			anotherDoc = openDocumentForCopy("C:\\Documents and Settings\\Administrator\\桌面\\GO_All.doc");
			anotherDoc.render(reportBase.buildFinalParamMap());
			copyAllFromAnother(anotherDoc);
		}
	}
	
	/**
	 * 判断是否有默认替换
	 * @param pattern
	 * @return
	 */
	private boolean paresePattern(String pattern) {
		if(pattern.contains("##e|")){
			String patternLeft = pattern.substring(2, pattern.length() - 1);
			String[] methods = patternLeft.split("##");
			for (int i = 0; i < methods.length; i++) {
				if (methods[i].startsWith("e|")) {
					String existText = methods[i].split("e\\|")[1];
					getSelection().replaceSelected(existText);
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * 打开另一个文档用来复制
	 * @param filePathName
	 * @return 
	 */
	public Document openDocumentForCopy(String filePathName) {
		Dispatch dispatch = Dispatch.call(dispatchDoc, "Open", filePathName).toDispatch();
		return new Document(wordApp, dispatchDoc);
	}

	/**
	 * 复制另一个文档的全部内容
	 */
	public void copyAllFromAnother(Document doc) {
		Dispatch range = Dispatch.get(doc.getDispatch(), "Content").toDispatch(); // 取得当前文档的内容  
		Dispatch.call(range, "Copy");
		Dispatch textRange = Dispatch.get(getSelection().getInstance(), "Range").toDispatch();  
		Dispatch.call(textRange, "Paste");
		doc.close();
	}

	/**
	 * 获取selection
	 * @return
	 */
	public Selection getSelection() {
		if(selection == null) {
			//获得光标所在的dispatch
			Dispatch dispatch = Dispatch.call(wordApp, "Selection").toDispatch();
			selection = new Selection(wordApp, currentDis, dispatch);
		}
		return selection;
	}



}
