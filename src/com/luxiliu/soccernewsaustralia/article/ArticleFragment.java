package com.luxiliu.soccernewsaustralia.article;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.luxiliu.soccernewsaustralia.R;
import com.luxiliu.soccernewsaustralia.feed.FeedManager;
import com.luxiliu.soccernewsaustralia.model.Article;
import com.luxiliu.soccernewsaustralia.model.Feed;
import com.luxiliu.soccernewsaustralia.model.News;
import com.luxiliu.soccernewsaustralia.net.ConnectionManager;
import com.luxiliu.soccernewsaustralia.widget.LoadingView;
import com.luxiliu.soccernewsaustralia.widget.NotifyingScrollView;

/**
 * The ArticleFragment provides a fragment to display feed content article
 * 
 * @author Luxi Liu (luxi.liu@gmail.com)
 * 
 */
public class ArticleFragment extends Fragment {
	private static final String LOG_TAG = "ArticleFragment";

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

    private Drawable mActionBarBackgroundDrawable;
    private NotifyingScrollView.OnScrollChangedListener mOnScrollChangedListener = new NotifyingScrollView.OnScrollChangedListener() {
        public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
            final int headerHeight = mArticleView.findViewById(R.id.image).getHeight() - getActivity().getActionBar().getHeight();
            final float ratio = (float) Math.min(Math.max(t, 0), headerHeight) / headerHeight;
            final int newAlpha = (int) (ratio * 255);
            mActionBarBackgroundDrawable.setAlpha(newAlpha);

            mArticleView.findViewById(R.id.image).setTranslationY(  t / 2 );
        }
    };

	private Handler mHandler = new Handler(Looper.getMainLooper()) {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case FeedManager.REQUEST_DOWNLOAD_FEED_COMPLETE:
				Feed feed = (Feed) msg.getData().getSerializable(
						FeedManager.KEY_FEED_CONTENTS);
				onRequestDownloadComplete(feed);
				break;

			case FeedManager.REQUEST_DOWNLOAD_FEED_FAIL:
				onRequestDownloadFail();
				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// enable action bar option menu
		setHasOptionsMenu(true);

		// retain fragment instance
		setRetainInstance(true);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// get news instance
		mNews = (News) getArguments().getSerializable(Intent.EXTRA_STREAM);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// get main view
		View view = inflater.inflate(R.layout.article_fragment, container,
				false);

		// get loading view
		mLoadingView = (LoadingView) view.findViewById(R.id.loading_view);
		mLoadingView.setOnRetryClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				retry();
			}
		});

		// get article view
		mArticleView = (ArticleView) view.findViewById(R.id.article);

        mActionBarBackgroundDrawable = new ColorDrawable(Color.argb(0xff, 0x66, 0x99, 0x00));
        mActionBarBackgroundDrawable.setAlpha(0);
        getActivity().getActionBar().setBackgroundDrawable(mActionBarBackgroundDrawable);

        NotifyingScrollView notifyingScrollView = (NotifyingScrollView)mArticleView.findViewById(R.id.scroll_view);
        notifyingScrollView.setOnScrollChangedListener(mOnScrollChangedListener);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (mState == State.NOT_INITIALIZED) {
			if (ConnectionManager.instance().isConnected(getActivity())) {
				// network is OK
				// start to initialize
				onInitializeDownloading();
				mFeedManager.requestDownloadFeed(mHandler,
						mNews.getFeedLinkUrl());
			} else {
				// no network connection
				onInitializeFailNoNetwork();
			}
		} else {
			// just restore view for previous state
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
			// back to previous activity
			if (getActivity() != null) {
				getActivity().finish();
			}
			break;

		case R.id.menu_item_share:
			// share this article
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
			// start a browser to view this article
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

	private void onRequestDownloadComplete(Feed feed) {
		if (mState == State.INITIALIZE_DOWNLOADING) {
			// initialize complete
			onInitializeDownloadComplete(feed);
		}
	}

	private void onRequestDownloadFail() {
		if (mState == State.INITIALIZE_DOWNLOADING) {
			// initialize fail
			onInitializeDownloadFail();
		}
	}

	private void onInitializeDownloading() {
		mState = State.INITIALIZE_DOWNLOADING;
		Log.d(LOG_TAG, mState.toString());

		mLoadingView.setDownloading();
		mArticleView.setVisibility(View.INVISIBLE);
	}

	private void onInitializeDownloadComplete(Feed feed) {
		mState = State.INITIALIZE_DOWNLOAD_COMPLETE;
		Log.d(LOG_TAG, mState.toString());

		mArticle = new Article(feed.getDocument());
		mLoadingView.setDownloadComplete();

		showArticle(mArticle);
	}

	private void onInitializeDownloadFail() {
		mState = State.INITIALIZE_DOWNLOAD_FAIL;
		Log.d(LOG_TAG, mState.toString());

		mLoadingView.setDownloadFailNoContent();
		mArticleView.setVisibility(View.INVISIBLE);
	}

	private void onInitializeFailNoNetwork() {
		mState = State.INITIALIZE_DOWNLOAD_FAIL_NO_NETWORK;
		Log.d(LOG_TAG, mState.toString());

		mLoadingView.setDownloadFailNoNetwork(true);
		mArticleView.setVisibility(View.INVISIBLE);
	}

	private void restoreView(State state) {
		Log.d(LOG_TAG, mState.toString());

		switch (state) {
		case NOT_INITIALIZED:
			break;

		case INITIALIZE_DOWNLOADING:
			mLoadingView.setDownloading();
			mArticleView.setVisibility(View.INVISIBLE);
			break;

		case INITIALIZE_DOWNLOAD_FAIL:
			mLoadingView.setDownloadFailNoContent();
			mArticleView.setVisibility(View.INVISIBLE);
			break;

		case INITIALIZE_DOWNLOAD_FAIL_NO_NETWORK:
			mLoadingView.setDownloadFailNoNetwork(true);
			mArticleView.setVisibility(View.INVISIBLE);
			break;

		case INITIALIZE_DOWNLOAD_COMPLETE:
			mLoadingView.setDownloadComplete();

			showArticle(mArticle);
			break;
		}
	}

	private void showArticle(Article article) {
		if (article != null) {
			mArticleView.setVisibility(View.VISIBLE);
			mArticleView.setArticle(article);
		}
	}

	private void retry() {
		if (mState == State.INITIALIZE_DOWNLOAD_FAIL_NO_NETWORK) {
			if (ConnectionManager.instance().isConnected(getActivity())) {
				// network is OK
				// start to initialize again
				onInitializeDownloading();
				mFeedManager.requestDownloadFeed(mHandler,
						mNews.getFeedLinkUrl());
			} else {
				// no network connection
				onInitializeFailNoNetwork();
			}
		}
	}
}
