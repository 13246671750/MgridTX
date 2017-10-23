package com.sg.uis.LsyNewView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.xclcharts.chart.SplineChart;
import org.xclcharts.chart.SplineData;
import org.xclcharts.renderer.XEnum;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.demo.xclcharts.view.SplineChart01View;
import com.mgrid.data.DataGetter;
import com.mgrid.main.MGridActivity;
import com.mgrid.main.MainWindow;
import com.mgrid.util.ExpressionUtils;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;

/** ����ͼ */
@SuppressLint({ "ShowToast", "InflateParams", "RtlHardcoded",
		"ClickableViewAccessibility" })
public class SgBrokenLine extends TextView implements IObject {

	private SplineChart Schart;// �ؼ�view
	private LinkedList<String> labels = new LinkedList<String>();// X���ǩ����
	private LinkedList<SplineData> chartData = new LinkedList<SplineData>();;// Y���ǩ����

	public SgBrokenLine(Context context) {
		super(context);

		m_oPaint = new Paint();
		m_rBBox = new Rect();
		chart = new SplineChart01View(context);
		chart.setTouch(false);
		Schart = chart.getChart();
		Schart.AWAY = 1;
		Schart.setPadding(70, 40, 5, 40);

	}

	@SuppressLint("Dra wAllocation")
	protected void onDraw(Canvas canvas) {
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;
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
			chart.layout(nX, nY, nX + nWidth, nY + nHeight);
		}
	}

	@Override
	public void addToRenderWindow(MainWindow rWin) {
		m_rRenderWindow = rWin;
		rWin.addView(this);
		rWin.addView(chart);

	}

	@Override
	public void removeFromRenderWindow(MainWindow rWin) {
		rWin.removeView(this);
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
		} else if ("Size".equals(strName)) {
			String[] arrSize = strValue.split(",");
			m_nWidth = Integer.parseInt(arrSize[0]);
			m_nHeight = Integer.parseInt(arrSize[1]);
		} else if ("Alpha".equals(strName)) {
			m_fAlpha = Float.parseFloat(strValue);
		} else if ("BackgroundColor".equals(strName)) {
			if (strValue.isEmpty())
				return;
			m_cBackgroundColor = Color.parseColor(strValue);
			// this.setBackgroundColor(m_cBackgroundColor);
		} else if ("Content".equals(strName)) {
			m_strContent = strValue;

		} else if ("FontFamily".equals(strName))
			m_strFontFamily = strValue;
		else if ("FontSize".equals(strName)) {
			float fWinScale = (float) MainWindow.SCREEN_WIDTH
					/ (float) MainWindow.FORM_WIDTH;
			m_fFontSize = Float.parseFloat(strValue) * fWinScale;

		} else if ("IsBold".equals(strName))
			m_bIsBold = Boolean.parseBoolean(strValue);
		else if ("FontColor".equals(strName)) {
			m_cFontColor = Color.parseColor(strValue);
			// this.setTextColor(m_cFontColor);
		} else if ("ClickEvent".equals(strName))
			m_strClickEvent = strValue;
		else if ("Url".equals(strName))
			m_strUrl = strValue;
		else if ("CmdExpression".equals(strName))
			m_strCmdExpression = strValue;

		else if ("HorizontalContentAlignment".equals(strName))
			m_strHorizontalContentAlignment = strValue;
		else if ("VerticalContentAlignment".equals(strName))
			m_strVerticalContentAlignment = strValue;
		else if ("Expression".equals(strName)) {
			mExpression = strValue;
			parse_cmd();
		} else if ("colorData".equals(strName)) {
			colorData = strValue;
			//System.out.println("��ɫ��"+colorData);
			parse_color();
		} else if ("labelData".equals(strName)) {
			labelData = strValue;
			parse_label();
		}
	}

	private void parse_label() {
		if (labelData == null || labelData.equals(""))
			return;
		String[] s = labelData.split("\\|");
		for (int i = 0; i < s.length; i++) {
			label_list.add(s[i]);
		}

	}

	private void parse_color() {
		if (colorData == null || colorData.equals(""))
			return;
		String[] s = colorData.split("\\|");
		for (int i = 0; i < s.length; i++) {
			color_list.add(s[i]);
		}
	}

	@Override
	public void initFinished() {
		int nFlag = Gravity.NO_GRAVITY;
		if ("Left".equals(m_strHorizontalContentAlignment))
			nFlag |= Gravity.LEFT;
		else if ("Right".equals(m_strHorizontalContentAlignment))
			nFlag |= Gravity.RIGHT;
		else if ("Center".equals(m_strHorizontalContentAlignment))
			nFlag |= Gravity.CENTER_HORIZONTAL;

		if ("Top".equals(m_strVerticalContentAlignment))
			nFlag |= Gravity.TOP;
		else if ("Bottom".equals(m_strVerticalContentAlignment)) {
			nFlag |= Gravity.BOTTOM;
			double padSize = CFGTLS.getPadHeight(m_nHeight,
					MainWindow.FORM_HEIGHT, getTextSize());
			setPadding(0, (int) padSize, 0, 0);
		} else if ("Center".equals(m_strVerticalContentAlignment)) {
			nFlag |= Gravity.CENTER_VERTICAL;
			double padSize = CFGTLS.getPadHeight(m_nHeight,
					MainWindow.FORM_HEIGHT, getTextSize()) / 2f;
			setPadding(0, (int) padSize, 0, (int) padSize);
		}

		setGravity(nFlag);
	}

	public String getBindingExpression() {
		return mExpression;
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

	// fjw add ��ť��������ܵĿ�������İ󶨱��ʽ����
	// �������ؼ����ʽ�����ؿؼ����ʽ��
	public boolean parse_cmd() {

		if (mExpression.equals("") || mExpression == null)
			return false;
		cmd_list = ExpressionUtils.getExpressionUtils().parse(mExpression);
		return true;
	}

	@Override
	public void updateWidget() {

		// ����Դ
		// Schart.setCategories(labels);
		// Schart.setDataSource(chartData);
		MGridActivity.xianChengChi.execute(new Runnable() {

			@Override
			public void run() {
				chartData=new LinkedList<SplineData>();
				for (int i = 0; i < partList.size(); i++) {
					List<Integer> dataList = partList.get(i);
					LinkedHashMap<Double, Double> linePoint = new LinkedHashMap<Double, Double>();
					for (int j = 0; j < dataList.size(); j++) {
						linePoint.put((double) j*5, (double) dataList.get(j));
					}
					SplineData dataSeries = new SplineData(label_list.get(i),
							linePoint, Color.parseColor(color_list.get(i)));
					dataSeries.setDotStyle(XEnum.DotStyle.HIDE);	
					dataSeries.getLinePaint().setStrokeWidth(1);
					dataSeries.setLabelVisible(true);
					dataSeries.getDotLabelPaint().setColor(Color.parseColor("#A9A9A9"));
					dataSeries.getDotLabelPaint().setTextSize(10);
					chartData.add(dataSeries);
				}
				handler.sendEmptyMessage(0);
				try {
					Thread.sleep(60 * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				m_bneedupdate = true;
			}
		});
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				// SBchart.getDataAxis().setAxisMax((int)maxCount+10);
				// SBchart.setDataSource(BarDataSet);
				Schart.setDataSource(chartData);
				chart.invalidate();

				break;
			}

		};
	};

	@Override
	public boolean updateValue() {

		for (int i = 0; i < cmd_list.size(); i++) {
			String str = cmd_list.get(i);
			String cmd[] = str.split("-");
			int equaip = Integer.parseInt(cmd[0]);
			int signal = Integer.parseInt(cmd[2]);
			String value = DataGetter.getSignalValue(equaip, signal);
			if (value == null || value.equals("") || value.equals("-999999"))
				return false;
			int Y=(int)Double.parseDouble(value);
			
			System.out.println("Y:"+Y+" cmd_list:"+cmd_list.size());
			//if(Y==0) continue;
			List<Integer> dataList = null;
			if (partList.size()==200)
			{
				partList.remove(0);
			}
			if (partList.size()<=i) {
				dataList = new ArrayList<Integer>();
				partList.add(dataList);
			} else {
				dataList = partList.get(i);
			}
			
			dataList.add((int)Double.parseDouble(value));
		}
		m_bneedupdate = false;
		return true;
	}

	@Override
	public boolean needupdate() {

		return m_bneedupdate;
	}

	@Override
	public void needupdate(boolean bNeedUpdate) {

	}

	public View getView() {
		return this;
	}

	public int getZIndex() {
		return m_nZIndex;
	}

	public Rect getBBox() {
		return m_rBBox;
	}

	// params:
	String m_strID = "";
	String m_strType = "";
	int m_nZIndex = 7;
	int m_nPosX = 152;
	int m_nPosY = 287;
	int m_nWidth = 75;
	int m_nHeight = 23;
	float m_fAlpha = 1.0f;
	int m_cBackgroundColor = 0xF00CF00C;
	String m_strContent = "��ť";
	String m_strFontFamily = "΢���ź�";
	float m_fFontSize = 12.0f;
	boolean m_bIsBold = false;
	int m_cFontColor = 0xFF008000;
	String m_strClickEvent = "��ʿ��-IDUϵͳ�趨UPS.xml";
	String m_strUrl = "www.baidu.com";
	String m_strCmdExpression = "Binding{[Cmd[Equip:1-Temp:173-Command:1-Parameter:1-Value:1]]}";
	String m_strHorizontalContentAlignment = "Center";
	String m_strVerticalContentAlignment = "Center";
	boolean m_bPressed = false;
	MainWindow m_rRenderWindow = null;
	String cmd_value = "";

	Paint m_oPaint = null;
	Rect m_rBBox = null;
	public static ProgressDialog dialog;

	// ��¼�������꣬���˻���������������������������⡣
	public float m_xscal = 0;
	public float m_yscal = 0;

	Intent m_oHomeIntent = null;

	private String signal = "";
	private String equail = "";
	SplineChart01View chart = null;
	public boolean m_bneedupdate = true;
	private String mExpression = "";
	private String colorData = "";
	private String labelData = "";
	private List<String> color_list = new ArrayList<String>();
	private List<String> label_list = new ArrayList<String>();
	private List<String> cmd_list = new ArrayList<String>();
	private HashMap<Double, Double> dataHash = new HashMap<Double, Double>(); // һ�����ϵ�һ����
	// private List<Integer> dataList=new ArrayList<Integer>(); //һ���ߵ�����
	private List<List<Integer>> partList = new ArrayList<List<Integer>>(); // �����ߵ������ܼ���

}
