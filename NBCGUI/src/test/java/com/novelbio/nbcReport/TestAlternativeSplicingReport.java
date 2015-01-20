package com.novelbio.nbcReport;

import java.util.ArrayList;
import java.util.List;

import com.novelbio.report.generateReport.AlternativeSplicingReport;

import junit.framework.TestCase;

public class TestAlternativeSplicingReport extends TestCase {
	
	public void testAlternativeSplicingReport() {
		AlternativeSplicingReport alternativeSplicingReport = new AlternativeSplicingReport();
		List<String> lsPrefix = new ArrayList<String>();
		lsPrefix.add("DKDIRvsWT");
		lsPrefix.add("WTIRvsDKD");
		lsPrefix.add("DKDIRvsDKD");
		lsPrefix.add("DKDIRvsWTIR");
		lsPrefix.add("DKDvsWT");
		lsPrefix.add("WTIRvsWT");
		alternativeSplicingReport.generateReport("/media/nbfs/nbCloud/public/AllProject/project_543cf162e4b0ece9e25da9db/task_54446b14e4b0ece9e25dec1c/RNAAlternativeSplicing_result", lsPrefix);
	}

}
