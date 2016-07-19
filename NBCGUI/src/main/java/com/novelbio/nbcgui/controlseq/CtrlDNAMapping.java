package com.novelbio.nbcgui.controlseq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.novelbio.analysis.IntCmdSoft;
import com.novelbio.analysis.seq.fasta.CopeFastq;
import com.novelbio.analysis.seq.mapping.MapBowtie2;
import com.novelbio.analysis.seq.mapping.MapBwaAln;
import com.novelbio.analysis.seq.mapping.MapDNA;
import com.novelbio.analysis.seq.mapping.MapDNAint;
import com.novelbio.analysis.seq.mapping.MapLibrary;
import com.novelbio.analysis.seq.sam.AlignSeqReading;
import com.novelbio.analysis.seq.sam.SamFile;
import com.novelbio.analysis.seq.sam.SamFileStatistics;
import com.novelbio.base.ExceptionNbcParamError;
import com.novelbio.base.ExceptionNullParam;
import com.novelbio.base.StringOperate;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.database.domain.geneanno.SpeciesFile;
import com.novelbio.database.domain.information.SoftWareInfo;
import com.novelbio.database.domain.information.SoftWareInfo.SoftWare;
import com.novelbio.database.model.species.Species;

@Component
@Scope("prototype")
public class CtrlDNAMapping implements IntCmdSoft {
	private static final Logger logger = Logger.getLogger(CtrlDNAMapping.class);
	
	private String outFilePrefix = "";
	
	List<String> lsCmd = new ArrayList<>();
	private Map<String, List<List<String>>> mapPrefix2LsFastq;
	MapLibrary libraryType = MapLibrary.PairEnd;
	int gapLen = 5;
	double mismatch = 2;
	int sensitive = MapBowtie2.Sensitive_Sensitive;
	boolean isLocal = true;
	int thread = 4;
	boolean isNeedSort = false;
	String chrIndexFile;
	Species species;
	int map2Index = Species.CHROM;
	
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
		
		String chrFile = getChrFile(chrIndexFile, species, softMapping, map2Index);
		mapSoftware.setChrIndex(chrFile);
	
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
		if (mapSoftware instanceof MapBowtie2) {
			((MapBowtie2)mapSoftware).setSensitive(sensitive);
			((MapBowtie2)mapSoftware).setLocal(isLocal);
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
	
	public static String getChrFile(String chrFile, Species species, SoftWare softMapping, int map2Index) {
		String chrFileResult = chrFile;
		if (StringOperate.isRealNull(chrFileResult)) {
			if (species == null || species.getTaxID() == 0) {
				throw new ExceptionNbcParamError("species is not exist and chrSeq file is also not exist!");
			}
			chrFileResult = species.getIndexRef(softMapping, map2Index);
		}
		return chrFileResult;
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
		return Species.getMapStr2Index();
	}

}
