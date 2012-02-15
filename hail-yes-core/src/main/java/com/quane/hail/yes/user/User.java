package com.quane.hail.yes.user;

import java.util.List;
import java.util.UUID;

import com.quane.hail.yes.SimpleLocation;

public class User {

	public enum UserType {
		DRIVER, PASSENGER
	}

	private UserType type;
	private UUID id;
	private SimpleLocation location;
	private List<User> neighbors;

	public User(UserType type) {
		super();
		this.type = type;
	}

	public UserType getType() {
		return type;
	}

	public void setType(UserType type) {
		this.type = type;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public SimpleLocation getLocation() {
		return location;
	}

	public void setLocation(SimpleLocation location) {
		this.location = location;
	}

	public List<User> getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(List<User> neighbors) {
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
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return type + " [id=" + id + ", location=" + location + ", neighbors="
				+ neighbors + "]";
	}

}
