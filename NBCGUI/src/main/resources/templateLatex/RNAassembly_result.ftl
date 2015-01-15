	\subsubsection{Trinity Assembly: }
	Based on the clean reads after filtering, Novelbio committed the transcripts assembly on each sample utilizing Trinity Assembly Software. The result of the first step assembly was showed as following table2:

<#if lsSampleInfo??>
<#assign i=lsSampleInfo?size/5>
<#assign i=i?floor>
\begin{table}[h]
  \centering
  \caption{Mapping Statistics}
  <#list 0..i as t>
  \begin{tabular}{llllll}
    \hline
    Sample Name & ${(lsSampleInfo[t*(4+1)].sampleName)!""} & ${(lsSampleInfo[t*(4+1)+1].sampleName)!""} & ${(lsSampleInfo[t*(4+1)+2].sampleName)!""} & ${(lsSampleInfo[t*(4+1)+3].sampleName)!""} & ${(lsSampleInfo[t*(4+1)+4].sampleName)!""} \\\hline
    Raw Reads & ${(lsSampleInfo[t*(4+1)].RawReads)!""} & ${(lsSampleInfo[t*(4+1)+1].RawReads)!""} & ${(lsSampleInfo[t*(4+1)+2].RawReads)!""} & ${(lsSampleInfo[t*(4+1)+3].RawReads)!""} & ${(lsSampleInfo[t*(4+1)+4].RawReads)!""} \\
    Clean Reads & ${(lsSampleInfo[t*(4+1)].CleanReads)!""} & ${(lsSampleInfo[t*(4+1)+1].CleanReads)!""} & ${(lsSampleInfo[t*(4+1)+2].CleanReads)!""} & ${(lsSampleInfo[t*(4+1)+3].CleanReads)!""} & ${(lsSampleInfo[t*(4+1)+4].CleanReads)!""} \\
    Number of contigs & ${(lsSampleInfo[t*(4+1)].contigNum)!""} & ${(lsSampleInfo[t*(4+1)+1].contigNum)!""} & ${(lsSampleInfo[t*(4+1)+2].contigNum)!""} & ${(lsSampleInfo[t*(4+1)+3].contigNum)!""} & ${(lsSampleInfo[t*(4+1)+4].contigNum)!""} \\
    Number of characters & ${(lsSampleInfo[t*(4+1)].characterNum)!""} & ${(lsSampleInfo[t*(4+1)+1].characterNum)!""} & ${(lsSampleInfo[t*(4+1)+2].characterNum)!""} & ${(lsSampleInfo[t*(4+1)+3].characterNum)!""} & ${(lsSampleInfo[t*(4+1)+4].characterNum)!""} \\
    Average contig length(bp) & ${(lsSampleInfo[t*(4+1)].avgContigLen)!""} & ${(lsSampleInfo[t*(4+1)+1].avgContigLen)!""} & ${(lsSampleInfo[t*(4+1)+2].avgContigLen)!""} & ${(lsSampleInfo[t*(4+1)+3].avgContigLen)!""} & ${(lsSampleInfo[t*(4+1)+4].avgContigLen)!""} \\
    Minimum contig length(bp) & ${(lsSampleInfo[t*(4+1)].minContigLen)!""} & ${(lsSampleInfo[t*(4+1)+1].minContigLen)!""} & ${(lsSampleInfo[t*(4+1)+2].minContigLen)!""} & ${(lsSampleInfo[t*(4+1)+3].minContigLen)!""} & ${(lsSampleInfo[t*(4+1)+4].minContigLen)!""} \\
    Contig N50 length(bp) & ${(lsSampleInfo[t*(4+1)].contigN50Len)!""} & ${(lsSampleInfo[t*(4+1)+1].contigN50Len)!""} & ${(lsSampleInfo[t*(4+1)+2].contigN50Len)!""} & ${(lsSampleInfo[t*(4+1)+3].contigN50Len)!""} & ${(lsSampleInfo[t*(4+1)+4].contigN50Len)!""} \\
    Reads Mapping to All-Uingene(\%) & ${(lsSampleInfo[t*(4+1)].readsMappingRate)!""} & ${(lsSampleInfo[t*(4+1)+1].readsMappingRate)!""} & ${(lsSampleInfo[t*(4+1)+2].readsMappingRate)!""} & ${(lsSampleInfo[t*(4+1)+3].readsMappingRate)!""} & ${(lsSampleInfo[t*(4+1)+4].readsMappingRate)!""} \\
    \hline
  \end{tabular}
  </#list>
\end{table}
</#if>