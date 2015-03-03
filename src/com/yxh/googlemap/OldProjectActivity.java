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
	private int checkNumber; // ��¼ѡ����Ŀ������
	
	private boolean mIsDeletProject; //��ʶ�Ƿ�ɾ����Ŀ

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���ر�����
		// requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.oldproject);

		// ����project���ݿ�
		projectData = new ProjectDatabaseManager(this);
		try {
			projectData.open();
			Log.i(TAG, "��project���ݿ�");
		} catch (Exception e) {
			Log.i(TAG, "��project���ݿ���������");
		}

		// �õ������б������ҳü����صĿؼ�
		Button returnButton = (Button) findViewById(R.id.oldProjectActivityReturn);
		CheckBox allCheckBox = (CheckBox) findViewById(R.id.checkBoxAllProject);

		// ��ȡlistView
		listView = (ListView) findViewById(R.id.lv_allProject);

		list = new ArrayList<HashMap<String, Object>>();
		// �õ�����
		getData();

		projectAdapter = new ProjectAdapter(list, this);

		// ��Adapter
		listView.setAdapter(projectAdapter);

		
		
		// ��item�ļ���
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

		//����item
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				HashMap<String, Object> project = list.get(position);
				String projectID = String.valueOf(project.get("projectID"));
		    	String projectName = (String) project.get("projectName");
		    	
		    	// ��ʾListViewDeletFragment
		    	ListViewDeletFragment deletFragment = new ListViewDeletFragment(projectName, projectID);
		    	deletFragment.show(getFragmentManager(), "ListViewDeletFragment");
		    	if (mIsDeletProject) {
		    		Toast.makeText(getBaseContext(), projectName + "��Ŀ�Ѿ���ɾ��", Toast.LENGTH_LONG).show();
		    		list.remove(position);
		    		// ˢ���б�
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
				finish(); // �رյ�ǰ���棬������һ������
			}
		});

		// ȫѡ������Ŀ
		allCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					// ����list �ĳ��ȣ���MarkerAdapter�е�mapֵȫ����Ϊtrue
					for (int i = 0; i < list.size(); i++) {
						ProjectAdapter.getIsChecked().put(i, true);
					}
					checkNumber = list.size();
					Log.i(TAG, "ȫѡ checkNumber: " + checkNumber);

					// ˢ���б�
					dataChanged();
				} else {
					// ����list �ĳ��ȣ�����ѡ�İ�ť��Ϊδѡ
					for (int i = 0; i < list.size(); i++) {
						if (ProjectAdapter.getIsChecked().get(i)) {
							ProjectAdapter.getIsChecked().put(i, false);
							checkNumber--;
						}
					}
					Log.i(TAG, "��ѡ checkNumber: " + checkNumber);
					// ˢ���б�
					dataChanged();
				}
			}
		});
	} // end of allCheckBox

	//ɾ��listView��item
	protected void deletListViewItem(int position) {
		listView.removeViewAt(position);
		// ˢ���б�
		dataChanged();
	}

	// ˢ���б�
	private void dataChanged() {
		projectAdapter.notifyDataSetChanged();
		Log.i(TAG, "dataChanged()");
	};

	// ��ʼ��һ��List
	private List<HashMap<String, Object>> getData() {
		// �½�һ�������࣬���ڴ�Ŷ�������
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
		
		Log.d(TAG, "��ֵ֮ǰ�� isDelectProject: " + mIsDeletProject);
		mIsDeletProject = delet;
		Log.d(TAG, "��ֵ֮��� isDelectProject: " + mIsDeletProject);
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
