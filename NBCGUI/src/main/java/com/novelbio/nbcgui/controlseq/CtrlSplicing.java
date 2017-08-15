package com.novelbio.nbcgui.controlseq;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Log4jConfigurer;

import com.google.common.collect.ArrayListMultimap;
import com.novelbio.GuiAnnoInfo;
import com.novelbio.analysis.ExceptionNBCsoft;
import com.novelbio.analysis.seq.genome.gffOperate.GffHashGene;
import com.novelbio.analysis.seq.mapping.StrandSpecific;
import com.novelbio.analysis.seq.rnaseq.ExonJunction;
import com.novelbio.analysis.seq.sam.ExceptionSamIndexError;
import com.novelbio.analysis.seq.sam.ExceptionSamNoIndexError;
import com.novelbio.base.ExceptionNbcParamError;
import com.novelbio.base.ExceptionNullParam;
import com.novelbio.base.StringOperate;
import com.novelbio.base.dataOperate.TxtReadandWrite;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.multithread.RunGetInfo;
import com.novelbio.base.multithread.RunProcess;
import com.novelbio.nbcgui.GUIinfo;
import com.novelbio.nbcgui.GUI.GUIanalysisCASH;

public class CtrlSplicing implements RunGetInfo<GuiAnnoInfo> , Runnable {
	private static final Logger logger = LoggerFactory.getLogger(CtrlSplicing.class);
	
	GUIinfo guiRNAautoSplice;
	GffHashGene gffHashGene;
	
	boolean isDisplayAllEvent = true; 
	String outFile;
	ArrayListMultimap<String, String> mapPrefix2LsBam;
	List<String[]> lsCompareGroup;
	StrandSpecific strandSpecific = StrandSpecific.NONE;
	boolean memoryLow = false;
	boolean isReconstruceIso = true;
	/** 是否合并文件--也就是不考虑重复，默认为false，也就是考虑为重复 **/
	boolean isCombine = false;
	/** 是否选择unique mapped reads */
	boolean isUniqueMappedReads = true;
	int juncAllReadsNum = 25;
	int juncSampleReadsNum = 10;
	double fdrCutoff = 0.999;
	
	int minAnchorLen = 5;
	int minIntronLen = 25;
	
	int newIsoReadsNum = 10;
	
	boolean runSepChr = true;
	boolean isArithmeticPvalue = false;
	String region;
	
	public static void main(String[] args) {
//		String ss = "--Case:A /media/nbfs/nbCloud/public/AllProject/@2016-12/project_584e3ded60b256c2864e17bc/task_5886bda460b2d6f392cbcb3c/SamConvert_result/KO20.hisat2.sorted.bam,"
//				+ "/media/nbfs/nbCloud/public/AllProject/@2016-12/project_584e3ded60b256c2864e17bc/task_5886bda460b2d6f392cbcb3c/SamConvert_result/KO37.hisat2.sorted.bam"
//				+ " --Control:B /media/nbfs/nbCloud/public/AllProject/@2016-12/project_584e3ded60b256c2864e17bc/task_5886bda460b2d6f392cbcb3c/SamConvert_result/WT.hisat2.sorted.bam,"
//				+ "/media/nbfs/nbCloud/public/AllProject/@2016-12/project_584e3ded60b256c2864e17bc/task_5886bda460b2d6f392cbcb3c/SamConvert_result/WT_2.hisat2.sorted.bam"
//				+ " --GTF /media/nbfs/nbCloud/public/nbcplatform/genome/species/9606/hg19_GRCh37/gff/ref_GRCh37.p13_top_level.gff3"// --ChrRegion chr1:5046215-7046215"
//				+ " --Output /home/novelbio/software/test/cash_v2.1.0-alpha1/all-new";
//		args = ss.split(" ");
		ExonJunction.isASD = true;
		
		GUIanalysisCASH guIanalysisCASH = new GUIanalysisCASH();
		guIanalysisCASH.main(new String[]{getVersion()});

//		if (args != null && args.length == 1 && args[0] != null 
//				&& (args[0].trim().toLowerCase().equals("--gui") || args[0].trim().toLowerCase().equals("-gui"))) {
//			GUIanalysisCASH guIanalysisCASH = new GUIanalysisCASH();
//			guIanalysisCASH.main(new String[]{getVersion()});
//		} else {
//			mainCmd(args);
//		}
	}
	
