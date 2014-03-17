package com.novelbio.database.model.species;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.novelbio.analysis.seq.fasta.SeqFastaHash;
import com.novelbio.analysis.seq.genome.gffOperate.GffType;
import com.novelbio.base.dataOperate.ExcelTxtRead;
import com.novelbio.base.dataOperate.TxtReadandWrite;
import com.novelbio.base.dataStructure.ArrayOperate;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.database.domain.geneanno.SpeciesFile;
import com.novelbio.database.domain.geneanno.SpeciesFile.ExtractSmallRNASeq;
import com.novelbio.database.domain.geneanno.TaxInfo;
import com.novelbio.database.domain.information.SoftWareInfo.SoftWare;
import com.novelbio.database.model.modgeneid.GeneID;
import com.novelbio.database.service.servgeneanno.IManageSpecies;
import com.novelbio.database.service.servgeneanno.ManageBlastInfo;
import com.novelbio.database.service.servgeneanno.ManageSpecies;
import com.novelbio.generalConf.PathDetailNBC;
/**
 * 物种信息，包括名字，以及各个文件所在路径
 * @author zong0jie
 */
public class Species implements Cloneable {
	private static Logger logger = Logger.getLogger(Species.class);
	
	int taxID = 0;
	TaxInfo taxInfo = new TaxInfo();
	String version = "";
	/** 有哪些版本,0：version 1：year<br>
	 * 按照年代从大到小排序
	 */
	ArrayList<String[]> lsVersion = new ArrayList<String[]>();
	/** key：版本ID,通通小写  value：具体的信息 */
	Map<String, SpeciesFile> mapVersion2Species = new LinkedHashMap<String, SpeciesFile>();
	
	String sepVersionAndYear = "_year_";
	
	/** 需要获得哪一种gffType */
	String gffDB;
	
	public Species() {
		if (!isOK) return;
	}
	public Species(int taxID) {
		if (!isOK) return;
		
		this.taxID = taxID;
		querySpecies(false);
		if (lsVersion.size() > 0) {
			this.version = lsVersion.get(0)[0];
		}
	}
	
	public Species(TaxInfo taxInfo) {
		if (!isOK) return;
		
		this.taxID = taxInfo.getTaxID();
		this.taxInfo = taxInfo.clone();
		querySpecies(true);
		if (lsVersion.size() > 0) {
			this.version = lsVersion.get(0)[0];
		}
	}
	
