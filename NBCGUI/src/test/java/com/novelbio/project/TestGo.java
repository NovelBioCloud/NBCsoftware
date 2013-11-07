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
import com.novelbio.database.service.SpringFactory;
import com.novelbio.nbcReport.Params.EnumReport;
import com.novelbio.nbcReport.Params.ReportProject;
import com.novelbio.nbcgui.controltest.CtrlTestGOInt;

public class TestGo {
	Map<String, String[]> mapParams;
	
	@Before
	public void init(){
		mapParams = new HashMap<String, String[]>();
		mapParams.put("inputData", new String[]{FileHadoop.getHdfsHeadSymbol("/nbCloud/public/test/gopath/A18VSA6-Dif_anno.xls"),FileHadoop.getHdfsHeadSymbol("/nbCloud/public/test/gopath/D6VSD0-Dif_anno.xls")});
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
		mapParams.put("background", new String[]{FileHadoop.getHdfsHeadSymbol("/nbCloud/public/test/gopath/maizeBG.txt")});
		mapParams.put("savePath", new String[]{FileHadoop.getHdfsHeadSymbol("/nbCloud/staff/gaozhu/我的文档/")});
	}
	
	@Test
	public void goRun(){
		String[] excelFiles = mapParams.get("inputData");
		String[] excelPrefixs = mapParams.get("inputDataPrefix");
		for (int i = 0; i < excelFiles.length; i++) {
			CtrlTestGOInt ctrlGO = (CtrlTestGOInt)SpringFactory.getFactory().getBean("ctrlGOall");
			Assert.assertNotNull(ctrlGO);
			ctrlGO.clearParam();
			GoAlgorithm goAlgorithm = GoAlgorithm.valueOf(mapParams.get("goAlgorithm")[0]);
			Assert.assertNotNull(goAlgorithm);
			ctrlGO.setGoAlgorithm(goAlgorithm);
			int taxID = -1;
			taxID = Integer.parseInt(mapParams.get("querySpecies")[0]);
			ctrlGO.setTaxID(taxID);
			int colAccID = Integer.parseInt(mapParams.get("acclDColNum")[0]);
			int colFC = Integer.parseInt(mapParams.get("valueColNum")[0]);
			ArrayList<String[]> lsAccID = null;
			if (colAccID != colFC) {
				lsAccID = ExcelTxtRead.readLsExcelTxt(excelFiles[i], new int[]{colAccID, colFC}, 1, 0);
			} else {
				lsAccID = ExcelTxtRead.readLsExcelTxt(excelFiles[i], new int[]{colAccID}, 1, 0);
			}
			Assert.assertNotNull(lsAccID);
			double evalue = 1e-10;
			List<Integer> lsStaxID = new ArrayList<Integer>();
			if (mapParams.get("blast") != null) {
				for (String taxIDString : mapParams.get("blastSpecies")) {
					lsStaxID.add(Integer.parseInt(taxIDString));
				}
			}
			ctrlGO.setBlastInfo(evalue, lsStaxID);
					
			if (mapParams.get("goLevel") != null) {
				ctrlGO.setGOlevel((Integer.parseInt(mapParams.get("goLevelNum")[0])));
			} else {
				ctrlGO.setGOlevel(-1);
			}
			
			if (mapParams.get("clusterGoPath") == null || colAccID == colFC) {
				double up = 0; double down = 0;
				if ( colAccID != colFC) {
					up = Double.parseDouble(mapParams.get("upValue")[0]);
					down = Double.parseDouble(mapParams.get("downValue")[0]);
				}
				ctrlGO.setUpDown(up, down);
				ctrlGO.setIsCluster(false);
			} else {
				ctrlGO.setIsCluster(mapParams.get("clusterGoPath") != null);
			}
			ctrlGO.setLsBG(mapParams.get("background")[0]);
			ctrlGO.setLsAccID2Value(lsAccID);
			ctrlGO.setTeamName(excelPrefixs[i]);
			ctrlGO.run();
			ctrlGO.saveExcel_And_GetGoType2File(FileOperate.addSep(mapParams.get("savePath")[0])+excelPrefixs[i]);
			ctrlGO.savePic_And_GetPre2Pic();
			ctrlGO.getReportGO().writeAsFile(FileOperate.addSep(mapParams.get("savePath")[0]) + EnumReport.GOAnalysis.getResultFolder());
		}
	}
	
	@Test
	public void runReport() throws Exception {
		List<String> lsFolders = new ArrayList<>();
		lsFolders.add(FileHadoop.getHdfsHeadSymbol("/nbCloud/staff/gaozhu/我的文档/"+EnumReport.GOAnalysis.getResultFolder()));
		ReportProject reportProject = new ReportProject(lsFolders);
		String reportTest = "/home/novelbio/桌面/testReportGO.docx";
		reportProject.outputReport(reportTest);
		Assert.assertNotNull(FileOperate.isFileExist(reportTest));
	}
	
	@After
	public void destroy(){
		mapParams = null;
	}
}
