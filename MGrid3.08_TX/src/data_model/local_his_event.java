package data_model;  

import android.util.Log;


//made author:fjw0312
//date:2016
//notice:
public class local_his_event {
	
	public String start_time="";   //�澯ʱ��        0
	public String finish_time="";   //����ʱ��     1	
	public String equip_name=""; //�豸��              2
	public String equipid="";    //�豸id	  3
	public String event_name="";  //�澯��	  4
	public String sig_name="";    //�ź���           
	public String sigid="";      //�ź�id     
	public String event_id="";    //�澯id    5
	public String severity="";   //�����澯�ȼ�  8
	public String value="";       //�ź�ֵ         11	
	public String event_mean="";  //�澯����     12
	
	public String timelaterStr = ""; 
	

	public boolean put_equiptName(String name){
		equip_name = name;
		return true;
	}

	public boolean put_signalName(String name){
		sig_name = name;
		return true;
	}
	
	public boolean put_eventName(String name){
		event_name = name;
		return true;
	}
	
	public String get_signalId(){
		return sigid;
	}
	
	public String get_eventId(){
		return event_id;
	}
	
		

	public boolean read_string(String buf){
		
		String[] a  = new String[100];
		
		a = buf.split(",");
		timelaterStr = buf.substring(40);

		if(a.length != 13){
			return false;
		}
		
		start_time = a[0];   
		finish_time = a[1];  
		equip_name = a[2];
		equipid = a[3];    
		event_name = a[4]; 
		event_id = a[5];   
		severity = a[8];  
		value = a[11];   
		event_mean = a[12]; 
		sig_name = "";    

		
		
		return true;
	}
		
}
