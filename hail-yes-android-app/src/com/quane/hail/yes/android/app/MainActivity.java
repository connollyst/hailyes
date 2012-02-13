package com.quane.hail.yes.android.app;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.quane.hail.yes.HailLocation;
import com.quane.hail.yes.HailLocations;
import com.quane.hail.yes.android.app.service.ServerCommunicator;
import com.quane.hail.yes.android.app.service.AppLocationListener;
import com.quane.hail.yes.android.app.ui.overlay.DriverOverlay;

/**
 * The main Activity in 'Hail Yes!'. Displays the Google Map, focused on the
 * user's current location and listens for location updates.
 * 
 * @ * @author Sean Connolly
 */
public class MainActivity extends MapActivity {

	private LocationManager locationManager;
	private LocationListener locationListener;

	private MapView mapView;

	private MapController mapController;
	private DriverOverlay itemizedoverlay;
	private List<Overlay> mapOverlays;

	final Handler mHandler = new Handler();

	/**
	 * Home is: -122.424302 x 37.758654
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Load up the 'main' XML layout
		setContentView(R.layout.main);
		mapView = (MapView) findViewById(R.id.map);
		mapView.setBuiltInZoomControls(true);
		mapController = mapView.getController();

		// Register the GPS location listener and manager
		locationListener = new AppLocationListener(this);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, locationListener);

		// Add the overlay layer
		mapOverlays = mapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(
				R.drawable.me_pin_32);
		itemizedoverlay = new DriverOverlay(drawable, this);
		mapOverlays.add(itemizedoverlay);

		// Set the default zoom on the map
		mapController.setZoom(16);

		// Get the current location in start-up
		GeoPoint initialLocation = getLastKnownGeoPoint();
		if (initialLocation == null) {
			System.err.println("There is no last known location, can't"
					+ " initialize the map.");
			// TODO
			initialLocation = new GeoPoint(19240000, -99120000);
		} else {
			updateMap(initialLocation);
		}

		// Add a locator for my current location
		OverlayItem overlayitem = new OverlayItem(initialLocation, "Me",
				"Hail Yes!");
		itemizedoverlay.addOverlay(overlayitem);
	}

	/**
	 * 
	 * @param view
	 */
	public void onHailButtonClick(View view) {
		GeoPoint lastKnownGeoPoint = getLastKnownGeoPoint();
		ServerCommunicator communicator = new ServerCommunicator(this);
		communicator.getCurrentState(lastKnownGeoPoint);
	}

	/**
	 * 
	 * @param locations
	 */
	public void setLocations(HailLocations locations) {
		itemizedoverlay.clear();
		if (locations == null || locations.getLocations() == null
				|| locations.getLocations().isEmpty()) {
			System.err.println("Given no locations to set.");
			return;
		}
		for (HailLocation location : locations.getLocations()) {
			System.out.println("Mapping hail location: "
					+ location.getLatitude() + " & " + location.getLongitude());
			GeoPoint point = new GeoPoint(location.getLatitudeE6(),
					location.getLongitudeE6());
			OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!",
					"I'm in Mexico City!");
			itemizedoverlay.addOverlay(overlayitem);
		}
		mapView.invalidate();
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
			int lastKnownLatitute = (int) (lastKnownLocation.getLatitude() * 1E6);
			int lastKnownLongitude = (int) (lastKnownLocation.getLongitude() * 1E6);
			lastKnownGeoPoint = new GeoPoint(lastKnownLatitute,
					lastKnownLongitude);
		} else {

		}
		return lastKnownGeoPoint;
	}

	public Handler getmHandler() {
		return mHandler;
	}

}