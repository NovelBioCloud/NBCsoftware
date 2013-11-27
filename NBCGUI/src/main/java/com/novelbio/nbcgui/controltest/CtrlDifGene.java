package com.novelbio.nbcgui.controltest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.novelbio.analysis.diffexpress.DiffExpAbs;
import com.novelbio.analysis.diffexpress.EnumDifGene;
import com.novelbio.base.FoldeCreate;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.generalConf.TitleFormatNBC;
import com.novelbio.nbcReport.EnumTableType;
import com.novelbio.nbcReport.XdocTmpltExcel;
import com.novelbio.nbcReport.Params.EnumReport;
import com.novelbio.nbcReport.Params.ReportDifGene;

public class CtrlDifGene {
	DiffExpAbs diffExpAbs;
	
	
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
		fileName = FoldeCreate.createAndInFold(fileName, EnumReport.DiffExp.getResultFolder());
		diffExpAbs.addFileName2Compare(fileName, comparePair);
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

	public void calculateResult() {
		diffExpAbs.calculateResult();
		getDiffReport();
	}
	
	public void setIsLog2Value(boolean isLog2Value) {
		diffExpAbs.setLog2Value(isLog2Value);
	}
	
	public void setSensitive(boolean isSensitive) {
		diffExpAbs.setSensitive(isSensitive);
	}
	
	public void clean() {
		diffExpAbs.clean();
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
		String outFolder = FileOperate.getParentPathName(lsResult.get(0));
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
}
