package com.novelbio.nbcgui.controlseq;

import java.util.ArrayList;
import java.util.List;

import com.novelbio.analysis.IntCmdSoft;
import com.novelbio.analysis.seq.genome.GffChrAbs;
import com.novelbio.analysis.seq.genome.gffOperate.GffHashGene;
import com.novelbio.analysis.seq.genome.gffOperate.GffType;
import com.novelbio.analysis.seq.mapping.MapLibrary;
import com.novelbio.analysis.seq.mapping.StrandSpecific;
import com.novelbio.analysis.seq.rnaseq.JunctionSaturationJava;
import com.novelbio.analysis.seq.rnaseq.RseQC.GeneBodyCoverage;
import com.novelbio.analysis.seq.rnaseq.RseQC.InnerDistance;
import com.novelbio.analysis.seq.rnaseq.RseQC.JunctionAnnotation;
import com.novelbio.analysis.seq.rnaseq.RseQC.RPKMSaturation;
import com.novelbio.analysis.seq.rnaseq.RseQC.ReadDuplication;
import com.novelbio.analysis.seq.sam.BamReadsInfo;
import com.novelbio.analysis.seq.sam.SamFile;
import com.novelbio.base.ExceptionNullParam;
import com.novelbio.base.FoldeCreate;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.database.model.species.Species;

public class CtrlRseQC implements IntCmdSoft {
	List<String[]> lsPrefix2File;
	String outPath;
	String bedFile;
	String exePath = "";
	boolean runGeneBody = true;
	
	/** 有GTF优先GTF */
	String gtfFile;
	Species species;
	GffHashGene gffHashGene;

	//InnerDistance
	/** 图片下界  */
	int innerDis_imageLowerBound = -250;
	/** 图片上界  */
	int innerDis_imageUpBound = 250;
	/** 绘图的步长  */
	int innerDis_imageStepLenght = 5;
	boolean runInnerDistance = true;

	//JunctionAnnotation and JunctionSaturation
	/**最小内含子长度*/
	int junc_intronLength = 50;
	boolean runJunctionAnno = true;

	//JunctionSaturation
	/**设定采样百分比的下界。默认是5，可输入0~100的整数*/
	protected int juncSat_sampleLowerBound = 5;
	/** 设定采样百分比的上界。默认是100，可输入0~100的整数*/
	protected int juncSat_sampleUpBound = 100;
	/**设定采样的步长。默认是5，可输入0~100的整数*/
	protected int juncSat_sampleStepLenght = 5;
	/**设定能够认为是junction的最少读长数目，默认是1*/
	int juncSat_leastReadNum = 1;
	boolean runJunctionSaturation = true;

	//ReadDuplication
	/** 读长重复次数的上限，仅用于绘图，默认是500*/
	protected int readDup_readsRepeatNum = 500;
	boolean runReadDuplication = true;

	//RPKMSaturation
	StrandSpecific rpkmSat_strandSpecific;
	MapLibrary rpkmSat_library;
	/** -c 指定RPKM的cutoff值。默认是0.01*/	
	double rpkmSat_cutoffValue = 0.01;
	boolean runRPKMSaturation = true;

	
	List<String> lsCmd = new ArrayList<>();
	
	/** 设定要跑哪几个模块，默认全跑 */
	public void setRunModule(boolean runGeneBody, boolean runInnerDistance, boolean runJunctionAnno, 
			boolean runJunctionSaturation, boolean runReadDuplication, boolean runRPKMSaturation) {
		this.runGeneBody = runGeneBody;
		this.runInnerDistance = runInnerDistance;
		this.runJunctionAnno = runJunctionAnno;
		this.runJunctionSaturation = runJunctionSaturation;
		this.runReadDuplication = runReadDuplication;
		this.runRPKMSaturation = runRPKMSaturation;
	}

	/** 输入文件，务必为bam文件 */
	public void setInFile(List<String[]> lsPrefix2File) {
		this.lsPrefix2File = lsPrefix2File;
	}
	/** 输出文件路径 */
	public void setOutFilePrefix(String outPath) {
		this.outPath = outPath;
	}

