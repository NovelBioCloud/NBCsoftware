package com.novelbio.analysis.seq;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ArrayListMultimap;
import com.novelbio.analysis.seq.rnaseq.RPKMcomput.EnumExpression;
import com.novelbio.base.dataOperate.TxtReadandWrite;
import com.novelbio.base.dataStructure.MathComput;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.generalConf.TitleFormatNBC;

/**
 * 将一系列基因表达的表整理到一起，方便归类等工作
 * @author zong0jie
 */
public class GeneExpTable {
	String geneTitleName;
	/** 基因annotation的title */
	Set<String> setGeneAnnoTitle = new LinkedHashSet<>();
	/** 基因名和注释的对照表 */
	ArrayListMultimap<String, String> mapGene2Anno = ArrayListMultimap.create();
	/** 时期信息 */
	Set<String> setCondition = new LinkedHashSet<>();
	/** 具体存储表达信息的表 */
	Map<String, Map<String, Double>> mapGene_2_Cond2Exp = new LinkedHashMap<>();
	/** 大致测序量，譬如rna-seq就是百万级别，小RNAseq就是可能就要变成十万级别 */
	int mapreadsNum = 1000000;
	/** 单个基因的平均表达量，用于UQ的时候除以uq的数量 */
	int geneExp = 100;
	/** 基因长度 */
	Map<String, Integer> mapGene2Len;
	/** 某个时期的全体reads信息，用来做标准化 */
	Map<String, Long> mapCond2AllReads = new HashMap<>();
	String currentCondition;
	/**
	 * @param geneAccIDName 表示AccID一列的具体名字，譬如symbol或者miRNAname等
	 */
	public GeneExpTable(TitleFormatNBC geneAccIDName) {
		this.geneTitleName = geneAccIDName.toString();
	}
	public GeneExpTable(String geneAccIDName) {
		this.geneTitleName = geneAccIDName;
	}

	
	/**
	 *  添加counts文本，将其加入mapGene_2_Cond2Exp中
	 * 同时添加注释信息并设定allCountsNumber为全体reads的累加
	 *
	 * @param file 读取的文件
	 * @param addAnno 是否添加注释，如果本对象已经有了注释，就不可以添加了，否则会出错
	 */
	public void read(String file, boolean addAnno) {
		TxtReadandWrite txtRead = new TxtReadandWrite(file);
		List<String> lsFirst3Lines = txtRead.readFirstLines(3);
		Map<Integer, String> mapCol2Sample = getMapCol2Sample(lsFirst3Lines);
		txtRead.close();
		read(file, addAnno, mapCol2Sample);
	}
	
	public void read(String file, boolean addAnno, Map<Integer, String> mapCol2Sample) {
		TxtReadandWrite txtRead = new TxtReadandWrite(file);
		if (mapCol2Sample == null) {
			txtRead.close();
			return;
		}
		for (String string : mapCol2Sample.values()) {
			setCondition.add(string);
		}
		String[] title = txtRead.readFirstLine().split("\t");
		geneTitleName = title[0];
		if (addAnno) {
			setLsAnnoTitle(title, mapCol2Sample.keySet());
		}
	
		for (String content : txtRead.readlines(2)) {
			String[] data = content.split("\t");
			String geneName = data[0];
			Map<String, Double> mapSample2Value = null;
			if (mapGene_2_Cond2Exp.containsKey(geneName)) {
				mapSample2Value = mapGene_2_Cond2Exp.get(geneName);
			} else {
				mapSample2Value = new HashMap<>();
				mapGene_2_Cond2Exp.put(geneName, mapSample2Value);
			}
			for (int i = 1; i < data.length; i++) {
				if (mapCol2Sample.containsKey(i)) {
					String sampleName = mapCol2Sample.get(i);
					Double value = Double.parseDouble(data[i]);
					mapSample2Value.put(sampleName, value);
				} else if (addAnno) {
					mapGene2Anno.put(geneName, data[i]);
				}
			}
		}
		setAllreadsPerConditon();
		txtRead.close();
	}
	/**
	 * @param lsFirst3Lines 前三行
	 * @return 从0开始计算的col --- sampleName
	 */
	private Map<Integer, String> getMapCol2Sample(List<String> lsFirst3Lines) {
		if (lsFirst3Lines.size() < 2) {
			return null;
		}
		Map<Integer, String> mapCol2Sample = new LinkedHashMap<>();
		String[] titleArray = lsFirst3Lines.get(0).split("\t");
		String[] dataArray1 = lsFirst3Lines.get(1).split("\t");
		String[] dataArray2 = null;
		if (lsFirst3Lines.size() > 2) {
			dataArray2 = lsFirst3Lines.get(2).split("\t");
		}
		for (int i = 1; i < dataArray1.length; i++) {
			try {
				Double.parseDouble(dataArray1[i]);
				if (dataArray2 != null) {
					Double.parseDouble(dataArray2[i]);
				}
				mapCol2Sample.put(i, titleArray[i]);
			} catch (Exception e) {
			}
		}
		return mapCol2Sample;
	}
	
