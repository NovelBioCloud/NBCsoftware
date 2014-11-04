package com.novelbio.omimdb.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "omimGene")
public class GeneMIM implements Serializable {

	/** gene ID */
	@Id
	private String geneId;
	/** gene MIM 号 */
	private String geneMimId;
	/** Cytogenetic location */
	private String cytLoc;
	/** mapping gene method */
	private String mapGenMet;
	
	public void setGeneId(String geneId) {
		this.geneId = geneId;
	}
	public String getGeneId() {
		return geneId;
	}
	public void setGeneMimId(String geneMimId) {
		this.geneMimId = geneMimId;
	}
	public String getGeneMimId() {
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
}
