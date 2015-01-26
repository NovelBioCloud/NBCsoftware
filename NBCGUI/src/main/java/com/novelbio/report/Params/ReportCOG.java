package com.novelbio.report.Params;

import com.novelbio.database.model.species.Species;

public class ReportCOG extends ReportBase {
	private static final long serialVersionUID = 1L;
	
	/**设置物种*/
	public void setSpecies(Species species) {
		String name = species.getNameLatin();
		String[] ss = name.split(" ");
		if (ss.length > 2) {
			name = ss[0] + " " + ss[1];
		}
		mapKey2Param.put("speciesName", name);
	}

	@Override
	public EnumTaskReport getEnumReport() {
		return EnumTaskReport.COG;
	}

}
