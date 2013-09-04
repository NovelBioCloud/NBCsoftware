package com.novelbio.project;

import java.util.List;

import com.novelbio.base.dataOperate.ExcelOperate;
import com.novelbio.base.dataOperate.ExcelTxtRead;

public class TestBase {
	public static void main(String[] args) {
		List<String[]> lsls =  ExcelTxtRead.readLsExcelTxt("/home/novelbio/桌面/100S_MappingStatistic.xls", 2, 1);
		System.out.println(lsls.size());
	}
}
