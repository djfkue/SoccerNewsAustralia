package com.luxiliu.soccernewsaustralia.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

/**
 * The Article class represents the <article> element in the feed content
 * 
 * @author Luxi Liu (luxi.liu@gmail.com)
 * 
 */
public class Article {
	private static final String NODE_ARTICLE = "results/resultList//article";
	private static final String ELEMENT_NAME = "article";
	private static final String ATTRIBUTE_ID_NAME = "id";
	private static final String TAG_TITLE = "title";
	private static final String TAG_DESCRIPTION = "description";
	private static final String TAG_PUBLISHED_DATE = "publishedDate";
	private static final String TAG_AUTHOR = "author";
	private static final String TAG_ARTICLE_TEXT = "articleText";
	private static final String NODE_IMAGES_IMAGE = "images//image";
	private static final String NODE_IMAGES_THUMBNAIL = "images//thumbnail";
	private static final String NODE_URLS_MOBILE_SITE = "urls/mobileSite";
	private static final String NODE_URLS_WEBSITE_LINK = "urls/websiteLink";

	private String mId;
	private String mTitle;
	private String mDescription;
	private Date mPublishedDate;
	private String mAuthor;
	private String mArticleText;
	private String mImageUrl;
	private String mThumbnailUrl;
	private String mMobileSiteUrl;
	private String mWebsiteLinkUrl;

	public Article(Document document) {
		if (document != null) {
			// Get root element from the document
			Element rootElement = document.getRootElement();

			// Get article element
			Element element = (Element) rootElement
					.selectSingleNode(NODE_ARTICLE);

			// Element must not be null, and its name must be "article"
			if (element != null && element.getName().equals(ELEMENT_NAME)) {
				// Attribute "id"
				mId = element.attributeValue(ATTRIBUTE_ID_NAME);

				// title
				mTitle = element.elementTextTrim(TAG_TITLE);

				// description
				mDescription = element.elementTextTrim(TAG_DESCRIPTION);

				// publishedDate
				try {
					String dateStr = element
							.elementTextTrim(TAG_PUBLISHED_DATE);
					if (dateStr != null) {
						mPublishedDate = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss Z", Locale.getDefault())
								.parse(dateStr);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}

				// author
				mAuthor = element.elementTextTrim(TAG_AUTHOR);

				// articleText
				mArticleText = element.elementTextTrim(TAG_ARTICLE_TEXT);

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

				// urls/websiteLink
				Node mobileSiteNode = element
						.selectSingleNode(NODE_URLS_MOBILE_SITE);
				if (mobileSiteNode != null) {
					mMobileSiteUrl = mobileSiteNode.getText();
				}

				// urls/websiteLink
				Node websiteLinkNode = element
						.selectSingleNode(NODE_URLS_WEBSITE_LINK);
				if (websiteLinkNode != null) {
					mWebsiteLinkUrl = websiteLinkNode.getText();
				}
			}
		}
	}

	public String getId() {
		return mId;
	}

	public String getTitle() {
		return mTitle;
	}

	public String getDescription() {
		return mDescription;
	}

	public Date getPublishedDate() {
		return mPublishedDate;
	}

	public String getAuthor() {
		return mAuthor;
	}

	public String getArticleText() {
		return mArticleText;
	}

	public String getImageUrl() {
		return mImageUrl;
	}

	public String getThumbnailUrl() {
		return mThumbnailUrl;
	}

	public String getMobileSiteUrl() {
		return mMobileSiteUrl;
	}

	public String getWebsiteLinkUrl() {
		return mWebsiteLinkUrl;
	}
}
