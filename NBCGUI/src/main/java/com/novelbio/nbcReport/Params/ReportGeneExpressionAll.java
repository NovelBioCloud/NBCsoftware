package com.novelbio.nbcReport.Params;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.novelbio.base.fileOperate.FileOperate;

public class ReportGeneExpressionAll  extends ReportBase{

	String no= "${no}";
	List<ReportGeneExpression> lsExpressions = new ArrayList<>();
	
	@Override
	public Map<String, Object> buildFinalParamMap() {
		HashMap<String, Object> mapName2Obj = new HashMap<>();
		mapName2Obj.put("lsExpressions", lsExpressions);
		mapName2Obj.put("no", no);
		return mapName2Obj;
	}

	
	
	public String getNo() {
		System.out.println(no);
		return no;
	}



	public void setNo(String no) {
		this.no = no;
	}






	public List<ReportGeneExpression> getLsExpressions() {
		return lsExpressions;
	}



	public void setLsExpressions(List<ReportGeneExpression> lsExpressions) {
		this.lsExpressions = lsExpressions;
	}


	private void addGeneExp(ReportGeneExpression reportGeneExpression) {
		lsExpressions.add(reportGeneExpression);
	}
	

	@Override
	public EnumReport getEnumReport() {
		return EnumReport.GeneExp;
	}



}
