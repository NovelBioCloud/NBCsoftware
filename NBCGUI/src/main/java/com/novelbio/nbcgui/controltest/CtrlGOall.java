package com.novelbio.nbcgui.controltest;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.novelbio.analysis.annotation.functiontest.FunctionTest;
import com.novelbio.analysis.annotation.functiontest.TopGO.GoAlgorithm;
import com.novelbio.base.FoldeCreate;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.plot.ImageUtils;
import com.novelbio.database.domain.geneanno.GOtype;
import com.novelbio.nbcReport.XdocTmpltExcel;
import com.novelbio.nbcReport.XdocTmpltPic;
import com.novelbio.nbcReport.Params.EnumReport;
import com.novelbio.nbcReport.Params.ReportGO;

/** 同时把BP、MF、CC三个类型都做了 */
@Component
@Scope("prototype")
public class CtrlGOall implements CtrlTestGOInt {
	
	Map<GOtype, CtrlGO> mapGOtype2CtrlGO = new LinkedHashMap<GOtype, CtrlGO>();
	/** 结果文件列表 */
	Map<GOtype, List<String>> mapGoType2File = new HashMap<>();
	List<String> lsResultPic = new ArrayList<>();

	GoAlgorithm goAlgorithm;
	int taxID = 0;
	List<Integer> lsBlastTaxID = new ArrayList<Integer>();
	boolean isCluster = false;
	ReportGO reportGO = new ReportGO();
	String savePathPrefix;
	String savePrefix = "";
	
	@Override
	public void setTaxID(int taxID) {
		for (CtrlGO ctrlGO : mapGOtype2CtrlGO.values()) {
			ctrlGO.setTaxID(taxID);
		}
		this.taxID = taxID;
	}
	
	public ReportGO getReportGO() {
		for (CtrlGO ctrlGO : mapGOtype2CtrlGO.values()) {
			reportGO.setFinderCondition(ctrlGO.getFinderCondition());
			reportGO.setUpRegulation(ctrlGO.getUpAndDownRegulation()[0]);
			reportGO.setDownRegulation(ctrlGO.getUpAndDownRegulation()[1]);
			reportGO.setTestMethod(ctrlGO.getTestMethod());
		}
		return reportGO;
	}
	
	@Override
	public void setLsAccID2Value(ArrayList<String[]> lsAccID2Value) {
		for (CtrlGO ctrlGO : mapGOtype2CtrlGO.values()) {
			ctrlGO.setLsAccID2Value(lsAccID2Value);
		}
	}

	@Override
	public void setUpDown(double up, double down) {
		for (CtrlGO ctrlGO : mapGOtype2CtrlGO.values()) {
			ctrlGO.setUpDown(up, down);
		}
	}

	@Override
	public void setBlastInfo(double blastevalue, List<Integer> lsBlastTaxID) {
		for (CtrlGO ctrlGO : mapGOtype2CtrlGO.values()) {
			ctrlGO.setBlastInfo(blastevalue, lsBlastTaxID);
		}
		this.lsBlastTaxID = lsBlastTaxID;
	}
	
	/** 只能是最初的一列基因那个BG文件，不能是gene_P_Item那种文件 */
	@Override
	public void setLsBG(String fileName) {
		for (CtrlGO ctrlGO : mapGOtype2CtrlGO.values()) {
			ctrlGO.setLsBG(fileName);
		}
	}

	@Override
	public void setIsCluster(boolean isCluster) {
		this.isCluster = isCluster;
		for (CtrlGO ctrlGO : mapGOtype2CtrlGO.values()) {
			ctrlGO.setIsCluster(isCluster);
		}
	}
	
	@Override
	public boolean isCluster() {
		return isCluster;
	}
	
	@Override
	public void setGoAlgorithm(GoAlgorithm goAlgorithm) {
		CtrlGO ctrlGO = new CtrlGO();
		ctrlGO.setGoAlgorithm(goAlgorithm);
		ctrlGO.setGOType(GOtype.BP);
		mapGOtype2CtrlGO.put(GOtype.BP, ctrlGO);
		
		ctrlGO = new CtrlGO();
		ctrlGO.setGoAlgorithm(goAlgorithm);
		ctrlGO.setGOType(GOtype.MF);
		mapGOtype2CtrlGO.put(GOtype.MF, ctrlGO);
		
		ctrlGO = new CtrlGO();
		ctrlGO.setGoAlgorithm(goAlgorithm);
		ctrlGO.setGOType(GOtype.CC);
		mapGOtype2CtrlGO.put(GOtype.CC, ctrlGO);

		this.goAlgorithm = goAlgorithm;
	}

