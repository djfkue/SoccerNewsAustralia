package com.luxiliu.soccernewsaustralia.home;

import java.io.Serializable;

import android.content.Context;

import com.luxiliu.soccernewsaustralia.home.aleague.ALeaguePage;
import com.luxiliu.soccernewsaustralia.home.socceroos.SocceroosPage;

/**
 * The HomePage provides an abstraction of a feed content topic. It's associated
 * with the topic's own fragment and drawer entry
 * 
 * @author Luxi Liu (luxi.liu@gmail.com)
 * 
 */
public abstract class HomePage implements Serializable {
	private static final long serialVersionUID = 7055897477453181778L;
	public static final int SOCCEROOS_PAGE_ID = 0;
	public static final int ALEAGUE_PAGE_ID = 1;

	public static final HomePage SOCCEROOS_PAGE;
	public static final HomePage ALEAGUE_PAGE;

	protected int mId;

	static {
		SOCCEROOS_PAGE = new SocceroosPage();

		ALEAGUE_PAGE = new ALeaguePage();
	}

	protected HomePage(int id) {
		mId = id;
	}

	public int getId() {
		return mId;
	}

	public abstract HomeFragment getFragment();

	public abstract String getTitle(Context context);
}
