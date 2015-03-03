package com.yxh.googlemap;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ProjectDatabaseManager {

	private static final String TAG = "ProjectDatabaseManager";
	private MapDatabaseHelper projectDatabaseHelper;
	private SQLiteDatabase projectDB;
	private SharedPreferences mPrefs;
	private long mCurrentProjectId;

	private static final String CURRENT_PROJECT_ID = "ProjectDatabaseManager.currentProjectId";
	private static final String PREFS_FILE = "Projects";
	private Context context;

	public ProjectDatabaseManager(Context context) {
		projectDatabaseHelper = new MapDatabaseHelper(context);
		mPrefs = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
		mCurrentProjectId = mPrefs.getLong(CURRENT_PROJECT_ID, -1);
	}

	public void open() throws SQLException {
		projectDB = projectDatabaseHelper.getWritableDatabase();
		Log.i(TAG, "得到数据库表格: Project");
	}

	public void close() {
		projectDB.close();
		Log.i(TAG, "关闭数据表格: project");
	}

	public Long createProject(String name, String describe) {

		// 获取项目名称和项目描述
		ContentValues intialValues = new ContentValues();
		intialValues.put(MapDatabaseHelper.PROJECT_NAME, name);
		intialValues.put(MapDatabaseHelper.PROJECT_DESCRIBE, describe);

		// 项目创建时间
		Calendar calendar = Calendar.getInstance();
		String projectCreatedTime = calendar.get(Calendar.YEAR) + "-"
				+ calendar.get(Calendar.MONTH) + "-"
				+ calendar.get(Calendar.DAY_OF_MONTH) + " "
				+ calendar.get(Calendar.HOUR_OF_DAY) + ":"
				+ calendar.get(Calendar.MINUTE) + ":"
				+ calendar.get(Calendar.SECOND);

		intialValues.put(MapDatabaseHelper.PROJECT_CREATE_TIME,
				projectCreatedTime);

		Log.i(TAG, "projectName: " + name);
		Log.i(TAG, "projectDescribe: " + describe);
		Log.i(TAG, "projectCreatedTime: " + projectCreatedTime);
		Log.i(TAG, "intialValues: " + intialValues);

		Log.i(TAG, "创建Project数据表成功");

		return projectDB.insert(MapDatabaseHelper.TABLE_PROJECT, null,
				intialValues);

	}

	/*
	 * 得到所有Project信息
	 */
	public List<Project> getAllProject() {

		List<Project> project = new ArrayList<Project>();

		Cursor cursor = projectDB.query(MapDatabaseHelper.TABLE_PROJECT, null,
				null, null, null, null, null);

		if (cursor.moveToFirst()) {
			do {
				String id = cursor.getString(cursor
						.getColumnIndex(MapDatabaseHelper.PROJECT_ID));
				String name = cursor.getString(cursor
						.getColumnIndex(MapDatabaseHelper.PROJECT_NAME));
				String createdTime = cursor.getString(cursor
						.getColumnIndex(MapDatabaseHelper.PROJECT_CREATE_TIME));

				Log.i(TAG, "getAllProject()  cursorCount: " + cursor.getCount());
				Log.i(TAG, "getAllProject()  id: " + id);
				Log.i(TAG,
						"getAllProject() Long.parseLong(id): "
								+ Long.parseLong(id));
				Log.i(TAG, "getAllProject()  name: " + name);
				Log.i(TAG, "getAllProject()  createdTime: " + createdTime);

				Project p = new Project(Long.parseLong(id), name, createdTime);

				project.add(p);

			} while (cursor.moveToNext());

		}
		cursor.close();
		return project;
	}

	// 根据传进来的projectID ，得到该project的信息
	public List<Project> getProject(String projectID) {

		List<Project> project = new ArrayList<Project>();

		Cursor cursor = projectDB.query(MapDatabaseHelper.TABLE_PROJECT, null,
				"project_id=?", new String[] {projectID}, null, null, null);

		if (cursor.moveToFirst()) {
			do {
				String id = cursor.getString(cursor
						.getColumnIndex(MapDatabaseHelper.PROJECT_ID));
				String name = cursor.getString(cursor
						.getColumnIndex(MapDatabaseHelper.PROJECT_NAME));
				String createdTime = cursor.getString(cursor
						.getColumnIndex(MapDatabaseHelper.PROJECT_CREATE_TIME));

				Log.i(TAG, "getProject()  cursorCount: " + cursor.getCount());
				Log.i(TAG, "getProject()  id: " + id);
				Log.i(TAG, "getProject() Long.parseLong(id): " + Long.parseLong(id));
				Log.i(TAG, "getProject()  name: " + name);
				Log.i(TAG, "getProject()  createdTime: " + createdTime);

				Project p = new Project(Long.parseLong(id), name, createdTime);

				project.add(p);

			} while (cursor.moveToNext());

		}
		cursor.close();
		return project;
	}
	
	// 删除单个Marker数据从数据表格中
		public void deleteSingleProject(String my_id) {
			projectDB.delete(MapDatabaseHelper.TABLE_PROJECT, "project_id = ? ",
					new String[] { my_id });
			Log.i(TAG, "删除单个Project成功, my_id: " + my_id);
		}
}
