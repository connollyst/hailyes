package com.quane.hail.yes.data;

import java.util.ArrayList;
import java.util.List;

import com.quane.hail.yes.HailLocation;
import com.quane.hail.yes.HailLocations;
import com.quane.hail.yes.user.Cabbie;
import com.quane.hail.yes.user.HailUser;
import com.quane.hail.yes.user.Hailer;

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
	public static HailLocations getUsersNearLocation(HailLocation location) {
		List<HailUser> users = new ArrayList<HailUser>();
		HailUser user;
		// fake cabbie #1000
		user = new Cabbie();
		user.setLocation(new HailLocation(location.getLatitude() - 1000,
				location.getLongitude() - 1000));
		users.add(user);

		// fake cabbie #2
		user = new Cabbie();
		user.setLocation(new HailLocation(location.getLatitude() + 1000,
				location.getLongitude() - 1000));
		users.add(user);

		// fake hailer #1000
		user = new Hailer();
		user.setLocation(new HailLocation(location.getLatitude() + 1000,
				location.getLongitude() + 1000));
		users.add(user);

		// fake hailer #2
		user = new Hailer();
		user.setLocation(new HailLocation(location.getLatitude() - 1000,
				location.getLongitude() + 1000));
		users.add(user);

		HailLocations locations = new HailLocations();
		for (HailUser nextUser : users) {
			locations.getLocations().add(nextUser.getLocation());
		}
		return locations;
	}
}
