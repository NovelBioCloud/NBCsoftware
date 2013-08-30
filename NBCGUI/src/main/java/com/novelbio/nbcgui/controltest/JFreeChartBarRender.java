package com.novelbio.nbcgui.controltest;

import java.awt.Color;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.chart.renderer.category.BarRenderer;

/** 用来渲染jfreechart的bar图的 */
public class JFreeChartBarRender extends BarRenderer {  
	
	
    private static final long serialVersionUID = 784630226449158436L;  
    private Paint[] colors;  
    //初始化柱子颜色  
    private String[] colorValues = { "#AFD8F8", "#F6BD0F", "#8BBA00", "#FF8E46", "#008E8E", "#D64646" };  
    Map<Integer, Paint> mapNum2Paint = new HashMap<Integer, Paint>();
    
    public void setLsBarColors(List<BarColor> lsBarColors) {
		mapNum2Paint.clear();
		for (BarColor barColor : lsBarColors) {
			for (Integer num : barColor.lsBarNum) {
				mapNum2Paint.put(num, barColor.paint);
			}
		}
	}
    
    /** 输入的barColor必须已经设定好了column信息 */
    public void addBarColor(BarColor barColor) {
    	for (Integer num : barColor.lsBarNum) {
			mapNum2Paint.put(num, barColor.paint);
		}
    }
  
    /** 不同的柱子用不同的颜色 */
    public Paint getItemPaint(int i, int j) {  
        return mapNum2Paint.get(j);
    }
    
    
    public static class BarColor {
    	List<Integer> lsBarNum = new ArrayList<Integer>();
    	Paint paint = Color.BLACK;
     	
    	/** 设定该系列bar的颜色，默认黑色 */
    	public BarColor(Paint paint) {
			this.paint = paint;
		}
    	
    	public void setLsBarNum(List<Integer> lsBarNum) {
			this.lsBarNum = lsBarNum;
		}
    	/** 设定要渲染哪几个bar */
    	public void addBarNum(int barNum) {
    		lsBarNum.add(barNum);
    	}
   
    	
    }
    
}

