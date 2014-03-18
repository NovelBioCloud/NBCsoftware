package com.novelbio.database.service.servgeneanno;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.novelbio.database.domain.geneanno.SpeciesFile;
import com.novelbio.database.domain.geneanno.TaxInfo;

public interface IManageSpecies {
	
	/** 返回所有有基因组的物种 */
	public List<Integer> getLsTaxID();
	
	/**
	 * @param taxID 必须选项，没这个就不用选了
	 * @param version 必须选，主要是hg19等等类似，不过我估计也用不到 <b> Version大小写敏感</b>
	 * @return 没有的话则返回size==0的list
	 */
	public SpeciesFile querySpeciesFile(int taxID, String version);
	
	/**
	 * @param taxID 必须选项，没这个就不用选了
	 * @param version 可选，主要是hg19等等类似，不过我估计也用不到
	 * @return 没有的话则返回size==0的list
	 */
	public List<SpeciesFile> queryLsSpeciesFile(int taxID);
	
	/**
	 * Version大小写敏感
	 * 没有就插入，有就覆盖
	 * @param taxInfo
	 */
	public void saveSpeciesFile(SpeciesFile speciesFile);

	/**
	 * @param taxID 0 则返回null
	 * @return
	 */
	public TaxInfo queryTaxInfo(int taxID);
	/**
	 * @param taxIDfile 0 则返回null
	 * @return
	 */
	public TaxInfo queryAbbr(String abbr);
	/**
	 * 没有就插入，有就升级
	 * @param taxInfo
	 */
	public void saveTaxInfo(TaxInfo taxInfo);

	/**
	 * 返回taxID对常用名
	 * @return
	 */
	public Map<Integer,String> getMapTaxIDName();
	/**
	 * 返回taxID对常用名
	 * @return
	 */
	public List<TaxInfo> getLsAllTaxID();

	public Page<TaxInfo> queryLsTaxInfo(Pageable pageable);
	
	public void deleteByTaxId(int taxid);
	
	public void deleteSpeciesFile(String speciesFileId);
	
	public void readSpeciesFile(String speciesFileInput);
	
	public SpeciesFile findOne(String speciesFileId);

}
