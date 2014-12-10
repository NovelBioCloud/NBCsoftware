package com.novelbio.omimdb.mgmt;

import java.util.List;

import junit.framework.TestCase;

import com.novelbio.database.domain.omim.GeneMIM;
import com.novelbio.database.domain.omim.OmimGeneMap;
import com.novelbio.database.model.modomim.MgmtGeneMIMInfo;

public class TestMgmtOMIM extends TestCase {

	public void testSave() {
//		int pheId = 35432345;
//		OmimGeneMap genemap = new OmimGeneMap();
//		genemap.setGenMimId(pheId);
//		genemap.setPhenDec("test");
//		
//		MgmtOMIM mgmtOMIM = MgmtOMIM.getInstance();
//		mgmtOMIM.save(genemap);
	
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		List<OmimGeneMap> lsOminGeneMaps = mgmtOMIM.findByGenMimId(pheId);
//		assertEquals(1, lsOminGeneMaps.size());
//		
//		OmimGeneMap dbRecord = lsOminGeneMaps.get(0);
//		assertEquals("test", dbRecord.getPhenDec());
//		
//		dbRecord.setPhenDec("tredfged");
//		mgmtOMIM.save(dbRecord);
//		
//		OmimGeneMap dbRecord1 = lsOminGeneMaps.get(0);
//		assertEquals("tredfged", dbRecord1.getPhenDec());
//		
//		dbRecord1.remove();
//		 lsOminGeneMaps = mgmtOMIM.findByPheMimId(pheId);
//		 assertEquals(0, lsOminGeneMaps.size());
	}
//	
	@Override
	protected void tearDown() throws Exception {
		MgmtGeneMIMInfo mgmtGeneOMIM = MgmtGeneMIMInfo.getInstance();
		List<GeneMIM> ls = mgmtGeneOMIM.findAll();
		for (GeneMIM geneMap : ls) {
			geneMap.remove();
		}
		super.tearDown();
		
	}
	
//	@Override
//	protected void tearDown() throws Exception {
//		MgmtOMIM mgmtOMIM = MgmtOMIM.getInstance();
//		List<OmimGeneMap> ls = mgmtOMIM.findAll();
//		for (OmimGeneMap omimGeneMap : ls) {
//			omimGeneMap.remove();
//		}
//		super.tearDown();
//		
//	}
	
	
}
	

