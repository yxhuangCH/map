package com.yxh.googlemap;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class LoginDialogFragment extends DialogFragment{
	
	private static final String TAG = "LoginAlertDialogFragment";
	private EditText mUsername, mPassword; 
	// 创建接口
	 public interface LoginInputListener  
	    {  
	        void onLoginInputComplete(String username, String password);  
	        void saveResponse(String postResult);
	    }  
	  
	 @Override  
	    public Dialog onCreateDialog(Bundle savedInstanceState)  
	    {  
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());  
	        // Get the layout inflater  
	        LayoutInflater inflater = getActivity().getLayoutInflater();  
	        View view = inflater.inflate(R.layout.log_in, null);  
	        mUsername = (EditText) view.findViewById(R.id.et_userName);  
	        mPassword = (EditText) view.findViewById(R.id.et_password);  
	       
	        builder.setView(view)  
	                .setPositiveButton("登陆",   new DialogInterface.OnClickListener() { 
	                            public void onClick(DialogInterface dialog, int id)  
	                            {  
	                            	 LoginInputListener listener = (LoginInputListener) getActivity();  
	                                 listener.onLoginInputComplete(mUsername.getText().toString(), mPassword.getText().toString()); 
//	                                 sendRequestWithHttpClientPost();
	                                 Log.i(TAG, "bulider.setView()  username: " + mUsername.getText().toString() + "  password: " + mPassword.getText().toString());
	                            }  
	                        })
	                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								LoginDialogFragment.this.getDialog().cancel();
							}
						});
	        return builder.create();  
	    } 

	 
	 
	 
	private void sendRequestWithHttpClientPost() {
			
			new Thread(new Runnable(){

				@Override
				public void run() {
					try {
						HttpClient httpClient = new DefaultHttpClient();
						String path = "http://www.zwepd.com/app/data/Login.php";
//					    String path = "https://passport.baidu.com/v2/api/?login";
						HttpPost httpPost = new HttpPost(path);
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("UserName", mUsername.getText().toString().trim()));
						params.add(new BasicNameValuePair("PassWord", mPassword.getText().toString().trim()));
						
						UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
						httpPost.setEntity(entity);
						
//						request.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
						HttpResponse httpResponse = httpClient.execute(httpPost);
					       
						if (httpResponse.getStatusLine().getStatusCode() == 200){
							//请求和响应都成功
							String postResult = EntityUtils.toString(httpResponse.getEntity());
							Log.i(TAG,"PostResult: " + postResult.toString());
							
							 LoginInputListener listener = (LoginInputListener) getActivity();  
                             listener.saveResponse(postResult);
                             
                             JsonReader reader = new JsonReader(new StringReader(postResult));  
                             reader.beginArray();  
                             while(reader.hasNext()){  
                                 reader.beginObject();  
                                 while(reader.hasNext()){  
                                     String tagName= reader.nextName();  
                                     if(tagName.equals("status")){  
                                         System.out.println("status->"+reader.nextInt());  
                                     }  
                                     else if (tagName.equals("msg")) {  
                                         System.out.println("msg->"+reader.nextString());  
                                     }  
                                 }  
                                 reader.endObject();  
                             }  
                             reader.endArray();  
//							saveResult(postResult);
						}
					} catch (Exception e) {
						e.printStackTrace();
					} 
				}
			}).start();
		}

    

}
