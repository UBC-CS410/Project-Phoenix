package com.example.dashboardactivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.example.dashboardactivity.MainActivity.ImageAdapter;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class AndroidGoogleMapsActivity extends MapActivity implements
		LocationListener {
	private MapView map;
	private MapController controller;
	private Location currentLocation;
	customMapOverlay mapOverlay;

	private String[] statArr;
	private String[] urlArr;
	private boolean isGpsEnabled, isNetworkEnabled;
	LocationManager lm;

	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_view);
		initMapView();
		initMyLocation();
		map.setSatellite(false);
		List<GeoPoint> geoPointList = new ArrayList<GeoPoint>();
		List<String> statList = new ArrayList<String>();
		List<Bitmap> bitMapList = new ArrayList<Bitmap>();
		List<Double> latList = new ArrayList<Double>();
		List<Double> lonList = new ArrayList<Double>();

		// get bundle

		Bundle bundle = getIntent().getExtras();
		statArr = bundle.getStringArray("stat");
		 latList = (ArrayList<Double>) bundle.getSerializable("lat");
		 lonList = (ArrayList<Double>) bundle.getSerializable("lon");
		urlArr = bundle.getStringArray("imgurl");

		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		isGpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
		isNetworkEnabled = lm
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

		if (isNetworkEnabled) {
			lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
					MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
			Log.d("Network", "Network");
			if (lm != null) {
				currentLocation = lm
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

			}
		}

		if (isGpsEnabled) {
			if (currentLocation == null) {
				lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
						MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES,
						this);
				Log.d("GPS Enabled", "GPS Enabled");
				if (lm != null) {
					currentLocation = lm
							.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				}
			}
		}

		if(!isGpsEnabled && !isNetworkEnabled){
			showDialog();
		}

		for (int i = 0; i < statArr.length; i++) {
			geoPointList.add(getGeoPoint(latList.get(i), lonList.get(i)));
			statList.add(statArr[i]);
			try {
				bitMapList.add(drawFromUrl(urlArr[i]));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}


		if (mapOverlay == null) {
			mapOverlay = new customMapOverlay(geoPointList, bitMapList,
					statList);
			map.getOverlays().add(mapOverlay);
		} else {
			map.getOverlays().remove(mapOverlay);
			map.invalidate();
			mapOverlay = new customMapOverlay(geoPointList, bitMapList,
					statList);
			map.getOverlays().add(mapOverlay);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.map_menu, menu);
		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.satellite:
			map.setSatellite(true);
			return true;
		case R.id.Street:
			map.setSatellite(false);
			return true;
		case R.id.FindMe:
			locationChanged(currentLocation);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}
	

	// initialize map
	private void initMapView() {
		map = (MapView) findViewById(R.id.mapView);
		controller = map.getController();
		map.setBuiltInZoomControls(true);
		controller.setZoom(12);
	}

	// if gps is off, show setting dialog
	private void showDialog() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("Error");
		alertDialog.setMessage("Do you want go to turn on GPS?");
		alertDialog.setPositiveButton("yes", new OnClickListener() {
			// @Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				startActivity(new Intent(
						android.provider.Settings.ACTION_WIRELESS_SETTINGS));
			}
		});
		alertDialog.setNegativeButton("cancle", new OnClickListener() {
			// @Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		alertDialog.show();
	}

	// start tracking
	private void initMyLocation() {
		final MyLocationOverlay myOverlay = new MyLocationOverlay(this, map);
		myOverlay.enableMyLocation();
		myOverlay.enableCompass();
		myOverlay.runOnFirstFix(new Runnable() {
			public void run() {
			}
		});
		map.getOverlays().add(myOverlay);
	}

	// @Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	// @Override
	public void onLocationChanged(Location location) {
		currentLocation = location;
	}

	// use to find me
	public void locationChanged(Location location) {
		if (location != null) {
			double lat = location.getLatitude();
			double lng = location.getLongitude();
			GeoPoint geoPoint = new GeoPoint((int) (lat * 1E6),
					(int) (lng * 1E6));

			controller.animateTo(geoPoint);
			controller.setZoom(15);
			map.invalidate();
		}
	}

	// int to geopoint
	public GeoPoint getGeoPoint(double lat, double log) {
		GeoPoint geoPoint = new GeoPoint((int) (lat * 1E6), (int) (log * 1E6));
		return geoPoint;
	}

	// convert url to bitmap
	Bitmap drawFromUrl(String url) throws java.net.MalformedURLException,
			java.io.IOException {
		Bitmap x;
		HttpURLConnection connection = (HttpURLConnection) new URL(url)
				.openConnection();
		connection.setRequestProperty("User-agent", "Mozilla/4.0");
		connection.connect();
		InputStream input = connection.getInputStream();
		x = BitmapFactory.decodeStream(input);
		return x;
	}
	
	protected void onPause(){
		super.onPause();
		Log.d("TAG", "-----onPause-----");
	}

	// @Override
	public void onProviderDisabled(String provider) {
			showDialog();
	}

	// @Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	// @Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

}
