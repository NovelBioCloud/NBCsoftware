package com.novelbio.omimdb.mgmt;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.novelbio.omimdb.mgmt.MgmtOMIM.MgmtOmimHolder;
import com.novelbio.omimdb.model.MIMAllToUni;
import com.novelbio.omimdb.model.OmimGeneMap;
import com.novelbio.omimdb.mongorepo.RepoMIMAllToUni;
import com.novelbio.springonly.factory.SpringFactory;

public class MgmtMIMAllToUni {

	private MgmtMIMAllToUni() {}
	RepoMIMAllToUni repoMIMAllToUni = (RepoMIMAllToUni)SpringFactory.getBean("repoMIMAllToUni");
	public List<MIMAllToUni> findInfByMimId(int MimId){
		return repoMIMAllToUni.findInfByMimId(MimId);
	}
	public List<MIMAllToUni> findAll(){
		return repoMIMAllToUni.findAll();
	}
	public void save(MIMAllToUni oIMAllToUni){
		repoMIMAllToUni.save(oIMAllToUni);
	}
	
	//懒汉模式的单例延迟--超牛逼
	static class MgmtMIMAllToUniHolder {
		static MgmtMIMAllToUni mgmtMIMAllToUni = new MgmtMIMAllToUni();
	}
	/** 
	 * 获得
	 * @return
	 */
	public static MgmtMIMAllToUni getInstance() {
		return MgmtMIMAllToUniHolder.mgmtMIMAllToUni;
	}
}
