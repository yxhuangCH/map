package com.yxh.googlemap;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

public class LoginAlertFragment extends DialogFragment implements DialogInterface.OnClickListener {

	   public static LoginAlertFragment newInstance(String title,String message){ 
	        LoginAlertFragment laf = new LoginAlertFragment(); 
	        Bundle bundle = new Bundle(); 
	        bundle.putString("alert-title", title); 
	        bundle.putString("alert-message", message); 
	       laf.setArguments(bundle); 
	        return laf; 
	    }
	   
	   private String getTitle(){
	        return getArguments().getString("alert-title"); 
	    } 
	    
	    private String getMessage(){ 
	        return getArguments().getString("alert-message"); 
	    } 
	    
	    
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
				.setTitle(getTitle()).setMessage(getMessage())
				.setPositiveButton("确定",  this) // 设置回调函数
				.setNegativeButton("取消", this); // 设置回调函数
		return builder.create();

	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		
	}
}
