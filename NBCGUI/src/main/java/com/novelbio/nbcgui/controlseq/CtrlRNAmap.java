package com.novelbio.nbcgui.controlseq;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.novelbio.analysis.IntCmdSoft;
import com.novelbio.analysis.seq.GeneExpTable;
import com.novelbio.analysis.seq.fasta.CopeFastq;
import com.novelbio.analysis.seq.genome.gffOperate.GffHashGene;
import com.novelbio.analysis.seq.genome.gffOperate.GffType;
import com.novelbio.analysis.seq.mapping.MapBowtie2;
import com.novelbio.analysis.seq.mapping.MapHisat;
import com.novelbio.analysis.seq.mapping.MapLibrary;
import com.novelbio.analysis.seq.mapping.MapRNA;
import com.novelbio.analysis.seq.mapping.MapRNAfactory;
import com.novelbio.analysis.seq.mapping.MapRsem;
import com.novelbio.analysis.seq.mapping.MapSplice;
import com.novelbio.analysis.seq.mapping.MapTophat;
import com.novelbio.analysis.seq.mapping.StrandSpecific;
import com.novelbio.analysis.seq.rnaseq.RPKMcomput.EnumExpression;
import com.novelbio.base.ExceptionNullParam;
import com.novelbio.base.StringOperate;
import com.novelbio.base.dataOperate.TxtReadandWrite;
import com.novelbio.base.fileOperate.ExceptionNbcFileInputNotExist;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.database.domain.information.SoftWareInfo.SoftWare;
import com.novelbio.database.model.species.ExceptionNbcSpeciesNotExist;
import com.novelbio.database.model.species.Species;
import com.novelbio.generalConf.TitleFormatNBC;

public class CtrlRNAmap implements IntCmdSoft {
	SoftWare softWare;
	MapLibrary mapLibrary;
	StrandSpecific strandSpecific;
	
	int threadNum = 10;
	Map<String, List<List<String>>> mapPrefix2LsFastq;
	
	MapRNA mapRNA;
	List<String> lsCmd = new ArrayList<>();
	
	Species species;
	String indexFile = "";
	/** ""表示使用GTF */
	String gtfAndGene2Iso;
	
	/** tophat是否用GTF文件进行校正，默认为true，如果出错就要考虑不用GTF */
	boolean useGTF = true;
	String outPath;
	
	/** 将没有比对上的reads用bowtie2再次比对上去 */
	boolean mapUnmapedReads = false;
	
	/** 仅供 hisat2 使用 */
	int alignNum = 5;
	
	int intronLenMin = 20;
	int intronLenMax = 500000;
	
	int hisatTrim5 = 0;
	int hisatTrim3 = 0;
	boolean  hisatDta = true;
	
	/** 保存最终结果，只有rsem才会有
	 * 第一行为标题
	 * 之后每一行为基因表达情况
	 *  */
	List<List<String>> lsExpResultRsemRPKM = new ArrayList<>();
	
	/** 保存最终结果，只有rsem才会有 */
	GeneExpTable rsemExpCounts;
	/** 保存最终结果，只有rsem才会有 */
	GeneExpTable rsemExpFPKM;
	
	int sensitive = MapBowtie2.Sensitive_Sensitive;
	
	public CtrlRNAmap(SoftWare softWare) {
		this.softWare = softWare;
	}
	public void setMapPrefix2LsFastq(CopeFastq copeFastq) {
		copeFastq.setMapCondition2LsFastQLR();
		this.mapPrefix2LsFastq = copeFastq.getMapCondition2LslsFastq();
	}
	
	public void setSpecies(Species species) {
		this.species = species;
	}
	public void setMapUnmapedReads(boolean mapUnmapedReads) {
		this.mapUnmapedReads = mapUnmapedReads;
	}
	public void setOutPath(String outPath) {
		this.outPath = outPath;
	}
	
	public void setAlignNum(String outPrefix) {
		this.outPath = outPrefix;
	}
	
	public void setIntronLenMin(int intronLenMin) {
		if (intronLenMin > 0) {
			this.intronLenMin = intronLenMin;
		}
	}
	public void setIntronLenMax(int intronLenMax) {
		if (intronLenMax > 0) {
			this.intronLenMax = intronLenMax;
		}
	}
	public void setHisatTrim3(int hisatTrim3) {
		if (hisatTrim3 < 0) return;
		this.hisatTrim3 = hisatTrim3;
	}
	public void setHisatTrim5(int hisatTrim5) {
		if (hisatTrim5 < 0) return;
		this.hisatTrim5 = hisatTrim5;
	}
	public void setHisatDta(boolean hisatDta) {
		this.hisatDta = hisatDta;
	}
	/** 默认为5 */
	public void setHisatAlignNum(int alignNum) {
		this.alignNum = alignNum;
	}
	
