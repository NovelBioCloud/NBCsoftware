package com.novelbio.omimdb.mongorepo;

import java.util.List;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.novelbio.omimdb.model.MIMAllToUni;

public interface RepoMIMAllToUni extends PagingAndSortingRepository<MIMAllToUni, Integer>{
	
	List<MIMAllToUni> findAll();
}
