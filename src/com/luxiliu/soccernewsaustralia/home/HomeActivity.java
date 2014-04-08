package com.luxiliu.soccernewsaustralia.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.luxiliu.soccernewsaustralia.R;
import com.luxiliu.soccernewsaustralia.activity.SNAActivity;
import com.luxiliu.soccernewsaustralia.drawer.NavDrawerActionBarDrawerToggle;
import com.luxiliu.soccernewsaustralia.drawer.NavDrawerFragment;

/**
 * The HomeActivity is the activity to display various feed content topic
 * 
 * @author Luxi Liu (luxi.liu@gmail.com)
 * 
 */
public class HomeActivity extends SNAActivity {
	private static final String LOG_TAG = "HomeActivity";

	private DrawerLayout mDrawerLayout;
	private NavDrawerActionBarDrawerToggle mDrawerToggle;
	private NavDrawerFragment mNavDrawerFragment;

	private HomePage mHomePage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.home_activity);

		setupView();

		if (savedInstanceState == null) {
			// Init content for the first time
			initContent();
		} else {
			// Restore content
			restoreContent(savedInstanceState);
		}
	}

	private void setupView() {
		// Setup "Home" menu
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// Setup drawer layout
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);

		// Setup drawer toggle
		mDrawerToggle = new NavDrawerActionBarDrawerToggle(this, mDrawerLayout) {

			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);

				// Set title as home page name when drawer toggle closed
				setTitle(mHomePage.getTitle(HomeActivity.this));
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);

				// Set title as app name when drawer toggle opened
				setTitle(R.string.app_name);
			}

		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		// Setup drawer fragment
		mNavDrawerFragment = (NavDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.nav_drawer_fragment);
	}

	private void initContent() {
		// Setup initial home page
		mHomePage = HomePage.ALEAGUE_PAGE;

		// Set title
		setTitle(mHomePage.getTitle(this));

		// Update nav drawer fragment
		mNavDrawerFragment.onHomePageActivated(mHomePage);

		// Init fragment
		initFragment(mHomePage);
	}

	private void restoreContent(Bundle savedInstanceState) {
		// Restore the home page
		mHomePage = (HomePage) savedInstanceState
				.getSerializable(Intent.EXTRA_STREAM);

		// Restore the title
		setTitle(mHomePage.getTitle(this));

		// Restore the fragment
		restoreFragment(mHomePage);
	}

	private void initFragment(HomePage homePage) {
		// Get fragment manager and transaction
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();

		// Find an existing home fragment
		// Remove if found
		HomeFragment homeFragment = (HomeFragment) fragmentManager
				.findFragmentById(R.id.home_fragment);
		if (homeFragment != null) {
			fragmentTransaction.remove(homeFragment);
		}

		// Get the fragment from initial home page
		homeFragment = homePage.getFragment();

		// Add the fragment
		fragmentTransaction.add(R.id.home_fragment, homeFragment).commit();
	}

	private void restoreFragment(HomePage homePage) {
		// Get fragment manager and transaction
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();

		// Find previous home fragment
		HomeFragment homeFragment = (HomeFragment) fragmentManager
				.findFragmentById(R.id.home_fragment);
		if (homeFragment == null) {
			// Get one from current home page if not found
			homeFragment = homePage.getFragment();
		}

		if (!homeFragment.isAdded()) {
			// Add the home fragment if not added yet
			fragmentTransaction.add(R.id.home_fragment, homeFragment).commit();
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		if (mDrawerToggle != null) {
			mDrawerToggle.syncState();
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		// A drawer entry is clicked

		// Get the home page for the clicked drawer entry
		int homePageId = intent.getIntExtra(Intent.EXTRA_UID, -1);
		if (homePageId != -1) {
			HomePage homePage = null;

			switch (homePageId) {
			case HomePage.SOCCEROOS_PAGE_ID:
				homePage = HomePage.SOCCEROOS_PAGE;
				break;
			case HomePage.ALEAGUE_PAGE_ID:
				homePage = HomePage.ALEAGUE_PAGE;
				break;
			case HomePage.AFC_PAGE_ID:
				homePage = HomePage.AFC_PAGE;
				break;
			default:
				homePage = HomePage.ALEAGUE_PAGE;
				break;
			}

			if (homePage.getId() != mHomePage.getId()) {
				mHomePage = homePage;

				// Update nav drawer fragment
				mNavDrawerFragment.onHomePageActivated(mHomePage);

				// Change to different home page
				changeHomePage(mHomePage);
			}
		} else {
			Log.d(LOG_TAG, String.format("Cannot find page %d", homePageId));
		}
	}

	private void changeHomePage(HomePage homePage) {
		// Set new title
		setTitle(homePage.getTitle(this));

		// Replace with new home page's fragment
		replaceFragment(homePage.getFragment());
	}

	private void replaceFragment(HomeFragment homeFragment) {
		// Get fragment transaction
		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();

		// Replace with new home fragment
		fragmentTransaction.replace(R.id.home_fragment, homeFragment).commit();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			// Menu item "Home" is used for navigation drawer
			if (mDrawerLayout.isDrawerVisible(mNavDrawerFragment.getView())) {
				mDrawerLayout.closeDrawer(mNavDrawerFragment.getView());
			} else {
				mDrawerLayout.openDrawer(mNavDrawerFragment.getView());
			}

			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// Save mHomePage instance
		outState.putSerializable(Intent.EXTRA_STREAM, mHomePage);
	}
}
