package com.novelbio.project;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.novelbio.analysis.seq.genome.gffOperate.GffHashGene;
import com.novelbio.analysis.seq.mapping.StrandSpecific;
import com.novelbio.database.model.species.Species;
import com.novelbio.database.service.SpringFactory;
import com.novelbio.nbcReport.Params.ReportSamAndRPKMAll;
import com.novelbio.nbcgui.controlseq.CtrlSamPPKMint;


public class TestSamAndRPKM {
	ReportSamAndRPKMAll reportSamAndRPKMAll;
	Map<String, String[]> mapParams;
	@Before
	public void init() {
		mapParams = new HashMap<String, String[]>();
		mapParams.put("prefixArray",new String[]{"test1"});
		mapParams.put("species", new String[]{"10090"});
		mapParams.put("speciesVersion", new String[]{"mmu10_NCBI"});
		mapParams.put("tesUp", new String[]{"-200"});
		mapParams.put("tesDown", new String[]{"200"});
		mapParams.put("taskName", new String[]{"测试Sam"});
		mapParams.put("tssUp", new String[]{"-1500"});
		mapParams.put("tssDown", new String[]{"1500"});
		mapParams.put("expressCount", new String[]{"1"});
		mapParams.put("strandType", new String[]{"NONE"});
		mapParams.put("taskState", new String[]{"-1500"});
		mapParams.put("tssUp", new String[]{"1"});
		mapParams.put("standardize", new String[]{"FPKM"});
		mapParams.put("inFileArray", new String[]{"/hdfs:/nbCloud/staff/gaozhu/我的文档/RNASeqMap_result/test1_junctions.bed"});
		mapParams.put("dbType", new String[]{"NCBI"});
		mapParams.put("outFileName", new String[]{"/hdfs:/nbCloud/staff/gaozhu/我的图片/"});
		mapParams.put("gtfFile", new String[]{""});
		
	}
	
	
	@Test
	public void runTest() {
		CtrlSamPPKMint ctrlSamRPKMLocate = (CtrlSamPPKMint)SpringFactory.getFactory().getBean("ctrlSamRPKMLocate");
		int speciesId = Integer.parseInt(mapParams.get("species")[0]);
		String expressCount =  mapParams.get("expressCount")[0];
		String strandType = mapParams.get("strandType")[0];
		String standardize = mapParams.get("standardize")[0];
		int tssUp = Integer.parseInt(mapParams.get("tssUp")[0]);
		int tssDown = Integer.parseInt(mapParams.get("tssDown")[0]);
		int tesUp = Integer.parseInt(mapParams.get("tesUp")[0]);
		int tesDown = Integer.parseInt(mapParams.get("tesDown")[0]);
		String outFolder = mapParams.get("outFileName")[0];
		String[] inFileArray =  mapParams.get("inFileArray")[0].split(",");
		String[] prefixArray = mapParams.get("prefixArray")[0].split(",");
		String useGTF = null;
		try {
			useGTF = mapParams.get("useGTF")[0];
		} catch (Exception e1) {
			useGTF = null;
		}
		boolean isUseGTF;
		if (useGTF == null) {
			isUseGTF = false;
		}else {
			isUseGTF = true;
		}
		if (isUseGTF) {
			String gtfFile =  mapParams.get("gtfFile")[0];
			GffHashGene gffHashGene = new GffHashGene(gtfFile);
			ctrlSamRPKMLocate.setGffHash(gffHashGene);
		}else {
			Species species = new Species(speciesId);
			String speciesVersion = mapParams.get("speciesVersion")[0];
			String dbType = mapParams.get("dbType")[0];
			String keyVersion = species.getMapVersion().get(speciesVersion);
			species.setVersion(keyVersion);
			String keyDBType = species.getMapGffDBAll().get(dbType);
			species.setGffDB(keyDBType);
			ctrlSamRPKMLocate.setSpecies(species);
		}
		
		List<String[]> lsList = new ArrayList<>();
		for (int i = 0; i < prefixArray.length; i++) {
			String[] lsLines = new String[]{inFileArray[i],prefixArray[i]};
			lsList.add(lsLines);
		}
		
		ctrlSamRPKMLocate.setQueryFile(lsList);
		boolean isFPKM = false;
		if (standardize.equals("FPKM")) {
			isFPKM = true;
		}
		boolean isExpressCount;
		if (expressCount == null) {
			isExpressCount = false;
		}else {
			isExpressCount = true;
		}
		ctrlSamRPKMLocate.setIsCountRPKM(isExpressCount, StrandSpecific.getMapStrandLibrary().get(strandType), isFPKM);
		int[] tss = new int[]{tssUp,tssDown};
		int[] tes = new int[]{tesUp,tesDown};
		ctrlSamRPKMLocate.setTssRange(tss);
		ctrlSamRPKMLocate.setTesRange(tes);
		ctrlSamRPKMLocate.setResultPrefix(outFolder);
		ctrlSamRPKMLocate.run();
	
	}

}
