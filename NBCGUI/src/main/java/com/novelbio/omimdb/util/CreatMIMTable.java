package com.novelbio.omimdb.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mongodb.util.MyAsserts.MyAssert;
import com.novelbio.base.dataOperate.TxtReadandWrite;
import com.novelbio.database.domain.omim.MIMAllToUni;
import com.novelbio.database.domain.omim.MIMInfo;
import com.novelbio.database.model.modomim.MgmtGeneMIMInfo;
import com.novelbio.database.model.modomim.MgmtMIMAllToUni;
import com.novelbio.database.model.modomim.MgmtOMIM;
import com.novelbio.database.model.modomim.MgmtOMIMUnit;

public class CreatMIMTable {
	String inFile;
	public boolean creatMIMTable(String inFile) {
		TxtReadandWrite txtMIMRead = new TxtReadandWrite(inFile);
		List<String> lsOmimUnit = new ArrayList<String>();
		MgmtOMIMUnit mgmtOMIMUnit =MgmtOMIMUnit.getInstance();
		MgmtMIMAllToUni mgmtMIMAllToUni = MgmtMIMAllToUni.getInstance();
		for (String content : txtMIMRead.readlines()) {
			if(content.startsWith("*RECORD*")) {
				MIMAllToUni mimAllToUni = MIMAllToUni.getInstanceFromOmimUnit(lsOmimUnit);
				MIMInfo mIMInfo = MIMInfo.getInstanceFromOmimUnit(lsOmimUnit);
				if ((mIMInfo != null) && (mIMInfo.getMimId() != 0) && (mIMInfo.getType() != '^')) {
					mgmtOMIMUnit.save(mIMInfo);
				}
				if ((mimAllToUni != null) && (mimAllToUni.getAllMIMId() != 0)) {
					mgmtMIMAllToUni.save(mimAllToUni);
				}
				lsOmimUnit.clear();
			}
			lsOmimUnit.add(content);
		}
		
		
		MIMInfo mIMInfo = MIMInfo.getInstanceFromOmimUnit(lsOmimUnit);
		if ((mIMInfo != null) && (mIMInfo.getMimId() != 0)) {
			mgmtOMIMUnit.save(mIMInfo);
		}
		
		MIMAllToUni mimAllToUni = MIMAllToUni.getInstanceFromOmimUnit(lsOmimUnit);
		if ((mimAllToUni != null) && (mimAllToUni.getAllMIMId() != 0)) {
			mgmtMIMAllToUni.save(mimAllToUni);
		}
		return true;
	}
	public String getInFile() {
		return inFile;
	}
	public void setInFile(String inFile) {
		this.inFile = inFile;
	}
}




