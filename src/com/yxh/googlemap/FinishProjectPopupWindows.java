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
		
		 Log.i(TAG, "FinishProjectPopupWindow �����ѱ�����");
		 
		 LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		 mView = inflater.inflate(R.layout.finish_project_popuwindow, null);  
		 
		 btnSaveProject = (Button) mView.findViewById(R.id.popupwindow_Button_saveProject);
		 btnAbandonProject = (Button) mView.findViewById(R.id.popupwindow_Button_abandonProject);
		 btnCancelProject = (Button) mView.findViewById(R.id.popupwindow_cancelButton);
		 
		 // ���ð�ť����
		 btnCancelProject.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Log.i(TAG, "ȡ����Ŀ");
			   dismiss();
			}		 
		 });
		 btnSaveProject.setOnClickListener(itemsOnClick);
		 btnAbandonProject.setOnClickListener(itemsOnClick);
		 
		
		   //����PopupWindow��View  
	        this.setContentView(mView);  
	        //����PopupWindow��������Ŀ�  
	        this.setWidth(LayoutParams.MATCH_PARENT);  
	        //����PopupWindow��������ĸ�  
	        this.setHeight(LayoutParams.WRAP_CONTENT);  
	        //����PopupWindow��������ɵ��  
	         this.setFocusable(true);  
	        //����SelectPicPopupWindow�������嶯��Ч��  
	        this.setAnimationStyle(R.style.Animation);
	        //ʵ����һ��ColorDrawable��ɫΪ��͸��  
	        ColorDrawable dw = new ColorDrawable(0xb0000000);  
	        //����SelectPicPopupWindow��������ı���  
	        this.setBackgroundDrawable(dw); 
	}
}
