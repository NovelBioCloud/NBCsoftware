package com.novelbio.nbcgui.controltest;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.novelbio.base.cmd.CmdOperate;
import com.novelbio.base.dataOperate.DateUtil;
import com.novelbio.base.dataOperate.ExcelTxtRead;
import com.novelbio.base.dataOperate.TxtReadandWrite;
import com.novelbio.base.dataStructure.ArrayOperate;
import com.novelbio.base.dataStructure.MathComput;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.generalConf.TitleFormatNBC;

import freemarker.template.Template;
@Component
@Scope("prototype")
public class DiffExpEdgeR extends DiffExpAbs {
	private static final Logger logger = Logger.getLogger(DiffExpEdgeR.class);
	/**
	 * lslsGeneInfo中每一列样本所对应的标准化的值
	 * 首先获得reads数最多的样本 m，其reads数为 mNum，
	 * 然后将每个样本k的reads数 kNum
	 * 标准化的系数  normalizeNum = kNum/mNum
	 * 标准化方法为 样本 k中每个基因的reads数 geneCountNumModify = geneCountNum * kNum/mNum
	 * 即为 geneCountNumModify = geneCountNum * normalizeNum
	 */
	HashMap<Integer, Double> mapColNum2NormalizeNum = new HashMap<Integer, Double>();

	/**
	 * 获得每个样本所需要乘以的修正系数
	 * @return
	 */
	protected void setNormalizeCoef() {
		//用来遍历全部样本列
		ArrayList<Integer> lsColNum = new ArrayList<Integer>();
		
		double maxSampleCount = 0;
		//获得样本中reads数最多的样本，并装在hash表中
		for (int i = 0; i < lsSampleColumn2GroupName.size(); i++) {
			String colSample = lsSampleColumn2GroupName.get(i)[0];
			int colNum = Integer.parseInt(colSample) - 1;//实际列所以要减去1
			lsColNum.add(colNum);
			ArrayList<Double> lsValue = new ArrayList<Double>();
			for (String[] strings : lsGeneInfo) {
				try {
					double tmpValue = Double.parseDouble(strings[colNum]);
					lsValue.add(tmpValue);
				} catch (Exception e) { }
			}
			double thisAllCounts = MathComput.sum(lsValue);
			mapColNum2NormalizeNum.put(colNum, (double) thisAllCounts);
			if (thisAllCounts > maxSampleCount) {
				maxSampleCount = thisAllCounts;
			}
		}
		
		//计算系数
		for (Integer integer : lsColNum) {
			double tmpAllCounts = mapColNum2NormalizeNum.get(integer);
			mapColNum2NormalizeNum.put(integer, maxSampleCount/tmpAllCounts);
		}
	}
	
	protected ArrayList<String[]> removeDuplicate(List<String[]> lsGeneInfo) {
		ArrayList<String[]> lsResult = super.removeDuplicate(lsGeneInfo);
		for (int i = 1; i < lsResult.size(); i++) {
			String[] strings = lsResult.get(i);
			for (int j = 1; i < strings.length; i++) {
				strings[j] = (int)Double.parseDouble(strings[j]) + "";
			}
		}
		return lsResult;
	}
	
