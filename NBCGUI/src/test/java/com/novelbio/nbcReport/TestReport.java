package com.novelbio.nbcReport;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.database.domain.information.SoftWareInfo.SoftWare;
import com.novelbio.database.model.species.Species;
import com.novelbio.generalConf.TitleFormatNBC;
import com.novelbio.report.ReportTable;
import com.novelbio.report.TemplateRender;
import com.novelbio.report.Params.ReportAbstract;
import com.novelbio.report.Params.ReportAlternativeSplicing;
import com.novelbio.report.Params.ReportDifGene;
import com.novelbio.report.Params.ReportQC;
import com.novelbio.report.Params.ReportRNASeqMap;
import com.novelbio.report.Params.ReportRNAassembly;
import com.novelbio.report.Params.ReportSamAndRPKM;

import junit.framework.TestCase;

public class TestReport extends TestCase {
	
	public void testDifGeneReport() {
		ReportDifGene reportDifGene = new ReportDifGene();
		reportDifGene.setAlgorithm("DESeq");
		reportDifGene.setCaseVSContorl("group");
		reportDifGene.setDifExpNum(100);
		reportDifGene.setUpDownGeneNum(50, 50);
		reportDifGene.setpValueOrFDR(TitleFormatNBC.Pvalue, 0.05);
		
		List<String[]> lsGroupAndGeneNum = new ArrayList<String[]>();
		String[] groupAndGeneNum = new String[4];
		groupAndGeneNum[0] = "caseVSControl";
		groupAndGeneNum[1] = "difExpNum";
		groupAndGeneNum[2] = "upGeneNum";
		groupAndGeneNum[3] = "downGeneNum";
		lsGroupAndGeneNum.add(groupAndGeneNum);
		lsGroupAndGeneNum.add(groupAndGeneNum);
		lsGroupAndGeneNum.add(groupAndGeneNum);
		lsGroupAndGeneNum.add(groupAndGeneNum);
		lsGroupAndGeneNum.add(groupAndGeneNum);
		
		ReportTable reportTable = new ReportTable();
		reportDifGene.addTable(reportTable.getMapKey2Param("test", "tabltest", lsGroupAndGeneNum, 4));
//		Map<String, Object> mapKey2GroupAndGeneNum = new HashMap<String, Object>();
//		mapKey2GroupAndGeneNum.put("caseVSControl", "group");
//		mapKey2GroupAndGeneNum.put("difExpNum", 100);
//		mapKey2GroupAndGeneNum.put("upGeneNum", 50);
//		mapKey2GroupAndGeneNum.put("downGeneNum", 50);
//		reportDifGene.addMapGroupAndGeneNum(mapKey2GroupAndGeneNum);
//		reportDifGene.addMapGroupAndGeneNum(mapKey2GroupAndGeneNum);
		
		TemplateRender templateRender = new TemplateRender();
		Writer out = new BufferedWriter(new OutputStreamWriter(FileOperate.getOutputStream("/home/novelbio/jpx/DifGeneReport.tex", true)));
		templateRender.render("/templateLatex/DifferenceExpression_result.ftl", reportDifGene.getMapKey2Param(), out);
		assertEquals(true, FileOperate.isFileExist("/home/novelbio/jpx/DifGeneReport.tex"));
	}
	
	public void testFastQCReport() {
		
		ReportQC reportQC = new ReportQC();
		reportQC.setAvgFilterRate(0.85);
		reportQC.setAvgSize(4.0);
//		Map<String, Object> mapKey2SampleInfo = new HashMap<String, Object>();
//	    mapKey2SampleInfo.put("sampleName", "sampleName");
//	    mapKey2SampleInfo.put("readsNum", 100);
//	    mapKey2SampleInfo.put("Length", 100);
//	    mapKey2SampleInfo.put("baseNum", 100);
//	    mapKey2SampleInfo.put("CG", 0.85);
		
//	    mapKey2SampleInfo.put("sampleName", "sampleName");
//	    mapKey2SampleInfo.put("beforeReadsNum", 100);
//	    mapKey2SampleInfo.put("afterReadsNum", 100);
//	    mapKey2SampleInfo.put("beforeBaseNum", 100);
//	    mapKey2SampleInfo.put("afterBaseNum", 100);
//	    mapKey2SampleInfo.put("baseFilterRate", 85);
		
//	    reportQC.addSampleInfo(mapKey2SampleInfo);
//	    reportQC.addSampleInfo(mapKey2SampleInfo);
		List<String[]> lsGroupAndGeneNum = new ArrayList<String[]>();
		String[] groupAndGeneNum = new String[4];
		groupAndGeneNum[0] = "caseVSControl";
		groupAndGeneNum[1] = "difExpNum";
		groupAndGeneNum[2] = "upGeneNum";
		groupAndGeneNum[3] = "downGeneNum";
		lsGroupAndGeneNum.add(groupAndGeneNum);
		lsGroupAndGeneNum.add(groupAndGeneNum);
		lsGroupAndGeneNum.add(groupAndGeneNum);
		lsGroupAndGeneNum.add(groupAndGeneNum);
		lsGroupAndGeneNum.add(groupAndGeneNum);
		
		ReportTable reportTable = new ReportTable();
		reportQC.addTable(reportTable.getMapKey2Param("test", "tabltest", lsGroupAndGeneNum, 4));
		
		TemplateRender templateRender = new TemplateRender();
		Writer out = new BufferedWriter(new OutputStreamWriter(FileOperate.getOutputStream("/home/novelbio/jpx/FastQCReport.tex", true)));
		templateRender.render("/templateLatex/QualityControl_result.ftl", reportQC.getMapKey2Param(), out);
		assertEquals(true, FileOperate.isFileExist("/home/novelbio/jpx/FastQCReport.tex"));
	}
	
