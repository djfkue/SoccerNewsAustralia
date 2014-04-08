package com.luxiliu.soccernewsaustralia.drawer;

import android.view.View;
import android.view.ViewGroup;

import com.luxiliu.soccernewsaustralia.home.HomePage;

/**
 * The NavDrawerEntry provides an abstract drawer entry in navigation drawer
 * list
 * 
 * @author Luxi Liu (luxi.liu@gmail.com)
 * 
 */
public abstract class NavDrawerEntry {
	public static final NavDrawerEntry SOCCEROOS_ENTRY;
	public static final NavDrawerEntry ALEAGUE_ENTRY;
	public static final NavDrawerEntry SETTINGS_ENTRY;
	public static final NavDrawerEntry AFCLEAGUE_ENTRY;

	static {
		SOCCEROOS_ENTRY = new HomePageEntry(HomePage.SOCCEROOS_PAGE);
		ALEAGUE_ENTRY = new HomePageEntry(HomePage.ALEAGUE_PAGE);
		AFCLEAGUE_ENTRY = new HomePageEntry(HomePage.AFC_PAGE);

		SETTINGS_ENTRY = new SettingsEntry();
	}

	public abstract View getView(View view, ViewGroup viewGroup);

	public abstract void onClick(View view);
}
