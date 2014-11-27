package com.novelbio.base.word;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;


/**
 * 文档对象
 * 说明：
 * 作者：何杨(heyang78@gmail.com)
 * 创建时间：2011-6-4 下午05:41:47
 * 修改时间：2011-6-4 下午05:41:47
 */
public class Document {
	private ActiveXComponent wordApp;
	private Documents documents;
	private Document anotherDoc;
	private Selection selection;
	private Dispatch instance;
//	public Document(Dispatch instance,ActiveXComponent wordApp,Documents documents) {
//		super(instance);
//		this.documents = documents;
//		this.wordApp = wordApp;
//	}
	
	public Document(Dispatch instance,ActiveXComponent wordApp,Documents documents) {
		this.instance = instance;
		this.documents = documents;
		this.wordApp = wordApp;
	}

	public Dispatch getInstance() {
		return instance;
	}

	public void setInstance(Dispatch instance) {
		this.instance = instance;
	}

	/**
	 * 文档另存为
	 * 
	 * 说明：
	 * @param filePathName
	 * @throws Exception
	 * 创建时间：2011-6-4 下午05:42:42
	 */
	public void saveAs(String filePathName) throws Exception{
		Dispatch.call(instance, "SaveAs", filePathName,new Variant(0));
	}
	/**
	 * 打开另一个子文档用来复制内容
	 * @param filePathName
	 * @return
	 * @throws Exception
	 */
	public Document openDocumentForCopy(String filePathName) throws Exception{
		Dispatch d = Dispatch.call(documents.getInstance(), "Open", filePathName).toDispatch();
		anotherDoc =new Document(d,wordApp,documents);
		return anotherDoc;
	}
	
	/**
	 * 复制另一个doc中的全部内容到当前doc的selection中然后关闭这个doc不保存
	 * @param selection
	 * @throws Exception 
	 */
	public void copyAllFromAnother() throws Exception{
		Dispatch range = Dispatch.get(anotherDoc.getInstance(), "Content").toDispatch(); // 取得当前文档的内容  
	    Dispatch.call(range, "Copy");
	    Dispatch textRange = Dispatch.get(getSelection().getInstance(), "Range")  
                .toDispatch();  
        Dispatch.call(textRange, "Paste");
        anotherDoc.close();
	}
	
	public void save() throws Exception{
		Dispatch.call(instance, "Save");
	}
	/**
	 * 得到当前选择的文字
	 * 
	 * 说明：
	 * @return
	 * @throws Exception
	 * 创建时间：2011-6-4 下午05:38:28
	 */
	public Selection getSelection() throws Exception{
		if(selection == null){
			Dispatch d= Dispatch.call(wordApp,"Selection").toDispatch();
			selection= new Selection(d,wordApp,instance);
		}
		return selection;
	}
	/**
	 * 关闭而不保存文档，想要保存自己调用保存方法
	 * 
	 * 说明：
	 * @throws Exception
	 * 创建时间：2011-6-4 下午05:43:52
	 */
	public void close() throws Exception{
		Dispatch.call(instance, "Close", new Variant(false));
	}
	
	/**
	 * 设置页眉的文字
	 * 
	 * 说明：
	 * @param headerText
	 * @throws Exception
	 * 创建时间：2011-6-4 下午07:22:37
	 */
	public void setHeaderText(String headerText,Dispatch selection) throws Exception{
		Dispatch activeWindow = Dispatch.get(instance, "ActiveWindow").toDispatch();
		Dispatch view = Dispatch.get(activeWindow, "View").toDispatch();
		Dispatch.put(view, "SeekView", new Variant(9)); //wdSeekCurrentPageHeader-9

		Dispatch headerFooter = Dispatch.get(selection, "HeaderFooter").toDispatch();
		Dispatch range = Dispatch.get(headerFooter, "Range").toDispatch();
		Dispatch.put(range, "Text", new Variant(headerText));
		Dispatch font = Dispatch.get(range, "Font").toDispatch();

		Dispatch.put(font, "Name", new Variant("楷体_GB2312"));
		Dispatch.put(font, "Bold", new Variant(true));
		Dispatch.put(font, "Size", 9);

		Dispatch.put(view, "SeekView", new Variant(0)); //wdSeekMainDocument-0恢复视图;
	}
	
