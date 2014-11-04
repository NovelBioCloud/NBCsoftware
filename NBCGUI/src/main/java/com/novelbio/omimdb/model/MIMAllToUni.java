package com.novelbio.omimdb.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "omimIDAllToUni")
public class MIMAllToUni  implements Serializable {

	/** 所有的MIM ID */
	@Id
	private String allMIMId;
	/** Unique MIM 号 */
	private String uniMIMId;

	public void setAllMIMId(String allMIMId) {
		this.allMIMId = allMIMId;
	}
	public String getAllMIMId() {
		return allMIMId;
	}
	public void setUniMIMId(String uniMIMId) {
		this.uniMIMId = uniMIMId;
	}
	public String getUniMIMId() {
		return allMIMId;
	}
	
}
