package com.novelbio.omimdb.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mongodb.util.MyAsserts.MyAssert;
import com.novelbio.base.dataOperate.TxtReadandWrite;
import com.novelbio.omimdb.mgmt.MgmtGeneMIMInfo;
import com.novelbio.omimdb.mgmt.MgmtOMIM;
import com.novelbio.omimdb.mgmt.MgmtOMIMUnit;
import com.novelbio.omimdb.model.MIMAllToUni;
import com.novelbio.omimdb.model.MIMInfo;

public class CreatMIMTable {
	String inFileString;
	public void creatMIMTable() {
		TxtReadandWrite txtMIMRead = new TxtReadandWrite(inFileString);
		List<String> lsOmimUnit = new ArrayList<String>();
		MgmtOMIMUnit mgmtOMIMUnit =MgmtOMIMUnit.getInstance();
		for (String content : txtMIMRead.readlines()) {
			//TODO 填充lsOmimUnit
			if(content.startsWith("*RECORD*")) {
//				MIMAllToUni mimAllToUni = MIMAllToUni.getInstanceFromOmimUnit(lsOmimunit);
				MIMInfo mIMInfo = MIMInfo.getInstanceFromOmimUnit(lsOmimUnit);
//				if (mIMInfo != null) {
//					mgmtOMIMUnit.save(mIMInfo);
//				}
				lsOmimUnit.clear();
			}
			lsOmimUnit.add(content);
		}
		
//		MIMInfo mIMInfo = MIMInfo.getInstanceFromOmimUnit(lsOmimUnit);
//		mgmtOMIMUnit.save(mIMInfo);
		
	}
	public String getInFileString() {
		return inFileString;
	}
	public void setInFileString(String inFileString) {
		this.inFileString = inFileString;
	}
}




