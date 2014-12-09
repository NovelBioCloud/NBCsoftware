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
	/**带开的文档集合*/
	private Dispatch documents;
	/**光标的位置，选中的文本*/
	private Selection selection;
	/**Dispatch实例*/
	private Dispatch instance;
	/**另外的文档*/
	private Document anotherDoc;

	public Document(ActiveXComponent wordApp, Dispatch instance) {
		this.wordApp = wordApp;
		this.instance = instance;
	}

	public Document(ActiveXComponent wordApp, Dispatch instance, Dispatch documents) {
		this.wordApp = wordApp;
		this.instance = instance;
		this.documents = documents;
	}

	public void setDocuments(Dispatch documents) {
		this.documents = documents;
	}

	public Dispatch getInstance() {
		return instance;
	}

	/**
	 * 按照参数的类别分别进行渲染
	 * @param key2Param
	 * @return
	 */
	public Document render(Map<String, Object> key2Param) {
		for (String key : key2Param.keySet()) {
			
			if (key2Param.get(key) == null)
				continue;
			
			List<Object> lsParam;

			if (key2Param.get(key) instanceof Collection) {
				if (((Collection<?>)key2Param.get(key)).isEmpty())
					continue;
				lsParam = new ArrayList<>((Collection<?>) key2Param.get(key));
			} else {
				lsParam = new ArrayList<>();
				lsParam.add(key2Param.get(key));
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
	 * 替换默认的
	 */
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
	 * 写入图片
	 * @param lsNBCWordImage
	 * @param isKeyExist
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
		Dispatch dispatch = Dispatch.call(documents, "Open", filePathName).toDispatch();
		return new Document(wordApp, dispatch, documents);
	}

	/**
	 * 复制另一个文档的全部内容
	 */
	public void copyAllFromAnother(Document doc) {
		Dispatch range = Dispatch.get(doc.getInstance(), "Content").toDispatch(); // 取得当前文档的内容  
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
			Dispatch dispatch = Dispatch.call(wordApp, "Selection").toDispatch();
			selection = new Selection(wordApp, this, dispatch);
		}
		return selection;
	}

	/**
	 * 文档另存为
	 * @param filePathName
	 */
	public void saveAs(String filePathName) {
		Dispatch.call(instance, "SaveAs", filePathName, new Variant(0));
	}

	/**
	 * 文档保存
	 */
	public void save() {
		Dispatch.call(instance, "Save");
	}

	/**
	 * 关闭文档
	 */
	public void close() {
		Dispatch.call(instance, "Close", new Variant(false));
	}

}
