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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(latitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(longitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HailLocation other = (HailLocation) obj;
		if (Double.doubleToLongBits(latitude) != Double
				.doubleToLongBits(other.latitude))
			return false;
		if (Double.doubleToLongBits(longitude) != Double
				.doubleToLongBits(other.longitude))
			return false;
		return true;
	}

}
