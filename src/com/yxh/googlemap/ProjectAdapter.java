package com.yxh.googlemap;

import java.util.ArrayList;
import java.util.HashMap;

import android.R.integer;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class ProjectAdapter extends BaseAdapter {
       
	    private static final String TAG = "ProjectAdapter";
	    // 填充数据的List
	    private ArrayList<HashMap<String, Object>> list;
	    // 记录CheckBox的状态
	    private static HashMap<Integer, Boolean> isChecked;
	    private Context context;
	    // 导入布局
	    private LayoutInflater inflater;
	
	    public ProjectAdapter(ArrayList<HashMap<String, Object>> list, Context context){
	    	this.context = context;
	    	this.list = list;
	    	inflater = LayoutInflater.from(context);
	    	isChecked = new HashMap<Integer, Boolean>();
	    	//初始化CheckBox
	    	initCheckBox();
	    	Log.i(TAG, "MarkerAdapter(ArrayList<HashMap<String, Object>> list, Context context)");
	    }
	    
	    // 初始化CheckBox的状态，设为已选
		private void initCheckBox() {
			for(int i = 0; i < list.size(); i++){
				getIsChecked().put(i, true);
				Log.i(TAG, "initCheckBox() i: " + i );
			}
			
		}
	
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Log.i(TAG, "getView(), positon: " + position);
			ViewHolder  viewHolder = null;
			if (convertView == null){
				viewHolder = new ViewHolder();
				// 导入布局并赋值给convertView
				convertView = inflater.inflate(R.layout.list_item, null);
				viewHolder.projectName = (TextView) convertView.findViewById(R.id.lv_projectName);
				viewHolder.projectID = (TextView) convertView.findViewById(R.id.lv_projectID);
				viewHolder.projectCreatedTime = (TextView) convertView.findViewById(R.id.lv_projectCreatedTime);
				viewHolder.cb = (CheckBox) convertView.findViewById(R.id.lv_checkBox);
				
				// 为convertView 设置标签, 将viewHolder存储在convertView中
				convertView.setTag(viewHolder);
			} else {
				// 重新获取viewHolder
				viewHolder = (ViewHolder) convertView.getTag();
			}
			
			
			// 设置list中TextView的显示
			viewHolder.projectName.setText((String) list.get(position).get("projectName"));
			viewHolder.projectID.setText(String.valueOf(list.get(position).get("projectID")));
			viewHolder.projectCreatedTime.setText((String) list.get(position).get("projectCreatedTime"));
			// 设定CheckBox 的状态
			viewHolder.cb.setChecked(getIsChecked().get(position));
			
			return convertView;
		}
	  
		// 创建ViewHolder 内部类
		public class ViewHolder{
			TextView  projectName, projectCreatedTime, projectID;
			CheckBox cb;
		}

		
		public static HashMap<Integer, Boolean> getIsChecked(){
			return isChecked;
		}
		
		public static void setIsChecked(HashMap<Integer, Boolean> isChecked){
			ProjectAdapter.isChecked = isChecked;
		}
}
