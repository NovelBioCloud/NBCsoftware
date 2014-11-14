package com.novelbio.omimdb.mongorepo;

import java.util.List;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.novelbio.omimdb.model.GeneMIM;
import com.novelbio.omimdb.model.MIMInfo;
import com.novelbio.omimdb.model.OmimGeneMap;

public interface RepoGeneMIMInfo  extends PagingAndSortingRepository<GeneMIM, String>{
	/**
	 * 
	 * @param 
	 * @param 
	 * @return
	 */
	@Query(value="{ 'GeneMimId' : ?0 }")
	List<GeneMIM> findInfByGeneMimId(int geneMimId);
	
	List<GeneMIM> findAll();
	
}
