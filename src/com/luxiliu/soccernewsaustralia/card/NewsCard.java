package com.luxiliu.soccernewsaustralia.card;

import com.luxiliu.soccernewsaustralia.R;
import com.luxiliu.soccernewsaustralia.model.News;
import com.luxiliu.soccernewsaustralia.util.Util;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import it.gmariotti.cardslib.library.internal.Card;

/**
 * This class provides a card view to display a News
 * 
 * @author Luxi Liu (luxi.liu@gmail.com)
 * 
 */
public class NewsCard extends Card {
	private News mNews;

	public NewsCard(Context context, News news) {
		super(context, R.layout.news_card);

		mNews = news;

		init();
	}

	private void init() {
		// Create a thumbnail
		NewsCardThumbnail newsCardThumbnail = new NewsCardThumbnail(
				getContext(), mNews.getImageUrl());

		// It must be set to use a external library
		newsCardThumbnail.setExternalUsage(true);

		// Add thumbnail
		addCardThumbnail(newsCardThumbnail);
	}

	@Override
	public void setupInnerViewElements(ViewGroup parent, View view) {
		// Get views, thumbnail will setup its view by itself
		TextView titleView = (TextView) parent
				.findViewById(R.id.news_card_title);
		TextView secondaryTitleView = (TextView) parent
				.findViewById(R.id.news_card_secondary_title);

		// Use RobotoSlab font
		Typeface typeface = Typeface.createFromAsset(mContext.getAssets(),
				"fonts/RobotoSlab-Light.ttf");

		// Set title
		if (titleView != null) {
			titleView.setTypeface(typeface);
			titleView.setText(mNews.getTitle());
		}

		// Set date as the secondary titile
		if (secondaryTitleView != null) {
			secondaryTitleView.setTypeface(typeface);
			secondaryTitleView.setText(Util.toPrettyDate(mNews
					.getPublishedDate()));
		}
	}
}
