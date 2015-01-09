	Out of the data set of \textbf{${DifNum}} differentially expressed genes, \textbf{${AllDifNum}} genes were annotated based on \textbf{${SpeciesName}} annotation database constructed by Novelbio, based on which significant level was calculated by fish exact test and \textbf{${SigTermNum}} pathways categories were detected to be significant enriched in as showed in Figure 3, in which Nitrogen metabolism, \textbf{${ItemTerm}} come to be the most significant pathway affected by the differentially expressed genes.\\

<#if lsImage??>
<#list lsImage as image>
	\begin{figure}
	  \begin{center}
	  <#list image.lsImgPath as imgPath>
	    \includegraphics[width=${image.width}cm,height=${image.height}cm]{${imgPath}}
	  </#list>
	  \end{center}
	  <#if image.imgTitle??>
	  \caption{${image.imgTitle}}
	  </#if>
	\end{figure}
</#list>
</#if>