	@Override
	public GoAlgorithm getGoAlgorithm() {
		return goAlgorithm;
	}

	@Override
	public void setGOlevel(int levelNum) {
		for (CtrlGO ctrlGO : mapGOtype2CtrlGO.values()) {
			ctrlGO.setGOlevel(levelNum);
		}
	}

	@Override
	public void clearParam() {
		for (CtrlGO ctrlGO : mapGOtype2CtrlGO.values()) {
			ctrlGO.clearParam();
		}
	}

	@Override
	public void run() {
		List<Thread> lsThreads = new ArrayList<Thread>();
		for (CtrlGO ctrlGO : mapGOtype2CtrlGO.values()) {
			Thread thread = new Thread(ctrlGO);
			thread.start();
			lsThreads.add(thread);
		}
		for (Thread thread : lsThreads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void saveExce(String excelPath) {
		mapGoType2File.clear();
		savePathPrefix = FoldeCreate.createAndInFold(excelPath, EnumReport.GOAnalysis.getResultFolder());
		if (!savePathPrefix.endsWith("\\") && !savePathPrefix.endsWith("/")) {
			savePrefix = FileOperate.getFileName(savePathPrefix);
		}
		
		for (CtrlGO ctrlGO : mapGOtype2CtrlGO.values()) {
			String saveName;
			if (savePathPrefix.endsWith("\\") || savePathPrefix.endsWith("/")) {
				saveName = savePathPrefix + ctrlGO.getResultBaseTitle() + ".xls";
			} else {
				saveName = FileOperate.changeFilePrefix(savePathPrefix, ctrlGO.getResultBaseTitle() + "_", "xls");
			}
			mapGoType2File.put(ctrlGO.getGOType(), ctrlGO.getLsResultExcel());
			for(XdocTmpltExcel xdocTmpltExcel : ctrlGO.saveExcel(saveName)){
				for (String excelFile : xdocTmpltExcel.getAllExcelFileName()) {
					reportGO.addResultFile(excelFile);
				}
			}
		}
		savePic();
	}
	
	/** 获得保存到文件夹的前缀，譬如保存到/home/zong0jie/stage10，那么前缀就是stage10 */
	@Override
	public String getSavePrefix() {
		return savePrefix;
	}

	private void savePic() {
		lsResultPic.clear();
		for (String prefix : getPrefix()) {
			List<BufferedImage> lsGOimage = new ArrayList<BufferedImage>();
			String excelSavePath = "";
			for (CtrlGO ctrlGO : getMapResult_Prefix2FunTest().values()) {
				BufferedImage bufferedImage = ctrlGO.getMapResult_Prefix2FunTest().get(prefix).getImagePvalue();
				lsGOimage.add(bufferedImage);
				excelSavePath = FileOperate.getParentPathName(ctrlGO.getSaveExcelPrefix());
			}
			BufferedImage bfImageCombine = ImageUtils.combineBfImage(true, 30, lsGOimage);
			String picNameLog2P = excelSavePath +  "GO-Analysis-Log2P_" + prefix + "_" + getSavePrefix() + ".png";
			
			String picName = ImageUtils.saveBufferedImage(bfImageCombine, picNameLog2P);
			if (picName != null) {
				reportGO.addResultFile(picName);
				XdocTmpltPic xdocTmpltPic = new XdocTmpltPic(picName);
				//图片的宽度和描述都可以在这里设置
				reportGO.addXdocTempPic(xdocTmpltPic);
				lsResultPic.add( picName);
			}
		}
	}
	
	/** 将本次GO分析的前缀全部抓出来，方便画图 */
	private Set<String> getPrefix() {
		Set<String> setPrefix = new LinkedHashSet<String>();
		for (CtrlGO ctrlGO : getMapResult_Prefix2FunTest().values()) {
			Map<String, FunctionTest> map = ctrlGO.getMapResult_Prefix2FunTest();
			for (String prefix : map.keySet()) {
				setPrefix.add(prefix);
			}
		}
		return setPrefix;
	}

	@Override
	public int getTaxID() {
		return taxID;
	}

	@Override
	public List<Integer> getBlastTaxID() {
		return lsBlastTaxID;
	}

	@Override
	public Map<GOtype, CtrlGO> getMapResult_Prefix2FunTest() {
		return mapGOtype2CtrlGO;
	}

	@Override
	public String getResultBaseTitle() {
		return "GO-Analysis";
	}
	
	public void setTeamName(String teamName){
		reportGO.setTeamName(teamName);
	}
	
	@Override
	public Map<GOtype, List<String>> getMapGoType2File() {
		return mapGoType2File;
	}
	@Override
	public List<String> getLsResultPic() {
		return lsResultPic;
	}
}
