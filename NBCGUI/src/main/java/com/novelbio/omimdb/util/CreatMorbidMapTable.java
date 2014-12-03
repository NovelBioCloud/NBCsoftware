package com.novelbio.omimdb.util;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.novelbio.base.dataOperate.TxtReadandWrite;
import com.novelbio.omimdb.mgmt.MgmtMorbidMap;
import com.novelbio.omimdb.model.MorbidMap;

public class CreatMorbidMapTable {

	public static void creatMorbidMapTable(String inFile, String inGeneIdFile) {
	
		MgmtMorbidMap mgmtMorbidMap = MgmtMorbidMap.getInstance();
		TxtReadandWrite txtMorbidMapRead = new TxtReadandWrite(inFile);
		CreatGenemapTable creatGenemapTable = new CreatGenemapTable();
		Map<String,String> mapGeneID2Name = creatGenemapTable.getGeneID(inGeneIdFile);
		int phenMIMId = 0;
		int geneId = 0;
		for (String content : txtMorbidMapRead.readlines()) {
			MorbidMap morbidMap = new MorbidMap();
			String[] arrMorbidMapLine = content.split("\\|");
			String[] arrGeneName = arrMorbidMapLine[1].split(",");
			if (mapGeneID2Name.containsKey(arrGeneName[0])) {
				geneId = Integer.parseInt(mapGeneID2Name.get(arrGeneName[0]));
			}
			morbidMap.setGeneId(geneId);
			int geneMimId = Integer.parseInt(arrMorbidMapLine[2]);
			morbidMap.setGeneMimId(geneMimId);
			morbidMap.setCytLoc(arrMorbidMapLine[3]);
			String[] arrDisease = arrMorbidMapLine[0].split(",");
			//疾病信息添加到morbidMap的疾病list中
			for (int i = 0; i < arrDisease.length; i++) {
				morbidMap.addDis(arrDisease[i]);
			}
			//以下获取phenotype MIM ID
			String phenInfo = arrDisease[arrDisease.length - 1].trim();
			Pattern pattern = Pattern.compile("^\\d{6}");
			Matcher matcher = pattern.matcher(phenInfo);
			int phenMimId = 0;
			//如果该疾病信息中含有phenotype MIM ID则，提取phenotype MIM ID号，如果没有含有phenotype MIM ID号,则将最后一行疾病信息添加到morbidMap的疾病list中
			if (matcher.find()) {	
				phenMimId = Integer.parseInt(matcher.group());
			}
			morbidMap.setPheneMimId(phenMimId);
			mgmtMorbidMap.save(morbidMap);;
		}
		txtMorbidMapRead.close();
	}
}


