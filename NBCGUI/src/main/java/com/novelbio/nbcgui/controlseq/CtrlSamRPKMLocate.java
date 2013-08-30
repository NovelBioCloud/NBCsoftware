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
import com.novelbio.base.dataOperate.TxtReadandWrite;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.multithread.RunProcess;
import com.novelbio.database.model.species.Species;
import com.novelbio.nbcgui.GuiAnnoInfo;
import com.novelbio.nbcgui.GUI.GuiSamStatistics;

@Component
@Scope("prototype")
public class CtrlSamRPKMLocate implements CtrlSamPPKMint {
	private static final Logger logger = Logger.getLogger(CtrlSamRPKMLocate.class);
	
	GuiSamStatistics guiSamStatistics;
	GffChrAbs gffChrAbs = new GffChrAbs();
	
	List<String[]> lsReadFile;
	boolean isCountExpression = true;
	boolean isCalculateFPKM = true;
	
	StrandSpecific strandSpecific = StrandSpecific.NONE;
	
	boolean isLocStatistics = true;
	
	Set<String> setPrefix;
	Map<String, GffChrStatistics> mapPrefix2LocStatistics;
	Map<String, SamFileStatistics> mapPrefix2Statistics;
	RPKMcomput rpkMcomput;
	
	int[] tss;
	int[] tes;
	
	String resultPrefix;

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
		isCountExpression = true;
		isCalculateFPKM = true;
		strandSpecific = StrandSpecific.NONE;
		
		isLocStatistics = true;
		
		setPrefix = null;
		mapPrefix2LocStatistics = null;
		mapPrefix2Statistics = null;
		rpkMcomput = null;
		
		tss = null;
		tes = null;
		
