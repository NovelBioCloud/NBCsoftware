package com.novelbio.database.domain.geneanno;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.novelbio.analysis.annotation.blast.BlastStatistics;
import com.novelbio.base.SepSign;
import com.novelbio.base.dataOperate.DateUtil;
import com.novelbio.base.dataOperate.TxtReadandWrite;
import com.novelbio.database.model.modgeneid.GeneID;
import com.novelbio.database.service.servgeneanno.ManageBlastInfo;

/**
 * <b>导入数据库时会</b>将queryID和subjectID都<b>转变为小写</b><p>
 * 导入数据库的时候数据库中必须<b>已经存在了subjectID</b><br>
 * 如果数据库中不存在queryID，而又需要导入，则先导入queryID，再导入blast信息<p>
 * Comparable接口实现为按照evalue从小到大排序
 * @author zong0jie
 */
@Document(collection = "blastInfo")
@CompoundIndexes({
    @CompoundIndex(unique = true, name = "queryID_queryTax_subTax_subID_idx",
    		def = "{'queryID': 1, 'queryTax': -1 , 'subjectTax': 1, 'subjectID': -1}")
 })
public class BlastInfo implements Comparable<BlastInfo> {
	@Id
	private String id;
	private String queryID;
	@Indexed
	private String subjectID;
	
	private String blastDate;

	private double identities = 0;
	private double evalue = 100;
	private double score = 0;
	private int alignLen = 0;
	
	protected int queryTax;
	protected int queryIDtype;
	protected int subjectTax;
	private int subjectIDtype;
	
	private int mismatchNum;
	private int gapNum;
	
	/** query比对到的起点 */
	private int qStartLoc;
	/** query比对到的终点 */
	private int qEndLoc;
	/** subject比对到的起点 */
	private int sStartLoc;
	/** subject比对到的终点 */
	private int sEndLoc;
	
	@Indexed
	private String blastFileId;
	
	@Transient
	GeneID geneIDS = null;
	
	public BlastInfo() {
		setDate();
	}
	
	/**
	 * 用 <b>-m8</b> 参数跑出来的Blast程序跑出来的结果<br>
	 * 默认<b>读取</b>数据库并初始化
	 * @param taxIDQ queryTaxID
	 * @param taxIDS subjectTaxID
	 * @param blastStr blast的具体ID
	 * @param isBlastIDtype 如果subjecdt是accID，具体的accID是否类似 blast的结果，如：dbj|AK240418.1|，那么获得AK240418，一般都是false
	 */
	public BlastInfo( int taxIDQ, int taxIDS, String blastStr) {
		this(true, taxIDQ, false, taxIDS, false, blastStr);
	}
	
	/**
	 * 用 <b>-m8</b> 参数跑出来的Blast程序跑出来的结果<br>
	 * 默认<b>不</b>读取数据库<b>不进行</b>初始化
	 * @param taxIDQ queryTaxID
	 * @param taxIDS subjectTaxID
	 * @param blastStr blast的具体ID
	 * @param isBlastIDtype 如果subjecdt是accID，具体的accID是否类似 blast的结果，如：dbj|AK240418.1|，那么获得AK240418，一般都是false
	 */
	public BlastInfo(String blastStr) {
		this(false, 0, false, 0, false, blastStr);
	}
	
	/**
	 * 用 <b>-m8</b> 参数跑出来的Blast程序跑出来的结果
	 * 默认输入的是accID
	 * @param queryDB 是否读取数据库并初始化
	 * @param taxIDQ queryTaxID
	 * @param taxIDS subjectTaxID
	 * @param blastStr blast的具体ID
	 */
	public BlastInfo(boolean queryDB, int taxIDQ, int taxIDS, String blastStr) {
		this(queryDB, taxIDQ, true, taxIDS, true, blastStr);
	}
	
	/**
	 * 用 <b>-m8</b> 参数跑出来的Blast程序跑出来的结果
	 * @param queryDB 是否读取数据库并初始化
	 * @param taxIDQ queryTaxID
	 * @param isAccIDQ queryID是否为accID，一般都是true
	 * @param taxIDS subjectTaxID
	 * @param isAccIDS subjectID是否为accID，一般都是true
	 * @param blastStr blast的具体某一行的内容
	 */
	public BlastInfo(boolean queryDB, int taxIDQ, boolean isAccIDQ, int taxIDS, boolean isAccIDS, String blastStr) {
		setDate();
		String[] blastInfo = blastStr.split("\t");
		if (queryDB) {
			query(taxIDQ, isAccIDQ, taxIDS, isAccIDS, blastInfo);
		} else {
			queryID = blastInfo[0];
			if (blastInfo[1].contains("|")) {
				subjectID = GeneID.getBlastAccID(blastInfo[1]);
			} else {
				subjectID = blastInfo[1];
			}
			
			this.queryTax = taxIDQ;
			this.subjectTax = taxIDS;
		}
		this.identities = Double.parseDouble(blastInfo[2].trim());
		this.alignLen = Integer.parseInt(blastInfo[3].trim());
		this.mismatchNum = Integer.parseInt(blastInfo[4]);
		this.gapNum = Integer.parseInt(blastInfo[5]);
		this.qStartLoc = Integer.parseInt(blastInfo[6]);
		this.qEndLoc = Integer.parseInt(blastInfo[7]);
		this.sStartLoc = Integer.parseInt(blastInfo[8]);
		this.sEndLoc = Integer.parseInt(blastInfo[9]);

		this.evalue = Double.parseDouble(blastInfo[10].trim());

		this.score = Double.parseDouble(blastInfo[11].trim());
	}
	
