package com.novelbio.nbcReport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 表格参数类
 * 
 * @author novelbio
 * 
 */
public class XdocTable implements Cloneable, Serializable {
	private static final long serialVersionUID = 1L;
	public static final String ROW_AUTO = "auto";
	private String weight = "0.0";
	private String cols = "";
	private String strokeWidth = "1.0";
	private String rows = "";
	private String backfillColor = "#00CCCC";
	private String singlefillColor = "#CCFFFF";
	private String doublefillColor = "#FFFFFF";
	private String upCompare = "";
	private String downCompare = "";
	private String title = "";
	private String note = "";
	private String height = "200";
	private List<String> lsAligns;
	private List<List<String>> lsExcelTable = null;
	private int autoRowMax = 15;
	private String rowHeights = "20,20,20,20,20,20,20,20,20,20,20,20,20,20,20";


	/**
	 * 
	 * @param cols
	 * @param align
	 *            l是居左　c是居中
	 */
	public XdocTable(String cols, String align) {
		this.rows = rowHeights;
		if (cols != null) {
			this.cols = cols;
		}
		if (align != null) {
			lsAligns = new ArrayList<String>();
			String[] aligns = align.split(",");
			for (int i = 0; i < aligns.length; i++) {
				if (aligns[i].equalsIgnoreCase("l")) {
					lsAligns.add("left");
				} else {
					lsAligns.add("center");
				}
			}
		}
	}

	/**
	 * 
	 * @param cols
	 * @param align
	 *            l是居左　c是居中
	 * @param rows
	 */
	public XdocTable(String cols, String align, String rowHeights) {
		String rows = rowHeights;
		if (cols != null) {
			this.cols = cols;
		}

		if (rows != null) {
			this.rows = rows;
		}
		if (align != null) {
			lsAligns = new ArrayList<String>();
			String[] aligns = align.split(",");
			for (int i = 0; i < aligns.length; i++) {
				if (aligns[i].equalsIgnoreCase("l")) {
					lsAligns.add("left");
				} else {
					lsAligns.add("center");
				}
			}
		}
	}
	
	/**
	 * 默认每行全部居中，共15行，高度15,
	 * 
	 * @param cols
	 */
	public XdocTable(String cols) {
		this.rows = rowHeights;
		String align = null;
		int colsLength = cols.split(",").length;
		for (int i = 0; i < colsLength; i++) {
			if (align == null) {
				align = "c";
			} else {
				align = align + ",c";
			}
		}

		if (cols != null) {
			this.cols = cols;
		}

	}
	
	/**
	 * 
	 * 自动行数，
	 * @param cols
	 * @param maxRow 最大行数
	 */
	public XdocTable(String cols, int maxRow) {
		setRowAuto(maxRow);
		if (cols != null) {
			this.cols = cols;
		}
	}

	public String getStrokeWidth() {
		return strokeWidth;
	}

	public void setStrokeWidth(String strokeWidth) {
		this.strokeWidth = strokeWidth;
	}

	/**
	 * 整个table的高度
	 * 
	 * @param height
	 */
	public void setHeight(String height) {
		this.height = height;
	}

	/**
	 * 整个table的高度
	 * 
	 * @param height
	 */
	public String getHeight() {
		return height;
	}

	/** 边框宽度 */
	public String getWeight() {
		return weight;
	}

	/** 列宽集合　如100,200,50,80 */
	public String getCols() {
		return cols;
	}

	/** 行高 如10,20,30 */
	public String getRows() {
		if (rows.equalsIgnoreCase(ROW_AUTO)) {
			rows = "20";
			int excelLength = lsExcelTable.size();
			int rowNum = excelLength < autoRowMax ? excelLength : autoRowMax;
			if (autoRowMax == 0) {
				rowNum = excelLength;
			}
			for (int i = 0; i < rowNum - 1; i++) {
				rows += ",20";
			}
		}
		return rows;
	}

	public List<String> getLsAligns() {
		if (lsAligns == null) {
			lsAligns = new ArrayList<>();
			int excelLength = 30;
			try {
				excelLength = lsExcelTable.get(0).size();
			} catch (Exception e) {
				// TODO: handle exception
			}
			for (int i = 0; i < excelLength; i++) {
				lsAligns.add("center");
			}
		}
		return lsAligns;
	}

	/** 表格第一行标题填充色 */
	public String getBackfillColor() {
		return backfillColor;
	}

	/** 奇数行填充色 */
	public String getSinglefillColor() {
		return singlefillColor;
	}

	/** 偶数行填充色 */
	public String getDoublefillColor() {
		return doublefillColor;
	}

	/** 表格上方说明 */
	public String getUpCompare() {
		return upCompare;
	}

	/** 表格下方说明 */
	public String getDownCompare() {
		return downCompare;
	}

	/** 表格总标题 */
	public String getTitle() {
		return title;
	}

	/** 下方的 注: */
	public String getNote() {
		return note;
	}

	/** 最大行数 */
	public int getMaxRow() {
		if (rows.equalsIgnoreCase(ROW_AUTO)) {
			int excelLength = lsExcelTable.size();
			int rowNum = excelLength < autoRowMax ? excelLength : autoRowMax;
			if (autoRowMax == 0) {
				rowNum = excelLength;
			}
			return rowNum;
		}
		return rows.split(",").length;
		
	}

	/** 最大列数 */
	public int getMaxCol() {
		return cols.split(",").length;
	}

	/** 表格数据集合 */
	public List<List<String>> getLsExcelTable() {
		return lsExcelTable;
	}

	/** 边框宽度 */
	public void setWeight(String weight) {
		this.weight = weight;
	}

	/** 列宽集合　如100,200,50,80 */
	public void setCols(String cols) {
		this.cols = cols;
	}

	/** 行高 如10,20,30 */
	public void setRows(String rows) {
		this.rows = rows;
	}

	/**
	 * 设置行自动增长
	 * 
	 * @param max
	 *            增长的上限 0为不限定
	 */
	public void setRowAuto(int max) {
		this.autoRowMax = max;
		this.rows = ROW_AUTO;
	}

	/** 表格第一行标题填充色 */
	public void setBackfillColor(String backfillColor) {
		this.backfillColor = backfillColor;
	}

	/** 奇数行填充色 */
	public void setSinglefillColor(String singlefillColor) {
		this.singlefillColor = singlefillColor;
	}

	/** 偶数行填充色 */
	public void setDoublefillColor(String doublefillColor) {
		this.doublefillColor = doublefillColor;
	}

	/** 表格上方说明 */
	public void setUpCompare(String upCompare) {
		this.upCompare = upCompare;
	}

	/** 表格下方说明 */
	public void setDownCompare(String downCompare) {
		this.downCompare = downCompare;
	}

	/** 表格总标题 */
	public void setTitle(String title) {
		this.title = title;
	}

	/** 下方的 注: */
	public void setNote(String note) {
		this.note = note;
	}

	/** 表格数据集合 */
	public void setLsExcelTable(List<List<String>> lsExcelTable) {
		this.lsExcelTable = lsExcelTable;
	}

	/**
	 * 取得克隆的对象
	 */
	public XdocTable getClone() {
		try {
			return (XdocTable) this.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
}
