	\subsubsection{Mapping Statistics:}
	Mapping statistics was showed in Table 2, from which we could mention the mapping rates about ${mappingRate}\% indicating the well-performance of the sequencing experiment. Furthermore, more than ${uniqueMappingRate}\% unique mapping rate and more than ${junctionReadsRate}\% of the junction reads could lead to the best quality of the gene expression and alternative splicing analysis.

<#if lsSampleInfo??>
<#assign i=lsSampleInfo?size/5>
<#assign i=i?floor>
\begin{table}[h]
  \centering
  \caption{Mapping Statistics}
  <#list 0..i as t>
  \begin{tabular}{llllll}
    \hline
    sampleName & ${(lsSampleInfo[t*(4+1)].sampleName)!""} & ${(lsSampleInfo[t*(4+1)+1].sampleName)!""} & ${(lsSampleInfo[t*(4+1)+2].sampleName)!""} & ${(lsSampleInfo[t*(4+1)+3].sampleName)!""} & ${(lsSampleInfo[t*(4+1)+4].sampleName)!""} \\\hline
    TotalReads & ${(lsSampleInfo[t*(4+1)].TotalReads)!""} & ${(lsSampleInfo[t*(4+1)+1].TotalReads)!""} & ${(lsSampleInfo[t*(4+1)+2].TotalReads)!""} & ${(lsSampleInfo[t*(4+1)+3].TotalReads)!""} & ${(lsSampleInfo[t*(4+1)+4].TotalReads)!""} \\
    TotalMappedReads & ${(lsSampleInfo[t*(4+1)].TotalMappedReads)!""} & ${(lsSampleInfo[t*(4+1)+1].TotalMappedReads)!""} & ${(lsSampleInfo[t*(4+1)+2].TotalMappedReads)!""} & ${(lsSampleInfo[t*(4+1)+3].TotalMappedReads)!""} & ${(lsSampleInfo[t*(4+1)+4].TotalMappedReads)!""} \\
    MappingRate & ${(lsSampleInfo[t*(4+1)].MappingRate)!""} & ${(lsSampleInfo[t*(4+1)+1].MappingRate)!""} & ${(lsSampleInfo[t*(4+1)+2].MappingRate)!""} & ${(lsSampleInfo[t*(4+1)+3].MappingRate)!""} & ${(lsSampleInfo[t*(4+1)+4].MappingRate)!""} \\
    UniqueMapping & ${(lsSampleInfo[t*(4+1)].UniqueMapping)!""} & ${(lsSampleInfo[t*(4+1)+1].UniqueMapping)!""} & ${(lsSampleInfo[t*(4+1)+2].UniqueMapping)!""} & ${(lsSampleInfo[t*(4+1)+3].UniqueMapping)!""} & ${(lsSampleInfo[t*(4+1)+4].UniqueMapping)!""} \\
    UniqueMappingRate & ${(lsSampleInfo[t*(4+1)].UniqueMappingRate)!""} & ${(lsSampleInfo[t*(4+1)+1].UniqueMappingRate)!""} & ${(lsSampleInfo[t*(4+1)+2].UniqueMappingRate)!""} & ${(lsSampleInfo[t*(4+1)+3].UniqueMappingRate)!""} & ${(lsSampleInfo[t*(4+1)+4].UniqueMappingRate)!""} \\
    junctionAllMappedReads & ${(lsSampleInfo[t*(4+1)].junctionAllMappedReads)!""} & ${(lsSampleInfo[t*(4+1)+1].junctionAllMappedReads)!""} & ${(lsSampleInfo[t*(4+1)+2].junctionAllMappedReads)!""} & ${(lsSampleInfo[t*(4+1)+3].junctionAllMappedReads)!""} & ${(lsSampleInfo[t*(4+1)+4].junctionAllMappedReads)!""} \\
    junctionUniqueMapping & ${(lsSampleInfo[t*(4+1)].junctionUniqueMapping)!""} & ${(lsSampleInfo[t*(4+1)+1].junctionUniqueMapping)!""} & ${(lsSampleInfo[t*(4+1)+2].junctionUniqueMapping)!""} & ${(lsSampleInfo[t*(4+1)+3].junctionUniqueMapping)!""} & ${(lsSampleInfo[t*(4+1)+4].junctionUniqueMapping)!""} \\
    repeatMapping & ${(lsSampleInfo[t*(4+1)].repeatMapping)!""} & ${(lsSampleInfo[t*(4+1)+1].repeatMapping)!""} & ${(lsSampleInfo[t*(4+1)+2].repeatMapping)!""} & ${(lsSampleInfo[t*(4+1)+3].repeatMapping)!""} & ${(lsSampleInfo[t*(4+1)+4].repeatMapping)!""} \\
    UnMapped & ${(lsSampleInfo[t*(4+1)].UnMapped)!""} & ${(lsSampleInfo[t*(4+1)+1].UnMapped)!""} & ${(lsSampleInfo[t*(4+1)+2].UnMapped)!""} & ${(lsSampleInfo[t*(4+1)+3].UnMapped)!""} & ${(lsSampleInfo[t*(4+1)+4].UnMapped)!""} \\
    \hline
  \end{tabular}
  </#list>
\end{table}
</#if>

	Chromosome Distribution Analysis for analyzing the sequencing quality and Gene Structure Analysis for analyzing the RNA capture quality was introduced and could be achieved in folder "3. Chromosome Distribution Analysis" and "4. Gene Structure Analysis".