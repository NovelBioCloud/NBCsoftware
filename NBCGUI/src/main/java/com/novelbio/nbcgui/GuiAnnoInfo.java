package com.novelbio.nbcgui;

import java.util.ArrayList;

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
	/** 列表信息 */
	ArrayList<String[]> lsInfo = new ArrayList<String[]>();
	/** 进度 */
	double numDouble;
	
	/** 0到100的数字
	 * 设定该区间后，所有的prop都会自动修正成该区间内的prop
	 * 譬如设定20-50，prop为0.2，那么自动修正prop为 20+(50-20) * 0.2
	 */
	int[] step = new int[]{0, 100};
	
	public GuiAnnoInfo() {};
	
	/**
	 * 0到100的数字
	 * 设定该区间后，所有的prop都会自动修正成该区间内的prop
	 * 譬如设定20-50，prop为0.2，那么自动修正prop为 20+(50-20) * 0.2
	 */
	public GuiAnnoInfo(int[] step) {
		this.step = step;
	}
	
	public void setNum(double num) {
		this.num = num;
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