	private void query(int taxIDQ, boolean isAccIDQ, int taxIDS, boolean isAccIDS, String[] blastInfo) {
		GeneID geneIDQ;
		if (isAccIDQ) {
			geneIDQ = new GeneID(blastInfo[0], taxIDQ);
		} else {
			geneIDQ = new GeneID(GeneID.IDTYPE_GENEID, blastInfo[0], taxIDQ);
			if (geneIDQ.getAccID_With_DefaultDB() == null) {
				geneIDQ = new GeneID(GeneID.IDTYPE_UNIID, blastInfo[0], taxIDQ);
			}
		}
		
		if (isAccIDS) {
			geneIDS = new GeneID(blastInfo[1], taxIDS, blastInfo[1].contains("|"));
		} else {
			geneIDS = new GeneID(GeneID.IDTYPE_GENEID, blastInfo[1], taxIDS);
			if (geneIDS.getAccID_With_DefaultDB() == null) {
				geneIDS = new GeneID(GeneID.IDTYPE_UNIID, blastInfo[1], taxIDS);
			}
		}
		this.queryID = geneIDQ.getGeneUniID();
		this.queryTax = geneIDQ.getTaxID();
		this.queryIDtype = geneIDQ.getIDtype();
		
		this.subjectID = geneIDS.getGeneUniID();
		this.subjectTax = geneIDS.getTaxID();
		this.subjectIDtype = geneIDS.getIDtype();
	}
	
	/**
	 * 仅仅获得geneIDQ的geneUniID, taxID ,IDtype
	 * @param queryID
	 * @param queryTax
	 * @param queryIDtype {@link GeneID#IDTYPE_ACCID} 等
	 */
	public void setGeneIDQ(String queryID, int queryTax, int queryIDtype) {
		this.queryID = queryID;
		this.queryTax = queryTax;
		this.queryIDtype = queryIDtype;
	}
	public void setGeneIDS(GeneID geneIDS) {
		this.geneIDS = geneIDS;
		this.subjectID = geneIDS.getGeneUniID();
		this.subjectTax = geneIDS.getTaxID();
		this.subjectIDtype = geneIDS.getIDtype();
	}
	public void convertQuerySubjectID_To_Lowcase() {
		this.queryID = queryID.toLowerCase();
		this.subjectID = subjectID.toLowerCase();
	}
	/** mongodb中的id */
	public void setId(String id) {
		this.id = id;
	}
	/** mongodb中的id */
	public String getId() {
		return id;
	}
	
	private void setDate() {
	     blastDate = DateUtil.getDate(); //将日期时间格式化
	}
	
	public GeneID getGeneIDS() {
		if (geneIDS == null) {
			geneIDS = new GeneID(subjectIDtype, subjectID, subjectTax);
		}
		return geneIDS;
	}
	
	/** 必须是有ID的blastFileInfo */
	public void setBlastFileInfo(BlastFileInfo blastFileInfo) {
		this.blastFileId = blastFileInfo.getId();
	}
	
	/**
	 * 两个一起设定比较方便
	 * @param evalue
	 * @param identities
	 */
	public void setEvalue_Identity(double evalue, double identities) {
		this.evalue = evalue;
		this.identities = identities;
	}
	
	/** 获得搜到的序列ID */
	public String getQueryID() {
		return this.queryID.trim();
	}
	
	/** 设置查找序列的物种 */
	public int getQueryTax() {
		return this.queryTax;
	}
	
	public int getQueryIDtype() {
		return queryIDtype;
	}
	
	/**
	 * 获得Blast搜索到的序列的ID
	 */
	public String getSubjectID() {
		return this.subjectID;
	}
	public BlastFileInfo getBlastFileInfo() {
		if (blastFileId == null) {
			return null;
		}
		ManageBlastInfo manageBlastInfo = ManageBlastInfo.getInstance();
		return manageBlastInfo.findBlastFile(blastFileId);
	}
	/**
	 * 获得Blast搜索到的序列的物种
	 */
	public int getSubjectTax() {
		return this.subjectTax;
	}
	
