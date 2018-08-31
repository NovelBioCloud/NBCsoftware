package test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.novelbio.bioinfo.sam.SamFile;

public class TestWord {
	private static final Logger logger = LoggerFactory.getLogger(TestWord.class);
	public static void main(String[] args) {
		
		SamFile samFile = new SamFile("/media/winE/test/RC5.bam");
		samFile.sort();
		
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