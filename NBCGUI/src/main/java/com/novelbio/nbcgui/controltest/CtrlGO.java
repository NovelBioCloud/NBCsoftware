package com.novelbio.nbcgui.controltest;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.novelbio.analysis.annotation.functiontest.ElimGOFunTest;
import com.novelbio.analysis.annotation.functiontest.FunctionTest;
import com.novelbio.analysis.annotation.functiontest.NovelGOFunTest;
import com.novelbio.analysis.annotation.functiontest.TopGO.GoAlgorithm;
import com.novelbio.base.PathDetail;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.database.domain.geneanno.GOtype;

public class CtrlGO extends CtrlGOPath {
	private static final Logger logger = Logger.getLogger(CtrlGO.class);
	
	GOtype GOClass = GOtype.BP;
	GoAlgorithm goAlgorithm = GoAlgorithm.novelgo;
	int goLevel = -1;
	
	public GOtype getGOClass() {
		return GOClass;
	}
	
	/**
	 * 必须第一时间设定，这个就会初始化检验模块
	 * 如果重新设定了该算法，则所有参数都会清空
	 * @param goAlgorithm
	 */
	public void setGoAlgorithm(GoAlgorithm goAlgorithm) {
		if (goAlgorithm != GoAlgorithm.novelgo) {
			functionTest = FunctionTest.getInstance(FunctionTest.FUNCTION_GO_ELIM);
			((ElimGOFunTest) functionTest).setAlgorithm(goAlgorithm);
		} else {
			functionTest = FunctionTest.getInstance(FunctionTest.FUNCTION_GO_NOVELBIO);
		}
	}
	public GoAlgorithm getGoAlgorithm() {
		return goAlgorithm;
	}
	/** GO的层级分析，只有当算法为NovelGO时才能使用 */
	public void setGOlevel(int levelNum) {
		if (functionTest instanceof NovelGOFunTest) {
			goLevel = levelNum;
			((NovelGOFunTest) functionTest).setGOlevel(levelNum);
		}
	}
	
	public void setGOType(GOtype goType) {
		this.GOClass = goType;
		functionTest.setDetailType(goType);
	}

	@Override
	protected void copeFile(String prix, String excelPath) {
		if (goAlgorithm != GoAlgorithm.novelgo) {
			String goMapFileSource = FileOperate.changeFileSuffix(PathDetail.getRworkspace() + "topGO/tGOall_elim_10_def.pdf", "_"+prix, null);
			String goMapFileTargetName = FileOperate.getFileNameSep(excelPath)[0] + prix + "GoMap.pdf";
			FileOperate.moveFile(goMapFileSource, FileOperate.getParentPathName(excelPath), goMapFileTargetName, true);
		}
	}
	
	@Override
	String getGene2ItemFileName(String fileName) {
		String suffix = "_GO_Item";
		List<Integer> blastTaxID = functionTest.getBlastTaxID();
		if (functionTest.isBlast()) {
			suffix = suffix + "_blast";
			Collections.sort(blastTaxID);//排个序
			for (int i : blastTaxID) {
				suffix = suffix + "_" + i;
			}
		}
		suffix = suffix + "_" + GOClass.getOneWord();
		if (goLevel > 0) {
			suffix = suffix + "_" + goLevel + "Level";
		}
		return FileOperate.changeFileSuffix(fileName, suffix, "txt");
	}

	@Override
	protected void clear() {
		GOClass = GOtype.BP;
		goAlgorithm = GoAlgorithm.classic;
		goLevel = -1;
		functionTest = null;
	}
	
	/** 返回文件的名字，用于excel和画图 */
	public String getResultBaseTitle() {
		return "GO-Analysis_"+getGOClass().getTwoWord();
	}
}
