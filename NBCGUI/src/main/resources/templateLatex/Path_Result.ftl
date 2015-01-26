	Out of the data set of \textbf{${DifNum}} differentially expressed genes, \textbf{${AllDifNum}} genes were annotated based on \textbf{${SpeciesName}} annotation database constructed by Novelbio, based on which significant level was calculated by fish exact test and \textbf{${SigTermNum}} pathways categories were detected to be significant enriched in as showed in (Figure <#if image??>\ref{${image.label}}</#if> ), in which Nitrogen metabolism, \textbf{${ItemTerm}} come to be the most significant pathway affected by the differentially expressed genes.\\

<#if image??>
	\begin{figure}[h]
	  \setlength{\abovecaptionskip}{0pt}
     \setlength{\belowcaptionskip}{0pt}
	  \begin{center}
	  <#list image.lsImgPath as imgPath>
	    \includegraphics[width=10cm,height=6cm]{${imgPath}}
	  </#list>
	  \end{center}
	  <#if image.imgTitle??>
	  \caption{${image.imgTitle}}\label{${image.label}}
	  </#if>
	\end{figure}
</#if>