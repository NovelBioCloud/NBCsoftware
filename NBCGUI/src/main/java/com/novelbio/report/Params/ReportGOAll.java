package com.novelbio.report.Params;

import java.util.List;

import com.novelbio.base.word.NBCWordImage;
import com.novelbio.database.domain.geneanno.GOtype;
import com.novelbio.database.model.species.Species;

public class ReportGOAll extends ReportBase  {
	private static final long serialVersionUID = 1L;
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
	
	@Override
	public EnumReport getEnumReport() {
		return EnumReport.GO_All;
	}
}
