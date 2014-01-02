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
import com.novelbio.analysis.seq.fastq.FastQFilter;
import com.novelbio.base.FoldeCreate;
import com.novelbio.base.dataOperate.TxtReadandWrite;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.database.service.SpringFactory;
import com.novelbio.nbcReport.Params.EnumReport;
import com.novelbio.nbcReport.Params.ReportQC;

@Component
@Scope("prototype")
public class CtrlFastQ {
	private static final Logger logger = Logger.getLogger(CtrlFastQ.class);
	FastQFilter fastQfilter = new FastQFilter();
	
	CopeFastq copeFastq = new CopeFastq();
	
	String outFilePrefix = "";
	
	/** 过滤好的结果 */
	Map<String, List<List<String>>> mapCondition2LRFiltered = new LinkedHashMap<>();
		
	/** 过滤前质控 */
	Map<String, FastQC[]> mapCond2FastQCBefore;
	/** 过滤前是否要QC，不要就只计数 */
	boolean qcBefore = true;
	/** 过滤后质控 */
	Map<String, FastQC[]> mapCond2FastQCAfter;
	/** 过滤后是否要QC，不要就只计数 */
	boolean qcAfter = true;
	
	List<ReportQC> lsReportQCs = new ArrayList<>();
	/** 返回fastqc的结果文件 */
	HashMultimap<String, String> mapPrefix2ResultQC = HashMultimap.create();
	
	boolean isJustFastqc = false;
	
	/**
	 * 得到所有的报告
	 * @return
	 */
	public List<ReportQC> getLsReportQCs() {
		return lsReportQCs;
	}
	
	public void setAdaptorLeft(String adaptorLeft) {
		fastQfilter.setFilterParamAdaptorLeft(adaptorLeft.trim());
	}
	public void setAdaptorRight(String adaptorRight) {
		fastQfilter.setFilterParamAdaptorRight(adaptorRight.trim());
	}
	public void setAdaptorLowercase(boolean adaptorLowercase) {
		fastQfilter.setFilterParamAdaptorLowercase(adaptorLowercase);
	}

	public void setFastqQuality(int fastqQuality) {
		fastQfilter.setQualityFilter(fastqQuality);
	}
	/** 是否过滤，如果不过滤则直接合并 */
	public void setFilter(boolean filter) {
		fastQfilter.setIsFiltered(filter);
	}
	public void setReadsLenMin(int readsLenMin) {
		fastQfilter.setFilterParamReadsLenMin(readsLenMin);
	}
	/** 是否仅输出fastqc的结果<br>
	 * true：仅输出fastqc<br>
	 * false：还输出过滤的结果
	 * @param isJustFastqc
	 */
	public void setJustFastqc(boolean isJustFastqc) {
		this.isJustFastqc = isJustFastqc;
	}
	/**
	 * @param trimNNN 是否过滤两端低质量序列
	 * @param qualityCutoff cutoff为多少
	 */
	public void setTrimNNN(boolean trimNNN, int qualityCutoff) {
		fastQfilter.setFilterParamTrimNNN(trimNNN, qualityCutoff);
	}
	
	public void setOutFilePrefix(String outFilePrefix) {
		this.outFilePrefix = FoldeCreate.createAndInFold(outFilePrefix, EnumReport.FastQC.getResultFolder());
	}
	
