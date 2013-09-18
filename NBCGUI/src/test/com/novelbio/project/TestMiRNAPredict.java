package com.novelbio.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.novelbio.analysis.seq.AlignSeq;
import com.novelbio.analysis.seq.FormatSeq;
import com.novelbio.analysis.seq.bed.BedSeq;
import com.novelbio.analysis.seq.genome.GffChrAbs;
import com.novelbio.analysis.seq.mirna.CtrlMiRNApredict;
import com.novelbio.analysis.seq.sam.SamFile;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.database.model.species.Species;

public class TestMiRNAPredict {
	Map<String, String[]> mapParams;
	
	@Before
	public void init(){
		mapParams = new HashMap<String, String[]>();
		//TODO 更改输入的文件路径
		mapParams.put("inputData", new String[]{"/hdfs:/nbCloud/public/test/miRNA/test_miRNA.fq,/hdfs:/nbCloud/public/test/miRNA/test1_miRNA.fq"});
		mapParams.put("inputDataPrefix", new String[]{"test,test1"});
		mapParams.put("taxId", new String[]{"3694"});
		mapParams.put("speciesVersion", new String[]{"Populus_210"});
		mapParams.put("dbType", new String[]{"phytozome"});
		mapParams.put("savePath", new String[]{"/hdfs:/nbCloud/staff/gaozhu/我的文档"});
	}
	
	@Test
	public void goRun(){
		String[] inputFiles = mapParams.get("inputData")[0].split(",");
		String[] inputFilePrefixs = mapParams.get("inputDataPrefix")[0].split(",");
		ArrayList<String[]> lsBedFile2Prefix = new ArrayList<>();
		for (int i = 0; i < inputFiles.length; i++) {
			String [] fastqFile2Prefix = new String[2];
			fastqFile2Prefix[0] = inputFiles[i];
			fastqFile2Prefix[1] = inputFilePrefixs[i];
			lsBedFile2Prefix.add(fastqFile2Prefix);
		}
		CtrlMiRNApredict ctrlMiRNApredict = new CtrlMiRNApredict();
		Species species = new Species(Integer.parseInt(mapParams.get("taxId")[0]), mapParams.get("speciesVersion")[0]);
		species.setGffDB(mapParams.get("dbType")[0]);
		GffChrAbs gffChrAbs = new GffChrAbs(species);
		Map<AlignSeq, String> mapBedFile2Prefix = new LinkedHashMap<AlignSeq, String>();
		List<String[]> lsInfo = lsBedFile2Prefix;
		for (String[] strings : lsInfo) {
			AlignSeq alignSeq = null;
			if (FormatSeq.getFileType(strings[0]) == FormatSeq.BED) {
				alignSeq = new BedSeq(strings[0]);
			} else if (FormatSeq.getFileType(strings[0]) == FormatSeq.BAM || FormatSeq.getFileType(strings[0]) == FormatSeq.SAM) {
				alignSeq = new SamFile(strings[0]);
			}
			if (alignSeq != null) {
				mapBedFile2Prefix.put(alignSeq, strings[1]);
			}
		}
		ctrlMiRNApredict.setGffChrAbs(gffChrAbs);
		ctrlMiRNApredict.setSpecies(species);
		ctrlMiRNApredict.setLsSamFile2Prefix(mapBedFile2Prefix);
		ctrlMiRNApredict.setOutPath(FileOperate.addSep(mapParams.get("savePath")[0]));
		
		ctrlMiRNApredict.runMiRNApredict();
		ctrlMiRNApredict.writeToFile();
	}
	
	
	@After
	public void destroy(){
		mapParams = null;
	}
}
