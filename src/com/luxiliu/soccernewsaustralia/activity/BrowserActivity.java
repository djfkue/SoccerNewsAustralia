package com.luxiliu.soccernewsaustralia.activity;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;

/**
 * The BrowserActivity is a simple activity to display a web page from a URL
 * 
 * @author Luxi Liu (luxi.liu@gmail.com)
 * 
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class BrowserActivity extends SNAActivity {
	private String mUrl;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// get the URL
		Intent intent = getIntent();
		mUrl = intent.getDataString();

		// setup action bar
		getSupportActionBar().setDisplayOptions(
				ActionBar.DISPLAY_USE_LOGO | ActionBar.DISPLAY_SHOW_HOME
						| ActionBar.DISPLAY_HOME_AS_UP
						| ActionBar.DISPLAY_SHOW_TITLE);

		// load the web page
		WebView webView = new WebView(this);
		webView.setHorizontalScrollBarEnabled(false);
		webView.setVerticalScrollBarEnabled(false);
		webView.loadUrl(mUrl);

		// set the main content view
		setContentView(webView);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// back to last activity if "Home" menu is clicked
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
