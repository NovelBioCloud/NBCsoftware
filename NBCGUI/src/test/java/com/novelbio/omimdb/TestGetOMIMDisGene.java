package com.novelbio.omimdb;

import com.novelbio.nbcgui.controlseq.CtrlOMIMDisToGene;

import junit.framework.TestCase;

public class TestGetOMIMDisGene extends TestCase {

	static String inFile = "D:\\OMIM\\disGeneTest.txt";
	static String outputFile = "D:\\OMIM\\disGeneList_Result.txt";
	static int accIDColumn = 1;
//	static String inGeneIdFile = "D:\\OMIM\\mim2gene.txt";
	public static void main(String[] args) {
		CtrlOMIMDisToGene ctrlOMIMDisToGene = new CtrlOMIMDisToGene();
		ctrlOMIMDisToGene.setAccIDColumn(accIDColumn);
		ctrlOMIMDisToGene.setInputFile(inFile);
		ctrlOMIMDisToGene.setOutputFile(outputFile);
		ctrlOMIMDisToGene.run();
	}
}
