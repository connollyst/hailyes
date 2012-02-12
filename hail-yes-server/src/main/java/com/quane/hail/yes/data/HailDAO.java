package com.quane.hail.yes.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.quane.hail.yes.HailLocation;
import com.quane.hail.yes.user.AbstractUser;
import com.quane.hail.yes.user.Driver;
import com.quane.hail.yes.user.Rider;

/**
 * Data access object.
 * 
 * @author Sean Connolly
 */
public class HailDAO {

	private static Map<UUID, AbstractUser> users = Collections
			.synchronizedMap(new HashMap<UUID, AbstractUser>());

	/**
	 * Returns a list of users near the location in question.
	 * 
	 * @param location
	 *            they query location
	 * @return a list of users near this location
	 */
	public static List<AbstractUser> getUsersNearLocation(HailLocation location) {
		List<AbstractUser> users = new ArrayList<AbstractUser>();
		AbstractUser user;
		// fake driver #1
		user = new Driver();
		user.setLocation(new HailLocation(location.getLatitude() - 1000,
				location.getLongitude() - 1000));
		users.add(user);

		// fake driver #2
		user = new Driver();
		user.setLocation(new HailLocation(location.getLatitude() + 1000,
				location.getLongitude() - 1000));
		users.add(user);

		// fake rider #1
		user = new Rider();
		user.setLocation(new HailLocation(location.getLatitude() + 1000,
				location.getLongitude() + 1000));
		users.add(user);

		// fake rider #2
		user = new Rider();
		user.setLocation(new HailLocation(location.getLatitude() - 1000,
				location.getLongitude() + 1000));
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
	public static AbstractUser saveUserLocation(AbstractUser user) {
		if (user.getId() == null) {
			user.setId(UUID.randomUUID());
		}
		users.put(user.getId(), user);
		user.setNeighbors(getUsersNearLocation(user.getLocation()));
		return user;
	}

	/**
	 * 
	 * @param user
	 */
	public static void removeUserLocation(AbstractUser user) {
		users.remove(user.getId());
	}
}
