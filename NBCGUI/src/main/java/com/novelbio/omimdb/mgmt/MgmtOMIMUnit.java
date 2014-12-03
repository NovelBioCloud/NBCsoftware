package com.novelbio.omimdb.mgmt;

import java.util.List;

import com.novelbio.omimdb.mgmt.MgmtGeneMIMInfo.MgmtOmimHolder;
import com.novelbio.omimdb.model.MIMAllToUni;
import com.novelbio.omimdb.model.MIMInfo;
import com.novelbio.omimdb.mongorepo.RepoGeneMIMInfo;
import com.novelbio.omimdb.mongorepo.RepoMIMInfo;
import com.novelbio.springonly.factory.SpringFactory;
public class MgmtOMIMUnit {

	private MgmtOMIMUnit () {}
	RepoMIMInfo repoMIMInfo = (RepoMIMInfo)SpringFactory.getBean("repoMIMInfo");
	public MIMInfo findByMimId(int MimId){
		return repoMIMInfo.findInfByMimId(MimId);
	}
	public List<MIMInfo> findAll(){
		return repoMIMInfo.findAll();
	}
	public void save(MIMInfo mIMInfo){
		repoMIMInfo.save(mIMInfo);
	}
	//懒汉模式的单例延迟--超牛逼
	static class MgmtOmimUnitHolder {
		static MgmtOMIMUnit mgmtOMIMUnit = new MgmtOMIMUnit();
	}
	/** 
	 * 获得
	 * @return
	 */
	public static MgmtOMIMUnit getInstance() {
		return MgmtOmimUnitHolder.mgmtOMIMUnit;
	}
}