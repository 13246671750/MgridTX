
package com.sg.uis;

import java.io.IOException;

import com.sg.common.SgRealTimeData;

import java.io.InputStream;
import java.text.DecimalFormat;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.mgrid.main.MainWindow;
import com.sg.common.IObject;
import com.sg.common.MutiThreadShareObject;
import com.sg.common.UtExpressionParser.stExpression;
import data_model.save_curve_signal;
import data_model.save_multipoint_signal;

/**��ɼ���ģʽ��ѡ�Ķ�̬�ź�����**/
//author��fjw0312
//made time��2015.8.18
public class SignalCurves extends View implements IObject {
	
	public SignalCurves(Context context) {
		super(context);
		
		m_rBBox = new Rect(); //�½�һ�����������ռ�		
		m_oPaint = new Paint();   //���軭����ռ�
		m_oPaint.setTextSize(m_fFontSize); //���û���������С
		m_oPaint.setColor(m_nFontColor);   //���û�����ɫ
		m_oPaint.setAntiAlias(true); // ���û��ʵľ��Ч��
		m_oPaint.setStyle(Paint.Style.STROKE); //���û��ʷ��
		
		//����ѡ��Ŧ�� ��ť��3��
		ridobuttons = new RadioButton[3];
		ridobuttons[0] = new RadioButton(context);
		ridobuttons[0].setText("1 h");
		ridobuttons[0].setChecked(true);
		ridobuttons[1] = new RadioButton(context);
		ridobuttons[1].setText("24 h");
		ridobuttons[2] = new RadioButton(context);
		ridobuttons[2].setText("1 mon");
		for(int i=0;i<3;i++){
			ridobuttons[i].setTextColor(Color.BLACK);
			ridobuttons[i].setOnClickListener(l);	
		}

	}
	
