package com.novelbio.report.Params;

import com.novelbio.database.model.species.Species;

public class ReportPathWay extends ReportBase{
	private static final long serialVersionUID = 1L;
	
	/**　测试方法　*/
	public void setTestMethod(String testMethod) {
		mapKey2Param.put("testMethod", testMethod);
	}
	
	/** 实验组名 */
	public void setTreatName(String teamName) {
		mapKey2Param.put("teamName", teamName);
	}
	
	/** 设定物种 */
	public void setSpeciesName(Species species) {
		String name = species.getNameLatin();
		String[] ss = name.split(" ");
		if (ss.length > 2) {
			name = ss[0] + " " + ss[1];
		}
		mapKey2Param.put("SpeciesName", name);
	}
	
	@Override
	public EnumTaskReport getEnumReport() {
		return EnumTaskReport.PathWay;
	}

}
