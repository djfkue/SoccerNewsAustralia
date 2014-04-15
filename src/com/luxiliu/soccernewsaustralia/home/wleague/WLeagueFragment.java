package com.luxiliu.soccernewsaustralia.home.wleague;

import android.content.Context;

import com.luxiliu.soccernewsaustralia.R;
import com.luxiliu.soccernewsaustralia.feed.FeedConfig;
import com.luxiliu.soccernewsaustralia.home.HomeFragment;

public class WLeagueFragment extends HomeFragment {

	public WLeagueFragment() {
		super(HomeFragment.WLEAGUE_FRAGMENT_ID);
	}

	@Override
	public String getFeedUrl() {
		return FeedConfig.WLEAGUE_FEED_URL;
	}

	@Override
	public String getFeedUrlWithPageIndex(int page) {
		return FeedConfig.WLEAGUE_FEED_URL;
	}

	@Override
	public boolean hasMultiplePages() {
		return false;
	}

	@Override
	public String getTitle(Context context) {
		return context.getString(R.string.wleague_title);
	}

}
