package com.novelbio.word;

import java.util.Map;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
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
	 * 渲染报告
	 * @param param
	 * @return
	 */
	public Document render(Map<String, NBCWordParam> param) {
		// TODO 未完成
		for(String key : param.keySet()) {
			if(param.get(key) == null) {
				continue;
			}
			NBCWordParam nbcWordParam = param.get(key);
			nbcWordParam.insertToDoc(this, key);
		}
		return this;
	}
	
	/**
	 * 打开另一个文档用来复制
	 * @param filePathName
	 */
	public void openDocumentForCopy(String filePathName) {
		Dispatch dispatch = Dispatch.call(documents, "Open", filePathName).toDispatch();
		anotherDoc =new Document(wordApp, dispatch, documents);
	}
	
	/**
	 * 复制另一个文档的全部内容
	 */
	public void copyAllFromAnother() {
		Dispatch range = Dispatch.get(anotherDoc.getInstance(), "Content").toDispatch(); // 取得当前文档的内容  
	    Dispatch.call(range, "Copy");
	    Dispatch textRange = Dispatch.get(getSelection().getInstance(), "Range").toDispatch();  
        Dispatch.call(textRange, "Paste");
        anotherDoc.close();
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
		Dispatch.call(instance, "SaveAs", filePathName,new Variant(0));
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
