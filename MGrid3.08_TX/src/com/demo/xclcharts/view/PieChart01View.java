/**
 * Copyright 2014  XCL-Charts
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 	
 * @Project XCL-Charts 
 * @Description Android鍥捐〃鍩虹被搴�
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 * @Copyright Copyright (c) 2014 XCL-Charts (www.xclcharts.com)
 * @license http://www.apache.org/licenses/  Apache v2 License
 * @version 1.0
 */
package com.demo.xclcharts.view;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.xclcharts.chart.PieChart;
import org.xclcharts.chart.PieData;
import org.xclcharts.event.click.ArcPosition;
import org.xclcharts.renderer.XChart;
import org.xclcharts.renderer.XEnum;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

/**
 * @ClassName PieChart01View
 * @Description  楗煎浘鐨勪緥瀛�
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 */
public class PieChart01View extends TouchView implements Runnable{
	
	private String TAG = "PieChart01View";
	private PieChart chart = new PieChart();	
	private LinkedList<PieData> chartData = new LinkedList<PieData>();

	public PieChart01View(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView();
	}
	
	public PieChart01View(Context context, AttributeSet attrs){   
        	super(context, attrs);   
        	initView();
	 }
	 
	 public PieChart01View(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			initView();
	 }
	 
	 private void initView()
	 {
		 	chartDataSet();	
			chartRender();
			new Thread(this).start();
	 }
	 	 	
