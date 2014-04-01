package com.luxiliu.soccernewsaustralia.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import android.content.res.Configuration;
import android.support.v4.app.Fragment;

/**
 * The Util class provides utility methods
 * 
 * @author Luxi Liu (luxi.liu@gmail.com)
 * 
 * 
 * 
 */
public final class Util {
	public static boolean isPortrait(Fragment fragment) {
		if (fragment != null) {
			return (fragment.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
		} else {
			return false;
		}
	}

	public static String toPrettyDate(Date date) {
		Date now = new Date();
		long duration = now.getTime() - date.getTime();
		long value = 0;
		String ret;

		// Default return value
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMM yyyy, HH:mm",
				Locale.getDefault());
		ret = sdf.format(date);

		if (duration > 0) {
			if ((value = TimeUnit.MILLISECONDS.toDays(duration)) != 0) {
				if (value == 1) {
					ret = value + " day ago";
				} else {
					ret = value + " days ago";
				}
			} else if ((value = TimeUnit.MILLISECONDS.toHours(duration)) != 0) {
				if (value == 1) {
					ret = value + " hour ago";
				} else {
					ret = value + " hours ago";
				}
			} else if ((value = TimeUnit.MILLISECONDS.toMinutes(duration)) != 0) {
				if (value == 1) {
					ret = value + " minute ago";
				} else {
					ret = value + " minutes ago";
				}
			} else if ((value = TimeUnit.MILLISECONDS.toSeconds(duration)) != 0) {
				if (value == 1) {
					ret = value + " second ago";
				} else {
					ret = value + " seconds ago";
				}
			}
		}

		return ret;
	}

	// Make Util class as a static class
	private Util() {
	}
}
