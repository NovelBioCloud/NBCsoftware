package com.novelbio.omimdb.mgmt;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.novelbio.database.service.SpringFactory;
import com.novelbio.omimdb.model.OmimGeneMap;
import com.novelbio.omimdb.mongorepo.RepoGenemap;


@Repository
public class MgmtOMIM {
	private MgmtOMIM() {}
	
	RepoGenemap repoGenemap = (RepoGenemap)SpringFactory.getFactory().getBean("repoGenemap");
	
	public List<OmimGeneMap> findByPheMimId(int phenMimId){
		return repoGenemap.findInfByPheMimId(phenMimId);
	}
	public List<OmimGeneMap> findByGenMimId(int phenMimId){
		return repoGenemap.findInfByGenMimId(phenMimId);
	}
	public List<OmimGeneMap> findAll(){
		return repoGenemap.findAll();
	}
	public void save(OmimGeneMap phenMimId){
		repoGenemap.save(phenMimId);
	}
//	public List<NBCSampleQC> findSampleById(String sampleID) {
//		return repoNBCSampleQC.findBySampleID(sampleID);
//	}
//	
//	public List<NBCSampleQC> findSampleByBatchId(String batchId) {
//		return repoNBCSampleQC.findByBatchID(batchId);
//	}
	
	
	//懒汉模式的单例延迟--超牛逼
	static class MgmtOmimHolder {
		static MgmtOMIM mgmtOMIM = new MgmtOMIM();
	}
	/** 
	 * 获得已经设定好computer信息的ClientManager对象
	 * @return
	 */
	public static MgmtOMIM getInstance() {
		return MgmtOmimHolder.mgmtOMIM;
	}
}
