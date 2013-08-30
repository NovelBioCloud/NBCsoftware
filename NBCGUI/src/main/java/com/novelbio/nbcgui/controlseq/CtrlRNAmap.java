package com.novelbio.nbcgui.controlseq;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.ArrayListMultimap;
import com.novelbio.analysis.seq.fastq.FastQ;
import com.novelbio.analysis.seq.genome.GffChrAbs;
import com.novelbio.analysis.seq.mapping.MapLibrary;
import com.novelbio.analysis.seq.mapping.MapRNA;
import com.novelbio.analysis.seq.mapping.MapRsem;
import com.novelbio.analysis.seq.mapping.MapTophat;
import com.novelbio.analysis.seq.mapping.StrandSpecific;
import com.novelbio.base.dataStructure.MathComput;
import com.novelbio.base.fileOperate.FileOperate;

public class CtrlRNAmap {
	public static final int TOP_HAT = 2;
	public static final int RSEM = 4;
	int mapType;
	MapLibrary mapLibrary;
	StrandSpecific strandSpecific;
	
	int threadNum = 4;
	Map<String, List<List<String>>> mapPrefix2LsFastq;
	
	MapRNA mapRNA;
	
	GffChrAbs gffChrAbs;
	String indexFile = "";
	/** ""表示使用GTF */
	String gtfAndGene2Iso = "";
	
	/** tophat是否用GTF文件进行校正，默认为true，如果出错就要考虑不用GTF */
	boolean useGTF = true;
	String outPrefix;
	/** 保存最终结果，只有rsem才会有
	 * 第一行为标题
	 * 之后每一行为基因表达情况
	 *  */
	List<List<String>> lsExpResultRsemRPKM = new ArrayList<>();
	/** 保存最终结果，只有rsem才会有
	 * 第一行为标题
	 * 之后每一行为基因表达情况
	 *  */
	List<List<String>> lsExpResultRsemCounts = new ArrayList<>();
	
	public CtrlRNAmap(int mapType) {
		if (mapType == TOP_HAT) {
			this.mapType = TOP_HAT;
		}
		else if (mapType == RSEM) {
			this.mapType = RSEM;
		}	
	}
	public void setMapPrefix2LsFastq(CopeFastq copeFastq) {
		copeFastq.setMapCondition2LsFastQLR();
		this.mapPrefix2LsFastq = copeFastq.getMapCondition2LslsFastq();
	}
	
	/** 输入CopeFastq的对象，该对象设定完后直接传入本方法，即可完成项目配置 */
	public void setCopeFastq(CopeFastq copeFastq) {
		
	}
	
	public void setGffChrAbs(GffChrAbs gffChrAbs) {
		this.gffChrAbs = gffChrAbs;
	}
	
