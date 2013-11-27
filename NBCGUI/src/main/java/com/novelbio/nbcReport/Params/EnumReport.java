package com.novelbio.nbcReport.Params;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;

import com.novelbio.base.dataOperate.DateUtil;

import freemarker.template.Configuration;
import freemarker.template.Template;



/**
 * 参数枚举类
 * 
 * @author novelbio
 * 
 */
public enum EnumReport {
	GeneExp("GeneExpression_result",new ReportGeneExpressionAll()),
	GeneStructure("GeneStructure_result",null),
	GOAnalysis("GOAnalysis_result",new ReportGOAll()),
	PathWay("PathWayAnalysis_result",new ReportPathWayAll()),
	SamStatistics("SamStatistic_result",new ReportSamAndRPKMAll()),
	NCRNAstatistics("ncrnaStatistic_result",null),
	FastQC("QualityControl_result",new ReportQCAll()),
	DNASeqMap("DNASeqMap_result",new ReportDNASeqMap()),
	RNASeqMap("RNASeqMap_result",new ReportRNASeqMap()),
	DiffExp("DifferenceExpression_result",new ReportDifGeneAll()),
	ReconstructTranscriptome("ReconstructTranscriptome_result", null),
	GOTree("GOTrees_result",null),
	GeneActNetwork("GeneActNetwork_result",null),
	MiRNATargetNetwork("MiRNATargetNetwork_result",null),
	PathwayActNetwork("PathwayActNetwork_result",null),
	CoExpNetLncRNA("CoExpNetLncRNA_result",null),
	Project("Novelbio_Result",null),
	Picture("Picture",null),
	Excel("Excel",null),
	MiRNASeqAnalysis("MiRNASeqAnalysis_result",null);
	String tempName;
	ReportBase reportBase;
	EnumReport(String tempName,ReportBase reportBase) {
		this.tempName = tempName;
		this.reportBase = reportBase;
	}

	/**
	 * 得到xdoc报告输出文件名
	 * 
	 * @return
	 */
	public String getReportXdocFileName() {
		return "report_" + this.name() + ".txt";
	}
	
	/**
	 * 获得序列化随机文件名
	 * @return
	 */
	public String getReportRandomFileName(){
		return "report_" + this.name() + DateUtil.getDateAndRandom();
	}
	/**
	 * 得到xdoc模板路径
	 * 
	 * @return
	 * @throws IOException 
	 */
	public String getTempPath() throws IOException {
		return "xdocTemplate";
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
