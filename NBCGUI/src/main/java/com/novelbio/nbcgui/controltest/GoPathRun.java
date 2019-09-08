package com.novelbio.nbcgui.controltest;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;

import com.novelbio.base.StringOperate;
import com.novelbio.base.dataOperate.ExcelTxtRead;
import com.novelbio.base.dataStructure.ArrayOperate;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.bioinfo.annotation.cog.COGanno;
import com.novelbio.bioinfo.annotation.cog.EnumCogType;
import com.novelbio.bioinfo.annotation.functiontest.TopGO.GoAlgorithm;
import com.novelbio.database.domain.species.Species;
import com.novelbio.database.service.SpringFactoryBioinfo;
import com.novelbio.nbcgui.controltest.CtrlCOG;
import com.novelbio.nbcgui.controltest.CtrlGOall;
import com.novelbio.nbcgui.controltest.CtrlPath;

public class GoPathRun {
	private static final Logger logger = Logger.getLogger(GoPathRun.class);
	private static final String GO = "GO";
	private static final String Pathway = "Pathway";
	private static final String COG = "COG";
	private static final String GO_PATH = "GO_PATH";
	private static final String ALL = "ALL";

	
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
	GoAlgorithm goAlgorithm;
	String cogType;
	String cogQueryFa;
	List<String> excelFiles;
	List<String> excelPrefixs;
	List<String> lsBlastTaxIds = new ArrayList<>();

	int colAccID;
	int colFC;
	
	boolean isGoLevel;
	int goLevelNum;
	boolean isClusterGoPath;
	
	int upValue;
	int downValue;
	
	private static List<String> getLsHelp() {
		List<String> lsHelp = new ArrayList<>();
		lsHelp.add("--taxId  like 9606");
		lsHelp.add("--version like GRCh38");
		lsHelp.add("--dbType like NCBI");
		lsHelp.add("--isBlast true/false default is false");
		lsHelp.add("--blastId 9606,10090   value sep with comma");
		lsHelp.add("--analysisType GO/Pathway/COG/GO_PATH/ALL");
		lsHelp.add("--cogType COG/KOG");
		lsHelp.add("--goAlgorithm novelgo/parentchild/classic/weight/weight01/elim");
		lsHelp.add("--geneCol 1");
		lsHelp.add("--foldChangeCol 4");
		lsHelp.add("--up 2");
		lsHelp.add("--down -2");
		lsHelp.add("--isGoLevel true/false defaule is false");
		lsHelp.add("--goLevelNum 3 no default");
		lsHelp.add("--isCluster true/false defaule is false");
		lsHelp.add("--isCombine true/false defaule is false");
		lsHelp.add("--prefix prefix1,prefix2  multi prefix sep with comma");
		lsHelp.add("--infile file1,file2  multi files sep with comma");
		lsHelp.add("--annoFile ");
		lsHelp.add("--querySeqCOG cog file need input a fasta file");
		lsHelp.add("--bg backgroud file");
		lsHelp.add("--outpath");
		return lsHelp;
	}
	
