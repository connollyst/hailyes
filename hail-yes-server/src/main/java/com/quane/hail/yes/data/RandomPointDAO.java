package com.quane.hail.yes.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.quane.hail.yes.SimpleLocation;
import com.quane.hail.yes.user.User;
import com.quane.hail.yes.user.UserDriver;
import com.quane.hail.yes.user.UserPassenger;

/**
 * Data access object.
 * 
 * @author Sean Connolly
 */
public class RandomPointDAO implements IDataAccessObject {

	private static final Random generator = new Random();
	private UserList userList = new UserList();

	/**
	 * Returns a list of users near the location in question.
	 * 
	 * @param location
	 *            they query location
	 * @return a list of users near this location
	 */
	public List<User> getUsersNearLocation(SimpleLocation location) {
		List<User> users = new ArrayList<User>();
		User user;
		// fake driver #1
		user = new UserDriver();
		user.setLocation(new SimpleLocation(location.getLatitude()
				- randomOffset(), location.getLongitude() - randomOffset()));
		users.add(user);

		// fake driver #2
		user = new UserDriver();
		user.setLocation(new SimpleLocation(location.getLatitude()
				+ randomOffset(), location.getLongitude() - randomOffset()));
		users.add(user);

		// fake rider #1
		user = new UserPassenger();
		user.setLocation(new SimpleLocation(location.getLatitude()
				+ randomOffset(), location.getLongitude() + randomOffset()));
		users.add(user);

		// fake rider #2
		user = new UserPassenger();
		user.setLocation(new SimpleLocation(location.getLatitude()
				- randomOffset(), location.getLongitude() + randomOffset()));
		users.add(user);

		return users;
	}

	/**
	 * Stores a user's location, creating a record for the user if one doesn't
	 * already exist, updating the record if one does.<br/>
	 * A list of users near the new location is returned to prevent the need for
	 * a follow up query.
	 * 
	 * @param user
	 *            the user to be created or updated
	 * @return a list of users near this user
	 */
	public User saveUserLocation(User user) {
		userList.add(user);
		user.setNeighbors(getUsersNearLocation(user.getLocation()));
		return user;
	}

	/**
	 * 
	 * @param user
	 */
	public void removeUserLocation(User user) {
		userList.remove(user);
	}

	private static double randomOffset() {
		return generator.nextDouble() * 0.01;
	}
}
