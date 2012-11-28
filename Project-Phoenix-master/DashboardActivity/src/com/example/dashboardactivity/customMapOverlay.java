package com.example.dashboardactivity;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.RectF;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class customMapOverlay extends Overlay {

	private ArrayList<GeoPoint> _displayedMarkers;
	private LinearLayout _bubbleLayout;
	private List<GeoPoint> geoPointList;
	private List<Bitmap> bitMapList;
	private List<String> stringList;

	public customMapOverlay(List<GeoPoint> g, List<Bitmap> b, List<String> s) {
		geoPointList = g;
		bitMapList = b;
		stringList = s;
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		// print();// test

		super.draw(canvas, mapView, shadow);
		GeoPoint geoPoint;
		Bitmap marker;
		for (int i = 0; i < geoPointList.size(); i++) {
			geoPoint = geoPointList.get(i);
			marker = bitMapList.get(i);

			Projection projection = mapView.getProjection();

			int latSpan = mapView.getLatitudeSpan();
			int lngSpan = mapView.getLongitudeSpan();
			GeoPoint mapCenter = mapView.getMapCenter();
			int mapLeftGeo = mapCenter.getLongitudeE6() - (lngSpan / 2);
			int mapRightGeo = mapCenter.getLongitudeE6() + (lngSpan / 2);

			int mapTopGeo = mapCenter.getLatitudeE6() - (latSpan / 2);
			int mapBottomGeo = mapCenter.getLatitudeE6() + (latSpan / 2);

			_displayedMarkers = new ArrayList<GeoPoint>();

			if ((geoPoint.getLatitudeE6() > mapTopGeo && geoPoint
					.getLatitudeE6() < mapBottomGeo)
					&& (geoPoint.getLongitudeE6() > mapLeftGeo && geoPoint
							.getLongitudeE6() < mapRightGeo)) {

				Point myPoint = new Point();
				projection.toPixels(geoPoint, myPoint);

				canvas.drawBitmap(marker, myPoint.x - 15, myPoint.y - 30, null);

				_displayedMarkers.add(geoPoint); // Add this line ....

			}
		}
	}

	@Override
	public boolean onTap(GeoPoint p, MapView mapView) {

		// If a bubble is currently displayed then clear it..
		if (_bubbleLayout != null) {
			mapView.removeView(_bubbleLayout);
		}

		if (performHitTest(mapView, p)) {

			// Get instance of the Bubble Layout ...
			LayoutInflater inflater = (LayoutInflater) mapView.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			_bubbleLayout = (LinearLayout) inflater.inflate(R.layout.bubble,
					mapView, false);

			// Locate the TextView
			TextView locationNameText = (TextView) _bubbleLayout
					.findViewById(R.id.status);

			GeoPoint newP = null;
			// Set the Text
			for (int i = 0; i < geoPointList.size(); i++) {
				if (hitTest(mapView, p, geoPointList.get(i))) {
					locationNameText.setText(stringList.get(i));
					newP = geoPointList.get(i);
				}
			}

			// .. configure its layout parameters
			MapView.LayoutParams params = new MapView.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, newP,
					MapView.LayoutParams.BOTTOM_CENTER);

			_bubbleLayout.setLayoutParams(params);

			// Add the view to the Map
			mapView.addView(_bubbleLayout);

			// Animate the map to center on the location
			mapView.getController().animateTo(newP);
			mapView.getController().setZoom(10);
		}

		return true;

	};

	private boolean performHitTest(MapView mapView, GeoPoint geo) {

		// Declare locals
		RectF hitTestRecr = new RectF();
		Point tappedCoordinates = new Point();
		Point locationCoordinates = new Point();
		Projection projection = mapView.getProjection();
		projection.toPixels(geo, tappedCoordinates);

		hitTestRecr.set(-25, -25, 25, 25);
		hitTestRecr.offset(tappedCoordinates.x, tappedCoordinates.y);

		for (int i = 0; i < geoPointList.size(); i++) {
			GeoPoint Location = geoPointList.get(i);
			projection.toPixels(Location, locationCoordinates);
			if (hitTestRecr.contains(locationCoordinates.x,
					locationCoordinates.y)) {
				return true;
			}
		}

		return false;
	}

	private boolean hitTest(MapView mapView, GeoPoint geo, GeoPoint p) {

		// Declare locals
		RectF hitTestRecr = new RectF();
		Point tappedCoordinates = new Point();
		Point locationCoordinates = new Point();

		Projection projection = mapView.getProjection();
		projection.toPixels(geo, tappedCoordinates);

		hitTestRecr.set(-25, -25, 25, 25);
		hitTestRecr.offset(tappedCoordinates.x, tappedCoordinates.y);

		projection.toPixels(p, locationCoordinates);
		if (hitTestRecr.contains(locationCoordinates.x, locationCoordinates.y)) {
			return true;

		}

		return false;
	}

}
