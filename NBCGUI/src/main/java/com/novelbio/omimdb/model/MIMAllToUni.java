package com.novelbio.omimdb.model;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.novelbio.database.service.SpringFactory;
import com.novelbio.omimdb.mongorepo.RepoGenemap;
import com.novelbio.omimdb.mongorepo.RepoMIMAllToUni;

/**
 * 
 * @author Administrator
 *
 */
@Document(collection = "omimIDAllToUni")
public class MIMAllToUni  implements Serializable {
	
	/** 所有的MIM ID */
	@Id
	private int allMIMId;
	/** Unique MIM 号 */
	private int uniMIMId;

	public void setAllMIMId(int allMIMId) {
		this.allMIMId = allMIMId;
	}
	public int getAllMIMId() {
		return allMIMId;
	}
	public void setUniMIMId(int uniMIMId) {
		this.uniMIMId = uniMIMId;
	}
	public int getUniMIMId() {
		return allMIMId;
	}
	
	/**
	 * 给定一个Omim的单元，从Omim的文件中读取获得的单元
	 * 返回实例化的MIMALLToUni对象
	 * @param lsOmimunit
	 * @return
	 */
	public static MIMAllToUni getInstanceFromOmimUnit(List<String> lsOmimunit) {
		if (lsOmimunit.isEmpty()) {
			return null;
		}
		//TODO
		return new MIMAllToUni();
	}
	 private static RepoMIMAllToUni repo() {
		 return SpringFactory.getFactory().getBean(RepoMIMAllToUni.class);
		 
	 }
	 public static MIMAllToUni findInfByMimId(int allMIMId) {
		 return repo().findOne(allMIMId+"");
		 }
	 public boolean remove() {
		 try {
			 repo().delete(allMIMId+"");
		 } catch (Exception e) {
			 return false;
		 }
		 return true;
	 }	 
}
