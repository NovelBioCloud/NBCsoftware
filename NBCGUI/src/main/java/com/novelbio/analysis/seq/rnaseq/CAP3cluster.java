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
 * simple script : cap3 trinity.fa -f 20 -o 100 -p 90 -z 3 > trinity.cap3.result
 * @author bll
 *
 */

public class CAP3cluster implements IntCmdSoft {
	String exePath = "";	
	
	/** 需要聚类的序列文件，fasta格式文件 */
	String fastaNeedCluster;
	/** 输出文件路径及名称 */
	String outFile;
	/** overlap区域允许的最大gap长度，默认 20 */
	String overlapGapLen;
	/** overlap区域长度阈值，默认 40 */
	String overlapLenCutff;
	/** overlap区域一致性比例，默认 90 */
	String overlapIdePerCutff;
	/** clip 位置reads支持数，默认 3 */
	String readsSupportNum;
	
	List<String> lsCmd = new ArrayList<String>();
	
	/** 设定需要聚类的序列文件，fasta格式 */
	public void setFastaNeedCluster(String fastaNeedCluster) {
		FileOperate.checkFileExistAndBigThanSize(fastaNeedCluster, 0);
		this.fastaNeedCluster = fastaNeedCluster;
	}
	/** 输出文件名 */
	public void setOutFile(String outFile) {
		this.outFile = outFile;
	}
	/** 设定overlap区域允许的最大gap长度  */
	public void setOverlapGapLen(String overlapGapLen) {
		this.overlapGapLen = overlapGapLen;
	}
	/** 设定overlap区域长度  */
	public void steOverlapLenCutff(String overlapLenCutff) {
		this.overlapLenCutff = overlapLenCutff;
	}
	/** 设定overlap一致性比例  */
	public void setOverlapIdePerCutff(String overlapIdePerCutff) {
		this.overlapIdePerCutff = overlapIdePerCutff;
	}
	/** 设定clip 位置reads支持数  */
	public void setReadsSupportNum(String readsSupportNum) {
		this.readsSupportNum = readsSupportNum;
	}

	public CAP3cluster(){
		SoftWareInfo softWareInfo = new SoftWareInfo(SoftWare.cap3);
		this.exePath = softWareInfo.getExePathRun();
	}
	public void run() {
		CmdOperate cmdOperate = new CmdOperate(getLsCmd());
		cmdOperate.runWithExp("CAP3 error");
	}
	private List<String> getLsCmd() {
		List<String> lsCmd = new ArrayList<>();
		lsCmd.add(exePath + "cap3");
		ArrayOperate.addArrayToList(lsCmd, getFastaNeedCluster());
		ArrayOperate.addArrayToList(lsCmd, getOverlapGapLen());
		ArrayOperate.addArrayToList(lsCmd, getOverlapLenCutff());
		ArrayOperate.addArrayToList(lsCmd, getOverlapIdePerCutff());
		ArrayOperate.addArrayToList(lsCmd, getReadsSupportNum());
		ArrayOperate.addArrayToList(lsCmd, getOutFile());
		return lsCmd;
	}
	private String[] getFastaNeedCluster() {
		return new String[]{" ",fastaNeedCluster};
	}
	private String[] getOutFile() {
		return new String[]{">", outFile};
	}
	private String[] getOverlapGapLen() {
		return new String[]{"-f", overlapGapLen};
	}
	private String[] getOverlapLenCutff() {
		return new String[] {"-o", overlapLenCutff};
	}
	private String[] getOverlapIdePerCutff() {
		return new String[] {"-p", overlapIdePerCutff};
	}
	private String[] getReadsSupportNum() {
		return new String[] {"-z", readsSupportNum};
	}
	public void cluster(){
		lsCmd.clear();
		CAP3cluster cap3cluster = new CAP3cluster();
		cap3cluster.setFastaNeedCluster(fastaNeedCluster);
		cap3cluster.setOverlapGapLen(overlapGapLen);
		cap3cluster.setOverlapIdePerCutff(overlapIdePerCutff);
		cap3cluster.setReadsSupportNum(readsSupportNum);
		cap3cluster.setOutFile(outFile);
		cap3cluster.run();
		lsCmd.addAll(cap3cluster.getCmdExeStr());	
	}
	public List<String> getCmdExeStr() {
		List<String> lsResult = new ArrayList<String>();
		CmdOperate cmdOperate = new CmdOperate(getLsCmd());
		lsResult.add(cmdOperate.getCmdExeStr());
		return lsResult;
	}
}
