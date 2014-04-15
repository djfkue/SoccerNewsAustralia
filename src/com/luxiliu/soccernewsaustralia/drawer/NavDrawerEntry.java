package com.luxiliu.soccernewsaustralia.drawer;

import android.view.View;
import android.view.ViewGroup;

import com.luxiliu.soccernewsaustralia.home.HomeFragment;

/**
 * The NavDrawerEntry provides an abstract drawer entry in navigation drawer
 * list
 * 
 * @author Luxi Liu (luxi.liu@gmail.com)
 * 
 */
public abstract class NavDrawerEntry {
	public static final NavDrawerEntry ALEAGUE_ENTRY = new HomeFragmentEntry(
			HomeFragment.ALEAGUE_FRAGMENT);

	public static final NavDrawerEntry WLEAGUE_ENTRY = new HomeFragmentEntry(
			HomeFragment.WLEAGUE_FRAGMENT);

	public static final NavDrawerEntry YLEAGUE_ENTRY = new HomeFragmentEntry(
			HomeFragment.YLEAGUE_FRAGMENT);

	public static final NavDrawerEntry AFCLEAGUE_ENTRY = new HomeFragmentEntry(
			HomeFragment.AFC_FRAGMENT);

	public static final NavDrawerEntry SOCCEROOS_ENTRY = new HomeFragmentEntry(
			HomeFragment.SOCCEROOS_FRAGMENT);

	public static final NavDrawerEntry SETTINGS_ENTRY = new SettingsEntry();

	public abstract View getView(View view, ViewGroup viewGroup);

	public abstract void onClick(View view);
}
