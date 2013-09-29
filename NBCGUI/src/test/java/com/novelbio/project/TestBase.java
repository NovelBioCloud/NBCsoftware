package com.novelbio.project;

import java.util.List;

import com.novelbio.base.dataOperate.ExcelOperate;
import com.novelbio.base.dataOperate.ExcelTxtRead;
import com.novelbio.nbcgui.GUI.GUIanalysisForm;

public class TestBase {
	public static void main(String[] args) {
		List<List<String>> lsls = ExcelTxtRead.readLsExcelTxtls("/media/hdfs/nbCloud/staff/gaozhu/我的文档/Difference-Expression_result/AvsB-Dif.xls", 1, 1);
		System.out.println(lsls);
	}
}
