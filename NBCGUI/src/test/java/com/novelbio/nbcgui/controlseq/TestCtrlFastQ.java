package com.novelbio.nbcgui.controlseq;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.HashMultimap;

public class TestCtrlFastQ {
	@Test
	public void testPredictFile() {
		HashMultimap<String, String> mapPrefix2Result = getPredictMap2File();
		for (String prefix : mapPrefix2Result.keySet()) {
			for (String string : mapPrefix2Result.get(prefix)) {
				System.out.println(prefix + " " + string);
			}
		}
	}
	
	
	private HashMultimap<String, String> getPredictMap2File() {
		CopeFastq copeFastq = new CopeFastq();
		List<String> lsLeftFq = new ArrayList<>();
		lsLeftFq.add("/media/hdfs/nbCloud/test1l.fq");
		lsLeftFq.add("/media/hdfs/nbCloud/test2l.fq");
		lsLeftFq.add("/media/hdfs/nbCloud/test3l.fq");
		lsLeftFq.add("/media/hdfs/nbCloud/test4l.fq");
		lsLeftFq.add("/media/hdfs/nbCloud/test5l.fq");
		List<String> lsRightFq = new ArrayList<>();
		lsRightFq.add("/media/hdfs/nbCloud/test1r.fq");
		lsRightFq.add("/media/hdfs/nbCloud/test2r.fq");
		lsRightFq.add("/media/hdfs/nbCloud/test3r.fq");
		lsRightFq.add("/media/hdfs/nbCloud/test4r.fq");
		lsRightFq.add("/media/hdfs/nbCloud/test5r.fq");
		
		List<String> lsPrefix = new ArrayList<>();
		lsPrefix.add("1");
		lsPrefix.add("1");
		lsPrefix.add("1");
		lsPrefix.add("1");
		lsPrefix.add("2");
		
		copeFastq.setCheckFileIsExist(false);
		copeFastq.setLsFastQfileLeft(lsLeftFq);
		copeFastq.setLsFastQfileRight(lsRightFq);
		copeFastq.setLsCondition(lsPrefix);		
		return CtrlFastQ.getPredictMapPrefix2FilteredFQ(copeFastq, "/media/hdfs/result", true);
	}
}