	public void testRNASeqMapReport() {
		ReportRNASeqMap reportRNASeqMap = new ReportRNASeqMap();
		reportRNASeqMap.setSoftware(SoftWare.mapsplice);
		Species species = new Species(10090);
		reportRNASeqMap.setSpeciesName(species);
		
		TemplateRender templateRender = new TemplateRender();
		Writer out = new BufferedWriter(new OutputStreamWriter(FileOperate.getOutputStream("/home/novelbio/jpx/RNASeqMapReport.tex", true)));
		templateRender.render("/templateLatex/RNASeqMap_result.ftl", reportRNASeqMap.getMapKey2Param(), out);
		assertEquals(true, FileOperate.isFileExist("/home/novelbio/jpx/RNASeqMapReport.tex"));

	}
	
	public void testSamAndRPKMReport() {
		
		ReportSamAndRPKM reportSamAndRPKM = new ReportSamAndRPKM();
		
		reportSamAndRPKM.setMappingRate(0.8565465456);
		reportSamAndRPKM.setUniqueMappingRate(0.90564684);
		reportSamAndRPKM.setJunctionReadsRate(0.25687448);
		
//		Map<String, Object> mapKey2SampleInfo = new HashMap<String, Object>();
//		mapKey2SampleInfo.put("sampleName", "S360d");
//		mapKey2SampleInfo.put("TotalReads", 100);
//		mapKey2SampleInfo.put("TotalMappedReads", 100);
//		mapKey2SampleInfo.put("MappingRate", 0.8567468746);
//		mapKey2SampleInfo.put("UniqueMapping", 100);
//		mapKey2SampleInfo.put("UniqueMappingRate", 0.8566546);
//		mapKey2SampleInfo.put("junctionAllMappedReads", 100);
//		mapKey2SampleInfo.put("junctionUniqueMapping", 100);
//		mapKey2SampleInfo.put("repeatMapping", 100);
//		mapKey2SampleInfo.put("UnMapped", 100);
//		reportSamAndRPKM.addSampleInfo(mapKey2SampleInfo);
//		reportSamAndRPKM.addSampleInfo(mapKey2SampleInfo);
//		reportSamAndRPKM.addSampleInfo(mapKey2SampleInfo);
//		reportSamAndRPKM.addSampleInfo(mapKey2SampleInfo);
//		reportSamAndRPKM.addSampleInfo(mapKey2SampleInfo);
//		reportSamAndRPKM.addSampleInfo(mapKey2SampleInfo);
//		reportSamAndRPKM.addSampleInfo(mapKey2SampleInfo);
//		reportSamAndRPKM.addSampleInfo(mapKey2SampleInfo);
//		reportSamAndRPKM.addSampleInfo(mapKey2SampleInfo);
//		reportSamAndRPKM.addSampleInfo(mapKey2SampleInfo);
//		reportSamAndRPKM.addSampleInfo(mapKey2SampleInfo);
//		reportSamAndRPKM.addSampleInfo(mapKey2SampleInfo);
		
		List<String[]> lsGroupAndGeneNum = new ArrayList<String[]>();
		String[] groupAndGeneNum = new String[4];
		groupAndGeneNum[0] = "caseVSControl";
		groupAndGeneNum[1] = "difExpNum";
		groupAndGeneNum[2] = "upGeneNum";
		groupAndGeneNum[3] = "downGeneNum";
		lsGroupAndGeneNum.add(groupAndGeneNum);
		lsGroupAndGeneNum.add(groupAndGeneNum);
		lsGroupAndGeneNum.add(groupAndGeneNum);
		lsGroupAndGeneNum.add(groupAndGeneNum);
		lsGroupAndGeneNum.add(groupAndGeneNum);
		
		ReportTable reportTable = new ReportTable();
		reportSamAndRPKM.addTable(reportTable.getMapKey2Param("test", "tabltest", lsGroupAndGeneNum, 4));
		
		TemplateRender templateRender = new TemplateRender();
		Writer out = new BufferedWriter(new OutputStreamWriter(FileOperate.getOutputStream("/home/novelbio/jpx/SamAndRPKMReport.tex", true)));
		templateRender.render("/templateLatex/SamStatistic_result.ftl", reportSamAndRPKM.getMapKey2Param(), out);
		assertEquals(true, FileOperate.isFileExist("/home/novelbio/jpx/SamAndRPKMReport.tex"));
		
	}
	
