package kdd.xinghuangxu.parse.html.news;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.html.dom.HTMLDocumentImpl;
import org.cyberneko.html.parsers.DOMFragmentParser;
import org.w3c.dom.DocumentFragment;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import kdd.xinghuangxu.parse.html.HtmlSource;
import kdd.xinghuangxu.parse.html.dataStruc.Outlink;
import kdd.xinghuangxu.parse.html.news.bbc.BbcDOMContentUtils;
import kdd.xinghuangxu.parse.html.tagsoup.DOMBuilder;

public class ParseHelper {

	//This configuration fields can be changed, but not now
	private String parserImpl = "neko";
	private String defaultCharEncoding = "windows-1252";
	private NewsDOMContentUtils utils;
	StringBuffer sb = new StringBuffer();

	URL url;
	DocumentFragment root;
	
	

	public ParseHelper(String url) throws MalformedURLException,IOException,Exception {

		this.url = new URL(url);
		HtmlSource fetcher = new HtmlSource(url);
		byte[] content = fetcher.getContent();
		InputSource input = new InputSource(new ByteArrayInputStream(content));
		this.root = parse(input);
		this.utils=new BbcDOMContentUtils();	
		
	}

	public URL getURL() {
		return url;
	}

	private DocumentFragment parse(InputSource input) throws Exception {
		if (parserImpl.equalsIgnoreCase("tagsoup"))
			return parseTagSoup(input);
		else
			return parseNeko(input);
	}

	private DocumentFragment parseTagSoup(InputSource input) throws Exception {
		HTMLDocumentImpl doc = new HTMLDocumentImpl();
		DocumentFragment frag = doc.createDocumentFragment();
		DOMBuilder builder = new DOMBuilder(doc, frag);
		org.ccil.cowan.tagsoup.Parser reader = new org.ccil.cowan.tagsoup.Parser();
		reader.setContentHandler(builder);
		reader.setFeature(org.ccil.cowan.tagsoup.Parser.ignoreBogonsFeature,
				true);
		reader.setFeature(org.ccil.cowan.tagsoup.Parser.bogonsEmptyFeature,
				false);
		reader.setProperty("http://xml.org/sax/properties/lexical-handler",
				builder);
		reader.parse(input);
		return frag;
	}

	private DocumentFragment parseNeko(InputSource input) throws Exception {
		DOMFragmentParser parser = new DOMFragmentParser();
		try {
			parser.setFeature(
					"http://cyberneko.org/html/features/augmentations", true);
			parser.setProperty(
					"http://cyberneko.org/html/properties/default-encoding",
					defaultCharEncoding);
			parser.setFeature(
					"http://cyberneko.org/html/features/scanner/ignore-specified-charset",
					true);
			parser.setFeature(
					"http://cyberneko.org/html/features/balance-tags/ignore-outside-content",
					false);
			parser.setFeature(
					"http://cyberneko.org/html/features/balance-tags/document-fragment",
					true);
			// parser.setFeature(
			// "http://cyberneko.org/html/features/report-errors",
			// LOG.isTraceEnabled());
		} catch (SAXException e) {
		}
		// convert Document to DocumentFragment
		HTMLDocumentImpl doc = new HTMLDocumentImpl();
		doc.setErrorChecking(false);
		DocumentFragment res = doc.createDocumentFragment();
		DocumentFragment frag = doc.createDocumentFragment();
		parser.parse(input, frag);
		res.appendChild(frag);

		try {
			while (true) {
				frag = doc.createDocumentFragment();
				parser.parse(input, frag);
				if (!frag.hasChildNodes())
					break;
				// if (LOG.isInfoEnabled()) {
				// LOG.info(" - new frag, " + frag.getChildNodes().getLength()
				// + " nodes.");
				// }
				res.appendChild(frag);
			}
		} catch (Exception e) {
			// LOG.error("Error: ", e);
		}
		;
		return res;
	}

	public DocumentFragment getDocumentFragment() {
		return root;
	}

	public NewsDOMContentUtils getNewDOMContentUtils() {
		return utils;
	}
	
	public String getTitle(){
		return utils.getNewsTitle(sb,root).replaceAll("&", "&amp;");
	}
	
	public String getDate(){
		return utils.getDate(root, url).replaceAll("&", "&amp;");
	}
	
	public String getBody(){
		return utils.getStoryBody(sb, root).replaceAll("&", "&amp;");
	}
	
	public Outlink[] getRelatedLinks(){
		return utils.getRelatedStoryLinks(root, url);
	}


}
