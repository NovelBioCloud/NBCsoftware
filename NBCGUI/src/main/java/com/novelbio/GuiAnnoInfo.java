package com.novelbio;

import java.util.ArrayList;
import java.util.List;

/**
 * 给GUI界面信息的类
 * @author jie
 *
 */
public class GuiAnnoInfo {
	/** 数量信息 */
	double num;
	/** 文字信息 */
	String info;
	/** 第二文字 */
	String info2;
	/** 列表信息 */
	ArrayList<String[]> lsInfo = new ArrayList<String[]>();
	/** 进度 */
	double numDouble;
	
	List<? extends Number> lsNumInfo;
	
	public GuiAnnoInfo() {};
	
	public void setLsNumInfo(List<? extends Number> lsNumInfo) {
		this.lsNumInfo = lsNumInfo;
	}
	public List<? extends Number> getLsNumInfo() {
		return lsNumInfo;
	}
	public void setNum(double num) {
		this.num = num;
	}
	public void setInfo2(String info2) {
		this.info2 = info2;
	}
	public String getInfo2() {
		return info2;
	}
	public void setDouble(double numDouble) {
		this.numDouble = numDouble;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public void setLsInfo(ArrayList<String[]> lsInfo) {
		this.lsInfo = lsInfo;
	}
	public void addInfo(String[] info) {
		lsInfo.add(info);
	}
	/** 数量信息 */
	public double getNumDouble() {
		return numDouble;
	}
	/** 数量信息 */
	public int getNumInt() {
		return (int) num;
	}
	/** 文字信息 */
	public String getInfo() {
		return info;
	}
	/** 列表信息 */
	public ArrayList<String[]> getLsInfo() {
		return lsInfo;
	}
	public double getDouble() {
		return numDouble;
	}
}
