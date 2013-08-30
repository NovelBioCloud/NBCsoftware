package com.novelbio.nbcgui.controlquery;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import com.novelbio.base.dataStructure.ArrayOperate;
import com.novelbio.database.domain.kegg.KGentry;
import com.novelbio.database.domain.kegg.KGpathway;
import com.novelbio.database.model.modgeneid.GeneID;
import com.novelbio.nbcgui.GUI.GuiBlastJpanel;

/**
 * @author zong0jie
 */
public class CtrlBlastPath extends SwingWorker<ArrayList<String[]>, ProgressDataPath> {


	/**
	 * 是否需要blast
	 */
	boolean blast = false;
	/**
	 * 查找物种
	 */
	int taxID = 0;
	/**
	 * blast物种
	 */
	int StaxID = 0;
	/**
	 * blast的evalue
	 */
	double evalue = 100;
	
	/**
	 * Go的类型
	 */
	String GoClass = "P";
	/**
	 * 界面对象
	 */
//	GUIBlast guiBlast;
	GuiBlastJpanel guiBlast;
	/**
	 * 
	 * @param blast
	 * @param taxID
	 * @param StaxID
	 * @param evalue
	 * @param guiBlast
	 */
//	public CtrlBlastPath(boolean blast, int taxID, int StaxID, double evalue,GUIBlast guiBlast) {
	public CtrlBlastPath(boolean blast, int taxID, int StaxID, double evalue,GuiBlastJpanel guiBlast) {
		this.blast = blast;
		this.taxID = taxID;
		this.StaxID = StaxID;
		this.evalue = evalue;
		this.guiBlast =guiBlast;
		
	}
	
	List<String> lsGeneID = null;
 
	
	/**
	 * 准备工作，将geneID读入内存同时准备查找，同时返回总共查找的数量，给进度条计数
	 * @return
	 * @throws Exception
	 */
	public int prepare(List<String> lsGeneID) {
		this.lsGeneID = lsGeneID;
		return lsGeneID.size();
	}
	
