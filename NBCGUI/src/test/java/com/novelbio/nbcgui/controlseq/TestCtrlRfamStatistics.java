package com.novelbio.nbcgui.controlseq;

import java.util.ArrayList;
import java.util.List;

public class TestCtrlRfamStatistics {
	public static void main(String[] args) {
		List<String[]> lsPrefix2Fq = new ArrayList<>();
		String parentPath = "/media/winE/NBC/Project/Project_FY/FYmouse20111122/rawdata/MEFrnaseq/cleanreads/";
		lsPrefix2Fq.add(new String[]{"KO0d", parentPath + "KO0d_L1_1.fq.gz"});
		lsPrefix2Fq.add(new String[]{"KO2d", parentPath + "KO2d_L1_1.fq.gz"});
		lsPrefix2Fq.add(new String[]{"WT0d", parentPath+ "WT0d_L1_1.fq.gz"});
		lsPrefix2Fq.add(new String[]{"WT2d", parentPath+ "WT2d_L1_1.fq.gz"});
		
		CtrlRfamStatistics ctrlRfamStatistics = new CtrlRfamStatistics();
		ctrlRfamStatistics.setLsFastqFiles(lsPrefix2Fq);
		ctrlRfamStatistics.setOutPath(parentPath);
		ctrlRfamStatistics.setThreadNum(7);
		ctrlRfamStatistics.calculate();
	}
}
