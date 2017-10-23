package com.fjw.view;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;


//�Զ���view ������  ���ڿؼ���Ӧ��Ԫ��
public class Axis extends View{

	public Axis(Context context) {
		super(context); 
		// TODO Auto-generated constructor stub
		//��������
		paint = new Paint();
		paint.setTextSize(10); //���û���������С
		paint.setColor(LineColor); //���û�����ɫ
		paint.setAntiAlias(false); //���÷Ǿ�ݷ��
		paint.setStyle(Paint.Style.STROKE); //���û��ʷ��
	}
	//Fileds
 public int x_num,y_num;  //����Ŀ̶���
 public	float x_start,y_start; //������ԭ�����������
 public	float x_end,y_end;     //�������߽��������������
 public	float x_lenth,y_lenth;  //�������ߵ����س���
 public	float x_unit,y_unit;    //�������߿̶ȵ�λ���������� 
 public	float x_per_unit,y_per_unit;     //�������/��ֵ ������λ
 public	float x_pad=40,y_pad=30;    //�ؼ��ı߽�
 public ArrayList<String> x_markValue = new ArrayList<String>();
 public ArrayList<String> y_markValue = new ArrayList<String>();
 public int LineColor = 0xFFFFFF00;
 public int BackgroundColor = 0xFFFFFFFF;
 public int flag=0;
 
 private Paint paint;  //���廭��
	
	//��д onDraw ���� ����canvas����
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		
		//���õװ���ɫ
		canvas.drawColor(BackgroundColor);
		
		//���û���
		paint.setTextSize(10);
		paint.setColor(LineColor);
		
		//����������
		canvas.drawLine(x_start, y_start, x_end, y_start, paint); //x��
		canvas.drawLine(x_start, y_start, x_start, y_end, paint); //y��
		//�����̶�
		for(int i=0;i<x_num+1;i++){  //x��̶�
			float x = x_start + x_unit*i;
			canvas.drawLine(x, y_start, x, y_start-6, paint); 
		}
		for(int i=0;i<y_num+1;i++){  //y��̶� 
			float y = y_start - y_unit*i;
			canvas.drawLine(x_start, y, x_start+6, y, paint); 
		}
		//���û���
		paint.setTextSize(10);
		paint.setColor(LineColor);
		if(x_markValue==null) return;
		if(y_markValue==null) return;       
		//������ǩ
		for(int i=0;i<x_num+1;i++){  //x���ǩ
			String s=x_markValue.get(i);
			float m=s.length()/2;	//	�õ�x���ǩ���ַ����� ����2 
			if(m==0) m=0.7f;
			float x = x_start + x_unit*i-m*6;  //����ʹ�ַ��ֲ���x�̶����·�
			if(Integer.parseInt(s)!=0)
			canvas.drawText(s, x, y_start+10, paint); 
		}
		for(int i=0;i<y_num+1;i++){  //y���ǩ
			float y = y_start - y_unit*i;
			canvas.drawText(y_markValue.get(i), x_start-35, y, paint); 
		}
		
		
	}
	//����layout ���� ��������layout
	public void doLayout(boolean bool,int l,int t, int r,int b){
		this.layout(l, t, r, b);
	}
	//����invalidate()���� �����ػ�view��->view.onDraw()�Զ�����
	public void doInvalidate(){
		this.invalidate();
	} 
	//����addView()���� ������ͼ��ӵ�����ͼ��
	public boolean doAddToView(ViewGroup view){
		view.addView(this);
		return true;
	}
	//�ؼ����ݸ��� ������ں���
	//�����ֱ�Ϊ  �ؼ��Ŀ�� �߶�  x��Ŀ̶���  y��Ŀ̶���  x��ı�ǩ���ֵ
	public boolean upDataValue(float width, float height,int xNum,int yNum,float x_maxVlaue,float y_maxVlaue){
		x_start = x_pad;
		y_start = height-y_pad;
		x_end = width;
		y_end = 0;
		x_num = xNum;
		y_num = yNum;
		x_lenth = x_end - x_start;
		y_lenth = y_start - y_end;
		x_unit = x_lenth/(x_num+1);
		y_unit = y_lenth/(y_num+1);
		x_per_unit = (x_lenth-x_unit)/x_maxVlaue;
		y_per_unit = (y_lenth-y_unit)/y_maxVlaue;
		dealMarkVlaue(x_maxVlaue,y_maxVlaue);
		return true;
	}
	
	
	public boolean upDataValueFlag(float width, float height,int xNum,int yNum,float x_maxVlaue,float y_maxVlaue,int flag){
		x_start = x_pad;
		y_start = height-y_pad;
		x_end = width;
		y_end = 0;
		x_num = xNum;
		y_num = yNum;
		x_lenth = x_end - x_start;
		y_lenth = y_start - y_end;
		x_unit = x_lenth/(x_num+1);
		y_unit = y_lenth/(y_num+1);
		x_per_unit = (x_lenth-x_unit)/x_maxVlaue;
		y_per_unit = (y_lenth-y_unit)/y_maxVlaue;
		this.flag=flag;
		dealMarkVlaue(x_maxVlaue,y_maxVlaue);
		return true;
	}
	
	
	
	
	//�������ֵ����x y���ǩ������
	public void dealMarkVlaue(float x_maxValue,float y_maxValue){
		if(flag==0)
		{
		  float xUnit = x_maxValue/x_num;
		  x_markValue.clear();
		  for(int i=0;i<x_num+1;i++){			
				 x_markValue.add(String.valueOf((int)xUnit*i));		
		  }
		}
		else if(flag==1)
		{
		
		   long time=System.currentTimeMillis();
		   SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		   Date d=new Date(time);
		   String s=sd.format(d);
		   String dateTime = s.substring(0,10); 
		   int year = Integer.parseInt( dateTime.substring(0,4));
		   x_markValue.clear();
		   for(int i=year-x_num;i<=year;i++){			
				 x_markValue.add(String.valueOf(i));		
		  }
			
		}
	
		float yUnit = y_maxValue/y_num;
		y_markValue.clear();
		for(int i=0;i<y_num+1;i++){
    		DecimalFormat decimalFloat = new DecimalFormat("0"); //floatС���㾫�ȴ���
    		String strValue= decimalFloat.format(yUnit*i);
			y_markValue.add(strValue);			
		}
	}
	

}
