package com.novelbio.omimdb.mongorepo;

import java.util.List;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.novelbio.omimdb.model.MIMInfo;

public interface RepoMIMInfo extends PagingAndSortingRepository<MIMInfo, String>{
	/**
	 * 
	 * @param 
	 * @param 
	 * @return
	 */
	@Query(value="{ 'MimId' : ?0 }")
	MIMInfo findInfByMimId(int MimId);
	
	List<MIMInfo> findAll();
	
	
}
