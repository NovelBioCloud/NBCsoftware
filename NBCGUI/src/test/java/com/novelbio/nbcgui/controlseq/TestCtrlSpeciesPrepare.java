package com.novelbio.nbcgui.controlseq;

import com.novelbio.database.domain.information.SoftWareInfo.SoftWare;
import com.novelbio.database.model.species.Species;

public class TestCtrlSpeciesPrepare {
	public static void main(String[] args) {
		Species species = new Species(9606);
		species.getIndexChr(SoftWare.bowtie);
	}
}
