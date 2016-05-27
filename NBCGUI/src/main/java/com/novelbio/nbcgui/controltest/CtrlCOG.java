package com.novelbio.nbcgui.controltest;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.google.common.collect.HashMultimap;
import com.novelbio.analysis.annotation.cog.COGanno;
import com.novelbio.analysis.annotation.cog.EnumCogType;
import com.novelbio.analysis.annotation.functiontest.CogFunTest;
import com.novelbio.analysis.annotation.functiontest.FunctionTest;
import com.novelbio.analysis.annotation.functiontest.FunctionTest.FunctionDrawResult;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.plot.ImageUtils;
import com.novelbio.database.model.modgeneid.GeneID;
@Service
@Scope("prototype")
public class CtrlCOG extends CtrlGOPath implements CtrlTestCOGInt {
	private static final Logger logger = Logger.getLogger(CtrlCOG.class);
	String saveParentPath = "";
	String savePrefix;
	List<String> lsResultPic = new ArrayList<>();
	EnumCogType cogType;
	
	/** @param QtaxID */
	public CtrlCOG() {
		functionTest = FunctionTest.getInstance(FunctionTest.FUNCTION_COG);
	}
	
	public void setCogAnno(COGanno cogAnno) {
		((CogFunTest)functionTest).setCogAnno(cogAnno);
		cogType = cogAnno.getCogType();
	}
	
	@Override
	protected void copeFile(String prix, String excelPath) { }
	
	@Override
	String getGene2ItemFileName(String fileName) {
		String suffix = "_" + cogType.toString() + "_Item";
		String bgName = FileOperate.changeFileSuffix(fileName, suffix, "txt");
		bgName = FileOperate.changeFilePrefix(bgName, ".", null);
		return bgName;
	}

	
	/** 将输入转化为geneID */
	@Override
	protected HashMultimap<String, GeneID> addBG_And_Convert2GeneID(HashMultimap<String, String> mapPrefix2SetAccID) {
		HashMultimap<String, GeneID> mapPrefix2SetGeneID = HashMultimap.create();
		for (String prefix : mapPrefix2SetAccID.keySet()) {
			Set<String> setAccID = mapPrefix2SetAccID.get(prefix);
			for (String accID : setAccID) {
				GeneID geneID = new GeneID(accID, functionTest.getTaxID());
				mapPrefix2SetGeneID.put(prefix, geneID);
			}
		}//*1
		//以下是打算将输入的testID补充进入BG，不过我觉得没必要了
		//我sfesa们只要将BG尽可能做到全面即可，不用想太多
//		for (String prefix : mapPrefix2SetGeneID.keySet()) {
//			Set<GeneID> setGeneIDs = mapPrefix2SetGeneID.get(prefix);
//			functionTest.addBGGeneID(setGeneIDs);
//		}
		return mapPrefix2SetGeneID;
	}
	
	@Override
	public void setSavePathPrefix(String resultPath) {
		if (resultPath.endsWith("\\") || resultPath.endsWith("/")) {
			saveParentPath = resultPath;
			savePrefix = "";
		} else {
			saveParentPath = FileOperate.getParentPathNameWithSep(resultPath);
			savePrefix = FileOperate.getFileName(resultPath) + ".";
		}
		String saveExcelPrefix = null;
		if (resultPath.endsWith("\\") || resultPath.endsWith("/")) {
			saveExcelPrefix = resultPath + getResultBaseTitle() + ".xlsx";
		} else {
			saveExcelPrefix = FileOperate.changeFilePrefix(resultPath, getResultBaseTitle() + "_", "xlsx");
		}
		setSaveExcelPrefix(saveExcelPrefix);
	}
	
	public void running() {
		super.running();
		savePic();
	}
	
	private void savePic() {
		lsResultPic.clear();
		for (Entry<String, FunctionDrawResult> entry : getMapPrefix2FunDrawTest().entrySet()) {
			String prix = entry.getKey();
			BufferedImage bfImageLog2Pic = entry.getValue().getImagePvalue();
			if (bfImageLog2Pic == null) continue;
			String picPvalueName = getSavePicPvalueName(prix);
			ImageUtils.saveBufferedImage(bfImageLog2Pic,picPvalueName );
			BufferedImage bfImageEnrichment = entry.getValue().getImageEnrichment();
			if (bfImageEnrichment == null) continue;
			String picEnrichmentName = getSavePicEnrichmentName(prix);
			ImageUtils.saveBufferedImage(bfImageEnrichment, picEnrichmentName);
			lsResultPic.add(picEnrichmentName);
		}
	}
	
	public String getSavePicPvalueName(String prefix) {
		return FileOperate.addSep(saveParentPath) + savePrefix + getResultBaseTitle() + "-Log2P."  + prefix + ".png";
	}
	public String getSavePicEnrichmentName(String prefix) {
		return FileOperate.addSep(saveParentPath) + savePrefix + getResultBaseTitle() +  "-Enrichment." + prefix + ".png";
	}
	@Override
	protected void clear() {
		functionTest = FunctionTest.getInstance(FunctionTest.FUNCTION_COG);
	}

	/** 返回文件的名字，用于excel和画图 */
	public String getResultBaseTitle() {
		return cogType + "-Analysis";
	}
	@Override
	public void setTeamName(String teamName) {}

	@Override
	public List<String> getLsResultPic() {
		return lsResultPic;
	}


}
