package com.novelbio.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.novelbio.base.dataStructure.ArrayOperate;
import com.novelbio.base.fileOperate.FileHadoop;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.database.service.SpringFactory;
import com.novelbio.nbcReport.Params.EnumReport;
import com.novelbio.nbcReport.Params.ReportProject;
import com.novelbio.nbcgui.controlseq.CtrlFastQ;

public class TestFastQC {
	Map<String, String[]> mapParams;
	
	@Before
	public void init(){
		mapParams = new HashMap<String, String[]>();
		mapParams.put("leftFqs", new String[]{
				FileHadoop.getHdfsHeadSymbol("/nbCloud/public/test/fastq/testFastQLeft.fq"),
				FileHadoop.getHdfsHeadSymbol("/nbCloud/public/test/fastq/test1FastQLeft.fq")});
		mapParams.put("prefixs", new String[]{"test","test1"});
		mapParams.put("rightFq", new String[]{
				FileHadoop.getHdfsHeadSymbol("/nbCloud/public/test/fastq/testFastQRight.fq"),
				FileHadoop.getHdfsHeadSymbol("/nbCloud/public/test/fastq/test1FastQRight.fq")});
		mapParams.put("leftAdaptor", new String[]{""});
		mapParams.put("rightAdaptor", new String[]{""});
//		mapParams.put("lowCaseAdaptor", new String[]{"1"});
		mapParams.put("readsQuality", new String[]{"20"});
		mapParams.put("trimEnd", new String[]{"1"});
		mapParams.put("savePath", new String[]{FileHadoop.getHdfsHeadSymbol("/nbCloud/staff/gaozhu/我的文档/")});
		mapParams.put("qcBeforeFilter", new String[]{"1"});
		mapParams.put("qcAfterFilter", new String[]{"1"});
		mapParams.put("retainBp", new String[]{"50"});
		
	}
	
	@Test
	public void pathWayRun(){
		CtrlFastQ ctrlFastQ = (CtrlFastQ)SpringFactory.getFactory().getBean("ctrlFastQ");
		ArrayList<String> lsLeftFq = ArrayOperate.converArray2List(mapParams.get("leftFqs")[0].split(","));
		ArrayList<String> lsPrefix = ArrayOperate.converArray2List(mapParams.get("prefixs")[0].split(","));
		ArrayList<String> lsRightFq = ArrayOperate.converArray2List(mapParams.get("rightFq")[0].split(","));
		ctrlFastQ.setLsFastQfileLeft(lsLeftFq);
		ctrlFastQ.setLsFastQfileRight(lsRightFq);
		ctrlFastQ.setLsPrefix(lsPrefix);
		ctrlFastQ.setFilter(true);
		ctrlFastQ.setAdaptorLeft(mapParams.get("leftAdaptor")[0]);
		ctrlFastQ.setAdaptorRight(mapParams.get("rightAdaptor")[0]);
		ctrlFastQ.setAdaptorLowercase(mapParams.get("lowCaseAdaptor") != null);
		ctrlFastQ.setFastqQuality(Integer.parseInt(mapParams.get("readsQuality")[0]));
		ctrlFastQ.setTrimNNN(mapParams.get("trimEnd") != null);
		ctrlFastQ.setOutFilePrefix( mapParams.get("savePath")[0]);
		ctrlFastQ.setFastQC(mapParams.get("qcBeforeFilter") != null, mapParams.get("qcAfterFilter") != null);
		ctrlFastQ.setReadsLenMin(Integer.parseInt(mapParams.get("retainBp")[0]));
		ctrlFastQ.running();
		String reportPath = FileOperate.addSep(mapParams.get("savePath")[0]) + EnumReport.FastQC.getResultFolder();
		ctrlFastQ.getReportQCAll().outputReportXdoc(reportPath);
		Assert.assertTrue(FileOperate.isFileExist(reportPath + FileOperate.getSepPath() + EnumReport.FastQC.getReportXdocFileName()));
	}
	
	//@Test
	public void runReport() {
		List<String> lsFolders = new ArrayList<>();
		lsFolders.add(FileHadoop.getHdfsHeadSymbol("/nbCloud/staff/gaozhu/我的文档/"+EnumReport.FastQC.getResultFolder()));
		ReportProject reportProject = new ReportProject(lsFolders);
		String reportTest = "/home/novelbio/桌面/testReportFastQC.docx";
		reportProject.outputReport(reportTest);
		Assert.assertNotNull(FileOperate.isFileExist(reportTest));
	}
	
	@After
	public void destroy(){
		mapParams = null;
	}
}
