package com.novelbio.nbcgui.controlseq;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.collect.HashMultimap;
import com.novelbio.analysis.seq.fasta.CopeFastq;
import com.novelbio.analysis.seq.fastq.FastQ;
import com.novelbio.analysis.seq.fastq.FastQC;
import com.novelbio.analysis.seq.fastq.FastQReadingChannel;
import com.novelbio.analysis.seq.fastq.FastQFilter;
import com.novelbio.base.ExceptionNullParam;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.multithread.RunProcess.RunThreadStat;

/** 单独的fq过滤模块 */
@Component
@Scope("prototype")
public class CtrlFastQfilter {
	private static final Logger logger = Logger.getLogger(CtrlFastQ.class);
	public static String FOLDER_NAME = "QCResults" + FileOperate.getSepPath();
	
	FastQFilter fastQfilterRecord;

	String outFilePrefix = "";
	String prefix = "";
	/**
	 * 前缀和该前缀所对应的一系列fastq文件。
	 * 如果是单端，则Fastq[]长度为1，如果是双端，则Fastq[]长度为2
	 */
	List<FastQ[]> lsFastQLR = new ArrayList<FastQ[]>();
	/** 过滤好的结果 */
	FastQ[] fastQLRfiltered;

	/** 过滤前质控 */
	FastQC[] fastQCbefore;
	/** 过滤后质控 */
	FastQC[] fastQCafter;
	
	HashMultimap<String, String> mapPrefix2QCresult = HashMultimap.create();
	
	/** 是否检查文件格式 */
	boolean isCheckFormat = true;
	boolean isJustFastqc = false;
	
	/** 设定过滤参数 */
	public void setFastQfilterParam(FastQFilter fastQfilterRecord) {
		if (fastQfilterRecord == null) {
			throw new ExceptionNullParam("No Param fastQfilterRecord");
		}
		this.fastQfilterRecord = fastQfilterRecord;
	}
	/** 是否只进行fastqc工作 */
	public void setJustFastqc(boolean isJustFastqc) {
		this.isJustFastqc = isJustFastqc;
	}
	public void setCheckFormat(boolean isCheckFormat) {
		this.isCheckFormat = isCheckFormat;
	}
	public void setOutFilePrefix(String outFilePrefix) {
		this.outFilePrefix = outFilePrefix;
	}
	public void setPrefix(String prefix) {
//		reportQC.setTeamName(prefix);
		this.prefix = prefix;
	}
	
	/**
	 * 前缀和该前缀所对应的一系列fastq文件。
	 * 如果是单端，则Fastq[]长度为1，如果是双端，则Fastq[]长度为2
	 */
	public void setLsFastQLR(List<String[]> lsFastQLR) {
		if (lsFastQLR == null) {
			this.lsFastQLR = null;
		}
		this.lsFastQLR = new ArrayList<>();
		for (String[] strings : lsFastQLR) {
			FastQ[] fastQs = CopeFastq.convertFastqFile(strings);
			this.lsFastQLR.add(fastQs);
		}
	}
	
	public String getOutFilePrefix() {
		return outFilePrefix;
	}

	public void setFastQLRfilteredOut(FastQ[] fastQLRfiltered) {
		this.fastQLRfiltered = fastQLRfiltered;
	}
	public FastQ[] getFastQLRfiltered() {
		return fastQLRfiltered;
	}
	public void setFastQCbefore(FastQC[] fastQCbefore) {
		this.fastQCbefore = fastQCbefore;
	}
	public void setFastQCafter(FastQC[] fastQCafter) {
		this.fastQCafter = fastQCafter;
	}
	public FastQC[] getFastQCbefore() {
		return fastQCbefore;
	}
	public FastQC[] getFastQCafter() {
		return fastQCafter;
	}
	public String getPrefix() {
		return prefix;
	}
	public void filteredAndCombineReads() {
		FastQReadingChannel fastQReadingChannel = new FastQReadingChannel();
		fastQReadingChannel.setCheckFormat(isCheckFormat);
		fastQReadingChannel.setFastQRead(lsFastQLR);
		// QC before Filter
		fastQReadingChannel.setFastQC(fastQCbefore[0], fastQCbefore[1]);
		fastQReadingChannel.setOutputResult(!isJustFastqc);
		if (!isJustFastqc) {
			// Filter
			fastQReadingChannel.setFilter(fastQfilterRecord, lsFastQLR.get(0)[0].getOffset());
			// QC after Filter
			fastQReadingChannel.setFastQC(fastQCafter[0], fastQCafter[1]);
			fastQReadingChannel.setFastQWrite(fastQLRfiltered[0], fastQLRfiltered[1]);
		}
		
		fastQReadingChannel.setThreadNum(8);
		fastQReadingChannel.run();
		if (fastQReadingChannel.getRunThreadStat() == RunThreadStat.finishAbnormal) {
			throw new RuntimeException(fastQReadingChannel.getException());
		}
	}
	