	public String getOutPrefix() {
		return outPath;
	}
	/** MapTop里面的参数 */
	public void setStrandSpecifictype(StrandSpecific strandSpecifictype) {
		if (strandSpecifictype == null) {
			throw new ExceptionNullParam("No Param StrandSpecific");
		}
		this.strandSpecific = strandSpecifictype;
	}
	public void setLibrary(MapLibrary mapLibrary) {
		if (mapLibrary == null) {
			throw new ExceptionNullParam("No Param MapLibrary");
		}
		this.mapLibrary = mapLibrary;
	}
	public void setThreadNum(int threadNum) {
		if (threadNum < 0) return;
		this.threadNum = threadNum;
	}
	public void setIsUseGTF(boolean useGTF) {
		this.useGTF = useGTF;
	}
	public void setSensitive(Integer sensitive) {
		if (sensitive == null) {
			throw new ExceptionNullParam("No Param Sensitive");
		}
		this.sensitive= sensitive;
	}
	
	public SoftWare getSoftWare() {
		return softWare;
	}
	/** 物种如果不存在，则返回红底黑字："用户自定义物种"，方便项目部手工修改 */
	public Species getSpecies() {
		return species;
	}
	/**
	 * 如果referece在数据库中找不到，就输入该文件
	 * @param indexFile
	 */
	public void setIndexFile(String indexFile) {
		validateFileExist(indexFile);
		this.indexFile = indexFile;
	}
	public void setGtfAndGene2Iso(String gtfAndGene2Iso) {
		validateFileExist(gtfAndGene2Iso);
		this.gtfAndGene2Iso = gtfAndGene2Iso;
	}
	
	public void mapping() {
		lsCmd.clear();
		lsExpResultRsemRPKM = new ArrayList<>();
		rsemExpCounts = new GeneExpTable(TitleFormatNBC.AccID);
		rsemExpFPKM = new GeneExpTable(TitleFormatNBC.AccID);
		for (Entry<String, List<List<String>>> entry : mapPrefix2LsFastq.entrySet()) {
			mapRNA = MapRNAfactory.generateMapRNA(softWare);
			String prefix = entry.getKey();
			List<List<String>> lsFastqFR = entry.getValue();

			setMapLibrary(mapLibrary);
			setIntronLen(intronLenMax, intronLenMin);
			mapRNA.setStrandSpecifictype(strandSpecific);
			mapRNA.setThreadNum(threadNum);
			mapRNA.setOutPathPrefix(outPath + prefix);
			if (FileOperate.isFileExistAndBigThanSize(mapRNA.getFinishName(), 0)) {
				continue;
			}
			setHisatParam();

			if (softWare == SoftWare.tophat) {
				((MapTophat)mapRNA).setSensitiveLevel(sensitive);
				if (useGTF) setGtf();
			} else if (softWare == SoftWare.hisat2) {
				((MapHisat)mapRNA).setSensitiveLevel(sensitive);
				((MapHisat)mapRNA).setAlignNum(alignNum);
				if (useGTF) setGtf();
				
			}
			
			mapRNA.setLeftFq(CopeFastq.convertFastqFile(lsFastqFR.get(0)));
			mapRNA.setRightFq(CopeFastq.convertFastqFile(lsFastqFR.get(1)));
			setRefFile();
			try {
				mapRNA.mapReads();
			} catch (Exception e) {
				try {
					lsCmd.addAll(mapRNA.getCmdExeStr());
				} catch (Exception e2) {}
				throw e;
			}
			lsCmd.addAll(mapRNA.getCmdExeStr());
			setExpResult(prefix, mapRNA);
		}
	}
	
	private void setHisatParam() {
		if (mapRNA instanceof MapHisat) {
			MapHisat mapHisat = (MapHisat)mapRNA;
			mapHisat.setTrim3(hisatTrim3);
			mapHisat.setTrim5(hisatTrim5);
			mapHisat.setDownstreamTranscriptomeAssembly(hisatDta);
		}
	}
	
	private void setGtf() {
		if (!StringOperate.isRealNull(gtfAndGene2Iso)) {
			String gtfFile = gtfAndGene2Iso;
			if (mapRNA instanceof MapHisat) {
				gtfFile = MapHisat.convert2SpliceTxt(gtfAndGene2Iso, FileOperate.getPathName(outPath));
			}
			mapRNA.setGtfFiles(gtfFile);
			return;
		}
		if (species == null || !FileOperate.isFileExistAndBigThan0(species.getGffFile())) {
			return;
		}
		
		if (mapRNA instanceof MapTophat || mapRNA instanceof MapSplice) {
			String gtfFile = GffHashGene.convertToOtherFile(species.getGffFile(), GffType.GTF);
			mapRNA.setGtfFiles(gtfFile);
		} else if (mapRNA instanceof MapHisat) {
			String spliceFile = MapHisat.convert2SpliceTxt(species.getGffFile(), FileOperate.getPathName(outPath));
			mapRNA.setGtfFiles(spliceFile);
		}
	}
	
