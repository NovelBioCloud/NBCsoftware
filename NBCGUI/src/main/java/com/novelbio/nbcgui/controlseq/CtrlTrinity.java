package com.novelbio.nbcgui.controlseq;

import java.util.ArrayList;
import java.util.List;

import com.novelbio.analysis.seq.fastq.FastQ;
import com.novelbio.analysis.seq.mapping.StrandSpecific;
import com.novelbio.analysis.seq.rnaseq.Trinity;


public class CtrlTrinity {
	CopeFastq copeFastq;
	
	int heapSpaceMax = 50;
	int threadNum = 20;
	StrandSpecific strandSpecific = StrandSpecific.NONE;
	boolean jaccard_clip = false;
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
	public void runTrinity() {
		copeFastq.setMapCondition2LsFastQLR();
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
			trinity.setThreadNum(threadNum);
			trinity.setSS_lib_type(strandSpecific);
			trinity.setIsJaccard_clip(jaccard_clip);
			trinity.setPairs_distance(insertSize);
			trinity.setOutputPath(outPrefix + prefix);
			trinity.setLsLeftFq(lsFqLeft);
			if (lsFqRight.size() > 0) {
				trinity.setLsRightFq(lsFqRight);
			}
			trinity.runTrinity();
		}
	}
	
}
