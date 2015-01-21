package com.novelbio.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.novelbio.base.fileOperate.FileOperate;
import com.novelbio.dbInfo.model.project.NBCTask;

public class ReportImage {
	/** 图片上层文件夹的前缀，后面加taskId，例 image_54aca51a8314525ab6dc8cb8*/
	public static final String IMAGE = "image_";
	
	/**图片的主题*/
	private String imgTitle;
	/**图片的宽度*/
	private int width = 5;
	/**图片的高度*/
	private int height = 3;
	/**图片文件路径的集合，可以存相对路径也可以存绝对路径*/
	private List<String> lsImgPath = new ArrayList<String>();
	/**图片的参数*/
	private Map<String, Object> mapKey2Param = new HashMap<String, Object>();
	
	/**获得图片的参数*/
	public Map<String, Object> getMapKey2Param() {
		mapKey2Param.put("imgTitle", imgTitle);
		mapKey2Param.put("width", width);
		mapKey2Param.put("height", height);
		mapKey2Param.put("lsImgPath", lsImgPath);
		return mapKey2Param;
	}
	
	/**设置图片标题*/
	public void setImgTitle(String imgTitle) {
		this.imgTitle = imgTitle;
	}
	
	/**设置宽度*/
	public void setImgWidth(int width) {
		this.width = width;
	}
	
	/**设置高度*/
	public void setImgHeight(int height) {
		this.height = height;
	}
	
	/**设置图片的名称，传过来的imgPath为图片的全路径，taskId用于生成图片的路径 例如：image_54aca51a8314525ab6dc8cb8/filename.png*/
	public void addImgPath(String imgPath) {
		//截取除图片的名称
		String imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1, imgPath.length());
		String taskId = NBCTask.getTaskId(imgPath);
		String imagePath = FileOperate.addSep(IMAGE + taskId) + imgName;
		lsImgPath.add(imagePath);
	}

}
