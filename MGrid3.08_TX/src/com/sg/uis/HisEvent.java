package com.sg.uis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.mgrid.data.DataGetter;
import com.mgrid.main.MGridActivity;
import com.mgrid.main.MainWindow;
import com.sg.common.IObject;
import com.sg.common.UtExpressionParser;
import com.sg.common.UtTable;
import comm_service.local_file;

import data_model.local_his_Alarm;
import data_model.local_his_event;

/** 历史告警 */
// 信号告警数据 HisEvent
// author :fjw0312
// time:2016 5 17
public class HisEvent extends UtTable implements IObject {

	// 方便中英文切换
	private String DeviceName;
	private String AlarmName;
	private String AlarmMeaning;
	private String Numericalsignal;
	private String AlarmSeverity;
	private String StartTime;
	private String EndTime;

	private String DeviceList;
	private String SetTime;
	private String PreveDay;
	private String NextDay;
	private String Receive;

	private String AllDevice;
	
	private String logPath="/mgrid/data/Command/0.log";
	private File logFile=new File(logPath);

	public HisEvent(Context context) {
		super(context);
		m_rBBox = new Rect();

		// 设置列表滑屏监听 主要作用：当检测到有滑屏click1=false 停止滑屏后click1=true;
		this.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				
				switch (arg1) {
				case OnScrollListener.SCROLL_STATE_IDLE:
					click1 = true;

					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
					click1 = false;

					break;
				case OnScrollListener.SCROLL_STATE_FLING:
					click1 = false;

					break;
				}
			}

			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {

			}
		});

		if (MGridActivity.whatLanguage) {
			DeviceName = "设备名称";
			AlarmName = "告警名称";
			AlarmMeaning = "告警含义";
			Numericalsignal = "信号数值";
			AlarmSeverity = "告警等级";
			StartTime = "开始时间";
			EndTime = "结束时间";

			DeviceList = "  设备↓   ";
			SetTime = "设置日期";
			PreveDay = "结束时间";
			NextDay = "开始时间";
			Receive = "  获取   ";
			AllDevice = "全部设备";
		} else {
			DeviceName = "DeviceName";
			AlarmName = "AlarmName";
			AlarmMeaning = "AlarmMeaning";
			Numericalsignal = "Numericalsignal";
			AlarmSeverity = "AlarmSeverity";
			StartTime = "StartTime";
			EndTime = "EndTime";

			DeviceList = "  Device↓   ";
			SetTime = "SetTime";
			PreveDay = "PreveDay";
			NextDay = "NextDay";
			Receive = "  Receive   ";

			AllDevice = "AllDevice";
		}

		// 标头标题
		lstTitles = new ArrayList<String>();
		AlarmTitles=new ArrayList<String>();
		lstTitles.add(DeviceName);
		// lstTitles.add("信号名称");
		lstTitles.add(AlarmName);
		lstTitles.add(AlarmMeaning);
		lstTitles.add(Numericalsignal);
		lstTitles.add(AlarmSeverity);
		lstTitles.add(StartTime);
		lstTitles.add(EndTime);

		// 信号名显示text
		view_text = new TextView(context);
		view_text.setTextColor(Color.BLACK);
		view_text.setText(DeviceList); // 变为中文
		view_text.setTextSize(16);
		view_text.setGravity(Gravity.CENTER);
		view_text.setBackgroundColor(Color.argb(100, 100, 100, 100));

		// 日期选择button
		view_timeButton = new Button(context);
		view_timeButton.setText(SetTime); // Set Time
		view_timeButton.setTextColor(Color.BLACK);
		view_timeButton.setTextSize(16);
		view_timeButton.setPadding(2, 2, 2, 2);
		view_timeButton.setOnClickListener(l);// 设置该控件的监听
		// 结束时间button
		view_PerveDay = new Button(context);
		view_PerveDay.setText(PreveDay); // PreveDay
		view_PerveDay.setTextColor(Color.BLACK);
		view_PerveDay.setTextSize(16);
		view_PerveDay.setPadding(2, 2, 2, 2);
		view_PerveDay.setOnClickListener(l);// 设置该控件的监听

		view_Text = new TextView(context);
		view_Text.setText("——");
		view_Text.setTextColor(Color.BLACK);
		view_Text.setTextSize(16);
		view_Text.setPadding(2, 2, 2, 2);

		// 开始时间button
		view_NextDay = new Button(context);
		view_NextDay.setText(NextDay); // NextDay
		view_NextDay.setTextColor(Color.BLACK);
		view_NextDay.setTextSize(16);
		view_NextDay.setPadding(2, 2, 2, 2);
		view_NextDay.setOnClickListener(l);// 设置该控件的监听
		// 接收receive
		view_Receive = new Button(context);
		view_Receive.setText(Receive);
		view_Receive.setTextColor(Color.BLACK);
		view_Receive.setTextSize(16);
		view_Receive.setPadding(2, 2, 2, 2);
		view_Receive.setOnClickListener(l);

		calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		dialog = new DatePickerDialog(context, new OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
			

			}
		}, year, month, day);

		view_EquiptSpinner = new Spinner(context);
		adapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		view_EquiptSpinner.setAdapter(adapter);
		adapter.add(DeviceList);
		adapter.add(AllDevice);
		if(logFile.exists()) {
			adapter.add("二次下电");
			AlarmTitles.add("配置ID");
			AlarmTitles.add("控制开关");
			AlarmTitles.add("告警原因");
			AlarmTitles.add("开始时间");
			AlarmTitles.add("结束时间");
			AlarmTitles.add("是否异常");
			AlarmTitles.add("控制结果");		
		}
		view_EquiptSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						get_equiptList(); // 解析下拉列表成员

					}

					public void onNothingSelected(AdapterView<?> parent) {

					}
				});

		lstContends = new ArrayList<List<String>>();
		lsyLs = new ArrayList<List<String>>();
		lsyLs1 = new ArrayList<List<String>>();
		lsyLs2=new ArrayList<List<String>>();
		map_EquiptNameList = new HashMap<String, String>();

	}

	// 监听器 view_Receive
	private OnClickListener l = new OnClickListener() {

		@Override
		public void onClick(View arg0) {

			set_year = dialog.getDatePicker().getYear();
			set_month = dialog.getDatePicker().getMonth() + 1;
			set_day = dialog.getDatePicker().getDayOfMonth();

			if (arg0 == view_timeButton) {

				dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "设置",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
							
								set_year = dialog.getDatePicker().getYear();
								set_month = dialog.getDatePicker().getMonth() + 1;
								set_day = dialog.getDatePicker()
										.getDayOfMonth();
								
								String set_months, set_days;
								if (set_day < 10) {
									set_days = "0" + String.valueOf(set_day);
								} else {
									set_days = String.valueOf(set_day);
								}
								if (set_month < 10) {
									set_months = "0"
											+ String.valueOf(set_month);
								} else {
									set_months = String.valueOf(set_month);
								}
								view_timeButton.setText(String
										.valueOf(set_year)
										+ "-"
										+ set_months
										+ "-" + set_days);
								view_NextDay.setText(String.valueOf(set_year)
										+ "-" + set_months + "-" + set_days);
								view_PerveDay.setText(String.valueOf(set_year)
										+ "-" + set_months + "-" + set_days);
							}
						});

				dialog.show();
				return;
			}
			if (arg0 == view_NextDay) {
				calendar = Calendar.getInstance();
				dialog_before = new DatePickerDialog(getContext(),
						new OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker arg0, int arg1,
									int arg2, int arg3) {
								

							}
						}, calendar.get(Calendar.YEAR),
						calendar.get(Calendar.MONTH),
						calendar.get(Calendar.DAY_OF_MONTH));

				dialog_before.setButton(DialogInterface.BUTTON_NEGATIVE, "设置",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								
								before_year = dialog_before.getDatePicker()
										.getYear();
								before_month = dialog_before.getDatePicker()
										.getMonth() + 1;
								before_day = dialog_before.getDatePicker()
										.getDayOfMonth();
								
								String before_months, before_days;
								if (before_day < 10) {
									before_days = "0"
											+ String.valueOf(before_day);
								} else {
									before_days = String.valueOf(before_day);
								}
								if (before_month < 10) {
									before_months = "0"
											+ String.valueOf(before_month);
								} else {
									before_months = String
											.valueOf(before_month);
								}
								view_NextDay.setText(String
										.valueOf(before_year)
										+ "-"
										+ before_months + "-" + before_days);
							}
						});

				dialog_before.show();
				return;
			}

			if (arg0 == view_PerveDay) {
				calendar = Calendar.getInstance();
				dialog_after = new DatePickerDialog(getContext(),
						new OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker arg0, int arg1,
									int arg2, int arg3) {
							

							}
						}, calendar.get(Calendar.YEAR),
						calendar.get(Calendar.MONTH),
						calendar.get(Calendar.DAY_OF_MONTH));

				dialog_after.setButton(DialogInterface.BUTTON_NEGATIVE, "设置",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								
								after_year = dialog_after.getDatePicker()
										.getYear();
								after_month = dialog_after.getDatePicker()
										.getMonth() + 1;
								after_day = dialog_after.getDatePicker()
										.getDayOfMonth();
								
								String after_months, after_days;
								if (after_day < 10) {
									after_days = "0"
											+ String.valueOf(after_day);
								} else {
									after_days = String.valueOf(after_day);
								}
								if (after_month < 10) {
									after_months = "0"
											+ String.valueOf(after_month);
								} else {
									after_months = String.valueOf(after_month);
								}
								view_PerveDay.setText(String
										.valueOf(after_year)
										+ "-"
										+ after_months + "-" + after_days);
							}
						});

				dialog_after.show();
				return;
			}

			closeEquiptName = (String) view_EquiptSpinner.getSelectedItem();
			view_text.setText(closeEquiptName);
			if (DeviceList.equals(closeEquiptName))
				return;

			str_EquiptId = map_EquiptNameList.get(closeEquiptName);
			mythread thread = new mythread();
			thread.start();
		}
	};

	// 通知ui刷新线程
	private class mythread extends Thread {

		@Override
		public void run() {
			
			updateValue();
			m_bneedupdate = true;

		}
	}

	private class mythread1 extends Thread {
		public void run() {
			click = false;
			try {
				Thread.sleep(500);
			} catch (Exception e) {

			}
			click = true;
		}
	}

	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		if (click) {
			return true;
		} else { // 当不能点击时 拦截掉触摸事件
			return false;
		}
	}

	@Override
	public void doLayout(boolean bool, int l, int t, int r, int b) {
		if (m_rRenderWindow == null)
			return;
		int nX = l
				+ (int) (((float) m_nPosX / (float) MainWindow.FORM_WIDTH) * (r - l));
		int nY = t
				+ (int) (((float) m_nPosY / (float) MainWindow.FORM_HEIGHT) * (b - t));
		int nWidth = (int) (((float) (m_nWidth) / (float) MainWindow.FORM_WIDTH) * (r - l));
		int nHeight = (int) (((float) (m_nHeight) / (float) MainWindow.FORM_HEIGHT) * (b - t));

		m_rBBox.left = nX;
		m_rBBox.top = nY;
		m_rBBox.right = nX + nWidth;
		m_rBBox.bottom = nY + nHeight;
		if (m_rRenderWindow.isLayoutVisible(m_rBBox)) {
			notifyTableLayoutChange(nX, nY, nX + nWidth, nY + nHeight);

			for (int i = 0; i < m_title.length; ++i){
				m_title[i].layout(nX + i * nWidth / m_title.length, nY - 18,
						nX + i * nWidth / m_title.length + nWidth
								/ m_title.length, nY);
				
			}
			for (int i = 0; i < s_title.length; ++i){
				s_title[i].layout(nX + i * nWidth / s_title.length, nY - 18,
						nX + i * nWidth / s_title.length + nWidth
								/ s_title.length, nY);
				
			}
			

			// 绘制view_button的底板空间
			int pv = nWidth / 5;
			view_text.layout(nX, nY - 40, nX + pv, nY - 14);
			view_EquiptSpinner.layout(nX, nY - 42, nX + pv, nY - 12);

			// view_Receive.layout(nX+4*pv+20, nY-42, nX+5*pv, nY-12);

			view_timeButton.layout(nX + pv + 20, nY - 42, nX + 2 * pv, nY - 12);

			view_NextDay
					.layout(nX + 2 * pv + 20, nY - 42, nX + 3 * pv, nY - 12);

			view_Text.layout(nX + 3 * pv + 5, nY - 42, nX + 3 * pv + 15,
					nY - 12);

			view_PerveDay.layout(nX + 3 * pv + 20, nY - 42, nX + 4 * pv,
					nY - 12);

			view_Receive
					.layout(nX + 4 * pv + 20, nY - 42, nX + 5 * pv, nY - 12);
		}
	}

	public void onDraw(Canvas canvas) {
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;

		super.onDraw(canvas);
	}

	public View getView() {
		return this;
	}

	public int getZIndex() {
		return m_nZIndex;
	}

	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				for (int i = 0; i < m_title.length; i++)
					m_title[i].setVisibility(View.VISIBLE);
				for (int i = 0; i < s_title.length; i++)
					s_title[i].setVisibility(View.GONE);

				break;
			case 1:
				for (int i = 0; i < m_title.length; i++)
					m_title[i].setVisibility(View.GONE);
				for (int i = 0; i < s_title.length; i++)
					s_title[i].setVisibility(View.VISIBLE);

				break;	
			}

			super.handleMessage(msg);
		}
	};
	
	
	
	@Override
	public void addToRenderWindow(MainWindow rWin) {
		this.setClickable(true);
		this.setBackgroundColor(m_cBackgroundColor);

		m_bUseTitle = false;
		m_title = new TextView[lstTitles.size()];
		for (int i = 0; i < m_title.length; i++) {
			m_title[i] = new TextView(getContext());
			// m_title[i].setTextColor(Color.BLACK);
			// m_title[i].setTextSize(25);
			// m_title[i].setBackgroundColor(Color.GRAY);
			m_title[i].setGravity(Gravity.CENTER);
			m_title[i].setText(lstTitles.get(i));
			m_title[i].setVisibility(View.INVISIBLE);
			rWin.addView(m_title[i]);
		}
		s_title = new TextView[AlarmTitles.size()];
		for (int i = 0; i < s_title.length; i++) {
			s_title[i] = new TextView(getContext());
			// m_title[i].setTextColor(Color.BLACK);
			// m_title[i].setTextSize(25);
			// m_title[i].setBackgroundColor(Color.GRAY);
			s_title[i].setGravity(Gravity.CENTER);
			s_title[i].setText(AlarmTitles.get(i));
			s_title[i].setVisibility(View.INVISIBLE);
			rWin.addView(s_title[i]);
		}
		
		m_rRenderWindow = rWin;
		rWin.addView(this);
		// view_button画布添加到窗口
		rWin.addView(view_Receive);
		rWin.addView(view_NextDay);
		rWin.addView(view_PerveDay);
		rWin.addView(view_text);
		rWin.addView(view_EquiptSpinner);
		rWin.addView(view_timeButton);
		rWin.addView(view_Text);

	}

	
	
	
	@Override
	public void removeFromRenderWindow(MainWindow rWin) {

		rWin.removeView(this);
		// view_button画布从到窗口去除
		rWin.removeView(view_Receive);
		rWin.removeView(view_NextDay);
		rWin.removeView(view_PerveDay);
		rWin.removeView(view_text);
		rWin.removeView(view_EquiptSpinner);
		rWin.removeView(view_timeButton);
		rWin.removeView(view_Text);
	}

	public void parseProperties(String strName, String strValue,
			String strResFolder) {
		if ("ZIndex".equals(strName)) {
			m_nZIndex = Integer.parseInt(strValue);
			if (MainWindow.MAXZINDEX < m_nZIndex)
				MainWindow.MAXZINDEX = m_nZIndex;
		} else if ("Location".equals(strName)) {
			String[] arrStr = strValue.split(",");
			m_nPosX = Integer.parseInt(arrStr[0]);
			m_nPosY = Integer.parseInt(arrStr[1]);

			// 设定列表坐标参数
			m_nLeft = m_nPosX;
			m_nTop = m_nPosY;
			m_nRight = m_nLeft + m_nTableWidth;
			m_nBottom = m_nTop + m_nTableHeight;
		} else if ("Size".equals(strName)) {
			String[] arrSize = strValue.split(",");
			m_nWidth = Integer.parseInt(arrSize[0]);
			m_nHeight = Integer.parseInt(arrSize[1]);

			// 设定列表坐标参数
			m_nTableWidth = m_nWidth;
			m_nTableHeight = m_nHeight;
			m_nRight = m_nLeft + m_nTableWidth;
			m_nBottom = m_nTop + m_nTableHeight;
		} else if ("Alpha".equals(strName)) {
			m_fAlpha = Float.parseFloat(strValue);
		} else if ("Expression".equals(strName)) {
			m_strExpression = strValue;
			// parse_expression();
		} else if ("RadioButtonColor".equals(strName)) {
			m_cRadioButtonColor = Color.parseColor(strValue);
		} else if ("ForeColor".equals(strName)) {
			m_cForeColor = Color.parseColor(strValue);
			this.setFontColor(m_cForeColor);
		} else if ("BackgroundColor".equals(strName)) {
			m_cBackgroundColor = Color.parseColor(strValue);
			this.setBackgroundColor(m_cBackgroundColor);
		} else if ("BorderColor".equals(strName)) {
			m_cBorderColor = Color.parseColor(strValue);
		} else if ("OddRowBackground".equals(strName)) {
			m_cOddRowBackground = Color.parseColor(strValue);
		} else if ("EvenRowBackground".equals(strName)) {
			m_cEvenRowBackground = Color.parseColor(strValue);
		}
	}

	@Override
	public void initFinished() {
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
	public void updateWidget() {
		update();
		mythread1 thread1 = new mythread1();
		thread1.start();
	}

	
	@SuppressWarnings({ "resource", "unused" })
	@Override
	public boolean updateValue() // 由于更新不给力在这里要做更新处理 fjw notice
	{

		Hashtable<String, local_his_event> hast_his;
		List<local_his_event> his_event_list;
		List<String> key;
		List<String> key2;
		String after = view_PerveDay.getText().toString();
		String before = view_NextDay.getText().toString();
		int after_num, before_num;
		if (after.length() < 10 || before.length() < 10) {
			after_num = set_year + set_month * 32 + set_day;
			before_num = set_year + set_month * 32 + set_day;
		} else {
			after_num = Integer.parseInt(after.substring(0, 4))
					+ Integer.parseInt(after.substring(5, 7)) * 32
					+ Integer.parseInt(after.substring(8, 10));
			before_num = Integer.parseInt(before.substring(0, 4))
					+ Integer.parseInt(before.substring(5, 7)) * 32
					+ Integer.parseInt(before.substring(8, 10));
		}

		if (!AllDevice.equals(closeEquiptName)&&!"二次下电".equals(closeEquiptName)) {
			handler.sendEmptyMessage(0);
			lstContends.clear(); // 清楚页面的以前数据 行信号
			m_bneedupdate = false; // 如果为真，表示数据不根据数据更新时时刷界面

			his_event_list = new ArrayList<local_his_event>();
			if (m_rRenderWindow.m_oShareObject.m_mapLocalEvent == null) {

				return false;
			}
			his_event_list = m_rRenderWindow.m_oShareObject.m_mapLocalEvent
					.get(this.getUniqueID());

			if (his_event_list == null) {
			//	List<String> lstRow_his = new ArrayList<String>();
				return true;
			}
			// 遍历做容错处理 去除重复采集的告警
			key = new ArrayList<String>();
			hast_his = new Hashtable<String, local_his_event>();
			Iterator<local_his_event> iter = his_event_list.iterator();
			while (iter.hasNext()) {
				local_his_event his_event = iter.next();
				if (his_event == null)
					return false;
				boolean flag = true;

				if (hast_his.containsKey(his_event.start_time + "#"
						+ his_event.event_id)) {
					flag = false;

					if ("1970-01-01".equals(his_event.finish_time.substring(0,
							10)))
						continue;
				}
				hast_his.put(his_event.start_time + "#" + his_event.event_id,
						his_event);

				if (flag) {
					key.add(his_event.start_time + "#" + his_event.event_id);
				}
			}

			if (key == null || hast_his == null)
				return false;

			key2 = null;
			key2 = new ArrayList<String>();
			key2.clear();
			for (int i = key.size() - 1; i >= 0; i--) {
				key2.add(key.get(i));
			}

			lstContends.clear(); // 清楚页面的以前数据 行信号
			if (key2 == null)
				return false;
			Iterator<String> iterator_key = key2.iterator();
			while (iterator_key.hasNext()) {
				String his_event_key = iterator_key.next();
				if (his_event_key == null || "".equals(his_event_key))
					return false;
				local_his_event his_event = hast_his.get(his_event_key);
				if (his_event == null)
					return false;
				List<String> lstRow_his = new ArrayList<String>();
				lstRow_his.clear();
				// //对通信中断告警结束时间做判断
				String finishTime = his_event.finish_time;
				//
				if (finishTime.length() < 10)
					return false;
				//
				if ("1970-01-01".equals(finishTime.substring(0, 10))) {
					finishTime = "null";

				}
				//
				String startTime = his_event.start_time.substring(0, 10);// 截取年月日
				int time_num = Integer.parseInt(startTime.substring(0, 4))
						+ Integer.parseInt(startTime.substring(5, 7)) * 32
						+ Integer.parseInt(startTime.substring(8, 10));
				if (!(time_num <= after_num && time_num >= before_num)) {

					continue;
				}
				//
				// 重复的强制处理
				if ((lstContends != null) || (lstContends.size() != 0)) {
					for (int m = 0; m < lstContends.size(); m++) {
						List<String> ls = lstContends.get(0);
						String t_name = ls.get(0);
						if (t_name.equals(closeEquiptName) == false)
							lstContends.remove(m);
					}
				}

				String eventName = DataGetter.getEventName(str_EquiptId,
						his_event.event_id);
				lstRow_his.add(closeEquiptName);
				lstRow_his.add(eventName);// 告警名称
				lstRow_his.add(his_event.event_mean);
				lstRow_his.add(his_event.value); // 信号数值
				lstRow_his.add(his_event.severity); // 告警等级
				lstRow_his.add(his_event.start_time); // 开始时间
				lstRow_his.add(finishTime);// 结束时间
				lstContends.add(lstRow_his);
				updateContends(lstTitles, lstContends);
			}

			updateContends(lstTitles, lstContends);
			lstContends.clear();
			hast_his.clear();
			his_event_list.clear();
			key.clear();
			key2.clear();
		} else if(AllDevice.equals(closeEquiptName)) {

			handler.sendEmptyMessage(0);
			for (int i = 0; i < ALLDeviceList.size(); i++) {
				String name = ALLDeviceList.get(i);
				str_Equiptidlsy = (map_EquiptNameList.get(name));
				m_bneedupdate = false; // 如果为真，表示数据不根据数据更新时时刷界面
				his_event_list = new ArrayList<local_his_event>();
				his_event_list = getHisEvent();
				// List<String> lstRow_his1 = new ArrayList<String>();
				//
				if (his_event_list == null) {
					//List<String> lstRow_his = new ArrayList<String>();
					return true;
				}
				Iterator<local_his_event> iter = his_event_list.iterator();
				while (iter.hasNext()) {
					local_his_event his_event = iter.next();

					List<String> lstRow_his1 = new ArrayList<String>();
					String finishTime = his_event.finish_time;
					//
					if (finishTime.length() < 10)
						return false;
					//
					if ("1970-01-01".equals(finishTime.substring(0, 10))) {
						
						finishTime = "null";

					}
					// //
					String startTime = his_event.start_time.substring(0, 10);// 截取年月日
					String eventName = DataGetter.getEventName(str_Equiptidlsy,
							his_event.event_id);
					int time_num = Integer.parseInt(startTime.substring(0, 4))
							+ Integer.parseInt(startTime.substring(5, 7)) * 32
							+ Integer.parseInt(startTime.substring(8, 10));
					if (!(time_num <= after_num && time_num >= before_num)) {
						continue;
					}

					// //重复的强制处理
					if ((lsyLs1 != null) || (lsyLs1.size() != 0)) {
						for (int m = 0; m < lsyLs1.size(); m++) {
							List<String> ls = lsyLs1.get(m);
							String t_name = ls.get(0); // 设备名称
							String e_name = ls.get(1); // 告警名称
							String s_time = ls.get(5);
							if (t_name.equals(name)
									&& s_time.equals(his_event.start_time)
									&& e_name.equals(eventName)) {
								lsyLs1.remove(m);
							}
						}
					}

					lstRow_his1.clear();
					String eventName1 = DataGetter.getEventName(
							str_Equiptidlsy, his_event.event_id);
					lstRow_his1.add(name);
					lstRow_his1.add(eventName1);// 告警名称
					lstRow_his1.add(his_event.event_mean);
					lstRow_his1.add(his_event.value); // 信号数值
					lstRow_his1.add(his_event.severity); // 告警等级
					lstRow_his1.add(his_event.start_time); // 开始时间
					lstRow_his1.add(finishTime);// 结束时间
					lsyLs1.add(lstRow_his1);

				}

			}

			updateContends(lstTitles, lsyLs1);
			lsyLs1.clear();

		}else if("二次下电".equals(closeEquiptName))
		{
			handler.sendEmptyMessage(1);
			m_bneedupdate = false;
			try {
				BufferedReader br=new BufferedReader(new InputStreamReader(
						new FileInputStream(logFile),"GBK"));
				
				String s=null;
				while((s=br.readLine())!=null)
				{
					List<String> list_alarm = new ArrayList<String>();
					local_his_Alarm lha=new local_his_Alarm();
					lha.read_string(s);
					list_alarm.add(lha.equip_name);
					list_alarm.add(lha.control);
					list_alarm.add(lha.alarm);
					list_alarm.add(lha.start_time);
					list_alarm.add(lha.end_time);
					list_alarm.add(lha.yichang);
					list_alarm.add(lha.result);
					
					
					
					lsyLs2.add(list_alarm);
				}
				updateContends(AlarmTitles, lsyLs2);
				lsyLs2.clear();
			} catch (Exception e) {
				
				e.printStackTrace();
			} 
		}

		return true;
	}

	private List<local_his_event> getHisEvent() {
		String filename = "hisevent-" + str_Equiptidlsy;
		List<local_his_event> his_event_list = new ArrayList<local_his_event>();
		try {

			local_file l_file = new local_file();

			if (!l_file.has_file(filename, 3)) {

				return null;
			}

			if (!l_file.read_all_line()) {

				return null;
			}
			List<String> list = l_file.buflist1;
			l_file = null;
			his_event_list.clear();

			Iterator<String> iter = list.iterator();
			while (iter.hasNext()) {
				String buf = iter.next();

				local_his_event his_event = new local_his_event();

				his_event.read_string(buf);

				his_event_list.add(his_event);

				if (his_event_list.size() > 500) {
					break;
				}
				his_event = null;
			}
		} catch (Exception e) {

		}

		return his_event_list;
	}

	@SuppressWarnings("static-access")
	public boolean get_equiptList() {

		if ("".equals(m_strExpression)) {
			
			return false;
			
		} else if (!"Binding{[Equip[Equip:0]]}".equals(m_strExpression)) {
			
			String s = UtExpressionParser.getInstance().getMathExpression(
					m_strExpression);
			ArrayList<Integer> list = new ArrayList<Integer>();
			String[] strCExp = s.split("\\|");
			for (String str : strCExp) {
				String[] strResult = str.split("\\]");
				String[] strResult1 = strResult[0].split("\\:");
				list.add(Integer.parseInt(strResult1[1]));
			}
			for (int id : list) {
				String str_equiptName = DataGetter.getEquipmentName(id);
				map_EquiptNameList.put(str_equiptName, String.valueOf(id));
				adapter.add(str_equiptName);
				ALLDeviceList.add(str_equiptName);
			}

		} else {
			
			HashSet<String> ht_equiptID = DataGetter.getEquipmentIdList();
			if (ht_equiptID == null)
				return false;
			Iterator<String> iter = ht_equiptID.iterator();
			ArrayList<Integer> list = new ArrayList<Integer>();
			while (iter.hasNext()) {
				String equiptId = iter.next();

				list.add(Integer.parseInt(equiptId));

			}
			Collections.sort(list);
			for (int id : list) {
				//System.out.println("equipt_id:" + id);
				String equiptName = DataGetter.getEquipmentName(id);
				if ("".equals(equiptName)) {

					continue;
				}
				adapter.add(equiptName);
				ALLDeviceList.add(equiptName);
				map_EquiptNameList.put(equiptName, id + "");
			}

		}
		return true;
	}

	@Override
	public boolean needupdate() {
		return m_bneedupdate;
	}

	@Override
	public void needupdate(boolean bNeedUpdate) {
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

	// 固定标题栏
	TextView[] m_title;
	TextView[] s_title;
	TextView view_text; // 信号名显示text
	Spinner view_EquiptSpinner = null; // 设备名选择spinner
	Button view_timeButton; // 日期选择button
	Button view_PerveDay; // 前一天button
	Button view_NextDay; // 后一天button
	Button view_Receive; // 接收receive
	TextView view_Text; // “——”

	private DatePickerDialog dialog; // 日期对话框选择应用
	private DatePickerDialog dialog_before;
	private DatePickerDialog dialog_after;

	private int year, month, day; // 对话框显示的年月日变量
	private Calendar calendar;

	public String get_day = ""; // 所要获取数据的日期

	private HashMap<String, String> map_EquiptNameList = null; // <设备名-设备id>
	private ArrayAdapter<String> adapter = null;
	private String closeEquiptName = "";
	public static String str_EquiptId = ""; // 所需要的设备-信号id字符串
	public String str_Equiptidlsy = "";
	public int after_year, after_month, after_day, before_year, before_month,
			before_day;// dialog数据
	int set_year;
	int set_month;
	int set_day;
	MainWindow m_rRenderWindow = null;
	Rect m_rBBox = null;

	public boolean m_bNeedINIT = true;
	public boolean m_bneedupdate = false;

	private boolean click = true;
	@SuppressWarnings("unused")
	private boolean click1 = true;
	@SuppressWarnings("unused")
	private boolean isScope = false;

	private List<String> ALLDeviceList = new ArrayList<String>();

	// TODO: 临时代替数据
	boolean m_needsort = true;
	// ArrayList<String> m_sortedarray = null;
	List<String> lstTitles = null;
	List<String> AlarmTitles = null;
	List<List<String>> lstContends = null;
	List<List<String>> lsyLs = null;
	List<List<String>> lsyLs1 = null;
	List<List<String>> lsyLs2 = null;
	@SuppressWarnings("unused")
	private Paint mPaint = new Paint(); // 注意以后变量的定义一定要赋予空间
	// List<String> fjw_signal = null;
}
