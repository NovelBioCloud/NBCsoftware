package com.novelbio.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.novelbio.base.fileOperate.FileHadoop;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.nbcReport.EnumTableType;
import com.novelbio.nbcReport.XdocTable;
import com.novelbio.nbcReport.XdocTmpltExcel;
import com.novelbio.nbcReport.Params.ReportDifGene;
import com.novelbio.nbcReport.Params.ReportDifGeneAll;
import com.novelbio.nbcReport.Params.ReportGeneExpressionAll;
import com.novelbio.nbcReport.Params.ReportProject;
import com.novelbio.nbcgui.controltest.DiffExpAbs;

public class TestDifGene {
	
	ReportDifGeneAll reportDifGeneAll;
	
	@Before
	public void init() {
		
		
		
		

	}
	
	@Test
	public void runTest() {
		
		DiffExpAbs diffExpAbs = (DiffExpAbs) DiffExpAbs.createDiffExp(50);

		
	}

}
