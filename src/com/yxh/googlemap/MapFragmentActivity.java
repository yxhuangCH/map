package com.yxh.googlemap;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragmentActivity extends Activity {

	private static final String TAG = "MapFragmentActivity";
	private Context context = this;

	// 显示项目名称和描述
	private String projectName;
	private String projectDescribe;
	private long projectId;
	private Long rowId;
	private int project_id;

	private String mId;
	private String mLatitude;
	private String mLongitude;
	private String mAltitude;
	private String mProjectId;
	private GoogleMap mMap;
	private LocationManager lm;
	double latitude;
	double langitude;
	
	// 将经度、纬度从String类型转化成Double类型
	Double dLatitude;
	Double dLongitude;
	Double dAltitude;

	private LocationDatabaseManager locationData;
	private MarkerDatabaseManager markerData;

	// 定义markerAttribute
	private String markerAttribute;

	// 自定义弹出PopupWindow
	private FinishProjectPopupWindows mFinishProjectPopupWindow;
	private MarkerAttributePopupWindow mMarkerAttributePopupWindow;

	// 退出时间
	private long mExitTime = 0L;
	
	private boolean mBound; //标识是否绑定服务

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 去除标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mapfragment);

		// 获取项目名称，并显示
		showProjectName();

		// 初始化地图
		initMap();

		// 加载数据库
		openData();

		// 检查GPS状态
		checkGPSStatues();

		// 判断Google Services 是否可用
		connectGoogleServices();

		// 切换地图样式
		changeMapStyle();

		// 获取位置
		getCurrentLocation();

		// 设置对标识点的监听
		OnMarkerClickListener();

		// Test
		OnMapClickListener();

		// 绑定后台服务
		doBindService();
		
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.i(TAG, "onStart() method was invoked");
	}
	
	// 绑定服务
	private void doBindService() {
		Log.i(TAG, "bindService() method was invoked");
		Intent bindIntent = new Intent(MapFragmentActivity.this,MapLocationService.class);
		bindService(bindIntent, connection, Context.BIND_AUTO_CREATE);
	}

	// 解除绑定服务
	private void doUnbindService() {
		Log.i(TAG, "unbindService() method was invoked");
		unbindService(connection);
	}

	// 绑定后台服务
	private ServiceConnection connection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) { // 绑定服务成功时调用
			Log.i(TAG,"ServiceConnection()  onServiceConnection() method was invoked");
			
			 mBound = true; //绑定服务
		}

		@Override
		public void onServiceDisconnected(ComponentName name) { // 解除服务时调用
			Log.i(TAG, "ServiceConnection() onServiceDisconnectde() method was invoked");
			mBound = false; //解除绑定
		}
	};

	private void OnMapClickListener() {

		mMap.setOnMapLongClickListener(new OnMapLongClickListener() {

			@Override
			public void onMapLongClick(LatLng latLng) {
				mMap.addMarker(new MarkerOptions().position(
						new LatLng(latLng.latitude, latLng.longitude)).title(
						"Hello world"));

				Toast.makeText(
						getBaseContext(),
						"Marker的坐标" + "\nlatitude: " + latLng.latitude
								+ "\nlongitude: " + latLng.longitude,
						Toast.LENGTH_LONG).show();
			}
		});
	}

	// 初始化地图
	private void initMap() {
		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		mMap.setMyLocationEnabled(true);
		mMap.setOnMyLocationButtonClickListener(new OnMyLocationButtonClickListener() {

			@Override
			public boolean onMyLocationButtonClick() {
				Toast.makeText(getBaseContext(), "定位中，请稍后！", Toast.LENGTH_LONG)
						.show();
				return false;
			}
		});
	}

	// 加载数据库
	private void openData() {
		// 加载location 数据库
		locationData = new LocationDatabaseManager(context);
		try {
			locationData.open();
			Log.i(TAG, "打开location数据库");
		} catch (Exception e) {
			Log.i(TAG, "打开location数据库遇到问题");
		}

		// 加载marker 数据库
		markerData = new MarkerDatabaseManager(context);
		try {
			markerData.open();
			Log.i(TAG, "打开marker数据库");
		} catch (Exception e) {
			Log.i(TAG, "打开marker数据库遇到问题");
		}

		// 创建MarkerDateSharedPreference的XML文件
//		markerShared = new MarkerDateSharedPreferenceManager();
//		markerShared.createMarkerDateSharedPreferences(context, projectName);
//		Log.i(TAG, "创建MarkerSharedPreference的XML文件" + " context: " + context
//				+ " projectName: " + projectName);
	}

	// 判断Google Services 是否可用
	private void connectGoogleServices() {

		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);

		if (ConnectionResult.SUCCESS == resultCode) {
			Toast.makeText(this, "Google Play Services 可用", Toast.LENGTH_LONG)
					.show();
		} else {
			Toast.makeText(this, "Google Play Services 不可用", Toast.LENGTH_LONG)
					.show();
		}
	}

	// 检查GPS状态
	private void checkGPSStatues() {
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			GPSStatues();
			return;
		}
	}

	// 显示GPS对话框
	public void GPSStatues() {
		new AlertDialog.Builder(this)
				.setTitle("定位提示")
				.setMessage("GPS未打开，请进行设置！")
				.setPositiveButton("设置",
						new android.content.DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent intent = new Intent(
										Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								startActivityForResult(intent, 0);
							}
						})
				.setNegativeButton("取消",
						new android.content.DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
							}
						}).show();

	}

	// 显示项目名称和描述
	private void showProjectName() {
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		projectName = bundle.getString("newProjectName");
		projectDescribe = bundle.getString("newProjectDescribe");
		projectId = bundle.getLong("newProjectId");
		rowId = bundle.getLong("keyRowId");

		TextView displayProjectName = (TextView) findViewById(R.id.displayProjectName);
		displayProjectName.setText(projectName);

		project_id = rowId.intValue();

		Log.i(TAG, "showProjectName()  projectName: " + projectName);
		Log.i(TAG, "showProjectName()  projectDescribe: " + projectDescribe);
		Log.i(TAG, "showProjectName()  projectId: " + projectId);
		Log.i(TAG, "showProjectName()  project_id: " + project_id);

	}

	/******************** 增加Marker到地图上、实现MarkerAttributePopupWindow *********************/
	/*
	 * 标点、开始、结束，三个按钮的实现
	 */
	// 增加Marker都地图上
	public void clickAddMarker(View source) {
		Log.i(TAG, "clickAddMarker,标点按钮已被按下...");
		Toast.makeText(this, "标点按钮已被按下", Toast.LENGTH_LONG).show();

		// 获取到最新location的信息
		List<LocationObject> mLocationObject = locationData.getLocation();

		mId = mLocationObject.get(0).getId();
		mLatitude = mLocationObject.get(0).getLatitude();
		mLongitude = mLocationObject.get(0).getLongitude();
		mAltitude = mLocationObject.get(0).getAltitude();
		mProjectId = mLocationObject.get(0).getProjectId();

		Log.i(TAG, "clickAddMarker()  mId: " + mId);
		Log.i(TAG, "clickAddMarker()  mLatitude: " + mLatitude);
		Log.i(TAG, "clickAddMarker()  mLongitude: " + mLongitude);
		Log.i(TAG, "clickAddMarker()  mAltitude: " + mAltitude);
		Log.i(TAG, "clickAddMarker()  mProjectId: " + mProjectId);

		// 将经度、纬度从String类型转化成Double类型
		try {
			dLatitude = Double.parseDouble(mLatitude);
			dLongitude = Double.parseDouble(mLongitude);
			dAltitude = Double.parseDouble(mAltitude);

			Log.i(TAG, "clickAddMarker()  dLatitude: " + dLatitude);
			Log.i(TAG, "clickAddMarker()  dLongitude: " + dLongitude);
			Log.i(TAG, "clickAddMarker()  dAltitude: " + dAltitude);

			// 标点到地图上
			mMap.addMarker(new MarkerOptions().position(new LatLng(dLatitude,
					dLongitude)));
			Log.i(TAG, "clickAddMarker()  标点成功");

			Toast.makeText( getBaseContext(),"Marker的坐标" + "id: " + mId + "\nlatitude: " + dLatitude
							+ "\nlongitude: " + dLongitude + "\naltitude: "
							+ mAltitude, Toast.LENGTH_LONG).show();

			//把点在信息增加到Marker数据表格中
//			markerData.addMarkerLocation(dLatitude, dLongitude, dAltitude,
//					projectName, mProjectId);

		} catch (NumberFormatException e) {
			e.printStackTrace();
			Log.e(TAG, "mLatitude、mLongitude 数据的转化出现错误");
		}

		// 显示MakerAttributePopupWindow
		mMarkerAttributePopupWindow = new MarkerAttributePopupWindow(
				MapFragmentActivity.this, itemsOnClick2, itemSelectedOnClick);
		mMarkerAttributePopupWindow.showAtLocation(
				MapFragmentActivity.this.findViewById(R.id.main), Gravity.LEFT
						| Gravity.CENTER_VERTICAL, 0, 0);

		// Spinner里数据的填充
		setSpinner();
	}// end of clickAddMarker()

	private void setSpinner() {

		String[] markerAttribute = { "电杆", "铁塔", "变压器", "拆除电杆", "利旧电杆", "转角杆",
				"终端杆", "耐张杆" };

		ArrayAdapter<String> adapterMarkerAttribute = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item, markerAttribute);
		adapterMarkerAttribute
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mMarkerAttributePopupWindow.spinner.setAdapter(adapterMarkerAttribute);
	}

	private OnClickListener itemsOnClick2 = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// MarkerAttributePopupWindow 淡出Map 界面
			mMarkerAttributePopupWindow.dismiss();

			switch (v.getId()) {
			case R.id.bt_marker_attribute_confrim:

				// 编辑MarkerSharedPreference 的XML 文件
//				editMarkerSharedPreference();
				
				//把点在信息增加到Marker数据表格中
				//获取前一个MarkerID,+1为当前markerID
			   int lastMarkerID =  markerData.getMarkerID( projectName) + 1 ;
				markerData.addMarkerLocation(lastMarkerID, dLatitude, dLongitude, dAltitude,
						projectName, mProjectId, markerAttribute);
				
				Log.i(TAG, "保存Marker 的属性 markerId: " +" dLatitude: " + dLatitude + " dLongitude:  " + dLongitude
						+ " dAltitude: " + dAltitude + " projectName: " + projectName 
						+ " mProjectId: " + mProjectId + " markerAttribute: " + markerAttribute);
				
//				markerData.addMarkerLocation(dLatitude, dLongitude, dAltitude,
//						projectName, mProjectId, markerAttribute);
//				
				break;
			case R.id.bt_marker_attribute_concel:
				Log.i(TAG, "取消设置 Marker 的属性");
				break;
			}
		}
	};

	private OnItemSelectedListener itemSelectedOnClick = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
           //选择的点的属性
			markerAttribute = parent.getItemAtPosition(position).toString();
			Toast.makeText(parent.getContext(), markerAttribute,
					Toast.LENGTH_SHORT).show();

			Log.i(TAG, "OnItemSelectedListener()-onItemSelected()");

		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			Toast.makeText(parent.getContext(), "什么也没有选择", Toast.LENGTH_SHORT).show();
			Log.i(TAG, "OnItemSelectedListener()_onNothingSelected()");
		}
	};

	/******************** 增加Marker到地图上、实现MarkerAttributePopupWindow *********************/

	public void clickEndMap(View source) {
		Log.i(TAG, "clickEndMap,结束按钮已被按下...");
		Toast.makeText(this, "结束按钮已被按下", Toast.LENGTH_SHORT).show();

		mFinishProjectPopupWindow = new FinishProjectPopupWindows(
				MapFragmentActivity.this, itemsOnClick);
		// 显示PopupWindow
		mFinishProjectPopupWindow.showAtLocation(
				MapFragmentActivity.this.findViewById(R.id.main),
				Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

	}// end of clickEndMap

	private OnClickListener itemsOnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mFinishProjectPopupWindow.dismiss();
			switch (v.getId()) {
			case R.id.popupwindow_Button_saveProject:
				Log.i(TAG, "保存线路");
				
				//线程睡眠2秒，让界面关闭的更好看一些
				 try {  
	                    Thread.sleep(2000);  
	                } catch (InterruptedException e) {  
	                    e.printStackTrace();  
	                }  
				finish();
				break;
			case R.id.popupwindow_Button_abandonProject:
				markerData.deleteAllMarker(String.valueOf(project_id));
				Log.i(TAG, "放弃线路 " + " ProjcetId: " + project_id);
				// 结束本次项目，退出本次工程界面
				onPause();
				break;
			case R.id.popupwindow_cancelButton:
				mFinishProjectPopupWindow.dismiss();
				Log.i(TAG, "取消");
				break;
			}

		}

	};

	// 改变地图类型
	private void changeMapStyle() {

		Button normalMap = (Button) findViewById(R.id.NormalMapButton);
		Button satelliteMap = (Button) findViewById(R.id.SatelliteMapButton);

		normalMap.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			}

		});

		satelliteMap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			}
		});

	}

	// 获取当前位置
	private void getCurrentLocation() {

		/*
		 * 设置查询条件 为高精度、需要海拔、需要方向、低电量、会产生费用
		 */
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(true);
		criteria.setBearingRequired(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		criteria.setCostAllowed(true);

		// 获取Provider
		String provider = lm.getBestProvider(criteria, true);
		Location location = lm.getLastKnownLocation(provider);

		/*
		 * 设置监听器，自动更新的最小时间为5秒，最小位移变化超过2米
		 */
		lm.requestLocationUpdates(provider, 5 * 1000, 2, locationListener);

	}

	// 位置改变时进行监听
	LocationListener locationListener = new LocationListener() {

		@Override
		public void onLocationChanged(Location location) {

			// Getting latitude of the current location
			double latitude = location.getLatitude();

			// Getting longitude of the current location
			double longtitude = location.getLongitude();

			// Getting altitude of the current location
			double altitude = location.getAltitude();

			// Clear the marker---test
			// googleMap.clear();

			// Creating a LatLng object for the current location
			LatLng latLng = new LatLng(latitude, longtitude);

			// Showing the current location in Google Map
			mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

			// Zoom in the Google Map
			// mMap.animateCamera(CameraUpdateFactory.zoomTo(2));

			// //Add a marker on the Google Map ---test
			// mMap.addMarker(new MarkerOptions()
			// .position(new LatLng(latitude, longtitude)));
			//
			Log.i(TAG, "LocationListener()  Latitude: " + latitude);
			Log.i(TAG, "LocationListener()  Longtitude: " + longtitude);
			Log.i(TAG, "LocationListener()  Altitude: " + altitude);
			Log.i(TAG, "LocationListener()  projectId: " + projectId);
			Log.i(TAG, "LocationListener()  project_id: " + project_id);

			// 将location的数据添加到location数据表中
			try {
				// locationData.addLocation(location);
				locationData.addLocation(location, project_id);
			} catch (Exception e) {
				e.printStackTrace();
				Log.i(TAG, "add location to the db err");
			}

			// Toast 测试用
			Toast.makeText(
					getApplicationContext(),
					"经度：" + latitude + "\n纬度： " + longtitude + "\n海拔： "
							+ altitude, Toast.LENGTH_LONG).show();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}
	};// end of locationListener

	// 对 Marker的点触进行监控
	private void OnMarkerClickListener() {
		mMap.setOnMarkerClickListener(new com.google.android.gms.maps.GoogleMap.OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(final Marker marker) {
				new AlertDialog.Builder(MapFragmentActivity.this)
						.setTitle("删除标点")
						.setMessage("确定删除该点？")
						.setIcon(R.drawable.ic_bullet_key_permission)
						.setPositiveButton(
								"确定",
								new android.content.DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {

										List<MarkerObject> mg = markerData.getMyMarkers();
										Log.i(TAG, "AlertDialog mg.size(): " + mg.size());
										for (int i = 0; i < mg.size(); i++) {
											if (mg.get(i).getmLatitude() == marker.getPosition().latitude&& mg.get(i).getmLongtitude() == marker.getPosition().longitude) {
												long myId = mg.get(i).getmId();
												String my_id;
												Log.i(TAG, "AlertDialog myId: "+ myId);
												try {my_id = String.valueOf(myId);
													markerData.deleteSingleMarker(my_id);
													Log.i(TAG,"AlertDialog my_id: "+ my_id);
													Toast.makeText(getBaseContext(),	"Marker_id: "+ my_id,Toast.LENGTH_LONG).show();
												} catch (Exception e) {
													Toast.makeText(getBaseContext(),"删除Marker失败",Toast.LENGTH_SHORT).show();
													Log.i("DeleteMarker","delete marker from the map err");
												}
											}
										}// end of for

										marker.remove();
									}
								})
						.setNegativeButton(
								"取消",
								new android.content.DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
									}
								}).show();

				Toast.makeText(getBaseContext(), "marker已被选中",
						Toast.LENGTH_LONG).show();
				Log.i(TAG, "marker已被中");
				return false;
			}

		});

	}

	public void onResume() {

		// 从新回到当前窗体时，进行快速定位
		super.onResume();

		// 启动时快速定位
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		boolean isNetwork = lm
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		boolean isGPS = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

		if (isNetwork) {
			lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
					5 * 1000, 2, locationListener);
			Location location = lm
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if (location != null) {
				latitude = location.getLatitude();
				langitude = location.getLongitude();
			}
		}

		if (isGPS) {
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 2,
					locationListener);
			Location location = lm
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (location != null) {
				latitude = location.getLatitude();
				langitude = location.getLongitude();
			}
		}
	}// end of onResume()

	// 退出不可见时
	public void onPause() {
		Log.i(TAG, "onPause() method was invoked");
		super.onPause();
		// 移除位置监听
		// lm.removeUpdates(locationListener);

	}

	@Override
	protected void onStop() {
		super.onStop();
		// 移除位置监听
		lm.removeUpdates(locationListener);
		Log.i(TAG, "onStop() method was invoked");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if( mBound){
			doUnbindService();
			mBound = false;
		}
		Log.i(TAG, "onDestroy() method was invoked");
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// return super.onKeyDown(keyCode, event);
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				// 如果按两次back键的时间小于两秒，则退出
				Toast.makeText(MapFragmentActivity.this, "再按一下当前界面",
						Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();
			} else {
				if( mBound){
					doUnbindService();
					mBound = false;
					finish();
				}
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
