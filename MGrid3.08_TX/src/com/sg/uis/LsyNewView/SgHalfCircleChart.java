package com.sg.uis.LsyNewView;

import org.xclcharts.chart.GaugeChart;

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

import com.demo.xclcharts.view.GaugeChart01View;
import com.mgrid.data.DataGetter;
import com.mgrid.main.MainWindow;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;

/** ��Բ���̱� */
@SuppressLint({ "ShowToast", "InflateParams", "RtlHardcoded", "ClickableViewAccessibility" })
public class SgHalfCircleChart extends TextView implements IObject {

	
	private GaugeChart Gchart;
	
	public SgHalfCircleChart(Context context) {
		super(context);

		m_oPaint = new Paint();
		m_rBBox = new Rect();
		Gauge01View=new GaugeChart01View(context);
		Gchart=Gauge01View.getChart();
		
	}
	

	@SuppressLint("DrawAllocation")
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
		
			Gauge01View.layout(nX, nY, nX + nWidth, nY + nHeight);
		}
	}

	@Override
	public void addToRenderWindow(MainWindow rWin) {
		m_rRenderWindow = rWin;
		rWin.addView(this);
		rWin.addView(Gauge01View);
		
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
		//	this.setTextColor(m_cFontColor);
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
//			 final Random r=new Random();
//			
//			 new Thread(new Runnable() {
//				
//				@Override
//				public void run() {
//					while(true)
//					{
//						DangQianValue=r.nextInt(180);
//						if(DangQianValue>DuiBiValue){
//							for (int m = DuiBiValue; m <=DangQianValue; m++) {
//							
//								Gchart.setCurrentAngle(m);
//								try {
//									Thread.sleep(10);
//								} catch (InterruptedException e) {
//									
//									e.printStackTrace();
//								}
//								handler.sendEmptyMessage(0);
//							}
//						}else
//						{
//							for (int m = DuiBiValue; m >= DangQianValue; m--) {
//						
//								Gchart.setCurrentAngle(m);
//								try {
//									Thread.sleep(10);
//								} catch (InterruptedException e) {
//								
//									e.printStackTrace();
//								}
//								handler.sendEmptyMessage(0);
//							}
//						}
//						DuiBiValue=DangQianValue;
//						try {
//							Thread.sleep(1000);
//						} catch (InterruptedException e) {
//						
//							e.printStackTrace();
//						}
//					}
//					
//				}
//			}).start();
		}
	}
	
	
	Handler handler=new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Gauge01View.invalidate();
				break;

			}
		};
	};

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
		
		if(mExpression==null||mExpression.equals("")) return false;
		String[] arg1=mExpression.split("-");
		equail=arg1[0].split(":")[1];
		signal=arg1[2].split(":")[1].split("]")[0];
	
		return true;
	}

	@Override
	public void updateWidget() {
		
		float wendu=Float.parseFloat(newValue) ;
		int angle=(int) (wendu/50*180);
		if(angle<0) return;
		Gchart.setCurrentAngle(angle);
		Gauge01View.invalidate();
	
	}

	@Override
	public boolean updateValue() {
		
		
		if(mExpression==null||mExpression.equals("")) return false;
		if(equail.equals("")||signal.equals("")) return false;
	
		newValue=DataGetter.getSignalMeaning(equail, signal);
		if(!newValue.equals(oldValue))
		{
			oldValue=newValue;	
			return true;
		}
		return false;
	}

	@Override
	public boolean needupdate() {
		
		
	//	System.out.println("��Ҫ����"+m_bneedupdate);
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
	
	private String signal="";	
	private String equail="";
	GaugeChart01View Gauge01View=null;
	private String newValue="";
    private String oldValue="";
    public boolean m_bneedupdate = true;
    private String mExpression = "";
//	private int DangQianValue,DuiBiValue;

	

}
