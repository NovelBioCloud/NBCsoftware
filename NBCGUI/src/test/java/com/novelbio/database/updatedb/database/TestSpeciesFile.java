package com.novelbio.database.updatedb.database;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.novelbio.database.model.geneanno.SpeciesFile;

public class TestSpeciesFile{
	@Test
	public void testPath(){
		SpeciesFile speciesFile = SpeciesFile.findByTaxIDVersion(10090, "mm9_UCSC");
		Assert.assertEquals("/media/hdfs/nbCloud/public/nbcplatform/genome/species/10090/mm9_UCSC/ChromFa/species/10090/mm9_UCSC/ChromFa/chrAll.fa", speciesFile.getChromSeqFile());
		Assert.assertEquals("/media/hdfs/nbCloud/public/nbcplatform/genome/species/10090/mm9_UCSC/gff/species/10090/mm9_UCSC/gff/rmsk_ucsc", speciesFile.getGffRepeatFile());
		Assert.assertEquals("/media/hdfs/nbCloud/public/nbcplatform/genome/species/10090/mm9_UCSC/refrna/species/10090/mm9_UCSC/refrna/rna_modify.fa", speciesFile.getRefSeqFile(true, false));
		Map<String, String[]> map = speciesFile.getGffDB2GffTypeFileMap();
		System.out.println(map.values());
		System.err.println();
	}
}
