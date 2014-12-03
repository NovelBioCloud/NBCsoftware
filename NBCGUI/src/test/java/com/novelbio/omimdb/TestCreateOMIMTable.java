package com.novelbio.omimdb;

import junit.framework.TestCase;

import com.novelbio.omimdb.util.CreatMIMTable;

public class TestCreateOMIMTable extends TestCase {

//	String omimFilePath = "D:\\OMIM\\omim_test.txt";
	public static void main(String[] args) {
		String omimFilePath = "D:\\OMIM\\omim.txt";
//		String omimFilePath = "D:\\OMIM\\omim_test.txt";
		
		System.out.println("Start! ");
		CreatMIMTable creatMIMTable =new CreatMIMTable();
		creatMIMTable.creatMIMTable(omimFilePath);
		
		
//		String testString = "1. Grier, R. E.; Farrington, F. H.; Kendig, R.; Mamunes, P.: Autosomal dominant inheritance of the Aarskog syndrome. Am. J. Med. Genet. 15: 39-46, 1983.  2. van de Vooren, M. J.; Niermeijer, M. F.; Hoogeboom, A. J. M.: The Aarskog syndrome in a large family, suggestive for autosomal dominant inheritance. Clin. Genet. 24: 439-445, 1983.  3. Welch, J. P.: Elucidation of a 'new' pleiotropic connective tissue disorder. Birth Defects Orig. Art. Ser. X(10): 138-146, 1974.";
//		String testString2 = " DESCRIPTION bbbbbbbbbbbbb";
//		String testString3 = " cccccccccccc CLINICAL CLINICAL1 bbbbbbbbbbbbb";
//		String reg = " \\d{1} ";
//		String reg = " \\. ";
//		String reg2 = " DESCRIPTION ";
//		String[] arrtestStrings = testString.split("\\s+\\d+\\.\\s+");
//		for (int i = 0; i < arrtestStrings.length; i++) {
//			System.out.println( arrtestStrings[i]);
//		}
//		
//		int flag = testString2.indexOf(reg2);
//		System.out.println("flag " + flag);
//		if (testString.indexOf(reg2) == 0) {
//			System.out.println("00000");
//		} else if (testString.indexOf(reg2) > 0) {
//			System.out.println("111111111111");
//		} else {
//		
//		}
	}
//	public void testaddInf() {
//		System.out.println("Start! ");
//		CreatMIMTable creatMIMTable =new CreatMIMTable();
//		creatMIMTable.setInFileString(omimFilePath);
//		creatMIMTable.creatMIMTable();
//	}
}
