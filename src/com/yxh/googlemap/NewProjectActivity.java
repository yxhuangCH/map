package com.yxh.googlemap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewProjectActivity extends Activity implements OnClickListener {

	Context context = this;
	private ProjectDatabaseManager projectData;
	private static final String TAG = "NewProjectActivity";

	long newProjectId;
	Long rowId;

	private Project mProject;
	private ProjectDatabaseManager projectDBManager;

	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_projection);

		Button newProjectOk = (Button) findViewById(R.id.new_project_ok);
		Button newProjectCancel = (Button) findViewById(R.id.new_project_concel);
		newProjectOk.setOnClickListener(this);
		newProjectCancel.setOnClickListener(this);

		// 加载project数据库
		projectData = new ProjectDatabaseManager(context);
		try {
			projectData.open();
			Log.i(TAG, "打开project数据库");
		} catch (Exception e) {
			Log.i(TAG, "打开project数据库遇到问题");
		}

		mProject = new Project(context);
		projectDBManager = new ProjectDatabaseManager(context);

	}

	@Override
	public void onClick(View v) {

		EditText newProjectName = (EditText) findViewById(R.id.new_projeciton_name);
		EditText newProjectDescribe = (EditText) findViewById(R.id.new_projec_describe);

		String name = newProjectName.getText().toString().trim();
		String describe = newProjectDescribe.getText().toString().trim();

		if (TextUtils.isEmpty(name)) {
			Toast.makeText(this, "项目名称不能为空", Toast.LENGTH_LONG).show();
			return;
		}
		int id = v.getId();

		if (id == R.id.new_project_ok) {

			// 将project的数据添加到 project 数据表中
			try {
				rowId = projectData.createProject(name, describe);

			} catch (Exception e) {
				e.printStackTrace();
				Log.i(TAG, "创建 project 数据表出线错误");
			}

			// 获取当前项目的Id
			// newProjectId = projectDBManager.getCurrentProjectId();

			Log.i(TAG, "newProjectName: " + name);
			Log.i(TAG, "newProjectDescribe: " + describe);
			Log.i(TAG, "newProjectId: " + newProjectId);

			// 将项目名称、项目描述传递给MapFragmentActivity;
			Intent intent = new Intent(NewProjectActivity.this,
					MapFragmentActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("newProjectName", name);
			bundle.putString("newProjectDescribe", describe);
			bundle.putLong("newProjectId", newProjectId);
			bundle.putLong("keyRowId", rowId);
			intent.putExtras(bundle);
			startActivity(intent);
            
			finish();
			// 显示对话框
		} else if (id == R.id.new_project_concel) {
			finish();
		}
	}

	@Override
	protected void onStart() {
		Log.i(TAG, "onStart()");
		super.onStart();
	}

	@Override
	protected void onRestart() {
		Log.i(TAG, "onRestart()");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		Log.i(TAG, "onResume()");
		super.onResume();
	}

	@Override
	protected void onPause() {
		Log.i(TAG, "onPause()");
		super.onPause();
	}

	@Override
	protected void onStop() {
		Log.i(TAG, "onStop()");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		Log.i(TAG, "onDestroy()");
		super.onDestroy();
	}
	
	
}
