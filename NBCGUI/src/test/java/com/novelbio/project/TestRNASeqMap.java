package com.novelbio.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.novelbio.analysis.seq.genome.GffChrAbs;
import com.novelbio.analysis.seq.mapping.MapBowtie;
import com.novelbio.analysis.seq.mapping.MapLibrary;
import com.novelbio.analysis.seq.mapping.MapTophat;
import com.novelbio.analysis.seq.mapping.StrandSpecific;
import com.novelbio.base.dataOperate.TxtReadandWrite;
import com.novelbio.base.dataStructure.ArrayOperate;
import com.novelbio.base.fileOperate.FileHadoop;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.database.model.species.Species;
import com.novelbio.nbcReport.Params.EnumReport;
import com.novelbio.nbcReport.Params.ReportProject;
import com.novelbio.nbcgui.controlseq.CopeFastq;
import com.novelbio.nbcgui.controlseq.CtrlRNAmap;

public class TestRNASeqMap {
	Map<String, String[]> mapParams;
	
	@Before
	public void init(){
		mapParams = new HashMap<String, String[]>();
		mapParams.put("leftInputData", new String[]{"/hdfs:/nbCloud/public/test/DNASeqMap/test_filtered_1.fq.gz,/hdfs:/nbCloud/nbCloud/public/test/DNASeqMap/test1_filtered_1.fq.gz"});
		mapParams.put("leftInputDataPrefix", new String[]{"test,test1"});
		mapParams.put("rightInputData", new String[]{"/hdfs:/nbCloud/public/test/DNASeqMap/test_filtered_2.fq.gz,/hdfs:/nbCloud/nbCloud/public/test/DNASeqMap/test1_filtered_2.fq.gz"});
		mapParams.put("useGTF", new String[]{"1"});
		mapParams.put("library", new String[]{"PairEnd"});
		mapParams.put("thread", new String[]{"2"});
		mapParams.put("mappingToFile", new String[]{""});
		mapParams.put("GTFfile", new String[]{""});
		mapParams.put("strandType", new String[]{"NONE"});
		mapParams.put("savePath", new String[]{"/hdfs:/nbCloud/staff/gaozhu/我的文档"});
		mapParams.put("algorithm", new String[]{"tophat"});
		mapParams.put("sensitive", new String[]{"Sensitive"});
		mapParams.put("taxId", new String[]{"10090"});
		mapParams.put("speciesVersion", new String[]{"mm10_NCBI"});
	}
	
	//@Test
	public void rnaMapingRun(){
		CtrlRNAmap ctrlRNAmap = null;
		if (mapParams.get("algorithm")[0].equalsIgnoreCase("rsem")) {
			ctrlRNAmap = new CtrlRNAmap(CtrlRNAmap.RSEM);
		}
		else if (mapParams.get("algorithm")[0].equalsIgnoreCase("tophat")) {
			ctrlRNAmap = new CtrlRNAmap(CtrlRNAmap.TOP_HAT);
		}

		int taxID = Integer.parseInt(mapParams.get("taxId")[0]);
		String version = mapParams.get("speciesVersion")[0];
		Species species = new Species(taxID,version);
		int threadNum = 4;
		try { threadNum = Integer.parseInt(mapParams.get("thread")[0]); } catch (Exception e1) { }
		
		CopeFastq copeFastq = new CopeFastq();
		copeFastq.setLsFastQfileLeft(ArrayOperate.converArray2List(mapParams.get("leftInputData")[0].split(",")));
		copeFastq.setLsFastQfileRight(ArrayOperate.converArray2List(mapParams.get("rightInputData")[0].split(",")));
		copeFastq.setLsCondition(ArrayOperate.converArray2List(mapParams.get("leftInputDataPrefix")[0].split(",")));
		
		ctrlRNAmap.setMapPrefix2LsFastq(copeFastq);
		
		if (taxID == 0) {
			ctrlRNAmap.setGtfAndGene2Iso(mapParams.get("GTFfile")[0]);
			ctrlRNAmap.setIndexFile(mapParams.get("mappingToFile")[0]);
			
		} else {
			ctrlRNAmap.setGffChrAbs(new GffChrAbs(species));
		}
		ctrlRNAmap.setLibrary(MapLibrary.valueOf(mapParams.get("library")[0]));
		ctrlRNAmap.setStrandSpecifictype(StrandSpecific.valueOf(mapParams.get("strandType")[0]));
		ctrlRNAmap.setThreadNum(threadNum);
		ctrlRNAmap.setIsUseGTF(mapParams.get("useGTF") != null);
		ctrlRNAmap.setSensitive(MapBowtie.getMapSensitive().get(mapParams.get("sensitive")[0]));
		ctrlRNAmap.setOutPathPrefix(mapParams.get("savePath")[0]);
		ctrlRNAmap.mapping();
		if (mapParams.get("algorithm")[0].equalsIgnoreCase("rsem")) {
			TxtReadandWrite txtWriteRpkm = new TxtReadandWrite(ctrlRNAmap.getOutPrefix() + "ResultRPKM.xls", true);
			TxtReadandWrite txtWriteCounts = new TxtReadandWrite(ctrlRNAmap.getOutPrefix() + "ResultCounts.xls", true);
			txtWriteRpkm.ExcelWrite(ctrlRNAmap.getLsExpRsemRPKM());
			txtWriteCounts.ExcelWrite(ctrlRNAmap.getLsExpRsemCounts());
			txtWriteRpkm.close();
			txtWriteCounts.close();
		}
	}
	
	@Test
	public void runReport() throws Exception {
		List<String> lsFolders = new ArrayList<>();
		lsFolders.add(FileHadoop.getHdfsHeadSymbol("/nbCloud/staff/gaozhu/我的文档/"+EnumReport.RNASeqMap.getResultFolder()));
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
