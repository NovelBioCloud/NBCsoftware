package com.novelbio.nbcReport.Params;

import com.novelbio.base.dataOperate.DateUtil;



/**
 * 参数枚举类
 * 
 * @author novelbio
 * 
 */
public enum EnumReport {
	GeneExp("GeneExp","GeneExpression_result",new ReportGeneExpressionAll()),
	GOAnalysis("GOAnalysis","GO-Analysis_result",new ReportGOAll()),
	PathWay("PathWay","PathWay-Analysis_result",new ReportPathWayAll()),
	SamAndRPKM("SamAndRPKM","SamAndRPKM_result",new ReportSamAndRPKMAll()),
	FastQC("FastQC","Quality-Control_result",new ReportQCAll()),
	DNASeqMap("DNASeqMap","DNASeqMap_result",new ReportDNASeqMap()),
	RNASeqMap("RNASeqMap","RNASeqMap_result",new ReportRNASeqMap()),
	DiffExp("DiffExp","Difference-Expression_result",new ReportDifGeneAll()),
	GOTree("GOTree","GO-Trees_result",null),
	GeneAct("GeneAct","Gene-Act-Network_result",null),
	MiRNA("MiRNA","miRNA-Target-Network_result",null),
	PathwayAct("PathwayAct","Pathway-Act-network_result",null),
	LncRNA("LncRNA","Co-Exp-Net_LncRNA_result",null),
	Project("project","Novelbio_Result",null),
	Picture("picture","Picture",null),
	Excel("excel","Excel",null);
	
	String type;
	String tempName;
	ReportBase reportBase;
	EnumReport(String type,String tempName,ReportBase reportBase) {
		this.type = type;
		this.tempName = tempName;
		this.reportBase = reportBase;
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
	 * 获得序列化随机文件名
	 * @return
	 */
	public String getReportRandomFileName(){
		return "report_" + type + DateUtil.getDateAndRandom();
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
	
	public ReportBase getReportAll(){
		return reportBase.getClone();
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
