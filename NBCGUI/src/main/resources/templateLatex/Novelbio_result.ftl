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

\noindent\textbf{${contractId}}\\

\noindent\textbf{CLIENT: }		${clientInfo}\\

\noindent\textbf{DATE:} 	${projectDate}\\

%\maketitle
\newpage

%========== 目录 ==========
    \tableofcontents
    \newpage

%========== PROJECT INFORMATION ==========
\section{PROJECT INFORMATION}
    \begin{table}[h]
      \centering
      \begin{tabular}{|c|c|c|c|}
        \hline
        \multirow{2}*{\includegraphics[width=3cm,height=1cm]{images/logo.png}}
        &\multicolumn{3}{|c|}{NovelBio Corp. Shanghai}\\\cline{2-4}
        & Project Final Report Approval & No. & ${contractId} \\\hline
        \multicolumn{4}{|c|}{Basic Information} \\\hline
        Project Code & \multicolumn{3}{|c|}{${projectCode}} \\\hline
        Project Name & \multicolumn{3}{|c|}{${projectName}} \\\hline
        Report Title & \multicolumn{3}{|c|}{${reportTitle}} \\\hline
        Completed Date & \multicolumn{3}{|c|}{${completedDate}}\\\hline
        Report Drafter & Bo Zhang & Date & ${drafterDate} \\\hline
        Quality Controls & Yanling Li & Date & ${qualityCtrlDate} \\\hline
        Study Manager & Bo Zhang & Date & ${studyDate} \\\hline
        Project Manager & Bo Zhang & Date & ${projectDate} \\\hline
        Director & Jie Zong & Date & ${directorDate}  \\\hline
        Note & \multicolumn{3}{|c|}{None} \\\hline
      \end{tabular}
    \end{table}

    \noindent 质量保证申明：\\
    我们确认实验中所采用的方法、步骤及观察均作了准确、完整的描述，报告结果准确、完整的反映了该项目的原始数据。\\
    我们对该项目研究的检查保证了原始数据的准确性。\\
    Quality Assurance Statement:\\
    We confirmed that methods, procedures and observations were accurately and completely described, and the reported results accurately and completely reflect the raw data of this study project.\\
    We performed study-based inspection to insure the accuracy of the raw data.\\

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
      <#if lsSampleInfo??>
        \begin{table}[h]
          \centering
            \begin{tabular}{cccc}
            \toprule[1.4pt]
                \hline
                No & Sample ID & Sample Type & RIN \\
                \hline
                <#list lsSampleInfo as sampleInfo>
                    ${sampleInfo.No} & ${sampleInfo.sampleID} & ${sampleInfo.sampleType} & ${sampleInfo.RIN} \\
                </#list>
                \hline
            \bottomrule[1.4pt]
            \end{tabular}
        \end{table}
      </#if>
      \item \textbf{Analysis Process}\\
      \textbf{NovelBio provided the RNA sequencing service on the work flow accepted by the clients, which could be study in the supplementary data. }
      \item \textbf{Data Quality: Instrument and Process Variability}\\
        Novelbio: Reads Quality Analysis and the GC distribution are applied using Fast-QC \\ (\href{http://www.bioinformatics.babraham.ac.uk/projects/fastqc/}{http://www.bioinformatics.babraham.ac.uk/projects/fastqc/}). Reads Quality Analysis was determined by the percentage of the reads over Q20 quality. GC distribution analysis was determined by the similarity of the raw reads distribution to the theory GC content.
        <#if lsQCSample??>
        \begin{table}[h]
          \centering
            \begin{tabular}{cc}
            \toprule[1.4pt]
                \hline
                QC Sample & Measurement \\
                \hline
                <#list lsQCSample as QCSample>
                ${QCSample.name} & ${QCSample.measurement} \\
                </#list>
                \hline
            \bottomrule[1.4pt]
            \end{tabular}
        \end{table}
        </#if>
    \end{enumerate}

    \newpage

%========== RESULT DESCRIPTION ==========
\section{RESULT DESCRIPTION}

\subsection{ABSTRACT}

    \paragraph{Raw Data Treatment:}
    Using high-throughput ${sequence}, the transcript with poly (A)+ RNA of ${speciesName} were analyzed. Reads sequenced were filtered and mapped to ${speciesName} genome (download from NCBI) using ${software} software. The mapped reads was counted to achieve the expression of each gene based on the gene annotation information from NCBI database.
    \paragraph{Preliminary Analysis:}
    The differentially expressed genes were achieved by ${algorithm} algorithms and annotated by NCBI Database and by blasting to ${blastSampleName} protein sequence of the transcripts. Based on the differentially expressed gene, we applied the Gene Ontology (GO) Analysis and Pathway Analysis to discover the function and pathway enriched among the differentially expressed gene.
    \paragraph{In-Depth Analysis:}
    Series Cluster Analysis was introduced to discover the expression trend of differentially expressed gene in different strains of grape from 0d, 1d to 7d. GO Analysis and Pathway Analysis was applied towards the gene with different expression trend. Venn Analysis was introduced towards the ${samples} group to discover the genes' expression trend difference. GO Analysis, Pathway Analysis and Co-Expression-Network was done to discover the core gene among the gene with different trends. Alternative Splicing Analysis result was achieved to discover the alternative splicing difference in different sample. Venn Analysis on Alternative Splicing was also committed to discover the difference between ${samples} strains.
    
     