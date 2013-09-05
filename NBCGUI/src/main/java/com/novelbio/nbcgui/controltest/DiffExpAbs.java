package com.novelbio.nbcgui.controltest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.novelbio.analysis.diffexpress.DiffExpInt;
import com.novelbio.analysis.diffexpress.DiffGeneVocalno;
import com.novelbio.base.PathDetail;
import com.novelbio.base.cmd.CmdOperate;
import com.novelbio.base.dataOperate.TxtReadandWrite;
import com.novelbio.base.dataStructure.ArrayOperate;
import com.novelbio.base.dataStructure.MathComput;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.database.service.SpringFactory;

import freemarker.template.Configuration;

public abstract class DiffExpAbs implements DiffExpInt {
	public static final int LIMMA = 10;
	public static final int DESEQ = 20;
	public static final int DEGSEQ = 30;
	public static final int EDEGR = 40;
	public static final int TTest = 50;
	public static final int EBSeq = 60;

	// 满足条件的差异基因的最少数量
//	public static final int QUANUM = 1000;
	public static final double THRESHOLD1 = 0.01;
	public static final double THRESHOLD2 = 0.05;
	//火山图的长宽
	public static final int PLOT_WIDTH = 1024;
	public static final int PLOT_HEIGTH = 1024;
	
	//TODO这个值要改，需要在数据库里获取，前台表单填写到数据库中
	public static final String sequencingType = "表达谱芯片";
	
	Configuration freeMarkerConfiguration = (Configuration)SpringFactory.getFactory().getBean("freemarkNBC");
	String workSpace;
	String fileNameRawdata = "";
	String outScript = "";
	
	ArrayList<String[]> lsGeneInfo = new ArrayList<String[]>();
	/**
	 * 一系列的表示基因分组的列，输入的时候就按照col进行了排序<br>
	 * 0: colNum, 实际number<br>
	 * 1: SampleGroupName
	 */
	ArrayList<String[]> lsSampleColumn2GroupName;
	/** 基因名
	 * 对应样本名
	 * 对应平均表达值
	 */
	HashMap<String, HashMap<String, Double>> mapGeneID_2_Sample2MeanValue;
	/**基因唯一ID，必须没有重复 */
	int colAccID = 0;
	/**
	 * 比较组与相应的输出文件名，可以输入一系列组
	 * map: condition to compare group <br>
	 * FileName <br>
	 * To<br>
	 * 0：treatment<br>
	 * 1：control
	 */
	HashMap<String, String[]> mapOutFileName2Compare = new LinkedHashMap<String, String[]>();
	
	boolean calculate = false;

	public DiffExpAbs() {
		setRworkspace();
		setOutScriptPath();
		setFileNameRawdata();
	}
	
	/**
	 * 一系列的表示基因分组的列<br>
	 * 0: colNum, 实际number<br>
	 * 1: SampleGroupName
	 */
	public void setCol2Sample(ArrayList<String[]> lsSampleColumn2GroupName) {
		//按列进行排序
		Collections.sort(lsSampleColumn2GroupName, new Comparator<String[]>() {
			public int compare(String[] o1, String[] o2) {
				Integer col1 = Integer.parseInt(o1[0]);
				Integer col2 = Integer.parseInt(o2[0]);
				return col1.compareTo(col2);
			}
		});
		this.lsSampleColumn2GroupName = lsSampleColumn2GroupName;
		calculate = false;
	}
	/**
	 * 设定输出文件夹和比较组
	 * @param fileName
	 * @param comparePair <br>
	 * 0: treatment<br>
	 * 1: control
	 */
	public void addFileName2Compare(String fileName, String[] comparePair) {
		mapOutFileName2Compare.put(fileName, comparePair);
		calculate = false;
	}
	public void setGeneInfo(ArrayList<String[]> lsGeneInfo) {
		this.lsGeneInfo = lsGeneInfo;
		calculate = false;
	}
	/** 基因标记列，实际列 */
	public void setColID(int colID) {
		this.colAccID = colID - 1;
		calculate = false;
	}
	protected abstract void setOutScriptPath();
	/** 设定原始数据的文件名 */
	protected abstract void setFileNameRawdata();
	
	protected void setRworkspace() {
		workSpace = PathDetail.getRworkspaceTmp();
	}
	/** 仅供测试 */
	public String getOutScript() {
		generateScript();
		return outScript;
	}
	/** 仅供测试 */
	public String getFileNameRawdata() {
		return fileNameRawdata;
	}
	
