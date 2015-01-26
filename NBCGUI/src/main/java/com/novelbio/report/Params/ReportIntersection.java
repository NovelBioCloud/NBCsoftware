package com.novelbio.report.Params;

public class ReportIntersection extends ReportBase {
	private static final long serialVersionUID = -3426407045526127182L;
	
	public void setSamples(String sample1, String sample2, String sample3) {
		String samples = sample1 + "," + sample2 + " and " + sample3;
		mapKey2Param.put("samples", samples);
	}

	@Override
	public EnumTaskReport getEnumReport() {
		return EnumTaskReport.Intersection;
	}

}
