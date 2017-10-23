package com.sg.uis.LsyNewView;




import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.xclcharts.chart.SplineData;
import org.xclcharts.renderer.XEnum;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.mgrid.main.MGridActivity;
import com.mgrid.main.MainWindow;
import com.sg.common.CFGTLS;
import com.sg.common.Calculator;
import com.sg.common.IObject;
import com.sg.common.SgRealTimeData;

/**�ʲ�������״ͼ*/
@SuppressLint({ "ShowToast", "InflateParams", "RtlHardcoded", "ClickableViewAccessibility" })
public class SgBARS extends TextView implements IObject {

	

	public SgBARS(Context context) {
		super(context);
		
		m_oPaint = new Paint(); 
		m_rBBox = new Rect();

	}
	

	@SuppressLint("Dra wAllocation")
	protected void onDraw(Canvas canvas) {
		if (m_rRenderWindow == null) 
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;
		Paint p=new Paint();
		Paint line=new Paint();
		
		line.setColor(Color.WHITE);
		line.setStrokeWidth(1);
		line.setStyle(Style.FILL);
		
	//	p.setColor(Color.RED);
		p.setStrokeWidth(2);
		p.setStyle(Style.FILL);
		p.setAntiAlias(true);
		p.setTextSize(20);
		
		double all=0;
		for (int i = 0; i < data_list.size(); i++) {
		//	canvas.drawRect(0, data_list[i], (int)W/3, (int)H*4/10, p);
			    p.setColor(Color.parseColor(color_list.get(i)));
				double d=data_list.get(i);
				canvas.drawRect(0, (float) (all*H)+1, (int)W/3, (float) (H*(all+d)), p);
				all+=d;
		}
		
//		canvas.drawRect(0, 0, (int)W/3, (int)H*4/10, p);
//		canvas.drawText("40%",(int)W*1/2 , (int)H*4/10+1, p);
		
//		p.setColor(Color.YELLOW);
//		canvas.drawRect(0, (int)H*4/10+1, (int)W/3, (int)H*9/10, p);
//		canvas.drawText("50%",(int)W*1/2 , (int)H*9/10+1, p);
		
//		p.setColor(Color.GREEN);
//		canvas.drawRect(0, (int)H*9/10+1, (int)W/3, H, p);
//		canvas.drawText("10%",(int)W*1/2 ,H, p);
		
//		canvas.drawLine((int)W/3, (int)H*4/10+1, W,(int)H*4/10+1, line);
//		canvas.drawLine((int)W/3, (int)H*9/10+1, W,(int)H*9/10+1, line);
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
		W=nWidth;
		H=nHeight;

		m_rBBox.left = nX;
		m_rBBox.top = nY;
		m_rBBox.right = nX + nWidth;
		m_rBBox.bottom = nY + nHeight;
		if (m_rRenderWindow.isLayoutVisible(m_rBBox)) {		
			this.layout(nX, nY, nX + nWidth, nY + nHeight);
		}
	}
 
	@Override
	public void addToRenderWindow(MainWindow rWin) {
		m_rRenderWindow = rWin;
		rWin.addView(this);
		
		
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
		}else if ("colorData".equals(strName)) {
			 colorData = strValue; 			
			 parse_color();
		}else if ("labelData".equals(strName)) {
			 labelData = strValue; 			
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
		
		if(mExpression.equals("")||mExpression==null) return false;

		return true;
	}

	@Override
	public void updateWidget() {
 
	       this.invalidate();
	       MGridActivity.xianChengChi.execute(new Runnable() {

				@Override
				public void run() {
					try {
						Thread.sleep(60 * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					m_bneedupdate = true;
				}
			});
	} 

	@Override
	public boolean updateValue() {
	
		m_bneedupdate=false;
		SgRealTimeData oRealTimeData = m_rRenderWindow.m_oShareObject.m_mapRealTimeDatas.get(this.getUniqueID());
		if (oRealTimeData == null)
			return false;
		
		String strValue = oRealTimeData.strValue;
		if (strValue == null || "".equals(strValue) == true)
			return false;
		//System.out.println("�������ʽ��"+oRealTimeData.strValue);
		String[] partMath=strValue.split("\\|");
		double all=0;
		double arg[] =new double[partMath.length];
		for (int i = 0; i < partMath.length; i++) {
			String mathCmd=partMath[i];			
			double count=cal.calculate(mathCmd);
			all+=count;
			arg[i]=count;
			System.out.println(mathCmd+" ���� "+count+" ��  "+all);
		}
		data_list.clear();
		for (int i = 0; i < arg.length; i++) {
			double percent =arg[i]/all;		
			data_list.add(percent);
		}
	
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
	
	private String signal="";	
	private String equail="";
    public boolean m_bneedupdate = true;
    private String mExpression = "";
	private String colorData="";
	private String labelData="";
	private Calculator cal = new Calculator();
	private List<Double> data_list=new ArrayList<Double>();
	private int W,H;		
	private List<String> color_list = new ArrayList<String>();

	

}


