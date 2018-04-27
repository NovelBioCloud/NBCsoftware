package com.novelbio.analysis.seq.lnc;

import com.novelbio.database.domain.species.Species;

import junit.framework.TestCase;

public class TestCPAT extends TestCase {
	String cpatOutPath = "/media/winE/test/testCPAT/";
	public void testSetSpecies() {
		Species species = new Species(7227);
		species.setVersion("dmel_r6_01");
		System.out.println(species.getGffFile());
		
		
//		CPAT cpat = new CPAT();
//		cpat.setSpecies(new Species(9606));
//		assertEquals(true, cpat.isModelSpecies);
//		assertEquals("human", cpat.speciesName);
	}
	
//	public void testAddSpecies() {
//		CPAT cpat = new CPAT();
//		cpat.setSpecies(new Species(9606));
//		cpat.setOutFile("/media/winE/test/testCPAT/");
//		cpat.addSpeciesInfo(new Species(10090));
//		cpat.addSpeciesInfo(new Species(3702));
//		cpat.setFastaNeedPredict(fastaNeedPredict);
//	}
}
