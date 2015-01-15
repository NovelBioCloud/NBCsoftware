package com.novelbio.report.Params;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.novelbio.database.domain.information.SoftWareInfo.SoftWare;
import com.novelbio.database.model.species.Species;
/**
 * 报告总类
 * @author Administrator
 *
 */
public class ReportAll extends ReportBase {
	private static final long serialVersionUID = 9094926973403951101L;
	public static final Logger logger = Logger.getLogger(ReportAll.class);
	
	/** 为项目报告添加名称 */
	public void setProjectName(String projectName) {
		mapKey2Param.put("projectName", projectName);
	}

	/** 设置物种信息 */
	public void setSpeciesName(Species species) {
		String name = species.getNameLatin();
		String[] ss = name.split(" ");
		if (ss.length > 2) {
			name = ss[0] + " " + ss[1];
		}
		mapKey2Param.put("speciesName", name);
	}
	
	/** 设置合同编号 */
	public void setContractId(String contractId) {
		mapKey2Param.put("contractId", contractId);
	}
	
	/** 设置客户信息 */
	public void setClientInfo(String clientInfo) {
		mapKey2Param.put("clientInfo", clientInfo);
	}
	
	/** 设置项目时间 */
	public void setProjectDate(String projectDate) {
		mapKey2Param.put("projectDate", projectDate);
	}
	
	/** 设置项目编号 */
	public void setProjectCode(String projectCode) {
		mapKey2Param.put("projectCode", projectCode);
	}
	
	/** 设置报告标题 */
	public void setReportTitle(String reportTitle) {
		mapKey2Param.put("reportTitle", reportTitle);
	}
	
	/** 设置完成时间 */
	public void setCompletedDate(String completedDate) {
		mapKey2Param.put("completedDate", completedDate);
	}
	
	/** 设置开始写报告的时间 */
	public void setDrafterDate(String drafterDate) {
		mapKey2Param.put("drafterDate", drafterDate);
	}
	
	/** 设置质量控制时间 */
	public void setQualityCtrlDate(String qualityCtrlDate) {
		mapKey2Param.put("qualityCtrlDate", qualityCtrlDate);
	}
	
	/** 设置研究时间 */
	public void setStudyDate(String studyDate) {
		mapKey2Param.put("studyDate", studyDate);
	}
	
	/** 设置监督时间 */
	public void setDirectorDate(String directorDate) {
		mapKey2Param.put("directorDate", directorDate);
	}
	
	/** 添加实验信息 */
	public void addExperimentInfo(Map<String, Object> mapKey2ExperimentInfo) {
		List<Map<String, Object>> lsExperimentInfo = null;
		if (mapKey2Param.containsKey("lsExperimentInfo")) {
			lsExperimentInfo = (List<Map<String, Object>>) mapKey2Param.get("lsExperimentInfo");
		} else {
			lsExperimentInfo = new ArrayList<Map<String,Object>>();
		}
		lsExperimentInfo.add(mapKey2ExperimentInfo);
		mapKey2Param.put("lsExperimentInfo", lsExperimentInfo);
	}
	
	/** 添加样本信息 */
	public void addSampleInfo(Map<String, Object> mapKey2SampleInfo) {
		List<Map<String, Object>> lsSampleInfo = null;
		if (mapKey2Param.containsKey("lsSampleInfo")) {
			lsSampleInfo = (List<Map<String, Object>>) mapKey2Param.get("lsSampleInfo");
		} else {
			lsSampleInfo = new ArrayList<Map<String,Object>>();
		}
		lsSampleInfo.add(mapKey2SampleInfo);
		mapKey2Param.put("lsSampleInfo", lsSampleInfo);
	}
	
	/** 添加质量控制信息 */
	public void addQCSample(Map<String, Object> mapKey2QCSample) {
		List<Map<String, Object>> lsQCSample = null;
		if (mapKey2Param.containsKey("lsQCSample")) {
			lsQCSample = (List<Map<String, Object>>) mapKey2Param.get("lsQCSample");
		} else {
			lsQCSample = new ArrayList<Map<String,Object>>();
		}
		lsQCSample.add(mapKey2QCSample);
		mapKey2Param.put("lsSampleInfo", lsQCSample);
	}
	
	/** 设置所用软件信息 */
	public void setSoftware(SoftWare softWare) {
		mapKey2Param.put("software", softWare);
	}
	
	/** 设置算法信息 */
	public void setAlgrithm(String algrithm) {
		mapKey2Param.put("algrithm", algrithm);
	}
	
	/** 设置blast的物种信息 */
	public void setBlastToSpecies(List<Species> lsBlastToSpecies) {
		int i = 0;
		String blastToSpeciesName = "";
		for (Species species : lsBlastToSpecies) {
			if (i == 0) {
				blastToSpeciesName = species.getCommonName();
			} else {
				blastToSpeciesName = blastToSpeciesName + " and " + species.getCommonName();
			}
			i++;
		}
		mapKey2Param.put("blastSpeciesName", blastToSpeciesName);
	}
	
	public void setSamples(String sample1, String sample2, String sample3) {
		String samples = sample1 + "," + sample2 + " and " + sample3;
		mapKey2Param.put("samples", samples);
	}
	
	/** 设置平台信息 */
	public void setSequence(String sequence) {
		mapKey2Param.put("sequence", sequence);
	}
	
	@Override
	public EnumReport getEnumReport() {
		return EnumReport.ReportAll;
	}


}
