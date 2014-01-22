package com.novelbio.nbcReport.Params;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.word.NBCWord;

/**
 * 参数对象基本抽象类
 * @author novelbio
 *
 */
public abstract class ReportBase  implements Cloneable, Serializable {
	private static final long serialVersionUID = 1L;
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
	public boolean writeAsFile(String savePath){
		this.savePath = savePath;
		String reportPath = FileOperate.addSep(savePath) + FileOperate.getSepPath() + ".report";
		FileOperate.createFolders(reportPath);
		FileOperate.delAllFile(reportPath);
		String randomReportFile = FileOperate.addSep(reportPath) +  getEnumReport().getReportRandomFileName();
		FileOperate.writeObjectToFile(this, randomReportFile);
		return true;
	}
	
	/**
	 * 从文件中反序列化对象并添加到reportAll中,会自动到savePath下的.report文件夹找所有的序列化文件
	 * @return
	 */
	public abstract boolean readReportFromFile(String savePath);
	
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
		String tempName = childReport.getEnumReport().getTempName();
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
