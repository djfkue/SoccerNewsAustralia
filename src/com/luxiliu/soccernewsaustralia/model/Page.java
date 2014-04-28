package com.luxiliu.soccernewsaustralia.model;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;

import com.luxiliu.soccernewsaustralia.model.Feed.Type;

/**
 * The Page class represents the news list content in the goal.com feed and FFA
 * feed
 * 
 * @author Luxi Liu (luxi.liu@gmail.com)
 * 
 */
public class Page {
	private static final String GOAL_FEED_ROOT_ELEMENT_NAME = "data";
	private static final String GOAL_FEED_NODE_ITEM = "results/resultsList/item";
	private static final String GOAL_FEED_NODE_PAGINATION = "results/resultsList/pagination";
	private static final String GOAL_FEED_ATTRIBUTE_CURRENT_PAGE_NAME = "currentPage";
	private static final String GOAL_FEED_ATTRIBUTE_PREVIOUS_PAGE_NAME = "previousPage";
	private static final String GOAL_FEED_ATTRIBUTE_NEXT_PAGE_NAME = "nextPage";

	private static final String FFA_FEED_ROOT_ELEMENT_NAME = "rss";
	private static final String FFA_FEED_NODE_ITEM = "channel/item";

	private Type mType;
	private ArrayList<News> mNewsList;
	private int mCurrentPage;
	private boolean mHasPreviousPage;
	private boolean mHasNextPage;

	public Page(Document document) {
		mType = Type.InvalidFeed;
		mNewsList = new ArrayList<News>();

		if (document != null) {
			// get root element from document
			Element rootElement = document.getRootElement();

			if (rootElement.getName().equals(GOAL_FEED_ROOT_ELEMENT_NAME)) {
				// parse goal feed content
				mType = Type.GoalFeed;
				parseGoalFeedPage(rootElement);
			} else if (rootElement.getName().equals(FFA_FEED_ROOT_ELEMENT_NAME)) {
				// parse FFA feed content
				mType = Type.FfaFeed;
				parseFfaFeedPage(rootElement);
			}
		}
	}

	private void parseGoalFeedPage(Element rootElement) {
		// get all the item nodes
		@SuppressWarnings("unchecked")
		List<Element> itemElementList = (List<Element>) rootElement
				.selectNodes(GOAL_FEED_NODE_ITEM);

		// create News object and add into list
		for (Element itemElement : itemElementList) {
			News news = new News(itemElement, mType);
			mNewsList.add(news);
		}

		// get pagination
		Element paginationElement = (Element) rootElement
				.selectSingleNode(GOAL_FEED_NODE_PAGINATION);
		mCurrentPage = Integer.valueOf(paginationElement
				.attributeValue(GOAL_FEED_ATTRIBUTE_CURRENT_PAGE_NAME));
		mHasPreviousPage = Boolean.valueOf(paginationElement
				.attributeValue(GOAL_FEED_ATTRIBUTE_PREVIOUS_PAGE_NAME));
		mHasNextPage = Boolean.valueOf(paginationElement
				.attributeValue(GOAL_FEED_ATTRIBUTE_NEXT_PAGE_NAME));
	}

	private void parseFfaFeedPage(Element rootElement) {
		// get all the item nodes
		@SuppressWarnings("unchecked")
		List<Element> itemElementList = (List<Element>) rootElement
				.selectNodes(FFA_FEED_NODE_ITEM);

		// create News object and add into list
		for (Element itemElement : itemElementList) {
			News news = new News(itemElement, mType);
			mNewsList.add(news);
		}

		// FFA feed has only one page
		mCurrentPage = 0;
		mHasPreviousPage = false;
		mHasNextPage = false;
	}

	public Type getType() {
		return mType;
	}

	public int getCurrentPage() {
		return mCurrentPage;
	}

	public boolean hasPreviousPage() {
		return mHasPreviousPage;
	}

	public boolean hasNextPage() {
		return mHasNextPage;
	}

	public ArrayList<News> getNewsList() {
		return new ArrayList<News>(mNewsList);
	}
}
