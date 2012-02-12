package com.sean.hailyes.android.app;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;

public class HailLocationListener implements LocationListener {

	private HailActivity hailActivity;

	/**
	 * Default constructor, requires access to the central HailActivity object
	 * so it can update the interface when necessary.
	 * 
	 * @param hailActivity
	 */
	public HailLocationListener(HailActivity hailActivity) {
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
		hailActivity.updateMap(locationGeoPoint);
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
