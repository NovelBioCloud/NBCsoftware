package com.novelbio.nbcgui.controltest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.novelbio.base.dataOperate.ExcelTxtRead;
import com.novelbio.base.dataOperate.TxtReadandWrite;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.plot.PlotScatter;
import com.novelbio.base.plot.Volcano;
import com.novelbio.generalConf.TitleFormatNBC;


/**
 * 用来筛选差异基因，画火山图的类
 * @author zong0jie
 */
 public class DiffGeneVocalno {
	 
	 /**给出图中的比例*/
		public static final double  PRE = 0.999;
		
	// 满足条件的差异基因数量,默认是1000，在构造方法里面设定，数量的10/1
	public static  int QUANUM = 1000;
	/** 75%的比较结果都要超过这个值 */
	public static final double MIN_PROP_NUM = 0.75;
	
	public static final double THRESHOLD1 = 0.01;
	public static final double THRESHOLD2 = 0.05;
	
	/** 上下调的阈值 */
	public static final double upfc = 1;
	public static final double downfc = -1;
	
	String excelFileName;
	List<Double> lsFDR = new ArrayList<Double>();
	List<Double> lsPvalue = new ArrayList<Double>();
	List<Double> lsLogFC = new ArrayList<Double>();
	/** 具体的值 */
	List<List<String>> lslsInfo;
	/** 具体的值 */
	List<List<String>> lslsDifGene;
	/** 谁跟谁比 */
	public String[] compare;
	
	
	
	/**画图的FDR的所用的纵坐标值，99%, 上面的值确定*/
	double max99FDR;
	/**画图的Pvalue的所用的纵坐标值，99%，上面的值确定*/
	double max99Pvalue;
	/**画图的LogFC的所用的横坐标值，99%，上面的值确定*/
	double max99LogFC;
	
	/** 画图的时候用pvalue还是fdr做阈值 */
	TitleFormatNBC titlePvalueFDR = TitleFormatNBC.FDR;
	/** pvalue或fdr的阈值，是0.05还是0.01 */
	double pvalueFDRthreshold = THRESHOLD2;	
	/**pValue的列号*/
	int pvalueCol;
	/**L的列号*/
	int logfcCol;
	/**FDR的列号*/
	int fdrCol;
	/**
	 * @param excelName 含有结果的excle的名字
	 * @param compare 谁跟谁比，譬如 string[]{treat, control}
	 */
	public DiffGeneVocalno(String excelName, String[] compare) {
		this.excelFileName = excelName;
		
		lslsInfo = ExcelTxtRead.readLsExcelTxtls(excelName, 0);
		QUANUM = lslsInfo.size()/10;
		List<String> lsTitle = lslsInfo.get(0);
		fdrCol = findColNum(lsTitle, TitleFormatNBC.FDR.toString());
		pvalueCol = findColNum(lsTitle, TitleFormatNBC.Pvalue.toString());
		logfcCol = findColNum(lsTitle, TitleFormatNBC.Log2FC.toString());
		
		List<List<String>> lsInfoWithoutTitle = lslsInfo.subList(1, lslsInfo.size());
		lsFDR = readListListCol(lsInfoWithoutTitle, fdrCol, 0,1);
		List<Double> lsFDR2 = readListListColOut_0(lsInfoWithoutTitle, fdrCol);
		Collections.sort(lsFDR2);
		max99FDR = 45;// -Math.log10(lsFDR2.get((int)(lsFDR2.size()*(1- PRE))));
		lsPvalue = readListListCol(lsInfoWithoutTitle, pvalueCol, 0, 1);
		List<Double>  lsPvalueOut_0 = readListListColOut_0(lsInfoWithoutTitle,pvalueCol);
		Collections.sort(lsPvalueOut_0);
		max99Pvalue = 45;// -Math.log10(lsPvalueOut_0.get((int)(lsPvalueOut_0.size()*(1- PRE))));
		lsLogFC = readListListCol(lsInfoWithoutTitle, logfcCol, 10, 0);
		List<Double> lsLogFC2 = new ArrayList<Double>();
		lsLogFC2.addAll(lsLogFC);
		Collections.sort(lsLogFC2);
		max99LogFC = 8;// lsLogFC2.get((int)(lsLogFC2.size()*PRE));
		this.compare = compare;
	}
	
	/**
	 *  根据title名字获取的列号
	 * @param lsTitle
	 * @param colName
	 * @return 返回从0开始的计数
	 */
	private int findColNum(List<String> lsTitle, String colName) {
		int colNum = -1;
		for (int i = 0; i < lsTitle.size(); i++) {
			if (lsTitle.get(i).equalsIgnoreCase(colName)) {
				colNum = i;
			}
		}
		return colNum;
	}

	/**
	 * 读取excel的某一列
	 * @param lsls 注意输入的lsls不包含title
	 * @param colNum 从0开始的计数
	 * @param inf inf的数值
	 * @param naValue 如果是NA，那么用什么值来填充
	 * @return
	 */
	public static List<Double> readListListCol(List<List<String>> lsls, int colNum, double inf, double naValue) {
		if (colNum < 0) {
			return new ArrayList<Double>();
		}
		List<Double> lsCol = new ArrayList<Double>();
		for (List<String> list : lsls) {
			String str = list.get(colNum);
			double value;
			//TODO 尚未考虑NA等情况
			if (str.trim().equalsIgnoreCase("inf")) {
				value = inf;
			} else if (str.trim().equalsIgnoreCase("-inf")) {
				value = -inf;
			} else if (str.trim().equalsIgnoreCase("na")) {
				value = naValue;
			} else {
				try {
					value = Double.parseDouble(str);
				} catch (Exception e) {
					value = naValue;
				}
			}
			
			lsCol.add(value);
		}
		return lsCol;
	}
	/**
	 * 读取excel的某一列,遇到NA不计入
	 * @param lsls 注意输入的lsls不包含title
	 * @param colNum 从0开始的计数
	 * @return
	 */
	public static List<Double> readListListColOut_0(List<List<String>> lsls, int colNum) {
		if (colNum < 0) {
			return new ArrayList<Double>();
		}
		List<Double> lsCol = new ArrayList<Double>();
		for (List<String> list : lsls) {
			String str = list.get(colNum);
			double value;
			//TODO 尚未考虑NA等情况
			try {
				value = Double.parseDouble(str);
			} catch (Exception e) {
				continue;
			}
			lsCol.add(value);
		}
		return lsCol;
	}

	/** 
	 * 设定用pvalue还是fdr卡，以及卡的阈值
	 * @param titlePvalueFdr
	 * @param threshold
	 */
	private void setThreshold(TitleFormatNBC titlePvalueFdr, double threshold) {
		this.titlePvalueFDR = titlePvalueFdr;
		this.pvalueFDRthreshold = threshold;
	}
	
	/** 根据阈值，获得差异基因的个数 */
	public int getDifGeneNum() {
		return getLsDifGene().size() - 1;
	}
	
	/**
	 * 写差异基因进入文本
	 * 具体文本名可以通过 {@link #getDifGeneFileName()} 获得
	 * @param difResultInfo
	 * @param excelFileName
	 * @return
	 */
	public void writeDifGene() {
		TxtReadandWrite txtWriteDifGene = new TxtReadandWrite(getDifGeneFileName(), true);
		txtWriteDifGene.writefilelnls(getLsDifGene());
		txtWriteDifGene.close();
	}
	
	public String getDifGeneFileName() {
		return FileOperate.changeFileSuffix(excelFileName, "-Dif", null);
	}
	
	/**
	 * 根据阈值，获得差异基因的表，可以直接写入excel中
	 * 第一行是title
	 */
	public List<List<String>> getLsDifGene() {
		if (lslsDifGene != null && lslsDifGene.size() > 0) {
			return lslsDifGene;
		}
		
		lslsDifGene = new ArrayList<List<String>>();
		lslsDifGene.add(lslsInfo.get(0));
		for (int i = 0; i < lsPvalue.size(); i++) {
			double pvalue = lsPvalue.get(i);
			double logfc = lsLogFC.get(i);
			if (titlePvalueFDR == TitleFormatNBC.FDR) {
				pvalue = lsFDR.get(i);
			}
			
			if (pvalue <= pvalueFDRthreshold && (logfc >= upfc || logfc <= downfc)) {
				//因为lslsInfo的第一行是title，所以要加上1行以保持一致
				lslsDifGene.add(lslsInfo.get(i+1));
			}
		}
		return lslsDifGene;
	}
	
	/**
	 * 画图，写图片的配置描述文件
	 * 并返回图片路径
	 */
	public void plotVolcanAndWriteParam(int plotwidth, int plotheigth) {
		PlotScatter plotScatter = plotVolcano();
		String imageName = getVolcanoFileName();
		plotScatter.saveToFile(imageName, plotwidth, plotheigth);		
		
		String outImageCon = FileOperate.changeFileSuffix(getVolcanoFileName(), "_pic", "txt");
		String groupName = FileOperate.getFileName(outImageCon).substring(0, 4);
		TxtReadandWrite txtReadandWrite2 = new TxtReadandWrite(outImageCon, true);
		txtReadandWrite2.writefileln("﻿title@@" + groupName + "组实验数据对应的聚类图");
		txtReadandWrite2.writefileln("note@@注：图中的两条线是阈值");
		txtReadandWrite2.close();
	}
	
	/** 画的火山图的路径 */
	public String getVolcanoFileName() {
		return FileOperate.changeFileSuffix(excelFileName, "_volcano", "jpg");
	}
	
	public PlotScatter plotVolcano() {
		double pValueBorder = -Math.log10(pvalueFDRthreshold);
		Volcano volcano = new Volcano();
		volcano.setMaxX(max99LogFC);
		volcano.setMinX(-max99LogFC);
		if (titlePvalueFDR == TitleFormatNBC.Pvalue) {
			volcano.setMaxY(max99Pvalue);
			volcano.setLogFC2Pvalue(lslsInfo, logfcCol, pvalueCol);
		} else if (titlePvalueFDR == TitleFormatNBC.FDR) {
			volcano.setMaxY(max99FDR);
			volcano.setLogFC2Pvalue(lslsInfo, logfcCol, fdrCol);
		}
		
		//LogFC恒定1
		volcano.setLogFCBorder(1);
		volcano.setLogPvalueBorder(pValueBorder);
		PlotScatter plotScatter = volcano.drawVolimage(titlePvalueFDR.toString());
		return plotScatter;
	}
	
	/**
	 * 自动卡一个阈值 如果FDR小于0.01的有1000个，那么阈值就是FDR=0.01
	 * 否则如果FDR小于0.05的有1000个，那么阈值就是FDR=0.05
	 * 否则考虑pValue，如果P-value小于0.01的有大于1000个，那么阈值是pvalue0.01， 在不行阈值选择pvalue=0.05
	 * 
	 * @param lslspValue 输入的pvalue
	 * @param lslsFDR 输入的fdr
	 * @return 返回卡定的阈值 string[2] 0是pvalue还是fdr<br>
	 * 1：具体的阈值
	 */
	public static String[] setThreshold(Collection<DiffGeneVocalno> lslspValue) {
		int thresholdNum = (int)(lslspValue.size() * MIN_PROP_NUM + 1);
		/** 超过1000个基因的样本数量 */
		int pvalueUpThresh1 = 0, pvalueUpThresh2 = 0, fdrUpThresh1 = 0, fdrUpThresh2 = 0;
		for (DiffGeneVocalno difResultInfo : lslspValue) {
			int FDR1Num = 0, FDR2Num = 0, pValue1Num = 0, pValue2Num = 0;
			for (int i = 0; i < difResultInfo.lsFDR.size(); i++) {
				double fdr = difResultInfo.lsFDR.get(i);
				double fc = difResultInfo.lsLogFC.get(i);
				if (fc >= downfc && fc <= upfc ) continue;
				
				if (fdr < THRESHOLD1) FDR1Num++;
				if (fdr < THRESHOLD2) FDR2Num++;
			}
			for (int i = 0; i < difResultInfo.lsPvalue.size(); i++) {
				double pvalue = difResultInfo.lsPvalue.get(i);
				double fc = difResultInfo.lsLogFC.get(i);
				if (fc >= downfc && fc <= upfc ) continue;
				
				if (pvalue < THRESHOLD1) pValue1Num++;
				if (pvalue < THRESHOLD2)pValue2Num++;
			}

			if (FDR1Num >= QUANUM) fdrUpThresh1++;
			if (FDR2Num >= QUANUM) fdrUpThresh2++;
			if (pValue1Num >= QUANUM) pvalueUpThresh1++;
			if (pValue2Num >= QUANUM) pvalueUpThresh2++;
		}
		TitleFormatNBC title = TitleFormatNBC.Pvalue;
		double threshold = THRESHOLD2;
		if (fdrUpThresh1 >= thresholdNum) {
			title = TitleFormatNBC.FDR;
			threshold = THRESHOLD1;
		} else {
			title = TitleFormatNBC.FDR;
			threshold = THRESHOLD2;
		}
		
//		if (fdrUpThresh1 >= thresholdNum) {
//			title = TitleFormatNBC.FDR;
//			threshold = THRESHOLD1;
//		} else if (fdrUpThresh2 >= thresholdNum) {
//			title = TitleFormatNBC.FDR;
//			threshold = THRESHOLD2;
//		} else if (pvalueUpThresh1 >= thresholdNum) {
//			title = TitleFormatNBC.Pvalue;
//			threshold = THRESHOLD1;
//		} else if (pvalueUpThresh2 >= thresholdNum) {
//			title = TitleFormatNBC.Pvalue;
//			threshold = THRESHOLD2;
//		}
		
		for (DiffGeneVocalno difResultInfo : lslspValue) {
			difResultInfo.setThreshold(title, threshold);
		}
		return new String[]{title.toString(), threshold + ""};
	}
}