	private OnClickListener l = new OnClickListener() {				
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			String strText = (String) ((RadioButton) arg0).getText();
			for(int i=0;i<3;i++)
				ridobuttons[i].setChecked(false);
			if("1 h".equals(strText)){
				mode = 1;				
			}else if("24 h".equals(strText)){
				mode = 2;
			}else if("1 mon".equals(strText)){
				mode = 3;
			}
			ridobuttons[mode-1].setChecked(true);
			setMode(mode);
			updateValue();  //��������
			updateWidget(); //��view ��������draw()��ֻ��draw��view
		}
	};

	@Override //��������
	protected void onDraw(Canvas canvas) {
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;
		
//		Log.e("SignalCurves","into onDraw!"+String.valueOf(mode));
		

		axis_pad = 40;
		axis_x_start = axis_pad;
		axis_x_end = m_nWidth-axis_pad;
		axis_y_start = m_nHeight-axis_pad;
		axis_y_end = axis_pad+20;
		axis_x_lenth = axis_x_end - axis_x_start;
		axis_y_lenth= axis_y_start - axis_y_end;
		axis_x_unit = axis_x_lenth/num; 
	    axis_y_unit = axis_y_lenth/10;
	    m_oPaint.setColor(Color.RED);   //���û���������ɫ
//	    canvas.drawCircle(axis_x_start, axis_y_start,5, m_oPaint); //����4����׼��
//	    canvas.drawCircle(axis_x_start, axis_y_end,5, m_oPaint); //����4����׼��
//	    canvas.drawCircle(axis_x_end, axis_y_start,5, m_oPaint); //����4����׼��
	    
		m_oPaint.setColor(m_nLineColor);   //���û���������ɫ
//		m_oPaint.setColor(Color.BLACK);   //���û���������ɫ
		//����������
		canvas.drawLine(axis_x_start, axis_y_start, axis_x_end+20, axis_y_start, m_oPaint); // ����x��
		canvas.drawLine(axis_x_start, axis_y_start, axis_x_start, axis_y_end-20, m_oPaint); // ����y��
		//�����̶ȵ�
		for(int i=0;i<11;i++){
			canvas.drawLine(axis_x_start, axis_y_start-axis_y_unit*i, axis_x_start+5, axis_y_start-axis_y_unit*i, m_oPaint); // ��y��̶���
		}
		for(int i=0;i<num+1;i++){
			canvas.drawLine(axis_x_start+axis_x_unit*i, axis_y_start, axis_x_start+axis_x_unit*i, axis_y_start-5, m_oPaint); // ��x��̶���
		}
		
//		Log.e("SignalCurves->onDraw>>axis_x_lenth:", Float.toString(axis_x_lenth));
//		Log.e("SignalCurves->onDraw>>axis_y_lenth:", Float.toString(axis_y_lenth));
		//���̶ȱ�ǩ
		m_oPaint.setTextSize(12); // ���������С
		for(int i=0;i<11;i++){                      //y���ǩ
			if(axis_y_lenth<300){  //���������С ��ǩ���
				if(i%2==1)  continue; 
			}
			if( ("".equals(y_markLine[i]))||(y_markLine[i]==null) ) y_markLine[i] = "0";
			canvas.drawText(y_markLine[i],18, axis_y_start-axis_y_unit*i, m_oPaint); // ��y��̶�
		}
		for(int i=0;i<num;i++){					
			if(axis_x_lenth<200){ 
				if(i%5!=0)  continue; 
			}
			if(axis_x_lenth<400){
				if(i%4!=0)  continue; 
			}
			if(axis_x_lenth<600){
				if(i%2!=0)  continue;  
			}
			if("".equals(x_markLineTOw[i][0])||(x_markLineTOw[i][0]==null) ) x_markLineTOw[i][0] = " ";
			if("".equals(x_markLineTOw[i][1])||(x_markLineTOw[i][1]==null) ) x_markLineTOw[i][1] = "0";
			canvas.drawText(x_markLineTOw[i][0], axis_x_start+axis_x_unit*i, axis_y_start+10, m_oPaint); // ��x��̶�
			canvas.drawText(x_markLineTOw[i][1], axis_x_start+axis_x_unit*i, axis_y_start+22, m_oPaint); // ��x��̶�
		}
		//������ֵ��
		m_oPaint.setColor(m_nFontColor);   //���û�����ɫ
		m_oPaint.setStyle(Style.FILL); 	//���û���Ϊʵ��
		float pre_x = 0;
		float pre_y = 0;
		for(int i=0;i<num;i++){
			float value = sig_value[i];
			float node_x = axis_x_start+axis_x_unit*i;
			float node_y = axis_y_start-value;
			canvas.drawCircle(node_x, node_y,3, m_oPaint); // ������ֵ��
			//����
			if(i!=0){
				canvas.drawLine(pre_x,pre_y,node_x,node_y,m_oPaint);
			}
			pre_x = node_x;
			pre_y = node_y;
		}
		
		super.onDraw(canvas);
	}

	
	@Override //���Ƶװ�
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
			//����RadioButton�ĵװ�ռ�
