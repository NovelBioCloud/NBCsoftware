package com.novelbio.nbcgui.controltest;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarPainter;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleInsets;

import com.novelbio.analysis.annotation.functiontest.StatisticTestResult;
import com.novelbio.base.plot.PlotBar;

public class GoPathBarPlot {
	/** 画的柱状图的柱的数量上限 */
	private static final int barMaxNumVertical = 25;
	/** 画的柱状图的柱的数量上限 */
	private static final int barMaxNumHorizon = 15;
	/**
	 * 根据参数画gopath的柱状图
	 * 
	 * @return　是否成功
	 */
	public static BufferedImage drawLog2PvaluePicture(List<StatisticTestResult> lsTestResults, String title) throws Exception {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (int i = 0; i < barMaxNumHorizon; i++) {
			if (i < lsTestResults.size())
				dataset.addValue(lsTestResults.get(i).getLog2Pnegative(), "", lsTestResults.get(i).getItemTerm());
		}
		JFreeChart chart = ChartFactory.createBarChart(title, null, "-Log2P", dataset, PlotOrientation.HORIZONTAL, false, false, false);
		// chart.getRenderingHints().put(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		// 设置图标题的字体
		chart.getTitle().setFont(new Font("黑体", Font.BOLD, 30));
		/** title永远是居中的，但是我们想要让title靠上或者靠边怎么办呢，
		 * 就要将title包装成一个矩形，然后jfreechart会将这个矩形居中
		 * 所以第一个就是矩形的上边，这样上边设置越大，title与上边框的距离就越大
		 * 第二个是左边，左边设置越大，title与左边界的距离也就越大
		 * 第三个是下边，下边越大，title与下边图片的距离也越大
		 */
		chart.getTitle().setPadding(20,0,20,0);
		// TextTitle title = new TextTitle("直方图测试");
		// 设置图例中的字体
		// LegendTitle legend = chart.getLegend();
		// legend.setItemFont(new Font("宋体", Font.BOLD, 16));
		// chart.setBorderPaint(Color.white);
		chart.setBorderVisible(true);
		// chart.setBackgroundPaint(Color.WHITE);
		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		plot.setBackgroundPaint(Color.white);
		plot.setOutlinePaint(Color.WHITE); // 设置绘图面板外边的填充颜色
		// CategoryPlot plot = (CategoryPlot) chart.getCategoryPlot();
		// plot.setRenderer(render);//使用我们设计的效果
		CategoryAxis cateaxis = plot.getDomainAxis();
		// BarRenderer render = new BarRenderer();
		// render.setBaseFillPaint(Color.pink);
		// plot.setRenderer(render);
		
		JFreeChartBarRender renderer = getJfreechartBarRender(lsTestResults);
		// 设置柱子宽度
		renderer.setMaximumBarWidth(0.03);
		renderer.setMinimumBarLength(0.01000000000000001D); // 宽度
		// 设置柱子高度
		renderer.setMinimumBarLength(0.1);
		// 设置柱子类型
		BarPainter barPainter = new StandardBarPainter();
		renderer.setBarPainter(barPainter);
		
//		renderer.setSeriesPaint(0, new Color(51, 102, 153));
		// 是否显示阴影
		renderer.setShadowVisible(false);
		// 阴影颜色
		// renderer1.setShadowPaint(Color.white);
		// 设置柱子边框的渐变色
		// renderer1.setBarPainter(new GradientBarPainter(1,1,1));
		// 设置柱子边框颜色
		// renderer1.setBaseOutlinePaint(Color.BLACK);
		// 设置柱子边框可见
		// renderer1.setDrawBarOutline(true);
		// 设置每个地区所包含的平行柱的之间距离，数值越大则间隔越大，图片大小一定的情况下会影响柱子的宽度，可以为负数
		renderer.setItemMargin(0.4);

		plot.setRenderer(renderer);

		// 设置横轴的标题
		// cateaxis.setLabelFont(new Font("粗体", Font.BOLD, 16));
		// 设置横轴的标尺
		cateaxis.setTickLabelFont(new Font(Font.SERIF, Font.BOLD, 22));
		cateaxis.setMaximumCategoryLabelWidthRatio(0.45f);
		// 让标尺以30度倾斜
//		cateaxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 3.0));
		// 纵轴
		NumberAxis numaxis = (NumberAxis) plot.getRangeAxis();
		numaxis.setTickUnit(new NumberTickUnit(PlotBar.getSpace(numaxis.getRange().getUpperBound(), 10)));
		numaxis.setLabelFont(new Font("宋体", Font.BOLD, 25));
		numaxis.setLabelInsets(new RectangleInsets(0, 500, 10, 0));
		return chart.createBufferedImage(1000, 1000);
		
		
//		FileOutputStream fosPng = null;
//		try {
//			fosPng = new FileOutputStream(picName);
//			ChartUtilities.writeChartAsPNG(fosPng, chart, 1200, 1000);
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//			return false;
//		} finally {
//			try {
//				fosPng.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		return true;
	}
	/**
	 * 根据参数画gopath的柱状图
	 * 
	 * @return　是否成功
	 */
	public static BufferedImage drawEnrichmentPicture(List<StatisticTestResult> lsTestResults, String title) throws Exception {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (int i = 0; i < barMaxNumVertical; i++) {
			if (i < lsTestResults.size())
				dataset.addValue(lsTestResults.get(i).getEnrichment(), "", lsTestResults.get(i).getItemTerm());
		}
		JFreeChart chart = ChartFactory.createBarChart(title, null, "Enrichment", dataset, PlotOrientation.VERTICAL, false, false, false);
		// 设置图标题的字体
		chart.getTitle().setFont(new Font("黑体", Font.BOLD, 30));
		chart.getTitle().setPadding(20,0,20,0);
		chart.setBorderVisible(true);
		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		plot.setBackgroundPaint(Color.white);
		plot.setOutlinePaint(Color.WHITE);
		
		NumberAxis numaxis = (NumberAxis) plot.getRangeAxis();
		numaxis.setLabelFont(new Font("宋体", Font.BOLD, 25));
		numaxis.setTickUnit(new NumberTickUnit(PlotBar.getSpace(numaxis.getRange().getUpperBound(), 10)));
		numaxis.setLabelInsets(new RectangleInsets(60, 15, 0, 30));
		
		CategoryAxis cateaxis = plot.getDomainAxis();
		cateaxis.setTickLabelFont(new Font(Font.SERIF, Font.BOLD, 22));
		cateaxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 3.0));// 让标尺以30度倾斜
		
		BarRenderer renderer =getJfreechartBarRender(lsTestResults);
		// 分类柱子之间的宽度
		renderer.setItemMargin(0.02);
		// 设置柱子宽度
		renderer.setMaximumBarWidth(0.028);
		renderer.setMinimumBarLength(0.01000000000000001D); // 宽度
		// 设置柱子高度
		renderer.setMinimumBarLength(0.1);
		// 设置柱子类型
		BarPainter barPainter = new StandardBarPainter();
		renderer.setBarPainter(barPainter);
		// 是否显示阴影
		renderer.setShadowVisible(false);
		// 设置每个地区所包含的平行柱的之间距离，数值越大则间隔越大，图片大小一定的情况下会影响柱子的宽度，可以为负数
		renderer.setItemMargin(0.4);
		plot.setRenderer(renderer);

		return chart.createBufferedImage(1200, 1000);
		