	public boolean isFiltered() {
		return fastQfilter.isFiltered();
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
	public void setLsFastQfileLeft(List<String> lsFastQfileLeft) {
		copeFastq.setLsFastQfileLeft(lsFastQfileLeft);
	}
	public void setLsFastQfileRight(List<String> lsFastQfileRight) {
		copeFastq.setLsFastQfileRight(lsFastQfileRight);
	}
	/**必须对每个文件都有一个前缀 */
	public void setLsPrefix(List<String> lsPrefix) {
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
	 * 这时候获得的就是所有没过滤的fastq文件<br>
	 * key: Prefix<br>
	 * Value: 0: listLeft<br>
	 * 1: listRight 单端则该list为空，不为null
	 */
	public Map<String, List<List<String>>> getFilteredMap() {
		if (mapCondition2LRFiltered.size() == 0) {
			if (copeFastq.getMapCondition2LsFastQLR().size() == 0) {
				copeFastq.setMapCondition2LsFastQLR();
				return copeFastq.getMapCondition2LslsFastq();
			}
		}
		return mapCondition2LRFiltered;
	}
	
	private void filteredAndCombineReads() {
		mapPrefix2ResultQC.clear();
		mapCond2FastQCBefore = new LinkedHashMap<String, FastQC[]>();
		mapCond2FastQCAfter = new LinkedHashMap<String, FastQC[]>();
		for (String prefix : copeFastq.getLsPrefix()) {
			List<String[]> lsFastQLR = copeFastq.getMapCondition2LsFastQLR().get(prefix);
			if (!fastQfilter.isFiltered() && lsFastQLR.size() < 2) {
				mapCondition2LRFiltered.put(prefix, copeFastq.getMapCondition2LslsFastq().get(prefix));
				continue;
			}
			CtrlFastQfilter ctrlFastQfilter = (CtrlFastQfilter)SpringFactory.getFactory().getBean("ctrlFastQfilter");
			ctrlFastQfilter.setOutFilePrefix(outFilePrefix);
			ctrlFastQfilter.setPrefix(prefix);
			ctrlFastQfilter.setLsFastQLR(lsFastQLR);
			
			FastQC[] fastQCsBefore = getFastQC(lsFastQLR, prefix, qcBefore);
			mapCond2FastQCBefore.put(prefix, fastQCsBefore);
			ctrlFastQfilter.setFastQCbefore(fastQCsBefore);
			FastQC[] fastQCsAfter = getFastQC(lsFastQLR, prefix, qcAfter);
			mapCond2FastQCAfter.put(prefix, fastQCsAfter);
			ctrlFastQfilter.setFastQCafter(fastQCsAfter);
			ctrlFastQfilter.setJustFastqc(isJustFastqc);
			if (!isJustFastqc) {
				ctrlFastQfilter.setFastQfilterParam(fastQfilter);
				ctrlFastQfilter.setFastQLRfilteredOut(createCombineFastq(fastQfilter.isFiltered(), outFilePrefix, prefix, lsFastQLR));
			}
			ctrlFastQfilter.filteredAndCombineReads();
			
			ctrlFastQfilter.saveFastQC(outFilePrefix + prefix);			
			lsReportQCs.add(ctrlFastQfilter.getReportQC());
			
			mapCondition2LRFiltered.put(prefix, ctrlFastQfilter.getLsFastQLRfiltered());
			addSupQCresult(prefix, ctrlFastQfilter.getMapPrefix2QCresult());
		}
		Map<String, FastQC[]> mapParam2FastqcLR = new LinkedHashMap<>();
		for (String prefix : mapCond2FastQCBefore.keySet()) {
			FastQC[] fastqcBefore = mapCond2FastQCBefore.get(prefix);
			FastQC[] fastqcAfter = mapCond2FastQCAfter.get(prefix);
			mapParam2FastqcLR.put(prefix, fastqcBefore);
			mapParam2FastqcLR.put(prefix, fastqcAfter);
		}
		List<String[]> lsSummary = FastQC.combineFastQCbaseStatistics(mapParam2FastqcLR);
		String totalExcelPath = outFilePrefix + "basicStatsAll.xls";
		TxtReadandWrite txtWrite = null;
		if (FileOperate.isFileExist(totalExcelPath)) {
			lsSummary.remove(0);
		}else{
			FileOperate.createFolders(FileOperate.getParentPathName(totalExcelPath));
		}
		txtWrite = new TxtReadandWrite(totalExcelPath, true);
		txtWrite.ExcelWrite(lsSummary);
		txtWrite.close();
		mapPrefix2ResultQC.put("Summary", totalExcelPath);
	}
	
	private void addSupQCresult(String prefix, HashMultimap<String, String> mapPrefix2File) {
		if (prefix == null) prefix = "";
		String sep = (prefix.equals("")) ? "" : "_";
		for (String subPrefix : mapPrefix2File.keySet()) {
			String prefixFinal = prefix + sep + subPrefix;
			mapPrefix2ResultQC.putAll(prefixFinal, mapPrefix2File.get(subPrefix));
		}
	}
	
	private static FastQC[] getFastQC(List<String[]> lsFastQLR, String prefix, boolean qc) {
		FastQC[] fastQCs = new FastQC[2];
		if (lsFastQLR.get(0).length == 1) {
			fastQCs[0] = new FastQC(prefix, qc);
		} else {
			fastQCs[0] = new FastQC(prefix + "_Left", qc);
			fastQCs[1] = new FastQC(prefix + "_Right", qc);
		}
		return fastQCs;
	}
	
	private static FastQ[] createCombineFastq(boolean filtered, String outFilePrefix, String condition, List<String[]> lsFastq) {
		FastQ[] fastQs = new FastQ[2];
		String[] fileName = createCombineFQname(filtered, outFilePrefix, condition, lsFastq);
		fastQs[0] = new FastQ(fileName[0], true);
		if (fileName[1] != null) {
			fastQs[1] = new FastQ(fileName[1], true);
		}
		return fastQs;
	}
	
	private static String[] createCombineFQname(boolean filtered, String outFilePrefix, String condition, List<String[]> lsFastq) {
		String[] fastQs = new String[2];
		if (filtered) condition = condition + "_filtered";
		if (lsFastq.size() > 1) condition = condition + "_Combine";
		
		if (lsFastq.get(0).length == 1 || lsFastq.get(0)[1] == null) {
			fastQs[0] = outFilePrefix + condition + ".fq.gz";
		} else {
			fastQs[0] = outFilePrefix + condition + "_1.fq.gz";
			fastQs[1] = outFilePrefix + condition + "_2.fq.gz";
		}
		return fastQs;
	}
	
	public HashMultimap<String, String> getMapPrefix2ResultQC() {
		return mapPrefix2ResultQC;
	}
	/** 返回预测的文件名
	 * @param copeFastq
	 * @param isFilter 是否过滤，如果不过滤就直接合并
	 * @return
	 */
	public static HashMultimap<String, String> getPredictMapPrefix2FilteredFQ(CopeFastq copeFastq, String outPrefix, boolean isFilter) {
		copeFastq.setMapCondition2LsFastQLR();

		HashMultimap<String, String> mapPrefix2LsFilteredFile = HashMultimap.create();
		outPrefix = FoldeCreate.getInFold(outPrefix, EnumReport.FastQC.getResultFolder());
		for (String prefix : copeFastq.getLsPrefix()) {
			List<String[]> lsFastQLR = copeFastq.getMapCondition2LsFastQLR().get(prefix);
			if (!isFilter && lsFastQLR.size() < 2) {
				mapPrefix2LsFilteredFile.put(prefix, lsFastQLR.get(0)[0]);
				if (lsFastQLR.get(0).length > 1) {
					mapPrefix2LsFilteredFile.put(prefix, lsFastQLR.get(0)[1]);
				}
				continue;
			}
			String[] fastqName = createCombineFQname(isFilter, outPrefix, prefix, lsFastQLR);
			mapPrefix2LsFilteredFile.put(prefix, fastqName[0]);
			if (fastqName != null) {
				mapPrefix2LsFilteredFile.put(prefix, fastqName[1]);
			}
		}
		return mapPrefix2LsFilteredFile;
	}
}