	@Override  
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {  
        super.onSizeChanged(w, h, oldw, oldh);  
       //鍥炬墍鍗犺寖鍥村ぇ灏�
        chart.setChartRange(w,h);
    }  	
	
	
	private void chartRender()
	{
		try {					
			
			//璁剧疆缁樺浘鍖洪粯璁ょ缉杩沺x鍊�
			int [] ltrb = getPieDefaultSpadding();
			chart.setPadding(ltrb[0], ltrb[1], ltrb[2], ltrb[3]);			
			
			//璁剧疆璧峰鍋忕Щ瑙掑害(鍗崇涓�涓墖鍖轰粠鍝釜瑙掑害寮�濮嬬粯鍒�)
			//chart.setInitialAngle(90);	
			
			//鏍囩鏄剧ず(闅愯棌锛屾樉绀哄湪涓棿锛屾樉绀哄湪鎵囧尯澶栭潰)
			chart.setLabelPosition(XEnum.SliceLabelPosition.INNER);
			chart.getLabelPaint().setColor(Color.WHITE);
			
			//鏍囬
			//chart.setTitle("楗煎浘-Pie Chart");
		//	chart.addSubtitle("(XCL-Charts Demo)");
			//chart.setTitleVerticalAlign(XEnum.VerticalAlign.BOTTOM);				
			
			//鏄剧ず鍥句緥
			chart.getPlotLegend().showLegend();
			//chart.getPlotLegend().getLegendLabelPaint().setTextSize(22);
			//chart.showBorder();	
			
			//chart.setDataSource(chartData);
			
			//婵�娲荤偣鍑荤洃鍚�
			//chart.ActiveListenItemClick();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		}
	}
	
	/*
	private String percent(double dle){
		NumberFormat nf=NumberFormat.getPercentInstance();
		nf.setMinimumFractionDigits(0);
		return nf.format(dle);
	}
	private double towDigits(double dle){
		BigDecimal bg = new BigDecimal(dle);
		return bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

	}
	*/
	
	
	private void chartDataSet()
	{
		/*
		//璁剧疆鍥捐〃鏁版嵁婧�		
		chartData.add(new PieData("HP","20%",20,(int)Color.rgb(155, 187, 90)));
		chartData.add(new PieData("IBM","30%",30,(int)Color.rgb(191, 79, 75),false));
		chartData.add(new PieData("DELL","10%",10,(int)Color.rgb(242, 167, 69)));
		//灏嗘姣斾緥鍧楃獊鍑烘樉绀�
		chartData.add(new PieData("EMC","40%",40,(int)Color.rgb(60, 173, 213),false));
		*/

		chartData.add(new PieData("closed","9%" ,   (0.09*100),(int)Color.rgb(155, 187, 90)));
		chartData.add(new PieData("inspect","3%" ,   (0.03*100),(int)Color.rgb(191, 79, 75)));
		chartData.add(new PieData("open","76%" ,  (0.76*100),(int)Color.rgb(242, 167, 69)));
		chartData.add(new PieData("workdone","6%" , (0.06*100),(int)Color.rgb(60, 173, 213)));
		chartData.add(new PieData("dispute","6%" ,  (0.06*100),(int)Color.rgb(90, 79, 88)));
	
		/*
		chartData.add(new PieData("closed","7%" ,   (0.07*100),(int)Color.rgb(155, 187, 90)));
		chartData.add(new PieData("inspect","19%" ,   (0.19*100),(int)Color.rgb(191, 79, 75)));
		chartData.add(new PieData("open","74%" ,  (0.74*100),(int)Color.rgb(242, 167, 69)));
	
		
		chartData.add(new PieData("closed","7%" ,   (0.07*100),(int)Color.rgb(155, 187, 90)));
		chartData.add(new PieData("inspect","18%" ,   (0.18*100),(int)Color.rgb(191, 79, 75)));
		chartData.add(new PieData("open","74%" ,  (0.74*100),(int)Color.rgb(242, 167, 69)));
		
		chartData.add(new PieData("closed","36%" ,   (0.36*100),(int)Color.rgb(155, 187, 90)));
		chartData.add(new PieData("inspect","16%" ,   (0.16*100),(int)Color.rgb(191, 79, 75)));
		chartData.add(new PieData("open","49%" ,  (0.49*100),(int)Color.rgb(242, 167, 69)));
		*/
	}

	@Override
    public void render(Canvas canvas) {
        try{
            chart.render(canvas);
        } catch (Exception e){
        	Log.e(TAG, e.toString());
        }
    }

	@Override
	public List<XChart> bindChart() {
		// TODO Auto-generated method stub		
		List<XChart> lst = new ArrayList<XChart>();
		lst.add(chart);		
		return lst;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {          
         	chartAnimation();         	
         }
         catch(Exception e) {
             Thread.currentThread().interrupt();
         }  
	}
	
	private void chartAnimation()
	{
		  try {       
			 
	          	for(int i=0;i< chartData.size() ;i++)
	          	{
	          		Thread.sleep(100);
	          		LinkedList<PieData> animationData = new LinkedList<PieData>();
	        
	          		int sum = 0;
	          		for(int j=0;j<=i;j++)
	          		{            			            			
	          			animationData.add(chartData.get(j));
	          			sum += chartData.get(j).getPercentage();
	          		}   		          		
	          				          				          		
	          		animationData.add(new PieData("","", 100 - sum,(int)Color.argb(1, 0, 0, 0)));		          		
	          		chart.setDataSource(animationData);
	          		
	          		//婵�娲荤偣鍑荤洃鍚�
	    			if(chartData.size() - 1 == i)chart.ActiveListenItemClick();
	    			
	          		postInvalidate();            				          	          	
	          }
			  
          }
          catch(Exception e) {
              Thread.currentThread().interrupt();
          }       
		  
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub		
		super.onTouchEvent(event);		
		if(event.getAction() == MotionEvent.ACTION_UP) 
		{						
			if(chart.isPlotClickArea(event.getX(),event.getY()))
			{											
				triggerClick(event.getX(),event.getY());					
			}
		}
		return true;
	}
	

	//瑙﹀彂鐩戝惉
	private void triggerClick(float x,float y)
	{	
		
		ArcPosition record = chart.getPositionRecord(x,y);			
		if( null == record) return;
		
		PieData pData = chartData.get(record.getDataID());											
		Toast.makeText(this.getContext(),								
				" key:" +  pData.getKey() +
				" Label:" + pData.getLabel() ,
				Toast.LENGTH_SHORT).show();
		
	}
	
	
}
