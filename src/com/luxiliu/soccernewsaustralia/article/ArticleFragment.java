package com.luxiliu.soccernewsaustralia.article;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.luxiliu.soccernewsaustralia.R;
import com.luxiliu.soccernewsaustralia.feed.FeedManager;
import com.luxiliu.soccernewsaustralia.model.Article;
import com.luxiliu.soccernewsaustralia.model.Content;
import com.luxiliu.soccernewsaustralia.model.News;
import com.luxiliu.soccernewsaustralia.net.ConnectionManager;
import com.luxiliu.soccernewsaustralia.widget.LoadingView;

/**
 * The ArticleFragment provides a fragment to display feed content article
 * 
 * @author Luxi Liu (luxi.liu@gmail.com)
 * 
 */
public class ArticleFragment extends Fragment {
	private static final String LOG_TAG = "ArticleFragment";

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

	private enum State {
		NOT_INITIALIZED, //
		INITIALIZE_DOWNLOADING, //
		INITIALIZE_DOWNLOAD_COMPLETE, //
		INITIALIZE_DOWNLOAD_FAIL, //
		INITIALIZE_DOWNLOAD_FAIL_NO_NETWORK, //
	}

	private State mState = State.NOT_INITIALIZED;
	private News mNews;
	private Article mArticle;
	private FeedManager mFeedManager = FeedManager.instance();

	private LoadingView mLoadingView;
	private ArticleView mArticleView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Enable action bar option menu
		setHasOptionsMenu(true);

		// Retain fragment instance
		setRetainInstance(true);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Get news instance
		mNews = (News) getArguments().getSerializable(Intent.EXTRA_STREAM);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Get main view
		View view = inflater.inflate(R.layout.article_fragment, container,
				false);

		// Get loading view
		mLoadingView = (LoadingView) view.findViewById(R.id.loading_view);

		// Get article view
		mArticleView = (ArticleView) view.findViewById(R.id.article);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (mState == State.NOT_INITIALIZED) {
			if (ConnectionManager.instance().isConnected(getActivity())) {
				// Network is OK
				// Start to initialize
				onInitializeDownloading();
				mFeedManager.requestDownloadFeed(mHandler,
						mNews.getFeedLinkUrl());
			} else {
				// No network connection
				onInitializeFailNoNetwork();
			}
		} else {
			// Just restore view for previous state
			restoreView(mState);
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);

		inflater.inflate(R.menu.article_actions, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// Back to previous activity
			if (getActivity() != null) {
				getActivity().finish();
			}
			break;

		case R.id.menu_item_share:
			// Share this article
			if (getActivity() != null) {
				Intent shareIntent = ShareCompat.IntentBuilder
						.from(getActivity())
						.setType("text/plain")
						.setSubject(mNews.getTitle())
						.setText(
								mNews.getTitle() + " "
										+ mNews.getWebsiteLinkUrl())
						.createChooserIntent();
				if (shareIntent.resolveActivity(getActivity()
						.getPackageManager()) != null) {
					startActivity(shareIntent);
				}
			}
			break;

		case R.id.menu_item_browser:
			// Start a browser to view this article
			if (getActivity() != null) {
				Intent browserIntent = new Intent();
				String url = null;
				if (mArticle != null) {
					url = mArticle.getMobileSiteUrl();
				} else {
					url = mNews.getWebsiteLinkUrl();
				}

				browserIntent.setData(Uri.parse(url));
				browserIntent.setAction(Intent.ACTION_VIEW);
				browserIntent.addCategory(Intent.CATEGORY_BROWSABLE);
				if (browserIntent.resolveActivity(getActivity()
						.getPackageManager()) != null) {
					startActivity(browserIntent);
				}
			}
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	private void onRequestDownloadComplete(Content content) {
		if (mState == State.INITIALIZE_DOWNLOADING) {
			// Initialize complete
			onInitializeDownloadComplete(content);
		}
	}

	private void onRequestDownloadFail() {
		if (mState == State.INITIALIZE_DOWNLOADING) {
			// Initialize fail
			onInitializeDownloadFail();
		}
	}

	private void onInitializeDownloading() {
		mState = State.INITIALIZE_DOWNLOADING;
		Log.d(LOG_TAG, mState.toString());

		mLoadingView.setDownloading();
	}

	private void onInitializeDownloadComplete(Content content) {
		mState = State.INITIALIZE_DOWNLOAD_COMPLETE;
		Log.d(LOG_TAG, mState.toString());

		mArticle = new Article(content.getDocument());
		mLoadingView.setDownloadComplete();
		showArticle(mArticle);
	}

	private void onInitializeDownloadFail() {
		mState = State.INITIALIZE_DOWNLOAD_FAIL;
		Log.d(LOG_TAG, mState.toString());

		mLoadingView.setDownloadFailNoContent();
		;
	}

	private void onInitializeFailNoNetwork() {
		mState = State.INITIALIZE_DOWNLOAD_FAIL_NO_NETWORK;
		Log.d(LOG_TAG, mState.toString());

		mLoadingView.setDownloadFailNoNetwork();
	}

	private void restoreView(State state) {
		Log.d(LOG_TAG, mState.toString());

		switch (state) {
		case NOT_INITIALIZED:
			break;

		case INITIALIZE_DOWNLOADING:
			mLoadingView.setDownloading();
			break;

		case INITIALIZE_DOWNLOAD_FAIL:
			mLoadingView.setDownloadFailNoContent();
			;
			break;

		case INITIALIZE_DOWNLOAD_FAIL_NO_NETWORK:
			mLoadingView.setDownloadFailNoNetwork();
			break;

		case INITIALIZE_DOWNLOAD_COMPLETE:
			mLoadingView.setDownloadComplete();
			showArticle(mArticle);
			break;
		}
	}

	private void showArticle(Article article) {
		if (article != null) {
			mArticleView.setArticle(article);
		}
	}
}
