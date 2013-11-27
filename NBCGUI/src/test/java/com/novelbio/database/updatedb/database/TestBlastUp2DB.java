package com.novelbio.database.updatedb.database;

import static org.junit.Assert.assertEquals;
import junit.framework.Assert;

import org.junit.Test;

import com.novelbio.database.model.modgeneid.GeneID;

public class TestBlastUp2DB {
	
//	@Test
	public void testCorrect() {
		BlastUp2DB blastUp2DB = new BlastUp2DB();
		blastUp2DB.setBlastIDType(GeneID.IDTYPE_ACCID);
		blastUp2DB.setQueryIDType(GeneID.IDTYPE_ACCID);
		blastUp2DB.setSubTaxID(10090);
		blastUp2DB.setTaxID(9913);
		String info = blastUp2DB.checkFile("/media/hdfs/nbCloud/staff/hongyanyan/zhangbo/cowBlast2mouse.txt");
		assertEquals(true, info == null);
	}
	
//	@Test
	public void testFalse() {
		BlastUp2DB blastUp2DB = new BlastUp2DB();
		blastUp2DB.setBlastIDType(GeneID.IDTYPE_ACCID);
		blastUp2DB.setQueryIDType(GeneID.IDTYPE_ACCID);
		blastUp2DB.setSubTaxID(10090);
		blastUp2DB.setTaxID(99132);
		String info = blastUp2DB.checkFile("/media/hdfs/nbCloud/staff/hongyanyan/zhangbo/cowBlast2mouse.txt");
		assertEquals(false, info == null);
	}
	
	@Test
	public void testTrue() {
		BlastUp2DB blastUp2DB = new BlastUp2DB();
		blastUp2DB.setBlastIDType(GeneID.IDTYPE_ACCID);
		blastUp2DB.setQueryIDType(GeneID.IDTYPE_ACCID);
		blastUp2DB.setSubTaxID(3702);
		blastUp2DB.setTaxName("cotton");
		String info = blastUp2DB.checkFile("/media/winD/cotton_all2AT.txt");
		System.out.println(info);
		assertEquals(true, info == null);
	}
}
