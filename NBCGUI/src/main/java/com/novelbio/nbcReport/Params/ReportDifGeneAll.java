package com.novelbio.nbcReport.Params;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.novelbio.base.fileOperate.FileOperate;

public class ReportDifGeneAll  extends ReportBase{
	
	String no = "${no}";
	List<ReportDifGene> lsDifGenes = new ArrayList<>();
	
	private void addDifGene(ReportDifGene reportDifGene) {
		lsDifGenes.add(reportDifGene);
	}
 
	@Override
	protected Map<String, Object> addParamMap() {
		HashMap<String, Object> map = new HashMap<>();
		map.put("no", no);
		map.put("lsDifGenes", lsDifGenes);
		if (lsDifGenes.size() > 0) {
			map.put("difGeneType", lsDifGenes.get(0).diffGeneType);
			map.put("log2FC", lsDifGenes.get(0).log2FC);
			map.put("pvalue", lsDifGenes.get(0).pValueOrFDR);
		}
		return map;
	}
	
	@Override
	public EnumReport getEnumReport() { 
		return EnumReport.DiffExp;
	}

	
	
	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public List<ReportDifGene> getLsDifGenes() {
		return lsDifGenes;
	}

	public void setLsDifGenes(List<ReportDifGene> lsDifGenes) {
		this.lsDifGenes = lsDifGenes;
	}

	@Override
	public boolean readReportFromFile(String savePath) {
		List<String> lsReportFiles = FileOperate.getFoldFileNameLs(FileOperate.addSep(savePath)+".report", "report_*", "*");
		for (String reportFile : lsReportFiles) {
			try {
				ReportDifGene reportDifGene = (ReportDifGene)FileOperate.readFileAsObject(reportFile);
				addDifGene(reportDifGene);
			} catch (Exception e) {
				continue;
			}
		}
		return true;
	}

}
