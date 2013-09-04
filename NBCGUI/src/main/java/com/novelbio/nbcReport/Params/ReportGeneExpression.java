package com.novelbio.nbcReport.Params;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.novelbio.nbcReport.XdocTmpltExcel;

public class ReportGeneExpression {
	
	Set<String> setResultFile;
	String geneExpType;
	List<XdocTmpltExcel> lsExcels;
	
	
	public ReportGeneExpression() {
		// TODO Auto-generated constructor stub
	}
	
	public Set<String> getSetResultFile() {
		Set<String> setFile = new LinkedHashSet<>();
		for (String outFile : setResultFile) {
			String outFile2 = EnumReport.GeneExp.getResultFolder() + outFile.split(EnumReport.GeneExp.getResultFolder())[1];
			setFile.add(outFile2);
		}
		return setFile;
	}
	public void setSetResultFile(Set<String> setResultFile) {
		this.setResultFile = setResultFile;
	}

	public void setGeneExpType(String geneExpType) {
		this.geneExpType = geneExpType;
	}
	public List<String> getLsExcels() {
		List<String> lsList = new ArrayList<>();
		for (XdocTmpltExcel xdocTmpltExcel : lsExcels) {
			lsList.add(xdocTmpltExcel.toString());
		}
		return lsList;
	}
	public void setLsExcels(List<XdocTmpltExcel> lsExcels) {
		this.lsExcels = lsExcels;
	}
	
	
	

}
