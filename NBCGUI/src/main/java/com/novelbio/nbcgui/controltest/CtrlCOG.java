package com.novelbio.nbcgui.controltest;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.novelbio.analysis.annotation.cog.COGanno;
import com.novelbio.analysis.annotation.functiontest.CogFunTest;
import com.novelbio.analysis.annotation.functiontest.FunctionTest;
import com.novelbio.base.FoldeCreate;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.plot.ImageUtils;
import com.novelbio.nbcReport.Params.EnumReport;
@Service
@Scope("prototype")
public class CtrlCOG extends CtrlGOPath implements CtrlTestCOGInt {
	private static final Logger logger = Logger.getLogger(CtrlCOG.class);
	String saveParentPath = "";
	String savePrefix = "";
	List<String> lsResultPic = new ArrayList<>();

	/** @param QtaxID */
	public CtrlCOG() {
		functionTest = FunctionTest.getInstance(FunctionTest.FUNCTION_COG);
	}
	/** COG没有blast */
	@Deprecated
	public void setBlastInfo(double blastevalue, List<Integer> lsBlastTaxID) {
	}
	@Override
	protected void copeFile(String prix, String excelPath) { }
	
	@Override
	String getGene2ItemFileName(String fileName) {
		String suffix = "_COG_Item";
		String bgName = FileOperate.changeFileSuffix(fileName, suffix, "txt");
		bgName = FileOperate.changeFilePrefix(bgName, ".", null);
		return bgName;
	}
	
	public void setCogAnno(COGanno cogAnno) {
		((CogFunTest)functionTest).setCogAnno(cogAnno);
	}
	
	@Override
	public List<String> saveExcel(String excelPath) {
		List<String> lsResultFile = new ArrayList<>();
		String excelPrefix = FoldeCreate.createAndInFold(excelPath, EnumReport.COG.getResultFolder());
		if (excelPrefix.endsWith("\\") || excelPrefix.endsWith("/")) {
			saveParentPath = excelPrefix;
		} else {
			saveParentPath = FileOperate.getParentPathNameWithSep(excelPrefix);
			savePrefix = FileOperate.getFileName(excelPath);
		}
		
		if (excelPrefix.endsWith("\\") || excelPrefix.endsWith("/")) {
			saveExcelPrefix = excelPrefix + getResultBaseTitle() + ".xls";
		} else {
			saveExcelPrefix = FileOperate.changeFilePrefix(excelPrefix, getResultBaseTitle() + "_", "xls");
		}
		if (isCluster) {
			lsResultFile =  saveExcelCluster(saveExcelPrefix);
		} else {
			lsResultFile =  saveExcelNorm(saveExcelPrefix);
		}
		savePic();
		return lsResultFile;
	}
	
	private void savePic() {
		lsResultPic.clear();
		for (Entry<String, FunctionTest> entry : getMapResult_Prefix2FunTest().entrySet()) {
			String prix = entry.getKey();
			BufferedImage bfImageLog2Pic = entry.getValue().getImagePvalue();;
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
		return FileOperate.addSep(getSaveParentPath()) + "COG-Analysis-Log2P_" + prefix + "_" + getSavePrefix() + ".png";
	}
	public String getSavePicEnrichmentName(String prefix) {
		return FileOperate.addSep(getSaveParentPath()) + "COG-Analysis-Enrichment_" + prefix + "_" + getSavePrefix() + ".png";
	}
	@Override
	protected void clear() {
		functionTest = FunctionTest.getInstance(FunctionTest.FUNCTION_COG);
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
		return "COG-Analysis";
	}
	@Override
	public void setTeamName(String teamName) {}

	@Override
	public List<String> getLsResultPic() {
		return lsResultPic;
	}

}
