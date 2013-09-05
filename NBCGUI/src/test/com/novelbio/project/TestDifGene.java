package com.novelbio.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.novelbio.base.fileOperate.FileHadoop;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.nbcReport.EnumTableType;
import com.novelbio.nbcReport.XdocTable;
import com.novelbio.nbcReport.XdocTmpltExcel;
import com.novelbio.nbcReport.Params.ReportDifGene;
import com.novelbio.nbcReport.Params.ReportDifGeneAll;
import com.novelbio.nbcReport.Params.ReportGeneExpressionAll;
import com.novelbio.nbcReport.Params.ReportProject;

public class TestDifGene {
	
	ReportDifGeneAll reportDifGeneAll;
	
	@Before
	public void init() {

		ReportDifGene reportDifGene = new ReportDifGene();
		reportDifGene.setDiffGeneType("DEGSeq");
		reportDifGene.setLog2FC(0.585);
		reportDifGene.setpValue(0.05);
		Set<String> lsList = new HashSet<>();
		lsList.add("/media/hdfs/nbCloud/staff/gaozhu/我的文档/Difference-Expression_result/Dif-mRNA.xls");
		reportDifGene.setLsResults(lsList);
		List<XdocTmpltExcel> lsTmpltExcels = new ArrayList<>();
		XdocTmpltExcel xdocTmpltExcel = new XdocTmpltExcel(EnumTableType.DifGene.getXdocTable());
		xdocTmpltExcel.setExcelTitle("是差异基因表达分析结果的截图展示");
		xdocTmpltExcel.addExcel(FileHadoop.getHdfsHeadSymbol("/nbCloud/staff/gaozhu/我的文档/Difference-Expression_result/Dif-mRNA.xls"), 1);
		lsTmpltExcels.add(xdocTmpltExcel);
		reportDifGene.setLsTmpltExcels(lsTmpltExcels);
		reportDifGene.writeAsFile("/hdfs:/nbCloud/staff/gaozhu/我的文档/Difference-Expression_result");
		
		

	}
	
	@Test
	public void runTest() {
		reportDifGeneAll = new ReportDifGeneAll();
		String savePath = FileHadoop.getHdfsHeadSymbol("/nbCloud/staff/gaozhu/我的文档/Difference-Expression_result");
		List<String> lsList = new ArrayList<>();
		lsList.add(savePath);
		ReportProject reportProject = new ReportProject(lsList);
		reportProject.outputReport("/home/novelbio/桌面/abd.docx");

	}

}