	public Species(int taxID, String version) {
		if (!isOK) return;//TODO
		
		this.taxID = taxID;
		querySpecies(false);
		setVersion(version);
	}
	public int getTaxID() {
		return taxID;
	}
	/**
	 * 设定taxID，如果设定的是全新的taxID，那么会重新设定version
	 * @param taxID
	 */
	public void setTaxID(int taxID) {
		if (!isOK) return;
		
		if (this.taxID == taxID) {
			return;
		}
		this.taxID = taxID;
		querySpecies(false);
		if (lsVersion.size() > 0) {
			this.version = lsVersion.get(0)[0];
		}
	}
	/**
	 * 设定版本号，设定之前务必先设定taxID。如果不存在该版本号，则直接返回
	 * @param version
	 */
	public void setVersion(String version) {
		if (version == null) {
			return;
		}
		version = version.split(sepVersionAndYear)[0].toLowerCase();
		if (!mapVersion2Species.containsKey(version)) {
			return;
		}
		this.version = version;
		this.gffDB = null;
	}
	/**
	 * 获得数据库中该物种的所有版本
	 * 倒序排列
	 * @return
	 */
	public String getVersion() {
		return this.version;
	}
	/**
	 * 获得数据库中该物种的所有版本
	 * 倒序排列
	 * @return
	 */
	public ArrayList<String> getVersionAll() {
		ArrayList<String> lsVersionOut = new ArrayList<String>();
		for (String[] string : lsVersion) {
			lsVersionOut.add(string[0] + sepVersionAndYear +string[1]);
		}
		return lsVersionOut;
	}
	/**
	 * 获得数据库中该物种的所有版本
	 * 倒序排列，主要用于gui中的选框
	 * @return
	 */
	public HashMap<String, String> getMapVersion() {
		LinkedHashMap<String, String> mapVersion = new LinkedHashMap<String, String>();
		for (String[] string : lsVersion) {
			mapVersion.put(string[0] + sepVersionAndYear +string[1], string[0]);
		}
		return mapVersion;
	}
	/**
	 * 获得该物种的信息
	 */
	private void querySpecies(boolean isTaxInfoExist) {
		lsVersion.clear();
		mapVersion2Species.clear();
		if (!isTaxInfoExist) {
			try {
				taxInfo = ManageSpecies.getInstance().queryTaxInfo(taxID);
			} catch (Exception e) {
				logger.error("cannot connect to database");
				e.printStackTrace();
				return;
			}
		}

		List<SpeciesFile> lsSpeciesFile = ManageSpecies.getInstance().queryLsSpeciesFile(taxID);
		for (SpeciesFile speciesFile : lsSpeciesFile) {
			lsVersion.add(new String[]{speciesFile.getVersion(), speciesFile.getPublishYear() + ""});
			mapVersion2Species.put(speciesFile.getVersion().toLowerCase(), speciesFile);
		}
		version = null;
		gffDB = null;
		//年代从大到小排序
//		Collections.sort(lsVersion, new Comparator<String[]>() {
//			public int compare(String[] o1, String[] o2) {
//				Integer o1int = Integer.parseInt(o1[1]);
//				Integer o2int = Integer.parseInt(o2[1]);
//				return -o1int.compareTo(o2int);
//			}
//		});
	}
	/** 常用名 */
	public String getCommonName() {
		if (taxInfo == null) {
			return taxID + "";
		}
		return taxInfo.getComName();
	}
	/** 拉丁名，没有就返回taxID */
	public String getNameLatin() {
		if (taxInfo == null) {
			return taxID + "";
		}
		return taxInfo.getLatin();
	}
	/** 拉丁名，两个单词的，不显示种系名，没有就返回taxID */
	public String getNameLatin_2Word() {
		if (taxInfo == null) {
			return taxID + "";
		}
		return taxInfo.getLatin();
	}
	/** KEGG上的缩写 */
	public String getAbbrName() {
		if (taxInfo == null) {
			return taxID + "";
		}
		return taxInfo.getAbbr();
	}
	/**
	 * @return
	 * key: chrID 小写
	 * value： length
	 */
	public Map<String, Long> getMapChromInfo() {
		SpeciesFile speciesFile = mapVersion2Species.get(version.toLowerCase());
		return speciesFile.getMapChromInfo();
	}
	/** 染色体全长序列 */
	public long getChromLenAll() {
		SpeciesFile speciesFile = mapVersion2Species.get(version.toLowerCase());
		Map<String, Long> hashChrID2Len = speciesFile.getMapChromInfo();
		ArrayList<Long> lsChrLen = ArrayOperate.getArrayListValue(hashChrID2Len);
		Long len = 0L;
		for (Long chrLen : lsChrLen) {
			len = len + chrLen;
		}
		return len;
	}
	public String getChromSeq() {
		SpeciesFile speciesFile = mapVersion2Species.get(version.toLowerCase());
		return speciesFile.getChromSeqFile();
	}
	public String getChromSeqSep() {
		SpeciesFile speciesFile = mapVersion2Species.get(version.toLowerCase());
		return speciesFile.getChromSeqFileSep();
	}
	/** 获得这个species在本version下的全体GffDB */
	public Map<String, String> getMapGffDBAll() {
		SpeciesFile speciesFile = mapVersion2Species.get(version.toLowerCase());
		return speciesFile.getMapGffDB();
	}
	/**
	 * 设定需要获取哪一种gff文件的注释
	 * @param gffDB 大小写不敏感
	 */
	public void setGffDB(String gffDB) {
		this.gffDB = gffDB;
	}
	/**
	 * 指定version，和type，返回对应的gff文件，没有则返回null，
	 * 根据设定的gfftype选择，如果没有设定gfftype，则选择最优先的gfftype。
	 * 优先级由GFFtype来决定
	 * @return gffFilePath
	 */
	public String getGffFile() {
		return getGffFile(gffDB);
	}

