package com.novelbio.nbcgui.controltest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.commons.math.stat.inference.TestUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.novelbio.base.dataOperate.TxtReadandWrite;
import com.novelbio.base.dataStructure.MathComput;
import com.novelbio.generalConf.TitleFormatNBC;

/** 常规t检验
 * 
 * @author zong0jie
 */
@Component
@Scope("prototype")
public class DiffExpTtest extends DiffExpAbs {
	/** 比较矩阵设计好后需要将每个ID对应到试验名上 <br>
	 * 譬如：<br>
	 * design = model.matrix(~ -1+factor (c(1,1,2,2,3,3))) <br>
	 * colnames(design) = c("Patient","Treat","Norm")<br>
	 * 1 = Patient<br>
	 * 2 = Treat<br>
	 * 3 = Norm<br>
	 * */
	HashMap<Integer, String> mapID2Sample = new HashMap<Integer, String>();
	@Override
	protected void setOutScriptPath() {
	}

	@Override
	protected void setFileNameRawdata() {
	}

	@Override
	protected void generateScript() {
		// TODO Auto-generated method stub
		
	}
	protected void writeToGeneFile() { }
	public void  modifyResult(){}
	public void clean(){};
	@Override
	protected void run() {
		/** col是实际col */
		HashMap<String, ArrayList<Integer>> mapSample2ColNum = new HashMap<String, ArrayList<Integer>>();
		for (String[] col2Sample : lsSampleColumn2GroupName) {
			ArrayList<Integer> lsColumn = new ArrayList<Integer>();
			if (mapSample2ColNum.containsKey(col2Sample[1])) {
				lsColumn = mapSample2ColNum.get(col2Sample[1]);
			} else {
				mapSample2ColNum.put(col2Sample[1], lsColumn);
			}
			lsColumn.add(Integer.parseInt(col2Sample[0]));
		}
		
		ArrayList<String[]> lsValues = getAnalysisGeneInfo();
		//遍历每个比较
		for (Entry<String, String[]> entry : mapOutFileName2Compare.entrySet()) {
			String fileName = entry.getKey();
			String[] treat2col = entry.getValue();
			
			ArrayList<String[]> lsTmpResult = new ArrayList<String[]>();
			lsTmpResult.add(new String[]{TitleFormatNBC.QueryID.toString(), treat2col[0], treat2col[1], TitleFormatNBC.Log2FC.toString(),TitleFormatNBC.Pvalue.toString(), TitleFormatNBC.FDR.toString()});
			ArrayList<Double> lsPvalue = new ArrayList<Double>();
			for (int i = 1; i < lsValues.size(); i++) {
				String[] strings = lsValues.get(i);
	
				ArrayList<Integer> lsColumn1 = mapSample2ColNum.get(treat2col[0]);
				double[] sample1 = new double[lsColumn1.size()];
				for (int j = 0; j < lsColumn1.size(); j++) {
					sample1[j] = Double.parseDouble(strings[lsColumn1.get(j) - 1] );
				}
				
				ArrayList<Integer> lsColumn2 = mapSample2ColNum.get(treat2col[1]);
				double[] sample2 = new double[lsColumn2.size()];
				for (int j = 0; j < lsColumn2.size(); j++) {
					sample2[j] = Double.parseDouble(strings[lsColumn2.get(j) - 1] );
				}
				double pvalue = 1;
				try { pvalue = TestUtils.tTest(sample1, sample2); } catch (Exception e) {  e.printStackTrace(); }
				double treatValue =  mapGeneID_2_Sample2MeanValue.get(strings[0]).get(treat2col[0]);
				double colValue =  mapGeneID_2_Sample2MeanValue.get(strings[0]).get(treat2col[1]);
				double logfc = treatValue > colValue ? 100:-100;
				if (treatValue > 0 && colValue > 0) {
					logfc = Math.log(treatValue/colValue)/Math.log(2);
				}
				lsTmpResult.add(new String[]{strings[0], treatValue + "", colValue + "", logfc + "", pvalue + "", ""});
				lsPvalue.add(pvalue);
			}
			ArrayList<Double> lsfdr = MathComput.pvalue2Fdr(lsPvalue);
			for (int i = 1; i < lsTmpResult.size(); i++) {
				String[] tmp = lsTmpResult.get(i);
				tmp[tmp.length - 1] = lsfdr.get(i-1) + "";
			}
			TxtReadandWrite txtOut = new TxtReadandWrite(fileName, true);
			txtOut.ExcelWrite(lsTmpResult);
			txtOut.close();
		}
	}
	
	@Override
	protected void modifySingleResultFile(String outFileName, String treatName,
			String controlName) {
		// TODO Auto-generated method stub
		
	}

}
