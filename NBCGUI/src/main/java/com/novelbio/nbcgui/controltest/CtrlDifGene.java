package com.novelbio.nbcgui.controltest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.novelbio.analysis.IntCmdSoft;
import com.novelbio.analysis.diffexpress.DiffExpAbs;
import com.novelbio.analysis.diffexpress.DiffExpDESeq;
import com.novelbio.analysis.diffexpress.EnumDifGene;
import com.novelbio.base.FoldeCreate;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.generalConf.TitleFormatNBC;
import com.novelbio.nbcReport.EnumTableType;
import com.novelbio.nbcReport.XdocTmpltExcel;
import com.novelbio.nbcReport.Params.EnumReport;
import com.novelbio.nbcReport.Params.ReportDifGene;

public class CtrlDifGene implements IntCmdSoft {
	DiffExpAbs diffExpAbs;
	String outPath;
	
	/**
	 * @param diffGeneID {@link DiffExpAbs#DEGSEQ} 等
	 */
	public CtrlDifGene(EnumDifGene diffGeneID) {
		diffExpAbs = (DiffExpAbs) DiffExpAbs.createDiffExp(diffGeneID);
	}
	
	public void setCol2Sample(List<String[]> lsSampleColumn2GroupName) {
		diffExpAbs.setCol2Sample(lsSampleColumn2GroupName);
	}

	public void addFileName2Compare(String fileName, String[] comparePair) {
		String fileNameFinal = FoldeCreate.createAndInFold(fileName, EnumReport.DiffExp.getResultFolder());
		outPath = FileOperate.getPathName(fileNameFinal);
		diffExpAbs.addFileName2Compare(fileNameFinal, comparePair);
	}

	public void setGeneInfo(ArrayList<String[]> lsGeneInfo) {
		diffExpAbs.setGeneInfo(lsGeneInfo);
	}

	public void setColID(int colID) {
		diffExpAbs.setColID(colID);
	}

	public List<String> getResultFileName() {
		return diffExpAbs.getResultFileName();
	}
	
	/** 计算差异 */
	public void calculateResult() {
		diffExpAbs.calculateResult();
		getDiffReport();
	}
	
	/** 将计算差异分为两个步骤，这是第一步，产生数据和脚本 */
	public void generateGeneAndScript() {
		diffExpAbs.generateGeneAndScript();
	}
	/** 将计算差异分为两个步骤，这是第二步，进行计算 */
	public void runAndModifyResult() {
		diffExpAbs.runAndModifyResult();
		getDiffReport();
	}
	/** 将输入的表达值取log */
	public void setLogTheValue(boolean logTheValue) {
		diffExpAbs.setLogValue(logTheValue);
	}
	
	public void setFitType(boolean fitTypeParametric) {
		if (diffExpAbs instanceof DiffExpDESeq) {
			((DiffExpDESeq)diffExpAbs).setFitType(fitTypeParametric);
		}
	}
	/** 
	 * 设定用pvalue还是fdr卡，以及卡的阈值
	 * @param titlePvalueFdr
	 * @param threshold
	 */
	public void setThreshold(TitleFormatNBC titlePvalueFdr, double threshold) {
		diffExpAbs.setThreshold(titlePvalueFdr, threshold);
	}
	
	/** 默认是正负1，表示卡两倍阈值 */
	public void setLogFCcutoff(double logFCcutoff) {
		diffExpAbs.setLogFCcutoff(logFCcutoff);
	}
	/** 做差异基因的时候各个样本表达量的值之和不能小于等于该数值
	 * 譬如如果多个样本表达量都为0，那就不考虑了
	 * @param addAllLine 默认为0，意思就是多个样本表达量之和为0 就不考虑
	 * 是小于等于的关系
	 */
	public void setMinSampleSumNum(double minSampleSumNum) {
		diffExpAbs.setMinSampleSumNum(minSampleSumNum);
	}
	/** 做差异基因的时候每个样本表达量的值不能都小于该数值
	 * 意思如果为 2, 3, 2, 1.3, 3
	 * 则当值设定为3时，上述基因删除
	 * @param minSampleSepNum 是小于的关系，不包括等于
	 */
	public void setMinSampleSepNum(double minSampleSepNum) {
		diffExpAbs.setMinSampleSepNum(minSampleSepNum);
	}
	public void setSensitive(boolean isSensitive) {
		diffExpAbs.setSensitive(isSensitive);
	}
	
	public void clean() {
		diffExpAbs.clean();
	}
	public void copyTmpFileToResultPath() {
		String outRawData = FileOperate.addSep(outPath) + "Script/";
 		diffExpAbs.copyTmpFileToPath(outRawData);
	}
	public ReportDifGene getDiffReport() {
		double logFC =  diffExpAbs.getLogFC();
		double pValueOrFDR = diffExpAbs.getpValueOrFDR();
		TitleFormatNBC titleFormatNBC = diffExpAbs.getTitleFormatNBC();
		List<String> lsResult =  diffExpAbs.getResultFileName();
		Set<String> lsStrings = new HashSet<>();
		lsStrings.addAll(lsResult);
		ReportDifGene reportDifGene = new ReportDifGene();
		reportDifGene.setLog2FC(logFC);
		reportDifGene.setpValueOrFDR(pValueOrFDR);
		reportDifGene.setTitleFormatNBC(titleFormatNBC);
		reportDifGene.setLsResults(lsStrings);
		List<XdocTmpltExcel> lsTmpltExcels = new ArrayList<>(); 
		
		for (String string : lsStrings) {
			XdocTmpltExcel xdocTmpltExcel = new XdocTmpltExcel(EnumTableType.DifGene.getXdocTable());
			xdocTmpltExcel.setExcelTitle("差异基因表达分析结果的截图展示");
			xdocTmpltExcel.addExcel(string, 1);
			lsTmpltExcels.add(xdocTmpltExcel);
		}
		String outFolder = FileOperate.getParentPathNameWithSep(lsResult.get(0));
		reportDifGene.setLsTmpltExcels(lsTmpltExcels);
		reportDifGene.writeAsFile(outFolder);
		return reportDifGene;
	}
	
	/**
	 * 输入比较的文件名和组后，
	 * 返回预测的文件名
	 * @param copeFastq
	 * @param isFilter 是否过滤，如果不过滤就直接合并
	 * @return
	 */
	public HashMultimap<String, String> getPredictMapPrefix2Result() {
		return diffExpAbs.getPredictMapPrefix2Result();
	}

	@Override
	public List<String> getCmdExeStr() {
		return diffExpAbs.getCmdExeStr();
	}
}
