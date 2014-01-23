package com.novelbio.nbcReport.Params;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.novelbio.base.fileOperate.FileOperate;

public class ReportDifGeneAll  extends ReportBase{
	private static final long serialVersionUID = 1L;
	String no = "${no}";
	List<ReportDifGene> lsDifGenes = new ArrayList<>();
	
	private void addDifGene(ReportDifGene reportDifGene) {
		lsDifGenes.add(reportDifGene);
	}
 
	@Override
	public Map<String, Object> buildFinalParamMap() {
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


}
