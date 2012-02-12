package com.quane.hail.yes.android.app;

/**
 * A simple location object that stores latitude and longitude coordinates.
 * 
 * @author Sean Connolly
 */
public class HailLocation {

	public int latitude;
	public int longitude;

	public HailLocation() {
		System.out.println("Called default HailLocation constructor.");
	}

	public HailLocation(int latitude, int longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public int getLatitude() {
		return latitude;
	}

	public void setLatitude(int latitude) {
		System.out.println("> Setting latitude");
		this.latitude = latitude;
	}

	public int getLongitude() {
		return longitude;
	}

	public void setLongitude(int longitude) {
		System.out.println("> Setting longitude");
		this.longitude = longitude;
	}

}
