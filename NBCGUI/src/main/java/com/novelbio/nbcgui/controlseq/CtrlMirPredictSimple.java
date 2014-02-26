package com.novelbio.nbcgui.controlseq;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.novelbio.analysis.seq.mirna.NovelMiRNADeep;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.database.domain.information.SoftWareInfo;
import com.novelbio.database.domain.information.SoftWareInfo.SoftWare;
import com.novelbio.database.model.species.Species;

/**
 * 简单的miRNA预测类，可用于查找isoMiRNA<br>
 * isoMiRNA:一篇plos one的文章中的分析，只需要跑一遍miRNA的预测即可<br>
 * <b>仅供Illumina数据使用</b> Proton数据错误率略高，本分析得到的结果不可靠
 */
public class CtrlMirPredictSimple {
	Species species;
	String outPath;
	String outPathSample;
	
	Map<String, String> mapPrefix2File;
	
	NovelMiRNADeep novelMiRNADeep = new NovelMiRNADeep();
	SoftWareInfo softWareInfo = new SoftWareInfo();
	
	boolean isUseOldResult = true;
	String novelMiRNAmrd;
	List<String> lsCmd = new ArrayList<>();
	boolean isFastq = false;

	/** 是否为fastq文件，默认为false--也就是fasta文件 */
	public void setIsFastq(boolean isFastq) {
		this.isFastq = isFastq;
	}
	public void setMapPrefix2Fastq(Map<String, String> mapPrefix2File) {
		this.mapPrefix2File = mapPrefix2File;
	}
	
	public void setIsUseOldResult(boolean isUseOldResult) {
		this.isUseOldResult = isUseOldResult;
	}
	public void setSpecies(Species species) {
		this.species = species;
	}
	
	public void setNovelMiRNAmrd(String novelMiRNAmrd) {
		this.novelMiRNAmrd = novelMiRNAmrd;
	}

	/**
	 *  输出文件夹
	 * @param outPath 输入汇总结果路径
	 * @param outPathSample 输出样本文件夹
	 * @param outPathTmp 临时文件夹
	 * @param samStatisticsPath 统计报告文件夹
	 */
	public void setOutPath(String outPath, String outPathSample) {
		this.outPath = outPath;
		this.outPathSample = outPathSample;
	}
	
	public void runMiRNApredict() {
		for (String prefix : mapPrefix2File.keySet()) {
			String fastIn = mapPrefix2File.get(prefix);
			runMiRNApredictSample(prefix, fastIn);
		}
	}
	
	public void runMiRNApredictSample(String prefix, String fastIn) {
		lsCmd.clear();
		String novelMiRNAPathDeep = outPathSample + prefix + "/miRNApredictDeep/";
		if (!FileOperate.createFolders(novelMiRNAPathDeep)) {
			throw new RuntimeException("cannot create fold: " + novelMiRNAPathDeep);
		}
		predict(fastIn, novelMiRNAPathDeep);
	}
	
	private void predict(String fastIn, String novelMiRNAPathDeep) {
		novelMiRNADeep.setFastaInput(fastIn);
		softWareInfo.setName(SoftWare.mirDeep);
		novelMiRNADeep.setExePath(softWareInfo.getExePath(), species.getIndexChr(SoftWare.bowtie));
		novelMiRNADeep.setMiRNASeq(species.getMiRNAmatureFile(), null, species.getMiRNAhairpinFile());
		novelMiRNADeep.setSpecies(species.getCommonName());
		novelMiRNADeep.setFastq(isFastq);
		novelMiRNADeep.setOutPath(novelMiRNAPathDeep);
		novelMiRNADeep.setNovelMiRNAdeepMrdFile(novelMiRNAmrd);
		novelMiRNADeep.predict();
		lsCmd.addAll(novelMiRNADeep.getCmdExeStr());
	}
	
}
