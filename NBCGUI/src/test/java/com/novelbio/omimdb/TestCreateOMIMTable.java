package com.novelbio.omimdb;

import junit.framework.TestCase;

import com.novelbio.omimdb.util.CreatMIMTable;

public class TestCreateOMIMTable extends TestCase {

//	String omimFilePath = "D:\\OMIM\\omim_test.txt";
	public static void main(String[] args) {
		String omimFilePath = "D:\\OMIM\\omim.txt";
		System.out.println("Start! ");
		CreatMIMTable creatMIMTable =new CreatMIMTable();
		creatMIMTable.creatMIMTable(omimFilePath);
	}
//	public void testaddInf() {
//		System.out.println("Start! ");
//		CreatMIMTable creatMIMTable =new CreatMIMTable();
//		creatMIMTable.setInFileString(omimFilePath);
//		creatMIMTable.creatMIMTable();
//	}
}
