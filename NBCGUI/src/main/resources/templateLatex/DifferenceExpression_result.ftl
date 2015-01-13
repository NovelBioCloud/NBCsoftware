    \subsubsection{Differentially Expressed Unigene Analysis}
    ${caseVSControl} Differ gene analysis were committed between the Control group including S1, S2 and S3 and the Case Group including R1, R2 and R3 by \textbf{${algorithm}}  Algorithm. We have discovered \textbf{${difExpNum}} differentially expressed Unigene including ${upGeneNum} up-regulated gene and ${downGeneNum} down regulated gene (Fold Change Case/Control >2 or <0.5, ${pValueOrFDR}).
 
<#if lsGroupAndGeneNum??>   
\begin{table}[h]
  \centering
  \caption{Differentially Expressed Unigene Analysis}
  \begin{tabular}{cccc}
    \hline
    caseVScontrol & difGeneNum & upGeneNum & downGeneNum \\\hline
    <#list lsGroupAndGeneNum as groupAndGeneNum>
			${groupAndGeneNum.caseVSControl} & ${groupAndGeneNum.difExpNum} & ${groupAndGeneNum.upGeneNum} & ${groupAndGeneNum.downGeneNum}\\
	 </#list>
    \hline
  \end{tabular}
\end{table}
</#if>