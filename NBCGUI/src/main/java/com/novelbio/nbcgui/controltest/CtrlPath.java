package com.novelbio.nbcgui.controltest;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.novelbio.analysis.annotation.functiontest.FunctionTest;
import com.novelbio.analysis.annotation.functiontest.TopGO.GoAlgorithm;
import com.novelbio.base.FoldeCreate;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.plot.ImageUtils;
import com.novelbio.nbcReport.XdocTmpltExcel;
import com.novelbio.nbcReport.XdocTmpltPic;
import com.novelbio.nbcReport.Params.EnumReport;
import com.novelbio.nbcReport.Params.ReportPathWay;
@Service
@Scope("prototype")
public class CtrlPath extends CtrlGOPath implements CtrlTestPathInt {
	private static final Logger logger = Logger.getLogger(CtrlPath.class);
	String saveParentPath = "";
	String savePrefix = "";
	ReportPathWay reportPathWay = new ReportPathWay();
	List<String> lsResultPic = new ArrayList<>();

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
	
	public ReportPathWay getReportPathWay() {
		reportPathWay.setDb("KEGG");
		reportPathWay.setFinderCondition(getFinderCondition());
		reportPathWay.setDownRegulation(getUpAndDownRegulation()[0]);
		reportPathWay.setUpRegulation(getUpAndDownRegulation()[1]);
		return reportPathWay;
	}
	
	@Override
	public List<String> saveExcel(String excelPath) {
		List<String> lsResultFile = new ArrayList<>();
		String excelPrefix = FoldeCreate.createAndInFold(excelPath, EnumReport.PathWay.getResultFolder());
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
			XdocTmpltPic xdocTmpltPic = new XdocTmpltPic(picPvalueName);
			xdocTmpltPic.setHeight(600);
			reportPathWay.addXdocTempPic(xdocTmpltPic);
			BufferedImage bfImageEnrichment = entry.getValue().getImageEnrichment();
			if (bfImageEnrichment == null) continue;
			String picEnrichmentName = getSavePicEnrichmentName(prix);
			ImageUtils.saveBufferedImage(bfImageEnrichment, picEnrichmentName);
			lsResultPic.add(picEnrichmentName);
			XdocTmpltPic xdocTmpltPic1 = new XdocTmpltPic(picEnrichmentName);
			xdocTmpltPic1.setHeight(500);
			reportPathWay.addXdocTempPic(xdocTmpltPic1);
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
	@Override
	public void setTeamName(String teamName) {
		reportPathWay.setTeamName(teamName);
	}
	@Override
	public List<String> getLsResultPic() {
		return lsResultPic;
	}
}
