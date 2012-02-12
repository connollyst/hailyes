package com.quane.hail.yes;

import java.util.ArrayList;
import java.util.List;

public class HailLocations {
	private List<HailLocation> locations;

	public HailLocations() {
		super();
		locations = new ArrayList<HailLocation>();
	}

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
