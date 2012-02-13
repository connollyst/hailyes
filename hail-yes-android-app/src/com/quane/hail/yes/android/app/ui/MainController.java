package com.quane.hail.yes.android.app.ui;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;
import com.quane.hail.yes.HailLocation;
import com.quane.hail.yes.android.app.MainActivity;
import com.quane.hail.yes.android.app.service.AppLocationListener;
import com.quane.hail.yes.android.app.service.ServerCommunicator;
import com.quane.hail.yes.android.app.ui.overlay.AppOverlay;

public class MainController {

	private static final String TAG = MainController.class.getSimpleName();

	private MainActivity mainActivity;

	private LocationManager locationManager;
	private LocationListener locationListener;

	private AppOverlay mapOverlay;

	private HailLocation myLocation;
	private List<HailLocation> otherLocations;

	// A runnable for executing the private redrawOverlay function in the UI
	// thread
	private final Runnable runnableRedrawOverlay = new Runnable() {
		public void run() {
			redrawOverlay();
		}
	};

	/**
	 * Default constructor.
	 * 
	 * @param mainActivity
	 */
	public MainController(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
		initLocationManager();
		initOverlay();
	}

	/**
	 * Register the GPS location listener and manager
	 */
	public void initLocationManager() {
		locationListener = new AppLocationListener(this);
		locationManager = (LocationManager) mainActivity
				.getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, locationListener);
	}

	/**
	 * Create and initialize the overlay layer
	 */
	public void initOverlay() {
		Drawable drawable = mainActivity.getDrawableMePin();
		mapOverlay = new AppOverlay(drawable);
		mainActivity.getMapViewOverlays().add(mapOverlay);

		// Center on the last known location
		GeoPoint initialLocation = getLastKnownGeoPoint();
		if (initialLocation == null) {
			// TODO
			System.err.println("There is no last known location, can't"
					+ " initialize the map.");
			initialLocation = new GeoPoint(19240000, -99120000);
		}
		mainActivity.centerMap(initialLocation);

		// Set my location for the overlay
		myLocation = new HailLocation(0, 0);
		myLocation.setLatitudeE6(initialLocation.getLatitudeE6());
		myLocation.setLongitudeE6(initialLocation.getLongitudeE6());

		// Draw the overlay
		redrawOverlay();
	}

	/**
	 * A thread safe function to redraw the overlay with a new set of locations.<br/>
	 * Here we queue a request to the MainActivity's Handler to redraw the
	 * overlay who will handle actually updating the UI in the correct thread.
	 * 
	 * @param locations
	 *            the new list of locations for drivers and riders
	 */
	public void redrawOverlay(List<HailLocation> locations) {
		otherLocations = locations;
		getActivityHandler().post(runnableRedrawOverlay);
	}

	/**
	 * Clear the list of overlay items and create a new set that correspond to
	 * the current list of locations.
	 * 
	 * TODO we could probably save a lot of CPU time by not creating new
	 * OverlayItems unless we need to and by just updating the coordinates of
	 * any existing points.. can that be done?
	 * 
	 * @param otherLocations
	 */
	private void redrawOverlay() {
		Log.v(TAG, "Redrawing overlay");
		GeoPoint point;
		OverlayItem item;

		// Remove all previous overlay items
		mapOverlay.clear();

		// Draw my overlay item
		System.out.println("Drawing my location @ " + myLocation.getLatitude()
				+ " & " + myLocation.getLongitude());
		point = new GeoPoint(myLocation.getLatitudeE6(),
				myLocation.getLongitudeE6());
		item = new OverlayItem(point, "Title", "Snippet");
		mapOverlay.addOverlayItem(item);

		// Draw all other overlay items
		if (otherLocations == null || otherLocations.isEmpty()) {
			System.err.println("Given no locations to set.");
		} else {
			for (HailLocation location : otherLocations) {
				System.out.println("Drawing other location @ "
						+ location.getLatitude() + " & "
						+ location.getLongitude());
				point = new GeoPoint(location.getLatitudeE6(),
						location.getLongitudeE6());
				item = new OverlayItem(point, "Title", "Snippet");
				mapOverlay.addOverlayItem(item);
			}
		}
		mapOverlay.populateOverlay();
	}

	/**
	 * Event handler for when the 'Hail!' button is clicked.<br/>
	 * 
	 */
	public void onHailButtonClick() {
		GeoPoint lastKnownGeoPoint = getLastKnownGeoPoint();
		ServerCommunicator communicator = new ServerCommunicator(this);
		communicator.getCurrentState(lastKnownGeoPoint);
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
			System.err.println("No last known location..."
					+ " we don't know where we are!?");
		}
		return lastKnownGeoPoint;
	}

	/**
	 * Handlers are bound to the thread they are created in and execute Messages
	 * and Runnables in that thread. This function returns the Handler created
	 * by the MainActivity and thus gives us access to the UI thread for safe
	 * updates.
	 * 
	 * @return the MainActivity Handler
	 */
	public Handler getActivityHandler() {
		return mainActivity.getHandler();
	}
}
