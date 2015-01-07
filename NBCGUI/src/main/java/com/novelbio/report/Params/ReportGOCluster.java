package com.novelbio.report.Params;

import java.util.List;

import com.novelbio.database.model.species.Species;

public class ReportGOCluster extends ReportBase {

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
	
	/** cluster类型数 */
	public void setClusterTypeNum(int clusterTypeNum) {
		mapKey2Param.put("ClusterTypeNum", clusterTypeNum);
	}

	@Override
	public EnumReport getEnumReport() {
		return EnumReport.GO_Cluster;
	}
	
}
