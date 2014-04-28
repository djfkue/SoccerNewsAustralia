package com.luxiliu.soccernewsaustralia.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.dom4j.Element;
import org.dom4j.Node;

import com.luxiliu.soccernewsaustralia.model.Feed.Type;

/**
 * The News class represents <item> element in the goal.com feed and FFA feed
 * 
 * @author Luxi Liu (luxi.liu@gmail.com)
 * 
 */
public class News implements Serializable {
	private static final long serialVersionUID = 2484234548662887150L;

	private static final String GOAL_FEED_ELEMENT_NAME = "item";
	private static final String GOAL_FEED_ATTRIBUTE_TYPE_NAME = "type";
	private static final String GOAL_FEED_ATTRIBUTE_TYPE_VALUE = "news";
	private static final String GOAL_FEED_ATTRIBUTE_ID_NAME = "id";
	private static final String GOAL_FEED_TAG_PUBLISHED_DATE = "publishedDate";
	private static final String GOAL_FEED_TAG_TITLE = "title";
	private static final String GOAL_FEED_TAG_DESCRIPTION = "description";
	private static final String GOAL_FEED_NODE_IMAGES_IMAGE = "images/image";
	private static final String GOAL_FEED_NODE_IMAGES_THUMBNAIL = "images/thumbnail";
	private static final String GOAL_FEED_NODE_URLS_FEED_LINK = "urls/feedLink";
	private static final String GOAL_FEED_NODE_URLS_WEBVIEW_LINK = "urls/webviewLink";
	private static final String GOAL_FEED_NODE_URLS_WEBSITE_LINK = "urls/websiteLink";

	private static final String FFA_FEED_ELEMENT_NAME = "item";
	private static final String FFA_FEED_TAG_TITLE = "title";
	private static final String FFA_FEED_TAG_LINK = "link";
	private static final String FFA_FEED_TAG_DESCRIPTION = "description";
	private static final String FFA_FEED_TAG_PUB_DATE = "pubDate";

	private String mId;
	private Date mPublishedDate;
	private String mTitle;
	private String mDescription;
	private String mImageUrl;
	private String mThumbnailUrl;
	private String mFeedLinkUrl;
	private String mWebviewLinkUrl;
	private String mWebsiteLinkUrl;

	private String mCategory;

	public News(Element element, Type type) {
		switch (type) {
		case GoalFeed:
			parseGoalFeedNews(element);
			break;

		case FfaFeed:
			parseFfaFeedNews(element);
			break;

		default:
			break;
		}
	}

	private void parseGoalFeedNews(Element element) {
		// element must not be null;
		// element name must be "item"
		// element attribute "type" must not be null
		// element attribute "type" must be "news"
		if (element != null
				&& element.getName().equals(GOAL_FEED_ELEMENT_NAME)
				&& element.attributeValue(GOAL_FEED_ATTRIBUTE_TYPE_NAME) != null
				&& element.attributeValue(GOAL_FEED_ATTRIBUTE_TYPE_NAME)
						.equals(GOAL_FEED_ATTRIBUTE_TYPE_VALUE)) {

			// attribute "id"
			mId = element.attributeValue(GOAL_FEED_ATTRIBUTE_ID_NAME);

			// <publishedDate>
			try {
				String dateStr = element
						.elementTextTrim(GOAL_FEED_TAG_PUBLISHED_DATE);
				if (dateStr != null) {
					// 2014-04-27 05:07:26 +0100
					mPublishedDate = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss Z", Locale.getDefault())
							.parse(dateStr);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}

			// <title>
			mTitle = element.elementTextTrim(GOAL_FEED_TAG_TITLE);

			// <description>
			mDescription = element.elementTextTrim(GOAL_FEED_TAG_DESCRIPTION);

			// <images>/<image>
			Node imageNode = element
					.selectSingleNode(GOAL_FEED_NODE_IMAGES_IMAGE);
			if (imageNode != null) {
				mImageUrl = imageNode.getText();
			}

			// <images>/<thumbnail>
			Node thumbnailNode = element
					.selectSingleNode(GOAL_FEED_NODE_IMAGES_THUMBNAIL);
			if (thumbnailNode != null) {
				mThumbnailUrl = thumbnailNode.getText();
			}

			// <urls>/<feedLink>
			Node feedLinkNode = element
					.selectSingleNode(GOAL_FEED_NODE_URLS_FEED_LINK);
			if (feedLinkNode != null) {
				mFeedLinkUrl = feedLinkNode.getText();
			}

			// <urls>/<webviewLink>
			Node webviewLinkNode = element
					.selectSingleNode(GOAL_FEED_NODE_URLS_WEBVIEW_LINK);
			if (webviewLinkNode != null) {
				mWebviewLinkUrl = webviewLinkNode.getText();
			}

			// <urls>/<websiteLink>
			Node websiteLinkNode = element
					.selectSingleNode(GOAL_FEED_NODE_URLS_WEBSITE_LINK);
			if (websiteLinkNode != null) {
				mWebsiteLinkUrl = websiteLinkNode.getText();
			}
		}
	}

	private void parseFfaFeedNews(Element element) {
		// element must not be null;
		// element name must be "item"
		if (element != null && element.getName().equals(FFA_FEED_ELEMENT_NAME)) {
			// <title>
			mTitle = element.elementTextTrim(FFA_FEED_TAG_TITLE);

			// <link>
			mWebsiteLinkUrl = element.elementText(FFA_FEED_TAG_LINK);

			// <description>
			mDescription = element.elementText(FFA_FEED_TAG_DESCRIPTION);

			// <pubDate>
			try {
				String dateStr = element.elementTextTrim(FFA_FEED_TAG_PUB_DATE);
				if (dateStr != null) {
					mPublishedDate = new SimpleDateFormat(
					// Mon, 14 Apr 2014 23:37:09 GMT
							"EEE, dd MMM yyyy HH:mm:ss Z", Locale.getDefault())
							.parse(dateStr);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

	public String getId() {
		return mId;
	}

	public Date getPublishedDate() {
		return mPublishedDate;
	}

	public String getTitle() {
		return mTitle;
	}

	public String getDescription() {
		return mDescription;
	}

	public String getImageUrl() {
		return mImageUrl;
	}

	public String getThumbnailUrl() {
		return mThumbnailUrl;
	}

	public String getFeedLinkUrl() {
		return mFeedLinkUrl;
	}

	public String getWebviewLinkUrl() {
		return mWebviewLinkUrl;
	}

	public String getWebsiteLinkUrl() {
		return mWebsiteLinkUrl;
	}

	public void setCategory(String category) {
		mCategory = category;
	}

	public String getCategory() {
		return mCategory;
	}

	@Override
	public boolean equals(Object news) {
		if (news instanceof News) {
			return mId.equals(((News) news).getId());
		} else {
			return false;
		}
	}
}
