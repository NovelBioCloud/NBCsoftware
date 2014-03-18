package com.novelbio.database.domain.geneanno;

import com.novelbio.base.StringOperate;
import com.novelbio.base.fileOperate.FileOperate;
/**
 * 物种下所有文件的类型
 * @author novelbio
 *
 */
public enum EnumSpeciesFile {
	chromSeqFile("ChromFa"),
	gffRepeatFile("gff"),
	refseqNCfile("refrna"),
	gffGeneFile("gff"),
	refseqFileAllIso("refrna"),
	refseqFileOneIso("refrna"),
	
	ChromSepPath("Chrom_Sep") {
		public String getSavePath(SpeciesFile speciesFile) {
			String basePath = SpeciesFile.getPathParent();
			String pathToVersion = speciesFile.getPathToVersion();
			if(StringOperate.isRealNull(pathToVersion))
				return null;
			return basePath + folder + FileOperate.getSepPath() + pathToVersion;
		}
	},
	
	
	;
	/**
	 * 对应保存的文件夹
	 */
	protected String folder;
	
	EnumSpeciesFile(String folder) {
		this.folder = folder;
	}
	
	/**
	 * 获得保存物种文件的路径
	 * @param speciesFile
	 * @return
	 */
	public String getSavePath(SpeciesFile speciesFile) {
		String basePath = speciesFile.speciesVersionPath();
		if(StringOperate.isRealNull(basePath))
			return null;
		return basePath + folder + FileOperate.getSepPath();
	}
	
}
