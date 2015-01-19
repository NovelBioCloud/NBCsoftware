package com.novelbio.nbcReport;

import java.util.List;

import com.novelbio.analysis.seq.GeneExpTable;
import com.novelbio.analysis.seq.GeneExpTable.EnumAddAnnoType;
import com.novelbio.analysis.seq.rnaseq.RPKMcomput.EnumExpression;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.report.generateReport.AlternativeSplicingReport;

import junit.framework.TestCase;

public class TestReadStatistics extends TestCase {
	
	public void testReadStatistics() {
		AlternativeSplicingReport asr = new AlternativeSplicingReport();
//		asr.generateReport("/home/novelbio/jpx/test", "9522LTS8vs9522HTS8");'
//		String filePath = FileOperate.addSep("/home/novelbio/jpx/test") + "9522LTS8vs9522HTS8" + "_statistics.txt";
//		String filePath = FileOperate.addSep("/home/novelbio/jpx/test") + "9522LTS8vs9522HTS8" + "_statistics.txt";
		String filePath = "/home/novelbio/jpx/test/9522LTS9vs9522HTS9_statistics.txt";
		GeneExpTable geneExpTable = new GeneExpTable("SplicingEvent");
		geneExpTable.read(filePath, EnumAddAnnoType.notAdd);
		filePath = "/home/novelbio/jpx/test/9522HTS9vs9522HTS8_statistics.txt";
		geneExpTable.read(filePath, EnumAddAnnoType.notAdd);
		filePath = "/home/novelbio/jpx/test/9522LTS8vs9522HTS8_statistics.txt";
		geneExpTable.read(filePath, EnumAddAnnoType.notAdd);
		
		List<String[]> lsData = geneExpTable.getLsAllCountsNum(EnumExpression.Counts);
	}

}
