package com.novelbio.nbcgui.controltest;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;

import com.novelbio.analysis.annotation.cog.COGanno;
import com.novelbio.analysis.annotation.cog.EnumCogType;
import com.novelbio.analysis.annotation.functiontest.TopGO.GoAlgorithm;
import com.novelbio.base.StringOperate;
import com.novelbio.base.dataOperate.ExcelTxtRead;
import com.novelbio.base.dataStructure.ArrayOperate;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.database.domain.species.Species;
import com.novelbio.database.service.SpringFactoryBioinfo;

public class GoPathRun {
	private static final Logger logger = Logger.getLogger(GoPathRun.class);
	private static final String GO = "GO";
	private static final String Pathway = "Pathway";
	private static final String COG = "COG";
	private static final String GO_PATH = "COG";
	private static final String ALL = "COG";

	
	Species species;
	
	 
	int taxId;
	String version;
	String dbType;
	String analysisType;
	String goAnnoFile = "";
	boolean isCombine;
	String bgGene;
	String outPath;
	boolean isBlast;
	String blastTaxIds;
	GoAlgorithm goAlgorithm;
	String cogType;
	String cogQueryFa;
	List<String> excelFiles;
	List<String> excelPrefixs;
	List<String> lsBlastTaxIds;

	int colAccID;
	int colFC;
	
	boolean isGoLevel;
	int goLevelNum;
	boolean isClusterGoPath;
	
	int upValue;
	int downValue;
	
	public static void main(String[] args) {
		Options opts = new Options();
		opts.addOption("taxId", true, "0");
		opts.addOption("version", true, "");
		opts.addOption("dbType", true, "");
		opts.addOption("appMainClass", true, "the main class of the app");
		opts.addOption("taskId", true, "taskId");
		opts.addOption("dockerImg", true, "dockerImg");
		CommandLine cliParser = null;
		try {
			cliParser = new GnuParser().parse(opts, args);
		} catch (Exception e) {
			LOG.error("error params:" + ArrayOperate.cmbString(args, " "));
			System.exit(1);
		}
		int containerNum = Integer.parseInt(cliParser.getOptionValue("numContainers", 1 + ""));
		if (containerNum <= 0)
			containerNum = 4;

		int priority = Integer.parseInt(cliParser.getOptionValue("priority", 0 + ""));
		String appPackage = cliParser.getOptionValue("appPackage", "");
		String appMainClass = cliParser.getOptionValue("appMainClass", "");
		String taskId = cliParser.getOptionValue("taskId");
		String dockerImg = cliParser.getOptionValue("dockerImg");
	}
	
	public void initial(String version, String DBtype, String analysisType, int taxId,
			String goAnnoFile, boolean isCombine, String bgGene, String outPath,
			boolean isBlast, String blastTaxIds) {
		this.version = version;
		this.dbType = DBtype;
		this.analysisType = analysisType;
		this.taxId = taxId;
		this.goAnnoFile = goAnnoFile;
		this.isCombine = isCombine;
		this.bgGene = bgGene;
		this.outPath = outPath;
		this.isBlast = isBlast;
		this.blastTaxIds = blastTaxIds;
	}
	public void setCogQueryFa(String cogQueryFa) {
		this.cogQueryFa = cogQueryFa;
	}
	public void setGoAlgorithm(String goAlgorithmStr) {
		goAlgorithm = GoAlgorithm.valueOf(goAlgorithmStr);
	}
	public void setCogType(String cogType) {
		this.cogType = cogType;
	}
	public void setCol(int colAccId, int colFc) {
		this.colAccID = colAccId;
		this.colFC = colFc;
	}
	public void setInFile(String prefixs, String infiles) {
		excelPrefixs = new ArrayList<>();
		for (String prefix : prefixs.split(",")) {
			excelPrefixs.add(prefix);
		}
		
		excelFiles = new ArrayList<>();
		for (String file : infiles.split(",")) {
			excelFiles.add(file);
		}
	}
	
	protected void running() {
		species = new Species(taxId);
		if (taxId != 0) {
			species.setVersion(version);
			species.setGffDB(dbType);
		}
		
		String resultPathGO = outPath + "/GOAnalysis_result";
		String resultPathPath = outPath + "/PathWayAnalysis_result"; 
		String resultPathCOG = outPath + "/COGAnalysis_result"; 

		FileOperate.deleteFileFolder(FileOperate.getPathName(resultPathGO));
		FileOperate.deleteFileFolder(FileOperate.getPathName(resultPathPath));
		FileOperate.deleteFileFolder(FileOperate.getPathName(resultPathCOG));

		if (analysisType == GO) {
			runGOPath( AnalysisType.GO, 100);
		} else if (analysisType == Pathway) {
			runGOPath( AnalysisType.Path, 100);
		} else if (analysisType == GO_PATH) {
			runGOPath( AnalysisType.GO, 50);
			runGOPath( AnalysisType.Path, 100);
		} else if (analysisType == COG) {
			runGOPath( AnalysisType.COG, 100);
		} else if (analysisType == ALL) {
			runGOPath( AnalysisType.GO, 30);
			runGOPath( AnalysisType.Path, 60);
			runGOPath( AnalysisType.COG, 100);
		}
	}
	
