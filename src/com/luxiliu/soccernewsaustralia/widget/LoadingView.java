package com.luxiliu.soccernewsaustralia.widget;

import com.luxiliu.soccernewsaustralia.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * The LoadingView provides a simple view to display the state of content
 * download
 * 
 * @author Luxi Liu (luxi.liu@gmail.com)
 * 
 */
public class LoadingView extends RelativeLayout {
	private ProgressBar mProgressBar;
	private RelativeLayout mLoadResultLayout;
	private ImageView mImageView;
	private TextView mTextView;

	public LoadingView(Context context) {
		super(context);

		initView();
	}

	public LoadingView(Context context, AttributeSet attrs) {
		super(context, attrs);

		initView();
	}

	public LoadingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		initView();
	}

	public void setDownloading() {
		setVisibility(View.VISIBLE);

		mProgressBar.setVisibility(View.VISIBLE);

		mLoadResultLayout.setVisibility(View.GONE);
	}

	public void setDownloadSucceed(boolean succeed) {
		if (succeed) {
			setVisibility(View.INVISIBLE);

			mProgressBar.setVisibility(View.GONE);

			mLoadResultLayout.setVisibility(View.GONE);
		} else {
			setVisibility(View.VISIBLE);

			mProgressBar.setVisibility(View.GONE);

			mLoadResultLayout.setVisibility(View.VISIBLE);

			mImageView.setImageDrawable(getResources().getDrawable(
					R.drawable.ic_empty_news));

			mTextView.setText(getResources().getString(
					R.string.content_failed_to_load));
		}
	}

	public void setNoNetwork() {
		setVisibility(View.VISIBLE);

		mProgressBar.setVisibility(View.GONE);

		mLoadResultLayout.setVisibility(View.VISIBLE);

		mImageView.setImageDrawable(getResources().getDrawable(
				R.drawable.ic_empty_download));
		mTextView.setText(getResources().getString(
				R.string.content_error_bad_connection));
	}

	private void initView() {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.loading_view, this, true);

		mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

		mLoadResultLayout = (RelativeLayout) findViewById(R.id.load_result_layout);

		mImageView = (ImageView) findViewById(R.id.image);

		mTextView = (TextView) findViewById(R.id.message);
	}
}
