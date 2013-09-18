package com.novelbio.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ArrayListMultimap;
import com.novelbio.analysis.seq.bed.BedSeq;
import com.novelbio.analysis.seq.sam.AlignSamReading;
import com.novelbio.analysis.seq.sam.SamFile;
import com.novelbio.analysis.seq.sam.SamToBed;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.database.model.species.Species;
import com.novelbio.nbcgui.controlseq.CtrlDNAMapping;

public class TestSamToBam {
	Map<String, String[]> mapParams;
	
	@Before
	public void init(){
		mapParams = new HashMap<String, String[]>();
		mapParams.put("samInputData", new String[]{"/hdfs:/nbCloud/public/test/samToBam/abc1.sam"});
		mapParams.put("samInputDataPrefix", new String[]{"test"});
		mapParams.put("vcfInputData", new String[]{"test1"});
		mapParams.put("taxId", new String[]{"3694"});
		mapParams.put("mergeByPrefix", new String[]{"1"});
		mapParams.put("addGroupInfo", new String[]{"1"});
		mapParams.put("sortBam", new String[]{"1"});
		mapParams.put("index", new String[]{"1"});
		mapParams.put("removeDuplicate", new String[]{"1"});
		mapParams.put("realign", new String[]{"1"});
		//mapParams.put("recalibrate", new String[]{"1"});
		mapParams.put("generatePileUpFile", new String[]{"1"});
		mapParams.put("toBed", new String[]{"1"});
		mapParams.put("mappingTo", new String[]{"8"});
		mapParams.put("mappingToFile", new String[]{""});
		mapParams.put("toBedOptionNoneUnique", new String[]{"1"});
		mapParams.put("speciesVersion", new String[]{"Populus_210"});
		mapParams.put("savePath", new String[]{"/hdfs:/nbCloud/staff/gaozhu/我的文档"});
	}
	
	@Test
	public void goRun(){
		String[] samInputFiles = mapParams.get("samInputData")[0].split(",");
		String[] samInputFilePrefixs = mapParams.get("samInputDataPrefix")[0].split(",");
		ArrayList<String[]> lsfastqFile2Prefix = new ArrayList<>();
		for (int i = 0; i < samInputFiles.length; i++) {
			String [] fastqFile2Prefix = new String[2];
			fastqFile2Prefix[0] = samInputFiles[i];
			fastqFile2Prefix[1] = samInputFilePrefixs[i];
			lsfastqFile2Prefix.add(fastqFile2Prefix);
		}
		
		ArrayListMultimap<String, String> mapPrefix2FileName = ArrayListMultimap.create();
		HashSet<String> setTmp = new HashSet<String>();
		for (String[] strings : lsfastqFile2Prefix) {
			if (FileOperate.isFileExist(strings[0])) {
				String prefix = getPrefix(strings[1], setTmp);
				mapPrefix2FileName.put(prefix, strings[0]);
			}
		}
		
		String resultMergePath = FileOperate.addSep( mapParams.get("savePath")[0]);
		String saveFolder = resultMergePath + "SamToBam_Result";
		if(!FileOperate.isFileFoldExist(saveFolder)){
			FileOperate.createFolders(saveFolder);
		}
		for (String prefix : mapPrefix2FileName.keySet()) {
			List<String> lsSamFiles = mapPrefix2FileName.get(prefix);
			convertSamFile(saveFolder, prefix, lsSamFiles);
		}
		
	}
	
	
	private void convertSamFile(String resultMergePath, String prefix, List<String> lsSamFilestr) {
		String refFile = "";
		int taxId = Integer.parseInt(mapParams.get("taxId")[0]);
		Species species = new Species(taxId);
		if (species.getTaxID() == 0) {
			refFile = mapParams.get("mappingToFile")[0];
		} else {
			if (CtrlDNAMapping.MAP_TO_CHROM == Integer.parseInt(mapParams.get("mappingTo")[0])) {
				refFile = species.getChromSeq();
			}else if (CtrlDNAMapping.MAP_TO_REFSEQ == Integer.parseInt(mapParams.get("mappingTo")[0])) {
				refFile = species.getRefseqFile(true);
			}
			species.setVersion(mapParams.get("speciesVersion")[0]);
		}
		
		List<SamFile> lsSamFiles = new ArrayList<SamFile>(); 
		for (String string : lsSamFilestr) {
			SamFile samFile = new SamFile(string);
			samFile.setReferenceFileName(refFile);
			lsSamFiles.add(samFile);
		}
		List<SamFile> lsBamFile = new ArrayList<SamFile>();
		for (SamFile samFile : lsSamFiles) {
			lsBamFile.add(samFile.convertToBam());
		}
		
		if (mapParams.get("mergeByPrefix") != null) {
			SamFile samFileMerge = mergeSamFile(resultMergePath, prefix, lsBamFile);
			lsBamFile.clear();
			lsBamFile.add(samFileMerge);
		}
		for (int i = 0; i < lsBamFile.size(); i++) {
			SamFile samFileMerge = lsBamFile.get(i);
			if (lsBamFile.size() == 1) {
				copeSamBamFile(prefix, samFileMerge);
			} else {
				copeSamBamFile(prefix + "_" + (i+1), samFileMerge);
			}
		}
	}
	
