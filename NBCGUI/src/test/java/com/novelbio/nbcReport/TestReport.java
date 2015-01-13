package com.novelbio.nbcReport;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.generalConf.TitleFormatNBC;
import com.novelbio.report.TemplateRender;
import com.novelbio.report.Params.ReportDifGene;
import com.novelbio.report.Params.ReportQC;

import junit.framework.TestCase;

public class TestReport extends TestCase {
	
	public void testDifGeneReport() {
		ReportDifGene reportDifGene = new ReportDifGene();
		reportDifGene.setAlgorithm("DESeq");
		reportDifGene.setCaseVSContorl("group");
		reportDifGene.setDifExpNum(100);
		reportDifGene.setUpDownGeneNum(50, 50);
		reportDifGene.setpValueOrFDR(TitleFormatNBC.Pvalue, 0.05);
		
		Map<String, Object> mapKey2GroupAndGeneNum = new HashMap<String, Object>();
		mapKey2GroupAndGeneNum.put("caseVSControl", "group");
		mapKey2GroupAndGeneNum.put("difExpNum", 100);
		mapKey2GroupAndGeneNum.put("upGeneNum", 50);
		mapKey2GroupAndGeneNum.put("downGeneNum", 50);
		reportDifGene.addMapGroupAndGeneNum(mapKey2GroupAndGeneNum);
		reportDifGene.addMapGroupAndGeneNum(mapKey2GroupAndGeneNum);
		
		TemplateRender templateRender = new TemplateRender();
		Writer out = new BufferedWriter(new OutputStreamWriter(FileOperate.getOutputStream("/home/novelbio/jpx/DifGeneReport.tex", true)));
		templateRender.render("/templateLatex/DifferenceExpression_result.ftl", reportDifGene.getMapKey2Param(), out);
		assertEquals(true, FileOperate.isFileExist("/home/novelbio/jpx/DifGeneReport.tex"));
	}
	
	public void testFastQCReport() {
		
		ReportQC reportQC = new ReportQC();
		reportQC.setAvgFilterRate(85);
		reportQC.setAvgSize(4.0);
		Map<String, Object> mapKey2SampleInfo = new HashMap<String, Object>();
//	    mapKey2SampleInfo.put("sampleName", "sampleName");
//	    mapKey2SampleInfo.put("readsNum", 100);
//	    mapKey2SampleInfo.put("Length", 100);
//	    mapKey2SampleInfo.put("baseNum", 100);
//	    mapKey2SampleInfo.put("CG", 0.85);
		
	    mapKey2SampleInfo.put("sampleName", "sampleName");
	    mapKey2SampleInfo.put("beforeReadsNum", 100);
	    mapKey2SampleInfo.put("afterReadsNum", 100);
	    mapKey2SampleInfo.put("beforeLen", 100);
	    mapKey2SampleInfo.put("afterLen", 100);
	    mapKey2SampleInfo.put("beforeBaseNum", 100);
	    mapKey2SampleInfo.put("afterBaseNum", 100);
	    mapKey2SampleInfo.put("beforeCG", 85);
	    mapKey2SampleInfo.put("afterCG", 85);
	    mapKey2SampleInfo.put("baseFilterRate", 85);
		
	    reportQC.addSampleInfo(mapKey2SampleInfo);
	    reportQC.addSampleInfo(mapKey2SampleInfo);
		
		TemplateRender templateRender = new TemplateRender();
		Writer out = new BufferedWriter(new OutputStreamWriter(FileOperate.getOutputStream("/home/novelbio/jpx/FastQCReport.tex", true)));
		templateRender.render("/templateLatex/QualityControl_result.ftl", reportQC.getMapKey2Param(), out);
		assertEquals(true, FileOperate.isFileExist("/home/novelbio/jpx/FastQCReport.tex"));
	}

}
