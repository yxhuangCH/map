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
	    // ������ݵ�List
	    private ArrayList<HashMap<String, Object>> list;
	    // ��¼CheckBox��״̬
	    private static HashMap<Integer, Boolean> isChecked;
	    private Context context;
	    // ���벼��
	    private LayoutInflater inflater;
	
	    public ProjectAdapter(ArrayList<HashMap<String, Object>> list, Context context){
	    	this.context = context;
	    	this.list = list;
	    	inflater = LayoutInflater.from(context);
	    	isChecked = new HashMap<Integer, Boolean>();
	    	//��ʼ��CheckBox
	    	initCheckBox();
	    	Log.i(TAG, "MarkerAdapter(ArrayList<HashMap<String, Object>> list, Context context)");
	    }
	    
	    // ��ʼ��CheckBox��״̬����Ϊ��ѡ
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
				// ���벼�ֲ���ֵ��convertView
				convertView = inflater.inflate(R.layout.list_item, null);
				viewHolder.projectName = (TextView) convertView.findViewById(R.id.lv_projectName);
				viewHolder.projectID = (TextView) convertView.findViewById(R.id.lv_projectID);
				viewHolder.projectCreatedTime = (TextView) convertView.findViewById(R.id.lv_projectCreatedTime);
				viewHolder.cb = (CheckBox) convertView.findViewById(R.id.lv_checkBox);
				
				// ΪconvertView ���ñ�ǩ, ��viewHolder�洢��convertView��
				convertView.setTag(viewHolder);
			} else {
				// ���»�ȡviewHolder
				viewHolder = (ViewHolder) convertView.getTag();
			}
			
			
			// ����list��TextView����ʾ
			viewHolder.projectName.setText((String) list.get(position).get("projectName"));
			viewHolder.projectID.setText(String.valueOf(list.get(position).get("projectID")));
			viewHolder.projectCreatedTime.setText((String) list.get(position).get("projectCreatedTime"));
			// �趨CheckBox ��״̬
			viewHolder.cb.setChecked(getIsChecked().get(position));
			
			return convertView;
		}
	  
		// ����ViewHolder �ڲ���
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
