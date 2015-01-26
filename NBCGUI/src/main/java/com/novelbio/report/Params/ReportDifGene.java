package com.novelbio.report.Params;

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

	@Override
	public EnumTaskReport getEnumReport() {
		return EnumTaskReport.DiffExp;
	}

}
