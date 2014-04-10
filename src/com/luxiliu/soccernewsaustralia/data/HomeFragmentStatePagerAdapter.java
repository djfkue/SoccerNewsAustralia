package com.luxiliu.soccernewsaustralia.data;

import com.luxiliu.soccernewsaustralia.home.HomeFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

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
