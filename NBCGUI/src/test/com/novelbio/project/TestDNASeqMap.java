package com.novelbio.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.novelbio.analysis.seq.mapping.MapLibrary;
import com.novelbio.base.dataStructure.ArrayOperate;
import com.novelbio.base.fileOperate.FileHadoop;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.database.domain.information.SoftWareInfo.SoftWare;
import com.novelbio.database.model.species.Species;
import com.novelbio.nbcReport.Params.EnumReport;
import com.novelbio.nbcReport.Params.ReportProject;
import com.novelbio.nbcgui.controlseq.CopeFastq;
import com.novelbio.nbcgui.controlseq.CtrlDNAMapping;

public class TestDNASeqMap {
	Map<String, String[]> mapParams;
	
	@Before
	public void init(){
		mapParams = new HashMap<String, String[]>();
		mapParams.put("leftInputData", new String[]{"/hdfs:/nbCloud/public/test/DNASeqMap/test_filtered_1.fq.gz", "/hdfs:/nbCloud/public/test/DNASeqMap/test1_filtered_1.fq.gz"});
		mapParams.put("leftInputDataPrefix", new String[]{"test,test1"});
		mapParams.put("rightInputData", new String[]{"/hdfs:/nbCloud/public/test/DNASeqMap/test_filtered_2.fq.gz", "/hdfs:/nbCloud/public/test/DNASeqMap/test1_filtered_2.fq.gz"});
		mapParams.put("gapLength", new String[]{"30"});
		mapParams.put("mismatch", new String[]{"5"});
		mapParams.put("thread", new String[]{"2"});
		mapParams.put("mappingToFile", new String[]{""});
		mapParams.put("library", new String[]{"PairEnd"});
		mapParams.put("savePath", new String[]{"/hdfs:/nbCloud/staff/gaozhu/我的图片"});
		mapParams.put("software", new String[]{"bwa"});
		mapParams.put("taxId", new String[]{"10090"});
		mapParams.put("speciesVersion", new String[]{"mm10_NCBI"});
		mapParams.put("mappingTo", new String[]{"8"});
	}
	
	@Test
	public void dnaMapingRun(){
		CtrlDNAMapping ctrlDNAMapping = new CtrlDNAMapping();
		CopeFastq copeFastq = new CopeFastq();
		copeFastq.setLsFastQfileLeft(ArrayOperate.converArray2List(mapParams.get("leftInputData")[0].split(",")));
		copeFastq.setLsFastQfileRight(ArrayOperate.converArray2List(mapParams.get("rightInputData")[0].split(",")));
		copeFastq.setLsCondition(ArrayOperate.converArray2List(mapParams.get("leftInputDataPrefix")[0].split(",")));
		ctrlDNAMapping.setCopeFastq(copeFastq);
		ctrlDNAMapping.setGapLen(Integer.parseInt(mapParams.get("gapLength")[0]));
		ctrlDNAMapping.setMismatch(Double.parseDouble(mapParams.get("mismatch")[0]));
		ctrlDNAMapping.setThread(Integer.parseInt(mapParams.get("thread")[0]));
		ctrlDNAMapping.setChrIndexFile(mapParams.get("mappingToFile")[0]);
		ctrlDNAMapping.setLibraryType(MapLibrary.valueOf(mapParams.get("library")[0]));
		ctrlDNAMapping.setSoftMapping(SoftWare.valueOf(mapParams.get("software")[0]));
		int taxID = Integer.parseInt(mapParams.get("taxId")[0]);
		String version = mapParams.get("speciesVersion")[0];
		Species species = new Species(taxID,version);
		System.out.println(species.getChromSeq());
		ctrlDNAMapping.setSpecies(species, Integer.parseInt(mapParams.get("mappingTo")[0]));
		ctrlDNAMapping.setOutFilePrefix(FileOperate.addSep(mapParams.get("savePath")[0]));
		ctrlDNAMapping.running();
	}
	
	//@Test
	public void runReport() throws Exception {
		List<String> lsFolders = new ArrayList<>();
		lsFolders.add(FileHadoop.getHdfsHeadSymbol("/nbCloud/staff/gaozhu/我的文档/"+EnumReport.DNASeqMap.getResultFolder()));
		ReportProject reportProject = new ReportProject(lsFolders);
		String reportTest = "/home/novelbio/桌面/testReportDNASeqMap.docx";
		reportProject.outputReport(reportTest);
		Assert.assertNotNull(FileOperate.isFileExist(reportTest));
	}
	
	@After
	public void destroy(){
		mapParams = null;
	}
}
