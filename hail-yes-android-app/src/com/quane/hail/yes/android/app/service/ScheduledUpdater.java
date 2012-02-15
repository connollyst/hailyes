package com.quane.hail.yes.android.app.service;

import android.os.Handler;
import android.os.SystemClock;

import com.quane.hail.yes.android.app.ui.MainController;

public class ScheduledUpdater {

	private static final int DELAY = 5000;

	private MainController mainController;
	private Handler mainHandler;

	boolean isRunning = false;
	long mStartTime = 0L;

	public ScheduledUpdater(MainController mainController) {
		this.mainController = mainController;
		this.mainHandler = mainController.getActivityHandler();
	}

	public void start() {
		if (!isRunning) {
			isRunning = true;
			System.out.println("Starting polling for updates.");
			mStartTime = System.currentTimeMillis();
			mainHandler.removeCallbacks(runnableUpdateTask);
			mainHandler.postDelayed(runnableUpdateTask, DELAY);
		} else {
			System.err.println("Cannot start polling for updates,"
					+ " already doing so.");
		}
	}

	public void stop() {
		if (isRunning) {
			mainHandler.removeCallbacks(runnableUpdateTask);
			isRunning = false;
			System.out.println("Stopping polling for updates.");
		} else {
			System.err.println("Cannot stop polling for updates,"
					+ " no instance running.");
		}
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	/**
	 * The runnable task for fetching the latest locations from the server and
	 * passing them to the controller.
	 */
	private Runnable runnableUpdateTask = new Runnable() {
		public void run() {
			System.out.println("Polling for updates @ "
					+ SystemClock.currentThreadTimeMillis());
			ServerCommunicator communicator = new ServerCommunicator(
					mainController);
			communicator.getNeighbors(mainController.getMyLocation());
			int next = DELAY;
			System.out.println("Queuing another update in " + (next / 1000)
					+ "s.. current time=" + SystemClock.uptimeMillis());
			mainHandler.postDelayed(this, next);
		}
	};

}
