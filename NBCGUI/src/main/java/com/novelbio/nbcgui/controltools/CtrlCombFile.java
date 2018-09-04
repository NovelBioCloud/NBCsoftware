package com.novelbio.nbcgui.controltools;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.novelbio.base.StringOperate;
import com.novelbio.base.dataOperate.TxtReadandWrite;
import com.novelbio.base.dataStructure.PatternOperate;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.bioinfo.tools.compare.CombineTab;

public class CtrlCombFile {
	CombineTab combineTab = new CombineTab();
	String oufFile = "";
	String imgPath = "";
	public void clean() {
		combineTab = new CombineTab();
	}
	public void setOufFile(String oufFile) {
		this.oufFile = oufFile;
	}
	
	public CombineTab getCombineTab() {
		return combineTab;
	}
	
	public String getImgPath() {
		return imgPath;
	}
	
	public Map<String, Integer> getMapSample2GeneNum() {
		return combineTab.getMapSample2GeneNum();
	}
	
	/**
	 * 比较哪几列
	 * @param colNum
	 */
	public void setCompareCol(String colNum) {
		ArrayList<String[]> lsStrColID = PatternOperate.getPatLoc(colNum, "\\d+", false);
		if (lsStrColID.size() == 0) {
			return;
		}
		ArrayList<Integer> lsColID = new ArrayList<Integer>();
		for (String[] strings : lsStrColID) {
			lsColID.add(Integer.parseInt(strings[0]));
		}
		combineTab.setColCompareOverlapID(lsColID);
	}
	
	/**
	 *  获得每个文件名, 对于每个文件，设定它的ID列
	 *  可以连续不断的设定
	 * @param condTxt 文件全名
	 * @param codName 给该文件起个名字，最后在列中显示
	 * @param colStrDetail 选择该文件的哪几列
	 */
	public void setColDetail(String condTxt, String codName, String colStrDetail) {
		if (StringOperate.isRealNull(colStrDetail) || colStrDetail.trim().equals("0")) {
			colStrDetail = "";
		}
		List<Integer> lsCols = CombineTab.getLsIntegers(colStrDetail);		
		combineTab.setColExtractDetail(condTxt, codName, lsCols);
	}
	
	public void output() {
		ArrayList<String[]> lsOut = combineTab.getResultLsUnion();
//		if (lsOut.size() > 60000) {
//			JOptionPane.showMessageDialog(null, "Result num is bigger than 60000, so save to txt file", "alert", JOptionPane.INFORMATION_MESSAGE);
			TxtReadandWrite txtWrite = new TxtReadandWrite(oufFile, true);
			txtWrite.ExcelWrite(lsOut);
			txtWrite.close();
			
			TxtReadandWrite txtWriteOneLine = new TxtReadandWrite(FileOperate.changeFileSuffix(oufFile, "_OneLine", "xls"), true);
			txtWriteOneLine.ExcelWrite(combineTab.getLsResultFromImage());
			txtWriteOneLine.close();
			
			/** 超过5个取交集就画不出图了 */
			if (combineTab.getAllFileNum() <= 5) {
				imgPath = FileOperate.changeFileSuffix(oufFile, null, "png");
				combineTab.renderScriptAndDrawImage(imgPath,"",""); 
            }
			
			return;
//		}
//		ExcelOperate excelOperate = new ExcelOperate();
//		excelOperate.openExcel(FileOperate.changeFileSuffix(oufFile, "", "xls"));
//		excelOperate.WriteExcel(1, 1, lsOut);
	}
}