	public static void main(String[] args) {
		if (args.length ==0 || args[0].equals("-h") || args[0].equals("--help")) {
			String helpInfo = "example: java -jar gopath.jar --taxId 7955 --version Zv10 --dbType NCBI_Zv10 --isBlast false --analysisType GO"
					+ " --goAlgorithm novelgo --geneCol 2 --foldChangeCol 2  --up 2 --down -2 --isCluster false"
					+ " --prefix test --infile /media/nbfs/nbCloud/public/AllProject/@2018-08/project_5b629d8860b2475e359b74c2/task_5b7fd9ed60b2a42e2e5e2752/miRNA-target-Final.txt "
					+ "--outpath /home/novelbio/test/gopath";
			System.out.println(helpInfo);
			System.out.println();
			System.out.println("Options:");
			List<String> lsHelp = getLsHelp();
			for (String content : lsHelp) {
				System.out.println(content);
			}
			System.exit(1);
		}
		
		Options opts = new Options();
		opts.addOption("taxId", true, "");
		opts.addOption("version", true, "");
		opts.addOption("dbType", true, "");
		opts.addOption("isBlast", true, "");
		opts.addOption("blastId", true, "");
		opts.addOption("analysisType", true, "");
		opts.addOption("cogType", true, "");
		opts.addOption("goAlgorithm", true, "");
		opts.addOption("geneCol", true, "");
		opts.addOption("foldChangeCol", true, "");
		opts.addOption("up", true, "");
		opts.addOption("down", true, "");
		opts.addOption("isGoLevel", true, "");
		opts.addOption("goLevelNum", true, "");
		opts.addOption("isCluster", true, "");
		opts.addOption("isCombine", true, "");
		opts.addOption("prefix", true, "");
		opts.addOption("infile", true, "");
		opts.addOption("annoFile", true, "");
		opts.addOption("querySeqCOG", true, "");
		opts.addOption("bg", true, "");
		opts.addOption("outpath", true, "");
		CommandLine cliParser = null;
		try {
			cliParser = new GnuParser().parse(opts, args);
		} catch (Exception e) {
			logger.error("error params:" + ArrayOperate.cmbString(args, " "), e);
			System.exit(1);
		}
		int containerNum = Integer.parseInt(cliParser.getOptionValue("numContainers", 1 + ""));
		if (containerNum <= 0)
			containerNum = 4;

		int taxId = Integer.parseInt(cliParser.getOptionValue("taxId",""));
		String version = cliParser.getOptionValue("version", "");
		String dbType = cliParser.getOptionValue("dbType", "");
		String analysisType = cliParser.getOptionValue("analysisType", "");
		String goAnnoFile = cliParser.getOptionValue("annoFile", "");
		boolean isCombine = cliParser.getOptionValue("isCombine", "false").equalsIgnoreCase("true");
		String bgGene = cliParser.getOptionValue("bg", "");
		String outPath = cliParser.getOptionValue("outpath", "");
		boolean isBlast = cliParser.getOptionValue("isBlast", "false").equalsIgnoreCase("true");
		String blastTaxIds = cliParser.getOptionValue("blastId", "");
		String prefixs = cliParser.getOptionValue("prefix", "");
		String infiles = cliParser.getOptionValue("infile", "");
		String cogType = cliParser.getOptionValue("cogType", "");
		String goAlgorithmStr = cliParser.getOptionValue("goAlgorithm", "");
		int colAccId = Integer.parseInt(cliParser.getOptionValue("geneCol", "1"));
		int colFc = Integer.parseInt(cliParser.getOptionValue("foldChangeCol", "4"));
		String cogQueryFa = cliParser.getOptionValue("querySeqCOG", "");
		
		int downValue = Integer.parseInt(cliParser.getOptionValue("down", "-1"));
		int upValue = Integer.parseInt(cliParser.getOptionValue("up", "1"));	
		boolean isGoLevel = cliParser.getOptionValue("isGoLevel", "false").equalsIgnoreCase("true");
		int goLevelNum = Integer.parseInt(cliParser.getOptionValue("goLevelNum", "1"));	
		boolean isClusterGoPath= cliParser.getOptionValue("isCluster", "false").equalsIgnoreCase("true");
		
		try {
			cliParser = new GnuParser().parse(opts, args);
		} catch (Exception e) {
			logger.error("error params:" + ArrayOperate.cmbString(args, " "));
			System.exit(1);
		}

		GoPathRun goPathRun = new GoPathRun();
		goPathRun.initial(version, dbType, analysisType, taxId, goAnnoFile, isCombine, bgGene, outPath, isBlast, blastTaxIds);
		goPathRun.setInFile(prefixs, infiles);
		goPathRun.setUpDownValue(upValue, downValue);
		goPathRun.setClusterGoPath(isClusterGoPath);
		goPathRun.setGoLevel(isGoLevel);
		goPathRun.setGoLevelNum(goLevelNum);
		
		goPathRun.setCogType(cogType);
		goPathRun.setGoAlgorithm(goAlgorithmStr);
		goPathRun.setCol(colAccId, colFc);
		goPathRun.setCogQueryFa(cogQueryFa);
		goPathRun.running();
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
		if (isBlast) {
			for (String taxIdBlast : blastTaxIds.split(",")) {
				lsBlastTaxIds.add(taxIdBlast);
			}
		}
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
	
	public void setUpDownValue(int upValue, int downValue) {
		this.upValue = upValue;
		this.downValue = downValue;
	}
	public void setGoLevel(boolean isGoLevel) {
		this.isGoLevel = isGoLevel;
	}
	public void setGoLevelNum(int goLevelNum) {
		this.goLevelNum = goLevelNum;
	}
	public void setClusterGoPath(boolean isClusterGoPath) {
		this.isClusterGoPath = isClusterGoPath;
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

		if (StringOperate.isEqual(analysisType, GO)) {
			runGOPath( AnalysisType.GO, 100);
		} else if (StringOperate.isEqual(analysisType, Pathway)) {
			runGOPath( AnalysisType.Path, 100);
		} else if (StringOperate.isEqual(analysisType, GO_PATH)) {
			runGOPath( AnalysisType.GO, 50);
			runGOPath( AnalysisType.Path, 100);
		} else if (StringOperate.isEqual(analysisType, COG)) {
			runGOPath( AnalysisType.COG, 100);
		} else if (StringOperate.isEqual(analysisType, ALL)) {
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
		resultPath = resultPath +excelPrefix;

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
		if (!isClusterGoPath || colAccID == colFC) {
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
		if (!StringOperate.isRealNull(goAnnoFile) && FileOperate.getFileName(goAnnoFile).toLowerCase().contains("go")) {
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
		resultPath = resultPath +excelPrefix;
		
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
		if (!StringOperate.isRealNull(goAnnoFile) && FileOperate.getFileName(goAnnoFile).toLowerCase().contains("path")) {
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
		resultPath = resultPath +excelPrefix;
		
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
		return StringOperate.isEqual(title, "NCBI-GeneId");
	}
	
	enum AnalysisType {
		GO, Path, COG
	}

}
