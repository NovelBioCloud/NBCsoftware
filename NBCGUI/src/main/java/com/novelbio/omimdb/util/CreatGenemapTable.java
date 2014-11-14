package com.novelbio.omimdb.util;

import java.util.ArrayList;
import java.util.List;

import org.freehep.graphicsio.swf.SWFAction.Push;

import com.lowagie.text.SplitCharacter;
import com.mongodb.util.MyAsserts.MyAssert;
import com.novelbio.base.SepSign;
import com.novelbio.base.dataOperate.TxtReadandWrite;
import com.novelbio.database.domain.geneanno.GeneInfo;
import com.novelbio.database.model.modgeneid.GeneID;
import com.novelbio.omimdb.mgmt.MgmtGeneMIMInfo;
import com.novelbio.omimdb.mgmt.MgmtOMIM;
import com.novelbio.omimdb.model.GeneMIM;
import com.novelbio.omimdb.model.OmimGeneMap;

public class CreatGenemapTable {
	String inFileString;
	
	public boolean creatGenemapTable(String inFileString) {
		String recodData;
		String[] arrAllComment = null;
		String[] arrComment;
		String phenDec;
		String comment;
		int phenMimId;
		int geneMimId;
		List<String> lsPhenToGeneMIM = new ArrayList<>();
		String phenToGene;
		TxtReadandWrite txtGenemapRead = new TxtReadandWrite(inFileString);
		MgmtOMIM mgmtOMIM = MgmtOMIM.getInstance();
		MgmtGeneMIMInfo mgmtGeneMIM = MgmtGeneMIMInfo.getInstance();
		
		for (String content : txtGenemapRead.readlines()) {
			String[] arrGenemap = content.split("\t");
			recodData = arrGenemap[3] + "-" +  arrGenemap[1] + "-" +arrGenemap[2];	
			geneMimId = Integer.parseInt(arrGenemap[8]); 
			if ((arrGenemap.length >= 12) && (!arrGenemap[11].equals(""))) {
//							
//				
//				phenMimId = geneMimId;
//				if (arrGenemap[11].indexOf(";")>-1) {
//					arrAllComment = arrGenemap[11].split(";");
//				}else{	
//					arrAllComment = new String[] {arrGenemap[11]};
//				}
//				for (String comInf : arrAllComment) {
//					arrComment = comInf.split(",");
//					comment = arrComment[arrComment.length-1].trim().replace("\"", "");
//					if (comment.matches("\\d{6}\\s+\\(\\d+\\)")) {
//						phenMimId = Integer.parseInt(comment.substring(0, 6)); 
//					}
//					phenToGene = phenMimId + "_" + geneMimId;
//					
//					if (!lsPhenToGeneMIM.contains(phenToGene)) {
//						OmimGeneMap omimGeneMap =new OmimGeneMap();
//						omimGeneMap.setGenMimId(geneMimId);
//						omimGeneMap.setPhenMimId(phenMimId);
//						omimGeneMap.setRecordTime(recodData);
////						omimGeneMap.setPhenDec(arrGenemap[7]);
//						omimGeneMap.setPhenMapMeth(arrGenemap[9]);
//						if (arrGenemap.length>12) {
//							omimGeneMap.setMouCorr(arrGenemap[12]);
//						}
//						mgmtOMIM.save(omimGeneMap);
//					}
//					lsPhenToGeneMIM.add(phenToGene);
//				}		
			}
			if (arrGenemap.length>8) {
				GeneMIM geneMIM =new GeneMIM();
				geneMIM.setGeneMimId(geneMimId);
				
				
//				geneMIM.setGeneId(geneId);
				geneMIM.setMapGenMet(arrGenemap[6]);
				geneMIM.setCytLoc(arrGenemap[4]);
				mgmtGeneMIM.save(geneMIM);
			}		
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
