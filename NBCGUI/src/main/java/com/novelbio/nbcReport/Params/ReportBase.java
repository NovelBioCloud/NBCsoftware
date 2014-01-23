package com.novelbio.nbcReport.Params;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.word.NBCWord;

/**
 * 参数对象基本抽象类
 * @author novelbio
 *
 */
public abstract class ReportBase  implements Cloneable, Serializable {
	private static final long serialVersionUID = 1L;
	public static Logger logger = Logger.getLogger(ReportBase.class);
	/**
	 * 所有的参数集合
	 */
	Map<String, Object> mapKey2Param = new HashMap<String, Object>();
	/**
	 * 保存路径
	 */
	protected String savePath;
	/**
	 * 子报告集合
	 */
	protected Map<String,LinkedHashSet<ReportBase>> mapTempName2setReportBase = new HashMap<>();
	/**
	 * 生成最终的报告参数
	 */
	public Map<String, Object> buildFinalParamMap() {
		mapKey2Param.putAll(mapTempName2setReportBase);
		return mapKey2Param;
	}
	
	/**
	 * 得到报告类型
	 * @return
	 */
	public abstract EnumReport getEnumReport();
	
	
	/**
	 * 把对象本身写成二进制文件
	 * @param savePath 保存的路径，会添加.report目录
	 * @return
	 */
	public String writeAsFile(String savePath){
		this.savePath = savePath;
		String reportPath = FileOperate.addSep(savePath) + ".report";
		FileOperate.createFolders(reportPath);
		FileOperate.delAllFile(reportPath);
		String randomReportFile = FileOperate.addSep(reportPath) +  getEnumReport().getReportRandomFileName();
		FileOperate.writeObjectToFile(this, randomReportFile);
		return randomReportFile;
	}
	
	/**
	 * 从文件中反序列化报告对象
	 * @param pathAndName 序列化文件的全路径
	 * @return
	 */
	public static ReportBase readReportFromFile(String pathAndName) {
		try {
			ReportBase reportBase = (ReportBase) FileOperate.readFileAsObject(pathAndName);
			return reportBase;
		} catch (Exception e) {
			logger.error("报告文件 :"+pathAndName +" 序列化失败");
			return null;
		}
	}
	
	/**
	 * 取得克隆的对象
	 */
	public ReportBase getClone() {
		try {
			return (ReportBase) this.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 得到word模板的全路径
	 * @return
	 */
	public String getTempPathAndName(){
		String pathAndName = FileOperate.addSep(getEnumReport().getTempPath())+getEnumReport().getTempName();
		if(!FileOperate.isFileExist(pathAndName))
			return null;
		return pathAndName;
	}
	
	/**
	 * 创建一个npcWord对象用来生成报告
	 */
	public NBCWord getWord() {
		NBCWord word = new NBCWord(getTempPathAndName());
		return word;
	}
	
	/**
	 * 添加子报告
	 * @param reportAll
	 */
	public void addChildReport(ReportBase childReport) {
		String tempName = childReport.getEnumReport().getResultFolder();
		LinkedHashSet<ReportBase> setReportBases = new LinkedHashSet<>();
		if(mapTempName2setReportBase.containsKey(tempName)){
			setReportBases = mapTempName2setReportBase.get(tempName);
			setReportBases.add(childReport);
			return;
		}
		setReportBases.add(childReport);
		mapTempName2setReportBase.put(tempName, setReportBases);
	}
}
