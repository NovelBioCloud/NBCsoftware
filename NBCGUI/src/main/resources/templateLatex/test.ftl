\begin{table}
  \centering
  \caption{}\label{}
  \begin{tabular}{cccc}
    \hline
     <#list lsStr as str>
		${str[0]} & ${str[1]} & ${str[2]} & ${str[3]}\\<#if str_index==0>\hline</#if>
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