//			for(int i = 0; i < ridobuttons.length; i++)
//				ridobuttons[i].layout(nX + (i+1) * nWidth / 4, nY, nX + (i+1) * nWidth / 4 + nWidth / 4, nY+20);
		}

	}
	 
	@Override//���ÿؼ���ӽ�����ҳ��
	public void addToRenderWindow(MainWindow rWin) {
		m_rRenderWindow = rWin;
		rWin.addView(this);
//		for(int i = 0; i < ridobuttons.length; i++)
//			rWin.addView(ridobuttons[i]);
	}
	
	@Override//���ÿؼ�����ҳ���Ƴ�
	public void removeFromRenderWindow(MainWindow rWin) {  
		m_rRenderWindow = null;
		rWin.removeView(this);
//		for(int i = 0; i < ridobuttons.length; i++)
//			rWin.removeView(ridobuttons[i]);
	} 
	
	//�����ؼ�����
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
        }
        else if ("Expression".equals(strName)) {
        	m_strExpression = strValue;
        }
        else if ("FontSize".equals(strName)) {
        	m_fFontSize= Float.parseFloat(strValue);
        	this.setFontSize(m_fFontSize);
        }
        else if ("FontColor".equals(strName)) {
        	m_nFontColor= Color.parseColor(strValue);
        	this.setFontColor(m_nFontColor);
        }
        else if ("LineColor".equals(strName)) {
        	m_nLineColor= Color.parseColor(strValue);
        	this.setLineColor(m_nLineColor);
        }
        else if ("BackgroundColor".equals(strName)) {
        	BackgroundColor = Color.parseColor(strValue);
        	this.setBackgroundColor(BackgroundColor);
        }
        else if ("mode".equals(strName)) {
        	mode = Integer.parseInt(strValue);
        	this.setMode(mode);
        }
	}

	//��øÿؼ�
	public View getView() {
		return this;
	}
	//��õװ巽��
	public Rect getBBox() {
		return m_rBBox;
	}	
	@Override //��ɳ�ʼ��������
	public void initFinished()
	{
	}
	
	//����ui �ؼ�id
	public void setUniqueID(String strID) {
		m_strID = strID;
	}
	//��ȡui �ؼ�id
	public String getUniqueID() {
		return m_strID;
	}
	//����ui �ؼ�����
	public void setType(String strType) {
		m_strType = strType;
	}
	//��ȡui �ؼ�����
	public String getType() {
		return m_strType;
	}
	//��ȡ�ؼ�����ͼ��
	public int getZIndex(){
		return m_nZIndex;
	}
	//��ȡ�ؼ����ʽ
	public String getBindingExpression() {
		return m_strExpression;
	}	
	//����ui�ؼ����ԣ�һЩ�������仯
	public void updateWidget() {
		this.invalidate(); //��view ��������draw()��ֻ��draw��view
	}
	@Override //�Ƿ���Ҫ����ˢ�±�־����
    public boolean needupdate()
    {
	    return m_bneedupdate;
    }	
	@Override //�Ƿ���Ҫ����ˢ�±�־����
    public void needupdate(boolean bNeedUpdate)
    {
	    m_bneedupdate = bNeedUpdate;
    }	
	@Override   //���ݲ������º��� ui���Ա仯���� 
	public boolean updateValue()
	{
		m_bneedupdate = false;
		//��ȡ�����̳߳� ��ʷ��������  ���ݻ�ȡ����
		save_multipoint_signal sig_class = new save_multipoint_signal();
		if(m_rRenderWindow.m_oShareObject.m_mapMultiPoint == null) return false;
		sig_class =	m_rRenderWindow.m_oShareObject.m_mapMultiPoint.get(this.getUniqueID());
		if(sig_class==null) return false;
		String x_buf[] = new String[num]; //����x��̶�
		float y_buf[] = new float[10];    //����y��̶�   float��
		float sig_buf[] = new float[num];  //�������ߵ���ֵ

		//��x��̶�  y��̶�  ���ߵ���ֵ ָ����뺯����ȡ��ֵ �����ؿ̶����ֵ
		float max_value = sig_class.get_curve(mode, x_buf, y_buf, sig_buf);
		float maxValue_f = ((int)max_value/10+1)*10;   //���������ֵ ��Ϊ10��������
		axis_y_per = axis_y_lenth/maxValue_f;
	//���ݸ�ֵ ok	
	
	//	for(int i=0;i<num;i++){
	//		Log.e("into SignalCurves-updateValue��ֵ��", Float.toString(y_buf[i]));
	//	}
		//����ת���̶� y��10���̶ȵ�
		for(int i=0;i<11;i++){
			DecimalFormat decimalFloat = new DecimalFormat("0"); //floatС���㾫�ȴ���
			y_markLine[i] = decimalFloat.format(maxValue_f/10*i);
		}
		
		for(int i=0;i<30;i++){
			sig_value[i] = 0;
		}
		//����ת���̶� x��̶ȵ�  Ӧ��1h�Ĳɼ�ʱ���ʽ���д���̫����
		for(int i=0;i<num;i++){
			if(x_buf[i]==null) x_buf[i]=" ";
			x_markLine[i] = x_buf[i];
			String a = x_markLine[i];
			if( (a.length()<8)&&(a.length()>1) ){
				x_markLineTOw[i][0] = " ";
				x_markLineTOw[i][1]= x_markLine[i];//��ȡ�ַ���
			}else{
				x_markLineTOw[i][0] = " ";//��ȡ�ַ���
				x_markLineTOw[i][1] = "0";
			}
			//�����ź�ֵ�Ķ�Ӧy�������
			sig_value[i] = axis_y_per * sig_buf[i];
	
		}
		//��ȡ�ɹ�
	//	for(int f=0;f<10;f++){
	//		Log.e("in SignalCurve��ӡ recive save_curve_value",sig_class.curve_time_buf[f]);
	//	}
		
		Log.w("SignalCurves","into updateValue!");
        
        return true;  //�пؼ������仯��Ҫ�仯����view��������text����ͼ�� ��Ҫ����true;
	}

