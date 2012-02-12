package com.quane.hail.yes.android.app;

import java.util.List;

public class HailLocations {
	private List<HailLocation> locations;

	public HailLocations(List<HailLocation> locations) {
		super();
		this.locations = locations;
	}

	public List<HailLocation> getLocations() {
		return locations;
	}

	public void setLocations(List<HailLocation> locations) {
		this.locations = locations;
	}

}
