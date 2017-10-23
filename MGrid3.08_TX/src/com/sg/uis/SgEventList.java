package com.sg.uis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.mgrid.data.DataGetter;
import com.mgrid.data.EquipmentDataModel.Event;
import com.mgrid.main.MGridActivity;
import com.mgrid.main.MainWindow;
import com.sg.common.IObject;
import com.sg.common.UtExpressionParser;
import com.sg.common.UtExpressionParser.stBindingExpression;
import com.sg.common.UtExpressionParser.stExpression;
import com.sg.common.UtTable;
import comm_service.service;

import data_model.ipc_control;

/** �¼��б� */
public class SgEventList extends UtTable implements IObject {

	private String DeviceName;
	private String AlarmName;
	private String AlarmMeaning;
	private String AlarmSeverity;
	private String StartTime;
	
	public SgEventList(Context context) {
		super(context);
		m_rBBox = new Rect();
		
		m_listTempEvents = new Hashtable<String, Hashtable<String, Event>>();
				
		
		//System.out.println(MGridActivity.whatLanguage);
		if(MGridActivity.whatLanguage)
		{
		    DeviceName="�豸����";
			AlarmName="�澯����";
			AlarmMeaning="�澯����";
			AlarmSeverity="�澯�ȼ�";
			StartTime="��ʼʱ��";
		
		}
		else
		{
			DeviceName="DeviceName";
			AlarmName="AlarmName";
			AlarmMeaning="AlarmMeaning";
			AlarmSeverity="AlarmSeverity";
			StartTime="StartTime";

		}
		
		
		
		// ��ͷ
		lstTitles = new ArrayList<String>();
		// ���ƣ����壬��ʼʱ��
		lstTitles.add(DeviceName);
		lstTitles.add(AlarmName);
		lstTitles.add(AlarmMeaning);
	//	lstTitles.add("�ź���ֵ"); 
		lstTitles.add(AlarmSeverity);
		lstTitles.add(StartTime);
	//	lstTitles.add("����ʱ��  ");
	
/*		
	//Ӣ�ĸ�ʽ
		lstTitles.add("Equipment name");
		lstTitles.add("name");
		lstTitles.add("meaning");
	//	lstTitles.add("�ź���ֵ");
		lstTitles.add("severity");
		lstTitles.add("Start time");
*/		
		lstContends = new ArrayList<List<String>>();
		mythread.start();
	}

	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 1:
				updateContends(lstTitles, lstContends); //�������ʾ
				update();
	//			SgEventList.this.invalidate(); 
				break;
			default:
				break;
			}
		};
	};

	Thread mythread = new Thread(new Runnable() {	 
		@Override
		public void run() {
		

			while(true){
				try{
					Thread.sleep(500);
					handler.sendEmptyMessage(1);
				}catch(Exception e){
					
				}
			}
			
		}
	});
	
	
	
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
			notifyTableLayoutChange(nX, nY, nX+nWidth, nY+nHeight);
			
			for (int i = 0; i < m_title.length; ++i)
				m_title[i].layout(nX + i * nWidth / m_title.length, nY-18, nX + i * nWidth / m_title.length + nWidth / m_title.length, nY);
		}
	}

	@Override
	public void addToRenderWindow(MainWindow rWin) {

		this.setClickable(true);
		this.setBackgroundColor(m_cBackgroundColor);
		
		m_bUseTitle = false;
		m_title = new TextView[lstTitles.size()];
		for (int i = 0; i < m_title.length; i++)
		{
			m_title[i] = new TextView(getContext());
			//m_title[i].setTextColor(Color.BLACK);
			//m_title[i].setTextSize(25);
			//m_title[i].setBackgroundColor(Color.GRAY);
			m_title[i].setGravity(Gravity.CENTER);
			m_title[i].setText(lstTitles.get(i));
			rWin.addView(m_title[i]);
		}
		
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

       	 	// �趨�б��������
			m_nLeft = m_nPosX;
			m_nTop  = m_nPosY;
			m_nRight  = m_nLeft + m_nTableWidth;
			m_nBottom = m_nTop + m_nTableHeight;
        }
        else if ("Size".equals(strName)) {
       	 	String[] arrSize = strValue.split(",");
       	 	m_nWidth = Integer.parseInt(arrSize[0]);
       	 	m_nHeight = Integer.parseInt(arrSize[1]);

       	 	// �趨�б��������
			m_nTableWidth  = m_nWidth;
			m_nTableHeight = m_nHeight;
			m_nRight  = m_nLeft + m_nTableWidth;
			m_nBottom = m_nTop + m_nTableHeight;
        }
        else if ("Alpha".equals(strName)) {
       	 	m_fAlpha = Float.parseFloat(strValue);
        }
        else if ("Expression".equals(strName)){
        	m_strExpression = strValue;
  
        	String str = strValue;
   
        	if( !"".equals(str) ){
        	String a[] = str.split(":");
        	String b[] = a[1].split("\\]");
        	equiptID = b[0];

        	}else{
        		equiptID = "0";
        	}
       }
        else if ("ForeColor".equals(strName)) {
        	m_cForeColor = Color.parseColor(strValue);
        	this.setFontColor(m_cForeColor);
        }
        else if ("BackgroundColor".equals(strName)) {
        	m_cBackgroundColor = Color.parseColor(strValue);
        	this.setBackgroundColor(m_cBackgroundColor);
        }
        else if ("BorderColor".equals(strName)) {
        	m_cBorderColor = Color.parseColor(strValue);
        }
        else if ("OddRowBackground".equals(strName)) {
        	m_cOddRowBackground = Color.parseColor(strValue);
        }
        else if ("EvenRowBackground".equals(strName)) {
        	m_cEvenRowBackground = Color.parseColor(strValue);
        }
        else if ("CmdExpression".equals(strName)) {
        	m_strCmdExpression = strValue;
        	parseCmdExpression(m_strCmdExpression);
        }
	}
	
	//��������������ʽm_strCmdExpression
	public boolean parseCmdExpression(String strExpression){		
		if("".equals(strExpression)) return false;
		if(strExpression==null) return false;
//		Log.e("eventList cmd send->m_strCmdExpression:",strExpression);
		//�������ؼ����ʽ�����ؿؼ����ʽ��
		stExpression oMathExpress = UtExpressionParser.getInstance().parseExpression(m_strCmdExpression);
		if (oMathExpress != null) {		
			//�����ؼ����ʽ�������ݵ�Ԫ���ʽ��
			Iterator<HashMap.Entry<String, stBindingExpression>> it = oMathExpress.mapObjectExpress.entrySet().iterator();
			while(it.hasNext())
			{
				stBindingExpression oBindingExpression = it.next().getValue();	
	        	if(oBindingExpression == null)  break;	        
				ipc_control ipcC = new ipc_control();
				ipcC.equipid = oBindingExpression.nEquipId;
				ipcC.ctrlid = oBindingExpression.nCommandId;
				ipcC.valuetype = oBindingExpression.nValueType;
				ipcC.value = oBindingExpression.strValue;//bindingExpression.strValue;

				lstCtrl.add(ipcC);
				
				oMathExpress = null;
				ipcC = null;						
			}
		}
		return true;
	}
	
	//���Ϳ�������
	public boolean send_cmd(){
		if (0 != service.send_control_cmd(service.IP, service.PORT, lstCtrl))
		{					

		}else{

		}
		
		return true;
	}

	@Override
	public void initFinished()
	{
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
	
	public String getBindingExpression() {
		return m_strExpression;
	}
	
	public void onDraw(Canvas canvas) {
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;
		
		super.onDraw(canvas);
	}
	
	@Override
	public void updateWidget()
	{
		update();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean updateValue()
	{

		
		m_bneedupdate = false;		
		if (m_rRenderWindow == null) return false;		
		Hashtable<String, Hashtable<String, Event>> listEvents = null;
		if (m_rRenderWindow.m_bHasRandomData == false)
		{ // �Ƿ����������
			listEvents = m_rRenderWindow.m_oShareObject.m_mapEventListDatas.get(this.getUniqueID());
			
		} else
		{
			listEvents = m_listTempEvents; //Ҫ���������
		}
		
		
		if (listEvents == null){   //
			lstContends.clear();
/*
			List<String> fjwRow = new ArrayList<String>();
			fjwRow.add("���޸澯��Ϣ");  
			fjwRow.add("���޸澯��Ϣ");
			fjwRow.add("���޸澯��Ϣ");
			fjwRow.add("���޸澯��Ϣ");
			fjwRow.add("���޸澯��Ϣ");
			lstContends.add(fjwRow);
			updateContends(lstTitles, lstContends); //
*/
			return true;   //
	//		return false;
		}

		// ������
		Iterator<Hashtable.Entry<String, Hashtable<String, Event>>> equip_it = listEvents.entrySet().iterator();
		lstContends.clear();	
		while(equip_it.hasNext())
		{
			Hashtable.Entry<String, Hashtable<String, Event>> entry = equip_it.next();
		    String id = entry.getKey();

			String equipname = DataGetter.getEquipmentName(id);
			Iterator<Hashtable.Entry<String, Event>> it = entry.getValue().entrySet().iterator();
			if( "0".equals(equiptID) || id.equals(equiptID) ){
				while(it.hasNext())
				{
					Hashtable.Entry<String, Event> event_entry = it.next();
					Event event = event_entry.getValue();
				
					List<String> lstRow = new ArrayList<String>();

					lstRow.add(equipname);
					lstRow.add(event.name);
					lstRow.add(event.meaning+"");
			//		lstRow.add(event.value);
					lstRow.add(String.valueOf(event.grade) );						

					
					if (m_needsort)
						lstRow.add(String.valueOf(event.starttime*1000));
					else
						lstRow.add(getDate(event.starttime*1000, "yyyy.MM.dd HH:mm:ss"));
					
			
					
					lstContends.add(lstRow);
				}
			}
		}
		
		// ��ʱ��������
		if (m_needsort)
		{
			class SortByEventTime implements Comparator<Object>
			{
				public int compare(Object o1, Object o2)
				{
					
					List<String> l1 = (List<String>) o1;
					
					List<String> l2 = (List<String>) o2;
					
					Long s1 = Long.valueOf(l1.get(4));
					Long s2 = Long.valueOf(l2.get(4));
					
					return s2.compareTo(s1);
				}
			}

			Collections.sort(lstContends, new SortByEventTime());
			
			// ����ʱ��
			Iterator<List<String>> it = lstContends.iterator();
			while(it.hasNext())
			{
				List<String> next = it.next();
				String startime = getDate(Long.valueOf(next.get(4)), "yyyy.MM.dd HH:mm:ss");
				next.set(4, startime);
			}
		}
		
		
		//updateContends(lstTitles, lstContends_two); //�������ʾ
		
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
		
	//	System.out.println("123Evenlit ��"+bNeedUpdate);
	    m_bneedupdate = bNeedUpdate;
	    if("".equals(m_strCmdExpression)) return;
		if(m_strCmdExpression==null) return;

	    //�ж����µĸ澯
	    	//��ȡ�µĸ澯��Ϣ
	    Hashtable<String, Hashtable<String, Event>> new_eventss = DataGetter.getRTEventList();
	    if(new_eventss==null) return;
	    if(new_eventss.isEmpty()) return;
	    if(old_eventss.isEmpty()){
	    	send_cmd(); //���Ϳ�������
		    old_eventss = new_eventss;
		    return;
	    }
	    	//�����豸id
	    Iterator<String> equip_it = new_eventss.keySet().iterator();		
		while(equip_it.hasNext()){
			String e_equipId = equip_it.next();


			if(old_eventss.containsKey(e_equipId)){
				Hashtable<String, Event> new_events = new_eventss.get(e_equipId); 
				Hashtable<String, Event> old_events = old_eventss.get(e_equipId);
			
				Iterator<String> event_itt = new_events.keySet().iterator();		
				while(event_itt.hasNext()){
					String event_id = event_itt.next();

					if(old_events.containsKey(event_id) == false){
						send_cmd(); //���Ϳ�������
						old_eventss = new_eventss;
					}
				}
			}else{
				send_cmd(); //���Ϳ�������
				old_eventss = new_eventss;
			}
		}
	   
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
    int m_nZIndex = 16;
	int m_nPosX = 2;
	int m_nPosY = 103;
	int m_nWidth = 321;
	int m_nHeight = 175;
	float m_fAlpha = 0.8f;
	String m_strExpression = "Binding{[Equip[Equip:115]]}";
	String m_strCmdExpression = "";
	int m_cForeColor = 0xFF00FF00;
	int m_cBackgroundColor = 0xFF000000;
	int m_cBorderColor = 0xFFFFFFFF;
	MainWindow m_rRenderWindow = null;
	Rect m_rBBox = null;
	Hashtable<String, Hashtable<String, Event>> m_listTempEvents = null;
	
	public boolean m_bneedupdate = true;
	
	List<ipc_control> lstCtrl = new ArrayList<ipc_control>();  //��������������
	public static Hashtable<String, Hashtable<String, Event>> old_eventss = new Hashtable<String, Hashtable<String, Event>>();
	String equiptID = "0";
	
	// �̶�������
	TextView[] m_title;
	
	// TODO: ��ʱ���淽��
	boolean m_needsort = true; 
	List<String> lstTitles = null;
	ArrayList<List<String>> lstContends = null;
	ArrayList<List<String>> lstContends_two=null;
}
