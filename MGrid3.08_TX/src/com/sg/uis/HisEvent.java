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

/** ��ʷ�澯 */
// �źŸ澯���� HisEvent
// author :fjw0312
// time:2016 5 17
public class HisEvent extends UtTable implements IObject {

	// ������Ӣ���л�
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

		// �����б������� ��Ҫ���ã�����⵽�л���click1=false ֹͣ������click1=true;
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
			DeviceName = "�豸����";
			AlarmName = "�澯����";
			AlarmMeaning = "�澯����";
			Numericalsignal = "�ź���ֵ";
			AlarmSeverity = "�澯�ȼ�";
			StartTime = "��ʼʱ��";
			EndTime = "����ʱ��";

			DeviceList = "  �豸��   ";
			SetTime = "��������";
			PreveDay = "����ʱ��";
			NextDay = "��ʼʱ��";
			Receive = "  ��ȡ   ";
			AllDevice = "ȫ���豸";
		} else {
			DeviceName = "DeviceName";
			AlarmName = "AlarmName";
			AlarmMeaning = "AlarmMeaning";
			Numericalsignal = "Numericalsignal";
			AlarmSeverity = "AlarmSeverity";
			StartTime = "StartTime";
			EndTime = "EndTime";

			DeviceList = "  Device��   ";
			SetTime = "SetTime";
			PreveDay = "PreveDay";
			NextDay = "NextDay";
			Receive = "  Receive   ";

			AllDevice = "AllDevice";
		}

		// ��ͷ����
		lstTitles = new ArrayList<String>();
		AlarmTitles=new ArrayList<String>();
		lstTitles.add(DeviceName);
		// lstTitles.add("�ź�����");
		lstTitles.add(AlarmName);
		lstTitles.add(AlarmMeaning);
		lstTitles.add(Numericalsignal);
		lstTitles.add(AlarmSeverity);
		lstTitles.add(StartTime);
		lstTitles.add(EndTime);

		// �ź�����ʾtext
		view_text = new TextView(context);
		view_text.setTextColor(Color.BLACK);
		view_text.setText(DeviceList); // ��Ϊ����
		view_text.setTextSize(16);
		view_text.setGravity(Gravity.CENTER);
		view_text.setBackgroundColor(Color.argb(100, 100, 100, 100));

		// ����ѡ��button
		view_timeButton = new Button(context);
		view_timeButton.setText(SetTime); // Set Time
		view_timeButton.setTextColor(Color.BLACK);
		view_timeButton.setTextSize(16);
		view_timeButton.setPadding(2, 2, 2, 2);
		view_timeButton.setOnClickListener(l);// ���øÿؼ��ļ���
		// ����ʱ��button
		view_PerveDay = new Button(context);
		view_PerveDay.setText(PreveDay); // PreveDay
		view_PerveDay.setTextColor(Color.BLACK);
		view_PerveDay.setTextSize(16);
		view_PerveDay.setPadding(2, 2, 2, 2);
		view_PerveDay.setOnClickListener(l);// ���øÿؼ��ļ���

		view_Text = new TextView(context);
		view_Text.setText("����");
		view_Text.setTextColor(Color.BLACK);
		view_Text.setTextSize(16);
		view_Text.setPadding(2, 2, 2, 2);

		// ��ʼʱ��button
		view_NextDay = new Button(context);
		view_NextDay.setText(NextDay); // NextDay
		view_NextDay.setTextColor(Color.BLACK);
		view_NextDay.setTextSize(16);
		view_NextDay.setPadding(2, 2, 2, 2);
		view_NextDay.setOnClickListener(l);// ���øÿؼ��ļ���
		// ����receive
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
			adapter.add("�����µ�");
			AlarmTitles.add("����ID");
			AlarmTitles.add("���ƿ���");
			AlarmTitles.add("�澯ԭ��");
			AlarmTitles.add("��ʼʱ��");
			AlarmTitles.add("����ʱ��");
			AlarmTitles.add("�Ƿ��쳣");
			AlarmTitles.add("���ƽ��");		
		}
		view_EquiptSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						get_equiptList(); // ���������б��Ա

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

	// ������ view_Receive
	private OnClickListener l = new OnClickListener() {

		@Override
		public void onClick(View arg0) {

			set_year = dialog.getDatePicker().getYear();
			set_month = dialog.getDatePicker().getMonth() + 1;
			set_day = dialog.getDatePicker().getDayOfMonth();

			if (arg0 == view_timeButton) {

				dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "����",
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

				dialog_before.setButton(DialogInterface.BUTTON_NEGATIVE, "����",
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

				dialog_after.setButton(DialogInterface.BUTTON_NEGATIVE, "����",
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

	// ֪ͨuiˢ���߳�
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
		} else { // �����ܵ��ʱ ���ص������¼�
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
			

			// ����view_button�ĵװ�ռ�
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
		// view_button������ӵ�����
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
		// view_button�����ӵ�����ȥ��
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

			// �趨�б��������
			m_nLeft = m_nPosX;
			m_nTop = m_nPosY;
			m_nRight = m_nLeft + m_nTableWidth;
			m_nBottom = m_nTop + m_nTableHeight;
		} else if ("Size".equals(strName)) {
			String[] arrSize = strValue.split(",");
			m_nWidth = Integer.parseInt(arrSize[0]);
			m_nHeight = Integer.parseInt(arrSize[1]);

			// �趨�б��������
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
	public boolean updateValue() // ���ڸ��²�����������Ҫ�����´��� fjw notice
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

		if (!AllDevice.equals(closeEquiptName)&&!"�����µ�".equals(closeEquiptName)) {
			handler.sendEmptyMessage(0);
			lstContends.clear(); // ���ҳ�����ǰ���� ���ź�
			m_bneedupdate = false; // ���Ϊ�棬��ʾ���ݲ��������ݸ���ʱʱˢ����

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
			// �������ݴ��� ȥ���ظ��ɼ��ĸ澯
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

			lstContends.clear(); // ���ҳ�����ǰ���� ���ź�
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
				// //��ͨ���жϸ澯����ʱ�����ж�
				String finishTime = his_event.finish_time;
				//
				if (finishTime.length() < 10)
					return false;
				//
				if ("1970-01-01".equals(finishTime.substring(0, 10))) {
					finishTime = "null";

				}
				//
				String startTime = his_event.start_time.substring(0, 10);// ��ȡ������
				int time_num = Integer.parseInt(startTime.substring(0, 4))
						+ Integer.parseInt(startTime.substring(5, 7)) * 32
						+ Integer.parseInt(startTime.substring(8, 10));
				if (!(time_num <= after_num && time_num >= before_num)) {

					continue;
				}
				//
				// �ظ���ǿ�ƴ���
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
				lstRow_his.add(eventName);// �澯����
				lstRow_his.add(his_event.event_mean);
				lstRow_his.add(his_event.value); // �ź���ֵ
				lstRow_his.add(his_event.severity); // �澯�ȼ�
				lstRow_his.add(his_event.start_time); // ��ʼʱ��
				lstRow_his.add(finishTime);// ����ʱ��
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
				m_bneedupdate = false; // ���Ϊ�棬��ʾ���ݲ��������ݸ���ʱʱˢ����
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
					String startTime = his_event.start_time.substring(0, 10);// ��ȡ������
					String eventName = DataGetter.getEventName(str_Equiptidlsy,
							his_event.event_id);
					int time_num = Integer.parseInt(startTime.substring(0, 4))
							+ Integer.parseInt(startTime.substring(5, 7)) * 32
							+ Integer.parseInt(startTime.substring(8, 10));
					if (!(time_num <= after_num && time_num >= before_num)) {
						continue;
					}

					// //�ظ���ǿ�ƴ���
					if ((lsyLs1 != null) || (lsyLs1.size() != 0)) {
						for (int m = 0; m < lsyLs1.size(); m++) {
							List<String> ls = lsyLs1.get(m);
							String t_name = ls.get(0); // �豸����
							String e_name = ls.get(1); // �澯����
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
					lstRow_his1.add(eventName1);// �澯����
					lstRow_his1.add(his_event.event_mean);
					lstRow_his1.add(his_event.value); // �ź���ֵ
					lstRow_his1.add(his_event.severity); // �澯�ȼ�
					lstRow_his1.add(his_event.start_time); // ��ʼʱ��
					lstRow_his1.add(finishTime);// ����ʱ��
					lsyLs1.add(lstRow_his1);

				}

			}

			updateContends(lstTitles, lsyLs1);
			lsyLs1.clear();

		}else if("�����µ�".equals(closeEquiptName))
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

	// �̶�������
	TextView[] m_title;
	TextView[] s_title;
	TextView view_text; // �ź�����ʾtext
	Spinner view_EquiptSpinner = null; // �豸��ѡ��spinner
	Button view_timeButton; // ����ѡ��button
	Button view_PerveDay; // ǰһ��button
	Button view_NextDay; // ��һ��button
	Button view_Receive; // ����receive
	TextView view_Text; // ��������

	private DatePickerDialog dialog; // ���ڶԻ���ѡ��Ӧ��
	private DatePickerDialog dialog_before;
	private DatePickerDialog dialog_after;

	private int year, month, day; // �Ի�����ʾ�������ձ���
	private Calendar calendar;

	public String get_day = ""; // ��Ҫ��ȡ���ݵ�����

	private HashMap<String, String> map_EquiptNameList = null; // <�豸��-�豸id>
	private ArrayAdapter<String> adapter = null;
	private String closeEquiptName = "";
	public static String str_EquiptId = ""; // ����Ҫ���豸-�ź�id�ַ���
	public String str_Equiptidlsy = "";
	public int after_year, after_month, after_day, before_year, before_month,
			before_day;// dialog����
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

	// TODO: ��ʱ��������
	boolean m_needsort = true;
	// ArrayList<String> m_sortedarray = null;
	List<String> lstTitles = null;
	List<String> AlarmTitles = null;
	List<List<String>> lstContends = null;
	List<List<String>> lsyLs = null;
	List<List<String>> lsyLs1 = null;
	List<List<String>> lsyLs2 = null;
	@SuppressWarnings("unused")
	private Paint mPaint = new Paint(); // ע���Ժ�����Ķ���һ��Ҫ����ռ�
	// List<String> fjw_signal = null;
}
