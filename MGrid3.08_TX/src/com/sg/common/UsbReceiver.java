package com.sg.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.sg.common.Export_His_Data;

/**
 * �Ѿ�������  û��
 * @author lsy
 *�������� U���Ƿ����Ĺ㲥
 */
public class UsbReceiver{
	   private BroadcastReceiver mReceiver;
	   public UsbReceiver(Context context){
	      mReceiver = new BroadcastReceiver(){
	

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			  //intent.getAction());��ȡ�洢�豸��ǰ״̬        

        System.out.println("UUUBroadcastReceiver:"+arg1.getAction());

         //intent.getData().getPath());��ȡ�洢�豸·��
        System.out.println("UUUpath:"+arg1.getData().getPath());
     //   Export_His_Data.fileName=arg1.getData().getPath()+"/123.dat";
        
			
		}

	     };
	  
	      IntentFilter filter = new IntentFilter();
	      filter.addAction(Intent.ACTION_MEDIA_SHARED);//���SDCardδ��װ,��ͨ��USB�������洢������
	      filter.addAction(Intent.ACTION_MEDIA_MOUNTED);//����sd�����Ǵ��ڲ����ж�/дȨ��
	      filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);//SDCard��ж��,���SDCard�Ǵ��ڵ�û�б���װ
	      filter.addAction(Intent.ACTION_MEDIA_CHECKING);  //�����������ڴ��̼��
	      filter.addAction(Intent.ACTION_MEDIA_EJECT);  //����İγ� SDCARD
	      filter.addAction(Intent.ACTION_MEDIA_REMOVED);  //��ȫ�γ�
	      filter.addDataScheme("file"); // ����Ҫ�д��У������޷��յ��㲥   
	     // context.registerReceiver(mReceiver, filter);
	}
	}