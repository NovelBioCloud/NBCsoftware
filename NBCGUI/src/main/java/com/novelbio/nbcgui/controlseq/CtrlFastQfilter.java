package com.novelbio.nbcgui.controlseq;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.collect.HashMultimap;
import com.novelbio.analysis.seq.fastq.FastQ;
import com.novelbio.analysis.seq.fastq.FastQC;
import com.novelbio.analysis.seq.fastq.FastQReadingChannel;
import com.novelbio.analysis.seq.fastq.FastQRecordFilter;
import com.novelbio.base.fileOperate.FileOperate;

/** 单独的fq过滤模块 */
@Component
@Scope("prototype")
public class CtrlFastQfilter {
	private static final Logger logger = Logger.getLogger(CtrlFastQ.class);
	public static String FOLDER_NAME = "QCImages" + FileOperate.getSepPath();
	
	FastQRecordFilter fastQfilterRecord;

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
	
	/** 设定过滤参数 */
	public void setFastQfilterParam(FastQRecordFilter fastQfilterRecord) {
		this.fastQfilterRecord = fastQfilterRecord;
	}
	
	public void setOutFilePrefix(String outFilePrefix) {
		this.outFilePrefix = outFilePrefix;
	}
	public void setPrefix(String prefix) {
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

	public void setFastQLRfiltered(FastQ[] fastQLRfiltered) {
		this.fastQLRfiltered = fastQLRfiltered;
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
		fastQReadingChannel.setFastQRead(lsFastQLR);
		// QC before Filter
		fastQReadingChannel.setFastQC(fastQCbefore[0], fastQCbefore[1]);
		// Filter
		fastQReadingChannel.setFilter(fastQfilterRecord, lsFastQLR.get(0)[0].getOffset());
		// QC after Filter
		fastQReadingChannel.setFastQC(fastQCafter[0], fastQCafter[1]);

		fastQReadingChannel.setFastQWrite(fastQLRfiltered[0], fastQLRfiltered[1]);
		fastQReadingChannel.setThreadNum(8);
		fastQReadingChannel.run();
	}
	
	/**
	 * 读取fastQC并生成excel表格，并遍历图片,以及对应的参数
	 * @param fastQCs
	 * @param isBefore
	 * @return 返回需要保存的参数文件
	 */
	public HashMultimap<String, String> saveFastQC(String savePathAndPrefix) {
		String fileName = FileOperate.getParentPathName(savePathAndPrefix) + "QCimage/" + FileOperate.getFileName(savePathAndPrefix);
		FileOperate.createFolders(FileOperate.getParentPathName(fileName) );
		HashMultimap<String, String> mapParam = HashMultimap.create();
		try {
			if (fastQCbefore.length <= 1 || fastQCbefore[1] == null) {
				fastQCbefore[0].saveToPath(savePathAndPrefix + "_BeforeFilter");
			} else {
				fastQCbefore[0].saveToPath(30, fastQCbefore[1], fileName + "_BeforeFilter");
			}
			mapParam.putAll(fastQCbefore[0].getMapParam(fileName + "_BeforeFilter"));

			if (fastQCafter.length <= 1 || fastQCafter[1] == null) {
				fastQCafter[0].saveToPath(savePathAndPrefix + "_AfterFilter");
			} else {
				fastQCafter[0].saveToPath(30, fastQCafter[1], fileName + "_AfterFilter");
			}
			mapParam.putAll(fastQCafter[0].getMapParam(fileName + "_AfterFilter"));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取报告生成器出错！");
		}
		return mapParam;
	}

}
