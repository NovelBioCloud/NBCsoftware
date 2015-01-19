	\subsubsection{Mapping Statistics:}
	Mapping statistics was showed in Table 2, from which we could mention the mapping rates about ${mappingRate}\% indicating the well-performance of the sequencing experiment. Furthermore, more than ${uniqueMappingRate}\% unique mapping rate and more than ${junctionReadsRate}\% of the junction reads could lead to the best quality of the gene expression and alternative splicing analysis.

<#if table??>
	\begin{table}
	  \centering
	  <#if table.tableTitle??>
	  \caption{${table.tableTitle}}
	  </#if>
        <#if table.lsLsData??>
          <#assign i=table.lsLsData[0]?size/table.columnNum>
          <#assign i=i?floor>
          <#list 0..i as t>
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

	Chromosome Distribution Analysis for analyzing the sequencing quality and Gene Structure Analysis for analyzing the RNA capture quality was introduced and could be achieved in folder "3. Chromosome Distribution Analysis" and "4. Gene Structure Analysis".