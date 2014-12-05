package com.novelbio.omimdb;

import java.util.List;

import com.mongodb.util.StringParseUtil;
import com.novelbio.analysis.annotation.genAnno.AnnoAbs;
import com.novelbio.analysis.annotation.genAnno.AnnoOMIM;

import junit.framework.TestCase;

public class TestAnnoOMIM extends TestCase {

	static String genName = "ENO1";
	static int geneId = 3028;
	static int taxId = 9606;
	public static void main(String[] args) {
//		AnnoOMIM annoOMIM = new AnnoOMIM();
//		
//		List<String[]> liOmimInf = annoOMIM.getInfo(taxId, geneId+"");
//	
//		for (String[] omimInf:liOmimInf) {
//			for (int i=0; i<omimInf.length; i++) {
//				System.out.println("Test result is ===> " + omimInf[i]);
//			}
//		}
//		
//	}
		String testString = "111111 (2)";
		String[] arrPhen = testString.split("\\s+");
		int phenMimId = 0;
		int type = 0;
		String test1 = "";
	if (testString.matches("^\\d{6}\\s+\\(\\d+\\)")) {
		phenMimId = Integer.parseInt(arrPhen[0]);
		
	}
	test1 = arrPhen[arrPhen.length - 1].replaceAll("[()]", "");
	type = Integer.parseInt(test1);
	System.out.println(type);
	}
}