	/** 返回是否为log过的值
	 * 主要用于limma，其实就是判断最大的表达值是否大于40
	 *  */
	public boolean isLogValue() {
		ArrayList<Double> lsValue = new ArrayList<Double>();
		for (String[] strings : lsGeneInfo) {
			int colNum = Integer.parseInt(lsSampleColumn2GroupName.get(0)[0]) - 1;
			try {
				double tmpValue = Double.parseDouble(strings[colNum]);
				lsValue.add(tmpValue);
			} catch (Exception e) { }
		}
		double result = MathComput.median(lsValue, 98);
		if (result < 40) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * <b>调用{@link #calculateResult()} 后才能调用</b><br>
	 * 返回文件名，以及对应的比较<br>
	 * key：文件全名<br>
	 * value：对应的比较。譬如 String[]{Treat, Control}
	 * @return
	 */
	public Map<String, String[]> getMapOutFileName2Compare() {
		return mapOutFileName2Compare;
	}
	
	/**
	 * 调用{@link #calculateResult()} 后才能调用
	 */
	public ArrayList<String> getResultFileName() {
		return ArrayOperate.getArrayListKey(mapOutFileName2Compare);
	}
	/** 计算差异 */
	public void calculateResult() {
		if (calculate) {
			return;
		}
		calculate = true;
		//清空文件
		for (String fileName : mapOutFileName2Compare.keySet()) {
			FileOperate.DeleteFileFolder(fileName);
		}
		writeToGeneFile();
		setMapSample_2_time2value();
		generateScript();
		run();
		modifyResult();
//		clean();
		plotDifParams();
	}
	/**
	 * 将输入的文件重整理成所需要的txt格式写入文本
	 */
	protected void writeToGeneFile() {
		TxtReadandWrite txtWrite = new TxtReadandWrite(fileNameRawdata, true);
		ArrayList<String[]> lsAnalysisGeneInfo = getAnalysisGeneInfo();
		String[] title = lsAnalysisGeneInfo.get(0);
		lsAnalysisGeneInfo = removeDuplicate(lsAnalysisGeneInfo.subList(1, lsAnalysisGeneInfo.size()));
		lsAnalysisGeneInfo.add(0, title);
		txtWrite.ExcelWrite(getAnalysisGeneInfo());
		txtWrite.close();
	}
	/**
	 * 获得选定的基因ID和具体值
	 * 排序方式按照输入的lsSampleColumn2GroupName进行排序，不做调整
	 * @return
	 * 0： geneID
	 * 1-n：value
	 */
	protected  ArrayList<String[]> getAnalysisGeneInfo() {
		ArrayList<String[]> lsResultGeneInfo = new ArrayList<String[]>();
		for (int m = 0; m < lsGeneInfo.size(); m++) {
			String[] strings = lsGeneInfo.get(m);
			
			String[] tmpResult = new String[lsSampleColumn2GroupName.size() + 1];
			tmpResult[0] = strings[colAccID];
			for (int i = 0; i < lsSampleColumn2GroupName.size(); i++) {
				int colNum = Integer.parseInt(lsSampleColumn2GroupName.get(i)[0]) - 1;
				//title
				if (m == 0) {
					tmpResult[i + 1] = strings[colNum];
					continue;
				}
				try {
					tmpResult[i + 1] = Double.parseDouble(strings[colNum].trim()) + "";
				} catch (Exception e) {
					tmpResult[i + 1] = 0 + "";
				}
			}
			lsResultGeneInfo.add(tmpResult);
		}
		return lsResultGeneInfo;
	}
	
	protected ArrayList<String[]> removeDuplicate(List<String[]> lsGeneInfo) {
		ArrayList<Integer> lsColID = new ArrayList<Integer>();
		for (int i = 2; i <= lsGeneInfo.size(); i++) {
			lsColID.add(i);
		}
		return MathComput.getMedian(lsGeneInfo, 1, lsColID);
	}
	
	private void setMapSample_2_time2value() {
		mapGeneID_2_Sample2MeanValue = new HashMap<String, HashMap<String,Double>>();
		for (String[] geneID2Info : lsGeneInfo) {
			String geneName = geneID2Info[colAccID];
			try {
				HashMap<String, Double> mapTime2value = mapTime2AvgValue(geneID2Info);
				mapGeneID_2_Sample2MeanValue.put(geneName, mapTime2value);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	/**
	 * 获得 
	 * 样本时期--该时期内平均值
	 * 的map
	 * @param info
	 * @return
	 */
	protected HashMap<String, Double> mapTime2AvgValue(String[] info) {
		HashMap<String, ArrayList<Double>> mapTime2LsValue = new HashMap<String, ArrayList<Double>>();
		
		for (int i = 0; i < lsSampleColumn2GroupName.size(); i++) {
			int colNum = Integer.parseInt(lsSampleColumn2GroupName.get(i)[0]) - 1;
			double value = Double.parseDouble(info[colNum]);
			String timeInfo = lsSampleColumn2GroupName.get(i)[1];//时期
			ArrayList<Double> lsValue = add_and_get_LsValue(timeInfo, mapTime2LsValue);
			lsValue.add(value);
		}
		HashMap<String, Double> mapTime2AvgValue = new HashMap<String, Double>();
		for (Entry<String, ArrayList<Double>> entry : mapTime2LsValue.entrySet()) {
			Double avgValue = MathComput.mean(entry.getValue());
			mapTime2AvgValue.put(entry.getKey(), avgValue);
		}
		return mapTime2AvgValue;
	}
	/** 
	 * 设定样本时期到具体值的信息。
	 * 没有该timeInfo就产生个新的list，有的话就获得原来的list 
	 */
	protected ArrayList<Double> add_and_get_LsValue(String timeInfo, HashMap<String, ArrayList<Double>> mapTime2value) {
		ArrayList<Double> lsValue = mapTime2value.get(timeInfo);
		if (lsValue == null) {
			lsValue = new ArrayList<Double>();
			mapTime2value.put(timeInfo, lsValue);
		}
		return lsValue;
	}
	
	protected abstract void generateScript();
	
	protected String getWorkSpace() {
		return workSpace.replace("\\", "/");
	}
	
	protected String getFileName() {
		return fileNameRawdata.replace("\\", "/");
	}
	/**
	 * 调用Rrunning并写入Cmd的名字,
	 * 例如：
	 * Rrunning("DEseq")
	 */
	protected abstract void run();
	
	protected void Rrunning(String cmdName) {
		String cmd = PathDetail.getRscript() + outScript.replace("\\", "/");
		CmdOperate cmdOperate = new CmdOperate(cmd);
		cmdOperate.run();
		try { Thread.sleep(2000); } catch (Exception e) {}
	}
	
	/** 仅供AOP拦截使用，外界不要调用
	 * 拦截在其完成之后
	 */
	public void modifyResult() {
		for (Entry<String, String[]> entry : mapOutFileName2Compare.entrySet()) {
			String fileName = entry.getKey();
			String[] groupPaire = entry.getValue();
			modifySingleResultFile(fileName, groupPaire[0], groupPaire[1]);
		}
	}
	protected abstract void modifySingleResultFile(String outFileName, String treatName, String controlName);
	
	/** 删除中间文件 */
	public void clean() {
		FileOperate.DeleteFileFolder(outScript);
		FileOperate.DeleteFileFolder(fileNameRawdata);
	}
	
	public void plotDifParams() {
		Map<String, String[]> mapExcelName2Compare = getMapOutFileName2Compare();
		Map<String, DiffGeneVocalno> mapExcelName2DifResultInfo= new LinkedHashMap<String, DiffGeneVocalno>();
		
		String outFolder = FileOperate.addSep(FileOperate.getParentPathName(getResultFileName().get(0)));
		
		int i = 0;
		//excel和火山图的总体描述，写在最前面。
		for (String excelName : mapExcelName2Compare.keySet()) {
			i++;
			if (i == 1) {
				//写表格描述
				String outEXCLECon = FileOperate.changeFileSuffix(excelName, "_xls", "txt");
				TxtReadandWrite txtReadandWrite = new TxtReadandWrite(outEXCLECon, true);
				txtReadandWrite.writefileln("﻿upCompare@@下表是差异基因筛选结果的截图展示，附带差异基因筛选表格说明，下图是差异基因火山图的展示。");
				txtReadandWrite.close();
			}
			mapExcelName2DifResultInfo.put(excelName, new DiffGeneVocalno(excelName, mapExcelName2Compare.get(excelName)));
		}
		
		String[] threshold = DiffGeneVocalno.setThreshold(mapExcelName2DifResultInfo.values());
		
		//画图，出差异基因的表格
		for (String excelFileName : mapExcelName2DifResultInfo.keySet()) {
			DiffGeneVocalno difResultInfo = mapExcelName2DifResultInfo.get(excelFileName);
			difResultInfo.writeDifGene();
			difResultInfo.plotVolcanAndWriteParam(PLOT_WIDTH, PLOT_HEIGTH);
		}
		
		//写params.txt文件
		writeParam(outFolder, threshold, mapExcelName2DifResultInfo);
	}
	
	private void writeParam(String outFolder, String[] threshold, Map<String, DiffGeneVocalno> mapExcelName2DifResultInfo) {
		//写params.txt文件
		String paramsFile = outFolder + "params.txt";
		TxtReadandWrite txtReadandWrite = new TxtReadandWrite(paramsFile, true);
		txtReadandWrite.writefileln("transcript@@" + sequencingType);
		txtReadandWrite.writefileln("finderCondition@@" +  threshold[0] + "=" + threshold[1]);
		for (DiffGeneVocalno difResultInfo : mapExcelName2DifResultInfo.values()) {
			String tmplsExcels = "lsExcels@@EXCEL::";
			String fileName = FileOperate.getFileName(difResultInfo.getDifGeneFileName());
			if (tmplsExcels.equals("lsExcels@@EXCEL::")) {
				tmplsExcels = tmplsExcels  + fileName;
			}else {
				tmplsExcels = tmplsExcels + ";" + fileName ;
			}
			txtReadandWrite.writefileln(tmplsExcels);
		}
		
		for (DiffGeneVocalno difResultInfo : mapExcelName2DifResultInfo.values()) {
			String tmplsPicture = "lsPictures@@PICTURE::";
			String pictureName = FileOperate.getFileName(difResultInfo.getVolcanoFileName());
			if (tmplsPicture.equals("lsPictures@@PICTURE::")) {
				tmplsPicture = tmplsPicture + pictureName;
			}else {
				tmplsPicture = tmplsPicture + ";" +  pictureName ;
			}
			txtReadandWrite.writefileln(tmplsPicture);
		}
		String resultStr = "result@@";
		for (String filename : mapExcelName2DifResultInfo.keySet()) {
			DiffGeneVocalno difResultInfo = mapExcelName2DifResultInfo.get(filename);
			String groupName = difResultInfo.compare[0] + "vs" + difResultInfo.compare[1];
			String tmp = groupName + "组共得到" + difResultInfo.getDifGeneNum() + "个差异基因用于后续分析";
			if (resultStr.equals("result@@")) {
				resultStr = resultStr + tmp;
			}else {
				resultStr = resultStr + ";" + tmp ;
			}
			
		}
		txtReadandWrite.writefileln(resultStr);
		txtReadandWrite.close();
	}

	/**
	 * 返回method的文字与其ID对照表
	 * ID就是本类的常量
	 */
	public static HashMap<String, Integer> getMapMethod2ID() {
		HashMap<String, Integer> mapMethod2ID = new LinkedHashMap<String, Integer>();
		mapMethod2ID.put("Limma--Microarray", LIMMA);
		mapMethod2ID.put("DEGseq--RPKM/Counts(recommand)", DEGSEQ);
		mapMethod2ID.put("EdegR--Counts(Needs Replication)", EDEGR);
		mapMethod2ID.put("DESeq--Counts(Needs Replication)", DESEQ);
		mapMethod2ID.put("EBSeq--Counts", EBSeq);
		mapMethod2ID.put("Ttest", TTest);
		return mapMethod2ID;
	}
	
	public static DiffExpInt createDiffExp(int DiffExpID) {
		DiffExpInt diffExpInt = null;
		if (DiffExpID == LIMMA) {
			diffExpInt = (DiffExpInt)SpringFactory.getFactory().getBean("diffExpLimma");
		} else if (DiffExpID == DESEQ) {
			diffExpInt = (DiffExpInt)SpringFactory.getFactory().getBean("diffExpDESeq");
		} else if (DiffExpID == DEGSEQ) {
			diffExpInt = (DiffExpInt)SpringFactory.getFactory().getBean("diffExpDEGseq");
		} else if (DiffExpID == TTest) {
			diffExpInt = (DiffExpInt)SpringFactory.getFactory().getBean("diffExpTtest");
		} else if (DiffExpID == EDEGR) {
			diffExpInt = (DiffExpInt)SpringFactory.getFactory().getBean("diffExpEdgeR");
		} else if (DiffExpID == EBSeq) {
			diffExpInt = (DiffExpInt)SpringFactory.getFactory().getBean("diffExpEBSeq");
		}
		return diffExpInt;
	}
}
