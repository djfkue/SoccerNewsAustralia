package com.luxiliu.soccernewsaustralia.drawer;

import com.luxiliu.soccernewsaustralia.R;
import com.luxiliu.soccernewsaustralia.home.HomeActivity;

import android.app.Activity;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

/**
 * The NavDrawerActionBarDrawerToggle provides a customized navigation drawer
 * toggle
 * 
 * @author Luxi Liu (luxi.liu@gmail.com)
 * 
 * 
 */
public class NavDrawerActionBarDrawerToggle extends ActionBarDrawerToggle {
	private Activity mActivity;

	public NavDrawerActionBarDrawerToggle(Activity activity,
			DrawerLayout drawerLayout) {
		super(activity, drawerLayout, R.drawable.ic_drawer_white,
				R.string.abc_action_bar_up_description,
				R.string.abc_action_bar_up_description);

		mActivity = activity;

		// Enable drawer indicator, so the toggle drawer could be shown
		setDrawerIndicatorEnabled(true);
	}

	@Override
	public void onDrawerClosed(View drawerView) {
		super.onDrawerClosed(drawerView);

		((HomeActivity) mActivity).onDrawerClosed();
	}

	@Override
	public void onDrawerOpened(View drawerView) {
		super.onDrawerOpened(drawerView);

		mActivity.setTitle(R.string.app_name);
	}

}
