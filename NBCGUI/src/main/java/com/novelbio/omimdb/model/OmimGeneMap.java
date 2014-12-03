package com.novelbio.omimdb.model;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.novelbio.base.StringOperate;
import com.novelbio.database.service.SpringFactory;
import com.novelbio.omimdb.mongorepo.RepoGenemap;

@Document(collection = "omimGenemap")
public class OmimGeneMap implements Serializable {
	@Id
	private String id;
	
	/** phenotype MIM 号 */
	@Indexed(unique = false)
	private int phenMimId;
	/** gene MIM 号 */
	private int genMimId;
	/** 记录时间 */
	private String recordTime;
	/** phenotype 描述 */
	private String phenDec;
	/** phenotype mapping 方法 */
	private Set<String> setPhenMapMeth;
	/** Mouse correlate */
	private String mouCorr;

	public int getPhenMimId() {
		return phenMimId;
	}

	/** phenotype MIM 号 */
	public void setPhenMimId(int phenMimId) {
		this.phenMimId = phenMimId;
	}

	/** gene MIM 号 */
	public int getGenMimId() {
		return genMimId;
	}

	/** gene MIM 号 */
	public void setGenMimId(int genMimId) {
		this.genMimId = genMimId;
	}

	/** 记录时间 */
	public String getRecordTime() {
		return recordTime;
	}

	/** 记录时间 */
	public void setRecordTime(String recordTime) {
		this.recordTime = recordTime;
	}

	/**  phenotype 描述  */
	public String getPhenDec() {
		return phenDec;
	}

	/**  phenotype 描述  */
	public void setPhenDec(String phenDec) {
		this.phenDec = phenDec;
	}

	/** phenotype mapping 方法 */
	public Set<String> getSetPhenMapMeth() {
		return setPhenMapMeth;
	}

	/** phenotype mapping 方法 */
	public void addPhenMapMeth(String phenMapMeth) {
		if (setPhenMapMeth == null) {
			setPhenMapMeth = new HashSet<String>();
		}
		this.setPhenMapMeth.add(phenMapMeth);
	}

	/** Mouse correlate */
	public String getMouCorr() {
		return mouCorr;
	}

	/** phenotype mapping 方法 */
	public void setMouCorr(String mouCorr) {
		this.mouCorr = mouCorr;
	}
	
	 private static RepoGenemap repo() {
		 return SpringFactory.getFactory().getBean(RepoGenemap.class);
		 
	 }
	 
	 public static OmimGeneMap findGeneInfByMimId(String id) {
	 return repo().findOne(id);
	 }

	 public boolean remove() {
		 try {
			 repo().delete(id);
		 } catch (Exception e) {
			 return false;
		 }
		 return true;
	 }
}
