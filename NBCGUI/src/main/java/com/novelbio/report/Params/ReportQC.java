package com.novelbio.report.Params;

/**
 * GOAnalysis参数对象类，记录结果报告所需要的参数
 * 
 * @author novelbio
 * 
 */
public class ReportQC extends ReportBase{
	private static final long serialVersionUID = 7757181385034786919L;
	
	public void setAvgSize(double avgSize) {
		mapKey2Param.put("avgSize", avgSize);
	}
	
	public void setAvgFilterRate(double avgFilterRate) {
		mapKey2Param.put("avgFilterRate", avgFilterRate * 100);
	}

	@Override
	public EnumTaskReport getEnumReport() {
		return EnumTaskReport.FastQC;
	}

}
