package com.novelbio.nbcgui.controltools;

import java.util.ArrayList;

import com.novelbio.analysis.tools.compare.CombineTab;
import com.novelbio.base.dataOperate.TxtReadandWrite;
import com.novelbio.base.dataStructure.PatternOperate;
import com.novelbio.base.fileOperate.FileOperate;

public class CtrlCombFile {
	CombineTab combineTab = new CombineTab();
	String oufFile = "";
	public void clean() {
		combineTab = new CombineTab();
	}
	public void setOufFile(String oufFile) {
		this.oufFile = oufFile;
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
	 * @param colDetai 选择该文件的哪几列
	 */
	public void setColDetail(String condTxt, String codName, String colStrDetail) {
		ArrayList<String[]> lsResult = PatternOperate.getPatLoc(colStrDetail, "\\d+", false);
		int[] colDetail = new int[lsResult.size()];
		for (int i = 0; i < colDetail.length; i++) {
			colDetail[i] = Integer.parseInt(lsResult.get(i)[0]);
		}
		combineTab.setColExtractDetail(condTxt, codName, colDetail);
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
			
			combineTab.renderScriptAndDrawImage(FileOperate.changeFileSuffix(oufFile, null, "tiff"),"","");
			
			return;
//		}
//		ExcelOperate excelOperate = new ExcelOperate();
//		excelOperate.openExcel(FileOperate.changeFileSuffix(oufFile, "", "xls"));
//		excelOperate.WriteExcel(1, 1, lsOut);
	}
}
