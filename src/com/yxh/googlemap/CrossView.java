package com.yxh.googlemap;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

public class CrossView extends View{

	private int mLength = 100;
	private int mWidth;
	private int mColor;
	private Context mContext;
	private Paint mPaint;
	
	public CrossView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CrossView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	public CrossView(Context context, AttributeSet attrs) {
		 super(context, attrs);
		 this.mContext= context;
		 TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CrossView);
		 
		 mLength = (int) ta.getDimension(R.styleable.CrossView_lineLength, 20);
		 mWidth = (int) ta.getDimension(R.styleable.CrossView_lineWidth, 0);
		 mColor = ta.getColor(R.styleable.CrossView_lineColor, 0);
		 
		 mPaint = new Paint();
		 mPaint.setColor(mColor);
		 mPaint.setStrokeWidth(mWidth);
		 mPaint.setAntiAlias(true);
		 
		 ta.recycle();
	}

	protected void onDraw(Canvas canvas){
	     super.onDraw(canvas);  
	     DrawCrossView(canvas);		
	}

	private void DrawCrossView(Canvas canvas) {
	
         //	获取屏幕的尺寸
		DisplayMetrics dm =  getResources().getDisplayMetrics();
		int width = dm.widthPixels;
		int height = dm.heightPixels;
		
	        
	        // 获取状态栏的高度  
	        Rect frame = new Rect();  
	        getWindowVisibleDisplayFrame(frame);  
	        int statusBarHeight = frame.top;  
	             
	        // 设置竖线的位置  
	        int startX = width / 2;  	        
	        int startY = height / 2 - statusBarHeight/2 - mLength / 2;   
	        int stopX = startX;  
	        int stopY = startY + mLength;  
	        
	        Log.i("ScreenHeight",  height + "");  
	        Log.i("ScreenWidth",  width + "");  
	        Log.i("statusBarHeight", statusBarHeight + "");  
	        Log.i("startY", startY + "");  
	        Log.i("stopY", stopY + "");  
	        Log.i("mLength", mLength/2 + "");
	       
	        canvas.drawLine(startX, startY, stopX, stopY, mPaint);  
	        
	        // 设置横线的位置  
	        startX = width / 2 - mLength / 2;  
	        startY = (height - statusBarHeight) / 2;  
	        stopX = startX + mLength;  
	        stopY = startY;  
	        
	        Log.d("startX", startX + "");  
	        
	        canvas.drawLine(startX, startY, stopX, stopY, mPaint);  	
	}
}
