package com.quane.hail.yes.android.app.ui.overlay;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;
import com.quane.hail.yes.SimpleLocation;

public class AppOverlay extends ItemizedOverlay<OverlayItem> {

	private static final String TAG = AppOverlay.class.getSimpleName();

	private ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();

	public AppOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}

	@Override
	protected OverlayItem createItem(int i) {
		return items.get(i);
	}

	@Override
	public int size() {
		return items.size();
	}

	public void addOverlayItem(OverlayItem overlay) {
		items.add(overlay);
		populate();
	}

	public void addOverlayItem(SimpleLocation location) {
		GeoPoint point = new GeoPoint(location.getLongitudeE6(),
				location.getLatitudeE6());
		addOverlayItem(point);
	}

	public void addOverlayItem(GeoPoint point) {
		Log.v(TAG, "Drawing location @ " + point.getLatitudeE6() + " & "
				+ point.getLongitudeE6());
		OverlayItem item = new OverlayItem(point, "Title", "Snippet");
		addOverlayItem(item);
	}

	public void clear() {
		items.clear();
	}

	public void populateOverlay() {
		populate();
	}

}
