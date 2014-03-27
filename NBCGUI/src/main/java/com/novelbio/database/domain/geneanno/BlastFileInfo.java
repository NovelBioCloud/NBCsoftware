package com.novelbio.database.domain.geneanno;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Properties;

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
 * 
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
	/** 随机文件夹 */
	String randomFolder = DateUtil.getDateAndRandom();
	@Indexed
	int queryTaxID;

	@Indexed
	int subjectTaxID;

	/** blast类型 */
	BlastType blastType = BlastType.blastn;
	/** 导入日期 */
	long impotDate = new Date().getTime();
	
	public BlastFileInfo(){}
	
	public BlastFileInfo(Properties properties){
		this.id = properties.getProperty("id");
		this.fileName = properties.getProperty("fileName");
		this.isTmp = Boolean.parseBoolean(properties.getProperty("isTmp"));
		this.userId = properties.getProperty("userId");
		this.userName = properties.getProperty("userName");
		this.randomFolder = properties.getProperty("randomFolder");
		this.queryTaxID = Integer.parseInt(properties.getProperty("queryTaxID"));
		this.subjectTaxID = Integer.parseInt(properties.getProperty("subjectTaxID"));
		try {
			this.blastType = BlastType.valueOf(properties.getProperty("blastType"));
		} catch (Exception e) {
			blastType = blastType.blastn;
		}
		this.impotDate = Long.parseLong(properties.getProperty("impotDate"));
//		try {
//		   chinese = new String(chinese.getBytes("ISO-8859-1"), "GBK"); // 处理中文乱码
//	    } catch (UnsupportedEncodingException e) {
//		   e.printStackTrace();
//	    }
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * 真正保存blast文件的路径
	 * 
	 * @return
	 */
	public String realFileAndName() {
		if (fileName.contains("\\") || fileName.contains("/")) {
			return fileName;
		}
		String fileNameFinal = FileOperate.addSep(PathDetail.getBlastFolder())
				+ fileName;
		fileNameFinal = FileOperate.changeFileSuffix(fileNameFinal, "_"
				+ randomFolder, null);
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

	/** 随机文件夹 */
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
	 * 
	 * @return
	 */
	private static ManageBlastInfo repo() {
		return ManageBlastInfo.getInstance();
	}

	/**
	 * 保存
	 * @param writeAsMeta 是否写入meta信息
	 * @return
	 * @throws IOException
	 */
	public boolean save(boolean writeAsMeta) throws IOException {
		boolean result = repo().saveBlastFile(this);
		if(writeAsMeta && result)
			writeAsMetaData();
		return result;
	}

	public int getSubjectTaxID() {
		return subjectTaxID;
	}

	// public String getUsrid() {
	// return usrid;
	// }
	// public void setUsrid(String usrid) {
	// this.usrid = usrid;
	// }
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
	 * 
	 * @return
	 * @throws BlastFileException
	 *             如果文件不对会抛出异常，可以在前台返回
	 * @throws IOException 
	 */
	public boolean importAndSave() throws BlastFileException, IOException {
		BlastUp2DB blastUp2DB = new BlastUp2DB();
		blastUp2DB.setBlastFileInfo(this, GeneID.IDTYPE_ACCID,
				GeneID.IDTYPE_ACCID);
		blastUp2DB.updateFile();
		return true;
	}

	/**
	 * 删除blastFileInfo和blastInfo
	 * 
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
	 * 
	 * @param id
	 *            blastID
	 * @return
	 */
	public static BlastFileInfo findInstance(String id) {
		return repo().findBlastFileById(id);
	}

	public static Page<BlastFileInfo> findAll(PageModel pageModel) {
		return repo().findAll(pageModel.bePageable());
	}

	public static List<BlastFileInfo> findAll() {
		return repo().findAll();
	}

	/**
	 * 写成meta信息文件
	 * 
	 * @throws IOException
	 */
	private void writeAsMetaData() throws IOException {
		Properties properties = new Properties();
		String metaFileName = realFileAndName() + ".meta";
		try {
			OutputStream outputStream = FileOperate.getOutputStream(
					metaFileName, true);
			properties.setProperty("id", id);
			if(fileName != null)
				properties.setProperty("fileName", fileName);
			properties.setProperty("isTmp", isTmp + "");
			if(userId != null)
				properties.setProperty("userId", userId);
			if(userName != null)
				properties.setProperty("userName", userName);
			if(randomFolder != null)
				properties.setProperty("randomFolder", randomFolder);
			properties.setProperty("queryTaxID", queryTaxID + "");
			properties.setProperty("subjectTaxID", subjectTaxID + "");
			if(blastType != null)
				properties.setProperty("blastType", blastType.name());
			properties.setProperty("impotDate", impotDate + "");
			properties.store(outputStream, "author: gaozhu");
			outputStream.close();
		} catch (IOException e) {
			throw new IOException("write as metaData file error！");
		}
	}

	public static void main(String[] args) throws IOException {
//		for (BlastFileInfo blast : BlastFileInfo.findAll()) {
//			blast.save(true);
//		}
		BlastFileInfo.importAllFromMeta();
	}
	/**
	 * 从blastFiles文件夹下扫描所有的meta信息，并导入数据库中
	 * @throws IOException
	 */
	public static void importAllFromMeta() throws IOException {
		List<String> lsMetaFiles = FileOperate.getFoldFileNameLs(
				PathDetail.getBlastFolder(), "*", "meta");
		for (String metaFile : lsMetaFiles) {
			readFromMetaFile(metaFile).save(false);
		}
	}
	/**
	 * 从一个meta文件中读取信息，并转化成一个BlastFileInfo对象
	 * @param metafileNameAndPath
	 * @return
	 */
	private static BlastFileInfo readFromMetaFile(String metafileNameAndPath) {
		Properties properties = new Properties();
		try {
			InputStream inputStream = FileOperate.getInputStream(metafileNameAndPath);
			properties.load(inputStream);
			inputStream.close(); // 关闭流
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new BlastFileInfo(properties);
	}
}
