package com.novelbio.omimdb.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.tools.ant.taskdefs.Exit;

import com.novelbio.base.dataOperate.TxtReadandWrite;
import com.novelbio.omimdb.mgmt.MgmtGeneMIMInfo;
import com.novelbio.omimdb.mgmt.MgmtOMIM;
import com.novelbio.omimdb.model.GeneMIM;
import com.novelbio.omimdb.model.OmimGeneMap;

public class CreatGenemapTable {
//	String inGeneIdFile;
	public boolean creatGenemapTable(String inFileString, String inGeneIdFile) {
		String recodData;
		String[] arrAllComment = null;
		String[] arrComment;
		String phenDec;
		String comment;
		int phenMimId;
		int geneMimId;
		List<String> lsPhenToGeneMIM = new ArrayList<>();
		String phenToGene;
		String geneName;
		String geneId;
		Map<String,String> mapGeneID2Name = CreatGenemapTable.getGeneID(inGeneIdFile);
		Map<String,String> mapOmimType = CreatGenemapTable.getOmimType(inGeneIdFile);
		TxtReadandWrite txtGenemapRead = new TxtReadandWrite(inFileString);
		MgmtOMIM mgmtOMIM = MgmtOMIM.getInstance();
		MgmtGeneMIMInfo mgmtGeneMIM = MgmtGeneMIMInfo.getInstance();
		
		for (String content : txtGenemapRead.readlines()) {
			String[] arrGenemap = content.split("\t");
			recodData = arrGenemap[3] + "-" +  arrGenemap[1] + "-" +arrGenemap[2];	
			geneMimId = Integer.parseInt(arrGenemap[8]); 
			if ((arrGenemap.length >= 12) && (!arrGenemap[11].equals(""))) {
		
				phenMimId = geneMimId;
				if (arrGenemap[11].indexOf(";")>-1) {
					arrAllComment = arrGenemap[11].split(";");
				}else{	
					arrAllComment = new String[] {arrGenemap[11]};
				}
				for (String comInf : arrAllComment) {
					arrComment = comInf.split(",");
					comment = arrComment[arrComment.length-1].trim().replace("\"", "");
					if (comment.matches("\\d{6}\\s+\\(\\d+\\)")) {
						phenMimId = Integer.parseInt(comment.substring(0, 6)); 
					}
					phenToGene = phenMimId + "_" + geneMimId;
					
					if (!lsPhenToGeneMIM.contains(phenToGene)) {
						OmimGeneMap omimGeneMap =new OmimGeneMap();
						omimGeneMap.setGenMimId(geneMimId);
						omimGeneMap.setPhenMimId(phenMimId);
						omimGeneMap.setRecordTime(recodData);
						omimGeneMap.setPhenDec(arrGenemap[7].replace("\"", ""));
						String testString = omimGeneMap.getPhenDec();
						String[] arrMeth = arrGenemap[9].replace("\"", "").split(",");
						for (String meth : arrMeth) {
							omimGeneMap.addPhenMapMeth(meth);
						}
						if (arrGenemap.length>12) {
							omimGeneMap.setMouCorr(arrGenemap[12]);
						}
						mgmtOMIM.save(omimGeneMap);
					}
					lsPhenToGeneMIM.add(phenToGene);
				}		
			}
			if (arrGenemap.length>8) {
				GeneMIM geneMIM =new GeneMIM();
				geneMIM.setGeneMimId(geneMimId);
				geneName = arrGenemap[5].split(",")[0].replaceAll("\"", "");
				if (mapGeneID2Name.containsKey(geneName)) {
					geneId = mapGeneID2Name.get(geneName);
				} else {
					geneId = "0";
				}
				geneMIM.setGeneId(Integer.parseInt(geneId));
				geneMIM.setMapGenMet(arrGenemap[6]);
				geneMIM.setCytLoc(arrGenemap[4]);
				mgmtGeneMIM.save(geneMIM);
			}		
		}
		txtGenemapRead.close();
		return true;
	}
	
	public static Map<String,String> getGeneID (String inGeneIdFile) {
		Map<String,String> mapGeneName2ID = new HashMap<String, String>();
		TxtReadandWrite txtGeneIdFileRead = new TxtReadandWrite(inGeneIdFile);
		for (String line : txtGeneIdFileRead.readlines()) {
			String[] lineInfo = line.split("\\s+");
//			if (lineInfo[1].startsWith("gene")) {
				if (lineInfo[2].matches("\\d+")) {
					mapGeneName2ID.put(lineInfo[3], lineInfo[2]);
//					System.out.println(lineInfo[3] +"  === " + lineInfo[2]);
				}
				
//			}
		}
		txtGeneIdFileRead.close();
		return mapGeneName2ID;
	}
	public static Map<String,String> getOmimType (String inGeneIdFile) {
		Map<String,String> mapOmimType = new HashMap<String, String>();
		TxtReadandWrite txtGeneIdFileRead = new TxtReadandWrite(inGeneIdFile);
		for (String line : txtGeneIdFileRead.readlines()) {
			String[] lineInfo = line.split("\\t+");
			mapOmimType.put(lineInfo[0], lineInfo[1]);		
		}
		txtGeneIdFileRead.close();
		return mapOmimType;
	}
	
}
