package com.novelbio.nbcReport.Params;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.novelbio.generalConf.TitleFormatNBC;
import com.novelbio.nbcReport.XdocTmpltExcel;

public class ReportDifGene  extends ReportBase{
	
	
	String diffGeneType;
	
	double log2FC;
	
	double pValueOrFDR;
	
	TitleFormatNBC titleFormatNBC;
	
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
	
	


	public double getpValueOrFDR() {
		return pValueOrFDR;
	}

	public void setpValueOrFDR(double pValueOrFDR) {
		this.pValueOrFDR = pValueOrFDR;
	}

	public TitleFormatNBC getTitleFormatNBC() {
		return titleFormatNBC;
	}

	public void setTitleFormatNBC(TitleFormatNBC titleFormatNBC) {
		this.titleFormatNBC = titleFormatNBC;
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



}
