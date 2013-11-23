package com.novelbio.nbcgui.controlseq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.collect.ArrayListMultimap;
import com.novelbio.GuiAnnoInfo;
import com.novelbio.analysis.seq.AlignSeq;
import com.novelbio.analysis.seq.FormatSeq;
import com.novelbio.analysis.seq.bed.BedSeq;
import com.novelbio.analysis.seq.genome.GffChrAbs;
import com.novelbio.analysis.seq.genome.GffChrStatistics;
import com.novelbio.analysis.seq.genome.gffOperate.GffHashGene;
import com.novelbio.analysis.seq.genome.gffOperate.GffHashGeneAbs;
import com.novelbio.analysis.seq.mapping.StrandSpecific;
import com.novelbio.analysis.seq.rnaseq.RPKMcomput;
import com.novelbio.analysis.seq.sam.AlignSeqReading;
import com.novelbio.analysis.seq.sam.AlignmentRecorder;
import com.novelbio.analysis.seq.sam.SamFile;
import com.novelbio.analysis.seq.sam.SamFileStatistics;
import com.novelbio.base.FoldeCreate;
import com.novelbio.base.dataOperate.TxtReadandWrite;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.multithread.RunProcess;
import com.novelbio.database.model.species.Species;
import com.novelbio.nbcReport.EnumTableType;
import com.novelbio.nbcReport.XdocTable;
import com.novelbio.nbcReport.XdocTmpltExcel;
import com.novelbio.nbcReport.XdocTmpltPic;
import com.novelbio.nbcReport.Params.EnumReport;
import com.novelbio.nbcReport.Params.ReportGeneExpression;
import com.novelbio.nbcReport.Params.ReportSamAndRPKM;
import com.novelbio.nbcgui.GUI.GuiSamStatistics;

@Component
@Scope("prototype")
public class CtrlSamRPKMLocate implements CtrlSamPPKMint {
	private static final Logger logger = Logger.getLogger(CtrlSamRPKMLocate.class);
	
	
	GuiSamStatistics guiSamStatistics;
	GffChrAbs gffChrAbs = new GffChrAbs();
	
	ReportSamAndRPKM reportSamAndRPKM;
	
	List<String[]> lsReadFile;

	
	StrandSpecific strandSpecific = StrandSpecific.NONE;
	
	String RPKMFileName;
	String fragmentsFileName;
	String  geneStructureResultFile;
	
	/** 是否统计Sam结果 */
	boolean isSamStatistics = true;
	/** 是否统计GeneStructure结果 */
	boolean isGeneStructureStatistics = true;
	boolean isCountExpression = true;
	boolean isCalculateFPKM = true;
	boolean isCountNCrna = false;
	
	Set<String> setPrefix;
	Map<String, GffChrStatistics> mapPrefix2LocStatistics;
	Map<String, SamFileStatistics> mapPrefix2Statistics;
	RPKMcomput rpkMcomput;
	
	int[] tss;
	int[] tes;
	
	String resultSamPrefix;
	String resultExpPrefix;
	String resultGeneStructure;
	
	List<String[]> lsCounts = null;
	
	/**
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
	boolean chrReadsCorrect = false;
	/**
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
		strandSpecific = StrandSpecific.NONE;
		
		
		/** 是否统计Sam结果 */
		isSamStatistics = true;
		/** 是否统计GeneStructure结果 */
		isGeneStructureStatistics = true;
		isCountExpression = true;
		isCalculateFPKM = true;
		isCountNCrna = false;
		
		setPrefix = null;
		mapPrefix2LocStatistics = null;
		mapPrefix2Statistics = null;
		rpkMcomput = null;
		
		tss = null;
		tes = null;
		
