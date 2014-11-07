package com.novelbio.omimdb.util;

import com.lowagie.text.SplitCharacter;
import com.mongodb.util.MyAsserts.MyAssert;
import com.novelbio.base.SepSign;
import com.novelbio.base.dataOperate.TxtReadandWrite;
import com.novelbio.omimdb.mgmt.MgmtOMIM;
import com.novelbio.omimdb.model.OmimGeneMap;

public class CreatGenemapTable {
	//String inFileString = "D:\\OMIM\\genemap2_test.txt";
	String inFileString;
	
	public boolean creatGenemapTable(String inFileString) {
		String recodData;
		String[] arrComment;
		String tmp;
		int phenMimId;
		int geneMimId;
		TxtReadandWrite txtGenemapRead = new TxtReadandWrite(inFileString);
		OmimGeneMap omimGeneMap =new OmimGeneMap();

		
	//	MgmtOMIM mgmtOMIM = MgmtOMIM.getInstance();
		for (String content : txtGenemapRead.readlines()) {
			String arrGenemap[] = content.split("\t");
			
			if (arrGenemap.length > 11) {
				recodData = arrGenemap[3] + "-" +  arrGenemap[1] + "-" +arrGenemap[2];				
				geneMimId = Integer.getInteger(arrGenemap[8]); 
//				omimGeneMap.setGenMimId(Integer.geneMimId);
//				omimGeneMap.setPhenDec(arrGenemap[7]);
//				omimGeneMap.setPhenMapMeth(arrGenemap[9]);
				omimGeneMap.setRecordTime(recodData);
				phenMimId = geneMimId;
				System.out.println("arrGenemap 11  is " + arrGenemap[11]);
				if (!arrGenemap[11].equals("")) {
					arrComment = arrGenemap[11].split(",");
					tmp = arrComment[arrComment.length-1].trim().replace("\"", "");
					if (tmp.matches("\\d+\\s+\\(\\d+\\)")) {
						String testphenMimId =  tmp.substring(0, 6);
						phenMimId = Integer.parseInt(testphenMimId); 
					}
				}		
				omimGeneMap.setMouCorr(arrGenemap[10]);
			}
			//mgmtOMIM.save(omimGeneMap);
		}
		txtGenemapRead.close();
		return true;
	}
	public String getInFileString() {
		return inFileString;
	}
	private void setInFileString(String inFileString) {
		this.inFileString = inFileString;
	}
}
