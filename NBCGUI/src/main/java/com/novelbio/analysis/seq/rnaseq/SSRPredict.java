package com.novelbio.analysis.seq.rnaseq;

import java.util.ArrayList;
import java.util.List;

import com.novelbio.analysis.IntCmdSoft;
import com.novelbio.base.cmd.CmdOperate;
import com.novelbio.base.dataStructure.ArrayOperate;
import com.novelbio.database.domain.information.SoftWareInfo;
import com.novelbio.database.domain.information.SoftWareInfo.SoftWare;

public class SSRPredict implements IntCmdSoft {

	//输入文件：需要进行SSR预测的序列文件，fasta格式文件
		String inputFile;
		String exePath = "";	
		public void setInputFile(String inputFile) {
			this.inputFile = inputFile;
		}
		
		private String[] getInputFile(String inputFile) {
			return new String[]{" ",inputFile};
		}
		
		public SSRPredict() {
			SoftWareInfo softWareInfo = new SoftWareInfo(SoftWare.misa);
			this.exePath = softWareInfo.getExePathRun();
		}
		
		public void run() {
			CmdOperate cmdOperate = new CmdOperate(getLsCmd());
			cmdOperate.runWithExp("MISA error:");
		}
		private List<String> getLsCmd() {
			List<String> lsCmd = new ArrayList<>();
			lsCmd.add(exePath + "misa.pl");
			ArrayOperate.addArrayToList(lsCmd, getInputFile(inputFile));
			return lsCmd;
		}
}
