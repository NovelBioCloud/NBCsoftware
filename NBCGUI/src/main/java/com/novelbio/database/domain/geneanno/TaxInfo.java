package com.novelbio.database.domain.geneanno;

import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.novelbio.analysis.seq.mirna.ListMiRNAdat;
import com.novelbio.database.service.servgeneanno.IManageSpecies;
import com.novelbio.database.service.servgeneanno.ManageSpecies;
import com.novelbio.generalConf.PathDetailNBC;

/**
 * 有关taxID的表格
 * @author zong0jie
 */
@Document(collection = "taxinfo")
public class TaxInfo implements Cloneable {
	/** NCBI的物种ID */
	@Id
	private int taxID;
	/** KEGG上的缩写 */
	@Indexed
	private String abbr;
	/** 拉丁名 */
	private String latin;
	/** 常用名 */
	private String comName;
	/** 中文名 */
	@Indexed
	private String chnName;
	/** 是否有miRNA */
	private Boolean isHaveMiRNA;
	/**
	 * NCBI的物种ID
	 * @param taxID
	 */
	public void setTaxID(int taxID) {
		if (taxID == 0) {
			return;
		}
		this.taxID=taxID;
	}
	/** NCBI的物种ID */
	public int getTaxID() {
		return this.taxID;
	}
	/** KEGG上的缩写 */
	public void setAbbr(String abbr) {
		if (abbr == null) {
			return;
		}
		this.abbr=abbr.trim().toLowerCase();
	}
	/** KEGG上的缩写 */
	public String getAbbr() {
		if (abbr == null) {
			return "";
		}
		return this.abbr;
	}
	/** 拉丁名 */
	public void setLatin(String latin) {
		if (latin == null) {
			return;
		}
		this.latin=latin.trim();
	}
	/** 拉丁名 */
	public String getLatin() {
		if (latin == null) {
			return "";
		}
		return this.latin;
	}
	/**
	 * @param speciesName 物种的拉丁名
	 */
	public String getLatinName_2Word() {
		String result = null;
		String latin = getLatin();
		String[] names = latin.split(" ");
		if (names.length > 1) {
			result = names[0] + " " + names[1];
		} else {
			result = latin;
		}
		return result;
	}
	/** 常用名 */
	public void setComName(String comName) {
		if (comName == null) {
			return;
		}
		this.comName=comName.trim();
	}
	/** 常用名 */
	public String getComName() {
		if (comName == null) {
			return "";
		}
		return this.comName;
	}
	/** 中文名 */
	public void setChnName(String chnName) {
		if (chnName == null) {
			return;
		}
		this.chnName=chnName.trim();
	}
	/** 中文名 */
	public String getChnName() {
		if (chnName == null) {
			return "";
		}
		return this.chnName;
	}
	
	public void setIsHaveMiRNA(Boolean isHaveMiRNA) {
		this.isHaveMiRNA = isHaveMiRNA;
	}
	
	public boolean isHaveMiRNA() {
		if (isHaveMiRNA == null) {
			isHaveMiRNA = ListMiRNAdat.isContainMiRNA(getLatinName_2Word(), PathDetailNBC.getMiRNADat());
		}
		return isHaveMiRNA;
	}

	/**
	 * 返回taxID对常用名
	 * @return
	 */
	public static Map<Integer,String> getMapTaxIDName() {
		IManageSpecies servTaxID = ManageSpecies.getInstance();
		return servTaxID.getMapTaxIDName();
	}

	/**
	 * 不仅仅比较taxID，全部比较一遍
	 * 且比较染色体长度
	 */
	public boolean equals(Object obj) {
		if (this == obj) return true;
		
		if (obj == null) return false;
		
		if (getClass() != obj.getClass()) return false;
		TaxInfo otherObj = (TaxInfo)obj;
		
		if (getAbbr().equals(otherObj.getAbbr())
		&&		
		getChnName().equals(otherObj.getChnName())
		&&
		getComName().equals(otherObj.getComName())
		&&
		getLatin().equals(otherObj.getLatin())
		&&
		getTaxID() == otherObj.getTaxID()
		&&
		isHaveMiRNA() == otherObj.isHaveMiRNA()
		)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * 数据库操作类
	 * @return
	 */
	private static IManageSpecies repo(){
		return ManageSpecies.getInstance();
	}
	
	public boolean save(){
		try {
			repo().saveTaxInfo(this);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 查询物种分页
	 * @param pageable
	 * @return
	 */
	public static Page<TaxInfo> queryLsTaxInfo(Pageable pageable){
		return repo().queryLsTaxInfo(pageable);
	}
	
	public TaxInfo clone() {
		try {
			return (TaxInfo) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 根据物种编号删除物种
	 * @param taxId2
	 */
	public static boolean deleteByTaxId(int taxId2) {
		try {
			repo().deleteByTaxId(taxId2);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
}
