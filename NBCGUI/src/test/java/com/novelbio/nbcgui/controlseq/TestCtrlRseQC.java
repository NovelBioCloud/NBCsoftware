package com.novelbio.nbcgui.controlseq;

import java.util.ArrayList;
import java.util.List;

import com.novelbio.base.cmd.CmdOperate;
import com.novelbio.database.model.species.Species;

public class TestCtrlRseQC {
	public static void main(String[] args) {
		CtrlRseQC rseQC = new CtrlRseQC();
		Species species = new Species(9606);
		rseQC.setSpecies(species);
		List<String[]> lsFile = new ArrayList<>();
		lsFile.add(new String[]{"testmapsplice","/media/hdfs/nbCloud/public/AllProject" +
				"/project_533a36f1e4b03da4b5785ea4/task_533a8e4ae4b04317ccac9d70/RNASeqMap_result/E0018_mapsplice_sorted.bam"});
		rseQC.setInFile(lsFile);
		rseQC.setOutFilePrefix("/home/zong0jie/Test/rnaseq/rseqc/");
		rseQC.setRunModule(false, false, false, true, false, false);
		rseQC.run();
	}
}