	public static String getFastQCPicName(String savePathAndPrefix) {
		String fileName = FileOperate.getParentPathNameWithSep(savePathAndPrefix) + FOLDER_NAME + FileOperate.getFileName(savePathAndPrefix);
		fileName = FastQC.getQualityScoreFileName(fileName + "_BeforeFilter");
		return fileName;
	}
	/**
	 * 读取fastQC并生成excel表格，并遍历图片,以及对应的参数
	 * @param fastQCs
	 * @param isBefore
	 * @return 返回需要保存的参数文件
	 */
	public HashMultimap<String, String> saveFastQC(String savePathAndPrefix) {
		mapPrefix2QCresult.clear();
		String fileNameBefore = getPathPrefix(savePathAndPrefix, true);
		String fileNameAfter = getPathPrefix(savePathAndPrefix, false);
		FileOperate.createFolders(FileOperate.getParentPathNameWithSep(fileNameAfter) );
		HashMultimap<String, String> mapParam = HashMultimap.create();
		try {
			List<String> lsPic = null, lsTable = null;
			if (fastQCbefore.length <= 1 || fastQCbefore[1] == null) {
				lsPic = fastQCbefore[0].saveToPathPic(fileNameBefore);
				lsTable = fastQCbefore[0].saveToPathTable(fileNameBefore);
			} else {
				lsPic = fastQCbefore[0].saveToPathPic(30, fastQCbefore[1], fileNameBefore);
				lsTable = fastQCbefore[0].saveToPathTable(fastQCbefore[1], fileNameBefore);
			}
			mapPrefix2QCresult.putAll("BeforeFilter_Pic", lsPic);
			mapPrefix2QCresult.putAll("BeforeFilter_Table", lsTable);
			
			mapParam.putAll(fastQCbefore[0].getMapParam(fileNameBefore));
			if (!isJustFastqc) {
				if (fastQCafter.length <= 1 || fastQCafter[1] == null) {
					lsPic = fastQCafter[0].saveToPathPic(fileNameAfter);
					lsTable = fastQCafter[0].saveToPathTable(fileNameAfter);
				} else {
					lsPic = fastQCafter[0].saveToPathPic(30, fastQCafter[1], fileNameAfter);
					lsTable = fastQCafter[0].saveToPathTable(fastQCafter[1], fileNameAfter);
				}
				mapPrefix2QCresult.putAll("AfterFilter_Pic", lsPic);
				mapPrefix2QCresult.putAll("AfterFilter_Table", lsTable);
				mapParam.putAll(fastQCafter[0].getMapParam(fileNameAfter));
			}
			
			//TODO 这里有问题，是否只将fastq过滤后的结果放入报告
			fillReport(lsPic, lsTable);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取报告生成器出错！");
		}
		return mapParam;
	}
	
	private void fillReport(List<String> lsPicPathAndNames, List<String> lsEecelPathAndNames) {
//		for (String excelPath : lsEecelPathAndNames) {
//			reportQC.addResultFile(excelPath);
//		}
//		for (String picPath : lsPicPathAndNames) {
//			if (picPath.contains("QualityScore")) {
//				XdocTmpltPic xdocTmpltPic = new XdocTmpltPic(picPath);
//				reportQC.addXdocTempPic(xdocTmpltPic);
//			}
//			if (picPath.contains("SequenceGCContent")) {
//				XdocTmpltPic xdocTmpltPic1 = new XdocTmpltPic(picPath);
//				reportQC.addXdocTempPic1(xdocTmpltPic1);
//			}
//			reportQC.addResultFile(picPath);
//		}
	}
	
	public HashMultimap<String, String> getMapPrefix2QCresult() {
		return mapPrefix2QCresult;
	}
	
	public static String getPathPrefix(String savePathAndPrefix, boolean isBeforeFilter) {
		String fileName = FileOperate.getParentPathNameWithSep(savePathAndPrefix) + FOLDER_NAME + FileOperate.getFileName(savePathAndPrefix);
		fileName += isBeforeFilter?  "_BeforeFilter" : "_AfterFilter";
		return fileName;
	}
}