	private void setLsAnnoTitle(String[] title, Set<Integer> colCol) {
		if (setGeneAnnoTitle.size() > 0) {
			return;
		}
		for (int i = 1; i < title.length; i++) {
			if (colCol.contains(i)) {
				continue;
			}
			setGeneAnnoTitle.add(title[i]);
		}
	}
	/** 返回一系列基因的名称 */
	public Set<String> getSetGeneName() {
		return mapGene_2_Cond2Exp.keySet();
	}
	/**  最早就要设定 */
	public void addLsGeneName(Collection<String> colGeneName) {
		setLsGeneName(colGeneName);
	}
	/**
	 * 大致测序量，譬如rna-seq就是百万级别，小RNAseq就是可能就要变成十万级别
	 * 就是tpm和rpkm那块，要乘的测序量
	 * @param mapreadsNum 默认为1百万 
	 */
	public void setMapreadsNum(int mapreadsNum) {
		this.mapreadsNum = mapreadsNum;
	}
	/**
	 * uq基因的大致表达量，用于UQ的时候除以uq的数量
	 * 就是uq那块，要乘的测序量
	 * @param geneExp 默认为100
	 */
	public void setGeneExp(int geneExp) {
		this.geneExp = geneExp;
	}
	public void setCurrentCondition(String currentCondition) {
		this.currentCondition = currentCondition;
		setCondition.add(currentCondition);
	}
	public String getCurrentCondition() {
		return currentCondition;
	}
	/** 返回全体condition */
	public Set<String> getSetCondition() {
		return setCondition;
	}
	/** 返回全体condition和对应的allReads */
	public Map<String, Long> getMapCond2AllReads() {
		return mapCond2AllReads;
	}
	/** mapGene2Anno中务必含有全体geneName */
	public void addAnnotation(Map<String, String> mapGene2Anno) {
		for (String geneName : mapGene2Anno.keySet()) {
			String anno = mapGene2Anno.get(geneName);
			if (anno == null) anno = "";
			this.mapGene2Anno.put(geneName, anno);
		}
	}
	/** mapGene2Anno中务必含有全体geneName */
	public void addAnnotationArray(Map<String, String[]> mapGene2Anno) {
		for (String geneName : mapGene2Anno.keySet()) {
			String[] anno = mapGene2Anno.get(geneName);
			for (String string : anno) {
				if (string == null) string = "";
				this.mapGene2Anno.put(geneName, string);
			}
		}
	}
	/** 会自动去重复 */
	public void addLsTitle(Collection<String> colTitle) {
		setGeneAnnoTitle.addAll(colTitle);
	}
	/** 初始化基因列表 */
	private void setLsGeneName(Collection<String> lsGeneName) {
		for (String geneName : lsGeneName) {
			if (mapGene_2_Cond2Exp.containsKey(geneName)) continue;
			
			Map<String, Double> mapCond2Exp = new HashMap<>();
			mapGene_2_Cond2Exp.put(geneName, mapCond2Exp);
		}
	}
	
