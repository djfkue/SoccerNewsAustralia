package com.luxiliu.soccernewsaustralia.preference;

import com.luxiliu.soccernewsaustralia.R;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * The Preferences class provides preferences settings of this application
 * 
 * @author Luxi Liu (luxi.liu@gmail.com)
 * 
 */
public class Preferences {
	private static final int DEFAULT_ARTICLE_TEXT_SIZE_INDEX = 1;
	private static final int ARTICLE_TEXT_SIZE_SMALL = 0;
	private static final int ARTICLE_TEXT_SIZE_NORMAL = 1;
	private static final int ARTICLE_TEXT_SIZE_LARGE = 2;

	// make Preferencs as a static class
	private Preferences() {
	}

	public static enum FontSize {
		Small, Normal, Large
	}

	public static FontSize getArticleFontSize(Context context) {
		// get all the available font size values
		String[] fontSizeValues = context.getResources().getStringArray(
				R.array.article_text_size_values);

		// get font size from preference
		String prefsFile = context.getPackageName() + "_preferences";
		SharedPreferences preferences = context.getSharedPreferences(prefsFile,
				Context.MODE_PRIVATE);
		String value = preferences.getString(
				context.getString(R.string.article_text_size_preference_key),
				fontSizeValues[DEFAULT_ARTICLE_TEXT_SIZE_INDEX]);

		// find corresponding font size
		int index = 0;
		for (; index < fontSizeValues.length; index++) {
			if (value.equals(fontSizeValues[index])) {
				break;
			}
		}

		switch (index) {
		case ARTICLE_TEXT_SIZE_SMALL:
			return FontSize.Small;

		case ARTICLE_TEXT_SIZE_NORMAL:
			return FontSize.Normal;

		case ARTICLE_TEXT_SIZE_LARGE:
			return FontSize.Large;

		default:
			return FontSize.Normal;
		}
	}

	public static FontSize toFontSize(Context context, String value) {
		String[] fontSizeValues = context.getResources().getStringArray(
				R.array.article_text_size_values);

		// find corresponding font size index
		int index = 0;
		for (; index < fontSizeValues.length; index++) {
			if (value.equals(fontSizeValues[index])) {
				break;
			}
		}

		switch (index) {
		case ARTICLE_TEXT_SIZE_SMALL:
			return FontSize.Small;

		case ARTICLE_TEXT_SIZE_NORMAL:
			return FontSize.Normal;

		case ARTICLE_TEXT_SIZE_LARGE:
			return FontSize.Large;

		default:
			return FontSize.Normal;
		}
	}
}
