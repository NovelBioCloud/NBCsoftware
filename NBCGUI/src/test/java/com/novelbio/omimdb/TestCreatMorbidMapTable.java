package com.novelbio.omimdb;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.novelbio.omimdb.util.CreatMorbidMapTable;

import junit.framework.TestCase;

public class TestCreatMorbidMapTable extends TestCase {
	static String inFile = "D:\\OMIM\\morbidmap.txt";
	static String inGeneIdFile = "D:\\OMIM\\mim2gene.txt";
	public static void main(String[] args) {
		CreatMorbidMapTable creatMorbidMapTable = new CreatMorbidMapTable();
		creatMorbidMapTable.creatMorbidMapTable(inFile, inGeneIdFile);
		
//		String testString = "17,20-lyase deficiency, isolated, 202110 (3)|CYP17A1, CYP17, P450C17|609300|10q24.32";
//		
//		String[] arrtestStrings = testString.split("\\|");
//		String[] arrDisease = arrtestStrings[0].split(",");
//		int phenMIMId = 0;
//		String phenInfo = arrDisease[arrDisease.length - 1].trim();
//		Pattern pattern = Pattern.compile("^\\d{6}");
////		Pattern paType = Pattern.compile("^\\(\\d+\\)");
//		Matcher matcher = pattern.matcher(phenInfo);
//		String id = "";
//		if (matcher.find()) {
//			id = matcher.group();
//		}
//		System.out.println("phenID is " + id);
	

	}
	
}
