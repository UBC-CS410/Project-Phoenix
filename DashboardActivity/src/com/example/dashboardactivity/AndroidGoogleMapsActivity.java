package com.example.dashboardactivity;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class AndroidGoogleMapsActivity extends MapActivity implements
		LocationListener {
	private MapView map;
	private MapController controller;
	private Location lastKnowLocation;
	private GeoPoint point;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
			showDialog();
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 500.0f,
				this);

		String locationProvider = LocationManager.GPS_PROVIDER;
		lastKnowLocation = lm.getLastKnownLocation(locationProvider);


		setContentView(R.layout.map_view);
		initMapView();
		initMyLocation();
			

		GeoPoint p = new GeoPoint(-122,37);
		placeMarker(map, p);
		
	}

	/** Find and initialize the map view. */
	private void initMapView() {
		map = (MapView) findViewById(R.id.mapView);
		controller = map.getController();
		map.setBuiltInZoomControls(true);
		
	}
	
	//if gps is show setting dialog
	private void showDialog(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("Error");
		alertDialog.setMessage("Do you want go to turn on GPS");
		alertDialog.setPositiveButton("yes", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				startActivity(new Intent(
						android.provider.Settings.ACTION_WIRELESS_SETTINGS));
			}
		});
		alertDialog.setNegativeButton("cancle", new OnClickListener(){
			@Override
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
				lastKnowLocation = myOverlay.getLastFix();
			}
		});
		map.getOverlays().add(myOverlay);
	}

	// place marker
	public void placeMarker(MapView mapView, GeoPoint geoPoint) {
		List<Overlay> mapOverlays = map.getOverlays();
		Drawable drawable = this.getResources().getDrawable(R.drawable.marker);
		customMarkerOverlay itemizedoverlay = new customMarkerOverlay(drawable, this);
		OverlayItem overlayitem = new OverlayItem(geoPoint, "leo", "chen");
		itemizedoverlay.addOverlay(overlayitem);
		mapOverlays.add(itemizedoverlay);
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		if (location != null) {
			double lat = location.getLatitude();
			double lng = location.getLongitude();
			MapView mapView = (MapView) findViewById(R.id.mapView);
			GeoPoint geoPoint = new GeoPoint((int) (lat * 1E6),
					(int) (lng * 1E6));
			MapController mc = mapView.getController();
			mc.animateTo(geoPoint);
			mc.setZoom(15);
			mapView.invalidate();
		} 

	}

	@Override
	public void onProviderDisabled(String provider) {
		showDialog();
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}
}
