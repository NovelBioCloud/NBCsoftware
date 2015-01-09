package com.novelbio.report.Params;

import java.util.List;

import com.novelbio.base.word.NBCWordImage;
import com.novelbio.database.domain.geneanno.GOtype;
import com.novelbio.database.model.species.Species;

public class ReportGOUpDown extends ReportBase {
	private static final long serialVersionUID = 1L;
	
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
	
}
