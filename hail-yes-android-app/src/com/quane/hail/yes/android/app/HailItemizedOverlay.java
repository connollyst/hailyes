package com.quane.hail.yes.android.app;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class HailItemizedOverlay extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context mContext;

	public HailItemizedOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}

	public HailItemizedOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		mContext = context;
	}

	@Override
	protected OverlayItem createItem(int i) {
		System.out.println("Someone's axin about item #" + i + ": "
				+ mOverlays.get(i));
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}

	@Override
	protected boolean onTap(int index) {
		System.out.println("HailItemizedOverlay.onTap");
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
		System.out.println("Someone's addin' a new item: " + overlay);
		mOverlays.add(overlay);
		populate();
	}
}
