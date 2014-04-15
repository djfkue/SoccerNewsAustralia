package com.luxiliu.soccernewsaustralia.home;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.Card.OnCardClickListener;
import uk.co.senab.actionbarpulltorefresh.extras.actionbarcompat.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

import com.luxiliu.soccernewsaustralia.R;
import com.luxiliu.soccernewsaustralia.article.ArticleActivity;
import com.luxiliu.soccernewsaustralia.card.NewsCard;
import com.luxiliu.soccernewsaustralia.data.CardHeaderGridArrayAdapter;
import com.luxiliu.soccernewsaustralia.feed.FeedManager;
import com.luxiliu.soccernewsaustralia.home.afc.AFCFragment;
import com.luxiliu.soccernewsaustralia.home.aleague.ALeagueFragment;
import com.luxiliu.soccernewsaustralia.home.socceroos.SocceroosFragment;
import com.luxiliu.soccernewsaustralia.home.wleague.WLeagueFragment;
import com.luxiliu.soccernewsaustralia.home.yleague.YLeagueFragment;
import com.luxiliu.soccernewsaustralia.model.Content;
import com.luxiliu.soccernewsaustralia.model.News;
import com.luxiliu.soccernewsaustralia.model.Page;
import com.luxiliu.soccernewsaustralia.net.ConnectionManager;
import com.luxiliu.soccernewsaustralia.widget.CardHeaderGridView;
import com.luxiliu.soccernewsaustralia.widget.LoadingView;

/**
 * The HomeFragment provides an abstract fragment to display the topic's feed
 * content
 * 
 * @author Luxi Liu (luxi.liu@gmail.com)
 * 
 */
