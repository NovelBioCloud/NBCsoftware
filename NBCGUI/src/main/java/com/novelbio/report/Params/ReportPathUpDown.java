package com.novelbio.report.Params;

public class ReportPathUpDown extends ReportBase {
	private static final long serialVersionUID = -1379935375370679905L;
	
	public void setUpDownRegulation(int upRegulation, int downRegulation) {
		mapKey2Param.put("UpRegulation", upRegulation);
		mapKey2Param.put("DownRegulation", downRegulation);
	}
	
	public void setFinderCondition(String finderCondition) {
		mapKey2Param.put("FinderCondition", finderCondition);
	}
	
	@Override
	public EnumTaskReport getEnumReport() {
		return EnumTaskReport.Path_UpDown;
	}

}
