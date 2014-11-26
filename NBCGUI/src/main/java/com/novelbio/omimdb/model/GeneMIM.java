package com.novelbio.omimdb.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.novelbio.springonly.factory.SpringFactory;
import com.novelbio.omimdb.mongorepo.RepoGeneMIMInfo;
import com.novelbio.omimdb.mongorepo.RepoGenemap;

@Document(collection = "omimGene")
public class GeneMIM implements Serializable {

	@Id
	private String id;
	/** gene MIM 号 */
	@Indexed
	private int geneMimId;
	/** gene ID */
	private int geneId;
	/** Cytogenetic location */
	private String cytLoc;
	/** mapping gene method */
	private String mapGenMet;
	
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
	public void setMapGenMet(String mapGenMet) {
		this.mapGenMet = mapGenMet;
	}
	public String getMapGenMet() {
		return mapGenMet;
	}
	private static RepoGeneMIMInfo repo() {
		 return SpringFactory.getBean(RepoGeneMIMInfo.class);
		 
	 }
	 
	 public static GeneMIM findGeneInfByMimId(String id) {
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
