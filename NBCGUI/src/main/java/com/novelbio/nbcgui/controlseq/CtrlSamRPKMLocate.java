package com.novelbio.nbcgui.controlseq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.novelbio.GuiAnnoInfo;
import com.novelbio.analysis.seq.AlignSeq;
import com.novelbio.analysis.seq.FormatSeq;
import com.novelbio.analysis.seq.bed.BedSeq;
import com.novelbio.analysis.seq.genome.GffChrAbs;
import com.novelbio.analysis.seq.genome.GffChrStatistics;
import com.novelbio.analysis.seq.genome.gffOperate.GffHashGene;
import com.novelbio.analysis.seq.genome.gffOperate.GffHashGeneAbs;
import com.novelbio.analysis.seq.mapping.MapTophat;
import com.novelbio.analysis.seq.mapping.StrandSpecific;
import com.novelbio.analysis.seq.rnaseq.RPKMcomput;
import com.novelbio.analysis.seq.sam.AlignSamReading;
import com.novelbio.analysis.seq.sam.AlignSeqReading;
import com.novelbio.analysis.seq.sam.AlignmentRecorder;
import com.novelbio.analysis.seq.sam.BamReadsInfo;
import com.novelbio.analysis.seq.sam.ExceptionSamError;
import com.novelbio.analysis.seq.sam.SamFile;
import com.novelbio.analysis.seq.sam.SamFileStatistics;
import com.novelbio.base.ExceptionNullParam;
import com.novelbio.base.dataOperate.TxtReadandWrite;
import com.novelbio.base.dataStructure.ArrayOperate;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.multithread.RunProcess;
import com.novelbio.database.model.species.Species;
import com.novelbio.nbcgui.GUI.GuiSamStatistics;

@Component
@Scope("prototype")
public class CtrlSamRPKMLocate implements CtrlSamPPKMint {
	private static final Logger logger = Logger.getLogger(CtrlSamRPKMLocate.class);
	
	
	GuiSamStatistics guiSamStatistics;
	GffChrAbs gffChrAbs = new GffChrAbs();
	
	List<String[]> lsReadFile;
	
	String  geneStructureResultFile;
	
	/** 是否统计Sam结果 */
	boolean isSamStatistics = true;
	/** 是否统计GeneStructure结果 */
	boolean isGeneStructureStatistics = true;
	boolean isCountExpression = true;
	boolean isCalculateFPKM = true;
	boolean isCountNCrna = false;
	
	Set<String> setPrefix = new LinkedHashSet<>();
	Map<String, GffChrStatistics> mapPrefix2LocStatistics = new HashMap<>();
	Map<String, SamFileStatistics> mapPrefix2Statistics = new HashMap<>();
	RPKMcomput rpkMcomput = new RPKMcomput();
	/** 仅用于RPKM计算 */
	boolean isJustUseUniqueMappedReads = false;
	int[] tss;
	int[] tes;
	
	String resultSamPrefix;
	String resultExpPrefix;
	String resultGeneStructure;
	String resultPath;
		
	StrandSpecific strandSpecific;
	
