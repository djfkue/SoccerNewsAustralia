package com.luxiliu.soccernewsaustralia.drawer;

import com.luxiliu.soccernewsaustralia.R;
import android.app.Activity;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;

/**
 * The NavDrawerActionBarDrawerToggle provides a customized navigation drawer
 * toggle
 * 
 * @author Luxi Liu (luxi.liu@gmail.com)
 * 
 * 
 */
public class NavDrawerActionBarDrawerToggle extends ActionBarDrawerToggle {

	public NavDrawerActionBarDrawerToggle(Activity activity,
			DrawerLayout drawerLayout) {
		super(activity, drawerLayout, R.drawable.ic_drawer_white,
				R.string.abc_action_bar_up_description,
				R.string.abc_action_bar_up_description);

		// Enable drawer indicator, so the toggle drawer could be shown
		setDrawerIndicatorEnabled(true);
	}
}
