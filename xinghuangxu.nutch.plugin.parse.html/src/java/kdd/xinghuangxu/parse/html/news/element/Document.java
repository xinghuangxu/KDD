package kdd.xinghuangxu.parse.html.news.element;

import java.net.URL;
import java.util.ArrayList;

import kdd.xinghuangxu.parse.html.dataStruc.Outlink;
import kdd.xinghuangxu.parse.html.news.NewsDOMContentUtils;
import kdd.xinghuangxu.parse.html.news.ParseHelper;
import kdd.xinghuangxu.parse.html.news.exception.NewsParsingException;

import org.w3c.dom.DocumentFragment;

/**
 * 
 * @author xinghuang
 *
 */
public class Document extends CompositeElement {

	public Document() {
		name = "document";
		elements = new ArrayList<NewsElement>();

	}

	public void parse(ParseHelper helper) throws NewsParsingException {

		elements.add(new IdElement());
		elements.add(new TitleElement());
		elements.add(new DateElement());
		elements.add(new BodyElement());
		Outlink[] links = helper.getRelatedLinks();
		for (Outlink outlink : links) {
			if (outlink.getToUrl().length() > 26
					&& "http://www.bbc.co.uk/news/".equalsIgnoreCase(outlink
							.getToUrl().substring(0, 26))) {
				String url = outlink.getToUrl().replaceAll("&", "&amp;");
				elements.add(new RelatedElement(url));
				helper.addLinkDB(url);
			}
		}

		super.parse(helper);

		// StringBuilder document = new StringBuilder();
		// URL url = helper.getURL();
		// DocumentFragment root = helper.getDocumentFragment();
		// NewsDOMContentUtils utils = helper.getNewDOMContentUtils();
		//
		// document.append(createElement("id", url.toString()
		// .replace("&", "&amp;")));
		//
		// StringBuffer sb = new StringBuffer();
		//
		// // get title
		// String title = utils.getNewsTitle(sb, root);
		// document.append(createElement("title", title.replaceAll("&",
		// "&amp;")));
		//
		// // get date
		// String date = utils.getDate(root, url);
		// document.append(createElement("date", date.replaceAll("&",
		// "&amp;")));
		//
		// // get body
		// sb.setLength(0);
		// String body = utils.getStoryBody(sb, root);
		// document.append(createElement("body", body.replaceAll("&",
		// "&amp;")));
		//
		// // Get related-story
		// Outlink[] links = utils.getRelatedStoryLinks(root, url);
		// for (Outlink outlink : links) {
		// if ("http://www.bbc.co.uk/news/".equalsIgnoreCase(outlink
		// .getToUrl().substring(0, 26)))
		// document.append(createElement("related", outlink.getToUrl()
		// .replaceAll("&", "&amp;")));
		// }
		// return document;

	}

	public void cleanUpRelatedLinks(Corpus corpus) {
		for (int i = 4; i < elements.size(); i++) {
			if ((!corpus.containDocument(elements.get(i).getValue()) || elements
					.get(i).getValue()
					.equalsIgnoreCase(elements.get(0).getValue())))
				elements.get(i).setValue("");
		}
	}

	// @Override
	// public String toString() {
	// StringBuilder sb=new StringBuilder();
	// sb.append("<"+name+">");
	// sb.append("<id>"+key+"</id>");
	// sb.append( super.toString());
	// sb.append("</"+name+">");
	// return sb.toString();
	// }

}
