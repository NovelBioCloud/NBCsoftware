package com.novelbio.nbcgui.controlquery;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import com.novelbio.base.dataStructure.ArrayOperate;
import com.novelbio.database.domain.geneanno.AGene2Go;
import com.novelbio.database.domain.geneanno.GOtype;
import com.novelbio.database.domain.geneanno.Gene2Go;
import com.novelbio.database.domain.geneanno.Go2Term;
import com.novelbio.database.model.modgeneid.GeneID;
import com.novelbio.nbcgui.GUI.GuiBlastJpanel;


public class CtrlBlastGo extends SwingWorker<ArrayList<String[]>, ProgressDataGo> {
	/** 查找物种 */
	int taxID = 0;
	/** blast物种 */
	int StaxID = -1;
	/** blast的evalue */
	double evalue = 100;
	
	/** Go的类型 */
	GOtype GoClass = GOtype.BP;
	/** 界面对象 */
	GuiBlastJpanel guiBlast;
	
	List<String> lsGeneID = null;
	
	/**
	 * @param guiBlast
	 */
	public CtrlBlastGo(GuiBlastJpanel guiBlast) {
		this.guiBlast =guiBlast;
	}
	public void setTaxID(int taxID) {
		this.taxID = taxID;
	}
	public void setGoClass(GOtype goClass) {
		GoClass = goClass;
	}
	/**
	 * 如果 StaxID小于0，就表示不进行blast
	 * @param StaxID
	 * @param evalue
	 */
	public void setBlastInfo(int StaxID, double evalue) {
		this.StaxID = StaxID;
		this.evalue = evalue;
	}
	/**
	 * 准备工作，将geneID读入内存同时准备查找，同时返回总共查找的数量，给进度条计数
	 * @return
	 * @throws Exception
	 */
	public int prepare(List<String> lsGeneID) {
		this.lsGeneID = lsGeneID;
		return lsGeneID.size();
	}
	