	/**
	 * 指定version, type和gffDB返回对应的gff文件，没有则返回null
	 * @param gffDB 无所谓大小写
	 */
	public String getGffFile(String gffDB) {
		SpeciesFile speciesFile = mapVersion2Species.get(version.toLowerCase());
		return speciesFile.getGffFile(gffDB);
	}
	
	/**
	 * @param gffDB 无所谓大小写
	 * @return
	 */
	public GffType getGffType() {
		return getGffType(gffDB);
	}
	
	/**
	 * @param gffDB 无所谓大小写
	 * @return
	 */
	public GffType getGffType(String gffDB) {
		SpeciesFile speciesFile = mapVersion2Species.get(version.toLowerCase());
		return speciesFile.getGffType(gffDB); 
	}
	/**
	 * 指定version，和type，返回对应的gffDB信息，没有则返回null，
	 * 自动选择最优先的gffDB。
	 * 优先级由GFFtype来决定
	 * @return gffType
	 */
	public String getGffDB() {
		if (gffDB == null) {
			SpeciesFile speciesFile = mapVersion2Species.get(version.toLowerCase());
			gffDB = speciesFile.getGffDB();
		}
		return gffDB;
	}
	/**
	 * <b>注意要判定文件是否存在</b>
	 * 返回UCSC的gffRepeat
	 * @param version
	 * @return
	 */
	public String getGffRepeat() {
		SpeciesFile speciesFile = mapVersion2Species.get(version.toLowerCase());
		return speciesFile.getGffRepeatFile();
	}
	/** 获得本物种指定version的miRNA前体序列，不存在则返回null */
	public String getMiRNAhairpinFile() {
		if (!taxInfo.isHaveMiRNA()) return null;
		
		String[] path = getMiRNAseq();
		if (path[0] == null) {
			return null;
		}
		return path[1];
	}
	
	/** 获得本物种指定version的miRNA序列，不存在则返回null */
	public String getMiRNAmatureFile() {
		if (!taxInfo.isHaveMiRNA()) return null;
		
		String[] path = getMiRNAseq();
		if (path[0] == null) {
			return null;
		}
		return path[0];
	}
	
	/**
	 * 返回绝对路径
	 * @return
	 * 0: miRNAfile<br>
	 * 1: miRNAhairpinFile
	 */
	private String[] getMiRNAseq() {
		String pathParent = PathDetailNBC.getGenomePath();
		String node = "miRNA/";
		String genomePath = node + taxID + FileOperate.getSepPath();
		String miRNAfile = pathParent + genomePath + "miRNA.fa";
		String miRNAhairpinFile = pathParent + genomePath + "miRNAhairpin.fa";
		if (!FileOperate.isFileExistAndBigThanSize(miRNAfile,10) || !FileOperate.isFileExistAndBigThanSize(miRNAhairpinFile,10)) {
			FileOperate.createFolders(FileOperate.getParentPathName(miRNAfile));
			ExtractSmallRNASeq extractSmallRNASeq = new ExtractSmallRNASeq();
			extractSmallRNASeq.setOutMatureRNA(miRNAfile);
			extractSmallRNASeq.setOutHairpinRNA(miRNAhairpinFile);
			extractSmallRNASeq.setMiRNAdata(PathDetailNBC.getMiRNADat(), getNameLatin_2Word());
			extractSmallRNASeq.getSeq();
		}
		if (!FileOperate.isFileExistAndBigThanSize(miRNAhairpinFile, 0)) {
			FileOperate.DeleteFileFolder(miRNAhairpinFile);
			FileOperate.DeleteFileFolder(miRNAfile);
			miRNAhairpinFile = null;
			miRNAfile = null;
		}
		return new String[]{miRNAfile, miRNAhairpinFile};
	}
	
