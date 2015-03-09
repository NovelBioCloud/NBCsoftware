package test;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class TestWord {
	
	public static void main(String[] args) {
		
//		ActiveXComponent wordApp;
//		wordApp = new ActiveXComponent("Word.Application");
//		wordApp.setProperty("Visible", new Variant(true));
//		
//		ActiveXComponent wordApp2 = new ActiveXComponent("Word.Application");
//		wordApp2.setProperty("Visible", new Variant(true));
//		
//		Dispatch dispatchWord = wordApp.getProperty("Documents").toDispatch();
//		Dispatch dispatchWord2 = wordApp2.getProperty("Documents").toDispatch();
//		
//		Dispatch dispatch1 = Dispatch.call(dispatchWord, "Open", "C:\\Documents and Settings\\Administrator\\桌面\\a.doc").toDispatch();
//		Dispatch dispatch2 = Dispatch.call(dispatchWord, "Open", "C:\\Documents and Settings\\Administrator\\桌面\\b.doc").toDispatch();
//		Dispatch dispatch3 = Dispatch.call(dispatchWord2, "Open", "C:\\Documents and Settings\\Administrator\\桌面\\c.doc").toDispatch();
//		
//		Dispatch selected = Dispatch.call(wordApp, "Selection").toDispatch();
//		Dispatch selected2 = Dispatch.call(wordApp2, "Selection").toDispatch();
//		
//		TestWord test = new TestWord();
//		test.copy(dispatch2, selected2);
		String path1 = "C:\\Documents and Settings\\Administrator\\桌面\\a.doc";
		String path2 = "C:\\Documents and Settings\\Administrator\\桌面\\b.doc";
		String path3 = "C:\\Documents and Settings\\Administrator\\桌面\\c.doc";
		String path4 = "C:\\Documents and Settings\\Administrator\\桌面\\d.doc";
		String pathTo1 = "C:\\Documents and Settings\\Administrator\\桌面\\e.doc";
		String pathTo2 = "C:\\Documents and Settings\\Administrator\\桌面\\f.doc";
		String pathTo3 = "C:\\Documents and Settings\\Administrator\\桌面\\g.doc";
		String pathTo4 = "C:\\Documents and Settings\\Administrator\\桌面\\h.doc";
		Thread thread1 = new Thread(new WordTest(path1, pathTo1));
		Thread thread2 = new Thread(new WordTest(path2, pathTo2));
//		Thread thread3 = new Thread(new WordTest(path3, pathTo3));
//		Thread thread4 = new Thread(new WordTest(path4, pathTo4));
		thread1.start();
		thread2.start();
		
	}
	
	public void copy(Dispatch dispatchDoc, Dispatch selection) {
		Dispatch range = Dispatch.get(dispatchDoc, "Content").toDispatch(); // 取得当前文档的内容  
		Dispatch.call(range, "Copy");
		Dispatch textRange = Dispatch.get(selection, "Range").toDispatch();  
		Dispatch.call(textRange, "Paste");
	}

}


class WordTest implements Runnable {
	ActiveXComponent wordApp = new ActiveXComponent("Word.Application");
	
	Dispatch dispatchWord = wordApp.getProperty("Documents").toDispatch();
	Dispatch dispatch;
	
	ActiveXComponent wordAppCopyTo = new ActiveXComponent("Word.Application");
	Dispatch dispatchWordCopyTo = wordAppCopyTo.getProperty("Documents").toDispatch();
	Dispatch dispatchCopyTo;
	
	public WordTest(String pathForm, String pathTo) {
		wordApp.setProperty("Visible", new Variant(true));
		dispatch = Dispatch.call(dispatchWord, "Open", pathForm).toDispatch();
		wordAppCopyTo.setProperty("Visible", new Variant(true));
		dispatchCopyTo = Dispatch.call(dispatchWordCopyTo, "Open", pathTo).toDispatch();
	}
	@Override
	public void run() {
		for (int i = 0; i < 10; i++) {
			Dispatch range = Dispatch.get(dispatch, "Content").toDispatch(); // 取得当前文档的内容  
			Dispatch.call(range, "Copy");
			
			Dispatch copyTo = Dispatch.call(wordAppCopyTo, "Selection").toDispatch();
		
			Dispatch textRange = Dispatch.get(copyTo, "Range").toDispatch();  
			Dispatch.call(textRange, "Paste");
		}
		
		
		
		
		
		
		
	}
	
}