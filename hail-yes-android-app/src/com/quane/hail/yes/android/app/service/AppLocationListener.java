package com.quane.hail.yes.android.app.service;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.quane.hail.yes.android.app.MainActivity;

public class AppLocationListener implements LocationListener {

	private MainActivity hailActivity;

	/**
	 * Default constructor, requires access to the central HailActivity object
	 * so it can update the interface when necessary.
	 * 
	 * @param hailActivity
	 */
	public AppLocationListener(MainActivity hailActivity) {
		super();
		this.hailActivity = hailActivity;
	}

	/**
	 * 
	 * @param location
	 *            the new location
	 */
	public void onLocationChanged(Location location) {
		GeoPoint locationGeoPoint = new GeoPoint(
				(int) (location.getLatitude() * 1000000),
				(int) (location.getLongitude() * 1000000));
//		hailActivity.updateMap(locationGeoPoint);
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
