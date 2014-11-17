package com.novelbio.omimdb.util;

import com.mongodb.util.MyAsserts.MyAssert;
import com.novelbio.base.dataOperate.TxtReadandWrite;

public class CreatMIMTable {
	String inFileString;
	public boolean creatMIMTable(String inFileString) {
		
		TxtReadandWrite txtMIMRead = new TxtReadandWrite(inFileString);
		int mIMId;
		String title;
		for (String content : txtMIMRead.readlines()) {
			if (content.startsWith("*")) {
				continue;
			} else if (content.matches("\\d{6}")) {
					mIMId = Integer.parseInt(content);
				} else if(content.matches("[#+%]\\d.*?$")) {
					title = content.substring(7);
//					if (content.) {
//						
//					}
				} else if(content.matches("\\d.*?$")){
					
				}
		}
		
		return true;
	}
	public String getInFileString() {
		return inFileString;
	}
	private void setInFileString(String inFileString) {
		this.inFileString = inFileString;
	}
}
