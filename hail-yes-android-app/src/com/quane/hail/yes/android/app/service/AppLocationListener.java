package com.quane.hail.yes.android.app.service;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import com.quane.hail.yes.SimpleLocation;
import com.quane.hail.yes.android.app.ui.MainController;

public class AppLocationListener implements LocationListener {

	private static final String TAG = AppLocationListener.class.getSimpleName();

	private MainController mainController;

	/**
	 * Default constructor, requires access to the central MainController object
	 * so it can communicate messages.
	 * 
	 * @param hailActivity
	 */
	public AppLocationListener(MainController mainController) {
		super();
		this.mainController = mainController;
	}

	/**
	 * Event fired when the device's location is changed.<br/>
	 * Simply passes the word on to the MainController.
	 * 
	 * @param location
	 *            the new location
	 */
	public void onLocationChanged(Location location) {
		Log.v(TAG, "The device's location has changed, updating my location..");
		mainController.setMyLocation(new SimpleLocation(location.getLatitude(),
				location.getLongitude()));
	}

	public void onProviderDisabled(String provider) {
		// TODO Should warn the user that their location is not being updated
		// anymore
	}

	public void onProviderEnabled(String provider) {
		// TODO Should hide any warning that was displayed
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO What is this?
	}

}
