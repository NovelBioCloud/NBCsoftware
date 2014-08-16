package com.novelbio.nbcgui.GUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.novelbio.base.dataOperate.ExcelTxtRead;
import com.novelbio.base.dataOperate.TxtReadandWrite;
import com.novelbio.database.model.modgeneid.GeneID;
import com.novelbio.database.model.species.Species;


public class GeneNetWork {
	
	private List<List<String>> lslsGeneExcel;
	Map<Integer, Map<String, List<String>>> mapTaxID_2_mapGene2NetWork = new HashMap<Integer, Map<String,List<String>>>();
	HashMap<String, Integer> mapTitle2colNum = new HashMap<String, Integer>();
	List<List<String>> lslsResult;
	
	/**把结果在Gui中展示出来*/
	public List<List<String>> getLslsResult() {
		return lslsResult;
	}
	/**为了在Gui里面展示出来当前表格的信息*/
	public List<List<String>> getLslsGene() {
		return lslsGeneExcel;
	}
	
	/**Gui中combox显示当前Titile*/
	public HashMap<String, Integer> getMapTitle2colNum() {
		return mapTitle2colNum;
	}
	
	/**把interactions文件读进来来放入一个map*/
	private void loading(String loadingFile,Species species) {
		if (mapTaxID_2_mapGene2NetWork.size() > 0) {
			return;
		}
		System.out.println(loadingFile);
		TxtReadandWrite txtReadInteract = new TxtReadandWrite(loadingFile);
		for (String content : txtReadInteract.readlines()) {
			if (content.startsWith("#")) {
				continue;
			}
			String[] ss = content.split("\t");
			int taxID = Integer.parseInt(ss[0]);
			Map<String, List<String>> mapGene2NetWork = null;
			if (mapTaxID_2_mapGene2NetWork.containsKey(taxID)) {
				mapGene2NetWork = mapTaxID_2_mapGene2NetWork.get(taxID);
			} else {
				mapGene2NetWork = new HashMap<String, List<String>>();
				mapTaxID_2_mapGene2NetWork.put(taxID, mapGene2NetWork);
			}
			
			String  geneID1 = ss[1];
			String  geneID2 = ss[6];
			if (geneID1.equals("-") || geneID2.equals("-") ) {
				continue;
			}
			String gene1_gene2 = geneID1 + "_" + geneID2;
//			String attribute =geneID1 + "\t" + geneID2 + "\t" + list.get(13) + "\t" + list.get(14) + "\t" + list.get(17);
			ArrayList<String> lsAttribute = new ArrayList<String>();
//			geneID3 = new GeneID(GeneID.IDTYPE_GENEID, geneID1, species.getTaxID());
			lsAttribute.add(geneID1);
//			lsAttribute.add(geneID3.getSymbol());
			lsAttribute.add(geneID2);
//			geneID3 = new GeneID(GeneID.IDTYPE_GENEID, geneID2, species.getTaxID());
//			lsAttribute.add(geneID3.getSymbol());
			lsAttribute.add(ss[13]);
			lsAttribute.add(ss[14]);
			lsAttribute.add(ss[17]);			
			mapGene2NetWork.put(gene1_gene2, lsAttribute);
		}
		txtReadInteract.close();
	}

	/**设置输出结果的Title*/
	private List<String> getTitle() {
		List<String> lsTitle = new ArrayList<String>();
		lsTitle.add("GeneID1");
		lsTitle.add("GeneID2");
		lsTitle.add("Document");
		lsTitle.add("Time");
		lsTitle.add("DataBase");
		lsTitle.add("GeneSymbol1");
		lsTitle.add("GeneSymbol2");
		return lsTitle;
	}
	
	/**读取Gene的表格*/
	public void readGeneExcel(String geneFile) {
		lslsGeneExcel = ExcelTxtRead.readLsExcelTxtls(geneFile, 1);
	}
	
	/**获取Title方便选择*/
	public HashMap<String, Integer> getTitle2colNum(){
		 mapTitle2colNum = new HashMap<String, Integer>();
		for (int i = 0; i< lslsGeneExcel.get(0).size() ; i++) {
			mapTitle2colNum.put(lslsGeneExcel.get(0).get(i), i + 1);
		}
		return mapTitle2colNum;
	}
	
	/** 读取geneid，如果是geneSymbol，则转化为geneID*/
	private LinkedHashSet<String> readGene( int ColNum ,Species species) {
		LinkedHashSet<String> geneID = new  LinkedHashSet<String>();
		
		for (int i = 1 ;i < lslsGeneExcel.size() ;i ++) {
			List<String> list = lslsGeneExcel.get(i);
			String gene = list.get(ColNum - 1);
			if (gene.matches("\\d+")) {
				geneID.add(gene);
			}else {
				GeneID geneID2 = new GeneID(gene, species.getTaxID());
				geneID.add(geneID2.getGeneUniID());
			}
			
			
		}
		return geneID;
	}
	
	
	/**找出geneID的映射关系*/
	private void mappingGeneID(LinkedHashSet<String> setGeneID,Species species) {
		lslsResult = new ArrayList<List<String>>();
		lslsResult.add(getTitle());
		Set<List<String>> allGeneRelation = new HashSet<List<String>>();
		Map<String, List<String>> mapGene2NetWork = mapTaxID_2_mapGene2NetWork.get(species.getTaxID());
		if (mapGene2NetWork == null) {
			return;
		}
		Set<String> setGeneID_InteractantID = mapGene2NetWork.keySet();
		for (String string : setGeneID_InteractantID) {
			String[] geneIDandInteractantID = string.split("_");
	
			for (String geneID : setGeneID) {
				if (geneIDandInteractantID[0].equals(geneID) && setGeneID.contains(geneIDandInteractantID[1])) {
					allGeneRelation.add(mapGene2NetWork.get(string));
				}
			}
		}
		
		for (List<String> lsLines : allGeneRelation) {
			GeneID geneIDSymbol1 = new GeneID(GeneID.IDTYPE_GENEID, lsLines.get(0), species.getTaxID());
			String geneSymbol1 = geneIDSymbol1.getSymbol();
			lsLines.add( geneSymbol1);
			GeneID geneIDSymbol2 = new GeneID(GeneID.IDTYPE_GENEID, lsLines.get(1), species.getTaxID());
			String geneSymbol2 = geneIDSymbol2.getSymbol();
			lsLines.add(geneSymbol2);
			lslsResult.add(lsLines);
		}
	}

	public void findGeneNetWork(String loadFile, int colNum,Species species) {
		loading(loadFile,species);
		mappingGeneID(readGene(colNum, species),species);
	}
	
	public void writeResult(List<List<String>> lslsResult, String outFile) {
		TxtReadandWrite txtWrite = new TxtReadandWrite(outFile, true);
		for (List<String> list : lslsResult) {
			txtWrite.writefileln(list);
		}
		txtWrite.close();
	}
	
}
