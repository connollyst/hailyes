package com.quane.hail.yes.user;

import com.quane.hail.yes.HailLocation;

public abstract class AbstractUser {

	private HailLocation location;

	public HailLocation getLocation() {
		return location;
	}

	public void setLocation(HailLocation location) {
		this.location = location;
	}

}