	private void setRefFile() {
		boolean isThrdPartIndex = false;
		if (FileOperate.isFileExist(indexFile)) {
			isThrdPartIndex = true;
			mapRNA.setRefIndex(indexFile);
			return;
		}
		
		if (species == null || species.getTaxID() == 0) {
			throw new ExceptionNbcSpeciesNotExist("species doesn't exist, so cannot set refgenome");
        }
		
		String indexUnmap = null;
		if (isThrdPartIndex) {
			indexUnmap = indexFile;
		} else {
			//用bwa的mem方法来进行二次mapping
			indexUnmap = species.getIndexChr(SoftWare.bwa_mem);
		}
		
		if (softWare == SoftWare.tophat) {
			mapRNA.setRefIndex(species.getIndexChr(softWare));
			((MapTophat)mapRNA).setMapUnmapedReads(mapUnmapedReads, indexUnmap);
		} else if (softWare == SoftWare.rsem) {
			mapRNA.setRefIndex(species.getIndexRef(softWare, true));
		} else if (softWare == SoftWare.mapsplice) {
			mapRNA.setRefIndex(species.getIndexChr(softWare));
			((MapSplice)mapRNA).setRefIndexFolder(species.getChromSeqSepFolder());
			((MapSplice)mapRNA).setMapUnmapedReads(mapUnmapedReads, indexUnmap);
		} else if (softWare == SoftWare.hisat2) {
			mapRNA.setRefIndex(species.getIndexChr(softWare));
		}
	}
	
	private void setMapLibrary(MapLibrary mapLibrary) {
		if (mapLibrary == MapLibrary.SingleEnd) {
			return;
		}
		else if (mapLibrary == MapLibrary.PairEnd) {
			mapRNA.setInsert(250);
		}
		else if (mapLibrary == MapLibrary.MatePair) {
			mapRNA.setInsert(4500);
		}
	}
	
	private void setIntronLen(int intronLenMax, int intronLenMin) {
		mapRNA.setIntronLenMin(intronLenMin);
		mapRNA.setIntronLenMax(intronLenMax);
	}
	
	/** 获得基因表达 */
	private void setExpResult(String prefix, MapRNA mapRNA) {
		if (softWare != SoftWare.rsem) return;
		
		MapRsem mapRsem = (MapRsem) mapRNA;
		mapRsem.getGeneExpInfo(prefix, rsemExpFPKM, rsemExpCounts);
	}
	
	public void writeToResult() {
		if (softWare == SoftWare.rsem) {
			TxtReadandWrite txtWriteRpkm = new TxtReadandWrite(outPath + "ResultFPKM.xls", true);
			TxtReadandWrite txtWriteCounts = new TxtReadandWrite(outPath + "ResultCounts.xls", true);
			txtWriteRpkm.ExcelWrite(rsemExpFPKM.getLsCountsNum(EnumExpression.RawValue));
			txtWriteCounts.ExcelWrite(rsemExpFPKM.getLsCountsNum(EnumExpression.Counts));
			txtWriteRpkm.close();
			txtWriteCounts.close();
		}
	}
	
	public static Map<String, SoftWare> getMapRNAmapType() {
		Map<String, SoftWare> mapName2Type = new LinkedHashMap<>();
		mapName2Type.put("Tophat", SoftWare.tophat);
		mapName2Type.put("MapSplice", SoftWare.mapsplice);
		mapName2Type.put("hisat2", SoftWare.hisat2);
//		mapName2Type.put("RSEM", SoftWare.rsem);

		return mapName2Type;
	}
	
	@Override
	public List<String> getCmdExeStr() {
		return lsCmd;
	}
	
	public void setCmdExeStr(List<String> lsCmd) {
		this.lsCmd = lsCmd;
	}
	
	/** 如果文件名不为空，那么判断该文件是否存在<br>
	 * 如果文件名为空，则不判断
	 * @param fileName
	 */
	private void validateFileExist(String fileName) {
		if (!StringOperate.isRealNull(fileName) && !FileOperate.isFileExistAndBigThan0(fileName)) {
			throw new ExceptionNbcFileInputNotExist("input file " + FileOperate.getFileName(fileName) + " is not exist");
        }
	}
	
}
