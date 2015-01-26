package com.novelbio.report.Params;

import com.novelbio.database.domain.information.SoftWareInfo.SoftWare;
import com.novelbio.database.model.species.Species;

public class ReportRNASeqMap extends ReportBase{
	private static final long serialVersionUID = 1615663921940298732L;
	
	public void setSoftware(SoftWare software) {
		mapKey2Param.put("software", software);
	}
	
	public void setSpeciesName(Species species) {
		String name = species.getNameLatin();
		String[] ss = name.split(" ");
		if (ss.length > 2) {
			name = ss[0] + " " + ss[1];
		}
		mapKey2Param.put("SpeciesName", name);
	}

	public EnumTaskReport getEnumReport() {
		return EnumTaskReport.RNASeqMap;
	}
}
