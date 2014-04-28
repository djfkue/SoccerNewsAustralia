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
 * The NewsCard class provides a card view to display a News
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
		// create a thumbnail
		NewsCardThumbnail newsCardThumbnail = new NewsCardThumbnail(
				getContext(), mNews.getImageUrl());

		// it must be set to use a external library
		newsCardThumbnail.setExternalUsage(true);

		// add thumbnail
		addCardThumbnail(newsCardThumbnail);
	}

	@Override
	public void setupInnerViewElements(ViewGroup parent, View view) {
		// get title and secondary title
		TextView titleView = (TextView) parent
				.findViewById(R.id.news_card_title);
		TextView descriptionView = (TextView) parent
				.findViewById(R.id.news_card_description);
		TextView dateView = (TextView) parent.findViewById(R.id.news_card_date);

		// use RobotoSlab font
		Typeface typeface = Typeface.createFromAsset(mContext.getAssets(),
				"fonts/RobotoSlab-Light.ttf");

		// set title
		if (titleView != null) {
			if (mNews.getImageUrl() == null || mNews.getImageUrl().isEmpty()) {
				// set suitable padding value if news has no image
				titleView.setPadding(
						titleView.getPaddingLeft(),
						view.getContext()
								.getResources()
								.getDimensionPixelSize(
										R.dimen.news_card_title_padding_top),
						titleView.getPaddingRight(),
						titleView.getPaddingBottom());
			}
			titleView.setTypeface(typeface);
			titleView.setText(mNews.getTitle());
		}

		// set description if news no image
		if (descriptionView != null) {
			if (mNews.getImageUrl() == null || mNews.getImageUrl().isEmpty()) {
				descriptionView.setTypeface(typeface);
				descriptionView.setText(mNews.getDescription());
			} else {
				descriptionView.setVisibility(View.GONE);
			}
		}

		// set date
		if (dateView != null) {
			dateView.setTypeface(typeface);
			dateView.setText(Util.toPrettyDate(mNews.getPublishedDate()));
		}
	}
}
