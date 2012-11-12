package kdd.xinghuangxu.parse.html;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.net.MalformedURLException;
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
import kdd.xinghuangxu.parse.html.util.DOMContentUtils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.html.dom.HTMLDocumentImpl;
import org.cyberneko.html.parsers.DOMFragmentParser;
import org.w3c.dom.DocumentFragment;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class HTMLParser {
	
	public HTMLParser(){
		setConf();
	}
	
	 ///TODO Set Up Input parameter What parameter should I input?"
	  public static void main(String[] args) throws Exception {
		  
//		  try{
//			  //create file
//			  FileWriter fstream=new FileWriter("out.txt");
//			  BufferedWriter out=new BufferedWriter(fstream);
//			  out.write("Hellow Java");
//			  out.close();
//		  }
//		  catch(Exception e)
//		  {
//			  System.err.print("Error: "+e.getMessage());
//		  }
//		  
		  
	    //LOG.setLevel(Level.FINE);
	    String url = args[0];
	    byte[] bytes=HtmlSource.getUrlSourceBytes(url);
	    //String url = "file:"+name;
	    //File file = new File(name);
	    //byte[] bytes = new byte[(int)file.length()];
	    //DataInputStream in = new DataInputStream(new FileInputStream(file));
	    //in.readFully(bytes);
	    //Configuration conf = NutchConfiguration.create();
	   // HtmlParser parser = new HtmlParser();
	    //parser.setConf(conf);
	    //HTMLParser htmlParser=new HTMLParser();
	    ParseResult parseResult=new HTMLParser().getParse(new Content(url,url,bytes,"text/html",new Metadata()));
	    Parse parse=parseResult.get(url);
	   // Parse parse = parser.getParse(
	            //new Content(url, url, bytes, "text/html", new Metadata())).get(url);
	    System.out.println("data: "+parse.getData());

	    System.out.println("text: "+parse.getText());
	    
	  }
	

	private DOMContentUtils utils;

	public ParseResult getParse(Content content) throws Exception {

		HTMLMetaTags metaTags = new HTMLMetaTags();

		URL base;
		try {
			base = new URL(content.getBaseUrl());
		} catch (MalformedURLException e) {
			return new ParseStatus(e).getEmptyParseResult(content.getUrl());
		}

		String text = "";
		String title = "";
		Outlink[] outlinks = new Outlink[0];
		Metadata metadata = new Metadata();

		// parse the content
		DocumentFragment root;
		// try {
		byte[] contentInOctets = content.getContent();
		InputSource input = new InputSource(new ByteArrayInputStream(
				contentInOctets));

		// EncodingDetector detector = new EncodingDetector(conf);
		// detector.autoDetectClues(content, true);
		// detector.addClue(sniffCharacterEncoding(contentInOctets), "sniffed");
		// String encoding = detector.guessEncoding(content,
		// defaultCharEncoding);

		// metadata.set(Metadata.ORIGINAL_CHAR_ENCODING, encoding);
		// metadata.set(Metadata.CHAR_ENCODING_FOR_CONVERSION, encoding);

		// input.setEncoding(encoding);
		// if (LOG.isTraceEnabled()) { LOG.trace("Parsing..."); }
		root = parse(input);
		// } catch (IOException e) {
		// return new ParseStatus(e).getEmptyParseResult(content.getUrl(),
		// getConf());
		// } catch (DOMException e) {
		// return new ParseStatus(e).getEmptyParseResult(content.getUrl(),
		// getConf());
		// } catch (SAXException e) {
		// return new ParseStatus(e).getEmptyParseResult(content.getUrl(),
		// getConf());
		// } catch (Exception e) {
		// LOG.error("Error: ", e);
		// return new ParseStatus(e).getEmptyParseResult(content.getUrl(),
		// getConf());
		// }

		// get meta directives
		HTMLMetaProcessor.getMetaTags(metaTags, root, base);

		// check meta directives
		if (!metaTags.getNoIndex()) { // okay to index
			StringBuffer sb = new StringBuffer();
			utils.getText(sb, root); // extract text
			text = sb.toString();
			sb.setLength(0);
			// if (LOG.isTraceEnabled()) { LOG.trace("Getting title..."); }
			utils.getTitle(sb, root); // extract title
			title = sb.toString().trim();
		}

		if (!metaTags.getNoFollow()) { // okay to follow links
			ArrayList<Outlink> l = new ArrayList<Outlink>(); // extract outlinks
			URL baseTag = utils.getBase(root);
			// if (LOG.isTraceEnabled()) { LOG.trace("Getting links..."); }
			utils.getOutlinks(baseTag != null ? baseTag : base, l, root);
			outlinks = l.toArray(new Outlink[l.size()]);
			// if (LOG.isTraceEnabled()) {
			// LOG.trace("found "+outlinks.length+" outlinks in "+content.getUrl());
			// }
		}

		ParseStatus status = new ParseStatus(ParseStatus.SUCCESS);
		if (metaTags.getRefresh()) {
			status.setMinorCode(ParseStatus.SUCCESS_REDIRECT);
			status.setArgs(new String[] { metaTags.getRefreshHref().toString(),
					Integer.toString(metaTags.getRefreshTime()) });
		}
		ParseData parseData = new ParseData(status, title, outlinks,
				content.getMetadata(), metadata);
		ParseResult parseResult = ParseResult.createParseResult(
				content.getUrl(), new ParseImpl(text, parseData));

		return parseResult;

		// run filters on parse
		/*
		 * ParseResult filteredParse = this.htmlParseFilters.filter(content,
		 * parseResult, metaTags, root); if (metaTags.getNoCache()) { // not
		 * okay to cache for (Map.Entry<org.apache.hadoop.io.Text, Parse> entry
		 * : filteredParse)
		 * entry.getValue().getData().getParseMeta().set(Nutch.CACHING_FORBIDDEN_KEY
		 * , cachingPolicy); } return filteredParse;
		 */

	}
	private String parserImpl;
	private String defaultCharEncoding;
	
	public void setConf(){
		parserImpl="neko";
		defaultCharEncoding="windows-1252";
		utils=new DOMContentUtils();
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
//			parser.setFeature(
//					"http://cyberneko.org/html/features/report-errors",
//					LOG.isTraceEnabled());
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
//				if (LOG.isInfoEnabled()) {
//					LOG.info(" - new frag, " + frag.getChildNodes().getLength()
//							+ " nodes.");
//				}
				res.appendChild(frag);
			}
		} catch (Exception e) {
//			LOG.error("Error: ", e);
		}
		;
		return res;
	}

}
