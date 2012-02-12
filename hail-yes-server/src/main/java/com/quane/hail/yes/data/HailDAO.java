package com.quane.hail.yes.data;

import java.util.ArrayList;
import java.util.List;

import com.quane.hail.yes.Cabbie;
import com.quane.hail.yes.HailUser;
import com.quane.hail.yes.Hailer;
import com.quane.hail.yes.Location;

/**
 * Data access object.
 * 
 * @author Sean Connolly
 */
public class HailDAO {

	/**
	 * 
	 * @param location
	 * @return
	 */
	public static List<HailUser> getUsersNearLocation(Location location) {
		List<HailUser> users = new ArrayList<HailUser>();
		HailUser user;
		// fake cabbie #1
		user = new Cabbie();
		user.setLocation(new Location(location.getLatitude() - 1, location
				.getLongitude() - 1));
		users.add(user);

		// fake cabbie #2
		user = new Cabbie();
		user.setLocation(new Location(location.getLatitude() + 1, location
				.getLongitude() - 1));
		users.add(user);

		// fake hailer #1
		user = new Hailer();
		user.setLocation(new Location(location.getLatitude() + 1, location
				.getLongitude() + 1));
		users.add(user);

		// fake hailer #2
		user = new Hailer();
		user.setLocation(new Location(location.getLatitude() - 1, location
				.getLongitude() + 1));
		users.add(user);

		return users;
	}
}
