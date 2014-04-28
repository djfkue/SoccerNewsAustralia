package com.luxiliu.soccernewsaustralia.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import com.luxiliu.soccernewsaustralia.R;

/**
 * The Util class provides utility methods
 * 
 * @author Luxi Liu (luxi.liu@gmail.com)
 * 
 */
public final class Util {
	// make Util class as a static class
	private Util() {
	}

	public static String getApplicationVersion(Context context) {
		String version = new String();

		try {
			// get package info
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);

			// get version name and code
			String versionName = packageInfo.versionName;
			int versionCode = packageInfo.versionCode;
			Object[] objects = new Object[2];
			objects[0] = versionName;
			objects[1] = Integer.valueOf(versionCode);

			// format to readable version string
			version = context.getString(R.string.release, objects);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return version;
	}

	public static String toPrettyDate(Date date) {
		Date now = new Date();
		long duration = now.getTime() - date.getTime();
		long value = 0;
		String ret;

		// default date string
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
}
