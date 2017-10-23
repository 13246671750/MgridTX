package com.mgrid.main;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.IBinder;

public class SoundService extends Service {

	
	private MediaPlayer mp;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mp=new MediaPlayer();
		mp.setLooping(true); 
		String song = Environment.getExternalStorageDirectory().getPath()  + "/vtu_pagelist/Alarm.wav";
        // System.out.println(song);
				try {
            mp.setDataSource(song);
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mp.release();
        stopSelf();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		boolean playing = intent.getBooleanExtra("playing", false);
		 if (playing) {
	            mp.start();
	        } else {
	            mp.pause(); 
	        }
		
		 /*
		  * START_STICKY�����service���̱�kill��������service��״̬Ϊ��ʼ״̬�������������͵�intent����
		  *   START_NOT_STICKY������ճ�Եġ���ʹ���������ֵʱ�������ִ����onStartCommand�󣬷����쳣kill����ϵͳ�����Զ������÷���
		  *   START_REDELIVER_INTENT���ش�Intent��ʹ���������ֵʱ�������ִ����onStartCommand�󣬷����쳣kill����ϵͳ���Զ������÷��񣬲���Intent��ֵ���롣
		  *     START_STICKY_COMPATIBILITY��START_STICKY�ļ��ݰ汾��������֤����kill��һ����������
		  */
		return   START_NOT_STICKY;  
	}

}
