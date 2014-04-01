package com.luxiliu.soccernewsaustralia.card;

import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import it.gmariotti.cardslib.library.internal.CardThumbnail;

/**
 * This class provides a thumbnail for a news card
 * 
 * @author Luxi Liu (luxi.liu@gmail.com)
 * 
 */
public class NewsCardThumbnail extends CardThumbnail {
	private String mThumbnailUrl;

	public NewsCardThumbnail(Context context, String thumbnailUrl) {
		super(context);

		// Thumbnail url
		mThumbnailUrl = thumbnailUrl;
	}

	@Override
	public void setupInnerViewElements(ViewGroup parent, View imageView) {
		// Load thumbnail image with Picasso library
		Picasso.with(getContext()).load(mThumbnailUrl)
				.into((ImageView) imageView);
	}
}
