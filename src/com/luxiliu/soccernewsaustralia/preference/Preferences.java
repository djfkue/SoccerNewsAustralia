package com.luxiliu.soccernewsaustralia.preference;

import com.luxiliu.soccernewsaustralia.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;

/**
 * The Preferences class is to provide preferences of applications
 * 
 * @author Luxi Liu (luxi.liu@gmail.com)
 * 
 */
public class Preferences {
	private static final int DEFAULT_ARTICLE_TEXT_SIZE_INDEX = 1;

	private Preferences() {

	}

	public static enum FontSize {
		Small, Normal, Large
	}

	public static FontSize getArticleFontSize(Context context) {
		// Get all the available font size values
		String[] fontSizeValues = context.getResources().getStringArray(
				R.array.article_text_size_values);

		// Get font size from preference
		String prefsFile = context.getPackageName() + "_preferences";
		SharedPreferences preferences = context.getSharedPreferences(prefsFile,
				Context.MODE_PRIVATE);
		String value = preferences.getString(
				context.getString(R.string.article_text_size_preference_key),
				fontSizeValues[DEFAULT_ARTICLE_TEXT_SIZE_INDEX]);

		// Find corresponding font size
		int index = 0;
		for (; index < fontSizeValues.length; index++) {
			if (value.equals(fontSizeValues[index])) {
				break;
			}
		}

		switch (index) {
		case 0:
			return FontSize.Small;
		case 1:
			return FontSize.Normal;
		case 2:
			return FontSize.Large;
		default:
			return FontSize.Normal;
		}
	}

	public static FontSize toFontSize(Context context, String value) {
		String[] fontSizeValues = context.getResources().getStringArray(
				R.array.article_text_size_values);

		int index = 0;
		for (; index < fontSizeValues.length; index++) {
			if (value.equals(fontSizeValues[index])) {
				break;
			}
		}

		switch (index) {
		case 0:
			return FontSize.Small;
		case 1:
			return FontSize.Normal;
		case 2:
			return FontSize.Large;
		default:
			return FontSize.Normal;
		}
	}
}
