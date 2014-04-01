package com.luxiliu.soccernewsaustralia.home.aleague;

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

public class ALeaguePage extends HomePage {

	private static final long serialVersionUID = -8220168275402777621L;

	public ALeaguePage() {
		super(HomePage.ALEAGUE_PAGE_ID);
	}

	@Override
	public HomeFragment getFragment() {
		return HomeFragment.ALEAGUE_FRAGMENT;
	}

	@Override
	public String getTitle(Context context) {
		return context.getResources().getString(R.string.aleague_title);
	}
}
