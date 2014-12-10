package com.novelbio.nbcgui.controlseq;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.novelbio.analysis.IntCmdSoft;
import com.novelbio.analysis.seq.denovo.N50AndSeqLen;
import com.novelbio.analysis.seq.mapping.StrandSpecific;
import com.novelbio.analysis.seq.rnaseq.Trinity;
import com.novelbio.analysis.seq.rnaseq.TrinityCopeIso;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.listOperate.HistList;

public class CtrlTrinity implements IntCmdSoft {
	CopeFastq copeFastq;
	List<String> lsCmd = new ArrayList<>();
	
	int heapSpaceMax = 50;
	int threadNum = 20;
	StrandSpecific strandSpecific = StrandSpecific.NONE;
	
	String genome;
	String genomeSortedBam;
	/** maximum allowed intron length (also maximum fragment span on genome) */
	int intronMaxLen = 50000;
	
	/**
	 * If you have especially large RNA-Seq data sets involving many hundreds of
	 * millions of reads to billions of reads, consider performing an in silico
	 * normalization of the full data set using Trinity --normalize_reads. The
	 * default normalization process should work well for most data sets. If you
	 * prefer to manually set normalization-related parameters, you can find the
	 * options under the full Trinity usage info:
	 */
	boolean isNormalizeReads = false;
	
	
	boolean jaccard_clip = false;
	/** 默认为1，设置为2时可以大大降低内存占用 */
	Integer min_kmer_cov;
	int insertSize = 500;
	String outPrefix;
	
	/** 单位是G，默认50G */
	public void setHeapSpaceMax(int heapSpaceMax) {
		if (heapSpaceMax < 0 || heapSpaceMax > 100) {
			return;
		}
		this.heapSpaceMax = heapSpaceMax;
	}
	/**默认20线程 */
	public void setThreadNum(int threadNum) {
		if (threadNum < 0 || threadNum > 100) {
			return;
		}
		this.threadNum = threadNum;
	}
	/** 默认没有链特异性 */
	public void setStrandSpecific(StrandSpecific strandSpecific) {
		this.strandSpecific = strandSpecific;
	}
	public void setInsertSize(int insertSize) {
		this.insertSize = insertSize;
	}
	/**只需将copeFastq设定好fastq等信息即可，不需要调用其{@link copeFastq#setMapCondition2LsFastQLR()}方法*/
	public void setCopeFastq(CopeFastq copeFastq) {
		this.copeFastq = copeFastq;
	}
	
	/** 输出文件夹 */
	public void setOutPrefix(String outPrefix) {
		this.outPrefix = outPrefix;
	}
	/** 默认为1，设置为2时可以大大降低内存占用 */
	public void setMin_kmer_cov(Integer min_kmer_cov) {
		this.min_kmer_cov = min_kmer_cov;
	}
	public void setGenome(String genome) {
		this.genome = genome;
	}
	/** 有的话就用该bam文件指导拼接，没有就自己做mapping */
	public void setGenomeSortedBam(String genomeSortedBam) {
		this.genomeSortedBam = genomeSortedBam;
	}
	/**
	 * If you have especially large RNA-Seq data sets involving many hundreds of
	 * millions of reads to billions of reads, consider performing an in silico
	 * normalization of the full data set using Trinity --normalize_reads. The
	 * default normalization process should work well for most data sets. If you
	 * prefer to manually set normalization-related parameters, you can find the
	 * options under the full Trinity usage info:
	 */
	public void setNormalizeReads(boolean isNormalizeReads) {
		this.isNormalizeReads = isNormalizeReads;
	}
	/** 最长intron的长度，也是用于指导拼接的，默认50000bp */
	public void setIntronMaxLen(int intronMaxLen) {
		this.intronMaxLen = intronMaxLen;
	}
	
