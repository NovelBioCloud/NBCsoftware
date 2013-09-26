package com.novelbio.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.novelbio.analysis.diffexpress.DiffExpAbs;
import com.novelbio.base.dataOperate.ExcelTxtRead;
import com.novelbio.base.fileOperate.FileHadoop;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.nbcReport.Params.ReportDifGeneAll;
import com.novelbio.nbcReport.Params.ReportProject;
import com.novelbio.nbcgui.controltest.CtrlDifGene;

public class TestDifGene {
	ReportDifGeneAll reportDifGeneAll;

	public static void main(String[] args) {
		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
		int DiffExpID = DiffExpAbs.TTest;
		CtrlDifGene ctrlDifGene = new CtrlDifGene(DiffExpID);
		int colId = 1;
		ctrlDifGene.setColID(colId);
		if (DiffExpID == DiffExpAbs.LIMMA) {
			if ("1" == null) {
				ctrlDifGene.setIsLog2Value(true);
			} else {
				ctrlDifGene.setIsLog2Value(false);
			}
		}
		ArrayList<String[]> lsSampleColumn2GroupName = new ArrayList<String[]>();
		String[] sampleColArray = { "3", "4", "5", "6", "7", "8" };
		String[] groupNameArray = { "C", "C", "C", "D", "D", "D" };
		for (int i = 0; i < groupNameArray.length; i++) {
			String[] sampleColumn2GroupName = new String[2];
			sampleColumn2GroupName[0] = sampleColArray[i];
			sampleColumn2GroupName[1] = groupNameArray[i];
			lsSampleColumn2GroupName.add(sampleColumn2GroupName);
		}
		ctrlDifGene.setCol2Sample(lsSampleColumn2GroupName);
		String inFileName = "/hdfs:/nbCloud/public/test/DifGene0910/rawdata/All_Fragments_0.txt";
		ArrayList<String[]> lsStrings = ExcelTxtRead.readLsExcelTxt(inFileName, 1);
		ctrlDifGene.setGeneInfo(lsStrings);
		String outFilePath = "/hdfs:/nbCloud/staff/gaozhu/我的文档";
		if (FileOperate.isFileDirectory(outFilePath)) {
			outFilePath = FileOperate.addSep(outFilePath);
		}

		String[] fileNameArray = { "CVSD" };
		String[] group1Array = { "C" };
		String[] group2Array = { "D" };
		for (int i = 0; i < group2Array.length; i++) {
			String fileName = fileNameArray[i];
			fileName = outFilePath + fileName;
			if (!fileName.endsWith("txt") && !fileName.endsWith("xls") && !fileName.endsWith("xlsx")) {
				fileName = FileOperate.changeFileSuffix(fileName, "", "xls");
			}

			String[] pair = new String[] { group1Array[i], group2Array[i] };
			ctrlDifGene.addFileName2Compare(fileName, pair);
			ctrlDifGene.calculateResult();
			ctrlDifGene.getResultFileName();
			ctrlDifGene.clean();
		}

	}
}