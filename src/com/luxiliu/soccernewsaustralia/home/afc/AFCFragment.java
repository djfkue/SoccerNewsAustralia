package com.luxiliu.soccernewsaustralia.home.afc;

import com.luxiliu.soccernewsaustralia.feed.FeedConfig;
import com.luxiliu.soccernewsaustralia.home.HomeFragment;

/**
 * The AFCFragment provides A league feed content. It provides pull down to
 * refresh features.
 * 
 * @author Luxi Liu (luxi.liu@gmail.com)
 * 
 */
public class AFCFragment extends HomeFragment {
	private static final int PAGE_SIZE = 20;
	private static final int PAGE = 1;

	@Override
	public String getFeedUrl() {

		return String.format(FeedConfig.AFC_FEED_URL, PAGE_SIZE, PAGE);
	}

	@Override
	public String getFeedUrlWithPageIndex(int page) {

		return String.format(FeedConfig.AFC_FEED_URL, PAGE_SIZE, page);
	}

	@Override
	public boolean hasMultiplePages() {
		return true;
	}
}
