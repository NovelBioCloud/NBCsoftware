package com.novelbio.omimdb.mongorepo;

import java.util.List;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.novelbio.omimdb.model.MorbidMap;

public interface RepoMorbidMap extends PagingAndSortingRepository<MorbidMap, Integer>{
	/**
	 * 
	 * @param 
	 * @param 
	 * @return
	 */
	@Query(value="{ 'GeneId' : ?0 }")
	List<MorbidMap> findInfByGeneId(int GeneId);
	@Query(value="{ 'listDis' : ?0 }")
	List<MorbidMap> findInfByDisease(String disease);

	List<MorbidMap> findAll();

}
