package com.novelbio.project;

import java.util.ArrayList;
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
import com.novelbio.nbcReport.Params.ReportGeneExpression;
import com.novelbio.nbcReport.Params.ReportGeneExpressionAll;
import com.novelbio.nbcReport.Params.ReportProject;

public class TestGeneExp {
	
	ReportGeneExpressionAll reportGeneExpressionAll;
	@Before
	public void init() {
		reportGeneExpressionAll = new ReportGeneExpressionAll();
		ReportGeneExpression reportGeneExpression = new ReportGeneExpression();
		reportGeneExpression.setGeneExpType("FPKM");
		List<XdocTmpltExcel> lsXdocTmpltExcels = new ArrayList<>();	
		XdocTmpltExcel xdocTmpltExcel = new XdocTmpltExcel(EnumTableType.GeneExp.getXdocTable());
		xdocTmpltExcel.addExcel(FileHadoop.getHdfsHeadSymbol("/nbCloud/public/test/GeneExp/All_Fragments.txt"), 1);
		xdocTmpltExcel.setExcelTitle("标准化Fragment值列表");
		
		XdocTmpltExcel xdocTmpltExcel2 = new XdocTmpltExcel(EnumTableType.GeneExp.getXdocTable());
		xdocTmpltExcel2.addExcel(FileHadoop.getHdfsHeadSymbol("/nbCloud/public/test/GeneExp/All_FPKM.txt"), 1);
		xdocTmpltExcel2.setExcelTitle("标准化FPKM值列表");
		lsXdocTmpltExcels.add(xdocTmpltExcel);
		lsXdocTmpltExcels.add(xdocTmpltExcel2);
		
		
		reportGeneExpression.setLsExcels(lsXdocTmpltExcels);
		Set<String> lsResult = new LinkedHashSet<>();
		lsResult.add("/media/hdfs/nbCloud/staff/gaozhu/我的文档/GeneExpression_result/All_FPKM.txt");
		lsResult.add("/media/hdfs/nbCloud/staff/gaozhu/我的文档/GeneExpression_result/All_Fragments.txt");
		reportGeneExpression.setSetResultFile(lsResult);
		reportGeneExpression.writeAsFile("/hdfs:/nbCloud/staff/gaozhu/我的文档/GeneExpression_result");

		
	}
	
	@Test
	public void GoTest() {
		String savePath = FileHadoop.getHdfsHeadSymbol("/nbCloud/staff/gaozhu/我的文档/GeneExpression_result");
		Assert.assertTrue(FileOperate.isFileExist(FileOperate.addSep(savePath)+reportGeneExpressionAll.getEnumReport().getReportXdocFileName()));
		List<String> lsList = new ArrayList<>();
		lsList.add(savePath);
		ReportProject reportProject = new ReportProject(lsList);
		reportProject.outputReport("/home/novelbio/桌面/abc.docx");

	}

}
