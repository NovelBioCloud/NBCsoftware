    
    In the distribution of the \textbf{Biological Process} categories (BP), <#if lsGOTermBP??><#list lsGOTermBP as goTerm><#if goTerm_index==0>\emph{\textbf{${goTerm.detail}}} made up the majority of the GO Analysis \textbf{(${goTerm.num} genes, P-Value: ${goTerm.pValue?string("0.##E0")})},<#else><#if lsGOTermBP?size==goTerm_index+1> and \emph{\textbf{${goTerm.detail}}} \textbf{(${goTerm.num} genes, P-Value: ${goTerm.pValue?string("0.##E0")})}.<#else>\emph{\textbf{${goTerm.detail}}} \textbf{(${goTerm.num} genes, P-Value: ${goTerm.pValue?string("0.##E0")})},</#if></#if></#list></#if>\\

    In the distribution of the \textbf{Molecular Function} categories (MF), <#if lsGOTermMF??><#list lsGOTermMF as goTerm><#if goTerm_index==0>\emph{\textbf{${goTerm.detail}}} made up the majority of the GO Analysis \textbf{(${goTerm.num} genes, P-Value: ${goTerm.pValue?string("0.##E0")})},<#else><#if lsGOTermMF?size==goTerm_index+1> and \emph{\textbf{${goTerm.detail}}} \textbf{(${goTerm.num} genes, P-Value: ${goTerm.pValue?string("0.##E0")})}.<#else>\emph{\textbf{${goTerm.detail}}} \textbf{(${goTerm.num} genes, P-Value: ${goTerm.pValue?string("0.##E0")})},</#if></#if></#list></#if>\\

    In the distribution of the \textbf{Cellular Component} categories (CC), <#if lsGOTermCC??><#list lsGOTermCC as goTerm><#if goTerm_index==0>\emph{\textbf{${goTerm.detail}}} made up the majority of the GO Analysis \textbf{(${goTerm.num} genes, P-Value: ${goTerm.pValue?string("0.##E0")})},<#else><#if lsGOTermCC?size==goTerm_index+1> and \emph{\textbf{${goTerm.detail}}} \textbf{(${goTerm.num} genes, P-Value: ${goTerm.pValue?string("0.##E0")})}.(In  Figure <#if image??>\ref{${image.label}}</#if> )<#else>\emph{\textbf{${goTerm.detail}}} \textbf{(${goTerm.num} genes, P-Value: ${goTerm.pValue?string("0.##E0")})},</#if></#if></#list></#if>\\

<#if image??>
	\begin{figure}[h]
	  \setlength{\abovecaptionskip}{0pt}
     \setlength{\belowcaptionskip}{0pt}
	  \begin{center}
	  <#list image.lsImgPath as imgPath>
	    \includegraphics[width=10cm,height=6cm]{${imgPath}}
	  </#list>
	  \end{center}
	  <#if image.imgTitle??>
	  \caption{${image.imgTitle}}\label{${image.label}}
	  </#if>
	\end{figure}
</#if>