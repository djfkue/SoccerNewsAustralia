package com.luxiliu.soccernewsaustralia.home.yleague;

import android.content.Context;

import com.luxiliu.soccernewsaustralia.R;
import com.luxiliu.soccernewsaustralia.feed.FeedConfig;
import com.luxiliu.soccernewsaustralia.home.HomeFragment;

public class YLeagueFragment extends HomeFragment {

	public YLeagueFragment() {
		super(HomeFragment.YLEAGUE_FRAGMENT_ID);
	}

	@Override
	public String getFeedUrl() {
		return FeedConfig.YLEAGUE_FEED_URL;
	}

	@Override
	public String getFeedUrlWithPageIndex(int page) {
		return FeedConfig.YLEAGUE_FEED_URL;
	}

	@Override
	public boolean hasMultiplePages() {
		return false;
	}

	@Override
	public String getTitle(Context context) {
		return context.getString(R.string.yleague_title);
	}

}
