package com.luxiliu.soccernewsaustralia.home.socceroos;

import android.content.Context;

import com.luxiliu.soccernewsaustralia.R;
import com.luxiliu.soccernewsaustralia.home.HomeFragment;
import com.luxiliu.soccernewsaustralia.home.HomePage;

/**
 * The SocceroosPage provides a home page for socceroos feed content
 * 
 * @author Luxi Liu (luxi.liu@gmail.com)
 * 
 */
public class SocceroosPage extends HomePage {
	private static final long serialVersionUID = 8677165630670483728L;

	public SocceroosPage() {
		super(HomePage.SOCCEROOS_PAGE_ID);
	}

	@Override
	public HomeFragment getFragment() {
		return HomeFragment.SOCCEROOS_FRAGMENT;
	}

	@Override
	public String getTitle(Context context) {
		return context.getResources().getString(R.string.socceroos_title);
	}
}
