package com.novelbio.nbcgui.controlseq;

import java.util.ArrayList;

import com.novelbio.analysis.seq.genome.GffChrAbs;
import com.novelbio.analysis.seq.mirna.MirTargetMammal;
import com.novelbio.analysis.seq.rnahybrid.RNAhybrid.RNAhybridClass;
import com.novelbio.analysis.tools.compare.CombineTab;
import com.novelbio.base.dataOperate.TxtReadandWrite;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.database.domain.information.SoftWareInfo;
import com.novelbio.database.domain.information.SoftWareInfo.SoftWare;
import com.novelbio.database.model.species.Species;

/**
 * 同时用miranda和RNAhybrid分析，结果取并集
 * @author zong0jie
 *
 */
public class CtrlMiRNAtargetPredict {
	public static void main(String[] args) {
		CtrlMiRNAtargetPredict ctrlMiRNAtargetPredict = new CtrlMiRNAtargetPredict();
		Species species = new Species();
		species.setTaxID(9606);
		
//		GffChrAbs gffChrAbs = new GffChrAbs(species.getGffFile()[0], species.getGffFile()[1], species.getChrRegxAndPath()[1], null, 0);
		
//		ctrlMiRNAtargetPredict.setGffChrAbs(gffChrAbs);
		ctrlMiRNAtargetPredict.setInputMiRNAseq("/home/zong0jie/Desktop/platformtest/predictTarget/mature_human_Final.fa");
		ctrlMiRNAtargetPredict.setInputUTR3File("/home/zong0jie/Desktop/platformtest/predictTarget/pig3UTR");
		ctrlMiRNAtargetPredict.setMirTargetOverlap("/home/zong0jie/Desktop/platformtest/predictTarget/outResult");
		ctrlMiRNAtargetPredict.setSpeciesType(RNAhybridClass.human);
		ctrlMiRNAtargetPredict.setTargetEnergy(15);
		ctrlMiRNAtargetPredict.setTargetPvalue(0.05);
		ctrlMiRNAtargetPredict.setTargetScore(150);
		ctrlMiRNAtargetPredict.predict();
	}
	MirTargetMammal mirTargetMammal = new MirTargetMammal();
	String txtMirTargetOverlap;
	CombineTab combineTab;
	/** 本方法和setInputUTR3File二选一 */
	public void setGffChrAbs(GffChrAbs gffChrAbs) {
		mirTargetMammal.setGffChrAbs(gffChrAbs);
	}
	/** 设定UTR3的序列，没有的话就从gffChrSeq中提取 */
	public void setInputUTR3File(String inputUTR3seq) {
		if (!FileOperate.isFileExistAndBigThanSize(inputUTR3seq, 10)) {
			return;
		}
		mirTargetMammal.setInputUTR3File(inputUTR3seq);
	}
	public void setInputMiRNAseq(String inputMiRNAseq) {
		mirTargetMammal.setInputMiRNAseq(inputMiRNAseq);
	}
	/**
	 * 最后结果交集文件
	 * @param txtMirTargetOverlap
	 */
	public void setMirTargetOverlap(String txtMirTargetOverlap) {
		this.txtMirTargetOverlap = txtMirTargetOverlap;
		mirTargetMammal.setOutFile(txtMirTargetOverlap);
	}
	/** RNAhybrid的物种类型 */
	public void setSpeciesType(RNAhybridClass rAhybridClass) {
		mirTargetMammal.setSpeciesType(rAhybridClass);
	}
	/** 默认0.01 */
	public void setTargetPvalue(double targetPvalue) {
		mirTargetMammal.setTargetPvalue(targetPvalue);
	}
	/** 默认150 */
	public void setTargetScore(int targetScore) {
		mirTargetMammal.setScore(targetScore);
	}
	/** 默认-15，输入的数会取绝对值再加负号 */
	public void setTargetEnergy(int targetEnergy) {
		mirTargetMammal.setEnergy(targetEnergy);
	}
	
	public void predict() {
		mirTargetMammal.predict();
	}
	
	private ArrayList<String[]> overLap(String txtInputFileMiranda, String txtInputFileMiRNAhybrid) {
		combineTab = new CombineTab();
		combineTab.setStrNull(null);
		combineTab.setColExtractDetail(txtInputFileMiranda, "mirnada", 2,3,4);
		combineTab.setColExtractDetail(txtInputFileMiRNAhybrid, "rnaHybrid", 3,4);
		combineTab.setColCompareOverlapID(1, 2);
		ArrayList<String[]> lsCombine = combineTab.getResultLsIntersection();
		
		return lsCombine;
	}
}