	private void runGOPath(AnalysisType analysisType, int progressSum) {
		if (analysisType == AnalysisType.GO) {
			FileOperate.deleteFileFolder(outPath + "/GOAnalysis_result");
		} else if(analysisType == AnalysisType.Path) {
			FileOperate.deleteFileFolder(outPath + "/PathWayAnalysis_result");
		} else if(analysisType == AnalysisType.COG) {
			FileOperate.deleteFileFolder(outPath + "/COGAnalysis_result");
		}
		
		if (excelFiles.size() != excelPrefixs.size()) {
			throw new RuntimeException("GO Pathway error: Input file number is " + excelFiles.size() + " not equal to prefix number: " + excelPrefixs.size());
		}
		for (int i = 0; i < excelFiles.size(); i++) {
			String file = excelFiles.get(i);
			String prefix = excelPrefixs.get(i);
			if (analysisType == AnalysisType.GO) {
				runGO(file, goAnnoFile, isCombine, prefix);
			} else if(analysisType == AnalysisType.Path) {
				runPath(file, goAnnoFile, isCombine, prefix);
			} else if(analysisType == AnalysisType.COG) {
				runCog(file, goAnnoFile, isCombine, prefix);
			}
		}
	}
	
	/** 删除任务中的样本时对应的结果文件的删除 */
	protected void rmResultFile(String savePath, String outPath, List<String> lsPrefix) {
	}
	private void runGO(String excelFile, String goAnnoFile, boolean isCombine, String excelPrefix) {
		String resultPath = outPath + "/GOAnalysis_result/";
		FileOperate.createFolders(resultPath);
		
		logger.info("Start run Go : " + excelPrefix);
		CtrlGOall ctrlGO = (CtrlGOall) SpringFactoryBioinfo.getFactory().getBean("ctrlGOall");
		ctrlGO.clearParam();
		ctrlGO.setGoAlgorithm(goAlgorithm);
		ctrlGO.setTaxID(species.getTaxID());
		List<String[]> lsAccID = null;
		if (colAccID != colFC) {
			lsAccID = ExcelTxtRead.readLsExcelTxt(excelFile, new int[]{colAccID, colFC}, 1, 0);
		} else {
			lsAccID = ExcelTxtRead.readLsExcelTxt(excelFile, new int[]{colAccID}, 1, 0);
		}
		boolean isGeneId = isGeneId(lsAccID.get(0)[0]);
		if (isGeneId) {
			lsAccID = lsAccID.subList(1, lsAccID.size());
		}
		if (isBlast) {
			double evalue = 1e-10;
			List<Integer> lsStaxID = new ArrayList<Integer>();
			for (String taxIDString : lsBlastTaxIds) {
				lsStaxID.add(Integer.parseInt(taxIDString));
			}
			ctrlGO.setBlastInfo(evalue, lsStaxID);
		}
		if (isGoLevel) {
			ctrlGO.setGOlevel(goLevelNum);
		} else {
			ctrlGO.setGOlevel(-1);
		}
		if (isClusterGoPath || colAccID == colFC) {
			double up = 0; double down = 0;
			if ( colAccID != colFC) {
				up = upValue;
				down = downValue;
			}
			ctrlGO.setUpDown(up, down);
			ctrlGO.setIsCluster(false);
		} else {
			ctrlGO.setIsCluster(isClusterGoPath);
		}
		if (!StringOperate.isRealNull(goAnnoFile)) {
			ctrlGO.setGOanno(goAnnoFile, isCombine);
		}
		if (!StringOperate.isRealNull(bgGene)) {
			ctrlGO.setLsBG(bgGene);
		} else {
			ctrlGO.setLsBG(species);
		}
		ctrlGO.setIsGeneId(isGeneId);
		ctrlGO.setLsAccID2Value(lsAccID);
		ctrlGO.setTeamName(excelPrefix);
		ctrlGO.setSavePathPrefix(resultPath);
		ctrlGO.run();
	}
	