	private boolean isBlast() {
		if (StaxID > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 给定文件，和文件分割符，以及第几列，获得该列的基因ID
	 * @param fileName
	 * @return
	 * @throws Exception 
	 */
	public ArrayList<String[]> doInBackground() throws Exception {
		ArrayList<String[]> lsDesp = new ArrayList<String[]>();
		for (int i = 0; i<lsGeneID.size(); i++) {
			String geneID = lsGeneID.get(i).trim();
			List<String[]> lstmpAnno = getLsGeneGo(geneID);
			ProgressDataGo progressData  = new ProgressDataGo();
			progressData.rowNum = i;
			lsDesp.addAll(lstmpAnno);
			progressData.lsInfo = lstmpAnno;
			publish(progressData);
		}
		return lsDesp;
	}

	private List<String[]> getLsGeneGo(String accID) {
		List<String[]> lsResult = null;
		GeneID geneID = new GeneID(accID, taxID);
//		if (geneID.getIDtype() == GeneID.IDTYPE_ACCID) {
//			String[] result = null;
//			if (!isBlast()) {
//				result = new String[4];
//			} else {
//				result = new String[8];
//			}
//			result[0] = accID;
//			for (int i = 1; i < result.length; i++) {
//				result[i] = "";
//			}
//			lsResult = new ArrayList<String[]>();
//			lsResult.add(result);
//		} else {
//	
//		}
		if (!isBlast()) {
			lsResult = getLsGOInfo(geneID);
		} else {
			lsResult = getLsGOInfoBlast(geneID);
		}
		return lsResult;
	}
	
	private List<String[]> getLsGOInfo(GeneID geneID) {
		List<String[]> lsResult = new ArrayList<String[]>();
		
		ArrayList<String> lsResultTmp = new ArrayList<String>();
		lsResultTmp.add(geneID.getAccID());
		lsResultTmp.add(geneID.getSymbol());
		List<AGene2Go> lsGene2Gos = geneID.getGene2GO(GoClass);
		for (AGene2Go aGene2Go : lsGene2Gos) {
			ArrayList<String> lsTmp = (ArrayList<String>) lsResultTmp.clone();
			lsTmp.add(aGene2Go.getGOID());
			lsTmp.add(aGene2Go.getGOTerm());
			lsResult.add(lsTmp.toArray(new String[0]));
		}
		if (lsResult.size() == 0) {
			fillLsResult(geneID.getAccID(), lsResult, 4);
		}
		return lsResult;
	}
	
	private List<String[]> getLsGOInfoBlast(GeneID geneID) {
		List<String[]> lsResult = new ArrayList<String[]>();
		
		ArrayList<String> lsResultTmp = new ArrayList<String>();
		lsResultTmp.add(geneID.getAccID());
		lsResultTmp.add(geneID.getSymbol());
		
		List<AGene2Go> lsGene2Gos = geneID.getGene2GO(GoClass);
		geneID.setBlastInfo(evalue, StaxID);
		List<AGene2Go> lsGene2GoBlast = new ArrayList<AGene2Go>();
		if (geneID.getGeneIDBlast() != null) {
			lsGene2GoBlast = geneID.getGeneIDBlast().getGene2GO(GoClass);
		}
		List<AGene2Go[]> lsGoInfo = getLsGOInfoBlast(lsGene2Gos, lsGene2GoBlast);
		for (AGene2Go[] aGene2Gos : lsGoInfo) {
			ArrayList<String> lsTmp = (ArrayList<String>) lsResultTmp.clone();
			addGoInfo(lsTmp, aGene2Gos[0]);
			if (aGene2Gos[1] != null) {
				lsTmp.add(geneID.getLsBlastInfos().get(0).getEvalue() + "");
				lsTmp.add(geneID.getGeneIDBlast().getSymbol());
			} else {
				lsTmp.add(""); lsTmp.add("");
			}
			addGoInfo(lsTmp, aGene2Gos[1]);
			lsResult.add(lsTmp.toArray(new String[0]));
		}
		if (lsResult.size() == 0) {
			fillLsResult(geneID.getAccID(), lsResult, 8);
		}
		
		return lsResult;
	}
	
	private void addGoInfo(List<String> lsTmp, AGene2Go aGene2Go) {
		if (aGene2Go == null) {
			lsTmp.add(""); lsTmp.add("");
		} else {
			lsTmp.add(aGene2Go.getGOID());
			lsTmp.add(aGene2Go.getGOTerm());
		}
	}
	
	private void fillLsResult(String accID, List<String[]> lsResult, int arrayLength) {
		String[] tmpResult = new String[arrayLength];
		tmpResult[0] = accID;
		for (int i = 1; i < tmpResult.length; i++) {
			tmpResult[i] = "";
		}
		lsResult.add(tmpResult);
	}
	
	/**
	 * 把两个list里面的GO合并在一个list里面，相同的Go放在一列
	 * @param lsGene2Go
	 * @param lsGene2GoBlast
	 * @return
	 */
	private List<AGene2Go[]> getLsGOInfoBlast(List<AGene2Go> lsGene2Go, List<AGene2Go> lsGene2GoBlast) {
		List<AGene2Go[]> lsAGene2Gos = new ArrayList<AGene2Go[]>();
		Map<String, AGene2Go> mapGOID2DetailBlast = new HashMap<String, AGene2Go>();
		for (AGene2Go aGene2Go : lsGene2GoBlast) {
			mapGOID2DetailBlast.put(aGene2Go.getGOID(), aGene2Go);
		}
		
		for (AGene2Go aGene2Go : lsGene2Go) {
			AGene2Go[] aGene2Gos = new AGene2Go[2];
			aGene2Gos[0] = aGene2Go;
			if (mapGOID2DetailBlast.containsKey(aGene2Go.getGOID())) {
				aGene2Gos[1] = mapGOID2DetailBlast.get(aGene2Go.getGOID());
				mapGOID2DetailBlast.remove(aGene2Go.getGOID());
			}
			lsAGene2Gos.add(aGene2Gos);
		}
		
		for (AGene2Go aGene2Go : mapGOID2DetailBlast.values()) {
			AGene2Go[] aGene2Gos = new AGene2Go[2];
			aGene2Gos[1] = aGene2Go;
			lsAGene2Gos.add(aGene2Gos);
		}
		return lsAGene2Gos;
	}
	
	public static String[] getTitle(boolean blast) {
		List<String> lsTitle = new ArrayList<String>();
		lsTitle.add("QueryID");
		lsTitle.add("Symbol/AccID");
		lsTitle.add("GOID");
		lsTitle.add("GOTerm");
		if (blast) {
			lsTitle.add("evalue");
			lsTitle.add("BlastSymbol/AccID");
			lsTitle.add("BlastGOID");
			lsTitle.add("GOTerm");
		}
		return lsTitle.toArray(new String[0]);
	}
	
	
	@Override
	public void process(List<ProgressDataGo> data) {
		if (isCancelled() || guiBlast == null) {
			return;
		}
		for (ProgressDataGo progressDataGo : data) {
			if (guiBlast.getJProgressBar1().getValue()<progressDataGo.rowNum) {
				guiBlast.getJProgressBar1().setValue(progressDataGo.rowNum);
			}
			for (String[] strings: progressDataGo.lsInfo) {
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

class ProgressDataGo {
	int rowNum=0;
	List<String[]> lsInfo = null;
}
