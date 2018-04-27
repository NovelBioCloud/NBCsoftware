package com.novelbio.nbcgui.controltest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.novelbio.analysis.annotation.functiontest.TopGO.GoAlgorithm;
import com.novelbio.database.domain.species.Species;
import com.novelbio.database.model.geneanno.GOtype;

/** 不是单例 */
public interface CtrlTestGOInt {
	public void setTaxID(int taxId);
	/** lsAccID2Value  arraylist-string[] 若为string[2],则第二个为上下调关系，判断上下调
	 * 若为string[1] 则跑全部基因作分析
	 */
	public void setLsAccID2Value(List<String[]> lsAccID2Value);
	
	public void setUpDown(double up, double down);
	
	public void setBlastInfo(double blastevalue, List<Integer> lsBlastTaxID);
	
	/**
	 * <b>在这之前要先设定GOlevel和GOannotation的文件</b>
	 * 简单的判断下输入的是geneID还是geneID2Item表
	 * @param fileName
	 */
	public void setLsBG(String fileName);
	/**
	 * <b>在这之前要先设定GOlevel和GOannotation的文件</b>
	 * 简单的判断下输入的是geneID还是geneID2Item表
	 * @param fileName
	 */
	public void setLsBG(Species species);
	
	public void setIsCluster(boolean isCluster);
	
	/**
	 * 保存图片并返回保存的前缀和文件名
	 * @return
	 */
	public void setSavePathPrefix(String excelPath);
	
	/**
	 * <b>GO用到</b><br>
	 * 必须第一时间设定，这个就会初始化检验模块
	 * 如果重新设定了该算法，则所有参数都会清空
	 * @param goAlgorithm
	 */
	public void setGoAlgorithm(GoAlgorithm goAlgorithm);
	/**
	 * <b>GO用到</b>
	 */
	public GoAlgorithm getGoAlgorithm();
	/** 
	 *  <b>GO用到</b><br>
	 *  GO的层级分析，只有当算法为NovelGO时才能使用 
	 */
	public void setGOlevel(int levelNum);
	
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
	
	/** 获得保存到文件夹的前缀，譬如保存到/home/zong0jie/stage10，那么前缀就是stage10 */
	public String getSavePrefix();
	
	/**设置对比组名*/
	public void setTeamName(String teamName);
	
	List<String> getLsResultPic();
	
	/** 设定自定义的GO注释文件
	 * @param goAnnoFile GO注释文件，第一列为GeneName，第二列为GOIterm
	 * @param isCombineDB 是否与数据库已有的数据进行合并，false表示仅用输入的文本来做go分析
	 */
	void setGOanno(String goAnnoFile, boolean isCombine);
}
