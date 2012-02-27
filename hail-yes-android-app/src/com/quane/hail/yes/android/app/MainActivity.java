package com.quane.hail.yes.android.app;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.quane.hail.yes.android.app.ui.MainController;

/**
 * The main Activity in 'Hail Yes!'.<br/>
 * In the Model-Controller-Presenter-View architecture Activities are
 * Presenters, communicating between the view, defined in XML definition and
 * conceived as the UI at runtime, and the controller, where all the business
 * logic resides.
 * 
 * @author Sean Connolly
 */
public class MainActivity extends MapActivity {

	private static final String TAG = MainActivity.class.getSimpleName();

	private final Handler handler = new Handler();
	private MainController mainController;
	private MapView mapView;

	/**
	 * Home is: -122.424302 x 37.758654
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mapView = (MapView) findViewById(R.id.map);
		mapView.setBuiltInZoomControls(true);
		mapView.getController().setZoom(15);
		// The main controller is responsible for coordinating all business
		// logic back to us, start it up now
		mainController = new MainController(this);
	}

	/**
	 * Inflates the /res/menu/main.xml file when the app is ready.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * Always returns false, we don't display routes.
	 */
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; not sure what we should do?
			return true;
		case R.id.hail_button:
			// button clicked to hail, let's go!
			mainController.onHailButtonClick();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		mainController.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mainController.onResume();
	}

	/**
	 * 
	 * @return
	 */
	public List<Overlay> getMapViewOverlays() {
		return mapView.getOverlays();
	}

	/**
	 * 
	 */
	public void invalidateMapView() {
		mapView.invalidate();
	}

	/**
	 * 
	 * @param location
	 */
	public void centerMap(GeoPoint location) {
		Log.i(TAG, "Centering map on " + (location.getLatitudeE6() / 1E6)
				+ " & " + (location.getLongitudeE6() / 1E6));
		mapView.getController().animateTo(location);
	}

	public Drawable getDrawableMePin() {
		return this.getResources().getDrawable(R.drawable.me_pin_32_2);
	}

	public Drawable getDrawableDriverPin() {
		return this.getResources().getDrawable(R.drawable.cab_pin_32);
	}

	public Drawable getDrawablePassengerPin() {
		return this.getResources().getDrawable(R.drawable.passenger_pin_32);
	}

	/**
	 * 
	 * @return
	 */
	public Handler getHandler() {
		return handler;
	}

}