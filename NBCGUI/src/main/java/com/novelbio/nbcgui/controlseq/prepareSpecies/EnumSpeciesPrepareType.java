package com.novelbio.nbcgui.controlseq.prepareSpecies;

/** 需要准备的各种 species 类型 */
public enum EnumSpeciesPrepareType {
	/** 如果染色体中的contig数量太多，就可能会影响mapsplice的结果，所以需要进行过滤 */
	filterTooMuchContig,
	
	/** mapsplice要求将染色体切分成每条一个文件 */
	spliceChromosome,
	
	//各种索引 */
	indexBwa, indexBowtie, indexBowtie2, indexRSEM, indexTophatTranscriptom,
	
	/** GO,Pathway的背景基因以及相关注释 */
	BackGroudGenePrepare,
	
	/** COG序列的比对以及相关注释 */
	COGblast,
	
	/** 准备提取miRNA */
	miRNAextract,
	
	rfam
	
}