	private void runPath(String excelFile, String goAnnoFile, boolean isCombine, String excelPrefix) {
		String resultPath = outPath + "/PathWayAnalysis_result/";
		FileOperate.createFolders(resultPath);

		logger.info("Start run Path : " + excelPrefix);
		List<String[]> lsAccID = null;
		if (colAccID != colFC) {
			lsAccID = ExcelTxtRead.readLsExcelTxt(excelFile, new int[]{colAccID, colFC}, 1, 0);
		} else {
			lsAccID = ExcelTxtRead.readLsExcelTxt(excelFile, new int[]{colAccID}, 1, 0);
		}
		boolean isGeneId = isGeneId(lsAccID.get(0)[0]);
		if (isGeneId) {
			lsAccID = lsAccID.subList(1, lsAccID.size());
		}
		double evalue = 1e-10;
		List<Integer> lsStaxID = new ArrayList<Integer>();
		if (isBlast) {
			for (String taxIDString : lsBlastTaxIds) {
				lsStaxID.add(Integer.parseInt(taxIDString));
			}
		}
		CtrlPath ctrlPath = (CtrlPath) SpringFactoryBioinfo.getFactory().getBean("ctrlPath");
		ctrlPath.clearParam();
		ctrlPath.setTaxID(species.getTaxID());
		ctrlPath.setBlastInfo(evalue, lsStaxID);
		if (!StringOperate.isRealNull(goAnnoFile)) {
			ctrlPath.setGene2itemAnnoFile(goAnnoFile, isCombine);
		}
		if (!StringOperate.isRealNull(bgGene)) {
			ctrlPath.setLsBG(bgGene);
		} else {
			ctrlPath.setLsBG(species);
		}
		if (!isClusterGoPath || colAccID == colFC) {
			double up = 0; double down = 0;
			if ( colAccID != colFC) {
				up = upValue;
				down = downValue;
			}
			ctrlPath.setUpDown(up, down);
			ctrlPath.setIsCluster(false);
		} else {
			ctrlPath.setIsCluster(isClusterGoPath);
		}
		ctrlPath.setIsGeneId(isGeneId);
		ctrlPath.setLsAccID2Value(lsAccID);
		ctrlPath.setTeamName(excelPrefix);
		ctrlPath.setSavePathPrefix(resultPath);
		ctrlPath.run();
	}
	
	private void runCog(String excelFile, String goAnnoFile, boolean isCombine, String excelPrefix) {
		String resultPath = outPath + "/COGAnalysis_result/";
		FileOperate.createFolders(resultPath);
		
		logger.info("Start run Cog : " + excelPrefix);
		EnumCogType enumCogType = EnumCogType.valueOf(cogType);
		ArrayList<String[]> lsAccID = null;
		if (colAccID != colFC) {
			lsAccID = ExcelTxtRead.readLsExcelTxt(excelFile, new int[]{colAccID, colFC}, 1, 0);
		} else {
			lsAccID = ExcelTxtRead.readLsExcelTxt(excelFile, new int[]{colAccID}, 1, 0);
		}
		double evalue = 1e-5;
		List<Integer> lsStaxID = new ArrayList<Integer>();
		if (isBlast) {
			for (String taxIDString : lsBlastTaxIds) {
				lsStaxID.add(Integer.parseInt(taxIDString));
			}
		}
		CtrlCOG ctrlCOG = (CtrlCOG)SpringFactoryBioinfo.getFactory().getBean("ctrlCOG");
		ctrlCOG.clearParam();
		ctrlCOG.setTaxID(species.getTaxID());
		COGanno cogAnno = new COGanno(enumCogType);
		try { 
			cogAnno.setEvalueCutoff(evalue); 
		} catch (Exception e) { }
		
		if (StringOperate.isRealNull(cogQueryFa)) {
			cogAnno.setSpecies(species);
		} else {
			cogAnno.setSeqFastaFile(cogQueryFa);	
		}
		ctrlCOG.setCogAnno(cogAnno);
		ctrlCOG.setBlastInfo(evalue, lsStaxID);
		if (!StringOperate.isRealNull(goAnnoFile)) {
			ctrlCOG.setGene2itemAnnoFile(goAnnoFile, isCombine);
		}
		if (!StringOperate.isRealNull(bgGene)) {
			ctrlCOG.setLsBG(bgGene);
		} else {
			ctrlCOG.setLsBG(species);
		}
		if (!isClusterGoPath || colAccID == colFC) {
			double up = 0; double down = 0;
			if ( colAccID != colFC) {
				up = upValue;
				down = downValue;
			}
			ctrlCOG.setUpDown(up, down);
			ctrlCOG.setIsCluster(false);
		} else {
			ctrlCOG.setIsCluster(isClusterGoPath);
		}
		ctrlCOG.setLsAccID2Value(lsAccID);
		ctrlCOG.setSavePathPrefix(resultPath);
		ctrlCOG.running();
	}
	
	private boolean isGeneId(String title) {
		if (title == null) title = "";
		title = title.replace("#", "");
		return StringOperate.isEqualIgnoreCase(title, "geneId");
	}
	
	enum AnalysisType {
		GO, Path, COG
	}

}
