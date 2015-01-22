package com.novelbio.nbcgui.controlseq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import uk.ac.babraham.FastQC.Modules.BasicStats;

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
	/** 返回fastqc的质检文件 */
	HashMultimap<String, String> mapPrefix2ResultQC = HashMultimap.create();
	
	boolean isJustFastqc = false;
	boolean isCheckFormat = true;
	/**
	 * 得到所有的报告
	 * @return
	 */
	public List<ReportQC> getLsReportQCs() {
		return lsReportQCs;
	}
	public void setCheckFormat(boolean isCheckFormat) {
		this.isCheckFormat = isCheckFormat;
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

	public void setFastqQuality(String fastqQuality) {
		fastQfilter.setQualityFilter(fastqQuality);
	}
	/** 是否过滤，如果不过滤则直接合并 */
	public void setFilter(boolean filter) {
		fastQfilter.setIsFiltered(filter);
	}
	public void setReadsLenMin(int readsLenMin) {
		fastQfilter.setFilterParamReadsLenMin(readsLenMin);
	}
	/** 最长的reads多少，一般不设定，仅在miRNA时才设定 */
	public void setReadsLenMax(int readsLenMax) {
		fastQfilter.setFilterParamReadsLenMax(readsLenMax);
	}
	/** 是否仅输出fastqc的结果<br>
	 * true：仅输出fastqc<br>
	 * false：还输出过滤的结果
	 * @param isJustFastqc
	 */
	public void setJustFastqc(boolean isJustFastqc) {
		this.isJustFastqc = isJustFastqc;
	}
	
	public boolean isJustFastqc() {
		return isJustFastqc;
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
		return fastQfilter.isNeedFilter();
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
		copeFastq.setMapCondition2LsFastQLR();
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
		copeFastq.setMapCondition2LsFastQLR();
		return copeFastq.getMapCondition2LslsFastq();
	}
	
	private void filteredAndCombineReads() {
		mapPrefix2ResultQC.clear();
		mapCond2FastQCBefore = new LinkedHashMap<String, FastQC[]>();
		mapCond2FastQCAfter = new LinkedHashMap<String, FastQC[]>();
		for (String prefix : copeFastq.getLsPrefix()) {
			List<String[]> lsFastQLR = copeFastq.getMapCondition2LsFastQLR().get(prefix);
			if (!fastQfilter.isNeedFilter() && lsFastQLR.size() < 2) {
				mapCondition2LRFiltered.put(prefix, copeFastq.getMapCondition2LslsFastq().get(prefix));
				continue;
			}
			CtrlFastQfilter ctrlFastQfilter = (CtrlFastQfilter)SpringFactory.getFactory().getBean("ctrlFastQfilter");
			boolean isRunFilter = false;
			if (!isJustFastqc) {
				ctrlFastQfilter.setFastQfilterParam(fastQfilter);
				String[] fileName = createCombineFQname(fastQfilter.isNeedFilter(), outFilePrefix, prefix, lsFastQLR, false);
				//文件存在则跳过
				if (!FileOperate.isFileExistAndBigThanSize(fileName[0], 10)) {
					isRunFilter = true;
				}
				ctrlFastQfilter.setFastQLRfilteredOut(createCombineFastq(fastQfilter.isNeedFilter(), outFilePrefix, prefix, lsFastQLR));
			} else {
				String filePic = CtrlFastQfilter.getFastQCPicName(outFilePrefix + prefix);
				//文件存在则跳过
				if (!FileOperate.isFileExistAndBigThanSize(filePic, 10)) {
					isRunFilter = true;
				}
			}
			
			if (!isRunFilter) {
				String[] fileNameFinal = createCombineFQname(fastQfilter.isNeedFilter(), outFilePrefix, prefix, lsFastQLR, false);
				List<List<String>> lsLR = new ArrayList<>();
				List<String> lsLeft = new ArrayList<>();
				lsLeft.add(fileNameFinal[0]);
				lsLR.add(lsLeft);
				if (FileOperate.isFileExistAndBigThanSize(fileNameFinal[1], 0)) {
					List<String> lsRight = new ArrayList<>();
					lsRight.add(fileNameFinal[1]);
					lsLR.add(lsRight);
				}
				mapCondition2LRFiltered.put(prefix, lsLR);
				continue;
			}
			ctrlFastQfilter.setOutFilePrefix(outFilePrefix);
			ctrlFastQfilter.setPrefix(prefix);
			ctrlFastQfilter.setLsFastQLR(lsFastQLR);
			ctrlFastQfilter.setCheckFormat(isCheckFormat);
			FastQC[] fastQCsBefore = getFastQC(lsFastQLR, prefix, qcBefore);
			mapCond2FastQCBefore.put(prefix, fastQCsBefore);
			ctrlFastQfilter.setFastQCbefore(fastQCsBefore);
			FastQC[] fastQCsAfter = getFastQC(lsFastQLR, prefix, qcAfter);
			mapCond2FastQCAfter.put(prefix, fastQCsAfter);
			ctrlFastQfilter.setFastQCafter(fastQCsAfter);
			ctrlFastQfilter.setJustFastqc(isJustFastqc);
			
			ctrlFastQfilter.filteredAndCombineReads();
			List<List<String>> lsLR = new ArrayList<>();
			if (!isJustFastqc) {
				String[] fileNameTmp = createCombineFQname(fastQfilter.isNeedFilter(), outFilePrefix, prefix, lsFastQLR, true);
				String[] fileNameFinal = createCombineFQname(fastQfilter.isNeedFilter(), outFilePrefix, prefix, lsFastQLR, false);
				FileOperate.moveFile(true, fileNameTmp[0], fileNameFinal[0]);
				List<String> lsLeft = new ArrayList<>();
				lsLeft.add(fileNameFinal[0]);
				lsLR.add(lsLeft);
				if (FileOperate.isFileExistAndBigThanSize(fileNameTmp[1], 0)) {
					FileOperate.moveFile(true, fileNameTmp[1], fileNameFinal[1]);
					List<String> lsRight = new ArrayList<>();
					lsRight.add(fileNameFinal[1]);
					lsLR.add(lsRight);
				}
			}
			ctrlFastQfilter.saveFastQC(outFilePrefix + prefix);
			
			lsReportQCs.add(ctrlFastQfilter.getReportQC());
			if (!isJustFastqc) {
				mapCondition2LRFiltered.put(prefix, lsLR);
			}
			
			addSupQCresult(prefix, ctrlFastQfilter.getMapPrefix2QCresult());
		}
		List<String[]> lsSummary = getStatistics();
		String totalExcelPath = outFilePrefix + "basicStatsAll.xls";
		TxtReadandWrite txtWrite = null;
		if (FileOperate.isFileExist(totalExcelPath)) {
			lsSummary.remove(0);
		}else{
			FileOperate.createFolders(FileOperate.getParentPathNameWithSep(totalExcelPath));
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
	
	/** 在yarn中，仅跑部分的过滤工作 */
	public void runSubPrefix() {
		copeFastq.setMapCondition2LsFastQLR();
	}
	
	
	private void filterSubPrefix() {
		mapPrefix2ResultQC.clear();
		mapCond2FastQCBefore = new LinkedHashMap<String, FastQC[]>();
		mapCond2FastQCAfter = new LinkedHashMap<String, FastQC[]>();
		for (String prefix : copeFastq.getLsPrefix()) {
			List<String[]> lsFastQLR = copeFastq.getMapCondition2LsFastQLR().get(prefix);
			if (!fastQfilter.isNeedFilter() && lsFastQLR.size() < 2) {
				mapCondition2LRFiltered.put(prefix, copeFastq.getMapCondition2LslsFastq().get(prefix));
				continue;
			}
			CtrlFastQfilter ctrlFastQfilter = (CtrlFastQfilter)SpringFactory.getFactory().getBean("ctrlFastQfilter");
			boolean isRunFilter = false;
			if (!isJustFastqc) {
				ctrlFastQfilter.setFastQfilterParam(fastQfilter);
				String[] fileName = createCombineFQname(fastQfilter.isNeedFilter(), outFilePrefix, prefix, lsFastQLR, false);
				//文件存在则跳过
				if (!FileOperate.isFileExistAndBigThanSize(fileName[0], 10)) {
					isRunFilter = true;
				}
				ctrlFastQfilter.setFastQLRfilteredOut(createCombineFastq(fastQfilter.isNeedFilter(), outFilePrefix, prefix, lsFastQLR));
			} else {
				String filePic = CtrlFastQfilter.getFastQCPicName(outFilePrefix + prefix);
				//文件存在则跳过
				if (!FileOperate.isFileExistAndBigThanSize(filePic, 10)) {
					isRunFilter = true;
				}
			}
			
			if (!isRunFilter) {
				String[] fileNameFinal = createCombineFQname(fastQfilter.isNeedFilter(), outFilePrefix, prefix, lsFastQLR, false);
				List<List<String>> lsLR = new ArrayList<>();
				List<String> lsLeft = new ArrayList<>();
				lsLeft.add(fileNameFinal[0]);
				lsLR.add(lsLeft);
				if (FileOperate.isFileExistAndBigThanSize(fileNameFinal[1], 0)) {
					List<String> lsRight = new ArrayList<>();
					lsRight.add(fileNameFinal[1]);
					lsLR.add(lsRight);
				}
				mapCondition2LRFiltered.put(prefix, lsLR);
				continue;
			}
			ctrlFastQfilter.setOutFilePrefix(outFilePrefix);
			ctrlFastQfilter.setPrefix(prefix);
			ctrlFastQfilter.setLsFastQLR(lsFastQLR);
			ctrlFastQfilter.setCheckFormat(isCheckFormat);
			FastQC[] fastQCsBefore = getFastQC(lsFastQLR, prefix, qcBefore);
			mapCond2FastQCBefore.put(prefix, fastQCsBefore);
			ctrlFastQfilter.setFastQCbefore(fastQCsBefore);
			FastQC[] fastQCsAfter = getFastQC(lsFastQLR, prefix, qcAfter);
			mapCond2FastQCAfter.put(prefix, fastQCsAfter);
			ctrlFastQfilter.setFastQCafter(fastQCsAfter);
			ctrlFastQfilter.setJustFastqc(isJustFastqc);
			
			ctrlFastQfilter.filteredAndCombineReads();
			List<List<String>> lsLR = new ArrayList<>();
			if (!isJustFastqc) {
				String[] fileNameTmp = createCombineFQname(fastQfilter.isNeedFilter(), outFilePrefix, prefix, lsFastQLR, true);
				String[] fileNameFinal = createCombineFQname(fastQfilter.isNeedFilter(), outFilePrefix, prefix, lsFastQLR, false);
				FileOperate.moveFile(true, fileNameTmp[0], fileNameFinal[0]);
				List<String> lsLeft = new ArrayList<>();
				lsLeft.add(fileNameFinal[0]);
				lsLR.add(lsLeft);
				if (FileOperate.isFileExistAndBigThanSize(fileNameTmp[1], 0)) {
					FileOperate.moveFile(true, fileNameTmp[1], fileNameFinal[1]);
					List<String> lsRight = new ArrayList<>();
					lsRight.add(fileNameFinal[1]);
					lsLR.add(lsRight);
				}
			}
			ctrlFastQfilter.saveFastQC(outFilePrefix + prefix);
			
			lsReportQCs.add(ctrlFastQfilter.getReportQC());
			if (!isJustFastqc) {
				mapCondition2LRFiltered.put(prefix, lsLR);
			}
		}
	}
	
	/** 读取运行完毕的文件信息，并将结果写入basicStatsAll */
	public void readFinishedFile() {
		copeFastq.setMapCondition2LsFastQLR();
		
		mapCond2FastQCBefore = new LinkedHashMap<String, FastQC[]>();
		mapCond2FastQCAfter = new LinkedHashMap<String, FastQC[]>();
		BasicStats basicStats = new BasicStats();
		
		for (String prefix : copeFastq.getLsPrefix()) {
			List<String[]> lsFastQLR = copeFastq.getMapCondition2LsFastQLR().get(prefix);
			if (!fastQfilter.isNeedFilter() && lsFastQLR.size() < 2) {
				mapCondition2LRFiltered.put(prefix, copeFastq.getMapCondition2LslsFastq().get(prefix));
				continue;
			}
			
			FastQC[] fastQCsBefore = getFastQC(lsFastQLR, prefix, qcBefore);
			mapCond2FastQCBefore.put(prefix, fastQCsBefore);
			FastQC[] fastQCsAfter = getFastQC(lsFastQLR, prefix, qcAfter);
			mapCond2FastQCAfter.put(prefix, fastQCsAfter);
			
			String basicInfoBefore = basicStats.getSavePath(CtrlFastQfilter.getPathPrefix(outFilePrefix + prefix, true));
			String basicInfoAfter = basicStats.getSavePath(CtrlFastQfilter.getPathPrefix(outFilePrefix + prefix, false));
			fastQCsBefore[0].getBasicStats().readTable(basicInfoBefore, true);
			if (fastQCsBefore[1] != null) {
				fastQCsBefore[1].getBasicStats().readTable(basicInfoBefore, false);
			}
			if (!isJustFastqc) {
				fastQCsAfter[0].getBasicStats().readTable(basicInfoAfter, true);
				if (fastQCsAfter[1] != null) {
					fastQCsAfter[1].getBasicStats().readTable(basicInfoAfter, false);
				}
			}
		}
		
		List<String[]> lsSummary = getStatistics();
		String totalExcelPath = outFilePrefix + "basicStatsAll.xls";
		TxtReadandWrite txtWrite = null;
		FileOperate.createFolders(FileOperate.getParentPathNameWithSep(totalExcelPath));
		txtWrite = new TxtReadandWrite(totalExcelPath, true);
		txtWrite.ExcelWrite(lsSummary);
		txtWrite.close();
	}
	
	private List<String[]> getStatistics() {
		List<String[]> lsResult = new ArrayList<>();
		if (isJustFastqc) {
			lsResult.add(new String[]{"SampleName", "Encoding", "TotalReads", "TotalBase","GC%"});
		} else {
			lsResult.add(new String[]{"SampleName", "Encoding", "TotalReads_Before", "TotalBase_Before",
					"TotalReads_After", "TotalBase_After", "ReadsFilter%", "BaseFilter%", "GC%_Before", "GC%_After"});
		}
		
		for (String prefix : mapCond2FastQCBefore.keySet()) {
			List<String> lsTmpResult = new ArrayList<>();
			FastQC[] fastQCBefore = mapCond2FastQCBefore.get(prefix);
			FastQC[] fastQCAfter = mapCond2FastQCAfter.get(prefix);
			lsTmpResult.add(prefix);
			lsTmpResult.add(fastQCBefore[0].getBasicStats().getEncoding());
			long beforeReadsNum = 0, beforeBaseNum = 0;
			double beforeCG = 0;
			if (fastQCBefore[1] != null) {
				beforeReadsNum = fastQCBefore[0].getBasicStats().getReadsNum() + fastQCBefore[1].getBasicStats().getReadsNum();
				beforeBaseNum = fastQCBefore[0].getBasicStats().getBaseNum() + fastQCBefore[1].getBasicStats().getBaseNum();
				beforeCG = (fastQCBefore[0].getBasicStats().getGCpersentage() + fastQCBefore[1].getBasicStats().getGCpersentage())/2;
			} else {
				beforeReadsNum = fastQCBefore[0].getBasicStats().getReadsNum();
				beforeBaseNum = fastQCBefore[0].getBasicStats().getBaseNum();
				beforeCG = fastQCBefore[0].getBasicStats().getGCpersentage();
			}

			long afterReadsNum = 0, afterBaseNum = 0;
			double afterGC = 0;
			if (!isJustFastqc) {
				if (fastQCBefore[1] != null) {
					afterReadsNum = fastQCAfter[0].getBasicStats().getReadsNum() + fastQCAfter[1].getBasicStats().getReadsNum();
					afterBaseNum = fastQCAfter[0].getBasicStats().getBaseNum() + fastQCAfter[1].getBasicStats().getBaseNum();
					afterGC = (fastQCAfter[0].getBasicStats().getGCpersentage() + fastQCAfter[1].getBasicStats().getGCpersentage())/2;
				} else {
					afterReadsNum = fastQCAfter[0].getBasicStats().getReadsNum();
					afterBaseNum = fastQCAfter[0].getBasicStats().getBaseNum();
					afterGC = fastQCAfter[0].getBasicStats().getGCpersentage();
				}
			}
			
			lsTmpResult.add(beforeReadsNum + "");
			lsTmpResult.add(beforeBaseNum + "");
			if (!isJustFastqc) {
				lsTmpResult.add(afterReadsNum + "");
				lsTmpResult.add(afterBaseNum + "");
				lsTmpResult.add((double)afterReadsNum/beforeReadsNum + "");
				lsTmpResult.add((double)afterBaseNum/beforeBaseNum + "");
			}
			lsTmpResult.add(beforeCG + "");
			if (!isJustFastqc) {
				lsTmpResult.add(afterGC + "");
			}
			lsResult.add(lsTmpResult.toArray(new String[0]));
		}
		return lsResult;
	}
	
	/** 用于自动化报告 */
	public Map<String, BasicStats> getMapPrefix2BasicStatsBefore() {
		Map<String, BasicStats> mapPrefix2BasicStats = new LinkedHashMap<String, BasicStats>();
		if (!qcBefore) return new HashMap<>();
		for (String prefix : mapCond2FastQCBefore.keySet()) {
			FastQC[] fastQCBefore = mapCond2FastQCBefore.get(prefix);
			BasicStats basicStats =  fastQCBefore[0].getBasicStats();
			if (fastQCBefore[1] != null) {
				basicStats = basicStats.add(fastQCBefore[1].getBasicStats());
			}
			mapPrefix2BasicStats.put(prefix, basicStats);
		}
		return mapPrefix2BasicStats;
	}
	
	/** 用于自动化报告 */
	public Map<String, BasicStats> getMapPrefix2BasicStatsAfter() {
		Map<String, BasicStats> mapPrefix2BasicStats = new LinkedHashMap<String, BasicStats>();
		if (!qcAfter) return new HashMap<>();
		for (String prefix : mapCond2FastQCAfter.keySet()) {
			FastQC[] fastQCAfter = mapCond2FastQCAfter.get(prefix);
			BasicStats basicStats =  fastQCAfter[0].getBasicStats();
			if (fastQCAfter[1] != null) {
				basicStats = basicStats.add(fastQCAfter[1].getBasicStats());
			}
			mapPrefix2BasicStats.put(prefix, basicStats);
		}
		return mapPrefix2BasicStats;
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
		String[] fileName = createCombineFQname(filtered, outFilePrefix, condition, lsFastq, true);
		fastQs[0] = new FastQ(fileName[0], true);
		if (fileName[1] != null) {
			fastQs[1] = new FastQ(fileName[1], true);
		}
		return fastQs;
	}
	
	private static String[] createCombineFQname(boolean filtered, String outFilePrefix, String condition, List<String[]> lsFastq, boolean isTmp) {
		String[] fastQs = new String[2];
		if (filtered) condition = condition + "_filtered";
		if (lsFastq.size() > 1) condition = condition + "_Combine";
		
		if (lsFastq.get(0).length == 1 || lsFastq.get(0)[1] == null) {
			fastQs[0] = outFilePrefix + condition + ".fq.gz";
			if (isTmp) {
				fastQs[0] = FileOperate.changeFileSuffix(fastQs[0], "_tmpFilter", null);	
			}
		} else {
			fastQs[0] = outFilePrefix + condition + "_1.fq.gz";
			fastQs[1] = outFilePrefix + condition + "_2.fq.gz";
			if (isTmp) {
				fastQs[0] = FileOperate.changeFileSuffix(fastQs[0], "_tmpFilter", null);
				fastQs[1] = FileOperate.changeFileSuffix(fastQs[1], "_tmpFilter", null);
			}
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
			String[] fastqName = createCombineFQname(isFilter, outPrefix, prefix, lsFastQLR, false);
			mapPrefix2LsFilteredFile.put(prefix, fastqName[0]);
			if (fastqName != null) {
				mapPrefix2LsFilteredFile.put(prefix, fastqName[1]);
			}
		}
		return mapPrefix2LsFilteredFile;
	}
}
