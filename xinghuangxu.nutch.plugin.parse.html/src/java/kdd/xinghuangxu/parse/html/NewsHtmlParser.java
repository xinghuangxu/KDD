package kdd.xinghuangxu.parse.html;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.text.html.HTMLWriter;

import kdd.xinghuangxu.parse.html.dataStruc.Content;
import kdd.xinghuangxu.parse.html.dataStruc.HTMLMetaTags;
import kdd.xinghuangxu.parse.html.dataStruc.Metadata;
import kdd.xinghuangxu.parse.html.dataStruc.Outlink;
import kdd.xinghuangxu.parse.html.dataStruc.Parse;
import kdd.xinghuangxu.parse.html.dataStruc.ParseData;
import kdd.xinghuangxu.parse.html.dataStruc.ParseImpl;
import kdd.xinghuangxu.parse.html.dataStruc.ParseResult;
import kdd.xinghuangxu.parse.html.dataStruc.ParseStatus;
import kdd.xinghuangxu.parse.html.news.bbc.BbcDOMContentUtils;
import kdd.xinghuangxu.parse.html.tagsoup.DOMBuilder;
import kdd.xinghuangxu.parse.html.util.DOMContentUtils;
import kdd.xinghuangxu.parse.html.util.NewsDOMContentUtils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.html.dom.HTMLDocumentImpl;
import org.apache.nutch.parse.Parser;
import org.cyberneko.html.parsers.DOMFragmentParser;
import org.w3c.dom.DocumentFragment;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class NewsHtmlParser {

	public static final String DIR_NAME = "DB";

	public static final String CORPUS_FILE_NAME = "corpus_db.xml";
	public static final String LINK_FILE_NAME = "link_db.xml";

	private NewsDOMContentUtils utils;

	private String parserImpl;
	private String defaultCharEncoding;

	public NewsHtmlParser() {
		setConf();
	}

	public void setConf() {
		// dafault value is neko, can also be tagsoup
		parserImpl = "neko";
		defaultCharEncoding = "windows-1252";

		// Not necessarily BBC News, will add a configuration file to design
		// what is the target news provider
		utils = new BbcDOMContentUtils();
	}

	public String getPath() {
		String current;
		try {
			current = new File(".").getCanonicalPath();
		} catch (IOException e) {
			return "";
		}
		return current + DIR_NAME + "\\" + CORPUS_FILE_NAME;
	}

	public static void main(String[] args) throws Exception {

		NewsHtmlParser parser = new NewsHtmlParser();
		Corpus corpus = parser.ParseAllNews(args);
		parser.WriteCorpus(corpus);
		return;
	}

	public void WriteCorpus(Corpus corpus) {
		corpus.Write();
	}

	public Corpus ParseAllNews(String[] urls) {
		Corpus corpus = new Corpus();
		for (int index = 0; index < urls.length; index++) {
			try {
				System.out.println(new URL(urls[index]).getHost());
				corpus.add(new URL(urls[index]).getPath(), ParseNews(new URL(
						urls[index])));
				// System.out.println((index+1)+". "+urls[index]);
			} catch (Exception e) {

			}
		}
		return corpus;
	}

	public StringBuilder ParseNews(URL url) throws Exception {

		StringBuilder document = new StringBuilder();
		document.append(createElement("id", url.toString()
				.replace("&", "&amp;")));

		HtmlSource mySource = new HtmlSource(url);
		// mySource.WriteOutSource("Source.html");
		byte[] content = mySource.getContent();

		// Build the xml tree by calling parse() who use nekoHTML
		InputSource input = new InputSource(new ByteArrayInputStream(content));
		DocumentFragment root = parse(input);

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

}