	//java -jar -Xmx10g xxx.jar --Case:aaa file1.bam -GTF file.gtf --Control:bbb file2.bam --Output sssss
	/**
	 * --Case:aaa (或者 -T:aaa) file1.bam,file2.bam
	 * --Control:bbb (或者 -C:bbb) file1.bam,file2.bam
	 * --Combine  (或者 -M )  true可选
	 * --JuncAllSample 样本全体junction reads 之和必须大于该值
	 * --JuncSample 单个样本junction reads必须大于该值
	 * --DisplayAllEvent  (或者 -D) true 可选
	 * --StrandSpecific (或者 -S) F R
	 * --Reconstruct (或者 -R)
	 * --GTF (或者 -G) file.gtf
	 * --Output (或者 -O) outfile
	 * @param args
	 */
	public static void mainCmd(String[] args) {
		//将输入的参数放到这个map里面
		Map<String, String> mapParam2Value = new LinkedHashMap<>();
		String param = null, value = null;
		if (args == null || args.length == 0 || (args.length == 1 && (args[0].equals("-h") || args[0].equals("--help")))) {
			for (String content:getHelp()) {
				System.out.println(content.toString());
			}
			return;
		} else if (args != null && (args.length == 1 && (args[0].equals("-v") || args[0].equals("--version")))) {
			System.out.println(getVersion());
			return;
		}
		System.out.println(getVersion());
		Set<String> setParam = getParam();
		for (int i = 0; i < args.length; i++) {
			if (args[i].startsWith("--") || args[i].startsWith("-")) {
				if(param != null) {
					mapParam2Value.put(param, value);
					value = null;
				}
				
				param = args[i].substring(1);
				if (param.startsWith("-")) {
					param = param.substring(1);
				}
				if (!param.startsWith("T:") && !param.startsWith("Case:") && !param.startsWith("C:") && !param.startsWith("Control:")) {
					if (!setParam.contains(param.trim())) {
						System.err.println("=================================");
						System.err.println( "ERROR! " + param.trim() + " is not a valid parameter!");
						System.err.println("=================================");
						System.out.println();
						for (String content:getHelp()) {
							System.out.println(content.toString());
						}
						System.exit(1);
					}
				}
				
				value = null;
				if (param.startsWith("T:")) {
					param = param.replace("T:", "Case:");
				} else if (param.startsWith("C:")) {
					param = param.replace("C:", "Control:");
				} else if (param.equals("M")) {
					param = "Combine";
				} else if (param.equals("D")) {
					param = "DisplayAllEven";
				} else if (param.equals("S")) {
					param = "StrandSpecific";
				} else if (param.equals("s")) {
					param = "SpliceCons";
				} else if (param.equals("O")) {
					param = "Output";
				} else if (param.equals("G")) {
					param = "GTF";
				} else if (param.equals("JuncAllSample")) {
					param = "JuncAllSample";
				} else if (param.equals("JuncOneGroup")) {
					param = "JuncOneGroup";
				} else if (param.equals("FdrCutoff")) {
					param = "FdrCutoff";
				} else if (param.equals("A")) {
					param = "minAnchorLen";
				} else if (param.equals("I")) {
					param = "minIntronLen";
				} else if (param.equals("J")) {
					param = "minJuncReadsForNewIso";
				}
			} else {
				value = args[i];
			}
		}
		//循环结束后把最后一个值装进去
		mapParam2Value.put(param, value);
		
		String paramCase = null, paramControl = null;
		for (String paramInfo : mapParam2Value.keySet()) {
			if (paramInfo.startsWith("Case")) {
				paramCase = paramInfo;
			} else if (paramInfo.startsWith("Control")) {
				paramControl = paramInfo;
			}
		}
		if (paramCase == null) {
			System.err.println("Please set Case parameter!");
			System.exit(1);
        }
		if (paramControl == null) {
			System.err.println("Please set Control parameter!");
			System.exit(1);
        }
		
		List<String[]> lsBam2Prefix = new ArrayList<>();
		
		String prefixCase = paramCase.split(":")[1];
		String caseFiles = mapParam2Value.get(paramCase);
		String prefixControl = paramControl.split(":")[1];
		String controlFiles = mapParam2Value.get(paramControl);
		String gtfFile = mapParam2Value.get("GTF");
		for (String string : caseFiles.split(",")) {
			lsBam2Prefix.add(new String[]{string, prefixCase});
		}
		for (String string : controlFiles.split(",")) {
			lsBam2Prefix.add(new String[]{string, prefixControl});
		}
		
		List<String[]> lsCompare = new ArrayList<>();
		lsCompare.add(new String[]{prefixCase, prefixControl});
		
		CtrlSplicing ctrlSplicing = new CtrlSplicing();
		ctrlSplicing.setLsBam2Prefix(lsBam2Prefix);
		ctrlSplicing.setLsCompareGroup(lsCompare);
		
//		ctrlSplicing.setSeqHash(seqHash);
		if (mapParam2Value.containsKey("Combine")) {
			checkParam(mapParam2Value, "Combine");
			String combine = mapParam2Value.get("Combine").toLowerCase();
			if (combine.equals("true") || combine.equals("t")) {
				ctrlSplicing.setCombine(true);
			} else if (combine.equals("false") || combine.equals("f")) {
				ctrlSplicing.setCombine(false);
			}
		}

		if (mapParam2Value.containsKey("minAnchorLen")) {
			checkParam(mapParam2Value, "minAnchorLen");

			int minAnchorLen = Integer.parseInt(mapParam2Value.get("minAnchorLen"));
			if (minAnchorLen >= 0) {
				ctrlSplicing.setminAnchorLen(minAnchorLen);
			}
		}
		if (mapParam2Value.containsKey("minIntronLen")) {
			checkParam(mapParam2Value, "minIntronLen");
			
			int minIntronLen = Integer.parseInt(mapParam2Value.get("minIntronLen"));
			if (minIntronLen >= 0) {
				ctrlSplicing.setMinIntronLen(minIntronLen);
			}
		}
		if (mapParam2Value.containsKey("minJuncReadsForNewIso")) {
			checkParam(mapParam2Value, "minJuncReadsForNewIso");

			int minJuncReadsForNewIso = Integer.parseInt(mapParam2Value.get("minJuncReadsForNewIso"));
			if (minJuncReadsForNewIso >= 0) {
				ctrlSplicing.setNewIsoReadsNum(minJuncReadsForNewIso);
			}
		}
		if (mapParam2Value.containsKey("runSepChr")) {
			checkParam(mapParam2Value, "runSepChr");

			String combine = mapParam2Value.get("runSepChr").toLowerCase();
			if (combine.equals("true") || combine.equals("t")) {
				ctrlSplicing.setRunSepChr(true);
			} else if (combine.equals("false") || combine.equals("f")) {
				ctrlSplicing.setRunSepChr(false);
			}
		}
		if (mapParam2Value.containsKey("DisplayAllEvent")) {
			checkParam(mapParam2Value, "DisplayAllEvent");

			String combine = mapParam2Value.get("DisplayAllEvent").toLowerCase();
			if (combine.equals("true") || combine.equals("t")) {
				ctrlSplicing.setDisplayAllEvent(true);
			} else if (combine.equals("false") || combine.equals("f")) {
				ctrlSplicing.setDisplayAllEvent(false);
			}
		}
		
		if (mapParam2Value.containsKey("SpliceCons")) {
			checkParam(mapParam2Value, "SpliceCons");

			String isReconstruct = mapParam2Value.get("SpliceCons").toLowerCase();
			if (isReconstruct.equals("true") || isReconstruct.equals("t")) {
				ctrlSplicing.setReconstructIso(true);
			} else if (isReconstruct.equals("false") || isReconstruct.equals("f")) {
				ctrlSplicing.setReconstructIso(false);
			}
		}
		
		if (mapParam2Value.containsKey("JuncAllSample")) {
			checkParam(mapParam2Value, "JuncAllSample");

			String juncNum = mapParam2Value.get("JuncAllSample");
			int juncAllReadsNum =0;
			try {
				juncAllReadsNum = Integer.parseInt(juncNum);
			} catch (Exception e) {
				System.err.println("paramater JuncAllSample error!");
				System.exit(1);
			}
			if (juncAllReadsNum > 0) {
				System.err.println("paramater JuncAllSample set to " + juncAllReadsNum);
				ctrlSplicing.setJuncAllReadsNum(juncAllReadsNum);
            }
        }
		if (mapParam2Value.containsKey("JuncOneGroup")) {
			checkParam(mapParam2Value, "JuncOneGroup");

			String juncNum = mapParam2Value.get("JuncOneGroup");
			int juncSampleNum =0;
			try {
				juncSampleNum = Integer.parseInt(juncNum);
			} catch (Exception e) {
				System.err.println("paramater JuncOneGroup error!");
				System.exit(1);
			}
			if (juncSampleNum > 0) {
				System.err.println("paramater JuncOneGroup set to " + juncSampleNum);
				ctrlSplicing.setJuncSampleReadsNum(juncSampleNum);
            }
        }
		if (mapParam2Value.containsKey("FdrCutoff")) {
			checkParam(mapParam2Value, "FdrCutoff");

			String fdrCutoffStr = mapParam2Value.get("FdrCutoff");
			double fdrCutoff = 0;
			try {
				fdrCutoff = Double.parseDouble(fdrCutoffStr);
			} catch (Exception e) {
				System.err.println("paramater FdrCutoff error!");
				System.exit(1);
			}
			if (fdrCutoff > 0 && fdrCutoff <=1) {
				System.err.println("paramater FdrCutoff set to " + fdrCutoff);
				ctrlSplicing.setFdrCutoff(fdrCutoff);
			} else {
				System.err.println("paramater FdrCutoff error, should in range (0,1]");
			}
		}
		
		if (mapParam2Value.containsKey("StrandSpecific")) {
			checkParam(mapParam2Value, "StrandSpecific");

			String strand = mapParam2Value.get("StrandSpecific").toLowerCase();
			if (strand.toLowerCase().equals("f")) {
				ctrlSplicing.setStrandSpecific(StrandSpecific.FIRST_READ_TRANSCRIPTION_STRAND);
			} else if (strand.toLowerCase().equals("r")) {
				ctrlSplicing.setStrandSpecific(StrandSpecific.SECOND_READ_TRANSCRIPTION_STRAND);
			} else if (strand.toLowerCase().equals("none")) {
				ctrlSplicing.setStrandSpecific(StrandSpecific.NONE);
			}else {
				throw new ExceptionNbcParamError("unknown strand type, can only be  F/R/NONE");
			}
		}
		
		if (mapParam2Value.containsKey("MergePval")) {
			checkParam(mapParam2Value, "MergePval");

			String type = mapParam2Value.get("MergePval").toLowerCase();
			if (type.toLowerCase().equals("a")) {
				ctrlSplicing.setArithmeticPvalue(true);
			} else if (type.toLowerCase().equals("g")) {
				ctrlSplicing.setArithmeticPvalue(false);
			} else {
				throw new ExceptionNbcParamError("unknown MergePval, can only be  A/G");
			}
		}
		
		if (mapParam2Value.containsKey("ChrRegion")) {
			checkParam(mapParam2Value, "ChrRegion");

			String region = mapParam2Value.get("ChrRegion");
			ctrlSplicing.setRegion(region);
		}
		
		if (mapParam2Value.containsKey("LogDebug")) {
			boolean isLogDebug = false;
			try {
				if (!isLogDebug) {
					Log4jConfigurer.initLogging("src/main/java/log4j2.properties");
					isLogDebug = true;
				}
			} catch (FileNotFoundException e) {
			}
			try {
				if (!isLogDebug) {
					Log4jConfigurer.initLogging("log4j2.properties");
					isLogDebug = true;
				}
			} catch (FileNotFoundException e) {
			}
			if (!isLogDebug) {
				logger.error("cannot set log to LogDebug, please check");
			}
		}
		
		ctrlSplicing.setOutFile(mapParam2Value.get("Output"));
		
		logger.info("Start reading gtf file " + gtfFile);
		ctrlSplicing.setGffHashGene(new GffHashGene(gtfFile));
		logger.info("Finish reading gtf file " + gtfFile);
		ctrlSplicing.run();
	}
	
