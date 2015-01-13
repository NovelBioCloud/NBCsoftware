\begin{table}
  \centering
  \caption{}\label{}
  \begin{tabular}{cccc}
    \hline
     <#list lsStr as string>
		${string[0]} & ${string[1]} & ${string[2]} & ${string[3]}\\<#if string_index==0>\hline</#if>
	  </#list>
    \hline
  \end{tabular}
\end{table}

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

<#if lsTable??>
<#list lsTable as table>
	\begin{table}
	  \centering
	  <#if table.tableTitle??>
	  \caption{table.tableTitle}
	  </#if>
	  \begin{tabular}{cccc}
	    \hline
	     <#list table.lsLsData as lsData>
			${lsData[0]} & ${lsData[1]} & ${lsData[2]} & ${lsData[3]}\\<#if str_index==0>\hline</#if>
		  </#list>
	    \hline
	  \end{tabular}
	\end{table}
</#list>
</#if>

${d?c}
${d?string("0.##E0")}

<#if test==1>是1<#else><#if lsStr?size==2>是2<#else>不知道</#if></#if>

\begin{table}
  \centering
  \caption{Differentially Expressed Unigene Analysis}
  \begin{tabular}{|c|c|c|c|}
    \hline
    % after \\: \hline or \cline{col1-col2} \cline{col3-col4} ...
    caseVScontrol & difGeneNum & upGeneNum & downGeneNum \\\hline
    <#list table.lsLsData as lsData>
			${lsData[0]} & ${lsData[1]} & ${lsData[2]} & ${lsData[3]}\\
	 </#list>
    \hline
  \end{tabular}
\end{table}


${lsStr?size}




