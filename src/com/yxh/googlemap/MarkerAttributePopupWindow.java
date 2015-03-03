package com.yxh.googlemap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Spinner;

public class MarkerAttributePopupWindow extends PopupWindow {

	private static final String TAG = "MarkerAttributePopupWindow";
	private View mView;
	
	private Button  bt_confrim, bt_concel;
	public  Spinner spinner;
	
	public MarkerAttributePopupWindow(Activity context, OnClickListener itemsOnClick, OnItemSelectedListener itemsSelectOnClick){
		super(context);
		
		 Log.i(TAG, "MarkerAttributePopupWindow 方法已被调用");
		 
		 LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		 mView = inflater.inflate(R.layout.marker_attribute, null);  
		 
		 // 找到PopupWindow上的控件
		 findView();
		 
		 //设置监听
			bt_concel.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					Log.i(TAG, "取消");
				   dismiss();
				}		 
			 });
	 bt_confrim.setOnClickListener(itemsOnClick);
	 
	 // 设置Spinner 下拉的形式
	 spinner.setOnItemSelectedListener(itemsSelectOnClick);
	
	// 设置PopupWindow
	setMarkerAttributePopupWindow();
	}
	

	// 设置PopupWindow
	private void setMarkerAttributePopupWindow() {
		
		   this.setContentView(mView);  
	        //设置PopupWindow弹出窗体的宽  
	        this.setWidth(LayoutParams.MATCH_PARENT);  
	        //设置PopupWindow弹出窗体的高  
	        this.setHeight(LayoutParams.WRAP_CONTENT);  
	        //设置PopupWindow弹出窗体可点击  
	         this.setFocusable(true);  
	        //设置SelectPicPopupWindow弹出窗体动画效果  
	        this.setAnimationStyle(R.style.Animation_markerAttribute);
	        //实例化一个ColorDrawable颜色为半透明  
	        ColorDrawable dw = new ColorDrawable(0xb0000000);  
	        //设置SelectPicPopupWindow弹出窗体的背景  
	        this.setBackgroundDrawable(dw); 
	}


	// 找到PopupWindow上的控件
	private void findView() {
		bt_confrim = (Button) mView.findViewById(R.id.bt_marker_attribute_confrim);
		bt_concel = (Button) mView.findViewById(R.id.bt_marker_attribute_concel);
		spinner = (Spinner) mView.findViewById(R.id.spinner1);
		
	}

}
