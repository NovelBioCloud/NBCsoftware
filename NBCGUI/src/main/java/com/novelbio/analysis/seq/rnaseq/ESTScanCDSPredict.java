package com.novelbio.analysis.seq.rnaseq;

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

public class ESTScanCDSPredict implements IntCmdSoft {
	
	//输入文件：需要进行CDS预测的序列文件，fasta格式文件
	String inputFile;
	String exePath = "";	
	
	//矩阵分值文件，最小阈值，默认100；
	int minMatrixValue = 100;
	
	//
	String scoreMatFile;
	
	//最短长度
	int minLength  = 50; 
	//翻译的蛋白序列文件
	String pepFile;
	//输出结果文件
	String cdsResultFile;
	
	public void setInputFile(String inputFile) {
		FileOperate.checkFileExistAndBigThanSize(inputFile, 0);
		this.inputFile = inputFile;
	}
	public void setMinMatrixValue(int minMatrixValue) {
		if (minMatrixValue > 0) {
			this.minMatrixValue = minMatrixValue;
		}
	}
	public void setScoreMatFile(String scoreMatFile) {
		FileOperate.checkFileExistAndBigThanSize(scoreMatFile, 0);
		this.scoreMatFile = scoreMatFile;
	}
	public void setMinLength(int minLength) {
		if (minLength > 0) {
			this.minLength = minLength;
		}
	}
	public void setPepFile(String pepFile) {
		FileOperate.checkFileExistAndBigThanSize(pepFile, 0);
		this.pepFile = pepFile;
	}
	public void setCdsResultFile(String cdsResultFile) {
		FileOperate.checkFileExistAndBigThanSize(cdsResultFile, 0);
		this.cdsResultFile = cdsResultFile;
	}
	public ESTScanCDSPredict() {
		SoftWareInfo softWareInfo = new SoftWareInfo(SoftWare.ESTScan);
		this.exePath = softWareInfo.getExePathRun();
	}
	public void run() {
		CmdOperate cmdOperate = new CmdOperate(getLsCmd());
		cmdOperate.runWithExp("CAP3 error:");
	}
	private List<String> getLsCmd() {
		List<String> lsCmd = new ArrayList<>();
		lsCmd.add(exePath + "misa.pl");
		ArrayOperate.addArrayToList(lsCmd, getInputFile(inputFile));
		ArrayOperate.addArrayToList(lsCmd, getMinMatrixValue());
		ArrayOperate.addArrayToList(lsCmd, getMinLength());
		ArrayOperate.addArrayToList(lsCmd, getPepFile());
		ArrayOperate.addArrayToList(lsCmd, getCdsResultFile());
		return lsCmd;
	}
	private String[] getInputFile(String inputFile) {
		return new String[]{" ", inputFile};
	}
	private String[] getMinMatrixValue() {
		return new String[] {"-m", minMatrixValue + ""};
	}
	private String[] getScoreMatFile() {
		return new String[] {"-M", scoreMatFile + ""};
	}
	private String[] getMinLength() {
		return new String[] {"-l", minLength + ""};
	}
	private String[] getPepFile() {
		return new String[] {"-t", pepFile + ""};
	}
	private String[] getCdsResultFile() {
		return new String[] {">", cdsResultFile + ""};
	}
	@Override
	public List<String> getCmdExeStr() {
		// TODO Auto-generated method stub
		return null;
	}
}