	/**
	 * 给定文件，和文件分割符，以及第几列，获得该列的基因ID
	 * @param fileName
	 * @return
	 * @throws Exception 
	 */
	public ArrayList<String[]> doInBackground() throws Exception {
		System.out.println("run");
		ArrayList<String[]> lsDesp = new ArrayList<String[]>();
		int length = 0;
		if (blast) 
			length = 13;
		else 
			length = 5; 
		
		for (int i = 0; i<lsGeneID.size(); i++) {
			String geneID = lsGeneID.get(i).trim();
		
			try {
				ArrayList<String[]> lstmpAnno = getGenPath(geneID, taxID, blast, StaxID, evalue);
				if (lstmpAnno == null || lstmpAnno.size()<1) {
					String[] tmp = new String[length];
					for (int j = 0; j < tmp.length; j++) {
						tmp[j] = "";
					}
					tmp[0] = geneID;
					lstmpAnno = new ArrayList<String[]>();
					lstmpAnno.add(tmp);
				}
	
				ProgressDataPath progressData  = new ProgressDataPath();
				progressData.rowNum = i;
				progressData.lsInfo = lstmpAnno;
				publish(progressData);
				lsDesp.addAll(lstmpAnno);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return lsDesp;
	}
	
	
	
	/**
	 * 给定accID，查询该accID所对应的pathway
	 * 目前只能在NCBIID中查询，不能在UniProt中查询
	 * @param accID accID 需要去空格处理以及判断accID是否为空
	 * @param taxID
	 * @param blast
	 * @param subTaxID
	 * @param evalue
	 * @return 如果没查到则返回null
	 * 如果blast：
	 * 0:accID
	 * 1:Symbol/AccID
	 * 2:PathID
	 * 3:PathName
	 * 4:evalue
	 * 5:SubjectSymbol
	 * 6:PathID
	 * 7:PathName
	 * 如果没有blast
	 * 0:accID
	 * 1:Symbol/AccID
	 * 2:PathID
	 * 3:PathName
	 */
	public static ArrayList<String[]> getGenPath(String accID,int taxID,boolean blast,int subTaxID,double evalue) {
		String[] tmpAccIDInfo = new String[] { accID, ""};
		ArrayList<String[]> lsResult = new ArrayList<String[]>();
		GeneID copedID = new GeneID(accID, taxID);
		if (copedID.getIDtype() == GeneID.IDTYPE_ACCID) {
			return null;
		}
		tmpAccIDInfo[1] = copedID.getSymbol();
		copedID.setBlastInfo(evalue, subTaxID);
		// 本基因的Path信息
		ArrayList<KGpathway> lsKGentrythis = copedID.getKegPath(blast);
		HashSet<String> hashPathID = new HashSet<String>();
		for (KGpathway kGpathway : lsKGentrythis) {
			String[] tmpResult = ArrayOperate.copyArray(tmpAccIDInfo, 4);
			if (hashPathID.contains(kGpathway.getMapNum())) {
				continue;
			}
			tmpResult[2] = "PATH:" + kGpathway.getMapNum();
			tmpResult[3] = kGpathway.getTitle();
			lsResult.add(tmpResult);
		}
		if (!blast) {
			if (lsResult.size() == 0) {
				return null;
			}
			return lsResult;
		}
		// blast基因的GO信息
		ArrayList<String[]> lsResultBlast = new ArrayList<String[]>();

		GeneID copedIDblast = copedID.getGeneIDBlast();
		if (copedIDblast == null) {
			for (String[] strings : lsResult) {
				String[] result = ArrayOperate.copyArray(strings, 8);
				lsResultBlast.add(result);
			}
			return lsResultBlast;
		}
		HashSet<String> hashPathIDBlast = new HashSet<String>();
		ArrayList<KGentry> lsPathBlast = copedIDblast.getKegEntity(false);
		int k = 0;
		for (int i = 0; i < lsPathBlast.size(); i++) {
			if (hashPathIDBlast.contains(lsPathBlast.get(i).getPathName())) {
				continue;
			}
			if (lsResult == null)
				lsResult = new ArrayList<String[]>();
			String[] tmpResultBlast = new String[8];
			// 初始化
			for (int j = 0; j < tmpResultBlast.length; j++) {
				tmpResultBlast[j] = "";
			}
			if (k < lsResult.size()) {
				for (int j = 0; j < lsResult.get(k).length; j++) {
					tmpResultBlast[j] = lsResult.get(k)[j];
				}
			} else {
				tmpResultBlast[0] = accID;
				tmpResultBlast[1] = copedID.getSymbol();
			}
			tmpResultBlast[4] = copedID.getLsBlastInfos().get(0).getEvalue() + "";
			tmpResultBlast[5] = copedIDblast.getSymbol();
			tmpResultBlast[6] = lsPathBlast.get(i).getPathName();
			tmpResultBlast[7] = lsPathBlast.get(i).getPathTitle();
			lsResultBlast.add(tmpResultBlast);
			k ++;
		}

		return lsResultBlast;
	}
	
	
	
	
	@Override
	public void process(List<ProgressDataPath> data) {
		if (isCancelled() || guiBlast == null) {
			return;
		}
		for (ProgressDataPath ProgressDataPath : data) {
			if (guiBlast.getJProgressBar1().getValue()<ProgressDataPath.rowNum) {
				guiBlast.getJProgressBar1().setValue(ProgressDataPath.rowNum);
			}
			for (String[] strings: ProgressDataPath.lsInfo) {
				guiBlast.getJTabGoandPath().addItem(strings);
			}
		}
	}
	
	public void done() {
		if (guiBlast == null) {
			return;
		}
		int maxValue = guiBlast.getJProgressBar1().getMaximum();
		guiBlast.getJProgressBar1().setValue(maxValue);
		guiBlast.getJBtnGoPath().setEnabled(true);
		guiBlast.getJBtnSaveGO().setEnabled(true);
		guiBlast.getJLbGOandPath().setText("Complete");
		try {
			guiBlast.setLsGoandPath(this.get());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
class ProgressDataPath
{
	int rowNum=0;
	ArrayList<String[]> lsInfo = null;
}
