package com.novelbio.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.novelbio.analysis.annotation.functiontest.TopGO.GoAlgorithm;
import com.novelbio.base.dataOperate.ExcelTxtRead;
import com.novelbio.base.fileOperate.FileHadoop;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.database.model.species.Species;
import com.novelbio.database.service.SpringFactory;
import com.novelbio.nbcReport.Params.EnumReport;
import com.novelbio.nbcReport.Params.ReportGOAll;
import com.novelbio.nbcReport.Params.ReportPathWay;
//import com.novelbio.nbcReport.Params.ReportPathWayAll;
import com.novelbio.nbcReport.Params.ReportAll;
import com.novelbio.nbcgui.controltest.CtrlTestPathInt;

public class TestPathway {
	Map<String, String[]> mapParams;
	
	@Before
	public void init(){
		mapParams = new HashMap<String, String[]>();
//		mapParams.put("inputData", new String[]{FileHadoop.addHdfsHeadSymbol("/nbCloud/public/test/gopath/A18VSA6-Dif_anno.xls"),FileHadoop.addHdfsHeadSymbol("/nbCloud/public/test/gopath/D6VSD0-Dif_anno.xls")});
		mapParams.put("inputData", new String[]{"C:\\gopath\\A18VSA6-Dif_anno.xls", "C:\\gopath\\D6VSD0-Dif_anno.xls"});
		mapParams.put("inputDataPrefix", new String[]{"test1","test2"});
		mapParams.put("goAlgorithm", new String[]{GoAlgorithm.novelgo.name()});
		mapParams.put("querySpecies", new String[]{"4577"});
		mapParams.put("acclDColNum", new String[]{"1"});
		mapParams.put("valueColNum", new String[]{"5"});
		mapParams.put("blast", new String[]{"1"});
		mapParams.put("blastSpecies", new String[]{"4513","3702"});
//		mapParams.put("goLevel", new String[]{"0"});
		mapParams.put("goLevelNum", new String[]{"0"});
//		mapParams.put("clusterGoPath", new String[]{});
		mapParams.put("upValue", new String[]{"1"});
		mapParams.put("downValue", new String[]{"-1"});
//		mapParams.put("background", new String[]{FileHadoop.addHdfsHeadSymbol("/nbCloud/public/test/gopath/maizeBG.txt")});
		mapParams.put("background", new String[]{"C:\\gopath\\maizeBG.txt"});
//		mapParams.put("savePath", new String[]{FileHadoop.addHdfsHeadSymbol("/nbCloud/staff/gaozhu/我的文档/")});
		mapParams.put("savePath", new String[]{"C:\\我的文档\\"});
	}
	
	@Test
	public void pathWayRun(){
		System.setProperty("hadoop.home.dir", "C:/hadoop/hadoop-2.5.0_compile_win/hadoop-2.5.0");
		String[] excelFiles = mapParams.get("inputData");
		String[] excelPrefixs = mapParams.get("inputDataPrefix");
		for (int i = 0; i < excelFiles.length; i++) {
			int colAccID = Integer.parseInt(mapParams.get("acclDColNum")[0]);
			int colFC = Integer.parseInt(mapParams.get("valueColNum")[0]);
			int taxID = -1;
			taxID = Integer.parseInt(mapParams.get("querySpecies")[0]);
			String backGroundFile = mapParams.get("background")[0];
			ArrayList<String[]> lsAccID = null;
			if (colAccID != colFC) {
				lsAccID = ExcelTxtRead.readLsExcelTxt(excelFiles[i], new int[]{colAccID, colFC}, 1, 0);
				
			} else {
				lsAccID = ExcelTxtRead.readLsExcelTxt(excelFiles[i], new int[]{colAccID}, 1, 0);
			}
			double evalue = 1e-10;
			List<Integer> lsStaxID = new ArrayList<Integer>();
			if (mapParams.get("blast") != null) {
				for (String taxIDString : mapParams.get("blastSpecies")) {
					lsStaxID.add(Integer.parseInt(taxIDString));
				}
			}
			CtrlTestPathInt ctrlPath = (CtrlTestPathInt)SpringFactory.getFactory().getBean("ctrlPath");
			ctrlPath.clearParam();
			Species species = new Species(taxID);
			ctrlPath.setTaxID(species);
			ctrlPath.setBlastInfo(evalue, lsStaxID);
			ctrlPath.setLsBG(backGroundFile);
			if (mapParams.get("clusterGoPath") == null || colAccID == colFC) {
				double up = 0; double down = 0;
				if ( colAccID != colFC) {
					up = Double.parseDouble(mapParams.get("upValue")[0]);
					down = Double.parseDouble(mapParams.get("downValue")[0]);
				}
				ctrlPath.setUpDown(up, down);
				ctrlPath.setIsCluster(false);
			} else {
				ctrlPath.setIsCluster(mapParams.get("clusterGoPath") != null);
			}
			ctrlPath.setLsAccID2Value(lsAccID);
			ctrlPath.setTeamName(excelPrefixs[i]);
			ctrlPath.run();
			String[] ss = mapParams.get("savePath");
			System.out.println(ss[0]);
			ctrlPath.saveExcel(FileOperate.addSep(mapParams.get("savePath")[0])+excelPrefixs[i]);
			ReportPathWay repPath = ctrlPath.getReportPathWay();
			for (String file : repPath.getSetResultRealFiles()) {
				Assert.assertTrue(FileOperate.isFileExist(file));
			}
			repPath.writeAsFile(FileOperate.addSep(mapParams.get("savePath")[0]) + EnumReport.PathWay.getResultFolder());
		}
		
	}
	
	@Test
	public void runReport() throws Exception {
		System.setProperty("hadoop.home.dir", "C:/hadoop/hadoop-2.5.0_compile_win/hadoop-2.5.0");
		List<String> lsFolders = new ArrayList<>();
//		lsFolders.add(FileHadoop.addHdfsHeadSymbol("/nbCloud/staff/gaozhu/我的文档/"+EnumReport.PathWay.getResultFolder()));
		lsFolders.add("C:\\我的文档\\"+EnumReport.PathWay.getResultFolder());
//		ReportAll reportProject = new ReportAll(lsFolders);
//		String reportTest = "/home/novelbio/桌面/testReportPathWay.docx";
		String reportTest = "C:\\Documents and Settings\\Administrator\\桌面\\testReportPathWay.docx";
//		reportProject.outputReport(reportTest);
		Assert.assertNotNull(FileOperate.isFileExist(reportTest));
	}
	
	@After
	public void destroy(){
		mapParams = null;
	}
}
