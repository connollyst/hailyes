package com.quane.hail.yes.android.app.ui.overlay;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;
import com.quane.hail.yes.SimpleLocation;
import com.quane.hail.yes.user.User;

/**
 * 
 * @author Sean Connolly
 */
public class AppOverlay extends ItemizedOverlay<OverlayItem> {

	private static final String TAG = AppOverlay.class.getSimpleName();

	private ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();

	private Drawable mePin;
	private Drawable driverPin;
	private Drawable passengerPin;

	public AppOverlay(Drawable mePin, Drawable driverPin, Drawable passengerPin) {
		super(boundCenterBottom(mePin));
		this.mePin = mePin;
		this.driverPin = driverPin;
		this.passengerPin = passengerPin;

		setMarkerBounds(this.mePin);
		setMarkerBounds(this.driverPin);
		setMarkerBounds(this.passengerPin);
	}

	@Override
	protected OverlayItem createItem(int i) {
		return items.get(i);
	}

	@Override
	public int size() {
		return items.size();
	}

	private void addOverlayItem(OverlayItem overlay) {
		items.add(overlay);
	}

	public void addOverlayItem(User user) {
		SimpleLocation location = user.getLocation();
		GeoPoint point = new GeoPoint(location.getLatitudeE6(),
				location.getLongitudeE6());
		Log.v(TAG, "Drawing location @ " + point.getLatitudeE6() + " & "
				+ point.getLongitudeE6() + " .. (" + location.getLatitude()
				+ " & " + location.getLongitude() + ")");
		OverlayItem item = new OverlayItem(point, user.getType().toString(),
				"Snippet");
		if (user.getIsMe()) {
			item.setMarker(mePin);
		} else if (user.isDriver()) {
			item.setMarker(driverPin);
		} else if (user.isPassenger()) {
			item.setMarker(passengerPin);
		} else {
			Log.e(TAG, "Cannot render an overlay item for "
					+ user.getType().toString());
		}
		addOverlayItem(item);
	}

	public void clear() {
		items.clear();
	}

	public void populateOverlay() {
		populate();
	}

	private void setMarkerBounds(Drawable drawable) {
		int w = drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight();
		drawable.setBounds(-w / 2, -h, w / 2, 0);
	}
}
