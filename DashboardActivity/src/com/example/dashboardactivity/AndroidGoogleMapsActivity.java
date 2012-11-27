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
	private MainActivity mainActivity;
	
	private String[] statArr; 
	private	double[] latArr; 
	private	double[] lonArr; 
	private	String[] urlArr; 
	
	
	
	// private GeoPoint point;
	Button btnFindme;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//mainActivity = (MainActivity) getApplicationContext();
		//mainActivity.getLatList()
		
		System.out.println("inside map activity");
		List<GeoPoint> geoPointList = new ArrayList<GeoPoint>();
		List<String> statList = new ArrayList<String>();
		List<Bitmap> bitMapList = new ArrayList<Bitmap>();
		List<Double> latList = new ArrayList<Double>();
		List<Double> lonList = new ArrayList<Double>();
		
		// get bundle
		
		Bundle bundle = getIntent().getExtras();
		statArr = bundle.getStringArray("stat");
		latList = (ArrayList<Double>) bundle.getSerializable("lat");
		lonList = (ArrayList<Double>)bundle.getSerializable("lon");
		urlArr = bundle.getStringArray("imgurl");
		
		
		
		System.out.println(statArr.length);
		//System.out.println(latArr.length);
		//System.out.println(lonArr.length);
		System.out.println(urlArr.length);

		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 500.0f,
				this);

		setContentView(R.layout.map_view);
		initMapView();
		initMyLocation();
		
		
		for (int i=0; i<statArr.length; i++){
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
		
		System.out.println("herere??");

//		//place marker
//		GeoPoint p = getGeoPoint(37, -123);
//		Bitmap pic = null;
//		try {
//			pic = drawFromUrl("http://bks6.books.google.com/books?id=aH7BPTrwNXUC&printsec=frontcover&img=1&zoom=5&edge=curl&sig=ACfU3U2aQRnAX2o2ny2xFC1GmVn22almpg");
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}



		//get current location
		String locationProvider = LocationManager.GPS_PROVIDER;
		currentLocation = lm.getLastKnownLocation(locationProvider);

		//find me button
		btnFindme = (Button) findViewById(R.id.btnFindMe);
		btnFindme.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				locationChanged(currentLocation);
			}
		});



//		GeoPoint p1 = new GeoPoint((int) (56.27058500725475 * 1E6), (int) (-2.6984095573425293 * 1E6));
//
//
//		List<GeoPoint> geoPoint = new ArrayList<GeoPoint>();
//		List<Bitmap> bitMap	= new ArrayList<Bitmap>();
//		List<String> string = new ArrayList<String>();
//
//
//		string.add("1");
//		string.add("2");
//		string.add("3");
//		geoPoint.add(p);
//		geoPoint.add(p1);
//		GeoPoint p2 = new GeoPoint((int) (60.00 * 1E6), (int) (-21.69 * 1E6));
//		geoPoint.add(p2);
//		bitMap.add(pic);
//		bitMap.add(pic);
//		bitMap.add(pic);
		
		for(int i=0; i<geoPointList.size(); i++){
			System.out.println("geo"+geoPointList.get(i));
		}
		 
		
		
		customMapOverlay demoOverlay = new customMapOverlay(geoPointList, bitMapList, statList);
		map.getOverlays().add(demoOverlay);

	}

	//initialize map
	private void initMapView() {
		map = (MapView) findViewById(R.id.mapView);
		controller = map.getController();
		map.setBuiltInZoomControls(true);

	}

	// if gps is off, show setting dialog
	private void showDialog() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("Error");
		alertDialog.setMessage("Do you want go to turn on GPS");
		alertDialog.setPositiveButton("yes", new OnClickListener() {
			//@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				startActivity(new Intent(
						android.provider.Settings.ACTION_WIRELESS_SETTINGS));
			}
		});
		alertDialog.setNegativeButton("cancle", new OnClickListener() {
			//@Override
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
				controller.setZoom(15);
				controller.animateTo(myOverlay.getMyLocation());
			}
		});
		map.getOverlays().add(myOverlay);
	}


	//@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	//@Override
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

	//int to geopoint
	public GeoPoint getGeoPoint(double lat, double log){
		GeoPoint geoPoint = new GeoPoint((int) (lat * 1E6),
				(int) (log * 1E6));
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

	//@Override
	public void onProviderDisabled(String provider) {
		if (provider == "gps") {
			showDialog();
		} else {
			showDialog();
		}
	}

	//@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	//@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

}
