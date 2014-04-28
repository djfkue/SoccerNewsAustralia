package com.luxiliu.soccernewsaustralia.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * The ConnectionManager class provides methods to query the state of network
 * connectivity.
 * 
 * @author Luxi Liu (luxi.liu@gmail.com)
 * 
 */
public class ConnectionManager {
	// make ConnectionManager as a singleton class
	private static final ConnectionManager mInstance = new ConnectionManager();

	private ConnectionManager() {
	}

	public static ConnectionManager instance() {
		return mInstance;
	}

	public boolean isConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

		return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
	}
}
