package com.novelbio.database.domain.geneanno;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.novelbio.analysis.seq.fasta.SeqFasta;
import com.novelbio.analysis.seq.fasta.SeqFastaHash;
import com.novelbio.analysis.seq.fasta.SeqHash;
import com.novelbio.analysis.seq.fasta.format.NCBIchromFaChangeFormat;
import com.novelbio.analysis.seq.genome.GffChrAbs;
import com.novelbio.analysis.seq.genome.GffChrSeq;
import com.novelbio.analysis.seq.genome.gffOperate.GffDetailGene.GeneStructure;
import com.novelbio.analysis.seq.genome.gffOperate.GffHashGene;
import com.novelbio.analysis.seq.genome.gffOperate.GffType;
import com.novelbio.analysis.seq.mirna.ListMiRNAdat;
import com.novelbio.analysis.seq.sam.SamIndexRefsequence;
import com.novelbio.base.dataOperate.TxtReadandWrite;
import com.novelbio.base.dataStructure.ArrayOperate;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.database.domain.information.SoftWareInfo.SoftWare;
import com.novelbio.database.service.servgeneanno.IManageSpecies;
import com.novelbio.database.service.servgeneanno.ManageSpecies;
import com.novelbio.generalConf.PathDetailNBC;

/**
 * 保存某个物种的各种文件信息，譬如mapping位置等等
 * 感觉可以用nosql进行存储，完全不能是一个结构化文件啊
 * @author zong0jie
 *
 */
@Document(collection="speciesfile")
@CompoundIndexes({
    @CompoundIndex(unique = true, name = "species_version_idx", def = "{'taxID': 1, 'version': -1}"),
 })
public class SpeciesFile {
	private static final Logger logger = Logger.getLogger(SpeciesFile.class);
	@Id
	String id;
	@Indexed
	int taxID = 0;
	/** 文件版本 */
	String version = "";
	/** 该版本的年代，大概年代就行 */
	int publishYear = 0;
	/** 染色体的单文件序列 */
	String chromSeq = "";
	/** 相对路径 gff的repeat文件，从ucsc下载 */
	String gffRepeatFile = "";
	/** 相对路径 refseq文件，全体Iso */
	String refseqFileAllIso;
	/** 相对路径 refseq文件，一个gene一个Iso */
	String refseqFileOneIso;
	/** refseq中的NCRNA文件 */
	String refseqNCfile = "";
	
	/**
	 * key: DBname, 为小写<br>
	 * value: 0, GffType 1:GffFile
	 */
	Map<String, String[]> mapDB2GffTypeAndFile = new LinkedHashMap<>();
	/** 用来将小写的DB转化为正常的DB，使得getDB获得的字符应该是正常的DB */
	Map<String, String> mapGffDBLowCase2DBNormal = new LinkedHashMap<>();
	
	/** 保存不同mapping软件所对应的索引 */
	@Transient
	Map<SoftWare, String> mapSoftware2IndexChrom = new HashMap<>();
	/** 相对路径 ref protein 文件，全体iso */
	@Transient
	String refProFileAllIso;
	/** 相对路径 ref protein 文件，一个gene一个Iso */
	@Transient
	String refProFileOneIso;
	
	/** 相对路径 保存不同mapping软件所对应的RefSeq索引
	 * 主要是全体Iso的RefSeq
	 *  */
	@Transient
	Map<SoftWare, String> mapSoftware2IndexRefAllIso = new HashMap<>();
	/** 相对路径 保存不同mapping软件所对应的索引
	 * 主要是全体gene，每个基因一个iso */
	@Transient
	Map<SoftWare, String> mapSoftware2IndexRefOneIso = new HashMap<>();
	/** key: chrID，为小写    value: chrLen */
	@Transient
	private Map<String, Long> mapChrID2ChrLen = new LinkedHashMap<String, Long>();

	
	/** 相对路径，类似 /media/hdfs/nbCloud/public/nbcplatform/ ，注意不要把genome写进去<br>
	 * 然后以后把speciesFile写在 /media/hdfs/nbCloud/public/nbcplatform/这个文件夹下就行
	 */
	@Transient
	String pathParent = PathDetailNBC.getGenomePath();
	
	/** mongodb的ID */
	public void setId(String id) {
		this.id = id;
	}
	/** mongodb的ID */
	public String getId() {
		return id;
	}
	
