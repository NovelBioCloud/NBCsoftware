package com.novelbio.nbcgui.controltest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.novelbio.analysis.annotation.functiontest.FunctionTest;
import com.novelbio.database.model.species.Species;

public interface CtrlTestPathInt {
	public void setTaxID(Species taxID);
	/** lsAccID2Value  arraylist-string[] 若为string[2],则第二个为上下调关系，判断上下调
	 * 若为string[1] 则跑全部基因作分析
	 */
	public void setLsAccID2Value(ArrayList<String[]> lsAccID2Value);
	
	public void setUpDown(double up, double down);
	
	public void setBlastInfo(double blastevalue, List<Integer> lsBlastTaxID);
	/**
	 * <b>在这之前要先设定GOlevel</b>
	 * 简单的判断下输入的是geneID还是geneID2Item表
	 * @param fileName
	 */
	public void setLsBG(String fileName);
	
	public void setIsCluster(boolean isCluster);

	public void setSavePathPrefix(String excelPath);
	
	public boolean isCluster();
	
	/**
	 * 清空参数，每次调用之前先清空参数
	 */
	public void clearParam();
	/** 运行 */
	public void run();
	
	/** 返回本次分析的物种ID */
	public int getTaxID();
	/** 返回本次分析blast到的物种list */
	public List<Integer> getBlastTaxID();
	
	public String getResultBaseTitle();
	
	public String getSaveExcelPrefix();
	 
	public void setTeamName(String teamName);
	public List<String> getLsResultExcel();
}
