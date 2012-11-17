package kdd.xinghuangxu.parse.html.nutch.plugin;



import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import kdd.xinghuangxu.parse.html.HtmlSource;
import kdd.xinghuangxu.parse.html.NewsHtmlParser;
import kdd.xinghuangxu.parse.html.news.element.Corpus;

import org.apache.hadoop.conf.Configuration;
import org.apache.nutch.parse.ParseResult;
import org.apache.nutch.parse.Parser;
import org.apache.nutch.protocol.Content;


public class NutchNewsParser implements Parser {

	private static final String outputFileName="corpus.xml";
	
	private static Corpus corpus=new Corpus();
	private static DataOutputStream out;
	
	@Override
	public Configuration getConf() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setConf(Configuration conf) {
//		corpus=new Corpus();
//		File file= new File(outputFileName);
//		try {
//			out = new DataOutputStream(new FileOutputStream(file));
//		} catch (FileNotFoundException e) {
//			out=null;
//		}
	}

	@Override
	public ParseResult getParse(Content content) {

		URL url = null;
		//boolean malFormedURL=false;
		try {
			url = new URL(content.getUrl());
		} catch (MalformedURLException e) {
			return null;
		}
		
		StringBuilder xml=new StringBuilder();
		try {
			xml = new NewsHtmlParser().ParseNews(url);
			//corpus.append(xml);
			
		} catch (Exception e) {
			
		}
		return null;

		// HTMLMetaTags metaTags = new HTMLMetaTags();
		//
		// URL base;
		// try {
		// base = new URL(content.getBaseUrl());
		// } catch (MalformedURLException e) {
		// return null;
		// }
		//
		// String text = "";
		// String title = "";
		// Outlink[] outlinks = new Outlink[0];
		// Metadata metadata = new Metadata();
		//
		// // parse the content
		// DocumentFragment root;
		// // try {
		// byte[] contentInOctets = content.getContent();
		// InputSource input = new InputSource(new ByteArrayInputStream(
		// contentInOctets));
		//
		// // EncodingDetector detector = new EncodingDetector(conf);
		// // detector.autoDetectClues(content, true);
		// // detector.addClue(sniffCharacterEncoding(contentInOctets),
		// "sniffed");
		// // String encoding = detector.guessEncoding(content,
		// // defaultCharEncoding);
		//
		// // metadata.set(Metadata.ORIGINAL_CHAR_ENCODING, encoding);
		// // metadata.set(Metadata.CHAR_ENCODING_FOR_CONVERSION, encoding);
		//
		// // input.setEncoding(encoding);
		// // if (LOG.isTraceEnabled()) { LOG.trace("Parsing..."); }
		// root = parse(input);
		// //Write out the root content to see what is in it.
		// //System.out.println(root.getTextContent());
		// // } catch (IOException e) {
		// // return new ParseStatus(e).getEmptyParseResult(content.getUrl(),
		// // getConf());
		// // } catch (DOMException e) {
		// // return new ParseStatus(e).getEmptyParseResult(content.getUrl(),
		// // getConf());
		// // } catch (SAXException e) {
		// // return new ParseStatus(e).getEmptyParseResult(content.getUrl(),
		// // getConf());
		// // } catch (Exception e) {
		// // LOG.error("Error: ", e);
		// // return new ParseStatus(e).getEmptyParseResult(content.getUrl(),
		// // getConf());
		// // }
		// // StringBuffer newsSb=new StringBuffer();
		// // BbcDOMContentUtils myNews=new BbcDOMContentUtils();
		// // System.out.println(myNews.getStoryBody(newsSb, root));
		// // get meta directives
		// HTMLMetaProcessor.getMetaTags(metaTags, root, base);
		//
		// // check meta directives
		// if (!metaTags.getNoIndex()) { // okay to index
		// StringBuffer sb = new StringBuffer();
		// utils.getText(sb, root); // extract text
		// text = sb.toString();
		// sb.setLength(0);
		// // if (LOG.isTraceEnabled()) { LOG.trace("Getting title..."); }
		// utils.getTitle(sb, root); // extract title
		// title = sb.toString().trim();
		// }
		//
		// if (!metaTags.getNoFollow()) { // okay to follow links
		// ArrayList<Outlink> l = new ArrayList<Outlink>(); // extract outlinks
		// URL baseTag = utils.getBase(root);
		// // if (LOG.isTraceEnabled()) { LOG.trace("Getting links..."); }
		// utils.getOutlinks(baseTag != null ? baseTag : base, l, root);
		// outlinks = l.toArray(new Outlink[l.size()]);
		// // if (LOG.isTraceEnabled()) {
		// //
		// LOG.trace("found "+outlinks.length+" outlinks in "+content.getUrl());
		// // }
		// }
		//
		// ParseStatus status = new ParseStatus(ParseStatus.SUCCESS);
		// if (metaTags.getRefresh()) {
		// status.setMinorCode(ParseStatus.SUCCESS_REDIRECT);
		// status.setArgs(new String[] { metaTags.getRefreshHref().toString(),
		// Integer.toString(metaTags.getRefreshTime()) });
		// }
		// ParseData parseData = new ParseData(status, title, outlinks,
		// content.getMetadata(), metadata);
		// // ParseResult parseResult = ParseResult.createParseResult(
		// // content.getUrl(), new ParseImpl(text, parseData));
		// return new ParseImpl(text, parseData);
		// // return parseResult;
		//
		// // run filters on parse
		// /*
		// * ParseResult filteredParse = this.htmlParseFilters.filter(content,
		// * parseResult, metaTags, root); if (metaTags.getNoCache()) { // not
		// * okay to cache for (Map.Entry<org.apache.hadoop.io.Text, Parse>
		// entry
		// * : filteredParse)
		// *
		// entry.getValue().getData().getParseMeta().set(Nutch.CACHING_FORBIDDEN_KEY
		// * , cachingPolicy); } return filteredParse;
		// */

	}

}
