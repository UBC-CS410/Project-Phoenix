package com.example.dashboardactivity;

import android.os.Bundle;

import com.google.android.maps.MapActivity;


public class ShowMapActivity extends MapActivity{
	 @Override
	 protected void onCreate(Bundle icicle) {
	    super.onCreate(icicle);
	    setContentView(R.layout.map_view);
	 }
	 @Override
	 protected boolean isRouteDisplayed() {
	   return false;
	 }
}
