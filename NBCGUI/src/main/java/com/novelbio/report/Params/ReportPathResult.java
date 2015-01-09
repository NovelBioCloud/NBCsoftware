package com.novelbio.report.Params;

import com.novelbio.database.model.species.Species;

public class ReportPathResult extends ReportBase {
	private static final long serialVersionUID = 8774691828180327463L;
	
	public void setDifNum(int difNum) {
		mapKey2Param.put("DifNum", difNum);
	}
	
	public void setAllDifNum(int allDifNum) {
		mapKey2Param.put("AllDifNum", allDifNum);
	}
	
	public void setSpeciesName(Species species) {
		String name = species.getNameLatin();
		String[] ss = name.split(" ");
		if (ss.length > 2) {
			name = ss[0] + " " + ss[1];
		}
		mapKey2Param.put("SpeciesName", name);
	}
	
	public void setSigTermNum(int sigTermNum) {
		mapKey2Param.put("SigTermNum", sigTermNum);
	}
	
	public void setItemTerm(String itemTerm1, String itemTerm2, String itemTerm3) {
		String itemTerm = itemTerm1 + "," + itemTerm2 + " and " + itemTerm3;
		mapKey2Param.put("ItemTerm", itemTerm);
	}

	@Override
	public EnumReport getEnumReport() {
		return EnumReport.Path_Result;
	}

}
