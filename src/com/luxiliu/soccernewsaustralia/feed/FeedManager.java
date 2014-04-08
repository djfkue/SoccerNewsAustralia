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

import com.luxiliu.soccernewsaustralia.model.Content;

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

	private static final FeedManager mInstance = new FeedManager();

	public static FeedManager instance() {
		return mInstance;
	}

	public void requestDownloadFeed(Handler handler, String url) {
		DownloadTask task = new DownloadTask(handler);

		task.execute(url);
	}

	private FeedManager() {
	}

	private static class DownloadTask extends
			AsyncTask<String, Integer, Content> {
		private static final int CONNECTION_TIMEOUT = 5000;
		private static final int READ_TIMEOUT = 5000;
		private Handler mHandler;

		public DownloadTask(Handler handler) {
			mHandler = handler;
		}

		@Override
		protected Content doInBackground(String... urls) {
			// These error may cause page null
			// No network connection; Cannot reach feed host, connection
			// timeout, invalid content, and etc.

			Content content = null;
			URL url = null;

			try {
				url = new URL(urls[0].replaceAll(" ", "%20"));

				HttpURLConnection connection = null;
				try {
					connection = (HttpURLConnection) url.openConnection();
					connection.setConnectTimeout(CONNECTION_TIMEOUT);
					connection.setReadTimeout(READ_TIMEOUT);

					InputStream inputStream = null;
					try {
						inputStream = connection.getInputStream();
						content = new Content(new SAXReader().read(inputStream));
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

			return content;
		}

		@Override
		protected void onPostExecute(Content content) {
			Message message = mHandler.obtainMessage();

			if (content != null) {
				Bundle bundle = new Bundle();
				bundle.putSerializable(KEY_FEED_CONTENTS, content);
				message.setData(bundle);
				message.what = FeedManager.REQUEST_DOWNLOAD_FEED_COMPLETE;
			} else {
				// Some errors happened in downloading
				message.what = FeedManager.REQUEST_DOWNLOAD_FEED_FAIL;
			}

			mHandler.sendMessage(message);
		}
	}
}