	public void testReportAbstract() {
		ReportAbstract reportAbstract = new ReportAbstract();
		reportAbstract.setAlgrithm("DESeq");
		List<Species> lsBlastToSpecies = new ArrayList<Species>();
		lsBlastToSpecies.add(new Species(10090));
		reportAbstract.setBlastToSpecies(lsBlastToSpecies);
		reportAbstract.setSamples("一", "二", "三");
		reportAbstract.setSequence("Life technologies Ion Proton Sequencer");
		reportAbstract.setSoftware(SoftWare.mapsplice);
		reportAbstract.setSpeciesName(new Species(10090));
		
		TemplateRender templateRender = new TemplateRender();
		Writer out = new BufferedWriter(new OutputStreamWriter(FileOperate.getOutputStream("/home/novelbio/jpx/AbstractReport.tex", true)));
		templateRender.render("/templateLatex/Abstract.ftl", reportAbstract.getMapKey2Param(), out);
		assertEquals(true, FileOperate.isFileExist("/home/novelbio/jpx/AbstractReport.tex"));
		
	}
	
	public void testReportAlternativeSplicing() {
		ReportAlternativeSplicing reportAlternativeSplicing = new ReportAlternativeSplicing();
		reportAlternativeSplicing.setGroupName("S360dvsWT0d");
	}
	
	public void testReportRNAassembly() {
		ReportRNAassembly reportRNAassembly = new ReportRNAassembly();
//		Map<String, Object> mapKey2SampleInfo = new HashMap<String, Object>();
//		mapKey2SampleInfo.put("sampleName", "S360d");
//		mapKey2SampleInfo.put("RawReads", 100);
//		mapKey2SampleInfo.put("CleanReads", 100);
//		mapKey2SampleInfo.put("contigNum", 0.8567468746);
//		mapKey2SampleInfo.put("characterNum", 100);
//		mapKey2SampleInfo.put("avgContigLen", 0.8566546);
//		mapKey2SampleInfo.put("minContigLen", 100);
//		mapKey2SampleInfo.put("contigN50Len", 100);
//		mapKey2SampleInfo.put("readsMappingRate", 100);
//		reportRNAassembly.addSampleInfo(mapKey2SampleInfo);
		
		List<String[]> lsGroupAndGeneNum = new ArrayList<String[]>();
		String[] groupAndGeneNum = new String[4];
		groupAndGeneNum[0] = "caseVSControl";
		groupAndGeneNum[1] = "difExpNum";
		groupAndGeneNum[2] = "upGeneNum";
		groupAndGeneNum[3] = "downGeneNum";
		lsGroupAndGeneNum.add(groupAndGeneNum);
		lsGroupAndGeneNum.add(groupAndGeneNum);
		lsGroupAndGeneNum.add(groupAndGeneNum);
		lsGroupAndGeneNum.add(groupAndGeneNum);
		lsGroupAndGeneNum.add(groupAndGeneNum);
		
		ReportTable reportTable = new ReportTable();
		reportRNAassembly.addTable(reportTable.getMapKey2Param("test", "tabltest", lsGroupAndGeneNum, 4));

		TemplateRender templateRender = new TemplateRender();
		Writer out = new BufferedWriter(new OutputStreamWriter(FileOperate.getOutputStream("/home/novelbio/jpx/RNAassemblyReport.tex", true)));
		templateRender.render("/templateLatex/RNAassembly_result.ftl", reportRNAassembly.getMapKey2Param(), out);
		assertEquals(true, FileOperate.isFileExist("/home/novelbio/jpx/RNAassemblyReport.tex"));
	}
	

}
