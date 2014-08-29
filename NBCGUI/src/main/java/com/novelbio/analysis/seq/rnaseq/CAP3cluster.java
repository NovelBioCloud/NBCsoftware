package com.novelbio.analysis.seq.rnaseq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.novelbio.analysis.IntCmdSoft;
import com.novelbio.base.cmd.CmdOperate;
import com.novelbio.base.dataOperate.TxtReadandWrite;
import com.novelbio.base.dataStructure.ArrayOperate;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.database.domain.information.SoftWareInfo;
import com.novelbio.database.domain.information.SoftWareInfo.SoftWare;

/**
 * simple script : cap3 trinity.fa -f 20 -o 100 -p 90 -z 3 > trinity.cap3.result.txt
 * @author bll
 *
 */

public class CAP3cluster implements IntCmdSoft {
	private static String OVERLAPGAPLEN = "20";
	private static String OVERLAPLENCUTFF = "40";
	private static String OVERLAPIDEPERCUTFF = "90";
	private static String READSSUPPORTNUM = "3";
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
	/** 存放Contigs ID 对应 Transcript ID 关系*/
	Map hasContigTra = new HashMap();
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
		if ((overlapGapLen.equals(""))||(overlapGapLen==null)) {
			overlapGapLen = OVERLAPGAPLEN;
		}
		this.overlapGapLen = overlapGapLen;
	}
	/** 设定overlap区域长度  */
	public void steOverlapLenCutff(String overlapLenCutff) {
		if ((overlapLenCutff.equals(""))||(overlapLenCutff==null)) {
			overlapLenCutff = OVERLAPLENCUTFF;
		}	
		this.overlapLenCutff = overlapLenCutff;
	}
	/** 设定overlap一致性比例  */
	public void setOverlapIdePerCutff(String overlapIdePerCutff) {
		if ((overlapIdePerCutff.equals(""))||(overlapIdePerCutff==null)) {
			overlapIdePerCutff = OVERLAPIDEPERCUTFF;
		}	
		this.overlapIdePerCutff = overlapIdePerCutff;
	}
	/** 设定clip 位置reads支持数  */
	public void setReadsSupportNum(String readsSupportNum) {
		if ((readsSupportNum.equals(""))||(readsSupportNum==null)) {
			readsSupportNum = READSSUPPORTNUM;
		}		
		this.readsSupportNum = readsSupportNum;
	}

	public CAP3cluster(){
		SoftWareInfo softWareInfo = new SoftWareInfo(SoftWare.cap3);
		this.exePath = softWareInfo.getExePathRun();
	}
	public void run() {
		CmdOperate cmdOperate = new CmdOperate(getLsCmd());
		cmdOperate.runWithExp("CAP3 error!");
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
		ContigIDToTranID contigIDToTranID = new ContigIDToTranID();
		contigIDToTranID.getContigIDToTranID();
		lsCmd.addAll(cap3cluster.getCmdExeStr());	
	}
	public List<String> getCmdExeStr() {
		List<String> lsResult = new ArrayList<String>();
		CmdOperate cmdOperate = new CmdOperate(getLsCmd());
		lsResult.add(cmdOperate.getCmdExeStr());
		return lsResult;
	}
	/**根据CAP3输出结果，提取Contig 对应 转录组本ID信息，存在HashMap中 */
	class ContigIDToTranID{
		/** CAP3结果文件名称*/
		String excelCAP3ResultFileName;
		/** CAP3 输出的Singlets 文件名称*/
		String excelCAP3ResultSingletsFileName;
		/** Contig ID 对应Transcript ID 列表结果文件名称*/
		String outContigIDToTranIDFileName;
		private void getContigIDToTranID(){
			this.excelCAP3ResultFileName = excelCAP3ResultFileName;
			TxtReadandWrite txtContig = new TxtReadandWrite(excelCAP3ResultFileName);
			String traID = "\t";
			String[] contigsID;
			String newContigID = "";
			for (String lineContentString : txtContig.readlines()) {
				if (lineContentString.startsWith("*")) {
					traID = "\t";
					contigsID = lineContentString.split(" ");
					newContigID = contigsID[1].concat(contigsID[2]);
				}else if (lineContentString.startsWith("c")) {
					lineContentString = lineContentString.substring(0,lineContentString.length()-1);
					traID = traID.concat(lineContentString.concat("\t"));	
					hasContigTra.put(newContigID, traID);
				}
			}
			this.getSingletsIDToTranID();
			this.print(hasContigTra);
			txtContig.close();
		}
		/**提取Singlets文件中的序列ID信息 */
		private void getSingletsIDToTranID(){
			this.excelCAP3ResultSingletsFileName =fastaNeedCluster.concat(".cap.singlets");
			TxtReadandWrite txtSinglets = new TxtReadandWrite(excelCAP3ResultSingletsFileName);
			for (String lineSingletsString : txtSinglets.readlines()) {
				if (lineSingletsString.startsWith(">")) {
					lineSingletsString = lineSingletsString.substring(1);
					hasContigTra.put(lineSingletsString.concat("\t"),lineSingletsString);
				}
			}
		}	
		/**用来输出Contig 对应 转录组本ID信息 */
		private void print(Map hashMap){
			this.outContigIDToTranIDFileName =  excelCAP3ResultFileName.concat("_contotra.xls");
			TxtReadandWrite txtWrite = new TxtReadandWrite(outContigIDToTranIDFileName,true);
			Iterator iterator = hashMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry entry = (Map.Entry) iterator.next();
				Object contigID = entry.getKey();
				Object val = entry.getValue();
				txtWrite.writefileln(contigID.toString().concat(val.toString()));
			}
			txtWrite.close();
		}
	}
}
