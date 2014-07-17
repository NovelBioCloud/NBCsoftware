package com.novelbio.analysis.seq.lnc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import com.novelbio.analysis.IntCmdSoft;
import com.novelbio.analysis.seq.fastq.FastQ;
import com.novelbio.analysis.seq.fasta.SeqFasta;
import com.novelbio.analysis.seq.mapping.StrandSpecific;
import com.novelbio.base.StringOperate;
import com.novelbio.base.cmd.CmdOperate;
import com.novelbio.base.cmd.ExceptionCmd;
import com.novelbio.base.dataOperate.ExcelTxtRead;
import com.novelbio.base.dataOperate.TxtReadandWrite;
import com.novelbio.base.dataStructure.ArrayOperate;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.database.domain.information.SoftWareInfo;
import com.novelbio.database.domain.information.SoftWareInfo.SoftWare;

/**
 * simple script : cpat.py -g lncRNA_candiate.fa -x *_hexamer.table -d *_logit.RData -o  *.result.xls
 * @author bll
 *
 */
public class CPAT implements IntCmdSoft {  //implements IntCmdSoft
	String exePath = "";
	
	
	
	/** 需要预测的序列文件，fasta格式文件 */
	String lncRNACanFa;

	/** 预先构建的六聚体频率表，格式表头：hexamer conding noncoding；例如：AAAAAAA 0.000647111 0.002072893 */
	String hexTab;
	
	/** 预先构建的训练对数模型，该文件为二进制文件 */
	String logRData;
	
	/** 被分析物种名称，用于给生成的训练集名称 */
	String species;
	/** 被分析物种的CDS区域序列文件，fasta格式文件，如果没有, 则用同源物种的代替*/
	
	String speCDSFa;
	
	/** 被分析物种的non-coding区域序列文件，fasta格式文件，如果没有，则用同源物种的代替 */
	String speNONCODFa;

	/** 输出文件路径及名称 */
	String outDir;
	
	/** 编码阈值设置  */
	Double codProb;
	
	public static void main(String[] args) {
		CPAT cpat = new CPAT();
		
		//
	}

	public CPAT() {
//		SoftWareInfo softWareInfo = new SoftWareInfo(SoftWare.cpat); 
		//com.novelbio.database.domain.information.SoftWareInfo.SoftWare.cpat
		
		CPATDB c = new CPATDB();
//		this.exePath = softWareInfo.getExePathRun();
	}
	
	/** 设置CPAT所在路径 */
	public void setExePath(String exePath) {
		if (exePath == null || exePath.equals("")) {
			return;
		}
		this.exePath = FileOperate.addSep(exePath);
	}
		
	private String[] getLncRNACanFa() {
		if (lncRNACanFa == null) {
			return null;
		}
		return new String[]{"-g", lncRNACanFa};
	}
 		
// 		Map<String, String> mapStye2File = new HashMap<>();

		public String[] getHexTab() {
 			if (hexTab == null) {
// 				mapStye2File.put("exePath",  exePath);
// 				mapStye2File.put("cds",  speCDSFa);
// 				mapStye2File.put("non",  speNONCODFa);	
// 				mapStye2File.put("hexTab",  hexTab);	
 				CPATDB cpatDB = new CPATDB();
 		 		cpatDB.runCPATHex( );
 			} 
 			return new String[]{"-x", hexTab};
 		}
	
	private String[] getLogRData() {
		if (logRData == null) {
			CPATDB cpatDB = new CPATDB();
	 		cpatDB.runCPATlog();
			//return null;
		}
		return new String[]{"-d", logRData};
	}
	
	private String[] getCPATOutput() {
		if (outDir == null) {
			return null;
		}
		return new String[]{"-o", outDir};
	}
		
	public void runCPAT() {
		List<String> lsCmd = getLsCmd();
		CmdOperate cmdOperate = new CmdOperate(lsCmd);
		cmdOperate.run();
		if (!cmdOperate.isFinishedNormal()) {
			throw new ExceptionCmd("run CPAT error:\n" + cmdOperate.getCmdExeStr() + "\n" + cmdOperate.getErrOut());
		}
	}
	
	private List<String> getLsCmd() {
		List<String> lsCmd = new ArrayList<String>();
		lsCmd.add(exePath + "cpat.py");
		ArrayOperate.addArrayToList(lsCmd, getLncRNACanFa());
		ArrayOperate.addArrayToList(lsCmd, getHexTab());
		ArrayOperate.addArrayToList(lsCmd, getLogRData());
		ArrayOperate.addArrayToList(lsCmd, getCPATOutput());
		return lsCmd;
	}

