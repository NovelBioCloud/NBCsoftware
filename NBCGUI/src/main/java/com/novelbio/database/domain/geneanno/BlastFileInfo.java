package com.novelbio.database.domain.geneanno;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.novelbio.analysis.annotation.blast.BlastType;
import com.novelbio.base.PageModel;
import com.novelbio.base.PathDetail;
import com.novelbio.base.dataOperate.DateUtil;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.database.model.modgeneid.GeneID;
import com.novelbio.database.service.servgeneanno.ManageBlastInfo;
import com.novelbio.database.updatedb.database.BlastUp2DB;
import com.novelbio.database.updatedb.database.BlastUp2DB.BlastFileException;

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
	
	String userId;
	String userName;
	/**随机文件夹*/
	String randomFolder = DateUtil.getDateAndRandom();
	@Indexed
	int queryTaxID;

	@Indexed
	int subjectTaxID;
	
	/** blast类型 */
	BlastType blastType = BlastType.blastn;
	/** 导入日期 */
	long impotDate = new Date().getTime();
		
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	/**
	 * 真正保存blast文件的路径
	 * @return
	 */
	public String realFileAndName() {
		if (fileName.contains("\\") || fileName.contains("/")) {
			return fileName;
		}
		String fileNameFinal = FileOperate.addSep(PathDetail.getBlastFolder()) + fileName;
		fileNameFinal = FileOperate.changeFileSuffix(fileNameFinal, "_" + randomFolder, null);
		return fileNameFinal;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public void setTmp(boolean isTmp) {
		this.isTmp = isTmp;
	}
	
	public void setQueryTaxID(int queryTaxID) {
		this.queryTaxID = queryTaxID;
	}
	/**随机文件夹*/
	public String getRandomFolder() {
		return randomFolder;
	}
	/** query物种的俗名 */
	public String getQueryTaxName() {
		return TaxInfo.findByTaxID(queryTaxID).getComName();
	}

	public void setSubjectTaxID(int subjectTaxID) {
		this.subjectTaxID = subjectTaxID;
	}
	/** subject物种的俗名 */
	public String getSubjectTaxName() {
		return TaxInfo.findByTaxID(subjectTaxID).getComName();
	}

	public String getFileName() {
		return fileName;
	}
	public boolean isTmp() {
		return isTmp;
	}
	public int getQueryTaxID() {
		return queryTaxID;
	}

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
	
	public int getSubjectTaxID() {
		return subjectTaxID;
	}
	
//	public String getUsrid() {
//		return usrid;
//	}
//	public void setUsrid(String usrid) {
//		this.usrid = usrid;
//	}
	public BlastType getBlastType() {
		return blastType;
	}
	public void setBlastType(BlastType blastType) {
		this.blastType = blastType;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserId() {
		return userId;
	}
	public long getImpotDate() {
		return impotDate;
	}
	/**
	 * 导入blast文件并保存blast信息,不成功的话，请删除blast文件和随机文件夹
	 * @return
	 * @throws BlastFileException 如果文件不对会抛出异常，可以在前台返回
	 */
	public boolean importAndSave() throws BlastFileException {
		BlastUp2DB blastUp2DB = new BlastUp2DB();
		blastUp2DB.setBlastFileInfo(this, GeneID.IDTYPE_ACCID, GeneID.IDTYPE_ACCID);
		blastUp2DB.updateFile();
		return true;
	}
	/**
	 * 删除blastFileInfo和blastInfo
	 * @return
	 */
	public boolean delete() {
		try {
			ManageBlastInfo.getInstance().removeBlastFile(this);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 根据blastID查找
	 * @param id blastID
	 * @return
	 */
	public static BlastFileInfo findInstance(String id) {
		return repo().findBlastFileById(id);
	}
	
	public static Page<BlastFileInfo> findAll(PageModel pageModel){
		return repo().findAll(pageModel.bePageable());
	}
	public static List<BlastFileInfo> findAll(){
		return repo().findAll();
	}
	
}
