package com.mgrid.main;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import android.view.ViewGroup;

public class ContainerView extends ViewGroup {

	public ContainerView(Context context) { 
		super(context);
	}

	// ��� VIEWGROUP ����VIEW���ֻ�������
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

	    if(mCurrentView != null)
	    {
	    	mCurrentView.layout(l, t, r, b);
	    }
	}
	
	
	
	protected void dispatchDraw(Canvas canvas)    
	{
	    super.dispatchDraw(canvas);
	    
	    if(mCurrentView != null)
	    {
	        drawChild(canvas, mCurrentView, getDrawingTime());
	    }
	}
	
	

	// ������� ViewGroup Ƕ���¼���Ӧ���⡣ TODO: ��������Ӱ���д��۲�  -- CharlesChen
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
	    if(mCurrentView != null)
	    {
	    	mCurrentView.measure(widthMeasureSpec, heightMeasureSpec);
	    }
		
		// �������ܣ����������ʾҳ�档

	}

	public View mCurrentView;
}
