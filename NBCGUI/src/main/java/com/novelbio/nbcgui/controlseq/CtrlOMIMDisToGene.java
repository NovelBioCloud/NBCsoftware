package com.novelbio.nbcgui.controlseq;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.classification.InterfaceAudience.Public;

import com.esotericsoftware.kryo.io.Input;
import com.novelbio.base.dataOperate.TxtReadandWrite;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.database.domain.geneanno.GeneInfo;
import com.novelbio.database.model.modgeneid.GeneID;
import com.novelbio.omimdb.mgmt.MgmtMorbidMap;
import com.novelbio.omimdb.mgmt.MgmtOMIMUnit;
import com.novelbio.omimdb.model.MIMInfo;
import com.novelbio.omimdb.model.MorbidMap;

public class CtrlOMIMDisToGene {

	private static int TAXID = 9606;
	String inputFile;
	String outputFile;
	int accIDColumn;
	
	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}
	public String getInputFile() {
		return inputFile;
	}
	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}
	public String getOutputFile() {
		return outputFile;
	}
	public void setAccIDColumn(int accIDColumn) {
		this.accIDColumn = accIDColumn;
	}
	public int getAccIDColumn() {
		return accIDColumn;
	}
	
	public void run () {
		TxtReadandWrite txtMorbidMapRead = new TxtReadandWrite(inputFile);
		TxtReadandWrite txtResultFileWrite = new TxtReadandWrite(outputFile, true);
		txtResultFileWrite.writefileln(CtrlOMIMDisToGene.getTitle());
		for (String content : txtMorbidMapRead.readlines()) {   //如何去掉第一行的Titile????
			String[] arrLine = content.split("\t");
			String disString = arrLine[accIDColumn - 1];
			List<String[]> liDisToGen = CtrlOMIMDisToGene.getOMIMDisGene(disString);
			for (String[] contentString : liDisToGen) {
				txtResultFileWrite.writefileln(contentString);
			}
		}
		txtMorbidMapRead.close();
		txtResultFileWrite.close();
	}
	//此处这个方法可以与AnnoOMIM.java类型的方法，进行合并，另外写一个类，运用多态的性能即可！
	public static List<String[]> getOMIMDisGene(String dis) {
		MgmtMorbidMap mgmtMorbidMap = MgmtMorbidMap.getInstance();
		MgmtOMIMUnit mgmtOMIMUnit = MgmtOMIMUnit.getInstance();
		List<String[]> lsResult = new ArrayList<String[]>();
		ArrayList<String> lsResultTmp = new ArrayList<String>();
		List<MorbidMap> liMorbidMap = mgmtMorbidMap.findInfByDisease(dis);
		lsResultTmp.add(dis);
		if (liMorbidMap.size() == 0) {
			fillLsResult(dis + "", lsResult, 9);	
			return lsResult;
		}
		for (MorbidMap morbidMap : liMorbidMap) {
			ArrayList<String> lsTmp = (ArrayList<String>) lsResultTmp.clone();
			//TODO此处换成Gene Name
			lsTmp.add(morbidMap.getGeneId() + "");  
			lsTmp.add(morbidMap.getGeneMimId() + "");
			if (morbidMap.getGeneMimId() != 0) {
				MIMInfo mimInfo = mgmtOMIMUnit.findByMimId(morbidMap.getGeneMimId());
				lsTmp.add(mimInfo.getDesc());
				List<String> listRef = mimInfo.getListRef();
				lsTmp.add(listRef.get(0));
			}
			if (morbidMap.getPheneMimId() == 0) {
				lsTmp.add("");
				lsTmp.add("");
				lsTmp.add("");
			} else {
				lsTmp.add(morbidMap.getPheneMimId() + "");
			}
			if (morbidMap.getPheneMimId() != 0) {
				MIMInfo mimInfo = mgmtOMIMUnit.findByMimId(morbidMap.getPheneMimId());
				lsTmp.add(mimInfo.getDesc());
				List<String> listRef = mimInfo.getListRef();
				lsTmp.add(listRef.get(0));
			}
			List<String> listDis = morbidMap.getListDis();
			String disease = "";
			for (String disContent : listDis) {
				disease = disease.concat(disContent + "");
			}
			lsTmp.add(morbidMap.getDisType() + "");
			lsResult.add(lsTmp.toArray(new String[0]));
		}
		return lsResult;
	}
	private static void fillLsResult(String accID, List<String[]> lsResult, int arrayLength) {
		String[] tmpResult = new String[arrayLength];
		tmpResult[0] = accID;
		for (int i = 1; i < tmpResult.length; i++) {                                  
			tmpResult[i] = "";
		}
		lsResult.add(tmpResult);
	}
	public static String[] getTitle() {
		List<String> lsTitle = new ArrayList<String>();
		lsTitle.add("Disease");
		lsTitle.add("Symbol/AccID");
		lsTitle.add("GeneMIMID");
		lsTitle.add("GeneOMIMDesc");
		lsTitle.add("GeneOMIMRefence");
		lsTitle.add("PhenMIMID");
		lsTitle.add("PhenOMIMDesc");
		lsTitle.add("PhenOMIMRefence");
		lsTitle.add("DiseaseType");
		return lsTitle.toArray(new String[0]);
	}
}
