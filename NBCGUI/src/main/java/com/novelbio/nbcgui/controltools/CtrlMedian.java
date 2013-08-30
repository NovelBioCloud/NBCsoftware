package com.novelbio.nbcgui.controltools;

import java.util.ArrayList;

import com.novelbio.base.dataOperate.ExcelOperate;
import com.novelbio.base.dataOperate.ExcelTxtRead;
import com.novelbio.base.dataStructure.MathComput;

public class CtrlMedian {
	/**
	 * 文件名
	 */
	String file = "";
	/**
	 * 按照第几列作为取中位数的ID
	 */
	int accID = 0;
	/**
	 * 从文件中提取的信息
	 */
	ArrayList<String[]> lsFileInfo = new ArrayList<String[]>();
	ArrayList<String[]> lsResult = new ArrayList<String[]>();
	ArrayList<Integer> lsCol = null;
	public void setFile(String file) {
		this.file = file;
	}
	/**
	 * 返回读取的待取中位数的文件
	 * @return
	 */
	public ArrayList<String[]> readFile()
	{
		lsFileInfo = ExcelTxtRead.readLsExcelTxt(file, 1);
		return lsFileInfo;
	}
	/**
	 * 设定唯一ID
	 * @param accID
	 */
	public void setAccID(int accID)
	{
		this.accID = accID;
	}
	/**
	 * 设定要对哪几列取中位数，不取的列都会用第一次出现的信息填充
	 * 譬如我比较accID，取了3-6列的中位数。但是还会出现symbol和description列等，那么就用第一个出现的symbol和description填充
	 */
	public void setMedianID(ArrayList<Integer> lsCol)
	{
		this.lsCol = lsCol;
	}
	/**
	 * 在此之前先要readFile()
	 * @return
	 */
	public ArrayList<String[]> getResult() {
		lsResult = MathComput.getMedian(lsFileInfo, accID, lsCol);
		return lsResult;
	}
	
	public void saveFile(String excelFile)
	{
		ExcelOperate excel = new ExcelOperate();
		excel.openExcel(excelFile);
		excel.WriteExcel(1, 1, lsResult);
		excel.Close();
	}
}
