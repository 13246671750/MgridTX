package com.mgrid.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import E_Mail.MailSenderInfo;
import E_Mail.SimpleMailSender;

import com.mgrid.main.MGridActivity;
import com.sg.common.IObject;

import data_model.save_curve_signal;
import data_model.save_multipoint_signal;

public class EquipmentDataModel
{
	
	public class Signal
	{
		public Signal()
		{
			registedObj = new ArrayList<IObject>();
			registedNameObj = new ArrayList<IObject>();
			registedAlarmObj = new ArrayList<IObject>();
		}
		
		public String id = "";
		public String value = "";
		public String meaning = "";
		public long freshtime = 0;
		public String name = "";
		public String unit = "";
		public int is_invalid;
		public int value_type = 0;
		public int severity = 0;
		public int precision = 2;
		public String description = "";
		public List<IObject> registedObj = null;
		public List<IObject> registedNameObj = null;
		public List<IObject> registedAlarmObj = null;
	}
	
	// ��ʾһ���澯����������
	public class EventConditionCfg
	{
		public int    conditionid;
		public int    severity;
		public String  startcompare;
		public String  endcompare;
		public int    startdelay;
		public int    enddelay;
		public String  meaning;
	}
	

	public class EventCfg
	{
		public EventCfg()
		{
			htConditions = new Hashtable<String, EventConditionCfg>();
			
			registedNameObj = new ArrayList<IObject>();
		}
		
		public int     eventid;
		public String  eventname;
		public int     enabled;     
		public int     signalid;
		public Hashtable<String, EventConditionCfg> htConditions = null; 
		
		public List<IObject> registedNameObj = null;
	}
	

	public class CommandCfg
	{
		public class CmdParameaningCfg
		{
			public int value;
			public String meaning;
		}
		
		public CommandCfg()
		{
			meanings = new ArrayList<CmdParameaningCfg>();
		}
		
		public int cmdid;
		public float fMaxValue;
		public float fMinValue;
		public String name;
		public String unit;
		public List<CmdParameaningCfg> meanings = null; 
	}
	

	public class Equipment
	{
		public Equipment()
		{				
			h_ESigId_curveList = new Hashtable<String,Hashtable<String, save_curve_signal>>();
			h_ESigId_curvesList = new  Hashtable<String,Hashtable<String, save_multipoint_signal>>();
			registedRCSignalID = new ArrayList<String>();  
			registedSignalID  = new ArrayList<String>(); 
			registedObjID_SignalID =  new Hashtable<String,String>();   
			registedBackgroundListObj =  new Hashtable<String,IObject>(); 
			registedLstObj = new ArrayList<IObject>();
			htSignalData = new Hashtable<String, Signal>();
			htEventCfg = new Hashtable<String, EventCfg>();
			htCmdCfg = new Hashtable<String, CommandCfg>();			
		}
		
		public String m_equipid = "";
		public String m_tempid = "";
		public String m_category = "";
		public String m_name = "";
		public String m_xmlfile = "";		
		
	
		public byte[] signal_req = null;
		
	
		public byte[] signal_rsp = null;
		
	
		public long lUpdateTime = 0;  // milliseconds since January 1, 1970 00:00:00 UTC.
		
	
		public String proAutoUpdateTime = "";
		
	
		public String strSampleUpdateTime = "";
		
		
		public long oldSameUpdateTime = 0;
		
		
		public List<IObject> registedLstObj = null;
		public Hashtable<String,IObject> registedBackgroundListObj = null; 
		public Hashtable<String,String> registedObjID_SignalID = null; 
		public List<String> registedSignalID = null;  
		public List<String> registedRCSignalID = null; 
		public Hashtable<String,Hashtable<String, save_curve_signal>> h_ESigId_curveList = null;  
		public Hashtable<String,Hashtable<String, save_multipoint_signal>> h_ESigId_curvesList = null; 
		public Hashtable<String, List<String>> u =null; 
		
	
		public Hashtable<String, Signal> htSignalData = null; 
		
	
		public Hashtable<String, EventCfg> htEventCfg = null;    
		
	
		public Hashtable<String, CommandCfg> htCmdCfg = null;  
	}
	

	public class Event
	{
	
		public String name;    
		public int grade;    
		public long starttime; 
		public long stoptime;   
		public String value;  
		public String meaning; 
		public String state;  
//		public String eventid;  
	}


	public Hashtable<String, Equipment> htEquipmentData = null;  
	//static public Hashtable<String, Hashtable<String, List<String>>> htHistoryData = null;  // 
	

	public Hashtable<String, Hashtable<String, Event>> htEventData = null;  
	

	public Hashtable<String, String> htEquipments = null; 
	

	public Hashtable<String, Hashtable<String, EventCfg>> htEventCfg = null; 
	

	public byte[] rtalarm_req = null;
	

	public String rtalarm_rspbody = "";
	

	public List<IObject> lstRegistedMainAlarmList = null;
	

	public Set<String> autoAnyPapeRefreshEquip = new HashSet<String>(); //fjw add
	public Set<IObject> registedStateObj = new HashSet<IObject>(); 
	

	boolean bEventData = false;
	boolean bEquipments = false;
	boolean bEventCfg = false;
	

	public HashSet<String> hsEquipSet = null;  
	

	public Hashtable<String, HashSet<String>> htPageEquipSet = null;  
	
