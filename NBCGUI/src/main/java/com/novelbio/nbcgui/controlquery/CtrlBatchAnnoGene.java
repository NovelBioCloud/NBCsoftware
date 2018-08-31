package com.novelbio.nbcgui.controlquery;

import java.util.ArrayList;
import java.util.List;

import com.novelbio.base.multithread.RunGetInfo;
import com.novelbio.base.multithread.RunProcess;
import com.novelbio.bioinfo.annotation.genanno.AnnoQuery;
import com.novelbio.bioinfo.annotation.genanno.AnnoQuery.AnnoQueryDisplayInfo;
import com.novelbio.bioinfo.gffchr.GffChrAbs;
import com.novelbio.database.domain.species.Species;
import com.novelbio.database.model.geneanno.GOtype;
import com.novelbio.nbcgui.GUI.GuiAnnoGene;

public class CtrlBatchAnnoGene implements RunGetInfo<AnnoQuery.AnnoQueryDisplayInfo> {
		
	/** 如果选定的是GO，则需要返回GO的信息 */
	GOtype gOtype;
	
	GuiAnnoGene guiAnnoBatch;	
	List<String[]> lsIn2Out;
	AnnoQuery annoQuery = new AnnoQuery();
	Species species;	
	
	public CtrlBatchAnnoGene(GuiAnnoGene guiBatchAnno) {
		this.guiAnnoBatch = guiBatchAnno;
		annoQuery.setRunGetInfo(this);
	}
	public void setAnnotationType(int annotationType) {
		annoQuery.setAnnoType(annotationType);
	}
	/** 当注释annotation时，如果需要同时注释基因的坐标信息，就设定这个 */
	public void setGffChrAbs(GffChrAbs gffChrAbs) {
		annoQuery.setGffChrAbs(gffChrAbs);
	}
	/** GOtype */
	public void setGOtype(GOtype gOtype) {
		annoQuery.setGOtype(gOtype);
	}
	public void setSpecies(Species species) {
		this.species = species;
		annoQuery.setTaxIDthis(species.getTaxID());
	}
	public void setSpecies(int taxID) {
		this.species = new Species(taxID);
		annoQuery.setTaxIDthis(taxID);
	}
	public void setListQuery(List<String[]> lsIn2Out) {
		guiAnnoBatch.getProcessBar().setMinimum(0);
		guiAnnoBatch.getProcessBar().setMaximum(1000);
		this.lsIn2Out = lsIn2Out;
	}
	public void setColumnAccIDFrom1(int colAccID) {
		annoQuery.setColAccIDFrom1(colAccID);
	}
	public void setBlastTo(boolean blast, int subjectID) {
		annoQuery.setBlast(blast);
		annoQuery.setTaxIDblastTo(subjectID);
	}

	public void execute() {
		for (String[] fileIn2Out : lsIn2Out) {
			annoQuery.setFirstLineFrom1(2);
			annoQuery.setGeneIDFile(fileIn2Out[0]);
			annoQuery.setSavePath(fileIn2Out[1]);
			annoQuery.run();
		}
	}
	public ArrayList<String[]> getResult() {
		return annoQuery.getLsResult();
	}

	@Override
	public void setRunningInfo(AnnoQueryDisplayInfo info) {
		guiAnnoBatch.getProcessBar().setValue((int) info.getCountNum());
	}
	
	@Override
	public void done(RunProcess runProcess) {
		guiAnnoBatch.getProcessBar().setValue(guiAnnoBatch.getProcessBar().getMaximum());
		guiAnnoBatch.getBtnSave().setEnabled(true);
		guiAnnoBatch.getBtnRun().setEnabled(true);
	}
	@Override
	public void threadSuspended(RunProcess runProcess) {
		guiAnnoBatch.getBtnRun().setEnabled(true);
	}
	@Override
	public void threadResumed(RunProcess runProcess) {
		guiAnnoBatch.getBtnRun().setEnabled(false);
	}
	@Override
	public void threadStop(RunProcess runProcess) {
		guiAnnoBatch.getBtnRun().setEnabled(true);
	}

}
