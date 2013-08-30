package com.novelbio.nbcgui.controlseq;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.novelbio.analysis.seq.fastq.FastQ;
import com.novelbio.analysis.seq.mapping.MapLibrary;
import com.novelbio.analysis.seq.sam.SamFileStatistics;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.database.domain.information.SoftWareInfo;
import com.novelbio.database.domain.information.SoftWareInfo.SoftWare;
import com.novelbio.database.model.species.Species;

public class CtrlDNAMapping {
	private static final Logger logger = Logger.getLogger(CtrlDNAMapping.class);
	public static final int MAP_TO_CHROM = 8;
	public static final int MAP_TO_REFSEQ = 4;
	public static final int MAP_TO_REFSEQ_LONGEST_ISO = 2;
	
	private String outFilePrefix = "";
	
	private HashMap<String, FastQ[]> mapCondition2CombFastQLRFiltered = new LinkedHashMap<String, FastQ[]>();
	MapLibrary libraryType = MapLibrary.SingleEnd;
	int gapLen = 5;
	double mismatch = 2;
	int thread = 4;
	
	String chrIndexFile;
	Species species;
	int map2Index = MAP_TO_CHROM;
	
	SoftWare softMapping = SoftWare.bwa;
	
	SoftWareInfo softWareInfo = new SoftWareInfo();
	
	SamFileStatistics samFileStatistics;
	/** 
	 * @param species
	 * @param map2Index mapping到什么上面去，有chrom，refseq和refseqLongestIso三种
	 */
	public void setSpecies(Species species, int map2Index) {
		this.species = species;
		this.map2Index = map2Index;
	}
	
	public SamFileStatistics getSamFileStatistics() {
		return samFileStatistics;
	}
	
	public void setChrIndexFile(String chrIndexFile) {
		if (FileOperate.isFileExistAndBigThanSize(chrIndexFile, 10)) {
			this.chrIndexFile = chrIndexFile;
		}
	}
	public void setOutFilePrefix(String outFilePrefix) {
		this.outFilePrefix = outFilePrefix + "MappingInfo";
	}
	public String getOutFilePrefix() {
		return outFilePrefix;
	}
	
	public void setGapLen(int gapLen) {
		this.gapLen = gapLen;
	}
	public int getGapLen() {
		return gapLen;
	}
	public void setMismatch(Double mismatch) {
		this.mismatch = mismatch;
	}
	public double getMismatch() {
		return mismatch;
	}
	public void setSoftMapping(SoftWare softMapping) {
		this.softMapping = softMapping;
	}
	public SoftWare getSoftMapping() {
		return softMapping;
	}
	public void setThread(int thread) {
		this.thread = thread;
	}
	public int getThread() {
		return thread;
	}
	
	public void running() {
		mapping("aa",null);
	}
	
	private void mapping() {
		softWareInfo.setName(softMapping);
		for (Entry<String, FastQ[]> entry : mapCondition2CombFastQLRFiltered.entrySet()) {
			mapping(entry.getKey(), entry.getValue());
		}
	}
	
	/**
	 *  仅供AOP使用
	 * @param prefix 文件前缀，实际输出文本为{@link #outFilePrefix} + prefix +.txt
	 * @param fastQs
	 */
	public String mapping(String prefix, FastQ[] fastQs) {
//		MapDNA mapSoftware = MapDNA.creatMapDNA(softMapping);		
//		mapSoftware.setExePath(softWareInfo.getExePath());
//
//		if (species.getTaxID() == 0) {
//			mapSoftware.setChrFile(chrIndexFile);
//		} else {
//			if (map2Index == MAP_TO_CHROM) {
//				mapSoftware.setChrFile(species.getIndexChr(softMapping));
//			} else if (map2Index == MAP_TO_REFSEQ) {
//				mapSoftware.setChrFile(species.getIndexRef(softMapping));
//			} else if (map2Index == MAP_TO_REFSEQ_LONGEST_ISO) {
//				mapSoftware.setChrFile(species.getRefseqLongestIsoNrFile());
//			}
//		}
//
//		mapSoftware.setFqFile(fastQs[0], fastQs[1]);
//		mapSoftware.setOutFileName(outFilePrefix + prefix);
//		mapSoftware.setGapLength(gapLen);
//		mapSoftware.setMismatch(mismatch);
//		mapSoftware.setSampleGroup(prefix, prefix, prefix, null);
//		mapSoftware.setMapLibrary(libraryType);
//		mapSoftware.setThreadNum(thread);
//		samFileStatistics = new SamFileStatistics();
//		mapSoftware.addAlignmentRecorder(samFileStatistics);
//		mapSoftware.mapReads();
		
		
		//拦截返回结果还有samFileStatistis用get方法
		return "asdfadfasdfasdfasd";
	}
	
	
	public static HashMap<String, Integer> getMapStr2Index() {
		HashMap<String, Integer> mapStr2Index = new HashMap<String, Integer>();
		mapStr2Index.put("chromosome", MAP_TO_CHROM);
		mapStr2Index.put("refseq", MAP_TO_REFSEQ);
		mapStr2Index.put("refseq Longest Iso", MAP_TO_REFSEQ_LONGEST_ISO);
		return mapStr2Index;
	}
}