	public int getSubjectIDtype() {
		return subjectIDtype;
	}
	
	/** query序列比对上的终点  */
	public int getqEndLoc() {
		return qEndLoc;
	}
	/** query序列比对上的起点 */
	public int getqStartLoc() {
		return qStartLoc;
	}
	/** subject序列比对上的终点  */
	public int getsEndLoc() {
		return sEndLoc;
	}
	/** subject序列比对上的起点，如果该值大于{@link #getsEndLoc()}
	 * 表示blast到了反向互补序列上
	 *  */
	public int getsStartLoc() {
		return sStartLoc;
	}
	/** 比对到了反向互补序列上 */
	public boolean isCis5to3() {
		return sStartLoc <= sEndLoc;
	}
	public int getSStartAbs() {
		return Math.min(sStartLoc, sEndLoc);
	}
	public int getSEndAbs() {
		return Math.max(sStartLoc, sEndLoc);
	}
	/**
	 * 获得查找的相似度,初值为0
	 */
	public double getIdentities() {
		return this.identities;
	}
	public int getMismatchNum() {
		return mismatchNum;
	}
	public int getGapNum() {
		return gapNum;
	}
	/**
	 * 获得查找的evalue，初值为100
	 */
	public double getEvalue() {
		return this.evalue;
	}

	/**
	 * 获得查找的日期
	 */
	public String getBlastDate() {
		return this.blastDate;
	}

	public int getAlignLen() {
		return alignLen;
	}

	public double getScore() {
		return score;
	}
	
	/**
	 * 按照相似度排序
	 * -1 表示更可信
	 */
	@Override
	public int compareTo(BlastInfo o) {
		Double evalueThis = evalue;
		Double evalueO = o.getEvalue();
		Double identityThis = identities;
		Double identityO = o.identities;
		Double scoreThis = score;
		Double scoreO = o.score;
		int result = -scoreThis.compareTo(scoreO);
		if (result == 0) {
			result = -identityThis.compareTo(identityO);
		}
		if (result == 0) {
			result = evalueThis.compareTo(evalueO);
		}
		return result;
	}
	
	public static BlastStatistics getHistEvalue(List<BlastInfo> lsBlastinfos) {
		BlastStatistics blastStatistics = new BlastStatistics();
		blastStatistics.setLsBlastinfos(lsBlastinfos);
		return blastStatistics;
	}
	
	/** 从blast文件中读取blast信息，不查数据库 */
	public static List<BlastInfo> readBlastFile(String blastFile) {
		List<BlastInfo> lsBlastInfos = new ArrayList<>();
		TxtReadandWrite txtRead = new TxtReadandWrite(blastFile);
		for (String content : txtRead.readlines()) {
			lsBlastInfos.add(new BlastInfo(content));
		}
		txtRead.close();
		return lsBlastInfos;
	}
	
	/** 将一系列blastInfo的结果去重复，一个geneID仅挑选evalue最小的那个blastTo */
	public static List<BlastInfo> removeDuplicateQueryID(Collection<BlastInfo> colBlastInfos) {
		 Map<String, BlastInfo> mapQuery2Evalue = new HashMap<>();
		for (BlastInfo blastInfo : colBlastInfos) {
			String queryID = blastInfo.getQueryID().toLowerCase();
			double evalue = blastInfo.getEvalue();
			if (mapQuery2Evalue.containsKey(queryID) && mapQuery2Evalue.get(queryID).getEvalue() <= evalue) {
				continue;
			}
			mapQuery2Evalue.put(queryID, blastInfo);
		}
		return new ArrayList<>(mapQuery2Evalue.values());
	}
	
	/** 将一系列blastInfo的结果去重复，一对query和subject仅获取evalue最小的那一对 */
	public static List<BlastInfo> removeDuplicate(Collection<BlastInfo> colBlastInfos) {
		 Map<String, BlastInfo> mapQuery2Evalue = new LinkedHashMap<>();
		for (BlastInfo blastInfo : colBlastInfos) {
			String queryID2SubID = blastInfo.getQueryID().toLowerCase() + SepSign.SEP_ID + blastInfo.getSubjectID().toLowerCase();
			double evalue = blastInfo.getEvalue();
			if (mapQuery2Evalue.containsKey(queryID2SubID) && mapQuery2Evalue.get(queryID2SubID).getEvalue() <= evalue) {
				continue;
			}
			mapQuery2Evalue.put(queryID2SubID, blastInfo);
		}
		return new ArrayList<>(mapQuery2Evalue.values());
	}
}
