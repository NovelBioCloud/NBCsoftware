package com.novelbio.nbcReport.Params;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.novelbio.nbcReport.XdocTmpltExcel;

public class ReportGeneExpressionAll  extends ReportBase{

	String no= "${no}";
	List<ReportGeneExpression> lsExpressions;
	
	@Override
	protected Map<String, Object> addParamMap() {
		HashMap<String, Object> mapName2Obj = new HashMap<>();
		mapName2Obj.put("lsExpressions", lsExpressions);
		mapName2Obj.put("no", no);
		return mapName2Obj;
	}

	
	
	public String getNo() {
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



	@Override
	public EnumReport getEnumReport() {
		return EnumReport.GeneExp;
	}



	@Override
	public boolean readReportFromFile(String savePath) {
		
		return false;
	}


}