	public EquipmentDataModel()
	{
		htEquipmentData = new Hashtable<String, Equipment>();
		htEventData = new Hashtable<String, Hashtable<String, Event>>();
		lstRegistedMainAlarmList = new ArrayList<IObject>();
		htEquipments = new Hashtable<String, String>();
		htEventCfg = new Hashtable<String, Hashtable<String, EventCfg>>();
		hsEquipSet = new HashSet<String>();
		htPageEquipSet = new Hashtable<String, HashSet<String>>();
	}
	
	
	public static Hashtable<String, String>equipID_eventNum_lst = new Hashtable<String, String>();
	public static List<String> eventLst = new ArrayList<String>(); 
	public static List<String> old_eventLst = new ArrayList<String>();
	
	//fjw add
	Thread myThread = new Thread(new Runnable() { 
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(true){
				
				synchronized (equipID_eventNum_lst) {
					equipID_eventNum_lst.clear();  
					if(htEventData!=null){
					//���澯��Ϣ  ��ӵ�����----						
								
	
						Iterator<String> iter = htEventData.keySet().iterator();
						while(iter.hasNext()){
							String str_equipID = iter.next();
						
							int num = 0;
							if(htEventData.get(str_equipID)==null){		
								continue;
							}else{
								num = htEventData.get(str_equipID).size();
							}
							equipID_eventNum_lst.put(str_equipID, String.valueOf(num));
							
							
							
							Iterator<String>  it= htEventData.get(str_equipID).keySet().iterator();
						
							while(it.hasNext()){
								String str_eventID = it.next();
								if(htEventData==null) return;
								if(htEventData.get(str_equipID)==null)
									continue;
								Event event =  htEventData.get(str_equipID).get(str_eventID);
						        if(event==null)
						        	continue;
								
								SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//ʱ���ʽת��
								Date date = new Date(event.starttime*1000);
								String sampletime = formatter.format(date);	
								
								//����澯�ȼ�
								String grade = "һ��澯";
								switch(event.grade){ 
								case 1:  grade = "֪ͨ";
									break;
								case 2:  grade = "һ��澯";
									break; 
								case 3:  grade = "���ظ澯";
									break;
								case 4:  grade = "�����澯";
									break;
								default:
									break;
								}
								String str = "�豸��"+DataGetter.getEquipmentName(str_equipID).trim()+" \n"
										+"�澯���ƣ�"+event.name.trim()+" \n"
										+"�澯�ȼ���"+grade+" \n"
								//		+event.value+" "
										+"�澯���壺"+event.meaning.trim()+" \n"
										+"��ʼʱ�䣺"+sampletime+" \n ";
								//		+String.valueOf(event.stoptime);
								eventLst.add(str);						
							}
						}
						
					}else{
						equipID_eventNum_lst.put("0", "0");
						eventLst.clear();
					} 
						equipID_eventNum_lst.put("0", String.valueOf(eventLst.size()) ); 
					
				} 
	
				//--------------------
				
					for(int i=0;i<eventLst.size();i++){
						if( old_eventLst.contains(eventLst.get(i)) ){//�澯֮ǰ�Ѳ���

						}else{

							if(MGridActivity.m_bTakeEMail){
								mythread_mail mail = new mythread_mail();
								mail.content = eventLst.get(i);
								mail.start();
							}
						} 
					}
				
					if(old_eventLst.size()>eventLst.size()){ 
						for(int i=0;i<old_eventLst.size();i++){ 
							if( eventLst.contains(old_eventLst.get(i))==false ){
								long time = java.lang.System.currentTimeMillis();	
								SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								Date date = new Date(time);
								String sampletime = formatter.format(date);
								String str = old_eventLst.get(i).substring(0, old_eventLst.get(i).length()-1)
										+"����ʱ�䣺"+sampletime; 
								
								if(MGridActivity.m_bTakeEMail){
									mythread_mail mail = new mythread_mail();
									mail.content = str;
									mail.start();
								}

							} 
						}
					}
					
					old_eventLst.clear();
					for(int i=0;i<eventLst.size();i++){
						old_eventLst.add(eventLst.get(i));
					}
					eventLst.clear();

				try{
					Thread.sleep(1000); //1s�������߳� 
				}catch(Exception e){
					
				}
			}
		}
	});
	

	private class mythread_mail extends Thread {
		
		
		public String content="";
		@Override
		public void run() {   //���Գɹ���
				// TODO Auto-generated method stub
				try{
				MailSenderInfo mailInfo = new MailSenderInfo();
				mailInfo.setMailServerHost("smtp.139.com");
				mailInfo.setMailServerPort("25"); 
				mailInfo.setValidate(true);  
				mailInfo.setUserName("vtu0001@139.com");
	            mailInfo.setPassword("vtu0001");     
	            mailInfo.setFromAddress("vtu0001@139.com");     
	            mailInfo.setToAddress(MGridActivity.m_sEMailToAddress);    
	            mailInfo.setSubject(MGridActivity.m_sEMailTitle+MGridActivity.m_sEMailVTUnumber);      
	            mailInfo.setContent(content);       

	            SimpleMailSender sms = new SimpleMailSender();     
	     
	            sms.sendTextMail(mailInfo);   
		  
			}catch(Exception e){
				 
			}
		} 
	}
}