	/**
	 *  获得rfam序列
	 * @param spciesSpecific 是否只获取当前物种的rfam序列
	 * @return
	 */
	public String getRfamFile(boolean spciesSpecific) {
		SpeciesFile speciesFile = mapVersion2Species.get(version.toLowerCase());
		return speciesFile.getRfamFile(spciesSpecific);
	}
	/** 获得本物中指定version的refseq的ncRNA序列 */
	public String getRefseqNCfile() {
		SpeciesFile speciesFile = mapVersion2Species.get(version.toLowerCase());
		return speciesFile.getRefseqNCfile();
	}
	/** 获得本物中指定version的refseq的序列
	 * @param isAllIso 是否需要全体iso
	 *  */
	public String getRefseqFile(boolean isAllIso) {
		SpeciesFile speciesFile = mapVersion2Species.get(version.toLowerCase());
		return speciesFile.getRefSeqFile(isAllIso, false);
	}
	/** 获得本物中指定version的refseq的序列
	 * @param isAllIso 是否需要全体iso
	 *  */
	public String getRefseqProFile(boolean isAllIso) {
		SpeciesFile speciesFile = mapVersion2Species.get(version.toLowerCase());
		return speciesFile.getRefSeqFile(isAllIso, true);
	}
	/** 指定mapping的软件，获得该软件所对应的索引文件
	 * 没有就新建一个，格式<br>
	 * softMapping.toString() + "_Chr_Index/"
	 *  */
	public String getIndexChr(SoftWare softMapping) {
		SpeciesFile speciesFile = mapVersion2Species.get(version.toLowerCase());
		return speciesFile.getIndexChromFa(softMapping);
	}
	/** 指定mapping的软件，获得该软件所对应的索引文件
	 * 没有就新建一个，格式<br>
	 * softMapping.toString() + "_Ref_Index/" 
	 *  */
	public String getIndexRef(SoftWare softMapping, boolean isAllIso) {
		SpeciesFile speciesFile = mapVersion2Species.get(version.toLowerCase());
		return speciesFile.getIndexRefseq(softMapping, isAllIso);
	}
	////////////////////////    升级   //////////////////////////////////////////////////////////////////////////////////////

	/** 用数据库查找的方式，遍历refseq文件，然后获得gene2iso的表 */
	public String getGene2IsoFileFromRefSeq() {
		String gene2IsoFile = FileOperate.changeFileSuffix(getRefseqFile(true), "_Gene2Iso", "txt");
		if (!FileOperate.isFileExist(gene2IsoFile)) {
			TxtReadandWrite txtGene2Iso = new TxtReadandWrite(gene2IsoFile, true);
			SeqFastaHash seqFastaHash = new SeqFastaHash(getRefseqFile(true), null, false);
			for (String geneIDstr : seqFastaHash.getLsSeqName()) {
				GeneID geneID = new GeneID(geneIDstr, getTaxID());
				String symbol = geneID.getSymbol();
				if (symbol == null || symbol.equals("")) {
					symbol = geneIDstr;
				}
				txtGene2Iso.writefileln(symbol + "\t" + geneIDstr);
			}
			seqFastaHash.close();
			txtGene2Iso.close();
		}
		return gene2IsoFile;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		
		if (getClass() != obj.getClass()) return false;
		Species otherObj = (Species)obj;
		if (
				getTaxID() == otherObj.getTaxID() 
				&& version.equals(otherObj.version)
				)
		{
			return true;
		}
		return false;
	}
	