	private static Set<String> getParam() {
		Set<String> setParam = new HashSet<>();
		setParam.add("Case");
		setParam.add("Control");
		setParam.add("GTF");
		setParam.add("Output");
		setParam.add("Combine");
		setParam.add("DisplayAllEvent");
		setParam.add("StrandSpecific");
		setParam.add("SpliceCons");
		setParam.add("JuncAllSample");
		setParam.add("JuncOneGroup");
		setParam.add("minAnchorLen");
		setParam.add("A");
		setParam.add("minIntronLen");
		setParam.add("I");
		setParam.add("minJuncReadsForNewIso");
		setParam.add("J");
		setParam.add("runSepChr");
		setParam.add("MergePval");
		setParam.add("ChrRegion");
		return setParam;
	}
	
	private static void checkParam(Map<String, String> mapParam2Value, String param) {
		String thisValue = mapParam2Value.get(param);
		if (StringOperate.isRealNull(thisValue)) {
			System.err.println("\n\n==========================================\n");
			System.err.println("Warning, does not set parameter " + param+". Please check!");
			System.err.println("\n==========================================\n");
			System.exit(1);
		}
	}
	
	private static String getVersion() {
		String version = "cash";
		for (String content:getHelp()) {
			if (content.toLowerCase().contains("cash version")) {
				version = content;
				break;
            }
		}
		return version;
	}
	
