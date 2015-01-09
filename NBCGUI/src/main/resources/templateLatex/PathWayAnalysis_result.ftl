    \subsubsection{Pathway Analysis:}
    Pathway analysis was used to find out the significant pathway of the differentially expressed gene sets according to KEGG database. \\

    Here we set the Pathway Analysis of the differentially expressed gene in WT1dvsWT0d as an Example:\\

    Out of the data set of ${DifNum} differentially expressed genes, 328 genes were annotated based on \emph{${speciesName}}  annotation database constructed by Novelbio, based on which significant level was calculated by fish exact test and 16 pathways categories were detected to be significant enriched in as showed in Figure 3, in which Nitrogen metabolism, Biosynthesis of secondary metabolites and Alanine, aspartate and glutamate metabolism  come to be the most significant pathway affected by the differentially expressed genes.\\

    Moreover, up-regulated and down-regulated pathway analysis was introduced by Novelbio which could indicate the up or down differentially genes effect tendency in pathway. \textbf{${upRegulation} significant} up-regulated enriched pathways and \textbf{${downRegulation} significant} pathway was enriched by down-regulated gene. The visualization result could be observed in file "9. Pathway-Analysis". \textbf{(${finderCondition})}

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
