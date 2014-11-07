package com.novelbio.nbcgui.controltest;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.novelbio.analysis.annotation.functiontest.FunctionTest;
import com.novelbio.analysis.annotation.functiontest.TopGO.GoAlgorithm;
import com.novelbio.base.ExceptionNullParam;
import com.novelbio.base.FoldeCreate;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.multithread.RunProcess.RunThreadStat;
import com.novelbio.base.plot.ImageUtils;
import com.novelbio.database.domain.geneanno.GOtype;
import com.novelbio.database.model.species.Species;
import com.novelbio.nbcReport.Params.EnumReport;
import com.novelbio.nbcReport.Params.ReportGO;
import com.novelbio.nbcReport.report.GOReport;

/** 同时把BP、MF、CC三个类型都做了 */
@Component
@Scope("prototype")
public class CtrlGOall implements CtrlTestGOInt {
	private static Logger logger = Logger.getLogger(CtrlGOall.class);
	public Map<GOtype, CtrlGO> mapGOtype2CtrlGO = new LinkedHashMap<GOtype, CtrlGO>();
	public Map<String, String> mapPrefix2ResultPic = new LinkedHashMap<>();

	GoAlgorithm goAlgorithm;
	int taxID = 0;
	List<Integer> lsBlastTaxID = new ArrayList<Integer>();
	boolean isCluster = false;
	boolean isJustAll = true;
	
	GOReport goReport;
	
	String savePathPrefix;
	String savePrefix = "";
	
	@Override
	public void setTaxID(Species species) {
		for (CtrlGO ctrlGO : mapGOtype2CtrlGO.values()) {
			ctrlGO.setTaxID(species);
		}
		this.taxID = species.getTaxID();
	}
	
	public ReportGO getReportGO() {
		return goReport.getReportGO();
	}
	/** 是否仅检测全体基因，不检测updown等 */
	public boolean isJustAll() {
		return isJustAll;
	}
	
	@Override
	public void setLsAccID2Value(ArrayList<String[]> lsAccID2Value) {
		if (lsAccID2Value != null && lsAccID2Value.size() > 0 && lsAccID2Value.get(0).length > 1) {
			isJustAll = false;
		}
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
		if (goAlgorithm == null) {
			throw new ExceptionNullParam("No GoAlgorithm Exist");
		}
		
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
		//程序是否顺利结束
		for (CtrlGO ctrlGO : mapGOtype2CtrlGO.values()) {
			if (ctrlGO.getRunThreadStat() == RunThreadStat.finishAbnormal) {
				Throwable throwable = ctrlGO.getException();
				if (throwable != null) {
					throw new RuntimeException(throwable);
				} else {
					throw new RuntimeException("unknown error:" + ctrlGO.getGOType().toString());
				}
			}
		}
	}
	
	@Override
	public void saveExcel(String excelPath) {
		savePathPrefix = FoldeCreate.createAndInFold(excelPath, EnumReport.GOAnalysis.getResultFolder());
		if (!savePathPrefix.endsWith("\\") && !savePathPrefix.endsWith("/")) {
			savePrefix = FileOperate.getFileName(savePathPrefix);
		}
		
		for (CtrlGO ctrlGO : mapGOtype2CtrlGO.values()) {
			String saveName;
			if (savePathPrefix.endsWith("\\") || savePathPrefix.endsWith("/")) {
				saveName = savePathPrefix + ctrlGO.getResultBaseTitle() + ".xlsx";
			} else {
				saveName = FileOperate.changeFilePrefix(savePathPrefix, ctrlGO.getResultBaseTitle() + "_", "xlsx");
			}
			ctrlGO.saveExcel(saveName);
		}

		savePic();
		
		goReport = new GOReport();
		goReport.setCtrlGOall(this);
		goReport.generateReport();
	}
	
	/** 获得保存到文件夹的前缀，譬如保存到/home/zong0jie/stage10，那么前缀就是stage10 */
	@Override
	public String getSavePrefix() {
		return savePrefix;
	}

	private void savePic() {
		mapPrefix2ResultPic.clear();
		logger.info("start draw pic");
		for (String prefix : getPrefix()) {
			List<BufferedImage> lsGOimage = new ArrayList<BufferedImage>();
			String excelSavePath = "";
			for (CtrlGO ctrlGO : getMapResult_Prefix2FunTest().values()) {
				FunctionTest functionTest = ctrlGO.getMapResult_Prefix2FunTest().get(prefix);
				if (functionTest == null) {
					continue;
				}
				BufferedImage bufferedImage = functionTest.getImagePvalue();
				lsGOimage.add(bufferedImage);
				excelSavePath = FileOperate.getParentPathNameWithSep(ctrlGO.getSaveExcelPrefix());
			}
			BufferedImage bfImageCombine = ImageUtils.combineBfImage(true, 30, lsGOimage);
			String picNameLog2P = excelSavePath +  "GO-Analysis-Log2P_" + prefix + "_" + getSavePrefix() + ".png";
			logger.info("draw pic:" + picNameLog2P);

			String picName = ImageUtils.saveBufferedImage(bfImageCombine, picNameLog2P);
			if (picName == null) continue;//存储图片失败
			mapPrefix2ResultPic.put(prefix, picName);
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
	
	public void setTeamName(String teamName) {
		
	}
	
	@Override
	public List<String> getLsResultPic() {
		return new ArrayList<>(mapPrefix2ResultPic.values());
	}
	
}
