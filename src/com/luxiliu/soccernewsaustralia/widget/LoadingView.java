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
 * The LoadingView provides a view for loading content
 * 
 * @author Luxi Liu (luxi.liu@gmail.com)
 * 
 */
public class LoadingView extends RelativeLayout {

	private ProgressBar mProgressBar;
	private RelativeLayout mLoadResultLayout;
	private ImageView mImageView;
	private TextView mMessageTextView;
	private TextView mRetryTextView;
	private OnClickListener mRetryListener;

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

	private void initView() {
		// inflate the main view
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.loading_view, this, true);

		// get progress bar
		mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

		// get result layout
		mLoadResultLayout = (RelativeLayout) findViewById(R.id.load_result_layout);

		// get image view
		mImageView = (ImageView) findViewById(R.id.image);

		// get message view
		mMessageTextView = (TextView) findViewById(R.id.message);

		// get retry text view
		mRetryTextView = (TextView) findViewById(R.id.retry);
		mRetryTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mRetryListener != null) {
					// "retry" is clicked
					mRetryListener.onClick(v);
				}
			}
		});
	}

	public void setDownloading() {
		// show the view
		setVisibility(View.VISIBLE);

		// show the progress bar
		mProgressBar.setVisibility(View.VISIBLE);

		// hide load result layout
		mLoadResultLayout.setVisibility(View.GONE);
	}

	public void setDownloadComplete() {
		// hide the view
		setVisibility(View.INVISIBLE);

		// hide the progress bar
		mProgressBar.setVisibility(View.GONE);

		// hide the load result layout
		mLoadResultLayout.setVisibility(View.GONE);
	}

	public void setDownloadFailNoNetwork(boolean retry) {
		// show the view
		setVisibility(View.VISIBLE);

		// hide the progress bar
		mProgressBar.setVisibility(View.GONE);

		// show the load result layout
		mLoadResultLayout.setVisibility(View.VISIBLE);

		// show the image and message
		mImageView.setImageDrawable(getResources().getDrawable(
				R.drawable.ic_empty_download));
		mMessageTextView.setText(getResources().getString(
				R.string.content_error_bad_connection));

		if (retry) {
			mRetryTextView.setVisibility(View.VISIBLE);
		} else {
			mRetryTextView.setVisibility(View.GONE);
		}
	}

	public void setDownloadFailNoContent() {
		// show the view
		setVisibility(View.VISIBLE);

		// hide the progress bar
		mProgressBar.setVisibility(View.GONE);

		// show the load result layout
		mLoadResultLayout.setVisibility(View.VISIBLE);

		// show the image and message
		mImageView.setImageDrawable(getResources().getDrawable(
				R.drawable.ic_empty_news));
		mMessageTextView.setText(getResources().getString(
				R.string.content_failed_to_load));

		mRetryTextView.setVisibility(View.GONE);
	}

	public void setOnRetryClickListener(OnClickListener listener) {
		mRetryListener = listener;
	}
}