//���ܻ���õı仯�ؼ�����
	public void setFontSize(float FontSize){
		m_fFontSize = FontSize;
	}
	public void setFontColor(int FontColor){
		m_nFontColor = FontColor;
	}
	public void setLineColor(int LineColor){
		m_nLineColor = LineColor;
	}
	public void setMode(int mode){
		switch(mode){
			case 1: num = 30; break;
			case 2: num = 24; break;
			case 3: num = 30; break;
			default:num = 30; break;
		}
	}
	
// params:
//xml �ؼ��Ĳ���
	String m_strID = "";     //�ؼ�id
	String m_strType = "";    //�ؼ�����
    int m_nZIndex = 20;     //�ؼ�id���
	int m_nPosX = 300;     //�ؼ�λ��location x y����ֵ
	int m_nPosY = 397;
	int m_nWidth = 150;    //�ؼ���С ���Ϳ� w h 
	int m_nHeight = 137;
	float m_fAlpha = 1.0f;   //�ؼ�ɫ�����
	String m_strExpression = null;//�ؼ����ʽ
	float m_fFontSize = 20.0f;	   //�ؼ����� �����С
	int m_nFontColor = 0xFFFF0000;  //�ؼ�����  ������ɫ 0xFFFF0000Ϊ��ɫ
	int m_nLineColor = 0xFFFF0000;  //�ؼ�����  (��������ɫ)������ɫ 0xFFFF0000Ϊ��ɫ
	int BackgroundColor = 0xFF000000; //�ؼ��װ���ɫ    0xFF000000Ϊ��ɫ
	
	RadioButton[] ridobuttons;
	
//�ؼ���Ҫ����
	Rect m_rBBox = null;   //�½��װ巽�� �����
	Paint m_oPaint = null; //�½����� �����
	MainWindow m_rRenderWindow = null;  //�½���ҳ �����
	public boolean m_bneedupdate = true; //�½���Ҫ�ؼ����ݸ��±�־�ı���
	public int mode = 1;
	public int num = 30;
//��������ز���
	float axis_pad = 40;
	float axis_x_start;
	float axis_x_end;
	float axis_y_start;
	float axis_y_end;
	float axis_x_lenth;
	float axis_y_lenth;
	float axis_x_unit;
	float axis_y_unit;
	float axis_y_per;
	
//�ر�ؼ���Ҫ����
	float sig_value[] = new float[30];  //�ź�ֵ����Ļ���س���
	String[][] x_markLineTOw = new String[30][2];
	String x_markLine[] = new String[30];
	String y_markLine[] = new String[11];
	
}