	/** 设置当前时期所有mapping上的reads */
	public void addAllReads(double allReads) {
		if (mapCond2AllReads.containsKey(currentCondition)) {
			allReads += mapCond2AllReads.get(currentCondition);
		}
		mapCond2AllReads.put(currentCondition, (long)allReads);
	}
	/** 设置当前时期所有mapping上的reads */
	public void setAllReads(double allReads) {
		mapCond2AllReads.put(currentCondition, (long)allReads);
	}
	/** 设置基因长度信息 */
	public void setMapGene2Len(Map<String, Integer> mapGene2Len) {
		this.mapGene2Len = mapGene2Len;
	}
	
	public void addMapGene2Len(Map<String, Integer> mapGene2Len) {
		if (this.mapGene2Len == null) {
			this.mapGene2Len = mapGene2Len;
		} else {
			this.mapGene2Len.putAll(mapGene2Len);
		}
	}
	/**
	 * 累加某个时期的全体表达值
	 * 在添加表达信息之前，先添加 {@link #addLsGeneName(Map)}*/
	public void addGeneExp(Map<String, ? extends Number> mapGene2Exp) {
		for (String geneName : mapGene2Exp.keySet()) {
			addGeneExp(geneName, mapGene2Exp.get(geneName).doubleValue());
		}
	}
	/** 在添加表达信息之前，先添加 {@link #addLsGeneName(Map)}
	 * @param condition
	 * @param geneName
	 * @param value 本基因需要加上的值
	 */
	public void addGeneExp(String geneName, double value) {
		Map<String, Double> mapCond2Exp = mapGene_2_Cond2Exp.get(geneName);
		if (mapCond2Exp.containsKey(currentCondition)) {
			double lastValue = mapCond2Exp.get(currentCondition);
			value += lastValue;
		}
		mapCond2Exp.put(currentCondition, value);
	}
	
	/**
	 * 获得当前时期的表达情况
	 * @param enumExpression
	 *  @return 返回按照 lsConditions顺序的基因表达list
	 */
	public List<String[]> getLsCountsNum(EnumExpression enumExpression) {
		setConditionAllreads(currentCondition);

		List<String[]> lsResult = new ArrayList<>();
		lsResult.add(getCurrentTitle());
		double uq = 0;
		if (enumExpression == EnumExpression.UQPM || enumExpression == EnumExpression.UQRPKM) {
			uq = getUQ(currentCondition);
		}
		for (String geneName : getSetGeneName()) {
			List<String> lsTmpResult = new ArrayList<String>();
			lsTmpResult.add(geneName);
			if (!mapGene2Anno.isEmpty()) {
				lsTmpResult.addAll(mapGene2Anno.get(geneName));
			}
			
			lsTmpResult.add(getValueCondition(geneName, enumExpression, uq));
			lsResult.add(lsTmpResult.toArray(new String[0]));
		}
		return lsResult;
	}
	public long getCurrentAllReads() {
		if (currentCondition == null || !mapCond2AllReads.containsKey(currentCondition)) {
			return 0;
		}
		return mapCond2AllReads.get(currentCondition);
	}
	/**
	 * 获得全体时期的表达情况
	 * @param enumExpression
	 *  @return 返回按照 lsConditions顺序的基因表达list
	 */
	public List<String[]> getLsAllCountsNum(EnumExpression enumExpression) {
		setAllreadsPerConditon();

		List<String[]> lsResult = new ArrayList<>();
		lsResult.add(getTitle());
		Map<String, Double> mapCondition2UQ = null; 
		if (enumExpression == EnumExpression.UQPM || enumExpression == EnumExpression.UQRPKM) {
			mapCondition2UQ = getMapCond2UQ();
		}
		for (String geneName : getSetGeneName()) {
			List<String> lsTmpResult = new ArrayList<String>();
			lsTmpResult.add(geneName);
			if (!mapGene2Anno.isEmpty()) {
				lsTmpResult.addAll(mapGene2Anno.get(geneName));
			}
			
			lsTmpResult.addAll(getLsValue(geneName, enumExpression, mapCondition2UQ));
			lsResult.add(lsTmpResult.toArray(new String[0]));
		}
		return lsResult;
	}
	/**
	 * 获得全体时期的表达情况和ratio信息，用于mapping率
	 * @param enumExpression
	 *  @return 返回按照 lsConditions顺序的基因表达list
	 */
	public List<String[]> getLsAllCountsNum2Ratio(EnumExpression enumExpression) {
		setAllreadsPerConditon();

		List<String[]> lsResult = new ArrayList<>();
		lsResult.add(getTitle2Ratio());
		Map<String, Double> mapCondition2UQ = null; 
		if (enumExpression == EnumExpression.UQPM || enumExpression == EnumExpression.UQRPKM) {
			mapCondition2UQ = getMapCond2UQ();
		}
		for (String geneName : getSetGeneName()) {
			List<String> lsTmpResult = new ArrayList<String>();
			lsTmpResult.add(geneName);
			if (!mapGene2Anno.isEmpty()) {
				lsTmpResult.addAll(mapGene2Anno.get(geneName));
			}
			
			lsTmpResult.addAll(getLsValue2Ratio(geneName, enumExpression, mapCondition2UQ));
			lsResult.add(lsTmpResult.toArray(new String[0]));
		}
		return lsResult;
	}
	