	/** 输入物种信息，与bed文件和gtf文件三选一  */
	public void setSpecies(Species species) {
		this.species = species;
	}
	///** 输入bed文件，与gtf文件和物种信息三选一  */
	//public void setBedFile(String bedFile) {
//		this.bedFile = bedFile;
	//}
	/** 输入gtf文件，与bed文件和物种信息三选一 */
	public void setGtfFile(String gtfFile) {
		this.gtfFile = gtfFile;
	}

	/** InnerDistance 参数，图片下限，默认-250 */
	public void setInnerDis_imageLowerBound(int innerDis_imageLowerBound) {
		this.innerDis_imageLowerBound = innerDis_imageLowerBound;
	}
	/** InnerDistance 参数，图片上限，默认250 */
	public void setInnerDis_imageUpBound(int innerDis_imageUpBound) {
		this.innerDis_imageUpBound = innerDis_imageUpBound;
	}
	/** InnerDistance 参数，图片步长，默认5 */
	public void setInnerDis_imageStepLenght(int innerDis_imageStepLenght) {
		this.innerDis_imageStepLenght = innerDis_imageStepLenght;
	}

	/** JunctionAnnotation和JunctionSaturation参数，最短内含子长度，默认50 */
	public void setJunc_intronLength(int junc_intronLength) {
		this.junc_intronLength = junc_intronLength;
	}
	/** JunctionSaturation 参数 设定能够认为是junction的最少读长数目，默认是1 */
	public void setJuncSat_leastReadNum(int juncSat_leastReadNum) {
		this.juncSat_leastReadNum = juncSat_leastReadNum;
	}
	/** JunctionSaturation 参数 设定采样百分比的上界。默认是100，可输入0~100的整数 */
	public void setJuncSat_sampleLowerBound(int juncSat_sampleLowerBound) {
		this.juncSat_sampleLowerBound = juncSat_sampleLowerBound;
	}
	/** JunctionSaturation 参数 设定采样的步长。默认是5，可输入0~100的整数 */
	public void setJuncSat_sampleStepLenght(int juncSat_sampleStepLenght) {
		this.juncSat_sampleStepLenght = juncSat_sampleStepLenght;
	}
	/** JunctionSaturation 参数 设定采样百分比的上界。默认是100，可输入0~100的整数*/
	public void setJuncSat_sampleUpBound(int juncSat_sampleUpBound) {
		this.juncSat_sampleUpBound = juncSat_sampleUpBound;
	}

	/** ReadDuplication 读长重复次数的上限，仅用于绘图，默认是500 */
	public void setReadDup_readsRepeatNum(int readDup_readsRepeatNum) {
		this.readDup_readsRepeatNum = readDup_readsRepeatNum;
	}
	/** RPKMSaturation 参数 -c 指定RPKM的cutoff值。默认是0.01*/	
	public void setRpkmSat_cutoffValue(double rpkmSat_cutoffValue) {
		this.rpkmSat_cutoffValue = rpkmSat_cutoffValue;
	}
	
	public void run() {
		lsCmd.clear();
		if (FileOperate.isFileExist(gtfFile)) {
			gffHashGene = new GffHashGene(GffType.GTF, gtfFile);
			String bedFile = FileOperate.getPathName(outPath) + FileOperate.getFileName(gtfFile);
			bedFile = FileOperate.changeFileSuffix(bedFile, "_toBed", "bed");
			gffHashGene.writeToBED(gtfFile);
			this.bedFile = bedFile;
		} else if (species != null) {
			GffChrAbs gffChrAbs = new GffChrAbs(species);
			this.bedFile = gffChrAbs.getBedFile();
			gffHashGene = gffChrAbs.getGffHashGene();
			gffChrAbs.close();
			gffChrAbs = null;
		} else {
			throw new ExceptionNullParam("no GTF file");
		}
		
		for (String[] prefix2File : lsPrefix2File) {
			String path = outPath + prefix2File[0];
			run(prefix2File[1], path);
		}
		
		gffHashGene = null;
	}
	
