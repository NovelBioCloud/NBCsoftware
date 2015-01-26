package com.novelbio.report.Params;

import com.novelbio.springonly.factory.SpringFactoryService;

/**
 * 不同对任务对应的分析类别（Preliminary，RowData，InDepth）
 */
public enum EnumTaskType {
	// TODO 完善这个枚举
	Other(null),
	GoPathway(EnumTaskClass.Preliminary),
	DNASeqMap(null),
	RNASeqMap(null),
	FastQC(EnumTaskClass.RowData),
	DifGene(EnumTaskClass.Preliminary),
	SamAndRPKM(null),
	SamToBam(null),
	BedFileOperate(null),
	MiRNASeqAnalysis(null),
	MiRNATarget(null),
	SnpAnno(null),
	GeneAnno(null),
	PlotChromosome(null),
	PlotTss(null),
	Report(null),
	Cytoscape(null),
	CuffLinks(null),
	CuffDiff(null),
	CuffCompare(null),
	CoExp(null),
	Degree(null),
	SnpFilterSimple(null),
	CmdResearch(null),
	Intersection(null),
	
	IntersectionTest(null),
	IntersectionFor3(null),
	RfamAnalysis(null),
	GeneAct(null),
	MirPredictSimple(null),
	RNAassembly(null),
	RNAalterSplice(EnumTaskClass.InDepth),
	RNAautoSplice(null),
	PeakAnno(null),
	PeakStatistics(null),
	LncLocation(null),
	Rseqc(null),
	GetSeq(null),
	Blast(null),
	RrnaFilter(null),
	SamToFastQ(null),
	SamToWig(null),
	FastaStatistics(null),
	GATK(null),
	SSR(null),
	Pindel(null),
	RNAdenovoRPKM(null),	
	CDSPredict(null),
	RNASeqCluster(null),
	OMIMDiseaseToGene(null),
	VarScan(null),
	
	CPAT(null),
	/** 只是用来测试的 */
	TestTask(null);
	
	EnumTaskClass taskClass = null;
	EnumTaskType(EnumTaskClass taskClass) {
		this.taskClass = taskClass;
	}
	
	/** 获取task的分析类别 */
	public EnumTaskClass getTaskClass() {
		return taskClass;
	}
	
	/**
	 * 取得对应的说明笔记编号
	 * @return
	 */
	public String getNoteId() {
		//TODO
//		return "";
		return SpringFactoryService.getGlobeParam(this.name() + "_noteId");
	}
//	/**
//	 * 
//	 * 任务类型的对照关系 忽略大小写
//	 * 获得已经设定好taskInfo的TaskThreadAbs
//	 * 
//	 * @param taskInfo 输入从数据库中查找获得的完整信息的taskInfo
//	 * @return
//	 * @throws Exception
//	 */
//	public static TaskThreadAbs getThread(NBCTask taskInfo) {
//		return taskInfo.getTaskType().getTaskThread(taskInfo);
//	}
//	
//	private TaskThreadAbs getTaskThread(NBCTask taskInfo) {
//		TaskThreadAbs taskThread = taskThreadAbs.getClone();
//		taskThread.setTaskInfo(taskInfo);
//		return taskThread;
//	}

}
