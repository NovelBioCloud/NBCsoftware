package com.novelbio.base.word;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

/**
 * 可以同时打开很多个word文档的客户端
 * @author Administrator
 */
public class WordApp{
	// Word应用程序本身
	private ActiveXComponent wordApp;
	
	// Word的文档集合对象
	private Documents documents;
	//主文档
	private Document nowDoc;
	/**
	 * 构造函数
	 * @param visible 是否可见
	 */
	public WordApp(boolean visible) throws Exception{
		initialize(visible);
	}
	
	/**
	 * 打开另一个子文档用来复制内容
	 * @param filePathName
	 * @return
	 * @throws Exception
	 */
	public Document openExistDocument(String filePathName) throws Exception{
		Dispatch d = Dispatch.call(documents.getInstance(), "Open", filePathName).toDispatch();
		nowDoc =new Document(d,wordApp,documents);
		return nowDoc;
	}
	
	/**
	 * 应用程序初始化
	 * @param visible 是否可见
	 */
	private void initialize(boolean visible) throws Exception{
		// 初始化com的线程，使用结束后要调用realease方法，见quit函数
		ComThread.InitSTA();
		
		wordApp=new ActiveXComponent("Word.Application");
		//是否可见word界面
		wordApp.setProperty("Visible", new Variant(visible));
		
		Dispatch d=wordApp.getProperty("Documents").toDispatch();
		documents=new Documents(d);
	}
	
	/**
	 * 应用程序退出
	 * 
	 * 说明：
	 * 创建时间：2011-6-4 下午05:18:56
	 */
	public void quit() throws Exception{
		nowDoc.close();
		wordApp.invoke("Quit", new Variant[]{new Variant(false),new Variant(false)});
		ComThread.Release();
	}
	
	/**
	 * 新建文档,并返回新建文档的句柄
	 * 
	 * 说明：
	 * @return
	 * @throws Exception
	 * 创建时间：2011-6-4 下午05:33:07
	 */
	public Document addNewDocument() throws Exception{
		Dispatch d=Dispatch.call(documents.getInstance(),"Add").toDispatch();
		Document doc=new Document(d,wordApp,documents);
		return doc;
	}
	
	
	/**
	 * 返回当前正在处理的document
	 * @return
	 */
	public Document getNowDocument(){
		return nowDoc;
	}
}