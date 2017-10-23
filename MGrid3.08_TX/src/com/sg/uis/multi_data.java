package com.sg.uis;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.mgrid.main.MainWindow;
import com.sg.common.Calculator;
import com.sg.common.IObject;
import com.sg.common.SgRealTimeData;
import com.sg.common.UtExpressionParser.stIntervalExpression;

/** �����ݿؼ� fjw test */
public class multi_data extends View implements IObject {
	public multi_data(Context context) {  
        super(context);
        this.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				return true;
			}
        });
        m_oPaint = new Paint(); 
        m_rBBox = new Rect();
        strValue_list = new ArrayList<String>();
    }
	
	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas) {
 //   	Log.e("multi_data-onDraw","into");	
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;

		
		m_oPaint.setColor(m_cSingleFillColor); 
		m_oPaint.setAntiAlias(true); // ���û��ʵľ��Ч��
		m_oPaint.setStrokeWidth(40);
		m_oPaint.setStyle(Paint.Style.FILL);
		
		
       
        int nWidth = (int) (((float)(m_nWidth) / (float)MainWindow.FORM_WIDTH) * (m_rRenderWindow.VIEW_RIGHT - m_rRenderWindow.VIEW_LEFT));
		int nHeight = (int) (((float)(m_nHeight) / (float)MainWindow.FORM_HEIGHT) * (m_rRenderWindow.VIEW_BOTTOM - m_rRenderWindow.VIEW_TOP));

		//��������Բ
		RectF rect = new RectF(0, 0, nWidth/10*8, nHeight); //80%�������ʾԲ��
		 canvas.drawArc(rect, //������ʹ�õľ��������С   
		            0,  //��ʼ�Ƕ�   
		            360, //ɨ���ĽǶ�   
		            true, //�Ƿ�ʹ������   
		            m_oPaint); 
//		 Log.e("multi_data-onDraw","�ؼ�id:"+m_strID+"--into  3333");		
       	
        m_oPaint.setStyle(Style.FILL);  //���û���Ϊʵ��     	       
        m_oPaint.setTextSize(40); // ���������С

        //��������������
        int i = 0;
        float angle = 0;
        Iterator<String> iter = strValue_list.iterator();
        while(iter.hasNext()){
        	String value = iter.next(); //�������
        	if("".equals(value)) break;
        	float data = Float.parseFloat(value);   
        	float data1 = data*360;
        	m_oPaint.setColor(m_DataColor[i]); 	//���û�����ɫ
    		canvas.drawArc(rect, //������ʹ�õľ��������С   
    				angle,  //��ʼ�Ƕ�   
    				data1, //ɨ���ĽǶ�   
 		            true, //�Ƿ�ʹ������   
 		            m_oPaint); 
    		 angle = angle+data1;
    		 float r_lenth = (float)nWidth/10*2/5;
    		 float r_x = (float)nWidth/10*8+4;
    		 float r_y = (float)nHeight/20 + (nHeight/20 +r_lenth)*i;    		
    		canvas.drawRect(r_x, r_y, r_x+r_lenth, r_y+r_lenth, m_oPaint);  // ������
    		m_oPaint.setTextSize(r_lenth); // ���������С
    		DecimalFormat decimalFloat = new DecimalFormat("0.00"); //floatС���㾫�ȴ���
    		String strValue= decimalFloat.format(data*100);
    		canvas.drawText(strValue, r_x+r_lenth*3/2, r_y+r_lenth, m_oPaint);   // ����ǩ
        	i++;
        }
       	
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
	
	public View getView() {
		return this;
	}
	
	public int getZIndex()
	{
		return m_nZIndex;
	}
	
	@Override
	public void addToRenderWindow(MainWindow rWin) {
		m_rRenderWindow = rWin;
		rWin.addView(this);
	}
	
	@Override
	public void removeFromRenderWindow(MainWindow rWin) {
		m_rRenderWindow = null;
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
        else if ("ForeColor".equals(strName)) {
        		m_cSingleFillColor = Color.parseColor(strValue);
        }
        else if ("DataColor".equals(strName)) {
        	String strData[] = strValue.split("\\+|\\|");  
        	for(int i=0;i<strData.length;i++){
        		m_DataColor[i] = Color.parseColor(strData[i]);
        	}
       }
        else if ("IsDashed".equals(strName)) {
        	m_bIsDashed = Boolean.parseBoolean(strValue);
        }
        else if ("Radius".equals(strName)) 
        	m_fRadius = Float.parseFloat(strValue);
        else if ("Effect".equals(strName))
        	m_strEffect = strValue;
        else if ("Expression".equals(strName)) {
        	m_strMultiExpression = strValue;
        	
        }
	}

	@Override
	public void initFinished()
	{
	}

	public String getBindingExpression() {
		return m_strMultiExpression;
	}
	
	// �豸����
	public void updateWidget() {
		this.invalidate();
	}
	
	@Override
	public boolean updateValue()
	{
		m_bneedupdate = false;
		if (m_strMultiExpression == null) 
			return false;
		//�������
		SgRealTimeData oRealTimeData = m_rRenderWindow.m_oShareObject.m_mapRealTimeDatas.get(this.getUniqueID());
		if (oRealTimeData == null)
			return false;
		
		String strValue = oRealTimeData.strValue;
		if (strValue == null || "".equals(strValue) == true)
			return false;
		Log.e("multi_data-updataValue����ֵ��",strValue);		
        // ���ݱ仯��ˢ��ҳ��
        if (old_string.equals(strValue) == false) {       	
        	old_string = strValue;     //������ֵ�����´αȽ� 
        	if("".equals(old_string)) return false;
        	if(old_string==null) return false;
        	String buf[] = new String[30];
   //     	buf = old_string.split("\\+");  //�ָ� . | + *��Ҫ��˫��б��
        	buf = old_string.split("\\|");  //�ָ� . | + *��Ҫ��˫��б��
        	value_nember = buf.length; //���������Ŀ  ��ȡ��ȷ
  //      	Log.e("multi_data-updataValue��buf��", buf[0]);
  //      	Log.e("multi_data-updataValue��������Ŀ��", String.valueOf(value_nember));
        	strValue_list.clear();
        	double all=0;//�ܺ�
        	double arg[] =new double[value_nember];
        	for(int i=0;i<value_nember;i++){
        		String str = buf[i];
        		Calculator cal = new Calculator();
        		double d = cal.calculate(str);//����ok
        		all+=d;
        		arg[i]=d;

        	}
        	for (int i = 0; i < arg.length; i++) {
        		String str_value = String.valueOf(arg[i]/all);
        		strValue_list.add(str_value);
			}
			return true;
        }
		
	        
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

	public Rect getBBox() {
		return m_rBBox;
	}
	
// params:
	String m_strID = "";
	String m_strType = "";
    int m_nZIndex = 3;
	int m_nPosX = 94;
	int m_nPosY = 9;
	int m_nWidth = 200;
	int m_nHeight = 150;
	float m_fAlpha = 1.0f;
	float m_fRotateAngle = 0.0f;
	int m_cSingleFillColor = 0x00000000;
	int[]  m_DataColor= new int[50];
	boolean m_bIsDashed = false;
	float m_fRadius = 0.0f;
	String m_strStateExpression = "";
	String m_strEffect = "";
	String m_strMultiExpression = "";
	boolean m_bIsHGradient = true; // ˮƽ����
	MainWindow m_rRenderWindow = null;
	
	int m_nSignalValue = -1;
	//stMathExpression m_oMathExpression = null;
	stIntervalExpression m_oColorIntervalExpression;
	String old_string = "";
	List<String> strValue_list = null;  //�����������
	int value_nember = 0;  //���ݸ���
	
	Paint m_oPaint = null;  
	Rect m_rBBox = null;
	
	public boolean m_bneedupdate = true;
}
