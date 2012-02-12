package com.quane.hail.yes;

/**
 * A simple location object that stores latitude and longitude coordinates.
 * 
 * @author Sean Connolly
 */
public class HailLocation {

	private Long latitude;
	private Long longitude;

	public HailLocation(Long latitude, Long longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Long getLatitude() {
		return latitude;
	}

	public void setLatitude(Long latitude) {
		this.latitude = latitude;
	}

	public Long getLongitude() {
		return longitude;
	}

	public void setLongitude(Long longitude) {
		this.longitude = longitude;
	}

}
