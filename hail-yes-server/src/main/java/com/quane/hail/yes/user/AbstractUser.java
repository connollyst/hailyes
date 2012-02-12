package com.quane.hail.yes.user;

import java.util.List;
import java.util.UUID;

import com.quane.hail.yes.HailLocation;

public abstract class AbstractUser {

	private UUID id;
	private HailLocation location;
	private List<AbstractUser> neighbors;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public HailLocation getLocation() {
		return location;
	}

	public void setLocation(HailLocation location) {
		this.location = location;
	}

	public List<AbstractUser> getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(List<AbstractUser> neighbors) {
		this.neighbors = neighbors;
	}

}
