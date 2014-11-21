package com.novelbio.omimdb;

import junit.framework.TestCase;

import com.novelbio.omimdb.util.CreatMIMTable;

public class TestCreateOMIMTable  extends TestCase {

	String omimFilePath = "D:\\OMIM\\omim_test.txt";
	public void testaddInf() {
		System.out.println("Start! ");
		CreatMIMTable creatMIMTable =new CreatMIMTable();
		creatMIMTable.setInFileString(omimFilePath);
		creatMIMTable.creatMIMTable();
	}
}
