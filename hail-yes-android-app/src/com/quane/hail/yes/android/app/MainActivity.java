package com.quane.hail.yes.android.app;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.quane.hail.yes.android.app.ui.MainController;

/**
 * The main Activity in 'Hail Yes!'. Displays the Google Map, focused on the
 * user's current location and listens for location updates.
 * 
 * @ * @author Sean Connolly
 */
public class MainActivity extends MapActivity {

	private MainController mainController;
	private MapView mapView;

	private final Handler handler = new Handler();

	/**
	 * Home is: -122.424302 x 37.758654
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mapView = (MapView) findViewById(R.id.map);
		mapView.setBuiltInZoomControls(true);

		// Set the default zoom on the map
		mapView.getController().setZoom(16);
	}

	/**
	 * Always returns false, we don't display routes.
	 */
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	/**
	 * 
	 * @param view
	 */
	public void onHailButtonClick(View view) {
		mainController.onHailButtonClick();
	}

	/**
	 * 
	 * @return
	 */
	public List<Overlay> getMapViewOverlays() {
		return mapView.getOverlays();
	}

	/**
	 * 
	 */
	public void invalidateMapView() {
		mapView.invalidate();
	}

	/**
	 * 
	 * @param location
	 */
	public void updateMap(GeoPoint location) {
		mapView.getController().animateTo(location);
	}

	public Drawable getDrawableMePin() {
		return this.getResources().getDrawable(R.drawable.me_pin_32);
	}

	/**
	 * 
	 * @return
	 */
	public Handler getHandler() {
		return handler;
	}

}