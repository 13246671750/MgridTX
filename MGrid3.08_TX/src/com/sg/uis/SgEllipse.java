package com.sg.uis;

import com.mgrid.main.MainWindow;
import com.sg.common.IObject;
import com.sg.common.SgRealTimeData;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
/** Բ */
public class SgEllipse extends View implements IObject {
	public SgEllipse(Context context) {  
        super(context); 
        this.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				return true;
			}
        });
        m_oPaint = new Paint();
        m_rRectF = new RectF();
        m_rBBox = new Rect();
    }
	
	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas) {
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;
		
		m_oPaint.setColor(m_cBorderColor);
		m_oPaint.setAntiAlias(true); // ���û��ʵľ��Ч��
		m_oPaint.setStrokeWidth(m_nBorderWidth);
		m_oPaint.setStyle(Paint.Style.STROKE);
        
        int nWidth = (int) (((float)(m_nWidth) / (float)MainWindow.FORM_WIDTH) * (m_rRenderWindow.VIEW_RIGHT - m_rRenderWindow.VIEW_LEFT));
		int nHeight = (int) (((float)(m_nHeight) / (float)MainWindow.FORM_HEIGHT) * (m_rRenderWindow.VIEW_BOTTOM - m_rRenderWindow.VIEW_TOP));

		m_rRectF.left = m_nBorderWidth;
		m_rRectF.top = m_nBorderWidth;
		m_rRectF.right = nWidth-m_nBorderWidth;
		m_rRectF.bottom = nHeight-m_nBorderWidth;
        canvas.drawOval(m_rRectF, m_oPaint);  
        
        // ������ɫ�ͽ����
        if (m_arrGradientColorPos != null) {
		    LinearGradient lg = null;
		    if (m_bIsHGradient) {
		        lg = new LinearGradient(0, nHeight/2, nWidth, nHeight/2, m_arrGradientFillColor, 
		        		m_arrGradientColorPos, TileMode.MIRROR);
		    }
		    else {
		        lg = new LinearGradient(nWidth/2, 0, nWidth/2, nHeight, m_arrGradientFillColor, 
		        		m_arrGradientColorPos, TileMode.MIRROR);      	
		    }
		    m_oPaint.setShader(lg);
        }
        else
        	m_oPaint.setColor(m_cSingleFillColor); // ����䵥ɫ
        m_oPaint.setStyle(Paint.Style.FILL);   
        canvas.drawOval(m_rRectF, m_oPaint); 
	}
	
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
			layout(nX, nY, nX+nWidth, nY+nHeight);
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
	
	public void parseProperties(String strName, String strValue, String strResFolder) {

        if ("ZIndex".equals(strName)) {
       	 	m_nZIndex = Integer.parseInt(strValue);
       	    if (MainWindow.MAXZINDEX < m_nZIndex) MainWindow.MAXZINDEX = m_nZIndex;
        }
        else if ("Location".equals(strName)) {
       	 	String[] arrStr = strValue.split(",");
       	 	m_nPosX = Integer.parseInt(arrStr[0]);
       	 	m_nPosY = Integer.parseInt(arrStr[1]);
        }
        else if ("Size".equals(strName)) {
       	 	String[] arrSize = strValue.split(",");
       	 	m_nWidth = Integer.parseInt(arrSize[0]);
       	 	m_nHeight = Integer.parseInt(arrSize[1]);
        }
        else if ("Alpha".equals(strName)) {
       	 	m_fAlpha = Float.parseFloat(strValue);
	       	m_oPaint.setAlpha((int)(m_fAlpha*255));
        }
        else if ("RotateAngle".equals(strName)) {
       	 	m_fRotateAngle = Float.parseFloat(strValue);
        }
        else if ("BorderColor".equals(strName)) {
        	m_cBorderColor = Color.parseColor(strValue);
        }
        else if ("BorderWidth".equals(strName))
        	m_nBorderWidth = Integer.parseInt(strValue);
        else if ("FillColor".equals(strName)) {
        	String[] arrStr = strValue.split(",");
        	if (arrStr.length == 1) {
        		m_cSingleFillColor = Color.parseColor(strValue);
        		m_cStartFillColor = m_cSingleFillColor;
        	}
        	else {
        		if (Integer.parseInt(arrStr[0]) == 0)
        			m_bIsHGradient = false;
        		else
        			m_bIsHGradient = true;
        		m_fAlpha = Float.parseFloat(arrStr[1]);
        		
        		int nCount = (arrStr.length - 2) / 2;
        		m_arrGradientColorPos = new float[nCount];
        		m_arrGradientFillColor = new int[nCount];
        		int nIndex = 0;
        		for (int i = 2; i < arrStr.length; i += 2) {	
        			int color = Color.parseColor(arrStr[i]);
        			m_arrGradientFillColor[nIndex] = Color.argb((int)(Color.alpha(color)*m_fAlpha), Color.red(color), Color.green(color), Color.blue(color));
        			m_arrGradientColorPos[nIndex] = Float.parseFloat(arrStr[i+1]);
        			nIndex++;
        		}
        	}
        }
        else if ("IsDashed".equals(strName)) {
        	m_bIsDashed = Boolean.parseBoolean(strValue);
        }
        else if ("StateExpression".equals(strName))
        	m_strStateExpression = strValue;
        else if ("Expression".equals(strName)) 
       	 	m_strExpression = strValue;
        else if ("ColorExpression".equals(strName))
        	m_strColorExpression = strValue;
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
	public void updateWidget() {
		this.invalidate();
	}
	
	@Override
	public boolean updateValue()
	{
		m_bneedupdate = false;

		SgRealTimeData oRealTimeData = m_rRenderWindow.m_oShareObject.m_mapRealTimeDatas.get(this.getUniqueID());
		if (oRealTimeData == null)
			return false;
		String strValue = oRealTimeData.strValue;
		if (strValue == null || "".equals(strValue) == true)
			return false;
		
		int nValue = 0;
		try {
			nValue = Integer.parseInt(strValue);
		}catch(Exception e) {
			
		}
		
		if(strValue.equals(oldSignalValue)){
			return false;
        }else{
        	oldSignalValue = strValue;
     	 	parseFontcolor(oRealTimeData.strData);   //������ֵ��ɫ���ʽ fjw add	        
	    	return true;
        }

	}
	//��ɫ��������  �����������ʾֵ   fjw add
	public int parseFontcolor(String strValue)  
	{
		m_cSingleFillColor = m_cStartFillColor;
		if( (m_strColorExpression == null)||("".equals(m_strColorExpression)) ) return -1;
		if( (strValue == null)||("".equals(strValue)) ) return -1;
		if("-999999".equals(strValue)) return -1;		
//		Log.e("Label-updataValue", "into!"+"--"+m_strColorExpression.substring(0,1));
		if( (">".equals(m_strColorExpression.substring(0,1)))!=true ) return -1;

	
		String buf[] = m_strColorExpression.split(">"); //��ȡ���ʽ�е���������ɫ��Ԫ
		for(int i=1;i<buf.length;i++){
			String a[] = buf[i].split("\\[|\\]"); //����ָ���[ ]			
//			Log.e("Label-updataValue", "�Ƚ�ֵ"+a[0]+"+��ɫ��ֵ��"+a[1]);
			//�Ƚ���ֵ	
			float data = Float.parseFloat(a[0]); //��ñȽ�ֵ
			float value = Float.parseFloat(strValue); //����ֵ
			if(value > data){
				m_cSingleFillColor = Color.parseColor(a[1]);
			}
		}		
		return m_cSingleFillColor;
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
	
	public View getView() {
		return this;
	}
	
	public int getZIndex()
	{
		return m_nZIndex;
	}
	
	public Rect getBBox() {
		return m_rBBox;
	}
	
// params:
	String m_strID = "";
	String m_strType = "";
    int m_nZIndex = 4;
	int m_nPosX = 349;
	int m_nPosY = 78;
	int m_nWidth = 200;
	int m_nHeight = 150;
	float m_fAlpha = 1.0f;
	float m_fRotateAngle = 0.0f;
	int m_cBorderColor = 0xFF000000;
	int m_nBorderWidth = 3;
	int m_cFillColor = 0x00000000;
	boolean m_bIsDashed = false;
	String m_strStateExpression = "Binding{[State[Equip:114]]}";
	String m_strExpression = "Binding{[Value[Equip:114-Temp:173-Signal:1]]}";
	String m_strColorExpression = ">20[#FF009090]>30[#FF0000FF]>50[#FFFF0000]>60[#FFFFFF00]";
	
	int m_cSingleFillColor = 0xFF0000FF;
	int m_cStartFillColor = 0x00000000;
	float[] m_arrGradientColorPos = null;
	int[] m_arrGradientFillColor = null;
	boolean m_bIsHGradient = false; // ˮƽ����
	
	MainWindow m_rRenderWindow = null;
	Paint m_oPaint = null;  
	RectF m_rRectF = null;
	Rect m_rBBox = null;
	public boolean m_bneedupdate = true;
	String oldSignalValue = "";
}
