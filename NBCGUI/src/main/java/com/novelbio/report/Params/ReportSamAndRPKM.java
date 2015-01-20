package com.novelbio.report.Params;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReportSamAndRPKM extends ReportBase{
	private static final long serialVersionUID = -1180209931503665138L;
	
	public void setMappingRate(double mappingRate) {
		mapKey2Param.put("mappingRate", mappingRate * 100);
	}
	
	public void setUniqueMappingRate(double uniqueMappingRate) {
		mapKey2Param.put("uniqueMappingRate", uniqueMappingRate * 100);
	}
	
	public void setJunctionReadsRate(double junctionReadsRate) {
		mapKey2Param.put("junctionReadsRate", junctionReadsRate * 100);
	}

	@Override
	public EnumReport getEnumReport() {
		return EnumReport.SamStatistics;
	}
	
}
