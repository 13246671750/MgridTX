package com.sg.uis;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import com.mgrid.data.DataGetter;
import com.mgrid.data.EquipmentDataModel.Signal;
import com.mgrid.main.MGridActivity;
import com.mgrid.main.MainWindow;
import com.sg.common.IObject;
import com.sg.common.UtExpressionParser;
import com.sg.common.UtTable;
import com.sg.common.UtTableAdapter;
import com.sg.common.UtExpressionParser.stBindingExpression;
import com.sg.common.UtExpressionParser.stExpression;

import data_model.ipc_history_signal;
import data_model.local_his_signal;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

/** 历史信号 */
//信号历史数据 SaveEquipt
//author :fjw0312
//time:2015 11 02
public class SaveEquipt extends UtTable implements IObject {

	//方便中英文切换
	private String DeviceName;
	private String SignalName;
	private String Value;
	private String Unit;
	private String ValueType;
	private String AlarmSeverity;
	private String AcquisitionTime;
	
	private String DeviceList;
	private String SetTime;
	private String PreveDay;
	private String NextDay;
	private String Receive;
	
	public SaveEquipt(Context context) {
		super(context);
		m_rBBox = new Rect();
		

		if(MGridActivity.whatLanguage)
		{
		    DeviceName="设备名称";
		    SignalName="信号名称";
		    Value="数值";
		    Unit="单位";
		    ValueType="数值类型";
		    AlarmSeverity="告警等级";
		    AcquisitionTime="采集时间";
			
			DeviceList="  设备↓   ";
			SetTime="设置日期";
			PreveDay="前一天";
			NextDay="后一天";
			Receive="  获取   ";
		}
		else
		{
			DeviceName="DeviceName";
			SignalName="SignalName";
			Value="Value";
			Unit="Numericalsignal";
			ValueType="ValueType";
			AlarmSeverity="AlarmSeverity";
			AcquisitionTime="AcquisitionTime";
			
			DeviceList="  Device↓   ";
			SetTime="SetTime";
			PreveDay="PreveDay";
			NextDay="NextDay";
			Receive="  Receive   ";
		}
		
		//标头标题
		lstTitles = new ArrayList<String>();
		lstTitles.add(DeviceName);
		lstTitles.add(SignalName);
		lstTitles.add(Value);
		lstTitles.add(Unit);		
		lstTitles.add(ValueType);
		lstTitles.add(AlarmSeverity);
		lstTitles.add(AcquisitionTime);
		//信号名显示text	
		view_text = new TextView(context);
		view_text.setTextColor(Color.BLACK);
		view_text.setText(DeviceList); //  Equiptment↓
		view_text.setTextSize(16);
		view_text.setGravity(Gravity.CENTER);
		view_text.setBackgroundColor(Color.argb(100, 100, 100, 100));
		
		//日期选择button
		view_timeButton = new Button(context);
		view_timeButton.setText(SetTime);   // Set Time
		view_timeButton.setTextColor(Color.BLACK);
		view_timeButton.setTextSize(16);
		view_timeButton.setPadding(2, 2, 2, 2);
		view_timeButton.setOnClickListener(l);//设置该控件的监听	
		//前一天button
		view_PerveDay = new Button(context);	
		view_PerveDay.setText(PreveDay);  // PreveDay
		view_PerveDay.setTextColor(Color.BLACK);
		view_PerveDay.setTextSize(16);
		view_PerveDay.setPadding(2, 2, 2, 2);		
		view_PerveDay.setOnClickListener(l);//设置该控件的监听	
		//后一天button
		view_NextDay = new Button(context);	
		view_NextDay.setText(NextDay);  // NextDay
		view_NextDay.setTextColor(Color.BLACK);
		view_NextDay.setTextSize(16);	
		view_NextDay.setPadding(2, 2, 2, 2);
		view_NextDay.setOnClickListener(l);//设置该控件的监听	
		//接收receive
		view_Receive = new Button(context);		
		view_Receive.setText(Receive); // Receive
		view_Receive.setTextColor(Color.BLACK);
		view_Receive.setTextSize(16);
		view_Receive.setPadding(2, 2, 2, 2);		
		view_Receive.setOnClickListener(l);	//设置该控件的监听	
		
	
		calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		dialog = new DatePickerDialog(context, new OnDateSetListener() {			
			@Override
			public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub		
				num = 0;  

			}
		}, year, month, day);
		
		
		//信号名选择spinner
		view_EquiptSpinner = new Spinner(context);//信号下拉列表控件
		adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item); 
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		view_EquiptSpinner.setAdapter(adapter);
		adapter.add(DeviceList);	
		view_EquiptSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(
                    AdapterView<?> parent, View view, int position, long id) {
            	parse_expression(); //解析下拉列表成员
 
            }

            public void onNothingSelected(AdapterView<?> parent) {
            	
            }
		});
		
		
		lstContends = new ArrayList<List<String>>();

		map_EquiptNameList = new HashMap<String,String>();  //<信号名字,设备id-信号id>

	}
	
	//监听器 view_Receive
		private OnClickListener l = new OnClickListener() {			
					@Override
					public void onClick(View arg0) {
//					
						// TODO Auto-generated method stub
						
						
						//获取设置的日期
						int set_year = dialog.getDatePicker().getYear();
						int set_month = dialog.getDatePicker().getMonth()+1;
						int set_day = dialog.getDatePicker().getDayOfMonth();

						
						//判断哪一个监听器的	
						if(arg0 == view_timeButton){
							dialog.show();  //弹出日期对话框
							flag = 1;	
							num = 0;
							return;
						}
						else if(arg0 == view_Receive){
							num = 0;
//						
						}
						else if(arg0 == view_NextDay){
//							
							num++;	
//						
							set_day = set_day + num; //天数加一天； num有正负之分
							//判断不超过今天日期
							long now_time = java.lang.System.currentTimeMillis();
							SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//时间格式转换
							Date date = new Date(now_time);
							String sampletime = formatter.format(date);
							 String now_year = sampletime.substring(0, 4);
							 String now_month = sampletime.substring(5, 7);
							 String now_day = sampletime.substring(8, 10);
							 int int_now_year = Integer.valueOf(now_year);
							 int int_now_month = Integer.valueOf(now_month);
							 int int_now_day = Integer.valueOf(now_day);							 
							//月末判断
							if(set_day > 31){
								set_day = set_day-31;
								set_month++;
								if(set_month>12){
									set_month = 1;
									set_year++;
								}
							}
							if(set_day < 1){
								set_day = set_day+31;
								set_month--;
								if(set_month<1){
									set_month = 12;
									set_year--;
								}
							}
							if((set_year==int_now_year)&&(set_month==int_now_month)&&(set_day > int_now_day)){								 
								 set_day = int_now_day;
								 num--;
							 }
//						
						
						}
						else if(arg0 == view_PerveDay){
//							
					
							num--;  //num有正负之分
							set_day = set_day + num; //天数加1天
							if(set_day < 1){
								set_day = 31+set_day;
								set_month--;
								if(set_month<1){
									set_month = 12;
									set_year--;
								}
							}if(set_day > 31){
								set_day = set_day-31;
								set_month++;
								if(set_month>12){
									set_month = 1;
									set_year++;
								}
							}
							
						}
						//处理月份日期字符格式
						String str_day;
						String str_nomth;
						if(set_day<10)
						{
							str_day = "0"+String.valueOf(set_day);
						}else{
							str_day = String.valueOf(set_day);
						}
						if(set_month<10)
						{
							str_nomth = "0"+String.valueOf(set_month);
						}else{
							str_nomth = String.valueOf(set_month);
						}
						get_day = String.valueOf(set_year)+"-"+str_nomth+"-"+str_day;
						
			//			get_day = String.valueOf(set_year)+String.valueOf(set_month)+String.valueOf(set_day);
//					
						if("".equals(get_day)) return;
						
						//显示所选择的设备的名字						
						String equipt_name = (String) view_EquiptSpinner.getSelectedItem();
//					
						view_text.setText(equipt_name);	
						if("Equiptment↓".equals(equipt_name))  return;
						str_EquiptId = map_EquiptNameList.get(equipt_name);
//					
											
						//设置控件需要更新标识变量
						m_bneedupdate = true;   									
			}
		};

	@Override
	public void doLayout(boolean bool, int l, int t, int r, int b) {
		if (m_rRenderWindow == null)
			return;
		int nX = l + (int) (((float)m_nPosX / (float)MainWindow.FORM_WIDTH) * (r-l));
		int nY = t + (int) (((float)m_nPosY / (float)MainWindow.FORM_HEIGHT) * (b-t));
		int nWidth = (int) (((float)(m_nWidth) / (float)MainWindow.FORM_WIDTH) * (r-l));
		int nHeight = (int) (((float)(m_nHeight) / (float)MainWindow.FORM_HEIGHT) * (b-t));

		m_rBBox.left = nX;
		m_rBBox.top = nY;
		m_rBBox.right = nX+nWidth;
		m_rBBox.bottom = nY+nHeight;
		if (m_rRenderWindow.isLayoutVisible(m_rBBox)) {
			notifyTableLayoutChange(nX, nY, nX+nWidth, nY+nHeight);
			
			for (int i = 0; i < m_title.length; ++i)
				m_title[i].layout(nX + i * nWidth / m_title.length, nY-18, nX + i * nWidth / m_title.length + nWidth / m_title.length, nY);
	
			//绘制view_button的底板空间	
			int pv = nWidth/5;
			view_text.layout(nX, nY-40, nX+pv, nY-14);
			view_EquiptSpinner.layout(nX, nY-42, nX+pv, nY-12);
			view_timeButton.layout(nX+pv+20, nY-42, nX+2*pv, nY-12);
			view_NextDay.layout(nX+2*pv+20, nY-42, nX+3*pv, nY-12);
			view_PerveDay.layout(nX+3*pv+20, nY-42, nX+4*pv, nY-12);
			view_Receive.layout(nX+4*pv+20, nY-42, nX+5*pv, nY-12);			
		}
	}
	
	public void onDraw(Canvas canvas) {
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;
//		Log.e("SaveEquipt-onDraw", "into onDraw!");
		super.onDraw(canvas);
	}
	
	public View getView() {
		return this;
	}
	
	public int getZIndex()
	{
		return m_nZIndex;
	}
	
	@Override
	public void addToRenderWindow(MainWindow rWin) {
		this.setClickable(true);
		this.setBackgroundColor(m_cBackgroundColor);
		
		m_bUseTitle = false;
		m_title = new TextView[lstTitles.size()];
		for (int i = 0; i < m_title.length; i++)
		{
			m_title[i] = new TextView(getContext());
			//m_title[i].setTextColor(Color.BLACK);
			//m_title[i].setTextSize(25);
			//m_title[i].setBackgroundColor(Color.GRAY);
			m_title[i].setGravity(Gravity.CENTER);
			m_title[i].setText(lstTitles.get(i));
			rWin.addView(m_title[i]);
		}		
		m_rRenderWindow = rWin;
		rWin.addView(this);
		//view_button画布添加到窗口
		rWin.addView(view_Receive);		
		rWin.addView(view_NextDay);	
		rWin.addView(view_PerveDay);
		rWin.addView(view_text);		
		rWin.addView(view_EquiptSpinner);	
		rWin.addView(view_timeButton);	
	
	}

	@Override
	public void removeFromRenderWindow(MainWindow rWin) {
		
		rWin.removeView(this);
		//view_button画布从到窗口去除
		rWin.removeView(view_Receive);
		rWin.removeView(view_NextDay);
		rWin.removeView(view_PerveDay);
		rWin.removeView(view_text);
		rWin.removeView(view_EquiptSpinner);
		rWin.removeView(view_timeButton);
	}

	public void parseProperties(String strName, String strValue, String strResFolder) {
		if ("ZIndex".equals(strName)) {
       	 	m_nZIndex = Integer.parseInt(strValue);
       	    if (MainWindow.MAXZINDEX < m_nZIndex) MainWindow.MAXZINDEX = m_nZIndex;
        }
        else if ("Location".equals(strName)) {
       	 	String[] arrStr = strValue.split(",");
       	 	m_nPosX = Integer.parseInt(arrStr[0]);
       	 	m_nPosY = Integer.parseInt(arrStr[1]);
       	 	
       	 	// 设定列表坐标参数
			m_nLeft = m_nPosX;
			m_nTop  = m_nPosY;
			m_nRight  = m_nLeft + m_nTableWidth;
			m_nBottom = m_nTop + m_nTableHeight;
        }
        else if ("Size".equals(strName)) {
       	 	String[] arrSize = strValue.split(",");
       	 	m_nWidth = Integer.parseInt(arrSize[0]);
       	 	m_nHeight = Integer.parseInt(arrSize[1]);

       	 	// 设定列表坐标参数
			m_nTableWidth  = m_nWidth;
			m_nTableHeight = m_nHeight;
			m_nRight  = m_nLeft + m_nTableWidth;
			m_nBottom = m_nTop + m_nTableHeight;
        }
        else if ("Alpha".equals(strName)) {
       	 	m_fAlpha = Float.parseFloat(strValue);
        }
        else if ("Expression".equals(strName)){
        	m_strExpression = strValue;
  //      	parse_expression();
        }
        else if ("RadioButtonColor".equals(strName)) {
        	m_cRadioButtonColor = Color.parseColor(strValue);
        }
        else if ("ForeColor".equals(strName)) {
        	m_cForeColor = Color.parseColor(strValue);
        	this.setFontColor(m_cForeColor);
        }
        else if ("BackgroundColor".equals(strName)) {
        	m_cBackgroundColor = Color.parseColor(strValue);
        	this.setBackgroundColor(m_cBackgroundColor);
        }
        else if ("BorderColor".equals(strName)) {
        	m_cBorderColor = Color.parseColor(strValue);
        }
        else if ("OddRowBackground".equals(strName)) {
        	m_cOddRowBackground = Color.parseColor(strValue);
        }
        else if ("EvenRowBackground".equals(strName)) {
        	m_cEvenRowBackground = Color.parseColor(strValue);
        }
        else if ("SaveTime".equals(strName)) {
        	save_time = Integer.parseInt(strValue);
        	save_time = save_time*60*60; //对输入参数的单位为h
        }
	}

	@Override
	public void initFinished()
	{
	}

	public String getBindingExpression() {
		return m_strExpression;
	}
	
	public void setUniqueID(String strID) {
		m_strID = strID;
	}
	public void setType(String strType) {
		m_strType = strType;
	}
	
	public String getUniqueID() {
		return m_strID;
	}
	
	public String getType() {
		return m_strType;
	}
	
	@Override
	public void updateWidget()
	{
		update();
	}

	@Override
	public boolean updateValue()  //由于更新不给力在这里要做更新处理 fjw notice
	{
		m_bneedupdate = false;  //如果为真，表示数据不根据数据更新时时刷界面
//	
		//获取历史数据设备列表      多个设备的历史数据列表 列表<设备表<历史信号结构体>>
		List<local_his_signal> his_sig_list = new ArrayList<local_his_signal>();
		if(m_rRenderWindow.m_oShareObject.m_mapSaveEquipt == null) return false;
		his_sig_list = m_rRenderWindow.m_oShareObject.m_mapSaveEquipt.get(this.getUniqueID());
		//遍历第一个列表 获得第一个设备历史信号	
		if (his_sig_list == null)
		{
			
				return false;
		}
	
		//遍历第一个设备列表 将每个历史信号的结构体
		lstContends.clear(); //清楚页面的以前数据 行信号
		Iterator<local_his_signal> iterator_his = his_sig_list.iterator();
		while(iterator_his.hasNext()){
			local_his_signal his_sig = iterator_his.next();
		    	List<String> lstRow_his = new ArrayList<String>();
		    	
		    	lstRow_his.add(his_sig.equip_name);
		    	lstRow_his.add(his_sig.name);
		    	float f=Float.parseFloat(his_sig.value);
		    	int ii=(int) (f*100);
		    	f=(float) (ii/100.0);
		    	lstRow_his.add(f+"");   //这一坨操作就是为了保留小数点后两位
		    
		    	lstRow_his.add(his_sig.unit);
		    	lstRow_his.add(his_sig.value_type);
		    	lstRow_his.add(his_sig.severity);
		    	lstRow_his.add(his_sig.freshtime);
//		    	fjw_signal.addAll(lstRow_his);
		    	lstContends.add(lstRow_his);
		    	
		 }
		updateContends(lstTitles, lstContends);
		updateContends(lstTitles, lstContends);
//		fjw_signal.clear();
		his_sig_list.clear();

		return true;
	}

	//解析出控件表达式，返回控件表达式类
	@SuppressWarnings({ "unchecked", "static-access" })
	public boolean parse_expression(){
			if("".equals(m_strExpression)) return false;
			String Mathstr=UtExpressionParser.getInstance().getMathExpression(m_strExpression);
			ArrayList<Integer> list=new ArrayList<Integer>();
			String[] strCExp=Mathstr.split("\\|");
		 	for(String str:strCExp)
			{
				String[] strResult=str.split("\\]");
				String[] strResult1=strResult[0].split("\\:");
				list.add( Integer.parseInt(strResult1[1]));
			}
			for(int id:list)
			{
				String str_equiptName = DataGetter.getEquipmentName(id);
				map_EquiptNameList.put(str_equiptName, String.valueOf(id));
				adapter.add(str_equiptName);
			}
			
//			stExpression oMathExpress = UtExpressionParser.getInstance().parseExpression(m_strExpression);
//			if (oMathExpress != null) {		
//				//遍历控件表达式各个数据单元表达式类
//				Iterator<HashMap.Entry<String, stBindingExpression>> it = oMathExpress.mapObjectExpress.entrySet().iterator();
//			
//				ArrayList<Integer> list=new ArrayList<Integer>();
//				while(it.hasNext())
//				{
//					stBindingExpression oBindingExpression = it.next().getValue();
//				
//					int equipt_id = oBindingExpression.nEquipId;
//					System.out.println("equipt_id:"+equipt_id);
//					
//					list.add(equipt_id);
//				//	Log.e("SaveEquipt-parse_expression>equipt_id:",String.valueOf(equipt_id));
//		//			int signal_id = oBindingExpression.nSignalId;
//		//			int temp_id = oBindingExpression.nTemplateId;
//
//		//			String str_signalName = DataGetter.getSignalName(equipt_id, signal_id);
//	//			
//			
//				}
//				Collections.sort(list);
//				for(int id:list)
//				{
//					
//					String str_equiptName = DataGetter.getEquipmentName(id);
//					map_EquiptNameList.put(str_equiptName, String.valueOf(id));
//					adapter.add(str_equiptName);
//				}
//				
//			}
		 return true;
		}

	@Override
    public boolean needupdate()
    {
	    return m_bneedupdate;
    }
	
	@Override
    public void needupdate(boolean bNeedUpdate)
    {
	    m_bneedupdate = bNeedUpdate;
    }
	
	public Rect getBBox() {
		return m_rBBox;
	}
	
