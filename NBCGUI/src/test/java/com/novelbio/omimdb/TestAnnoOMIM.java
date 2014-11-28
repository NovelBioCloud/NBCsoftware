package com.novelbio.omimdb;

import java.util.List;

import com.novelbio.analysis.annotation.genAnno.AnnoAbs;
import com.novelbio.analysis.annotation.genAnno.AnnoOMIM;

import junit.framework.TestCase;

public class TestAnnoOMIM extends TestCase {

	static String genName = "ENO1";
	static int geneId = 2023;
	static int taxId = 9606;
	public static void main(String[] args) {
		AnnoOMIM annoOMIM = new AnnoOMIM();
		
		List<String[]> liOmimInf = annoOMIM.getInfo(taxId, geneId+"");
	
		for (String[] omimInf:liOmimInf) {
			for (int i=0; i<omimInf.length; i++) {
				System.out.println("Test result is ===> " + omimInf[i]);
			}
		}
		
	}

	
}