	/** @return 返回每个时期的UQreads */
	private Map<String, Double> getMapCond2UQ() {
		Map<String, Double> mapGene2UQ = new HashMap<>();
		for (String condition : setCondition) {
			mapGene2UQ.put(condition, getUQ(condition));
		}
		return mapGene2UQ;
	}
	
	/** @return 返回指定时期的UQreads */
	private double getUQ(String condition) {
		List<Double> lsValues = new ArrayList<>();
		for (String geneName : getSetGeneName()) {
			Map<String, Double> mapCond2Counts = mapGene_2_Cond2Exp.get(geneName);
			if (mapCond2Counts == null) {
				lsValues.add(0.0);
			} else {
				Double readsCounts = mapCond2Counts.get(condition);
				if (readsCounts == null) {
					lsValues.add(0.0);
				} else {
					lsValues.add(readsCounts);
				}
			}
		}
		double uq = MathComput.median(lsValues, 75);
		return uq;
	}
	
	/** 获得全体时期的基因表达情况 */
	private List<String> getLsValue(String geneName, EnumExpression enumExpression, Map<String, Double> mapCondition2UQ) {
		List<String> lsValue = new ArrayList<>();
		Map<String, Double> mapCond2Exp = mapGene_2_Cond2Exp.get(geneName);
		for (String condition : setCondition) {
			Double value = mapCond2Exp.get(condition);			
			double uq = (mapCondition2UQ != null) ? mapCondition2UQ.get(condition) : 0;
			
			Long allReadsNum = mapCond2AllReads.get(condition);
			Integer geneLen = mapGene2Len == null ? 0 : mapGene2Len.get(geneName);
			String geneValue = getValue(enumExpression, value, allReadsNum, uq, geneLen);
			lsValue.add(geneValue);
		}
		return lsValue;
	}
	/** 获得全体时期的基因表达情况以及其在allreads中的比例 */
	private List<String> getLsValue2Ratio(String geneName, EnumExpression enumExpression, Map<String, Double> mapCondition2UQ) {
		List<String> lsValue = new ArrayList<>();
		Map<String, Double> mapCond2Exp = mapGene_2_Cond2Exp.get(geneName);
		for (String condition : setCondition) {
			Double value = mapCond2Exp.get(condition);			
			double uq = (mapCondition2UQ != null) ? mapCondition2UQ.get(condition) : 0;
			
			Long allReadsNum = mapCond2AllReads.get(condition);
			Integer geneLen = mapGene2Len == null ? 0 : mapGene2Len.get(geneName);
			String geneValue = getValue(enumExpression, value, allReadsNum, uq, geneLen);
			lsValue.add(geneValue);
			lsValue.add(getValue(EnumExpression.Ratio, value, allReadsNum, uq, geneLen));
		}
		return lsValue;
	}
	/** 如果allReads没有设定，则设定每个时期的allReads数量为该时期counts数加和 */
	private void setAllreadsPerConditon() {
		for (String condition : setCondition) {
			setConditionAllreads(condition);
		}
	}
	/** 如果allReads没有设定，则设定每个时期的allReads数量为该时期counts数加和 */
	private void setConditionAllreads(String condition) {
		if (mapCond2AllReads.containsKey(condition)) {
			return;
		}
		double number = 0;
		for (String geneName : mapGene_2_Cond2Exp.keySet()) {
			Map<String, Double> mapCond2Exp = mapGene_2_Cond2Exp.get(geneName);
			Double value = mapCond2Exp.get(condition);
			if (value == null) value = 0.0;
			number += value;
		}
		mapCond2AllReads.put(condition, (long) number);
	}
	/**
	 * 获得单个时期的基因表达情况
	 * @param geneName
	 * @param enumExpression
	 * @return
	 */
	private String getValueCondition(String geneName, EnumExpression enumExpression, double uq) {
		Map<String, Double> mapCond2Exp = mapGene_2_Cond2Exp.get(geneName);
		Double value = mapCond2Exp.get(currentCondition);
		Long allReadsNum = mapCond2AllReads.get(currentCondition);
		Integer geneLen = mapGene2Len == null ? 0 : mapGene2Len.get(geneName);
		String geneValue = getValue(enumExpression, value, allReadsNum, uq, geneLen);
		return geneValue;
	}
	
