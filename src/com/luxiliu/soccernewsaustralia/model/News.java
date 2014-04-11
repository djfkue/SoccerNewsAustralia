package com.luxiliu.soccernewsaustralia.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.dom4j.Element;
import org.dom4j.Node;

/**
 * The News class represents <item> element in the feed content
 * 
 * @author Luxi Liu (luxi.liu@gmail.com)
 * 
 */
public class News implements Serializable {
	private static final long serialVersionUID = 2484234548662887150L;

	private static final String ELEMENT_NAME = "item";
	private static final String ATTRIBUTE_TYPE_NAME = "type";
	private static final String ATTRIBUTE_TYPE_VALUE = "news";
	private static final String ATTRIBUTE_ID_NAME = "id";
	private static final String TAG_PUBLISHED_DATE = "publishedDate";
	private static final String TAG_TITLE = "title";
	private static final String TAG_DESCRIPTION = "description";
	private static final String NODE_IMAGES_IMAGE = "images/image";
	private static final String NODE_IMAGES_THUMBNAIL = "images/thumbnail";
	private static final String NODE_URLS_FEED_LINK = "urls/feedLink";
	private static final String NODE_URLS_WEBVIEW_LINK = "urls/webviewLink";
	private static final String NODE_URLS_WEBSITE_LINK = "urls/websiteLink";

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

	public News(Element element) {
		// Element must not be null;
		// Element name must be "item"
		// Element attribute "type" must not be null
		// Element attribute "type" must be "news"
		if (element != null
				&& element.getName().equals(ELEMENT_NAME)
				&& element.attributeValue(ATTRIBUTE_TYPE_NAME) != null
				&& element.attributeValue(ATTRIBUTE_TYPE_NAME).equals(
						ATTRIBUTE_TYPE_VALUE)) {

			// Attribute "id"
			mId = element.attributeValue(ATTRIBUTE_ID_NAME);

			// publishedDate
			try {
				String dateStr = element.elementTextTrim(TAG_PUBLISHED_DATE);
				if (dateStr != null) {
					mPublishedDate = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss Z", Locale.getDefault())
							.parse(dateStr);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}

			// title
			mTitle = element.elementTextTrim(TAG_TITLE);

			// description
			mDescription = element.elementTextTrim(TAG_DESCRIPTION);

			// images/image
			Node imageNode = element.selectSingleNode(NODE_IMAGES_IMAGE);
			if (imageNode != null) {
				mImageUrl = imageNode.getText();
			}

			// images/thumbnail
			Node thumbnailNode = element
					.selectSingleNode(NODE_IMAGES_THUMBNAIL);
			if (thumbnailNode != null) {
				mThumbnailUrl = thumbnailNode.getText();
			}

			// urls/feedLink
			Node feedLinkNode = element.selectSingleNode(NODE_URLS_FEED_LINK);
			if (feedLinkNode != null) {
				mFeedLinkUrl = feedLinkNode.getText();
			}

			// urls/webviewLink
			Node webviewLinkNode = element
					.selectSingleNode(NODE_URLS_WEBVIEW_LINK);
			if (webviewLinkNode != null) {
				mWebviewLinkUrl = webviewLinkNode.getText();
			}

			// urls/websiteLink
			Node websiteLinkNode = element
					.selectSingleNode(NODE_URLS_WEBSITE_LINK);
			if (websiteLinkNode != null) {
				mWebsiteLinkUrl = websiteLinkNode.getText();
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
