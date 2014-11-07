package com.novelbio.omimdb.util;

import java.util.List;

import com.novelbio.omimdb.mgmt.MgmtOMIM;
import com.novelbio.omimdb.model.OmimGeneMap;

import junit.framework.TestCase;

public class TestCreatGenemapTable extends TestCase {
	String genemapFilePath = "D:\\OMIM\\genemap2_test.txt";
	int pheId = 100050;
	public void testaddInf() {
		CreatGenemapTable creatGenemapTable = new CreatGenemapTable();
		
		creatGenemapTable.creatGenemapTable(genemapFilePath);
//		MgmtOMIM mgmtOMIM = MgmtOMIM.getInstance();
//		List<OmimGeneMap> lsOminGeneMaps = mgmtOMIM.findByGenMimId(pheId);
//		assertEquals(1, lsOminGeneMaps.size());
	}
}
