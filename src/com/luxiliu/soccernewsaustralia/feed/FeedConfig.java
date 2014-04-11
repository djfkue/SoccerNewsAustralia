package com.luxiliu.soccernewsaustralia.feed;

/**
 * The FeedConfig provides URLs of feed content provider
 * 
 * @author Luxi Liu (luxi.liu@gmail.com)
 * 
 */
public final class FeedConfig {
	// Make FeedConfig class as a static class
	private FeedConfig() {
	}

	public static final String SOCCEROOS_FEED_URL = "http://www.goalfeeds.performgroup.com/api/editorial/news.xml/edition/en-AU/team/156/pageSize/%s/page/%s";
	public static final String ALEAGUE_FEED_URL = "http://www.goalfeeds.performgroup.com/api/editorial/news.xml/edition/en-AU/channel/aleague/pageSize/%s/page/%s";
	public static final String AFC_FEED_URL = "http://www.goalfeeds.performgroup.com/api/editorial/news.xml/edition/en-AU/channel/AFC Champions League/pageSize/%s/page/%s";
}
