package com.novelbio.omimdb.mgmt;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.novelbio.omimdb.model.OmimGeneMap;
import com.novelbio.omimdb.mongorepo.RepoGenemap;
import com.novelbio.springonly.factory.SpringFactory;


@Repository
public class MgmtOMIM {
	//需要写一个空的构造函数
	private MgmtOMIM() {}

	RepoGenemap repoGenemap = (RepoGenemap)SpringFactory.getBean("repoGenemap");
	public List<OmimGeneMap> findByPheMimId(int phenMimId){
		return repoGenemap.findInfByPheMimId(phenMimId);
	}
	public List<OmimGeneMap> findByGenMimId(int geneMimId){
		return repoGenemap.findInfByGenMimId(geneMimId);
	}
	public List<OmimGeneMap> findAll(){
		return repoGenemap.findAll();
	}
	public void save(OmimGeneMap phenMimId){
		repoGenemap.save(phenMimId);
	}
	
	//懒汉模式的单例延迟--超牛逼
	static class MgmtOmimHolder {
		static MgmtOMIM mgmtOMIM = new MgmtOMIM();
	}
	/** 
	 * 获得
	 * @return
	 */
	public static MgmtOMIM getInstance() {
		return MgmtOmimHolder.mgmtOMIM;
	}
}
