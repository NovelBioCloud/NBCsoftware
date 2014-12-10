package com.novelbio.omimdb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.fop.fo.properties.ForcePageCount;

import com.novelbio.database.domain.omim.OmimGeneMap;
import com.novelbio.database.model.modgeneid.GeneID;
import com.novelbio.database.model.modomim.MgmtOMIM;
import com.novelbio.database.updatedb.omim.CreatGenemapTable;

import junit.framework.TestCase;

public class TestCreateGenemapTable extends TestCase {
	static String genemapFilePath = "D:\\OMIM\\genemap2_v2.txt";
	static String inGeneIdFile = "D:\\OMIM\\mim2gene.txt";
//	static String minToGeneFile = "D:\\OMIM\\mim2gene.txt";
	public static void main(String[] args) {

		CreatGenemapTable creatGenemapTable = new CreatGenemapTable();
		creatGenemapTable.creatGenemapTable(genemapFilePath, inGeneIdFile);
		
//		List<T> list=new ArrayList<T>（）
		
//		creatGenemapTable.setInGeneIdFile(inGeneIdFile);
//		MgmtOMIM mgmtOMIM = MgmtOMIM.getInstance();
//		List<OmimGeneMap> lsOminGeneMaps = mgmtOMIM.findByGenMimId(geneId);

//		assertEquals(1, lsOminGeneMaps.size());
	}
}


//List<String> list = new ArrayList<String>();
//list.add("aa");
//list.add("aa");
//list.add("bb");
//list.add("cc");
//list.add("bb");
//List<String> listpathList = new ArrayList<String>();
//listpathList.add("D:\\aa.txt");
//listpathList.add("D:\\ab.txt");
//listpathList.add("D:\\bb.txt");
//listpathList.add("D:\\cc.txt");
//listpathList.add("D:\\bd.txt");
//List<String> listNewPathList = new ArrayList<String>();
//String str = "";
//for (int i=0; i< list.size()-1; i++) {
//	str = listpathList.get(i);
//	for (int j=i+1; j< list.size(); j++) {
//		if (list.get(i).equals(list.get(j))) {
//			str = str.concat("," + listpathList.get(j));
//			listpathList.set(i, str);
//			listpathList.remove(j);
//			list.remove(j);
//		}
//	}
//}
//
//for (int x=0; x<list.size(); x++) {
//	System.out.println(list.get(x));
//	System.out.println(listpathList.get(x));
//}
//


//System.out.println(list.get(3));

