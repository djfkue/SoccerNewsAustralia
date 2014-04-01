package com.luxiliu.soccernewsaustralia.home.socceroos;

import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

import com.luxiliu.soccernewsaustralia.feed.FeedConfig;
import com.luxiliu.soccernewsaustralia.home.HomeFragment;

/**
 * The SocceroosFragment provides Australia socceroos feed content display. It
 * provides pull down to refresh features. Socceroo feed content can only get
 * one page from feed server
 * 
 * @author Luxi Liu (luxi.liu@gmail.com)
 * 
 */
public class SocceroosFragment extends HomeFragment implements
		OnRefreshListener {
	private static final int PAGE_SIZE = 20;
	private static final int PAGE = 1;

	@Override
	public String getFeedUrl() {
		return String.format(FeedConfig.SOCCEROOS_FEED_URL, PAGE_SIZE, PAGE);
	}

	@Override
	public String getFeedUrlWithPageIndex(int page) {
		return String.format(FeedConfig.SOCCEROOS_FEED_URL, PAGE_SIZE, page);
	}

	@Override
	public boolean hasMultiplePages() {
		return false;
	}
}
