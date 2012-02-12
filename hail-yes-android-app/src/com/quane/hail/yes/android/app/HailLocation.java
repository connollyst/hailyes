package com.quane.hail.yes.android.app;

/**
 * A simple location object that stores latitude and longitude coordinates.
 * 
 * @author Sean Connolly
 */
public class HailLocation {

	private int latitude;
	private int longitude;

	public HailLocation(int latitude, int longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public int getLatitude() {
		return latitude;
	}

	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}

	public int getLongitude() {
		return longitude;
	}

	public void setLongitude(int longitude) {
		this.longitude = longitude;
	}

}