		resultPrefix = null;
	}
	
	public void setQueryFile(List<String[]> lsReadFile) {
		this.lsReadFile = lsReadFile;
	}
	public void setIsCountRPKM(boolean isCountExpression, StrandSpecific strandSpecific, boolean isCountFPKM) {
		this.isCountExpression = isCountExpression;
		this.strandSpecific = strandSpecific;
		this.isCalculateFPKM = isCountFPKM;
	}
	public void setTssRange(int[] tss) {
		this.tss = tss;
	}
	public void setTesRange(int[] tes) {
		this.tes = tes;
	}
	/** 设定输出文件路径前缀 */
	public void setResultPrefix(String resultPrefix) {
		this.resultPrefix = resultPrefix;
	}
	public void run() {
		rpkMcomput = new RPKMcomput();
		int fileSize = getFileSize();		
		guiSamStatistics.getProcessBar().setMaximum(fileSize);
		
		ArrayListMultimap<String, AlignSeqReading> mapPrefix2AlignSeqReadings = getMapPrefix2LsAlignSeqReadings();
		if (!isCountExpression && !isLocStatistics) {
			return;
		}
		double readByte = 0;
		for (String prefix : mapPrefix2AlignSeqReadings.keySet()) {
			List<AlignSeqReading> lsAlignSeqReadings = mapPrefix2AlignSeqReadings.get(prefix);
			List<AlignmentRecorder> lsAlignmentRecorders = new ArrayList<AlignmentRecorder>();
			SamFileStatistics samFileStatistics = null;
			if (isCountExpression && gffChrAbs.getTaxID() != 0) {
				rpkMcomput.setCurrentCondition(prefix);
				rpkMcomput.setConsiderStrand(strandSpecific);
				rpkMcomput.setCalculateFPKM(isCalculateFPKM);
				lsAlignmentRecorders.add(rpkMcomput);
			}

			if (isLocStatistics) {
				if (gffChrAbs.getTaxID() != 0) {
					GffChrStatistics gffChrStatistics = new GffChrStatistics();
					gffChrStatistics.setGffChrAbs(gffChrAbs);
					gffChrStatistics.setTesRegion(tes);
					gffChrStatistics.setTssRegion(tss);
					lsAlignmentRecorders.add(gffChrStatistics);
					mapPrefix2LocStatistics.put(prefix, gffChrStatistics);
				}
				samFileStatistics = new SamFileStatistics(prefix);
				lsAlignmentRecorders.add(samFileStatistics);
				try {
					Map<String, Long> mapChrID2Len = ((SamFile)lsAlignSeqReadings.get(0).getSamFile()).getMapChrIDLowcase2Length();
					samFileStatistics.setStandardData(mapChrID2Len);
				} catch (Exception e) {
					// TODO: handle exception
				}

				mapPrefix2Statistics.put(prefix, samFileStatistics);
			}
			
			for (AlignSeqReading alignSeqReading : lsAlignSeqReadings) {
				alignSeqReading.setReadInfo(0L, readByte);
				alignSeqReading.addColAlignmentRecorder(lsAlignmentRecorders);
				if (alignSeqReading.getSamFile() instanceof SamFile) {
					rpkMcomput.setIsPairend(((SamFile)alignSeqReading.getSamFile()).isPairend());
				}
				alignSeqReading.setRunGetInfo(this);
				alignSeqReading.run();
				logger.info("finish reading " + alignSeqReading.getSamFile().getFileName());
				readByte = alignSeqReading.getReadByte();
			}
			if (samFileStatistics != null) {
				SamFileStatistics.saveInfo(resultPrefix+ prefix, samFileStatistics);
			}
			logger.info("finish reading " + prefix);
			try {
				writeToFileCurrent(prefix);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		try {
			writeToFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		done(null);
		guiSamStatistics.getProcessBar().setValue(guiSamStatistics.getProcessBar().getMaximum());
		guiSamStatistics.getBtnSave().setEnabled(true);
		guiSamStatistics.getBtnRun().setEnabled(true);
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
	
	/** 返回保存的路径 */
	@Override
	public String getResultPrefix() {
		return resultPrefix;
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
		
		if (gffChrAbs.getTaxID() != 0) {
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
	
	private void writeToFile() {
		if (isCountExpression && gffChrAbs.getTaxID() != 0) {
			String suffixRPKM = "All_RPKM", suffixUQRPKM = "All_UQRPKM", suffixCounts = "All_Counts", tpm = "All_TPM";
			if (rpkMcomput.isCalculateFPKM()) {
				suffixRPKM = "All_FPKM";
				suffixUQRPKM = "All_UQFPKM";
				suffixCounts = "All_Fragments";
			}
			if (!resultPrefix.endsWith("/") && !resultPrefix.endsWith("\\")) {
				suffixRPKM = "_" + suffixRPKM;
				suffixUQRPKM = "_" + suffixUQRPKM;
				suffixCounts = "_" + suffixCounts;
				tpm = "_" + tpm;
			}
			
			String outTPM = FileOperate.changeFileSuffix(resultPrefix, tpm, "txt");
			String outRPKM = FileOperate.changeFileSuffix(resultPrefix, suffixRPKM, "txt");
			String outCounts = FileOperate.changeFileSuffix(resultPrefix, suffixCounts, "txt");
			String outUQRPKM = FileOperate.changeFileSuffix(resultPrefix, suffixUQRPKM, "txt");
			
			List<String[]> lsTpm = rpkMcomput.getLsTPMs();
			List<String[]> lsRpkm = rpkMcomput.getLsRPKMs();
			List<String[]> lsUQRpkm = rpkMcomput.getLsUQRPKMs();
			List<String[]> lsCounts = rpkMcomput.getLsCounts();
			
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
	}
	
	private void writeToFileCurrent(String prefix) {
		if (isCountExpression && gffChrAbs.getTaxID() != 0) {
			String suffixRPKM = "_RPKM", suffixUQRPKM = "_UQRPKM", suffixCounts = "_Counts", tpm = "_TPM";
			if (rpkMcomput.isCalculateFPKM()) {
				suffixRPKM = "_FPKM";
				suffixUQRPKM = "_UQFPKM";
				suffixCounts = "_Fragments";
			}
			String outTPM = FileOperate.changeFileSuffix(resultPrefix, prefix + tpm, "txt");
			String outRPKM = FileOperate.changeFileSuffix(resultPrefix, prefix + suffixRPKM, "txt");
			String outUQRPKM = FileOperate.changeFileSuffix(resultPrefix, prefix + suffixUQRPKM, "txt");
			String outCounts = FileOperate.changeFileSuffix(resultPrefix, prefix + suffixCounts, "txt");
			
			List<String[]> lsTpm = rpkMcomput.getLsTPMsCurrent();
			List<String[]> lsRpkm = rpkMcomput.getLsRPKMsCurrent();
			List<String[]> lsUQRpkm = rpkMcomput.getLsUQRPKMsCurrent();
			List<String[]> lsCounts = rpkMcomput.getLsCountsCurrent();
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
		if (isLocStatistics) {
			String prefixWrite = "_" + prefix; 
			if (resultPrefix.endsWith("/") || resultPrefix.endsWith("\\")) {
				prefixWrite = prefix;
			}
			if (gffChrAbs.getTaxID() != 0) {
				GffChrStatistics gffChrStatistics = mapPrefix2LocStatistics.get(prefix);

				String outStatistics = FileOperate.changeFileSuffix(resultPrefix, prefixWrite + "_GeneStructure", "txt");
				TxtReadandWrite txtWrite = new TxtReadandWrite(outStatistics, true);
				txtWrite.ExcelWrite(gffChrStatistics.getStatisticsResult());
				txtWrite.close();
			}
			
			SamFileStatistics samFileStatistics = mapPrefix2Statistics.get(prefix);
			String outSamStatistics = FileOperate.changeFileSuffix(resultPrefix, prefixWrite + "_MappingStatistics", "txt");
			TxtReadandWrite txtWriteStatistics = new TxtReadandWrite(outSamStatistics, true);
			txtWriteStatistics.ExcelWrite(samFileStatistics.getMappingInfo());
			txtWriteStatistics.close();
		}
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
