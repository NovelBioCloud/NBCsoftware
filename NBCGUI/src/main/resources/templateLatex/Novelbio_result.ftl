%!mode::"TeX:UTF-8"
%========== 指定文档类型 ==========
\documentclass{article}

%========== 导入相关的包 ==========
\usepackage{ctex}
\usepackage{graphicx,psfrag}
\usepackage{draftwatermark}
\usepackage{amsmath}
\usepackage{amssymb}
\usepackage[top=2.54cm,bottom=2.54cm,left=3.18cm,right=3.18cm]{geometry}
\usepackage{fancyhdr}
\usepackage{booktabs}
\usepackage{colortbl}
\usepackage{multirow}
\usepackage{hyperref}

%========== 设置图片路径 ==========
%\graphicspath{{F:/template/GoPathway/images/}}

%========== 定义颜色,在后面直接用tabcolor就是现在设置的这个颜色 ==========
\definecolor{tabcolor}{rgb}{0.105,0.410,0.113}

%========== 设置水印文字 ==========
\SetWatermarkText{NovelBio}
\SetWatermarkLightness{0.8}
\SetWatermarkScale{0.8}

%========== 设置页眉页脚 ==========
\pagestyle{fancy}
\lhead{\includegraphics[width=3cm,height=1cm]{images/logo.png}}
\chead{}
\rhead{}
\lfoot{地址：上海市闵行区莘建东路58号绿地科技大厦B座701室\\
电话：021-60516639               网址：www.novelbio.com    }
\cfoot{}
\rfoot{\thepage}

%========== 设置作者和标题 ==========
%\author{张三}
%\title{GoPathway结果报告}

%========== 修改目录、图片、表格标题 ==========
%\renewcommand\figurename{图}
%\renewcommand\tablename{表}
%\renewcommand\contentsname{\centerline{目录}}

%========== 设置全局英文字体 ==========
\setmainfont{Times New Roman}

%========== 文档的开始 ==========
\begin{document}

%========== 封面 ==========
\begin{figure}
  \includegraphics[width=9cm,height=4cm]{images/logo.png}\\
\end{figure}
~\\
~\\
~\\
~\\
\noindent{\zihao{2}\textcolor[rgb]{0.00,1.00,1.00}{\emph{$mNovelbio^{TM}$}} \textcolor[rgb]{0.00,0.50,0.75}{REPORT}}\\

\noindent{\zihao{3}\textbf{${speciesName} RNA sequencing Report}}\\

\noindent\textbf{}\\

\noindent\textbf{CLIENT: }		\\

\noindent\textbf{DATE:} 	\\

%\maketitle
\newpage

%========== 目录 ==========
    \tableofcontents
    \newpage

%========== PROJECT INFORMATION ==========


    \newpage

%========== DELIVERABLES ==========
\section{DELIVERABLES}

    %一个列表
    \begin{enumerate}
      \item \textbf{Data Provided}\\
      Data are transferred electronically by Hard Disk which includes the following:
        \begin{enumerate}
          \item Raw Data
          \item Clean Data After Filtering (Criteria Provided)
          \item Preliminary Analysis
          \item In-depth Analysis
          \item Written Analysis Report
        \end{enumerate}
      \item \textbf{Table and Graphics}\\
      The table of the Preliminary Analysis and In-depth Analysis were applied to in Excel Format, while graphics were provided in 'png' and 'tiff' format which was acceptable for publication
      \item \textbf{Written report}\\
      Study description and results and QC information are provided in this MS word format. \textbf{Method}, \textbf{Result Explanation}, and \textbf{Reference} were provided in the word as well.
    \end{enumerate}

    \newpage

%========== STUDY DESCRIPTION ==========
\section{STUDY DESCRIPTION}

    \begin{enumerate}
      \item \textbf{Purpose of Experiment}\\
      The goal of this study was to characterize transcriptome profiles manifested in ${speciesName}. Transcriptome assembly and annotated were introduced to predict the probable function of the transcripts.
      \item \textbf{Experimental design}\\
<#if tbSampleInfo??>
	\begin{table}[h]
	  \centering
	  <#if tbSampleInfo.tableTitle??>
	  \caption{${tbSampleInfo.tableTitle}}
	  </#if>
        <#if tbSampleInfo.lsLsData??>
          <#assign i=(tbSampleInfo.lsLsData[0]?size-1)/tbSampleInfo.columnNum>
          <#assign i=i?ceiling>
          <#list 0..i-1 as t>
        	  \begin{tabular}{cccccccccccccc}
        	    \hline
                <#list tbSampleInfo.lsLsData as lsData>
                	<#assign row=tbSampleInfo.lsFirstColumn[lsData_index]+" & ">
            		<#list 1..tbSampleInfo.columnNum as j>
            				<#assign data="">
                     <#if lsData[t*tbSampleInfo.columnNum+j]??>
            					<#assign data=lsData[t*tbSampleInfo.columnNum+j]>
            			<#else>
            					<#assign data="">
            			</#if>
        				<#if j==tbSampleInfo.columnNum>
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
      \item \textbf{Sample Information}\\
      Sample DNA QC detailed report is attached in a separate file.\\

      \item \textbf{Analysis Process}\\
      \textbf{NovelBio provided the RNA sequencing service on the work flow accepted by the clients, which could be study in the supplementary data. }
      \item \textbf{Data Quality: Instrument and Process Variability}\\
        Novelbio: Reads Quality Analysis and the GC distribution are applied using Fast-QC \\ (\href{http://www.bioinformatics.babraham.ac.uk/projects/fastqc/}{http://www.bioinformatics.babraham.ac.uk/projects/fastqc/}). Reads Quality Analysis was determined by the percentage of the reads over Q20 quality. GC distribution analysis was determined by the similarity of the raw reads distribution to the theory GC content.

    \end{enumerate}

    \newpage

%========== RESULT DESCRIPTION ==========
\section{RESULT DESCRIPTION}

\subsection{ABSTRACT}

     