package com.novelbio.word;

import org.apache.log4j.Logger;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class NBCWord {
	
	public static Logger logger = Logger.getLogger(NBCWord.class);
	
	private ActiveXComponent nbcWord;
	private Dispatch documents;
	private Document document;
	
	public NBCWord(String path) {
		ComThread.InitSTA();
		nbcWord = new ActiveXComponent("Word.Application");
		nbcWord.setProperty("Visible", new Variant(true));
		documents = nbcWord.getProperty("Documents").toDispatch();
	}

}
