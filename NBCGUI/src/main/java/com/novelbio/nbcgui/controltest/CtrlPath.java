package com.novelbio.nbcgui.controltest;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.novelbio.analysis.annotation.functiontest.FunctionTest;
import com.novelbio.analysis.annotation.functiontest.TopGO.GoAlgorithm;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.plot.ImageUtils;
import com.novelbio.nbcgui.FoldeCreate;
@Service
@Scope("prototype")
public class CtrlPath extends CtrlGOPath implements CtrlTestPathInt {
	private static final Logger logger = Logger.getLogger(CtrlPath.class);
	private static final String pathSaveTo = "Path-Analysis_result";
	String saveParentPath = "";
	String savePrefix = "";
	
	/** @param QtaxID */
	public CtrlPath() {
		functionTest = FunctionTest.getInstance(FunctionTest.FUNCTION_PATHWAY_KEGG);
	}
	/** 不需要该参数 */
	public GoAlgorithm getGoAlgorithm() {
		return GoAlgorithm.novelgo;
	}
	@Override
	protected void copeFile(String prix, String excelPath) {
	}
	@Override
	String getGene2ItemFileName(String fileName) {
		String suffix = "_Path_Item";
		List<Integer> blastTaxID = functionTest.getBlastTaxID();
		if (functionTest.isBlast()) {
			suffix = suffix + "_blast";
			Collections.sort(blastTaxID);//排个序
			for (int i : blastTaxID) {
				suffix = suffix + "_" + i;
			}
		}
		return FileOperate.changeFileSuffix(fileName, suffix, "txt");
	}
	
	public void saveExcel(String excelPath) {
		String excelPrefix = FoldeCreate.createAndInFold(excelPath, pathSaveTo);
		if (excelPrefix.endsWith("\\") || excelPrefix.endsWith("/")) {
			saveParentPath = excelPrefix;
		} else {
			saveParentPath = FileOperate.getParentPathName(excelPrefix);
			savePrefix = FileOperate.getFileName(excelPath);
		}
		
		if (excelPrefix.endsWith("\\") || excelPrefix.endsWith("/")) {
			saveExcelPrefix = excelPrefix + getResultBaseTitle() + ".xls";
		} else {
			saveExcelPrefix = FileOperate.changeFilePrefix(excelPrefix, getResultBaseTitle() + "_", "xls");
		}
		if (isCluster) {
			saveExcelCluster(saveExcelPrefix);
		} else {
			saveExcelNorm(saveExcelPrefix);
		}
		savePic();
	}
	
	public void savePic() {
		for (Entry<String, FunctionTest> entry : getMapResult_Prefix2FunTest().entrySet()) {
			String prix = entry.getKey();
			BufferedImage bfImageLog2Pic = entry.getValue().getImagePvalue();;
			if (bfImageLog2Pic == null) continue;
			ImageUtils.saveBufferedImage(bfImageLog2Pic, getSavePicPvalueName(prix));
			
			BufferedImage bfImageEnrichment = entry.getValue().getImageEnrichment();
			if (bfImageEnrichment == null) continue;
			ImageUtils.saveBufferedImage(bfImageEnrichment, getSavePicEnrichmentName(prix));
		}
	}
	
	public String getSavePicPvalueName(String prefix) {
		return FileOperate.addSep(getSaveParentPath()) + "Path-Analysis-Log2P_" + prefix + "_" + getSavePrefix() + ".png";
	}
	public String getSavePicEnrichmentName(String prefix) {
		return FileOperate.addSep(getSaveParentPath()) + "Path-Analysis-Enrichment_" + prefix + "_" + getSavePrefix() + ".png";
	}
	@Override
	protected void clear() {
		functionTest = FunctionTest.getInstance(FunctionTest.FUNCTION_PATHWAY_KEGG);
	}
	
	/** 获得保存到文件夹的前缀，譬如保存到/home/zong0jie/stage10，那么前缀就是stage10 */
	public String getSavePrefix() {
		return savePrefix;
	}
	/** 获得保存到的文件夹路径 */
	public String getSaveParentPath() {
		return saveParentPath;
	}
	/** 返回文件的名字，用于excel和画图 */
	public String getResultBaseTitle() {
		return "Pathway-Analysis";
	}
}
