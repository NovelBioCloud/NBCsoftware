package com.novelbio.nbcReport.Params;

import java.util.List;
import java.util.Map;

import com.novelbio.base.word.NBCWordImage;
import com.novelbio.database.domain.geneanno.GOtype;
import com.novelbio.database.model.species.Species;

public class ReportGOUpDown extends ReportBase {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 添加图片
	 * @param up 上调还是下调
	 * @param nbcWordImage
	 */
	public void addNBCWordImage(boolean up, NBCWordImage nbcWordImage) {
		if (up) {
			mapKey2Param.put("upRegulateImage", nbcWordImage);
		} else {
			mapKey2Param.put("downRegulateImage", nbcWordImage);
		}
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
	
	/**
	 * GO-Analysis中BP中，P<0.01的GO Term的个数
	 * @param up 上调还是下调
	 * @param goType
	 * @param goBpSigNum
	 */
	public void setGoSigNum(boolean up, GOtype goType, int goBpSigNum) {
		if (up) {
			setGoSigNumUp(goType, goBpSigNum);
		} else {
			setGoSigNumDown(goType, goBpSigNum);
		}
	}
	
	/** GO-Analysis中BP中，P<0.01的GO Term的个数 */
	private void setGoSigNumUp(GOtype goType, int goBpSigNum) {
		if (goType == GOtype.BP) {
			mapKey2Param.put("SigGOBPUP", goBpSigNum);
		} else if (goType == GOtype.CC) {
			mapKey2Param.put("SigGOCCUP", goBpSigNum);
		} else if (goType == GOtype.MF) {
			mapKey2Param.put("SigGOMFUP", goBpSigNum);
		}
	}
	/** GO-Analysis中BP中，P<0.01的GO Term的个数 */
	private void setGoSigNumDown(GOtype goType, int goBpSigNum) {
		if (goType == GOtype.BP) {
			mapKey2Param.put("SigGOBPDOWN", goBpSigNum);
		} else if (goType == GOtype.CC) {
			mapKey2Param.put("SigGOCCDOWN", goBpSigNum);
		} else if (goType == GOtype.MF) {
			mapKey2Param.put("SigGOMFDOWN", goBpSigNum);
		}
	}
	
	/**
	 * @param goType
	 * @param up 上调还是下调
	 * @param termRank 第几个显著的go
	 * @param goTermDetail 具体的go名字
	 * @param geneNum 该go所拥有的条目
	 */
	public void setGoTerm_Num(GOtype goType, boolean up, int termRank, String goTermDetail, int geneNum, double pvalue) {
		if (termRank == 1) {
			setGoTerm_1st_Num(up, goType, goTermDetail, geneNum, pvalue);
		} else if (termRank == 2) {
			setGoTerm_2nd_Num(up, goType, goTermDetail, geneNum, pvalue);
		} else if (termRank == 3) {
			setGoTerm_3rd_Num(up, goType, goTermDetail, geneNum, pvalue);
		}
	}
	
	/** 载入GO-Analysis中BP中，排在第一显著的GO-Term的名称和个数 */
	private void setGoTerm_1st_Num(boolean up, GOtype goType, String goTermDetail, int geneNum, double pvalue) {
		String updown = up ? "UP": "DOWN";
		if (goType == GOtype.BP) {
			mapKey2Param.put("SigGOBPfirst" + updown, goTermDetail);
			mapKey2Param.put("SigGOBPfirst" + updown + "Num", geneNum);
			mapKey2Param.put("SigGOBPfirst" + updown + "P", pvalue);
		} else if (goType == GOtype.CC) {
			mapKey2Param.put("SigGOCCfirst" + updown, goTermDetail);
			mapKey2Param.put("SigGOCCfirst" + updown + "Num", geneNum);
			mapKey2Param.put("SigGOCCfirst" + updown + "P", pvalue);
		} else if (goType == GOtype.MF) {
			mapKey2Param.put("SigGOMFfirst" + updown, goTermDetail);
			mapKey2Param.put("SigGOMFfirst" + updown + "Num", geneNum);
			mapKey2Param.put("SigGOMFfirst" + updown + "P", pvalue);
		}
	}
	/** 载入GO-Analysis中BP中，排在第一显著的GO-Term的名称和个数 */
	private void setGoTerm_2nd_Num(boolean up, GOtype goType, String goTermDetail, int geneNum, double pvalue) {
		String updown = up ? "UP": "DOWN";
		if (goType == GOtype.BP) {
			mapKey2Param.put("SigGOBPsecond" + updown, goTermDetail);
			mapKey2Param.put("SigGOBPsecond" + updown + "Num", geneNum);
			mapKey2Param.put("SigGOBPsecond" + updown + "P", pvalue);
		} else if (goType == GOtype.CC) {
			mapKey2Param.put("SigGOCCsecond" + updown, goTermDetail);
			mapKey2Param.put("SigGOCCsecond" + updown + "Num", geneNum);
			mapKey2Param.put("SigGOCCsecond" + updown + "P", pvalue);
		} else if (goType == GOtype.MF) {
			mapKey2Param.put("SigGOMFsecond" + updown, goTermDetail);
			mapKey2Param.put("SigGOMFsecond" + updown + "Num", geneNum);
			mapKey2Param.put("SigGOMFsecond" + updown + "P", pvalue);
		}
	}
	/** 载入GO-Analysis中BP中，排在第一显著的GO-Term的名称和个数 */
	private void setGoTerm_3rd_Num(boolean up, GOtype goType, String goTermDetail, int geneNum, double pvalue) {
		String updown = up ? "UP": "DOWN";
		if (goType == GOtype.BP) {
			mapKey2Param.put("SigGOBPthird" + updown, goTermDetail);
			mapKey2Param.put("SigGOBPthird" + updown + "Num", geneNum);
			mapKey2Param.put("SigGOBPthird" + updown + "P", pvalue);
		} else if (goType == GOtype.CC) {
			mapKey2Param.put("SigGOCCthird" + updown, goTermDetail);
			mapKey2Param.put("SigGOCCthird" + updown + "Num", geneNum);
			mapKey2Param.put("SigGOCCthird" + updown + "P", pvalue);
		} else if (goType == GOtype.MF) {
			mapKey2Param.put("SigGOMFthird" + updown, goTermDetail);
			mapKey2Param.put("SigGOMFthird" + updown + "Num", geneNum);
			mapKey2Param.put("SigGOMFthird" + updown + "P", pvalue);
		}
	}
	
	/** 设定上下调的基因数量 */
	public void setUpDownNum(int upGeneNum, int downGeneNum) {
		mapKey2Param.put("DifNumUP", upGeneNum);
		mapKey2Param.put("DifNumDOWN", downGeneNum);
		mapKey2Param.put("DifNum", upGeneNum + downGeneNum);
	}
	
	@Override
	public EnumReport getEnumReport() {
		return EnumReport.GO_UpDown;
	}
	
	@Override
	public Map<String, Object> buildFinalParamMap() {
		return super.buildFinalParamMap();
	}
}
