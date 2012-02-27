package com.quane.hail.yes.android.app.service;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

import com.quane.hail.yes.android.app.ui.MainController;

/**
 * A class for updating the UI at scheduled intervals.
 * 
 * @author Sean Connolly
 */
public class ScheduledUpdater {

	private static final String TAG = ScheduledUpdater.class.getSimpleName();

	private static final int DELAY = 5000;

	private MainController mainController;
	private Handler mainHandler;

	boolean isRunning = false;
	long mStartTime = 0L;

	/**
	 * Default constructor.
	 * 
	 * @param mainController
	 */
	public ScheduledUpdater(MainController mainController) {
		this.mainController = mainController;
		this.mainHandler = mainController.getActivityHandler();
	}

	/**
	 * Start polling for updates on neightbors' locations.
	 */
	public void start() {
		if (!isRunning) {
			Log.i(TAG, "Starting polling for updates.");
			setRunning(true);
			mStartTime = System.currentTimeMillis();
			mainHandler.removeCallbacks(runnableUpdateTask);
			mainHandler.postDelayed(runnableUpdateTask, DELAY);
		} else {
			Log.e(TAG, "Cannot start polling for updates,"
					+ " already doing so.");
		}
	}

	/**
	 * Stop polling for updates on neighbors' locations.
	 */
	public void stop() {
		if (isRunning) {
			Log.i(TAG, "Stopping polling for updates.");
			mainHandler.removeCallbacks(runnableUpdateTask);
			setRunning(false);
		} else {
			Log.e(TAG, "Cannot stop polling for updates,"
					+ " no instance running.");
		}
	}

	/**
	 * Is the scheduled updater currently running?
	 * 
	 * @return true/false if the updater is running
	 */
	public boolean isRunning() {
		return isRunning;
	}

	/**
	 * Set if the updater is currently running.
	 * 
	 * @param isRunning
	 *            true/false if the updater is running
	 */
	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	/**
	 * The runnable task for fetching the latest locations from the server and
	 * passing them to the controller.
	 */
	private Runnable runnableUpdateTask = new Runnable() {
		public void run() {
			Log.v(TAG,
					"Polling for updates @ "
							+ SystemClock.currentThreadTimeMillis());
			ServerCommunicator communicator = new ServerCommunicator(
					mainController);
			communicator.getNeighbors(mainController.getMe());
			int next = DELAY;
			Log.v(TAG, "Queuing another update in " + (next / 1000)
					+ "s.. current time=" + SystemClock.uptimeMillis());
			mainHandler.postDelayed(this, next);
		}
	};

}
