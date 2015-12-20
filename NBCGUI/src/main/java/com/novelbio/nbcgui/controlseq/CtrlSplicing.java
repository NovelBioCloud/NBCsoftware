package com.novelbio.nbcgui.controlseq;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ArrayListMultimap;
import com.novelbio.GuiAnnoInfo;
import com.novelbio.analysis.ExceptionNBCsoft;
import com.novelbio.analysis.seq.fasta.SeqHash;
import com.novelbio.analysis.seq.genome.gffOperate.GffHashGene;
import com.novelbio.analysis.seq.mapping.StrandSpecific;
import com.novelbio.analysis.seq.rnaseq.ExonJunction;
import com.novelbio.analysis.seq.sam.ExceptionSamError;
import com.novelbio.base.ExceptionNullParam;
import com.novelbio.base.StringOperate;
import com.novelbio.base.dataOperate.TxtReadandWrite;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.multithread.RunGetInfo;
import com.novelbio.base.multithread.RunProcess;
import com.novelbio.nbcgui.GUIinfo;
import com.novelbio.nbcgui.GUI.GUIanalysisCASH;

public class CtrlSplicing implements RunGetInfo<GuiAnnoInfo> , Runnable {
	GUIinfo guiRNAautoSplice;
	GffHashGene gffHashGene;
	SeqHash seqHash;
	
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
	boolean isUniqueMappedReads = false;
	int juncAllReadsNum = 25;
	int juncSampleReadsNum = 10;
	double fdrCutoff = 0.999;
	
	int minAnchorLen = 5;
	int minIntronLen = 25;
	
	int newIsoReadsNum = 10;
	
	public static void main(String[] args) {
		if (args == null || args.length == 0 || args.length == 1 && StringOperate.isRealNull(args[0])) {
			GUIanalysisCASH guIanalysisCASH = new GUIanalysisCASH();
			guIanalysisCASH.main(args);
		} else {
			mainCmd(args);
		}
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
		}
		
