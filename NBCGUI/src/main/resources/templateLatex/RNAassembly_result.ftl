	\subsubsection{Trinity Assembly: }
	Based on the clean reads after filtering, Novelbio committed the transcripts assembly on each sample utilizing Trinity Assembly Software. The result of the first step assembly was showed as following table2:

<#if table??>
	\begin{table}[h]
	  \centering
	  <#if table.tableTitle??>
	  \caption{${table.tableTitle}}
	  </#if>
        <#if table.lsLsData??>
          <#assign i=(table.lsLsData[0]?size-1)/table.columnNum>
          <#assign i=i?ceiling>
          <#list 0..i-1 as t>
        	  \begin{tabular}{cccccccccccccc}
        	    \hline
                <#list table.lsLsData as lsData>
                	<#assign row=table.lsFirstColumn[lsData_index]+" & ">
            		<#list 1..table.columnNum as j>
            				<#assign data="">
                     <#if lsData[t*table.columnNum+j]??>
            					<#assign data=lsData[t*table.columnNum+j]>
            			<#else>
            					<#assign data="">
            			</#if>
        				<#if j==table.columnNum>
        				<#assign row=row+data+" \\\\">
        				<#else>
        				<#assign row=row+data+" & ">
        				</#if>
            		</#list>
            		<#if lsData_index==0>
            				<#assign row=row+"\\hline">		
            		</#if>
                	${row}
                </#list>
        	    \hline
        	  \end{tabular}
          </#list>
        </#if>
	\end{table}
</#if>