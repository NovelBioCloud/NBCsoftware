package com.novelbio.nbcgui.controlseq;

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
import com.novelbio.base.ExceptionNullParam;
import com.novelbio.base.multithread.RunGetInfo;
import com.novelbio.base.multithread.RunProcess;
import com.novelbio.nbcgui.GUIinfo;

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
	boolean isReconstruceIso = false;
	/** 是否合并文件--也就是不考虑重复，默认为true，也就是合并文件 **/
	boolean isCombine = true;
	
	
	/**
	 * --Case:aaa file1.bam,file2.bam
	 * --Control:bbb file1.bam,file2.bam
	 * --Combine true  可选
	 * --DisplayAllEvent true 可选
	 * --StrandSpecific F R
	 * @param args
	 */
	public static void main(String[] args) {
		//将输入的参数放到这个map里面
		Map<String, String> mapParam2Value = new LinkedHashMap<>();
		String param = null, value = null;
		if (args == null || (args.length == 1 && (args[0].equals("-h") || args[0].equals("--help")))) {
			
		}
		
		for (int i = 0; i < args.length; i++) {
			if (args[i].startsWith("--")) {
				if (i > 0) {
					mapParam2Value.put(param, value);
				}
				param = args[i].substring(2);
				value = null;
			} else {
				value = args[i];
			}
		}
		String paramCase = null, paramControl = null;
		for (String paramInfo : mapParam2Value.keySet()) {
			if (paramInfo.startsWith("Case")) {
				paramCase = paramInfo;
			} else if (paramInfo.startsWith("Control")) {
				paramControl = paramInfo;
			}
		}
		List<String[]> lsBam2Prefix = new ArrayList<>();
		String prefixCase = paramCase.split(":")[1];
		String caseFiles = mapParam2Value.get(paramCase);
		String prefixControl = paramControl.split(":")[1];
		String controlFiles = mapParam2Value.get(paramControl);
		
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
		
		if (mapParam2Value.containsKey("Combine")) {
			String combine = mapParam2Value.get("Combine").toLowerCase();
			if (combine.equals("true") || combine.equals("t")) {
				ctrlSplicing.setCombine(true);
			} else if (combine.equals("false") || combine.equals("f")) {
				ctrlSplicing.setCombine(false);
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

		ctrlSplicing.setReconstructIso(true);
		if (mapParam2Value.containsKey("StrandSpecific")) {
			String strand = mapParam2Value.get("StrandSpecific").toLowerCase();
			if (strand.equals("f")) {
				ctrlSplicing.setStrandSpecific(StrandSpecific.FIRST_READ_TRANSCRIPTION_STRAND);
			} else if (strand.equals("r")) {
				ctrlSplicing.setStrandSpecific(StrandSpecific.SECOND_READ_TRANSCRIPTION_STRAND);
			}
		}
		
		ctrlSplicing.setOutFile(mapParam2Value.get("Output"));
		ctrlSplicing.run();
	}
	
	private List<String> getHelp() {
		List<String> lsHelp = new ArrayList<>();
		lsHelp.add("Usage: java -jar -Xmx10000m <--options> --Case:prefixCase file1.bam,file2.bam --Control:prefixControl file3.bam,file4.bam --Output outPath");
		lsHelp.add("Example: java -jar -Xmx10000m --DisplayAllEvent True --StrandSpecific F  --Case:prefixCase file1.bam,file2.bam --Control:prefixControl file3.bam,file4.bam --Output outPath");
		lsHelp.add("Input:");
		lsHelp.add("--Case:prefixCase  case bam files, using comma to seperate files. prefixCase");
		return lsHelp;
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
			mapPrefix2LsBam.put(bam2Prefix[2], bam2Prefix[0]);
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
		this.outFile = outFile;
	}
	
	@Override
	public void run() {
		for (String[] comparePrefix : lsCompareGroup) {
			String treat = comparePrefix[0], ctrl = comparePrefix[1];
			List<String> lsTreatBam = mapPrefix2LsBam.get(treat);
			List<String> lsCtrlBam = mapPrefix2LsBam.get(ctrl);
			ExonJunction exonJunction = new ExonJunction();
			exonJunction.setStrandSpecific(strandSpecific);
			exonJunction.setGffHashGene(gffHashGene);
			exonJunction.setOneGeneOneSpliceEvent(!isDisplayAllEvent);
			exonJunction.setRunGetInfo(this);
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
			exonJunction.setSeqHash(seqHash);
			exonJunction.setCombine(isCombine);
			if (isReconstruceIso) {
				exonJunction.setgenerateNewIso(true);
			}
			long fileLength = exonJunction.getFileLength();
			ArrayList<Double> lsLevels = new ArrayList<Double>();
			lsLevels.add(0.3);
			lsLevels.add(0.4);
			lsLevels.add(0.7);
			lsLevels.add(1.0);
			setProgressBarLevelLs(lsLevels);
			setProcessBarStartEndBarNum(0, 0, fileLength);
			exonJunction.run();
			if (!exonJunction.isFinished()) {
				throw new ExceptionNBCsoft("Autonative Splicing Error:" + comparePrefix[0] + " vs " + comparePrefix[1]);
			}
		}
		if (guiRNAautoSplice != null) {
			guiRNAautoSplice.setMessage("Congratulations! Enjoy your PASH.");
			guiRNAautoSplice.done(null);
		}
	
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