public abstract class HomeFragment extends Fragment implements
		OnRefreshListener {
	private static final String LOG_TAG = "HomeFragment";

	private enum State {
		NOT_INITIALIZED, //
		INITIALIZE_DOWNLOADING, //
		INITIALIZE_DOWNLOAD_COMPLETE, //
		INITIALIZE_DOWNLOAD_FAIL, //
		INITIALIZE_DOWNLOAD_FAIL_NO_NETWORK, //
		PULL_TO_REFRESH_DOWNLOADING, //
		PULL_TO_REFRESH_DOWNLOAD_COMPLETE, //
		PULL_TO_REFRESH_DOWNLOAD_FAIL, //
		LOAD_MORE_DOWNLOADING, //
		LOAD_MORE_DOWNLOAD_COMPLETE, //
		LOAD_MORE_DOWNLOAD_FAIL
	}

	private boolean mClickable = true;
	private LoadingView mLoadingView;
	private PullToRefreshLayout mPullToRefreshLayout;
	private CardHeaderGridView mCardHeaderGridView;
	private View mHeaderView;
	private View mFooterView;
	private View mLoadMoreView;

	private FeedManager mFeedManager = FeedManager.instance();
	private ArrayList<Page> mPageList = new ArrayList<Page>();
	private CardHeaderGridArrayAdapter mCardHeaderArrayAdapter;
	private State mState = State.NOT_INITIALIZED;
	private Context mContext;

	// The handler to deal with feed content request
	private Handler mHandler = new Handler(Looper.getMainLooper()) {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case FeedManager.REQUEST_DOWNLOAD_FEED_COMPLETE:
				// Feed download succeed
				Content content = (Content) msg.getData().getSerializable(
						FeedManager.KEY_FEED_CONTENTS);
				onRequestDownloadComplete(content);
				break;

			case FeedManager.REQUEST_DOWNLOAD_FEED_FAIL:
				// Feed download fail
				onRequestDownloadFail();
				break;
			}
		}
	};

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Keep application context
		if (mContext == null) {
			mContext = activity.getApplicationContext();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Retain fragment instance
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Create main view
		View view = inflater.inflate(R.layout.home_fragment, container, false);

		// Create pull to refresh action bar
		// p.s. getActivity() should not be null
		mPullToRefreshLayout = (PullToRefreshLayout) view
				.findViewById(R.id.pull_to_fresh_layout);
		ActionBarPullToRefresh.from(getActivity()).allChildrenArePullable()
				.listener(this).setup(mPullToRefreshLayout);

		// Create progress bar
		mLoadingView = (LoadingView) view.findViewById(R.id.loading_view);

		// Create CardListView
		mCardHeaderGridView = (CardHeaderGridView) view
				.findViewById(R.id.news_card_list);
		mCardHeaderGridView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (hasMultiplePages() && mPageList != null
						&& mPageList.size() != 0
						&& mPageList.get(mPageList.size() - 1).hasNextPage()) {
					// The topic has multiple pages, and current page has a next
					// page
					if (totalItemCount != 0
							&& (firstVisibleItem + visibleItemCount) == totalItemCount
							&& mFooterView.getVisibility() == View.VISIBLE) {
						// Scroll to the bottom, and it's time to load more
						loadMore();
					}
				}
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
		});

		// Set card list view header and footer
		mHeaderView = inflater.inflate(R.layout.header_footer, null, false);
		mFooterView = inflater.inflate(R.layout.header_footer, null, false);
		mCardHeaderGridView.addHeaderView(mHeaderView);
		mCardHeaderGridView.addFooterView(mFooterView);

		// Create load more view
		mLoadMoreView = inflater.inflate(R.layout.load_more_view, null, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (mState == State.NOT_INITIALIZED) {
			if (ConnectionManager.instance().isConnected(mContext)) {
				// Start to initialize for the first time
				onInitializeDownloading();
				mFeedManager.requestDownloadFeed(mHandler, getFeedUrl());
			} else {
				onInitializeFailNoNetwork();
			}
		} else {
			// Just restore view for previous state
			restoreView(mState);
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		// Re-enable list item to be clicked
		mClickable = true;
	}

	@Override
	public void onRefreshStarted(View view) {
		if (mState == State.INITIALIZE_DOWNLOAD_FAIL
				|| mState == State.INITIALIZE_DOWNLOAD_FAIL_NO_NETWORK) {
			// Pull to refresh to re-initialize
			if (ConnectionManager.instance().isConnected(mContext)) {
				// Network is OK to re-initialize
				onInitializeDownloading();
				mFeedManager.requestDownloadFeed(mHandler, getFeedUrl());
			} else {
				// No network to initialize
				onInitializeFailNoNetwork();
			}
		} else {
			// Pull to refresh to download
			if (ConnectionManager.instance().isConnected(mContext)) {
				// Network is OK to download
				onPullToRefreshDownloading();
				mFeedManager.requestDownloadFeed(mHandler, getFeedUrl());
			} else {
				// No network to download
				onPullToRefreshDownloadFail();
			}
		}
	}

	private void onRequestDownloadComplete(Content content) {
		if (mState == State.INITIALIZE_DOWNLOADING) {
			// Initialize complete
			onInitializeDownloadComplete(content);
		} else if (mState == State.PULL_TO_REFRESH_DOWNLOADING) {
			// Pull to refresh download complete
			onPullToRefreshDownloadComplete(content);
		} else if (mState == State.LOAD_MORE_DOWNLOADING) {
			// Load more download complete
			onLoadMoreDownloadComplete(mPageList,
					new Page(content.getDocument()));
		}
	}

	private void onRequestDownloadFail() {
		if (mState == State.INITIALIZE_DOWNLOADING) {
			// Initialize fail
			onInitializeDownloadFail();
		} else if (mState == State.PULL_TO_REFRESH_DOWNLOADING) {
			// Pull to refresh download fail
			onPullToRefreshDownloadFail();
		} else if (mState == State.LOAD_MORE_DOWNLOADING) {
			// Load more download fail
			onLoadMoreDownloadFail();
		}
	}

	private void onInitializeDownloading() {
		mState = State.INITIALIZE_DOWNLOADING;
		Log.d(LOG_TAG, mState.toString());

		// Update views
		mPullToRefreshLayout.setEnabled(false);
		mPullToRefreshLayout.setRefreshing(false);
		mLoadingView.setDownloading();
	}

	private void onInitializeDownloadComplete(Content content) {
		mState = State.INITIALIZE_DOWNLOAD_COMPLETE;
		Log.d(LOG_TAG, mState.toString());

		// Clear page list, and add the new page
		mPageList.clear();
		mPageList.add(new Page(content.getDocument()));

		// Update view
		mPullToRefreshLayout.setEnabled(true);
		mPullToRefreshLayout.setRefreshing(false);
		mLoadingView.setDownloadComplete();

		showPageList(mPageList);
	}

	private void onInitializeDownloadFail() {
		mState = State.INITIALIZE_DOWNLOAD_FAIL;
		Log.d(LOG_TAG, mState.toString());

		// Clear page list
		mPageList.clear();

		// Update view
		mPullToRefreshLayout.setEnabled(true);
		mPullToRefreshLayout.setRefreshing(false);
		mLoadingView.setDownloadFailNoContent();
	}

	private void onInitializeFailNoNetwork() {
		mState = State.INITIALIZE_DOWNLOAD_FAIL_NO_NETWORK;
		Log.d(LOG_TAG, mState.toString());

		// Update views
		mPullToRefreshLayout.setEnabled(true);
		mPullToRefreshLayout.setRefreshing(false);
		mLoadingView.setDownloadFailNoNetwork(false);
	}

	private void onPullToRefreshDownloading() {
		mState = State.PULL_TO_REFRESH_DOWNLOADING;
		Log.d(LOG_TAG, mState.toString());

		// Update views
		mPullToRefreshLayout.setEnabled(true);
		mPullToRefreshLayout.setRefreshing(true);
		mLoadingView.setVisibility(View.INVISIBLE);
	}

	private void onPullToRefreshDownloadComplete(Content content) {
		mState = State.PULL_TO_REFRESH_DOWNLOAD_COMPLETE;
		Log.d(LOG_TAG, mState.toString());

		// Pull to refresh does not keep previous page
		// Clear page list and add the new page
		mPageList.clear();
		mPageList.add(new Page(content.getDocument()));

		// Update view
		mPullToRefreshLayout.setEnabled(true);
		mPullToRefreshLayout.setRefreshing(false);
		mLoadingView.setDownloadComplete();

		showPageList(mPageList);
	}

	private void onPullToRefreshDownloadFail() {
		mState = State.PULL_TO_REFRESH_DOWNLOAD_FAIL;
		Log.d(LOG_TAG, mState.toString());

		// Keep previous page list
		// Just update view
		mPullToRefreshLayout.setEnabled(true);
		mPullToRefreshLayout.setRefreshing(false);
		mLoadingView.setDownloadComplete();
	}

	private void onLoadMoreDownloading() {
		mState = State.LOAD_MORE_DOWNLOADING;
		Log.d(LOG_TAG, mState.toString());

		// Enable load more footer view
		setLoadingMoreFooterEnabled(true);

		// Update views
		mPullToRefreshLayout.setEnabled(false);
		mPullToRefreshLayout.setRefreshing(false);
		mLoadingView.setVisibility(View.INVISIBLE);
	}

	private void onLoadMoreDownloadComplete(ArrayList<Page> pageList, Page page) {
		mState = State.LOAD_MORE_DOWNLOAD_COMPLETE;
		Log.d(LOG_TAG, mState.toString());

		// Disable load more footer view
		setLoadingMoreFooterEnabled(false);

		// Update views
		mPullToRefreshLayout.setEnabled(true);
		mPullToRefreshLayout.setRefreshing(false);
		mLoadingView.setVisibility(View.INVISIBLE);

		// Add new page to page list and show
		addAndShowPageList(pageList, page);
	}

	private void onLoadMoreDownloadFail() {
		mState = State.LOAD_MORE_DOWNLOAD_FAIL;
		Log.d(LOG_TAG, mState.toString());

		// Disalbe load more footer view
		setLoadingMoreFooterEnabled(false);

		// Update views
		mPullToRefreshLayout.setEnabled(true);
		mPullToRefreshLayout.setRefreshing(false);
		mLoadingView.setVisibility(View.INVISIBLE);
	}

	private void loadMore() {
		if (mState == State.INITIALIZE_DOWNLOAD_COMPLETE
				|| mState == State.PULL_TO_REFRESH_DOWNLOAD_COMPLETE
				|| mState == State.PULL_TO_REFRESH_DOWNLOAD_FAIL
				|| mState == State.LOAD_MORE_DOWNLOAD_COMPLETE
				|| mState == State.LOAD_MORE_DOWNLOAD_FAIL) {

			onLoadMoreDownloading();

			mFeedManager.requestDownloadFeed(mHandler,
					getFeedUrlWithPageIndex(mPageList.get(mPageList.size() - 1)
							.getCurrentPage() + 1));
		}
	}

	private void restoreView(State state) {
		Log.d(LOG_TAG, state.toString());

		switch (state) {
		case NOT_INITIALIZED:
			break;

		case INITIALIZE_DOWNLOADING:
			mPullToRefreshLayout.setEnabled(false);
			mPullToRefreshLayout.setRefreshing(false);
			mLoadingView.setDownloading();
			break;

		case INITIALIZE_DOWNLOAD_FAIL:
			mPullToRefreshLayout.setEnabled(true);
			mPullToRefreshLayout.setRefreshing(false);
			mLoadingView.setDownloadFailNoContent();
			break;

		case INITIALIZE_DOWNLOAD_FAIL_NO_NETWORK:
			mPullToRefreshLayout.setEnabled(true);
			mPullToRefreshLayout.setRefreshing(false);
			mLoadingView.setDownloadFailNoNetwork(false);
			break;

		case INITIALIZE_DOWNLOAD_COMPLETE:
			mPullToRefreshLayout.setEnabled(true);
			mPullToRefreshLayout.setRefreshing(false);
			mLoadingView.setDownloadComplete();

			restorePageList(mPageList);
			break;

		case PULL_TO_REFRESH_DOWNLOADING:
			mPullToRefreshLayout.setEnabled(true);
			mPullToRefreshLayout.setRefreshing(true);
			mLoadingView.setVisibility(View.INVISIBLE);

			restorePageList(mPageList);
			break;

		case PULL_TO_REFRESH_DOWNLOAD_FAIL:
			mPullToRefreshLayout.setEnabled(true);
			mPullToRefreshLayout.setRefreshing(false);
			mLoadingView.setVisibility(View.INVISIBLE);

			restorePageList(mPageList);
			break;

		case PULL_TO_REFRESH_DOWNLOAD_COMPLETE:
			mPullToRefreshLayout.setEnabled(true);
			mPullToRefreshLayout.setRefreshing(false);
			mLoadingView.setVisibility(View.INVISIBLE);

			restorePageList(mPageList);
			break;

		case LOAD_MORE_DOWNLOADING:
			setLoadingMoreFooterEnabled(true);
			mPullToRefreshLayout.setEnabled(false);
			mPullToRefreshLayout.setRefreshing(false);
			mLoadingView.setVisibility(View.INVISIBLE);

			restorePageList(mPageList);
			break;

		case LOAD_MORE_DOWNLOAD_FAIL:
			setLoadingMoreFooterEnabled(false);
			mPullToRefreshLayout.setEnabled(true);
			mPullToRefreshLayout.setRefreshing(false);
			mLoadingView.setVisibility(View.INVISIBLE);

			restorePageList(mPageList);
			break;

		case LOAD_MORE_DOWNLOAD_COMPLETE:
			setLoadingMoreFooterEnabled(false);
			mPullToRefreshLayout.setEnabled(true);
			mPullToRefreshLayout.setRefreshing(false);
			mLoadingView.setVisibility(View.INVISIBLE);

			restorePageList(mPageList);
			break;

		default:
			break;
		}
	}

	private void showPageList(ArrayList<Page> pageList) {
		// Parent activity cannot be null
		if (mContext != null) {
			// Create a card list
			ArrayList<Card> cardList = new ArrayList<Card>();

			for (Page page : pageList) {
				// For each page
				ArrayList<News> newsList = page.getNewsList();

				// For each news
				for (final News news : newsList) {
					// Create a news card
					NewsCard newsCard = new NewsCard(mContext, news);

					// Setup news card
					newsCard.setOnClickListener(new OnNewsCardClickListener(
							news));

					// Add news card to list
					cardList.add(newsCard);
				}
			}

			// Create card array adapter and add to list view
			mCardHeaderArrayAdapter = new CardHeaderGridArrayAdapter(mContext,
					cardList);
			mCardHeaderGridView.setAdapter(mCardHeaderArrayAdapter);
		}
	}

	private void restorePageList(ArrayList<Page> pageList) {
		// Parent activity cannot be null
		if (mContext != null) {
			mCardHeaderGridView.setAdapter(mCardHeaderArrayAdapter);
		}
	}

	private void addAndShowPageList(ArrayList<Page> pageList, Page page) {
		// Page list cannot be null
		if (pageList != null) {
			pageList.add(page);

			// Parent activity cannot be null
			if (mContext != null) {
				// Create a card list
				ArrayList<Card> cardList = new ArrayList<Card>();

				// Get news list from new page
				ArrayList<News> newsList = page.getNewsList();
				// For each news
				for (final News news : newsList) {
					// Create a news card
					NewsCard newsCard = new NewsCard(mContext, news);

					// Setup news card
					newsCard.setOnClickListener(new OnNewsCardClickListener(
							news));

					// Add news card to list
					cardList.add(newsCard);
				}

				// Add card list to adapter and notify data changed
				mCardHeaderArrayAdapter.addAll(cardList);
				mCardHeaderArrayAdapter.notifyDataSetChanged();
			}
		}
	}

	private void setLoadingMoreFooterEnabled(boolean enabled) {
		if (enabled) {
			// Add load more view to footer
			if (mCardHeaderGridView.getFooterViewCount() == 1) {
				mCardHeaderGridView.addFooterView(mLoadMoreView);
			}
		} else {
			// Remove load more view from footer
			if (mCardHeaderGridView.getFooterViewCount() == 2) {
				mCardHeaderGridView.removeFooterView(mLoadMoreView);
			}
		}
	}

	private class OnNewsCardClickListener implements OnCardClickListener {
		private News mNews;

		public OnNewsCardClickListener(News news) {
			mNews = news;
		}

		public void onClick(Card card, View view) {
			if (mClickable) {
				// To avoid multiple clicks and multiple
				// activities
				mClickable = false;

				mNews.setCategory(String.valueOf(getActivity().getTitle()));

				if (mNews.getFeedLinkUrl() != null
						&& !mNews.getFeedLinkUrl().isEmpty()) {
					// Start ArticleActivity to display news article details
					Intent articleIntent = new Intent();
					articleIntent.putExtra(Intent.EXTRA_STREAM, mNews);
					articleIntent
							.setClass(getActivity(), ArticleActivity.class);
					startActivity(articleIntent);
				} else {
					// Start browser to display news article details
					Intent browserIntent = new Intent();
					browserIntent.setAction(Intent.ACTION_VIEW);
					browserIntent.addCategory(Intent.CATEGORY_BROWSABLE);
					browserIntent.setData(Uri.parse(mNews.getWebsiteLinkUrl()));
					startActivity(browserIntent);
				}
			}
		}
	};

	private int mId;

	public static final int ALEAGUE_FRAGMENT_ID = 0;
	public static final int WLEAGUE_FRAGMENT_ID = 1;
	public static final int YLEAGUE_FRAGMENT_ID = 2;
	public static final int AFC_FRAGMENT_ID = 3;
	public static final int SOCCEROOS_FRAGMENT_ID = 4;
	public static final int SIZE = 5;

	public static final HomeFragment ALEAGUE_FRAGMENT = new ALeagueFragment();
	public static final HomeFragment WLEAGUE_FRAGMENT = new WLeagueFragment();
	public static final HomeFragment YLEAGUE_FRAGMENT = new YLeagueFragment();
	public static final HomeFragment AFC_FRAGMENT = new AFCFragment();
	public static final HomeFragment SOCCEROOS_FRAGMENT = new SocceroosFragment();

	protected HomeFragment(int id) {
		mId = id;
	}

	public static final HomeFragment getHomeFragment(int id) {
		switch (id) {
		case ALEAGUE_FRAGMENT_ID:
			return ALEAGUE_FRAGMENT;

		case WLEAGUE_FRAGMENT_ID:
			return WLEAGUE_FRAGMENT;

		case YLEAGUE_FRAGMENT_ID:
			return YLEAGUE_FRAGMENT;

		case AFC_FRAGMENT_ID:
			return AFC_FRAGMENT;

		case SOCCEROOS_FRAGMENT_ID:
			return SOCCEROOS_FRAGMENT;

		default:
			return ALEAGUE_FRAGMENT;
		}
	}

	public int getInternalId() {
		return mId;
	}

	public abstract String getFeedUrl();

	public abstract String getFeedUrlWithPageIndex(int page);

	public abstract boolean hasMultiplePages();

	public abstract String getTitle(Context context);
}