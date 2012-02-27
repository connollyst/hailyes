package com.quane.hail.yes.data;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
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

	private static final Random generator = new Random();

	private Logger logger = Logger.getLogger(getClass());

	private UserList userList = new UserList();

	private final ScheduledExecutorService scheduler = Executors
			.newScheduledThreadPool(1);

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
		scheduler.shutdownNow();
	}

	/**
	 * Start running the simulation.<br/>
	 * We initiate a scheduler that will continually update the locations of all
	 * users.
	 */
	public void startUpdatingSimulatedUsers() {
		scheduler.scheduleAtFixedRate(new Runnable() {
			public void run() {
				updateSimulatedUsers();
			}
		}, 10, 10, TimeUnit.SECONDS);
	}

	/**
	 * Returns a list of users near the location in question.
	 * 
	 * @param location
	 *            they query location
	 * @return a list of users near this location
	 */
	public List<User> getUsersNearLocation(SimpleLocation location) {
		return userList.getList();
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
		spawnNewSimulatedUsers(user.getLocation());
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

	/**
	 * 
	 * @param location
	 */
	private void spawnNewSimulatedUsers(SimpleLocation location) {
		User user;
		while (userList.size() < 7 || generator.nextBoolean()) {
			if (generator.nextBoolean()) {
				user = new UserDriver();
			} else {
				user = new UserPassenger();
			}
			double latitude, longitude;
			if (generator.nextBoolean()) {
				latitude = location.getLatitude() - randomOffset();
			} else {
				latitude = location.getLatitude() + randomOffset();
			}
			if (generator.nextBoolean()) {
				longitude = location.getLongitude() - randomOffset();
			} else {
				longitude = location.getLongitude() + randomOffset();
			}
			user.setLocation(new SimpleLocation(latitude, longitude));
			userList.add(user);
		}
	}

	private static double randomOffset() {
		return generator.nextDouble() * 0.01;
	}

	/**
	 * 
	 */
	private void updateSimulatedUsers() {
		logger.info("Running updateSimulatedUsers");

	}

}
