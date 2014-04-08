package com.luxiliu.soccernewsaustralia.home.afc;

import android.content.Context;

import com.luxiliu.soccernewsaustralia.R;
import com.luxiliu.soccernewsaustralia.home.HomeFragment;
import com.luxiliu.soccernewsaustralia.home.HomePage;

/**
 * The ALeague provides a home page for A league feed content
 * 
 * @author Luxi Liu (luxi.liu@gmail.com)
 * 
 */

public class AFCPage extends HomePage {

	private static final long serialVersionUID = -8355708556099541369L;

	public AFCPage() {
		super(HomePage.AFC_PAGE_ID);
	}

	@Override
	public HomeFragment getFragment() {
		return HomeFragment.AFC_FRAGMENT;
	}

	@Override
	public String getTitle(Context context) {
		return context.getResources().getString(R.string.afc_title);
	}
}
