package com.novelbio.report.Params;

import com.novelbio.base.PathDetail;
import com.novelbio.base.SepSign;


/**
 * 参数枚举类
 * 
 * @author novelbio
 * 
 */
public enum EnumReport {
	GeneExp("GeneExpression_result",new ReportGeneExpression()),
	GeneStructure("GeneStructure_result",null),
	
	//以下是GO的所有报告模块
	GOAnalysis("GOAnalysis_result",new ReportGO()),
	GO_All("GO_All",new ReportGOAll()),
	GO_UpDown("GO_UpDown",new ReportGOUpDown()),
	GO_Cluster("GO_Cluster",new ReportGOCluster()),
	GO_Result("GO_Result",new ReportGOResult()),
	
	PathWay("PathWayAnalysis_result",new ReportPathWay()),
	Path_UpDown("Path_UpDown",new ReportPathUpDown()),
	Path_Result("Path_Result",new ReportPathResult()),
	COG("COGAnalysis_result",null),
	
	SamStatistics("SamStatistic_result",new ReportSamAndRPKM()),
	NCRNAstatistics("ncrnaStatistic_result",null),
	FastQC("QualityControl_result",new ReportQC()),
	DNASeqMap("DNASeqMap_result",new ReportDNASeqMap()),
	RNASeqMap("RNASeqMap_result",new ReportRNASeqMap()),
	RSeQC("RSeQC_result",null),
	RemoveRrna("RemoveRrna_result",null),
	SamToFastq("SamToFastq_result",null),
	
	RNAassembly("RNAassembly_result",null),
	RNAAlternativeSplicing("RNAAlternativeSplicing_result",null),

	DiffExp("DifferenceExpression_result",new ReportDifGene()),
	RfamStatistics("RfamStatistics_result", null),
	ReconstructTranscriptome("ReconstructTranscriptome_result", null),
	GOTree("GOTrees_result",null),
	GeneActNetwork("GeneActNetwork_result",null),
	MiRNATargetPredict("MiRNATargetPredict_result",null),
	MiRNATargetNetwork("MiRNATargetNetwork_result",null),
	PathwayActNetwork("PathwayActNetwork_result",null),
	CoExpNetLncRNA("CoExpNetLncRNA_result",null),
	CoExpNet("CoExpNetWork_result",null),
	GetSeq("GetSeq_result",null),
	GATK("GATK_result",null),
	VarScan("VarScan_result",null),
	CDSPredict("CDSPredict_result",null),
	GeneAnno("GeneAnno_result",null),
	Blast("Blast_result", null),
	Pindel("Pindel_result", null),
	Degree("Degree_result", null),
	Intersection("Intersection_result", new ReportIntersection()),
	
	ReportAll("Novelbio_result",new ReportAll()),
	@Deprecated 
	Picture("Picture",null),
	@Deprecated 
	Excel("Excel",null),
	
	MiRNASeqAnalysis("MiRNASeqAnalysis_result",null),
	MiRNAPredict("MiRNAPredict_result",null),
	
	SamConvert("SamConvert_result", null),
	
	Abstract("Abstract", null);
	
	/** 模板名称 也可以是结果文件夹名*/
	String tempName;
	/** 报告对象 */
	ReportBase reportBase;
	/**
	 * 
	 * @param tempName 模板名称 也可以是结果文件夹名
	 * @param reportBase 报告对象
	 */
	EnumReport(String tempName,ReportBase reportBase) {
		this.tempName = tempName;
		this.reportBase = reportBase;
	}

	/**
	 * 获得序列化随机文件名
	 * @return
	 */
	public String getReportFileName(){
		return "report_" + this.name();
	}
	
	/**
	 * 获得序列化文件名
	 * @return
	 * @param sufix 文件名中的后缀
	 */
	public String getReportFileName(String sufix){
//		return "report_" + this.name() + "_" + sufix;
		return sufix + SepSign.SEP_INFO + this.name();
	}
	/**
	 * 得到word模板路径
	 */
	public String getTempPath() {
		return PathDetail.getReportTempPath();
	}
	
	/**
	 * 获取reportBase 报告对象
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getReportBase(){
		return (T) reportBase.getClone();
	}
	
	/**
	 * 得到模板文件名
	 * @return
	 */
	public String getTempName(){
		return tempName+".doc";
	}
	
	public String getFtlTempName(){
		return tempName+".ftl";
	}
	
	/**
	 * 得到结果目录文件名也可以是模板的名称
	 * @return
	 */
	public String getResultFolder(){
		return tempName;
	}
}
