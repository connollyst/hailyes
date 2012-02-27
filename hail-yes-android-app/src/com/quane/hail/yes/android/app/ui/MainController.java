package com.quane.hail.yes.android.app.ui;

import java.util.List;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.quane.hail.yes.SimpleLocation;
import com.quane.hail.yes.android.app.MainActivity;
import com.quane.hail.yes.android.app.service.AppLocationListener;
import com.quane.hail.yes.android.app.service.ScheduledUpdater;
import com.quane.hail.yes.android.app.service.ServerCommunicator;
import com.quane.hail.yes.android.app.ui.overlay.AppOverlay;
import com.quane.hail.yes.user.User;
import com.quane.hail.yes.user.UserPassenger;

/**
 * 
 * @author Sean Connolly
 */
public class MainController {

	private static final String TAG = MainController.class.getSimpleName();

	private MainActivity mainActivity;

	private LocationManager locationManager;
	private LocationListener locationListener;
	private ScheduledUpdater scheduledUpdater;
	private ServerCommunicator communicator;

	private AppOverlay mapOverlay;

	private User me;
	private List<User> neighbors;

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
		initServerCommunicator();
		initLocationManager();
		initOverlay();
	}

	/**
	 * 
	 */
	public void initServerCommunicator() {
		communicator = new ServerCommunicator(this);
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
		mapOverlay = new AppOverlay(mainActivity.getDrawableMePin(),
				mainActivity.getDrawableDriverPin(),
				mainActivity.getDrawablePassengerPin());
		mainActivity.getMapViewOverlays().add(mapOverlay);

		// Center on the last known location
		SimpleLocation initialLocation = getLastKnownLocation();
		if (initialLocation == null) {
			// TODO
			System.err.println("There is no last known location, can't"
					+ " initialize the map.");
			initialLocation = new SimpleLocation(0, 0);
		}
		mainActivity.centerMap(new GeoPoint(initialLocation.getLatitudeE6(),
				initialLocation.getLongitudeE6()));

		// Set my location for the overlay
		SimpleLocation myLocation = new SimpleLocation(
				initialLocation.getLatitudeE6(),
				initialLocation.getLongitudeE6());
		me = new UserPassenger(); // TODO
		me.setLocation(myLocation);
		me.setIsMe(true);

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
	public void redrawOverlay(List<User> users) {
		neighbors = users;
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
	 * @param neighbors
	 */
	private void redrawOverlay() {
		Log.v(TAG, "Redrawing overlay");

		// Remove all previous overlay items
		mapOverlay.clear();

		// Draw my overlay item
		mapOverlay.addOverlayItem(me);

		// Draw all other overlay items
		if (neighbors == null || neighbors.isEmpty()) {
			Log.e(TAG, "Given no locations to set.");
		} else {
			for (User user : neighbors) {
				Log.v(TAG, "Drawing other user " + user);
				mapOverlay.addOverlayItem(user);
			}
		}

		// Push the updates
		mapOverlay.populateOverlay();
		mainActivity.invalidateMapView();
	}

	/**
	 * Event handler for when the 'Hail!' button is clicked.<br/>
	 * 
	 * 
	 */
	public void onHailButtonClick() {
		if (me.getIsSearching()) {
			communicator.unregisterMyself(me);
		} else {
			communicator.registerMyself(me);
		}
	}

	public void onPause() {
		scheduledUpdater.stop();
	}

	public void onResume() {
		scheduledUpdater.start();
	}

	/**
	 * 
	 * @return the last known location, or null if none exists
	 */
	private SimpleLocation getLastKnownLocation() {
		SimpleLocation simpleLocation = null;
		Location lastKnownLocation = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (lastKnownLocation != null) {
			simpleLocation = new SimpleLocation(
					lastKnownLocation.getLatitude(),
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
	public User getMe() {
		return me;
	}

	/**
	 * Set the current location of the app user.<br/>
	 * Puts in a request to the UI to reflect the changes.
	 * 
	 * @param myLocation
	 *            the current location
	 */
	public void setMe(User me) {
		this.me = me;
		getActivityHandler().post(runnableRedrawOverlay);
	}
}
