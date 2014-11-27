package com.novelbio.analysis.annotation.genAnno;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.novelbio.database.domain.geneanno.AGene2Go;
import com.novelbio.database.domain.geneanno.GOtype;
import com.novelbio.database.model.modgeneid.GeneID;

public class AnnoGO extends AnnoAbs {
	GOtype gOtype;
	
	public void setgOtype(GOtype gOtype) {
		this.gOtype = gOtype;
	}
	
	public String[] getTitle() {
		List<String> lsTitle = new ArrayList<String>();
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
	
	protected List<String[]> getInfo(int taxID, String accID) {
		List<String[]> lsResult = new ArrayList<String[]>();
		GeneID geneID = new GeneID(accID, taxID);
		ArrayList<String> lsResultTmp = new ArrayList<String>();
//		lsResultTmp.add(geneID.getAccID());
		lsResultTmp.add(geneID.getSymbol());
		List<AGene2Go> lsGene2Gos = geneID.getGene2GO(gOtype);
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
	
	protected List<String[]> getInfoBlast(int taxID, int subTaxID, double evalue, String accID) {
		List<String[]> lsResult = new ArrayList<String[]>();
		GeneID geneID = new GeneID(accID, taxID);
		ArrayList<String> lsResultTmp = new ArrayList<String>();
//		lsResultTmp.add(geneID.getAccID());
		lsResultTmp.add(geneID.getSymbol());
		
		List<AGene2Go> lsGene2Gos = geneID.getGene2GO(gOtype);
		geneID.setBlastInfo(evalue, subTaxID);
		List<AGene2Go> lsGene2GoBlast = new ArrayList<AGene2Go>();
		if (geneID.getGeneIDBlast() != null) {
			lsGene2GoBlast = geneID.getGeneIDBlast().getGene2GO(gOtype);
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
	
}
