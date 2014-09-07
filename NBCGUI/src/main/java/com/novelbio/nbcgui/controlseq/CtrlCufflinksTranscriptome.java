package com.novelbio.nbcgui.controlseq;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.HashMultimap;
import com.novelbio.analysis.IntCmdSoft;
import com.novelbio.analysis.seq.fasta.SeqHash;
import com.novelbio.analysis.seq.genome.GffChrAbs;
import com.novelbio.analysis.seq.genome.GffHashModifyNewGffORF;
import com.novelbio.analysis.seq.genome.GffHashModifyOldGffUTR;
import com.novelbio.analysis.seq.genome.gffOperate.GffHashGene;
import com.novelbio.analysis.seq.genome.gffOperate.GffType;
import com.novelbio.analysis.seq.mapping.StrandSpecific;
import com.novelbio.analysis.seq.rnaseq.CuffMerge;
import com.novelbio.analysis.seq.rnaseq.CufflinksGTF;
import com.novelbio.analysis.seq.rnaseq.GffHashMerge;
import com.novelbio.analysis.seq.rnaseq.TranscriptomStatistics;
import com.novelbio.base.ExceptionNullParam;
import com.novelbio.base.FoldeCreate;
import com.novelbio.base.dataOperate.TxtReadandWrite;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.nbcReport.Params.EnumReport;

public class CtrlCufflinksTranscriptome implements IntCmdSoft {
	boolean reconstructTranscriptome = false;
	
	CufflinksGTF cufflinksGTF = new CufflinksGTF();
	GffChrAbs gffChrAbs = new GffChrAbs();
	String outPrefix;
	int thread = 4;
	String gtfRefFile;
	String chrSeq;
//	String outGtf;
	String outStatistics;
	String prefixNewGene;
	
	boolean isUseOldResult = true;
	
	/** 是否修正新的Gtf文件 */
	boolean isModifyNewGTF = true;
	boolean isAddUtrToRefGtf = true;
	
	List<String> lsCmd = new ArrayList<>();
	
	/** 新基因的前缀 */
	public void setGenePrefixNew(String prefixNewGene) {
		if (prefixNewGene == null || prefixNewGene.trim().equals("")) {
			return;
		}
		prefixNewGene = prefixNewGene.trim();
		if (!prefixNewGene.endsWith("_")) {
			prefixNewGene += "_";
		}
		this.prefixNewGene = prefixNewGene;
	}
	/** 使用上次跑出来的结果
	 * 同样的参数重跑，遇到上次跑出来的结果是否可以直接使用而不重跑
	 *  */
	public void setIsUseOldResult(boolean isUseOldResult) {
		this.isUseOldResult = isUseOldResult;
	}
	/** 是否修正新的GTF文件
	 * true修正cufflinks产生的gtf文件
	 * false修正原来的gtf文件，主要是加上utr区域，一般用于低等生物
	 * @param isModifyNewGTF
	 */
	public void setModifyNewGTF(boolean isModifyNewGTF) {
		this.isModifyNewGTF = isModifyNewGTF;
	}
	/** 是否修正新的GTF文件
	 * true修正cufflinks产生的gtf文件
	 * false修正原来的gtf文件，主要是加上utr区域，一般用于低等生物
	 * @param isModifyNewGTF
	 */
	public void setAddUtrToRefGtf(boolean isAddUtrToRefGtf) {
		this.isAddUtrToRefGtf = isAddUtrToRefGtf;
	}
	/** 输入已有的物种信息<br>
	 * 注意如果还输入了gff文件和chr文件，本类会修改该gffChrAbs
	 * 和{@link #setGTFfile(String)} 两者选一
	 * @param gffChrAbs 注意结束后会关闭流
	 */
	public void setGffChrAbs(GffChrAbs gffChrAbs) {
		if (gffChrAbs == null) {
			return;
		}
		this.gffChrAbs = gffChrAbs;
	}
	/**
	 * species的chr文件优先级高于该文件
	 * @param chrSeq
	 */
	public void setChrSeq(String chrSeq) {
		this.chrSeq = chrSeq;
	}
	/** 用额外的GTF辅助重建转录本<br>
	 * 和{@link #setGffChrAbs(gffChrAbs)} 两者选一
	 * 优先级高于gffChrAbs
	 * @param gtfFile
	 */
	public void setGTFfile(String gtfFile) {
		this.gtfRefFile = gtfFile;
	}
	public void setLsBamFile2Prefix(ArrayList<String[]> lsBamFile2Prefix) {
		cufflinksGTF.setBam(lsBamFile2Prefix);
	}
	public void setThreadNum(int threadNum) {
		this.thread = threadNum;
		cufflinksGTF.setThreadNum(threadNum);
	}
	public void setStrandSpecifictype(StrandSpecific strandSpecific) {
		if (strandSpecific == null) {
			throw new ExceptionNullParam("StrandSpecific is null");
		}
		cufflinksGTF.setStrandSpecifictype(strandSpecific);
	}

