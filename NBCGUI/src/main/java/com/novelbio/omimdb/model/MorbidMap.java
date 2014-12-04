package com.novelbio.omimdb.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.novelbio.omimdb.mongorepo.RepoMorbidMap;
import com.novelbio.springonly.factory.SpringFactory;

@Document(collection = "morbidMap")
public class MorbidMap implements Serializable {
	@Id
	private String id;
	/** gene MIM 号 */
	@Indexed
	private int geneId;
	/** gene ID */
	private int geneMimId;
	/** 遗传学位置 */
	private String cytLoc;
	/** 孟德尔表型MIM ID */
	private int phenMimId;
	/** 疾病信息 */
	private List<String> listDis;
	/** 疾病信息 类型
	 * 1 the disorder was positioned by mapping of the wildtype gene;
	 * 2 the disease phenotype itself was mapped;
	 * 3 the molecular basis of the disorder is known; 
	 * 4 the disorder is a chromosome deletion or duplication syndrome;
	 * */
	private int disType;
	
	public void setGeneId(int geneId) {
		this.geneId = geneId;
	}
	public int getGeneId() {
		return geneId;
	}
	public void setGeneMimId(int geneMimId) {
		this.geneMimId = geneMimId;
	}
	public int getGeneMimId() {
		return geneMimId;
	}
	public void setCytLoc(String cytLoc) {
		this.cytLoc = cytLoc;
	}
	public String getCytLoc() {
		return cytLoc;
	}
	public void setPheneMimId(int phenMimId) {
		this.phenMimId = phenMimId;
	}
	public int getPheneMimId() {
		return phenMimId;
	}
	public List<String> getListDis() {
		return listDis;
	}
	public void addDis(String dis) {
		if (listDis == null) {
			listDis = new ArrayList<String>();
		}
		this.listDis.add(dis);
	}
	public void setDisType(int disType) {
		this.disType = disType;
	}
	public int getDisType() {
		return disType;
	}
	private static RepoMorbidMap repo() {
		 return SpringFactory.getBean(RepoMorbidMap.class);
	 }
	 public static MorbidMap findInfByGeneId(int id) {
		 return repo().findOne(id);
	 }
		/** 根据完成情况查询分配的任务 */
		public static List<MorbidMap> findInfByDisease(String disease) {
			return repo().findInfByDisease(disease);
		}

//	 public boolean remove() {
//		 try {
//			 repo().delete(id);
//		 } catch (Exception e) {
//			 return false;
//		 }
//		 return true;
//	 }
}


