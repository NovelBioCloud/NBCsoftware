package com.novelbio.report.Params;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.novelbio.database.domain.geneanno.GOtype;

public class ReportGOResult extends ReportBase {
	private static final long serialVersionUID = 1L;
	
	int geneNum;
	
	public void setGeneNum(int geneNum) {
		this.geneNum = geneNum;
	}
	public int getGeneNum() {
		return geneNum;
	}
	
	/**
	 * @param goType
	 * @param termRank 第几个显著的go
	 * @param goTermDetail 具体的go名字
	 * @param geneNum 该go所拥有的条目
	 */
	public void addGoTerm(GOtype goType, String goTermDetail, int geneNum, double pvalue) {
		// TODO 如果为更多时，把goterm放在一个list中
		//key为参数名称，GOTermParam是对应的参数的值
		Map<String, Object> mapKey2GOTermParam = new HashMap<String, Object>();
		mapKey2GOTermParam.put("detail", goTermDetail);
		mapKey2GOTermParam.put("num", geneNum);
		mapKey2GOTermParam.put("pValue", pvalue);
		if (goType == GOtype.BP) {
			addGOTerm("lsGOTermBP", mapKey2GOTermParam);
		} else if (goType == GOtype.CC) {
			addGOTerm("lsGOTermCC", mapKey2GOTermParam);
		} else if (goType == GOtype.MF) {
			addGOTerm("lsGOTermMF", mapKey2GOTermParam);
		}
	}
	
	private void addGOTerm(String paramName, Map<String, Object> mapKey2GOTermParam) {
		List<Map<String, Object>> lsMapKey2GOTermParam = null;
		if (mapKey2Param.containsKey(paramName)) {
			lsMapKey2GOTermParam = (List<Map<String, Object>>) mapKey2Param.get(paramName);
		} else {
			lsMapKey2GOTermParam =  new ArrayList<Map<String,Object>>();
		}
		lsMapKey2GOTermParam.add(mapKey2GOTermParam);
		mapKey2Param.put(paramName, lsMapKey2GOTermParam);
	}

	@Override
	public EnumTaskReport getEnumReport() {
		return EnumTaskReport.GO_Result;
	}

}
