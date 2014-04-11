package com.luxiliu.soccernewsaustralia.data;

import com.luxiliu.soccernewsaustralia.home.HomeFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * The HomeFragmentStatePagerAdapter class provide an adapter of view pager
 * 
 * @author Luxi Liu (luxi.liu@gmail.com)
 * 
 */
public class HomeFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

	public HomeFragmentStatePagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		return HomeFragment.getHomeFragment(position);
	}

	@Override
	public int getCount() {
		return HomeFragment.SIZE;
	}
}