	/**
	 * 非unique mapping的reads仅取第一条来统计
	 * ====================================
	 * 由于非unique mapped reads的存在，为了精确统计reads在染色体上的分布，每个染色体上的reads数量用double来记数<br>
	 * 这样如果一个reads在bam文本中出现多次--也就是mapping至多个位置，就会将每个记录(reads)除以其mapping number,<br>
	 * 从而变成一个小数，然后加到染色体上。
	 * 
	 *  因为用double来统计reads数量，所以最后所有染色体上的reads之和与总reads数相比会有一点点的差距<br>
	 * 选择correct就会将这个误差消除。意思就是将所有染色体上的reads凑出总reads的数量。<br>
	 * 算法是  每条染色体reads(结果) = 每条染色体reads数量(原始)  + (总mapped reads数 - 染色体总reads数)/染色体数量<p>
	 * 
	 *  Because change double to long will lose some accuracy, for example double 1.2 convert to int will be 1,<br> 
	 *   so the result "All Chr Reads Number" will not equal to "All Map Reads Number",
		so we make a correction here.
	 */
	@Deprecated
	boolean chrReadsCorrect = false;
	/**
	 * 
	 * 非unique mapping的reads仅取第一条来统计
	 * ====================================
	 * 由于非unique mapped reads的存在，为了精确统计reads在染色体上的分布，每个染色体上的reads数量用double来记数<br>
	 * 这样如果一个reads在bam文本中出现多次--也就是mapping至多个位置，就会将每个记录(reads)除以其mapping number,<br>
	 * 从而变成一个小数，然后加到染色体上。
	 * 
	 *  因为用double来统计reads数量，所以最后所有染色体上的reads之和与总reads数相比会有一点点的差距<br>
	 * 选择correct就会将这个误差消除。意思就是将所有染色体上的reads凑出总reads的数量。<br>
	 * 算法是  每条染色体reads(结果) = 每条染色体reads数量(原始)  + (总mapped reads数 - 染色体总reads数)/染色体数量<p>
	 * 
	 *  Because change double to long will lose some accuracy, for example double 1.2 convert to int will be 1,<br> 
	 *   so the result "All Chr Reads Number" will not equal to "All Map Reads Number",
		so we make a correction here.
	 */
	@Deprecated
	public void setChrReadsCorrect(boolean chrReadsCorrect) {
		this.chrReadsCorrect = chrReadsCorrect;
	}
	@Override
	public void setGUI(GuiSamStatistics guiPeakStatistics) {
		this.guiSamStatistics = guiPeakStatistics;
	}
	public void setSpecies(Species species) {
		this.gffChrAbs.setSpecies(species);
	}
	public void setGffHash(GffHashGene gffHashGene) {
		this.gffChrAbs.setGffHash(gffHashGene);
	}
	public void setGffHash(GffHashGeneAbs gffHashGene) {
		this.gffChrAbs.setGffHash(gffHashGene);
	}
	
	@Override
	public void clear() {
		lsReadFile = null;		
		
		/** 是否统计Sam结果 */
		isSamStatistics = true;
		/** 是否统计GeneStructure结果 */
		isGeneStructureStatistics = true;
		isCountExpression = true;
		isCalculateFPKM = true;
		isCountNCrna = false;
		
		setPrefix.clear();
		mapPrefix2LocStatistics.clear();
		mapPrefix2Statistics.clear();
		rpkMcomput = new RPKMcomput();
		
		tss = null;
		tes = null;
		isJustUseUniqueMappedReads = false;
		resultExpPrefix = null;
		resultSamPrefix = null;
	}
	
	/** fileName2Prefix */
	public void setQueryFile(List<String[]> lsReadFile) {
		this.lsReadFile = lsReadFile;
	}
	@Override
	public void setIsCountRPKM(boolean isCountExpression, StrandSpecific strandSpecific, boolean isCountNCRNA, boolean isJustUseUniqueMappedReads) {
		this.isCountExpression = isCountExpression;
		if (strandSpecific == null) {
			throw new ExceptionNullParam("No Param StrandSpecific");
		}
		this.strandSpecific = strandSpecific;
		this.isCountNCrna = isCountNCRNA;
		this.isJustUseUniqueMappedReads = isJustUseUniqueMappedReads;
	}
	public void setIsGeneStructureStatistics(boolean isGeneStructureStatistics) {
		this.isGeneStructureStatistics = isGeneStructureStatistics;
	}
	public void setIsSamStatistics(boolean isSamStatistics) {
		this.isSamStatistics = isSamStatistics;
	}
	public void setTssRange(int[] tss) {
		this.tss = tss;
	}
	public void setTesRange(int[] tes) {
		this.tes = tes;
	}
	
	public void setResultSamPrefix(String resultSamPrefix) {
		this.resultSamPrefix = resultSamPrefix;
	}
	
	public void setResultExpPrefix(String resultExpPrefix) {
		this.resultExpPrefix = resultExpPrefix;
	}
	
	public void setResultGeneStructure(String resultGeneStructure) {
		this.resultGeneStructure = resultGeneStructure;
	}
	
	public void setResultPath(String resultPath) {
		this.resultPath =resultPath;
	}
	
	public void runTaskSub() {
		if (!isCountExpression && !isSamStatistics && !isGeneStructureStatistics) {
			return;
		}
		FileOperate.createFolders(FileOperate.getPathName(resultSamPrefix));
		if (isCalculateExp()) {
			FileOperate.createFolders(FileOperate.getPathName(resultExpPrefix));
		}
		if (isCalculateGeneStructure()) {
			FileOperate.createFolders(FileOperate.getPathName(resultGeneStructure));
		}
		calculateSub();
	}
	
	public void readAllExpSample() {
		setPrefix.clear();
		for (String[] fileName2Prefix : lsReadFile) {
			setPrefix.add(fileName2Prefix[1]);
		}
		readAllExpInfo();
		writeExpToFile();
	}
	
