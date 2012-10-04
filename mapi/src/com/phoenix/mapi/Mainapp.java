package com.phoenix.mapi;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

public class Mainapp extends MapActivity{
	
	MapController mControl;
	GeoPoint GeoP;
	MapView mapV;

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		mapV = (MapView) findViewById(R.id.mapView);
		mapV.displayZoomControls(true);
		mapV.setBuiltInZoomControls(true);
		
		double lat = 49.25;
		double lng = -123.11;
		
		GeoP = new GeoPoint ((int) ( lat*1E6), (int) ( lng*1E6));
		
		mControl = mapV.getController();
		mControl.animateTo(GeoP);
		mControl.setZoom(13);
		
		
	}
	
}
