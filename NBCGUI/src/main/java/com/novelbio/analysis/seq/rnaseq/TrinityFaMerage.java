package com.novelbio.analysis.seq.rnaseq;

import java.util.ArrayList;
import java.util.List;

import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.base.dataOperate.TxtReadandWrite;
import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public class TrinityFaMerage {
	String trinityResultDir = "D:\\11";
	String merageResultFile = "D:\\12\\merage.txt";
	public void changeTrinityFaId(){
		List<String[]> LisFilePath = new ArrayList<String[]>();
		LisFilePath = FileOperate.getFoldFileName(trinityResultDir);
		String[] strFaID;
		TxtReadandWrite txtWrite = new TxtReadandWrite(merageResultFile);
		for (String[] content:LisFilePath) {
			for (String file:content) {
				TxtReadandWrite contentfile = new TxtReadandWrite(file);
				System.out.println(file);
				for (String linecontent : contentfile.readlines()) {
					System.out.print(linecontent);
				/*	if (linecontent.startsWith(">")) {
						strFaID = linecontent.split(" ");
						System.out.print(strFaID[0]);
					}*/
				}
				
			}
		
		}
	}
	public static void main (String[] args){
		TrinityFaMerage trinityfamerage = new TrinityFaMerage();
		trinityfamerage.changeTrinityFaId();
	}
}