	public void run() {
		if (!isCountExpression && !isSamStatistics && !isGeneStructureStatistics) {
			return;
		}
		FileOperate.createFolders(FileOperate.getPathName(resultSamPrefix));
		if (isCalculateExp()) {
			FileOperate.createFolders(FileOperate.getPathName(resultExpPrefix));
		}
		if (isCalculateGeneStructure()) {
			FileOperate.createFolders(FileOperate.getPathName(resultGeneStructure));
		}
		try {
			calculate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		done(null);
		
		if (guiSamStatistics != null) {
			int fileSize = getFileSize();		
			guiSamStatistics.getProcessBar().setMaximum(fileSize);
			guiSamStatistics.getProcessBar().setValue(guiSamStatistics.getProcessBar().getMaximum());
			guiSamStatistics.getBtnSave().setEnabled(true);
			guiSamStatistics.getBtnRun().setEnabled(true);
		}
	}
	
	private void calculate() {
		calculateSub();
		writeExpToFile();
	}
	
	private void calculateSub() {
		ArrayListMultimap<String, AlignSeqReading> mapPrefix2AlignSeqReadings = getMapPrefix2LsAlignSeqReadings();
		double readByte = 0;
		//TODO 跳过机制还不完善，现在只能跳过rpkm的计算，未来需要添加判定让其可以跳过全部计算
		for (String prefix : mapPrefix2AlignSeqReadings.keySet()) {
			List<AlignSeqReading> lsAlignSeqReadings = mapPrefix2AlignSeqReadings.get(prefix);
			List<AlignmentRecorder> lsAlignmentRecorders = new ArrayList<AlignmentRecorder>();
			if (isCountExpression && gffChrAbs.getGffHashGene() != null) {
				rpkMcomput.setAndAddCurrentCondition(prefix);
				rpkMcomput.setUniqueMapped(isJustUseUniqueMappedReads);
				if (!rpkMcomput.isExistTmpResultAndReadExp(resultExpPrefix, isCountNCrna)) {
					lsAlignmentRecorders.add(rpkMcomput);
				}
			}
			if (isCalculateGeneStructure()) {
				setGeneStructure(prefix, lsAlignmentRecorders, gffChrAbs);
			}
			if (isSamStatistics) {
				SamFile samFile = (SamFile)lsAlignSeqReadings.get(0).getFirstSamFile();
				if (samFile != null) {
					setSamStatistics(prefix, lsAlignmentRecorders, samFile.getMapChrID2Length());
				}
			}
			
			for (AlignSeqReading alignSeqReading : lsAlignSeqReadings) {
				alignSeqReading.setReadInfo(0L, readByte);
				alignSeqReading.addColAlignmentRecorder(lsAlignmentRecorders);
				alignSeqReading.setRunGetInfo(this);
				alignSeqReading.run();
				logger.info("finish reading " + alignSeqReading.getFirstSamFile().getFileName());
				readByte = alignSeqReading.getReadByte();
			}
			logger.info("finish reading " + prefix);
			try {
				writeToFileCurrent(prefix);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private boolean isCalculateExp() {
		return isCountExpression && gffChrAbs.getGffHashGene() != null;
	}
	private boolean isCalculateGeneStructure() {
		return isGeneStructureStatistics && gffChrAbs.getGffHashGene() != null;
	}
	/** 统计geneStructure的模块 */
	private void setGeneStructure(String prefix, List<AlignmentRecorder> lsAlignmentRecorders, GffChrAbs gffChrAbs) {
		if (gffChrAbs.getGffHashGene() != null) {
			GffChrStatistics gffChrStatistics = new GffChrStatistics();
			gffChrStatistics.setGffChrAbs(gffChrAbs);
			gffChrStatistics.setTesRegion(tes);
			gffChrStatistics.setTssRegion(tss);
			lsAlignmentRecorders.add(gffChrStatistics);
			mapPrefix2LocStatistics.put(prefix, gffChrStatistics);
		}
	}
	
	/** 统计geneStructure的模块 */
	private void setSamStatistics(String prefix, List<AlignmentRecorder> lsAlignmentRecorders, Map<String, Long> mapChrID2Len) {
		SamFileStatistics samFileStatistics = new SamFileStatistics(prefix);
		samFileStatistics.setCorrectChrReadsNum(chrReadsCorrect);
		lsAlignmentRecorders.add(samFileStatistics);
		samFileStatistics.setStandardData(mapChrID2Len);
		mapPrefix2Statistics.put(prefix, samFileStatistics);
	}
	
	public Map<String, SamFileStatistics> getMapPrefix2Statistics() {
		return mapPrefix2Statistics;
	}
	
	private int getFileSize() {
		long fileSizeLong = 0;
		for (String[] fileName : lsReadFile) {
			fileSizeLong += (long) FileOperate.getFileSizeLong(fileName[0]);
		}
		return (int)(fileSizeLong/1024);
	}
	
	@Override
	public Map<String, Long> getMapChrID2Len() {
		return gffChrAbs.getSeqHash().getMapChrLength();
	}
	
	/**
	 * 本步会初始化mapPrefix2LocStatistics和rpkMcomput
	 * @return
	 */
	private ArrayListMultimap<String, AlignSeqReading> getMapPrefix2LsAlignSeqReadings() {
		TxtReadandWrite txtWrite = null;
		if (isCalculateExp()) {
			rpkMcomput.setGffChrAbs(gffChrAbs);
			if (strandSpecific == StrandSpecific.UNKNOWN) {
				txtWrite = new TxtReadandWrite(resultGeneStructure + "SamLibraryInfo.txt", true);
				txtWrite.writefileln("SampleName\t" + ArrayOperate.cmbString(BamReadsInfo.getTitle(), "\t"));
			}

		}
				
		ArrayListMultimap<String, AlignSeqReading> mapPrefix2AlignSeqReadings = ArrayListMultimap.create();
		for (String[] fileName2Prefix : lsReadFile) {
			setPrefix.add(fileName2Prefix[1]);
			FormatSeq formatSeq = getFileFormat(fileName2Prefix[0]);

			AlignSeq alignSeq = null;
			AlignSeqReading alignSeqReading = null;
			if (formatSeq == FormatSeq.SAM || formatSeq == FormatSeq.BAM) {
				alignSeq = new SamFile(fileName2Prefix[0]);
				alignSeqReading = new AlignSamReading((SamFile)alignSeq);
				if (isCalculateExp()) {
					rpkMcomput.setIsPairend(((SamFile) alignSeq).isPairend());
					rpkMcomput.setSorted(SamFile.isSorted((SamFile) alignSeq));
					if (strandSpecific == StrandSpecific.UNKNOWN) {
						BamReadsInfo bamReadsInfo = new BamReadsInfo();
						bamReadsInfo.setGffHashGene(gffChrAbs.getGffHashGene());
						bamReadsInfo.setSamFile((SamFile) alignSeq);
						try {
							bamReadsInfo.calculate();
						} catch (Exception e) {
							throw new ExceptionSamError(e.getMessage() + ", please chose the correct Strand information");
						}
						
						if (bamReadsInfo.getStrandSpecific() == StrandSpecific.UNKNOWN) {
							throw new ExceptionSamError("unknown strand type:\n" + alignSeq.getFileName() + "\t" + bamReadsInfo.toString());
						}
						txtWrite.writefileln(FileOperate.getFileName(alignSeq.getFileName()) + "\t" + bamReadsInfo.toString());
						txtWrite.flush();
						rpkMcomput.setConsiderStrand(bamReadsInfo.getStrandSpecific());
					} else {
						rpkMcomput.setConsiderStrand(strandSpecific);
					}
				}				
			} else if (formatSeq == FormatSeq.BED) {
				alignSeq = new BedSeq(fileName2Prefix[0]);
				alignSeqReading = new AlignSeqReading(alignSeq);
			} else {
				continue;
			}
			if (alignSeq.getFileName().endsWith(MapTophat.TophatSuffix)) {
				String unmapFileName = alignSeq.getFileName().replace(MapTophat.TophatSuffix, MapTophat.UnmapSuffix);
				if (FileOperate.isFileExistAndBigThanSize(unmapFileName, 0)) {
					alignSeqReading.addSeq(new SamFile(unmapFileName));
				}
			}
//			List<Alignment> lsAlignments = new ArrayList<Alignment>();
//			lsAlignments.add(new Align("chr3l", 13020716, 13037150));
//			((AlignSamReading)alignSeqReading).setLsAlignments(lsAlignments);
			mapPrefix2AlignSeqReadings.put(fileName2Prefix[1], alignSeqReading);
		}
		
		if (txtWrite != null) {
			txtWrite.close();
		}
		return mapPrefix2AlignSeqReadings;
	}
	
	private FormatSeq getFileFormat(String fileName) {
		 return FormatSeq.getFileType(fileName);
	}
	
	private void writeExpToFile() {
		if (isCountExpression && gffChrAbs.getGffHashGene() != null) {
			rpkMcomput.writeToFile(resultExpPrefix, isCountNCrna);
		}
	}
	
	private void writeToFileCurrent(String prefix) {
		if (isCountExpression && gffChrAbs.getGffHashGene() != null) {
			if (!rpkMcomput.isExistTmpResultAndReadExp(resultExpPrefix, isCountNCrna)) {
				rpkMcomput.writeToFileCurrent(resultExpPrefix, isCountNCrna);
			}
		}
		
		if (isGeneStructureStatistics) {
			String tmpGeneStructure = resultGeneStructure + prefix;
			if (gffChrAbs.getGffHashGene() != null) {
				GffChrStatistics gffChrStatistics = mapPrefix2LocStatistics.get(prefix);

				String outStatistics =tmpGeneStructure + GffChrStatistics.GeneStructureSuffix;
				TxtReadandWrite txtWrite = new TxtReadandWrite(outStatistics, true);
				txtWrite.ExcelWrite(gffChrStatistics.getStatisticsResultWithBG());
				txtWrite.close();
			}
		}
		if (isSamStatistics) {
			String tmpSamResult = resultSamPrefix + prefix;
			SamFileStatistics samFileStatistics = mapPrefix2Statistics.get(prefix);
			SamFileStatistics.savePic(tmpSamResult, samFileStatistics);
			SamFileStatistics.saveExcel(tmpSamResult, samFileStatistics);
		}
	}
	
	private void readAllExpInfo() {
		String samFileName = lsReadFile.iterator().next()[0];
		FormatSeq formatSeq = getFileFormat(samFileName);
		Map<String, Long> mapChrId2Len = new HashMap<>();
		if (formatSeq == FormatSeq.SAM || formatSeq == FormatSeq.BAM) {
			SamFile alignSeq = new SamFile(samFileName);
			mapChrId2Len = alignSeq.getMapChrID2Length();
			rpkMcomput.setIsPairend(alignSeq.isPairend());
		}
		rpkMcomput.setGffChrAbs(gffChrAbs);
		for (String prefix : setPrefix) {
			if (isCountExpression && gffChrAbs.getGffHashGene() != null) {
				rpkMcomput.setAndAddCurrentCondition(prefix);
				rpkMcomput.setUniqueMapped(isJustUseUniqueMappedReads);
				if (!rpkMcomput.isExistTmpResultAndReadExp(resultExpPrefix, isCountNCrna)) {
					throw new ExceptionSamError(prefix);
				}
			}
			
			if (isSamStatistics && (formatSeq == FormatSeq.SAM || formatSeq == FormatSeq.BAM)) {
				SamFileStatistics samFileStatistics = new SamFileStatistics(prefix);
				samFileStatistics.setCorrectChrReadsNum(chrReadsCorrect);
				samFileStatistics.setStandardData(mapChrId2Len);
				String tmpFile = resultSamPrefix + prefix;
				tmpFile = SamFileStatistics.getSaveExcel(tmpFile);
				if (!FileOperate.isFileExistAndBigThanSize(tmpFile, 0)) {
					throw new ExceptionSamError(prefix + "error, file " + tmpFile + " is not exist or size is 0.");
				}
				samFileStatistics.readTable(tmpFile);
				mapPrefix2Statistics.put(prefix, samFileStatistics);
			}
		}
	}
	
	@Override
	public void setRunningInfo(GuiAnnoInfo info) {
		if (guiSamStatistics != null) {
			try {
				guiSamStatistics.getProcessBar().setValue((int)( info.getNumDouble()/1024));
				guiSamStatistics.getLabel().setText(info.getInfo());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void done(RunProcess<GuiAnnoInfo> runProcess) {
		if (guiSamStatistics != null) {
			guiSamStatistics.done();
		}
	}
	
	@Override
	public void threadSuspended(RunProcess<GuiAnnoInfo> runProcess) {
		if (guiSamStatistics != null) {
			guiSamStatistics.getBtnRun().setEnabled(true);
		}
	}
	
	@Override
	public void threadResumed(RunProcess<GuiAnnoInfo> runProcess) {
		if (guiSamStatistics != null) {
			guiSamStatistics.getBtnRun().setEnabled(false);
		}
	}
	
	@Override
	public void threadStop(RunProcess<GuiAnnoInfo> runProcess) {
		if (guiSamStatistics != null) {
			guiSamStatistics.getBtnRun().setEnabled(true);
		}
	}
	
}
