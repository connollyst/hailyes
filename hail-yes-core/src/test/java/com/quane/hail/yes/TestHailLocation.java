package com.quane.hail.yes;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class TestHailLocation {

	private static final double TEST_LATITUDE = 37.758654;
	private static final double TEST_LONGITUDE = -122.424302;
	private static final int TEST_LATITUDE_E6 = 37758654;
	private static final int TEST_LONGITUDE_E6 = -122424302;

	@Test
	public void testGetLatitudeE6() {
		HailLocation loc = new HailLocation(TEST_LATITUDE, TEST_LONGITUDE);
		int latE6 = loc.getLatitudeE6();
		assertTrue(TEST_LATITUDE_E6 == latE6);
	}

	@Test
	public void testSetLatitudeE6() {
		HailLocation loc = new HailLocation(0, 0);
		loc.setLatitudeE6(TEST_LATITUDE_E6);
		double lat = loc.getLatitude();
		assertTrue(TEST_LATITUDE == lat);
	}

	@Test
	public void testGetLongitudeE6() {
		HailLocation loc = new HailLocation(TEST_LATITUDE, TEST_LONGITUDE);
		int lonE6 = loc.getLongitudeE6();
		assertTrue(TEST_LONGITUDE_E6 == lonE6);
	}

	@Test
	public void testSetLongitudeE6() {
		HailLocation loc = new HailLocation(0, 0);
		loc.setLongitudeE6(TEST_LONGITUDE_E6);
		double lon = loc.getLongitude();
		assertTrue(TEST_LONGITUDE == lon);
	}

}
