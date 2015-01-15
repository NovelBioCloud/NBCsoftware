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

${d}


<#if test==1>是1<#else><#if lsStr?size==2>是2<#else>不知道</#if></#if>


${lsStr?size}

${speciesName!"\\colorbox{red}{用户自定义物种}"}

<#assign i=11/5 />
<#assign i=i?ceiling-1>
${i}

<#list 0..i as t>
${t}
</#list>

$PI^{TM}$


<#list 0..0 as t>
${t}
</#list>





