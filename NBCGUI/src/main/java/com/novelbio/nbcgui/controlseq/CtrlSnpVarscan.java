package com.novelbio.nbcgui.controlseq;

import com.novelbio.base.fileOperate.FileOperate;

/**
 * simple script : ESTScan -m 100 -d 50 -i 50 -M at.smat -p 4 -N 0 -w 60 -s 1 All-Unigene,final.fa -t All-Unigene.final.pep > All-Unigene.final.cds
 * @author bll
 *
 */
public class CtrlSnpVarscan  implements IntCmdSoft {
	String inputFile;
	public void setInputFile(String inputFile) {
		FileOperate.checkFileExistAndBigThanSize(inputFile, 0);
		this.inputFile = inputFile;
	}
	
}
