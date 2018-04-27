package com.novelbio.nbcgui.controlseq;

import com.novelbio.database.domain.species.Species;
import com.novelbio.database.model.information.SoftWareInfo.SoftWare;

public class TestCtrlSpeciesPrepare {
	public static void main(String[] args) {
		Species species = new Species(9606);
		species.getIndexChr(SoftWare.bowtie);
	}
}
