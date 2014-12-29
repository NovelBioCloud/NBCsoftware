package com.novelbio.nbcReport.reportParams;

import java.util.HashMap;
import java.util.Map;

import com.novelbio.database.model.species.Species;

public class ReportGO {
	
	/**GOAnalysis的参数键值对，用于渲染模板*/
	Map<String, Object> mapKey2Param = new HashMap<String, Object>();
	
	/** 设定物种名称 */
	public void setSpeciesName(Species species) {
		String name = species.getNameLatin();
		String[] ss = name.split(" ");
		if (ss.length > 2) {
			name = ss[0] + " " + ss[1];
		}
		mapKey2Param.put("SpeciesName", name);
	}
	
	/**获取GOAnalysis的参数*/
	public Map<String, Object> getReportGOParam() {
		return mapKey2Param;
	}

}
