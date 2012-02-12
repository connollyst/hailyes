package com.quane.hail.yes;

/**
 * A simple location object that stores latitude and longitude coordinates.
 * 
 * @author Sean Connolly
 */
public class HailLocation {

	private static final int E6 = 1000000;

	private double latitude;
	private double longitude;

	public HailLocation(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitudeE6() {
		return (int) (latitude * E6);
	}

	public void setLatitudeE6(int latitudeE6) {
		this.latitude = (latitudeE6 / E6);
	}

	public double getLongitudeE6() {
		return (int) (longitude * E6);
	}

	public void setLongitudeE6(int longitudeE6) {
		this.longitude = (longitudeE6 / E6);
	}

}
