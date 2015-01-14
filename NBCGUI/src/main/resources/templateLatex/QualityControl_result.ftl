	\subsubsection{Illumina Sequencing and Data Filtering:}
	In order to achieve the best quality of the RNA Sequencing, Novelbio applied the reads filtration to filter the reads with lower quality and short sequence under following criteria: read length > 50; over 30\% base quality >13. Filtering result was showed in table 1, about \textbf{${avgSize?string("#.#")} Gb} Clean Data was obtained <#if avgFilterRate??>with an average filtering rate of \textbf{${avgFilterRate}\%} separately，</#if>which indicated that the high quality of the reads sequenced in the Experiment. Other quality control result was showed in supplementary datasheet and could be achieved in file "1.FastQC" including the quality score indicating the reads in visualized ways and Sequence GC Content indicating no other species pollution in experiment.
	
<#if lsSampleInfo??>
\begin{table}[h]
  \centering
  \caption{Raw Data \& Clean Data Statistics}
  \begin{tabular}{ccccc}
    \hline
    sampleName & readsNum & length & baseNum & CG\% \\\hline
    <#list lsSampleInfo as sampleInfo>
    ${sampleInfo.sampleName} & ${sampleInfo.readsNum} & ${sampleInfo.Length} & ${sampleInfo.baseNum} & ${sampleInfo.CG}\% \\
    </#list>
    \hline
  \end{tabular}
\end{table}
</#if>

<#if lsSampleInfoHasBA??>
\begin{table}[h]
  \centering
  \caption{Raw Data \& Clean Data Statistics}
  \begin{tabular}{cccccc}
    \hline
    sampleName & beforeReadsNum & afterReadsNum & beforeBaseNum & afterBaseNum & baseFilterRate\% \\\hline
    <#list lsSampleInfoHasBA as sampleInfo>
    ${sampleInfo.sampleName} & ${sampleInfo.beforeReadsNum} & ${sampleInfo.afterReadsNum} & ${sampleInfo.beforeBaseNum} & ${sampleInfo.afterBaseNum} & ${sampleInfo.baseFilterRate}\% \\
    </#list>
    \hline
  \end{tabular}
\end{table}
</#if>
