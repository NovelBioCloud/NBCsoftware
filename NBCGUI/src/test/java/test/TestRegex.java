package test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.nbcReport.Params.ReportBase;
import com.novelbio.report.ReportImage;
import com.novelbio.report.Params.ReportGOResult;
import com.novelbio.report.generateReport.GenerateReport;

public class TestRegex {
	
	public static void main(String[] args) {
		
		List<String> lsImgPath = FileOperate.getFoldFileNameLs("/home/novelbio/jpx/test", "*", "*");
		
		
//		Writer out = new BufferedWriter(new OutputStreamWriter(FileOperate.getOutputStream("/home/novelbio/jpx/jpx.txt", true)));
//		try {
//			out.write("dgadrfg\n");
//			out.write("test");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//			try {
//				out.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
//		System.out.println("\\%");
		
//		List<String[]> lslsList = new ArrayList<String[]>();
//		String[] lsStrings = new String[2];
//		lslsList.add(lsStrings);
//		lslsList.get(0)[0] = "adfgad";
//		System.out.println(lslsList.get(0)[0]);
		
//		DecimalFormat decimalFormat = new DecimalFormat("#.###"); 
//		long long1 = 12345678546l;
//		long long2 = 54654698546l;
//		double d = (double)long2/long1;
//		System.out.println(decimalFormat.format(d));
		
//		int num = 0;
//		for (int i = 0; i < 5; i++) {
//			if (num++ >= 3) {
//				System.out.println("num1:"+num);
//				break;
//			}
//			System.out.println("num2:"+num);
//			System.out.println("i:"+i);
//		}
		
//		TestRegex testRegex = new TestRegex();
//		System.out.println(testRegex.reportImage == null);
		
//		Map<String, String> mapKey2Str = new HashMap<String, String>();
//		mapKey2Str.put("a", "A");
//		mapKey2Str.remove("a");
//		System.out.println(mapKey2Str.size());
		
//		ReportBase reportBase = ReportBase.readReportFromFile("/media/nbfs/nbCloud/public/dev/AllProject/project_53a3ef98559aa1839ed9c4d2/task_54aca51a8314525ab6dc8cb8/GOAnalysis_result/.report/report_GOAnalysis_AvsB-Dif");
//		System.out.println(reportBase);
		
//		String imgPath = "/asdgadfg/test";
//		String imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1, imgPath.length());
//		System.out.println(imgName);
		
//		String reportPath = "/media/nbfs/nbCloud/public/dev/AllProject/project_53a3ef98559aa1839ed9c4d2/task_54aca51a8314525ab6dc8cb8/GOAnalysis_result";
//		GenerateReport generateReport = new GenerateReport();
//		generateReport.generateReport(reportPath);
		
//		String reportPath = "/media/nbfs/nbCloud/public/dev/AllProject/project_53a3ef98559aa1839ed9c4d2/task_54aa3ed38314b5cdd982a205/GOAnalysis_result";
//		String filePath = "/media/nbfs/nbCloud/public/dev/AllProject/project_53a3ef98559aa1839ed9c4d2/task_54aa3ed38314b5cdd982a205/GOAnalysis_result/.report/sdagasdrga";
//		String resultPath = reportPath + filePath.substring(filePath.lastIndexOf("/"), filePath.length()) + ".tex";
//		System.out.println(resultPath);
		
//		String regex = "\\$\\{text\\}";
//		Pattern pattern = Pattern.compile(regex);
//		Matcher matcher = pattern.matcher("${text}");
//		System.out.println(matcher.matches());
		
	}

}
