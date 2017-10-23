
package com.sg.uis;



import com.sg.common.SgRealTimeData;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;

import com.mgrid.main.MainWindow;
import com.sg.common.IObject;


/**д�� չʾ����**/
//author��fjw0312
//made time��2015.12.29
//�ÿؼ��ɼ�
public class test_quxian extends View implements IObject {
	
	public test_quxian(Context context) {
		super(context);
		
		m_rBBox = new Rect(); //�½�һ�����������ռ�		
		m_oPaint = new Paint();   //���軭����ռ�
		m_2Paint = new Paint();   //���軭����ռ� ���������
		m_oPaint.setTextSize(m_fFontSize); //���û���������С
		m_oPaint.setColor(m_nFontColor);   //���û�����ɫ
		m_oPaint.setAntiAlias(true); // ���û��ʵľ��Ч��
		m_oPaint.setStyle(Paint.Style.STROKE); //���û��ʷ��
		max_value = new float[4];
		for(int i=0;i<4;i++){
			max_value[i] = 1;
		}
		
		map_Htime_vlaue = new HashMap<Integer,String>();  //����Сʱ��ʱ�䡪����ֵ
		map_Dtime_vlaue = new HashMap<Integer,String>();  //����ÿ���ʱ�䡪����ֵ
		map_Mtime_vlaue = new HashMap<Integer,String>();  //����ÿ�µ�ʱ�䡪����ֵ
		map_Ytime_vlaue = new HashMap<Integer,String>();  //����ÿ���ʱ�䡪����ֵ
		Htimelist = new ArrayList<Integer>();  //����Сʱ��ʱ��
		Dtimelist = new ArrayList<Integer>();  //����ÿ���ʱ��
		Mtimelist = new ArrayList<Integer>();  //����ÿ�µ�ʱ��
		Ytimelist = new ArrayList<Integer>();  //����ÿ���ʱ��
		//�������ߵ���ֵ
		for(int i=0;i<300;i+=20){
			Htimelist.add(i);
			float v = (float)0.6+ (float)0.0005*i;
			String s= String.valueOf(v);
			map_Htime_vlaue.put(i, s);
		}
		for(int i=300;i<900;i+=20){
			Htimelist.add(i);
			float v = (float)0.75 - (float)0.0005*(i-300);
			String s= String.valueOf(v);
			map_Htime_vlaue.put(i, s);
		}
		for(int i=900;i<2000;i+=20){
			Htimelist.add(i);
			float v = (float)0.45 + (float)0.0004*(i-900);
			String s= String.valueOf(v);
			map_Htime_vlaue.put(i, s);
		}
		for(int i=2000;i<3600;i+=20){
			Htimelist.add(i);
			float v = (float)0.89 - (float)0.0003*(i-2000);
			String s= String.valueOf(v);
			map_Htime_vlaue.put(i, s);
		}
		
		for(int i=0;i<200;i+=10){
			Dtimelist.add(i);
			float v = (float)0.6+ (float)0.0005*i;
			String s= String.valueOf(v);
			map_Dtime_vlaue.put(i, s);
		}
		for(int i=200;i<250;i+=10){
			Dtimelist.add(i);
			float v = (float)0.7+ (float)0.004*(i-200);
			String s= String.valueOf(v);
			map_Dtime_vlaue.put(i, s);
		}
		for(int i=250;i<280;i+=10){
			Dtimelist.add(i);
			float v = (float)0.9- (float)0.01*(i-250);
			String s= String.valueOf(v);
			map_Dtime_vlaue.put(i, s);
		}
		for(int i=280;i<1000;i+=10){
			Dtimelist.add(i);
			float v = (float)0.6- (float)0.0001*(i-280);
			String s= String.valueOf(v);
			map_Dtime_vlaue.put(i, s);
		}
		for(int i=1000;i<1010;i+=10){
			Dtimelist.add(i);
			float v = (float)0.528- (float)0.03*(i-1000);
			String s= String.valueOf(v);
			map_Dtime_vlaue.put(i, s);
		}
		for(int i=1010;i<1014;i+=10){
			Dtimelist.add(i);
			float v = (float)0.228+ (float)0.08*(i-1010);
			String s= String.valueOf(v);
			map_Dtime_vlaue.put(i, s);
		}
		for(int i=1014;i<1440;i+=10){
			Dtimelist.add(i);
			float v = (float)0.548+ (float)0.0002*(i-1014);
			String s= String.valueOf(v);
			map_Dtime_vlaue.put(i, s);
		}
		
		for(int i=0;i<31;i++){
			Mtimelist.add(i);
			float v = (float)0.6;
			if(i==6 || i==13 ||i==18||i==22) v = v+(float)0.18;
			if(i==2||i==16||i==21) v = v-(float)0.26;
			if(i==28) v = v+(float)0.09;
			String s= String.valueOf(v);
			map_Mtime_vlaue.put(i, s);
		}
		for(int i=0;i<12;i++){
			Ytimelist.add(i);
			float v = (float)0.6;
			if(i==2) v = v-(float)0.06;
			if(i==6) v = v+(float)0.10;
			if(i==7) v = v+(float)0.12;
			if(i==8) v = v+(float)0.11;
			if(i==11) v = v-(float)0.09;
			if(i==11) v = v-(float)0.1;
			String s= String.valueOf(v);
			map_Ytime_vlaue.put(i, s);
		}

		
		//����ѡ��Ŧ�� ��ť��4��
		ridobuttons = new RadioButton[4];
		ridobuttons[0] = new RadioButton(context);
		ridobuttons[0].setText("1 h");
		ridobuttons[0].setChecked(true);
		ridobuttons[1] = new RadioButton(context);
		ridobuttons[1].setText("24 h");
		ridobuttons[2] = new RadioButton(context);
		ridobuttons[2].setText("1 mon");
		ridobuttons[3] = new RadioButton(context);
		ridobuttons[3].setText("1 year");
		for(int i=0;i<4;i++){
			ridobuttons[i].setTextColor(Color.BLACK);
			ridobuttons[i].setOnClickListener(l);				
/*				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					String strText = (String) ((RadioButton) arg0).getText();
					if("1 h".equals(strText)){
						mode = 0;
						
					}else if("24 h".equals(strText)){
						mode = 1;
					}else if("1 mon".equals(strText)){
						mode = 2;
					}else if("1 year".equals(strText)){
						mode = 3;
					}
					ridobuttons[mode].setChecked(true);
					
				}
			});
*/		}			
	}
	private OnClickListener l = new OnClickListener() {				
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			String strText = (String) ((RadioButton) arg0).getText();
			for(int i=0;i<4;i++)
				ridobuttons[i].setChecked(false);
			if("1 h".equals(strText)){
				mode = 0;				
			}else if("24 h".equals(strText)){
				mode = 1;
			}else if("1 mon".equals(strText)){
				mode = 2;
			}else if("1 year".equals(strText)){
				mode = 3;
			}
			ridobuttons[mode].setChecked(true);
			updateWidget(); //��view ��������draw()��ֻ��draw��view
		}
	};

	@Override //��������
	protected void onDraw(Canvas canvas) {
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;
		
		Log.w("SignalCurve","into onDraw!");
		//��������
//		m_oPaint.setColor(Color.WHITE);   //���û�����ɫ
//		m_oPaint.setTextSize(25); //���û���������С
//		canvas.drawText("��̬��ʷ����",m_nHeight/2, 25, m_oPaint); // ��y��̶�
		
		m_oPaint.setColor(m_nLineColor);   //���û�����ɫ
		m_oPaint.setTextSize(20); //���û���������С
		int jg = 40;  //����������صľ���
		int kel = 5;  //�̶ȳ���
		//����������
		canvas.drawLine(jg, m_nHeight-jg, m_nWidth-jg, m_nHeight-jg, m_oPaint); // ����x��
		canvas.drawLine(jg, m_nHeight-jg, jg, jg, m_oPaint);    // ����y��
		//�����̶ȵ�
		int num = 12+1;
		if(mode==1){
			num = 24+1;
		}else if(mode==2){
			num = 31+1;
		}else if(mode==3){
			num = 12+1;
		}
		int x_unit = (m_nWidth-2*jg-jg/2)/(num-1);  //x���������Ч����m_nWidth-2*jg-jg/2
		int y_unit = (m_nHeight-2*jg-jg/2)/10; //y���������Ч����m_nHeight-2*jg-jg/2
		for(int i=0;i<11;i++){
			canvas.drawLine(jg, m_nHeight-jg-y_unit*i, jg+kel, m_nHeight-jg-y_unit*i, m_oPaint); // ��y��̶���
		}
		for(int i=0;i<num;i++){
			canvas.drawLine(x_unit*i+jg, m_nHeight-jg, x_unit*i+jg, m_nHeight-jg-kel, m_oPaint); // ��x��̶���
		}
		
		//y�����ֵ���ֵ
		float y_max = max_value[mode]+max_value[mode]/10*2; //Ϊ���ֵ��120%
		float y_p = y_max/10;
		
		//���̶ȱ�ǩ
		m_oPaint.setTextSize(12); // ���������С
		for(int i=0;i<11;i++){   //����Y��
			DecimalFormat decimalFloat = new DecimalFormat("0.00"); //floatС���㾫�ȴ���
			 String str_yp = decimalFloat.format(y_p*i);
			canvas.drawText(str_yp, jg/8, m_nHeight-jg-y_unit*i, m_oPaint); // ��y���ǩ
			decimalFloat = null;
		}
		for(int i=0;i<num;i++){  //����x��			
//			canvas.drawText(x_markLine[mode][i], x_unit*i+20, m_nHeight-10, m_oPaint); // ��x���ǩ
			canvas.drawText(x_markLine[mode][i], x_unit*i+20, m_nHeight-jg/2, m_oPaint); // ��x���ǩ
		}
		
		//������ֵ��
		
		m_2Paint.setColor(m_nFontColor);   //���û�����ɫ      
		m_2Paint.setStyle(Style.FILL); 	//���û���Ϊʵ��
		m_2Paint.setStrokeWidth((float)2.0);    //�����������
		HashMap<Integer,String> map = new HashMap<Integer,String>();
		List<Integer> time_list = new ArrayList<Integer>();
		float xv_unit = 0;
		float yv_unit = (m_nHeight-2*jg-jg/2)/y_max;
		if(mode==0){
			map = map_Htime_vlaue;
			time_list = Htimelist;
			xv_unit = (float)(m_nWidth-2*jg-jg/2)/3600;			
		}
		else if(mode==1){
			map = map_Dtime_vlaue;
			time_list = Dtimelist;
			xv_unit = (float)(m_nWidth-2*jg-jg/2)/60/24;
//			Log.e("AutoSigList-onDraw-ģʽ1","map�����ȣ�"
//			+String.valueOf(map.size())+"list�����ȣ�"+String.valueOf(time_list.size()));
		}
		else if(mode==2){
			map = map_Mtime_vlaue;
			time_list = Mtimelist;
			xv_unit = (float)(m_nWidth-2*jg-jg/2)/31;
//			Log.e("AutoSigList-onDraw-ģʽ2","map�����ȣ�"
//					+String.valueOf(map.size())+"list�����ȣ�"+String.valueOf(time_list.size()));
		}
		else if(mode==3){
			map = map_Ytime_vlaue;
			time_list = Ytimelist;
			xv_unit = (float)(m_nWidth-2*jg-jg/2)/12;
//			Log.e("AutoSigList-onDraw-ģʽ3","map�����ȣ�"
//					+String.valueOf(map.size())+"list�����ȣ�"+String.valueOf(time_list.size()));
		}
		float pre_x = 0;
		float pre_y = 0;
		//��������
		Iterator<Integer> keylist = time_list.iterator();
		while(keylist.hasNext()){
			int ii_time = keylist.next();		 //���ʱ��
			String s_value = map.get( ii_time );  //�����ֵ			
			float i_value = Float.parseFloat(s_value);
			
			float now_x = jg+ii_time*xv_unit;
			float now_y = m_nHeight-jg-i_value*yv_unit;
			
	//		canvas.drawPoint(now_x, now_y, m_oPaint);
			canvas.drawCircle(now_x, now_y,1, m_oPaint); // ������ֵ��	
			
			if(pre_x!=0 || pre_y!=0)
				canvas.drawLine(pre_x,pre_y,now_x,now_y,m_2Paint);  //�������ӳ�����
			pre_x = now_x;
			pre_y = now_y;
			Log.e("AutoSigList-onDraw","map�����ȣ�"
					+String.valueOf(map.size())+"list�����ȣ�"+String.valueOf(time_list.size()));
			
		}
		map = null;
		time_list = null;  
		
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
			for (int i = 0; i < 4; i++)
				ridobuttons[i].layout(nX + i * nWidth / 4, nY, nX + i * nWidth / 4 + nWidth / 4, nY+20);
		}
	}
	
	@Override//���ÿؼ���ӽ�����ҳ��
	public void addToRenderWindow(MainWindow rWin) {
		m_rRenderWindow = rWin;
		rWin.addView(this);
		for (int i = 0; i < ridobuttons.length; i++)
			rWin.addView(ridobuttons[i]);
//			rWin.addView(ridobuttons[1]);
//			rWin.addView(ridobuttons[2]);
//			rWin.addView(ridobuttons[4]);
	}
	
	@Override//���ÿؼ�����ҳ���Ƴ�
	public void removeFromRenderWindow(MainWindow rWin) {  
		m_rRenderWindow = null;
		for (int i = 0; i < ridobuttons.length; ++i)
			rWin.removeView(ridobuttons[i]);
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
        else if ("mode".equals(strName)) {
        	if("".equals(strValue)) mode = 0;
        	else mode = Integer.parseInt(strValue);
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
		
		
		Log.w("SignalCurve","into updateValue!");     
        return true;  //�пؼ������仯��Ҫ�仯����view��������text����ͼ�� ��Ҫ����true;
	}
//������������ֵ
	public float get_max_vlaue(HashMap<Integer,String> map){
		float max_value = 0;
		//��������
		Iterator<Integer> keylist = map.keySet().iterator();
		while(keylist.hasNext()){
			String g_value = map.get( keylist.next()  );
			float f_value = Float.parseFloat(g_value);
			if(f_value>max_value)
				max_value = f_value;
		}
		return max_value;
	}
//���������ƽ��ֵ
	public float get_aver(HashMap<Integer,String> map){
		float aver_value = 0;
		float num = 0;
		int i = 0;
		//��������
		Iterator<Integer> keylist = map.keySet().iterator();
		while(keylist.hasNext()){
			String g_value = map.get( keylist.next() );
			float f_value = Float.parseFloat(g_value);
			if(f_value==0) continue;
			num+=f_value;
			i++;
		}
		aver_value = num/i;
		
		return aver_value;
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
	Paint m_2Paint = null; //�½����� �����
	MainWindow m_rRenderWindow = null;  //�½���ҳ �����
	public boolean m_bneedupdate = true; //�½���Ҫ�ؼ����ݸ��±�־�ı���
	RadioButton[] ridobuttons;
 
	
//X������������ǩ
	String x_markLine[][] = {{"0","5","10","15","20","25","30","35","40","45","50","55","60"},
	{"0","1:00","2:00","3:00","4:00","5:00","6:00","7:00","8:00","9:00","10:00","11:00",
		    "12:00","13:00","14:00","15:00","16:00","17:00","18:00","19:00","20:00",
		    "21:00","22:00","23:00","24:00"},
	{"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15",
			"16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"},
	{"0","1","2","3","4","5","6","7","8","9","10","11","12"}};
	int mode = 0; //��������ģʽ 0��1h  1:1��  2��һ����   3��һ��
	HashMap<Integer,String> map_Htime_vlaue = null;  //����Сʱ��ʱ�䡪����ֵ
	HashMap<Integer,String> map_Dtime_vlaue = null;  //����ÿ���ʱ�䡪����ֵ
	HashMap<Integer,String> map_Mtime_vlaue = null;  //����ÿ�µ�ʱ�䡪����ֵ
	HashMap<Integer,String> map_Ytime_vlaue = null;  //����ÿ���ʱ�䡪����ֵ
	List<Integer> Htimelist = null;  //����Сʱ��ʱ��
	List<Integer> Dtimelist = null;  //����ÿ���ʱ��
	List<Integer> Mtimelist = null;  //����ÿ�µ�ʱ��
	List<Integer> Ytimelist = null;  //����ÿ���ʱ��
		
		
	float max_value[] = null;
	public  static int old_min = 0;
		
}
