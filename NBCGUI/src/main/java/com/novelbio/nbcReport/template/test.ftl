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
\graphicspath{{F:/template/GoPathway/images/}}

%========== 定义颜色,在后面直接用tabcolor就是现在设置的这个颜色 ==========
\definecolor{tabcolor}{rgb}{0.105,0.410,0.113}

%========== 设置水印文字 ==========
\SetWatermarkText{NovelBio}
\SetWatermarkLightness{0.8}
\SetWatermarkScale{0.8}

%========== 设置页眉页脚 ==========
\pagestyle{fancy}
\lhead{\includegraphics[width=3cm,height=1cm]{logo.png}}
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
  \includegraphics[width=9cm,height=4cm]{logo.png}\\
\end{figure}
~\\
~\\
~\\
~\\
\noindent{\zihao{2}\textcolor[rgb]{0.00,1.00,1.00}{\emph{$mNovelbio^{TM}$}} \textcolor[rgb]{0.00,0.50,0.75}{REPORT}}\\

\noindent{\zihao{3}\textbf{Vitis \emph{vinifera} RNA sequencing Report}}\\

\noindent\textbf{XA-201409-XWR-RNASEQ}\\

\noindent\textbf{CLIENT: }		西北农林科技大学 徐伟荣 博士\\

\noindent\textbf{DATE:} 	Sep 14, 2014\\

%\maketitle
\newpage

%========== 目录 ==========
    \tableofcontents
    \newpage
    
%========== 内容 ==========
${name}${sex}

\end{document}