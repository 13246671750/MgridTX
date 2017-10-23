
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

import com.mgrid.main.MainWindow;
import com.sg.common.IObject;
import com.sg.common.MutiThreadShareObject;
import com.sg.common.UtExpressionParser.stExpression;
import data_model.save_curve_signal;

/**��̬�ź�����**/
//author��fjw0312
//made time��2015.8.14
//�ÿؼ��ɼ�SignalCurve����ģ�͵����� �ɼ���Ϊ10����
public class SignalCurve extends View implements IObject {
	
	public SignalCurve(Context context) {
		super(context);
		
		m_rBBox = new Rect(); //�½�һ�����������ռ�		
		m_oPaint = new Paint();   //���軭����ռ�
		m_oPaint.setTextSize(m_fFontSize); //���û���������С
		m_oPaint.setColor(m_nFontColor);   //���û�����ɫ
		m_oPaint.setAntiAlias(true); // ���û��ʵľ��Ч��
		m_oPaint.setStyle(Paint.Style.STROKE); //���û��ʷ��
	}

	@Override //��������
	protected void onDraw(Canvas canvas) {
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;
		
		Log.w("SignalCurve","into onDraw!");
		//��������
		m_oPaint.setColor(Color.WHITE);   //���û�����ɫ
		m_oPaint.setTextSize(25); //���û���������С
		canvas.drawText("��̬��ʷ����",m_nHeight/2, 25, m_oPaint); // ��y��̶�
		
		m_oPaint.setColor(m_nLineColor);   //���û�����ɫ
		m_oPaint.setTextSize(20); //���û���������С
		//����������
		canvas.drawLine(40, m_nHeight-40, m_nWidth-40, m_nHeight-40, m_oPaint); // ����x��
		canvas.drawLine(40, m_nHeight-40, 40, 40, m_oPaint);    // ����y��
		//�����̶ȵ�
		int x_unit = (m_nWidth-80)/num;  //�����40pin
		int y_unit = (m_nHeight-90)/10; //��β����10
		for(int i=0;i<10;i++){
			canvas.drawLine(40, y_unit*i+50, 45, y_unit*i+50, m_oPaint); // ��y��̶���
		}
		for(int i=0;i<num;i++){
			canvas.drawLine(x_unit*i+40, m_nHeight-40, x_unit*i+40, m_nHeight-45, m_oPaint); // ��x��̶���
		}
		//���̶ȱ�ǩ
		m_oPaint.setTextSize(12); // ���������С
		for(int i=0;i<10;i++){
			if("".equals(y_markLine[i])||(".00".equals(y_markLine[i])) ) y_markLine[i] = "0.00";
			canvas.drawText(y_markLine[9-i],2, y_unit*(i+1)+50, m_oPaint); // ��y��̶�
		}
		for(int i=0;i<num;i++){
			if("".equals(x_markLineTOw[i][0]) ) x_markLineTOw[i][0] = " ";
			if("".equals(x_markLineTOw[i][1]) ) x_markLineTOw[i][1] = "0";
			canvas.drawText(x_markLineTOw[i][0], x_unit*i+20, m_nHeight-10, m_oPaint); // ��x��̶�
			canvas.drawText(x_markLineTOw[i][1], x_unit*i+20, m_nHeight-22, m_oPaint); // ��x��̶�
		}
		//������ֵ��
		m_oPaint.setColor(m_nFontColor);   //���û�����ɫ
		m_oPaint.setStyle(Style.FILL); 	//���û���Ϊʵ��
		for(int i=0;i<num;i++){
			float value = sig_value[i];
			canvas.drawCircle(x_unit*i+40, m_nHeight-40-value,4, m_oPaint); // ������ֵ��
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
		}
	}
	
	@Override//���ÿؼ���ӽ�����ҳ��
	public void addToRenderWindow(MainWindow rWin) {
		m_rRenderWindow = rWin;
		rWin.addView(this);
	}
	
	@Override//���ÿؼ�����ҳ���Ƴ�
	public void removeFromRenderWindow(MainWindow rWin) {  
		m_rRenderWindow = null;
		rWin.removeView(this);
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
		//��ȡ�����̳߳� ��ʷ��������
		save_curve_signal sig_class = new save_curve_signal();
		if(m_rRenderWindow.m_oShareObject.m_mapHisPoint == null) return false;
		sig_class =	m_rRenderWindow.m_oShareObject.m_mapHisPoint.get(this.getUniqueID());
		if(sig_class==null) return false;
		String x_buf[] = sig_class.curve_mark_xline; //��ȡx��̶�
		float y_buf[] = sig_class.curve_mark_yline;  //��ȡy��̶�   float��
		float sig_buf[] = sig_class.curve_value_buf;  //��ȡ���ߵ���ֵ
		float max_value = sig_class.max_markLineValue; //������̶�
		
		//����ת���̶�
		for(int i=0;i<10;i++){
			DecimalFormat decimalFloat = new DecimalFormat(".00"); //floatС���㾫�ȴ���
			y_markLine[i] = decimalFloat.format(y_buf[i]);
			x_markLine[i] = x_buf[i];
			if((x_markLine[i]!="0") && (x_markLine[i]!=null)){
				x_markLineTOw[i][0] = x_markLine[i].substring(0, 10);//��ȡ�ַ���
				x_markLineTOw[i][1] = x_markLine[i].substring(11);
			}else{
				x_markLineTOw[i][0] = " ";//��ȡ�ַ���
				x_markLineTOw[i][1]= "   0";
			}
			//�����ź�ֵ�Ķ�Ӧy�������
			sig_value[i] = (m_nHeight - 90)/max_value * sig_buf[i];
	
		}
		//��ȡ�ɹ�
	//	for(int f=0;f<10;f++){
	//		Log.e("in SignalCurve��ӡ recive save_curve_value",sig_class.curve_time_buf[f]);
	//	}
		
		Log.w("SignalCurve","into updateValue!");
        
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
	public int getCollectTime(){
		return collectTime;
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
	int m_nFontColor = 0xFFFF0000;  //�ؼ����� (���ߵ���ɫ) ������ɫ 0xFFFF0000Ϊ��ɫ
	int m_nLineColor = 0xFFFF0000;  //�ؼ�����  (��������ɫ)������ɫ 0xFFFF0000Ϊ��ɫ
	int BackgroundColor = 0xFF000000; //�ؼ��װ���ɫ    0xFF000000Ϊ��ɫ
	
//�ؼ���Ҫ����
	Rect m_rBBox = null;   //�½��װ巽�� �����
	Paint m_oPaint = null; //�½����� �����
	MainWindow m_rRenderWindow = null;  //�½���ҳ �����
	public boolean m_bneedupdate = true; //�½���Ҫ�ؼ����ݸ��±�־�ı���
//�ؼ��ر�ı�Ҫ����
	public int num = 10; //�ؼ��ɼ�����
	public int collectTime = 20; //���ߵ�ɼ�����  20s
	
//�ر�ؼ���Ҫ����
	float sig_value[] = {0,0,0,0,0,0,0,0,0,0};  //�ź�ֵ����Ļ���س���
	String[][] x_markLineTOw = {{" ","   0"},{" ","   0"},{" ","   0"},{" ","   0"},{" ","   0"},
			{" ","   0"},{" ","   0"},{" ","   0"},{" ","   0"},{" ","   0"}};
	String x_markLine[] = {"0","0","0","0","0","0","0","0","0","0"};
	String y_markLine[] = {"0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00"};
	
}
