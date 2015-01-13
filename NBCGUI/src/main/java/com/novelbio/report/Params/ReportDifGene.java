package com.novelbio.report.Params;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.novelbio.generalConf.TitleFormatNBC;

public class ReportDifGene  extends ReportBase{
	private static final long serialVersionUID = -5594189660605968860L;
	
	public void setAlgorithm(String algorithm) {
		mapKey2Param.put("algorithm", algorithm);
	}
	
	public void setDifExpNum(int difExpNum) {
		mapKey2Param.put("difExpNum", difExpNum);
	}
	
	public void setUpDownGeneNum(int upGeneNum, int downGeneNum) {
		mapKey2Param.put("upGeneNum", upGeneNum);
		mapKey2Param.put("downGeneNum", downGeneNum);
	}
	
	public void setpValueOrFDR(TitleFormatNBC titleFormatNBC, double value) {
		String pValueOrFDR = titleFormatNBC + "<" + value;
		mapKey2Param.put("pValueOrFDR", pValueOrFDR);
	}
	
	/** 组名 */
	public void setCaseVSContorl(String caseVSContorl) {
		mapKey2Param.put("caseVSControl", caseVSContorl);
	}
	
	/**
	 * 添加组名和对应的基因数量，把一条数据（mapKey2GroupOrGeneNum）添加到报告所有参数集合中去
	 * @param mapKey2GroupOrGeneNum key对应的是参数名称（如caseVSControl，difExpNum，upGeneNum，downGeneNum ）GroupOrGeneNum为对应的值
	 */
	public void addMapGroupAndGeneNum(Map<String, Object> mapKey2GroupOrGeneNum) {
		List<Map<String, Object>> lsGroupAndGeneNum = null;
		if (mapKey2Param.containsKey("lsGroupAndGeneNum")) {
			lsGroupAndGeneNum = (List<Map<String, Object>>) mapKey2Param.get("lsGroupAndGeneNum");
		} else {
			lsGroupAndGeneNum = new ArrayList<Map<String,Object>>();
		}
		lsGroupAndGeneNum.add(mapKey2GroupOrGeneNum);
		mapKey2Param.put("lsGroupAndGeneNum", lsGroupAndGeneNum);
	}

	@Override
	public EnumReport getEnumReport() {
		return EnumReport.DiffExp;
	}

}
