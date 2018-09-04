package com.novelbio.nbcgui.controlseq;

import com.novelbio.bioinfo.gffchr.GffChrAbs;
import com.novelbio.software.snpanno.SnpAnnotation;

public class CtrlSnpAnnotation  {

	SnpAnnotation snpAnnotation = new SnpAnnotation();

	public CtrlSnpAnnotation() {
	}

	public void clean() {
		snpAnnotation.clearSnpFile();
	}

	public void setGffChrAbs(GffChrAbs gffChrAbs) {
		snpAnnotation.setGffChrAbs(gffChrAbs);
	}

	public void setCol(int colChrID, int colRefStartSite, int colRefNr, int colThisNr) {
		snpAnnotation.setCol(colChrID, colRefStartSite, colRefNr, colThisNr);
	}

	public void addSnpFile(String txtFile, String txtOut) {
		snpAnnotation.addTxtSnpFile(txtFile, txtOut);
	}

	public void runAnnotation() {
		snpAnnotation.run();
	}

	public void stop() {
		snpAnnotation.threadStop();
	}

	public void suspend() {
		snpAnnotation.threadSuspend();
	}

	public void resume() {
		snpAnnotation.threadResume();
	}
}
