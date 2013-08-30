package com.novelbio.nbcgui.controlseq;

import java.util.ArrayList;

import com.novelbio.analysis.seq.AlignSeq;
import com.novelbio.analysis.seq.FormatSeq;
import com.novelbio.analysis.seq.bed.BedSeq;
import com.novelbio.analysis.seq.rnaseq.Align2DGEvalue;
import com.novelbio.analysis.seq.sam.SamFile;
import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.database.model.species.Species;

public class CtrlDGEgetvalue {
	Align2DGEvalue align2dgEvalue = new Align2DGEvalue();
	
	public void setSpecies(Species species) {
		align2dgEvalue.setSpecies(species);
	}
	/** 设定sam，bam，或者bed文件
	 * 这些文件必须是排过序的
	 * */
	public void setLsAlignSeq(ArrayList<String[]> lsFile2Prefix, FormatSeq formatSeq, String outFile) {
		ArrayList<AlignSeq> lsAlignSeqs = new ArrayList<AlignSeq>();
		ArrayList<String> lsTitles = new ArrayList<String>();
		for (String[] strings : lsFile2Prefix) {
			if (formatSeq == FormatSeq.BED) {
				lsAlignSeqs.add(new BedSeq(strings[0]));
			}
			else if (formatSeq == FormatSeq.SAM || formatSeq == formatSeq.BAM) {
				lsAlignSeqs.add(new SamFile(strings[0]));
			}
			lsTitles.add(strings[1]);
		}
		if (FileOperate.isFileDirectory(outFile)) {
			outFile = FileOperate.addSep(outFile) + "result.xls";
		}
		align2dgEvalue.setLsAlignSeq(lsAlignSeqs, lsTitles, outFile);
	}
	/** 将输入的文件排序 */
	public void sort() {
		align2dgEvalue.sort();
	}
	/**
	 * 一个基因会有多个位点有reads覆盖，就是说DGE的试验会造成在一个基因的多个位置有reads富集
	 * 是选择全部reads还是选择最高点的reads
	 * @param allTags
	 */
	public void setAllTags(boolean allTags) {
		align2dgEvalue.setAllTags(allTags);
	}
	/**
	 * 无法设定compressType
	 * 将bed文件转化成DGE所需的信息，直接可以用DEseq分析的
	 * 返回获得的结果文件名
	 */
	public String dgeCal() {
		return align2dgEvalue.dgeCal();
	}

}
