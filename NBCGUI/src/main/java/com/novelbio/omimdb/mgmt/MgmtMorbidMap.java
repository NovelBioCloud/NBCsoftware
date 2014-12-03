package com.novelbio.omimdb.mgmt;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.novelbio.omimdb.mgmt.MgmtOMIM.MgmtOmimHolder;
import com.novelbio.omimdb.model.MorbidMap;
import com.novelbio.omimdb.model.OmimGeneMap;
import com.novelbio.omimdb.mongorepo.RepoGenemap;
import com.novelbio.omimdb.mongorepo.RepoMorbidMap;
import com.novelbio.springonly.factory.SpringFactory;

@Repository
public class MgmtMorbidMap {
	private MgmtMorbidMap() {}
	RepoMorbidMap repoMorbidMap = (RepoMorbidMap)SpringFactory.getBean("repoMorbidMap");
	public List<MorbidMap> findInfByGeneId(int geneId){
		return repoMorbidMap.findInfByGeneId(geneId);
	}
	public List<MorbidMap> findInfByDisease(String disease){
		return repoMorbidMap.findInfByDisease(disease);
	}
	public List<MorbidMap> findAll(){
		return repoMorbidMap.findAll();
	}
	public void save(MorbidMap morbidMap){
		repoMorbidMap.save(morbidMap);
	}
	//懒汉模式的单例延迟--超牛逼
	static class MgmtMorbidMapHolder {
		static MgmtMorbidMap mgmtMorbidMap = new MgmtMorbidMap();
	}
	/** 
	 * 获得
	 * @return
	 */
	public static MgmtMorbidMap getInstance() {
		return MgmtMorbidMapHolder.mgmtMorbidMap;
	}
}
