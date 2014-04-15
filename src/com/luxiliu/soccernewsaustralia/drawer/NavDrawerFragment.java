package com.luxiliu.soccernewsaustralia.drawer;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.luxiliu.soccernewsaustralia.R;
import com.luxiliu.soccernewsaustralia.home.HomeFragment;

/**
 * The NavDrawerFragment provides a navigation drawer fragment
 * 
 * @author Luxi Liu (luxi.liu@gmail.com)
 * 
 */
public class NavDrawerFragment extends Fragment implements OnItemClickListener {
	private ArrayList<NavDrawerEntry> mNavDrawerEntryList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Inflate the fragment view
		View view = inflater.inflate(R.layout.nav_drawer_fragment, container);

		// Get drawer list view
		ListView mDrawerListView = (ListView) view
				.findViewById(R.id.drawer_list);

		// Create drawer entry list
		mNavDrawerEntryList = new ArrayList<NavDrawerEntry>();
		mNavDrawerEntryList.add(NavDrawerEntry.ALEAGUE_ENTRY);
		mNavDrawerEntryList.add(NavDrawerEntry.WLEAGUE_ENTRY);
		mNavDrawerEntryList.add(NavDrawerEntry.AFCLEAGUE_ENTRY);
		mNavDrawerEntryList.add(NavDrawerEntry.SOCCEROOS_ENTRY);
		mNavDrawerEntryList.add(NavDrawerEntry.SETTINGS_ENTRY);

		// Add drawer entries to list view
		NavDrawerEntryAdapter adapter = new NavDrawerEntryAdapter(
				inflater.getContext(), mNavDrawerEntryList);
		mDrawerListView.setAdapter(adapter);
		mDrawerListView.setOnItemClickListener(this);

		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// Close the drawer provided a entry is clicked
		((DrawerLayout) getView().getParent()).closeDrawer(getView());

		// Get clicked drawer entry
		NavDrawerEntry navDrawerEntry = mNavDrawerEntryList.get(position);

		// Let that drawer entry deal the click event
		navDrawerEntry.onClick(view);
	}

	public void onHomeFragmentActivated(HomeFragment homeFragment) {
		for (int i = 0; i < mNavDrawerEntryList.size(); i++) {
			NavDrawerEntry entry = (NavDrawerEntry) mNavDrawerEntryList.get(i);
			if (entry instanceof HomeFragmentEntry) {
				if (i == homeFragment.getInternalId()) {
					((HomeFragmentEntry) entry).setActivated(getActivity(),
							true);
				} else {
					((HomeFragmentEntry) entry).setActivated(getActivity(),
							false);
				}
			}
		}
	}
}
