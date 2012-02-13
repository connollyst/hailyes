package com.quane.hail.yes.android.app.ui.overlay;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class DriverOverlay extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context mContext;

	public DriverOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		mContext = context;
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}

	public void clear() {
		mOverlays.clear();
		populate();
	}

	@Override
	protected boolean onTap(int index) {
		try {
			OverlayItem item = mOverlays.get(index);
			AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
			dialog.setTitle(item.getTitle());
			dialog.setMessage(item.getSnippet());
			dialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public void addOverlay(OverlayItem overlay) {
		mOverlays.add(overlay);
		// maybe we don't want to populate every time?.. or does this just
		// invalidate and Android knows to own draw once we are done?
		populate();
	}
}
