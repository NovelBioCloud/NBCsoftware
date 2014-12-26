package com.novelbio.nbcgui.controlseq;

import java.util.ArrayList;
import java.util.List;

import com.novelbio.analysis.IntCmdSoft;
import com.novelbio.base.cmd.CmdOperate;
import com.novelbio.base.dataStructure.ArrayOperate;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.database.domain.information.SoftWareInfo;
import com.novelbio.database.domain.information.SoftWareInfo.SoftWare;

/**
 * simple script : ESTScan -m 100 -d 50 -i 50 -M at.smat -p 4 -N 0 -w 60 -s 1 All-Unigene,final.fa -t All-Unigene.final.pep > All-Unigene.final.cds
 * @author bll
 *
 */

public class CtrlSSRPredict implements IntCmdSoft {

	//输入文件：需要进行CDS预测的序列文件，fasta格式文件
	String inputFile;
	String misainiFile;
	String exePath = "/media/hdfs/nbCloud/staff/bianlianle/software/MISA";	
		
	public void setInputFile(String inputFile) {
		FileOperate.checkFileExistAndBigThanSize(inputFile, 0);
		this.inputFile = inputFile;
	}
	public void setMisainiFile(String misainiFile) {
		FileOperate.checkFileExistAndBigThanSize(misainiFile, 0);
		this.misainiFile = misainiFile;
	}
	
	
	public CtrlSSRPredict() {
		SoftWareInfo softWareInfo = new SoftWareInfo(SoftWare.misa);
		this.exePath = softWareInfo.getExePathRun();
	}
	public void run() {
		CmdOperate cmdOperate = new CmdOperate(getCmdExeStr());
		cmdOperate.runWithExp("MISA error:");
	}

	private String[] getInputFile(String inputFile) {
		return new String[]{inputFile};
	}

	@Override
	public List<String> getCmdExeStr() {
		// TODO Auto-generated method stub
		List<String> lsCmd = new ArrayList<>();
		lsCmd.add(exePath + "/misa.pl");
		ArrayOperate.addArrayToList(lsCmd, getInputFile(inputFile));
		System.out.println("lsCmd is " + lsCmd);
		return lsCmd;
	}
	

}
