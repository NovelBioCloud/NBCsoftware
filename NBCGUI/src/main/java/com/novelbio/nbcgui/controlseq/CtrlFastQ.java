package com.novelbio.nbcgui.controlseq;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.collect.HashMultimap;
import com.novelbio.analysis.seq.fastq.FastQ;
import com.novelbio.analysis.seq.fastq.FastQC;
import com.novelbio.analysis.seq.fastq.FastQRecordFilter;
import com.novelbio.aoplog.ReportBuilder;
import com.novelbio.base.dataOperate.TxtReadandWrite;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.database.service.SpringFactory;
import com.novelbio.nbcgui.FoldeCreate;

@Component
@Scope("prototype")
public class CtrlFastQ {
	private static Logger logger = Logger.getLogger(CtrlFastQ.class);
	private static final String pathSaveTo = "Quality-Control_result";
	FastQRecordFilter fastQfilterRecord = new FastQRecordFilter();
	
	CopeFastq copeFastq = new CopeFastq();
	
	String outFilePrefix = "";
	
	/** 过滤好的结果 */
	Map<String, List<String[]>> mapCondition2LRFiltered = new LinkedHashMap<>();
		
	/** 过滤前质控 */
	Map<String, FastQC[]> mapCond2FastQCBefore;
	/** 过滤前是否要QC，不要就只计数 */
	boolean qcBefore = true;
	/** 过滤后质控 */
	Map<String, FastQC[]> mapCond2FastQCAfter;
	/** 过滤后是否要QC，不要就只计数 */
	boolean qcAfter = true;

	
	public void setAdaptorLeft(String adaptorLeft) {
		fastQfilterRecord.setFilterParamAdaptorLeft(adaptorLeft.trim());
	}
	public void setAdaptorRight(String adaptorRight) {
		fastQfilterRecord.setFilterParamAdaptorRight(adaptorRight.trim());
	}
	public void setAdaptorLowercase(boolean adaptorLowercase) {
		fastQfilterRecord.setFilterParamAdaptorLowercase(adaptorLowercase);
	}

	public void setFastqQuality(int fastqQuality) {
		fastQfilterRecord.setQualityFilter(fastqQuality);
	}
	/** 是否过滤，如果不过滤则直接合并 */
	public void setFilter(boolean filter) {
		fastQfilterRecord.setIsFiltered(filter);
	}
	public void setReadsLenMin(int readsLenMin) {
		fastQfilterRecord.setFilterParamReadsLenMin(readsLenMin);
	}
	public void setTrimNNN(boolean trimNNN) {
		fastQfilterRecord.setFilterParamTrimNNN(trimNNN);
	}
	
	public void setOutFilePrefix(String outFilePrefix) {
		this.outFilePrefix = FoldeCreate.createAndInFold(outFilePrefix, pathSaveTo);
	}
	
	public boolean isFiltered() {
		return fastQfilterRecord.isFiltered();
	}
	
	public String getOutFilePrefix() {
		return outFilePrefix;
	}
	
	public void setFastQC(boolean QCbeforeFilter, boolean QCafterFilter) {
		this.qcBefore = QCbeforeFilter;
		this.qcAfter = QCafterFilter;
	}
	
	public boolean isQcBefore() {
		return qcBefore;
	}
	public boolean isQcAfter() {
		return qcAfter;
	}
	public Map<String, FastQC[]> getMapCond2FastQCBefore() {
		return mapCond2FastQCBefore;
	}
	public Map<String, FastQC[]> getMapCond2FastQCAfter() {
		return mapCond2FastQCAfter;
	}
	
	/**
	 * arraylist - string[3]: <br>
	 * 0: fastqFile <br>
	 * 1: prefix <br>
	 * 2: group
	 */
	public void setLsFastQfileLeft(ArrayList<String> lsFastQfileLeft) {
		copeFastq.setLsFastQfileLeft(lsFastQfileLeft);
	}
	public void setLsFastQfileRight(ArrayList<String> lsFastQfileRight) {
		copeFastq.setLsFastQfileRight(lsFastQfileRight);
	}
	/**必须对每个文件都有一个前缀 */
	public void setLsPrefix(ArrayList<String> lsPrefix) {
		copeFastq.setLsCondition(lsPrefix);
	}
	
	public void running() {
		if (!copeFastq.setMapCondition2LsFastQLR()) {
			return;
		}
		//过滤以及合并reads
		filteredAndCombineReads();
	}
	
