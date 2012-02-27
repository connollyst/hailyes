package com.quane.hail.yes;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * A simple location object that stores latitude and longitude coordinates.
 * 
 * @author Sean Connolly
 */
public class SimpleLocation {

	private double latitude;
	private double longitude;

	public SimpleLocation() {

	}

	public SimpleLocation(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public SimpleLocation(int latitudeE6, int longitudeE6) {
		setLatitudeE6(latitudeE6);
		setLongitudeE6(longitudeE6);
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

	@JsonIgnore
	public int getLatitudeE6() {
		return (int) (latitude * 1E6);
	}

	@JsonIgnore
	public void setLatitudeE6(int latitudeE6) {
		this.latitude = (latitudeE6 / 1E6);
	}

	@JsonIgnore
	public int getLongitudeE6() {
		return (int) (longitude * 1E6);
	}

	@JsonIgnore
	public void setLongitudeE6(int longitudeE6) {
		this.longitude = (longitudeE6 / 1E6);
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
		SimpleLocation other = (SimpleLocation) obj;
		if (Double.doubleToLongBits(latitude) != Double
				.doubleToLongBits(other.latitude))
			return false;
		if (Double.doubleToLongBits(longitude) != Double
				.doubleToLongBits(other.longitude))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SimpleLocation [latitude=" + latitude + ", longitude="
				+ longitude + "]";
	}

}
