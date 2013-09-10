package com.novelbio.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.novelbio.analysis.seq.genome.GffChrAbs;
import com.novelbio.analysis.seq.mirna.CtrlMiRNAfastq;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.database.model.species.Species;
import com.novelbio.generalConf.PathDetailNBC;

public class TestMiRNASeqAnalysis {
	Map<String, String[]> mapParams;
	
	@Before
	public void init(){
		mapParams = new HashMap<String, String[]>();
		mapParams.put("inputData", new String[]{"/hdfs:/nbCloud/public/test/miRNA/test_miRNA.fq,/hdfs:/nbCloud/public/test/miRNA/test1_miRNA.fq"});
		mapParams.put("inputDataPrefix", new String[]{"test,test1"});
		mapParams.put("taxId", new String[]{"3694"});
		mapParams.put("mappingAllToGenome", new String[]{"1"});
		mapParams.put("mappingAllToRfam", new String[]{"1"});
		mapParams.put("mappingToSpecies", new String[]{"1"});
		mapParams.put("speciesVersion", new String[]{"Populus_210"});
		mapParams.put("dbType", new String[]{"phytozome"});
		mapParams.put("savePath", new String[]{"/hdfs:/nbCloud/staff/gaozhu/我的文档"});
	}
	
	@Test
	public void goRun(){
		String[] inputFiles = mapParams.get("inputData")[0].split(",");
		String[] inputFilePrefixs = mapParams.get("inputDataPrefix")[0].split(",");
		ArrayList<String[]> lsfastqFile2Prefix = new ArrayList<>();
		for (int i = 0; i < inputFiles.length; i++) {
			String [] fastqFile2Prefix = new String[2];
			fastqFile2Prefix[0] = inputFiles[i];
			fastqFile2Prefix[1] = inputFilePrefixs[i];
			lsfastqFile2Prefix.add(fastqFile2Prefix);
		}
		
		CtrlMiRNAfastq ctrlMiRNAfastq = new CtrlMiRNAfastq();
		Species species = new Species(Integer.parseInt(mapParams.get("taxId")[0]), mapParams.get("speciesVersion")[0]);
		species.setGffDB(mapParams.get("dbType")[0]);
		GffChrAbs gffChrAbs = new GffChrAbs(species);
		ctrlMiRNAfastq.clear();
		ctrlMiRNAfastq.setMappingAll2Genome(mapParams.get("mappingAllToGenome") != null);
		ctrlMiRNAfastq.setRfamSpeciesSpecific(mapParams.get("mappingToSpecies") != null);
		ctrlMiRNAfastq.setSpecies(species);
		ctrlMiRNAfastq.setOutPath(FileOperate.addSep(mapParams.get("savePath")[0]));
		ctrlMiRNAfastq.setGffChrAbs(gffChrAbs);
		ctrlMiRNAfastq.setLsFastqFile(lsfastqFile2Prefix);
		ctrlMiRNAfastq.setMiRNAinfo(PathDetailNBC.getMiRNADat());
		ctrlMiRNAfastq.setRfamFile(PathDetailNBC.getRfamTab());
		ctrlMiRNAfastq.setMapAll2Rfam(mapParams.get("mappingAllToRfam") != null);
		ctrlMiRNAfastq.mappingAndCounting();
		ctrlMiRNAfastq.writeToFile();
	}
	
	@After
	public void destroy(){
		mapParams = null;
	}
}