		resultExpPrefix = null;
		resultSamPrefix = null;
	}
	
	public void setQueryFile(List<String[]> lsReadFile) {
		this.lsReadFile = lsReadFile;
	}
	@Override
	public void setIsCountRPKM(boolean isCountExpression, StrandSpecific strandSpecific, boolean isCountFPKM, boolean isCountNCRNA) {
		this.isCountExpression = isCountExpression;
		this.strandSpecific = strandSpecific;
		this.isCalculateFPKM = isCountFPKM;
		this.isCountNCrna = isCountNCRNA;
	}
	public void setTssRange(int[] tss) {
		this.tss = tss;
	}
	public void setTesRange(int[] tes) {
		this.tes = tes;
	}
	/** 设定输出文件路径前缀 */
	public void setResultPrefix(String resultPrefix) {
		this.resultSamPrefix = FoldeCreate.getInFold(resultPrefix, EnumReport.SamStatistics.getResultFolder());
		this.resultExpPrefix = FoldeCreate.getInFold(resultPrefix, EnumReport.GeneExp.getResultFolder());
		this.resultGeneStructure = FoldeCreate.getInFold(resultPrefix, EnumReport.GeneStructure.getResultFolder());
	}
	
	public void run() {
		rpkMcomput = new RPKMcomput();		
		if (!isCountExpression && !isSamStatistics && !isGeneStructureStatistics) {
			return;
		}
		FileOperate.createFolders(FileOperate.getPathName(resultSamPrefix));
		if (isCountExpression) {
			FileOperate.createFolders(FileOperate.getPathName(resultExpPrefix));
		}
		if (isGeneStructureStatistics) {
			FileOperate.createFolders(FileOperate.getPathName(resultGeneStructure));
		}
		try {
			calculate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (isCountExpression) {
			writeReportGeneExp();
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
		ArrayListMultimap<String, AlignSeqReading> mapPrefix2AlignSeqReadings = getMapPrefix2LsAlignSeqReadings();
		double readByte = 0;
		
		for (String prefix : mapPrefix2AlignSeqReadings.keySet()) {
			List<AlignSeqReading> lsAlignSeqReadings = mapPrefix2AlignSeqReadings.get(prefix);
			List<AlignmentRecorder> lsAlignmentRecorders = new ArrayList<AlignmentRecorder>();
			if (isCountExpression && gffChrAbs.getGffHashGene() != null) {
				rpkMcomput.setAndAddCurrentCondition(prefix);
				rpkMcomput.setConsiderStrand(strandSpecific);
				rpkMcomput.setCalculateFPKM(isCalculateFPKM);
				
				lsAlignmentRecorders.add(rpkMcomput);
			}
			if (isGeneStructureStatistics) {
				setGeneStructure(prefix, lsAlignmentRecorders, gffChrAbs);
			}
			if (isSamStatistics) {
				SamFile samFile = (SamFile)lsAlignSeqReadings.get(0).getFirstSamFile();
				if (samFile != null) {
					setSamStatistics(prefix, lsAlignmentRecorders, samFile.getMapChrIDLowcase2Length());
				}
			}
			
			for (AlignSeqReading alignSeqReading : lsAlignSeqReadings) {
				alignSeqReading.setReadInfo(0L, readByte);
				alignSeqReading.addColAlignmentRecorder(lsAlignmentRecorders);
				if (alignSeqReading.getFirstSamFile() instanceof SamFile) {
					rpkMcomput.setIsPairend(((SamFile)alignSeqReading.getFirstSamFile()).isPairend());
				}
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
		try {
			writeExpToFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	private void setSamStatistics(String prefix, List<AlignmentRecorder> lsAlignmentRecorders, Map<String, Long> mapChrIDlowcase2Len) {
		SamFileStatistics samFileStatistics = new SamFileStatistics(prefix);
		samFileStatistics.setCorrectChrReadsNum(chrReadsCorrect);
		lsAlignmentRecorders.add(samFileStatistics);
		samFileStatistics.setStandardData(mapChrIDlowcase2Len);
		mapPrefix2Statistics.put(prefix, samFileStatistics);
	}
	
	
	/** 给AOP用的 */
	public void aop() {
		logger.info("test");
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
		mapPrefix2LocStatistics = new HashMap<String, GffChrStatistics>();
		mapPrefix2Statistics = new HashMap<String, SamFileStatistics>();
		
		if (gffChrAbs.getGffHashGene() != null) {
			rpkMcomput.setGffChrAbs(gffChrAbs);
		}
		
		setPrefix = new LinkedHashSet<String>();
		
		ArrayListMultimap<String, AlignSeqReading> mapPrefix2AlignSeqReadings = ArrayListMultimap.create();
		for (String[] fileName2Prefix : lsReadFile) {
			setPrefix.add(fileName2Prefix[1]);
			FormatSeq formatSeq = getFileFormat(fileName2Prefix[0]);

			AlignSeq alignSeq = null;
			if (formatSeq == FormatSeq.SAM || formatSeq == FormatSeq.BAM) {
				alignSeq = new SamFile(fileName2Prefix[0]);
			} else if (formatSeq == FormatSeq.BED) {
				alignSeq = new BedSeq(fileName2Prefix[0]);
			} else {
				continue;
			}
			AlignSeqReading alignSeqReading = new AlignSeqReading(alignSeq);
			
			mapPrefix2AlignSeqReadings.put(fileName2Prefix[1], alignSeqReading);
		}
		return mapPrefix2AlignSeqReadings;
	}
	
	private FormatSeq getFileFormat(String fileName) {
		 return FormatSeq.getFileType(fileName);
	}
	
	private void writeExpToFile() {
		if (isCountExpression && gffChrAbs.getGffHashGene() != null) {
			String suffixRPKM = "All_RPKM", suffixUQRPKM = "All_UQRPKM", 
					suffixCounts = "All_Counts", tpm = "All_TPM", ncrna = "All_ncRNA_Statistics;";
			if (rpkMcomput.isCalculateFPKM()) {
				suffixRPKM = "All_FPKM";
				suffixUQRPKM = "All_UQFPKM";
				suffixCounts = "All_Fragments";
			}
			if (!resultExpPrefix.endsWith("/") && !resultExpPrefix.endsWith("\\")) {
				suffixRPKM = "_" + suffixRPKM;
				suffixUQRPKM = "_" + suffixUQRPKM;
				suffixCounts = "_" + suffixCounts;
				tpm = "_" + tpm;
			}
			
			String outTPM = FileOperate.changeFileSuffix(resultExpPrefix, tpm, "txt");
			String outRPKM = FileOperate.changeFileSuffix(resultExpPrefix, suffixRPKM, "txt");
			String outNCrna = FileOperate.changeFileSuffix(resultExpPrefix, ncrna, "txt");

			RPKMFileName = outRPKM;
			String outCounts = FileOperate.changeFileSuffix(resultExpPrefix, suffixCounts, "txt");
			fragmentsFileName = outCounts;
			String outUQRPKM = FileOperate.changeFileSuffix(resultExpPrefix, suffixUQRPKM, "txt");
			
			List<String[]> lsTpm = rpkMcomput.getLsTPMs();
			List<String[]> lsRpkm = rpkMcomput.getLsRPKMs();
			List<String[]> lsUQRpkm = rpkMcomput.getLsUQRPKMs();
			List<String[]> lsCounts2 = rpkMcomput.getLsCounts();
			if (isCountNCrna) {
				List<String[]> lsNCRNA = rpkMcomput.getLsNCrnaStatistics();
				TxtReadandWrite txtNCRNA = new TxtReadandWrite(outNCrna, true);
				txtNCRNA.ExcelWrite(lsNCRNA);
				txtNCRNA.close();
			}
			TxtReadandWrite txtWriteRpm = new TxtReadandWrite(outTPM, true);
			txtWriteRpm.ExcelWrite(lsTpm);
			TxtReadandWrite txtWriteRpkm = new TxtReadandWrite(outRPKM, true);
			txtWriteRpkm.ExcelWrite(lsRpkm);
			TxtReadandWrite txtWriteUQRpkm = new TxtReadandWrite(outUQRPKM, true);
			txtWriteUQRpkm.ExcelWrite(lsUQRpkm);
			TxtReadandWrite txtWriteCounts = new TxtReadandWrite(outCounts, true);
			txtWriteCounts.ExcelWrite(lsCounts2);
			txtWriteCounts.close();
			txtWriteRpkm.close();
			txtWriteUQRpkm.close();
			txtWriteRpm.close();
		}
	}
	
	private void writeToFileCurrent(String prefix) {
		String tmpExpResult = FileOperate.getPathName(resultExpPrefix) + "tmp/";
//		String tmpSamResult = FileOperate.getPathName(resultSamPrefix) + "tmp/";
//		String tmpGeneStructure = FileOperate.getPathName(resultGeneStructure) + "tmp/";
		
		if (isCountExpression && gffChrAbs.getGffHashGene() != null) {
			FileOperate.createFolders(tmpExpResult);
			tmpExpResult = tmpExpResult + prefix;
			String suffixRPKM = "_RPKM", suffixUQRPKM = "_UQRPKM", 
					suffixCounts = "_Counts", suffixTpm = "_TPM", suffixNCrna = "_ncRNA_Statistics";
			if (rpkMcomput.isCalculateFPKM()) {
				suffixRPKM = "_FPKM";
				suffixUQRPKM = "_UQFPKM";
				suffixCounts = "_Fragments";
			}
			String outTPM = tmpExpResult + suffixTpm + ".txt";
			String outRPKM =  tmpExpResult + suffixRPKM + ".txt";
			String outUQRPKM =  tmpExpResult + suffixUQRPKM + ".txt";
			String outCounts =  tmpExpResult + suffixCounts + ".txt";
			String outNcRNA =  tmpExpResult + suffixNCrna + ".txt";
			
			lsCounts = rpkMcomput.getLsCountsCurrent();

			List<String[]> lsTpm = rpkMcomput.getLsTPMsCurrent();
			List<String[]> lsRpkm = rpkMcomput.getLsRPKMsCurrent();
			List<String[]> lsUQRpkm = rpkMcomput.getLsUQRPKMsCurrent();
			if (isCountNCrna) {
				List<String[]> lsNCrna = rpkMcomput.getLsNCrnaStatisticsCurrent();
				TxtReadandWrite txtWriteNCrna = new TxtReadandWrite(outNcRNA, true);
				txtWriteNCrna.ExcelWrite(lsNCrna);
				txtWriteNCrna.close();
			}
			
			TxtReadandWrite txtWriteRpm = new TxtReadandWrite(outTPM, true);
			txtWriteRpm.ExcelWrite(lsTpm);
			TxtReadandWrite txtWriteRpkm = new TxtReadandWrite(outRPKM, true);
			txtWriteRpkm.ExcelWrite(lsRpkm);
			TxtReadandWrite txtWriteUQRpkm = new TxtReadandWrite(outUQRPKM, true);
			txtWriteUQRpkm.ExcelWrite(lsUQRpkm);
			TxtReadandWrite txtWriteCounts = new TxtReadandWrite(outCounts, true);
			txtWriteCounts.ExcelWrite(lsCounts);
			txtWriteCounts.close();
			txtWriteRpkm.close();
			txtWriteUQRpkm.close();
			txtWriteRpm.close();
		}
		if (isGeneStructureStatistics) {
//			FileOperate.createFolders(tmpGeneStructure);
//			String tmpGeneStructure = tmpGeneStructure + prefix;
			String tmpGeneStructure = resultGeneStructure + prefix;
			if (gffChrAbs.getTaxID() != 0) {
				GffChrStatistics gffChrStatistics = mapPrefix2LocStatistics.get(prefix);

				String outStatistics =tmpGeneStructure + "_GeneStructure.txt";
				geneStructureResultFile = outStatistics;
				TxtReadandWrite txtWrite = new TxtReadandWrite(outStatistics, true);
				txtWrite.ExcelWrite(gffChrStatistics.getStatisticsResult());
				txtWrite.close();
			}
		}
		if (isSamStatistics) {
//			FileOperate.createFolders(tmpSamResult);
//			tmpSamResult = tmpSamResult + prefix;
			String tmpSamResult = resultSamPrefix + prefix;
			SamFileStatistics samFileStatistics = mapPrefix2Statistics.get(prefix);
			String outSamStatistics = tmpSamResult + "_MappingStatistics.txt";
			TxtReadandWrite txtWriteStatistics = new TxtReadandWrite(outSamStatistics, true);
			txtWriteStatistics.ExcelWrite(samFileStatistics.getMappingInfo());
			txtWriteStatistics.close();
		}
	}
	
	public ReportSamAndRPKM WriteReportSamAndRPKM(String picPathName, String excelPathName) {
		reportSamAndRPKM = new ReportSamAndRPKM();
		
		List<XdocTmpltExcel> lsExcels = new ArrayList<>();
		XdocTmpltExcel xdocTmpltExcel = new XdocTmpltExcel(EnumTableType.MappingResult.getXdocTable());
		xdocTmpltExcel.setExcelTitle("Mapping率分析统计结果");
		xdocTmpltExcel.addExcel(excelPathName, 1);
		
		XdocTmpltExcel xdocTmpltExcel2 = new XdocTmpltExcel(EnumTableType.MappingChrFile.getXdocTable());
		xdocTmpltExcel2.setExcelTitle("Reads在染色体上的分布统计");
		xdocTmpltExcel2.addExcel(excelPathName, 2);	
		
		lsExcels.add(xdocTmpltExcel);
		lsExcels.add(xdocTmpltExcel2);

		
		XdocTmpltExcel xdocTmpltExcel3 = new XdocTmpltExcel(EnumTableType.MappingStatistics.getXdocTable());
		xdocTmpltExcel3.setExcelTitle("Reads在基因上的分布统计图表");
		xdocTmpltExcel3.addExcel(geneStructureResultFile, 1);
		lsExcels.add(xdocTmpltExcel3);
		reportSamAndRPKM.setLsExcels(lsExcels);
		List<XdocTmpltPic> lsPics = new ArrayList<>();
		XdocTmpltPic xdocTmpltPic = new XdocTmpltPic(picPathName);
		xdocTmpltPic.setTitle("Reads在染色体上的分布柱状图");
		lsPics.add(xdocTmpltPic);
		reportSamAndRPKM.setLsTmpltPics(lsPics);
		
		Set<String> lsResultFile = new LinkedHashSet<>();
		lsResultFile.add(excelPathName);
		lsResultFile.add(geneStructureResultFile);
		lsResultFile.add(picPathName);
		reportSamAndRPKM.setSetResultFile(lsResultFile);
		
		reportSamAndRPKM.writeAsFile(FileOperate.getPathName(resultSamPrefix));
		return reportSamAndRPKM;
	}
	
	//TODO 新增了NCrna统计表
	
	
	public ReportGeneExpression writeReportGeneExp() {
		ReportGeneExpression reportGeneExpression = new ReportGeneExpression();
		if (isCalculateFPKM) {
			reportGeneExpression.setGeneExpType("FPKM");
		}else {
			reportGeneExpression.setGeneExpType("RPKM");
		}
		
		List<XdocTmpltExcel> lsXdocTmpltExcels = new ArrayList<>();	
		int colNum = lsCounts.get(0).length;
		
		XdocTmpltExcel xdocTmpltExcel = new XdocTmpltExcel(new XdocTable(colNum, 20));
		xdocTmpltExcel.addExcel(fragmentsFileName, 1);
		xdocTmpltExcel.setExcelTitle("标准化Fragment值列表");
		
		XdocTmpltExcel xdocTmpltExcel2 = new XdocTmpltExcel(new XdocTable(colNum, 20));
		xdocTmpltExcel2.addExcel(RPKMFileName, 1);
		xdocTmpltExcel2.setExcelTitle("标准化FPKM值列表");
		lsXdocTmpltExcels.add(xdocTmpltExcel);
		lsXdocTmpltExcels.add(xdocTmpltExcel2);
		
		
		reportGeneExpression.setLsExcels(lsXdocTmpltExcels);
		Set<String> lsResult = new LinkedHashSet<>();
		lsResult.add(fragmentsFileName);
		lsResult.add(RPKMFileName);
		reportGeneExpression.setSetResultFile(lsResult);
		reportGeneExpression.writeAsFile(FileOperate.getPathName(resultExpPrefix));
		return reportGeneExpression;
	}
	
	
	@Override
	public void setRunningInfo(GuiAnnoInfo info) {
		guiSamStatistics.getProcessBar().setValue((int)( info.getNumDouble()/1024));
		guiSamStatistics.getLabel().setText(info.getInfo());
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
