package com.novelbio.nbcgui.controltools;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.novelbio.analysis.tools.compare.CombineTab;
import com.novelbio.base.StringOperate;
import com.novelbio.base.dataOperate.TxtReadandWrite;
import com.novelbio.base.dataStructure.PatternOperate;
import com.novelbio.base.fileOperate.FileOperate;

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
		List<Integer> lsCols = getLsIntegers(colStrDetail);		
		combineTab.setColExtractDetail(condTxt, codName, lsCols);
	}
	
	protected static List<Integer> getLsIntegers(String colInfo) {
		if (StringOperate.isRealNull(colInfo)) {
			return Lists.newArrayList(0);
		}
		colInfo = colInfo.replace(",", " ").replace(";", " ");
		
		String[] ss = colInfo.split(" +");
		Set<Integer> setCols = new LinkedHashSet<>();
		for (String colTmp : ss) {
			if (StringOperate.isRealNull(colTmp)) {
				continue;
			}
			colTmp = colTmp.trim();
			if (colTmp.contains("-")) {
				setCols.addAll(getLsSequenceNum(colTmp));
			} else {
				try {
					setCols.add(Integer.parseInt(colTmp));
				} catch (Exception e) {
					throw new RuntimeException("cannot contain " + colTmp);
				}
			}
		}
		return new ArrayList<>(setCols);
	}
	
	private static List<Integer> getLsSequenceNum(String colSeq) {
		String[] ss = colSeq.trim().split("-");
		if (ss.length > 2) {
			throw new RuntimeException("cannot contain " + colSeq);
		}
		int start = 0, end = 0;
		try {
			start = Integer.parseInt(ss[0]);
			end = Integer.parseInt(ss[1]);
		} catch (Exception e) {
			throw new RuntimeException("cannot contain " + colSeq);
		}
		
		List<Integer> lsCols = new ArrayList<>();
		
		if (start <= end) {
			for (int i = start; i <= end; i++) {
				lsCols.add(i);
			}
		} else {
			for (int i = start; i >= end; i--) {
				lsCols.add(i);
			}
		}
		return lsCols;
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
