package com.novelbio.database.domain.geneanno;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.novelbio.analysis.annotation.blast.BlastType;
import com.novelbio.base.PageModel;
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
	String userId;
	String userName;
	@Indexed
	String queryTaxID;
	/** query物种的俗名 */
	String queryTaxName;
	@Indexed
	int subjectTaxID;
	/** subject物种的俗名 */
	String subjectTaxName;
	
	/** blast类型 */
	BlastType blastType = BlastType.blastn;
	
	/** 文件的日期 */
	String dateBlastFile;
	long blastFileModifyDate = -1L;
	long impotDate = new Date().getTime();
	/** 导入日期 */
	String dateImport = DateUtil.getDateDetail();
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
		blastFileModifyDate = FileOperate.getTimeLastModify(fileName);
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
	/** query物种的俗名 */
	public String getQueryTaxName() {
		return queryTaxName;
	}
	/** query物种的俗名 */
	public void setQueryTaxName(String queryTaxName) {
		this.queryTaxName = queryTaxName;
	}
	public void setSubjectTaxID(int subjectTaxID) {
		this.subjectTaxID = subjectTaxID;
	}
	/** subject物种的俗名 */
	public String getSubjectTaxName() {
		return subjectTaxName;
	}
	/** subject物种的俗名 */
	public void setSubjectTaxName(String subjectTaxName) {
		this.subjectTaxName = subjectTaxName;
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
//	public String getQueryLatinName() {
//		String name = null;
//		try {
//			int taxID = Integer.parseInt(queryTaxID);
//			Species species = new Species(taxID);
//			name = species.getNameLatin();
//		} catch (Exception e) {}
//		
//		if (name == null || name.equals("")) {
//			name = queryTaxID;
//		}
//		return name;
//	}
	
	/**
	 * 存储管理
	 * @return
	 */
	private static ManageBlastInfo repo(){
		return ManageBlastInfo.getInstance();
	}
	/**
	 * 保存
	 * @return
	 */
	public boolean save() {
		return repo().saveBlastFile(this);
	}
	
	/**
	 * 根据blastID查找
	 * @param id blastID
	 * @return
	 */
	public static BlastFileInfo findInstance(String id){
		return repo().findBlastFile(id);
	}
	
	public static Page<BlastFileInfo> findAll(PageModel pageModel){
		return repo().findAll(pageModel.bePageable());
	}
	public static List<BlastFileInfo> findAll(){
		return repo().findAll();
	}
	
	public int getSubjectTaxID() {
		return subjectTaxID;
	}
	
	public String getUsrid() {
		return usrid;
	}
	public void setUsrid(String usrid) {
		this.usrid = usrid;
	}
	public BlastType getBlastType() {
		return blastType;
	}
	public void setBlastType(BlastType blastType) {
		this.blastType = blastType;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public long getBlastFileModifyDate() {
		return blastFileModifyDate;
	}
	public void setBlastFileModifyDate(long blastFileModifyDate) {
		this.blastFileModifyDate = blastFileModifyDate;
	}
	public long getImpotDate() {
		return impotDate;
	}
	public void setImpotDate(long impotDate) {
		this.impotDate = impotDate;
	}
	public void setDateBlastFile(String dateBlastFile) {
		this.dateBlastFile = dateBlastFile;
	}
	public void setDateImport(String dateImport) {
		this.dateImport = dateImport;
	}
//	public String getSubjectLatinName() {
//		Species species = new Species(subjectTaxID);
//		String name = species.getNameLatin();
//		
//		if (name == null || name.equals("")) {
//			name = queryTaxID;
//		}
//		return name;
//	}

}
