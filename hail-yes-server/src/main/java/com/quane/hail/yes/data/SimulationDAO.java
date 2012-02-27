package com.quane.hail.yes.data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.quane.hail.yes.SimpleLocation;
import com.quane.hail.yes.user.User;
import com.quane.hail.yes.user.UserDriver;
import com.quane.hail.yes.user.UserPassenger;

/**
 * An implementation of the DAO that runs a simulation.<br/>
 * The demo works by creating a bunch of random cabs and passengers near a
 * user's location if none exist. These locations are constantly updated then,
 * in a very structured way that simulates real activity.
 * 
 * @author Sean Connolly
 */
public class SimulationDAO implements IDataAccessObject {

	private static final Random RANDOMIZER = new Random();

	private static enum DIRECTION {
		NORTH, SOUTH, EAST, WEST
	};

	private final ScheduledExecutorService scheduler = Executors
			.newScheduledThreadPool(1);

	private Logger logger = Logger.getLogger(getClass());

	private SynchronizedUserMap userMap = new SynchronizedUserMap();
	private Map<UUID, DIRECTION> userDirectionMap = new HashMap<UUID, DIRECTION>();
	private long lastQuery = System.currentTimeMillis();

	/**
	 * Default constructor.<br/>
	 * Starts running the simulation right away.
	 */
	public SimulationDAO() {
		super();
		startUpdatingSimulatedUsers();
	}

	/**
	 * 'Close' the DAO. For this DAO it means we stop running the simulation.
	 * This cannot be undone.
	 */
	public void close() {
		logger.info("Shutting down simulation.");
		scheduler.shutdownNow();
	}

	/**
	 * Start running the simulation.<br/>
	 * We initiate a scheduler that will continually update the locations of all
	 * users.
	 */
	public void startUpdatingSimulatedUsers() {
		logger.info("Starting up simulation.");
		scheduler.scheduleAtFixedRate(new Runnable() {
			public void run() {
				// If folks are still asking for updates, continue the
				// simulation
				long current = System.currentTimeMillis();
				if (current - lastQuery < 1000 * 60) {
					updateSimulatedUsers();
				}
			}
		}, 1, 5, TimeUnit.SECONDS);
	}

	/**
	 * Returns a list of users near the location in question.
	 * 
	 * @param location
	 *            they query location
	 * @return a list of users near this location
	 */
	public List<User> getUsersNearLocation(SimpleLocation location) {
		lastQuery = System.currentTimeMillis();
		return userMap.getList();
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
		userMap.add(user);
		spawnNewSimulatedUsers(user.getLocation());
		user.setNeighbors(getUsersNearLocation(user.getLocation()));
		return user;
	}

	/**
	 * 
	 * @param user
	 */
	public void removeUserLocation(User user) {
		userMap.remove(user);
	}

	/**
	 * 
	 * @param location
	 */
	private void spawnNewSimulatedUsers(SimpleLocation location) {
		User user;
		while (userMap.size() < 7 || RANDOMIZER.nextBoolean()) {
			if (RANDOMIZER.nextBoolean()) {
				user = new UserDriver();
			} else {
				user = new UserPassenger();
			}
			double latitude, longitude;
			if (RANDOMIZER.nextBoolean()) {
				latitude = location.getLatitude() - randomOffset();
			} else {
				latitude = location.getLatitude() + randomOffset();
			}
			if (RANDOMIZER.nextBoolean()) {
				longitude = location.getLongitude() - randomOffset();
			} else {
				longitude = location.getLongitude() + randomOffset();
			}
			user.setLocation(new SimpleLocation(latitude, longitude));
			userMap.add(user);
		}
	}

	private static double randomOffset() {
		return RANDOMIZER.nextDouble() * 0.01;
	}

	/**
	 * 
	 */
	private void updateSimulatedUsers() {
		logger.info("Updating simulated user locations (" + userMap.size()
				+ " users)..");
		try {
			double defaultDistance = 0.0001;
			List<DIRECTION> directions = Arrays.asList(DIRECTION.values());
			for (User user : userMap.getList()) {
				logger.info("BEFORE: " + user);
				// Get this user's current direction or pick a random one if
				// there
				// is none
				DIRECTION direction = userDirectionMap.get(user.getId());
				if (direction == null) {
					direction = directions.get(RANDOMIZER.nextInt(directions
							.size()));
				}
				// Give a 1 in 20 chance of trying to change directions
				if (RANDOMIZER.nextInt(20) == 1) {
					direction = directions.get(RANDOMIZER.nextInt(directions
							.size()));
				}
				// Store the new user direction
				userDirectionMap.put(user.getId(), direction);
				// Now we can go ahead and update the user's location according
				// to which direction they are now going
				double dx = 0.0;
				double dy = 0.0;
				switch (direction) {
				case NORTH:
					dx = 0.0;
					dy = defaultDistance;
					break;
				case SOUTH:
					dx = 0.0;
					dy = -defaultDistance;
					break;
				case EAST:
					dx = -defaultDistance;
					dy = 0.0;
					break;
				case WEST:
					dx = defaultDistance;
					dy = 0.0;
					break;
				}
				SimpleLocation userLocation = user.getLocation();
				userLocation.setLatitude(userLocation.getLatitude() + dx);
				userLocation.setLongitude(userLocation.getLongitude() + dy);
				logger.info("AFTER:  " + user);
			}
		} catch (Throwable t) {
			logger.error("An error occurred while running the simulating.", t);
		}
	}

}