	/**
	 * option, set if you have paired reads and you expect high gene density with
	 *  UTR overlap (use FASTQ input file format for reads). (note: jaccard_clip is an
	 *  expensive operation, so avoid using it unless necessary due to finding 
	 *  excessive fusion transcripts w/o it.)
	 * @param jaccard_clip 默认false，一般不要修改它。真菌的考虑设置为true，问王俊宁确认
	 */
	public void setJaccard_clip(boolean jaccard_clip) {
		this.jaccard_clip = jaccard_clip;
	}
	
	/**
	 * 运行Trinity，运行结束后返回 prefix对应拼接结果的文件
	 * @return
	 */
	private Map<String, String> runTrinity_getMapPrefix2File() {
		lsCmd.clear();
		copeFastq.setMapCondition2LsFastQLR();
		Map<String, String> mapPrefix2Trinity = new LinkedHashMap<>();
		for (String prefix : copeFastq.getLsPrefix()) {
			List<String[]> lsFastQs = copeFastq.getMapCondition2LsFastQLR().get(prefix);
			List<String> lsFqLeft = new ArrayList<>();
			List<String> lsFqRight = new ArrayList<>();
			for (String[] fastQ : lsFastQs) {
				lsFqLeft.add(fastQ[0]);
				if (fastQ.length > 1) {
					lsFqRight.add(fastQ[1]);
				}
			}
			
			Trinity trinity = new Trinity();
			trinity.setBflyHeapSpaceMax(heapSpaceMax);
			trinity.setJellyfishMemory(heapSpaceMax);
			trinity.setThreadNum(threadNum);
			trinity.setSS_lib_type(strandSpecific);
			trinity.setIsJaccard_clip(jaccard_clip);
			trinity.setGenome(genome);
			trinity.setGenomeSortedBam(genomeSortedBam);
			trinity.setIntronMaxLen(intronMaxLen);
			trinity.setNormalizeReads(isNormalizeReads);
			trinity.setIsFull_cleanup(true);
			
			if (min_kmer_cov != null && min_kmer_cov > 0) {
				trinity.setMin_kmer_cov(min_kmer_cov);
			}
			
			trinity.setPairs_distance(insertSize);
			trinity.setOutputPath(outPrefix + prefix);
			trinity.setLsLeftFq(lsFqLeft);
			if (lsFqRight.size() > 0) {
				trinity.setLsRightFq(lsFqRight);
			}
			lsCmd.addAll(trinity.getCmdExeStr());
			trinity.runTrinity();
			String outFile = trinity.getResultPath();
			mapPrefix2Trinity.put(prefix, outFile);
		}
		return mapPrefix2Trinity;
	}
	
	private void copeAfterAssembly(String trinityFile) {
		if (!FileOperate.isFileExistAndBigThanSize(trinityFile, 0)) {
			return;
		}
		N50AndSeqLen n50AndSeqLen = new N50AndSeqLen(trinityFile);
		n50AndSeqLen.doStatistics();
		//TODＯ 这里需要自动化生成图表
		HistList histList = n50AndSeqLen.gethListLength();
		n50AndSeqLen.getLsNinfo();
		
		TrinityCopeIso trinityCopeIso = new TrinityCopeIso();
		trinityCopeIso.setInFileName(trinityFile);
		trinityCopeIso.setOutTrinityGeneFile(FileOperate.changeFileSuffix(trinityFile, "_Gene", "fa"));
		trinityCopeIso.setOutTrinityIsoFile(FileOperate.changeFileSuffix(trinityFile, "_Iso", "fa"));
		trinityCopeIso.removeTmpFile();
		trinityCopeIso.cope();
	}
	@Override
	public List<String> getCmdExeStr() {
		return lsCmd;
	}
	
	public void running() {
		runTrinity_getMapPrefix2File();
//		Map<String, String> mapAssemblyResult = runTrinity_getMapPrefix2File();
//		for (String fileName: mapAssemblyResult.keySet()) {
//			System.out.println("Trinity Assembly result is " + fileName);
//		}
	}
	
}
