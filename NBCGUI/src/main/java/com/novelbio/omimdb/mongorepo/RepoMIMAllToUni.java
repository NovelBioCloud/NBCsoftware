package com.novelbio.omimdb.mongorepo;

import java.util.List;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.novelbio.omimdb.model.MIMAllToUni;

public interface RepoMIMAllToUni extends PagingAndSortingRepository<MIMAllToUni, String>{
	/**
	 * 
	 * @param 
	 * @param 
	 * @return
	 */
	@Query(value="{ 'MimId' : ?0 }")
	List<MIMAllToUni> findInfByMimId(int MimId);
	
	List<MIMAllToUni> findAll();
}
