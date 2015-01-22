package com.novelbio.nbcReport;

import java.util.List;
import java.util.Map;

import com.novelbio.report.ReportImage;

import junit.framework.TestCase;

public class TestReportImage extends TestCase {
	
	public void testReportImage() {
		ReportImage reportImage = new ReportImage();
		reportImage.addImgPath("/media/nbfs/nbCloud/public/AllProject/project_54b726b6e4b05de727ffa782/task_54bce9d5e4b05de728003f8c/SamStatistic_result/ChrDistribution_A-1.png");
		reportImage.setImgTitle("测试");
		reportImage.setImgHeight(12);
		reportImage.setImgWidth(11);
		Map<String, Object> mapKey2Param = reportImage.getMapKey2Param();
		assertEquals("测试", mapKey2Param.get("imgTitle"));
		assertEquals(12, mapKey2Param.get("height"));
		assertEquals(11, mapKey2Param.get("width"));
		List<String> lsImgPath = (List<String>) mapKey2Param.get("lsImgPath");
		assertEquals("image_54bce9d5e4b05de728003f8c/ChrDistribution_A-1.png", lsImgPath.get(0));

	}

}
