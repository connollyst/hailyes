package com.sean.hailyes.android.app;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;

public class HailLocationListener implements LocationListener {

	private Hail hail;

	public HailLocationListener(Hail hail) {
		super();
		this.hail = hail;
	}

	public void onLocationChanged(Location location) {
		GeoPoint locationGeoPoint = new GeoPoint(
				(int) (location.getLatitude() * 1000000),
				(int) (location.getLongitude() * 1000000));
		hail.updateMap(locationGeoPoint);
	}

	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

}
