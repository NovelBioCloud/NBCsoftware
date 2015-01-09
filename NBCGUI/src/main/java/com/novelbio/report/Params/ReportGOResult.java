package com.novelbio.report.Params;

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
	public void setGoTerm_Num(GOtype goType, int termRank, String goTermDetail, int geneNum, double pvalue) {
		if (termRank == 0) {
			setGoTerm_1st_Num(goType, goTermDetail, geneNum, pvalue);
		} else if (termRank == 1) {
			setGoTerm_2nd_Num(goType, goTermDetail, geneNum, pvalue);
		} else if (termRank == 2) {
			setGoTerm_3rd_Num(goType, goTermDetail, geneNum, pvalue);
		}
	}
	
	/** 载入GO-Analysis中BP中，排在第一显著的GO-Term的名称和个数 */
	private void setGoTerm_1st_Num(GOtype goType, String goTermDetail, int geneNum, double pvalue) {
		if (goType == GOtype.BP) {
			mapKey2Param.put("GOBPFirst", goTermDetail);
			mapKey2Param.put("GOBPFirstNum", geneNum);
			mapKey2Param.put("GOBPFirstP", pvalue);
		} else if (goType == GOtype.CC) {
			mapKey2Param.put("GOCCFirst", goTermDetail);
			mapKey2Param.put("GOCCFirstNum", geneNum);
			mapKey2Param.put("GOCCFirstP", pvalue);
		} else if (goType == GOtype.MF) {
			mapKey2Param.put("GOMFFirst", goTermDetail);
			mapKey2Param.put("GOMFFirstNum", geneNum);
			mapKey2Param.put("GOMFFirstP", pvalue);
		}
	}
	/** 载入GO-Analysis中BP中，排在第一显著的GO-Term的名称和个数 */
	private void setGoTerm_2nd_Num(GOtype goType, String goTermDetail, int geneNum, double pvalue) {
		if (goType == GOtype.BP) {
			mapKey2Param.put("GOBPSecond", goTermDetail);
			mapKey2Param.put("GOBPSecondNum", geneNum);
			mapKey2Param.put("GOBPSecondP", pvalue);
		} else if (goType == GOtype.CC) {
			mapKey2Param.put("GOCCSecond", goTermDetail);
			mapKey2Param.put("GOCCSecondNum", geneNum);
			mapKey2Param.put("GOCCSecondP", pvalue);
		} else if (goType == GOtype.MF) {
			mapKey2Param.put("GOMFSecond", goTermDetail);
			mapKey2Param.put("GOMFSecondNum", geneNum);
			mapKey2Param.put("GOMFSecondP", pvalue);
		}
	}
	/** 载入GO-Analysis中BP中，排在第一显著的GO-Term的名称和个数 */
	private void setGoTerm_3rd_Num(GOtype goType, String goTermDetail, int geneNum, double pvalue) {
		if (goType == GOtype.BP) {
			mapKey2Param.put("GOBPThird", goTermDetail);
			mapKey2Param.put("GOBPThirdNum", geneNum);
			mapKey2Param.put("GOBPThirdP", pvalue);
		} else if (goType == GOtype.CC) {
			mapKey2Param.put("GOCCThird", goTermDetail);
			mapKey2Param.put("GOCCThirdNum", geneNum);
			mapKey2Param.put("GOCCThirdP", pvalue);
		} else if (goType == GOtype.MF) {
			mapKey2Param.put("GOMFThird", goTermDetail);
			mapKey2Param.put("GOMFThirdNum", geneNum);
			mapKey2Param.put("GOMFThirdP", pvalue);
		}
	}

	@Override
	public EnumReport getEnumReport() {
		return EnumReport.GO_Result;
	}

}
