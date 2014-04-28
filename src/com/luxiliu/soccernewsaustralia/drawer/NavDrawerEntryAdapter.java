package com.luxiliu.soccernewsaustralia.drawer;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * The NavDrawerEntryAdapter provides a adapter of navigation drawer list view
 * 
 * @author Luxi Liu (luxi.liu@gmail.com)
 * 
 */
public class NavDrawerEntryAdapter extends ArrayAdapter<NavDrawerEntry> {

	public NavDrawerEntryAdapter(Context context,
			ArrayList<NavDrawerEntry> objects) {
		super(context, 0, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// return drawer entry's own view
		return getItem(position).getView(convertView, parent);
	}
}
