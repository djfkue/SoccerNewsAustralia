package com.luxiliu.soccernewsaustralia.widget;

import com.luxiliu.soccernewsaustralia.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

/**
 * The LoadingView provides a simple view to display the state of content
 * download
 * 
 * @author Luxi Liu (luxi.liu@gmail.com)
 * 
 */
public class LoadMoreView extends RelativeLayout {
	private ProgressBar mProgressBar;

	public LoadMoreView(Context context) {
		super(context);

		initView();
	}

	public LoadMoreView(Context context, AttributeSet attrs) {
		super(context, attrs);

		initView();
	}

	public LoadMoreView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		initView();
	}

	public void setEnable(boolean enable) {
		if (enable) {
			mProgressBar.setVisibility(View.VISIBLE);
			mProgressBar.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		} else {
			setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 1));

			mProgressBar.setVisibility(View.INVISIBLE);
			mProgressBar.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, 0));
		}

		invalidate();
	}

	private void initView() {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.loading_view, this, true);

		mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
	}
}
