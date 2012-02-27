package com.quane.hail.yes.data;

import java.util.List;

import com.quane.hail.yes.SimpleLocation;
import com.quane.hail.yes.user.User;

public interface IDataAccessObject {

	public List<User> getUsersNearLocation(SimpleLocation location);

	public User saveUserLocation(User user);

	public void removeUserLocation(User user);

	public void close();
}
