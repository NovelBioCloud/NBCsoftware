package com.novelbio.database.mongorepo.geneanno;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.novelbio.database.domain.geneanno.BlastFileInfo;

public interface RepoBlastFileInfo extends PagingAndSortingRepository<BlastFileInfo, String> {
	List<BlastFileInfo> findAll();
}
