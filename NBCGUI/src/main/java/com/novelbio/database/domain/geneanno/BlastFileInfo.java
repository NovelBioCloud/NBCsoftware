package com.novelbio.database.domain.geneanno;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.novelbio.base.dataOperate.DateUtil;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.database.model.species.Species;

/**
 * 记录blast信息是否为
 * @author zong0jie
 *
 */
@Document(collection = "blastfileinfo")
public class BlastFileInfo {
	@Id
	String id;
	@Indexed
	String fileName;
	@Indexed
	boolean isTmp;
	String usrid;
	@Indexed
	String queryTaxID;
	@Indexed
	int subjectTaxID;
	
	/** 文件的日期 */
	String dateBlastFile;
	/** 导入日期 */
	String dateImport = DateUtil.getDateDetail();
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
		dateBlastFile = FileOperate.getTimeLastModifyStr(fileName);
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}
	/** 上传人 */
	public void setUserID(String userid) {
		if (userid == null || userid.equals("")) return;
		
		this.usrid = userid;
	}
	public void setTmp(boolean isTmp) {
		this.isTmp = isTmp;
	}
	
	public String getDateBlastFile() {
		return dateBlastFile;
	}
	
	public String getDateImport() {
		return dateImport;
	}
	public void setQueryTaxID(String queryTaxID) {
		this.queryTaxID = queryTaxID;
	}
	public void setQueryTaxID(int queryTaxID) {
		this.queryTaxID = queryTaxID + "";
	}
	public void setSubjectTaxID(int subjectTaxID) {
		this.subjectTaxID = subjectTaxID;
	}
	
	public String getFileName() {
		return fileName;
	}
	public boolean isTmp() {
		return isTmp;
	}
	public String getQueryTaxID() {
		return queryTaxID;
	}
	public String getQueryLatinName() {
		String name = null;
		try {
			int taxID = Integer.parseInt(queryTaxID);
			Species species = new Species(taxID);
			name = species.getNameLatin();
		} catch (Exception e) {}
		
		if (name == null || name.equals("")) {
			name = queryTaxID;
		}
		return name;
	}

	public int getSubjectTaxID() {
		return subjectTaxID;
	}
	
	public String getSubjectLatinName() {
		Species species = new Species(subjectTaxID);
		String name = species.getNameLatin();
		
		if (name == null || name.equals("")) {
			name = queryTaxID;
		}
		return name;
	}

}
