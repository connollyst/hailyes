package com.quane.hail.yes;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * A simple location object that stores latitude and longitude coordinates.
 * 
 * @author Sean Connolly
 */
public class SimpleLocation {

	// We only care to store coordinates out to 6 significant digits
	private final DecimalFormat preciseLocationFormatter = new DecimalFormat(
			"#.######");
	// We can round coordinates down to 2 for rough coordinate calculations
	private final DecimalFormat roughLocationFormatter = new DecimalFormat(
			"#.##");

	private double latitude = 0.0;
	private double longitude = 0.0;

	public SimpleLocation() {
		roughLocationFormatter.setRoundingMode(RoundingMode.HALF_UP);
	}

	public SimpleLocation(double latitude, double longitude) {
		this();
		setLatitude(latitude);
		setLongitude(longitude);
	}

	public SimpleLocation(int latitudeE6, int longitudeE6) {
		this();
		setLatitudeE6(latitudeE6);
		setLongitudeE6(longitudeE6);
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = Long.valueOf(preciseLocationFormatter.format(latitude));
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = Long.valueOf(preciseLocationFormatter
				.format(longitude));
	}

	/**
	 * Rounds down the latitude to a more rough latitude. This number, combined
	 * with the rough longitude, can be used to tell if two users are in the
	 * same location, roughly.
	 * 
	 * @return a rounded down latitude
	 */
	@JsonIgnore
	public double getRoughLatitude() {
		return Double.valueOf(roughLocationFormatter.format(latitude));
	}

	/**
	 * Rounds down the longitude to a more rough longitude. This number,
	 * combined with the rough latitude, can be used to tell if two users are in
	 * the same location, roughly.
	 * 
	 * @return a rounded down longitude
	 */
	@JsonIgnore
	public double getRoughLongitude() {
		return Double.valueOf(roughLocationFormatter.format(longitude));
	}

	/**
	 * Gets the latitude in the 'E6' format.
	 * 
	 * @return
	 */
	@JsonIgnore
	public int getLatitudeE6() {
		return (int) (latitude * 1E6);
	}

	/**
	 * Sets the latitude using the 'E6' format.
	 * 
	 * @param latitudeE6
	 */
	@JsonIgnore
	public void setLatitudeE6(int latitudeE6) {
		this.latitude = (latitudeE6 / 1E6);
	}

	/**
	 * Gets the longitude in the 'E6' format.
	 * 
	 * @return
	 */
	@JsonIgnore
	public int getLongitudeE6() {
		return (int) (longitude * 1E6);
	}

	/**
	 * Sets the longitude using the 'E6' format.
	 * 
	 * @param longitudeE6
	 */
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
