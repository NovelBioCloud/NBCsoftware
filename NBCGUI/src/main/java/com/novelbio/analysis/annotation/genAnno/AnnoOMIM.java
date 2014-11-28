package com.novelbio.analysis.annotation.genAnno;

import java.util.ArrayList;
import java.util.List;

import com.novelbio.database.domain.geneanno.AGene2Go;
import com.novelbio.database.domain.geneanno.GOtype;
import com.novelbio.database.model.modgeneid.GeneID;
import com.novelbio.omimdb.mgmt.MgmtGeneMIMInfo;
import com.novelbio.omimdb.mgmt.MgmtOMIM;
import com.novelbio.omimdb.mgmt.MgmtOMIMUnit;
import com.novelbio.omimdb.model.GeneMIM;
import com.novelbio.omimdb.model.MIMInfo;
import com.novelbio.omimdb.model.OmimGeneMap;

public class AnnoOMIM extends AnnoAbs {
	
	public String[] getTitle() {
		List<String> lsTitle = new ArrayList<String>();
		lsTitle.add("Symbol/AccID");
		lsTitle.add("GeneMIMID");
		lsTitle.add("PhenMIMID");
		lsTitle.add("OMIMTile");
		lsTitle.add("Refence");
		return lsTitle.toArray(new String[0]);
	}
	
//	public List<String[]> getOmimInfo(int taxID, String accID) {
		
//		List<String[]> lsResult = new ArrayList<String[]>();
//		GeneID geneID = new GeneID(accID, taxID);
//		ArrayList<String> lsResultTmp = new ArrayList<String>();
//		lsResultTmp.add(geneID.getSymbol());
//		int geneId = Integer.parseInt(geneID.getGeneUniID());
		
//	public List<String[]> getOmimInfo(int taxID, int accID) {
//		
//	}
	private void fillLsResult(String accID, List<String[]> lsResult, int arrayLength) {
		String[] tmpResult = new String[arrayLength];
		tmpResult[0] = accID;
		for (int i = 1; i < tmpResult.length; i++) {
			tmpResult[i] = "";
		}
		lsResult.add(tmpResult);
	}

	@Override
	public List<String[]> getInfo(int taxID, String accID) {
		int geneOmimId =0;	
		String[] omimAllInfo;
		List<String[]> lsResult = new ArrayList<String[]>();
		ArrayList<String> lsResultTmp = new ArrayList<String>();
		lsResultTmp.add(accID + "");
			
//		根据geneID在omimGenemap表中，查找对应的geneMimID
		MgmtGeneMIMInfo mgmtGeneMIMInfo = MgmtGeneMIMInfo.getInstance();
		List<GeneMIM> lsgeneOmimId = mgmtGeneMIMInfo.findOmimInfByGeneId(Integer.parseInt(accID));
		for (GeneMIM geneMIM:lsgeneOmimId) {
			geneOmimId = geneMIM.getGeneMimId();
		}
//		根据上一步得到的geneMimID,在表omimGenemap中查找对应的phenMimID
		MgmtOMIM mgmtOMIM = MgmtOMIM.getInstance();
		List<OmimGeneMap> lsOminGeneMaps = mgmtOMIM.findByGenMimId(geneOmimId);
		
		for (OmimGeneMap omimgene:lsOminGeneMaps) {
			ArrayList<String> lsTmp = (ArrayList<String>) lsResultTmp.clone();
			lsTmp.add(omimgene.getGenMimId() + "");
			lsTmp.add(omimgene.getPhenMimId() + "");
//			根据phenMimID在表中查找其详细的描述信息
			MgmtOMIMUnit mgmtOMIMUnit = MgmtOMIMUnit.getInstance();
			List<MIMInfo> lsMimInfo = mgmtOMIMUnit.findByMimId(omimgene.getPhenMimId());
			for (MIMInfo mIMInfo:lsMimInfo) {
				lsTmp.add(mIMInfo.getMimTitle());
				omimAllInfo = mIMInfo.getOthInfor().split("\\*FIELD\\*");
				for (String content:omimAllInfo) {
					if (content.trim().startsWith("RF")) {
						lsTmp.add(content.substring(3));
					}
				}
				lsResult.add(lsTmp.toArray(new String[0]));
			}
		}
		if (lsResult.size() == 0) {
//			fillLsResult(geneID.getAccID(), lsResult, 5);   如果基因ID从数据库中查询的获取的话，使用该语句
			fillLsResult(accID + "", lsResult, 5);	//此句做测试使用
		}
		return lsResult;
	}

	@Override
	protected List<String[]> getInfoBlast(int taxID, int subTaxID,
			double evalue, String accID) {
		// TODO Auto-generated method stub
		return null;
	}
}
