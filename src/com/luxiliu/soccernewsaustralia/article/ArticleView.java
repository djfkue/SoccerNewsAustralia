package com.luxiliu.soccernewsaustralia.article;

import com.luxiliu.soccernewsaustralia.R;
import com.luxiliu.soccernewsaustralia.model.Article;
import com.luxiliu.soccernewsaustralia.preference.Preferences;
import com.luxiliu.soccernewsaustralia.preference.Preferences.FontSize;
import com.luxiliu.soccernewsaustralia.util.Util;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * The ArticleView provides a view to display an article
 * 
 * @author Luxi Liu (luxi.liu@gmail.com)
 * 
 */

public class ArticleView extends LinearLayout {
	private Article mArticle;

	private ImageView mImageView;
	private TextView mTitleTextView;
	private TextView mDescriptionTextView;
	private TextView mAuthorTextView;
	private TextView mDateTextView;
	private TextView mArticleTextView;

	public ArticleView(Context context) {
		super(context);

		initView();
	}

	public ArticleView(Context context, AttributeSet attrs) {
		super(context, attrs);

		initView();
	}

	private void initView() {
		// inflate the main view
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.article_view, this, true);

		// get image
		mImageView = (ImageView) findViewById(R.id.image);

		// get title
		mTitleTextView = (TextView) findViewById(R.id.title);

		// get description
		mDescriptionTextView = (TextView) findViewById(R.id.description);

		// get author
		mAuthorTextView = (TextView) findViewById(R.id.author);

		// get date
		mDateTextView = (TextView) findViewById(R.id.date);

		// get text
		mArticleTextView = (TextView) findViewById(R.id.articleText);
	}

	public void setArticle(Article article) {
		mArticle = article;

		updateView();
	}

	private void updateView() {
		if (mArticle != null) {
			// update image
			Picasso.with(getContext()).load(mArticle.getImageUrl())
					.into((ImageView) mImageView);

			// customize font
			Typeface titleTypeface = Typeface.createFromAsset(getContext()
					.getAssets(), "fonts/RobotoSlab-Light.ttf");
			Typeface authorDateArticleTypeface = Typeface.createFromAsset(
					getContext().getAssets(), "fonts/Roboto-Light.ttf");
			Typeface descriptionTypeface = titleTypeface;
			Typeface articleTextTypeface = authorDateArticleTypeface;

			// customize font size
			float density = getResources().getDisplayMetrics().density;
			FontSize fontSize = Preferences.getArticleFontSize(getContext());
			switch (fontSize) {
			case Small:
				mTitleTextView.setTextSize(getResources().getDimension(
						R.dimen.article_title_text_size_small)
						/ density);
				mAuthorTextView.setTextSize(getResources().getDimension(
						R.dimen.article_author_text_size_small)
						/ density);
				mDateTextView.setTextSize(getResources().getDimension(
						R.dimen.article_date_text_size_small)
						/ density);
				mDescriptionTextView.setTextSize(getResources().getDimension(
						R.dimen.article_description_text_size_small)
						/ density);
				mArticleTextView.setTextSize(getResources().getDimension(
						R.dimen.article_text_text_size_small)
						/ density);
				break;

			case Normal:
				mTitleTextView.setTextSize(getResources().getDimension(
						R.dimen.article_title_text_size_normal)
						/ density);
				mAuthorTextView.setTextSize(getResources().getDimension(
						R.dimen.article_author_text_size_normal)
						/ density);
				mDateTextView.setTextSize(getResources().getDimension(
						R.dimen.article_date_text_size_normal)
						/ density);
				mDescriptionTextView.setTextSize(getResources().getDimension(
						R.dimen.article_description_text_size_normal)
						/ density);
				mArticleTextView.setTextSize(getResources().getDimension(
						R.dimen.article_text_text_size_normal)
						/ density);
				break;

			case Large:
				mTitleTextView.setTextSize(getResources().getDimension(
						R.dimen.article_title_text_size_large)
						/ density);
				mAuthorTextView.setTextSize(getResources().getDimension(
						R.dimen.article_author_text_size_large)
						/ density);
				mDateTextView.setTextSize(getResources().getDimension(
						R.dimen.article_date_text_size_large)
						/ density);
				mDescriptionTextView.setTextSize(getResources().getDimension(
						R.dimen.article_description_text_size_large)
						/ density);
				mArticleTextView.setTextSize(getResources().getDimension(
						R.dimen.article_text_text_size_large)
						/ density);
				break;
			}

			// update title
			mTitleTextView.setTypeface(titleTypeface);
			mTitleTextView.setText(mArticle.getTitle());

			// update author
			mAuthorTextView.setTypeface(authorDateArticleTypeface);
			mAuthorTextView.setText(mArticle.getAuthor());

			// update date
			mDateTextView.setTypeface(authorDateArticleTypeface);
			mDateTextView
					.setText(Util.toPrettyDate(mArticle.getPublishedDate()));

			// update description
			mDescriptionTextView.setTypeface(descriptionTypeface);
			mDescriptionTextView.setText(mArticle.getDescription());

			// update article
			mArticleTextView.setTypeface(articleTextTypeface);
			mArticleTextView.setText(Html.fromHtml(mArticle.getArticleText()));
		}
	}
}
