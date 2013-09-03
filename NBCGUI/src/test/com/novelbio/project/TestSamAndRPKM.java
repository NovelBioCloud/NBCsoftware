package com.novelbio.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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


public class TestSamAndRPKM {
	ReportSamAndRPKM reportSamAndRPKM;
	
	@Before
	public void init() {
		reportSamAndRPKM = new ReportSamAndRPKM();
		List<XdocTmpltExcel> lsExcels = new ArrayList<>();
		XdocTmpltExcel xdocTmpltExcel = new XdocTmpltExcel(EnumTableType.MappingResult);
		xdocTmpltExcel.setExcelTitle("Mapping率分析统计结果");
		xdocTmpltExcel.addExcel(FileHadoop.getHdfsHeadSymbol("/nbCloud/public/test/samAndRPKM/100S_MappingStatistic.xls"), 1);
		
		XdocTmpltExcel xdocTmpltExcel2 = new XdocTmpltExcel(EnumTableType.MappingChrFile);
		xdocTmpltExcel2.setExcelTitle("Reads在染色体上的分布统计");
		xdocTmpltExcel2.addExcel(FileHadoop.getHdfsHeadSymbol("/nbCloud/public/test/samAndRPKM/100S_MappingStatistic.xls"), 2);	
		
		lsExcels.add(xdocTmpltExcel);
		lsExcels.add(xdocTmpltExcel2);
		reportSamAndRPKM.setLsExcels(lsExcels);
		
		List<XdocTmpltExcel> lsExcels2 = new ArrayList<>();
		XdocTmpltExcel xdocTmpltExcel3 = new XdocTmpltExcel(EnumTableType.MappingStatistics);
		xdocTmpltExcel3.setExcelTitle("Reads在基因上的分布统计图表");
		xdocTmpltExcel3.addExcel(FileHadoop.getHdfsHeadSymbol("/nbCloud/public/test/samAndRPKM/100S_GeneStructure.txt"), 1);
		lsExcels2.add(xdocTmpltExcel3);
		reportSamAndRPKM.setLsExcels2(lsExcels2);
		
		List<XdocTmpltPic> lsPics = new ArrayList<>();
		XdocTmpltPic xdocTmpltPic = new XdocTmpltPic(FileHadoop.getHdfsHeadSymbol("/nbCloud/public/test/samAndRPKM/ChrDistribution_100S.png"));
		xdocTmpltPic.setTitle("Reads在染色体上的分布柱状图");
		lsPics.add(xdocTmpltPic);
		reportSamAndRPKM.setLsTmpltPics(lsPics);
		
		List<String> lsResultFile = new ArrayList<>();
		lsResultFile.add("S100_MappingStatistic.xls");
		lsResultFile.add("ChrDistribution_S100.png");
		lsResultFile.add("S_MappingStatistic.xls");
		lsResultFile.add("ChrDistribution_S.png");
		reportSamAndRPKM.setLsResultFile(lsResultFile);
		
		reportSamAndRPKM.setExcelName1("S100_MappingStatistics.xls");
		reportSamAndRPKM.setExcelName2("S_MappingStatistics.xls");
		reportSamAndRPKM.setExcelName3("S100_MappingStatistics.xls");
		reportSamAndRPKM.setExcelName4("S_MappingStatistics.xls");
		
		
	}
	
	
	@Test
	public void runTest() {
		String savePath = FileHadoop.getHdfsHeadSymbol("/nbCloud/staff/gaozhu/我的文档/SamAndRPKM_result");
		reportSamAndRPKM.outputReportXdoc(savePath);
		Assert.assertTrue(FileOperate.isFileExist(FileOperate.addSep(savePath)+reportSamAndRPKM.getEnumReport().getReportXdocFileName()));
		List<String> lsList = new ArrayList<>();
		lsList.add(savePath);
		ReportProject reportProject = new ReportProject(lsList);
		reportProject.outputReport("/home/novelbio/桌面/bbc.docx");
	}

}
