package com.novelbio.nbcgui.controlquery;

import java.util.List;

import com.novelbio.analysis.annotation.genanno.AnnoQuery;
import com.novelbio.analysis.annotation.genanno.AnnoQuery.AnnoQueryDisplayInfo;
import com.novelbio.analysis.seq.genome.GffChrAnno;
import com.novelbio.base.multithread.RunGetInfo;
import com.novelbio.base.multithread.RunProcess;
import com.novelbio.database.domain.species.Species;
import com.novelbio.nbcgui.GUI.GuiAnnoPeak;

public class CtrlBatchAnnoPeak implements RunGetInfo<AnnoQuery.AnnoQueryDisplayInfo> {
	GuiAnnoPeak guiAnnoPeak;
	GffChrAnno gffChrAnno = new GffChrAnno();
	Species species;
	int[] filterTss;
	int[] filterTes;
	boolean filterGeneBody = true;
	boolean filterExon = false;
	boolean filterIntron = false;
	boolean filter5UTR = false;
	boolean filter3UTR = false;
	
	public CtrlBatchAnnoPeak() {}
	
	public CtrlBatchAnnoPeak(GuiAnnoPeak guiAnnoPeak) {
		this.guiAnnoPeak = guiAnnoPeak;
		gffChrAnno.setRunGetInfo(this);
	}
	public void setSpecies(Species species) {
		this.species = species;
		gffChrAnno.setSpecies(species);
	}
	public void setSpecies(int taxID) {
		this.species = new Species(taxID);
		gffChrAnno.setSpecies(taxID);
	}
	
	@Deprecated
	public void setListQuery(List<String[]> lsGeneInfo) {
		if (guiAnnoPeak != null) {
			guiAnnoPeak.getProcessBar().setMinimum(0);
			guiAnnoPeak.getProcessBar().setMaximum(lsGeneInfo.size() - 1);
		}
		gffChrAnno.setLsGeneInfo(lsGeneInfo);
	}
	public void setColChrID(int colChrID) {
		gffChrAnno.setColChrID(colChrID);
	}
	public void setColPeakSummit(int colSummit) {
		gffChrAnno.setColSummit(colSummit);
	}
	public void setColPeakStartEnd(int start, int end) {
		gffChrAnno.setColStartEnd(start, end);
	}
	public void setIsSummitSearch(boolean summitSearch) {
		gffChrAnno.setSearchSummit(summitSearch);
	}
	public void setTssRange(int[] tss) {
		filterTss = tss;
	}
	public void setTesRange(int[] tes) {
		filterTes = tes;
	}
	public void setFilterGeneBody(boolean filterGeneBody) {
		this.filterGeneBody = filterGeneBody;
	}
	
	@Deprecated
	public void execute() {
		gffChrAnno.setTss(filterTss);
		gffChrAnno.setTes(filterTes);
		gffChrAnno.setFilterGeneBody(filterGeneBody, filterExon, filterIntron);
		gffChrAnno.setFilterUTR(filter5UTR, filter3UTR);
		gffChrAnno.run();
	}
	
	public void execute(String infile, String outFile) {
		gffChrAnno.setTss(filterTss);
		gffChrAnno.setTes(filterTes);
		gffChrAnno.setFilterGeneBody(filterGeneBody, filterExon, filterIntron);
		gffChrAnno.setFilterUTR(filter5UTR, filter3UTR);
		gffChrAnno.annoFileTxt(infile, outFile);
	}
	
	@Deprecated
	/** 包含title */
	public List<String[]> getResult() {
		return gffChrAnno.getLsResult();
	}
	
	@Deprecated
	public String[] getTitle() {
		return gffChrAnno.getTitleGeneInfoFilterAnno();
	}
	
	
	@Override
	public void setRunningInfo(AnnoQueryDisplayInfo info) {
		if (guiAnnoPeak != null) {
			guiAnnoPeak.getProcessBar().setValue((int) info.getCountNum());
			guiAnnoPeak.getJScrollPaneDataResult().addItem(info.getTmpInfo());
		}
	}
	
	@Override
	public void done(RunProcess runProcess) {
		if (guiAnnoPeak != null) {
			guiAnnoPeak.getProcessBar().setValue(guiAnnoPeak.getProcessBar().getMaximum());
			guiAnnoPeak.getBtnSave().setEnabled(true);
			guiAnnoPeak.getBtnRun().setEnabled(true);
		}
	}
	@Override
	public void threadSuspended(RunProcess runProcess) {
		if (guiAnnoPeak != null) {
			guiAnnoPeak.getBtnRun().setEnabled(true);
		}
	}
	@Override
	public void threadResumed(RunProcess runProcess) {
		if (guiAnnoPeak != null) {
			guiAnnoPeak.getBtnRun().setEnabled(false);
		}
	}
	@Override
	public void threadStop(RunProcess runProcess) {
		if (guiAnnoPeak != null) {
			guiAnnoPeak.getBtnRun().setEnabled(true);
		}
	}
	
}
