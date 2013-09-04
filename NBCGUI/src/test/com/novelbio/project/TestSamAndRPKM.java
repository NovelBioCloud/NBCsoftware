package com.novelbio.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.novelbio.base.fileOperate.FileHadoop;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.nbcReport.EnumTableType;
import com.novelbio.nbcReport.XdocTmpltExcel;
import com.novelbio.nbcReport.XdocTmpltPic;
import com.novelbio.nbcReport.Params.ReportProject;
import com.novelbio.nbcReport.Params.ReportSamAndRPKM;
import com.novelbio.nbcReport.Params.ReportSamAndRPKMAll;


public class TestSamAndRPKM {
	ReportSamAndRPKMAll reportSamAndRPKMAll;
	
	@Before
	public void init() {
		reportSamAndRPKMAll = new ReportSamAndRPKMAll();
		ReportSamAndRPKM reportSamAndRPKM = new ReportSamAndRPKM();
		List<XdocTmpltExcel> lsExcels = new ArrayList<>();
		XdocTmpltExcel xdocTmpltExcel = new XdocTmpltExcel(EnumTableType.MappingResult.getXdocTable());
		xdocTmpltExcel.setExcelTitle("Mapping率分析统计结果");
		xdocTmpltExcel.addExcel(FileHadoop.getHdfsHeadSymbol("/nbCloud/public/test/samAndRPKM/100S_MappingStatistic.xls"), 1);
		
		XdocTmpltExcel xdocTmpltExcel2 = new XdocTmpltExcel(EnumTableType.MappingChrFile.getXdocTable());
		xdocTmpltExcel2.setExcelTitle("Reads在染色体上的分布统计");
		xdocTmpltExcel2.addExcel(FileHadoop.getHdfsHeadSymbol("/nbCloud/public/test/samAndRPKM/100S_MappingStatistic.xls"), 2);	
		
		lsExcels.add(xdocTmpltExcel);
		lsExcels.add(xdocTmpltExcel2);

		
		XdocTmpltExcel xdocTmpltExcel3 = new XdocTmpltExcel(EnumTableType.MappingStatistics.getXdocTable());
		xdocTmpltExcel3.setExcelTitle("Reads在基因上的分布统计图表");
		xdocTmpltExcel3.addExcel(FileHadoop.getHdfsHeadSymbol("/nbCloud/public/test/samAndRPKM/100S_GeneStructure.txt"), 1);
		lsExcels.add(xdocTmpltExcel3);
		reportSamAndRPKM.setLsExcels(lsExcels);
		List<XdocTmpltPic> lsPics = new ArrayList<>();
		XdocTmpltPic xdocTmpltPic = new XdocTmpltPic(FileHadoop.getHdfsHeadSymbol("/nbCloud/public/test/samAndRPKM/ChrDistribution_100S.png"));
		xdocTmpltPic.setTitle("Reads在染色体上的分布柱状图");
		lsPics.add(xdocTmpltPic);
		reportSamAndRPKM.setLsTmpltPics(lsPics);
		
		Set<String> lsResultFile = new LinkedHashSet<>();
		lsResultFile.add("/media/hdfs/nbCloud/staff/gaozhu/我的文档/SamAndRPKM_result/100S_GeneStructure.txt");
		lsResultFile.add("/media/hdfs/nbCloud/staff/gaozhu/我的文档/SamAndRPKM_result/100S_GeneStructure.txt");
		lsResultFile.add("/media/hdfs/nbCloud/staff/gaozhu/我的文档/SamAndRPKM_result/100S_GeneStructure.txt");
		lsResultFile.add("/media/hdfs/nbCloud/staff/gaozhu/我的文档/SamAndRPKM_result/100S_GeneStructure.txt");
		reportSamAndRPKM.setSetResultFile(lsResultFile);
		reportSamAndRPKM.writeAsFile("/hdfs:/nbCloud/staff/gaozhu/我的文档/SamAndRPKM_result");
		
	}
	
	
	@Test
	public void runTest() {
		String savePath = FileHadoop.getHdfsHeadSymbol("/nbCloud/staff/gaozhu/我的文档/SamAndRPKM_result");
		Assert.assertTrue(FileOperate.isFileExist(FileOperate.addSep(savePath)+reportSamAndRPKMAll.getEnumReport().getReportXdocFileName()));
		List<String> lsList = new ArrayList<>();
		lsList.add(savePath);
		ReportProject reportProject = new ReportProject(lsList);
		reportProject.outputReport("/home/novelbio/桌面/bbc.docx");
	}

}
