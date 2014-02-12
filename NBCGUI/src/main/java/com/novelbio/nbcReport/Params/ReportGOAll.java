package com.novelbio.nbcReport.Params;

import java.util.List;

import com.novelbio.base.word.NBCWordImage;
import com.novelbio.database.domain.geneanno.GOtype;
import com.novelbio.database.model.species.Species;

public class ReportGOAll extends ReportBase  {
	private static final long serialVersionUID = 604960994760611470L;
	/**
	 * 添加图片模板
	 * @param nbcWordImage
	 */
	public void addNBCWordImage(NBCWordImage nbcWordImage) {
		mapKey2Param.put("GOAllImage", nbcWordImage);
	}
	/** 总的差异基因数量 */
	public void setDifGeneNum(int difGeneNum) {
		mapKey2Param.put("DifNum", difGeneNum);
	}
	/** 物种 */
	public void setSpecies(Species species) {
		mapKey2Param.put("SpeciesName", species.getCommonName());
	}
	/**　blast到的一系列物种　*/
	public void setBlastToSpecies(List<Species> lsBlastToSpecies) {
		int i = 0;
		String blastToSpeciesName = "";
		for (Species species : lsBlastToSpecies) {
			if (i == 0) {
				blastToSpeciesName = species.getCommonName();
			} else {
				blastToSpeciesName = blastToSpeciesName + " and " + species.getCommonName();
			}
			i++;
		}
		mapKey2Param.put("BlastSpeciesName", blastToSpeciesName);
	}
	
	/** GO-Analysis中BP中，P<0.01的GO Term的个数 */
	public void setGoSigNum(GOtype goType, int goBpSigNum) {
		if (goType == GOtype.BP) {
			mapKey2Param.put("SigGOBP", goBpSigNum);
		} else if (goType == GOtype.CC) {
			mapKey2Param.put("SigGOCC", goBpSigNum);
		} else if (goType == GOtype.MF) {
			mapKey2Param.put("SigGOMF", goBpSigNum);
		}
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
			mapKey2Param.put("SigGOBPfirst", goTermDetail);
			mapKey2Param.put("SigGOBPfirstNum", geneNum);
			mapKey2Param.put("SigGOBPfirstP", pvalue);
		} else if (goType == GOtype.CC) {
			mapKey2Param.put("SigGOCCfirst", goTermDetail);
			mapKey2Param.put("SigGOCCfirstNum", geneNum);
			mapKey2Param.put("SigGOCCfirstP", pvalue);
		} else if (goType == GOtype.MF) {
			mapKey2Param.put("SigGOMFfirst", goTermDetail);
			mapKey2Param.put("SigGOMFfirstNum", geneNum);
			mapKey2Param.put("SigGOMFfirstP", pvalue);
		}
	}
	/** 载入GO-Analysis中BP中，排在第一显著的GO-Term的名称和个数 */
	private void setGoTerm_2nd_Num(GOtype goType, String goTermDetail, int geneNum, double pvalue) {
		if (goType == GOtype.BP) {
			mapKey2Param.put("SigGOBPsecond", goTermDetail);
			mapKey2Param.put("SigGOBPsecondNum", geneNum);
			mapKey2Param.put("SigGOBPsecondP", pvalue);
		} else if (goType == GOtype.CC) {
			mapKey2Param.put("SigGOCCsecond", goTermDetail);
			mapKey2Param.put("SigGOCCsecondNum", geneNum);
			mapKey2Param.put("SigGOCCsecondP", pvalue);
		} else if (goType == GOtype.MF) {
			mapKey2Param.put("SigGOMFsecond", goTermDetail);
			mapKey2Param.put("SigGOMFsecondNum", geneNum);
			mapKey2Param.put("SigGOMFsecondP", pvalue);
		}
	}
	/** 载入GO-Analysis中BP中，排在第一显著的GO-Term的名称和个数 */
	private void setGoTerm_3rd_Num(GOtype goType, String goTermDetail, int geneNum, double pvalue) {
		if (goType == GOtype.BP) {
			mapKey2Param.put("SigGOBPthird", goTermDetail);
			mapKey2Param.put("SigGOBPthirdNum", geneNum);
			mapKey2Param.put("SigGOBPthirdP", pvalue);
		} else if (goType == GOtype.CC) {
			mapKey2Param.put("SigGOCCthird", goTermDetail);
			mapKey2Param.put("SigGOCCthirdNum", geneNum);
			mapKey2Param.put("SigGOCCthirdP", pvalue);
		} else if (goType == GOtype.MF) {
			mapKey2Param.put("SigGOMFthird", goTermDetail);
			mapKey2Param.put("SigGOMFthirdNum", geneNum);
			mapKey2Param.put("SigGOMFthirdP", pvalue);
		}
	}

	@Override
	public EnumReport getEnumReport() {
		return EnumReport.GOAnalysis;
	}
}
