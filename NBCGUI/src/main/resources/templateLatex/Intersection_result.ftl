\subsubsection{Venn Analysis:}
In order to study the similarities and differences between ${samples} group, Venn Analysis is introduced to discover the differentially expressed gene similarity and difference of each group:\\
Here we have executed two Venn Analysis for clients.\\
Venn Analysis 1: We have compared the differentially expressed gene of S360dvsWT0d and S950dvsWT0d to discover the gene difference of different strains. (Figure 4)\\

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

GO Analysis and Pathway Analysis towards the S360dvsWT0d only, S950dvsWT0d only and intersection part was applied to discover function and pathway of the group.\\
\textbf{Venn Analysis 2: } We have compared the gene used for utilizing in series cluster in ${samples} group to analyze the series difference of the series. (Figure 5)\\
GO Analysis and Pathway Analysis towards the gene with same series in S36 and S95 and different series in WT of the part containing 814 genes and 1479 genes to discover the S36 and S95 strain unique function and pathway for co-expression network analysis.\\

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
