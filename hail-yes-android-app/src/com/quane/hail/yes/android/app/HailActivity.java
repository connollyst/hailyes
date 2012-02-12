package com.quane.hail.yes.android.app;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.sean.hailyes.android.app.R;

/**
 * The main Activity in 'Hail Yes!'. Displays the Google Map, focused on the
 * user's current location and listens for location updates.
 * 
 * @ * @author Sean Connolly
 */
public class HailActivity extends MapActivity {

	private LocationManager locationManager;
	private LocationListener locationListener;

	private MapView mapView;

	private MapController mapController;

	/**
	 * Home is: -122.424302 37.758654
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Load up the 'main' XML layout
		setContentView(R.layout.main);
		mapView = (MapView) findViewById(R.id.map);
		mapController = mapView.getController();

		// Set the default zoom on the map
		mapController.setZoom(15);

		locationListener = new HailLocationListener(this);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, locationListener);

		// Get the current location in start-up
		GeoPoint initialLocation = getLastKnownGeoPoint();
		if (initialLocation == null) {
			System.err.println("There is no last known location, can't"
					+ " initialize the map.");
		} else {
			updateMap(initialLocation);
		}
	}

	/**
	 * 
	 * @param view
	 */
	public void onHailButtonClick(View view) {
		GeoPoint lastKnownGeoPoint = getLastKnownGeoPoint();
		HailCommunicator communicator = new HailCommunicator();
		communicator.getCurrentState(this, lastKnownGeoPoint);
	}

	public void setLocations(HailLocations locations) {
		if (locations == null || locations.getLocations() == null
				|| locations.getLocations().isEmpty()) {
			System.err.println("Given no locations to set.");
			return;
		}
		for (HailLocation location : locations.getLocations()) {
			System.out.println("Mapping hail location: "
					+ location.getLatitude() + " & " + location.getLongitude());
		}
	}

	/**
	 * 
	 * @param location
	 */
	public void updateMap(GeoPoint location) {
		mapController.animateTo(location);
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
	 * @return the last known GeoPoint, or null if none exists
	 */
	private GeoPoint getLastKnownGeoPoint() {
		GeoPoint lastKnownGeoPoint = null;
		Location lastKnownLocation = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (lastKnownLocation != null) {
			int lastKnownLatitute = (int) (lastKnownLocation.getLatitude() * 1000000);
			int lastKnownLongitude = (int) (lastKnownLocation.getLongitude() * 1000000);
			lastKnownGeoPoint = new GeoPoint(lastKnownLatitute,
					lastKnownLongitude);
		} else {

		}
		return lastKnownGeoPoint;
	}
}