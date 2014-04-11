package com.luxiliu.soccernewsaustralia.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;

import com.luxiliu.soccernewsaustralia.R;
import com.luxiliu.soccernewsaustralia.activity.SNAActivity;
import com.luxiliu.soccernewsaustralia.data.HomeFragmentStatePagerAdapter;
import com.luxiliu.soccernewsaustralia.drawer.NavDrawerActionBarDrawerToggle;
import com.luxiliu.soccernewsaustralia.drawer.NavDrawerFragment;

/**
 * The HomeActivity is the activity to display feed content list
 * 
 * @author Luxi Liu (luxi.liu@gmail.com)
 * 
 */
public class HomeActivity extends SNAActivity {
	private DrawerLayout mDrawerLayout;
	private NavDrawerActionBarDrawerToggle mDrawerToggle;
	private NavDrawerFragment mNavDrawerFragment;

	private ViewPager mPager;
	private PagerAdapter mPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.home_activity);

		setupView();

		setupContent();
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

				// Restore title to fragment title when drawer toggle closed
				setTitle(HomeFragment.getHomeFragment(mPager.getCurrentItem())
						.getTitle(HomeActivity.this));
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);

				// Set title as application name when drawer toggle opened
				setTitle(R.string.app_name);
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		// Setup drawer fragment
		mNavDrawerFragment = (NavDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.nav_drawer_fragment);

		// Setup ViewPager and PagerAdapter.
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setPageTransformer(true, new DepthPageTransformer());

		mPagerAdapter = new HomeFragmentStatePagerAdapter(
				getSupportFragmentManager());
		mPager.setAdapter(mPagerAdapter);
		mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				invalidateOptionsMenu();

				// Get home fragment
				HomeFragment homeFragment = HomeFragment
						.getHomeFragment(position);

				updateViewOnFragmentChange(homeFragment);
			}
		});
	}

	private void setupContent() {
		// Get current home fragment
		HomeFragment homeFragment = HomeFragment.getHomeFragment(mPager
				.getCurrentItem());

		updateViewOnFragmentChange(homeFragment);
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

		// Get the home fragment for the clicked drawer entry
		int homeFragmentId = intent.getIntExtra(Intent.EXTRA_UID, -1);
		HomeFragment homeFragment = HomeFragment
				.getHomeFragment(homeFragmentId);

		updateViewOnFragmentChange(homeFragment);
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
		outState.putInt(Intent.EXTRA_UID,
				HomeFragment.getHomeFragment(mPager.getCurrentItem())
						.getInternalId());
	}

	private void updateViewOnFragmentChange(HomeFragment homeFragment) {
		// Set title
		setTitle(homeFragment.getTitle(this));

		// Update view pager
		mPager.setCurrentItem(homeFragment.getInternalId(), true);

		// Update drawer fragment
		mNavDrawerFragment.onHomeFragmentActivated(homeFragment);
	}

	/**
	 * The DepthPageTransformer class provide a transform animation for view
	 * pager
	 * 
	 * @author Luxi Liu (luxi.liu@gmail.com)
	 * 
	 */
	private class DepthPageTransformer implements ViewPager.PageTransformer {
		private static final float MIN_SCALE = 1.0f;

		public void transformPage(View view, float position) {
			int pageWidth = view.getWidth();

			if (position < -1) { // [-Infinity,-1)
				// This page is way off-screen to the left.
				view.setAlpha(0);

			} else if (position <= 0) { // [-1,0]
				// Use the default slide transition when moving to the left page
				view.setAlpha(1);
				view.setTranslationX(0);
				view.setScaleX(1);
				view.setScaleY(1);

			} else if (position <= 1) { // (0,1]
				// Fade the page out.
				view.setAlpha(1 - position);

				// Counteract the default slide transition
				view.setTranslationX(pageWidth * -position);

				// Scale the page down (between MIN_SCALE and 1)
				float scaleFactor = MIN_SCALE + (1 - MIN_SCALE)
						* (1 - Math.abs(position));
				view.setScaleX(scaleFactor);
				view.setScaleY(scaleFactor);

			} else { // (1,+Infinity]
				// This page is way off-screen to the right.
				view.setAlpha(0);
			}
		}
	}
}
