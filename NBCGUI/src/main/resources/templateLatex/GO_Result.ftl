    In the distribution of the \textbf{Biological Process} categories (BP), the \emph{\textbf{cholesterol biosynthetic process}} ${GOBPFirst} made up the majority of the GO Analysis \textbf{(${GOBPFirstNum} genes, P-Value: ${GOBPFirstP?c})}, following by \emph{\textbf{sterol biosynthetic process}} ${GOBPSecond} \textbf{(${GOBPSecondNum} genes, P-Value: ${GOBPSecondP?c})} and \emph{\textbf{cholesterol metabolic process ${GOBPThird}}} \textbf{(${GOBPThirdNum} genes, P-Value: ${GOBPThirdP?c})}.\\

    In the distribution of the \textbf{Molecular Function} categories (MF), the \emph{\textbf{cholesterol biosynthetic process}} ${GOMFFirst} made up the majority of the GO Analysis \textbf{(${GOMFFirstNum} genes, P-Value: ${GOMFFirstP?c})}, following by \emph{\textbf{sterol biosynthetic process}} ${GOMFSecond} \textbf{(${GOMFSecondNum} genes, P-Value: ${GOMFSecondP?c})} and \emph{\textbf{cholesterol metabolic process ${GOMFThird}}} \textbf{(${GOMFThirdNum} genes, P-Value: ${GOMFThirdP?c})}.\\

    In the distribution of the \textbf{Cellular Component} categories (CC), the \emph{\textbf{cholesterol biosynthetic process}} ${GOCCFirst} made up the majority of the GO Analysis \textbf{(${GOCCFirstNum} genes, P-Value: ${GOCCFirstP?c})}, following by \emph{\textbf{sterol biosynthetic process}} ${GOCCSecond} \textbf{(${GOCCSecondNum} genes, P-Value: ${GOCCSecondP?c})} and \emph{\textbf{cholesterol metabolic process ${GOCCThird}}} \textbf{(${GOCCThirdNum} genes, P-Value: ${GOCCThirdP?c})}.\\

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
