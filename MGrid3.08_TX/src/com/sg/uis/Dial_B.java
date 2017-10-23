package com.sg.uis;

import java.text.DecimalFormat;

import com.mgrid.main.MainWindow;
import com.sg.common.IObject;
import com.sg.common.SgRealTimeData;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
/**�Զ������ made fjw 2016 04 25 */
public class Dial_B extends View implements IObject {
	public Dial_B(Context context) {  
        super(context); 
        this.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				return true;
			}
        });
        m_oPaint = new Paint();
        m_rRectF1 = new RectF();
        m_rRectF2 = new RectF();
        m_rRectF3 = new RectF();
        m_rBBox = new Rect();
       
    }
	
	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas) {
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;
		

        
        int nWidth = (int) (((float)(m_nWidth) / (float)MainWindow.FORM_WIDTH) * (m_rRenderWindow.VIEW_RIGHT - m_rRenderWindow.VIEW_LEFT));
		int nHeight = (int) (((float)(m_nHeight) / (float)MainWindow.FORM_HEIGHT) * (m_rRenderWindow.VIEW_BOTTOM - m_rRenderWindow.VIEW_TOP));

		float angle = 360/maxValue * data_value;
		int pad = m_nBorderWidth/2+4;  
		m_rRectF1.left = pad+m_nfillWidth/2;
		m_rRectF1.top = pad+m_nfillWidth/2;
		if(nWidth<nHeight){    
			m_rRectF1.right = nWidth-pad-m_nfillWidth/2;;
			m_rRectF1.bottom = nWidth-pad-m_nfillWidth/2;
		}else{
			m_rRectF1.right = nHeight-pad-m_nfillWidth/2;
			m_rRectF1.bottom = nHeight-pad-m_nfillWidth/2;
		}
		if(mode==1){

		m_oPaint.setColor(m_cSingleFillColor);
		m_oPaint.setAntiAlias(true);    
		m_oPaint.setStrokeWidth(m_nfillWidth);
//		m_oPaint.setStrokeWidth(2);
		m_oPaint.setStyle(Paint.Style.STROKE);
		RectF rect = new RectF(m_rRectF1.left, m_rRectF1.top,m_rRectF1.right,m_rRectF1.bottom);
		

		} 
		
			m_rRectF2.left = m_rRectF1.left-m_nfillWidth/2;
			m_rRectF2.top = m_rRectF1.top-m_nfillWidth/2;
			m_rRectF2.right = m_rRectF1.right+m_nfillWidth/2;
			m_rRectF2.bottom = m_rRectF1.bottom+m_nfillWidth/2;
			m_oPaint.setColor(m_cLineColor);
			m_oPaint.setAntiAlias(true); 
			m_oPaint.setStrokeWidth(m_nBorderWidth);
			m_oPaint.setStyle(Paint.Style.STROKE);
	        canvas.drawOval(m_rRectF2, m_oPaint);  
	        
		
			m_rRectF3.left = m_rRectF2.left+m_nfillWidth;
			m_rRectF3.top = m_rRectF2.top+m_nfillWidth;
			m_rRectF3.right = m_rRectF2.right-m_nfillWidth;
			m_rRectF3.bottom = m_rRectF2.bottom-m_nfillWidth;
			m_oPaint.setColor(m_cLineColor); // ����䵥ɫ
			m_oPaint.setStrokeWidth(m_nBorderWidth);
			m_oPaint.setStyle(Paint.Style.STROKE);   
//			canvas.drawOval(m_rRectF3, m_oPaint); 
			

			float x_origin = (m_rRectF3.left+m_rRectF3.right)/(float)2.0;
			float y_origin = (m_rRectF3.top+m_rRectF3.bottom)/(float)2.0;
			float x_p = m_rRectF2.right - x_origin;
			float y_p = m_rRectF2.bottom - y_origin;
			
		
//			m_oPaint.setColor(Color.RED);			
			m_oPaint.setStrokeWidth(4);				
			canvas.save();
			canvas.translate(x_origin, y_origin);			
			for(int i=0;i<scale;i++){						 
				 canvas.rotate(360/scale);
				 canvas.drawLine(0, y_p, 0, y_p-16, m_oPaint);	
			}
			canvas.restore();
			
//			m_oPaint.setColor(Color.RED);			
			m_oPaint.setStrokeWidth(2);				
			canvas.save();
			canvas.translate(x_origin, y_origin);
			float count2 = scale*10;
			for(int i=0;i<count2;i++){						 
				 canvas.rotate(360/count2);
				 canvas.drawLine(0, y_p, 0, y_p-10, m_oPaint);	
			}
			canvas.restore();
			
	
			canvas.save();	
			canvas.translate(x_origin, y_origin);
			m_oPaint.setColor(m_cLineColor);
			m_oPaint.setAntiAlias(true); // ���û��ʵľ��Ч��
			m_oPaint.setTextSize(16);
			m_oPaint.setStyle(Paint.Style.FILL);
			Path path = new Path();
			path.addArc(new RectF(-x_p, -y_p, x_p, y_p), 0, 360);	
			canvas.rotate(90);
			canvas.rotate(-360/scale);
			for(int i=0;i<scale;i++){						 
				canvas.rotate(360/scale);
//				 if(i%2==0){ 
					 float label_value = maxValue/scale *(float)i;
					 DecimalFormat decimalFloat = new DecimalFormat("0"); //floatС���㾫�ȴ���
					 String str = decimalFloat.format(label_value);
					 canvas.drawTextOnPath(str, path, -5, 30, m_oPaint);
//				 }
				 
			}
			canvas.restore(); 
			
	      //����ָ��
			m_oPaint.setColor(Color.RED);			
			m_oPaint.setStrokeWidth(3);			
			canvas.save();
			canvas.translate(x_origin, y_origin);							 
			canvas.rotate(angle);
			canvas.drawLine(0, 0, 0, y_p*(float)0.68, m_oPaint);		
			
			
			canvas.restore();
			
				 
		
			m_oPaint.setColor(m_cLineColor); // ����䵥ɫ		
			m_oPaint.setStyle(Paint.Style.FILL);   
			canvas.drawCircle(x_origin, y_origin, 10,  m_oPaint);
					
			

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
        else if ("BackgroundColor".equals(strName)) {  //����ɫ
        	m_cBackgroundColor = Color.parseColor(strValue);
        }
        else if ("BorderColor".equals(strName)) {     //ǰ��ɫ СԲ��ɫ
        	m_cBorderColor = Color.parseColor(strValue);
        }
        else if ("FillColor".equals(strName)) {
        		m_cSingleFillColor = Color.parseColor(strValue);
        		m_cStartFillColor = m_cSingleFillColor;
        }
        else if ("LineColor".equals(strName)) {
        	m_cLineColor = Color.parseColor(strValue);
        }
        else if ("Expression".equals(strName)) 
       	 	m_strExpression = strValue;
        else if ("ColorExpression".equals(strName))
        	m_strColorExpression = strValue;
        else if ("WarmPer".equals(strName)){
           	warnPer = Float.parseFloat(strValue);
        } 
        else if ("WarmPerColor".equals(strName)){
        	warnPerColor = Color.parseColor(strValue);
        }
        else if ("MaxValue".equals(strName))
        	maxValue = Integer.parseInt(strValue);
        else if ("scale".equals(strName))
        	scale = Integer.parseInt(strValue);
        else if ("mode".equals(strName))
        	if("".equals(strValue)){ 
        		
        	}else{
        		mode = Integer.parseInt(strValue);
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
 //    	 	parseFontcolor(oRealTimeData.strData);   //������ֵ��ɫ���ʽ fjw add	    
     	 	if("".equals(oRealTimeData.strData)) return false;
     	 	data_value = Float.parseFloat(oRealTimeData.strData);
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
	int m_cBackgroundColor = 0xFFFFFFFF;
	int m_cBorderColor = 0xFF000000;

	int m_cFillColor = 0xFFF2C0FF;
	int m_cLineColor = 0xFF000000;
	boolean m_bIsDashed = false;
	String m_strExpression = "Binding{[Value[Equip:114-Temp:173-Signal:1]]}";
	String m_strColorExpression = ">20[#FF009090]>30[#FF0000FF]>50[#FFFF0000]>60[#FFFFFF00]";
	
	int m_cSingleFillColor = 0xFF0000FF;
	int m_cStartFillColor = 0x00000000;
	float[] m_arrGradientColorPos = null;
	int[] m_arrGradientFillColor = null;
	boolean m_bIsHGradient = false; // ˮƽ����
	 
	float maxValue = 100;  //���̵����ֵ
	int scale = 5;      //���̵Ŀ̶�
	int mode = 1;        //������ʽ
	float data_value = 10; //Ŀǰ�ı�����ֵ
	int m_nBorderWidth = 10;  //�������
	int m_nfillWidth = 30;  //����Բ�����
	String str_value="";
	float warnPer = (float)0.8;  //�澯Բ����ֵ��ʼ����
	int warnPerColor = 0xFFFF0000;   //�澯Բ����ɫ
	
	MainWindow m_rRenderWindow = null;
	Paint m_oPaint = null;  
	RectF m_rRectF1 = null;
	RectF m_rRectF2 = null;
	RectF m_rRectF3 = null;
	Rect m_rBBox = null;
	public boolean m_bneedupdate = true;
	String oldSignalValue = "";
}