	/**
	 * 设置图片水印
	 * 
	 * 说明：
	 * @param imagePath
	 * @param selection
	 * @throws Exception
	 * 创建时间：2011-6-4 下午07:48:53
	 */
	public void setImageWaterMark(String imagePath,Dispatch selection) throws Exception{
		Dispatch activeWindow = Dispatch.get(instance, "ActiveWindow").toDispatch();
		Dispatch view = Dispatch.get(activeWindow, "View").toDispatch();
		Dispatch.put(view, "SeekView", new Variant(9)); //wdSeekCurrentPageHeader-9

		Dispatch headerFooter = Dispatch.get(selection, "HeaderFooter").toDispatch();
		
		// 获取水印图形对象
		Dispatch shapes=Dispatch.get(headerFooter, "Shapes").toDispatch();
		
		Dispatch picture=Dispatch.call(shapes, "AddPicture",imagePath).toDispatch();
		
		Dispatch.call(picture, "Select");
		Dispatch.put(picture,"Left",new Variant(10));
		Dispatch.put(picture,"Top",new Variant(10));
		Dispatch.put(picture,"Width",new Variant(190));
		Dispatch.put(picture,"Height",new Variant(190));
		
		Dispatch.put(view, "SeekView", new Variant(0)); //wdSeekMainDocument-0恢复视图;
	}
	
	/**
	 * 设置图片水印
	 * 
	 * 说明：
	 * @param imagePath
	 * @param selection
	 * @param left
	 * @param top
	 * @param width
	 * @param height
	 * @throws Exception
	 * 创建时间：2011-6-4 下午08:00:16
	 */
	public void setImageWaterMark(String imagePath,Dispatch selection,int left,int top,int width,int height) throws Exception{
		Dispatch activeWindow = Dispatch.get(instance, "ActiveWindow").toDispatch();
		Dispatch view = Dispatch.get(activeWindow, "View").toDispatch();
		Dispatch.put(view, "SeekView", new Variant(9)); //wdSeekCurrentPageHeader-9

		Dispatch headerFooter = Dispatch.get(selection, "HeaderFooter").toDispatch();
		
		// 获取水印图形对象
		Dispatch shapes=Dispatch.get(headerFooter, "Shapes").toDispatch();
		
		Dispatch picture=Dispatch.call(shapes, "AddPicture",imagePath).toDispatch();
		
		Dispatch.call(picture, "Select");
		Dispatch.put(picture,"Left",new Variant(left));
		Dispatch.put(picture,"Top",new Variant(top));
		Dispatch.put(picture,"Width",new Variant(width));
		Dispatch.put(picture,"Height",new Variant(height));
		
		Dispatch.put(view, "SeekView", new Variant(0)); //wdSeekMainDocument-0恢复视图;
	}
	
	/**
	 * 给文档加上保护
	 * 
	 * 说明：
	 * @param pswd
	 * @throws Exception
	 * 创建时间：2011-6-4 下午07:33:44
	 */
	public void setProtected(String pswd) throws Exception{
		String protectionType = Dispatch.get(instance, "ProtectionType").toString();
		if(protectionType.equals("-1")){
			Dispatch.call(instance, "Protect", new Variant(3), new Variant(true), pswd);
		} 
	}
	
	/**
	 * 给文档解除保护
	 * 
	 * 说明：
	 * @param pswd
	 * @throws Exception
	 * 创建时间：2011-6-4 下午07:38:04
	 */
	public void releaseProtect(String pswd) throws Exception{
		String protectionType = Dispatch.get(instance, "ProtectionType").toString();
		if(protectionType.equals("3")){
			Dispatch.call(instance, "Unprotect", pswd);
		}
	}
}