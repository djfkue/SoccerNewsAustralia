package com.luxiliu.soccernewsaustralia.article;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.luxiliu.soccernewsaustralia.R;
import com.luxiliu.soccernewsaustralia.activity.SNAActivity;
import com.luxiliu.soccernewsaustralia.model.News;

/**
 * The ArticleActivity is the activity to display feed article
 * 
 * @author luxi.liu
 * 
 */
public class ArticleActivity extends SNAActivity {
	private static final String ARTICLE_FRAGMENT = "article_fragment";
	private Fragment mArticleFragment;
	private News mNews;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mNews = (News) getIntent().getSerializableExtra(Intent.EXTRA_STREAM);

		setContentView(R.layout.article_activity);

		setupView();

		if (savedInstanceState == null) {
			// Init content for the first time
			initContent();
		} else {
			// Restore content
			restoreContent(savedInstanceState);
		}
	}

	private void setupView() {
		// Setup "Home" menu
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// Set title
		setTitle(mNews.getCategory());
	}

	private void initContent() {
		// Init fragment
		initFragment();
	}

	private void restoreContent(Bundle savedInstanceState) {
		restoreFragment();
	}

	private void initFragment() {
		// Initialize fragment manager and transaction
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();

		// Find an existing article fragment
		mArticleFragment = (Fragment) fragmentManager
				.findFragmentByTag(ARTICLE_FRAGMENT);

		if (mArticleFragment == null) {
			// Create a new fragment if not found and set argument to it
			mArticleFragment = new ArticleFragment();
			mArticleFragment.setArguments(getIntent().getExtras());
		}

		if (!mArticleFragment.isAdded()) {
			// Add fragment is not added yet
			fragmentTransaction.add(R.id.article_fragment, mArticleFragment,
					ARTICLE_FRAGMENT).commit();
		}
	}

	private void restoreFragment() {
		initFragment();
	}
}