	/** 计算单个基因的表达值 */
	private String getValue(EnumExpression enumExpression, Double value, Long allReadsNum, double upQuerterNum, Integer geneLen) {
		String resultValue = null;
		if (geneLen == null) geneLen = 1000;
		if (allReadsNum == null) allReadsNum = (long) mapreadsNum;
		DecimalFormat df = new DecimalFormat("0.##"); 
		if (value == null) return "0";
		if (enumExpression == EnumExpression.RawValue) {
			resultValue = value + "";
		} else if (enumExpression == EnumExpression.Counts) {
			resultValue = value.intValue() + "";
		} else if (enumExpression == EnumExpression.TPM) {
			resultValue = (int)(value*mapreadsNum/allReadsNum) + "";
		} else if (enumExpression == EnumExpression.RPKM) {
			resultValue = value*mapreadsNum*1000/allReadsNum/geneLen + "";
		} else if (enumExpression == EnumExpression.UQRPKM) {
			resultValue = value*geneExp*1000/upQuerterNum/geneLen + "";
		} else if (enumExpression == EnumExpression.UQPM) {
			resultValue = value*geneExp/upQuerterNum + "";
		} else if (enumExpression == EnumExpression.Ratio) {
			resultValue = df.format(value/allReadsNum) + "";
		}
		return resultValue;
	}
	
	private String[] getTitle() {
		List<String> lsTitle = new ArrayList<>();
		lsTitle.add(geneTitleName);
		lsTitle.addAll(setGeneAnnoTitle);

		lsTitle.addAll(setCondition);
		
		return lsTitle.toArray(new String[0]);
	}
	private String[] getTitle2Ratio() {
		List<String> lsTitle = new ArrayList<>();
		lsTitle.add(geneTitleName);
		lsTitle.addAll(setGeneAnnoTitle);
		for (String condition : setCondition) {
			lsTitle.add(condition);
			lsTitle.add(condition + "_Rate");
		}		
		return lsTitle.toArray(new String[0]);
	}
	private String[] getCurrentTitle() {
		List<String> lsTitle = new ArrayList<>();
		lsTitle.add(geneTitleName);
		lsTitle.addAll(setGeneAnnoTitle);

		lsTitle.add(currentCondition);
		return lsTitle.toArray(new String[0]);
	}
	
	/**
	 * 如果文件夹不存在，会新建文件夹
	 * @param writeAllCondition
	 * @param fileName
	 * @param expTable
	 * @param enumExpression
	 */
	public void writeFile(boolean writeAllCondition, String fileName, EnumExpression enumExpression) {
		List<String[]> lsValues = null;
		if (writeAllCondition) {
			lsValues = getLsAllCountsNum(enumExpression);
		} else {
			lsValues = getLsCountsNum(enumExpression);
		}
		
		if (lsValues == null || lsValues.size() <= 1) {
			return;
		}
		FileOperate.createFolders(FileOperate.getPathName(fileName));
		TxtReadandWrite txtWrite = new TxtReadandWrite(fileName, true);
		txtWrite.ExcelWrite(lsValues);
		txtWrite.close();
	}
}
