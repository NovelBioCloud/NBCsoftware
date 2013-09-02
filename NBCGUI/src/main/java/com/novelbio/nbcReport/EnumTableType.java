package com.novelbio.nbcReport;

import com.novelbio.nbcReport.Params.EnumReport;



public enum EnumTableType {

	GO_Result("GO-Analysis_DownGO_Result", new XdocTable( "100,100,60,60,60,60,60,60,80,80"  )),
	GO_Gene2GO("GO-Analysis_DownGene2GO", new XdocTable( "120,120,150,80,80,50,50")), 
	GO_GO2Gene("GO-Analysis_DownGO2Gene", new XdocTable("80,80,120,120,150,50,50")), 
	
//	GO_DownGO("GO-Analysis_DownGO_Result", new XdocTable( "100,100,60,60,60,60,60,60,80,80"  )), 
//	GO_DownGene2GO("GO-Analysis_DownGene2GO", new XdocTable( "120,120,150,80,80,50,50")), 
//	GO_DownGO2Gene("GO-Analysis_DownGO2Gene", new XdocTable("80,80,120,120,150,50,50")), 
//	GO_UpGO("GO-Analysis_UpGO_Result", new XdocTable( "100,100,60,60,60,60,60,60,80,80")), 
//	GO_UpGene2GO("GO-Analysis_UpGene2GO", new XdocTable("120,120,150,80,80,50,50")), 
//	GO_UpGO2Gene("GO-Analysis_UpGO2Gene", new XdocTable("80,80,120,120,150,50,50")), 
//	GO_AllGO("GO-Analysis_AllGO_Result", new XdocTable( "100,100,60,60,60,60,60,60,80,80" )), 
//	GO_AllGene2GO("GO-Analysis_AllGene2GO", new XdocTable( "120,120,150,80,80,50,50")), 
//	GO_AllGO2Gene("GO-Analysis_AllGO2Gene", new XdocTable( "80,80,120,120,150,50,50")), 
	Pathway_Result("Pathway-Analysis_DownPathway_Result", new XdocTable("100,100,60,60,60,60,60,60,80,80", "c,l,c,c,c,c,c,c,c,c")), 
	Pathway_Gene2Path("Pathway-Analysis_DownPathway2Gene", new XdocTable("80,80,120,120,150,50,50")), 
//	Pathway_Up("Pathway-Analysis_UpPathway_Result", new XdocTable("100,100,60,60,60,60,60,60,80,80")), 
//	Pathway_Up2Gene("Pathway-Analysis_UpPathway2Gene", new XdocTable("80,80,120,120,150,50,50")), 
//	Pathway_All("Pathway-Analysis_AllPathway_Result", new XdocTable("100,100,60,60,60,60,60,60,80,80")), 
//	Pathways_All2Gene("Pathway-Analysis_AllPathway2Gene", new XdocTable("80,80,120,120,150,50,50")), 
	DifGene("Dif-Gene", new XdocTable("120,80,220,40,40,60,60,50" )),
	MappingResult("MappingResult",new XdocTable("200,200" , "c,c","15,15,15,15,15,15,15,15,15")),
	MappingChrFile("MappingChrFile",new XdocTable("100,150,150,150,150","c,c,c,c,c","15,15,15,15,15,15,15,15,15,15,15,15")),
	MappingStatistics("MappingStatistics",new XdocTable("200,200,200","c,c,c","15,15,15,15,15,15,15,15"));
	
	private String tempName;
	private XdocTable xdocTable;

	EnumTableType(String tempName, XdocTable xdocTable) {
		this.tempName = tempName;
		this.xdocTable = xdocTable;
	}
	
	/**
	 * 取得表格模板名
	 * @return
	 */
	public String getTempName() {
		return tempName + ".xdoc";
	}
	
	/**
	 * 得到xdoc模板路径
	 * 
	 * @return
	 */
	public String getTempPath() {
		String path = EnumReport.class.getClassLoader().getResource("xdocTemplate").getFile();
		return path;
	}
	
	
	/**
	 * 取得表格对象
	 * @return
	 */
	public XdocTable getXdocTable() {
		return xdocTable.getClone();
	}
}
