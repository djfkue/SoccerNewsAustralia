package com.luxiliu.soccernewsaustralia.article;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.WindowManager;

import com.luxiliu.soccernewsaustralia.R;
import com.luxiliu.soccernewsaustralia.activity.SNAActivity;
import com.luxiliu.soccernewsaustralia.model.News;

/**
 * The ArticleActivity is the activity to display feed article
 * 
 * @author Luxi Liu (luxi.liu@gmail.com)
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

		setupContent();
	}

	private void setupView() {
		// setup "Home" menu
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// set title
		setTitle(mNews.getCategory());
	}

	private void setupContent() {
		// setup fragment
		setupFragment();
	}

	private void setupFragment() {
		// initialize fragment manager and transaction
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();

		// find an existing article fragment
		mArticleFragment = (Fragment) fragmentManager
				.findFragmentByTag(ARTICLE_FRAGMENT);

		if (mArticleFragment == null) {
			// create a new fragment if not found and set argument to it
			mArticleFragment = new ArticleFragment();
			mArticleFragment.setArguments(getIntent().getExtras());
		}

		if (!mArticleFragment.isAdded()) {
			// add fragment is not added yet
			fragmentTransaction.add(R.id.article_fragment, mArticleFragment,
					ARTICLE_FRAGMENT).commit();
		}
	}
}
