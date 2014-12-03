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
	MgmtGeneMIMInfo mgmtGeneMIMInfo = MgmtGeneMIMInfo.getInstance();
	MgmtOMIM mgmtOMIM = MgmtOMIM.getInstance();
	MgmtOMIMUnit mgmtOMIMUnit = MgmtOMIMUnit.getInstance();
	
	@Override
	public List<String[]> getInfo(int taxID, String accID) {
//		GeneID geneId = new GeneID(accID, taxID);
//		int geneIdint = Integer.parseInt(geneId.getGeneUniID());
		int geneIdint = Integer.parseInt(accID);
		List<String[]> lsResult = new ArrayList<String[]>();
		ArrayList<String> lsResultTmp = new ArrayList<String>();
		lsResultTmp.add(accID + "");
			
//		根据geneID在omimGenemap表中，查找对应的geneMimID
		GeneMIM geneMim = mgmtGeneMIMInfo.findOmimInfByGeneId(geneIdint);
		if (geneMim == null) {
			fillLsResult(accID + "", lsResult, 5);	
			return lsResult;
		}
		int geneOmimId = geneMim.getGeneMimId();	
//		根据上一步得到的geneMimID,在表omimGenemap中查找对应的phenMimID
		List<OmimGeneMap> LsGene2OmimId = mgmtOMIM.findByGenMimId(geneOmimId);
		
		for (OmimGeneMap omimgene : LsGene2OmimId) {
			ArrayList<String> lsTmp = (ArrayList<String>) lsResultTmp.clone();
			lsTmp.add(omimgene.getGenMimId() + "");
			lsTmp.add(omimgene.getPhenMimId() + "");
//			根据phenMimID在表中查找其详细的描述信息
			MIMInfo mimInfo = mgmtOMIMUnit.findByMimId(omimgene.getPhenMimId());
//			lsTmp.add(mimInfo.getMimTitle());
			lsTmp.add(mimInfo.getDesc());
			List<String> listRef = mimInfo.getListRef();
			lsTmp.add(listRef.get(0));
			lsResult.add(lsTmp.toArray(new String[0]));
		}
		
		if (lsResult.size() == 0) {
//			fillLsResult(geneID.getAccID(), lsResult, 5);   如果基因ID从数据库中查询的获取的话，使用该语句
			fillLsResult(accID + "", lsResult, 5);	//此句做测试使用
		}
		return lsResult;
	}
	private void fillLsResult(String accID, List<String[]> lsResult, int arrayLength) {
		String[] tmpResult = new String[arrayLength];
		tmpResult[0] = accID;
		for (int i = 1; i < tmpResult.length; i++) {                                  
			tmpResult[i] = "";
		}
		lsResult.add(tmpResult);
	}
	@Override
	protected List<String[]> getInfoBlast(int taxID, int subTaxID,
			double evalue, String accID) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String[] getTitle() {
		List<String> lsTitle = new ArrayList<String>();
		lsTitle.add("Symbol/AccID");
		lsTitle.add("GeneMIMID");
		lsTitle.add("PhenMIMID");
		lsTitle.add("OMIMDesc");
		lsTitle.add("Refence");
		return lsTitle.toArray(new String[0]);
	}
}
