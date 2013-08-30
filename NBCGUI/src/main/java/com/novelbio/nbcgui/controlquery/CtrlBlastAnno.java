package com.novelbio.nbcgui.controlquery;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import com.novelbio.base.dataOperate.TxtReadandWrite;
import com.novelbio.base.dataStructure.ArrayOperate;
import com.novelbio.database.model.modgeneid.GeneID;
import com.novelbio.nbcgui.GUI.GuiBlastJpanel;



public class CtrlBlastAnno extends SwingWorker<ArrayList<String[]>, ProgressData>
{
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
	public CtrlBlastAnno(boolean blast, int taxID, int StaxID, double evalue,GuiBlastJpanel guiBlast) {
		this.blast = blast;
		this.taxID = taxID;
		this.StaxID = StaxID;
		this.evalue = evalue;
		this.guiBlast =guiBlast;
		
	}
	
	
	List<String> lsAccID = null;
	
	/**
	 * 准备工作，将geneID读入内存同时准备查找，同时返回总共查找的数量，给进度条计数
	 * @return
	 * @throws Exception
	 */
	public int prepare(List<String> lsGeneID) {
		this.lsAccID = lsGeneID;
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
			length = 6;
		else 
			length = 2;
		int i = 0;
		for (String accID : lsAccID) {
			i ++;
			accID = accID.trim();
			try {
				GeneID copedID = new GeneID(accID, taxID);
				copedID.setBlastInfo(1e-10, StaxID);
				String[] tmpAnno = copedID.getAnno(blast);
				if (tmpAnno == null) {
					tmpAnno = new String[length];
					for (int j = 0; j < tmpAnno.length; j++) {
						tmpAnno[j] = "";
					}
				}
				//在tmpAnno的最前面加上accID，最尾部加上keggID
				/////////去除物种那一列/////////////////////////////
				ArrayList<int[]> lsIndelInfo = new ArrayList<int[]>();
				lsIndelInfo.add(new int[]{0,2});
				lsIndelInfo.add(new int[]{length, 1});
				if (blast) {
					lsIndelInfo.add(new int[]{2,-1});
					lsIndelInfo.add(new int[]{3,1});
					lsIndelInfo.add(new int[]{4,1});
				}
				String[] tmpResult = ArrayOperate.indelElement(tmpAnno, lsIndelInfo, "");
				tmpResult[0] = accID;	//在tmpAnno的最前面加上accID
				tmpResult[1] = copedID.getAccID_With_DefaultDB().getAccID();//第二列加上默认数据库的ID
				if (!blast) {
					tmpResult[tmpResult.length - 1] = copedID.getKeggInfo().getKegID(); //最尾部加上keggID
				}
				else {
					tmpResult[4] = copedID.getKeggInfo().getKegID(); //最尾部加上keggID
					GeneID copedIDblast = copedID.getGeneIDBlast();
					if (copedIDblast != null ) {
						tmpResult[6] = copedIDblast.getAccID_With_DefaultDB().getAccID();//加上默认数据库的ID
						tmpResult[tmpResult.length - 1] = copedIDblast.getKeggInfo().getKegID(); //最尾部加上keggID
					}
				}
				///////////////////////////////////////////////////
				ProgressData progressData = new ProgressData();
				progressData.rowNum = i;
				progressData.tmpInfo = tmpResult;
				publish(progressData);
				lsDesp.add(tmpResult);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		return lsDesp;
	}
	
	@Override
	public void process(List<ProgressData> data)
	{
		if (isCancelled() || guiBlast == null) {
			return;
		}
		for (ProgressData progressData : data) {
			if (guiBlast.getJProgressBar1().getValue()<progressData.rowNum) {
				guiBlast.getJProgressBar1().setValue(progressData.rowNum);
			}
			guiBlast.getJTabAnnol().addRow(progressData.tmpInfo);
		}
	}
	
	public void done() {
		if (guiBlast == null) {
			return;
		}
		int maxValue = guiBlast.getJProgressBar1().getMaximum();
		guiBlast.getJProgressBar1().setValue(maxValue);
		guiBlast.getJBtnSaveAno().setEnabled(true);
		guiBlast.getJBtnAnno().setEnabled(true);
		guiBlast.getJLblCond().setText("Complete");
		try {
			guiBlast.setLsAnno(this.get());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}

class ProgressData
{
	public int rowNum;
	public String[] tmpInfo;
}