	private void run(String inFile, String outPath) {
		if (!inFile.trim().toLowerCase().endsWith("bam") || !FileOperate.isFileExistAndBigThanSize(inFile, 0)) {
			throw new ExceptionNullParam(inFile + "error, can only use bam file");
		}
		if (runRPKMSaturation) {
			if (gffHashGene == null) {
				throw new ExceptionNullParam("cannot run without a gtf file");
			}
			BamReadsInfo bamReadsInfo = new BamReadsInfo();
			bamReadsInfo.setGffHashGene(gffHashGene);
			bamReadsInfo.setSamFile(new SamFile(inFile));
			bamReadsInfo.calculate();
			rpkmSat_library = bamReadsInfo.getMapLibrary();
			rpkmSat_strandSpecific = bamReadsInfo.getStrandSpecific();
			lsCmd.add(FileOperate.getFileName(inFile) + " library: " + rpkmSat_library.toString() + 
					"\nstrand_specific: " + rpkmSat_strandSpecific.toStringShort());
		}
		if (runGeneBody) {
			GeneBodyCoverage geneBodyCoverage = new  GeneBodyCoverage(inFile, outPath, bedFile);
			lsCmd.addAll(geneBodyCoverage.getCmdExeStr());
			geneBodyCoverage.run();
		}
		
		if (runInnerDistance) {
			InnerDistance innerDistance = new InnerDistance(inFile, outPath, bedFile);
			innerDistance.setImageLowerBound(innerDis_imageLowerBound);
			innerDistance.setImageUpBound(innerDis_imageUpBound);
			innerDistance.setImageStepLength(innerDis_imageStepLenght);
			lsCmd.addAll(innerDistance.getCmdExeStr());
			innerDistance.run();
		}
		
		if (runJunctionAnno) {
			JunctionAnnotation junctionAnnotation = new JunctionAnnotation(inFile, outPath, bedFile);
			junctionAnnotation.setIntronLength(junc_intronLength);
			lsCmd.addAll(junctionAnnotation.getCmdExeStr());
			junctionAnnotation.run();
		}
		
		if (runJunctionSaturation) {
			JunctionSaturationJava junctionSaturation = new JunctionSaturationJava();
			junctionSaturation.setGffHashGene(gffHashGene);
			junctionSaturation.setNodeNum(20);
			junctionSaturation.setSavePath(outPath + "JunctionSaturation.png");
			junctionSaturation.setIntronLen(junc_intronLength, 0);
			junctionSaturation.setLeastReadNum(juncSat_leastReadNum);
			junctionSaturation.setNodeNum(20);
			junctionSaturation.plot();
		}
		
		if (runReadDuplication) {
			ReadDuplication readDuplication =  new ReadDuplication(inFile, outPath, null);
			lsCmd.addAll(readDuplication.getCmdExeStr());
			readDuplication.run();
		}
		
		if (runRPKMSaturation) {
			RPKMSaturation rPKMsaSaturation = new RPKMSaturation(inFile, outPath, bedFile);
			rPKMsaSaturation.setLeastReadNum(juncSat_leastReadNum);
			rPKMsaSaturation.setMapLibrary(rpkmSat_library);
			rPKMsaSaturation.setStrandSpecific(rpkmSat_strandSpecific);
			rPKMsaSaturation.setSampleLowerBound(juncSat_sampleLowerBound);
			rPKMsaSaturation.setSampleUpBound(juncSat_sampleUpBound);
			rPKMsaSaturation.setSampleStepLength(juncSat_sampleStepLenght);
			lsCmd.addAll(rPKMsaSaturation.getCmdExeStr());
			rPKMsaSaturation.run();
		}
	}

	@Override
	public List<String> getCmdExeStr() {
		return lsCmd;
	}
}
