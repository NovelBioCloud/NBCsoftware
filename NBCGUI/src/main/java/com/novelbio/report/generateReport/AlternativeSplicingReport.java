package com.novelbio.report.generateReport;

import java.util.List;

import com.novelbio.analysis.seq.GeneExpTable;
import com.novelbio.analysis.seq.rnaseq.RPKMcomput.EnumExpression;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.nbcgui.controlseq.CtrlSplicing;
import com.novelbio.report.Params.ReportAlternativeSplicing;

public class AlternativeSplicingReport {
	
	private static final String fileNameSufix = "_statistics.txt";
	private ReportAlternativeSplicing reportAlternativeSplicing = new ReportAlternativeSplicing();
	
	public AlternativeSplicingReport(CtrlSplicing ctrlSplicing) {
		GeneExpTable ex = null;
		List<String[]>  ls = ex.getLsAllCountsNum(EnumExpression.Counts);
//		generateReport(ctrlSplicing);
	}
	
	public void generateReport(String prefix) {
		//TODO 读文本
		String fileName = prefix + fileNameSufix;
	}

}
