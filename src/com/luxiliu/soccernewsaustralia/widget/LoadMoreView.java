package com.luxiliu.soccernewsaustralia.widget;

import com.luxiliu.soccernewsaustralia.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

/**
 * The LoadMoreView provides a view for loading more state
 * 
 * @author Luxi Liu (luxi.liu@gmail.com)
 * 
 */
public class LoadMoreView extends RelativeLayout {

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

	private void initView() {
		// inflate the main view
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.loading_view, this, true);
	}
}
