package com.novelbio.report.generateReport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.novelbio.analysis.diffexpress.DiffExpAbs;
import com.novelbio.nbcgui.controltest.CtrlDifGene;
import com.novelbio.report.ReportTable;
import com.novelbio.report.Params.ReportDifGene;

public class DifGeneReport {
	/** 报告中表格的标题 */
	private static final String TABLETITLE = "Differentially Expressed Unigene Analysis";
	/** 报告中表格的列数 */
	private static final int TABLECOLUMN = 4;
	
	private ReportDifGene reportDifGene = new ReportDifGene();
	
	public DifGeneReport(CtrlDifGene ctrlDifGene) {
		generateReport(ctrlDifGene);
	}
	
	public ReportDifGene getReportDifGene() {
		return reportDifGene;
	}
	
	private void generateReport(CtrlDifGene ctrlDifGene) {
		DiffExpAbs diffExpAbs = ctrlDifGene.getDiffExpAbs();
		Map<String, Integer[]> mapGroup2GeneNum = diffExpAbs.getMapDifGeneGroup2DifGeneNum();
		
		List<String[]> lsGroupAndGeneNum = new ArrayList<String[]>();
		String[] groupAndGeneNum = new String[TABLECOLUMN];
		groupAndGeneNum[0] = "caseVSControl";
		groupAndGeneNum[1] = "difExpNum";
		groupAndGeneNum[2] = "upGeneNum";
		groupAndGeneNum[3] = "downGeneNum";
		lsGroupAndGeneNum.add(groupAndGeneNum);
		
		//标记是第几条记录
		int i = 0;
		for (String group : mapGroup2GeneNum.keySet()) {
			Integer[] lsGeneNum = mapGroup2GeneNum.get(group);
			//把第一条记录显示成一段文字
			if (i++ == 0) {
				reportDifGene.setCaseVSContorl(group);
				reportDifGene.setDifExpNum(lsGeneNum[0]);
				reportDifGene.setUpDownGeneNum(lsGeneNum[1], lsGeneNum[2]);
			}
			//如果mapGroup2GeneNum的个数大于1就向DifGene的报告中添加，在报告中生成一个表格
			if (mapGroup2GeneNum.size() > 1) {
				groupAndGeneNum = new String[TABLECOLUMN];
				groupAndGeneNum[0] = group;
				groupAndGeneNum[1] = lsGeneNum[0] + "";
				groupAndGeneNum[2] = lsGeneNum[1] + "";
				groupAndGeneNum[3] = lsGeneNum[2] + "";
				lsGroupAndGeneNum.add(groupAndGeneNum);
			}
		}
		
		ReportTable reportTable = new ReportTable();
		reportDifGene.addTable(reportTable.getMapKey2Param(TABLETITLE, lsGroupAndGeneNum, TABLECOLUMN));
		
		reportDifGene.setpValueOrFDR(diffExpAbs.getTitleFormatNBC(), diffExpAbs.getpValueOrFDR());
		// TODO 算法
		reportDifGene.setAlgorithm("开发中");
	}

}