	public void setTaxID(int taxID) {
		this.taxID = taxID;
	}
	public int getTaxID() {
		return taxID;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getVersion() {
		return version;
	}
	////////////   以下方法仅用于视图   /////////////////////////
	public String getChromSeqRaw() {
		return chromSeq;
	}
	public String getGffRepeatFileRaw() {
		return gffRepeatFile;
	}
	public String getRefseqFileAllIsoRaw() {
		return refseqFileAllIso;
	}
	public String getRefseqFileOneIsoRaw() {
		return refseqFileOneIso;
	}
	public String getRefseqNCfileRaw() {
		return refseqNCfile;
	}
	/////////////////////////////////////
	/**
	 * @return
	 * key: chrID 小写
	 * value： length
	 */
	public Map<String, Long> getMapChromInfo() {
		if (!mapChrID2ChrLen.isEmpty()) {
			return mapChrID2ChrLen;
		}
		String chrFile = getChromSeqFile();
		if (FileOperate.isFileExistAndBigThanSize(chrFile + ".fai", 0)) {
			mapChrID2ChrLen = new LinkedHashMap<>();
			TxtReadandWrite txtRead = new TxtReadandWrite(chrFile + ".fai");
			for (String content : txtRead.readlines()) {
				String[] ss = content.split("\t");
				mapChrID2ChrLen.put(ss[0], Long.parseLong(ss[1]));
			}
			txtRead.close();
		} else if (FileOperate.isFileExistAndBigThanSize(chrFile, 0)) {
			SeqHash seqHash = new SeqHash(getChromSeqFile(), " ");
			mapChrID2ChrLen = seqHash.getMapChrLength();
			seqHash.close();
		}
			
		return mapChrID2ChrLen;
	}
	
	
	
	public void setChromSeq(String chromSeq) {
		this.chromSeq = chromSeq;
	}
	/** 获得总体的文件 */
	public String getChromSeqFile() {
		if (chromSeq == null || chromSeq.equals("")) {
			return null;
		}
		String chromeSeq = pathParent + chromSeq;
		if (FileOperate.isFileExistAndBigThanSize(chromeSeq, 0)) {
			SamIndexRefsequence samIndexRefsequence = new SamIndexRefsequence();
			samIndexRefsequence.setRefsequence(chromeSeq);
			samIndexRefsequence.indexSequence();
		}
		return chromeSeq;
	}
	/** 获得分割的文件夹 */
	public String getChromSeqFileSep() {
		String chromeSeq = FileOperate.addSep(pathParent + "Chrom_Sep/" + getSpeciesPathWithoutRoot());
		if (!FileOperate.isFileFoldExist(chromeSeq)) {
			FileOperate.createFolders(chromeSeq);
			NCBIchromFaChangeFormat ncbIchromFaChangeFormat = new NCBIchromFaChangeFormat();
			ncbIchromFaChangeFormat.setChromFaPath(getChromSeqFile(), "");
			ncbIchromFaChangeFormat.writeToSepFile(chromeSeq);
		}
		return chromeSeq;
	}
	/**
	 * 给GUI的下拉框用的，一般用不到
	 * @return GffFile
	 */
	public Map<String, String> getMapGffDB() {
		Map<String, String> mapStringDB = new LinkedHashMap<String, String>();
		
		if (mapGffDBLowCase2DBNormal.size() == 0) {
			return mapStringDB;
		}
		for (String gffDB : mapGffDBLowCase2DBNormal.values()) {
			mapStringDB.put(gffDB, gffDB);
		}
		return mapStringDB;
	}
	
	public void addGffDB2TypeFile(String gffDB, GffType gffType, String gffFile) {
		mapDB2GffTypeAndFile.put(gffDB.toLowerCase(), new String[]{gffType.toString(), gffFile});
		mapGffDBLowCase2DBNormal.put(gffDB.toLowerCase(), gffDB);
	}
	
	/**
	 * 获得某个Type的Gff文件，如果没有则返回null
	 * @param gffDB 指定gffDB 如果为null，表示不指定，则返回默认
	 * @return
	 */
	public String getGffFile(String gffDB) {
		if (gffDB == null) {
			return getGffFile();
		}
		return pathParent + mapDB2GffTypeAndFile.get(gffDB.toLowerCase())[1];
	}
	/**
	 * 获得某个Type的GffType，如果没有则返回null
	 * @param GFFtype 指定gfftype 如果为null，表示不指定，则返回默认
	 * @return
	 */
	public GffType getGffType(String gffDB) {
		if (gffDB == null) {
			return getGffType();
		}
		String gffType = mapDB2GffTypeAndFile.get(gffDB.toLowerCase())[0];
		return GffType.getType(gffType);
	}
	
	/**
	 * 按照优先级返回gff文件，优先级由GFFtype来决定
	 * @return GffDB
	 */
	public String getGffDB() {
		String[] gffInfo = getGffDB2GffTypeFile();
		return gffInfo[0];
	}
	
	/**
	 * 按照优先级返回gff文件，优先级由GFFtype来决定
	 * @return GffFile
	 */
	public String getGffFile() {
		String[] gffInfo = getGffDB2GffTypeFile();
		return pathParent + gffInfo[2];
	}
	/**
	 * 按照优先级返回gff类型，优先级由GffDB来决定
	 * 公返回枚举
	 * @return  GffType
	 */
	public GffType getGffType() {
		String[] gffInfo = getGffDB2GffTypeFile();
		String gffType = gffInfo[1];
		return GffType.getType(gffType);
	}
	/**
	 * 按照优先级返回gff文件，优先级由GFFDB来决定
	 * @return string[2]<br>
	 * 0: gffDB<br>
	 * 1: GffType<br>
	 * 2: GffFile 相对路径
	 */
	private String[] getGffDB2GffTypeFile() {
		if (mapDB2GffTypeAndFile.size() == 0) {
			return new String[]{null, null};
		}
		Entry<String, String[]> entyGffDB2File = mapDB2GffTypeAndFile.entrySet().iterator().next();
		String gffDB = entyGffDB2File.getKey();
		gffDB = mapGffDBLowCase2DBNormal.get(gffDB);
		String[] gffType2File = entyGffDB2File.getValue();
		return new String[]{gffDB, gffType2File[0], gffType2File[1]};
	}
	
	public void setGffRepeatFile(String gffRepeatFile) {
		this.gffRepeatFile = gffRepeatFile;
	}
	
	public String getGffRepeatFile() {
		if (gffRepeatFile == null || gffRepeatFile.equals("")) {
			return "";
		}
		return pathParent + gffRepeatFile;
	}
	
	/**
	 * 添加相对路径
	 * @param softWare
	 * @param indexChrom
	 */
	public void addIndexChrom(SoftWare softWare, String indexChrom) {
		mapSoftware2IndexChrom.put(softWare, indexChrom);
	}
	/** 返回该mapping软件所对应的index的文件
	 * 没有就新建一个
	 * 格式如下：
	 * softMapping.toString() + "_Chr_Index/"
	 */
	public String getIndexChromFa(SoftWare softMapping) {
		String indexChromFa = pathParent + mapSoftware2IndexChrom.get(softMapping);
		if (!FileOperate.isFileExist(indexChromFa)) {
			indexChromFa = creatAndGetSeqIndex(false, softMapping, getChromSeqFile(), mapSoftware2IndexChrom);
		}
		return indexChromFa;
	}

	/** 返回该mapping软件所对应的index的文件
	 * 没有就新建一个
	 * 格式如下：softMapping.toString() + "_Ref_Index/"
	 */
	public String getIndexRefseq(SoftWare softMapping, boolean isAllIso) {
		String indexRefseqThis = null;
		if (isAllIso) {
			indexRefseqThis = mapSoftware2IndexRefAllIso.get(softMapping);
		} else {
			indexRefseqThis = mapSoftware2IndexRefOneIso.get(softMapping);
		}
		if (!FileOperate.isFileExist(indexRefseqThis)) {
			Map<SoftWare, String> mapSoft2Seq = null;
			if (isAllIso) {
				mapSoft2Seq = mapSoftware2IndexRefAllIso;
			} else {
				mapSoft2Seq = mapSoftware2IndexRefOneIso;
			}
			indexRefseqThis = creatAndGetSeqIndex(true, softMapping, getRefSeqFile(isAllIso, false), mapSoft2Seq);
		}
		return indexRefseqThis;
	}
	
	/**
	 * 如果不存在该index，那么就新创建一个index并且保存入数据库 
	 * @param refseq 是否为refseq
	 * @param isAllIso 是否提取全体iso
	 * @param softMapping mapping的软件
	 * @param seqIndex 该index所对应的保存在数据库中的值，譬如indexChr
	 * @param seqFile 该index所对应的序列，用getChromSeq()获得
	 * @param mapSoftware2ChrIndexPath 该index所对应的hash表，如 mapSoftware2ChrIndexPath
	 * @return softMapping.toString() + "_Ref_Index/" 或 softMapping.toString() + "_Chr_Index/"
	 */
	private String creatAndGetSeqIndex(Boolean refseq, SoftWare softMapping, String seqFile, Map<SoftWare, String> mapSoftware2ChrIndexPath) {
		String indexChromFinal = null;
		String IndexPath = null;
		String seqName = null;
		String indexFinalPath = null;
		if (mapSoftware2ChrIndexPath.size() > 0) {
			//media/winE/Bioinformatics/GenomeData/mouse/ucsc_mm9/Index/bwa_Index/mm9.fasta
			String indexChromFaOther = mapSoftware2ChrIndexPath.entrySet().iterator().next().getValue();
			seqName = FileOperate.getFileName(indexChromFaOther);
			IndexPath = FileOperate.getParentPathName(FileOperate.getParentPathName(indexChromFaOther));
		}
		else {
			///media/winE/Bioinformatics/GenomeData/mouse/ucsc_mm9/ChromFa/all/mm9.fasta
			seqName = FileOperate.getFileName(seqFile);
			IndexPath = FileOperate.addSep(pathParent + "index/" +softMapping.toString() + "/" + getSpeciesPathWithoutRoot());
		}
		
		if (refseq)
			indexFinalPath = IndexPath + "Ref_Index/";
		else
			indexFinalPath = IndexPath + "Chr_Index/";
		
		indexChromFinal = indexFinalPath + seqName;
		if (FileOperate.isFileExist(indexChromFinal)) {
			return indexChromFinal;
		}
		FileOperate.DeleteFileFolder(indexChromFinal);
		if (!FileOperate.linkFile(seqFile, indexChromFinal, true)) {
			logger.error("创建链接出错：" + seqFile + " " + indexChromFinal);
			return null;
		}
		mapSoftware2ChrIndexPath.put(softMapping, indexChromFinal);
		
		return indexChromFinal;
	}
	
	/**
	 * 返回本version的物种的目录，类似cangshu_hamsters/CriGri_1.0
	 * 没有genome
	 *  是相对路径<p>
	 *  结尾带"/"
	 */
	private String getSpeciesPathWithoutRoot() {
		String path = getSpeciesPath();
		StringBuilder start = new StringBuilder();
		String beginSep = "";//Path开头的反斜杠，一般只有1-2个
		String name = null;
		char[] pathChar = path.toCharArray();
		
		for (char c : pathChar) {
			if (c == '/' || c == '\\') {
				if (start.length() == 0) {
					beginSep = beginSep + c;
					continue;
				} else {
					start.append(c);
					name = start.toString();
					break;
				}
			}
			start.append(c);
		}
		String root = beginSep + name;
		String result = path.replace(root, "");
		return FileOperate.addSep(result);
	}

	public void setPublishYear(int publishYear) {
		this.publishYear = publishYear;
	}
	/** 获得年代 */
	public int getPublishYear() {
		return publishYear;
	}
	/** 设定相对路径 */
	public void setRefseqFileAllIso(String refseqFileAllIso) {
		this.refseqFileAllIso = refseqFileAllIso;
	}
	/** 设定相对路径 */
	public void setRefseqFileOneIso(String refseqFileOneIso) {
		this.refseqFileOneIso = refseqFileOneIso;
	}
	
	/**
	 *  返回绝对路径
	 * @param isAllIso 是否获取含有全部iso的序列
	 * @return
	 */
	public String getRefSeqFile(boolean isAllIso, boolean isProtein) {
		return pathParent + getRefSeqFileRlt(isAllIso, isProtein);
	}
	
	/**
	 * 获得refSeq的相对路径
	 * @return
	 */
	private String getRefSeqFileRlt(boolean isAllIso, boolean isProtein) {
		String refseq = set_And_GetRefSeqName(isAllIso, isProtein);
		if (FileOperate.isFileExistAndBigThanSize(pathParent + refseq, 0.2)) {
			return refseq;
		}
		//说明没有序列
		if (!FileOperate.isFileExistAndBigThanSize(getGffFile(), 0) || !FileOperate.isFileExistAndBigThanSize(getChromSeqFile(), 0)) {
			return "";
		}
		try {
			FileOperate.createFolders(FileOperate.getParentPathName(pathParent + refseq));
			GffChrAbs gffChrAbs = new GffChrAbs();
			gffChrAbs.setGffHash(new GffHashGene(getGffType(), getGffFile()));
			gffChrAbs.setSeqHash(new SeqHash(getChromSeqFile(), " "));
			GffChrSeq gffChrSeq = new GffChrSeq(gffChrAbs);
			if (isProtein) {
				gffChrSeq.setGeneStructure(GeneStructure.CDS);
				gffChrSeq.setGetAAseq(true);
			} else {
				gffChrSeq.setGeneStructure(GeneStructure.ALLLENGTH);
				gffChrSeq.setGetAAseq(false);
			}
			gffChrSeq.setGetReplicateIso(false);
			gffChrSeq.setGetAllIso(isAllIso);
			gffChrSeq.setGetIntron(false);
			gffChrSeq.setGetSeqGenomWide();
			gffChrSeq.setOutPutFile(pathParent + refseq);
			gffChrSeq.run();
			save();
			gffChrAbs.close();
			gffChrAbs = null;
			gffChrSeq = null;
		} catch (Exception e) {
			logger.error("生成 RefRNA序列出错");
		}
	
		return refseq;
	}
	
	/** 生成文件名 */
	private String set_And_GetRefSeqName(boolean isAllIso, boolean isProtein) {
		String refseq;
		if (!isProtein) {
			if (isAllIso) {
				if (refseqFileAllIso == null || refseqFileAllIso.trim().equals("")) {
					if (getChromSeqFile() == null) {
						return null;
					}
					refseqFileAllIso = getSpeciesPath() + "refrna/rnaAllIso_" + version + ".fa";
				}
				refseq = refseqFileAllIso;
			} else {
				if (refseqFileOneIso == null || refseqFileOneIso.trim().equals("")) {
					if (getChromSeqFile() == null) {
						return null;
					}
					refseqFileOneIso = getSpeciesPath() + "refrna/rnaOneIso_" + version + ".fa";
				}
				refseq = refseqFileOneIso;
			}
		} else {
			if (isAllIso) {
				if (refProFileAllIso == null || refProFileAllIso.trim().equals("")) {
					if (getChromSeqFile() == null) {
						return null;
					}
					refProFileAllIso = getSpeciesPath() + "refprotein/proteinAllIso_" + version + ".fa";
				}
				refseq = refProFileAllIso;
			} else {
				if (refProFileOneIso == null || refProFileOneIso.trim().equals("")) {
					if (getChromSeqFile() == null) {
						return null;
					}
					refProFileOneIso = getSpeciesPath() + "refprotein/proteinOneIso_" + version + ".fa";
				}
				refseq = refProFileOneIso;
			}
		}
		return refseq;
	}
	
	/** 设定相对路径 */
	public void setRefseqNCfile(String refseqNCfile) {
		this.refseqNCfile = refseqNCfile;
	}
	public String getRefseqNCfile() {
		if (refseqNCfile == null || refseqNCfile.equals("")) {
			return null;
		}
		return pathParent + refseqNCfile;
	}
	
	/**
	 * 是否物种特异性的提取
	 * @param speciesSpecific
	 * @return
	 */
	public String getRfamFile(boolean speciesSpecific) {
		String node = "rfam/";
		String speciesPath = pathParent + node + getSpeciesPathWithoutRoot();
		String rfamFile = null;
		if (speciesSpecific) {
			rfamFile = speciesPath + "rfamFile";
		} else {
			rfamFile = speciesPath + "rfamFileAll";
		}
		if (!FileOperate.isFileExistAndBigThanSize(rfamFile,10)) {
			FileOperate.createFolders(FileOperate.getParentPathName(rfamFile));
			ExtractSmallRNASeq extractSmallRNASeq = new ExtractSmallRNASeq();
			//TODO 
			//这里提取的是全体rfam序列
			if (speciesSpecific) {
				extractSmallRNASeq.setRfamFile(PathDetailNBC.getRfamSeq(), taxID);
			} else {
				extractSmallRNASeq.setRfamFile(PathDetailNBC.getRfamSeq(), 0);
			}
			extractSmallRNASeq.setOutRfamFile(rfamFile);
			extractSmallRNASeq.getSeq();
		}
		return rfamFile;
	}
	
	public void save() {
		ManageSpecies.getInstance().saveSpeciesFile(this);
	}
	
	/**
	 * 仔仔细细的全部比较一遍，方便用于数据库升级
	 * @param obj
	 * @return
	 */
	public boolean equalsDeep(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		
		if (getClass() != obj.getClass()) return false;
		SpeciesFile otherObj = (SpeciesFile)obj;
		if (mapChrID2ChrLen.equals(otherObj.mapChrID2ChrLen)
			&& compareMapStrArray(mapDB2GffTypeAndFile, otherObj.mapDB2GffTypeAndFile)
			&& mapGffDBLowCase2DBNormal.equals(otherObj.mapGffDBLowCase2DBNormal)
			&& mapSoftware2IndexChrom.equals(otherObj.mapSoftware2IndexChrom)
			&& mapSoftware2IndexRefAllIso.equals(otherObj.mapSoftware2IndexRefAllIso)
			&& mapSoftware2IndexRefOneIso.equals(otherObj.mapSoftware2IndexRefOneIso)
			&& ArrayOperate.compareString(chromSeq, otherObj.chromSeq)
			&& ArrayOperate.compareString(gffRepeatFile, otherObj.gffRepeatFile)
			&& this.publishYear == otherObj.publishYear
			&& ArrayOperate.compareString(this.refseqFileOneIso, otherObj.refseqFileOneIso)
			&& ArrayOperate.compareString(this.refseqFileAllIso, otherObj.refseqFileAllIso)
			&&ArrayOperate.compareString( this.refseqNCfile, otherObj.refseqNCfile)
			&& this.taxID == otherObj.taxID
			&& ArrayOperate.compareString(this.version, otherObj.version)
			)
		{
			return true;
		}
		return false;
	}
	
	/** 返回本version的物种的目录，类似genome/cangshu_hamsters/CriGri_1.0/
	 * 结尾带"/"
	 * 是相对路径
	 * @return
	 */
	private String getSpeciesPath() {
		String file = chromSeq;
		if (file == null || file.equals("")) file = getGffDB2GffTypeFile()[2];
		if (file == null || file.equals("")) file = refseqFileAllIso;
		if (file == null || file.equals("")) file = refseqFileOneIso;
		if (file == null || file.equals("")) return null;
		
		return FileOperate.getParentPathName(FileOperate.getParentPathName(file));
		
	}
	
	private<T, K> boolean compareMapStrArray(Map<T, K[]> map1, Map<T, K[]> map2) {
		if (map1 == null && map2 != null || map1 != null && map2 == null) {
			return false;
		}
		if (map1 == null && map2 == null) {
			return true;
		}
        if (map1.size() != map2.size())
            return false;

        try {
            Iterator<Entry<T, K[]>> i = map1.entrySet().iterator();
            while (i.hasNext()) {
                Entry<T, K[]> e = i.next();
                T key = e.getKey();
                K[] value = e.getValue();
                if (value == null) {
                    if (!(map2.get(key)==null && map2.containsKey(key)))
                        return false;
                } else {
                	K[] value2 = map2.get(key);
                	if (value2.length != value.length) {
						return false;
					}
                	for (int j = 0; j < value.length; j++) {
						if (!value[j].equals(value2[j])) {
							 return false;
						}
					}                       
                }
            }
        } catch (ClassCastException unused) {
            return false;
        } catch (NullPointerException unused) {
            return false;
        }
        return true;
	}
	
	public static List<SpeciesFile> findByTaxID(int taxID) {
		IManageSpecies manageSpecies = ManageSpecies.getInstance();
		return manageSpecies.queryLsSpeciesFile(taxID);
	}
	public static SpeciesFile findByTaxIDVersion(int taxID, String version) {
		IManageSpecies manageSpecies = ManageSpecies.getInstance();
		return manageSpecies.querySpeciesFile(taxID, version);
	}
	/** 提取小RNA的一系列序列 */
	static public class ExtractSmallRNASeq {
		String RNAdataFile = "";
		/** 用于在mir.dat文件中查找miRNA的物种名 */
		String miRNAdataSpeciesName = "";
		
		/** 提取ncRNA的正则表达式 */
		String regxNCrna  = "NR_\\d+|XR_\\d+";
		/** refseq的序列文件，要求是NCBI下载的文件 */
		String refseqFile = "";


		/** Rfam的正则 */
		int taxIDfram = 0;
		/** Rfam的名字regx */
		String regxRfamWrite = "(?<=\\>)\\S+";
		/** rfam的文件 */
		String rfamFile = "";
		
		/** 提取的rfam的文件 */
		String outRfamFile;
		/** 提取到的目标文件夹和前缀 */
		String outPathPrefix = "";
		String outHairpinRNA;
		String outMatureRNA;
		/** 从RefSeq中提取的ncRNA序列 */
		String outNcRNA;
		
		/**  需要提取的miRNA的名字，都为小写 */
		Set<String> setMiRNAname;
		
		public void setLsMiRNAname(Collection<String> lsMiRNAname) {
			setMiRNAname = new LinkedHashSet<String>();
			for (String string : lsMiRNAname) {
				setMiRNAname.add(string.toLowerCase());
			}
		}
		/**
		 * 设定输出文件夹和前缀，这个设定了就不用设定别的了
		 * @param outPathPrefix
		 */
		public void setOutPathPrefix(String outPathPrefix) {
			this.outPathPrefix = outPathPrefix;
		}
		
		public void setOutNcRNA(String outNcRNA) {
			this.outNcRNA = outNcRNA;
		}
		public void setOutHairpinRNA(String outHairpinRNA) {
			this.outHairpinRNA = outHairpinRNA;
		}
		public void setOutMatureRNA(String outMatureRNA) {
			this.outMatureRNA = outMatureRNA;
		}
		public void setOutRfamFile(String outRfamFile) {
			this.outRfamFile = outRfamFile;
		}
		/**
		 * @param rnaDataFile
		 * @param speciesName 物种的拉丁名 两个单词
		 */
		public void setMiRNAdata(String rnaDataFile, String speciesName) {
			this.RNAdataFile = rnaDataFile;
			this.miRNAdataSpeciesName = speciesName;
		}
		/**
		 * 待提取的NCBI上下载的refseq文件
		 * @param refseqFile
		 */
		public void setRefseqFile(String refseqFile) {
			this.refseqFile = refseqFile;
		}
		/**
		 * 待提取某物中的rfam文件
		 * @param rfamFile
		 * @param regx rfam的物种名
		 */
		public void setRfamFile(String rfamFile, int taxIDrfam) {
			this.rfamFile = rfamFile;
			this.taxIDfram = taxIDrfam;//TODO 看这里是物种的什么名字
		}
		/**
		 * 提取序列
		 */
		public void getSeq() {
			if (FileOperate.isFileExist(refseqFile)) {
				if (outNcRNA == null)
					outNcRNA = outPathPrefix + "_ncRNA.fa";
				try {
					extractNCRNA(refseqFile, outNcRNA, regxNCrna);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("提取ncRNA出错：" + refseqFile);
				}
			}
			
			if (FileOperate.isFileExist(RNAdataFile)) {
				if (outHairpinRNA == null)
					outHairpinRNA = outPathPrefix + "_hairpin.fa";
				if (outMatureRNA == null)
					outMatureRNA = outPathPrefix + "_mature.fa";
				
				ListMiRNAdat listMiRNALocation = new ListMiRNAdat();
				try {
					listMiRNALocation.extractMiRNASeqFromRNAdata(setMiRNAname, miRNAdataSpeciesName, RNAdataFile, outHairpinRNA, outMatureRNA);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("提取miNRA出错：" + miRNAdataSpeciesName + " " + RNAdataFile);
				}
				listMiRNALocation = null;
			}
			
			if (FileOperate.isFileExist(rfamFile)) {
				if (outRfamFile == null)
					outRfamFile = outPathPrefix + "_rfam.fa";
					
				try {
					extractRfam(rfamFile, outRfamFile, taxIDfram);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("提取rfam出错：" + taxIDfram + " " + rfamFile);
				}
				
			}
		}
		/**
		 * 从NCBI的refseq.fa文件中提取NCRNA
		 * @param refseqFile
		 * @param outNCRNA
		 * @param regx 类似 "NR_\\d+|XR_\\d+";
		 */
		private void extractNCRNA(String refseqFile, String outNCRNA, String regx) {
			if (!FileOperate.isFileExistAndBigThanSize(refseqFile, 0)) {
				return;
			}
			SeqFastaHash seqFastaHash = new SeqFastaHash(refseqFile,regx,false);
			seqFastaHash.writeToFile( regx ,outNCRNA );
			seqFastaHash.close();
		}
		
		private void extractRfam(String rfamFile, String outRfam, int taxIDquery) {
			if (!FileOperate.isFileExistAndBigThanSize(rfamFile, 0)) {
				return;
			}
			if (taxIDquery <= 0) {
				extractRfam(rfamFile, outRfam);
			} else {
				extractRfamTaxID(rfamFile, outRfam, taxIDquery);
			}
		}
		/**
		 * 从rfam.txt文件中提取指定物种的ncRNA序列
		 * @param hairpinFile
		 * @param outNCRNA
		 * @param regx 物种的英文，人类就是Homo sapiens
		 */
		private void extractRfamTaxID(String rfamFile, String outRfam, int taxIDquery) {
			TxtReadandWrite txtOut = new TxtReadandWrite(outRfam, true);
			 SeqFastaHash seqFastaHash = new SeqFastaHash(rfamFile,null,false);
			 seqFastaHash.setDNAseq(true);
			 ArrayList<SeqFasta> lsSeqfasta = seqFastaHash.getSeqFastaAll();
			 for (SeqFasta seqFasta : lsSeqfasta) {
				 int taxID = 0;
				 try {
					 taxID = Integer.parseInt(seqFasta.getSeqName().trim().split(" +")[1].split(":")[0]);
				 } catch (Exception e) {
					 logger.error("本序列中找不到taxID：" + seqFasta.getSeqName());
				 }
				 
				 if (taxID == taxIDquery) {
					 SeqFasta seqFastaNew = seqFasta.clone();
					 String name = seqFasta.getSeqName().trim().split(" +")[0];
					 name = name.replace(";", "//");
					 seqFastaNew.setName(name);
					 txtOut.writefileln(seqFastaNew.toStringNRfasta());
				 }
			 }
			 txtOut.close();
			 seqFastaHash.close();
		}
		
		/**
		 * 修正rfam.txt文件中提取指定物种的ncRNA序列
		 * @param hairpinFile
		 * @param outNCRNA
		 * @param regx 物种的英文，人类就是Homo sapiens
		 */
		private void extractRfam(String rfamFile, String outRfam) {
			TxtReadandWrite txtRead = new TxtReadandWrite(rfamFile, false);
			TxtReadandWrite txtWrite = new TxtReadandWrite(outRfam, true);

			HashMap<String, Integer> map = new HashMap<String, Integer>();
			for (String string : txtRead.readlines()) {
				if (string.startsWith(">")) {
					string = string.split(" +")[0].replace(";", "//");
					if (map.containsKey(string)) {
						int tmp = map.get(string) + 1;
						map.put(string, tmp);
					} else {
						map.put(string, 0);
					}
					int tmpNum = map.get(string);
					if (tmpNum == 0) {
						txtWrite.writefileln(string);
					} else {
						txtWrite.writefileln(string + "_new" + tmpNum);
					}
				} else {
					string = string.replace("U", "T");
					txtWrite.writefileln(string);
				}
			}
			txtRead.close();
			txtWrite.close();
		}
		
	}
	
}

/** 比较GFFDB的比较器 */
class CompGffDB implements Comparator<String> {
	public int compare(String o1, String o2) {
		if (o1.equalsIgnoreCase(o2)) {
			return 0;
		}
		if (o1.equalsIgnoreCase("ucsc")) {
			return 1;
		} else if (o2.equalsIgnoreCase("ucsc")) {
			return -1;
		} else if (o1.equalsIgnoreCase("ncbi") && !o2.equalsIgnoreCase("ucsc")) {
			return 1;
		} else if (!o1.equalsIgnoreCase("ucsc") && o2.equalsIgnoreCase("ncbi")) {
			return -1;
		}
		return o1.compareTo(o2);
	}
}
