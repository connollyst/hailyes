package com.quane.hail.yes.user;

import java.util.List;
import java.util.UUID;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.quane.hail.yes.SimpleLocation;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

	public enum UserType {
		DRIVER, PASSENGER, UNKNOWN
	}

	private UUID id;
	private UserType type;
	private boolean isMe;
	private boolean isSearching;
	private SimpleLocation location;
	private List<User> neighbors;

	public User() {
		this(UserType.UNKNOWN);
	}

	public User(UserType type) {
		super();
		this.type = type;
		this.isMe = false;
		this.isSearching = false;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UserType getType() {
		return type;
	}

	public void setType(UserType type) {
		this.type = type;
	}

	@JsonIgnore
	public boolean getIsMe() {
		return isMe;
	}

	@JsonIgnore
	public void setIsMe(boolean isMe) {
		this.isMe = isMe;
	}

	public boolean getIsSearching() {
		return isSearching;
	}

	public void setIsSearching(boolean isSearching) {
		this.isSearching = isSearching;
	}

	public SimpleLocation getLocation() {
		return location;
	}

	public void setLocation(SimpleLocation location) {
		this.location = location;
	}

	@JsonIgnore
	public List<User> getNeighbors() {
		return neighbors;
	}

	@JsonIgnore
	public void setNeighbors(List<User> neighbors) {
		this.neighbors = neighbors;
	}

	@JsonIgnore
	public boolean isDriver() {
		return type.equals(UserType.DRIVER);
	}

	@JsonIgnore
	public boolean isPassenger() {
		return type.equals(UserType.PASSENGER);
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