	public List<String> getCmdExeStr() {
		List<String> lsResult = new ArrayList<String>();
		List<String> lsCmd = getLsCmd();
		CmdOperate cmdOperate = new CmdOperate(lsCmd);
		lsResult.add(cmdOperate.getCmdExeStr());
		return lsResult;
	}
}

	class CPATDB  extends CPAT {
		
		FileOperate fileOperate = new FileOperate();
	
		
		/** 被分析物种名称，用于给生成的训练集名称 */
//		String species;
		
//		String outDir;
	 
		
		public static void CPATDB() {
//		SoftWareInfo softWareInfo = new SoftWareInfo(SoftWare.cpat); 
//			//com.novelbio.database.domain.information.SoftWareInfo.SoftWare.cpat
//			
//			//CPATDB c = new CPATDB();
//			this.exePath = softWareInfo.getExePathRun();
		}
		private String[] getSpeCDSFa() {
	 	 	if (speCDSFa == null) {
	 	 			return null;
	 	 		}
	 	 		return new String[]{"-c", speCDSFa};
	 	 }
	 		
	 	 	private String[] getSpeNONCODFa() {
	 	 	if (speNONCODFa == null) {
	 	 	
	 	 			return null;
	 	 		}
	 	 	return new String[]{"-n", speNONCODFa};
	 	 }
	 	 	private String[] getHexOutput() {
	 			if (outDir == null) {
	 				return null;
	 			}
	 			return new String[]{">", outDir + species + "__hexamer.table"};
	 		}
	 		
	 		private String[] getLogOutput() {
	 			if (outDir == null) {
	 				return null;
	 			}
	 			return new String[]{"-o",  outDir + species };
	 		}	
		
	/** python make_hexamer_tab.py  -c rna_modify.fa -n ../data/NONCODEv4_tair.fa > Rice_hexamer.table */
		public void runCPATHex() {
			
			if (fileOperate.isFileExist(hexTab)){
				return;
			} else {
				List<String> lsHexCmd =   getLsHexCmd();
				CmdOperate cmdOperate = new CmdOperate(lsHexCmd);
				cmdOperate.run();
				if (!cmdOperate.isFinishedNormal()) {
					throw new ExceptionCmd("run CPAT error:\n" + cmdOperate.getCmdExeStr() + "\n" + cmdOperate.getErrOut());
				}
			}
			
		}
		
		private List<String> getLsHexCmd() {
			List<String> lsHexCmd = new ArrayList<String>();
			lsHexCmd.add(exePath + "make_hexamer_tab.py");
			ArrayOperate.addArrayToList(lsHexCmd, getSpeCDSFa());
			ArrayOperate.addArrayToList(lsHexCmd, getSpeNONCODFa());
			ArrayOperate.addArrayToList(lsHexCmd, getHexOutput());   
			return lsHexCmd;
		}
	
		/** python make_logitModel.py -x Rice_hexamer.table -c rna_modify.fa -n ../data/NONCODEv4_tair.fa -o  Rice */
		
		public void runCPATlog( ) {
			if (fileOperate.isFileExist(logRData)){
				return;
			} else {
				List<String> lsLogCmd =   getLsLogCmd();
				CmdOperate cmdOperate = new CmdOperate(lsLogCmd);
				cmdOperate.run();
				if (!cmdOperate.isFinishedNormal()) {
					throw new ExceptionCmd("run CPAT error:\n" + cmdOperate.getCmdExeStr() + "\n" + cmdOperate.getErrOut());
				}
			}
		}
		
		private List<String> getLsLogCmd() {
			List<String> lsLogCmd = new ArrayList<String>();
			lsLogCmd.add(exePath + "make_logitModel.py");
			ArrayOperate.addArrayToList(lsLogCmd, getSpeCDSFa());
			ArrayOperate.addArrayToList(lsLogCmd, getHexTab());
			ArrayOperate.addArrayToList(lsLogCmd, getSpeNONCODFa());
			ArrayOperate.addArrayToList(lsLogCmd, getLogOutput());   
			return lsLogCmd;
		}
	}
	
	
	class filterCPATResult {
		String excelFileName;
		String outFileName;
		
		private void filterCPATResult(String excelFileName) {
			this.excelFileName = excelFileName;
			this.outFileName = FileOperate.changeFileSuffix(excelFileName, "_filter", null);
			TxtReadandWrite txtWrite = new TxtReadandWrite(excelFileName, true);
			List<String[]> lsAll = ExcelTxtRead.readLsExcelTxt(excelFileName,1);
			for (String[] strings : lsAll.subList(1, lsAll.size())) {
				filter(strings);
			}
			
		}
		private void filter(String[] info) {
			for (int i = 0; i <info.length; i++) {
				String code = info[i].split("\t")[-1];
				//if (Double.parseDouble(code)<) {
					
				//}
			}
			
		}
		
		
	}
