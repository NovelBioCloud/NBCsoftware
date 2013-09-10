package com.novelbio.project;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.novelbio.analysis.seq.bed.BedSeq;

public class TestBedOperate {
	Map<String, String[]> mapParams;
	
	@Before
	public void init(){
		mapParams = new HashMap<String, String[]>();
		mapParams.put("inputData", new String[]{"/hdfs:/nbCloud/public/test/miRNA/test_miRNA.fq,/hdfs:/nbCloud/public/test/miRNA/test1_miRNA.fq"});
		mapParams.put("extend", new String[]{"1"});
		mapParams.put("extendValue", new String[]{"10"});
		mapParams.put("mappingNumFrom", new String[]{"0"});
		mapParams.put("mappingNumTo", new String[]{"20"});
		mapParams.put("filterReads", new String[]{"1"});
		mapParams.put("filterReadsValue", new String[]{"trans"});
		mapParams.put("sortBed", new String[]{"1"});
		mapParams.put("savePath", new String[]{"/hdfs:/nbCloud/staff/gaozhu/我的文档"});
	}
	
	@Test
	public void goRun(){
		String[] inputFiles = mapParams.get("inputData")[0].split(",");
		for (int i = 0; i < inputFiles.length; i++) {
			BedSeq bedSeq = new BedSeq(inputFiles[i]);
			if (mapParams.get("extend") != null) {
				int extendLen = Integer.parseInt(mapParams.get("extendValue")[0]);
				bedSeq = bedSeq.extend(extendLen);
			}
			if (mapParams.get("filterReads") != null) {
				Boolean strand = null;
				strand = mapParams.get("filterReadsValue")[0].equalsIgnoreCase("trans");
				int small = Integer.parseInt(mapParams.get("mappingNumFrom")[0]);
				int big = Integer.parseInt(mapParams.get("mappingNumTo")[0]);
				bedSeq = bedSeq.filterSeq(small, big, strand);
			}
			if (mapParams.get("sortBed") != null) {
				bedSeq.sort();
			}
		}
	}
	
	@After
	public void destroy(){
		mapParams = null;
	}
}
