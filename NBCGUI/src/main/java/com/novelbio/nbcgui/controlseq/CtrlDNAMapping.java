package com.novelbio.nbcgui.controlseq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.collect.HashMultimap;
import com.novelbio.analysis.IntCmdSoft;
import com.novelbio.analysis.seq.FormatSeq;
import com.novelbio.analysis.seq.mapping.MapBowtie;
import com.novelbio.analysis.seq.mapping.MapBwaAln;
import com.novelbio.analysis.seq.mapping.MapDNA;
import com.novelbio.analysis.seq.mapping.MapDNAint;
import com.novelbio.analysis.seq.mapping.MapLibrary;
import com.novelbio.analysis.seq.sam.AlignSeqReading;
import com.novelbio.analysis.seq.sam.SamFile;
import com.novelbio.analysis.seq.sam.SamFileStatistics;
import com.novelbio.base.ExceptionNullParam;
import com.novelbio.base.FoldeCreate;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.database.domain.information.SoftWareInfo;
import com.novelbio.database.domain.information.SoftWareInfo.SoftWare;
import com.novelbio.database.model.species.Species;

@Component
@Scope("prototype")
public class CtrlDNAMapping implements IntCmdSoft {
	private static final Logger logger = Logger.getLogger(CtrlDNAMapping.class);
	public static final int MAP_TO_CHROM = 8;
	public static final int MAP_TO_REFSEQ_ALL_ISO = 4;
	public static final int MAP_TO_REFSEQ_LONGEST_ISO = 2;
	
	private String outFilePrefix = "";
	
	List<String> lsCmd = new ArrayList<>();
	private Map<String, List<List<String>>> mapPrefix2LsFastq;
	MapLibrary libraryType = MapLibrary.SingleEnd;
	int gapLen = 5;
	double mismatch = 2;
	int sensitive = MapBowtie.Sensitive_Sensitive;
	boolean isLocal = true;
	int thread = 4;
	boolean isNeedSort = false;
	String chrIndexFile;
	Species species;
	int map2Index = MAP_TO_CHROM;
	
	SoftWare softMapping = SoftWare.bwa_aln;
	
	SoftWareInfo softWareInfo = new SoftWareInfo();
	
	SamFileStatistics samFileStatistics;
	
	//结果文件
	Map<String, String> mapPrefix2Bam = new HashMap<>();
	Map<String, String> mapPrefix2Statistics = new HashMap<>();
	Map<String, String> mapPrefix2Pic = new HashMap<>();
	
	/** 
	 * @param species
	 * @param map2Index mapping到什么上面去，有CtrlDNAMapping.MAP_TO_CHROM，refseq和refseqLongestIso三种
	 */
	public void setSpecies(Species species, int map2Index) {
		this.species = species;
		this.map2Index = map2Index;
	}
	
	/** 设定输入文件 */
	public void setMapCondition2CombFastQLRFiltered(Map<String, List<List<String>>> mapPrefix2LsLRfq) {
		this.mapPrefix2LsFastq = mapPrefix2LsLRfq;
	}
	
