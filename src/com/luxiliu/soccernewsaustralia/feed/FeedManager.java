package com.luxiliu.soccernewsaustralia.feed;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.dom4j.io.SAXReader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.luxiliu.soccernewsaustralia.model.Feed;

/**
 * The FeedManager class provides methods to download feed content from provider
 * 
 * @author Luxi Liu (luxi.liu@gmail.com)
 * 
 */
public final class FeedManager {
	public static final String KEY_FEED_CONTENTS = "feed_contents";
	public static final int REQUEST_DOWNLOAD_FEED_COMPLETE = 0x00;
	public static final int REQUEST_DOWNLOAD_FEED_FAIL = 0x01;

	// make FeedManager as a static class
	private static final FeedManager mInstance = new FeedManager();

	public static FeedManager instance() {
		return mInstance;
	}

	private FeedManager() {
	}

	public void requestDownloadFeed(Handler handler, String url) {
		DownloadTask task = new DownloadTask(handler);

		task.execute(url);
	}

	private static class DownloadTask extends AsyncTask<String, Integer, Feed> {
		private static final int CONNECTION_TIMEOUT = 5000;
		private static final int READ_TIMEOUT = 5000;
		private Handler mHandler;

		public DownloadTask(Handler handler) {
			mHandler = handler;
		}

		@Override
		protected Feed doInBackground(String... urls) {
			Feed feed = null;
			URL url = null;

			try {
				// replace spaces
				url = new URL(urls[0].replaceAll(" ", "%20"));

				HttpURLConnection connection = null;
				try {
					// create connection
					connection = (HttpURLConnection) url.openConnection();
					connection.setConnectTimeout(CONNECTION_TIMEOUT);
					connection.setReadTimeout(READ_TIMEOUT);

					InputStream inputStream = null;
					try {
						// get input stream from connection
						inputStream = connection.getInputStream();

						// transform into Feed instance
						feed = new Feed(new SAXReader().read(inputStream));
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						inputStream.close();
						connection.disconnect();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

			return feed;
		}

		@Override
		protected void onPostExecute(Feed feed) {
			Message message = mHandler.obtainMessage();

			if (feed != null) {
				Bundle bundle = new Bundle();
				bundle.putSerializable(KEY_FEED_CONTENTS, feed);
				message.setData(bundle);
				message.what = FeedManager.REQUEST_DOWNLOAD_FEED_COMPLETE;
			} else {
				// some errors happened in downloading
				message.what = FeedManager.REQUEST_DOWNLOAD_FEED_FAIL;
			}

			mHandler.sendMessage(message);
		}
	}
}
