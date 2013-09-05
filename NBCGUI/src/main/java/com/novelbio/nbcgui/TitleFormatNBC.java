package com.novelbio.nbcgui;

import java.util.HashMap;

public enum TitleFormatNBC {
	/** 公司名缩写 */
	CompanyNameAbbr("NovelBio"),
	
	QueryID("QueryID"), AccID("AccID"), Symbol("Symbol"), Pvalue("P-Value"), Log2Pnegative("(-log2P)"),
	GeneName("GeneName"), GeneType("GeneType"),
	
	FDR("FDR"), FoldChange("FoldChange"), Log2FC("Log2FC"),
	Log10FC("Log10FC"), Evalue("E-Value"),
	
	Enrichment("Enrichment"),
	
	ChrID("ChrID"), LocStart("LocStart"), LocEnd("LocEnd"),
	Location("Location"), Description("Description"), 
	
	miRNAName("miRNAName"), miRNApreName("miRNApreName"),
	mirSequence("mirSequence"), mirPreSequence("mirPreSequence"),
	
	RfamID("RfamID"), RfamType("RfamType"), RfamAnnotaion("RfamAnnotaion"), RfamDescription("RfamDescription"), RfamClass("RfamClass"),
	NCRNAID("NCRNAID"),
	
	GeneStructure("GeneStructure"),
	
	RepeatName("RepeatName"), RepeatFamily("RepeatFamily"),
	
	Score("Score"), 
	GOID("GOID"), GOTerm("GOTerm"),
	PathwayID("PathwayID"), PathwayTerm("PathwayTerm");
	
	String item;
	TitleFormatNBC(String item) {
		this.item = item;
	}
	
	@Override
	public String toString() {
		return item;
	}
	
//	public HashMap<String, String>
	
}
