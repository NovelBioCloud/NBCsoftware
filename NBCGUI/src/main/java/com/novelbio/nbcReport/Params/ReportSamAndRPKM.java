package com.novelbio.nbcReport.Params;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.novelbio.nbcReport.XdocTmpltExcel;
import com.novelbio.nbcReport.XdocTmpltPic;

public class ReportSamAndRPKM extends ReportBase{
	Set<String> setResultFile;
	List<XdocTmpltExcel> lsExcels;
	List<XdocTmpltPic> lsTmpltPics;
	
	
	public void setSetResultFile(Set<String> setResultFile) {
		this.setResultFile = setResultFile;
	}

	public Set<String> getSetResultFile() {
		Set<String> lsSet = new LinkedHashSet<>();
		for (String string : setResultFile) {
			lsSet.add(EnumReport.SamStatistics + string.split(EnumReport.SamStatistics.getResultFolder())[1]);
		}
		return lsSet;
	}


	public List<String> getLsExcels() {
		List<String> lsList = new ArrayList<>();
		for (XdocTmpltExcel xdocExcel : lsExcels) {
			lsList.add(xdocExcel.toString());
		}
		return lsList;
	}


	public void setLsExcels(List<XdocTmpltExcel> lsExcels) {
		this.lsExcels = lsExcels;
	}



	public List<String> getLsTmpltPics() {
		List<String> lsList = new ArrayList<>();
		for (XdocTmpltPic xdocPic : lsTmpltPics) {
			lsList.add(xdocPic.toString());
		}
		return lsList;
	}


	public void setLsTmpltPics(List<XdocTmpltPic> lsTmpltPics) {
		this.lsTmpltPics = lsTmpltPics;
	}

	@Override
	public EnumReport getEnumReport() {
		return EnumReport.SamStatistics;
	}
	
}
