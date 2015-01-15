\subsubsection{Alternative Splicing Analysis:}
Group：${groupName}
Alternative splicing analysis was constructed to discover the difference of each samples group and discover several important alternative splicing genes. Following table 4 was the alternative splicing result of compare ${groupName}.

<#if lsSplicingEvent??>
<#assign i=lsSplicingEvent?size/3>
<#assign i=i?floor>
\begin{table}[h]
  \centering
  \caption{Mapping Statistics}
  <#list 0..i as t>
  \begin{tabular}{llll}
    \hline
    SplicingEvent & ${lsSplicingEvent[i*(2+1)].SplicingEvent} & ${lsSplicingEvent[i*(2+1)+1].SplicingEvent} & ${lsSplicingEvent[i*(2+1)+2].SplicingEvent} \\\hline
    cassette & ${lsSplicingEvent[i*(2+1)].cassette} & ${lsSplicingEvent[i*(2+1)+1].cassette} & ${lsSplicingEvent[i*(2+1)+2].cassette} \\
    cassette\_multi & ${lsSplicingEvent[i*(2+1)].cassetteMulti} & ${lsSplicingEvent[i*(2+1)+1].cassetteMulti} & ${lsSplicingEvent[i*(2+1)+2].cassetteMulti} \\
    alt5 & ${lsSplicingEvent[i*(2+1)].alt5} & ${lsSplicingEvent[i*(2+1)+1].alt5} & ${lsSplicingEvent[i*(2+1)+2].alt5} \\
    alt3 & ${lsSplicingEvent[i*(2+1)].alt3} & ${lsSplicingEvent[i*(2+1)+1].alt3} & ${lsSplicingEvent[i*(2+1)+2].alt3} \\
    altend & ${lsSplicingEvent[i*(2+1)].altend} & ${lsSplicingEvent[i*(2+1)+1].altend} & ${lsSplicingEvent[i*(2+1)+2].altend} \\
    altstart & ${lsSplicingEvent[i*(2+1)].altstart} & ${lsSplicingEvent[i*(2+1)+1].altstart} & ${lsSplicingEvent[i*(2+1)+2].altstart} \\
    mutually\_exclusive & ${lsSplicingEvent[i*(2+1)].mutuallyExclusive} & ${lsSplicingEvent[i*(2+1)+1].mutuallyExclusive} & ${lsSplicingEvent[i*(2+1)+2].mutuallyExclusive} \\
    retain\_intron & ${lsSplicingEvent[i*(2+1)].retainIntron} & ${lsSplicingEvent[i*(2+1)+1].retainIntron} & ${lsSplicingEvent[i*(2+1)+2].retainIntron} \\
    \hline
  \end{tabular}
  </#list>
\end{table}
</#if>

Moreover, Novelbio compare the alternaive splicing situation of S36 strain and WT stain in different time point.