	public Species clone() {
		Species speciesClone = null;
		try {
			speciesClone = (Species)super.clone();
			speciesClone.taxInfo = taxInfo;
			speciesClone.lsVersion = new ArrayList<String[]>(lsVersion);
			speciesClone.mapVersion2Species = new HashMap<String, SpeciesFile>(mapVersion2Species);
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return speciesClone;
	}
	
	/** 根据指定的SeqType，返回相应的序列 */
	public String getSeqFile(SeqType seqType) {
		if (seqType == SeqType.genome) {
			return getChromSeq();
		} else if (seqType == SeqType.refseqAllIso) {
			return getRefseqFile(true);
		} else if (seqType == SeqType.refseqOneIso) {
			return getRefseqFile(false);
		} else {
			return "";
		}
	}
	
	public String getSeqIndex(SeqType seqType, SoftWare softMapping) {
		if (seqType == SeqType.genome) {
			return getIndexChr(softMapping);
		} else if (seqType == SeqType.refseqAllIso) {
			return getIndexRef(softMapping, true);
		} else if (seqType == SeqType.refseqOneIso) {
			return getIndexRef(softMapping, false);
		} else {
			return "";
		}
	}
	
	/**
	 * 返回该版本物种所含有的Genome，RefSeqAllIso，RefSeqOneIso
	 * 的信息
	 * @return
	 */
	public Map<String, SeqType> getMapSeq2Type() {
		Map<String, SeqType> mapType2Detail = new LinkedHashMap<>();
		String chrFile = getChromSeq();
		if (FileOperate.isFileExistAndBigThanSize(chrFile, 0)) {
			mapType2Detail.put("genome", SeqType.genome);
		}
		String refseqAllIso = getRefseqFile(true);
		if (FileOperate.isFileExistAndBigThanSize(refseqAllIso, 0)) {
			mapType2Detail.put("refseqAllIso", SeqType.refseqAllIso);
		}
		String refseqOneIso = getRefseqFile(false);
		if (FileOperate.isFileExistAndBigThanSize(refseqOneIso, 0)) {
			mapType2Detail.put("refseqOneIso", SeqType.refseqOneIso);
		}		
		return mapType2Detail;
	}
	
	public static enum SeqType {
		genome, refseqAllIso, refseqOneIso
//		, rfamAll, rfamSpecies, miRNAmature, miRNAhairpin
	}
	
	/**
	 * 返回常用名对taxID
	 * @param speciesType 根据不同的
	 * @return
	 */
	public static Map<String, Species> getSpeciesName2Species(EnumSpeciesType speciesType) {
		return getSpeciesName2Species(speciesType, false, null);
	}
	/**
	 * 返回常用名对taxID
	 * @param speciesType 根据不同的
	 * @param getBlastSpecies 是否获取blast中的临时物种信息
	 * @return
	 */
	public static Map<String, Species> getSpeciesName2Species(EnumSpeciesType speciesType, boolean getBlastSpecies, String usrid) {
		HashMap<String, Species> mapName2Species = new LinkedHashMap<String, Species>();
		Species speciesUnKnown = new Species();
		if (speciesType != EnumSpeciesType.miRNA) {
			mapName2Species.put("UnKnown Species", speciesUnKnown);
		}
	
		//按照物种名进行排序
		TreeMap<String, Species> treemapName2Species = new TreeMap<String, Species>(new Comparator<String>() {
			public int compare(String o1, String o2) {
				return o1.toLowerCase().compareTo(o2.toLowerCase());
			}
		});
		
		IManageSpecies servSpeciesFile = ManageSpecies.getInstance();
		List<TaxInfo> lsTaxID = servSpeciesFile.getLsAllTaxID();
		
		Set<Integer> setTaxID = new HashSet<Integer>();
		for (TaxInfo taxInfo : lsTaxID) {
			Species species = new Species(taxInfo);
			if (species.getCommonName().equals("")) {
				continue;
			}
			if (speciesType == EnumSpeciesType.KeggName && species.getAbbrName().equals("")) {
				continue;
			} else if (speciesType == EnumSpeciesType.Genome) {
				List<SpeciesFile> lsSpeciesFiles = servSpeciesFile.queryLsSpeciesFile(taxInfo.getTaxID());
				if (lsSpeciesFiles.size() == 0) {
					continue;
				}
			} else if (speciesType == EnumSpeciesType.miRNA && !species.taxInfo.isHaveMiRNA()) {
				continue;
			}
			setTaxID.add(taxInfo.getTaxID());
			treemapName2Species.put(species.getCommonName().toLowerCase(), species);
		}
		
		/** 添加 物种--序列表 中特有的物种 */
		if (speciesType == EnumSpeciesType.Genome) {
			for (Integer integer : servSpeciesFile.getLsTaxID()) {
				if (setTaxID.contains(integer)) {
					continue;
				}
				Species species = new Species(integer);
				treemapName2Species.put(species.getTaxID() + "", species);
			}
		}
		
		/** 添加blast表中的物种 */
		if (getBlastSpecies) {
			ManageBlastInfo manageBlastInfo = ManageBlastInfo.getInstance();
			treemapName2Species.putAll(manageBlastInfo.getMapSpeciesOnyInBlast(usrid));
		}
		
		for (String name : treemapName2Species.keySet()) {
			Species species = treemapName2Species.get(name);
			mapName2Species.put(name, species);
		}
		
		return mapName2Species;
	}
	
	/** 该taxID是否在数据库中记录 */
	public static boolean validateTaxID(String taxIdOrName) {
		try {
			int taxID = Integer.parseInt(taxIdOrName);
			Species species = new Species(taxID);
			String latinName = species.getNameLatin();
			if (latinName == null || latinName.equals(taxID)) {
				return false;
			}
		} catch (Exception e) {}
		
		return false;
	}
	
	static boolean isOK = true;
//	static {
//		String file = "";
//		if (FileOperate.isFileExist("/lib/firmware/tigon/property")) {
//			TxtReadandWrite txtRead = new TxtReadandWrite("/lib/firmware/tigon/property");
//			for (String string : txtRead.readlines(3)) {
//				if (string.equals("201301jndsfiudsioold")) {
//					isOK = true;
//				}
//				break;
//			}
//			txtRead.close();
//		} else if (FileOperate.isFileExist("C:/Windows/IME/IMEJP10/DICTS/property")) {
//			TxtReadandWrite txtRead = new TxtReadandWrite("C:/Windows/IME/IMEJP10/DICTS/property");
//			for (String string : txtRead.readlines(3)) {
//				if (string.equals("201301jndsfiudsioold")) {
//					isOK = true;
//				}
//				break;
//			}
//			txtRead.close();
//		}		
//	}
	/**
	 * 将配置信息导入数据库
	 * @param txtFile 	 配置信息：第一行，item名称
	 */
	public static void updateTaxInfo(String taxInfoFile) {
		ArrayList<String[]> lsInfo = ExcelTxtRead.readLsExcelTxt(taxInfoFile, 0);
		String[] title = lsInfo.get(0);
		title[0] = title[0].replace("#", "");
		HashMap<String, Integer> hashName2ColNum = new HashMap<String, Integer>();
		for (int i = 0; i < title.length; i++) {
			hashName2ColNum.put(title[i].trim().toLowerCase(), i);
		}
		
		for (int i = 1; i < lsInfo.size()-1; i++) {
			TaxInfo taxInfo = new TaxInfo();
			String[] info = lsInfo.get(i);
			info = ArrayOperate.copyArray(info, 6);
			
			int m = hashName2ColNum.get("taxid");
			taxInfo.setTaxID(Integer.parseInt(info[m]));
			
			m = hashName2ColNum.get("chinesename");
			taxInfo.setChnName(info[m]);
			
			m = hashName2ColNum.get("latinname");
			taxInfo.setLatin(info[m]);
			
			m = hashName2ColNum.get("commonname");
			taxInfo.setComName(info[m]);
			
			m = hashName2ColNum.get("abbreviation");
			taxInfo.setAbbr(info[m]);
			
			m = hashName2ColNum.get("ishavemirna");
			taxInfo.setHaveMiRNA(info[5] != null && info[5].equalsIgnoreCase("true"));
			//升级
			taxInfo.save();
		}
	}
	
	public static enum EnumSpeciesType {
		/** 全体物种 */
		All,
		/** 有Kegg缩写的 */
		KeggName,
		/** 有基因组序列的 */
		Genome,
		/** 有miRNA的*/
		miRNA
	}
}