	/** 是否重建转录本 */
	public void setReconstructTranscriptome(boolean reconstructTranscriptome) {
		this.reconstructTranscriptome = reconstructTranscriptome;
	}
	/** 是否根据prefix合并bam文件，然后再做cufflinks分析 默认false */
	public void setIsMergeBamByPrefix(boolean isMergeBamByPrefix) {
		cufflinksGTF.setIsMergeBamByPrefix(isMergeBamByPrefix);
	}
	/** 在junction 的一头搭上exon的最短比例 0-1之间，默认0.09 */
	public void setSmallAnchorFraction(double anchorLength) {
		cufflinksGTF.setSmallAnchorFraction(anchorLength);
	}
	/** 是否用上四分之一位点标准化 */
	public void setUpQuartileNormalized(boolean isUpQuartileNormalized) {
		cufflinksGTF.setUpQuartileNormalized(isUpQuartileNormalized);
	}

	public void setOutPathPrefix(String outPathPrefix) {
		this.outPrefix = FoldeCreate.createAndInFold(outPathPrefix, EnumReport.ReconstructTranscriptome.getResultFolder());
		String tmpCufflinksResult = FileOperate.getPathName(outPrefix) + "tmpCufflinks/";
		FileOperate.createFolders(tmpCufflinksResult);
		cufflinksGTF.setOutPathPrefix(tmpCufflinksResult);
	}
	
	public void run() {
		lsCmd.clear();
		reconstruct();
		close();
	}
	
	public void reconstruct() {
		setGffHashRef();
		setSeqHash();

		cufflinksGTF.setChrFile(getChromFaFile());
		cufflinksGTF.setGtfFile(getGtfFileName(), reconstructTranscriptome);
		cufflinksGTF.setIntronLen(getIntronSmall2Big());
		cufflinksGTF.setSkipErrorMode(true);
		cufflinksGTF.setIsUseOldResult(isUseOldResult);
		try {
			cufflinksGTF.runCufflinks();
		} catch (Throwable e) {
			lsCmd.addAll(cufflinksGTF.getCmdExeStr());
			throw e;
		}
		lsCmd.addAll(cufflinksGTF.getCmdExeStr());
		List<String> lsResultGTF = cufflinksGTF.getLsCufflinksResult();

		if (!reconstructTranscriptome) {
			return;
		}
		
		outStatistics =  outPrefix + "novelTranscriptomStatistics.txt";
		String resultGtf = null;
		String outMergePrefix = outPrefix + "tmpMerge";
		if (lsResultGTF.size() > 1) {
			CuffMerge cuffMerge = new CuffMerge();
			cuffMerge.setIsUseOldResult(isUseOldResult);
			cuffMerge.setLsGtfTobeMerged(lsResultGTF);
			cuffMerge.setRefGtf(cufflinksGTF.getGtfReffile());
			cuffMerge.setOutputPrefix(outMergePrefix);
			try { cuffMerge.setRefChrFa(gffChrAbs.getSpecies().getChromSeq()); } catch (Exception e) { }
			cuffMerge.setThreadNum(thread);
			try {
				resultGtf = cuffMerge.runCuffmerge();
			} catch (Throwable e) {
				lsCmd.addAll(cuffMerge.getCmdExeStr());
				throw e;
			}
			lsCmd.addAll(cuffMerge.getCmdExeStr());
		} else if (lsResultGTF.size() == 1) {
			resultGtf = lsResultGTF.get(0);
		}
		if (gffChrAbs.getGffHashGene() == null) {
			FileOperate.copyFile(resultGtf, outPrefix + "novelTranscriptom.gtf", true);
		}
		if (isModifyNewGTF) {
			modifyCufflinksGtf(resultGtf, outPrefix + "novelTranscriptom.gtf");
		}
		if (isAddUtrToRefGtf) {
			modifyOldGtf(resultGtf, outPrefix + "RefAddUtrTranscriptom.gtf");
		}
	}
	
	/** 
	 * @param resultGtf 重建完转录本的gtf文件
	 * @param outGtf 输出结果文件
	 */
	private void modifyCufflinksGtf(String resultGtf, String outGtf) {
		//TODO 需要增加预测ncRNA和orf的模块
		if (gffChrAbs == null || gffChrAbs.getGffHashGene() == null) {
			return;
		}
		
		//注释orf
		GffHashGene gffHashGeneThis = new GffHashGene(GffType.GTF, resultGtf);
		GffHashModifyNewGffORF gffHashModifyORF = new GffHashModifyNewGffORF();
		gffHashModifyORF.setGffHashGeneRaw(gffHashGeneThis);

		gffHashModifyORF.setGffHashGeneRef(gffChrAbs.getGffHashGene());
		gffHashModifyORF.setRenameGene(true);
		
		gffHashModifyORF.setRenameIso(false);//TODO 可以考虑不换iso的名字
		gffHashModifyORF.modifyGff();
		GffHashGene gffHashGeneModify = gffHashModifyORF.getGffResult();
		List<String> lsChrName = (gffChrAbs.getSeqHash() != null)? gffChrAbs.getSeqHash().getLsSeqName() : null;
		gffHashGeneModify.writeToGTF(lsChrName, outGtf);
		
		GffHashMerge gffHashMerge = new GffHashMerge();
		gffHashMerge.setSeqHash(gffChrAbs.getSeqHash());
		gffHashMerge.setGffHashGeneRef(gffChrAbs.getGffHashGene());
		gffHashMerge.addGffHashGene(gffHashGeneModify);
		TranscriptomStatistics transcriptomStatistics = gffHashMerge.getStatisticsCompareGff();
		TxtReadandWrite txtOut = new TxtReadandWrite(outStatistics, true);
		txtOut.ExcelWrite(transcriptomStatistics.getStatisticsResult());
		txtOut.close();
	}
	