	public void setCopeFastq(CopeFastq copeFastq) {
		copeFastq.setMapCondition2LsFastQLR();
		this.mapPrefix2LsFastq = copeFastq.getMapCondition2LslsFastq();
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
		this.outFilePrefix = outFilePrefix;
	}
	public String getOutFilePrefix() {
		return outFilePrefix;
	}
	public void setSortNeed(boolean isNeedSort) {
		this.isNeedSort = isNeedSort;
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
	public void setSensitive(int sensitive) {
		this.sensitive = sensitive;
	}
	/** 仅用于bowtie2 */
	public void setIsLocal(boolean isLocal) {
		this.isLocal = isLocal;
	}
	public int getSensitive() {
		return sensitive;
	}
	public void setSoftMapping(SoftWare softMapping) {
		if (softMapping == null) {
			throw new ExceptionNullParam("No Param of Software");
		}
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
		mapPrefix2Bam.clear();
		mapPrefix2Pic.clear();
		mapPrefix2Statistics.clear();
		lsCmd.clear();
		for (String prefix : mapPrefix2LsFastq.keySet()) {
			List<List<String>> lsFastQs = mapPrefix2LsFastq.get(prefix);
			SamFile samFile = mapping(prefix, lsFastQs);
			if (samFile != null) {
				mapPrefix2Bam.put(prefix, samFile.getFileName());
			} else {
				continue;
			}
			if (samFileStatistics != null) {
				String excel = SamFileStatistics.saveExcel(outFilePrefix + prefix, samFileStatistics);
				String pic = SamFileStatistics.savePic(outFilePrefix + prefix, samFileStatistics);
				mapPrefix2Statistics.put(prefix, excel);
				mapPrefix2Pic.put(prefix, pic);
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
	private SamFile mapping(String prefix, List<List<String>> fastQsFile) {
		MapDNAint mapSoftware = MapDNA.creatMapDNA(softMapping);		

		if (species == null || species.getTaxID() == 0) {
			mapSoftware.setChrIndex(chrIndexFile);
		} else {
			if (map2Index == MAP_TO_CHROM) {
				mapSoftware.setChrIndex(species.getIndexChr(softMapping));
			} else if (map2Index == MAP_TO_REFSEQ_ALL_ISO) {
				mapSoftware.setChrIndex(species.getIndexRef(softMapping, true));
			} else if (map2Index == MAP_TO_REFSEQ_LONGEST_ISO) {
				mapSoftware.setChrIndex(species.getIndexRef(softMapping, false));
			}
		}
		if (softMapping == SoftWare.bwa_aln) {
			MapBwaAln mapBwaAln = (MapBwaAln)mapSoftware;
			mapBwaAln.setGapLength(gapLen);
			mapBwaAln.setMismatch(mismatch);
		}
		mapSoftware.setPrefix(prefix);
		mapSoftware.setSampleGroup(prefix, prefix, prefix, null);
		mapSoftware.setMapLibrary(libraryType);
		mapSoftware.setSortNeed(isNeedSort);
		mapSoftware.setThreadNum(thread);
		mapSoftware.setOutFileName(outFilePrefix + prefix);
		if (FileOperate.isFileExistAndBigThanSize(mapSoftware.getOutNameCope(), 0)) {
			return new SamFile(mapSoftware.getOutNameCope());
		}
		
		mapSoftware.setLeftFq(CopeFastq.convertFastqFile(fastQsFile.get(0)));
		mapSoftware.setRightFq(CopeFastq.convertFastqFile(fastQsFile.get(1)));
		if (mapSoftware instanceof MapBowtie) {
			((MapBowtie)mapSoftware).setSensitive(sensitive);
			((MapBowtie)mapSoftware).setLocal(isLocal);
		}

		lsCmd.addAll(mapSoftware.getCmdExeStr());
		SamFile samFile = mapSoftware.mapReads();
		samFileStatistics = new SamFileStatistics(prefix);
		samFileStatistics.setStandardData(samFile.getMapChrID2Length());
		AlignSeqReading alignSeqReading = new AlignSeqReading(samFile);
		alignSeqReading.addAlignmentRecorder(samFileStatistics);
		alignSeqReading.run();
		return samFile;
	}
	
	/**
	 * 预判结果文件<br>
	 * key: 文件类型<br>
	 * value：结果文件名
	 * @return
	 */
	public HashMultimap<String, String> getPredictMapPrefix2Result(boolean isNeedSort, List<String> lsPrefix) {
		HashMultimap<String, String> mapFileFormat2FileName = HashMultimap.create();
		softWareInfo.setName(softMapping);
		MapDNAint mapSoftware = MapDNA.creatMapDNA(softMapping);
		mapSoftware.setSortNeed(isNeedSort);
		for (String prefix : lsPrefix) {
			mapSoftware.setOutFileName(outFilePrefix + prefix);
			mapFileFormat2FileName.put(FormatSeq.BAM.toString(), mapSoftware.getOutNameCope());
			mapFileFormat2FileName.put("Excel", SamFileStatistics.getSaveExcel(outFilePrefix + prefix));
			mapFileFormat2FileName.put("Pic", SamFileStatistics.getSavePic(outFilePrefix + prefix));
		}
		return mapFileFormat2FileName;
	}
	
	public Map<String, String> getMapPrefix2Bam() {
		return mapPrefix2Bam;
	}
	public Map<String, String> getMapPrefix2Pic() {
		return mapPrefix2Pic;
	}
	public Map<String, String> getMapPrefix2Statistics() {
		return mapPrefix2Statistics;
	}

	@Override
	public List<String> getCmdExeStr() {
		return lsCmd;
	}
	
	public static HashMap<String, Integer> getMapStr2Index() {
		HashMap<String, Integer> mapStr2Index = new HashMap<String, Integer>();
		mapStr2Index.put("chromosome", MAP_TO_CHROM);
		mapStr2Index.put("refseq", MAP_TO_REFSEQ_ALL_ISO);
		mapStr2Index.put("refseq Longest Iso", MAP_TO_REFSEQ_LONGEST_ISO);
		return mapStr2Index;
	}

}
