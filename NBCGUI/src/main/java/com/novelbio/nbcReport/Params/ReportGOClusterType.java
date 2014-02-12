package com.novelbio.nbcReport.Params;

import com.novelbio.base.word.NBCWordImage;
import com.novelbio.database.domain.geneanno.GOtype;

public class ReportGOClusterType extends ReportBase {
	private static final long serialVersionUID = 4313797841531882560L;

	/**
	 * 添加图片模板
	 * @param nbcWordImage
	 */
	public void addNBCWordImage(NBCWordImage nbcWordImage) {
		mapKey2Param.put("clusterTypeImage", nbcWordImage);
	}

	/**
	 * @param goType
	 * @param termRank 第几个显著的go
	 * @param goTermDetail 具体的go名字
	 * @param geneNum 该go所拥有的条目
	 */
	public void setGoTerm_Num(GOtype goType, int termRank, String goTermDetail, int geneNum, double pvalue) {
		if (termRank == 1) {
			setGoTerm_1st_Num(goType, goTermDetail, geneNum, pvalue);
		} else if (termRank == 2) {
			setGoTerm_2nd_Num(goType, goTermDetail, geneNum, pvalue);
		} else if (termRank == 3) {
			setGoTerm_3rd_Num(goType, goTermDetail, geneNum, pvalue);
		}
	}
	
	/** 载入GO-Analysis中BP中，排在第一显著的GO-Term的名称和个数 */
	private void setGoTerm_1st_Num(GOtype goType, String goTermDetail, int geneNum, double pvalue) {
		if (goType == GOtype.BP) {
			mapKey2Param.put("Clustertype1GOBPfirst", goTermDetail);
			mapKey2Param.put("Clustertype1GOBPfirstNum", geneNum);
			mapKey2Param.put("Clustertype1GOBPfirstP", pvalue);
		} else if (goType == GOtype.CC) {
			mapKey2Param.put("Clustertype1GOCCfirst", goTermDetail);
			mapKey2Param.put("Clustertype1GOCCfirstNum", geneNum);
			mapKey2Param.put("Clustertype1GOCCfirstP", pvalue);
		} else if (goType == GOtype.MF) {
			mapKey2Param.put("Clustertype1GOMFfirst", goTermDetail);
			mapKey2Param.put("Clustertype1GOMFfirstNum", geneNum);
			mapKey2Param.put("Clustertype1GOMFfirstP", pvalue);
		}
	}
	/** 载入GO-Analysis中BP中，排在第一显著的GO-Term的名称和个数 */
	private void setGoTerm_2nd_Num(GOtype goType, String goTermDetail, int geneNum, double pvalue) {
		if (goType == GOtype.BP) {
			mapKey2Param.put("Clustertype1GOBPsecond", goTermDetail);
			mapKey2Param.put("Clustertype1GOBPsecondNum", geneNum);
			mapKey2Param.put("Clustertype1GOBPsecondP", pvalue);
		} else if (goType == GOtype.CC) {
			mapKey2Param.put("Clustertype1GOCCsecond", goTermDetail);
			mapKey2Param.put("Clustertype1GOCCsecondNum", geneNum);
			mapKey2Param.put("Clustertype1GOCCsecondP", pvalue);
		} else if (goType == GOtype.MF) {
			mapKey2Param.put("Clustertype1GOMFsecond", goTermDetail);
			mapKey2Param.put("Clustertype1GOMFsecondNum", geneNum);
			mapKey2Param.put("Clustertype1GOMFsecondP", pvalue);
		}
	}
	/** 载入GO-Analysis中BP中，排在第一显著的GO-Term的名称和个数 */
	private void setGoTerm_3rd_Num(GOtype goType, String goTermDetail, int geneNum, double pvalue) {
		if (goType == GOtype.BP) {
			mapKey2Param.put("Clustertype1GOBPthird", goTermDetail);
			mapKey2Param.put("Clustertype1GOBPthirdNum", geneNum);
			mapKey2Param.put("Clustertype1GOBPthirdP", pvalue);
		} else if (goType == GOtype.CC) {
			mapKey2Param.put("Clustertype1GOCCthird", goTermDetail);
			mapKey2Param.put("Clustertype1GOCCthirdNum", geneNum);
			mapKey2Param.put("Clustertype1GOCCthirdP", pvalue);
		} else if (goType == GOtype.MF) {
			mapKey2Param.put("Clustertype1GOMFthird", goTermDetail);
			mapKey2Param.put("Clustertype1GOMFthirdNum", geneNum);
			mapKey2Param.put("Clustertype1GOMFthirdP", pvalue);
		}
	}

	@Override
	public EnumReport getEnumReport() {
		return EnumReport.GOAnalysis;
	}

	
	
	
}
