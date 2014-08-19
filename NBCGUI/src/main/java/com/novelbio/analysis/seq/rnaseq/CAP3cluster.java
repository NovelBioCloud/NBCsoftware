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
 * simple script : cap3 trinity.fa > trinity.cap3.result
 * @author bll
 *
 */

public class CAP3cluster implements IntCmdSoft {
	String exePath = "";	
	
	/** 需要聚类的序列文件，fasta格式文件 */
	String fastaNeedCluster;
	
	/** 输出文件路径及名称 */
	String outFile;
	
	List<String> lsCmd = new ArrayList<String>();
	
	/** 设定需要聚类的序列文件，fasta格式 */
	public void setFastaNeedCluster(String fastaNeedCluster) {
		FileOperate.checkFileExistAndBigThanSize(fastaNeedCluster, 0);
		this.fastaNeedCluster = fastaNeedCluster;
	}
	
	private String[] getFastaNeedCluster(){
		return new String[]{" ",fastaNeedCluster};
	}
	
	/** 输出文件名 */
	public void setOutFile(String outFile) {
		this.outFile = outFile;
	}
	
	private String[] getOutFile() {
		return new String[]{">", outFile};
	}
	
	public CAP3cluster(){
		SoftWareInfo softWareInfo = new SoftWareInfo(SoftWare.cap);
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
		ArrayOperate.addArrayToList(lsCmd, getOutFile());
		return lsCmd;
	}
	
	public void cluster(){
		lsCmd.clear();
		CAP3cluster cap3cluster = new CAP3cluster();
		cap3cluster.setFastaNeedCluster(fastaNeedCluster);
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