	/** 
	 * @param resultGtf 重建完转录本的gtf文件
	 * @param outGtf 输出结果文件
	 */
	private void modifyOldGtf(String resultGtf, String outGtf) {
		if (gffChrAbs == null || gffChrAbs.getGffHashGene() == null) {
			return;
		}
		
		GffHashModifyOldGffUTR gffHashModifyOldGffUTR = new GffHashModifyOldGffUTR();
		GffHashGene gffHashGeneThis = new GffHashGene(GffType.GTF, resultGtf);
		gffHashModifyOldGffUTR.setGffHashGeneRaw(gffHashGeneThis);
		gffHashModifyOldGffUTR.setGffHashGeneRef(gffChrAbs.getGffHashGene());
		gffHashModifyOldGffUTR.modifyGff();
		
		List<String> lsChrName = (gffChrAbs.getSeqHash() != null)? gffChrAbs.getSeqHash().getLsSeqName() : null;
		gffChrAbs.getGffHashGene().writeToGTF(lsChrName, outGtf);
	}
	
	
	/** 根据gff文件，返回最小和最大intron的长度
	 * 如果gff不存在，则返回null
	 * @return
	 */
	private int[] getIntronSmall2Big() {
		GffHashGene gffHashGene = gffChrAbs.getGffHashGene();
		if (gffHashGene == null) {
			return null;
		}
		List<Integer> lsIntronSortedS2M = gffHashGene.getLsIntronSortedS2M();
		int intronLenMin = lsIntronSortedS2M.get(50);
		int intronLenMax = lsIntronSortedS2M.get(lsIntronSortedS2M.size() - 10);
		return new int[]{intronLenMin, intronLenMax};
	}
	
	private String getGtfFileName() {
		String gtfFile = null;
		if (FileOperate.isFileExistAndBigThanSize(gtfRefFile, 10)) {
			gtfFile = gtfRefFile;
		} else if (gffChrAbs != null && gffChrAbs.getGffHashGene() != null) {
			gtfFile = gffChrAbs.getGtfFile();
		}
		return gtfFile;
	}
	private String getChromFaFile() {
		String chromFa = null;
		if (gffChrAbs != null && gffChrAbs.getSeqHash() != null) {
			chromFa = gffChrAbs.getSeqHash().getChrFile();
		} else if(FileOperate.isFileExistAndBigThanSize(chrSeq, 10)) {
			chromFa = chrSeq;
		}
		return chromFa;		
	}
	
	private void setGffHashRef() {
		if (FileOperate.isFileExistAndBigThanSize(gtfRefFile, 10)) {
			GffHashGene gffHashGene = new GffHashGene(GffType.GTF, gtfRefFile);
			gffChrAbs.setGffHash(gffHashGene);
		}
	}
	
	private void setSeqHash() {
		if (FileOperate.isFileExistAndBigThanSize(chrSeq, 10)) {
			SeqHash seqHash = new SeqHash(chrSeq, null);
			if (gffChrAbs.getSeqHash() != null) {
				gffChrAbs.getSeqHash().close();
				gffChrAbs.setSeqHash(seqHash);
			}
		}
	}
	/** 关闭gffChrAbs中的seqhash信息 */
	private void close() {
		gffChrAbs.close();
	}
	
	/** 返回预测的文件名
	 * @param isFilter 是否过滤，如果不过滤就直接合并
	 * @return
	 */
	public static HashMultimap<String, String> getPredictMapPrefix2FilteredFQ(String outPrefix, boolean isReconstruct) {
		HashMultimap<String, String> mapPrefix2File = HashMultimap.create();
		if (!isReconstruct) return mapPrefix2File;
			
		String outFoldPrefix = FoldeCreate.getInFold(outPrefix, EnumReport.ReconstructTranscriptome.getResultFolder());
		String outGtf = outFoldPrefix + "novelTranscriptom.gtf";
		String outStatistics = outFoldPrefix + "novelTranscriptomStatistics.txt";
		mapPrefix2File.put("reconstruct_gtf", outGtf);
		mapPrefix2File.put("reconstruct_statistics", outStatistics);
		return mapPrefix2File;
	}
	@Override
	public List<String> getCmdExeStr() {
		// TODO Auto-generated method stub
		return null;
	}

}
