package com.luxiliu.soccernewsaustralia.home.aleague;

import android.content.Context;

import com.luxiliu.soccernewsaustralia.R;
import com.luxiliu.soccernewsaustralia.feed.FeedConfig;
import com.luxiliu.soccernewsaustralia.home.HomeFragment;

/**
 * The ALeagueFragment provides A league feed content. It provides pull down to
 * refresh, and pull up to load more features.
 * 
 * @author Luxi Liu (luxi.liu@gmail.com)
 * 
 */
public class ALeagueFragment extends HomeFragment {
	private static final int PAGE_SIZE = 20;
	private static final int PAGE = 1;

	public ALeagueFragment() {
		super(HomeFragment.ALEAGUE_FRAGMENT_ID);
	}

	@Override
	public String getFeedUrl() {
		return String.format(FeedConfig.ALEAGUE_FEED_URL, PAGE_SIZE, PAGE);
	}

	@Override
	public String getFeedUrlWithPageIndex(int page) {
		return String.format(FeedConfig.ALEAGUE_FEED_URL, PAGE_SIZE, page);
	}

	@Override
	public boolean hasMultiplePages() {
		return true;
	}

	@Override
	public String getTitle(Context context) {
		return context.getString(R.string.aleague_title);
	}
}