		for (int i = 0; i < args.length; i++) {
			if (args[i].startsWith("--") || args[i].startsWith("-")) {
				if(param != null) {
					mapParam2Value.put(param, value);
				}
				
				param = args[i].substring(1);
				if (param.startsWith("-")) {
					param = param.substring(1);
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
				} else if (param.equals("R")) {
					param = "Reconstruct";
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
		
		ctrlSplicing.setGffHashGene(new GffHashGene(gtfFile));
//		ctrlSplicing.setSeqHash(seqHash);
		if (mapParam2Value.containsKey("Combine")) {
			String combine = mapParam2Value.get("Combine").toLowerCase();
			if (combine.equals("true") || combine.equals("t")) {
				ctrlSplicing.setCombine(true);
			} else if (combine.equals("false") || combine.equals("f")) {
				ctrlSplicing.setCombine(false);
			}
		}

		if (mapParam2Value.containsKey("minAnchorLen")) {
			int minAnchorLen = Integer.parseInt(mapParam2Value.get("minAnchorLen"));
			if (minAnchorLen >= 0) {
				ctrlSplicing.setminAnchorLen(minAnchorLen);
			}
		}
		if (mapParam2Value.containsKey("minIntronLen")) {
			int minIntronLen = Integer.parseInt(mapParam2Value.get("minIntronLen"));
			if (minIntronLen >= 0) {
				ctrlSplicing.setMinIntronLen(minIntronLen);
			}
		}
		if (mapParam2Value.containsKey("minJuncReadsForNewIso")) {
			int minJuncReadsForNewIso = Integer.parseInt(mapParam2Value.get("minJuncReadsForNewIso"));
			if (minJuncReadsForNewIso >= 0) {
				ctrlSplicing.setNewIsoReadsNum(minJuncReadsForNewIso);
			}
		}
		if (mapParam2Value.containsKey("DisplayAllEvent")) {
			String combine = mapParam2Value.get("DisplayAllEvent").toLowerCase();
			if (combine.equals("true") || combine.equals("t")) {
				ctrlSplicing.setDisplayAllEvent(true);
			} else if (combine.equals("false") || combine.equals("f")) {
				ctrlSplicing.setDisplayAllEvent(false);
			}
		}
		
		if (mapParam2Value.containsKey("Reconstruct")) {
			String isReconstruct = mapParam2Value.get("Reconstruct").toLowerCase();
			if (isReconstruct.equals("true") || isReconstruct.equals("t")) {
				ctrlSplicing.setReconstructIso(true);
			} else if (isReconstruct.equals("false") || isReconstruct.equals("f")) {
				ctrlSplicing.setReconstructIso(false);
			}
		}
		
		if (mapParam2Value.containsKey("JuncAllSample")) {
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
			String strand = mapParam2Value.get("StrandSpecific").toLowerCase();
			if (strand.equals("f") || strand.toLowerCase().equals("f")) {
				ctrlSplicing.setStrandSpecific(StrandSpecific.FIRST_READ_TRANSCRIPTION_STRAND);
			} else if (strand.equals("r") || strand.toLowerCase().equals("r")) {
				ctrlSplicing.setStrandSpecific(StrandSpecific.SECOND_READ_TRANSCRIPTION_STRAND);
			}
		}
		ctrlSplicing.setOutFile(mapParam2Value.get("Output"));
		try {
			ctrlSplicing.run();
		} catch (ExceptionSamError e) {
			System.err.println("input sam file error " + e.getMessage());
			throw e;
        }
	
	}
	
	private static List<String> getHelp() {
		InputStream in = CtrlSplicing.class.getClassLoader().getResourceAsStream("resources/altersplice/help");
		TxtReadandWrite txtRead = new TxtReadandWrite(in);
		List<String> lsHelp = new ArrayList<>();
		for (String content : txtRead.readlines()) {
			lsHelp.add(content);
        }
		
		
//		lsHelp.add("--FdrCutoff double,  default is 0.95");
//		lsHelp.add("  in range (0,1]");
//		lsHelp.add("");
//		lsHelp.add("--GetSeq /path/to/chromesome/file,  default is null");
//		lsHelp.add("  CASH can extract sequences near the splicing site, set the chromosome file correspondance to the gfffile(make sure the chrId equals to gff file)");
//		lsHelp.add("  True: reconstruct gene structure. Notice, even the Reconstruct option is true, CASH still need the gtf file input");
//		lsHelp.add("  False: not reconstruct gene structure");
		return lsHelp;
	}
	
	/** 至少有多少条reads支持的junction才会用于重建转录本 */
	public void setNewIsoReadsNum(int newIsoReadsNum) {
		this.newIsoReadsNum = newIsoReadsNum;
	}
	
	public void setFdrCutoff(double fdrCutoff) {
	    this.fdrCutoff = fdrCutoff;
    }
	
	public void setJuncAllReadsNum(int juncAllReadsNum) {
	    this.juncAllReadsNum = juncAllReadsNum;
    }
	public void setJuncSampleReadsNum(int juncSampleReadsNum) {
	    this.juncSampleReadsNum = juncSampleReadsNum;
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
	public void done(RunProcess<GuiAnnoInfo> runProcess) {
		if (guiRNAautoSplice == null) return;
		guiRNAautoSplice.done(runProcess);
	}

	@Override
	public void threadSuspended(RunProcess<GuiAnnoInfo> runProcess) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void threadResumed(RunProcess<GuiAnnoInfo> runProcess) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void threadStop(RunProcess<GuiAnnoInfo> runProcess) {
		// TODO Auto-generated method stub
	}
	
	public void setGffHashGene(GffHashGene gffHashGene) {
		this.gffHashGene = gffHashGene;
	}
	
	/**
	 * 如果seqhash
	 * @param seqPath
	 */
	public void setSeqHash(SeqHash seqHash) {
		this.seqHash = seqHash;
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
		for (String[] comparePrefix : lsCompareGroup) {
			String treat = comparePrefix[0], ctrl = comparePrefix[1];
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
			for (String bamFile : lsTreatBam) {
				exonJunction.addBamSorted(treat, bamFile);
			}
			for (String bamFile : lsCtrlBam) {
				exonJunction.addBamSorted(ctrl, bamFile);
			}
			exonJunction.setCompareGroups(treat, ctrl);
			exonJunction.setResultFile(outFile);
			exonJunction.setRunGetInfo(this);
			
			exonJunction.setCombine(isCombine);
			exonJunction.setUseUniqueMappedReads(isUniqueMappedReads);
			if (isReconstruceIso) {
				exonJunction.setgenerateNewIso(true);
			}
			System.out.println(exonJunction.getFileLength());
			long fileLength = exonJunction.getFileLength();
			
			Map<String, Long> mapChrId2Len = exonJunction.getMapChrId2Len();
			ArrayList<Double> lsLevels = new ArrayList<Double>();
			long allLen = getChrAll(mapChrId2Len);
			for (String chrId : mapChrId2Len.keySet()) {
				lsLevels.add((double)mapChrId2Len.get(chrId)/allLen);
			}
			setProgressBarLevelLs(lsLevels);
			exonJunction.run();
			if (!exonJunction.isFinishedNormal()) {
				throw new ExceptionNBCsoft("Alternative Splicing Error:" + comparePrefix[0] + " vs " + comparePrefix[1], exonJunction.getException());
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
