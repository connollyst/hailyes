package com.quane.hail.yes.user;

import java.util.List;
import java.util.UUID;

import com.quane.hail.yes.HailLocation;

public class BasicUser {

	private UUID id;
	private HailLocation location;
	private List<BasicUser> neighbors;

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

	public List<BasicUser> getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(List<BasicUser> neighbors) {
		this.neighbors = neighbors;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		BasicUser other = (BasicUser) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
