package com.novelbio.report.generateReport;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.novelbio.analysis.diffexpress.DiffExpAbs;
import com.novelbio.nbcgui.controltest.CtrlDifGene;
import com.novelbio.report.Params.ReportDifGene;

public class DifGeneReport {
	
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
				Map<String, Object> mapKey2GroupAndGeneNum = new HashMap<String, Object>();
				mapKey2GroupAndGeneNum.put("caseVSControl", group);
				mapKey2GroupAndGeneNum.put("difExpNum", lsGeneNum[0]);
				mapKey2GroupAndGeneNum.put("upGeneNum", lsGeneNum[1]);
				mapKey2GroupAndGeneNum.put("downGeneNum", lsGeneNum[2]);
				reportDifGene.addMapGroupAndGeneNum(mapKey2GroupAndGeneNum);
			}
		}
		reportDifGene.setpValueOrFDR(diffExpAbs.getTitleFormatNBC(), diffExpAbs.getpValueOrFDR());
		// TODO 算法
		reportDifGene.setAlgorithm("开发中");
	}

}
