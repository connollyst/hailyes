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
import com.quane.hail.yes.android.app.service.ScheduledUpdater;
import com.quane.hail.yes.android.app.service.ServerCommunicator;
import com.quane.hail.yes.android.app.ui.overlay.AppOverlay;

public class MainController {

	private static final String TAG = MainController.class.getSimpleName();

	private MainActivity mainActivity;

	private LocationManager locationManager;
	private LocationListener locationListener;
	private ScheduledUpdater scheduledUpdater;

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
		HailLocation initialLocation = getLastKnownLocation();
		if (initialLocation == null) {
			// TODO
			System.err.println("There is no last known location, can't"
					+ " initialize the map.");
			initialLocation = new HailLocation(0, 0);
		}
		mainActivity.centerMap(new GeoPoint(initialLocation.getLatitudeE6(),
				initialLocation.getLongitudeE6()));

		// Set my location for the overlay
		myLocation = new HailLocation(0, 0);
		myLocation.setLatitudeE6(initialLocation.getLatitudeE6());
		myLocation.setLongitudeE6(initialLocation.getLongitudeE6());

		// Draw the overlay
		redrawOverlay();

		// Start up a scheduled updater to continually poll the server
		scheduledUpdater = new ScheduledUpdater(this);
		scheduledUpdater.start();
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
		HailLocation lastKnownGeoPoint = getLastKnownLocation();
		ServerCommunicator communicator = new ServerCommunicator(this);
		communicator.getNeighbors(lastKnownGeoPoint);
	}

	/**
	 * 
	 * @return the last known location, or null if none exists
	 */
	private HailLocation getLastKnownLocation() {
		HailLocation simpleLocation = null;
		Location lastKnownLocation = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (lastKnownLocation != null) {
			simpleLocation = new HailLocation(lastKnownLocation.getLatitude(),
					lastKnownLocation.getLongitude());
		} else {
			System.err.println("No last known location..."
					+ " we don't know where we are!?");
		}
		return simpleLocation;
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

	/**
	 * Get the current location of the app user.
	 * 
	 * @return the current location
	 */
	public HailLocation getMyLocation() {
		return myLocation;
	}

	/**
	 * Set the current location of the app user.<br/>
	 * Puts in a request to the UI to reflect the changes.
	 * 
	 * @param myLocation
	 *            the current location
	 */
	public void setMyLocation(HailLocation myLocation) {
		this.myLocation = myLocation;
		getActivityHandler().post(runnableRedrawOverlay);
	}
}
