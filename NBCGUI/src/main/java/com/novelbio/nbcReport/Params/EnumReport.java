package com.novelbio.nbcReport.Params;



/**
 * 参数枚举类
 * 
 * @author novelbio
 * 
 */
public enum EnumReport {
	GOAnalysis("GOAnalysis","GO-Analysis_result"),
	PathWay("PathWay","PathWay-Analysis_result"),
	FastQC("FastQC","Quality-Control_result"),
	Mapping("Mapping","Mapping_result"),
	DiffExp("DiffExp","Difference Expression_result"),
	GOTree("GOTree","GO-Trees_result"),
	GeneAct("GeneAct","Gene-Act-Network_result"),
	MiRNA("MiRNA","miRNA-Target-Network_result"),
	PathwayAct("PathwayAct","Pathway-Act-network_result"),
	LncRNA("LncRNA","Co-Exp-Net_LncRNA_result"),
	Project("project","Novelbio_Result"),
	Picture("picture","Picture"),
	Excel("excel","Excel");
	
	String type;
	String tempName;
	EnumReport(String type,String tempName) {
		this.type = type;
		this.tempName = tempName;
	}

	/**
	 * 得到xdoc报告输出文件名
	 * 
	 * @return
	 */
	public String getReportXdocFileName() {
		return "report_" + type + ".txt";
	}
	
	/**
	 * 得到xdoc模板路径
	 * 
	 * @return
	 */
	public String getTempPath() {
		String path = EnumReport.class.getClassLoader().getResource("xdocTemplate").getFile();
		return path;
	}
	
	/**
	 * 得到模板文件名
	 * @return
	 */
	public String getTempName(){
		return tempName+".xdoc";
	}
	
	/**
	 * 得到结果目录文件名
	 * @return
	 */
	public String getResultFolder(){
		return tempName;
	}
	
	public static EnumReport findByFolderName(String folderName){
		EnumReport[] enumReports = EnumReport.values();
		for (int i = 0; i < enumReports.length; i++) {
			if (enumReports[i].getResultFolder().equalsIgnoreCase(folderName)) {
				return enumReports[i];
			}
		}
		return null;
	}
	
}
