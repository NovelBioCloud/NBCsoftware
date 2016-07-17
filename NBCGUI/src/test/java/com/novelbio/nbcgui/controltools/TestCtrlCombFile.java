package com.novelbio.nbcgui.controltools;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;

public class TestCtrlCombFile {
	
	@Test
	public void testGetLsIntegers() {
		List<Integer> lsIntegers = CtrlCombFile.getLsIntegers("1 2 3 4-6 9 12-14");
		List<Integer> lsIntegersExp = Lists.newArrayList(1,2,3,4,5,6,9,12,13,14);
		Assert.assertEquals(lsIntegers, lsIntegersExp);
	}
	
}