	/** 当设定好lsCondition和lsLeft和lsRight后，可以不filter直接获得该项目<br>
	 * 这时候获得的就是所有没过滤的fastq文件
	 */
	public Map<String, List<String[]>> getFilteredMap() {
		if (mapCondition2LRFiltered.size() == 0) {
			if (copeFastq.getMapCondition2LsFastQLR().size() == 0) {
				copeFastq.setMapCondition2LsFastQLR();
				return copeFastq.getMapCondition2LsFastQLR();
			}
		}
		return mapCondition2LRFiltered;
	}
	
	private void filteredAndCombineReads() {

		mapCond2FastQCBefore = new LinkedHashMap<String, FastQC[]>();
		mapCond2FastQCAfter = new LinkedHashMap<String, FastQC[]>();
		for (String prefix : copeFastq.getLsPrefix()) {
			List<String[]> lsFastQLR = copeFastq.getMapCondition2LsFastQLR().get(prefix);
			if (!fastQfilterRecord.isFiltered() && lsFastQLR.size() < 2) {
				mapCondition2LRFiltered.put(prefix, lsFastQLR);
				continue;
			}
			CtrlFastQfilter ctrlFastQfilter = (CtrlFastQfilter)SpringFactory.getFactory().getBean("ctrlFastQfilter");
			ctrlFastQfilter.setFastQfilterParam(fastQfilterRecord);
			ctrlFastQfilter.setOutFilePrefix(outFilePrefix);
			ctrlFastQfilter.setPrefix(prefix);
			ctrlFastQfilter.setLsFastQLR(lsFastQLR);
			
			FastQC[] fastQCsBefore = getFastQC(lsFastQLR, prefix, qcBefore);
			mapCond2FastQCBefore.put(prefix, fastQCsBefore);
			ctrlFastQfilter.setFastQCbefore(fastQCsBefore);
			FastQC[] fastQCsAfter = getFastQC(lsFastQLR, prefix, qcAfter);
			mapCond2FastQCAfter.put(prefix, fastQCsAfter);
			ctrlFastQfilter.setFastQCafter(fastQCsAfter);
			
			ctrlFastQfilter.setFastQLRfiltered(createCombineFastq(prefix, lsFastQLR));
			ctrlFastQfilter.filteredAndCombineReads();
			
			HashMultimap<String, String> mapParam = ctrlFastQfilter.saveFastQC(outFilePrefix + prefix);
			saveFastQCfilterParamSingle(mapParam);
		}
		Map<String, FastQC[]> mapParam2FastqcLR = new LinkedHashMap<>();
		for (String prefix : mapCond2FastQCBefore.keySet()) {
			FastQC[] fastqcBefore = mapCond2FastQCBefore.get(prefix);
			FastQC[] fastqcAfter = mapCond2FastQCAfter.get(prefix);
			mapParam2FastqcLR.put(prefix, fastqcBefore);
			mapParam2FastqcLR.put(prefix, fastqcAfter);
		}
		List<String[]> lsSummary = FastQC.combineFastQCbaseStatistics(mapParam2FastqcLR);
		TxtReadandWrite txtWrite = new TxtReadandWrite(outFilePrefix + "basicStatsAll.xls", true);
		txtWrite.ExcelWrite(lsSummary);
		txtWrite.close();
		
	}
	
	private FastQC[] getFastQC(List<String[]> lsFastQLR, String prefix, boolean qc) {
		FastQC[] fastQCs = new FastQC[2];
		if (lsFastQLR.get(0).length == 1) {
			fastQCs[0] = new FastQC(prefix, qc);
		} else {
			fastQCs[0] = new FastQC(prefix + "_Left", qc);
			fastQCs[1] = new FastQC(prefix + "_Right", qc);
		}
		return fastQCs;
	}
	
	private FastQ[] createCombineFastq(String condition, List<String[]> lsFastq) {
		FastQ[] fastQs = new FastQ[2];
		if (fastQfilterRecord.isFiltered()) condition = condition + "_filtered";
		if (lsFastq.size() > 1) condition = condition + "_Combine";
		
		if (lsFastq.get(0).length == 1 || lsFastq.get(0)[1] == null) {
			fastQs[0] = new FastQ(outFilePrefix + condition + ".fq.gz", true);
		} else {
			fastQs[0] = new FastQ(outFilePrefix + condition + "_1.fq.gz", true);
			fastQs[1] = new FastQ(outFilePrefix + condition + "_2.fq.gz", true);
		}
		return fastQs;
	}

	/** 单个过滤写入文本 */
	private void saveFastQCfilterParamSingle(HashMultimap<String, String> mapParam) {
		String savePath = FileOperate.getPathName(outFilePrefix);
//		ReportBuilder.writeDescFile(savePath, mapParam);
	}
	
}
