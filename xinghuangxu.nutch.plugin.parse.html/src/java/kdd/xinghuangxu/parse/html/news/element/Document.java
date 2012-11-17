package kdd.xinghuangxu.parse.html.news.element;

import java.net.URL;

import kdd.xinghuangxu.parse.html.dataStruc.Outlink;
import kdd.xinghuangxu.parse.html.news.NewsDOMContentUtils;
import kdd.xinghuangxu.parse.html.news.ParseHelper;
import kdd.xinghuangxu.parse.html.news.exception.NewsParsingException;

import org.w3c.dom.DocumentFragment;

public class Document implements NewsElement {

	
	@Override
	public StringBuilder parse(ParseHelper helper) throws NewsParsingException {

		StringBuilder document = new StringBuilder();
		URL url = helper.getURL();
		DocumentFragment root = helper.getDocumentFragment();
		NewsDOMContentUtils utils = helper.getNewDOMContentUtils();
		document.append(createElement("id", url.toString()
				.replace("&", "&amp;")));

		StringBuffer sb = new StringBuffer();

		// get title
		String title = utils.getNewsTitle(sb, root);
		document.append(createElement("title", title.replaceAll("&", "&amp;")));

		// get date
		String date = utils.getDate(root, url);
		document.append(createElement("date", date.replaceAll("&", "&amp;")));

		// get body
		sb.setLength(0);
		String body = utils.getStoryBody(sb, root);
		document.append(createElement("body", body.replaceAll("&", "&amp;")));

		// Get related-story
		Outlink[] links = utils.getRelatedStoryLinks(root, url);
		for (Outlink outlink : links) {
			if ("http://www.bbc.co.uk/news/".equalsIgnoreCase(outlink
					.getToUrl().substring(0, 26)))
				document.append(createElement("related", outlink.getToUrl()
						.replaceAll("&", "&amp;")));
		}
		return document;

	}

	private StringBuilder createElement(String name, String value) {
		StringBuilder sb = new StringBuilder();
		sb.append("<" + name + ">");
		sb.append(value);
		sb.append("</" + name + ">");
		return sb;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return null;
	}




}