	public void calculateResult() {
		setNormalizeCoef();
		super.calculateResult();
	}
	/**
	 * 获得 
	 * 样本时期--该时期内平均值
	 * 的map
	 * 内部在每个值上都乘以了修正系数
	 * @param info
	 * @return
	 */
	protected HashMap<String, Double> mapTime2AvgValue(String[] info) {
		HashMap<String, ArrayList<Double>> mapTime2LsValue = new HashMap<String, ArrayList<Double>>();
		
		for (int i = 0; i < lsSampleColumn2GroupName.size(); i++) {
			int colNum = Integer.parseInt(lsSampleColumn2GroupName.get(i)[0]) - 1;
			//每个值都乘以修正系数
			double value = Double.parseDouble(info[colNum]) * mapColNum2NormalizeNum.get(colNum);
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
	
	
	@Override
	protected void setOutScriptPath() {
		outScript = workSpace + "EdgeR_" + DateUtil.getDateAndRandom() + ".R";
		
	}

	@Override
	protected void setFileNameRawdata() {
		fileNameRawdata = workSpace + "EdgeRGeneInfo_" + DateUtil.getDateAndRandom() + ".txt";
	}

	@Override
	protected void run() {
		Rrunning("EdgeR");
	}
	
	@Override
	protected void generateScript() {
		Map<String,Object> mapData = new HashMap<String, Object>();
		mapData.put("workspace", getWorkSpace());
		mapData.put("filename", getFileName());
		mapData.put("Group", getGroupInfo());
		mapData.put("isReplicate", isHaveReplicate());
		mapData.put("mapCompare2Outfile", getMapCompare2Outfile());
		try {
			Template template = freeMarkerConfiguration.getTemplate("/R/diffgene/EdgeR.ftl");
			StringWriter sw = new StringWriter();
			TxtReadandWrite txtReadandWrite = new TxtReadandWrite(outScript, true);
			// 处理并把结果输出到字符串中
			template.process(mapData, sw);
			txtReadandWrite.writefile(sw.toString());
			txtReadandWrite.close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("渲染出错啦! " + e.getMessage());
		}
	}
	
	/**
	 * group = factor( c("Patient", "Patient","Treat","Treat")) )
	 * @param content
	 * @return
	 */
	private String getGroupInfo() {
		String result = CmdOperate.addQuot(lsSampleColumn2GroupName.get(0)[1]);
		for (int i = 1; i < lsSampleColumn2GroupName.size(); i++) {
			String[] sampleCol2GroupName = lsSampleColumn2GroupName.get(i);
			result = result + ", " + CmdOperate.addQuot(sampleCol2GroupName[1]);
		}
		return result;
	}

	/** 是否有重复 */
	private boolean isHaveReplicate() {
		boolean haveReplicate = false;
		HashSet<String> setGroupName = new HashSet<String>();
		for (String[] sampleCol2GroupName : lsSampleColumn2GroupName) {
			if (setGroupName.contains(sampleCol2GroupName[1])) {
				haveReplicate = true;
				break;
			}
			setGroupName.add(sampleCol2GroupName[1]);
		}
		return haveReplicate;
	}
	 
	/**
	 * 添加样本名字和比较
	 * result = exactTest(m, pair=c({$Compare})); 
	 * result2=cbind(resultFinal,fdr=p.adjust(resultFinal[,3])); 
	 * write.table(result2, file="{$OutFileName}",sep="\t")
	 * @param content
	 * @return
	 */
	private Map<String, String> getMapCompare2Outfile() {
		Map<String, String> mapCompare2Outfile = new LinkedHashMap<String, String>();
		ArrayList<String> lsFileName = ArrayOperate.getArrayListKey(mapOutFileName2Compare);
		for (int i = 0; i < lsFileName.size(); i++) {
			String outFileName = lsFileName.get(i);
			String[] pair = mapOutFileName2Compare.get(outFileName);
			String compare = CmdOperate.addQuot(pair[1]) + "," + CmdOperate.addQuot(pair[0]);
			mapCompare2Outfile.put(compare, outFileName.replace("\\", "/"));
		}
		return mapCompare2Outfile;
	}
 
	
	@Override
	protected void modifySingleResultFile(String outFileName, String treatName, String controlName) {
		if (!FileOperate.isFileExistAndBigThanSize(outFileName, 0)) {
			logger.error("没产生结果文件" + outFileName);
			return;
		}
		ArrayList<String[]> lsResult = new ArrayList<String[]>();
		ArrayList<String[]> lsDifGene = ExcelTxtRead.readLsExcelTxt(outFileName, 1);
		String[] title = new String[]{TitleFormatNBC.AccID.toString(), treatName, controlName, TitleFormatNBC.Log2FC.toString(), TitleFormatNBC.Pvalue.toString(), TitleFormatNBC.FDR.toString()};
		lsResult.add(title);

		ArrayList<int[]> lsIndelItem = new ArrayList<int[]>();
		lsIndelItem.add(new int[]{1, 2});//"treat" and control
		lsIndelItem.add(new int[]{2, -1});//"LogCMP"

		for (int i = 1; i < lsDifGene.size(); i++) {
			String[] tmpResult = ArrayOperate.indelElement(lsDifGene.get(i), lsIndelItem, "");
			String geneID = tmpResult[0].replace("\"", "");
			tmpResult[1] = mapGeneID_2_Sample2MeanValue.get(geneID).get(treatName) + "";
			tmpResult[2] = mapGeneID_2_Sample2MeanValue.get(geneID).get(controlName) + "";
			if (!tmpResult[2].equals("0")) {
				setLogFC(tmpResult);
			}
			for (int j = 0; j < tmpResult.length; j++) {
				tmpResult[j] = tmpResult[j].replace("\"", "");
			}
			lsResult.add(tmpResult);
		}
		FileOperate.DeleteFileFolder(outFileName);
		//防止R还没输出结果就去读取
		try { Thread.sleep(50); } catch (Exception e) { }
		
		TxtReadandWrite txtOutFinal = new TxtReadandWrite(outFileName, true);
		txtOutFinal.ExcelWrite(lsResult);
		txtOutFinal.close();
	}
	
	private void setLogFC(String[] resultInfo) {
		if (!resultInfo[2].equals("0")) {
			try {
				double treat = Double.parseDouble(resultInfo[1]);
				double control = Double.parseDouble(resultInfo[2]);
				resultInfo[3] = Math.log(treat/control)/Math.log(2) + "";
			} catch (Exception e) { }
		}
	}

}
