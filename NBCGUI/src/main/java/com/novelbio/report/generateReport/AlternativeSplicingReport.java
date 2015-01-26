package com.novelbio.report.generateReport;

import java.util.ArrayList;
import java.util.List;

import com.novelbio.analysis.seq.GeneExpTable;
import com.novelbio.analysis.seq.GeneExpTable.EnumAddAnnoType;
import com.novelbio.analysis.seq.rnaseq.RPKMcomput.EnumExpression;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.report.ReportTable;
import com.novelbio.report.Params.ReportAlternativeSplicing;

public class AlternativeSplicingReport {
	/** 报告中表格的列数 */
	private static final int COLUMNNUM = 4;
	/** 报告中表格的标题 */
	private static final String TABLETITLE = "Alternative Splicing Result ";
	private static final String TABLELABEL = "tablalterSplicing";
	private static final String fileNameSufix = "_statistics.txt";
	private static final String geneAccIDName = "SplicingEvent";
	private ReportAlternativeSplicing reportAlternativeSplicing = new ReportAlternativeSplicing();
	
	public AlternativeSplicingReport() {

	}
	
	public ReportAlternativeSplicing getAlternativeSplicing() {
		return reportAlternativeSplicing;
	}
	
	public void generateReport(String savePath, List<String> lsPrefix) {
		List<String[]> lsSplicingEvent = new ArrayList<String[]>();
		for (int i = 0; i < lsPrefix.size(); i++) {
			List<String[]> lsLsData = getSplicingEvent(savePath, lsPrefix.get(i));
			if (i == 0) {
				for (int j = 0; j < lsLsData.size(); j++) {
					String[] splicingEvent = new String[lsPrefix.size() + 1];
					splicingEvent[0] = lsLsData.get(j)[0];
					splicingEvent[1] = lsLsData.get(j)[1];
					lsSplicingEvent.add(splicingEvent);
				}
			} else {
				for (int j = 0; j < lsLsData.size(); j++) {
					lsSplicingEvent.get(j)[i + 1] = lsLsData.get(j)[1];
				}
			}
		}
		ReportTable reportTable = new ReportTable();
		reportAlternativeSplicing.addTable(reportTable.getMapKey2Param(TABLETITLE, TABLELABEL, lsSplicingEvent, COLUMNNUM));
		
		reportAlternativeSplicing.setGroupName("开发中");
		
	}
	
	public List<String[]> getSplicingEvent(String savePath, String prefix) {
		String filePath = FileOperate.addSep(savePath) + prefix + fileNameSufix;
		GeneExpTable geneExpTable = new GeneExpTable(geneAccIDName);
		geneExpTable.read(filePath, EnumAddAnnoType.notAdd);
		List<String[]> lsLsSplicingEvent = geneExpTable.getLsAllCountsNum(EnumExpression.Counts);
		List<String[]> lsLsData = new ArrayList<String[]>();
		for (int i = 0; i < lsLsSplicingEvent.size(); i++) {
			String[] lsSplicingEvent = lsLsSplicingEvent.get(i);
			String[] lsData = new String[2];
			lsData[0] = lsSplicingEvent[0];
			if (i == 0) {
				lsData[1] = prefix;
			} else {
				lsData[1] = lsSplicingEvent[1];
			}
			lsLsData.add(lsData);
		}
		return lsLsData;
	}

}
