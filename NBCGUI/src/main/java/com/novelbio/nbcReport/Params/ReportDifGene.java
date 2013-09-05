package com.novelbio.nbcReport.Params;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.novelbio.nbcReport.XdocTmpltExcel;

public class ReportDifGene  extends ReportBase{
	
	
	String diffGeneType;
	
	double log2FC;
	
	double pValue;
	
	double FDR;
	
	Set<String> lsResults ;
	
	
	
	List<XdocTmpltExcel> lsTmpltExcels;
	
	
	
	

	public String getDiffGeneType() {
		return diffGeneType;
	}

	public void setDiffGeneType(String diffGeneType) {
		this.diffGeneType = diffGeneType;
	}

	public double getLog2FC() {
		return log2FC;
	}

	public void setLog2FC(double log2fc) {
		log2FC = log2fc;
	}

	public double getpValue() {
		return pValue;
	}

	public void setpValue(double pValue) {
		this.pValue = pValue;
	}

	public Set<String> getLsResults() {
		Set<String> lsSet = new LinkedHashSet<>();
		for (String string : lsResults) {
			lsSet.add(EnumReport.DiffExp + string.split(EnumReport.DiffExp.getResultFolder())[1]);
		}
		return lsSet;
	}

	public void setLsResults(Set<String> lsResults) {
		this.lsResults = lsResults;
	}
	
	

	public double getFDR() {
		return FDR;
	}

	public void setFDR(double fDR) {
		FDR = fDR;
	}

	public List<String> getLsTmpltExcels() {
		List<String> lsList = new ArrayList<>();
		for (XdocTmpltExcel xdocTmpltExcel : lsTmpltExcels) {
			lsList.add(xdocTmpltExcel.toString());
		}
		return lsList;
	}

	public void setLsTmpltExcels(List<XdocTmpltExcel> lsTmpltExcels) {
		this.lsTmpltExcels = lsTmpltExcels;
	}

	@Override
	public EnumReport getEnumReport() {
		return EnumReport.DiffExp;
	}

	@Override
	public boolean readReportFromFile(String savePath) {
		// TODO Auto-generated method stub
		return false;
	}

}