	/**
	 * 将输入的文件转化为bam文件，并合并
	 * @param prefix
	 * @param lsSamFile
	 * @return
	 */
	private SamFile mergeSamFile(String resultPath, String prefix, List<SamFile> lsSamFile) {
		if (lsSamFile.size() == 1) {
			return lsSamFile.get(0);
		}
		String resultName = resultPath + prefix;
		resultName = FileOperate.changeFileSuffix(resultName, "_merge", "bam");
		SamFile samFileMerge = SamFile.mergeBamFile(resultName , lsSamFile);
		return samFileMerge;
	}
	
	
	
	private void copeSamBamFile(String prefix, SamFile samFileMerge) {
		if (mapParams.get("addGroupInfo") != null) {
			samFileMerge = samFileMerge.addGroup(prefix, prefix, prefix, null);
		}
		if (mapParams.get("sortBam") != null) {
			samFileMerge = samFileMerge.sort();
		}
		if (mapParams.get("index") != null) {
			samFileMerge.indexMake();
		}
		if (mapParams.get("removeDuplicate") != null) {
			samFileMerge = samFileMerge.removeDuplicate();
			if (samFileMerge == null) {
				return;
			}
		}
		if (mapParams.get("realign") != null) {
			samFileMerge = samFileMerge.addGroup(prefix, prefix, prefix, "ILLUMINA");
			samFileMerge = samFileMerge.realign();
			if (samFileMerge == null) {
				return;
			}
		}
		if (mapParams.get("recalibrate") != null) {
			String[] vcfInputFiles = mapParams.get("vcfInputData")[0].split(",");
			List<String> lsVcfFile = new ArrayList<String>();
			for (String strings : vcfInputFiles) {
				if (FileOperate.isFileExistAndBigThanSize(strings, 0)) {
					lsVcfFile.add(strings);
				}
			}
			if (lsVcfFile.size() != 0) {
				samFileMerge = samFileMerge.recalibrate(lsVcfFile);
				if (samFileMerge == null) {
					return;
				};
			}
		}
		if (mapParams.get("generatePileUpFile") != null) {
			samFileMerge.pileup();
		}
		
		if(mapParams.get("toBed") != null){
			samToBed(samFileMerge);
		}
	}
	
	private void samToBed(SamFile samFile) {
		AlignSamReading alignSamReading = new AlignSamReading(samFile);
		SamToBed samToBed = new SamToBed(samFile);
		samToBed.setUniqueRandomSelectOneRead(mapParams.get("toBedOptionNoneUnique") != null);
		alignSamReading.addAlignmentRecorder(samToBed);
		alignSamReading.run();
		BedSeq bedSeq = samToBed.getBedSeq();
		bedSeq.close();
	}
	
	
	private String getPrefix(String prefixOld, HashSet<String> setTmp) {
		if (prefixOld != null && !prefixOld.equals("")) {
			return prefixOld;
		}
		int i = 0;
		while (setTmp.contains(i + "")) {
			i++;
		}
		return i + "";
	}
	
	@After
	public void destroy(){
		mapParams = null;
	}
}
