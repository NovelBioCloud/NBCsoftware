package com.novelbio.report.Params;

import java.util.List;

import com.novelbio.database.domain.information.SoftWareInfo.SoftWare;
import com.novelbio.database.model.species.Species;

public class ReportAbstract extends ReportBase {
	private static final long serialVersionUID = 3048107392948432050L;
	
	public void setSpeciesName(Species species) {
		String name = species.getNameLatin();
		String[] ss = name.split(" ");
		if (ss.length > 2) {
			name = ss[0] + " " + ss[1];
		}
		mapKey2Param.put("speciesName", name);
	}
	
	public void setSoftware(SoftWare softWare) {
		mapKey2Param.put("software", softWare);
	}
	
	public void setAlgrithm(String algrithm) {
		mapKey2Param.put("algrithm", algrithm);
	}
	
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
		mapKey2Param.put("blastSpeciesName", blastToSpeciesName);
	}
	
	public void setSamples(String sample1, String sample2, String sample3) {
		String samples = sample1 + "," + sample2 + " and " + sample3;
		mapKey2Param.put("samples", samples);
	}
	
	public void setSequence(String sequence) {
		mapKey2Param.put("sequence", sequence);
	}
	
	@Override
	public EnumReport getEnumReport() {
		return EnumReport.Abstract;
	}
	
	

}