//		FileOutputStream fosPng = null;
//		try {
//			fosPng = new FileOutputStream(picName);
//			ChartUtilities.writeChartAsPNG(fosPng, chart, 1000, 1000);
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//		} finally {
//			try {
//				fosPng.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		return true;
	}
	/**
	 * 根据fdr的值，来对bar进行染色
	 * @param lsTestResults
	 * @return
	 */
	private static JFreeChartBarRender getJfreechartBarRender(List<StatisticTestResult> lsTestResults) {
		JFreeChartBarRender jFreeChartBarRender = new JFreeChartBarRender();
		JFreeChartBarRender.BarColor barColorRed = new JFreeChartBarRender.BarColor(new Color(228, 55, 18));
		JFreeChartBarRender.BarColor barColorBlue = new JFreeChartBarRender.BarColor(new Color(51, 102, 153));

		int i = 0;
		for (StatisticTestResult statisticTestResult : lsTestResults) {
			if (statisticTestResult.getPvalue() < 0.05) {
				barColorRed.addBarNum(i);
			} else {
				barColorBlue.addBarNum(i);
			}
			i++;
		}
		
		jFreeChartBarRender.addBarColor(barColorRed);
		jFreeChartBarRender.addBarColor(barColorBlue);
		return jFreeChartBarRender;
	}

}
