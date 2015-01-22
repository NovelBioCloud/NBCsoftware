	\subsubsection{Illumina Sequencing and Data Filtering:}
	In order to achieve the best quality of the RNA Sequencing, Novelbio applied the reads filtration to filter the reads with lower quality and short sequence under following criteria: read length > 50; over 30\% base quality >13. Filtering result was showed in table 1, about \textbf{${avgSize?string("#.#")} Gb} Clean Data was obtained <#if avgFilterRate??>with an average filtering rate of \textbf{${avgFilterRate}\%} separately，</#if>which indicated that the high quality of the reads sequenced in the Experiment. Other quality control result was showed in supplementary datasheet and could be achieved in file "1.FastQC" including the quality score indicating the reads in visualized ways and Sequence GC Content indicating no other species pollution in experiment.

<#if lsSampleInfo??>
	\begin{table}[h]
	  \centering
	  <#if lsSampleInfo.tableTitle??>
	  \caption{${lsSampleInfo.tableTitle}}
	  </#if>
        <#if lsSampleInfo.lsLsData??>
          <#assign i=(lsSampleInfo.lsLsData[0]?size-1)/lsSampleInfo.columnNum>
          <#assign i=i?ceiling>
          <#list 0..i-1 as t>
        	  \begin{tabular}{cccccccccccccc}
        	    \hline
                <#list lsSampleInfo.lsLsData as lsData>
                	<#assign row=lsSampleInfo.lsFirstColumn[lsData_index]+" & ">
            		<#list 1..lsSampleInfo.columnNum as j>
            				<#assign data="">
                     <#if lsData[t*lsSampleInfo.columnNum+j]??>
            					<#assign data=lsData[t*lsSampleInfo.columnNum+j]>
            			<#else>
            					<#assign data="">
            			</#if>
        				<#if j==lsSampleInfo.columnNum>
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
	
<#if lsSampleInfoHasBA??>
	\begin{table}[h]
	  \centering
	  <#if lsSampleInfoHasBA.tableTitle??>
	  \caption{${lsSampleInfoHasBA.tableTitle}}
	  </#if>
        <#if lsSampleInfoHasBA.lsLsData??>
          <#assign i=(lsSampleInfoHasBA.lsLsData[0]?size-1)/lsSampleInfoHasBA.columnNum>
          <#assign i=i?ceiling>
          <#list 0..i-1 as t>
        	  \begin{tabular}{cccccccccccccc}
        	    \hline
                <#list lsSampleInfoHasBA.lsLsData as lsData>
                	<#assign row=lsSampleInfoHasBA.lsFirstColumn[lsData_index]+" & ">
            		<#list 1..lsSampleInfoHasBA.columnNum as j>
            				<#assign data="">
                     <#if lsData[t*lsSampleInfoHasBA.columnNum+j]??>
            					<#assign data=lsData[t*lsSampleInfoHasBA.columnNum+j]>
            			<#else>
            					<#assign data="">
            			</#if>
        				<#if j==lsSampleInfoHasBA.columnNum>
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
