package com.luxiliu.soccernewsaustralia.home;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.Card.OnCardClickListener;
import it.gmariotti.cardslib.library.view.CardListView;
import uk.co.senab.actionbarpulltorefresh.extras.actionbarcompat.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.content.Intent;
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
import com.luxiliu.soccernewsaustralia.feed.FeedManager;
import com.luxiliu.soccernewsaustralia.home.aleague.ALeagueFragment;
import com.luxiliu.soccernewsaustralia.home.socceroos.SocceroosFragment;
import com.luxiliu.soccernewsaustralia.model.Content;
import com.luxiliu.soccernewsaustralia.model.News;
import com.luxiliu.soccernewsaustralia.model.Page;
import com.luxiliu.soccernewsaustralia.net.ConnectionManager;
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

	private State mState = State.NOT_INITIALIZED;

	// The handler to deal with feed content request
	private Handler mHandler = new Handler(Looper.getMainLooper()) {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case FeedManager.REQUEST_DOWNLOAD_FEED_COMPLETE:
				Content content = (Content) msg.getData().getSerializable(
						FeedManager.KEY_FEED_CONTENTS);
				onRequestDownloadComplete(content);
				break;

			case FeedManager.REQUEST_DOWNLOAD_FEED_FAIL:
				onRequestDownloadFail();
				break;
			}
		}
	};

	private boolean mClickable = true;
	private LoadingView mLoadingView;
	private PullToRefreshLayout mPullToRefreshLayout;
	private CardListView mCardListView;
	private View mHeaderView;
	private View mFooterView;
	private View mLoadMoreView;

	private FeedManager mFeedManager = FeedManager.instance();
	private ArrayList<Page> mPageList = new ArrayList<Page>();
	private CardArrayAdapter mCardArrayAdapter;

	public static final HomeFragment SOCCEROOS_FRAGMENT;
	public static final HomeFragment ALEAGUE_FRAGMENT;

	static {
		SOCCEROOS_FRAGMENT = new SocceroosFragment();

		ALEAGUE_FRAGMENT = new ALeagueFragment();
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
		mPullToRefreshLayout = (PullToRefreshLayout) view
				.findViewById(R.id.pull_to_fresh_layout);
		ActionBarPullToRefresh.from(getActivity()).allChildrenArePullable()
				.listener(this).setup(mPullToRefreshLayout);

		// Create progress bar
		mLoadingView = (LoadingView) view.findViewById(R.id.loading_view);

		// Create CardListView
		mCardListView = (CardListView) view.findViewById(R.id.news_card_list);
		mCardListView.setOnScrollListener(new OnScrollListener() {

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
						// It's time to load more
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
		mCardListView.addHeaderView(mHeaderView);
		mCardListView.addFooterView(mFooterView);

		// Create load more view
		mLoadMoreView = inflater.inflate(R.layout.load_more_view, null, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (mState == State.NOT_INITIALIZED) {
			if (ConnectionManager.instance().isConnected(getActivity())) {
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

		mClickable = true;
	}

	@Override
	public void onRefreshStarted(View view) {
		if (mState == State.INITIALIZE_DOWNLOAD_FAIL
				|| mState == State.INITIALIZE_DOWNLOAD_FAIL_NO_NETWORK) {
			// Pull to refresh to re-initialize
			if (ConnectionManager.instance().isConnected(getActivity())) {
				// Network is OK to re-initialize
				onInitializeDownloading();
				mFeedManager.requestDownloadFeed(mHandler, getFeedUrl());
			} else {
				onInitializeFailNoNetwork();
			}
		} else {
			// Pull to refresh to download
			if (ConnectionManager.instance().isConnected(getActivity())) {
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
		mLoadingView.setDownloadSucceed(true);
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
		mLoadingView.setDownloadSucceed(false);
	}

	private void onInitializeFailNoNetwork() {
		mState = State.INITIALIZE_DOWNLOAD_FAIL_NO_NETWORK;
		Log.d(LOG_TAG, mState.toString());

		mPullToRefreshLayout.setEnabled(true);
		mPullToRefreshLayout.setRefreshing(false);
		mLoadingView.setNoNetwork();
	}

	private void onPullToRefreshDownloading() {
		mState = State.PULL_TO_REFRESH_DOWNLOADING;
		Log.d(LOG_TAG, mState.toString());

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
		mLoadingView.setDownloadSucceed(true);
		showPageList(mPageList);
	}

	private void onPullToRefreshDownloadFail() {
		mState = State.PULL_TO_REFRESH_DOWNLOAD_FAIL;
		Log.d(LOG_TAG, mState.toString());

		// Keep previous page list
		// Just update view
		mPullToRefreshLayout.setEnabled(true);
		mPullToRefreshLayout.setRefreshing(false);
		mLoadingView.setDownloadSucceed(true);
	}

	private void onLoadMoreDownloading() {
		mState = State.LOAD_MORE_DOWNLOADING;
		Log.d(LOG_TAG, mState.toString());

		setLoadingMoreFooterEnabled(true);

		mPullToRefreshLayout.setEnabled(false);
		mPullToRefreshLayout.setRefreshing(false);
		mLoadingView.setVisibility(View.INVISIBLE);
	}

	private void onLoadMoreDownloadComplete(ArrayList<Page> pageList, Page page) {
		mState = State.LOAD_MORE_DOWNLOAD_COMPLETE;
		Log.d(LOG_TAG, mState.toString());

		setLoadingMoreFooterEnabled(false);

		mPullToRefreshLayout.setEnabled(true);
		mPullToRefreshLayout.setRefreshing(false);
		mLoadingView.setVisibility(View.INVISIBLE);

		addAndShowPageList(pageList, page);
	}

	private void onLoadMoreDownloadFail() {
		mState = State.LOAD_MORE_DOWNLOAD_FAIL;
		Log.d(LOG_TAG, mState.toString());

		setLoadingMoreFooterEnabled(false);

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
							.getPagination() + 1));
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
			mLoadingView.setDownloadSucceed(false);
			break;

		case INITIALIZE_DOWNLOAD_FAIL_NO_NETWORK:
			mPullToRefreshLayout.setEnabled(true);
			mPullToRefreshLayout.setRefreshing(false);
			mLoadingView.setNoNetwork();
			break;

		case INITIALIZE_DOWNLOAD_COMPLETE:
			mPullToRefreshLayout.setEnabled(true);
			mPullToRefreshLayout.setRefreshing(false);
			mLoadingView.setDownloadSucceed(true);

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
		if (getActivity() != null) {
			// Create a card list
			ArrayList<Card> cardList = new ArrayList<Card>();

			for (Page page : pageList) {
				// For each page
				ArrayList<News> newsList = page.getNewsList();

				// For each news
				for (final News news : newsList) {
					// Create a news card
					NewsCard newsCard = new NewsCard(getActivity(), news);

					// Setup news card
					newsCard.setOnClickListener(new OnCardClickListener() {
						@Override
						public void onClick(Card card, View view) {
							if (mClickable) {
								// To avoid multiple clicks and multiple
								// activities
								mClickable = false;

								// Start ArticleActivity to display news
								// article
								// details
								news.setCategory(String.valueOf(getActivity()
										.getTitle()));
								Intent intent = new Intent();
								intent.putExtra(Intent.EXTRA_STREAM, news);
								intent.setClass(getActivity(),
										ArticleActivity.class);
								startActivity(intent);
							}
						}
					});

					// Add news card to list
					cardList.add(newsCard);
				}
			}

			// Create card array adapter and add to list view
			mCardArrayAdapter = new CardArrayAdapter(getActivity(), cardList);
			mCardListView.setAdapter(mCardArrayAdapter);
		}
	}

	private void restorePageList(ArrayList<Page> pageList) {
		// Parent activity cannot be null
		if (getActivity() != null) {
			mCardListView.setAdapter(mCardArrayAdapter);
		}
	}

	private void addAndShowPageList(ArrayList<Page> pageList, Page page) {
		// Page list cannot be null
		if (pageList != null) {
			pageList.add(page);

			// Parent activity cannot be null
			if (getActivity() != null) {
				// Create a card list
				ArrayList<Card> cardList = new ArrayList<Card>();

				// Get news list from new page
				ArrayList<News> newsList = page.getNewsList();
				// For each news
				for (final News news : newsList) {
					// Create a news card
					NewsCard newsCard = new NewsCard(getActivity(), news);

					// Setup news card
					newsCard.setOnClickListener(new OnCardClickListener() {
						@Override
						public void onClick(Card card, View view) {
							if (mClickable) {
								// To avoid multiple clicks and multiple
								// activities
								mClickable = false;

								// Start ArticleActivity to display news
								// article
								// details
								news.setCategory(String.valueOf(getActivity()
										.getTitle()));
								Intent intent = new Intent();
								intent.putExtra(Intent.EXTRA_STREAM, news);
								intent.setClass(getActivity(),
										ArticleActivity.class);
								startActivity(intent);
							}
						}
					});

					// Add news card to list
					cardList.add(newsCard);
				}

				// Add card list to adapter and notify data changed
				mCardArrayAdapter.addAll(cardList);
				mCardArrayAdapter.notifyDataSetChanged();
			}
		}
	}

	private void setLoadingMoreFooterEnabled(boolean enabled) {
		if (enabled) {
			// Add load more view to footer
			if (mCardListView.getFooterViewsCount() == 1) {
				mCardListView.addFooterView(mLoadMoreView);
			}
		} else {
			// Remove load more view from footer
			if (mCardListView.getFooterViewsCount() == 2) {
				mCardListView.removeFooterView(mLoadMoreView);
			}
		}
	}

	public abstract String getFeedUrl();

	public abstract String getFeedUrlWithPageIndex(int page);

	public abstract boolean hasMultiplePages();
}