	private static List<String> getHelp() {
		InputStream in = CtrlSplicing.class.getClassLoader().getResourceAsStream("resources/altersplice/help");
		if (in == null) {
			in = CtrlSplicing.class.getClassLoader().getResourceAsStream("altersplice/help");
        }
		
		TxtReadandWrite txtRead = new TxtReadandWrite(in);
		List<String> lsHelp = new ArrayList<>();
		for (String content : txtRead.readlines()) {
			lsHelp.add(content);
        }
		txtRead.close();
		return lsHelp;
	}
	
	/** 至少有多少条reads支持的junction才会用于重建转录本 */
	public void setNewIsoReadsNum(int newIsoReadsNum) {
		this.newIsoReadsNum = newIsoReadsNum;
	}
	public void setFdrCutoff(double fdrCutoff) {
	    this.fdrCutoff = fdrCutoff;
    }
	public void setRunSepChr(boolean runSepChr) {
		this.runSepChr = runSepChr;
	}
	public void setJuncAllReadsNum(int juncAllReadsNum) {
	    this.juncAllReadsNum = juncAllReadsNum;
    }
	public void setJuncSampleReadsNum(int juncSampleReadsNum) {
	    this.juncSampleReadsNum = juncSampleReadsNum;
    }
	public void setArithmeticPvalue(boolean isArithmeticPvalue) {
		this.isArithmeticPvalue = isArithmeticPvalue;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	/** 是否仅选择unique mapped reads */
	public void setUniqueMappedReads(boolean isUniqueMappedReads) {
		this.isUniqueMappedReads = isUniqueMappedReads;
	}
	
	public void setGuiRNAautoSplice(GUIinfo guiRNAautoSplice) {
		this.guiRNAautoSplice = guiRNAautoSplice;
	}
	public void setMemoryLow(boolean memoryLow) {
		this.memoryLow = memoryLow;
	}
	public void setLsBam2Prefix(List<String[]> lsBam2Prefix) {
		mapPrefix2LsBam = ArrayListMultimap.create();
		for (String[] bam2Prefix : lsBam2Prefix) {
			mapPrefix2LsBam.put(bam2Prefix[1], bam2Prefix[0]);
		}
	}
	
	/** 三列，treat，ctrl，outprefix */
	public void setLsCompareGroup(List<String[]> lsCompareGroup) {
		this.lsCompareGroup = lsCompareGroup;
	}
	public void setReconstructIso(boolean isReconstructIso) {
		this.isReconstruceIso = isReconstructIso;
	}
	/** 是否合并文件--也就是不考虑重复，默认为true，也就是合并文件 **/
	public void setCombine(boolean isCombine) {
		this.isCombine = isCombine;
	}
	
	public void setStrandSpecific(StrandSpecific strandSpecific) {
		if (strandSpecific == null) {
			throw new ExceptionNullParam("No Param StrandSpecific");
		}
		
		this.strandSpecific = strandSpecific;
	}
	@Override
	public void setRunningInfo(GuiAnnoInfo info) {
		if (guiRNAautoSplice == null) return;
		
		guiRNAautoSplice.setRunningInfo(info);
		if (info.getLsNumInfo() != null && info.getLsNumInfo().size() > 0) {
			setProcessBarStartEndBarNum(info.getLsNumInfo().get(0).intValue(), 
					info.getLsNumInfo().get(1).longValue(), info.getLsNumInfo().get(2).longValue());
		}
	}
	
	public void setminAnchorLen(int minAnchorLen) {
	    this.minAnchorLen = minAnchorLen;
    }
	public void setMinIntronLen(int minIntronLen) {
	    this.minIntronLen = minIntronLen;
    }
	@Override
	public void done(RunProcess runProcess) {
		if (guiRNAautoSplice == null) return;
		guiRNAautoSplice.done(runProcess);
	}

	@Override
	public void threadSuspended(RunProcess runProcess) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void threadResumed(RunProcess runProcess) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void threadStop(RunProcess runProcess) {
		// TODO Auto-generated method stub
	}
	
	public void setGffHashGene(GffHashGene gffHashGene) {
		this.gffHashGene = gffHashGene;
	}
	
	public void setDisplayAllEvent(boolean isDisplayAllEvent) {
		this.isDisplayAllEvent = isDisplayAllEvent;
	}
	
	public void setOutFile(String outFile) {
		FileOperate.createFolders(FileOperate.getPathName(outFile));
		this.outFile = outFile;
	}
	
	@Override
	public void run() {
		if (!StringOperate.isRealNull(region) && runSepChr == false) {
			throw new ExceptionNbcParamError("wile set ChrRegion, runSepChr must be true");
		}
		
		for (String[] comparePrefix : lsCompareGroup) {
			String treat = comparePrefix[0], ctrl = comparePrefix[1];
			String outprefix = comparePrefix.length > 2 ?comparePrefix[2] : treat + "vs" + ctrl;
			if (!mapPrefix2LsBam.containsKey(treat)) {
				throw new ExceptionNullParam("No Group Name: " + treat);
			}
			if (!mapPrefix2LsBam.containsKey(ctrl)) {
				throw new ExceptionNullParam("No Group Name: " + ctrl);
			}
			List<String> lsTreatBam = mapPrefix2LsBam.get(treat);
			List<String> lsCtrlBam = mapPrefix2LsBam.get(ctrl);
			ExonJunction exonJunction = new ExonJunction();
			exonJunction.setStrandSpecific(strandSpecific);
			exonJunction.setIntronMinLen(minIntronLen);
			exonJunction.setJunctionMinAnchorLen(minAnchorLen);
			exonJunction.setNewIsoReadsNum(newIsoReadsNum);
			exonJunction.setFdrCutoff(fdrCutoff);
			exonJunction.setGffHashGene(gffHashGene);
			exonJunction.setOneGeneOneSpliceEvent(!isDisplayAllEvent);
			exonJunction.setRunGetInfo(this);
			exonJunction.setJuncReadsNum(juncAllReadsNum, juncSampleReadsNum);
			exonJunction.setIsLessMemory(memoryLow);
			exonJunction.setRunSepChr(runSepChr);
			for (String bamFile : lsTreatBam) {
				exonJunction.addBamSorted(treat, bamFile);
			}
			for (String bamFile : lsCtrlBam) {
				exonJunction.addBamSorted(ctrl, bamFile);
			}
			exonJunction.setCompareGroups(treat, ctrl, outprefix);
			exonJunction.setResultFile(outFile);
			exonJunction.setRunGetInfo(this);
			
			exonJunction.setCombine(isCombine);
			exonJunction.setUseUniqueMappedReads(isUniqueMappedReads);
			exonJunction.setgenerateNewIso(isReconstruceIso);
			
			Map<String, Long> mapChrId2Len = exonJunction.getMapChrId2Len();
			ArrayList<Double> lsLevels = new ArrayList<Double>();
			long allLen = getChrAll(mapChrId2Len);
			for (String chrId : mapChrId2Len.keySet()) {
				lsLevels.add((double)mapChrId2Len.get(chrId)/allLen);
			}
			setProgressBarLevelLs(lsLevels);
			exonJunction.addRegion(region);
			exonJunction.setPvalueA(isArithmeticPvalue);
			try {
				exonJunction.running();
				exonJunction.writeToFile(false);
			} catch (ExceptionSamIndexError e) {
				String info = "\n\n==========================================\n"
						+ e.getMessage()
						+ "\nPlease try with \"--runSepChr False\" again."
						+ "\n==========================================\n";
				ExceptionSamIndexError exceptionSamIndexError = new ExceptionSamIndexError(info);
				exceptionSamIndexError.setStackTrace(e.getStackTrace());
				throw exceptionSamIndexError;
			} catch (ExceptionSamNoIndexError e) {
				String info = "\n\n==========================================\n"
						+ e.getMessage()
						+ "\nPlease sort and index the bam file and try again."
						+ "\n==========================================\n";
				ExceptionSamIndexError exceptionSamIndexError = new ExceptionSamIndexError(info);
				exceptionSamIndexError.setStackTrace(e.getStackTrace());
				throw exceptionSamIndexError;
			} catch (Exception e) {
				throw new ExceptionNBCsoft("Alternative Splicing Error:" + comparePrefix[0] + " vs " + comparePrefix[1], e);
			}
		}
		if (guiRNAautoSplice != null) {
			guiRNAautoSplice.setMessage("Congratulations! Enjoy your CASH.");
			guiRNAautoSplice.done(null);
		}
		
	}
	
	private long getChrAll(Map<String, Long> mapChrId2Len) {
		long summary = 0;
		for (String chrId : mapChrId2Len.keySet()) {
			summary += mapChrId2Len.get(chrId);
		}
		return summary;
	}
	
	public void setProgressBarLevelLs(ArrayList<Double> lsLevels) {
		if (guiRNAautoSplice == null) return;
		
		guiRNAautoSplice.setProgressBarLevelLs(lsLevels);
	}
	
	/**
	 * 设定本次步骤里面将绘制progressBar的第几部分
	 * 并且本部分的最短点和最长点分别是什么
	 * @param information gui上显示的文本信息
	 * @param level 本次步骤里面将绘制progressBar的第几部分，也就是跑到第几步了。总共3步
	 * @param startBarNum 本步骤起点，一般为0
	 * @param endBarNum 本步骤终点
	 */
	public void setProcessBarStartEndBarNum(int level, long startBarNum, long endBarNum) {
		if (guiRNAautoSplice == null) return;
		
		guiRNAautoSplice.setProcessBarStartEndBarNum(level, startBarNum, endBarNum);
	}

}
