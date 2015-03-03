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

	// ��ʾ��Ŀ���ƺ�����
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
	
	// �����ȡ�γ�ȴ�String����ת����Double����
	Double dLatitude;
	Double dLongitude;
	Double dAltitude;

	private LocationDatabaseManager locationData;
	private MarkerDatabaseManager markerData;

	// ����markerAttribute
	private String markerAttribute;

	// �Զ��嵯��PopupWindow
	private FinishProjectPopupWindows mFinishProjectPopupWindow;
	private MarkerAttributePopupWindow mMarkerAttributePopupWindow;

	// �˳�ʱ��
	private long mExitTime = 0L;
	
	private boolean mBound; //��ʶ�Ƿ�󶨷���

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// ȥ��������
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mapfragment);

		// ��ȡ��Ŀ���ƣ�����ʾ
		showProjectName();

		// ��ʼ����ͼ
		initMap();

		// �������ݿ�
		openData();

		// ���GPS״̬
		checkGPSStatues();

		// �ж�Google Services �Ƿ����
		connectGoogleServices();

		// �л���ͼ��ʽ
		changeMapStyle();

		// ��ȡλ��
		getCurrentLocation();

		// ���öԱ�ʶ��ļ���
		OnMarkerClickListener();

		// Test
		OnMapClickListener();

		// �󶨺�̨����
		doBindService();
		
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.i(TAG, "onStart() method was invoked");
	}
	
	// �󶨷���
	private void doBindService() {
		Log.i(TAG, "bindService() method was invoked");
		Intent bindIntent = new Intent(MapFragmentActivity.this,MapLocationService.class);
		bindService(bindIntent, connection, Context.BIND_AUTO_CREATE);
	}

	// ����󶨷���
	private void doUnbindService() {
		Log.i(TAG, "unbindService() method was invoked");
		unbindService(connection);
	}

	// �󶨺�̨����
	private ServiceConnection connection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) { // �󶨷���ɹ�ʱ����
			Log.i(TAG,"ServiceConnection()  onServiceConnection() method was invoked");
			
			 mBound = true; //�󶨷���
		}

		@Override
		public void onServiceDisconnected(ComponentName name) { // �������ʱ����
			Log.i(TAG, "ServiceConnection() onServiceDisconnectde() method was invoked");
			mBound = false; //�����
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
						"Marker������" + "\nlatitude: " + latLng.latitude
								+ "\nlongitude: " + latLng.longitude,
						Toast.LENGTH_LONG).show();
			}
		});
	}

	// ��ʼ����ͼ
	private void initMap() {
		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		mMap.setMyLocationEnabled(true);
		mMap.setOnMyLocationButtonClickListener(new OnMyLocationButtonClickListener() {

			@Override
			public boolean onMyLocationButtonClick() {
				Toast.makeText(getBaseContext(), "��λ�У����Ժ�", Toast.LENGTH_LONG)
						.show();
				return false;
			}
		});
	}

	// �������ݿ�
	private void openData() {
		// ����location ���ݿ�
		locationData = new LocationDatabaseManager(context);
		try {
			locationData.open();
			Log.i(TAG, "��location���ݿ�");
		} catch (Exception e) {
			Log.i(TAG, "��location���ݿ���������");
		}

		// ����marker ���ݿ�
		markerData = new MarkerDatabaseManager(context);
		try {
			markerData.open();
			Log.i(TAG, "��marker���ݿ�");
		} catch (Exception e) {
			Log.i(TAG, "��marker���ݿ���������");
		}

		// ����MarkerDateSharedPreference��XML�ļ�
//		markerShared = new MarkerDateSharedPreferenceManager();
//		markerShared.createMarkerDateSharedPreferences(context, projectName);
//		Log.i(TAG, "����MarkerSharedPreference��XML�ļ�" + " context: " + context
//				+ " projectName: " + projectName);
	}

	// �ж�Google Services �Ƿ����
	private void connectGoogleServices() {

		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);

		if (ConnectionResult.SUCCESS == resultCode) {
			Toast.makeText(this, "Google Play Services ����", Toast.LENGTH_LONG)
					.show();
		} else {
			Toast.makeText(this, "Google Play Services ������", Toast.LENGTH_LONG)
					.show();
		}
	}

	// ���GPS״̬
	private void checkGPSStatues() {
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			GPSStatues();
			return;
		}
	}

	// ��ʾGPS�Ի���
	public void GPSStatues() {
		new AlertDialog.Builder(this)
				.setTitle("��λ��ʾ")
				.setMessage("GPSδ�򿪣���������ã�")
				.setPositiveButton("����",
						new android.content.DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent intent = new Intent(
										Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								startActivityForResult(intent, 0);
							}
						})
				.setNegativeButton("ȡ��",
						new android.content.DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
							}
						}).show();

	}

	// ��ʾ��Ŀ���ƺ�����
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

	/******************** ����Marker����ͼ�ϡ�ʵ��MarkerAttributePopupWindow *********************/
	/*
	 * ��㡢��ʼ��������������ť��ʵ��
	 */
	// ����Marker����ͼ��
	public void clickAddMarker(View source) {
		Log.i(TAG, "clickAddMarker,��㰴ť�ѱ�����...");
		Toast.makeText(this, "��㰴ť�ѱ�����", Toast.LENGTH_LONG).show();

		// ��ȡ������location����Ϣ
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

		// �����ȡ�γ�ȴ�String����ת����Double����
		try {
			dLatitude = Double.parseDouble(mLatitude);
			dLongitude = Double.parseDouble(mLongitude);
			dAltitude = Double.parseDouble(mAltitude);

			Log.i(TAG, "clickAddMarker()  dLatitude: " + dLatitude);
			Log.i(TAG, "clickAddMarker()  dLongitude: " + dLongitude);
			Log.i(TAG, "clickAddMarker()  dAltitude: " + dAltitude);

			// ��㵽��ͼ��
			mMap.addMarker(new MarkerOptions().position(new LatLng(dLatitude,
					dLongitude)));
			Log.i(TAG, "clickAddMarker()  ���ɹ�");

			Toast.makeText( getBaseContext(),"Marker������" + "id: " + mId + "\nlatitude: " + dLatitude
							+ "\nlongitude: " + dLongitude + "\naltitude: "
							+ mAltitude, Toast.LENGTH_LONG).show();

			//�ѵ�����Ϣ���ӵ�Marker���ݱ����
//			markerData.addMarkerLocation(dLatitude, dLongitude, dAltitude,
//					projectName, mProjectId);

		} catch (NumberFormatException e) {
			e.printStackTrace();
			Log.e(TAG, "mLatitude��mLongitude ���ݵ�ת�����ִ���");
		}

		// ��ʾMakerAttributePopupWindow
		mMarkerAttributePopupWindow = new MarkerAttributePopupWindow(
				MapFragmentActivity.this, itemsOnClick2, itemSelectedOnClick);
		mMarkerAttributePopupWindow.showAtLocation(
				MapFragmentActivity.this.findViewById(R.id.main), Gravity.LEFT
						| Gravity.CENTER_VERTICAL, 0, 0);

		// Spinner�����ݵ����
		setSpinner();
	}// end of clickAddMarker()

	private void setSpinner() {

		String[] markerAttribute = { "���", "����", "��ѹ��", "������", "���ɵ��", "ת�Ǹ�",
				"�ն˸�", "���Ÿ�" };

		ArrayAdapter<String> adapterMarkerAttribute = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item, markerAttribute);
		adapterMarkerAttribute
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mMarkerAttributePopupWindow.spinner.setAdapter(adapterMarkerAttribute);
	}

	private OnClickListener itemsOnClick2 = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// MarkerAttributePopupWindow ����Map ����
			mMarkerAttributePopupWindow.dismiss();

			switch (v.getId()) {
			case R.id.bt_marker_attribute_confrim:

				// �༭MarkerSharedPreference ��XML �ļ�
//				editMarkerSharedPreference();
				
				//�ѵ�����Ϣ���ӵ�Marker���ݱ����
				//��ȡǰһ��MarkerID,+1Ϊ��ǰmarkerID
			   int lastMarkerID =  markerData.getMarkerID( projectName) + 1 ;
				markerData.addMarkerLocation(lastMarkerID, dLatitude, dLongitude, dAltitude,
						projectName, mProjectId, markerAttribute);
				
				Log.i(TAG, "����Marker ������ markerId: " +" dLatitude: " + dLatitude + " dLongitude:  " + dLongitude
						+ " dAltitude: " + dAltitude + " projectName: " + projectName 
						+ " mProjectId: " + mProjectId + " markerAttribute: " + markerAttribute);
				
//				markerData.addMarkerLocation(dLatitude, dLongitude, dAltitude,
//						projectName, mProjectId, markerAttribute);
//				
				break;
			case R.id.bt_marker_attribute_concel:
				Log.i(TAG, "ȡ������ Marker ������");
				break;
			}
		}
	};

	private OnItemSelectedListener itemSelectedOnClick = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
           //ѡ��ĵ������
			markerAttribute = parent.getItemAtPosition(position).toString();
			Toast.makeText(parent.getContext(), markerAttribute,
					Toast.LENGTH_SHORT).show();

			Log.i(TAG, "OnItemSelectedListener()-onItemSelected()");

		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			Toast.makeText(parent.getContext(), "ʲôҲû��ѡ��", Toast.LENGTH_SHORT).show();
			Log.i(TAG, "OnItemSelectedListener()_onNothingSelected()");
		}
	};

	/******************** ����Marker����ͼ�ϡ�ʵ��MarkerAttributePopupWindow *********************/

	public void clickEndMap(View source) {
		Log.i(TAG, "clickEndMap,������ť�ѱ�����...");
		Toast.makeText(this, "������ť�ѱ�����", Toast.LENGTH_SHORT).show();

		mFinishProjectPopupWindow = new FinishProjectPopupWindows(
				MapFragmentActivity.this, itemsOnClick);
		// ��ʾPopupWindow
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
				Log.i(TAG, "������·");
				
				//�߳�˯��2�룬�ý���رյĸ��ÿ�һЩ
				 try {  
	                    Thread.sleep(2000);  
	                } catch (InterruptedException e) {  
	                    e.printStackTrace();  
	                }  
				finish();
				break;
			case R.id.popupwindow_Button_abandonProject:
				markerData.deleteAllMarker(String.valueOf(project_id));
				Log.i(TAG, "������· " + " ProjcetId: " + project_id);
				// ����������Ŀ���˳����ι��̽���
				onPause();
				break;
			case R.id.popupwindow_cancelButton:
				mFinishProjectPopupWindow.dismiss();
				Log.i(TAG, "ȡ��");
				break;
			}

		}

	};

	// �ı��ͼ����
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

	// ��ȡ��ǰλ��
	private void getCurrentLocation() {

		/*
		 * ���ò�ѯ���� Ϊ�߾��ȡ���Ҫ���Ρ���Ҫ���򡢵͵��������������
		 */
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(true);
		criteria.setBearingRequired(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		criteria.setCostAllowed(true);

		// ��ȡProvider
		String provider = lm.getBestProvider(criteria, true);
		Location location = lm.getLastKnownLocation(provider);

		/*
		 * ���ü��������Զ����µ���Сʱ��Ϊ5�룬��Сλ�Ʊ仯����2��
		 */
		lm.requestLocationUpdates(provider, 5 * 1000, 2, locationListener);

	}

	// λ�øı�ʱ���м���
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

			// ��location��������ӵ�location���ݱ���
			try {
				// locationData.addLocation(location);
				locationData.addLocation(location, project_id);
			} catch (Exception e) {
				e.printStackTrace();
				Log.i(TAG, "add location to the db err");
			}

			// Toast ������
			Toast.makeText(
					getApplicationContext(),
					"���ȣ�" + latitude + "\nγ�ȣ� " + longtitude + "\n���Σ� "
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

	// �� Marker�ĵ㴥���м��
	private void OnMarkerClickListener() {
		mMap.setOnMarkerClickListener(new com.google.android.gms.maps.GoogleMap.OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(final Marker marker) {
				new AlertDialog.Builder(MapFragmentActivity.this)
						.setTitle("ɾ�����")
						.setMessage("ȷ��ɾ���õ㣿")
						.setIcon(R.drawable.ic_bullet_key_permission)
						.setPositiveButton(
								"ȷ��",
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
													Toast.makeText(getBaseContext(),"ɾ��Markerʧ��",Toast.LENGTH_SHORT).show();
													Log.i("DeleteMarker","delete marker from the map err");
												}
											}
										}// end of for

										marker.remove();
									}
								})
						.setNegativeButton(
								"ȡ��",
								new android.content.DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
									}
								}).show();

				Toast.makeText(getBaseContext(), "marker�ѱ�ѡ��",
						Toast.LENGTH_LONG).show();
				Log.i(TAG, "marker�ѱ���");
				return false;
			}

		});

	}

	public void onResume() {

		// ���»ص���ǰ����ʱ�����п��ٶ�λ
		super.onResume();

		// ����ʱ���ٶ�λ
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

	// �˳����ɼ�ʱ
	public void onPause() {
		Log.i(TAG, "onPause() method was invoked");
		super.onPause();
		// �Ƴ�λ�ü���
		// lm.removeUpdates(locationListener);

	}

	@Override
	protected void onStop() {
		super.onStop();
		// �Ƴ�λ�ü���
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
				// ���������back����ʱ��С�����룬���˳�
				Toast.makeText(MapFragmentActivity.this, "�ٰ�һ�µ�ǰ����",
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
