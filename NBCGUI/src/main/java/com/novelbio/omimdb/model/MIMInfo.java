package com.novelbio.omimdb.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "omimInfo")
public class MIMInfo implements Serializable {

	/** MIM ID */
	@Id
	private String mimId;
	/** MIM Title 号 */
	private String mimTitle;
	/** MIM description */
	private String descrption;
	/** Other information */
	private String othInfor;
	
	public void setMimId(String mimId) {
		this.mimId = mimId;
	}
	public String getMimId() {
		return mimId;
	}
	public void setMimTitle(String mimTitle) {
		this.mimTitle = mimTitle;
	}
	public String getMimTitle() {
		return mimTitle;
	}
	public void setDescrption(String descrption) {
		this.descrption = descrption;
	}
	public String getDescrption() {
		return descrption;
	}
	public void setOthInfor(String othInfor) {
		this.othInfor = othInfor;
	}
	public String getOthInfor() {
		return descrption;
	}
	
	
}