	public void setOutPathPrefix(String outPathPrefix) {
		this.outPrefix = outPathPrefix;
	}
	/** MapTop里面的参数 */
	public void setStrandSpecifictype(StrandSpecific strandSpecifictype) {
		this.strandSpecific = strandSpecifictype;
	}
	public void setLibrary(MapLibrary mapLibrary) {
		this.mapLibrary = mapLibrary;
	}
	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}
	public void setIsUseGTF(boolean useGTF) {
		this.useGTF= useGTF;
	}
	/**
	 * 如果referece在数据库中找不到，就输入该文件
	 * @param indexFile
	 */
	public void setIndexFile(String indexFile) {
		this.indexFile = indexFile;
	}
	public void setGtfAndGene2Iso(String gtfAndGene2Iso) {
		this.gtfAndGene2Iso = gtfAndGene2Iso;
	}
	
	public void mapping() {
		lsExpResultRsemRPKM = new ArrayList<>();
		lsExpResultRsemCounts = new ArrayList<>();
		for (Entry<String, List<List<String>>> entry : mapPrefix2LsFastq.entrySet()) {
			if (!creatMapRNA()) {
				return;
			}
			mapRNA.setGffChrAbs(gffChrAbs);
			setRefFile();
			String prefix = entry.getKey();
			List<List<String>> lsFastqFR = entry.getValue();
		
			mapRNA.setLeftFq(CopeFastq.convertFastqFile(lsFastqFR.get(0)));
			mapRNA.setRightFq(CopeFastq.convertFastqFile(lsFastqFR.get(1)));
			setMapLibrary(mapLibrary);
			mapRNA.setStrandSpecifictype(strandSpecific);
			mapRNA.setThreadNum(threadNum);
			mapRNA.setOutPathPrefix(outPrefix + prefix);
			
			if (mapType == TOP_HAT && !useGTF) {
				mapRNA.setGtf_Gene2Iso(null);
			} else {
				mapRNA.setGtf_Gene2Iso(gtfAndGene2Iso);
			}
			
			mapRNA.mapReads();
			setExpResultCounts(prefix, mapRNA);
			setExpResultRPKM(prefix, mapRNA);
		}
	}
	
	private boolean creatMapRNA() {
		if (mapType == TOP_HAT) {
			mapRNA = new MapTophat();
			return true;
		}
		else if (mapType == RSEM) {
			mapRNA = new MapRsem();
			return true;
		}
		else {
			return false;
		}
	}
	private void setRefFile() {
		if (gffChrAbs == null || FileOperate.isFileExist(indexFile)) {
			mapRNA.setFileRef(indexFile);
			return;
		}
		
		if (mapType == TOP_HAT) {
			mapRNA.setFileRef(gffChrAbs.getSpecies().getIndexChr(mapRNA.getBowtieVersion()));
		} else {
			mapRNA.setFileRef(gffChrAbs.getSpecies().getRefseqFile());
		}
	}
	private void setMapLibrary(MapLibrary mapLibrary) {
		if (mapLibrary == MapLibrary.SingleEnd) {
			return;
		}
		else if (mapLibrary == MapLibrary.PairEnd) {
			mapRNA.setInsert(450);
		}
		else if (mapLibrary == MapLibrary.MatePair) {
			mapRNA.setInsert(4500);
		}
	}
	/** 获得基因表达 */
	private void setExpResultRPKM(String prefix, MapRNA mapRNA) {
		if (mapType != RSEM) {
			return;
		}
		MapRsem mapRsem = (MapRsem) mapRNA;
		ArrayListMultimap<String, Double> mapGeneID2LsExp = mapRsem.getMapGeneID2LsExp();
		//第一组结果直接装进去
		if (lsExpResultRsemRPKM.size() == 0) {
			ArrayList<String> lsTitleRPKM = new ArrayList<String>();
			lsTitleRPKM.add("GeneID"); lsTitleRPKM.add(prefix + "_RPKM");
			lsExpResultRsemRPKM.add(lsTitleRPKM);
			
			for (String geneID : mapGeneID2LsExp.keySet()) {
				ArrayList<String> lsDetail = new ArrayList<String>();
				
				List<Double> lsValue = mapGeneID2LsExp.get(geneID);
				lsDetail.add(geneID);//获得基因名
				lsDetail.add(MathComput.mean(lsValue) + "" );//获得平均数
				lsExpResultRsemRPKM.add(lsDetail);
			}
		}
		//后面的就在hash表里面查
		else {
			lsExpResultRsemRPKM.get(0).add(prefix + "_RPKM");
			for (int i = 1; i < lsExpResultRsemRPKM.size(); i++) {
				List<String> lsDetail = lsExpResultRsemRPKM.get(i);
				List<Double> lsValue = mapGeneID2LsExp.get(lsDetail.get(0));
				lsDetail.add(MathComput.mean(lsValue) + "");
			}
		}
	}
	/** 获得基因表达 */
	private void setExpResultCounts(String prefix, MapRNA mapRNA) {
		if (mapType != RSEM) {
			return;
		}
		MapRsem mapRsem = (MapRsem) mapRNA;
		ArrayListMultimap<String, Integer> mapGeneID2LsCounts = mapRsem.getMapGeneID2LsCounts();
		//第一组结果直接装进去
		if (lsExpResultRsemCounts.size() == 0) {
			ArrayList<String> lsTitleCounts = new ArrayList<String>();
			lsTitleCounts.add("GeneID"); lsTitleCounts.add(prefix + "_Counts");
			lsExpResultRsemCounts.add(lsTitleCounts);
			for (String geneID : mapGeneID2LsCounts.keySet()) {
				List<Integer> lsValue = mapGeneID2LsCounts.get(geneID);

				ArrayList<String> lsDetail = new ArrayList<String>();
				lsDetail.add(geneID);//获得基因名
				lsDetail.add((int)MathComput.mean(lsValue) + "" );//获得平均数
				lsExpResultRsemCounts.add(lsDetail);
			}
		}
		//后面的就在hash表里面查
		else {
			lsExpResultRsemCounts.get(0).add(prefix + "_Counts");
			for (int i = 1; i < lsExpResultRsemCounts.size(); i++) {
				List<String> lsDetail = lsExpResultRsemCounts.get(i);
				List<Integer> lsValue = mapGeneID2LsCounts.get(lsDetail.get(0));
				lsDetail.add((int)MathComput.mean(lsValue) + "");
			}
		}
	}
	
	public ArrayList<String[]> getLsExpRsemRPKM() {
		ArrayList<String[]> lsResult = new ArrayList<String[]>();
		for (List<String> lsTmpResult : lsExpResultRsemRPKM) {
			String[] tmpResult = new String[lsTmpResult.size()];
			for (int i = 0; i < tmpResult.length; i++) {
				tmpResult[i] = lsTmpResult.get(i);
			}
			lsResult.add(tmpResult);
		}
		return lsResult;
	}
	
	public ArrayList<String[]> getLsExpRsemCounts() {
		ArrayList<String[]> lsResult = new ArrayList<String[]>();
		for (List<String> lsTmpResult : lsExpResultRsemCounts) {
			String[] tmpResult = new String[lsTmpResult.size()];
			for (int i = 0; i < tmpResult.length; i++) {
				tmpResult[i] = lsTmpResult.get(i);
			}
			lsResult.add(tmpResult);
		}
		return lsResult;
	}
}
