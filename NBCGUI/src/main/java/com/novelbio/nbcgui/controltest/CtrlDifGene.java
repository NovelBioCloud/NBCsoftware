package com.novelbio.nbcgui.controltest;

import java.util.ArrayList;

import com.novelbio.analysis.diffexpress.DiffExpAbs;
import com.novelbio.analysis.diffexpress.DiffExpInt;
import com.novelbio.nbcReport.Params.ReportDifGene;

public class CtrlDifGene {
	DiffExpInt diffExpAbs;
	
	/**
	 * @param diffGeneID {@link DiffExpAbs#DEGSEQ} 等
	 */
	public CtrlDifGene(int diffGeneID) {
		diffExpAbs = DiffExpAbs.createDiffExp(diffGeneID);
	}

	public void setCol2Sample(ArrayList<String[]> lsSampleColumn2GroupName) {
		diffExpAbs.setCol2Sample(lsSampleColumn2GroupName);
	}

	public void addFileName2Compare(String fileName, String[] comparePair) {
		diffExpAbs.addFileName2Compare(fileName, comparePair);
	}

	public void setGeneInfo(ArrayList<String[]> lsGeneInfo) {
		diffExpAbs.setGeneInfo(lsGeneInfo);
	}

	public void setColID(int colID) {
		diffExpAbs.setColID(colID);
	}

	public ArrayList<String> getResultFileName() {
		return diffExpAbs.getResultFileName();
	}

	public void calculateResult() {
		diffExpAbs.calculateResult();
	}

	public void clean() {
		diffExpAbs.clean();
	}
	
	public ReportDifGene getDiffReport() {

		
		
		return null;
	}
	
	
}
