package com.yxh.googlemap;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

public class FinishProjectPopupWindows extends PopupWindow{
	
	private static final String TAG = "FinishProjectPopupWindows";

	private View mView;
	public Button btnSaveProject, btnAbandonProject, btnCancelProject;

	public FinishProjectPopupWindows(Activity context, OnClickListener itemsOnClick) {
		super(context);
		
		 Log.i(TAG, "FinishProjectPopupWindow 方法已被调用");
		 
		 LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		 mView = inflater.inflate(R.layout.finish_project_popuwindow, null);  
		 
		 btnSaveProject = (Button) mView.findViewById(R.id.popupwindow_Button_saveProject);
		 btnAbandonProject = (Button) mView.findViewById(R.id.popupwindow_Button_abandonProject);
		 btnCancelProject = (Button) mView.findViewById(R.id.popupwindow_cancelButton);
		 
		 // 设置按钮监听
		 btnCancelProject.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Log.i(TAG, "取消项目");
			   dismiss();
			}		 
		 });
		 btnSaveProject.setOnClickListener(itemsOnClick);
		 btnAbandonProject.setOnClickListener(itemsOnClick);
		 
		
		   //设置PopupWindow的View  
	        this.setContentView(mView);  
	        //设置PopupWindow弹出窗体的宽  
	        this.setWidth(LayoutParams.MATCH_PARENT);  
	        //设置PopupWindow弹出窗体的高  
	        this.setHeight(LayoutParams.WRAP_CONTENT);  
	        //设置PopupWindow弹出窗体可点击  
	         this.setFocusable(true);  
	        //设置SelectPicPopupWindow弹出窗体动画效果  
	        this.setAnimationStyle(R.style.Animation);
	        //实例化一个ColorDrawable颜色为半透明  
	        ColorDrawable dw = new ColorDrawable(0xb0000000);  
	        //设置SelectPicPopupWindow弹出窗体的背景  
	        this.setBackgroundDrawable(dw); 
	}
}
