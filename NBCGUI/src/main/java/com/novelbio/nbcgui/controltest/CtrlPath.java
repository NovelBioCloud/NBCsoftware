package com.novelbio.nbcgui.controltest;

import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.novelbio.analysis.annotation.functiontest.FunctionTest;
import com.novelbio.analysis.annotation.functiontest.FunctionTest.FunctionDrawResult;
import com.novelbio.analysis.annotation.functiontest.TopGO.GoAlgorithm;
import com.novelbio.base.StringOperate;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.plot.ImageUtils;
@Service
@Scope("prototype")
public class CtrlPath extends CtrlGOPath implements CtrlTestPathInt {
	private static final Logger logger = LoggerFactory.getLogger(CtrlPath.class);
	String saveParentPath;
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
	protected void copeFile(String prix, String excelPath) { }
	
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
		String bgName = FileOperate.changeFileSuffix(fileName, suffix, "txt");
		bgName = FileOperate.changeFilePrefix(bgName, ".", null);
		return bgName;
	}
	
	@Override
	public void setSavePathPrefix(String excelPath) {
		if (excelPath.endsWith("\\") || excelPath.endsWith("/")) {
			saveParentPath = excelPath;
		} else {
			saveParentPath = FileOperate.getParentPathNameWithSep(excelPath);
			savePrefix = FileOperate.getFileName(excelPath) + ".";
		}
		String saveExcelPrefix;
		if (excelPath.endsWith("\\") || excelPath.endsWith("/")) {
			saveExcelPrefix = excelPath + getResultBaseTitle() + ".xlsx";
		} else {
			saveExcelPrefix = FileOperate.changeFileSuffix(excelPath, "." + getResultBaseTitle(), "xlsx");
		}
		setSaveExcelPrefix(saveExcelPrefix);
	}
	
	public void running() {
		super.running();
		savePic();
	}
	
	private void savePic() {
		for (Entry<String, FunctionDrawResult> entry : getMapPrefix2FunDrawTest().entrySet()) {
			String prix = entry.getKey();
			BufferedImage bfImageLog2Pic = entry.getValue().getImagePvalue();;
			if (bfImageLog2Pic == null) continue;
			String picPvalueName = getSavePicPvalueName(prix);
			ImageUtils.saveBufferedImage(bfImageLog2Pic,picPvalueName );
//			reportPathWay.addXdocTempPic(xdocTmpltPic);
			BufferedImage bfImageEnrichment = entry.getValue().getImageEnrichment();
			if (bfImageEnrichment == null) continue;
			String picEnrichmentName = getSavePicEnrichmentName(prix);
			ImageUtils.saveBufferedImage(bfImageEnrichment, picEnrichmentName);
//			reportPathWay.addXdocTempPic(xdocTmpltPic1);
		}
	}
	
	public String getSavePicPvalueName(String prefix) {
		return FileOperate.addSep(getSaveParentPath()) + savePrefix + "Path-Analysis-Log2P."  + prefix + ".png";
	}
	public String getSavePicEnrichmentName(String prefix) {
		return FileOperate.addSep(getSaveParentPath()) + savePrefix + "Path-Analysis-Enrichment." + prefix + ".png";
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
	@Override
	public void setTeamName(String teamName) {
//		reportPathWay.setTeamName(teamName);
	}

}
