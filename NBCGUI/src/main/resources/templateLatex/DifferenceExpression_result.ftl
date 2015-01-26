    \subsubsection{Differentially Expressed Unigene Analysis}
    ${caseVSControl} Differ gene analysis were committed between the Control group including S1, S2 and S3 and the Case Group including R1, R2 and R3 by \textbf{${algorithm}}  Algorithm. We have discovered \textbf{${difExpNum}} differentially expressed Unigene including ${upGeneNum} up-regulated gene and ${downGeneNum} down regulated gene (Fold Change Case/Control >2 or <0.5, ${pValueOrFDR}).(In  Table <#if table??>\ref{${table.label}</#if>})
 
<#if table??>
	\begin{table}[h]
	  \centering
	  <#if table.tableTitle??>
	  \caption{${table.tableTitle}}\label{${table.label}}
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