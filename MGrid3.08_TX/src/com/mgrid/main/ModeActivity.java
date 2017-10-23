package com.mgrid.main;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.mgrid.data.DataGetter;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerNativeActivity;

public class ModeActivity extends UnityPlayerNativeActivity {

	float startX, startY, endX, endY;
    private int GaojinValue=-1;
    private static long EXIST_TIME=10*1000;
    private long startTime=0;
   
	@Override
	protected void onCreate(Bundle arg0) {

		super.onCreate(arg0);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// 强制为横屏
		setContentView(R.layout.mode_activity);
		LinearLayout p = (LinearLayout) findViewById(R.id.UnityView);
		View mView = mUnityPlayer.getView();
		p.addView(mView);
		//getWindow().setGravity(Gravity.RIGHT | Gravity.BOTTOM);
		
	    	//设置背景透明度  alpha在0.0f到1.0f之间。1.0完全不透明，0.0f完全透明   
		    //dimAmount在0.0f和1.0f之间，0.0f完全不暗，1.0f全暗
//		WindowManager.LayoutParams lp=getWindow().getAttributes();
//		lp.alpha=1.0f;
//		lp.dimAmount=0.0f;
//        getWindow().setAttributes(lp);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

		

        
          
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		
		startTime=System.currentTimeMillis();
		if(MGridActivity.dialog!=null)
		MGridActivity.dialog.dismiss();
		
	    if(!update.isAlive())
	    {
	    //	update.start();
	    }
	    
		
	}
	
	
	
	
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		int pointerCount = arg0.getPointerCount();
		System.out.println("pointerCount:" + pointerCount);
		if (pointerCount == 1) {
			switch (arg0.getAction()) {
			case MotionEvent.ACTION_DOWN:
				startX = arg0.getX();
				startY = arg0.getY();
				break;
			case MotionEvent.ACTION_UP:
				endX = arg0.getX();
				endY = arg0.getY();
				float m = endX - startX;
				System.out.println("UP:" + m);

				break;
			case MotionEvent.ACTION_MOVE:
				endX = arg0.getX();
				endY = arg0.getY();

				float f = endX - startX;
				System.out.println("MOVE:" + f);
				UnityPlayer.UnitySendMessage("Main Sphere", "ZoomOut", "");

				break;

			}
		}
		if (pointerCount == 2) {

			ActivityJump();
		
		}
		return super.onTouchEvent(arg0);
	}

	private void ZhenChang() {
		UnityPlayer.UnitySendMessage("Main Cube", "ZhenChang", "");
	}

	private void GaoJing() {
		UnityPlayer.UnitySendMessage("Main Cube", "GaoJing", "");
	}

	

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				GaoJing();
				break;

			case 1:
				ZhenChang();
				break;
			case 2:
				
				break;
			}
		};
	};
	
	private void ActivityJump()
	{
		Intent intent = new Intent(ModeActivity.this, MGridActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
	}
	
	Thread update=new Thread(new Runnable() {
		
		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				String value = DataGetter.getSignalValue(1, 1);
				GaojinValue = (int) Float.parseFloat(value);
				if(GaojinValue==0)
				{
					GaoJing();
				}else
				{
					ZhenChang();
				}
			}		
		}
	});
	
	Thread time=new Thread(new Runnable() {
		
		@Override
		public void run() {
			long l=System.currentTimeMillis();
			while (l-startTime<=EXIST_TIME) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				l=System.currentTimeMillis();
			}	
			ActivityJump();
		}
	});
	
	
	
}
