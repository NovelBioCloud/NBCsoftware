package com.novelbio.nbcgui.controlseq;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.novelbio.analysis.seq.fastq.FastQ;
import com.novelbio.analysis.seq.mapping.MapDNA;
import com.novelbio.analysis.seq.mapping.MapDNAint;
import com.novelbio.analysis.seq.mapping.MapLibrary;
import com.novelbio.analysis.seq.sam.SamFileStatistics;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.database.domain.information.SoftWareInfo;
import com.novelbio.database.domain.information.SoftWareInfo.SoftWare;
import com.novelbio.database.model.species.Species;
import com.novelbio.nbcgui.FoldeCreate;

@Component
@Scope("prototype")
public class CtrlDNAMapping {
	private static final Logger logger = Logger.getLogger(CtrlDNAMapping.class);
	public static final int MAP_TO_CHROM = 8;
	public static final int MAP_TO_REFSEQ = 4;
	public static final int MAP_TO_REFSEQ_LONGEST_ISO = 2;
	
	private static final String pathSaveTo = "Mapping_result";

	
	private String outFilePrefix = "";
	
	private Map<String, List<String[]>> mapCondition2CombFastQLRFiltered = new LinkedHashMap<>();
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
	
	/** 设定输入文件 */
	public void setMapCondition2CombFastQLRFiltered(Map<String, List<String[]>> mapCondition2CombFastQLRFiltered) {
		this.mapCondition2CombFastQLRFiltered = mapCondition2CombFastQLRFiltered;
	}
	
	public void setCopeFastq(CopeFastq copeFastq) {
		copeFastq.setMapCondition2LsFastQLR();
		this.mapCondition2CombFastQLRFiltered = copeFastq.getMapCondition2LsFastQLR();
	}
	
	public SamFileStatistics getSamFileStatistics() {
		return samFileStatistics;
	}

	public void setLibraryType(MapLibrary selectedValue) {
		this.libraryType = selectedValue;
	}
	public void setChrIndexFile(String chrIndexFile) {
		if (FileOperate.isFileExistAndBigThanSize(chrIndexFile, 10)) {
			this.chrIndexFile = chrIndexFile;
		}
	}
	public void setOutFilePrefix(String outFilePrefix) {
		this.outFilePrefix = FoldeCreate.createAndInFold(outFilePrefix, pathSaveTo);
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
		mapping();
	}
	
	private void mapping() {
		for (Entry<String, List<String[]>> entry : mapCondition2CombFastQLRFiltered.entrySet()) {
			List<String[]> lsFastQs = entry.getValue();
			if (lsFastQs.size() == 1) {
				mapping(entry.getKey(), lsFastQs.get(0));
				SamFileStatistics.saveInfo(outFilePrefix + entry.getKey(), samFileStatistics);
			} else {
				for (int i = 0; i < lsFastQs.size(); i++) {
					String[] fastQs = lsFastQs.get(i);
					mapping(entry.getKey() + "_" + i, fastQs);
					SamFileStatistics.saveInfo(entry.getKey() + "_" + i, samFileStatistics);
				}
			}
		}
	}
	
	/**
	 * 外部调用使用，
	 * 使用方法：<br>
	 * for (Entry<String, FastQ[]> entry : mapCondition2CombFastQLRFiltered.entrySet()) {<br>
			mapping(entry.getKey(), entry.getValue());<br>
		}<br>
	 * <br>
	 *  供AOP使用
	 * @param prefix 文件前缀，实际输出文本为{@link #outFilePrefix} + prefix +.txt
	 * @param fastQs
	 */
	public String mapping(String prefix, String[] fastQsFile) {
		FastQ[] fastQs = CopeFastq.convertFastqFile(fastQsFile);
		softWareInfo.setName(softMapping);
		MapDNAint mapSoftware = MapDNA.creatMapDNA(softMapping);		
		mapSoftware.setExePath(softWareInfo.getExePath());

		if (species.getTaxID() == 0) {
			mapSoftware.setChrFile(chrIndexFile);
		} else {
			if (map2Index == MAP_TO_CHROM) {
				mapSoftware.setChrFile(species.getIndexChr(softMapping));
			} else if (map2Index == MAP_TO_REFSEQ) {
				mapSoftware.setChrFile(species.getIndexRef(softMapping));
			} else if (map2Index == MAP_TO_REFSEQ_LONGEST_ISO) {
				mapSoftware.setChrFile(species.getRefseqLongestIsoNrFile());
			}
		}
		if (fastQs.length == 1) {
			mapSoftware.setFqFile(fastQs[0], null);
		} else {
			mapSoftware.setFqFile(fastQs[0], fastQs[1]);
		}
		
		mapSoftware.setOutFileName(outFilePrefix + prefix);
		mapSoftware.setGapLength(gapLen);
		mapSoftware.setMismatch(mismatch);
		mapSoftware.setSampleGroup(prefix, prefix, prefix, null);
		mapSoftware.setMapLibrary(libraryType);
		mapSoftware.setThreadNum(thread);
		samFileStatistics = new SamFileStatistics(prefix);
		mapSoftware.addAlignmentRecorder(samFileStatistics);
		mapSoftware.mapReads();
		return mapSoftware.getCmdMapping();
	}
	
	public static HashMap<String, Integer> getMapStr2Index() {
		HashMap<String, Integer> mapStr2Index = new HashMap<String, Integer>();
		mapStr2Index.put("chromosome", MAP_TO_CHROM);
		mapStr2Index.put("refseq", MAP_TO_REFSEQ);
		mapStr2Index.put("refseq Longest Iso", MAP_TO_REFSEQ_LONGEST_ISO);
		return mapStr2Index;
	}

}
