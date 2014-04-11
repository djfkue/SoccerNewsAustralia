package com.luxiliu.soccernewsaustralia.model;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;

/**
 * The Page class represents the <resultsList> element in the feed content
 * 
 * @author Luxi Liu (luxi.liu@gmail.com)
 * 
 */
public class Page {
	private static final String NODE_ITEM = "results/resultsList/item";
	private static final String NODE_PAGINATION = "results/resultsList/pagination";
	private static final String ATTRIBUTE_CURRENT_PAGE_NAME = "currentPage";
	private static final String ATTRIBUTE_PREVIOUS_PAGE_NAME = "previousPage";
	private static final String ATTRIBUTE_NEXT_PAGE_NAME = "nextPage";

	private ArrayList<News> mNewsList;
	private int mCurrentPage;
	private boolean mHasPreviousPage;
	private boolean mHasNextPage;

	public Page(Document document) {
		mNewsList = new ArrayList<News>();

		if (document != null) {
			// Get root element from document
			Element rootElement = document.getRootElement();

			// Get all the item nodes
			@SuppressWarnings("unchecked")
			List<Element> itemElementList = (List<Element>) rootElement
					.selectNodes(NODE_ITEM);

			// Create News object and add into list
			for (Element itemElement : itemElementList) {
				News news = new News(itemElement);
				mNewsList.add(news);
			}

			// Get pagination
			Element paginationElement = (Element) rootElement
					.selectSingleNode(NODE_PAGINATION);
			mCurrentPage = Integer.valueOf(paginationElement
					.attributeValue(ATTRIBUTE_CURRENT_PAGE_NAME));
			mHasPreviousPage = Boolean.valueOf(paginationElement
					.attributeValue(ATTRIBUTE_PREVIOUS_PAGE_NAME));
			mHasNextPage = Boolean.valueOf(paginationElement
					.attributeValue(ATTRIBUTE_NEXT_PAGE_NAME));
		}
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
