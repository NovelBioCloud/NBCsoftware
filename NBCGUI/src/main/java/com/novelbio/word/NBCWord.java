package com.novelbio.word;

import org.apache.log4j.Logger;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.novelbio.nbcReport.Params.ReportBase;

/**
 * 整个NBCWord及其相关类，就是一个操作dispatch的过程。dispatch类似一个操作wrod的接口，
 * 所有打开，修改，删除等工作都会通过dispatch与word(实际上应该是com组件)进行交互。
 * @author Administrator
 *
 */
public class NBCWord {
	public static void main(String[] args) {
		ActiveXComponent wordApp;
		wordApp = new ActiveXComponent("Word.Application");
		//Visible 设置是否在桌面打开一个word档
		wordApp.setProperty("Visible", new Variant(true));
		
		Dispatch dispatchWord = wordApp.getProperty("Documents").toDispatch();
		
		Dispatch dispatchWord2 = Dispatch.call(dispatchWord, "Open", "C:\\Documents and Settings\\Administrator\\桌面\\GOAnalysis_result.doc").toDispatch();
		
		Dispatch selected = Dispatch.call(dispatchWord, "Selection").toDispatch();
		Dispatch.call(selected, "TypeText", "fserfsefsefrse");
	}
	
	public static Logger logger = Logger.getLogger(NBCWord.class);
	
	/**word应用程序，最根本的控制word的组件 */
	private ActiveXComponent wordApp;
	
	/** 专门用于打开word的dispatch，该dispatchWord是wordApp其中某个属性
	 * 的一个地址引用，调用Dispatch.call来修改该 dispatchWord的open文件 会将wordApp指向该word文档 */
	private Dispatch dispatchWord;

	private Dispatch currentDis;
	/**当前的文档*/
	private Document nowDoc;
	
	public NBCWord(String path) {
		initWordApp();
		if(path == null) {
			addNewDocument();
		} else {
			openExistDocument(path);
		}
	}
	
	/**
	 * 初始化word，即产生 {@link #dispatchWord}
	 */
	private void initWordApp() {
		ComThread.InitSTA();
		wordApp = new ActiveXComponent("Word.Application");
		//Visible 设置是否在桌面打开一个word档
		wordApp.setProperty("Visible", new Variant(true));
		dispatchWord = wordApp.getProperty("Documents").toDispatch();
	}
	
	/** 新建空文档 */
	private void addNewDocument() {
		currentDis = Dispatch.call(dispatchWord, "Add").toDispatch();
		nowDoc = new Document(wordApp, currentDis);
	}
	
	/** 打开现有文档 */
	private void openExistDocument(String path) {
		currentDis = Dispatch.call(dispatchWord, "Open", path).toDispatch();
		nowDoc = new Document(wordApp, currentDis);
	}

	/** 渲染报告 */
	public void renderReport(ReportBase reportBase){
		nowDoc.render(reportBase.buildFinalParamMap());
	}
	
	/**
	 * 文档另存为
	 * @param filePathName
	 */
	public void saveAs(String filePathName) {
		Dispatch.call(currentDis, "SaveAs", filePathName, new Variant(0));
	}

	/**
	 * 文档保存
	 */
	public void save() {
		Dispatch.call(currentDis, "Save");
	}

	/**
	 * 退出word，在之前要保存
	 */
	public void quit() {
		Dispatch.call(currentDis, "Close", new Variant(false));
		wordApp.invoke("Quit", new Variant[]{new Variant(false),new Variant(false)});
		ComThread.Release();
	}
	
	/** 获得该文档的全部内容，以dispatch的形式返回，可用于复制到另一个文档中 */
	public Dispatch getAllContent() {
		
	}
	
	/**
	 * 打开另一个文档用来复制
	 * @param filePathName
	 * @return 
	 */
	public Dispatch openDocumentForCopy(String filePathName) {
		return Dispatch.call(dispatchWord, "Open", filePathName).toDispatch();
	}
	
	/**
	 * 复制另一个文档的全部内容
	 */
	public void copyAllFromAnother(Dispatch dispatchDoc) {
		Dispatch range = Dispatch.get(dispatchDoc, "Content").toDispatch(); // 取得当前文档的内容  
		Dispatch.call(range, "Copy");
		Dispatch textRange = Dispatch.get(dispatchDoc, "Range").toDispatch();  
		Dispatch.call(textRange, "Paste");
		Dispatch.call(dispatchDoc, "Close", new Variant(false));
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
