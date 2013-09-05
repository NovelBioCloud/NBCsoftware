package com.novelbio.nbcReport;

import java.io.Serializable;

public enum EnumTableType implements Serializable{

	GO_Result(new XdocTable( "100,100,60,60,60,60,60,60,80,80"  )),
	GO_Gene2GO( new XdocTable( "120,120,150,80,80,50,50")), 
	GO_GO2Gene(new XdocTable("80,80,120,120,150,50,50")), 
	
	Pathway_Result(new XdocTable("100,100,60,60,60,60,60,60,80,80", "c,l,c,c,c,c,c,c,c,c")), 
	Pathway_Gene2Path(new XdocTable("80,80,120,120,150,50,50")), 

	DifGene(new XdocTable("120,80,220,40,40,60,60,50",20)),
	
	MappingResult(new XdocTable("200,200",10)),
	MappingChrFile(new XdocTable("100,150,150,150,150",25)),
	MappingStatistics(new XdocTable("200,200,200",25)),
	
	QC_BasicStatAll(new XdocTable("100,150,150,150,40",20)),
	GeneExp(new XdocTable("100,100,100,100",20));
	
	private XdocTable xdocTable;

	EnumTableType(XdocTable xdocTable) {
		this.xdocTable = xdocTable;
	}
	
	/**
	 * 取得表格对象
	 * @return
	 */
	public XdocTable getXdocTable() {
		return xdocTable.getClone();
	}
}
