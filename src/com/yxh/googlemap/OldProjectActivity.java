package com.yxh.googlemap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.yxh.googlemap.ListViewDeletFragment.DeletProjectListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.Toast;

public class OldProjectActivity extends Activity implements DeletProjectListener{

	private static final String TAG = "OldProjectActivity";
	private ArrayList<HashMap<String, Object>> list;
	private ProjectAdapter projectAdapter;
	private ListView listView;
	private ProjectDatabaseManager projectData;
	private int checkNumber; // 记录选中项目的数量
	
	private boolean mIsDeletProject; //标识是否删除项目

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 隐藏标题栏
		// requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.oldproject);

		// 加载project数据库
		projectData = new ProjectDatabaseManager(this);
		try {
			projectData.open();
			Log.i(TAG, "打开project数据库");
		} catch (Exception e) {
			Log.i(TAG, "打开project数据库遇到问题");
		}

		// 得到工程列表界面上页眉上相关的控件
		Button returnButton = (Button) findViewById(R.id.oldProjectActivityReturn);
		CheckBox allCheckBox = (CheckBox) findViewById(R.id.checkBoxAllProject);

		// 获取listView
		listView = (ListView) findViewById(R.id.lv_allProject);

		list = new ArrayList<HashMap<String, Object>>();
		// 得到数据
		getData();

		projectAdapter = new ProjectAdapter(list, this);

		// 绑定Adapter
		listView.setAdapter(projectAdapter);

		
		
		// 对item的监听
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				HashMap<String, Object> project = list.get(position);
		    	String projectID = String.valueOf(project.get("projectID"));
				Log.d(TAG, "onItemClickListener() projectID: " + projectID);
				Intent intent = new Intent(OldProjectActivity.this, MarkerPagerActivity.class);
				intent.putExtra(MarkerFragment.EXTRA_PROJECT_ID,  projectID);
				startActivity(intent);

			}
		});

		//长按item
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				HashMap<String, Object> project = list.get(position);
				String projectID = String.valueOf(project.get("projectID"));
		    	String projectName = (String) project.get("projectName");
		    	
		    	// 显示ListViewDeletFragment
		    	ListViewDeletFragment deletFragment = new ListViewDeletFragment(projectName, projectID);
		    	deletFragment.show(getFragmentManager(), "ListViewDeletFragment");
		    	if (mIsDeletProject) {
		    		Toast.makeText(getBaseContext(), projectName + "项目已经被删除", Toast.LENGTH_LONG).show();
		    		list.remove(position);
		    		// 刷新列表
					dataChanged();
				}
		    	mIsDeletProject = false;
				return false;
			}

		});
		
		// SimpleAdapter simpleAdapter = new SimpleAdapter(this, getData(),
		// R.layout.list_item,
		// new String[]{"projectName", "projectCreatedTime"}, new
		// int[]{R.id.listView_projectName, R.id.listView_projectCreatedTime });
		//
		// listView.setAdapter(simpleAdapter);
		//
		returnButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish(); // 关闭当前界面，返回上一个界面
			}
		});

		// 全选所有项目
		allCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					// 遍历list 的长度，将MarkerAdapter中的map值全部设为true
					for (int i = 0; i < list.size(); i++) {
						ProjectAdapter.getIsChecked().put(i, true);
					}
					checkNumber = list.size();
					Log.i(TAG, "全选 checkNumber: " + checkNumber);

					// 刷新列表
					dataChanged();
				} else {
					// 遍历list 的长度，将已选的按钮设为未选
					for (int i = 0; i < list.size(); i++) {
						if (ProjectAdapter.getIsChecked().get(i)) {
							ProjectAdapter.getIsChecked().put(i, false);
							checkNumber--;
						}
					}
					Log.i(TAG, "反选 checkNumber: " + checkNumber);
					// 刷新列表
					dataChanged();
				}
			}
		});
	} // end of allCheckBox

	//删除listView的item
	protected void deletListViewItem(int position) {
		listView.removeViewAt(position);
		// 刷新列表
		dataChanged();
	}

	// 刷新列表
	private void dataChanged() {
		projectAdapter.notifyDataSetChanged();
		Log.i(TAG, "dataChanged()");
	};

	// 初始化一个List
	private List<HashMap<String, Object>> getData() {
		// 新建一个集合类，用于存放多条数据
		// ArrayList<HashMap<String, Object>> list = new
		// ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map = null;

		List<Project> mp = projectData.getAllProject();
		for (int i = 0; i < mp.size(); i++) {
			map = new HashMap<String, Object>();
			map.put("projectName", mp.get(i).getProjectName());
			map.put("projectID", mp.get(i).getId());
			map.put("projectCreatedTime", mp.get(i).getProjectCreatedTime());

			list.add(map);

			Log.i(TAG, "getDate() + pojectID: " + mp.get(i).getId());

		}

		Log.i(TAG, "ListView getData()");
		return list;
	}

	@Override
	public boolean isDeletProject(boolean delet) {
		
		Log.d(TAG, "赋值之前的 isDelectProject: " + mIsDeletProject);
		mIsDeletProject = delet;
		Log.d(TAG, "赋值之后的 isDelectProject: " + mIsDeletProject);
		return mIsDeletProject;
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		Log.d(TAG, "onStart");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause()");
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		Log.d(TAG, "onStop");
	}

}