// params:
	String m_strID = "";
	String m_strType = "";  
    int m_nZIndex = 15;
	int m_nPosX = 40;
	int m_nPosY = 604;
	int m_nWidth = 277;
	int m_nHeight = 152;
	float m_fAlpha = 0.8f;
	String m_strExpression = "Binding{[Equip[Equip:2]]}";
	int m_cRadioButtonColor = 0xFFFF8000;
	int m_cForeColor = 0xFF00FF00;
	int m_cBackgroundColor = 0xFF000000;
	int m_cBorderColor = 0xFFFFFFFF;
	
	// radio buttons
	//RadioButton[] m_oRadioButtons;
	
	// 固定标题栏
	TextView[] m_title;
	TextView view_text;		            //信号名显示text		
	Spinner view_EquiptSpinner = null; 		//信号名选择spinner
	Button  view_timeButton;		        //日期选择button
	Button  view_PerveDay;		            //前一天button
	Button  view_NextDay;		            //后一天button
	Button  view_Receive;		            //接收receive
	
	private HashMap<String,String> map_EquiptNameList = null;  //<设备名，设备id>
	private  ArrayAdapter<String> adapter = null;
	private  DatePickerDialog  dialog;  //日期对话框选择应用
	private int year,month,day;   //对话框显示的年月日变量
	private Calendar calendar;
	private int flag = 0;
	private int num = 0; //加减按纽加减数	
	public static String str_EquiptId = ""; //所需要的设备id字符串	
	public static String get_day="";   //所要获取数据的日期
	public static int save_time = 60*60*2;   //saveEquipt采集时间   2h
	
	MainWindow m_rRenderWindow = null;
	Rect m_rBBox = null;
	
	public boolean m_bNeedINIT = true;
	public boolean m_bneedupdate = false;
	
	// TODO: 临时代替数据
	boolean m_needsort = true;
//	ArrayList<String> m_sortedarray = null;
	List<String> lstTitles = null;
	List<List<String>> lstContends = null;
	private Paint  mPaint = new Paint();  //注意以后变量的定义一定要赋予空间
//	List<String> fjw_signal = null;
}
