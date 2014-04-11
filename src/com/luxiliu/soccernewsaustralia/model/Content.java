package com.luxiliu.soccernewsaustralia.model;

import java.io.Serializable;

import org.dom4j.Document;

/**
 * The Content class represents a XML feed content retrieved from server. The
 * content could be transformed into an Article or a Page.
 * 
 * @author Luxi Liu (luxi.liu@gmail.com)
 * 
 */
public class Content implements Serializable {
	private static final long serialVersionUID = 961250637847274483L;

	private Document mDocument;

	public Content(Document document) {
		mDocument = document;
	}

	public Document getDocument() {
		return mDocument;
	}
}
