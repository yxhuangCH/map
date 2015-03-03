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
		
		 Log.i(TAG, "MarkerAttributePopupWindow �����ѱ�����");
		 
		 LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		 mView = inflater.inflate(R.layout.marker_attribute, null);  
		 
		 // �ҵ�PopupWindow�ϵĿؼ�
		 findView();
		 
		 //���ü���
			bt_concel.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					Log.i(TAG, "ȡ��");
				   dismiss();
				}		 
			 });
	 bt_confrim.setOnClickListener(itemsOnClick);
	 
	 // ����Spinner ��������ʽ
	 spinner.setOnItemSelectedListener(itemsSelectOnClick);
	
	// ����PopupWindow
	setMarkerAttributePopupWindow();
	}
	

	// ����PopupWindow
	private void setMarkerAttributePopupWindow() {
		
		   this.setContentView(mView);  
	        //����PopupWindow��������Ŀ�  
	        this.setWidth(LayoutParams.MATCH_PARENT);  
	        //����PopupWindow��������ĸ�  
	        this.setHeight(LayoutParams.WRAP_CONTENT);  
	        //����PopupWindow��������ɵ��  
	         this.setFocusable(true);  
	        //����SelectPicPopupWindow�������嶯��Ч��  
	        this.setAnimationStyle(R.style.Animation_markerAttribute);
	        //ʵ����һ��ColorDrawable��ɫΪ��͸��  
	        ColorDrawable dw = new ColorDrawable(0xb0000000);  
	        //����SelectPicPopupWindow��������ı���  
	        this.setBackgroundDrawable(dw); 
	}


	// �ҵ�PopupWindow�ϵĿؼ�
	private void findView() {
		bt_confrim = (Button) mView.findViewById(R.id.bt_marker_attribute_confrim);
		bt_concel = (Button) mView.findViewById(R.id.bt_marker_attribute_concel);
		spinner = (Spinner) mView.findViewById(R.id.spinner1);
		
	}

}
