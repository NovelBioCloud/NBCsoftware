package com.novelbio.omimdb.model;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.lowagie.text.SplitCharacter;
import com.novelbio.omimdb.mongorepo.RepoGenemap;
import com.novelbio.omimdb.mongorepo.RepoMIMAllToUni;
import com.novelbio.springonly.factory.SpringFactory;
/**
 * 
 * @author Administrator
 *
 */
@Document(collection = "omimIdAllToUni")
public class MIMAllToUni  implements Serializable {
	
	/** 所有的MIM ID */
	@Indexed
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
		MIMAllToUni mIMAllToUni = new MIMAllToUni();
		String fieldTitle = "";
		String fieldTxt = "";
		String[] mimID;
		int allMimID = 0;
		int uniMimID = 0;
		for (String content : lsOmimunit) {
			if (content.startsWith("*FIELD*")) {
				if (fieldTitle.equals("TI")) {
					if (fieldTxt.matches("\\^\\d{6}.*?\\d{6}\\s*$")) {
						mimID = fieldTxt.substring(1).split("\\s");
						allMimID = Integer.parseInt(mimID[0]);
						uniMimID = Integer.parseInt(mimID[3]);
					} else if (fieldTxt.matches("[#\\+\\*%]\\d.*?$")) {
						fieldTxt = fieldTxt.substring(1, 7);
						allMimID = Integer.parseInt(fieldTxt);
						uniMimID = allMimID;
					} else if (fieldTxt.matches("\\d.*?$")) {
						fieldTxt = fieldTxt.substring(0, 6);
						allMimID = Integer.parseInt(fieldTxt);
						uniMimID = allMimID;
					} else {
						continue;
					}
					if (allMimID != 0) {
						mIMAllToUni.setAllMIMId(allMimID);
						mIMAllToUni.setUniMIMId(uniMimID);
//						System.out.println("allMimId=" + allMimID);
//						System.out.println("uniMimID=" + uniMimID);
					} 
					
				} 
				fieldTitle = content.split("\\s")[1];
				fieldTxt = "";
			} else {
				fieldTxt = fieldTxt.concat(content + " ");
			}
			
		}
		
		
		//TODO
		return mIMAllToUni;
	}
	 private static RepoMIMAllToUni repo() {
		 return SpringFactory.getBean(RepoMIMAllToUni.class);
		 
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
