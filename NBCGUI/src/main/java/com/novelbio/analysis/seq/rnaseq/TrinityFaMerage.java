package com.novelbio.analysis.seq.rnaseq;

import java.util.LinkedHashMap;
import java.util.Map;

import com.novelbio.base.SepSign;
import com.novelbio.base.dataOperate.TxtReadandWrite;

/**
 * 本类主要用于合并Trinity的文件用于聚类，以及将聚类的结果做后续分析
 * @author zomg0jie
 *
 */
public class TrinityFaMerage {
	Map<String, String> mapPrefix2TrinityFile = new LinkedHashMap<>();
	
	/**
	 * 添加Trinity拼接好的结果，注意，一个prefix必须只有一个file对应
	 * @param prefix 样本名
	 * @param fileName Trinity拼接好的文件
	 */
	public void addTrinityFa(String prefix, String fileName) {
		mapPrefix2TrinityFile.put(prefix, fileName);
	}
	
	
	
	
	
}
